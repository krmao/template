package com.smart.library.util.map

@Suppress("MemberVisibilityCanBePrivate", "unused")
object STMapUtil {
    // 台湾
    private val taiwanLatLngs = arrayOf(arrayOf(STLatLng(120.492553, 25.319199), STLatLng(122.085570, 23.155511)), arrayOf(STLatLng(119.240111, 23.951114), STLatLng(121.646117, 21.611470)))
    // 香港
    private val hongkongLatLngs = arrayOf(arrayOf(STLatLng(113.9970582, 22.49289015), STLatLng(114.451617, 22.14861424)), arrayOf(STLatLng(113.8405030, 22.4307039), STLatLng(114.37196669, 22.12889756)))
    // 澳门
    private val macaoLatLngs = arrayOf(arrayOf(STLatLng(113.528594, 22.2303135), STLatLng(113.607558, 22.125717)), arrayOf(STLatLng(113.450660, 22.1511581), STLatLng(113.596572, 22.064003)))
    // 中国内地坐标.
    private val chinaLatLngs = arrayOf(arrayOf(STLatLng(85.374342, 41.697126), STLatLng(124.486996, 28.705892)), arrayOf(STLatLng(98.942349, 28.714002), STLatLng(122.527683, 23.331042)), arrayOf(STLatLng(108.012216, 23.415965), STLatLng(119.252965, 17.294543)), arrayOf(STLatLng(120.025651, 51.286036), STLatLng(126.391116, 41.330674)), arrayOf(STLatLng(82.936701, 46.727439), STLatLng(90.553182, 41.621242)), arrayOf(STLatLng(126.188746, 48.211817), STLatLng(129.757821, 42.485061)), arrayOf(STLatLng(129.518656, 47.611932), STLatLng(131.46877, 44.959641)), arrayOf(STLatLng(131.376783, 47.487374), STLatLng(133.805226, 46.225387)), arrayOf(STLatLng(79.753968, 41.87613), STLatLng(85.604309, 30.872189)), arrayOf(STLatLng(113.457816, 44.802677), STLatLng(120.117638, 41.517618)), arrayOf(STLatLng(118.977005, 23.526282), STLatLng(121.975765, 21.629857)), arrayOf(STLatLng(109.667973, 17.321053), STLatLng(119.050594, 14.580095)), arrayOf(STLatLng(76.258482, 40.359687), STLatLng(80.01153, 35.915704)), arrayOf(STLatLng(90.534784, 44.710915), STLatLng(94.030271, 41.531444)), arrayOf(STLatLng(80.710628, 45.077082), STLatLng(83.028687, 41.862379)), arrayOf(STLatLng(85.93546, 48.414308), STLatLng(88.437492, 46.645143)), arrayOf(STLatLng(93.975079, 42.559912), STLatLng(101.462779, 41.600531)), arrayOf(STLatLng(93.956681, 44.157262), STLatLng(95.354876, 42.491869)), arrayOf(STLatLng(116.69574, 46.301949), STLatLng(120.117638, 44.619006)), arrayOf(STLatLng(116.401384, 49.444657), STLatLng(120.191227, 48.057877)), arrayOf(STLatLng(121.000708, 53.244099), STLatLng(124.569783, 51.210984)), arrayOf(STLatLng(106.724405, 42.232628), STLatLng(113.494611, 41.683336)), arrayOf(STLatLng(112.133211, 44.355602), STLatLng(113.5682, 42.123151)), arrayOf(STLatLng(110.918989, 43.155464), STLatLng(112.2068, 42.232628)), arrayOf(STLatLng(115.150367, 45.324216), STLatLng(116.76933, 44.724032)), arrayOf(STLatLng(126.299129, 49.588397), STLatLng(128.102064, 48.057877)), arrayOf(STLatLng(128.06527, 49.131761), STLatLng(129.757821, 48.131826)), arrayOf(STLatLng(129.721026, 48.62209), STLatLng(130.530508, 47.611932)), arrayOf(STLatLng(124.349016, 52.822665), STLatLng(125.710416, 51.095279)), arrayOf(STLatLng(122.325313, 28.884167), STLatLng(123.760302, 25.662561)), arrayOf(STLatLng(111.029373, 14.651757), STLatLng(118.388292, 10.6053)), arrayOf(STLatLng(109.778357, 10.095218), STLatLng(109.778357, 10.095218)), arrayOf(STLatLng(109.631178, 10.459649), STLatLng(116.548562, 7.753573)), arrayOf(STLatLng(110.514249, 7.826971), STLatLng(113.678584, 4.73448)), arrayOf(STLatLng(124.330619, 41.399976), STLatLng(125.48045, 40.68961)), arrayOf(STLatLng(126.345123, 42.51229), STLatLng(128.046872, 41.827986)), arrayOf(STLatLng(127.973283, 42.539507), STLatLng(129.104717, 42.143692)), arrayOf(STLatLng(74.510739, 40.16236), STLatLng(76.350468, 38.678393)), arrayOf(STLatLng(119.087389, 21.629857), STLatLng(120.706351, 20.142916)), arrayOf(STLatLng(106.853187, 23.339537), STLatLng(108.067408, 21.990651)), arrayOf(STLatLng(129.707229, 44.975967), STLatLng(130.985841, 43.017244)), arrayOf(STLatLng(130.958245, 44.582859), STLatLng(131.169814, 43.104932)), arrayOf(STLatLng(131.418177, 46.247729), STLatLng(133.129126, 45.359896)), arrayOf(STLatLng(133.073934, 48.054793), STLatLng(134.269758, 47.409374)), arrayOf(STLatLng(99.701237, 23.386249), STLatLng(101.577762, 22.174986)), arrayOf(STLatLng(100.179567, 22.243514), STLatLng(101.559364, 21.745927)), arrayOf(STLatLng(101.485775, 23.437187), STLatLng(104.24537, 22.875776)), arrayOf(STLatLng(98.008686, 25.240784), STLatLng(99.057332, 24.181992)), arrayOf(STLatLng(124.463999, 40.686109), STLatLng(124.905534, 40.461646)), arrayOf(STLatLng(125.457453, 41.334141), STLatLng(126.055365, 40.979564)), arrayOf(STLatLng(126.368119, 41.824546), STLatLng(126.607284, 41.645397)), arrayOf(STLatLng(125.47585, 40.979564), STLatLng(125.687419, 40.853958)), arrayOf(STLatLng(124.477797, 40.46516), STLatLng(124.72846, 40.343852)), arrayOf(STLatLng(124.470898, 40.347371), STLatLng(124.618076, 40.285757)), arrayOf(STLatLng(124.891736, 40.694862), STLatLng(125.153898, 40.607283)), arrayOf(STLatLng(126.046166, 41.332407), STLatLng(126.262335, 41.165784)), arrayOf(STLatLng(127.214395, 41.836586), STLatLng(128.083667, 41.546995)), arrayOf(STLatLng(126.386516, 50.257998), STLatLng(126.386516, 50.257998)), arrayOf(STLatLng(126.280732, 50.257998), STLatLng(127.513351, 49.580921)), arrayOf(STLatLng(126.36352, 50.934256), STLatLng(127.117809, 50.225552)), arrayOf(STLatLng(125.669022, 52.39849), STLatLng(126.276133, 51.247082)), arrayOf(STLatLng(80.948643, 30.905163), STLatLng(81.403976, 30.280446)), arrayOf(STLatLng(83.574857, 30.911112), STLatLng(85.488176, 29.214825)), arrayOf(STLatLng(98.136317, 28.872274), STLatLng(99.079179, 27.642374)), arrayOf(STLatLng(111.25399, 21.257314), STLatLng(118.79061, 11.683099)), arrayOf(STLatLng(110.57284, 11.510906), STLatLng(117.45028, 9.2850385)), arrayOf(STLatLng(109.03475, 8.7533664), STLatLng(114.37410, 5.5489422)))
    // 中国大范围
    private val chinaBigLatLngs = arrayOf(
            arrayOf(STLatLng(73.083331, 54.006559), STLatLng(135.266195, 17.015367)), // 中国本度大框.
            arrayOf(STLatLng(109.806384, 17.579908), STLatLng(112.529184, 3.638301)), // 以下四个是西沙群岛.
            arrayOf(STLatLng(112.124443, 17.773916), STLatLng(115.583135, 7.159653)),
            arrayOf(STLatLng(115.251984, 17.562261), STLatLng(117.422865, 9.54155)),
            arrayOf(STLatLng(117.054919, 17.773916), STLatLng(118.931443, 11.507795))
    )
    // 中国周边范围
    private val chinaSideLatLngs = arrayOf(
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
            arrayOf(STLatLng(72.374784, 34.326035), STLatLng(78.372303, 27.905294)), // 印度
            arrayOf(STLatLng(120.131867, 19.569888), STLatLng(125.411891, 15.992375)) // 菲律宾
    )

    // 是否在国外
    @JvmStatic
    fun isOversea(latLng: STLatLng?): Boolean = latLng != null && (!isInLatLngs(latLng.longitude, latLng.latitude, chinaBigLatLngs) || isInLatLngs(latLng.longitude, latLng.latitude, chinaSideLatLngs))

    // 是否在国内.
    @JvmStatic
    fun isInChina(latLng: STLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, chinaLatLngs)

    // 是否在大陆
    @JvmStatic
    fun isInMainland(latLng: STLatLng?): Boolean = latLng != null && isInChina(latLng) && !isInTaiwan(latLng) && !isInHongkong(latLng) && !isInMacao(latLng)

    // 是否在台湾
    @JvmStatic
    fun isInTaiwan(latLng: STLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, taiwanLatLngs)

    // 是否在香港
    @JvmStatic
    fun isInHongkong(latLng: STLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, hongkongLatLngs)

    // 是否在澳门
    @JvmStatic
    fun isInMacao(latLng: STLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, macaoLatLngs)

    @JvmStatic
    fun isInLatLngs(inx: Double, iny: Double, latLngs: Array<Array<STLatLng>>): Boolean {
        val zoomTimes = 1
        val lx = inx * zoomTimes
        val ly = iny * zoomTimes
        var startX: Double
        var startY: Double
        var endX: Double
        var endY: Double
        for (rect in latLngs) {
            startX = rect[0].longitude * zoomTimes
            startY = rect[0].latitude * zoomTimes
            endX = rect[1].longitude * zoomTimes
            endY = rect[1].latitude * zoomTimes
            if (lx in startX..endX && ly <= startY && ly >= endY) //只要在任何一个区域内就行
                return true
        }
        return false
    }

    @JvmStatic
    fun isValidLatLng(latitude: Double?, longitude: Double?) = if (latitude == null || longitude == null) false else (Math.abs(latitude) < 90 && Math.abs(latitude) > 0 && Math.abs(longitude) < 180 && Math.abs(longitude) > 0)

    const val PI = 3.1415926535897932384626 // 圆周率
    const val EARTH_RADIUS = 6378245.0      // 椭球长半径
    const val EE = 0.00669342162296594323   // 偏心率

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     */
    @JvmStatic
    fun convertGCJ02ToBD09(lat: Double, lon: Double): STLatLng {
        val z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * PI)
        val theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * PI)
        return STLatLng(z * Math.sin(theta) + 0.006, z * Math.cos(theta) + 0.0065)
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 BD-09 坐标转换成GCJ-02 坐标
     */
    @JvmStatic
    fun convertBD09ToGCJ02(lat: Double, lon: Double): STLatLng {
        val x = lon - 0.0065
        val y = lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI)
        return STLatLng(z * Math.sin(theta), z * Math.cos(theta))
    }

    /**
     * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 WGS84 坐标转换成 GCJ-02 坐标
     */
    @JvmStatic
    fun convertWGS84ToGCJ02(lat: Double, lon: Double): STLatLng {
        var transformLat = transformLat(lon - 105.0, lat - 35.0)
        var transformLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * PI
        var magic = Math.sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        transformLat = transformLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * PI)
        transformLon = transformLon * 180.0 / (EARTH_RADIUS / sqrtMagic * Math.cos(radLat) * PI)
        return STLatLng(lat + transformLat, lon + transformLon)
    }

    /**
     * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 GCJ-02 坐标转换成 WGS84 坐标
     */
    @JvmStatic
    fun convertGCJ02ToWGS84(lat: Double, lon: Double): STLatLng {
        val transformLatLng: STLatLng =
                if (outOfChina(lat, lon)) {
                    STLatLng(lat, lon)
                } else {
                    var dLat = transformLat(lon - 105.0, lat - 35.0)
                    var dLon = transformLon(lon - 105.0, lat - 35.0)
                    val radLat = lat / 180.0 * PI
                    var magic = Math.sin(radLat)
                    magic = 1 - EE * magic * magic
                    val sqrtMagic = Math.sqrt(magic)
                    dLat = dLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * PI)
                    dLon = dLon * 180.0 / (EARTH_RADIUS / sqrtMagic * Math.cos(radLat) * PI)
                    val mgLat = lat + dLat
                    val mgLon = lon + dLon
                    return STLatLng(mgLat, mgLon)
                }
        return STLatLng(lat * 2 - transformLatLng.latitude, lon * 2 - transformLatLng.longitude)
    }

    private fun outOfChina(lat: Double, lon: Double): Boolean = (lon < 72.004 || lon > 137.8347) || (lat < 0.8293 || lat > 55.8271)

    private fun transformLat(x: Double, y: Double): Double {
        var ret = (-100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x)))
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0
        return ret
    }

    /**
     * 高德地图官方计算方式
     */
    @JvmStatic
    fun getDistanceByGCJ02(from: STLatLng?, to: STLatLng?): Float {
        return if (from != null && to != null) {
            try {
                var var4 = from.longitude
                var var6 = from.latitude
                var var8 = to.longitude
                var var10 = to.latitude
                var4 *= 0.01745329251994329
                var6 *= 0.01745329251994329
                var8 *= 0.01745329251994329
                var10 *= 0.01745329251994329
                val var12 = Math.sin(var4)
                val var14 = Math.sin(var6)
                val var16 = Math.cos(var4)
                val var18 = Math.cos(var6)
                val var20 = Math.sin(var8)
                val var22 = Math.sin(var10)
                val var24 = Math.cos(var8)
                val var26 = Math.cos(var10)
                val var28 = DoubleArray(3)
                val var29 = DoubleArray(3)
                var28[0] = var18 * var16
                var28[1] = var18 * var12
                var28[2] = var14
                var29[0] = var26 * var24
                var29[1] = var26 * var20
                var29[2] = var22
                val var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]))
                (Math.asin(var30 / 2.0) * 1.27420015798544E7).toFloat()
            } catch (e: Throwable) {
                e.printStackTrace()
                0.0f
            }
        } else {
            0.0f
        }
    }
}