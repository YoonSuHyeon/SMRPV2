package com.example.smrpv2.model.home_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeMedItem {
     //약 이미지
     @SerializedName("itemName")
     @Expose

    private String name; //약 이름
    @SerializedName("itemSeq")
    @Expose
    private String itemSeq; //약 식별번호

    public HomeMedItem(String name, String itemSeq) {
        this.name = name;
        this.itemSeq = itemSeq;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }
}
