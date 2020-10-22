package com.example.smrpv2.model;

public class InquiryDto {
    String content; //내용
    String userId; //사용자 아이디

    public InquiryDto(String content, String userId) {
        this.content = content;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
