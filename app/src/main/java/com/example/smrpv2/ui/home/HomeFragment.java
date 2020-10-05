package com.example.smrpv2.ui.home;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.smrpv2.R;
import com.example.smrpv2.model.home_model.HomeMedItem;
import com.example.smrpv2.retrofit.RetrofitService_home;
import com.example.smrpv2.ui.search.SearchActivity;
import com.example.smrpv2.ui.start.AutoSlide;
import com.example.smrpv2.ui.start.ViewPagerAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

/**
 * HomeFragment : 메인 화면
 * 서버가 연결이 안돼서 서버에 관한 코드는 일단 비어놓음 (나중 수정할 예정)
 */
public class HomeFragment extends Fragment {

    private ViewPager mainViewPager;
    private ViewPager smallViewPager;
    private ViewPagerAdapter adapter;
    private ViewPagerAdapter bannerAdapter;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    NavHostFragment navHostFragment;
    NavController navController;

    private RecyclerView recyclerView;
    ImageView ic_med_search;
    ImageView ic_prescription_register;
    ImageView ic_envelope_register;
    ImageView ic_pharmacy_search;
    ImageView ic_hospital_search;
    ImageView ic_register_record;
    ImageView ic_dose_record;
    ImageView ic_alarm_set;
    private static TextView humidity_textView,temp_textview,Txt_statement, Txt_weather;
    private static ImageView weather_imageview;


    AutoSlide autoSlide;
    private Bitmap bitmap;
    private final long DELAY_MS = 1000; // 자동 슬라이드를 위한 변수
    private final long PERIOD_MS = 3000; // 자동 슬라이드를 위한 변수
    private double latitude, longitude;
    private int[] images= {R.drawable.home_main_banner3, R.drawable.home_main_banner1,R.drawable.home_main_banner2};
    private int[] bannerImages ={R.drawable.home_small_banner1, R.drawable.home_small_banner2,R.drawable.home_small_banner3};
    final HashMap<String,String> sky_image = new HashMap<>();
    private ArrayList<HomeMedItem> homeMedItemArrayList=new ArrayList<HomeMedItem>();

    private static RetrofitService_home json;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(container.getChildCount() > 0)
            container.removeViewAt(0);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        /* 초기화 작업.... */
        //weather 영역
        weather_imageview = root.findViewById(R.id.weather_imageview); //하늘 상태 사진
        temp_textview = root.findViewById(R.id.temp_textview); //온도 textVie
        humidity_textView = root.findViewById(R.id.humidity_textView); //하늘상태
        Txt_statement = root.findViewById(R.id.Txt_statement);
        Txt_weather = root.findViewById(R.id.Txt_weather);

        // 각 종 viewPager(배너), adapter, 초기화
        mainViewPager =  root.findViewById(R.id.banner);
        smallViewPager =  root.findViewById(R.id.banner2);
        adapter = new ViewPagerAdapter(getActivity(),images,1);
        bannerAdapter =  new ViewPagerAdapter(getActivity(),bannerImages);
        CircleIndicator indicator = root.findViewById(R.id.indicator_home); // 인디케이터
        CircleIndicator indicator2 = root.findViewById(R.id.indicator_home2); // 인디케이터
        recyclerView=root.findViewById(R.id.Lst_line);//랭킹

        // 하단 이미지 버튼
        ic_med_search = root.findViewById(R.id.ic_med_search);
        ic_prescription_register = root.findViewById(R.id.ic_prescription_register);
        ic_envelope_register = root.findViewById(R.id.ic_envelope_register);
        ic_pharmacy_search = root.findViewById(R.id.ic_pharmacy_search);
        ic_hospital_search = root.findViewById(R.id.ic_hospital_search);
        ic_register_record = root.findViewById(R.id.ic_register_record);
        ic_dose_record = root.findViewById(R.id.ic_dose_record);
        ic_alarm_set = root.findViewById(R.id.ic_alarm_set);

        if(navHostFragment==null) {
            navHostFragment =
                    (NavHostFragment) ((AppCompatActivity) getContext()).getSupportFragmentManager()
                            .findFragmentById(R.id.nav_host_fragment);
        }
        navController = navHostFragment.getNavController();


        /* view 설정 */
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mainViewPager.setAdapter(adapter);
        smallViewPager.setAdapter(bannerAdapter);
        mainViewPager.setCurrentItem(1);
        smallViewPager.setCurrentItem(0);
        indicator.setViewPager(mainViewPager);
        indicator2.setViewPager(smallViewPager);

        // 첫 번째 배너 사이 간격 조정
        resizeBannerPadding();

        // 동적으로 배너 크기 바꾸기
        LinearLayout viewP = root.findViewById(R.id.viewP);
        LinearLayout viewP2 = root.findViewById(R.id.vP);
        resizeBannerSize(viewP, 3);
        resizeBannerSize(viewP2,4);


        input_weatherStyle(sky_image);
        autoSlide = new AutoSlide(smallViewPager, DELAY_MS, PERIOD_MS);
        autoSlide.startSlide();
        displayWeather();
        displayRank();

        //하단 이미지 버튼 이동
        ic_med_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        ic_prescription_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), Search_prescriptionActivity.class);
                //startActivity(intent);
            }
        });
        ic_envelope_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), Search_prescriptionActivity.class);
                //startActivity(intent);
            }
        });
        ic_alarm_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_nav_alarm);
            }
        });
        ic_pharmacy_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_nav_pharmacy);
            }
        });
        ic_hospital_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_nav_hospital);
            }
        });
        ic_register_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_nav_medicine);
            }
        });
        ic_dose_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_home_to_nav_inquiry);
            }
        });

        return root;

    }
    void resizeBannerPadding(){
        int dp = 40;
        float d = getResources().getDisplayMetrics().density;
        final int margin = (int) (dp * d);
        mainViewPager.setClipToPadding(false);
        mainViewPager.setPadding(margin, 0, margin, 0);
        mainViewPager.setPageMargin(margin/2);
    }
    void resizeBannerSize(LinearLayout lay_banner, int div){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams lay = (ViewGroup.LayoutParams) lay_banner.getLayoutParams();
        lay.height = displayMetrics.heightPixels/div-5 ;
        lay_banner.setLayoutParams(lay);
    }
    void input_weatherStyle( HashMap<String,String> sky_image){
        if(sky_image != null) {
            sky_image.put("01n", "clear_sky");
            sky_image.put("02n", "few_clouds");
            sky_image.put("03n", "scattered_clouds");
            sky_image.put("04n", "broken_clouds");
            sky_image.put("09n", "show_rain");
            sky_image.put("10n", "rain");
            sky_image.put("11n", "thunderstom");
            sky_image.put("13n", "snow");
            sky_image.put("50n", "mist");

            //아침,낮 일떄 return 받는 이미지가 d으로 끝남
            sky_image.put("01d", "clear_sky");
            sky_image.put("02d", "few_clouds");
            sky_image.put("03d", "scattered_clouds");
            sky_image.put("04d", "broken_clouds");
            sky_image.put("09d", "show_rain");
            sky_image.put("10d", "rain");
            sky_image.put("11d", "thunderstom");
            sky_image.put("13d", "snow");
            sky_image.put("50d", "mist");

        }

    }
    void displayWeather(){ // 날씨 표시 메소드

        /**
         *
         *  서버 연결이 안돼서 제대로 구할 수가 없음. 나중에 수정 예정이라 임시로 비어놓음
         *
         *
         */

    }
    void displayRank(){ // 랭크 표시 메소드
        /**
         *
         *  서버 연결이 안돼서 제대로 구할 수가 없음. 나중에 수정 예정이라 임시로 비어놓음
         *
         *
         */
    }

    private class Url_Connection extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... str_url) {
            try {
                URL url = new URL(str_url[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e1){
                e1.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result){
            weather_imageview.setImageBitmap(bitmap);

        }
    }


}

