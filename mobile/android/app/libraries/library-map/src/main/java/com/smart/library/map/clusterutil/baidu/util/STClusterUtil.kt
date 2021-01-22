package com.smart.library.map.clusterutil.baidu.util

import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.Overlay
import com.baidu.mapapi.map.PolygonOptions
import com.baidu.mapapi.map.Stroke
import com.baidu.mapapi.model.LatLng
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.smart.library.map.clusterutil.baidu.projection.Bounds
import com.smart.library.map.clusterutil.baidu.projection.Point
import com.smart.library.util.STLogUtil
import java.util.*

/**
 * 相关研究   https://blog.csdn.net/javine/article/details/51195014
 * 四叉树算法 http://www.cocoachina.com/articles/21265
 *
 * DefaultClusterRenderer
 *      private static final int MIN_CLUSTER_SIZE = 2; // todo default is 4
 * NonHierarchicalDistanceBasedAlgorithm
 *      public static final int MAX_DISTANCE_AT_ZOOM = 100; // essentially 100 dp.
 */
@Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
object STClusterUtil {
    private const val debug = true
    private val hashMap: HashMap<LatLng, Overlay?> = hashMapOf()

    @JvmStatic
    fun log(msg: String) {
        if (debug) STLogUtil.d("[cluster]", msg)
    }

    /**
     * 在地图上绘制 cluster 范围矩形框
     */
    @JvmStatic
    fun addClusterSearchBoundsOverlay(map: BaiduMap, cluster: Cluster<*>) {
        if (!debug) return

        hashMap[cluster.position]?.remove()
        hashMap[cluster.position] = null

        val searchBounds = cluster.searchBounds ?: return
        val latLngLT = NonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.minX, searchBounds.minY))
        val latLngRT = NonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.maxX, searchBounds.minY))
        val latLngLB = NonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.minX, searchBounds.maxY))
        val latLngRB = NonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.maxX, searchBounds.maxY))

        val points: MutableList<LatLng> = ArrayList()
        points.add(latLngLT)
        points.add(latLngRT)
        points.add(latLngRB)
        points.add(latLngLB)

        val mPolygonOptions = PolygonOptions()
            .points(points)
            .fillColor(-0x55000100)
            .stroke(Stroke(5, -0x55ff0100))
        hashMap[cluster.position] = map.addOverlay(mPolygonOptions)
    }

    @JvmStatic
    private fun createBoundsFromSpan(p: Point, span: Double): Bounds {
        // TODO: Use a span that takes into account the visual size of the marker, not just its LatLng.
        val halfSpan = span / 2
        return Bounds(p.x - halfSpan, p.x + halfSpan, p.y - halfSpan, p.y + halfSpan)
    }
}