package com.example.smrpv2.model.pharmcy_model;

import android.util.Log;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name="items")
public class PharmacyItems {

    @ElementList(name="item",inline = true)
    ArrayList<PharmacyItem> items;
    public ArrayList<PharmacyItem> getItemsList(){ return items; }
    public void clear(){
        items.clear();
    }
}
