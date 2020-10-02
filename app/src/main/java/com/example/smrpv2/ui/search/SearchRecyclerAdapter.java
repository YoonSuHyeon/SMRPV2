package com.example.smrpv2.ui.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.ui.medicine.MedicineDetailActivity;

import java.util.ArrayList;

/**
 * SearchRecyclerAdapter : SearchAcitivity에 대한 모든 RecyclerAdapter 담당
 * ViewHolder : SearchActivity에서 약에 대한 정보(모양, 색상 등) 담당. 여기서 텍스트로 된 부분은 제외
 * TextViewHolder : SearchActivity에서 약에 대한 정보 중 텍스트로 된 정보, 즉 모든 정보의 맨 앞 아이템 담당
 * SearchResultViewHolder : SearchActivity에서 약 검색 시 띄어주는 약 리스트 담당
 *
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    SearchRecyclerAdapter(ArrayList<MedicineItem> list, OnItemClickListener mListener, RecyclerView rList, int size, int[] images) {
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
    SearchRecyclerAdapter(ArrayList<MedicineItem> list){
        mData = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view;
        if(viewType == 5)
        {
            view = inflater.inflate(R.layout.search_list, parent, false);
            return new ViewHolder(view);
        }
        else if(viewType == 6){
            view = inflater.inflate(R.layout.search_list2, parent, false);
            return new TextViewHolder(view);
        }
        view= inflater.inflate(R.layout.recycler_medicine_item,parent,false);
        return new SearchResultViewHolder(view);
     }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MedicineItem item = mData.get(position);
        if(holder instanceof ViewHolder)
        { holder1 = ((ViewHolder) holder);

        ((ViewHolder) holder).icon.setImageDrawable(item.getIcon()) ;
        ((ViewHolder) holder).name.setText(item.getName());
        ((ViewHolder) holder).name.setTextColor(Color.parseColor("#989898"));

            if(mSelectedItems.get(position)) {
                ((ViewHolder) holder).icon.setImageResource(row_images[position - 1]);
                ((ViewHolder) holder).name.setTextColor(Color.rgb(0, 119, 63));
            }
        }

        else if(holder instanceof TextViewHolder) {
            holder2 = (TextViewHolder) holder;
            ((TextViewHolder) holder).name.setText(item.getName());

        }else{
            String stringURL = mData.get(position).getUrl();
            String name = mData.get(position).getName();
            String entpName = mData.get(position).getEntpName();
            String form = mData.get(position).getForm();
            String type = mData.get(position).getType();
            ((SearchResultViewHolder) holder).medicine_tv1.setText(name);
            ((SearchResultViewHolder) holder).medicine_tv2.setText(entpName);
            ((SearchResultViewHolder) holder).medicine_tv3.setText(form);
            ((SearchResultViewHolder) holder).medicine_tv4.setText(type);
           Glide.with(context).load(stringURL).override(500,100).fitCenter().into(((SearchResultViewHolder)holder).medicine_img);

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

    /**
     * ViewHolder : SearchActivity에서 약에 대한 정보(모양, 색상 등) 담당. 여기서 텍스트로 된 부분은 제외
     * 특히 클릭 이벤트(SearchAcitivity에서 약에 대한 정보 클릭)가 발생할 시 색상 및 테두리 변경을 담당해준다.
     */
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
                        mSelectedItems.put(pos, true);
                        TextView name0 =(TextView)holder2.itemView.findViewById(R.id.Txt_name);
                        name0.setTextColor(Color.parseColor("#989898"));
                        name0.setBackgroundResource(android.R.color.transparent);
                        mSelectedItems.put(0, false);
                        icon.setImageResource(row_images[pos-1]);
                        name.setTextColor(Color.rgb(0,119,63));

                    }
                }
            });



        }
    }

    /**
     * TextViewHolder : SearchActivity에서 약에 대한 정보 중 텍스트로 된 정보, 즉 모든 정보의 맨 앞 아이템 담당
     * 특히 클릭 이벤트(SearchAcitivity에서 약에 대한 정보 클릭)가 발생할 시 색상 및 테두리 변경을 담당해준다.
     */
    public class TextViewHolder extends RecyclerView.ViewHolder{

        TextView name; int p;
        TextViewHolder(View itemView) {
            super(itemView) ;
            name = itemView.findViewById(R.id.Txt_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, getAdapterPosition(),rList);
                    int pos = getAdapterPosition() ;
                    if (mSelectedItems.get(pos, false) ){
                        mSelectedItems.put(pos, false);
                        name.setBackgroundResource(android.R.color.transparent);
                        name.setTextColor(Color.parseColor("#989898"));
                    } else {
                        mSelectedItems.put(pos, true);
                        for (int i = 1; i < mSelectedItems.size(); i++) {
                            mSelectedItems.put(i, false);
                            notifyItemChanged(i);
                        }
                        name.setBackgroundResource(R.drawable.img_stroke);
                        name.setTextColor(Color.rgb(0,119,63));
                    }
                }
            });
        }
    }

    /**
     * SearchResultViewHolder : SearchActivity에서 약 검색 시 띄어주는 약 리스트 담당
     * 클릭 이벤트 -> 약 정보 클릭시 약 상세정보 띄어주기
     */
    public class SearchResultViewHolder extends RecyclerView.ViewHolder{

        ImageView medicine_img;
        TextView medicine_tv1,medicine_tv2,medicine_tv3,medicine_tv4;
        SearchResultViewHolder(final View itemView){
            super(itemView);

            medicine_img = itemView.findViewById(R.id.medicine_img);
            medicine_tv1 = itemView.findViewById(R.id.medicine_tv1);
            medicine_tv2 = itemView.findViewById(R.id.medicine_tv2);
            medicine_tv3 = itemView.findViewById(R.id.medicine_tv3);
            medicine_tv4 = itemView.findViewById(R.id.medicine_tv4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context.getApplicationContext(), MedicineDetailActivity.class);
                        intent.putExtra("itemSeq",mData.get(pos).getItemSeq().toString());
                        intent.putExtra("Search","Search");
                        context.startActivity(intent);
                    }
                }
            });

        }

    }
    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

}