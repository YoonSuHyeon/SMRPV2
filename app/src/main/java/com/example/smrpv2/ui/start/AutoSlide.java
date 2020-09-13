package com.example.smrpv2.ui.start;

import android.os.Handler;

import androidx.viewpager.widget.ViewPager;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AutoSlide : 시간 경과에 따라 ViewPager에 나타나는 이미지가 자동 슬라이드 되는 기능을 정의한다.
 */
public class AutoSlide {
    static Timer timer = null; // 자동 슬라이드를 위한 변수
    static TimerTask timerTask= null;

    ViewPager viewPager;
    int currentPage = 0; // 현재 자동 슬라이드 페이지
    long delay_ms, period_ms;

    /**
     *
     * @param viewPager ViewPager 변수
     * @param delay_ms 기다리는 시간
     * @param period_ms 몇 초마다 반복되는지
     */
    public AutoSlide(ViewPager viewPager, long delay_ms, long period_ms){
        this.viewPager = viewPager;
        this.delay_ms = delay_ms;
        this.period_ms = period_ms;
    }

    public void startSlide(){
        if(timer != null){
            timerTask.cancel();
            timer.cancel() ;
        }

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == 3) { // 현재페이지가 맨 끝일 시 맨 처음 페이지로 돌아간다.
                    currentPage = 0;
                }

                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timerTask = new TimerTask() {
            public void run() {
                handler.post(Update);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, delay_ms, period_ms);
    }


}
