/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.smart.library.map.clusterutil.baidu.clustering.algo

import androidx.collection.LruCache
import com.smart.library.map.clusterutil.baidu.clustering.Cluster
import com.smart.library.map.clusterutil.baidu.clustering.ClusterItem
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Optimistically fetch clusters for adjacent zoom levels, caching them as necessary.
 */
@Suppress("unused")
class STPreCachingAlgorithmDecorator<T : ClusterItem?>(private val algorithm: STAlgorithm<T>) : STAlgorithm<T> {

    // TODO: evaluate maxSize parameter for LruCache.
    private val cache = LruCache<Int, Set<Cluster<T>>>(5)
    private val cacheLock: ReadWriteLock = ReentrantReadWriteLock()

    override fun addItem(item: T) {
        algorithm.addItem(item)
        clearCache()
    }

    override fun addItems(items: Collection<T>) {
        algorithm.addItems(items)
        clearCache()
    }

    override fun clearItems() {
        algorithm.clearItems()
        clearCache()
    }

    override fun removeItem(item: T) {
        algorithm.removeItem(item)
        clearCache()
    }

    private fun clearCache() {
        cache.evictAll()
    }

    override fun getClusters(zoom: Double): Set<Cluster<T>> {
        val discreteZoom = zoom.toInt()
        val results = getClustersInternal(discreteZoom)
        // TODO: Check if requests are already in-flight.
        if (cache[discreteZoom + 1] == null) {
            Thread(PreCacheRunnable(discreteZoom + 1)).start()
        }
        if (cache[discreteZoom - 1] == null) {
            Thread(PreCacheRunnable(discreteZoom - 1)).start()
        }
        return results
    }

    override fun getItems(): Collection<T> = algorithm.getItems()

    private fun getClustersInternal(discreteZoom: Int): Set<Cluster<T>> {
        var results: Set<Cluster<T>>?
        cacheLock.readLock().lock()
        results = cache[discreteZoom]
        cacheLock.readLock().unlock()
        if (results == null) {
            cacheLock.writeLock().lock()
            results = cache[discreteZoom]
            if (results == null) {
                results = algorithm.getClusters(discreteZoom.toDouble())
                cache.put(discreteZoom, results)
            }
            cacheLock.writeLock().unlock()
        }
        return results
    }

    private inner class PreCacheRunnable(private val mZoom: Int) : Runnable {
        override fun run() {
            try {
                // Wait between 500 - 1000 ms.
                Thread.sleep((Math.random() * 500 + 500).toLong())
            } catch (e: InterruptedException) {
                // ignore. keep going.
            }
            getClustersInternal(mZoom)
        }
    }
}