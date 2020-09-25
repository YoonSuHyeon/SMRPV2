package com.example.smrpv2.model.hospital_model;


import com.google.gson.annotations.SerializedName;

public class Item {


    @SerializedName("addr") //병원주소
    private String addr;
    @SerializedName("clCdNm")//병원 종별코드명
    private String clCdNm;
    @SerializedName("distance")//병원 거리
    private double distance;
    @SerializedName("XPos")//병원 x좌표
    private String XPos;
    @SerializedName("YPos")//병원 y좌표
    private String YPos;
    @SerializedName("yadmNm")//병원 이름
    private String yadmNm;
    @SerializedName("hospUrl")//병원 사이트
    private String hospUrl;
    @SerializedName("telno")//병원 전화번호
    private String telno;





    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getClCdNm() {
        return clCdNm;
    }

    public void setClCdNm(String clCdNm) {
        this.clCdNm = clCdNm;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getXPos() {
        return XPos;
    }

    public void setXPos(String XPos) {
        this.XPos = XPos;
    }

    public String getYPos() {
        return YPos;
    }

    public void setYPos(String YPos) {
        this.YPos = YPos;
    }

    public String getYadmNm() {
        return yadmNm;
    }

    public void setYadmNm(String yadmNm) {
        this.yadmNm = yadmNm;
    }

    public String getHospUrl() {
        return hospUrl;
    }

    public void setHospUrl(String hospUrl) {
        this.hospUrl = hospUrl;
    }
    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }
}
