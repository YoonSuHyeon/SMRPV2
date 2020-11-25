package com.example.smrpv2.model.home_model;

import com.example.smrpv2.model.pharmcy_model.PharmacyItem;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="items")
public class Covid19_items {
    @ElementList(name="item",inline = true)
    List<Covid19_item> item;

    public List<Covid19_item> getItemsList(){ return item; }

    public void clear(){
        item.clear();
    }
}
