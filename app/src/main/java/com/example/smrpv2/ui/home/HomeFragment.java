package com.example.smrpv2.ui.home;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;
import com.example.smrpv2.R;
import com.example.smrpv2.model.home_model.Covid19_item;
import com.example.smrpv2.model.home_model.Covid19_response;
import com.example.smrpv2.model.home_model.HomeMedItem;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;
import com.example.smrpv2.ui.common.LocationValue;
import com.example.smrpv2.ui.search.SearchActivity;
import com.example.smrpv2.ui.start.AutoSlide;
import com.example.smrpv2.ui.start.ViewPagerAdapter;
import com.google.common.net.InternetDomainName;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

   // private RecyclerView recyclerView;
    ImageView ic_med_search;
    ImageView ic_prescription_register;
    ImageView ic_envelope_register;
    ImageView ic_pharmacy_search;
    ImageView ic_hospital_search;
    ImageView ic_register_record;
    ImageView ic_dose_record;
    ImageView ic_alarm_set;

    TextView covidresult1,covidresult2,covidresult3,covidresult4; //금일 코로나 현황 수를 보여주는 텍스트뷰
    TextView gapresult1,gapresult2,gapresult3,gapresult4; //전날 금일 현황 수의 차이를 보여주는 텍스트뷰


    AutoSlide autoSlide;
    private Bitmap bitmap;
    private final long DELAY_MS = 1000; // 자동 슬라이드를 위한 변수
    private final long PERIOD_MS = 3000; // 자동 슬라이드를 위한 변수
    private double latitude, longitude;
    private int[] images= {R.drawable.home_main_banner3, R.drawable.home_main_banner1,R.drawable.home_main_banner2};
    private int[] bannerImages ={R.drawable.home_small_banner1, R.drawable.home_small_banner2,R.drawable.home_small_banner3};
    public boolean isRunning = true;
    HashMap<String,String> sky_image;
    private ArrayList<HomeMedItem> homeMedItemArrayList=new ArrayList<HomeMedItem>();
    private RetrofitService_Server json;
    private HomeFragment homeFragment;
    private View root;
    private String beforeeDate, todayDate;
    private LocationValue locationValue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(container.getChildCount() > 0)
            container.removeViewAt(0);
        root = inflater.inflate(R.layout.fragment_home, container, false);


        if(navHostFragment==null) {
            navHostFragment =
                    (NavHostFragment) ((AppCompatActivity) getContext()).getSupportFragmentManager()
                            .findFragmentById(R.id.nav_host_fragment);
        }
        navController = navHostFragment.getNavController();


        init();

        // 첫 번째 배너 사이 간격 조정
        resizeBannerPadding();

        // 미리 위치데이터를 받는다.
        locationValue = new LocationValue(getActivity());
        locationValue.startMoule();
        // 동적으로 배너 크기 바꾸기
        LinearLayout viewP = root.findViewById(R.id.viewP);
        LinearLayout viewP2 = root.findViewById(R.id.vP);
        resizeBannerSize(viewP, 3);
        resizeBannerSize(viewP2,4);

        autoSlide = new AutoSlide(smallViewPager, DELAY_MS, PERIOD_MS);
        autoSlide.startSlide();

        /*LocationValue location = new LocationValue(getActivity());
        location.startMoule();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();*/
        show_Covid();


        //current_time(); // 시간표시
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



    void show_Covid(){ //코로나 확진자 데이터 가져오기
        RetrofitService_Server parsing = RetrofitHelper.getCovid().create(RetrofitService_Server.class);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.d("show_Covid", "year: "+year);
        Log.d("show_Covid", "month: "+month);
        Log.d("show_Covid", "day: "+day);
        String str_year = covert_int(year);
        String str_month = covert_int(month);
        String str_day = covert_int(day);

        int temp_day = day -2; //이틀전이 전월인지 현재 월이랑 같은지 판별


        todayDate = str_year+str_month+str_day;//오늘 년월일
        Log.d("show_Covid", "todayDate = str_year: "+todayDate);
        
        if(temp_day<=0){//이틀전에 저번달일시
            Calendar before_monthCalendar = Calendar.getInstance(); //이틀전의 월를 설정하기 위함
            int mon = month -1; //저번달
            before_monthCalendar.set(Calendar.YEAR,mon,1);//저번달을 기준으로 설정
            int mon_maxDay = before_monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int before_day = mon_maxDay + temp_day; //이틀전의 날짜
            beforeeDate = covert_int(before_monthCalendar.get(Calendar.MONTH)+1) + covert_int(mon_maxDay)+covert_int(before_day);
        }else{
            beforeeDate = str_year+str_month+covert_int(temp_day);
        }
        Log.d("show_Covid", "beforeeDate: "+beforeeDate);

        final Call<Covid19_response> call = parsing.getCovid(beforeeDate,todayDate); //2일전부터 ~ 지금 날짜 까지 데이터 가져오기
        new Thread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<Covid19_response>() {
                    @Override
                    public void onResponse(Call<Covid19_response> call, Response<Covid19_response> response) { //리스트 0~n-1(오늘부터 과거순으로)
                        Log.d("TAG", "onResponse: "+response.body().getBody().toString());
                        Log.d("TAG", "onResponse: "+response.body().getBody().getItems().getItemsList().size());
                        //Log.d("TAG", "onResponse: "+response.body().getBody().getItems().getItemsList().get(0).getCreateDt());//
                        Log.d("TAG", "onResponse: "+response.body().getBody().getItems().getItemsList().get(1).getCreateDt());
                        int size = response.body().getBody().getItems().getItemsList().size();

                        if(size>=2){
                            Covid19_item item1 = response.body().getBody().getItems().getItemsList().get(0);
                            Covid19_item item2 = response.body().getBody().getItems().getItemsList().get(1);
                            showCovidType1(item1);//오늘 현황
                            showCovidType2(item1,item2); //어제 현황과 오늘 현황 변동
                        }else{
                            Covid19_item item1 = response.body().getBody().getItems().getItemsList().get(0);
                            showCovidType1(item1);
                        }
                    }

                    @Override
                    public void onFailure(Call<Covid19_response> call, Throwable t) {

                    }
                });
            }
        }).start();
    }
    String covert_int(int num){
       String temp = String.valueOf(num);
       if(temp.length() == 1)
           temp = "0"+temp;
       return temp;
    }
    public static String toNumFormat(int num) { //숫자 콤마 생성
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }
    void showCovidType1(Covid19_item item){
        /**
         * 금일 코로나 현황을 보여줌
         * */
        covidresult1.setText(toNumFormat(convertInteger(item.getDecideCnt()))); //확진환자 수
        covidresult2.setText(toNumFormat(convertInteger(item.getExamCnt()))); // 누적 검사수
        covidresult3.setText(toNumFormat(convertInteger(item.getClearCnt()))); // 격리 해제수
        covidresult4.setText(toNumFormat(convertInteger(item.getDeathCnt()))); // 사망자 수
    }
    void showCovidType2(Covid19_item today_item,Covid19_item yesterday_item) {
        /**
         * 격차를 보여줌
         * */
        int decideCnt = convertInteger(today_item.getDecideCnt()) - convertInteger(yesterday_item.getDecideCnt()); //전날과 오늘날의 누적 확진환자 수 차이
        int examCnt = convertInteger(today_item.getExamCnt()) - convertInteger(yesterday_item.getExamCnt()); //전날과 오늘날의 누적 검사수 차이
        int clearCnt = convertInteger(today_item.getClearCnt()) - convertInteger(yesterday_item.getClearCnt()); //전날과 오늘날의 격리 해제수 차이
        int deathCnt = convertInteger(today_item.getDeathCnt()) - convertInteger(yesterday_item.getDeathCnt()); //전날과 오늘날의 사망자 수 차이


        showGapTextview(gapresult1, decideCnt);//변동된 확진환자 수를 보여주기 위함
        showGapTextview(gapresult2, examCnt);//변동된 누적검사 수를 보여주기 위함
        showGapTextview(gapresult3, clearCnt);//변동된 격리해제 수를 보여주기 위함
        showGapTextview(gapresult4, deathCnt);//변동된 사망자 수를 보여주기 위함
    }
    int convertInteger(String num){
       return Integer.parseInt(num);
    }
    void showGapTextview(TextView textView , int result){
        Log.d("TAG", "showGapTextview: "+result);
        if(result>=0){
            textView.setText(toNumFormat(result)+"▲");
        }else if(result<0){
            int abs_num = Math.abs(result);
            textView.setText(toNumFormat(abs_num)+"▼");
        }
    }
    /*void current_time(){//현재 시간을 표시하는 메소드

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("YYYY년 MM월 dd일");
                    SimpleDateFormat format2 = new SimpleDateFormat("HH시 mm분");
                    final String formatDate = format.format(date);
                    final String formatDate2 = format2.format(date);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            clock_textView.setText(formatDate);
                            clock_textView2.setText(formatDate2);
                            clock_textView.setTextSize(14);
                            clock_textView2.setTextSize(20);
                        }
                    });
                }
            }
        }).start();
    }*/


    /*private class Url_Connection extends AsyncTask<String,Void,String>{
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
            //weather_imageview.setImageBitmap(bitmap);

        }
    }*/

    public HomeFragment getInstance(){
        return homeFragment;
    }

    public void init(){
        /* 초기화 작업.... */
        //서버 통신 영역
        json = RetrofitHelper.getWeather().create(RetrofitService_Server.class);
        /*//Clock영역
        clock_textView = root.findViewById(R.id.colockTextview);
        clock_textView2 = root.findViewById(R.id.colockTextview2);*/



        //금일 코로나 결과 텍스트뷰
        covidresult1 = root.findViewById(R.id.covidresult_TextView1);
        covidresult2 = root.findViewById(R.id.covidresult_TextView2);
        covidresult3 = root.findViewById(R.id.covidresult_TextView3);
        covidresult4 = root.findViewById(R.id.covidresult_TextView4);

        //전날과 금일 코로나 변동 결과 텍스트뷰
        gapresult1 = root.findViewById(R.id.gap_TextView1);
        gapresult2 = root.findViewById(R.id.gap_TextView2);
        gapresult3 = root.findViewById(R.id.gap_TextView3);
        gapresult4 = root.findViewById(R.id.gap_TextView4);



        // 각 종 viewPager(배너), adapter, 초기화
        mainViewPager =  root.findViewById(R.id.banner);
        smallViewPager =  root.findViewById(R.id.banner2);
        adapter = new ViewPagerAdapter(getActivity(),images,1);
        bannerAdapter =  new ViewPagerAdapter(getActivity(),bannerImages);
        CircleIndicator indicator = root.findViewById(R.id.indicator_home); // 인디케이터
        CircleIndicator indicator2 = root.findViewById(R.id.indicator_home2); // 인디케이터


        // 하단 이미지 버튼
        ic_med_search = root.findViewById(R.id.ic_med_search);
        ic_prescription_register = root.findViewById(R.id.ic_prescription_register);
        ic_envelope_register = root.findViewById(R.id.ic_envelope_register);
        ic_pharmacy_search = root.findViewById(R.id.ic_pharmacy_search);
        ic_hospital_search = root.findViewById(R.id.ic_hospital_search);
        ic_register_record = root.findViewById(R.id.ic_register_record);
        ic_dose_record = root.findViewById(R.id.ic_dose_record);
        ic_alarm_set = root.findViewById(R.id.ic_alarm_set);

        homeFragment = this;

        /* view 설정 */

        mainViewPager.setAdapter(adapter);
        smallViewPager.setAdapter(bannerAdapter);
        mainViewPager.setCurrentItem(1);
        smallViewPager.setCurrentItem(0);
        indicator.setViewPager(mainViewPager);
        indicator2.setViewPager(smallViewPager);
    }
    @Override
    public void onDestroy() {
        try {
            Thread.sleep(500); //시간 스레드 동작 false로 설정후 적용하기 위한 0.5초 스레드 대기 지연
            isRunning = false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

}

