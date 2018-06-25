package com.smart.library.util.map

@Suppress("MemberVisibilityCanBePrivate", "unused")
object CXMapUtil {
    // 台湾
    private val taiwanLatLngs = arrayOf(arrayOf(CXLatLng(120.492553, 25.319199), CXLatLng(122.085570, 23.155511)), arrayOf(CXLatLng(119.240111, 23.951114), CXLatLng(121.646117, 21.611470)))
    // 香港
    private val hongkongLatLngs = arrayOf(arrayOf(CXLatLng(113.9970582, 22.49289015), CXLatLng(114.451617, 22.14861424)), arrayOf(CXLatLng(113.8405030, 22.4307039), CXLatLng(114.37196669, 22.12889756)))
    // 澳门
    private val macaoLatLngs = arrayOf(arrayOf(CXLatLng(113.528594, 22.2303135), CXLatLng(113.607558, 22.125717)), arrayOf(CXLatLng(113.450660, 22.1511581), CXLatLng(113.596572, 22.064003)))
    // 中国内地坐标.
    private val chinaLatLngs = arrayOf(arrayOf(CXLatLng(85.374342, 41.697126), CXLatLng(124.486996, 28.705892)), arrayOf(CXLatLng(98.942349, 28.714002), CXLatLng(122.527683, 23.331042)), arrayOf(CXLatLng(108.012216, 23.415965), CXLatLng(119.252965, 17.294543)), arrayOf(CXLatLng(120.025651, 51.286036), CXLatLng(126.391116, 41.330674)), arrayOf(CXLatLng(82.936701, 46.727439), CXLatLng(90.553182, 41.621242)), arrayOf(CXLatLng(126.188746, 48.211817), CXLatLng(129.757821, 42.485061)), arrayOf(CXLatLng(129.518656, 47.611932), CXLatLng(131.46877, 44.959641)), arrayOf(CXLatLng(131.376783, 47.487374), CXLatLng(133.805226, 46.225387)), arrayOf(CXLatLng(79.753968, 41.87613), CXLatLng(85.604309, 30.872189)), arrayOf(CXLatLng(113.457816, 44.802677), CXLatLng(120.117638, 41.517618)), arrayOf(CXLatLng(118.977005, 23.526282), CXLatLng(121.975765, 21.629857)), arrayOf(CXLatLng(109.667973, 17.321053), CXLatLng(119.050594, 14.580095)), arrayOf(CXLatLng(76.258482, 40.359687), CXLatLng(80.01153, 35.915704)), arrayOf(CXLatLng(90.534784, 44.710915), CXLatLng(94.030271, 41.531444)), arrayOf(CXLatLng(80.710628, 45.077082), CXLatLng(83.028687, 41.862379)), arrayOf(CXLatLng(85.93546, 48.414308), CXLatLng(88.437492, 46.645143)), arrayOf(CXLatLng(93.975079, 42.559912), CXLatLng(101.462779, 41.600531)), arrayOf(CXLatLng(93.956681, 44.157262), CXLatLng(95.354876, 42.491869)), arrayOf(CXLatLng(116.69574, 46.301949), CXLatLng(120.117638, 44.619006)), arrayOf(CXLatLng(116.401384, 49.444657), CXLatLng(120.191227, 48.057877)), arrayOf(CXLatLng(121.000708, 53.244099), CXLatLng(124.569783, 51.210984)), arrayOf(CXLatLng(106.724405, 42.232628), CXLatLng(113.494611, 41.683336)), arrayOf(CXLatLng(112.133211, 44.355602), CXLatLng(113.5682, 42.123151)), arrayOf(CXLatLng(110.918989, 43.155464), CXLatLng(112.2068, 42.232628)), arrayOf(CXLatLng(115.150367, 45.324216), CXLatLng(116.76933, 44.724032)), arrayOf(CXLatLng(126.299129, 49.588397), CXLatLng(128.102064, 48.057877)), arrayOf(CXLatLng(128.06527, 49.131761), CXLatLng(129.757821, 48.131826)), arrayOf(CXLatLng(129.721026, 48.62209), CXLatLng(130.530508, 47.611932)), arrayOf(CXLatLng(124.349016, 52.822665), CXLatLng(125.710416, 51.095279)), arrayOf(CXLatLng(122.325313, 28.884167), CXLatLng(123.760302, 25.662561)), arrayOf(CXLatLng(111.029373, 14.651757), CXLatLng(118.388292, 10.6053)), arrayOf(CXLatLng(109.778357, 10.095218), CXLatLng(109.778357, 10.095218)), arrayOf(CXLatLng(109.631178, 10.459649), CXLatLng(116.548562, 7.753573)), arrayOf(CXLatLng(110.514249, 7.826971), CXLatLng(113.678584, 4.73448)), arrayOf(CXLatLng(124.330619, 41.399976), CXLatLng(125.48045, 40.68961)), arrayOf(CXLatLng(126.345123, 42.51229), CXLatLng(128.046872, 41.827986)), arrayOf(CXLatLng(127.973283, 42.539507), CXLatLng(129.104717, 42.143692)), arrayOf(CXLatLng(74.510739, 40.16236), CXLatLng(76.350468, 38.678393)), arrayOf(CXLatLng(119.087389, 21.629857), CXLatLng(120.706351, 20.142916)), arrayOf(CXLatLng(106.853187, 23.339537), CXLatLng(108.067408, 21.990651)), arrayOf(CXLatLng(129.707229, 44.975967), CXLatLng(130.985841, 43.017244)), arrayOf(CXLatLng(130.958245, 44.582859), CXLatLng(131.169814, 43.104932)), arrayOf(CXLatLng(131.418177, 46.247729), CXLatLng(133.129126, 45.359896)), arrayOf(CXLatLng(133.073934, 48.054793), CXLatLng(134.269758, 47.409374)), arrayOf(CXLatLng(99.701237, 23.386249), CXLatLng(101.577762, 22.174986)), arrayOf(CXLatLng(100.179567, 22.243514), CXLatLng(101.559364, 21.745927)), arrayOf(CXLatLng(101.485775, 23.437187), CXLatLng(104.24537, 22.875776)), arrayOf(CXLatLng(98.008686, 25.240784), CXLatLng(99.057332, 24.181992)), arrayOf(CXLatLng(124.463999, 40.686109), CXLatLng(124.905534, 40.461646)), arrayOf(CXLatLng(125.457453, 41.334141), CXLatLng(126.055365, 40.979564)), arrayOf(CXLatLng(126.368119, 41.824546), CXLatLng(126.607284, 41.645397)), arrayOf(CXLatLng(125.47585, 40.979564), CXLatLng(125.687419, 40.853958)), arrayOf(CXLatLng(124.477797, 40.46516), CXLatLng(124.72846, 40.343852)), arrayOf(CXLatLng(124.470898, 40.347371), CXLatLng(124.618076, 40.285757)), arrayOf(CXLatLng(124.891736, 40.694862), CXLatLng(125.153898, 40.607283)), arrayOf(CXLatLng(126.046166, 41.332407), CXLatLng(126.262335, 41.165784)), arrayOf(CXLatLng(127.214395, 41.836586), CXLatLng(128.083667, 41.546995)), arrayOf(CXLatLng(126.386516, 50.257998), CXLatLng(126.386516, 50.257998)), arrayOf(CXLatLng(126.280732, 50.257998), CXLatLng(127.513351, 49.580921)), arrayOf(CXLatLng(126.36352, 50.934256), CXLatLng(127.117809, 50.225552)), arrayOf(CXLatLng(125.669022, 52.39849), CXLatLng(126.276133, 51.247082)), arrayOf(CXLatLng(80.948643, 30.905163), CXLatLng(81.403976, 30.280446)), arrayOf(CXLatLng(83.574857, 30.911112), CXLatLng(85.488176, 29.214825)), arrayOf(CXLatLng(98.136317, 28.872274), CXLatLng(99.079179, 27.642374)), arrayOf(CXLatLng(111.25399, 21.257314), CXLatLng(118.79061, 11.683099)), arrayOf(CXLatLng(110.57284, 11.510906), CXLatLng(117.45028, 9.2850385)), arrayOf(CXLatLng(109.03475, 8.7533664), CXLatLng(114.37410, 5.5489422)))
    // 中国大范围
    private val chinaBigLatLngs = arrayOf(
            arrayOf(CXLatLng(73.083331, 54.006559), CXLatLng(135.266195, 17.015367)), // 中国本度大框.
            arrayOf(CXLatLng(109.806384, 17.579908), CXLatLng(112.529184, 3.638301)), // 以下四个是西沙群岛.
            arrayOf(CXLatLng(112.124443, 17.773916), CXLatLng(115.583135, 7.159653)),
            arrayOf(CXLatLng(115.251984, 17.562261), CXLatLng(117.422865, 9.54155)),
            arrayOf(CXLatLng(117.054919, 17.773916), CXLatLng(118.931443, 11.507795))
    )
    // 中国周边范围
    private val chinaSideLatLngs = arrayOf(
            arrayOf(CXLatLng(125.478833, 40.538425), CXLatLng(135.928497, 16.590044)),
            arrayOf(CXLatLng(128.054454, 54.43779), CXLatLng(136.370032, 49.918776)),
            arrayOf(CXLatLng(89.567309, 54.351906), CXLatLng(115.617882, 47.881407)),
            arrayOf(CXLatLng(71.832315, 54.566279), CXLatLng(82.28198, 46.323836)),
            arrayOf(CXLatLng(72.788974, 28.001436), CXLatLng(85.88785, 16.590044)),
            arrayOf(CXLatLng(92.510877, 48.029708), CXLatLng(111.570476, 45.034268)),
            arrayOf(CXLatLng(85.593493, 26.157025), CXLatLng(97.294174, 16.519064)),
            arrayOf(CXLatLng(97.073406, 20.935216), CXLatLng(107.743838, 16.305964)),
            arrayOf(CXLatLng(98.324422, 45.190596), CXLatLng(109.142033, 42.854577)),
            arrayOf(CXLatLng(71.979493, 45.863038), CXLatLng(78.896877, 41.817208)),
            arrayOf(CXLatLng(72.374784, 34.326035), CXLatLng(78.372303, 27.905294)), // 印度
            arrayOf(CXLatLng(120.131867, 19.569888), CXLatLng(125.411891, 15.992375)) // 菲律宾
    )

    // 是否在国外
    @JvmStatic
    fun isOversea(latLng: CXLatLng?): Boolean = latLng != null && (!isInLatLngs(latLng.longitude, latLng.latitude, chinaBigLatLngs) || isInLatLngs(latLng.longitude, latLng.latitude, chinaSideLatLngs))

    // 是否在国内.
    @JvmStatic
    fun isInChina(latLng: CXLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, chinaLatLngs)

    // 是否在大陆
    @JvmStatic
    fun isInMainland(latLng: CXLatLng?): Boolean = latLng != null && isInChina(latLng) && !isInTaiwan(latLng) && !isInHongkong(latLng) && !isInMacao(latLng)

    // 是否在台湾
    @JvmStatic
    fun isInTaiwan(latLng: CXLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, taiwanLatLngs)

    // 是否在香港
    @JvmStatic
    fun isInHongkong(latLng: CXLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, hongkongLatLngs)

    // 是否在澳门
    @JvmStatic
    fun isInMacao(latLng: CXLatLng?): Boolean = latLng != null && isInLatLngs(latLng.longitude, latLng.latitude, macaoLatLngs)

    @JvmStatic
    fun isInLatLngs(inx: Double, iny: Double, latLngs: Array<Array<CXLatLng>>): Boolean {
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
    fun convertGCJ02ToBD09(lat: Double, lon: Double): CXLatLng {
        val z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * PI)
        val theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * PI)
        return CXLatLng(z * Math.sin(theta) + 0.006, z * Math.cos(theta) + 0.0065)
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 BD-09 坐标转换成GCJ-02 坐标
     */
    @JvmStatic
    fun convertBD09ToGCJ02(lat: Double, lon: Double): CXLatLng {
        val x = lon - 0.0065
        val y = lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI)
        return CXLatLng(z * Math.sin(theta), z * Math.cos(theta))
    }

    /**
     * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 WGS84 坐标转换成 GCJ-02 坐标
     */
    @JvmStatic
    fun convertWGS84ToGCJ02(lat: Double, lon: Double): CXLatLng {
        var transformLat = transformLat(lon - 105.0, lat - 35.0)
        var transformLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * PI
        var magic = Math.sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        transformLat = transformLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * PI)
        transformLon = transformLon * 180.0 / (EARTH_RADIUS / sqrtMagic * Math.cos(radLat) * PI)
        return CXLatLng(lat + transformLat, lon + transformLon)
    }

    /**
     * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 GCJ-02 坐标转换成 WGS84 坐标
     */
    @JvmStatic
    fun convertGCJ02ToWGS84(lat: Double, lon: Double): CXLatLng {
        val transformLatLng: CXLatLng =
                if (outOfChina(lat, lon)) {
                    CXLatLng(lat, lon)
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
                    return CXLatLng(mgLat, mgLon)
                }
        return CXLatLng(lat * 2 - transformLatLng.latitude, lon * 2 - transformLatLng.longitude)
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
    fun getDistanceByGCJ02(from: CXLatLng?, to: CXLatLng?): Float {
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