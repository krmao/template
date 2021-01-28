/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.smart.library.map.clusterutil.baidu.clustering.algo

import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem

/**
 * Logic for computing clusters
 */
interface STAlgorithm<T : ClusterItem?> {
    fun addItem(item: T)
    fun addItems(items: Collection<T>)
    fun removeItem(item: T)
    fun clearItems()
    fun getItems(): Collection<T>
    fun getClusters(zoom: Double): Set<Cluster<T>>
}