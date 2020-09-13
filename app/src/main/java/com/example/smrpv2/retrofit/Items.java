package com.example.smrpv2.retrofit;

import com.example.smrpv2.model.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {
    @SerializedName("item") List<Item> list;
    public List<Item> getItemsList(){return list;}
}
