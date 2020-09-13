package com.example.smrpv2.model;

import com.google.gson.annotations.SerializedName;

public class Weather_item {



    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String icon;




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
