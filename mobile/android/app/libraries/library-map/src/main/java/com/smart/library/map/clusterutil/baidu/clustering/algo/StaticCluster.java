/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.smart.library.map.clusterutil.baidu.clustering.algo;

import androidx.annotation.Nullable;

import com.smart.library.map.clusterutil.baidu.clustering.Cluster;
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem;
import com.baidu.mapapi.model.LatLng;
import com.smart.library.map.clusterutil.baidu.projection.Bounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A cluster whose center is determined upon creation.
 */
public class StaticCluster<T extends ClusterItem> implements Cluster<T> {
    private final LatLng mCenter;
    private final List<T> mItems = new ArrayList<T>();

    public StaticCluster(LatLng center) {
        mCenter = center;
    }

    public boolean add(T t) {
        return mItems.add(t);
    }

    @Override
    public LatLng getPosition() {
        return mCenter;
    }

    public boolean remove(T t) {
        return mItems.remove(t);
    }

    @Override
    public Collection<T> getItems() {
        return mItems;
    }

    @Override
    public int getSize() {
        return mItems.size();
    }

    private Bounds bounds;

    @Nullable
    @Override
    public Bounds getSearchBounds() {
        return bounds;
    }

    @Override
    public void setSearchBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public String toString() {
        return "StaticCluster{"
                + "mCenter=" + mCenter
                + ", mItems.size=" + mItems.size()
                + '}';
    }
}