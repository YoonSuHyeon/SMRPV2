package com.example.smrpv2.model.pharmcy_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "header", strict = false)
public class Header {
    @Element(name = "resultCode", required = false)
    String resultCode;

    @Element(name = "resultMsg", required = false)
    String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }
}