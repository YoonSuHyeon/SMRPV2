package com.example.smrpv2.model.findUserInformation_model;

import com.google.gson.annotations.SerializedName;

class password_message {
    @SerializedName("Password")
    String password;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
