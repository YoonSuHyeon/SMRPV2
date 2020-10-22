package com.example.smrpv2.ui.findid;

public class Change_Password {
    String userId, name, email, passWd;

    public Change_Password(String userid, String name, String email, String passWd){
        this.userId = userid;
        this.name = name;
        this.email = email;
        this.passWd = passWd;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassWd() {
        return passWd;
    }


}