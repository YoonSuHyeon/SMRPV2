package com.example.smrpv2.model.searchMed_model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class KakaoOcrDto {


    @SerializedName("result")
    List<KakaoOcrBodyDto> list;

    public List<KakaoOcrBodyDto> getList(){
        return list;
    }
}
