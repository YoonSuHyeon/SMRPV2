package com.example.smrpv2.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class MedicineItem implements Serializable {

    // Medicine 정보 - SearchActivity에서 쓰임 - 약 모양, 약 색깔 등등에 관한 것
    private Drawable Dra_icon ;
    private String Str_inform;
    private int viewType;
    private String Str_text;
    private long id; // 등록된약
    //
    private String Str_url; //약 이미지
    private String Str_name; //약 이름
    private String Str_itemSeq; //약 식별번호
    private String Str_time; //시간
    private String Str_entpName; //제조사
    private String Str_form;
    private String Str_type;

    public MedicineItem(){}

    /**
     * SearchActivity에서 검색하기 버튼 눌렀을 때 밑에 RecyclerView 출력되게 하는 아이템
     *
     */
    public MedicineItem(String itemSeq, String url, String name, String entpName, String Str_form, String Str_type) {
        this.Str_itemSeq = itemSeq;
        this.Str_url = url;
        this.Str_name = name;
        this.Str_entpName = entpName;
        this.Str_form = Str_form;
        this.Str_type = Str_type;
    }
    public MedicineItem(long id,String url, String name, String itemSeq, String time, String entpName){
        this.id=id;
        this.Str_url = url;
        this.Str_name = name;
        this.Str_itemSeq =itemSeq;
        this.Str_time =time;
        this.Str_entpName = entpName;
    }

    /**
     *
     * Search_prescriptionActivity && PrescriptionAdapter 에서 쓰임
     * (약 봉투 & 처방전 찍은 거에 대한 약 리스트)
     */
    public MedicineItem(String itemSeq, String url, String name, String type){
        this.Str_url = url;
        this.Str_name = name;
        this.Str_itemSeq =itemSeq;
        this.Str_type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIcon(Drawable icon) {Dra_icon = icon;}
    public void setInform(String n) {
        Str_inform = n ;
    }
    public void setViewType(int v){viewType = v;}
    public void setText(String t) {
        Str_text = t;
    }
    public Drawable getIcon() {
        return this.Dra_icon ;
    }
    public String getInform() {
        return this.Str_inform ;
    }
    public int getViewType() {
        return viewType;
    }
    public String getText() {
        return Str_text;
    }

    public String getForm() {
        return Str_form;
    }
    public String getType() {
        return Str_type;
    }
    public String getTime() {
        return Str_time;
    }
    public void setTime(String str_time) {
        this.Str_time = str_time;
    }
    public String getItemSeq() {
        return Str_itemSeq;
    }
    public void setItemSeq(String str_itemSeq) {
        this.Str_itemSeq = str_itemSeq;
    }
    public String getUrl() {
        return Str_url;
    }
    public void setUrl(String str_url) {
        this.Str_url = str_url;
    }
    public String getName() {
        return Str_name;
    }
    public void setName(String str_name) {
        this.Str_name = str_name;
    }
    public String getEntpName() {
        return Str_entpName;
    }
    public void setEntpName(String str_entpName) {
        this.Str_entpName = str_entpName;
    }

}