package com.example.smrpv2.ui.medicine.medshot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;

import java.util.ArrayList;


public class MedicineResultRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MedicineItem> mData = null ;
    RecyclerView rList;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    RecyclerView.ViewHolder holder1, holder2;
    int size;
    private int[] row_images;
    int t1=0, t2=0;
    Context context;
    public interface OnItemClickListener {
        void onItemClick(View v, int position, RecyclerView rList) ;
    }

    private OnItemClickListener mListener = null ;
    MedicineResultRecyclerAdapter(ArrayList<MedicineItem> list, OnItemClickListener mListener, RecyclerView rList, int size, int[] images) {
        Log.e("DD","111");
        mData = list ;
        this.mListener = mListener;
        this.rList = rList;
        this.size = size;
        mSelectedItems.put(0,true);

        row_images = images;
        t1=1;
        for(int i =1 ; i < size; i++) {

            mSelectedItems.put(i,false);}
    }
    MedicineResultRecyclerAdapter(ArrayList<MedicineItem> list, Context context){
        mData = list;

        if(mData.isEmpty()) Toast.makeText(context,"검색 결과가 없습니다.", Toast.LENGTH_LONG).show();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        Log.e("DD","2222");
        View view;
        view = inflater.inflate(R.layout.search_list, parent, false);
        return new ViewHolder(view);

     }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("DD","3333");
        MedicineItem item = mData.get(position);
        if(holder instanceof ViewHolder)
        { holder1 = ((ViewHolder) holder);

        ((ViewHolder) holder).icon.setImageDrawable(item.getIcon()) ;
        ((ViewHolder) holder).name.setText(item.getName());
        ((ViewHolder) holder).name.setTextColor(Color.parseColor("#989898"));

            if(mSelectedItems.get(position)) {
                ((ViewHolder) holder).icon.setImageResource(row_images[position]);
                ((ViewHolder) holder).name.setTextColor(Color.rgb(0, 119, 63));
            }
        }


    }


    @Override
    public int getItemCount() {
        if(mData==null){

        }

        return mData.size() ;
    }
    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getViewType();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon ;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView) ;
            icon = itemView.findViewById(R.id.Img_icon);
            name = itemView.findViewById(R.id.Txt_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    mListener.onItemClick(v, getAdapterPosition(),rList);
                    if (mSelectedItems.get(pos, false) ){
                        mSelectedItems.put(pos, false);
                        notifyItemChanged(pos);

                    } else {

                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            mSelectedItems.put(i, false);
                            notifyItemChanged(i);
                        }
                        mSelectedItems.put(pos, true);
                        icon.setImageResource(row_images[pos]);
                        name.setTextColor(Color.rgb(0,119,63));

                    }
                }
            });



        }
    }

    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

}