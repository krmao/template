package com.jianyi.controller.model;

import com.jianyi.base.utils.JsonUtil;

import java.io.Serializable;

@SuppressWarnings("unuesd")
public class CommonRequestModel<T> implements Serializable {
    public int version;//协议版本号
    public int category;//协议分类
    public int platform;//1:ios 2:android
    public String token = "";
    public T data;

    public CommonRequestModel() {
    }

    public CommonRequestModel(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.toJSONStringFormat(this);
    }
}
