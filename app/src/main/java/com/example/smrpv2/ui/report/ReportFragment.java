package com.example.smrpv2.ui.report;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.DiseaseItem;

import java.util.ArrayList;

/**
 * ReportFragmnet : 자신에게 발생하는 증상을 선택한다.
 * 제출버튼을 누르면 ReportResultActivity 호출하여 진단 결과를 알려줌
 */
public class ReportFragment extends Fragment implements ReportRecyclerAdapter.OnItemClickListener {


    ReportRecyclerAdapter adapter;
    RecyclerView Lst_symptom;
    Button Btn_submit;
    View root;
    String symptom_array[];
    String symptom_content_array[];
    final int SELECTED_ITEM=1;
    final int NON_SELECTED_ITEM=0;
    int total_selectedItem=0;
    ArrayList<DiseaseItem> symptom_list = new ArrayList<DiseaseItem>();
    private ArrayList<Integer> mSelectedSymptomItems = new ArrayList<Integer>();

    public View onCreateView(@NonNull LayoutInflater inflater,
    ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_report, container, false);

        Lst_symptom = root.findViewById(R.id.Lst_symptom);
        Btn_submit = root.findViewById(R.id.Btn_submit);
        symptom_array =  getResources().getStringArray(R.array.symptom_array);
        symptom_content_array = getResources().getStringArray(R.array.symptom_content_array);
        adapter = new ReportRecyclerAdapter(symptom_list, this,Lst_symptom);

        for(int i=0; i < symptom_array.length; i++)
            addItem(symptom_list, symptom_array[i],symptom_content_array[i],0);
        for(int i=0; i < symptom_list.size(); i++)
            mSelectedSymptomItems.add(i,0);


        Lst_symptom.setAdapter(adapter);

        Btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(total_selectedItem ==0 ){
                    Toast.makeText(getContext(),"한 개이상 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getActivity(), ReportResultActivity.class);
                    intent.putExtra("selected", mSelectedSymptomItems);
                    startActivity(intent);

                }
            }

        });


        return root;
    }
    public void addItem(ArrayList<DiseaseItem> list, String s, String sc, int viewType) {
        DiseaseItem item = new DiseaseItem();
        item.setSymptom(s);
        item.setSymptomContent(sc);
        item.setViewType(viewType);
        list.add(item);
    }

    @Override
    public void onItemClick(View v, int position, RecyclerView rList) {
        DiseaseItem item;
        TextView Txt_sym=  v.findViewById(R.id.Txt_symptom);
        if (mSelectedSymptomItems.get(position)==SELECTED_ITEM){
            mSelectedSymptomItems.set(position,NON_SELECTED_ITEM);
            total_selectedItem--;
        }
        else {
            mSelectedSymptomItems.set(position,SELECTED_ITEM);
            total_selectedItem++;

        }
        
    }



}