package com.example.smrpv2.ui.hospital;

import android.view.View;

interface OnHospitalItemClickListener {
    public void onItemClick(HospitalAdapter.ViewHolder hoder, View view, int position);//ViewHolder : 각 뷰를 보관하는 Holder객체를 의미
    public void onPathClick(int position);
    public void onCallClick(int postion);
}
