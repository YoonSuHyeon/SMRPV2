package com.example.smrpv2.ui.medicine;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.medicine.medshot.CameraFrontActivity;
import com.example.smrpv2.ui.medicine.searchMed.Search_prescriptionActivity;
import com.example.smrpv2.ui.search.SearchActivity;

import java.net.Inet4Address;

/**
 * PoupFragment : MedicineFragment, 즉 약 등록하기 화면에서 +버튼 클릭 시 띄어주는 팝업창이다.
 * 기능 : 약 사진 찍기, 약봉투,처방전 사진 찍기, 약 검색하기 화면으로 이동 가능
 */
public class PopupFragment extends DialogFragment {

    private ImageView Img_med_shoot; // 약 촬영 아이콘
    private ImageView Img_envelope; // 약 봉투 아이콘
    private ImageView Img_prescription; // 처방전 아이콘
    private ImageView Img_search; // 약명검색 아이콘
    private Button Btn_cancel; // 취소 아이콘
    private Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medicine_dialog, container, false);

        //초기화..
        Img_med_shoot = view.findViewById(R.id.Img_med_shoot);
        Img_envelope = view.findViewById(R.id.Img_envelope);
        Img_prescription = view.findViewById(R.id.Img_prescription);
        Img_search = view.findViewById(R.id.Img_search);
        Btn_cancel = view.findViewById(R.id.Btn_cancel);


        Img_med_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CameraFrontActivity.class);
                    startActivity(intent);
            }
        });
        Img_envelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext().getApplicationContext(), Search_prescriptionActivity.class);//
                startActivity(intent);
                dismiss();
            }
        });
        Img_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext().getApplicationContext(), Search_prescriptionActivity.class);//
                startActivity(intent);
                dismiss();

            }
        });
        Img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        Btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
