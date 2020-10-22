package com.example.smrpv2.ui.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smrpv2.R;
import com.example.smrpv2.model.DoseTime;
import com.example.smrpv2.model.MedicineAlarmAskDto;
import com.example.smrpv2.model.MedicineAlarmResponDto;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.SumMedInfo;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.medicine.ListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AlarmEditActivity : AlarmFragment에서 알람 목록 중 하나를 눌렀을 때, 그 클릭한 알람을 수정하기 위한 수정 ACTIVITY
 * 기능 1. 클릭한 알람 수정
 * 기능 2. 클릭한 알람 삭제
 */

/**
 * AlarmEditActivity와 AlarmSetActivity의 XML을 바꿈으로써 서버에서 바꾸어야하는 부분
 * - oneTimeDose()가 원래 EditText로, 숫자와 관련. 근데 xml이 아침, 점심, 저녁의 3가지 버튼으로 바뀜. => 이거에 대한 서버 값 불러와야 할 듯
 * - 근데 알람fragment의 알람 리스트에는 oneTimeDose가 숫자로 표기됨. 그래서 임시로 oneTimeDoseCount변수를 만들어 클릭된 거 개수 서버로 보내줌ㅈ => 이것도 바꿀 필요 있어 보임.
 */

public class AlarmEditActivity extends AppCompatActivity  {
    Context context;
    MedicineAlarmResponDto tempBefore; //이전에 정보
    ListView Lst_medicine;
    ListViewAdapter alarmListViewAdapter; //알람에 약을 추가한 어댑터
    ImageView ic_dot;
    ImageView iv_back;
    Button Btn_add,Btn_edit,btn_before,btn_after;
    Button Btn_morning, Btn_afternoon, Btn_evening, Btn_addDate, Btn_init;
    EditText et_alramName,et_dosingPeriod;
    String remainingTime;
    Long groupId;
    String user_id;
    final int ALARM = 1;
    final int BEFORE_MEAL=1;
    final int AFTER_MEAL=0;
    final int AFTERNOON_CHECK=0;
    final int MORNING_CHECK=0;
    final int EVENING_CHECK=0;
    final int NOT_VALUE = -10;
    int init_dosingPeriod=-10;
    int oneTimeDoseCount=0;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private static int dosingType=1;
    ArrayList<MedicineItem> alarmMedicineList=new ArrayList<>(); // 약추가한 리스트
    ArrayList<String> array_oneTimeDose = new ArrayList<String>(Arrays.asList("아침","점심","저녁"));
    ArrayList<String> selectedOneTimeDose= new ArrayList<String>();
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        context=this;
        Intent intent = getIntent();
        SharedPreferences loginInfromation = getSharedPreferences("setting",0);
        user_id = loginInfromation.getString("id","");
        groupId = intent.getLongExtra("groupId",0);
        remainingTime = intent.getStringExtra("remainingTime");
        iv_back = findViewById(R.id.iv_back);

        Btn_add = findViewById(R.id.Btn_add);
        Lst_medicine=findViewById(R.id.Lst_medicine2);
        ic_dot = findViewById(R.id.ic_dot);
        Btn_edit= findViewById(R.id.Btn_edit);
        btn_before=findViewById(R.id.btn_before);
        btn_after=findViewById(R.id.btn_after);
        et_alramName=findViewById(R.id.et_alramName);
        et_dosingPeriod=findViewById(R.id.et_dosingPeriod);
     //   et_oneTimeDose=findViewById(R.id.et_oneTimeDose);
        Btn_addDate = findViewById(R.id.btn_addDate);
        Btn_init = findViewById(R.id.btn_init);
        Btn_morning = findViewById(R.id.btn_morning);
        Btn_afternoon = findViewById(R.id.btn_afternoon);
        Btn_evening = findViewById(R.id.btn_evening);
        et_dosingPeriod.setEnabled(false);
        alarmListViewAdapter=new ListViewAdapter(alarmMedicineList,this,0);
        Lst_medicine.setAdapter(alarmListViewAdapter);

        read_alarm(); // 서버로부터 설정한 알람 정보 읽음


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

                dosingType=AFTER_MEAL;
                btn_before.setBackgroundResource(R.drawable.setbtn);
                btn_after.setBackgroundResource(R.drawable.setbtnclick);


            }
        });
        Btn_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedOneTimeDose(2,selectedOneTimeDose,Btn_evening);

            }
        });
        Btn_afternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedOneTimeDose(1,selectedOneTimeDose,Btn_afternoon);
            }
        });

        Btn_morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedOneTimeDose(0,selectedOneTimeDose,Btn_morning);
            }
        });
        Btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(init_dosingPeriod ==NOT_VALUE)
                    et_dosingPeriod.setText(String.valueOf(0));
                else
                    et_dosingPeriod.setText(String.valueOf(init_dosingPeriod));
            }
        });
        Btn_addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dosingPeriod;

                if(init_dosingPeriod ==NOT_VALUE) {dosingPeriod = 0;init_dosingPeriod=dosingPeriod;}
                else dosingPeriod = Integer.parseInt(et_dosingPeriod.getText().toString())+1;
                et_dosingPeriod.setText(String.valueOf(dosingPeriod));



            }
        });

        ic_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // dialog를 띄울 Activity에서 구현
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                bottomSheetDialog.init(groupId, ALARM);
                bottomSheetDialog.show(getSupportFragmentManager(),"bottomSheet");
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
        Btn_edit.setOnClickListener(new View.OnClickListener() {//알람설정을 누른경우
            @Override
            public void onClick(View v) {
              //수정 누르면 수정하기 작업

                if ( et_alramName.getText().toString().equals("") || et_dosingPeriod.getText().toString().equals("")
                       ) {
                    Toast.makeText(context, "모두 입력해 주세요 .", Toast.LENGTH_SHORT).show();
                }else{
                    ArrayList<String> temp = new ArrayList<String>(); //일련번호 리스트를 만드는과정
                    for(MedicineItem i :alarmMedicineList){
                        temp.add(i.getItemSeq());
                    }
                    if (temp.size() == 0) {
                        Toast.makeText(context, "약을 등록해 주세요.", Toast.LENGTH_SHORT).show();
                    }else{

                        /**
                         * 서버
                         */

                        ArrayList<Long> registerId = new ArrayList<>();

                        for(MedicineItem item :alarmMedicineList){
                            registerId.add(item.getId());
                        }
                        String alarmName =et_alramName.getText().toString();
                        int dosingPeriod =Integer.parseInt(et_dosingPeriod.getText().toString());

                        String doseType ;
                        if(dosingType==1){
                            doseType="식전";
                        }else{
                            doseType = "식후";

                        }
                        //DosTime 아침 점심 저녁 어떤것인지 판단 하는 로직  DoseTime 에는 Y OR N 가 들어간다.
                        DoseTime doseTime = new DoseTime("N", "N", "N");

                        for (String dose : selectedOneTimeDose) {
                            switch (dose) {
                                case "아침": {
                                    doseTime.setMorning("Y");
                                    break;
                                }
                                case "점심": {
                                    doseTime.setLunch("Y");
                                    break;
                                }
                                case "저녁": {
                                    doseTime.setDinner("Y");
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                        }

                        //이전의 값을 넣어준다 .  현재는  startAlarm과 finishAlarm은 못바꾸게함 따라서 dosingPeriod를 변경 불가능함.
                        Log.d("변경하기전", tempBefore.getFinishAlarm()+"");
                        Log.d("변경하기전남은시간", remainingTime+"");

                        //예외처리 남은 기간보다 더적은  기간을 입력하게되면 입력 하지 못하게 막음..
                        String[] split = remainingTime.split("/");  // [0] =  진행된 일수  [1] 총 진행해야하는  일수
                        



                        MedicineAlarmAskDto medicineAlarmAskDto = new MedicineAlarmAskDto(tempBefore.getId(),"q",registerId,alarmName,dosingPeriod,tempBefore.getStartAlarm(),tempBefore.getFinishAlarm(),doseTime,doseType);
                        Call<Message> call = RetrofitHelper.getRetrofitService_server().medicineAlarmUpdate(medicineAlarmAskDto);
                        call.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                if(response.body().getResultCode().equals("OK")){
                                    Log.d("변경완료", "변경");
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                Log.d("변경 불가능", t.toString());
                            }
                        });


                        Toast.makeText(AlarmEditActivity.this, "수정", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }

            }
        });
    }
    private void showAlertDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_med_dialog, null);
        builder.setView(view);
        final ArrayList<MedicineItem> items = new ArrayList<>();
        final AlertDialog dialog = builder.create();

        final Button Btn_ok = view.findViewById(R.id.Btn_ok);
        ListView Lst_medicine = view.findViewById(R.id.Lst_medicine);
        final ListViewAdapter adapter = new ListViewAdapter(items, this,-1);

        Lst_medicine.setAdapter(adapter);
        Btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 확인 버튼 누르기
                Toast.makeText(getApplicationContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();
                alarmMedicineList.addAll(adapter.res());


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
    void read_alarm(){
        Call<MedicineAlarmResponDto> call = RetrofitHelper.getRetrofitService_server().getMedicineAlarm(groupId);
        call.enqueue(new Callback<MedicineAlarmResponDto>() {
            @Override
            public void onResponse(Call<MedicineAlarmResponDto> call, Response<MedicineAlarmResponDto> response) {
                tempBefore=response.body();

                et_alramName.setText(response.body().getAlarmName());
                String doseType = response.body().getDoseType();
                if(doseType.equals("식전")){
                    dosingType=BEFORE_MEAL;
                    btn_before.setBackgroundResource(R.drawable.setbtnclick);
                    btn_after.setBackgroundResource(R.drawable.setbtn);
                }else{
                    dosingType=AFTER_MEAL;
                    btn_before.setBackgroundResource(R.drawable.setbtn);
                    btn_after.setBackgroundResource(R.drawable.setbtnclick);
                }
                //et_oneTimeDose.setText(String.valueOf(response.body().getOneTimeCapacity()));
                DoseTime doseTime = tempBefore.getDoseTime();
                int[] doseTimes = doseTime.getDoseTime();
                if(doseTimes[0]==1){
                    checkSelectedOneTimeDose(0,selectedOneTimeDose,Btn_morning);
                }
                if(doseTimes[1]==1){
                    checkSelectedOneTimeDose(1,selectedOneTimeDose,Btn_afternoon);
                }
                if(doseTimes[2] ==1){
                    checkSelectedOneTimeDose(2,selectedOneTimeDose,Btn_evening);
                }

                et_dosingPeriod.setText(String.valueOf(response.body().getDosingPeriod()));
                init_dosingPeriod = response.body().getDosingPeriod();

                ArrayList<SumMedInfo> regMedicineArrayList = response.body().getRegMedicineArrayList();

                for (SumMedInfo sumMedInfo : regMedicineArrayList) {
                    alarmMedicineList.add(new MedicineItem(sumMedInfo.getId(),sumMedInfo.getImageUrl(),sumMedInfo.getItemName(),sumMedInfo.getItemSeq()
                    ,sumMedInfo.getCreatedAt(),sumMedInfo.getEntpName()));
                }
                alarmListViewAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<MedicineAlarmResponDto> call, Throwable t) {
                Log.d("알람 가져오기", t.toString());
            }
        });
        /**
         *
         *  서버 내용. 연결이 안돼서 임시로 비움. 나중에 연결 후 추가예정
         *
         *
         */

    }
    void checkSelectedOneTimeDose(int position, ArrayList<String> item, Button bt){
        if (mSelectedItems.get(position, false) ){
            mSelectedItems.put(position, false);
            bt.setBackgroundResource(R.drawable.setbtn);
            bt.setTextColor(Color.BLACK);

        }
        else {
            mSelectedItems.put(position, true);
            bt.setBackgroundResource(R.drawable.setbtnclick);
            bt.setTextColor(Color.WHITE);
        }

        for(int i =0; i<3; i++) {
            if(i==0){item.clear(); oneTimeDoseCount=0;}
            if (mSelectedItems.get(i, false)) {
                item.add(array_oneTimeDose.get(i));
                Log.e("fff", array_oneTimeDose.get(i));
                oneTimeDoseCount++;
            }
        }

    }
}
