package com.example.smrpv2.ui.medicine.medshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineDeepModelAskDto;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.medicine_model.Prescriptionitem;
import com.example.smrpv2.model.prescription_model.RegMedicineList;
import com.example.smrpv2.model.searchMed_model.MedicineInfoRsponDTO;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.common.ShareProgrees;
import com.example.smrpv2.ui.medicine.searchMed.PrescriptionAdapter;
import com.example.smrpv2.ui.medicine.searchMed.Search_prescriptionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 약촬영 후 분석한 약모양, 약 문구를 사용자에게 보여주며
 * 사용자는 data값을 수정하고 서버에 해당 data 관련 정보를 받기위한 acitivty
 *
 * */
public class CameraResultActivity extends AppCompatActivity {
    private Button Btn_add;
    private ImageView iv_back;
    private ArrayList<Prescriptionitem> list; //구축 서버로 부터 받은 의약품 리스트
    private HashMap<Integer, String> select_pill_list; //사용자 선택한 약 정보를 담는 hashmap
    private ArrayList<String> itemseq_list;
    private PrescriptionAdapter prescriptionAdapter;
    private RecyclerView recyclerView;
    private boolean bool_end = false;
    private ShareProgrees progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        initView();

        Intent intent = getIntent();
        String frontText = intent.getStringExtra("frontText");
        String backText = intent.getStringExtra("backText");
        String color = intent.getStringExtra("color");
        String shape = intent.getStringExtra("shape");
        String frontDividing = intent.getStringExtra("frontDividing");
        String backDividing = intent.getStringExtra("backDividing");

        if(frontDividing.contains("문구") || frontDividing.contains("민무늬"))
            frontDividing = "";


        if(backDividing.contains("문구") || backDividing.contains("민무늬"))
            backDividing = "";

        Log.d("frontText", frontText);
        Log.d("backText", backText);
        Log.d("color", color);
        Log.d("shape", shape);
        Log.d("frontDividing", frontText);
        Log.d("backDividing", backDividing);

        progressDialog.show();



        sendData(frontText,backText,color,shape,frontDividing,backDividing);//서버에게 카메라를통해 얻응 정보를 보내서 약을 가져오는


        Btn_add.setOnClickListener(new View.OnClickListener() { //추가하기 버튼 누를시
            @Override
            public void onClick(View v) { //추가하기
                if(select_pill_list.size()==0){ //선택한 약이 없을 경우
                    Toast.makeText(getApplicationContext(),"약을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    for(Map.Entry<Integer, String>elem : select_pill_list.entrySet())
                        itemseq_list.add(elem.getValue());
                    /**
                     *
                     * 서버 : 검색된 약 추가하기
                     */

                    RegMedicineList regMedicineList = new RegMedicineList(UserInform.getUserId(),itemseq_list);
                    Log.d("TAG", "itemseq_list.size: "+itemseq_list.size());
                    Call<Message> call = RetrofitHelper.getRetrofitService_server().medicineListAdd(regMedicineList);
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Log.d("onResponse", "onResponse: "+response.body().getResultCode());
                            if(response.body().getResultCode().equals("OK")){
                                //정상적으로 반영
                                Toast.makeText(getApplicationContext(), itemseq_list.size()+"건을 등록하였습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {

                        }
                    });
                }


            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });//뒤로가기

        prescriptionAdapter.setOnClickListener(new PrescriptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrescriptionAdapter.ViewHolder holder, View v, int position) { //리스트 이벤트 처리

                if(select_pill_list.size()==0){//사용자가 추가한 약 리스트가 없는 경우
                    select_pill_list.put(position,list.get(position).getItemSeq());

                }else{
                    if(select_pill_list.get(position)==null){ //선택한 약이 hashmap에 없을경우
                        select_pill_list.put(position,list.get(position).getItemSeq());
                    }else{
                        select_pill_list.remove(position);
                    }
                }

            }

        });
    }
    private void sendData(String frontText, String backText, String color, String shape, String frontDividing, String backDividing){//약을 검색하기위한 정보를 서버와 교환
        progressDialog.dismiss();

        MedicineDeepModelAskDto medicineDeepModelAskDto = new MedicineDeepModelAskDto(frontDividing, backDividing, color, frontText, backText, shape);

        Call<ArrayList<MedicineInfoRsponDTO>> call = RetrofitHelper.getRetrofitService_server().searchDeepMedicine(medicineDeepModelAskDto);
        call.enqueue(new Callback<ArrayList<MedicineInfoRsponDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<MedicineInfoRsponDTO>> call, Response<ArrayList<MedicineInfoRsponDTO>> response) {
                ArrayList<MedicineInfoRsponDTO> body = response.body();

                Log.d("성고잉에요", "성공");
               /* for (MedicineInfoRsponDTO info : body) {
                    Log.d("Infi", info.getItemName());
                }*/
                Log.d("사이즈", "dasda"+response.body().size());
                for(int i = 0 ; i < response.body().size();i++){
                    MedicineInfoRsponDTO dto = response.body().get(i);
                    list.add(new Prescriptionitem(dto.getItemSeq(),dto.getItemImage(),dto.getItemName(),dto.getEntpName(),dto.getEtcOtcName()));//약 식별번호 / 약 이미지 / 약 이름 / 약 제조사 / 약 포장 /약 의약품정보(일반, 전문)
                    prescriptionAdapter.notifyDataSetChanged();
                }
                LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mlinearLayoutManager.getOrientation());//구분선을 넣기 위함
                recyclerView.addItemDecoration(dividerItemDecoration);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ArrayList<MedicineInfoRsponDTO>> call, Throwable t) {
                Log.d("에러에요", t.toString());
            }
        });



        //서버에서 구현한후
        /*Call<ArrayList<MedicineInfoRsponDTO>> call = RetrofitHelper.getRetrofitService_server().sendWords(array);
        call.enqueue(new Callback<ArrayList<MedicineInfoRsponDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<MedicineInfoRsponDTO>> call, Response<ArrayList<MedicineInfoRsponDTO>> response) {

                for(int i = 0 ; i < response.body().size();i++){
                    MedicineInfoRsponDTO dto = response.body().get(i);
                    list.add(new Prescriptionitem(dto.getItemSeq(),dto.getItemImage(),dto.getItemName(),dto.getEntpName(),dto.getEtcOtcName()));//약 식별번호 / 약 이미지 / 약 이름 / 약 제조사 / 약 포장 /약 의약품정보(일반, 전문)
                    prescriptionAdapter.notifyDataSetChanged();
                }
                LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mlinearLayoutManager.getOrientation());//구분선을 넣기 위함
                recyclerView.addItemDecoration(dividerItemDecoration);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<MedicineInfoRsponDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"분석 결과가 없습니다. 이전 페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });*/
    }
    private class Dialog extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1 = new ProgressDialog(CameraResultActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.setMessage("로딩중입니다..");

            progressDialog1.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            while(!bool_end)
                ;
            bool_end = false;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog1.dismiss();
            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void initView(){
        Btn_add = findViewById(R.id.add_btn);
        iv_back = findViewById(R.id.iv_back);
        select_pill_list = new HashMap<Integer, String>();
        itemseq_list = new ArrayList<String>();
        list = new ArrayList<Prescriptionitem>();
        prescriptionAdapter = new PrescriptionAdapter(list);
        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(prescriptionAdapter);
        progressDialog = new ShareProgrees(this,"분석중입니다.\n잠시만 기다려 주세요.");
    }
}