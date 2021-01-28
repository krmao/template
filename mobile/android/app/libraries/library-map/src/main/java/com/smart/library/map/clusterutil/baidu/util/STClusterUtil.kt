package com.smart.library.map.clusterutil.baidu.util

import android.graphics.Color
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.Overlay
import com.baidu.mapapi.map.PolygonOptions
import com.baidu.mapapi.map.Stroke
import com.baidu.mapapi.model.LatLng
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.algo.STNonHierarchicalDistanceBasedAlgorithm
import com.smart.library.map.clusterutil.baidu.projection.Point
import com.smart.library.util.STLogUtil
import java.util.*

/**
 * 1. 相关研究 {@link https://blog.csdn.net/javine/article/details/51195014}
 * 2. 四叉树算法 {@link http://www.cocoachina.com/articles/21265}
 * 3. DefaultClusterRenderer.MIN_CLUSTER_SIZE = 2;                         // 至少大于多少个 markers 才能聚合
 * 4. STNonHierarchicalDistanceBasedAlgorithm.MAX_DISTANCE_AT_ZOOM = 100;    // 两个 marker 之间满足多少距离可以聚合
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
        val latLngLT = STNonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.minX, searchBounds.minY))
        val latLngRT = STNonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.maxX, searchBounds.minY))
        val latLngLB = STNonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.minX, searchBounds.maxY))
        val latLngRB = STNonHierarchicalDistanceBasedAlgorithm.PROJECTION.toLatLng(Point(searchBounds.maxX, searchBounds.maxY))

        val points: MutableList<LatLng> = ArrayList()
        points.add(latLngLT)
        points.add(latLngRT)
        points.add(latLngRB)
        points.add(latLngLB)

        val mPolygonOptions = PolygonOptions()
            .points(points)
            .fillColor(Color.parseColor("#20000100"))
            .stroke(Stroke(3, Color.parseColor("#20ff0100")))
        hashMap[cluster.position] = map.addOverlay(mPolygonOptions)
    }
}