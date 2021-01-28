package com.smart.library.map

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.TextureMapView
import com.baidu.mapapi.model.LatLng
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem
import com.smart.library.map.clusterutil.baidu.clustering.ClusterManager
import com.smart.library.map.clusterutil.baidu.clustering.view.LessMoreClusterRenderer
import com.smart.library.map.layer.STMapView
import com.smart.library.map.model.STMapType
import com.smart.library.util.STStatusBarUtil
import kotlinx.android.synthetic.main.st_map_fragment.*
import kotlin.random.Random

@Suppress("unused")
class STMapFragment : STBaseFragment() {

    companion object {
        @JvmStatic
        fun goTo(activity: Activity?, useBaidu: Boolean) {
            val bundle = Bundle()
            bundle.putBoolean("useBaidu", useBaidu)
            STActivity.startActivity(activity, STMapFragment::class.java, bundle)
        }
    }

    private val mapView: STMapView by lazy { mapBaiduView }
    private val useBaidu: Boolean by lazy { arguments?.getBoolean("useBaidu") ?: true }

    private val realMapView: TextureMapView by lazy { mapView.mapView() as TextureMapView }
    private val realBaiduMap: BaiduMap by lazy { realMapView.map }
    private val clusterManager: ClusterManager<MyItem> by lazy { ClusterManager<MyItem>(context, realBaiduMap) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.st_map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fitsSystemWindows = false // 顶到状态栏后面
        activity?.let { STStatusBarUtil.setStatusBarTextColor(it, false) } // 设置状态栏字体深色

        mapView.initialize(mapType = if (useBaidu) STMapType.BAIDU else STMapType.GAODE) {
            initMarkers()
        }
        mapView.onCreate(context, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    private fun initMarkers() {
        // 添加Marker点
        addMarkers()
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        realBaiduMap.setOnMapStatusChangeListener(clusterManager)
        // 设置maker点击时的响应
        realBaiduMap.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterClickListener(object : ClusterManager.OnClusterClickListener<MyItem?> {
            override fun onClusterClick(cluster: Cluster<MyItem?>): Boolean {
                Toast.makeText(context, "有" + cluster.size.toString() + "个点", Toast.LENGTH_SHORT).show()
                return false
            }
        })
        clusterManager.setOnClusterItemClickListener(object : ClusterManager.OnClusterItemClickListener<MyItem?> {
            override fun onClusterItemClick(item: MyItem?): Boolean {
                Toast.makeText(context, "${item?.position}}", Toast.LENGTH_SHORT).show()
                return false
            }
        })
        clusterManager.setRenderer(LessMoreClusterRenderer(context, realBaiduMap, clusterManager));
        // mapView.setMapCenter(true, 11f, STLatLng(39.9655, 116.4055, STLatLngType.BD09))
    }


    /**
     * 向地图添加Marker点
     */
    private fun addMarkers() {
        // 添加Marker点
        val random = Random(1)
        clusterManager.addItems(
            (0..100).mapIndexed { _, _ ->
                MyItem(LatLng(39.96 + (random.nextFloat() / 0.5f), 116.40 + random.nextFloat() / 0.5f))
            }
        )
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    class MyItem constructor(private val position: LatLng) : ClusterItem {
        private val bitmapDescriptor: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)
        override fun getBitmapDescriptor(): BitmapDescriptor {
            return bitmapDescriptor
        }

        override fun getPosition(): LatLng {
            return this.position
        }

    }
}
