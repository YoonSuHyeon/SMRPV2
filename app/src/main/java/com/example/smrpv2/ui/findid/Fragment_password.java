package com.example.smrpv2.ui.findid;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_password extends Fragment {
    FindIdActivity findIdActivity;
    private String name="",id="",email="",message_code="";
    private EditText et_pass_name,et_pass_id,et_pass_email,et_verification_code,et_change_password1,et_change_password2;
    private Button btn_identifycode,btn_indentify,btn_chagepassword;
    private TextView time_textview, inform_textview;
    private LinearLayout linearLayout,password_linearlayout,basic_linearLayout;
    String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$";





    private AlertDialog.Builder dialog;
    private CountDownTimer countDownTimer;

    final int MILLISINFUTURE = 180 * 1000; //총 시간 (180초 = 3분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        findIdActivity=(FindIdActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        findIdActivity=null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup)inflater.inflate(R.layout.find_password,container,false);
        
        init(rootview); //xml 객체 초기화

        Change_Password change_password = findIdActivity.getChang_Password();

        id = change_password.getUserId();
        Log.d("id", "ididid: "+id);
        if(id.length()>0){
            Log.d("TAG", "change_password.getName(): "+change_password.getName());
            et_pass_name.setText(change_password.getName());
            et_pass_id.setText(id);
            Log.d("TAG", "change_password.getEmail(): "+change_password.getEmail());
            et_pass_email.setText(change_password.getEmail());
        }

        // 유효성 검사  이메일 인증하기 하기전에 기본입력값을 입력했는지
        /*btn_indentify.setOnClickListener(new View.OnClickListener() {//이름, 아이디, 이메
            @Override
            public void onClick(View v) {
                inform_textview.setVisibility(View.VISIBLE);
                inform_textview.setTextColor(Color.RED);
                inform_textview.setText("인증 메시지를 전송하였습니다. \n\r 1~3분 지연시간이 있을 수 있습니다.");
                name= et_pass_name.getText().toString(); //이름
                id = et_pass_id.getText().toString(); //사용자 id
                email = et_pass_email.getText().toString();// 사용자 이메일
                RetrofitService_Server networkService= RetrofitHelper.getRetrofitService_server();
                
                /**
                 * findPassword 코드는 현재 구현되어있지 않음 이부분
                 * */
             /*   Call<Message> call = networkService.findId(name);

                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {*/
                        /*Log.d("TAG", "onResponse: "+response.body().getResponse());
                        message_code = response.body().getResponse();
                        if(!message_code.equals("404")&&message_code.length()>0){ //정상적인 계정을 입력할때
                            linearLayout.setVisibility(View.VISIBLE); //인증코드 구역 활성화
                            btn_identifycode.setVisibility(View.VISIBLE); //인증코드 확인 버튼 활성화
                            if(countDownTimer!=null)
                                countDownTimer.cancel();

                            countDownTimer();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),"계정을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<response> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(),"통신이 불안정합니다.", Toast.LENGTH_SHORT).show();
                        inform_textview.setVisibility(View.GONE);
                    }
                });
            }
        });*/

        btn_identifycode.setOnClickListener(new View.OnClickListener() { //인증코드 확인하기
            public void onClick(View v) {
                String inputcode = String.valueOf(et_verification_code.getText());
                Log.d("TAG", "message_code: "+message_code);
                Log.d("TAG", "inputcode: "+inputcode);
                if(message_code.equals(inputcode)){
                    basic_linearLayout.setVisibility(View.GONE); //기본정보 영역 비활성화
                    linearLayout.setVisibility(View.GONE);//인증 영역 비활성화

                    password_linearlayout.setVisibility(View.VISIBLE); //비밀번호 변경영역 활성화
                    btn_chagepassword.setVisibility(View.VISIBLE); // 비밀번호 변경 버튼 활성화
                    btn_identifycode.setVisibility(View.GONE);//인증코드 확인 버튼 비활성화
                    countDownTimer.cancel();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"인증코드를 잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
      /*  btn_chagepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd1 = String.valueOf(et_change_password1.getText());
                String passwd2 = String.valueOf(et_change_password2.getText());
                Matcher matcher = Pattern.compile(pwPattern).matcher(passwd1);
                if(passwd1.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"변경할 패스워드를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }else if(!matcher.matches()){
                    Toast.makeText(getActivity().getApplicationContext(),"대소문자,특수문자 포함 9~12자리 수를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(passwd2.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"재확인 패스워드를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }else if(!passwd1.equals(passwd2)){
                    Toast.makeText(getActivity().getApplicationContext(),"비밀번호 불일치", Toast.LENGTH_SHORT).show();
                }else if(passwd1.equals(passwd2)){
                    RetrofitService retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
                    Change_Password inform = new Change_Password(id,name,email,passwd2);
                    Call<response> call  = retrofitService.changpasswd(inform);
                    call.enqueue(new Callback<response>() {
                        @Override
                        public void onResponse(Call<response> call, Response<response> response) {
                            Log.d("TAG", "onResponse: "+response.body().getResponse());
                            String state=  response.body().getResponse();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            if(state.equals("ok")){
                                builder.setTitle("변경성공").setMessage("비밀번호 변경을 성공하셨습니다. \n 로그인페이지로 이동합니다.");
                            }else{
                                builder.setTitle("변경실페").setMessage("비밀번호 변경을 실패하셨습니다. \n 다시 시도해주세요.");
                            }
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            getActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<response> call, Throwable t) {

                        }
                    });
                }
            }
        });*/
        return rootview;
    }

    public void countDownTimer() { //카운트 다운 메소드

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_textview.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_textview.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
                et_verification_code.setTextColor(Color.RED);
                et_verification_code.setText("시간만료!");
                time_textview.setText("0:00");
            }
        }.start();

    }

    public void init(ViewGroup root){
        et_pass_name =root.findViewById(R.id.et_pass_name); // 사용자 이름
        et_pass_id =root.findViewById(R.id.et_pass_id); // 사용자 아이디
        et_pass_email =root.findViewById(R.id.et_pass_email); // 사용자 이메일
        et_verification_code = root.findViewById(R.id.verification_code);
        btn_indentify = root.findViewById(R.id.identify_button); //인증하기 버튼
        inform_textview = root.findViewById(R.id.inform_message); //인증메시지 현황 상태 출력
        inform_textview.setVisibility(View.GONE); //텍스트뷰 비활성화
        btn_identifycode = root.findViewById(R.id.btn_identifycode); // 인증코드 확인 버튼
        btn_chagepassword = root.findViewById(R.id.btn_change_password); //패스워드 변경 버튼
        time_textview = root.findViewById(R.id.time_textview); //시간을 출력하는 뷰
        basic_linearLayout = root.findViewById(R.id.basic_linearLayout); //기본정보 입력 영역

        dialog = new AlertDialog.Builder(getContext());

        linearLayout = root.findViewById(R.id.verificaion_line); //인증코드
        linearLayout.setVisibility(View.GONE); //인증코드필드 비활성화
        btn_identifycode.setVisibility(View.GONE); //인증코드 버튼 비활성화
        password_linearlayout = root.findViewById(R.id.password_linearlayout); //비밀번호 영역
        password_linearlayout.setVisibility(View.GONE);// 비밀번호 영역 비활성화
        btn_chagepassword.setVisibility(View.GONE); // 비밀번호 변경 버튼 비활성화

        et_change_password1 = root.findViewById(R.id.change_password1); //변경하려는 비밀번호 edit1
        et_change_password2 = root.findViewById(R.id.change_password2); //변경하려는 비밀번호 edit2
    }
}