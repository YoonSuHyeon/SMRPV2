package com.example.smrpv2.ui.inquiry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.smrpv2.R;
import com.example.smrpv2.model.InquiryMessage;

/**
 *
 * InquiryFragment : 문의하기 기능을 한다. 사용자가 입력한 글을 서버에게 전송한다.
 */

public class InquiryFragment extends Fragment {

    private Button btn_send;
    private TextView content;
    private SharedPreferences login_inform;
    private String userid, message;
    private AlertDialog.Builder diaglog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inquiry, container, false);

        //초기화..
        btn_send = root.findViewById(R.id.sendbutton); //전송하기 버튼
        diaglog  = new AlertDialog.Builder(getContext());
        diaglog.setCancelable(false);
        content = root.findViewById(R.id.content); //사용자가 입력한 내용
        login_inform = getActivity().getSharedPreferences("setting",0);
        userid = login_inform.getString("id","");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = String.valueOf(content.getText());
                if(message.equals("")){ //메시지 입력없이 전송하기 버튼을 누를경우
                    Toast.makeText(getActivity().getApplicationContext(),"내용을 입력하세요.",Toast.LENGTH_SHORT).show();
                }else{
                    InquiryMessage send_message = new InquiryMessage(message,userid);
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
