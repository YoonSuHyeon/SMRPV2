package com.example.smrpv2.model.pharmcy_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "body")
public class Body {


    @Element(name = "items")
    PharmacyItems items;

    @Element(name = "numOfRows")
    int numOfRows;

    @Element(name = "pageNo")
    int pageNo;

    @Element(name = "totalCount")
    int totalCount;

    public PharmacyItems getItems() {
        return items;
    }

}