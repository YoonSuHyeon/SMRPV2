package com.example.smrpv2.ui.pharmacy;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smrpv2.R;
import com.example.smrpv2.model.pharmcy_model.PharmacyItem;
import java.util.List;

public class PharmacyAdapter extends  RecyclerView.Adapter<PharmacyAdapter.ViewHolder> {

    public List<PharmacyItem> items;
    public OnPharmacyItemClickListener listener;


    public PharmacyAdapter(List<PharmacyItem> list){
        this.items = list;
    }
    /*public interface  OnPharmacyItemClickListener{
        void onItemClick(PharmacyAdapter.ViewHolder holder, View view, int position);
        void onCallClick(int position);
        void onPath(int position);
    }*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.pharmacy_list, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyAdapter.ViewHolder holder, int position) {
        PharmacyItem item = items.get(position);
        holder.setItem(item);
    }

    public void setOnItemClickListener(OnPharmacyItemClickListener listener){
        this.listener = listener;
    }

    public void onItemClick(PharmacyAdapter.ViewHolder holder, View view, int position){
        if(listener != null)
            listener.onItemClick(holder,view,position);
    }
    public void onCallClick(int position){
        if(listener != null){
            listener.onCallClick(position);
        }
    }
    public void onPath(int position){
        if(listener != null){
            listener.onPath(position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textview_name,textview_distance,textview_address,textView_telno;
        Button button_path, button_call;

        public ViewHolder(View itemView, PharmacyAdapter pharmacyAdapter) {
            super(itemView);

            textview_name = itemView.findViewById(R.id.textView_phy_name);
            textview_distance = itemView.findViewById(R.id.textView_distance);
            textview_address = itemView.findViewById(R.id.textView_address);
            textView_telno = itemView.findViewById(R.id.textView_telno);
            button_path= itemView.findViewById(R.id.root_Btn);

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
                public void onClick(View view) {
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
                        listener.onPath(position);
                    }
                }
            });
        }

        public void setItem(PharmacyItem item) {
            String yadmnm = item.getYadmNm();
            textview_name.setText(yadmnm); //이름

            String address = item.getAddr();
            if(address.contains("("))
                address = address.substring(0,address.indexOf("("));

            address += "("+item.getPostNo()+")";
            textview_address.setText(address); //주소

            String distance = item.getDistance();
            if(distance.contains("."))
                distance = distance.substring(0,distance.indexOf("."))+"m";
            textview_distance.setText(distance);//거리
            textView_telno.setText(item.getTelNo());//전화번호

        }
    }
}
