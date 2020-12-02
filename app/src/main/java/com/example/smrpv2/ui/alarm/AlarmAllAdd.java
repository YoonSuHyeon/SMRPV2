package com.example.smrpv2.ui.alarm;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.smrpv2.model.MedicineAlarmResponDto;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.common.SharedData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmAllAdd extends Service {
    public AlarmAllAdd() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public ComponentName startService(Intent service) {
        //Toast.makeText(this, "startService", Toast.LENGTH_SHORT).show();
        return super.startService(service);
    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        //Toast.makeText(this, "startForegroundService", Toast.LENGTH_SHORT).show();
        return super.startForegroundService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user_inform", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("user_id","");
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "id========>"+id, Toast.LENGTH_SHORT).show();
        if(!id.equals("")){
            Call<ArrayList<MedicineAlarmResponDto>> call = RetrofitHelper.getRetrofitService_server().getMedicineAlarmAll(id);
            call.enqueue(new Callback<ArrayList<MedicineAlarmResponDto>>() {
                @Override
                public void onResponse(Call<ArrayList<MedicineAlarmResponDto>> call, Response<ArrayList<MedicineAlarmResponDto>> response) {
                    Log.d("AlarmAllAdd", "onResponse: "+response.body().size());
                    for(int i = 0 ; i < response.body().size();i++){
                        MedicineAlarmResponDto item = response.body().get(i);

                    }

                }

                @Override
                public void onFailure(Call<ArrayList<MedicineAlarmResponDto>> call, Throwable t) {
                    Toast.makeText(AlarmAllAdd.this, "onFailureonFailure", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return super.onStartCommand(intent, flags, startId);
    }
}