package com.example.smrpv2.model.pharmcy_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import retrofit2.http.GET;

@Root(name="item", strict=false)
public class PharmacyItem {
    public PharmacyItem(){

    }

   /* public PharmacyItem(String addr, String distance, String postNo, String telNo, String yadmNm, double latitude, double longitude) {
        this.addr = addr;
        this.distance = distance;
        this.postNo = postNo;
        this.telNo = telNo;
        this.yadmNm = yadmNm;
        this.latitude = latitude;
        this.longitude = longitude;
    }*/


    @Element(name="addr")
    private String addr; //주소
    @Element(name="distance")
    private String distance;//직선거리
    @Element(name="postNo")
    private String postNo;//우편번혼
    @Element(name="telno")
    private String telNo;//전화번호
    @Element(name="yadmNm")
    private String yadmNm; //약국이름
    @Element(name="XPos")//
    private double latitude;// 경도(XPOS)
    @Element(name="YPos")
    private double longitude; //위도(YPOS)


    public String getAddr() {
        return addr;
    }

    public String getDistance() {
        return distance;
    }

    public String getPostNo() {
        return postNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getYadmNm() {
        return yadmNm;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
