package com.cfm.bankinterface.config;

import java.util.HashMap;

/**
 * Created by hongda on 15-12-5.
 */
public class ReqURL {
    private String path;

    private HashMap<String,String> params = new HashMap<String,String>();


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public void addParam(String key, String value){
        this.params.put(key,value);
    }
}
