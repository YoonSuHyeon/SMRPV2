package com.example.smrpv2.ui.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.smrpv2.model.MedicineAlarmResponDto;
import com.example.smrpv2.model.user_model.UserDto;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmAllAdd extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Call<ArrayList<MedicineAlarmResponDto>> call = RetrofitHelper.getRetrofitService_server().getMedicineAlarmAll(UserInform.getUserId());
        call.enqueue(new Callback<ArrayList<MedicineAlarmResponDto>>() {
            @Override
            public void onResponse(Call<ArrayList<MedicineAlarmResponDto>> call, Response<ArrayList<MedicineAlarmResponDto>> response) {
                Log.d("AlarmAllAdd", "onResponse: "+response.body().size());
            }

            @Override
            public void onFailure(Call<ArrayList<MedicineAlarmResponDto>> call, Throwable t) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


}
