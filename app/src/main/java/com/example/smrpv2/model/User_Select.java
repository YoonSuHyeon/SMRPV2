package com.example.smrpv2.model;

import java.util.ArrayList;

/**
 * Search_prescriptionActivity에서 사용
 * (나중에 서버 구현 후 다른 Item들과 합쳐질 수 있음)
 */
public class User_Select {
    private String userId;;
    ArrayList<String> itemSeq;

    public User_Select(String id, ArrayList<String> list){
        this.userId = id;
        this.itemSeq = list;
    }

    public String getId() {
        return userId;
    }

    public ArrayList<String> getList() {
        return itemSeq;
    }
}
