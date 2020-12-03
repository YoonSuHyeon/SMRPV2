package com.example.smrpv2.ui.common;

import android.app.Activity;
import android.app.ProgressDialog;

public class ShareProgrees {
    ProgressDialog progressDialog;
    public ShareProgrees(Activity activity, String message){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);

    }
    public void show(){
        progressDialog.show();
    }
    public void dismiss(){
        progressDialog.dismiss();
    }
}
