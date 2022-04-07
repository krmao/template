/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.smart.library.map.clusterutil.baidu.clustering.algo

import com.baidu.mapapi.model.LatLng
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem
import com.smart.library.map.clusterutil.baidu.projection.Bounds
import com.smart.library.map.clusterutil.baidu.projection.Point
import com.smart.library.map.clusterutil.baidu.projection.SphericalMercatorProjection
import com.smart.library.map.clusterutil.baidu.quadtree.PointQuadTree
import com.smart.library.map.clusterutil.baidu.util.STClusterUtil.log
import java.util.*

/**
 * 无等级的聚类算法
 *
 * A simple clustering algorithm with O(nlog n) performance. Resulting clusters are not
 * hierarchical.
 *
 *
 * High level algorithm:<br></br>
 * 1. Iterate over items in the order they were added (candidate clusters).<br></br>
 * 2. Create a cluster with the center of the item. <br></br>
 * 3. Add all items that are within a certain distance to the cluster. <br></br>
 * 4. Move any items out of an existing cluster if they are closer to another cluster. <br></br>
 * 5. Remove those items from the list of candidate clusters.
 *
 *
 * Clusters have the center of the first element (not the centroid of the items within it).
 */
@Suppress("ReplaceJavaStaticMethodWithKotlinAnalog", "unused")
class STNonHierarchicalDistanceBasedAlgorithm<T : ClusterItem>() : STAlgorithm<T> {

    private val quadItems: MutableCollection<QuadItem<T>> = ArrayList()                                   // Any modifications should be synchronized on mQuadTree.
    private val quadTree = PointQuadTree<QuadItem<T>>(0.0, 1.0, 0.0, 1.0)       // Any modifications should be synchronized on mQuadTree.

    override fun addItem(item: T) {
        val quadItem = QuadItem(item)
        synchronized(quadTree) {
            quadItems.add(quadItem)
            quadTree.add(quadItem)
        }
    }

    override fun addItems(items: Collection<T>) {
        for (item in items) {
            addItem(item)
        }
    }

    override fun clearItems() {
        synchronized(quadTree) {
            quadItems.clear()
            quadTree.clear()
        }
    }

    override fun removeItem(item: T) {
        // TODO: delegate QuadItem#hashCode and QuadItem#equals to its item.
        throw UnsupportedOperationException("STNonHierarchicalDistanceBasedAlgorithm.remove not implemented")
    }

    /**
     * cluster算法核心
     *
     * @param zoom map的级别
     * @return
     */
    override fun getClusters(zoom: Double): Set<Cluster<T>> {
        val zoomSpecificSpan = MAX_DISTANCE_AT_ZOOM / Math.pow(2.0, zoom) / 256 // 根据 zoom 计算每一个 marker 所占的范围跨度(正方形边宽)
        val results: MutableSet<Cluster<T>> = HashSet()                         // 最终返回结果

        val cacheVisitedCandidates: MutableSet<QuadItem<T>> = HashSet()                     // 临时缓存, 标记该 item 是否已经被访问过
        val cacheItemToClusterDistanceMap: MutableMap<QuadItem<T>, Double> = HashMap()      // 临时缓存, 保存该点距离簇的距离
        val cacheItemToClusterMap: MutableMap<QuadItem<T>, STStaticCluster<T>> = HashMap()  // 临时缓存, 保存该点指向的簇

        synchronized(quadTree) {
            log("getClusters zoom=" + zoom + ", zoomSpecificSpan=" + zoomSpecificSpan + ", mQuadTree.bounds=" + quadTree.mBounds)

            // 遍历每一个候选者
            for (candidate in quadItems) {
                //region 如果该候选者已被标记访问过, 忽略(遍历过或者搜索过)
                if (cacheVisitedCandidates.contains(candidate)) {
                    // Candidate is already part of another cluster.
                    continue
                }
                //endregion

                //region 以该候选者为中心根据 zoom 建立范围区域
                val searchBounds = createBoundsFromSpan(candidate.point, zoomSpecificSpan)
                log("-------- searchBounds=$searchBounds")
                //endregion

                //region 根据该候选者范围搜索区域包含的戳点
                val searchResultClusterItems: Collection<QuadItem<T>> = quadTree.search(searchBounds)
                //endregion

                //region 如果只查询到一个戳点, 不具备建立新簇的条件, 作为独立单点存在, 添加到返回结果中
                if (searchResultClusterItems.size == 1) {
                    // Only the current marker is in range. Just add the single item to the results.
                    candidate.setSearchBounds(searchBounds)         // 设置戳点的区域范围
                    results.add(candidate)                          // 添加到返回结果中
                    cacheVisitedCandidates.add(candidate)           // 标记戳点已被访问过
                    cacheItemToClusterDistanceMap[candidate] = 0.0  // 因为只有一个点, 所以该点距离簇的距离 为 0
                    continue
                }
                //endregion

                //region 生成新的簇, 以该候选者为中心建立簇, 设置该簇包含的戳点, 设置该簇的有效范围
                val newCluster = STStaticCluster<T>(candidate.clusterItem.position)
                newCluster.searchBounds = searchBounds // 设置簇的区域范围
                results.add(newCluster)                // 添加该点到返回结果
                for (searchClusterItem in searchResultClusterItems) {
                    val existingItemToClusterDistance = cacheItemToClusterDistanceMap[searchClusterItem]   // 缓存中戳点距离某个簇的距离
                    val distance = distanceSquared(searchClusterItem.point, candidate.point)               // 计算戳点到新簇的距离
                    if (existingItemToClusterDistance != null) {
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingItemToClusterDistance < distance) {                                    // 如果该戳点距离别的簇更近, 则不添加到新簇, 依然保留的原来的簇
                            continue
                        }
                        // Move item to the closer cluster.
                        cacheItemToClusterMap[searchClusterItem]?.remove(searchClusterItem.clusterItem)    // 如果该戳点距离新簇更近, 则从原来的簇中删除该戳点
                    }
                    newCluster.add(searchClusterItem.clusterItem)                   // 添加戳点到新簇
                    cacheItemToClusterMap[searchClusterItem] = newCluster           // 缓存戳点指向新簇
                    cacheItemToClusterDistanceMap[searchClusterItem] = distance     // 缓存戳点与新簇的距离
                }
                //endregion
                cacheVisitedCandidates.addAll(searchResultClusterItems)             // 标记戳点已被访问过, 避免重复访问(查询到的戳点已经属于某个簇, 如果候选者中包含这个戳点, 则没必要将该戳点作为簇再查询一遍)
            }
        }
        return results
    }

    override fun getItems(): Collection<T> {
        val items: MutableList<T> = ArrayList()
        synchronized(quadTree) {
            for (quadItem in quadItems) {
                items.add(quadItem.clusterItem)
            }
        }
        return items
    }

    private fun distanceSquared(a: Point, b: Point): Double {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)
    }

    private fun createBoundsFromSpan(p: Point, span: Double): Bounds {
        // TODO: Use a span that takes into account the visual size of the marker, not just its LatLng.
        val halfSpan = span / 2
        return Bounds(p.x - halfSpan, p.x + halfSpan, p.y - halfSpan, p.y + halfSpan)
    }

    private class QuadItem<T : ClusterItem>(val clusterItem: T) : PointQuadTree.Item, Cluster<T> {
        private val items: Set<T> = setOf(clusterItem)
        private val position: LatLng = clusterItem.position
        private val point: Point = PROJECTION.toPoint(position)
        private var bounds: Bounds? = null

        override fun getPoint(): Point = point
        override fun getPosition(): LatLng = position
        override fun getItems(): Set<T> = items
        override fun getSize(): Int = 1
        override fun getSearchBounds(): Bounds? = bounds
        override fun setSearchBounds(bounds: Bounds) {
            this.bounds = bounds
        }
    }

    companion object {
        const val MAX_DISTANCE_AT_ZOOM = 150 // essentially 100 dp.
        val PROJECTION = SphericalMercatorProjection(1.0)
    }
}