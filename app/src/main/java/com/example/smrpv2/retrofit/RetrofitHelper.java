package com.example.smrpv2.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit을 사용하기 baseURL 지정 .
 * **/
public class RetrofitHelper {

    //싱글톤 서버 retrofit    http://smrp.iptime.org:8080/ 기존서버   ,   aws 서버  http://ec2-15-165-121-89.ap-northeast-2.compute.amazonaws.com:8080/ ,수현서버 :http://192.168.200.124:8090/
    private static Retrofit retrofit = new Retrofit.Builder().baseUrl("http://ec2-15-165-121-89.ap-northeast-2.compute.amazonaws.com:8080/").
            addConverterFactory(GsonConverterFactory.create()).build();
    //GsonConverterFactory.create() : json용; //서버 retrofit
    private static RetrofitService_Server retrofitService_server= retrofit.create(RetrofitService_Server.class); // 팀에서 구축한 서버와 통신 객체 선언


    static public RetrofitService_Server getRetrofitService_server(){//Singletone패턴
        return retrofitService_server;
    }

    static public Retrofit getSearch(){
        return new Retrofit.Builder().baseUrl("http://smrp.iptime.org:8080/").
                addConverterFactory(GsonConverterFactory.create()).build();
    }

    static public Retrofit getPhy(){
        return new Retrofit.Builder().baseUrl("http://apis.data.go.kr").
                addConverterFactory(SimpleXmlConverterFactory.createNonStrict()).build();//SimpleXmlConverterFactory.createNonStrict() : xml용
    }
    static public Retrofit getHos(){
        return new Retrofit.Builder().baseUrl("http://apis.data.go.kr").
                addConverterFactory(GsonConverterFactory.create()).build();
    }

    static public Retrofit getWeather(){
        return new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

}
