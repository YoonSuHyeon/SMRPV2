package com.example.smrpv2.model.home_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "response", strict = false)
public class Covid19_response {
    @Element(name = "header", required = false)
    Covid19_header header;
    @Element(name = "body",required = false)
    Covid19_body body;

    public Covid19_header getHeader() {
        return header;
    }

    public Covid19_body getBody() {
        return body;
    }
}
