package com.example.smrpv2.model.home_model;

import com.google.gson.annotations.SerializedName;

public class Weather_main {

    @SerializedName("temp") private double temp; //현재온도

    public double getTemp() {
        return temp;
    }

    @SerializedName("feels_like") private double feels_like; //체감온도
    public double getFells_like(){
        return feels_like;
    }
    @SerializedName("temp_min") private double temp_min;//최저온도
    public double getTemp_min(){
        return temp_min;
    }

    @SerializedName("temp_max") private double temp_max;//최고온도
    public double getTemp_max(){
        return temp_max;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @SerializedName("humidity")
    private String humidity;

    public String getHumidity(){
        return humidity;
    }
    public void setHumidity(String humidity){
        this.humidity = humidity;
    }
}
