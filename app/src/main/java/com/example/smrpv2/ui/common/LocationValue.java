package com.example.smrpv2.ui.common;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationValue {

    private GPSListener gpsListener = null;
    private double latitude =0.0;
    private double longitude =0.0;
    private Location gps_location = null;
    private Location network_location = null;
    private Activity activity;

    /**
     *요청하는 activity를 인자로 받아 GPS 모듈과 네트워크 모듈을 이용하여
     * 위치정보값을 가져옴
     */
    public LocationValue(Activity activity){
        this.activity = activity;
    }
    public void startMoule(){ //GPS와 네트워크 모듈을 이용하여 자표값을 얻는다/
        LocationManager lm_1 = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE); //GPS 모듈을 이용함
        LocationManager lm_2 = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE); //네트워크 모듈을 이용함


        long minTime = 1000;//1000ms ---> 1초
        float minDistance = 10; //10m
        gpsListener = new GPSListener();
        try{
            lm_1.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener); // GPS모듈을 이용하여 위치값 얻음
            lm_2.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,gpsListener); //네트워크를 통해서 위치값을 얻음
            gps_location = lm_1.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 네트워크를 통해서 얻는 마지막 위치값
            network_location = lm_2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(gps_location != null){ // GPS 모듈을 동해서 값을 갖지 못한다면
                latitude = gps_location.getLatitude();// GPS 모듈 경도 값 ex) 37.30616958190577
                longitude = gps_location.getLongitude();//GPS 모듈 위도 값 ex) 127.92099856059595

            }else{
                latitude = network_location.getLatitude();// GPS 모듈 경도 값 ex) 37.30616958190577
                longitude = network_location.getLongitude();//GPS 모듈 위도 값 ex) 127.92099856059595
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

    public Double getLatitude(){
        return latitude;
    } // 위도값을 반환
    public Double getLongitude(){
        return longitude;
    } //경도 값을 반환

    private class GPSListener implements LocationListener {//위치리너스 클래스

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }//위치 공급자의 상태가 바뀔 때 , 위치 이동이나 시간 경과등이 발생시 호출

        @Override
        public void onProviderEnabled(String provider) {

        } //위치 공급자가 사용 가능해질(enabled) 때 호출

        @Override
        public void onProviderDisabled(String provider) {

        }//위치 공급자가 사용 불가능해질(disabled) 때 호출
    }
}
