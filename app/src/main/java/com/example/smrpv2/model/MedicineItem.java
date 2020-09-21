package com.example.smrpv2.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class MedicineItem implements Serializable {

    // Medicine 정보 - SearchActivity에서 쓰임 - 약 모양, 약 색깔 등등에 관한 것
    private Drawable Dra_icon ;
    private String Str_inform;
    private int viewType;
    private String Str_text;

    //
    private String url; //약 이미지
    private String name; //약 이름
    private String itemSeq; //약 식별번호
    private String time; //시간
    private String entpName; //제조사
    private String Str_form;
    private String Str_type;

    public MedicineItem(){}

    /**
     * SearchActivity에서 검색하기 버튼 눌렀을 때 밑에 RecyclerView 출력되게 하는 아이템
     *
     */
    public MedicineItem(String itemSeq, String url, String name, String entpName, String Str_form, String Str_type) {
        this.itemSeq = itemSeq;
        this.url = url;
        this.name = name;
        this.entpName = entpName;
        this.Str_form = Str_form;
        this.Str_type = Str_type;
    }
    public MedicineItem(String url, String name, String itemSeq, String time, String entpName){
        this.url = url;
        this.name = name;
        this.itemSeq=itemSeq;
        this.time=time;
        this.entpName = entpName;
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
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getItemSeq() {
        return itemSeq;
    }
    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEntpName() {
        return entpName;
    }
    public void setEntpName(String entpName) {
        this.entpName = entpName;
    }

}