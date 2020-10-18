package com.example.smrpv2.ui.alarm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.smrpv2.R;
import com.example.smrpv2.model.alarm_model.AlarmItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * AlarmListViewAdapter : AlarmFragment에서 쓰는 Adapter
 * 클릭 시 AlarmEditActivity 호출
 */
public class AlarmListViewAdapter extends BaseAdapter {
    AlarmItem listViewAlarmItemI;
    LinearLayout linearLayout;
    TextView alarmName;
    TextView dose;
    TextView doseTypeView;
    TextView period;
    TextView remainingTime;
    ProgressBar progressBar;
    View convertView;
    private ArrayList<AlarmItem> listViewItemArrayList ;
    private FragmentActivity activity;

    public AlarmListViewAdapter (ArrayList<AlarmItem> listViewItemArrayList, FragmentActivity activity){
        this.listViewItemArrayList=listViewItemArrayList;
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return listViewItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_alarm, parent, false);
            this.convertView=convertView;
        }

        //초기화..
        linearLayout = (LinearLayout) convertView.findViewById(R.id.line_medicine);
        alarmName = (TextView) convertView.findViewById(R.id.tv_alarmName);
        dose = (TextView) convertView.findViewById(R.id.tv_dose);
        doseTypeView = (TextView) convertView.findViewById(R.id.tv_doseType);
        period = (TextView) convertView.findViewById(R.id.tv_period);
        remainingTime = (TextView) convertView.findViewById(R.id.tv_remainingTime);
        progressBar = (ProgressBar) convertView.findViewById(R.id.progress);

        listViewAlarmItemI = listViewItemArrayList.get(position);
        Log.d("groupIdposition", position + "");
        Log.d("groupIdpos", pos + "");
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewAlarmItemI = listViewItemArrayList.get(position);
                Intent intent = new Intent(activity.getBaseContext().getApplicationContext(), AlarmEditActivity.class);
                intent.putExtra("groupId", listViewAlarmItemI.getAlramGroupId());
                Log.d("groupId", listViewAlarmItemI.getAlramGroupId() + "");
                activity.startActivity(intent);

            }
        });


       read_date();
        return convertView;
    }
    void read_date(){
            int sidx = listViewAlarmItemI.getStartAlram().indexOf("T");
            int fidx = listViewAlarmItemI.getFinishAlram().indexOf("T");
            String tempStart=listViewAlarmItemI.getStartAlram().substring(0,sidx);
            String tempFinish=listViewAlarmItemI.getFinishAlram().substring(0,fidx);

            //현재 날짜 구하기
            long now = System.currentTimeMillis();
            Date date =new Date(now);

            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            String time = mformat.format(date);
            StringTokenizer st = new StringTokenizer(time,"-");
            StringTokenizer st2 = new StringTokenizer(tempFinish,"-");
            String nowTime="";
            int countTokens = st.countTokens();

            for(int i=0; i<countTokens; i++){
                nowTime += st.nextToken();
            }
            countTokens = st2.countTokens();
            String finishTime = "";
            for(int i=0; i<countTokens; i++){
                finishTime += st2.nextToken();
            }
            int now_time=Integer.parseInt(nowTime);
            int finish_time = Integer.parseInt(finishTime);

            if(now_time > finish_time){
                linearLayout.setBackgroundResource(R.drawable.drop_shadow2);
                progressBar.setProgressDrawable(convertView.getResources().getDrawable(R.drawable.progressbar_custom2));
            }

            try{
                Date FirstDate = mformat.parse(tempFinish);
                Date SecondDate = mformat.parse(time);

                long calDate = FirstDate.getTime() - SecondDate.getTime();

                long calDateDays = calDate / ( 24*60*60*1000);

                calDateDays = Math.abs(calDateDays);

                alarmName.setText(listViewAlarmItemI.getAlramName());
                dose.setText(listViewAlarmItemI.getOneTimeDose()+"회");
                doseTypeView.setText(listViewAlarmItemI.getDoseType());
                period.setText(tempStart+" ~ "+tempFinish);


                double progress =(1.0-((double)(calDateDays)/Double.parseDouble(listViewAlarmItemI.getDosingPeriod()))) *100;
                if(FirstDate.getTime()<SecondDate.getTime()){
                    progressBar.setProgress(100);
                    remainingTime.setText(listViewAlarmItemI.getDosingPeriod()+"/"+listViewAlarmItemI.getDosingPeriod());
                }else{
                    progressBar.setProgress((int) progress);
                    remainingTime.setText((Long.parseLong(listViewAlarmItemI.getDosingPeriod())-calDateDays)+"/"+listViewAlarmItemI.getDosingPeriod());
                }


            }catch(Exception e){
                e.printStackTrace();
            }




    }
}
