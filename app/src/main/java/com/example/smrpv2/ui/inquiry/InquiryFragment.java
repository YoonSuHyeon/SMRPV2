package com.example.smrpv2.ui.inquiry;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smrpv2.R;
import com.example.smrpv2.model.InquiryDto;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * InquiryFragment : 문의하기 기능을 한다. 사용자가 입력한 글을 서버에게 전송한다.
 */

public class InquiryFragment extends Fragment {

    private Button btn_send;
    private EditText content;
    private SharedPreferences login_inform;
    private String userid, message;
    private AlertDialog.Builder diaglog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inquiry, container, false);

        //초기화..
        btn_send = root.findViewById(R.id.sendbutton); //전송하기 버튼
        diaglog = new AlertDialog.Builder(getContext());
        diaglog.setCancelable(false);
        content = root.findViewById(R.id.content); //사용자가 입력한 내용
        login_inform = getActivity().getSharedPreferences("setting", 0);
        userid = UserInform.getUserId();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = String.valueOf(content.getText());
                if (message.equals("")) { //메시지 입력없이 전송하기 버튼을 누를경우
                    Toast.makeText(getActivity().getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    InquiryDto send_message = new InquiryDto(message, userid);
                    Call<Message> call = RetrofitHelper.getRetrofitService_server().addInquiry(send_message);
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.body().getResultCode().equals("OK")){
                                Toast.makeText(getActivity().getApplicationContext(),"전송 완료",Toast.LENGTH_SHORT).show();
                                content.setText("");
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Log.d("inquiry",t.toString());
                        }
                    });

                    /**
                     *
                     * 서버
                     *
                     *
                     */
                }
            }
        });

        return root;
    }

}
