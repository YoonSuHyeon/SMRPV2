package com.example.smrpv2.model;

import java.util.ArrayList;

public class PillName {
    private ArrayList<String> list;

    public PillName(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }
}
