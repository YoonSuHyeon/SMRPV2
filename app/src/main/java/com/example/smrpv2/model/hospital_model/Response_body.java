package com.example.smrpv2.model.hospital_model;

import com.google.gson.annotations.SerializedName;

public class Response_body {

    @SerializedName("body") Body body;

    public Body getBody(){
        return body;
    }
}
