package com.example.smrpv2.retrofit;

import com.example.smrpv2.model.InquiryDto;
import com.example.smrpv2.model.MedicineAlarmAskDto;
import com.example.smrpv2.model.MedicineAlarmResponDto;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.RegmedicineAsk;
import com.example.smrpv2.model.SumMedInfo;
import com.example.smrpv2.model.home_model.Covid19_response;
import com.example.smrpv2.model.hospital_model.Response_hos;
import com.example.smrpv2.model.pharmcy_model.Response_phy;
import com.example.smrpv2.model.prescription_model.RegMedicineList;
import com.example.smrpv2.model.prescription_model.User_Select;
import com.example.smrpv2.model.searchMed_model.ConMedicineAskDto;
import com.example.smrpv2.model.searchMed_model.MedicineInfoRsponDTO;
import com.example.smrpv2.model.searchMed_model.OcrSpaceDto;
import com.example.smrpv2.model.user_model.LoginUser;
import com.example.smrpv2.model.home_model.Weather_response;
import com.example.smrpv2.model.user_model.UserDto;
import com.example.smrpv2.model.user_model.User;
import com.example.smrpv2.model.common.KakaoDto;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService_Server {

    @GET("/user/findId")
    Call<Message> findId( //아이디 찾기
                          @Query("name") String name,
                          @Query("email") String email
    );

    @POST("/user/join")
    Call<Message> join( //회원가입
                        @Body
                                User user
    );

    @GET("/user/idCheck")
    Call<Message> overlapId(//아이디 중복 검사
                            @Query("userId") String userId
    );


    @POST("/user/login")
    Call<UserDto> login( //로그인
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
    Call<Message> medicineAdd(// 약등록하기
                              @Body RegmedicineAsk regmedicineAsk
    );

    @POST("/medicine/register/addList")
    Call<Message> medicineListAdd( // 사용자가 선택한 약리스트 등록하기
            @Body RegMedicineList regMedicineList
            );
    @POST("/medicine/search")
    Call<ArrayList<MedicineInfoRsponDTO>> medicinSendList(
            @Body String[] ocrList
    );

    @DELETE("medicine/register/delete")
    Call<Message> delRegMedicine( //약삭제
                                  @Query("registerId") long registerId
    );


    @GET("medicine/alarmAll")
    Call<ArrayList<MedicineAlarmResponDto>> getMedicineAlarmAll( //알람 리스트 가져오기
                                                                 @Query("userId") String userId
    );

    @GET("medicine/alarm")
    Call<MedicineAlarmResponDto> getMedicineAlarm( // 특정한 알람 가져오기
                                                   @Query("medicineAlarmId") Long medicineAlarmId
    );

    @DELETE("medicine/alarm/delete")
    Call<Message> deleteMedicineAlarm( //알람삭제
                                       @Query("medicineAlarmId") long medicineAlarmId
    );

    @POST("medicine/alarm/add")
    Call<MedicineAlarmResponDto> addMedicineAlarm( //알람 등록
                                    @Body MedicineAlarmAskDto medicineAlarmAskDto
    );

    @PUT("medicine/update")
    Call<MedicineAlarmResponDto> medicineAlarmUpdate( //알람 수정
                                       @Body MedicineAlarmAskDto medicineAlarmAskDto
    );


    @POST("/userInfo/inquiry/add")
    Call<Message> addInquiry( //문의하기
                              @Body InquiryDto inquiry
    );
    @POST("medicine/searchOCR") //인식된 글자를 구축 서버에게 전송
    Call<ArrayList<MedicineInfoRsponDTO>> sendWords(
            @Body String[] s
    );

    /*@Multipart
    @POST("/parse/image")
    //@Headers({"Host: dapi.kakao.com","Authorization: KakaoAK 1801da9c015ce87583138632980c2c5a","Content-Type: multipart/form-data"})
    @Headers("apikey: 37a618557788957")
    Call<OcrSpaceDto> sendOcr(//문자인식을 위해 카카오 서버에게 이미지 파일 전송
                         @Header("Host") String header,
                         @Header("Authorization") String authorization,
                         @Header("Content-Type") String content_type,
                              //@HeaderMap Map<String,String> map,
                              @Part MultipartBody.Part file,
                              @Query("language") String language
                              //@String language;
    );*/



    @Multipart
    @Headers("Authorization: KakaoAK 54c2a8378374c5dfdafc6e85dc03a3fa")
    @POST("/v2/vision/text/ocr")// 카카오 OCR API
    Call<KakaoDto> sendKakaoOcr(
                                @Part MultipartBody.Part image);


    @Multipart
    @POST("/medicine/uploadImage") //서버에게 사진전송
    Call<Message> uploadImage(@Part ArrayList<MultipartBody.Part> files);


    /*병원 찾기 기능에 필요한 요청 메시지*/
    @GET("/B551182/hospInfoService/getHospBasisList?serviceKey=LjJVA0wW%2BvsEsLgyJaBLyTywryRMuelTIYxsWnQTaPpxdZjpuxVCdCtyNxvObDmBJ57VVaSi3%2FerYKQFQmKs8g%3D%3D&_type=json")
    Call<Response_hos> gethosList(@Query("yPos") double lat, @Query("xPos") double lng, @Query("radius") Integer m, @Query("dgsbjtCd") String dgsbjtCd);

    /*약국 찾기 기능에 필요한 요청 메시지*/
    @GET("/B551182/pharmacyInfoService/getParmacyBasisList?ServiceKey=LjJVA0wW%2BvsEsLgyJaBLyTywryRMuelTIYxsWnQTaPpxdZjpuxVCdCtyNxvObDmBJ57VVaSi3%2FerYKQFQmKs8g%3D%3D")
    Call<Response_phy> getphyList(@Query("xPos") double lat, @Query("yPos") double lng, @Query("radius") Integer m);


    /*날씨 찾기 기능에 필요한 요청 메시지*/
    @GET("weather?APPID=49168c2b50d7dfa50b8e7a0054b1b229&lang=kr&units=metric")
// 날씨 정보를 요청 메시지
    Call<Weather_response> getweatherList(@Query("lat") double lat, @Query("lon") double lon);

    @GET("/openapi/service/rest/Covid19/getCovid19InfStateJson?ServiceKey=LjJVA0wW%2BvsEsLgyJaBLyTywryRMuelTIYxsWnQTaPpxdZjpuxVCdCtyNxvObDmBJ57VVaSi3%2FerYKQFQmKs8g%3D%3D")
    Call<Covid19_response> getCovid(
            @Query("startCreateDt") String startCreateDt,
            @Query("endCreateDt") String endCreateDt
    );

}
