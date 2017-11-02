package com.jianyi.controller.model.data.request;

import com.jianyi.base.utils.JsonUtil;

import java.io.Serializable;

public class UserRequestDataModel implements Serializable {
    public int id;

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}
