package com.example.smrpv2.model.prescription_model;

import java.util.ArrayList;

public class RegMedicineList {
    String userId;
    ArrayList<String> itemSeq;


    public RegMedicineList(String userId, ArrayList<String> list) {
        this.userId = userId; //사용자 id
        this.itemSeq = list; //사용자가 선택한 등록할 약 리스트
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setList(ArrayList<String> list) {
        this.itemSeq = list;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getList() {
        return itemSeq;
    }
}
