package com.example.smrpv2.ui.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import com.example.smrpv2.model.AlarmListDto;
import com.example.smrpv2.model.DoseTime;
import com.example.smrpv2.model.MedicineAlarmAskDto;
import com.example.smrpv2.model.MedicineAlarmResponDto;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.SumMedInfo;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.medicine.ListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AlarmSetActivity : AlarmFragment에서 +버튼 눌렀을 때 알람을 설정하는 액티비티
 */

/**
 * AlarmEditActivity와 AlarmSetActivity의 XML을 바꿈으로써 서버에서 바꾸어야하는 부분
 * - oneTimeDose()가 원래 EditText로, 숫자와 관련. 근데 xml이 아침, 점심, 저녁의 3가지 버튼으로 바뀜. => 이거에 대한 서버 값 불러와야 할 듯
 * - 근데 알람fragment의 알람 리스트에는 oneTimeDose가 숫자로 표기됨. 그래서 임시로 oneTimeDoseCount변수를 만들어 클릭된 거 개수 서버로 보내줌ㅈ => 이것도 바꿀 필요 있어 보임.
 */

public class AlarmSetActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";


    Context context;
    AlarmManager alarmManager;
    private InputMethodManager imm;

    ListView Lst_medicine;
    ListViewAdapter alarmListViewAdapter; //알람에 약을 추가한 어댑터
    Button Btn_add, btn_Set_Alarm, btn_before, btn_after;
    Button Btn_morning, Btn_afternoon, Btn_evening, Btn_addDate, Btn_init;
    EditText et_alramName, et_dosingPeriod, et_oneTimeDose;
    ImageView iv_back;

    String back = "a";
    String user_id;
    int count = 0;
    private static int dosingType = 1;
    final int BEFORE_MEAL = 1;
    final int AFTER_MEAL = 0;
    final int AFTERNOON_CHECK = 0;
    final int MORNING_CHECK = 0;
    final int EVENING_CHECK = 0;

    int oneTimeDoseCount = 0;
    int init_dosingPeriod = -10;
    final int NOT_VALUE = -10;
    ArrayList<MedicineItem> alarmMedicineList = new ArrayList<>(); // 약추가한 리스트
    ArrayList<MedicineItem> list = new ArrayList<>();
    ArrayList<String> array_oneTimeDose = new ArrayList<String>(Arrays.asList("아침", "점심", "저녁"));
    ArrayList<String> selectedOneTimeDose = new ArrayList<String>();
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

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

        //알람초기화
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        final Calendar calendar = Calendar.getInstance();

        Intent intent = getIntent();
        SharedPreferences loginInfromation = getSharedPreferences("setting", 0);
        user_id = loginInfromation.getString("id", "");
        list = (ArrayList<MedicineItem>) intent.getSerializableExtra("list");
        back = intent.getStringExtra("back");

        iv_back = findViewById(R.id.iv_back);
        Btn_add = findViewById(R.id.Btn_add);
        btn_before = findViewById(R.id.btn_before);
        btn_after = findViewById(R.id.btn_after);
        btn_Set_Alarm = findViewById(R.id.btn_set_alarm);
        et_alramName = findViewById(R.id.et_alramName);
        et_dosingPeriod = findViewById(R.id.et_dosingPeriod);
        // et_oneTimeDose = findViewById(R.id.et_oneTimeDose);
        Btn_addDate = findViewById(R.id.btn_addDate);
        Btn_init = findViewById(R.id.btn_init);
        Btn_morning = findViewById(R.id.btn_morning);
        Btn_afternoon = findViewById(R.id.btn_afternoon);
        Btn_evening = findViewById(R.id.btn_evening);

        Lst_medicine = findViewById(R.id.Lst_medicine2);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        alarmListViewAdapter = new ListViewAdapter(alarmMedicineList, this, 0); //alarmMedicineList =ArrayList
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
                dosingType = BEFORE_MEAL;
                btn_before.setBackgroundResource(R.drawable.setbtnclick);
                btn_before.setTextColor(Color.WHITE);
                btn_after.setBackgroundResource(R.drawable.setbtn);
                btn_after.setTextColor(Color.BLACK);

            }
        });
        btn_after.setOnClickListener(new View.OnClickListener() { //식후버튼을 눌렀을 때
            @Override
            public void onClick(View view) {
                dosingType = AFTER_MEAL;
                btn_before.setBackgroundResource(R.drawable.setbtn);
                btn_before.setTextColor(Color.BLACK);
                btn_after.setBackgroundResource(R.drawable.setbtnclick);
                btn_after.setTextColor(Color.WHITE);

            }
        });

        Btn_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedOneTimeDose(2, selectedOneTimeDose, Btn_evening);

            }
        });
        Btn_afternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedOneTimeDose(1, selectedOneTimeDose, Btn_afternoon);
            }
        });

        Btn_morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedOneTimeDose(0, selectedOneTimeDose, Btn_morning);
            }
        });
        Btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (init_dosingPeriod == NOT_VALUE)
                    et_dosingPeriod.setText(String.valueOf(0));
                else
                    et_dosingPeriod.setText(String.valueOf(init_dosingPeriod));
            }
        });
        Btn_addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dosingPeriod;

                if (init_dosingPeriod == NOT_VALUE) {
                    dosingPeriod = 0;
                    init_dosingPeriod = dosingPeriod;
                } else dosingPeriod = Integer.parseInt(et_dosingPeriod.getText().toString()) + 1;
                et_dosingPeriod.setText(String.valueOf(dosingPeriod));


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


                if (et_alramName.getText().toString().equals("") || et_dosingPeriod.getText().toString().equals("")
                ) {
                    Toast.makeText(context, "모두 입력해 주세요 .", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> temp = new ArrayList<String>(); //일련번호 리스트를 만드는과정
                    for (MedicineItem i : alarmMedicineList) {
                        temp.add(i.getItemSeq());
                    }
                    if (temp.size() == 0) {
                        Toast.makeText(context, "약을 등록해 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        final ArrayList<Long> registerId = new ArrayList<>();

                        for (MedicineItem item : alarmMedicineList) {//사용자가 선택한 알람 등록할 약리스트를 다시 list에 담는과정
                            registerId.add(item.getId()); //사용자가 선택한 알람 등록할 약리스트
                        }
                        String alarmName = et_alramName.getText().toString();
                        int dosingPeriod = Integer.parseInt(et_dosingPeriod.getText().toString());
                        //int oneTimeCapacity =oneTimeDoseCount;//Integer.parseInt(et_oneTimeDose.getText().toString());
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
                        Log.d("dose", "아침" + doseTime.getMorning());
                        Log.d("dose", "점심" + doseTime.getLunch());
                        Log.d("dose", "저녁" + doseTime.getDinner());

                        String doseType;
                        if (dosingType == 1) {
                            doseType = "식전";
                        } else {
                            doseType = "식후";

                        }
                        final MedicineAlarmAskDto medicineAlarmAskDto = new MedicineAlarmAskDto(0, UserInform.getUserId(), registerId, alarmName, dosingPeriod, null, null, doseTime, doseType);
                        Call<MedicineAlarmResponDto> call = RetrofitHelper.getRetrofitService_server().addMedicineAlarm(medicineAlarmAskDto); //서버에 알람등록
                        call.enqueue(new Callback<MedicineAlarmResponDto>() {
                            @Override
                            public void onResponse(Call<MedicineAlarmResponDto> call, Response<MedicineAlarmResponDto> response) {

                                MedicineAlarmResponDto medicineAlarmResponDto = response.body();
                                Log.d("알람설정중에서", response.body().getAlarmName());

                                //알람등록
                                setAlarm(medicineAlarmResponDto);
                                onBackPressed();

                            }

                            @Override
                            public void onFailure(Call<MedicineAlarmResponDto> call, Throwable t) {

                            }
                        });


                        /**
                         * 서버
                         */
                    }
                }
            }


        });


    }

    private void setAlarm(MedicineAlarmResponDto medicineAlarmResponDto) {
        Date currentTime = Calendar.getInstance().getTime();
        GregorianCalendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(currentTime);

        //식전 7:30  12:00 18:40
        //식후 8:00 13:00  19:40
        String doseType = medicineAlarmResponDto.getDoseType();
        int dosingPeriod = medicineAlarmResponDto.getDosingPeriod();
        DoseTime doseTime = medicineAlarmResponDto.getDoseTime();
        String content = medicineAlarmResponDto.getAlarmName();
        List<AlarmListDto> alarmListList = medicineAlarmResponDto.getAlarmListList();


        int count = 0; //알람PK의 수를 세기위함
        if (doseType.equals("식전")) {//식전
            for (int i = 0; i < dosingPeriod; i++) {
                GregorianCalendar temp = cal;
                temp.add(Calendar.DATE, i);
                if (doseTime.getMorning().equals("Y")) {
                    Long id = alarmListList.get(count).getId();
                    PendingIntent pendingIntent = makePendingIntent(Long.valueOf(id).intValue(), content);

                    temp.set(Calendar.HOUR_OF_DAY, 7);
                    temp.set(Calendar.MINUTE, 30);
                    Log.d("time", cal.getTime().toString());
                    Log.d("time2", temp.getTime().toString());
                    if (!cal.getTime().after(temp.getTime())) {
                        Log.d("알람등록", "알람등록");
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                temp.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    count++;
                }
                if (doseTime.getLunch().equals("Y")) {
                    Long id = alarmListList.get(count).getId();
                    PendingIntent pendingIntent = makePendingIntent(Long.valueOf(id).intValue(), content);
                    temp.set(Calendar.HOUR_OF_DAY, 12);
                    temp.set(Calendar.MINUTE, 00);
                    Log.d("time", cal.getTime().toString());
                    Log.d("time2", temp.getTime().toString());

                    if (!cal.getTime().after(temp.getTime())) {
                        Log.d("알람등록", "알람등록");
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                temp.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    count++;
                }
                if (doseTime.getDinner().equals("Y")) {
                    Long id = alarmListList.get(count).getId();

                    PendingIntent pendingIntent = makePendingIntent(Long.valueOf(id).intValue(), content);
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat hh_format = new SimpleDateFormat("hh");
                    SimpleDateFormat mm_format = new SimpleDateFormat("mm");
                    Log.d("TAG", "setAlarm: "+hh_format.format(date));
                    Log.d("TAG", "setAlarm: "+Integer.parseInt(mm_format.format(date)));
                    Log.d("TAG", "setAlarm: "+ Integer.parseInt(mm_format.format(date))+5);
                    temp.set(Calendar.HOUR_OF_DAY,18);
                    temp.set(Calendar.MINUTE, 40);

                   /* temp.set(Calendar.HOUR_OF_DAY,18);
                    temp.set(Calendar.MINUTE, 40);*/
                    Log.d("time", cal.getTime().toString());
                    Log.d("time2", temp.getTime().toString());

                    if (!cal.getTime().after(temp.getTime())) {
                        Log.d("알람등록", "알람등록");
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                temp.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    count++;
                }
            }
        } else {//식후
            //식전 7:30  12:00 18:40
            //식후 8:00 13:00  19:40
            for (int i = 0; i < dosingPeriod; i++) {
                GregorianCalendar temp = (GregorianCalendar)cal.clone() ;
                temp.add(Calendar.DATE, i);
                if (doseTime.getMorning().equals("Y")) {
                    temp.set(Calendar.HOUR_OF_DAY, 8);
                    temp.set(Calendar.MINUTE, 0);
                    Log.d("time", cal.getTime().toString());
                    Log.d("time2", temp.getTime().toString());

                    if (!cal.getTime().after(temp.getTime())) {
                        Log.d("알람등록", "알람등록");
                        Long id = alarmListList.get(count).getId();
                        PendingIntent pendingIntent = makePendingIntent(Long.valueOf(id).intValue(), content);
                          alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                temp.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    count++;
                }
                if (doseTime.getLunch().equals("Y")) {
                    temp.set(Calendar.HOUR_OF_DAY, 13);
                    temp.set(Calendar.MINUTE, 0);
                    Log.d("time", cal.getTime().toString());
                    Log.d("time2", temp.getTime().toString());

                    if (!cal.getTime().after(temp.getTime())) {
                        Log.d("알람등록", "알람등록");
                        Long id = alarmListList.get(count).getId();
                        PendingIntent pendingIntent = makePendingIntent(Long.valueOf(id).intValue(), content);
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                temp.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    count++;

                }
                if (doseTime.getDinner().equals("Y")) {
                    temp.set(Calendar.HOUR_OF_DAY, 19);
                    temp.set(Calendar.MINUTE, 40);
                    Log.d("time", cal.getTime().toString());
                    Log.d("time2", temp.getTime().toString());

                    if (!cal.getTime().after(temp.getTime())) {//지난 시간 은 등록안하게하기위함
                        Long id = alarmListList.get(count).getId();
                        Log.d("알람등록", "알람등록");
                        PendingIntent pendingIntent = makePendingIntent(Long.valueOf(id).intValue(), content);
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                temp.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    count++;


                }
            }
        }
        Log.d("알람", "알람등록");
    }

    private PendingIntent makePendingIntent(int privateId, String content) {
        Intent aIntent = new Intent(this, AlarmReceiver.class);
        aIntent.putExtra("content", content);
        aIntent.putExtra("privateId", privateId);
        return PendingIntent.getBroadcast(context, privateId, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_med_dialog, null);
        builder.setView(view);
        final ArrayList<MedicineItem> items = new ArrayList<>();
        final AlertDialog dialog = builder.create();

        ListView Lst_medicine = view.findViewById(R.id.Lst_medicine); //약 추가하기 팝업 창 내가 등록한 약
        final Button Btn_ok = view.findViewById(R.id.Btn_ok);
        final ListViewAdapter adapter = new ListViewAdapter(items, this, -1);

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

        Call<ArrayList<SumMedInfo>> call = RetrofitHelper.getRetrofitService_server().medicineRegs(UserInform.getUserId());
        call.enqueue(new Callback<ArrayList<SumMedInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<SumMedInfo>> call, Response<ArrayList<SumMedInfo>> response) {
                ArrayList<SumMedInfo> med_items = response.body();

                items.clear();
                for (int i = 0; i < med_items.size(); i++) {
                    items.add(new MedicineItem(med_items.get(i).getId(), med_items.get(i).getImageUrl(), med_items.get(i).getItemName(), med_items.get(i).getItemSeq(), med_items.get(i).getCreatedAt(), med_items.get(i).getEntpName()));


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

    void checkSelectedOneTimeDose(int position, ArrayList<String> item, Button bt) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.put(position, false);
            bt.setBackgroundResource(R.drawable.setbtn);
            bt.setTextColor(Color.BLACK);

        } else {
            mSelectedItems.put(position, true);
            bt.setBackgroundResource(R.drawable.setbtnclick);
            bt.setTextColor(Color.WHITE);
        }

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                item.clear();
                oneTimeDoseCount = 0;
            }
            if (mSelectedItems.get(i, false)) {
                item.add(array_oneTimeDose.get(i));
                Log.e("fff", array_oneTimeDose.get(i));
                oneTimeDoseCount++;
            }
        }

    }
}
