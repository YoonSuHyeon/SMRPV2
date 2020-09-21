package com.example.smrpv2.ui.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smrpv2.R;
import com.example.smrpv2.model.DiseaseItem;
import java.util.ArrayList;

/**
 * ReportResultActivity : 이전 화면(ReportFragmnet)에서 선택한 증상에 대한 결과(질병 정보)를 알려준다.
 */

public class ReportResultActivity extends AppCompatActivity implements ReportRecyclerAdapter.OnItemClickListener {//implements ReportFragment.OnSymptomListener {

    TextView Txt_name;
    ReportRecyclerAdapter adapter;
    RecyclerView Lst_disease;
    ImageView iv_back;
    //sym~ : 증상에 대한 질병 목록
    int sym0[] = {22, 7, 34, 32, 31, 13, 24};int sym7[] = {20, 7, 49, 28, 45};int sym8[] = { 20, 29, 39, 51, 44, 10, 3, 12, 7, 49};int sym9[] = {23, 38, 8, 9, 26, 42};
    int sym1[] = {24};int sym10[] = {31, 12, 28};int sym11[] = {30, 36, 16};
    int sym2[] = {0};int sym12[] = {4, 37, 39,27, 51};int sym13[] = {15, 43, 19, 40, 27, 24};
    int sym3[] = {8,9};int sym14[] = {44, 20, 12, 31, 47,11,3};int sym15[] = {37, 3, 15};
    int sym4[] = { 12};int sym16[] = {34};int sym17[] = {44, 37, 33, 51, 18, 7, 49, 45, 22, 32, 13};
    int sym5[] = {37, 33, 20, 38, 44, 7, 28, 49, 3, 45};int sym18[] = {10, 46, 1, 41};int sym19[] = {44, 37, 18, 2, 10, 46};
    int sym6[] = {37, 44, 39, 51, 43, 12, 15, 16, 48, 41, 40, 19, 14};int sym20[] = {7, 15, 19};
    int sym21[] = {35};int sym22[] = {5, 4};int sym23[] = {21};int sym24[] = { 17, 5, 4};int sym25[] = {37, 10, 31};
    int sym26[] = {18, 10, 31, 40, 41};int sym27[] = {37, 18, 10, 40, 41};int sym28[] = {25, 29, 51, 44, 3, 49, 19};int sym29[] = {22, 32};int sym30[] = {17, 22};
    int sym31[] = {4, 20, 5, 51,19, 39, 33, 37, 27, 40, 15, 26};int sym32[] = {41};int sym33[] = {6};int sym34[] = {35, 17,29, 43, 25, 50, 37, 4, 5, 18, 6};

    int disease_array[][] = {sym0,sym1,sym2,sym3,sym4,sym5,sym6,sym7,sym8,sym9,sym10,sym11,sym12,sym13,sym14,sym15,sym16,sym17,sym18,sym19,sym20
          ,sym21,sym22,sym23,sym24,sym25,sym26,sym27,sym28,sym29,sym30,sym31,sym32,sym33,sym34};
    private int[] diagnosis_result = new int[51];

    private String Str_diseaseName[];
    private String disease_depart[];
    private String disease_contents[];
    ArrayList<DiseaseItem> list = new ArrayList<DiseaseItem>();
    private ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
    double symptom_count=0.0;
    final int SELECTED_ITEM=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_result);

        Txt_name = findViewById(R.id.Txt_name);
        Lst_disease = findViewById(R.id.Lst_disease);
        iv_back = findViewById(R.id.iv_back);
        disease_depart =getResources().getStringArray(R.array.disease_depart);
        disease_contents = getResources().getStringArray(R.array.disease_contents);
        Str_diseaseName =getResources().getStringArray(R.array.disease_array);
        Intent intent = getIntent();
        mSelectedItems = intent.getIntegerArrayListExtra("selected");


        for(int i = 0; i < mSelectedItems.size(); i++){
          if(mSelectedItems.get(i)==SELECTED_ITEM) {
              {
                  symptom_count++;
                  for(int j=0; j < disease_array[i].length; j++){
                      if(disease_array[i][j]==-1) break;
                      diagnosis_result[disease_array[i][j]]++;
                  }
              }
          }
        }

        for(int i=0; i<diagnosis_result.length; i++){
            if(diagnosis_result[i] != 0){ // 병에 대응하는 배열 요소 값이 0이 아닌 경우(증상에 해당하는 병인 경우)

                double prob = cal_probability(diagnosis_result[i],symptom_count);
                addItem(list,Str_diseaseName[i],disease_contents[i],prob,disease_depart[i],1);
            }
        }
        adapter = new ReportRecyclerAdapter(list, this,Lst_disease);
        Lst_disease.setAdapter(adapter);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void addItem(ArrayList<DiseaseItem> list , String dis, String sym, double prob, String depart, int viewType) {
        DiseaseItem item = new DiseaseItem();
        item.setDisease(dis);
        item.setSymptom(sym);
        item.setProbability(prob+"");
        item.setDepartment(depart);
        item.setViewType(viewType);
        list.add(item);
    }
    private double cal_probability(int result, double count){
        return  Math.round(((result/count))*100.0 * 10) / 10.0;

    }
    @Override
    public void onItemClick(View v, int position, RecyclerView rList) {
        String sym;
        String disease;
        String depart;

        sym = list.get(position).getSymptom(); // 클릭한 질병의 증상 정보
        disease =list.get(position).getDisease(); // 클릭한 질병의 이름
        depart = list.get(position).getDepartment(); // 클릭한 질병의 진료 과

        // 각 정보들을 상세정보 페이지에 전달해준다.
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("selected_sym",sym);
        intent.putExtra("selected_dis",disease);
        intent.putExtra("selected_depart",depart);
        startActivity(intent);


    }



}
