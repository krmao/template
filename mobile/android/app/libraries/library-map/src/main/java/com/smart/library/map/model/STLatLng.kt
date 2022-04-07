package com.smart.library.map.model

import android.os.Parcel
import android.os.Parcelable
import com.smart.library.util.STValueUtil
import java.io.Serializable
import kotlin.math.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class STLatLng(val latitude: Double = 0.0, val longitude: Double = 0.0, val type: STLatLngType = STLatLngType.GCJ02) : Parcelable, Serializable {
    fun copy(): STLatLng = STLatLng(latitude, longitude, type)

    fun isNotValid(): Boolean = !isValid()

    fun isValid(): Boolean = isValidLatLng(latitude, longitude)

    fun convertTo(latLngType: STLatLngType?): STLatLng? = convert(this, latLngType)

    override fun equals(other: Any?): Boolean = this === other || (other is STLatLng && STValueUtil.isAEqualB(latitude, other.latitude) && STValueUtil.isAEqualB(longitude, other.longitude))

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }

    override fun toString(): String = "(latitude=$latitude, longitude=$longitude, type=$type)"

    constructor(source: Parcel) : this(
        source.readDouble(),
        source.readDouble(),
        STLatLngType.values()[source.readInt()]
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(latitude)
        writeDouble(longitude)
        writeInt(type.ordinal)
    }

    companion object {
        const val PI = 3.1415926535897932384626 // 圆周率

        const val EARTH_RADIUS = 6378245.0      // 椭球长半径

        const val EE = 0.00669342162296594323   // 偏心率

        @JvmStatic
        val NOT_VALID = STLatLng()

        private val taiwanLatLngList = arrayOf(arrayOf(STLatLng(120.492553, 25.319199), STLatLng(122.085570, 23.155511)), arrayOf(STLatLng(119.240111, 23.951114), STLatLng(121.646117, 21.611470)))

        private val hongkongLatLngList = arrayOf(arrayOf(STLatLng(113.9970582, 22.49289015), STLatLng(114.451617, 22.14861424)), arrayOf(STLatLng(113.8405030, 22.4307039), STLatLng(114.37196669, 22.12889756)))

        private val macaoLatLngList = arrayOf(arrayOf(STLatLng(113.528594, 22.2303135), STLatLng(113.607558, 22.125717)), arrayOf(STLatLng(113.450660, 22.1511581), STLatLng(113.596572, 22.064003)))

        private val chinaLatLngList = arrayOf(
            arrayOf(STLatLng(85.374342, 41.697126), STLatLng(124.486996, 28.705892)),
            arrayOf(STLatLng(98.942349, 28.714002), STLatLng(122.527683, 23.331042)),
            arrayOf(STLatLng(108.012216, 23.415965), STLatLng(119.252965, 17.294543)),
            arrayOf(STLatLng(120.025651, 51.286036), STLatLng(126.391116, 41.330674)),
            arrayOf(STLatLng(82.936701, 46.727439), STLatLng(90.553182, 41.621242)),
            arrayOf(STLatLng(126.188746, 48.211817), STLatLng(129.757821, 42.485061)),
            arrayOf(STLatLng(129.518656, 47.611932), STLatLng(131.46877, 44.959641)),
            arrayOf(STLatLng(131.376783, 47.487374), STLatLng(133.805226, 46.225387)),
            arrayOf(STLatLng(79.753968, 41.87613), STLatLng(85.604309, 30.872189)),
            arrayOf(STLatLng(113.457816, 44.802677), STLatLng(120.117638, 41.517618)),
            arrayOf(STLatLng(118.977005, 23.526282), STLatLng(121.975765, 21.629857)),
            arrayOf(STLatLng(109.667973, 17.321053), STLatLng(119.050594, 14.580095)),
            arrayOf(STLatLng(76.258482, 40.359687), STLatLng(80.01153, 35.915704)),
            arrayOf(STLatLng(90.534784, 44.710915), STLatLng(94.030271, 41.531444)),
            arrayOf(STLatLng(80.710628, 45.077082), STLatLng(83.028687, 41.862379)),
            arrayOf(STLatLng(85.93546, 48.414308), STLatLng(88.437492, 46.645143)),
            arrayOf(STLatLng(93.975079, 42.559912), STLatLng(101.462779, 41.600531)),
            arrayOf(STLatLng(93.956681, 44.157262), STLatLng(95.354876, 42.491869)),
            arrayOf(STLatLng(116.69574, 46.301949), STLatLng(120.117638, 44.619006)),
            arrayOf(STLatLng(116.401384, 49.444657), STLatLng(120.191227, 48.057877)),
            arrayOf(STLatLng(121.000708, 53.244099), STLatLng(124.569783, 51.210984)),
            arrayOf(STLatLng(106.724405, 42.232628), STLatLng(113.494611, 41.683336)),
            arrayOf(STLatLng(112.133211, 44.355602), STLatLng(113.5682, 42.123151)),
            arrayOf(STLatLng(110.918989, 43.155464), STLatLng(112.2068, 42.232628)),
            arrayOf(STLatLng(115.150367, 45.324216), STLatLng(116.76933, 44.724032)),
            arrayOf(STLatLng(126.299129, 49.588397), STLatLng(128.102064, 48.057877)),
            arrayOf(STLatLng(128.06527, 49.131761), STLatLng(129.757821, 48.131826)),
            arrayOf(STLatLng(129.721026, 48.62209), STLatLng(130.530508, 47.611932)),
            arrayOf(STLatLng(124.349016, 52.822665), STLatLng(125.710416, 51.095279)),
            arrayOf(STLatLng(122.325313, 28.884167), STLatLng(123.760302, 25.662561)),
            arrayOf(STLatLng(111.029373, 14.651757), STLatLng(118.388292, 10.6053)),
            arrayOf(STLatLng(109.778357, 10.095218), STLatLng(109.778357, 10.095218)),
            arrayOf(STLatLng(109.631178, 10.459649), STLatLng(116.548562, 7.753573)),
            arrayOf(STLatLng(110.514249, 7.826971), STLatLng(113.678584, 4.73448)),
            arrayOf(STLatLng(124.330619, 41.399976), STLatLng(125.48045, 40.68961)),
            arrayOf(STLatLng(126.345123, 42.51229), STLatLng(128.046872, 41.827986)),
            arrayOf(STLatLng(127.973283, 42.539507), STLatLng(129.104717, 42.143692)),
            arrayOf(STLatLng(74.510739, 40.16236), STLatLng(76.350468, 38.678393)),
            arrayOf(STLatLng(119.087389, 21.629857), STLatLng(120.706351, 20.142916)),
            arrayOf(STLatLng(106.853187, 23.339537), STLatLng(108.067408, 21.990651)),
            arrayOf(STLatLng(129.707229, 44.975967), STLatLng(130.985841, 43.017244)),
            arrayOf(STLatLng(130.958245, 44.582859), STLatLng(131.169814, 43.104932)),
            arrayOf(STLatLng(131.418177, 46.247729), STLatLng(133.129126, 45.359896)),
            arrayOf(STLatLng(133.073934, 48.054793), STLatLng(134.269758, 47.409374)),
            arrayOf(STLatLng(99.701237, 23.386249), STLatLng(101.577762, 22.174986)),
            arrayOf(STLatLng(100.179567, 22.243514), STLatLng(101.559364, 21.745927)),
            arrayOf(STLatLng(101.485775, 23.437187), STLatLng(104.24537, 22.875776)),
            arrayOf(STLatLng(98.008686, 25.240784), STLatLng(99.057332, 24.181992)),
            arrayOf(STLatLng(124.463999, 40.686109), STLatLng(124.905534, 40.461646)),
            arrayOf(STLatLng(125.457453, 41.334141), STLatLng(126.055365, 40.979564)),
            arrayOf(STLatLng(126.368119, 41.824546), STLatLng(126.607284, 41.645397)),
            arrayOf(STLatLng(125.47585, 40.979564), STLatLng(125.687419, 40.853958)),
            arrayOf(STLatLng(124.477797, 40.46516), STLatLng(124.72846, 40.343852)),
            arrayOf(STLatLng(124.470898, 40.347371), STLatLng(124.618076, 40.285757)),
            arrayOf(STLatLng(124.891736, 40.694862), STLatLng(125.153898, 40.607283)),
            arrayOf(STLatLng(126.046166, 41.332407), STLatLng(126.262335, 41.165784)),
            arrayOf(STLatLng(127.214395, 41.836586), STLatLng(128.083667, 41.546995)),
            arrayOf(STLatLng(126.386516, 50.257998), STLatLng(126.386516, 50.257998)),
            arrayOf(STLatLng(126.280732, 50.257998), STLatLng(127.513351, 49.580921)),
            arrayOf(STLatLng(126.36352, 50.934256), STLatLng(127.117809, 50.225552)),
            arrayOf(STLatLng(125.669022, 52.39849), STLatLng(126.276133, 51.247082)),
            arrayOf(STLatLng(80.948643, 30.905163), STLatLng(81.403976, 30.280446)),
            arrayOf(STLatLng(83.574857, 30.911112), STLatLng(85.488176, 29.214825)),
            arrayOf(STLatLng(98.136317, 28.872274), STLatLng(99.079179, 27.642374)),
            arrayOf(STLatLng(111.25399, 21.257314), STLatLng(118.79061, 11.683099)),
            arrayOf(STLatLng(110.57284, 11.510906), STLatLng(117.45028, 9.2850385)),
            arrayOf(STLatLng(109.03475, 8.7533664), STLatLng(114.37410, 5.5489422))
        )

        private val chinaBigLatLngList = arrayOf(
            arrayOf(STLatLng(73.083331, 54.006559), STLatLng(135.266195, 17.015367)),
            arrayOf(STLatLng(109.806384, 17.579908), STLatLng(112.529184, 3.638301)),
            arrayOf(STLatLng(112.124443, 17.773916), STLatLng(115.583135, 7.159653)),
            arrayOf(STLatLng(115.251984, 17.562261), STLatLng(117.422865, 9.54155)),
            arrayOf(STLatLng(117.054919, 17.773916), STLatLng(118.931443, 11.507795))
        )

        private val chinaSideLatLngList = arrayOf(
            arrayOf(STLatLng(125.478833, 40.538425), STLatLng(135.928497, 16.590044)),
            arrayOf(STLatLng(128.054454, 54.43779), STLatLng(136.370032, 49.918776)),
            arrayOf(STLatLng(89.567309, 54.351906), STLatLng(115.617882, 47.881407)),
            arrayOf(STLatLng(71.832315, 54.566279), STLatLng(82.28198, 46.323836)),
            arrayOf(STLatLng(72.788974, 28.001436), STLatLng(85.88785, 16.590044)),
            arrayOf(STLatLng(92.510877, 48.029708), STLatLng(111.570476, 45.034268)),
            arrayOf(STLatLng(85.593493, 26.157025), STLatLng(97.294174, 16.519064)),
            arrayOf(STLatLng(97.073406, 20.935216), STLatLng(107.743838, 16.305964)),
            arrayOf(STLatLng(98.324422, 45.190596), STLatLng(109.142033, 42.854577)),
            arrayOf(STLatLng(71.979493, 45.863038), STLatLng(78.896877, 41.817208)),
            arrayOf(STLatLng(72.374784, 34.326035), STLatLng(78.372303, 27.905294)),
            arrayOf(STLatLng(120.131867, 19.569888), STLatLng(125.411891, 15.992375))
        )

        @JvmStatic
        fun isOversea(latLng: STLatLng?): Boolean = latLng != null && (!isInLatLngRect(latLng.longitude, latLng.latitude, chinaBigLatLngList) || isInLatLngRect(latLng.longitude, latLng.latitude, chinaSideLatLngList))

        @JvmStatic
        fun isInChina(latLng: STLatLng?): Boolean = latLng != null && isInLatLngRect(latLng.longitude, latLng.latitude, chinaLatLngList)

        @JvmStatic
        fun isInMainland(latLng: STLatLng?): Boolean = latLng != null && isInChina(latLng) && !isInTaiwan(latLng) && !isInHongkong(latLng) && !isInMacao(latLng)

        @JvmStatic
        fun isInTaiwan(latLng: STLatLng?): Boolean = latLng != null && isInLatLngRect(latLng.longitude, latLng.latitude, taiwanLatLngList)

        @JvmStatic
        fun isInHongkong(latLng: STLatLng?): Boolean = latLng != null && isInLatLngRect(latLng.longitude, latLng.latitude, hongkongLatLngList)

        @JvmStatic
        fun isInMacao(latLng: STLatLng?): Boolean = latLng != null && isInLatLngRect(latLng.longitude, latLng.latitude, macaoLatLngList)

        @JvmStatic
        fun isInLatLngRect(inx: Double, iny: Double, latLngList: Array<Array<STLatLng>>): Boolean {
            val zoomTimes = 1
            val lx = inx * zoomTimes
            val ly = iny * zoomTimes
            var startX: Double
            var startY: Double
            var endX: Double
            var endY: Double
            for (rect in latLngList) {
                startX = rect[0].longitude * zoomTimes
                startY = rect[0].latitude * zoomTimes
                endX = rect[1].longitude * zoomTimes
                endY = rect[1].latitude * zoomTimes
                if (lx in startX..endX && ly <= startY && ly >= endY) return true //只要在任何一个区域内就行
            }
            return false
        }

        @JvmStatic
        fun isValidLatLng(latitude: Double?, longitude: Double?) = if (latitude == null || longitude == null) false else (abs(latitude) <= 90 && abs(longitude) <= 180)

        @JvmStatic
        @Suppress("NON_EXHAUSTIVE_WHEN")
        fun convert(fromLatLon: STLatLng?, toLatLngType: STLatLngType?): STLatLng? {
            if (fromLatLon?.isValid() == true && toLatLngType != null) {
                if (fromLatLon.type != toLatLngType) {

                    fun convertGCJ02ToBD09(gcj02Lat: Double, gcj02Lon: Double): STLatLng {
                        val z = sqrt(gcj02Lon * gcj02Lon + gcj02Lat * gcj02Lat) + 0.00002 * sin(gcj02Lat * kotlin.math.PI)
                        val theta = atan2(gcj02Lat, gcj02Lon) + 0.000003 * cos(gcj02Lon * kotlin.math.PI)
                        return STLatLng(z * sin(theta) + 0.006, z * cos(theta) + 0.0065, STLatLngType.BD09)
                    }

                    fun convertGCJ02ToWGS84(gcj02Lat: Double, gcj02Lon: Double): STLatLng {
                        var dLat = transformLat(gcj02Lon - 105.0, gcj02Lat - 35.0)
                        var dLon = transformLon(gcj02Lon - 105.0, gcj02Lat - 35.0)
                        val radLat = gcj02Lat / 180.0 * kotlin.math.PI
                        var magic = sin(radLat)
                        magic = 1 - EE * magic * magic
                        val sqrtMagic = sqrt(magic)
                        dLat = dLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * kotlin.math.PI)
                        dLon = dLon * 180.0 / (EARTH_RADIUS / sqrtMagic * cos(radLat) * kotlin.math.PI)
                        val mgLat = gcj02Lat + dLat
                        val mgLon = gcj02Lon + dLon
                        return STLatLng(mgLat, mgLon, STLatLngType.WGS84)
                    }

                    fun convertBD09ToGCJ02(bd09Lat: Double, bd09Lon: Double): STLatLng {
                        val x = bd09Lon - 0.0065
                        val y = bd09Lat - 0.006
                        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * kotlin.math.PI)
                        val theta = atan2(y, x) - 0.000003 * cos(x * kotlin.math.PI)
                        return STLatLng(z * sin(theta), z * cos(theta), STLatLngType.GCJ02)
                    }

                    fun convertWGS84ToGCJ02(wgs84Lat: Double, wgs84Lon: Double): STLatLng {
                        var transformLat = transformLat(wgs84Lon - 105.0, wgs84Lat - 35.0)
                        var transformLon = transformLon(wgs84Lon - 105.0, wgs84Lat - 35.0)
                        val radLat = wgs84Lat / 180.0 * kotlin.math.PI
                        var magic = sin(radLat)
                        magic = 1 - EE * magic * magic
                        val sqrtMagic = sqrt(magic)
                        transformLat = transformLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * kotlin.math.PI)
                        transformLon = transformLon * 180.0 / (EARTH_RADIUS / sqrtMagic * cos(radLat) * kotlin.math.PI)
                        return STLatLng(wgs84Lat + transformLat, wgs84Lon + transformLon, STLatLngType.GCJ02)
                    }

                    when (fromLatLon.type) {
                        STLatLngType.GCJ02 -> {
                            val gcj02Lat: Double = fromLatLon.latitude
                            val gcj02Lon: Double = fromLatLon.longitude

                            when (toLatLngType) {
                                STLatLngType.WGS84 -> return convertGCJ02ToWGS84(gcj02Lat, gcj02Lon)
                                STLatLngType.BD09 -> return convertGCJ02ToBD09(gcj02Lat, gcj02Lon)
                            }
                        }
                        STLatLngType.WGS84 -> {
                            val wgs84Lat: Double = fromLatLon.latitude
                            val wgs84Lon: Double = fromLatLon.longitude

                            when (toLatLngType) {
                                STLatLngType.GCJ02 -> return convertWGS84ToGCJ02(wgs84Lat, wgs84Lon)
                                STLatLngType.BD09 -> {
                                    val gcj02LatLon = convertWGS84ToGCJ02(wgs84Lat, wgs84Lon)
                                    return convertGCJ02ToBD09(gcj02LatLon.latitude, gcj02LatLon.longitude)
                                }
                            }
                        }
                        STLatLngType.BD09 -> {
                            val bd09Lat: Double = fromLatLon.latitude
                            val bd09Lon: Double = fromLatLon.longitude
                            when (toLatLngType) {
                                STLatLngType.GCJ02 -> return convertBD09ToGCJ02(bd09Lat, bd09Lon)
                                STLatLngType.WGS84 -> {
                                    val gcj02LatLng = convertBD09ToGCJ02(bd09Lat, bd09Lon)
                                    return convertGCJ02ToWGS84(gcj02LatLng.latitude, gcj02LatLng.longitude)
                                }
                            }
                        }
                    }
                } else return fromLatLon.copy()
            }
            return null
        }

        private fun transformLat(x: Double, y: Double): Double {
            var ret = (-100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(abs(x)))
            ret += (20.0 * sin(6.0 * x * kotlin.math.PI) + 20.0 * sin(2.0 * x * kotlin.math.PI)) * 2.0 / 3.0
            ret += (20.0 * sin(y * kotlin.math.PI) + 40.0 * sin(y / 3.0 * kotlin.math.PI)) * 2.0 / 3.0
            ret += (160.0 * sin(y / 12.0 * kotlin.math.PI) + 320 * sin(y * kotlin.math.PI / 30.0)) * 2.0 / 3.0
            return ret
        }

        private fun transformLon(x: Double, y: Double): Double {
            var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(abs(x))
            ret += (20.0 * sin(6.0 * x * kotlin.math.PI) + 20.0 * sin(2.0 * x * kotlin.math.PI)) * 2.0 / 3.0
            ret += (20.0 * sin(x * kotlin.math.PI) + 40.0 * sin(x / 3.0 * kotlin.math.PI)) * 2.0 / 3.0
            ret += (150.0 * sin(x / 12.0 * kotlin.math.PI) + 300.0 * sin(x / 30.0 * kotlin.math.PI)) * 2.0 / 3.0
            return ret
        }

        /**
         * cal by amap
         */
        @JvmStatic
        fun getDistanceByGCJ02(fromGCJ02Lat: Double, fromGCJ02Lon: Double, toGCJ02Lat: Double, toGCJ02Lon: Double): Float {
            return try {
                var var4 = fromGCJ02Lon
                var var6 = fromGCJ02Lat
                var var8 = toGCJ02Lon
                var var10 = toGCJ02Lat
                var4 *= 0.01745329251994329
                var6 *= 0.01745329251994329
                var8 *= 0.01745329251994329
                var10 *= 0.01745329251994329
                val var12 = sin(var4)
                val var14 = sin(var6)
                val var16 = cos(var4)
                val var18 = cos(var6)
                val var20 = sin(var8)
                val var22 = sin(var10)
                val var24 = cos(var8)
                val var26 = cos(var10)
                val var28 = DoubleArray(3)
                val var29 = DoubleArray(3)
                var28[0] = var18 * var16
                var28[1] = var18 * var12
                var28[2] = var14
                var29[0] = var26 * var24
                var29[1] = var26 * var20
                var29[2] = var22
                val var30 = sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]))
                (asin(var30 / 2.0) * 1.27420015798544E7).toFloat()
            } catch (e: Throwable) {
                e.printStackTrace()
                0.0f
            }
        }

        @JvmStatic
        fun getLatLngTypeByMapType(latLng: STLatLng, mapType: STMapType): STLatLngType {
            val isOversea = isOversea(latLng)
            val isTaiwan = isInTaiwan(latLng)
            val isMainland = isInMainland(latLng)
            if (!isOversea) {
                when {
                    mapType === STMapType.BAIDU -> return STLatLngType.BD09
                    mapType === STMapType.GAODE -> return if (isTaiwan) STLatLngType.WGS84 else STLatLngType.GCJ02
                    else -> return if (isMainland) STLatLngType.GCJ02 else STLatLngType.WGS84
                }
            } else {
                return STLatLngType.WGS84
            }
        }

        @JvmField
        val CREATOR: Parcelable.Creator<STLatLng> = object : Parcelable.Creator<STLatLng> {
            override fun createFromParcel(source: Parcel): STLatLng = STLatLng(source)
            override fun newArray(size: Int): Array<STLatLng?> = arrayOfNulls(size)
        }
    }
}
