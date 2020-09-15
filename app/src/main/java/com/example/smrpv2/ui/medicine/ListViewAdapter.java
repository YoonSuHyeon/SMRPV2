package com.example.smrpv2.ui.medicine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

//import com.bumptech.glide.Glide;
import com.example.smrpv2.R;

import com.example.smrpv2.model.MedListViewItem;

import java.util.ArrayList;

/**
 * 약의 관련된 모든 ListView에 대한 Adaper
 * 약간식 다른 기능은 call_case로 구분하였음
 */
public class ListViewAdapter extends BaseAdapter {

    private FragmentActivity activity;
    final int MEDCINE_FRAGMENT = 1;
    final int TEMP_ALARM = 0;
    private int REQUEST_TEST=1;
    int call_case;
    private ArrayList<Integer> tempViewItemArrayList=new ArrayList<>();
    private ArrayList<MedListViewItem> listViewItemArrayList ;

    public ListViewAdapter(ArrayList<MedListViewItem> listViewItemArrayList, FragmentActivity activity, int call_case){
        this.listViewItemArrayList=listViewItemArrayList;
        this.activity=activity;
        this.call_case = call_case;
    }
    public ArrayList<MedListViewItem> res(){
        ArrayList<MedListViewItem> arrayList=new ArrayList<>() ;
        for(int i : tempViewItemArrayList){
            arrayList.add(listViewItemArrayList.get(i));
        }
        return arrayList;
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
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }


        final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.line_medicine);
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.med_name_1) ;
        TextView entpTextView = (TextView) convertView.findViewById(R.id.textView3) ;

        final MedListViewItem listViewItem = listViewItemArrayList.get(position);

        // 연결이 안되어 임시로 막아놓음
        /*iconImageView.setImageDrawable(listViewItem.getUrl());//500,100
        Glide.with(activity).load(listViewItem.getUrl()).override(500, 150).fitCenter().into(iconImageView);*/
        titleTextView.setText(listViewItem.getName());
        descTextView.setText(listViewItem.getTime());
        entpTextView.setText(listViewItem.getEntpName());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity.getBaseContext().getApplicationContext(), MedicineDetailActivity.class);
                intent.putExtra("itemSeq",listViewItem.getItemSeq());

                if(call_case == MEDCINE_FRAGMENT){
                    intent.putExtra("time",listViewItem.getTime());
                    activity. startActivity(intent);
                }
                else if(call_case == TEMP_ALARM){
                    intent.putExtra("listViewItemArrayList",listViewItemArrayList);
                    activity.startActivityForResult(intent, REQUEST_TEST);

                }else{
                    if(!tempViewItemArrayList.contains(pos)){
                        tempViewItemArrayList.add(pos);
                        linearLayout.setBackgroundColor(Color.LTGRAY);
                    }else{
                        linearLayout.setBackgroundColor(Color.WHITE);
                        tempViewItemArrayList.remove(Integer.valueOf(pos));
                    }
                }

            }
        });

        return convertView;


    }
}
