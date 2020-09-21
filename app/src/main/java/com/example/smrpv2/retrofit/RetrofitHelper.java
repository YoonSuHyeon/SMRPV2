package com.example.smrpv2.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit을 사용하기 baseURL 지정 .
 * **/
public class RetrofitHelper {
    static public Retrofit getRetrofit(){
        return new Retrofit.Builder().baseUrl("http://222.113.57.91:8080/").
                addConverterFactory(GsonConverterFactory.create()).build(); //GsonConverterFactory.create() : json용
    }
    static public Retrofit getPharmacy(){
        return new Retrofit.Builder().baseUrl("http://apis.data.go.kr").
                addConverterFactory(SimpleXmlConverterFactory.createNonStrict()).build();//SimpleXmlConverterFactory.createNonStrict() : xml용
    }
}
