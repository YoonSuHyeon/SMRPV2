package com.example.smrpv2.retrofit;

import com.example.smrpv2.model.home_model.Weather_item;
import com.example.smrpv2.model.home_model.Weather_main;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {


    @SerializedName("weather")
    List<Weather_item> list;
    public List<Weather_item> getweatherList() {
        return list;
    }


    @SerializedName("main")
    Weather_main weather_main;
    public Weather_main getWeather_main() {
        return weather_main;
    }

}
