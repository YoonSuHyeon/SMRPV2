package com.example.smrpv2.ui.report;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smrpv2.R;
import com.example.smrpv2.model.DiseaseItem;

import java.util.ArrayList;

/**
 * ReportRecyclerAdapter : ReportFragment와 ReportResultActivity에서 사용
 * ReportFragment : ViewHolder 사용 - 각 RecyclerView 리스트 아이템 보이기 // 아이템 클릭 시 색깔 변환
 * ReportResultActivity : DiseaseViewHolder 사용 - 각 RecyclerView 리스트 아이템 보이기 // 아이템 클릭 시 상세 정보 Activity 띄움
 */
public class ReportRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DiseaseItem> mData = null ;
    RecyclerView rList;
    final int SYMPTOM_LIST = 0;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    public interface OnItemClickListener {
        void onItemClick(View v, int position, RecyclerView rList) ;
    }

    private ReportRecyclerAdapter.OnItemClickListener mListener = null ;
    ReportRecyclerAdapter(ArrayList<DiseaseItem> list, OnItemClickListener mListener, RecyclerView rList) {
        mData = list ;
        this.mListener = mListener;
        this.rList = rList;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view;
        if(viewType == SYMPTOM_LIST){
            view = inflater.inflate(R.layout.list_symptom, parent, false);
            return new ViewHolder(view);
        }
        view = inflater.inflate(R.layout.list_disease, parent, false);
        return new DiseaseViewHolder(view);


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DiseaseItem item = mData.get(position);
        if(holder instanceof ViewHolder)
        {
            ((ViewHolder)holder).Txt_symptom.setText(item.getSymptom());
            ((ViewHolder)holder).Txt_symptom_content.setText(item.getSymptomContent());
            if ( mSelectedItems.get(position) ){

                ((ViewHolder) holder).Lay_symptom.setBackgroundResource(R.drawable.shadow3);
                ((ViewHolder) holder).Txt_symptom.setTextColor(Color.parseColor("#0022FF"));
                ((ViewHolder) holder).Txt_symptom_content.setTextColor(Color.parseColor("#0022FF"));
                ((ViewHolder) holder).ic_check.setImageResource(R.drawable.ic_check_color);
            } else {
                ((ViewHolder) holder).Lay_symptom.setBackgroundResource(R.drawable.shadow2);
                ((ViewHolder) holder). Txt_symptom.setTextColor(Color.parseColor("#2E2E2E"));
                ((ViewHolder) holder).Txt_symptom_content.setTextColor(Color.parseColor("#8D8D8D"));
                ((ViewHolder) holder).ic_check.setImageResource(R.drawable.ic_check_basic);
            }

        }
        else if(holder instanceof DiseaseViewHolder){

            ((DiseaseViewHolder)holder).Txt_disease.setText(item.getDisease());
            ((DiseaseViewHolder)holder).Txt_symptom.setText(item.getSymptom());
            ((DiseaseViewHolder)holder).Txt_probability.setText(item.getProbability());
            ((DiseaseViewHolder)holder).Txt_department.setText(item.getDepartment());
        }



    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getViewType();
    }

    /**
     * ReportFragment가 사용 -> 각 리스트 띄움 && 클릭 이벤트 시 색깔 변화
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        String Str_symptom;
        ArrayList<String> list;
        LinearLayout Lay_symptom;
        TextView Txt_symptom;
        TextView Txt_symptom_content;
        ImageView ic_check;
        ViewHolder(View itemView) {
            super(itemView) ;
            Lay_symptom = itemView.findViewById(R.id.Lay_symptom);
            Txt_symptom = itemView.findViewById(R.id.Txt_symptom);
            ic_check = itemView.findViewById(R.id.ic_check);
            Txt_symptom_content = itemView.findViewById(R.id.Txt_symptom_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() ;
                    mListener.onItemClick(v, getAdapterPosition(),rList);
                    if ( mSelectedItems.get(position, false) ){
                        mSelectedItems.put(position, false);
                        Lay_symptom.setBackgroundResource(R.drawable.shadow2);
                        Txt_symptom.setTextColor(Color.parseColor("#2E2E2E"));
                        Txt_symptom_content.setTextColor(Color.parseColor("#8D8D8D"));
                        ic_check.setImageResource(R.drawable.ic_check_basic);


                    } else {
                        mSelectedItems.put(position, true);
                        Lay_symptom.setBackgroundResource(R.drawable.shadow3);
                        Txt_symptom.setTextColor(Color.parseColor("#0022FF"));
                        Txt_symptom_content.setTextColor(Color.parseColor("#0022FF"));
                        ic_check.setImageResource(R.drawable.ic_check_color);

                    }






                }
            });



        }
    }

    /**
     * ReportResultActivity - 각 리스트 띄움 && 클릭 이벤트 시 상세 정보 Activity 전환
     */
    public class DiseaseViewHolder extends RecyclerView.ViewHolder{
        TextView Txt_symptom;
        TextView Txt_disease;
        TextView Txt_department;
        TextView Txt_probability;
        DiseaseViewHolder(View itemView){
            super(itemView);
            Txt_disease = itemView.findViewById(R.id.Txt_disease);
            Txt_symptom = itemView.findViewById(R.id.Txt_Symptom);
            Txt_probability = itemView.findViewById(R.id.Txt_probability);
            Txt_department = itemView.findViewById(R.id.Txt_department);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    mListener.onItemClick(v, getAdapterPosition(),rList);




                }
            });
        }

    }



}
