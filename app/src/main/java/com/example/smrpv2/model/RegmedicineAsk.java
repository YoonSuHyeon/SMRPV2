package com.example.smrpv2.model;

public class RegmedicineAsk {
    String userId;
    String itemSeq;


    public RegmedicineAsk(String userId, String itemSeq) {
        this.userId = userId; //사용자 id
        this.itemSeq = itemSeq;//등록할 약 번호
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }





}
