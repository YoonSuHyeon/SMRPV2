package com.example.smrpv2.retrofit;

import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.RegmedicineAsk;
import com.example.smrpv2.model.SumMedInfo;
import com.example.smrpv2.model.hospital_model.Response_hos;
import com.example.smrpv2.model.pharmcy_model.Response_phy;
import com.example.smrpv2.model.searchMed_model.ConMedicineAskDto;
import com.example.smrpv2.model.searchMed_model.MedicineInfoRsponDTO;
import com.example.smrpv2.model.user_model.LoginUser;
import com.example.smrpv2.model.home_model.Weather_response;
import com.example.smrpv2.model.user_model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService_Server {

    @GET("user/findId")
    Call<Message> findId( //아이디 중복을 위한 메시지..
                          @Query("userId") String userId
    );

    @POST("/user/join")
    Call<Message> join( //회원가입
                        @Body
                                User user
    );

    @POST("/user/login")
    Call<Message> login( //로그인
                         @Body LoginUser loginUser
    );

    @POST("/medicine/search/condition")
    Call<List<MedicineInfoRsponDTO>> findList( // 약 검색
                                               @Body ConMedicineAskDto selectedItem
    );

    @GET("/medicine/search")
    Call<MedicineInfoRsponDTO> getMedicine( // 아이템 번호로 약 검색하기
            @Query("itemSeq") String itemSeq
    );

    @POST("/medicine/ocr")
    Call<MedicineInfoRsponDTO> medicineOcr(//사진촬영 OCR
                                            @Body String[] medicineLogo
    );

    @GET("/medicine/registers")
    Call<ArrayList<SumMedInfo>> medicineRegs( //사용자 등록된 약
                                              @Query("userId") String userId
    );

    @POST("/medicine/register/add")
    Call<Message>medicineAdd(// 약등록하기
                            @Body RegmedicineAsk regmedicineAsk
                            );






    /*병원 찾기 기능에 필요한 요청 메시지*/
    @GET("/B551182/hospInfoService/getHospBasisList?serviceKey=LjJVA0wW%2BvsEsLgyJaBLyTywryRMuelTIYxsWnQTaPpxdZjpuxVCdCtyNxvObDmBJ57VVaSi3%2FerYKQFQmKs8g%3D%3D&_type=json")
//("/userInfo")
    Call<Response_hos> gethosList(@Query("yPos") double lat, @Query("xPos") double lng, @Query("radius") Integer m, @Query("dgsbjtCd") String dgsbjtCd);

    /*약국 찾기 기능에 필요한 요청 메시지*/
    @GET("/B551182/pharmacyInfoService/getParmacyBasisList?ServiceKey=LjJVA0wW%2BvsEsLgyJaBLyTywryRMuelTIYxsWnQTaPpxdZjpuxVCdCtyNxvObDmBJ57VVaSi3%2FerYKQFQmKs8g%3D%3D")
    Call<Response_phy> getphyList(@Query("xPos") double lat, @Query("yPos") double lng, @Query("radius") Integer m);

    /*날씨 찾기 기능에 필요한 요청 메시지*/
    @GET("weather?APPID=49168c2b50d7dfa50b8e7a0054b1b229&lang=kr&units=metric")
// 날씨 정보를 요청 메시지
    Call<Weather_response> getweatherList(@Query("lat") double lat, @Query("lon") double lon);

}
