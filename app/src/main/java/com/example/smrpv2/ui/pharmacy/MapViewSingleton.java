package com.example.smrpv2.ui.pharmacy;

import android.content.Context;

import net.daum.mf.map.api.MapView;

public class MapViewSingleton {
    private MapView mapView = null;
    Context context = null;
    protected MapViewSingleton(Context context){
        this.context = context;
    }

    public MapView getMapview(){
        if(mapView==null){
            mapView = new MapView(context);
        }
        return mapView;
    }
    public void state(){
        System.out.println("mapView state==>"+mapView);
    }
}
