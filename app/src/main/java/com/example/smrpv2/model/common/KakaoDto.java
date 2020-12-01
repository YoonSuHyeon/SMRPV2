package com.example.smrpv2.model.common;

import java.util.List;

public class KakaoDto {
    /**
     * 카카오 OCR
     * */
    List<KaKaoResult> result;

    public List<KaKaoResult> getResult() {
        return result;
    }

    public void setResult(List<KaKaoResult> result) {
        this.result = result;
    }
}
