package com.example.smrpv2.model.prescription_model;

import java.util.ArrayList;

/**
 * Search_prescriptionActivity에서 사용
 * (나중에 서버 구현 후 다른 Item들과 합쳐질 수 있음)
 */
public class User_Select {
    private String userId;; //사용자 아이디
    String[] recognition_words; //카카오 ocr에서 인식한 글자들을 가지고 있는 리스트

    public User_Select(String id, String[] recognition_words){
        this.userId = id;
        this.recognition_words = recognition_words;
    }

    public String getId() {
        return userId;
    }

    public String[] getList() {
        return recognition_words;
    }
}
