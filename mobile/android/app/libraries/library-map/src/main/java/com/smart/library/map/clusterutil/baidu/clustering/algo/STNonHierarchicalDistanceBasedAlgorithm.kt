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

    private val quadItems: MutableCollection<QuadItem<T>> = ArrayList()                                  // Any modifications should be synchronized on mQuadTree.
    private val quadTree = PointQuadTree<QuadItem<T>>(0.0, 1.0, 0.0, 1.0)  // Any modifications should be synchronized on mQuadTree.

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
        val visitedCandidates: MutableSet<QuadItem<T>> = HashSet()              // 本次 getClusters 过程中是否已经访问过
        val results: MutableSet<Cluster<T>> = HashSet()
        val distanceToCluster: MutableMap<QuadItem<T>, Double> = HashMap()
        val itemToCluster: MutableMap<QuadItem<T>, STStaticCluster<T>> = HashMap()
        synchronized(quadTree) {
            log("getClusters zoom=" + zoom + ", zoomSpecificSpan=" + zoomSpecificSpan + ", mQuadTree.bounds=" + quadTree.mBounds)
            for (candidate in quadItems) {
                if (visitedCandidates.contains(candidate)) {
                    // Candidate is already part of another cluster.
                    continue
                }
                val searchBounds = createBoundsFromSpan(candidate.point, zoomSpecificSpan)
                log("-------- searchBounds=$searchBounds")
                var clusterItems: Collection<QuadItem<T>>
                // search 某边界范围内的clusterItems
                clusterItems = quadTree.search(searchBounds)
                if (clusterItems.size == 1) {
                    // Only the current marker is in range. Just add the single item to the results.
                    candidate.setSearchBounds(searchBounds)
                    results.add(candidate)
                    visitedCandidates.add(candidate)
                    distanceToCluster[candidate] = 0.0
                    continue
                }
                val cluster = STStaticCluster<T>(candidate.mClusterItem.position)
                cluster.searchBounds = searchBounds
                results.add(cluster)
                for (clusterItem in clusterItems) {
                    val existingDistance = distanceToCluster[clusterItem]
                    val distance = distanceSquared(clusterItem.point, candidate.point)
                    if (existingDistance != null) {
                        // Item already belongs to another cluster. Check if it's closer to this cluster.
                        if (existingDistance < distance) {
                            continue
                        }
                        // Move item to the closer cluster.
                        itemToCluster[clusterItem]!!.remove(clusterItem.mClusterItem)
                    }
                    distanceToCluster[clusterItem] = distance
                    cluster.add(clusterItem.mClusterItem)
                    itemToCluster[clusterItem] = cluster
                }
                visitedCandidates.addAll(clusterItems)
            }
        }
        return results
    }

    override fun getItems(): Collection<T> {
        val items: MutableList<T> = ArrayList()
        synchronized(quadTree) {
            for (quadItem in quadItems) {
                items.add(quadItem.mClusterItem)
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

    private class QuadItem<T : ClusterItem>(val mClusterItem: T) : PointQuadTree.Item, Cluster<T> {
        private val items: Set<T> = setOf(mClusterItem)
        private val position: LatLng = mClusterItem.position
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