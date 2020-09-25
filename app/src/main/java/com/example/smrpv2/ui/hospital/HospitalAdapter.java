package com.example.smrpv2.ui.hospital;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Hospital;
import com.example.smrpv2.model.hospital_model.Item;

import java.util.ArrayList;

/**
 *
 * HospitalAdapter : Hospital Adapter
 *
 */
public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    public ArrayList<Item> items = new ArrayList<>();
    public OnHospitalItemClickListener listener;

    public HospitalAdapter(ArrayList<Item> list) {
        this.items = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.hospital_list, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.setItem(item);
    }

    public void setOnitemClickListener(OnHospitalItemClickListener listener){
        this.listener = listener;
    }
    public void onItemClick(HospitalAdapter.ViewHolder holder, View view, int position){
        if(listener != null)
            listener.onItemClick(holder,view,position);
    }
    public void onCallClick(int position){
        if(listener != null){
            listener.onCallClick(position);
        }
    }
    public void onPathClick(int position){
        if(listener != null){
            listener.onPathClick(position);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textview_name,textview_type,textView_addr,textview_hosurl,textview_hostelno,textView_distance;
        Button button_path,button_call;
        public ViewHolder(@NonNull View itemView, HospitalAdapter hospitalAdapter) {
            super(itemView);

            textview_name = itemView.findViewById(R.id.textView_hos_name); //병원 이름
            textview_type = itemView.findViewById(R.id.textView_hos_type); //병원 타입
            textView_distance = itemView.findViewById(R.id.textView_hos_distance); //병원거리
            textView_addr = itemView.findViewById(R.id.textView_hos_addr); //병원 주소
            textview_hostelno = itemView.findViewById(R.id.textView_telno); //병원 전화번호
            button_path = itemView.findViewById(R.id.pathBtn);//길찾기
            button_call = itemView.findViewById(R.id.callBtn);//전화걸기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });

            button_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        listener.onCallClick(position);
                    }
                }
            });
            button_path.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        listener.onPathClick(position);
                    }
                }
            });
        }
        public void setItem(Item item){
            textview_name.setText(item.getYadmNm()); //병원이름
            textview_type.setText(item.getClCdNm()); //병원 규모
            textView_addr.setText(item.getAddr());//병원 주소
            textview_hostelno.setText(item.getTelno());//병원 전화버놓
            textView_distance.setText(String.valueOf((int)item.getDistance())+"m");//병원거리
        }
    }
}
