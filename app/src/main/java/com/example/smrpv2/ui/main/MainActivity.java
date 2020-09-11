package com.example.smrpv2.ui.main;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.smrpv2.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String name;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.appbar);
        toolbar.setTitle(R.string.app_bar_title);
        setSupportActionBar(toolbar);




        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //iv_headerImage.setImage();

        name = getIntent().getStringExtra("name");

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // xml 파일에서 넣어놨던 header 선언
        View header = navigationView.getHeaderView(0);
        // header에 있는 리소스 가져오기
        TextView text = (TextView) header.findViewById(R.id.tv_header);
        TextView text2 = (TextView) header.findViewById(R.id.tv_header2);
        TextView text3 = (TextView) header.findViewById(R.id.tv_header3);
        text.setText(name+"");
        //text.setText(name + "님 \n\r 환영합니다.");

        ImageView imageView =(ImageView) header.findViewById(R.id.iv_headerImage);
        //imageView.setImageDrawable(getDrawable(R.drawable.home_navigationbar_user_image));
        // imageView.setImageDrawable(getDrawable(R.drawable.clear_sky1));


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_alarm, R.id.nav_hospital,
                R.id.nav_inquiry, R.id.nav_logout,R.id.nav_medicine,R.id.nav_pharmacy,R.id.nav_report_notice)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onBackPressed(){ //네비게이션 드로어를 클릭하여 활성화 한 후 물리적으로 뒤로가기 버튼을 했을때 드로어 창이 닫힘
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 버튼을 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }




    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

}
