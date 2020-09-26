package com.example.smrpv2.ui.hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.smrpv2.R;
public class PopupHospital extends DialogFragment {
    private static PopupHospital popupHospital;
    private HospitalFragment hos_frgment;

    private int count = 13, i;
    Button[] btn;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.hospital_dialog, container, false);

        hos_frgment = HospitalFragment.getInstance();

        btn = new Button[count];
        Integer[] numBtnIds = {R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,R.id.btn10,R.id.btn11,R.id.btn12,R.id.Btn_cancel};

        for(i = 0; i<count;i++){//버튼 객체 할당
            btn[i] = (Button)root.findViewById(numBtnIds[i]);
        }
        //btn[count-1] = root.findViewById(R.id.Btn_cancel);

        this.SetListener();
        /*for(i = 0; i<count-1;i++){
            final int index;
            index = i;

            btn[index].setOnClickListener(this);
        }*/
        return root;
    }
    public void SetListener(){
        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn1:
                        hos_frgment.DgsbjtCd("가정의학과");
                        dismiss();
                        break;
                    case R.id.btn2:
                        hos_frgment.DgsbjtCd("일반내과");
                        dismiss();
                        break;
                    case R.id.btn3:
                        hos_frgment.DgsbjtCd("소아과");
                        dismiss();
                        break;
                    case R.id.btn4:
                        hos_frgment.DgsbjtCd("성형외과");
                        dismiss();
                        break;
                    case R.id.btn5:
                        hos_frgment.DgsbjtCd("안과");
                        dismiss();
                        break;
                    case R.id.btn6:
                        hos_frgment.DgsbjtCd("외과");
                        dismiss();
                        break;
                    case R.id.btn7:
                        hos_frgment.DgsbjtCd("침구과");
                        dismiss();
                        break;
                    case R.id.btn8:
                        hos_frgment.DgsbjtCd("응급실");
                        dismiss();
                        break;
                    case R.id.btn9:
                        hos_frgment.DgsbjtCd("이비인후과");
                        dismiss();
                        break;
                    case R.id.btn10:
                        hos_frgment.DgsbjtCd("정신건강의학과");
                        dismiss();
                        break;
                    case R.id.btn11:
                        hos_frgment.DgsbjtCd("치과");
                        dismiss();
                        break;
                    case R.id.btn12:
                        hos_frgment.DgsbjtCd("정형외과");
                        dismiss();
                        break;
                    case R.id.Btn_cancel:
                        dismiss();
                        break;

                }
            }
        };
        for(i=0;i<count;i++){
            btn[i].setOnClickListener(listener);
        }
    }
    public static PopupHospital getInstance(){

        if(popupHospital==null)
            popupHospital = new  PopupHospital();
        return popupHospital;
    }
}
