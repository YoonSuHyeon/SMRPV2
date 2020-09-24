package com.example.smrpv2.ui.logout;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smrpv2.model.user.User;
import com.example.smrpv2.ui.login.LoginActivity;
import com.example.smrpv2.R;

public class LogoutFragment extends DialogFragment implements View.OnClickListener {
    private SharedPreferences loginInfromation;
    private SharedPreferences.Editor editor;
    private FragmentManager fragmentManager;
    private Boolean bool_logout = false;
    public static AlarmManager alarmManager=null;
    public static PendingIntent pendingIntent=null;
    Intent intent;
    private Dialog enddialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());

        alertdialog.setCancelable(false);//외부영역 터치시 dismiss되는것을 방지
        alertdialog.setMessage("현재 계정을 종료하시겠습니까?");
        loginInfromation = getActivity().getSharedPreferences("setting",0);
        enddialog = new Dialog();

        alertdialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String user_id = loginInfromation.getString("id","");
                String user_pass = loginInfromation.getString("password","");
                String name = loginInfromation.getString("name","");
                String getAutoLogin = loginInfromation.getString("auto_login","");



                User user = new User(user_id,"",user_pass,"","",""); //서버에서 USER 클래스를 받기에 불필요한 매개변수가 들어가도 이해할것
                /**
                 * 서버
                 * 알람에 관한것
                 */
                editor = loginInfromation.edit();
                editor.clear();
                editor.commit();
            }
        });

        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.addToBackStack(null);
                ft.commit();
                fragmentManager.popBackStack();

            }
        });

        AlertDialog alert = alertdialog.create();
        alert.show();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_logout, container, false);
        root.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onClick(View v) {

    }


    private class Dialog extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로그아웃 중입니다.");
            progressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2500); // 2초 지속

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            super.onPostExecute(result);
        }
    }

}