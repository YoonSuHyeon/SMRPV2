package com.example.smrpv2.ui.medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.RegmedicineAsk;
import com.example.smrpv2.model.SumMedInfo;
import com.example.smrpv2.model.searchMed_model.MedicineInfoRsponDTO;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.alarm.AlarmSetActivity;
import com.example.smrpv2.ui.alarm.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MedicineDetailActivity : 약 리스트 클릭 시 상세 정보 보여줌
 * 1. MedicineFragment에서 쓰이는 경우 :
 *   1) MedicineFragment의 약 리스트 중 하나 선택 시 이 약을 알람으로 설정할 수 있는 알람설정 버튼 활성화
 *   2) 점 세 개를 눌러 삭제 시 약이 MedicineFragment에서 삭제 됨
 * 2. 그외 경우 :
 *   1) 단순한 약 상세 정보 보여줌
 *   2) 점 세 개를 눌러 삭제 시 AlarmEditActivity 또는 AlarmSetActivity에서 알람을 설정하기 위해 추가한 약 삭제
 */
public class MedicineDetailActivity extends AppCompatActivity implements Serializable {
    Context context;

    ImageView medicineImage;//약 사진
    ImageView iv_back; //뒤로가기 이미지뷰
    ImageView ic_dot;
    Button Btn_set;
    Button Btn_add;
    TextView medicineName,medicineEntpName,medicineChart,medicineClassName,medicineEtcOtcName,medicineEffect,medicineUsage;

    String itemSeq ,time, search;// intent용 변수
    private String str_image, str_name, str_seq,str_eq;
    String user_id;
    final int MEDICINE =0;
    final int TEMP_ALARM=2;
    private ArrayList<MedicineItem> listViewItemArrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);
        context=this;



        //초기화..
        Btn_set = findViewById(R.id.btn_set);
        Btn_add = findViewById(R.id.btn_add);
        medicineImage=findViewById(R.id.iv_medicine);
        iv_back=findViewById(R.id.iv_back);
        ic_dot = findViewById(R.id.ic_dot);

        medicineName=findViewById(R.id.tv_medicine_name) ;    //약이름
        medicineEntpName=findViewById(R.id.tv_entpName);//약 제조사
        medicineChart=findViewById(R.id.tv_chart);//약성상
        medicineClassName=findViewById(R.id.tv_className);//약분류
        medicineEtcOtcName=findViewById(R.id.tv_etcOtcName);//약구분
        medicineEffect=findViewById(R.id.tv_effect);//약효능효과
        medicineUsage=findViewById(R.id.tv_usage);//약용법용량

        SharedPreferences loginInfromation = getSharedPreferences("setting",0);
        user_id = loginInfromation.getString("id","");

        //itemSeq 받는 과정
        Intent intent =getIntent();
        itemSeq =intent.getStringExtra("itemSeq");
        time = intent.getStringExtra("time");
        search = intent.getStringExtra("Search");
        listViewItemArrayList = (ArrayList<MedicineItem>) intent.getSerializableExtra("listViewItemArrayList");

        display_medicineDetailInform(itemSeq);



        if(time != null){ // time이 null이 아닌 경우는 MedicineFragment에서 약 클릭한 경우

            Btn_set.setVisibility(View.VISIBLE);
        }else if(search != null){ // search가 null이 아닌 경우 SearchActivity에서 검색된 약 클릭한 경우
            Btn_add.setVisibility(View.VISIBLE);
            ic_dot.setVisibility(View.INVISIBLE);
        }



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MedicineItem> list = new ArrayList<>();
                list.add(new MedicineItem(str_image,str_name,str_seq,time,str_eq)); //ListViewItem 클래스의 성질을 가지고 있는 ArrayList 객체에 정보(약 이미지url, 약 이름, 약 식별번호

                Intent intent= new Intent(getApplicationContext(), AlarmSetActivity.class);
                intent.putExtra("list",list);
                startActivity(intent);

            }
        });
        Btn_add.setOnClickListener(new View.OnClickListener() { //약추가하는 버튼..
            @Override
            public void onClick(View v) {

                RegmedicineAsk regmedicineAsk = new RegmedicineAsk("q",itemSeq);
                Call<Message> call= RetrofitHelper.getRetrofitService_server().medicineAdd(regmedicineAsk);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                      /*  if(response.body().getResultCode().equals("PASS")){
                            Toast.makeText(context, "등록 완료", Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.d("onadd", itemSeq);
                    }
                });


                /**
                 * 서버 : searchMed에서 알약 검색 후 밑에 테이블에 약 띄어주는데
                 * 그것을 클릭 시 MedicineDetailActivity가 호출되어 추가하기 버튼이 활성화 돼 약을 추가할 수 있다.
                 * 즉, MedicineFragment에 추가할 수 있다.
                 * 이 부분은 그 추가하는 서버 코드
                 */
            }
        });


        ic_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();

                if(time != null){
                    bottomSheetDialog.init(user_id,itemSeq, MEDICINE);
                }else{
                    bottomSheetDialog.init(user_id,itemSeq, listViewItemArrayList, TEMP_ALARM);
                }
                bottomSheetDialog.show(getSupportFragmentManager(),"bottomSheet");
            }
        });

    }

    /**
     * 약 상세 정보 각 View에 추가
     */
    void display_medicineDetailInform(String itemSeq){
        Call<MedicineInfoRsponDTO> call= RetrofitHelper.getRetrofitService_server().getMedicine(itemSeq);
        call.enqueue(new Callback<MedicineInfoRsponDTO>() {
            @Override
            public void onResponse(Call<MedicineInfoRsponDTO> call, Response<MedicineInfoRsponDTO> response) {
                MedicineInfoRsponDTO medicineInfoRsponDTO=response.body();
                medicineName.setText(medicineInfoRsponDTO.getItemName());
               //추가적으로 값을 넣어주어야한다.


                Log.d("zzvbb","끝");
            }

            @Override
            public void onFailure(Call<MedicineInfoRsponDTO> call, Throwable t) {
                Log.d("onfail", t.toString());
            }
        });

    }
}
