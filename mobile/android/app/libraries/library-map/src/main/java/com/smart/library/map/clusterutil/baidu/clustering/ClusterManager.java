/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.smart.library.map.clusterutil.baidu.clustering;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.smart.library.map.clusterutil.baidu.MarkerManager;
import com.smart.library.map.clusterutil.baidu.clustering.algo.Algorithm;
import com.smart.library.map.clusterutil.baidu.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.smart.library.map.clusterutil.baidu.clustering.algo.PreCachingAlgorithmDecorator;
import com.smart.library.map.clusterutil.baidu.clustering.view.ClusterRenderer;
import com.smart.library.map.clusterutil.baidu.clustering.view.LessMoreClusterRenderer;
import com.smart.library.map.clusterutil.baidu.util.STClusterUtil;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Groups many items on a map based on zoom level.
 * <p/>
 * ClusterManager should be added to the map
 * <li>
 * <p>
 */
public class ClusterManager<T extends ClusterItem> implements BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMarkerClickListener {
    private final MarkerManager mMarkerManager;
    private final MarkerManager.Collection mMarkers;
    private final MarkerManager.Collection mClusterMarkers;

    private Algorithm<T> mAlgorithm;
    private final ReadWriteLock mAlgorithmLock = new ReentrantReadWriteLock();
    private ClusterRenderer<T> mRenderer;

    private BaiduMap mMap;
    private MapStatus mPreviousCameraPosition;
    private ClusterTask mClusterTask;
    private final ReadWriteLock mClusterTaskLock = new ReentrantReadWriteLock();

    private OnClusterItemClickListener<T> mOnClusterItemClickListener;
    private OnClusterInfoWindowClickListener<T> mOnClusterInfoWindowClickListener;
    private OnClusterItemInfoWindowClickListener<T> mOnClusterItemInfoWindowClickListener;
    private OnClusterClickListener<T> mOnClusterClickListener;

    public ClusterManager(Context context, BaiduMap map) {
        this(context, map, new MarkerManager(map));
    }

    public ClusterManager(Context context, BaiduMap map, MarkerManager markerManager) {
        mMap = map;
        mMarkerManager = markerManager;
        mClusterMarkers = markerManager.newCollection();
        mMarkers = markerManager.newCollection();
        mRenderer = new LessMoreClusterRenderer<T>(context, map, this);
        mAlgorithm = new PreCachingAlgorithmDecorator<T>(new NonHierarchicalDistanceBasedAlgorithm<T>());
        mClusterTask = new ClusterTask();
        mRenderer.onAdd();
    }

    public MarkerManager.Collection getMarkerCollection() {
        return mMarkers;
    }

    public MarkerManager.Collection getClusterMarkerCollection() {
        return mClusterMarkers;
    }

    public MarkerManager getMarkerManager() {
        return mMarkerManager;
    }

    public void setRenderer(ClusterRenderer<T> view) {
        mRenderer.setOnClusterClickListener(null);
        mRenderer.setOnClusterItemClickListener(null);
        mClusterMarkers.clear();
        mMarkers.clear();
        mRenderer.onRemove();
        mRenderer = view;
        mRenderer.onAdd();
        mRenderer.setOnClusterClickListener(mOnClusterClickListener);
        mRenderer.setOnClusterInfoWindowClickListener(mOnClusterInfoWindowClickListener);
        mRenderer.setOnClusterItemClickListener(mOnClusterItemClickListener);
        mRenderer.setOnClusterItemInfoWindowClickListener(mOnClusterItemInfoWindowClickListener);
        cluster();
    }

    public void setAlgorithm(Algorithm<T> algorithm) {
        mAlgorithmLock.writeLock().lock();
        try {
            if (mAlgorithm != null) {
                algorithm.addItems(mAlgorithm.getItems());
            }
            mAlgorithm = new PreCachingAlgorithmDecorator<T>(algorithm);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
        cluster();
    }

    public void clearItems() {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.clearItems();
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    public void addItems(Collection<T> items) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.addItems(items);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }

    }

    public void addItem(T myItem) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.addItem(myItem);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    public void removeItem(T item) {
        mAlgorithmLock.writeLock().lock();
        try {
            mAlgorithm.removeItem(item);
        } finally {
            mAlgorithmLock.writeLock().unlock();
        }
    }

    /**
     * Force a re-cluster. You may want to call this after adding new item(s).
     */
    public void cluster() {
        mClusterTaskLock.writeLock().lock();
        try {
            // Attempt to cancel the in-flight request.
            mClusterTask.cancel(true);
            mClusterTask = new ClusterTask();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                STClusterUtil.log("cluster ClusterTask execute ...");
                mClusterTask.execute(mMap.getMapStatus().zoom);
            } else {
                mClusterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMap.getMapStatus().zoom);
            }
        } finally {
            mClusterTaskLock.writeLock().unlock();
        }
    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus status, int reason) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        if (mRenderer instanceof BaiduMap.OnMapStatusChangeListener) {
            ((BaiduMap.OnMapStatusChangeListener) mRenderer).onMapStatusChange(mapStatus);
        }

        // Don't re-compute clusters if the map has just been panned/tilted/rotated.
        MapStatus position = mMap.getMapStatus();
        if (mPreviousCameraPosition != null && mPreviousCameraPosition.zoom == position.zoom) {
            return;
        }
        mPreviousCameraPosition = mMap.getMapStatus();

        STClusterUtil.log("onMapStatusChange zoom=" + position.zoom + " will do cluster ...");
        cluster();
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return getMarkerManager().onMarkerClick(marker);
    }

    /**
     * Runs the clustering algorithm in a background thread, then re-paints when results come back.
     */
    private class ClusterTask extends AsyncTask<Float, Void, Set<? extends Cluster<T>>> {
        @Override
        protected Set<? extends Cluster<T>> doInBackground(Float... zoom) {
            mAlgorithmLock.readLock().lock();
            try {
                STClusterUtil.log("ClusterTask doInBackground getClusters ...");
                return mAlgorithm.getClusters(zoom[0]);
            } finally {
                mAlgorithmLock.readLock().unlock();
            }
        }

        @Override
        protected void onPostExecute(Set<? extends Cluster<T>> clusters) {
            STClusterUtil.log("ClusterTask onPostExecute onClustersChanged ...");
            mRenderer.onClustersChanged(clusters);
        }
    }

    /**
     * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
     * the ClusterManager must be added as a click listener to the map.
     */
    public void setOnClusterClickListener(OnClusterClickListener<T> listener) {
        mOnClusterClickListener = listener;
        mRenderer.setOnClusterClickListener(listener);
    }

    /**
     * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
     * the ClusterManager must be added as a info window click listener to the map.
     */
    public void setOnClusterInfoWindowClickListener(OnClusterInfoWindowClickListener<T> listener) {
        mOnClusterInfoWindowClickListener = listener;
        mRenderer.setOnClusterInfoWindowClickListener(listener);
    }

    /**
     * Sets a callback that's invoked when an individual ClusterItem is tapped. Note: For this
     * listener to function, the ClusterManager must be added as a click listener to the map.
     */
    public void setOnClusterItemClickListener(OnClusterItemClickListener<T> listener) {
        mOnClusterItemClickListener = listener;
        mRenderer.setOnClusterItemClickListener(listener);
    }

    /**
     * Sets a callback that's invoked when an individual ClusterItem's Info Window is tapped. Note: For this
     * listener to function, the ClusterManager must be added as a info window click listener to the map.
     */
    public void setOnClusterItemInfoWindowClickListener(OnClusterItemInfoWindowClickListener<T> listener) {
        mOnClusterItemInfoWindowClickListener = listener;
        mRenderer.setOnClusterItemInfoWindowClickListener(listener);
    }

    /**
     * Called when a Cluster is clicked.
     */
    public interface OnClusterClickListener<T extends ClusterItem> {
        public boolean onClusterClick(Cluster<T> cluster);
    }

    /**
     * Called when a Cluster's Info Window is clicked.
     */
    public interface OnClusterInfoWindowClickListener<T extends ClusterItem> {
        public void onClusterInfoWindowClick(Cluster<T> cluster);
    }

    /**
     * Called when an individual ClusterItem is clicked.
     */
    public interface OnClusterItemClickListener<T extends ClusterItem> {
        public boolean onClusterItemClick(T item);
    }

    /**
     * Called when an individual ClusterItem's Info Window is clicked.
     */
    public interface OnClusterItemInfoWindowClickListener<T extends ClusterItem> {
        public void onClusterItemInfoWindowClick(T item);
    }
}
