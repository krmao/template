package com.jianyi.controller.model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

@SuppressWarnings("unuesd")
public class CommonResponseJsonObjectModel extends CommonResponseModel<JSONObject> {
    public JSONObject data = new JSONObject();

    public void addBeanList(String key, List<?> list) {
        data.put(key, list);
    }

    public void addKey$Value(String key, Object value) {
        data.put(key, value);
    }

    public void addBean(String key, Object bean) {
        data.put(key, bean);
    }
}
