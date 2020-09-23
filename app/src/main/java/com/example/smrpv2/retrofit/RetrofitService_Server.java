package com.example.smrpv2.retrofit;

import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.User;

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
    Call<Message> join(
            @Body
                    User user
    );

}
