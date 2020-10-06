package com.example.smrpv2.model.user_model;
/**
 * 로그인 사용자 정보
 * **/
public class LoginUser {
    String userId;
    String passWord;
    public LoginUser(String userId, String passWord) {
        this.userId = userId;
        this.passWord = passWord;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


}
