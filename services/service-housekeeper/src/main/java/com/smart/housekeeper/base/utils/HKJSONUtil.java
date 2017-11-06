package com.smart.housekeeper.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.sql.Timestamp;
import java.util.Date;

public class HKJSONUtil {
    private static final SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();

    static {
        SERIALIZE_CONFIG.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        SERIALIZE_CONFIG.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj, SERIALIZE_CONFIG);
    }

    public static String toJSONStringFormat(Object obj) {
        return JSON.toJSONString(obj, SERIALIZE_CONFIG, SerializerFeature.PrettyFormat);
    }
}
