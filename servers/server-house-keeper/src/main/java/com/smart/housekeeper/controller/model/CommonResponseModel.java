package com.smart.housekeeper.controller.model;

import com.smart.housekeeper.base.utils.JsonUtil;

import java.io.Serializable;

@SuppressWarnings({"unuesd", "WeakerAccess"})
public class CommonResponseModel<T> implements Serializable {
    public int version;//协议版本号
    public int category;//协议分类
    public int platform;//1:ios 2:android
    public int status;//状态 0:成功 1:失败 2:未登录
    public String message = "";//响应描述
    public T data;

    public CommonResponseModel() {
    }

    public CommonResponseModel(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.toJSONStringFormat(this);
    }
}
