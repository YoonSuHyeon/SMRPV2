package com.example.smrpv2.model;

import java.io.Serializable;

/**
 * MedListViewItem : 약 정보
 */
public class MedListViewItem implements Serializable {
    private String url; //약 이미지
    private String name; //약 이름
    private String itemSeq; //약 식별번호
    private String time; //시간
    private String entpName; //제조사

    public MedListViewItem(String url, String name, String itemSeq, String time, String entpName){
        this.url = url;
        this.name = name;
        this.itemSeq=itemSeq;
        this.time=time;
        this.entpName = entpName;
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
