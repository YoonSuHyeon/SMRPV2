package com.example.smrpv2.model.pharmcy_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "response", strict = false)
public class Response_phy {

    @Element(name = "header", required = false)
    Header header;

    @Element(name = "body", required = false)
    Body body;

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}