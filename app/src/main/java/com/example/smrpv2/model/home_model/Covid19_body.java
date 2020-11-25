package com.example.smrpv2.model.home_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "body",strict = false)
public class Covid19_body {
    @Element(name = "items",required = false)
    Covid19_items item;

    public Covid19_items getItems() {
        return item;
    }
}
