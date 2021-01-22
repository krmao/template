/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.smart.library.map.clusterutil.baidu.clustering;


import androidx.annotation.Nullable;

import com.baidu.mapapi.model.LatLng;
import com.smart.library.map.clusterutil.baidu.projection.Bounds;

import java.util.Collection;

/**
 * A collection of ClusterItems that are nearby each other.
 */
public interface Cluster<T extends com.smart.library.map.clusterutil.baidu.clustering.ClusterItem> {
    public LatLng getPosition();

    Collection<T> getItems();

    int getSize();

    @Nullable
    Bounds getSearchBounds();

    void setSearchBounds(Bounds bounds);
}