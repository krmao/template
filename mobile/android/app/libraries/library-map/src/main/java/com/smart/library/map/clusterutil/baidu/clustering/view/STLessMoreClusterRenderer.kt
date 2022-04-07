package com.smart.library.map.clusterutil.baidu.clustering.view

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.MessageQueue.IdleHandler
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.model.LatLng
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem
import com.smart.library.map.clusterutil.baidu.clustering.ClusterManager
import com.smart.library.map.clusterutil.baidu.clustering.ClusterManager.*
import com.smart.library.map.clusterutil.baidu.util.STClusterUtil.log
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Suppress("unused")
class STLessMoreClusterRenderer<T : ClusterItem>(private val map: BaiduMap, private val clusterManager: ClusterManager<T>) : ClusterRenderer<T> {
    private var currentMarkers = Collections.newSetFromMap(ConcurrentHashMap<MarkerWithPosition, Boolean>())    // Markers that are currently on the map.
    private var currentClusters: Set<Cluster<T>>? = null                                                        // The currently displayed set of clusters.
    private val markerCache = MarkerCache<T>()                                                                  // Markers for single ClusterItems.

    private var currentZoom = 0f
    private val viewModifier: ViewModifier = ViewModifier()
    private var itemClickListener: OnClusterItemClickListener<T>? = null

    // private var clickListener: OnClusterClickListener<T>? = null
    // private var infoWindowClickListener: OnClusterInfoWindowClickListener<T>? = null
    // private var itemInfoWindowClickListener: OnClusterItemInfoWindowClickListener<T>? = null

    override fun onAdd() {
        clusterManager.markerCollection.setOnMarkerClickListener { marker -> itemClickListener != null && itemClickListener?.onClusterItemClick(markerCache[marker]) ?: false }
    }

    override fun onRemove() {
        clusterManager.markerCollection.setOnMarkerClickListener(null)
    }

    /**
     * Determine whether the cluster should be rendered as individual markers or a cluster.
     */
    private fun shouldRenderAsCluster(cluster: Cluster<T>): Boolean = cluster.size > MIN_CLUSTER_SIZE

    override fun onClustersChanged(clusters: Set<Cluster<T>>) {
        log("4 onClustersChanged mViewModifier.queue ...")
        viewModifier.queue(clusters)
    }

    override fun setOnClusterItemClickListener(listener: OnClusterItemClickListener<T>?) {
        itemClickListener = listener
    }

    //region no need
    override fun setOnClusterClickListener(listener: OnClusterClickListener<T>?) {
        // clickListener = listener
    }

    override fun setOnClusterInfoWindowClickListener(listener: OnClusterInfoWindowClickListener<T>?) {
        // infoWindowClickListener = listener
    }


    override fun setOnClusterItemInfoWindowClickListener(listener: OnClusterItemInfoWindowClickListener<T>?) {
        // itemInfoWindowClickListener = listener
    }

    /**
     * Get the marker from a ClusterItem
     *
     * @param clusterItem ClusterItem which you will obtain its marker
     * @return a marker from a ClusterItem or null if it does not exists
     */
    fun getMarker(clusterItem: T): Marker? = markerCache[clusterItem]

    /**
     * Get the ClusterItem from a marker
     *
     * @param marker which you will obtain its ClusterItem
     * @return a ClusterItem from a marker or null if it does not exists
     */
    fun getClusterItem(marker: Marker?): T? = if (marker != null) markerCache[marker] else null
    //endregion

    /**
     * 序列化进行渲染地图, 一次只渲染一次, 结束后会检查有没有新的 nextClustersRenderTask, 如果有继续执行
     *
     * ViewModifier ensures only one re-rendering of the view occurs at a time, and schedules
     * re-rendering, which is performed by the RenderTask.
     */
    @SuppressLint("HandlerLeak")
    private inner class ViewModifier : Handler() {
        private var viewModificationInProgress = false
        private var nextClustersRenderTask: RenderTask? = null
        override fun handleMessage(msg: Message) {
            if (msg.what == Companion.TASK_FINISHED) {
                viewModificationInProgress = false
                if (nextClustersRenderTask != null) {
                    // Run the task that was queued up.
                    sendEmptyMessage(Companion.RUN_TASK)
                }
                return
            }
            removeMessages(Companion.RUN_TASK)
            if (viewModificationInProgress) {
                // Busy - wait for the callback.
                return
            }
            if (nextClustersRenderTask == null) {
                // Nothing to do.
                return
            }
            val renderTask: RenderTask?
            synchronized(this) {
                renderTask = nextClustersRenderTask
                nextClustersRenderTask = null
                viewModificationInProgress = true
            }
            if (renderTask != null) {
                renderTask.setCallback(Runnable { sendEmptyMessage(Companion.TASK_FINISHED) })
                renderTask.setMapZoom(map.mapStatus.zoom)
                log("6 Thread(renderTask).start() ...")
                Thread(renderTask).start()
            }
        }

        fun queue(clusters: Set<Cluster<T>>) {
            synchronized(this) {
                nextClustersRenderTask = RenderTask(clusters) // Overwrite any pending cluster tasks - we don't care about intermediate states.
            }
            log("5 mViewModifier.queue do RenderTask ...")
            sendEmptyMessage(Companion.RUN_TASK)
        }
    }

    /**
     * 渲染
     *
     * Transforms the current view (represented by DefaultClusterRenderer.mClusters and DefaultClusterRenderer.mZoom) to a
     * new zoom level and set of clusters.
     *
     *
     * This must be run off the UI thread. Work is coordinated in the RenderTask, then queued up to
     * be executed by a MarkerModifier.
     *
     *
     * There are three stages for the render:
     *
     *
     * 1. Markers are added to the map
     *
     *
     * 2. Markers are animated to their final position
     *
     *
     * 3. Any old markers are removed from the map
     *
     *
     * When zooming in, markers are animated out from the nearest existing cluster. When zooming
     * out, existing clusters are animated to the nearest new cluster.
     */
    private inner class RenderTask(val newClusters: Set<Cluster<T>>) : Runnable {
        private var callback: Runnable? = null
        private var newZoom = 0f

        /**
         * A callback to be run when all work has been completed.
         */
        fun setCallback(callback: Runnable?) {
            this.callback = callback
        }

        fun setMapZoom(zoom: Float) {
            this.newZoom = zoom
        }

        @SuppressLint("NewApi")
        override fun run() {
            log("RenderTask run ...")
            if (newClusters == currentClusters) {
                log("RenderTask run clusters not changed return")
                callback?.run()
                return
            }
            val markerModifier = MarkerModifier()
            val markersToRemove = currentMarkers // 默认当前所有已显示的 markers 都将要被删除
            val zoom = newZoom
            val zoomingIn = zoom > this@STLessMoreClusterRenderer.currentZoom
            val zoomDelta = zoom - this@STLessMoreClusterRenderer.currentZoom
            val newVisibleBounds = map.mapStatus.bound

            //region 添加新的 marker/clusters
            // Create the new markers and animate them to their new positions.
            // 添加的新的 marker/clusters, 在屏幕范围内的优先处理
            val newMarkers = Collections.newSetFromMap(ConcurrentHashMap<MarkerWithPosition, Boolean>())
            for (clusterAdding in newClusters) {
                val onScreen = newVisibleBounds.contains(clusterAdding.position)
                log("RenderTask run CreateMarkerTask for new clusters")
                if (zoomingIn && onScreen) {
                    markerModifier.add(true, CreateMarkerTask(clusterAdding, newMarkers))
                } else {
                    markerModifier.add(onScreen, CreateMarkerTask(clusterAdding, newMarkers))
                }
            }
            // Wait for all markers to be added.
            markerModifier.waitUntilFree()
            //endregion

            //region 删除之前的 marker/clusters
            // Don't remove any markers that were just added. This is basically anything that had
            // a hit in the MarkerCache.
            // 新添加的且之前已经显示的 markers 不用删除
            markersToRemove.removeAll(newMarkers)
            // Remove the old markers, animating them into clusters if zooming out.
            for (marker in markersToRemove) {
                val onScreen = newVisibleBounds.contains(marker.position)
                // Don't animate when zooming out more than 3 zoom levels.
                if (!zoomingIn && zoomDelta > -3 && onScreen) {
                    markerModifier.remove(true, marker.marker)
                } else {
                    markerModifier.remove(onScreen, marker.marker)
                }
            }
            markerModifier.waitUntilFree()
            //endregion

            //region 更新当前变量
            currentMarkers = newMarkers
            currentClusters = newClusters
            this@STLessMoreClusterRenderer.currentZoom = zoom
            //endregion
            callback?.run()
        }
    }

    /**
     * 尽量不阻塞主线程添加/删除 markers
     *
     * Handles all markerWithPosition manipulations on the map. Work (such as adding, removing, or
     * animating a markerWithPosition) is performed while trying not to block the rest of the app's
     * UI.
     */
    @SuppressLint("HandlerLeak")
    private inner class MarkerModifier : Handler(Looper.getMainLooper()), IdleHandler {
        private val lock: Lock = ReentrantLock()
        private val busyCondition = lock.newCondition()
        private val createMarkerTasks: Queue<CreateMarkerTask> = LinkedList()
        private val onScreenCreateMarkerTasks: Queue<CreateMarkerTask> = LinkedList()
        private val removeMarkerTasks: Queue<Marker> = LinkedList()
        private val onScreenRemoveMarkerTasks: Queue<Marker> = LinkedList()

        /**
         * Whether the idle listener has been added to the UI thread's MessageQueue.
         */
        private var isIdleHandlerAdded = false

        /**
         * Creates markers for a cluster some time in the future.
         *
         * @param priority whether this operation should have priority.
         */
        fun add(priority: Boolean, c: CreateMarkerTask) {
            lock.lock()
            sendEmptyMessage(Companion.BLANK)
            if (priority) {
                onScreenCreateMarkerTasks.add(c)
            } else {
                createMarkerTasks.add(c)
            }
            lock.unlock()
        }

        /**
         * Removes a markerWithPosition some time in the future.
         *
         * @param priority whether this operation should have priority.
         * @param m        the markerWithPosition to remove.
         */
        fun remove(priority: Boolean, m: Marker) {
            lock.lock()
            sendEmptyMessage(Companion.BLANK)
            if (priority) {
                onScreenRemoveMarkerTasks.add(m)
            } else {
                removeMarkerTasks.add(m)
            }
            lock.unlock()
        }

        override fun handleMessage(msg: Message) {
            if (!isIdleHandlerAdded) {
                Looper.myQueue().addIdleHandler(this)
                isIdleHandlerAdded = true
            }
            removeMessages(Companion.BLANK)
            lock.lock()
            try {
                // Perform up to 10 tasks at once.
                // Consider only performing 10 remove tasks, not adds and animations.
                // Removes are relatively slow and are much better when batched.
                for (i in 0..9) {
                    performNextTask()
                }
                if (!isBusy) {
                    isIdleHandlerAdded = false
                    Looper.myQueue().removeIdleHandler(this)
                    // Signal any other threads that are waiting.
                    busyCondition.signalAll()
                } else {
                    // Sometimes the idle queue may not be called - schedule up some work regardless
                    // of whether the UI thread is busy or not.
                    // TODO: try to remove this.
                    sendEmptyMessageDelayed(Companion.BLANK, 10)
                }
            } finally {
                lock.unlock()
            }
        }

        /**
         * Perform the next task. Prioritise any on-screen work.
         */
        private fun performNextTask() {
            if (!this.onScreenRemoveMarkerTasks.isEmpty()) {
                removeMarker(onScreenRemoveMarkerTasks.poll())
            } else if (!onScreenCreateMarkerTasks.isEmpty()) {
                onScreenCreateMarkerTasks.poll()?.perform()
            } else if (!createMarkerTasks.isEmpty()) {
                createMarkerTasks.poll()?.perform()
            } else if (!removeMarkerTasks.isEmpty()) {
                removeMarker(removeMarkerTasks.poll())
            }
        }

        private fun removeMarker(marker: Marker?) {
            marker ?: return
            markerCache.remove(marker)
            clusterManager.markerManager.remove(marker)
        }

        /**
         * @return true if there is still work to be processed.
         */
        val isBusy: Boolean
            get() = try {
                lock.lock()
                !(createMarkerTasks.isEmpty() && onScreenCreateMarkerTasks.isEmpty() && onScreenRemoveMarkerTasks.isEmpty() && removeMarkerTasks.isEmpty())
            } finally {
                lock.unlock()
            }

        /**
         * Blocks the calling thread until all work has been processed.
         */
        fun waitUntilFree() {
            while (isBusy) {
                // Sometimes the idle queue may not be called - schedule up some work regardless
                // of whether the UI thread is busy or not.
                // TODO: try to remove this.
                sendEmptyMessage(Companion.BLANK)
                lock.lock()
                try {
                    if (isBusy) {
                        busyCondition.await()
                    }
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                } finally {
                    lock.unlock()
                }
            }
        }

        override fun queueIdle(): Boolean {
            // When the UI is not busy, schedule some work.
            sendEmptyMessage(Companion.BLANK)
            return true
        }
    }

    /**
     * 缓存 marker & item
     * A cache of markers representing individual ClusterItems.
     */
    private class MarkerCache<T> {
        private val cache: MutableMap<T, Marker> = HashMap()
        private val cacheReverse: MutableMap<Marker, T> = HashMap()
        operator fun get(item: T): Marker? = cache[item]
        operator fun get(marker: Marker): T? = cacheReverse[marker]

        fun put(item: T, marker: Marker) {
            cache[item] = marker
            cacheReverse[marker] = item
        }

        fun remove(marker: Marker) {
            val item = cacheReverse[marker]
            cacheReverse.remove(marker)
            cache.remove(item)
        }
    }

    /**
     * 打 点/簇 到地图上
     * Creates markerWithPosition(s) for a particular cluster, animating it if necessary.
     */
    private inner class CreateMarkerTask(private val clusterAdding: Cluster<T>, private val markersAdded: MutableSet<MarkerWithPosition>) {
        fun perform() {
            val shouldRenderAsCluster = shouldRenderAsCluster(clusterAdding)
            log("CreateMarkerTask add cluster items to map (not cluster self)")
            for (item in clusterAdding.items) {
                var marker = markerCache[item]
                var markerWithPosition: MarkerWithPosition
                if (marker == null) {
                    val markerOptions = MarkerOptions()
                    markerOptions.position(item.position)
                    markerOptions.icon(item.bitmapDescriptor)
                    marker = clusterManager.markerCollection.addMarker(markerOptions)
                    markerWithPosition = MarkerWithPosition(marker)
                    markerCache.put(item, marker)
                } else {
                    markerWithPosition = MarkerWithPosition(marker)
                }
                log("CreateMarkerTask onClusterItemRendered")
                markersAdded.add(markerWithPosition)
                if (shouldRenderAsCluster) break
            }
        }
    }

    private class MarkerWithPosition(val marker: Marker) {
        val position: LatLng = marker.position
        override fun equals(other: Any?): Boolean = if (other is MarkerWithPosition) marker == other.marker else false
        override fun hashCode(): Int = marker.hashCode()
    }

    companion object {
        private const val BLANK = 0
        private const val RUN_TASK = 0
        private const val TASK_FINISHED = 1
        private const val MIN_CLUSTER_SIZE = 1  // If cluster size is less than this size, display individual markers.
    }
}