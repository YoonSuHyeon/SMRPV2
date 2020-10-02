package com.example.smrpv2.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.user.User;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;
import com.example.smrpv2.ui.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 회원가입 화면  : 아이디 중복검사 ,  회원가입 기능
 **/
public class SignUpActivity extends AppCompatActivity {
    Context context;
    EditText Txt_sua_id, Txt_sua_email, Txt_sua_password, Txt_sua_passwordCheck, Txt_sua_name, Txt_birth;
    Button Btn_duplicate, Btn_sua_signUp;
    RadioButton Rdb_man, Rdn_woman;
    boolean checkIdStatus = false; // 중복확인검사 상태확인을 위한 변수
    final private static String check_id = "^[a-zA-Z0-9]*$";
    final private static String check_email = "^[0-9a-zA-Z@\\.\\_\\-]+$";
    final private static String check_passWord = "^[a-z0-9_-]{6,18}$";
    final private static String check_birth = "\\d{6}";
    final private static String check_name ="^[가-힣]{2,4}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        initView(); //findViewById 초기화
        inputFilter();

        Btn_duplicate.setOnClickListener(new View.OnClickListener() {//중복확인 버튼 클릭시
            @Override
            public void onClick(View view) {
                checkId(Txt_sua_id.getText().toString()); //id 중복확인

            }
        });

        Btn_sua_signUp.setOnClickListener(new View.OnClickListener() {//회원가입 버튼을 클릭시..
            @Override
            public void onClick(View view) {
                if (!checkAllText()) { //처음으로 전부 모든 입력을 했는지 검사를한다.
                    show("모두 입력해 주세요");
                } else if (!checkIdStatus) {//중복 확인 버튼 상태 확인.
                    show("아이디 중복확인을 해주세요.");
                } else {//회원가입
                    signUp();
                }


            }
        });


    }

    /*
     * 아래부터는 사용된 메소드
     * */


    public void signUp() { //회원가입..

        //새로운 User 생성
        String id = Txt_sua_id.getText().toString();
        String email = Txt_sua_email.getText().toString();
        String passWord = Txt_sua_password.getText().toString();
        String name = Txt_sua_name.getText().toString();
        String gender = "WOMAN";
        if (Rdb_man.isChecked()) gender = "MAN";
        String birth = Txt_birth.getText().toString();
        User user = new User(id, email, passWord, name, gender, birth);

        //서버에게 user를 보내줌
        Call<Message> call = RetrofitHelper.getRetrofitService_server().join(user);
        call.enqueue(new Callback<Message>() {

            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Log.d("회원가입", response.toString());
                Log.d("회원가입이이", response.body().toString());
                show("회원가입 완료.");
                //회원가입 완료시 Login Activity 이동
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                show("오류입니다.");
                Log.d("Sign", t.toString());
            }
        });


    }

    public Boolean checkAllText() { //EditText 에 입력이 되어있는지 확인..

        //입력된 사용자 정보를 가져온다 .
        String id = Txt_sua_id.getText().toString();
        String email = Txt_sua_email.getText().toString();
        String passWord = Txt_sua_password.getText().toString();
        String passWordCheck = Txt_sua_passwordCheck.getText().toString();
        String name = Txt_sua_name.getText().toString();
        String gender = "WOMAN";
        if (Rdb_man.isChecked()) gender = "MAN";
        String birth = Txt_birth.getText().toString();

        //이메일 형식 검사
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(context, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
            return false;
        }
        //패스워드 형식 검사
        if(!passWord.matches(check_passWord)){
            Toast.makeText(getApplicationContext(),"안전하지 않은 패스워드입니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        //패스워드 일치하지 않은 경우
        if(!passWord.equals(passWordCheck)){
            Toast.makeText(getApplicationContext(),"패스워드가 다릅니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        //이름이 형식검사
       if(!name.matches(check_name)){
           Toast.makeText(getApplicationContext(),"이름을 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
           return false;
       }
        //생년월일이 숫자가 아닐시
       if(!birth.matches(check_birth)){
           if(birth.length()==6){
               Toast.makeText(getApplicationContext(),"생년월일은 숫자만 입력해주세요.",Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(getApplicationContext(),"생년월일 형식을 yymmdd로 입력해주세요.",Toast.LENGTH_SHORT).show();
           }
           return false;
       }

        return true;
    }


    private void checkId(String id) { //ID 중복확인

        Call<Message> call = RetrofitHelper.getRetrofitService_server().findId(id);
        call.enqueue(new Callback<Message>() {

            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.d("gg", response.toString());
                //중복검사
                String message = response.body().getResultCode();

                if (message.equals("FAIL")) {
                    show("사용할 수 있는 ID 입니다.");
                    checkIdStatus = true;
                } else {
                    show("이미 등록된 ID 입니다.");
                    checkIdStatus = false;
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("zzzzzz", t.toString());
            }
        });
    }

    public void show(String s) { //Toast 메시지 출력
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    private void initView() { //init
        Txt_sua_id = findViewById(R.id.Txt_sua_id);
        Txt_sua_email = findViewById(R.id.Txt_sua_email);
        Txt_sua_password = findViewById(R.id.Txt_sua_password);
        Txt_sua_passwordCheck = findViewById(R.id.Txt_sua_passwordCheck);
        Txt_sua_name = findViewById(R.id.Txt_sua_name);
        Txt_birth = findViewById(R.id.Txt_birth);
        Btn_duplicate = findViewById(R.id.Btn_duplicate);
        Btn_sua_signUp = findViewById(R.id.Btn_sua_signUp);
        Rdb_man = findViewById(R.id.Rdb_man);
        Rdn_woman = findViewById(R.id.Rdn_woman);

    }

    /**
     * 입력 제한
     */
    private void inputFilter(){
        InputFilter id_inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.toString().matches(check_id)){
                    return source;
                }else
                    return "";
            }
        };
        InputFilter email_inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.toString().matches(check_email)){
                    return source;
                }else
                    return "";
            }
        };
        InputFilter[] id_filter = new InputFilter[]{id_inputFilter};
        InputFilter[] email_filter = new InputFilter[]{email_inputFilter};
        Txt_sua_id.setFilters(id_filter);
        Txt_sua_email.setFilters(email_filter);
    }
}