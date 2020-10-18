package com.example.smrpv2.model.searchMed_model;

import java.util.ArrayList;

public class MedicinPost_Response {
    ArrayList<String> list;
    public MedicinPost_Response(ArrayList<String> list){
        this.list = list;
    }

    public ArrayList<String> getResponselist(){
        return list;
    }
}
