package com.example.smrpv2.model.pharmcy_model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="items")
public class PharmacyItems {

    @ElementList(name="item",inline = true)
    List<PharmacyItem> items;
    public List<PharmacyItem> getItemsList(){return items;}
}
