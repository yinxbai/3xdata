package com.txdata.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;

public class SortedJSONObject extends JSONObject {

    public SortedJSONObject() {
        super(new LinkedHashMap<>());
    }
}
