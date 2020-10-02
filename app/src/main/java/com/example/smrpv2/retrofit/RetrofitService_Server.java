package com.example.smrpv2.retrofit;

import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.ConMedicineAskDto;
import com.example.smrpv2.model.MedicineInfoRsponDTO;
import com.example.smrpv2.model.user.LoginUser;
import com.example.smrpv2.model.user.User;

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

    @GET("/medicine2/findItemId")
    Call<MedicineInfoRsponDTO> findmedicine(
            @Query("itemSeq") String itemSeq
    );

    @POST("/medicine/ocr")
    Call<MedicineInfoRsponDTO>medicineOcr(//사진촬영 OCR
            @Body String[] medicineLogo
    );


}
