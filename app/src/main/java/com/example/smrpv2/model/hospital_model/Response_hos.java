package com.example.smrpv2.model.hospital_model;

import com.example.smrpv2.model.pharmcy_model.Header;
import com.google.gson.annotations.SerializedName;

public class Response_hos {
    @SerializedName("response")
    Response_body response_body;

    public Response_body getResponse(){
        return response_body;
    }

}
