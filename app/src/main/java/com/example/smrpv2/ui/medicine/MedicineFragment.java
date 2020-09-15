package com.example.smrpv2.ui.medicine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.smrpv2.R;

import com.example.smrpv2.model.MedListViewItem;
import com.example.smrpv2.ui.start.AutoSlide;
import com.example.smrpv2.ui.start.ViewPagerAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class MedicineFragment extends Fragment {
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ListView Lst_medicine; // 등록한 약 목록(아직 구현x)
    private ListViewAdapter listViewAdapter;

    private TextView Txt_empty; // 등록한 약이 없을 시 text메세지로 알려줌
    private ImageView Img_ic_plus; // +아이콘

    private AutoSlide autoSlide;
    private final int MEDICINE_FRAGMENT = 1;
    private final long DELAY_MS = 1000; // 자동 슬라이드를 위한 변수
    private final long PERIOD_MS = 3000; // 자동 슬라이드를 위한 변수
    ArrayList<MedListViewItem> items = new ArrayList<MedListViewItem>();
    private int[] images= {R.drawable.ad_banner1, R.drawable.ad_banner2,R.drawable.ad_banner3}; // ViewPagerAdapter에  보낼 이미지. 이걸로 이미지 슬라이드 띄어줌

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        View v = inflater.inflate(R.layout.medicine_fragment, container, false);

        //초기화..
        viewPager =  v.findViewById(R.id.banner);
        adapter = new ViewPagerAdapter(getActivity(),images);
        CircleIndicator indicator = v.findViewById(R.id.indicator); // 인디케이터
        Lst_medicine = v.findViewById(R.id.Lst_medicine);
        Txt_empty = v.findViewById(R.id.Txt_empty);
        Img_ic_plus = v.findViewById(R.id.Img_ic_plus);
        listViewAdapter=new ListViewAdapter(items,getActivity(),MEDICINE_FRAGMENT);

        // view 설정..
        Lst_medicine.setEmptyView(Txt_empty);
        indicator.setViewPager(viewPager); // 인디케이터 뷰에 추가
        viewPager.setAdapter(adapter);
        Lst_medicine.setAdapter(listViewAdapter);

        autoSlide = new AutoSlide(viewPager,DELAY_MS,PERIOD_MS);

        Img_ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopupFragment p = new PopupFragment(); // DialogFragment(약촬영, 약봉투, 처방전 팝업창을 위한 프레그먼트)
               // p.show(getActivity().getSupportFragmentManager(),"popup"); //팝업 창 띄우기

            }
        });

        /*SharedPreferences loginInfromation = getActivity().getSharedPreferences("setting",0);
        user_id = loginInfromation.getString("id","");*/

        return v;
    }
    public void onStart() {
        super.onStart();
        /**
         *
         *
         * 서버서버
         *
         *
         */

    }


}
