package com.example.smrpv2.ui.medicine;

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

import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.ui.start.AutoSlide;
import com.example.smrpv2.ui.start.ViewPagerAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 *
 * MedicineFragment : 약 등록하기 화면
 *  +버튼을 눌러 뜨는 팝업창에서 약 사진 찍기 or 약봉투 및 처방전 사진 찍기 or 약 검색하기 중 하나를 선택해 각 기능을 수행한 후 약을 등록할 수 있다.
 *  등록된 약의 리스트를 보여준다.
 *  등록된 약의 리스트 중 하나를 클릭 시 상세 정보를 볼 수 있고, 그 창에서 알람을 설정할 수 있다.(MedicineDetailAcitivity 및 AlarmSetActivity)
 *
 */
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
    ArrayList<MedicineItem> items = new ArrayList<MedicineItem>();
    private int[] images= {R.drawable.ad_banner1, R.drawable.ad_banner2,R.drawable.ad_banner3}; // ViewPagerAdapter에  보낼 이미지. 이걸로 이미지 슬라이드 띄어줌

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_medicine, container, false);

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
        autoSlide.startSlide();
        Img_ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupFragment p = new PopupFragment();
                p.show(getActivity().getSupportFragmentManager(),"popup"); //팝업 창 띄우기

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
