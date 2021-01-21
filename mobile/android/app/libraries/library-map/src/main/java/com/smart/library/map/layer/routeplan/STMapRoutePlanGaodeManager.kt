package com.smart.library.map.layer.routeplan

import android.content.Context
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Polyline
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RouteSearch
import com.smart.library.map.layer.STDialogManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.STNetworkUtil
import com.smart.library.util.STToastUtil
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.async
import org.jetbrains.anko.onUiThread
import java.lang.ref.WeakReference

@Suppress("MemberVisibilityCanBePrivate", "unused", "PrivatePropertyName")
class STMapRoutePlanGaodeManager(_context: Context, val map: AMap, val onLocationCallback: (((locationLatLng: LatLng) -> Unit?)) -> Unit?, val onDistanceChanged: (distance: Float) -> Unit?) {

    private val TAG: String = "map-routePlanning"

    private var routePlanningOverlayManager: STMapRoutePlanGaodeOverlayManager? = null
    private val routePlanningProgressDialog by lazy { STDialogManager.createLoadingDialog(context?.weakRef?.get(), "规划中...") }
    private val routeSearch: RouteSearch by lazy { RouteSearch(context?.weakRef?.get()) }

    private var context: AnkoAsyncContext<Context?>? = null

    init {
        context = AnkoAsyncContext(WeakReference(_context))
    }

    fun removeFromMap() {
        routePlanningOverlayManager?.removeFromMap()
    }

    fun selectPolyline(selectedPolyline: Polyline?) {
        routePlanningOverlayManager?.selectPolyline(selectedPolyline)
    }

    fun startSearchRoutePlan(endPoint: LatLonPoint?, callback: ((success: Boolean) -> Unit?)? = null) {
        onLocationCallback.invoke { startSearchRoutePlan(it.latLngPoint(), endPoint, callback) }
    }

    fun startSearchRoutePlan(startPoint: LatLonPoint?, endPoint: LatLonPoint?, callback: ((success: Boolean) -> Unit?)? = null) {
        STLogUtil.d(TAG, "开始查询路径规划:startPoint=$startPoint, endPoint=$endPoint")

        routePlanningOverlayManager?.removeFromMap()
        routePlanningOverlayManager = null

        if (!STNetworkUtil.checkNetworkState()) {
            STToastUtil.show("网络不给力")
            callback?.invoke(false)
            return
        }

        if (startPoint == null || !startPoint.latLng().isValid()) {
            // CXToastUtil.show("网络不给力")
            // CXToastUtil.show("路径规划需要设置有效的起点")
            callback?.invoke(false)
            return
        }
        if (endPoint == null || !endPoint.latLng().isValid()) {
            // CXToastUtil.show("路径规划需要设置有效的终点")
            callback?.invoke(false)
            return
        }

        routePlanningProgressDialog?.show()
        async {
            var driveRouteResult: DriveRouteResult?
            try {
                driveRouteResult = calculateDriveRoute(1, RouteSearch.DriveRouteQuery(RouteSearch.FromAndTo(startPoint, endPoint), RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, ""))
            } catch (mapException: AMapException) {
                STLogUtil.e(TAG, "路径规划失败", mapException)
                driveRouteResult = null
            }

            context?.weakRef?.get()?.onUiThread {
                if (driveRouteResult != null && driveRouteResult.paths != null) {
                    routePlanningOverlayManager = STMapRoutePlanGaodeOverlayManager(map, driveRouteResult.paths, driveRouteResult.startPos.latLng(), driveRouteResult.targetPos.latLng()) {
                        onDistanceChanged.invoke(it.distance)
                        Unit
                    }
                    routePlanningOverlayManager?.addToMap()
                    callback?.invoke(true)
                } else {
                    STToastUtil.show("未查询到路径规划")
                    callback?.invoke(false)
                }
                routePlanningProgressDialog?.dismiss()
            }
        }

    }

    private fun calculateDriveRoute(retryCount: Int, query: RouteSearch.DriveRouteQuery): DriveRouteResult? {
        var driveRouteResult: DriveRouteResult?
        try {
            driveRouteResult = routeSearch.calculateDriveRoute(query)
        } catch (mapException: AMapException) {
            STLogUtil.e(TAG, "路径规划失败", mapException)
            driveRouteResult = null
            if (retryCount > 0) {
                STLogUtil.e(TAG, "retry calculateDriveRoute now ->")
                driveRouteResult = calculateDriveRoute(retryCount - 1, query)
            }
        }
        return driveRouteResult
    }
}