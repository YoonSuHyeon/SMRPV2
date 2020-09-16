package com.example.smrpv2.ui.report;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.smrpv2.R;

/**
 * ReportNoticeFragment : 진단 시작 알림 Fragment
 */
public class ReportNoticeFragment extends Fragment {

    Button Btn_start;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_notice, container, false);

        Btn_start =root. findViewById(R.id.Btn_start);

        Btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) ((AppCompatActivity) getContext()).getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_nav_report_notice_to_nav_report);

            }
        });

        return  root;
    }
}