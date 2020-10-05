package com.example.smrpv2.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smrpv2.R;
import com.example.smrpv2.model.home_model.HomeMedItem;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * HomeRecyclerAdapter : 홈 화면의 약 랭킹의 Adapter 담당
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private final int FIRST = 0;
    private final int SECOND = 1;
    private ArrayList<HomeMedItem> mList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected LinearLayout linearLayout;
        protected ImageView ic_rank;
        protected TextView txt_medName;

        public ViewHolder(View view){
            super(view);
            linearLayout = view.findViewById(R.id.line_medicine);
            this.ic_rank = view.findViewById(R.id.imageView1);
            this.txt_medName = view.findViewById(R.id.med_name);
        }

    }
    HomeRecyclerAdapter(ArrayList<HomeMedItem> list){
        this.mList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_rank_list,viewGroup,false);
        ViewHolder viewHolder =new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,int position){
        String text = mList.get(position).getName();
        StringTokenizer st = new StringTokenizer(text,"(");
        String str_medName = st.nextToken();

        if(position==FIRST){
            viewHolder.ic_rank.setImageResource(R.drawable.ic_rank1);
        }else if(position==SECOND){
            viewHolder.ic_rank.setImageResource(R.drawable.ic_rank2);
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#D88659"));
        }else{
            viewHolder.ic_rank.setImageResource(R.drawable.ic_rank3);
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#5DDDCE"));
        }

        viewHolder.txt_medName.setText(str_medName);
    }

    @Override
    public int getItemCount(){
        return (null != mList ? mList.size() : 0);
    }
}

