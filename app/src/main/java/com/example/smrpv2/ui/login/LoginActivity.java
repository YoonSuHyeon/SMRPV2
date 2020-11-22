package com.example.smrpv2.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.user_model.LoginUser;
import com.example.smrpv2.model.user_model.UserDto;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.ui.common.SharedData;
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

    boolean bool_store_login = false; //자동로그인 유무
    boolean bool_store_id = false; //아이디 저장 유무
    SharedPreferences loginInformation, storeIdInformation; //자동로그인 및 아이디 저장 시 필요
    SharedPreferences.Editor autoLogin_editor, storeId_editor;//자동로그인 및 아이디 저장 시 필요
    String user_id="",user_pass="";//자동로그인 및 아이디 저장 시 필요
    boolean getAutoLogin= false, getStoreId =false;//자동로그인 및 아이디 저장 시 필요
    String name="";
    SharedData sharedData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //초기화 작업
        context=this;
        initView();
        storeId(); //아이디 저장 기능이 설정되어있는지 확인
       // autoLogin(); //자동로그인 기능이 설정되어있는지 확인


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
               /* Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);*/

                checkAutoAndStore(); // 이 코드는 임시.. (테스트용) 나중에 서버랑 합쳐야함..
                if(user_id.equals("") || user_pass.equals("")){
                    if(user_id.equals("") && user_pass.equals(""))
                        show("아이디와 패스워드를 이용하세요.");
                    else if(user_id.equals(""))
                        show("아이디를 입력하세요.");
                    else
                        show("비밀번호를 입력하세요.");
                }
                else
                    login(user_id,user_pass);
            }
        });




    }

    private void login(String id, String passwd){

        LoginUser loginUser = new LoginUser(id,passwd);

        //로그인 시도
        Call<UserDto> call=RetrofitHelper.getRetrofitService_server().login(loginUser);
        call.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {

                if(response.body().getUserId() != null){ //로그인 성공

                    checkAutoAndStore();
                    //MainActivity로 화면 이동
                    UserInform user = new UserInform(response.body().getUserId(),response.body().getEmail(),response.body().getName(),
                            response.body().getGender(),response.body().getBirth(),response.body().getCreatedAt());
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    //intent.putExtra("name",response.body().getName());
                    startActivity(intent);
                    finish();
                }else{ //로그인 실패
                    show("아이디 및 비밀번호를 확인하고 다시 입력하세요.");
                }

            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                show("서버 통신 오류");
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
    private void storeId(){//아이디 저장 기능이 설정되어있는지 확인하고 설정
        allocteSharedData(); //sharedData 할당
        bool_store_id = sharedData.isStroe_id(); //파일에 저장되어있는 아이디 저장 기능 설정 값을 읽어옴
        if(bool_store_id){ //아이디 저장 기능이 설정이 되어있을경우
            String user_id = sharedData.getUser_id();
            Txt_id.setText(user_id);
            Chk_storeId.setChecked(true);
        }
    }

    /**
     * 자동 로그인 코드
     */
    private void autoLogin(){//자동 로그인이 설정되어있는지 확인
        allocteSharedData(); //sharedData 할당

        getAutoLogin = sharedData.isAuto_login(); //파일에 자동로그인 설정이 되어있는지 확인

        if(getAutoLogin){
            String user_id = sharedData.getUser_id(); //사용자 id값을 저장된 파일에서 가져온다
            String user_pass = sharedData.getUser_password();//사용자 password값을 저장된 파일에서 가져온다

            //name = loginInformation.getString("name","");
            Txt_id.setText(user_id);
            Txt_password.setText(user_pass);
            Chk_autoLogin.setChecked(true);
            Btn_login.setClickable(false);

            login(user_id,user_pass);//로그인 시도

            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();*/
        }

    }

    /**
     * 이전에 자동로그인 or 아이디 저장 체크박스를 클릭했던 경우
     * (나중에 일부 코드는 서버로 합칠쳐햠)
     * 임시 테스트코드
     */
    private void checkAutoAndStore(){

        //입력된 아이디 비밀번호 가져오기
        user_id =  Txt_id.getText().toString(); //TextView 필드에 입력한 아이디 값을 가져온다
        user_pass =  Txt_password.getText().toString(); //TextView필드에 입력한 비밀번호 값을 가져온다.
        if (bool_store_login) //자동 로그인을 체크 하고 로그인 버튼을 누를시
            sharedData.setUserAuto(this,user_id,user_pass,bool_store_login);


        if(bool_store_id) //아이디저장 기능이 활성화 된경우
            sharedData.setUserId(this, user_id, bool_store_id); //아이디 저장 기능을 사용하고 사용자 id값을 저장


    }

    private void show(String msg){
        
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void allocteSharedData(){
        if(sharedData == null)
            sharedData = new SharedData(this);
    }
    //사용자가 editText를 클릭한 시점에서 배경부븐을 선택하면 키보드를 자동으로 내려가지도록 설정
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}