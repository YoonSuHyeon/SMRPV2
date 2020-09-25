package com.example.smrpv2.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.smrpv2.model.hospital_model.Response_hos;
import com.example.smrpv2.model.pharmcy_model.PharmacyItems;
import com.example.smrpv2.model.pharmcy_model.Response_phy;

public interface RetrofitService_pharmacy {
    @GET("/B551182/pharmacyInfoService/getParmacyBasisList?ServiceKey=LjJVA0wW%2BvsEsLgyJaBLyTywryRMuelTIYxsWnQTaPpxdZjpuxVCdCtyNxvObDmBJ57VVaSi3%2FerYKQFQmKs8g%3D%3D")
    Call<Response_phy> getList(@Query("xPos") double lat, @Query("yPos") double lng, @Query("radius") Integer m);

}
