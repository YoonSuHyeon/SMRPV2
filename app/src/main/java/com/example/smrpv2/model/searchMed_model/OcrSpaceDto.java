package com.example.smrpv2.model.searchMed_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OcrSpaceDto {
/**
 * 카카오 ocr 구현시 삭제 관련 파일 다 삭제
 * **/



    @SerializedName("OCRExitCode")
    String OCRExitCode;
    @SerializedName("ParsedResults")
    List<ParsedResults> ParsedResults;

    public List<ParsedResults> getParsedResults() {
        return ParsedResults;
    }
    public String getParsedText() {
        return OCRExitCode;
    }
}
