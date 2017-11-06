package com.smart.housekeeper.controller.model.data.request;

import com.smart.housekeeper.base.utils.HKJSONUtil;

import java.io.Serializable;

public class UserRequestDataModel implements Serializable {
    public int id;

    @Override
    public String toString() {
        return HKJSONUtil.toJSONString(this);
    }
}
