package com.example.smrpv2.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.smrpv2.R;
import com.example.smrpv2.model.user_model.User;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.ui.common.SharedData;
import com.example.smrpv2.ui.home.HomeFragment;
import com.example.smrpv2.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    String name;
    Toolbar toolbar;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private Dialog enddialog;
    private TextView headr_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar= findViewById(R.id.appbar);
        toolbar.setTitle(R.string.app_bar_title);
        setSupportActionBar(toolbar);
        SharedData sharedData = new SharedData(this);
        boolean auto_state = sharedData.isAuto_login();
        if(auto_state)
            Toast.makeText(getApplicationContext(),"자동로그인이 되었습니다.",Toast.LENGTH_LONG).show();


        //name = getIntent().getStringExtra("name");
        name = UserInform.getName();
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view); //navigationView을 사용하기 위한 객체 선언
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);

        headr_textView = header.findViewById(R.id.tv_header);//
        headr_textView.setText(name);
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
    public void onBackPressed(){
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout_btn:
                logout_dialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout_dialog(){
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        alertdialog.setCancelable(false);//외부영역 터치시 dismiss되는것을 방지
        final SharedData sharedData = new SharedData(this);
        alertdialog.setMessage("현재 계정을 종료하시겠습니까?");



        alertdialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sharedData.reset(); //로그아웃시 사용자의 자동로그인을 해제하기위해 reset 메소드 실행
                HomeFragment homeFragment = new HomeFragment().getInstance();
                if(homeFragment != null) //홈프래그먼트에 runOnUiThread를 멈추기 위해서 해당 if문 사용
                    homeFragment.isRunning = false;
                new MainActivity.Dialog().execute();

               // User user = new User(user_id,"",user_pass,"","",""); //서버에서 USER 클래스를 받기에 불필요한 매개변수가 들어가도 이해할것
                /**
                 * 서버
                 * 알람에 관한것
                 */

            }
        });

        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertdialog.create();
        alert.show();
    }
    private class Dialog extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로그아웃 중입니다.");
            progressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2500); // 2초 지속

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, LoginActivity.class); //로그인페이지로 이동
            startActivity(intent);
            finish();
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
