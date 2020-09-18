package com.example.smrpv2.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;
import java.util.ArrayList;

/**
 * SearchActivity : 약을 검색할 수 있는 Activity
 * 사용자가 약의 정보(모양, 제형, 색깔 등등)을 선택 후 검색하면 그와 관련된 약의 리스트를 띄어준다.
 */

public class SearchActivity extends AppCompatActivity implements SearchRecyclerAdapter.OnItemClickListener{
    Boolean scrollStop;

    ArrayList<String> shape1 = new ArrayList<String>();
    ArrayList<String> formula1 = new ArrayList<String>();
    ArrayList<String> line1 = new ArrayList<String>();
    ArrayList<String> color1 = new ArrayList<String>();
    String color="color_all",shape="shape_all",formula="formula_all",line="line_all";
    Button Btn_search;

    RecyclerView Lst_shape= null ;
    RecyclerView Lst_color= null ;
    RecyclerView Lst_dosageForm= null ;
    RecyclerView Lst_line= null ;
    ImageView iv_back;

    SearchRecyclerAdapter adapter_row1 = null ;
    SearchRecyclerAdapter adapter_row2  = null ;
    SearchRecyclerAdapter adapter_row3  = null ;
    SearchRecyclerAdapter adapter_row4 = null ;
    ImageView iv1;

    Bitmap bitmap,resized;
    String stringURL;
    ArrayList<MedicineItem> list_row1 = new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row2= new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row3 = new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row4 = new ArrayList<MedicineItem>();

    EditText et_findMedicine;
    ArrayList<MedicineItem> searchResultItem = null;
    Button Btn_add;
    ArrayList<String> selected_row1 = new ArrayList<String>();
    ArrayList<String> selected_row2 = new ArrayList<String>();
    private SparseBooleanArray mSelectedItems1 = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItems2 = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItems3 = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItems4 = new SparseBooleanArray(0);

    private int[] row_images1 = {R.drawable.ic_circle_green,R.drawable.ic_triangle_green, R.drawable.ic_rectangle_green,R.drawable.ic_rhombus_green, R.drawable.ic_oblong_green,R.drawable.ic_oval_green,R.drawable.ic_semicircle_green,R.drawable.ic_pentagon_green,R.drawable.ic_hexagon_green,R.drawable.ic_octagon_green,R.drawable.ic_etc_green};
    private int[] row_images2 = {R.drawable.ic_white_green,R.drawable.ic_yellow_green,R.drawable.ic_orange_green,R.drawable.ic_pink_green,R.drawable.ic_red_green,R.drawable.ic_brown_green,R.drawable.ic_yellowgreen_green, R.drawable.ic_purple_green,R.drawable.ic_blue_green,R.drawable.ic_blue_green,R.drawable.ic_navy_green,R.drawable.ic_redviolet_green,R.drawable.ic_gray_green,R.drawable.ic_black_green};
    private int[] row_images3 = {R.drawable.ic_ref_green,R.drawable.ic_hard_cap_green,R.drawable.ic_soft_cap_green};
    private int[] row_images4 = {R.drawable.ic_empty_green,R.drawable.ic_line_minus_green,R.drawable.ic_line_pluse_green,R.drawable.ic_line_etc_green};
    //@@@@@@@@@@@@@@@

    private InputMethodManager imm;
    @SuppressLint("ClickableViewAccessibility")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_findMedicine = findViewById(R.id.et_findMedicine);
        Lst_shape = findViewById(R.id.Lst_shape);
        Lst_color = findViewById(R.id.Lst_color);
        Lst_dosageForm = findViewById(R.id.Lst_dosageForm);
        Lst_line = findViewById(R.id.Lst_line);
        Btn_search = findViewById(R.id.Btn_search);
        iv_back = findViewById(R.id.iv_back);

        shape1.add("모양전체");
        color1.add("색상전체");
        formula1.add("제형전체");
        line1.add("분할선전체");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        adapter_row1  = new SearchRecyclerAdapter(list_row1,this, Lst_shape,12, row_images1) ;
        adapter_row2  = new SearchRecyclerAdapter(list_row2,this, Lst_color,15,row_images2) ;
        adapter_row3  = new SearchRecyclerAdapter(list_row3,this, Lst_dosageForm,4,row_images3) ;
        adapter_row4  = new SearchRecyclerAdapter(list_row4,this,Lst_line,5,row_images4) ;


        Lst_shape .setAdapter(adapter_row1);
        Lst_shape .setLayoutManager(layoutManager ) ;

        Lst_color .setAdapter(adapter_row2);
        Lst_color .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;

        Lst_dosageForm .setAdapter(adapter_row3);
        Lst_dosageForm .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;

        Lst_line .setAdapter(adapter_row4);
        Lst_line .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;



        resizeTableSize();




        //list_row1 - 모양 결정
        addItem(list_row1,null,
                "모양\n전체",1, "모양전체");
        addItem(list_row1,getDrawable(R.drawable.ic_circle), "원형",0,"원형");
        addItem(list_row1,getDrawable(R.drawable.ic_triangle),
                "삼각형",0, "삼각형");
        addItem(list_row1,getDrawable(R.drawable.ic_rectangle),
                "사각형",0,"사각형");
        addItem(list_row1,getDrawable(R.drawable.ic_rhombus),
                "마름모",0,"마름모형");
        addItem(list_row1,getDrawable(R.drawable.ic_oblong),
                "장방형",0,"장방형");
        addItem(list_row1,getDrawable(R.drawable.ic_oval),
                "타원형",0,"타원형");
        addItem(list_row1,getDrawable(R.drawable.ic_semicircle),
                "반원형",0,"반원형");

        addItem(list_row1,getDrawable(R.drawable.ic_pentagon),
                "오각형",0,"오각형");

        addItem(list_row1,getDrawable(R.drawable.ic_hexagon),
                "육각형",0,"육각형");

        addItem(list_row1,getDrawable(R.drawable.ic_octagon),
                "팔각형",0,"팔각형");


        addItem(list_row1,getDrawable(R.drawable.ic_etc),
                "기타",0,"기타");


        //list_row2 - 색상
        addItem(list_row2,null,
                "색상\n전체",1, "색상전체");
        addItem(list_row2,getDrawable(R.drawable.ic_white),
                "하양",0, "하양");
        addItem(list_row2,getDrawable(R.drawable.ic_yellow),
                "노랑",0,"노랑");
        addItem(list_row2,getDrawable(R.drawable.ic_orange),
                "주황",0,"주황");
        addItem(list_row2,getDrawable(R.drawable.ic_pink),
                "분홍",0,"분홍");
        addItem(list_row2,getDrawable(R.drawable.ic_red),
                "빨강",0,"빨강");
        addItem(list_row2,getDrawable(R.drawable.ic_brown),
                "갈색",0,"갈색");
        addItem(list_row2,getDrawable(R.drawable.ic_yellowgreen),
                "연두",0,"연두");
        addItem(list_row2,getDrawable(R.drawable.ic_purple),
                "보라",0,"보라");
        addItem(list_row2,getDrawable(R.drawable.ic_bluegreen),
                "청록",0,"청록");
        addItem(list_row2,getDrawable(R.drawable.ic_blue),
                "파랑",0,"파랑");
        addItem(list_row2,getDrawable(R.drawable.ic_navy),
                "남색",0,"남색");
        addItem(list_row2,getDrawable(R.drawable.ic_redviolet),
                "자주",0,"자주");
        addItem(list_row2,getDrawable(R.drawable.ic_gray),
                "회색",0,"회색");
        addItem(list_row2,getDrawable(R.drawable.ic_black),
                "검정",0,"검정");









        //list_row3 - 제형
        addItem(list_row3,null,
                "제형\n전체",1,"제형전체");
        addItem(list_row3,getDrawable(R.drawable.ic_ref),
                "정제류",0,"정제류");
        addItem(list_row3,getDrawable(R.drawable.ic_hard_cap),
                "경질캡슐",0,"경질캡슐");
        addItem(list_row3,getDrawable(R.drawable.ic_soft_cap),
                "연질캡슐",0,"연질캡슐");

        //list_row4 - 분할선
        addItem(list_row4,null,
                "분할선전체",1,"분할선전체");
        addItem(list_row4,getDrawable(R.drawable.ic_empty),
                "없음",0,"없음");
        addItem(list_row4,getDrawable(R.drawable.ic_minus),
                "(-)형",0,"-");
        addItem(list_row4,getDrawable(R.drawable.ic_line_plus),
                "(+)형",0,"+");
        addItem(list_row4,getDrawable(R.drawable.ic_line_etc),
                "기타",0,"기타");

        mSelectedItems1.put(0,true);
        mSelectedItems2.put(0,true);
        mSelectedItems3.put(0,true);
        mSelectedItems4.put(0,true);
        for(int i =1 ; i < 12; i++) mSelectedItems1.put(i,false);
        for(int i =1 ; i < 15; i++) mSelectedItems2.put(i,false);
        for(int i =1 ; i < 4; i++) mSelectedItems3.put(i,false);
        for(int i =1 ; i < 5; i++) mSelectedItems4.put(i,false);

        Lst_shape.getItemAnimator().setChangeDuration(0);
        Lst_color.getItemAnimator().setChangeDuration(0);
        Lst_dosageForm.getItemAnimator().setChangeDuration(0);
        Lst_line.getItemAnimator().setChangeDuration(0);
        adapter_row3.notifyDataSetChanged();



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchResultItem = new ArrayList<MedicineItem>();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *
                 * 서버
                 *
                 */
            }
        });


    }
    public void addItem(ArrayList<MedicineItem> list, Drawable icon, String n, int v, String t) {
        MedicineItem item = new MedicineItem();
        if((icon!=null)){
            item.setIcon(icon);
        }
        item.setName(n);
        item.setViewType(v);
        item.setText(t);
        list.add(item);
    }
    @Override
    public void onItemClick(View v, int position, RecyclerView rList) {


        MedicineItem item;
        switch(rList.getId()){

            case R.id.Lst_shape : {

                item= list_row1.get(position) ;
                checkSelectedItem(position,mSelectedItems1, item, shape1, list_row1);
                break;
            }
            case R.id.Lst_color : {
                item= list_row2.get(position);
                checkSelectedItem(position,mSelectedItems2, item, color1, list_row2);
                break;
            }
            case R.id.Lst_dosageForm : {
                item= list_row3.get(position) ;
                checkSelectedItem(position,mSelectedItems3, item, formula1, list_row3);
                break;
            }
            case R.id.Lst_line : {
                item= list_row4.get(position) ;
                checkSelectedItem(position,mSelectedItems4, item, line1, list_row4);
                break;
            }
        }
    }

    void checkSelectedItem(int position, SparseBooleanArray mSelectedItems, MedicineItem item,  ArrayList<String> type, ArrayList<MedicineItem> list_row){

        if (mSelectedItems.get(position, false) ){
            mSelectedItems.put(position, false);
        }
        else {
            mSelectedItems.put(position, true);
            if(item.getViewType()==1){
                for (int i = 1; i < mSelectedItems.size(); i++)
                    mSelectedItems.put(i, false);
            }
            else{
                mSelectedItems.put(0,false);
            }

        }
        for(int i=0; i <mSelectedItems.size(); i++){
            if(i==0)type.clear();
            item = list_row.get(i);
            if(mSelectedItems.get(i)){
                type.add(item.getText());
            }
        }
    }
    void resizeTableSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int mHeight = displayMetrics.heightPixels/3-5;
        LinearLayout Layout_table= findViewById(R.id.Layout_table);
        ViewGroup.LayoutParams lay = (ViewGroup.LayoutParams) Layout_table.getLayoutParams();
        lay.height =  mHeight;
        Layout_table.setLayoutParams(lay);
    }

}