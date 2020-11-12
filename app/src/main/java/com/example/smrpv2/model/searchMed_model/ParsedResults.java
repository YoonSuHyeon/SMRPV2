package com.example.smrpv2.model.searchMed_model;

import com.google.gson.annotations.SerializedName;


public class ParsedResults {



    @SerializedName("ParsedText")
    String ParsedText;

    public String getParsedText() {
        return ParsedText;
    }
}
