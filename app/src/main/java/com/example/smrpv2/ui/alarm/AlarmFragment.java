package com.example.smrpv2.ui.alarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smrpv2.R;

public class AlarmFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        if(container.getChildCount() > 0)
            container.removeViewAt(0);

        final android.view.View root = inflater.inflate(R.layout.alarm_fragment, container, false);

        return root;

    }
}