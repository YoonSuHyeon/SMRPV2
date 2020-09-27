package com.example.smrpv2.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.user.LoginUser;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.findid.FindIdActivity;

import com.example.smrpv2.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * 로그인 액티비티
 */
public class LoginActivity extends AppCompatActivity {
    Context context; //this 객체
    TextView findId; //아이디 비밀 번호 찾기 TextView
    EditText Txt_id,Txt_password;
    Button Btn_login;
    CheckBox Chk_autoLogin,Chk_storeId;

    boolean bool_store_login = false;
    boolean bool_store_id = false;
    SharedPreferences loginInformation, storeIdInformation; //자동로그인 및 아이디 저장 시 필요
    SharedPreferences.Editor autoLogin_editor, storeId_editor;//자동로그인 및 아이디 저장 시 필요
    String user_id="",user_pass="",getAutoLogin="", getStoreId ="";//자동로그인 및 아이디 저장 시 필요
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //초기화 작업
        context=this;
        initView();
        storeId();
        autoLogin();


        //자동 로그인 체크박스...
        Chk_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_store_login = isChecked;
            }
        });
        //아이디 저장 체크박스...
        Chk_storeId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bool_store_id = isChecked;

            }
        });
        //아이디 비밀번호 찾기 클릭 시 ...
        findId.setOnClickListener(new View.OnClickListener() { // 아이디 / 비밀번호 찾기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FindIdActivity.class);
                startActivity(intent);

            }
        });
        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**임시로 Intent로 선언 테스트용**/
                checkAutoAndStore(); // 이 코드는 임시.. (테스트용) 나중에 서버랑 합쳐야함..
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                //login();
            }
        });




    }

    private void login(){

        LoginUser loginUser = new LoginUser(user_id,user_pass);

        //로그인 시도
        Call<Message> call=RetrofitHelper.getRetrofitService_server().login(loginUser);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Log.d("login",response.toString());
                Log.d("ddd", response.body().getResultCode());

                if(response.body().getResultCode().equals("PASS")){ //로그인 성공

                    //MainActivity로 화면 이동
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                }else{ //로그인 실패
                    Toast.makeText(context, "로그인 오류", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });


    }
    private void initView() { //init
        findId=findViewById(R.id.tv_findId);
        Btn_login = findViewById(R.id.Btn_Innerlogin);
        Txt_id=findViewById(R.id.Txt_id);
        Txt_password = findViewById(R.id.Txt_password);
        Chk_autoLogin = findViewById(R.id.auto_login);
        Chk_storeId = findViewById(R.id.store_id);

    }

    /**
     * 아이디 저장 코드
     */
    private void storeId(){
        storeIdInformation = getSharedPreferences("store_id",0);
        storeId_editor = storeIdInformation.edit();
        getStoreId = storeIdInformation.getString("store_id","");
        if(getStoreId.equals("true")){
            user_id = storeIdInformation.getString("id","");
            Txt_id.setText(user_id);
            Chk_storeId.setChecked(true);
        }
    }

    /**
     * 자동 로그인 코드
     */
    private void autoLogin(){
        loginInformation = getSharedPreferences("setting",0);
        autoLogin_editor = loginInformation.edit();
        getAutoLogin = loginInformation.getString("auto_login","");

        if(getAutoLogin.equals("true")){
            user_id = loginInformation.getString("id","");
            user_pass = loginInformation.getString("password","");
            name = loginInformation.getString("name","");
            Txt_id.setText(user_id);
            Txt_password.setText(user_pass);
            Chk_autoLogin.setChecked(true);
            Btn_login.setClickable(false);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();

        }

    }

    /**
     * 이전에 자동로그인 or 아이디 저장 체크박스를 클릭했던 경우
     * (나중에 일부 코드는 서버로 합칠쳐햠)
     * 임시 테스트코드
     */
    private void checkAutoAndStore(){

        //입력된 아이디 비밀번호 가져오기
        user_id =  Txt_id.getText().toString();
        user_pass =  Txt_password.getText().toString();
        if (bool_store_login) {//자동 로그인을 체크 하고 로그인 버튼을 누를시
            autoLogin_editor.putString("auto_login", "true");
            autoLogin_editor.putString("id", user_id); //자동로그인시 ID 값 입력
            autoLogin_editor.putString("password", user_pass); //자동로그인시 패스워드 값 입력
            autoLogin_editor.putString("name",name); //자동로그인시 패스워드 이름 입력
        }else{ //자동로그인을 하지 않은 상태에서 로그인시
            autoLogin_editor.putString("auto_login", "false");
        }

        if(bool_store_id){//아이디저장

            storeId_editor.putString("store_id","true");
            storeId_editor.putString("id",user_id);
        }else{
            storeId_editor.putString("store_id","false");
        }
        autoLogin_editor.putString("id", user_id); //자동로그인시 ID 값 입력
        autoLogin_editor.commit();
        storeId_editor.commit();

    }
}