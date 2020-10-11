package com.example.smrpv2.model;

import java.time.LocalDate;

public class SumMedInfo {


    private String imageUrl;
    private String itemSeq;
    private String itemName;
    private String entpName;
    private String createdAt;
    public SumMedInfo(String imageUrl, String itemSeq, String itemName, String entpName, String createdAt) {
        this.imageUrl = imageUrl;
        this.itemSeq = itemSeq;
        this.itemName = itemName;
        this.entpName = entpName;
        this.createdAt = createdAt;
    }




    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getEntpName() {
        return entpName;
    }

    public void setEntpName(String entpName) {
        this.entpName = entpName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}
