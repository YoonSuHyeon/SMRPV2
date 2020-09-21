package com.example.smrpv2.ui.report;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smrpv2.R;

public class DetailActivity extends AppCompatActivity {

    String Str_sym;
    String Str_dis;
    String Str_depart;
    TextView Txt_symptom_content;
    TextView Txt_disease_content;
    TextView Txt_department_content;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        Intent intent = getIntent();
        Str_sym = intent.getStringExtra("selected_sym");
        Str_dis = intent.getStringExtra("selected_dis");
        Str_depart = intent.getStringExtra("selected_depart");

        Txt_department_content= findViewById(R.id.Txt_department_content);
        Txt_symptom_content= findViewById(R.id.Txt_symptom_content);
        Txt_disease_content= findViewById(R.id.Txt_disease_content);

        Txt_department_content.setText(Str_depart);
        Txt_symptom_content.setText(Str_sym);
        Txt_disease_content.setText(Str_dis);
    }
}
