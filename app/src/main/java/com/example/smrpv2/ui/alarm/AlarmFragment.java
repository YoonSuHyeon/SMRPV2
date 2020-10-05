package com.example.smrpv2.ui.alarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smrpv2.R;
import com.example.smrpv2.model.alarm_model.AlarmItem;
import com.example.smrpv2.ui.start.AutoSlide;
import com.example.smrpv2.ui.start.ViewPagerAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * 알람 리스트 출력
 * 기능 1. 알람 추가(+버튼)
 * 기능 2. 알람 편집(알람 리스트 중 하나 클릭)
 */
public class AlarmFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private AlarmListViewAdapter listViewAdapter;

    private ListView Lst_medicine;
    private TextView Txt_empty;
    private ImageView Img_ic_plus;

    AutoSlide autoSlide;
    private final long DELAY_MS = 1000;
    private final long PERIOD_MS = 3000;
    private String user_id;
    ArrayList<AlarmItem> items = new ArrayList<AlarmItem>();
    private int[] ad_banner= {R.drawable.ad_banner1, R.drawable.ad_banner2,R.drawable.ad_banner3};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(container.getChildCount() > 0)
            container.removeViewAt(0);
        final android.view.View v = inflater.inflate(R.layout.alarm_fragment, container, false);


        //초기화.....
        Lst_medicine = v.findViewById(R.id.Lst_medicine);
        Txt_empty = v.findViewById(R.id.Txt_empty);
        Lst_medicine.setEmptyView(Txt_empty);
        Img_ic_plus = v.findViewById(R.id.Img_ic_plus);
        viewPager =  v.findViewById(R.id.banner);
        adapter = new ViewPagerAdapter(getActivity(),ad_banner);
        CircleIndicator indicator = v.findViewById(R.id.indicator); // 인디케이터
        listViewAdapter= new AlarmListViewAdapter(items,getActivity());


        //view 설정...
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        Lst_medicine.setAdapter(listViewAdapter);


        SharedPreferences loginInfromation = getActivity().getSharedPreferences("setting",0);
        user_id = loginInfromation.getString("id","");


        autoSlide = new AutoSlide(viewPager, DELAY_MS, PERIOD_MS);
        autoSlide.startSlide();

        Img_ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), AlarmSetActivity.class);
                startActivity(intent);


            }
        });

        items.add(new AlarmItem("hghg",
                "2020-11-11",
                "2020-11-12",
                "3",
                (long)123,
                "식전",
                "4"
        ));
        listViewAdapter.notifyDataSetChanged();


        return v;

    }
    public void onStart() {
        super.onStart();

        /**
         *
         *  서버 내용. 연결이 안돼서 임시로 비움. 나중에 연결 후 추가예정
         *
         *
         */
    }
}
