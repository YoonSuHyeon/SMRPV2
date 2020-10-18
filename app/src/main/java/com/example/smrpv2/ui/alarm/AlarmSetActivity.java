package com.example.smrpv2.ui.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.smrp.LoginActivity;
import com.example.smrpv2.R;
//import com.example.smrp.RetrofitHelper;
//import com.example.smrp.RetrofitService;
//import com.example.smrp.medicine.ListViewItem;
//import com.example.smrp.reponse_medicine3;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.SumMedInfo;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.medicine.ListViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AlarmSetActivity : AlarmFragment에서 +버튼 눌렀을 때 알람을 설정하는 액티비티
 *
 */
public class AlarmSetActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";


    Context context;
    AlarmManager alarmManager;
    private InputMethodManager imm;

    ListView Lst_medicine;
    ListViewAdapter alarmListViewAdapter; //알람에 약을 추가한 어댑터
    Button Btn_add, btn_Set_Alarm,btn_before,btn_after;
    EditText et_alramName, et_dosingPeriod, et_oneTimeDose;
    ImageView iv_back;

    String back = "a";
    String user_id;
    int count = 0;
    private static int dosingType=1;
    final int BEFORE_MEAL=1;
    final int AFTER_MEAL=0;
    ArrayList<MedicineItem> alarmMedicineList = new ArrayList<>(); // 약추가한 리스트
    ArrayList<MedicineItem> list = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                alarmMedicineList.clear(); // 전에 있던 약 리스트 정보를 삭제
                //반환값과 함께 전달받은 리스트뷰로 갱신하기
                alarmMedicineList.addAll((ArrayList<MedicineItem>) data.getSerializableExtra("listViewItemArrayList"));
                alarmListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        this.context = this;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();

        Intent intent = getIntent();
        SharedPreferences loginInfromation = getSharedPreferences("setting", 0);
        user_id = loginInfromation.getString("id", "");
        list = (ArrayList<MedicineItem>) intent.getSerializableExtra("list");
        back = intent.getStringExtra("back");

        iv_back = findViewById(R.id.iv_back);
        Btn_add = findViewById(R.id.Btn_add);
        btn_before=findViewById(R.id.btn_before);
        btn_after=findViewById(R.id.btn_after);
        btn_Set_Alarm = findViewById(R.id.btn_set_alarm);
        et_alramName = findViewById(R.id.et_alramName);
        et_dosingPeriod = findViewById(R.id.et_dosingPeriod);
        et_oneTimeDose = findViewById(R.id.et_oneTimeDose);
        Lst_medicine = findViewById(R.id.Lst_medicine2);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        alarmListViewAdapter = new ListViewAdapter(alarmMedicineList, this,0); //alarmMedicineList =ArrayList
        Lst_medicine.setAdapter(alarmListViewAdapter);  //Lst_medicine: listView

        if (back != null) {
            alarmMedicineList = (ArrayList<MedicineItem>) intent.getSerializableExtra("listViewItemArrayList");
            alarmListViewAdapter.notifyDataSetChanged();
        } else {
            if (list != null && list.size() > 0) {
                alarmMedicineList.addAll(list);
                alarmListViewAdapter.notifyDataSetChanged();
            }

        }


        btn_before.setOnClickListener(new View.OnClickListener() {//식전버튼을 눌렀을 때
            @Override
            public void onClick(View view) {
                dosingType=BEFORE_MEAL;
                btn_before.setBackgroundResource(R.drawable.setbtnclick);
                btn_after.setBackgroundResource(R.drawable.setbtn);

            }
        });
       btn_after.setOnClickListener(new View.OnClickListener() { //식후버튼을 눌렀을 때
           @Override
           public void onClick(View view) {
               dosingType = AFTER_MEAL;
               btn_before.setBackgroundResource(R.drawable.setbtn);
               btn_after.setBackgroundResource(R.drawable.setbtnclick);

           }
       });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        btn_Set_Alarm.setOnClickListener(new View.OnClickListener() {//알람설정을 누른경우
            @Override
            public void onClick(View v) { // 알람설정

                   /**
                         * 서버
                         */

            }



        });




    }
    private void showAlertDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_med_dialog, null);
        builder.setView(view);
        final ArrayList<MedicineItem> items = new ArrayList<>();
        final AlertDialog dialog = builder.create();

        ListView Lst_medicine = view.findViewById(R.id.Lst_medicine); //약 추가하기 팝업 창 내가 등록한 약
        final Button Btn_ok = view.findViewById(R.id.Btn_ok);
        final ListViewAdapter adapter = new ListViewAdapter(items, this,-1);

        Lst_medicine.setAdapter(adapter);
        Btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 확인 버튼 누르기(약추가하기 기능에서)


                int num = 0;
                Toast.makeText(getApplicationContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();

                ArrayList<MedicineItem> list = adapter.res();
                if (alarmMedicineList.size() == 1) {//등록된 약 기능에서 알람추가시 중복제거

                    for (int i = 0; i < list.size(); i++) {
                        if (alarmMedicineList.get(0).getItemSeq().equals(list.get(i).getItemSeq())) {
                            list.remove(i);
                            num++;
                        }
                    }
                } else {
                    for (int i = 0; i < alarmMedicineList.size(); i++) {
                        for (int j = 0; j < list.size(); j++) {
                            if (alarmMedicineList.get(i).getItemSeq().equals(list.get(j).getItemSeq())) {
                                list.remove(j);
                                num++;
                            }
                        }
                    }
                }

                if (num > 0)
                    Toast.makeText(getApplicationContext(), "중복된 약 " + num + "건을 제외하였습니다.", Toast.LENGTH_SHORT).show();
                alarmMedicineList.addAll(list);


                alarmListViewAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        Call<ArrayList<SumMedInfo>> call= RetrofitHelper.getRetrofitService_server().medicineRegs("q");
        call.enqueue(new Callback<ArrayList<SumMedInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<SumMedInfo>> call, Response<ArrayList<SumMedInfo>> response) {
                ArrayList<SumMedInfo> med_items = response.body();

                items.clear();
                for(int i = 0; i<  med_items.size(); i++)
                {
                    items.add(new MedicineItem(med_items.get(i).getId(),med_items.get(i).getImageUrl(),med_items.get(i).getItemName(),med_items.get(i).getItemSeq(),med_items.get(i).getCreatedAt(),med_items.get(i).getEntpName()));


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<SumMedInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        dialog.show();
    }
}
