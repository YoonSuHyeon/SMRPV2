package com.example.smrpv2.model.searchMed_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class KakaoOcrBodyDto {


    @SerializedName("recognition_words")
    List<Recognition_words> list;
}
