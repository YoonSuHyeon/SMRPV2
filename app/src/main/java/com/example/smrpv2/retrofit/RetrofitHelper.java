package com.example.smrpv2.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit을 사용하기 baseURL 지정 .
 * **/
public class RetrofitHelper {
    private static Retrofit retrofit = null; //서버 retrofit

    static public Retrofit getRetrofit(){ //싱글톤 서버 retrofit
        if(retrofit==null){
           retrofit= new Retrofit.Builder().baseUrl("http://smrp.iptime.org:8080/").
                    addConverterFactory(GsonConverterFactory.create()).build(); //GsonConverterFactory.create() : json용
        }
        return retrofit;

    }
    static public Retrofit getPharmacy(){
        return new Retrofit.Builder().baseUrl("http://apis.data.go.kr").
                addConverterFactory(SimpleXmlConverterFactory.createNonStrict()).build();//SimpleXmlConverterFactory.createNonStrict() : xml용
    }
}
