package com.example.smrpv2.ui.pharmacy;

import android.content.Context;

import net.daum.mf.map.api.MapView;

public class MapViewSingleton {
    private MapView mapView = null;
    Context context = null;
    protected MapViewSingleton(Context context){
        this.context = context;
    } //MapView 객체를 받기 위하며 클래스를 호출하는 해당 Context 값을 저장

    public MapView getMapview(){ //MapView객체를 생성하고 이를 반환
        if(mapView==null){
            mapView = new MapView(context);
        }
        return mapView;
    }
    public void state(){ //MapView 객체의 생성상태를 확인하는 메소드
        System.out.println("mapView state==>"+mapView);
    }
}
