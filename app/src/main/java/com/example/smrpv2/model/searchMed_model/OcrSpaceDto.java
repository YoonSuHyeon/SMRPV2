package com.example.smrpv2.model.searchMed_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OcrSpaceDto {




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
