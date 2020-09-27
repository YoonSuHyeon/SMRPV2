package com.example.smrpv2.ui.tos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.smrpv2.R;
/**
 * 약관동의 법2
 */
public class ToSLaw2Activity extends Dialog {


    private Button Btn_ok;
    private View.OnClickListener mPositivieListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos__law2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;

        Btn_ok = findViewById(R.id.okbutton);
        Btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    protected ToSLaw2Activity(Context context) {
        super(context);
    }
}
