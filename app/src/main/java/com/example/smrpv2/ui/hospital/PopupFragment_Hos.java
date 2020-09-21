package com.example.smrpv2.ui.hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.smrpv2.R;

/**
 *
 * 진료과목 선택하는 PoupFragment이다.
 *
 */
public class PopupFragment_Hos extends DialogFragment {

    private static PopupFragment_Hos popupFragment_hos;
    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn_cancle;

    private HospitalFragment hospitalFragment;
    String dgsbjtCd_name="null";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.hospital_dialog, container, false);



        btn1 = root.findViewById(R.id.btn1);
        btn2 = root.findViewById(R.id.btn2);
        btn3 = root.findViewById(R.id.btn3);
        btn4 = root.findViewById(R.id.btn4);
        btn5 = root.findViewById(R.id.btn5);
        btn6 = root.findViewById(R.id.btn6);
        btn7 = root.findViewById(R.id.btn7);
        btn8 = root.findViewById(R.id.btn8);
        btn9 = root.findViewById(R.id.btn9);
        btn10 = root.findViewById(R.id.btn10);
        btn11 = root.findViewById(R.id.btn11);
        btn12 = root.findViewById(R.id.btn12);
        btn13 = root.findViewById(R.id.btn13);
        btn_cancle = root.findViewById(R.id.Btn_cancel);
        hospitalFragment = HospitalFragment.getInstance();
        Log.d("TAG", "hospitalFragment2: "+hospitalFragment);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "가정의학과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "성형외과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "소아과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "내과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "외과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "이비인후과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "정형외과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "피부과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "안과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "치과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "정신건강의학과";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);

                dismiss();
            }
        });
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "응급실";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });
        btn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "한의원";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);
                dismiss();
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgsbjtCd_name = "취소";
                hospitalFragment.DgsbjtCd(dgsbjtCd_name);

                dismiss();
            }
        });

        return root;
    }
    public static PopupFragment_Hos getInstance(){

        if(popupFragment_hos==null)
            popupFragment_hos = new  PopupFragment_Hos();
        return popupFragment_hos;
    }
    public String getDgsbjtCd_name(){
        Log.d("TAG", "getDgsbjtCd_name: "+dgsbjtCd_name);
        return dgsbjtCd_name;
    }

}
