package com.example.smrpv2.model.medicine_model;

public class Prescriptionitem {
    private String itemSeq;
    private String stringURL;
    private String type, name;


    public Prescriptionitem(String itemSeq, String stringURL, String type, String name) { //약 식별번호 / 약 이미지 / 약 이름 / 약 제조사 / 약 포장 /약 의약품정보(일반, 전문)
        this.itemSeq = itemSeq; //알약 식별코드
        this.stringURL = stringURL; //알약 이미지
        this.type = type; //알약 식별포장
        this.name = name; //알약 제품명
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public String getStringURL() {
        return stringURL;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
