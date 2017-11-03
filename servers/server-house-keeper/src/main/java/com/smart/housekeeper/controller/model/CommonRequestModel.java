package com.smart.housekeeper.controller.model;

import com.smart.housekeeper.base.utils.HKJSONUtil;

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
        return HKJSONUtil.toJSONStringFormat(this);
    }
}
