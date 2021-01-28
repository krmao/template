/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.smart.library.map.clusterutil.baidu.clustering.algo

import com.baidu.mapapi.model.LatLng
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem
import com.smart.library.map.clusterutil.baidu.projection.Bounds
import java.util.*

/**
 * A cluster whose center is determined upon creation.
 */
class STStaticCluster<T : ClusterItem?>(private val center: LatLng) : Cluster<T> {
    private val items: MutableList<T> = ArrayList()
    private var bounds: Bounds? = null

    fun add(t: T): Boolean = items.add(t)
    override fun getPosition(): LatLng = center
    fun remove(t: T): Boolean = items.remove(t)
    override fun getItems(): Collection<T> = items
    override fun getSize(): Int = items.size

    override fun getSearchBounds(): Bounds? = bounds
    override fun setSearchBounds(bounds: Bounds?) {
        this.bounds = bounds
    }

    override fun toString(): String {
        return "STStaticCluster{mCenter=$center, mItems.size=${items.size}}"
    }
}