package com.example.smrpv2.model;

public class InquiryMessage {
    String content, userId;
    public InquiryMessage(String content, String userid){
        this.content = content;
        this.userId = userid;

    }
    public String getContent() {
        return content;
    }

    public String getUserid() {
        return userId;
    }





}
