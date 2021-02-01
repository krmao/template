package com.smart.library.map.clusterutil.baidu

import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.map.MarkerOptions

class STMarkerManager(val map: BaiduMap?) : MarkerManager(map) {

    override fun newCollection(): Collection {
        return STCollection()
    }

    override fun newCollection(id: String?): Collection {
        require(mNamedCollections[id] == null) { "collection id is not unique: $id" }
        val collection: Collection = STCollection()
        mNamedCollections[id] = collection
        return collection
    }

    inner class STCollection : MarkerManager.Collection() {

        override fun addMarker(opts: MarkerOptions?): Marker {
            return super.addMarker(opts)
        }

        override fun remove(marker: Marker?): Boolean {
            return super.remove(marker)
        }

        override fun clear() {
            super.clear()
        }
    }

}