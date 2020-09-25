package com.example.smrpv2.model.hospital_model;

import com.google.gson.annotations.SerializedName;

public class Body {
    @SerializedName("items") Items items;

    public Items getItems(){
        return items;
    }
}
