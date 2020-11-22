package com.example.smrpv2.ui.medicine.searchMed;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smrpv2.R;
import com.example.smrpv2.model.medicine_model.Prescriptionitem;

import java.util.ArrayList;

/**
 * 약 봉투 & 처방전 사진 찍고 그에 맞는 약 리 스트에 대한 Adapter
 * MedicineItem으로 약 리스트 구성
 */
public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {
    private ArrayList<Prescriptionitem> list = new ArrayList<>();
    private OnItemClickListener listener;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    Context context;

    PrescriptionAdapter(ArrayList<Prescriptionitem> list){
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(PrescriptionAdapter.ViewHolder holder, View v, int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_search_medicine_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//onBindViewHolder함수는 생성된 뷰홀더에 데이터를 바인딩 해주는 함수

        String stringURL = list.get(position).getStringURL(); //의약품 이미지 url
        String name = list.get(position).getName();//의약품 이름
        String entpName = list.get(position).getEntpName();//의약품 제조사
        String etcOtcName = list.get(position).getEtcOtcName();// 의약품정보(일반, 전문)
        holder.Txt_name.setText(name);
        holder.Txt_entpname.setText(entpName);
        holder.Txt_etcname.setText(etcOtcName);
        Glide.with(context).load(stringURL).override(500,150).fitCenter().into(holder.img);

    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;

    }
    public void onItemClick(PrescriptionAdapter.ViewHolder holder, View view , int position){
        if(listener!=null)
            listener.onItemClick(holder,view,position);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView Txt_name, Txt_entpname,Txt_etcname;
        int pos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.medicine_image); //의약품 이미지
            Txt_name = itemView.findViewById(R.id.medicine_name); //의약품 이름
            Txt_entpname = itemView.findViewById(R.id.medicine_entpName); //의약품 제조사
            Txt_etcname = itemView.findViewById(R.id.medicine_etcName); //의약품 정보(일반, 전문)
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        pos = getAdapterPosition();
                        onItemClick(ViewHolder.this,v,pos);
                        if ( mSelectedItems.get(pos, false) ){
                            mSelectedItems.put(pos, false);
                            v.setBackgroundColor(Color.WHITE);
                        } else {
                            mSelectedItems.put(pos, true);
                            v.setBackgroundColor(Color.LTGRAY);
                        }

                    }
                }
            });

        }
    }


}
