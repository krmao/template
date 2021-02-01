package com.smart.library.map.clusterutil.baidu.clustering.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.smart.library.map.clusterutil.baidu.clustering.Cluster;
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem;
import com.smart.library.map.clusterutil.baidu.clustering.ClusterManager;
import com.smart.library.map.clusterutil.baidu.clustering.algo.STNonHierarchicalDistanceBasedAlgorithm;
import com.smart.library.map.clusterutil.baidu.projection.Point;
import com.smart.library.map.clusterutil.baidu.projection.SphericalMercatorProjection;
import com.smart.library.map.clusterutil.baidu.util.STClusterUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The default view for a ClusterManager. Markers are animated in and out of clusters.
 */
public class LessMoreClusterRenderer<T extends ClusterItem> implements ClusterRenderer<T> {
    @SuppressLint("ObsoleteSdkInt")
    private static final boolean SHOULD_ANIMATE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    private final BaiduMap map;
    private final ClusterManager<T> clusterManager;
    /**
     * Markers that are currently on the map.
     */
    private Set<MarkerWithPosition> currentMarkers = Collections.newSetFromMap(new ConcurrentHashMap<MarkerWithPosition, Boolean>());

    /**
     * Markers for single ClusterItems.
     */
    private final MarkerCache<T> mMarkerCache = new MarkerCache<T>();

    /**
     * If cluster size is less than this size, display individual markers.
     */
    private static final int MIN_CLUSTER_SIZE = 1;

    /**
     * The currently displayed set of clusters.
     */
    private Set<? extends Cluster<T>> currentClusters;

    /**
     * Lookup between markers and the associated cluster.
     */
    private final Map<Marker, Cluster<T>> mMarkerToCluster = new HashMap<>();
    private final Map<Cluster<T>, Marker> mClusterToMarker = new HashMap<>();

    /**
     * The target zoom level for the current set of clusters.
     */
    private float currentZoom;

    private final ViewModifier viewModifier = new ViewModifier();

    private ClusterManager.OnClusterClickListener<T> mClickListener;
    private ClusterManager.OnClusterInfoWindowClickListener<T> mInfoWindowClickListener;
    private ClusterManager.OnClusterItemClickListener<T> mItemClickListener;
    private ClusterManager.OnClusterItemInfoWindowClickListener<T> mItemInfoWindowClickListener;

    public LessMoreClusterRenderer(Context context, BaiduMap map, ClusterManager<T> clusterManager) {
        this.map = map;
        this.clusterManager = clusterManager;
    }

    @Override
    public void onAdd() {
        clusterManager.getMarkerCollection().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return mItemClickListener != null && mItemClickListener.onClusterItemClick(mMarkerCache.get(marker));
            }
        });

        clusterManager.getClusterMarkerCollection().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return mClickListener != null && mClickListener.onClusterClick(mMarkerToCluster.get(marker));
            }
        });
    }

    @Override
    public void onRemove() {
        clusterManager.getMarkerCollection().setOnMarkerClickListener(null);
        clusterManager.getClusterMarkerCollection().setOnMarkerClickListener(null);
    }

    /**
     * ViewModifier ensures only one re-rendering of the view occurs at a time, and schedules
     * re-rendering, which is performed by the RenderTask.
     */
    @SuppressLint("HandlerLeak")
    private class ViewModifier extends Handler {
        private static final int RUN_TASK = 0;
        private static final int TASK_FINISHED = 1;
        private boolean mViewModificationInProgress = false;
        private RenderTask mNextClusters = null;

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TASK_FINISHED) {
                mViewModificationInProgress = false;
                if (mNextClusters != null) {
                    // Run the task that was queued up.
                    sendEmptyMessage(RUN_TASK);
                }
                return;
            }
            removeMessages(RUN_TASK);

            if (mViewModificationInProgress) {
                // Busy - wait for the callback.
                return;
            }

            if (mNextClusters == null) {
                // Nothing to do.
                return;
            }

            RenderTask renderTask;
            synchronized (this) {
                renderTask = mNextClusters;
                mNextClusters = null;
                mViewModificationInProgress = true;
            }

            renderTask.setCallback(new Runnable() {
                @Override
                public void run() {
                    sendEmptyMessage(TASK_FINISHED);
                }
            });
            renderTask.setProjection(map.getProjection());
            renderTask.setMapZoom(map.getMapStatus().zoom);
            new Thread(renderTask).start();
        }

        public void queue(Set<? extends Cluster<T>> clusters) {
            synchronized (this) {
                // Overwrite any pending cluster tasks - we don't care about intermediate states.
                mNextClusters = new RenderTask(clusters);
            }
            STClusterUtil.log("mViewModifier.queue do RenderTask ...");
            sendEmptyMessage(RUN_TASK);
        }
    }

    /**
     * Determine whether the cluster should be rendered as individual markers or a cluster.
     */
    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        return cluster.getSize() > MIN_CLUSTER_SIZE;
    }

    /**
     * Transforms the current view (represented by DefaultClusterRenderer.mClusters and DefaultClusterRenderer.mZoom) to a
     * new zoom level and set of clusters.
     * <p/>
     * This must be run off the UI thread. Work is coordinated in the RenderTask, then queued up to
     * be executed by a MarkerModifier.
     * <p/>
     * There are three stages for the render:
     * <p/>
     * 1. Markers are added to the map
     * <p/>
     * 2. Markers are animated to their final position
     * <p/>
     * 3. Any old markers are removed from the map
     * <p/>
     * When zooming in, markers are animated out from the nearest existing cluster. When zooming
     * out, existing clusters are animated to the nearest new cluster.
     */
    private class RenderTask implements Runnable {
        final Set<? extends Cluster<T>> clusters;
        private Runnable mCallback;
        private Projection mProjection;
        private float currentZoom;

        private RenderTask(Set<? extends Cluster<T>> clusters) {
            this.clusters = clusters;
        }

        /**
         * A callback to be run when all work has been completed.
         */
        public void setCallback(Runnable callback) {
            mCallback = callback;
        }

        public void setProjection(Projection projection) {
            this.mProjection = projection;
        }

        public void setMapZoom(float zoom) {
            this.currentZoom = zoom;
        }

        @SuppressLint("NewApi")
        public void run() {
            STClusterUtil.log("RenderTask run ...");
            if (clusters.equals(LessMoreClusterRenderer.this.currentClusters)) {
                STClusterUtil.log("RenderTask run clusters not changed return");
                mCallback.run();
                return;
            }

            final MarkerModifier markerModifier = new MarkerModifier();
            final Set<MarkerWithPosition> markersToRemove = currentMarkers; // 默认当前所有已显示的 markers 都将要被删除
            final float zoom = currentZoom;
            final boolean zoomingIn = zoom > LessMoreClusterRenderer.this.currentZoom;
            final float zoomDelta = zoom - LessMoreClusterRenderer.this.currentZoom;

            final LatLngBounds visibleBounds = map.getMapStatus().bound;

            //region 添加新的 marker/clusters
            // Create the new markers and animate them to their new positions.
            // 添加的新的 marker/clusters, 在屏幕范围内的优先处理
            final Set<MarkerWithPosition> newMarkers = Collections.newSetFromMap(new ConcurrentHashMap<MarkerWithPosition, Boolean>());
            for (Cluster<T> clusterAdding : clusters) {
                boolean onScreen = visibleBounds.contains(clusterAdding.getPosition());
                STClusterUtil.log("RenderTask run CreateMarkerTask for new clusters");
                if (zoomingIn && onScreen && SHOULD_ANIMATE) {
                    markerModifier.add(true, new CreateMarkerTask(clusterAdding, newMarkers));
                } else {
                    markerModifier.add(onScreen, new CreateMarkerTask(clusterAdding, newMarkers));
                }
            }
            // Wait for all markers to be added.
            markerModifier.waitUntilFree();
            //endregion

            //region 删除之前的 marker/clusters
            // Don't remove any markers that were just added. This is basically anything that had
            // a hit in the MarkerCache.
            // 新添加的且之前已经显示的 markers 不用删除
            markersToRemove.removeAll(newMarkers);
            // Remove the old markers, animating them into clusters if zooming out.
            for (final MarkerWithPosition marker : markersToRemove) {
                boolean onScreen = visibleBounds.contains(marker.position);
                // Don't animate when zooming out more than 3 zoom levels.
                if (!zoomingIn && zoomDelta > -3 && onScreen && SHOULD_ANIMATE) {
                    markerModifier.remove(true, marker.marker);
                } else {
                    markerModifier.remove(onScreen, marker.marker);
                }
            }
            markerModifier.waitUntilFree();
            //endregion

            //region 更新当前变量
            currentMarkers = newMarkers;
            LessMoreClusterRenderer.this.currentClusters = clusters;
            LessMoreClusterRenderer.this.currentZoom = zoom;
            //endregion

            mCallback.run();
        }
    }

    @Override
    public void onClustersChanged(Set<? extends Cluster<T>> clusters) {
        STClusterUtil.log("onClustersChanged mViewModifier.queue ...");
        viewModifier.queue(clusters);
    }

    @Override
    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<T> listener) {
        mClickListener = listener;
    }

    @Override
    public void setOnClusterInfoWindowClickListener(ClusterManager.OnClusterInfoWindowClickListener<T> listener) {
        mInfoWindowClickListener = listener;
    }

    @Override
    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<T> listener) {
        mItemClickListener = listener;
    }

    @Override
    public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<T> listener) {
        mItemInfoWindowClickListener = listener;
    }

    private static double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }

    private static Point findClosestCluster(List<Point> markers, Point point) {
        if (markers == null || markers.isEmpty()) {
            return null;
        }

        // TODO: make this configurable.
        double minDistSquared = STNonHierarchicalDistanceBasedAlgorithm.MAX_DISTANCE_AT_ZOOM * STNonHierarchicalDistanceBasedAlgorithm.MAX_DISTANCE_AT_ZOOM;
        Point closest = null;
        for (Point candidate : markers) {
            double dist = distanceSquared(candidate, point);
            if (dist < minDistSquared) {
                closest = candidate;
                minDistSquared = dist;
            }
        }
        return closest;
    }

    /**
     * Handles all markerWithPosition manipulations on the map. Work (such as adding, removing, or
     * animating a markerWithPosition) is performed while trying not to block the rest of the app's
     * UI.
     */
    @SuppressLint("HandlerLeak")
    private class MarkerModifier extends Handler implements MessageQueue.IdleHandler {
        private static final int BLANK = 0;

        private final Lock lock = new ReentrantLock();
        private final Condition busyCondition = lock.newCondition();

        private Queue<CreateMarkerTask> mCreateMarkerTasks = new LinkedList<CreateMarkerTask>();
        private Queue<CreateMarkerTask> mOnScreenCreateMarkerTasks = new LinkedList<CreateMarkerTask>();
        private Queue<Marker> mRemoveMarkerTasks = new LinkedList<Marker>();
        private Queue<Marker> mOnScreenRemoveMarkerTasks = new LinkedList<Marker>();

        /**
         * Whether the idle listener has been added to the UI thread's MessageQueue.
         */
        private boolean mListenerAdded;

        private MarkerModifier() {
            super(Looper.getMainLooper());
        }

        /**
         * Creates markers for a cluster some time in the future.
         *
         * @param priority whether this operation should have priority.
         */
        public void add(boolean priority, CreateMarkerTask c) {
            lock.lock();
            sendEmptyMessage(BLANK);
            if (priority) {
                mOnScreenCreateMarkerTasks.add(c);
            } else {
                mCreateMarkerTasks.add(c);
            }
            lock.unlock();
        }

        /**
         * Removes a markerWithPosition some time in the future.
         *
         * @param priority whether this operation should have priority.
         * @param m        the markerWithPosition to remove.
         */
        public void remove(boolean priority, Marker m) {
            lock.lock();
            sendEmptyMessage(BLANK);
            if (priority) {
                mOnScreenRemoveMarkerTasks.add(m);
            } else {
                mRemoveMarkerTasks.add(m);
            }
            lock.unlock();
        }

        @Override
        public void handleMessage(Message msg) {
            if (!mListenerAdded) {
                Looper.myQueue().addIdleHandler(this);
                mListenerAdded = true;
            }
            removeMessages(BLANK);

            lock.lock();
            try {

                // Perform up to 10 tasks at once.
                // Consider only performing 10 remove tasks, not adds and animations.
                // Removes are relatively slow and are much better when batched.
                for (int i = 0; i < 10; i++) {
                    performNextTask();
                }

                if (!isBusy()) {
                    mListenerAdded = false;
                    Looper.myQueue().removeIdleHandler(this);
                    // Signal any other threads that are waiting.
                    busyCondition.signalAll();
                } else {
                    // Sometimes the idle queue may not be called - schedule up some work regardless
                    // of whether the UI thread is busy or not.
                    // TODO: try to remove this.
                    sendEmptyMessageDelayed(BLANK, 10);
                }
            } finally {
                lock.unlock();
            }
        }

        /**
         * Perform the next task. Prioritise any on-screen work.
         */
        private void performNextTask() {
            if (!mOnScreenRemoveMarkerTasks.isEmpty()) {
                removeMarker(mOnScreenRemoveMarkerTasks.poll());
            } else if (!mOnScreenCreateMarkerTasks.isEmpty()) {
                mOnScreenCreateMarkerTasks.poll().perform(this);
            } else if (!mCreateMarkerTasks.isEmpty()) {
                mCreateMarkerTasks.poll().perform(this);
            } else if (!mRemoveMarkerTasks.isEmpty()) {
                removeMarker(mRemoveMarkerTasks.poll());
            }
        }

        private void removeMarker(Marker m) {
            Cluster<T> cluster = mMarkerToCluster.get(m);
            mClusterToMarker.remove(cluster);
            mMarkerCache.remove(m);
            mMarkerToCluster.remove(m);
            clusterManager.getMarkerManager().remove(m);
        }

        /**
         * @return true if there is still work to be processed.
         */
        public boolean isBusy() {
            try {
                lock.lock();
                return !(mCreateMarkerTasks.isEmpty() && mOnScreenCreateMarkerTasks.isEmpty() && mOnScreenRemoveMarkerTasks.isEmpty() && mRemoveMarkerTasks.isEmpty());
            } finally {
                lock.unlock();
            }
        }

        /**
         * Blocks the calling thread until all work has been processed.
         */
        public void waitUntilFree() {
            while (isBusy()) {
                // Sometimes the idle queue may not be called - schedule up some work regardless
                // of whether the UI thread is busy or not.
                // TODO: try to remove this.
                sendEmptyMessage(BLANK);
                lock.lock();
                try {
                    if (isBusy()) {
                        busyCondition.await();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }

        @Override
        public boolean queueIdle() {
            // When the UI is not busy, schedule some work.
            sendEmptyMessage(BLANK);
            return true;
        }
    }

    /**
     * A cache of markers representing individual ClusterItems.
     */
    private static class MarkerCache<T> {
        private Map<T, Marker> mCache = new HashMap<T, Marker>();
        private Map<Marker, T> mCacheReverse = new HashMap<Marker, T>();

        public Marker get(T item) {
            return mCache.get(item);
        }

        public T get(Marker m) {
            return mCacheReverse.get(m);
        }

        public void put(T item, Marker m) {
            mCache.put(item, m);
            mCacheReverse.put(m, item);
        }

        public void remove(Marker m) {
            T item = mCacheReverse.get(m);
            mCacheReverse.remove(m);
            mCache.remove(item);
        }
    }

    /**
     * Get the marker from a ClusterItem
     *
     * @param clusterItem ClusterItem which you will obtain its marker
     * @return a marker from a ClusterItem or null if it does not exists
     */
    public Marker getMarker(T clusterItem) {
        return mMarkerCache.get(clusterItem);
    }

    /**
     * Get the marker from a Cluster
     *
     * @param cluster which you will obtain its marker
     * @return a marker from a cluster or null if it does not exists
     */
    public Marker getMarker(Cluster<T> cluster) {
        return mClusterToMarker.get(cluster);
    }

    /**
     * Get the ClusterItem from a marker
     *
     * @param marker which you will obtain its ClusterItem
     * @return a ClusterItem from a marker or null if it does not exists
     */
    public T getClusterItem(Marker marker) {
        return mMarkerCache.get(marker);
    }

    /**
     * Get the Cluster from a marker
     *
     * @param marker which you will obtain its Cluster
     * @return a Cluster from a marker or null if it does not exists
     */
    public Cluster<T> getCluster(Marker marker) {
        return mMarkerToCluster.get(marker);
    }

    /**
     * Creates markerWithPosition(s) for a particular cluster, animating it if necessary.
     */
    private class CreateMarkerTask {
        private final Cluster<T> clusterAdding;
        private final Set<MarkerWithPosition> markersAdded;

        /**
         * @param clusterAdding the cluster to render.
         * @param markersAdded  a collection of markers to append any created markers.
         */
        public CreateMarkerTask(Cluster<T> clusterAdding, Set<MarkerWithPosition> markersAdded/*, LatLng animateFrom*/) {
            this.clusterAdding = clusterAdding;
            this.markersAdded = markersAdded;
        }

        private void perform(MarkerModifier markerModifier) {
            // Don't show small clusters. Render the markers inside, instead.
            boolean shouldRenderAsCluster = shouldRenderAsCluster(clusterAdding);

            STClusterUtil.log("CreateMarkerTask add cluster items to map (not cluster self)");

            for (T item : clusterAdding.getItems()) {
                Marker marker = mMarkerCache.get(item);
                MarkerWithPosition markerWithPosition;
                if (marker == null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(item.getPosition());
                    markerOptions.icon(item.getBitmapDescriptor());
                    marker = clusterManager.getMarkerCollection().addMarker(markerOptions);
                    markerWithPosition = new MarkerWithPosition(marker);
                    mMarkerCache.put(item, marker);
                } else {
                    markerWithPosition = new MarkerWithPosition(marker);
                }
                STClusterUtil.log("CreateMarkerTask onClusterItemRendered");
                markersAdded.add(markerWithPosition);

                if (shouldRenderAsCluster) break;
            }
        }
    }

    /**
     * A Marker and its position. Marker.getPosition() must be called from the UI thread, so this
     * object allows lookup from other threads.
     */
    private static class MarkerWithPosition {
        private final Marker marker;
        private final LatLng position;

        private MarkerWithPosition(Marker marker) {
            this.marker = marker;
            position = marker.getPosition();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof MarkerWithPosition) {
                return marker.equals(((MarkerWithPosition) other).marker);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return marker.hashCode();
        }
    }
}