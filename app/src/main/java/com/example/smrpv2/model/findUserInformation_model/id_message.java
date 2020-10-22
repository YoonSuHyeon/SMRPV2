package com.example.smrpv2.model.findUserInformation_model;

import com.google.gson.annotations.SerializedName;

class id_message {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    String id;


}
