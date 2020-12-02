package com.example.smrpv2.ui.medicine.medshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.Message;

import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * (수정 예정)
 * 모양, 색, 분할선의 RecyclerView를 따로 실행시킬 시 제대로 잘 출력 됨
 * 하지만 밑에 SendFile 및 bit map 관련 코드와 같이 실행 시 RecyclerView가 안뜸.
 * 그리고 KaKaoOCRActivity 중에 EditText와 관련된 것은 임시로 주석 처리 했음
 * (수정 예정)
 */


public class OcrSpaceActivity extends AppCompatActivity implements MedicineResultRecyclerAdapter.OnItemClickListener {
    private Bitmap targetBitmap_front,targetBitmap_back;
    private final String TAG = "TAG";
    String total_result="",frontImgDate="";//검색결과
    ImageView im1,im2;


    ArrayList<String> shape1 = new ArrayList<String>();
    ArrayList<String> front_dividing_line1 = new ArrayList<String>();
    ArrayList<String> back_dividing_line1 = new ArrayList<String>();
    ArrayList<String> color1 = new ArrayList<String>();
    Button Btn_confirm;

    RecyclerView Lst_shape= null ;
    RecyclerView Lst_color= null ;
    RecyclerView Lst_front_dividing_line = null ;
    RecyclerView Lst_back_dividing_line = null ;
    ImageView iv_back;

    MedicineResultRecyclerAdapter adapter_row1 = null ;
    MedicineResultRecyclerAdapter adapter_row2  = null ;
    MedicineResultRecyclerAdapter adapter_row3  = null ;
    MedicineResultRecyclerAdapter adapter_row4 = null ;

    ArrayList<MedicineItem> list_row1 = new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row2= new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row3 = new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row4 = new ArrayList<MedicineItem>();


    private SparseBooleanArray mSelectedItems1 = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItems2 = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItems3 = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedItems4 = new SparseBooleanArray(0);

    private int[] row_images1 = {R.drawable.ic_circle_green,R.drawable.ic_triangle_green, R.drawable.ic_rectangle_green,R.drawable.ic_rhombus_green, R.drawable.ic_oblong_green,R.drawable.ic_oval_green,R.drawable.ic_semicircle_green,R.drawable.ic_pentagon_green,R.drawable.ic_hexagon_green,R.drawable.ic_octagon_green,R.drawable.ic_etc_green};
    private int[] row_images2 = {R.drawable.ic_white_green,R.drawable.ic_yellow_green,R.drawable.ic_orange_green,R.drawable.ic_pink_green,R.drawable.ic_red_green,R.drawable.ic_brown_green,R.drawable.ic_yellowgreen_green, R.drawable.ic_purple_green,R.drawable.ic_bluegreen_green,R.drawable.ic_blue_green,R.drawable.ic_navy_green,R.drawable.ic_redviolet_green,R.drawable.ic_gray_green,R.drawable.ic_black_green};
    private int[] row_images3 = {R.drawable.ic_empty_green,R.drawable.ic_line_minus_green,R.drawable.ic_line_pluse_green, R.drawable.ic_line_etc_green};
    //@@@@@@@@@@@@@@@




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_ocr);


        Lst_shape = findViewById(R.id.Lst_shape);
        Lst_color = findViewById(R.id.Lst_color);
        Lst_front_dividing_line = findViewById(R.id.Lst_front_dividing_line);
        Lst_back_dividing_line = findViewById(R.id.Lst_back_dividing_line);

        Btn_confirm = findViewById(R.id.btn_confirm);
        iv_back = findViewById(R.id.iv_back);

        shape1.add("원형");
        color1.add("흰색");
        front_dividing_line1.add("민무늬");
        back_dividing_line1.add("민무늬");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        adapter_row1  = new MedicineResultRecyclerAdapter(list_row1,this, Lst_shape,11, row_images1) ;
        adapter_row2  = new MedicineResultRecyclerAdapter(list_row2,this, Lst_color,14,row_images2) ;
        adapter_row3  = new MedicineResultRecyclerAdapter(list_row3,this, Lst_front_dividing_line,4,row_images3) ;
        adapter_row4  = new MedicineResultRecyclerAdapter(list_row4,this, Lst_back_dividing_line,4,row_images3) ;
        addList();


        Lst_shape .setLayoutManager(layoutManager ) ;
        Lst_shape .setAdapter(adapter_row1);

        Lst_color .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;
        Lst_color .setAdapter(adapter_row2);

        Lst_front_dividing_line.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;
        Lst_front_dividing_line.setAdapter(adapter_row3);

        Lst_back_dividing_line.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;
        Lst_back_dividing_line.setAdapter(adapter_row4);
        //초기화 작업..


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkItem())
                    ;
            }
        });







        String frontImg = getIntent().getStringExtra("frontImg");
        String backImg =getIntent().getStringExtra("backImg");
        assert frontImg != null;
        Log.d("gggg",frontImg);
        assert backImg != null;
        Log.d("gggg",backImg);
        frontImgDate=backImg.substring(0,frontImg.lastIndexOf("/"))+"/picF.jpg"; //앞면이미지
        String backImgDate=backImg.substring(0,backImg.lastIndexOf("/"))+"/picB.jpg"; //뒷면이미지

        Bitmap rotatedBitmap = null;

       


       im1 = findViewById(R.id.img_front);
       im2 = findViewById(R.id.img_back);
        try {
            File frontfile = new File(frontImg);
            File backfile = new File(backImg);

            Bitmap frontbitmap = MediaStore.Images.Media
                    .getBitmap(getContentResolver(), Uri.fromFile(frontfile));

            Bitmap backbitmap = MediaStore.Images.Media
                    .getBitmap(getContentResolver(), Uri.fromFile(backfile));

            if (frontbitmap != null) {
                ExifInterface ei = new ExifInterface(frontImg);
                int front_orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                switch (front_orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(frontbitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(frontbitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(frontbitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = frontbitmap;
                }

            }


            targetBitmap_front=Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-112,rotatedBitmap.getHeight()/2-112,224,224);
            //im1.setImageBitmap(targetBitmap_front);
            im2.setImageBitmap(targetBitmap_back);

            SavePicture(targetBitmap_front,frontImgDate);

            if (backbitmap != null) {
                ExifInterface ei = new ExifInterface(backImg);
                int back_orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                switch (back_orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(backbitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(backbitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(backbitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = backbitmap;
                }

            }
            //sendFile(frontfile,backfile);

        }catch(Exception err){
            err.printStackTrace();
        }


        //targetBitmap_back = Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-250,rotatedBitmap.getHeight()/2-250,500,500);


    }
    private void Uploading_bitmap_front(File file){

        //File file1 = new File(frontImgDate);
        try {
            Bitmap bitmap = MediaStore.Images.Media
                    .getBitmap(getContentResolver(), Uri.fromFile(file));
            im1.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part fPart = MultipartBody.Part.createFormData("files","picF.jpg",body);
        RetrofitService_Server retrofit = RetrofitHelper.getOcr().create(RetrofitService_Server.class);

       /* Call<OcrSpaceDto> eng_call = retrofit.sendOcr(fPart,"eng");
        Call<OcrSpaceDto> kor_call = retrofit.sendOcr(fPart,"kor");


        eng_call.enqueue(new Callback<OcrSpaceDto>() {
            @Override
            public void onResponse(Call<OcrSpaceDto> call, Response<OcrSpaceDto> response) {

                if(response.body().getParsedResults().get(0).getParsedText() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getParsedResults().get(0).getParsedText());
                    total_result += response.body().getParsedResults().get(0).getParsedText();
                    Log.d(TAG, "total_result: "+total_result);
                }
                else
                    Toast.makeText(getApplicationContext(),"영어 인식 불가",Toast.LENGTH_SHORT);


            }

            @Override
            public void onFailure(Call<OcrSpaceDto> call, Throwable t) {
                Log.d(TAG, "onFailureonFailure1: ");
                Toast.makeText(getApplicationContext(),"OCR 서버 통신 오류",Toast.LENGTH_SHORT);
            }
        });

        kor_call.enqueue(new Callback<OcrSpaceDto>() {
            @Override
            public void onResponse(Call<OcrSpaceDto> call, Response<OcrSpaceDto> response) {
                if(response.body().getParsedResults().get(0).getParsedText() != null) {
                    Log.d(TAG, "onResponse2: " + response.body().getParsedResults().get(0).getParsedText());
                    total_result += response.body().getParsedResults().get(0).getParsedText();
                    Log.d(TAG, "total_result: "+total_result);
                }
                else
                    Toast.makeText(getApplicationContext(),"한글 인식 불가",Toast.LENGTH_SHORT);

            }

            @Override
            public void onFailure(Call<OcrSpaceDto> call, Throwable t) {
                Log.d(TAG, "onFailureonFailure2: ");
            }
        });*/


    }

    private void sendFile(File frontfile,File backfile){ //구축서버에 이미지 파일 전송

        ArrayList<MultipartBody.Part> list = new ArrayList<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),frontfile);
        RequestBody body2 = RequestBody.create(MediaType.parse("image/*"),backfile);
        MultipartBody.Part fPart = MultipartBody.Part.createFormData("files","front.jpg",body);
        MultipartBody.Part bPart = MultipartBody.Part.createFormData("files","back.jpg",body2);
        list.add(fPart);
        list.add(bPart);

        Call<Message> call = RetrofitHelper.getRetrofitService_server().uploadImage(list);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.d("sendFile", "성공");
                Log.d("sendFile", response.toString());


            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("sendFile T:", t.toString());
            }
        });
    }
    private void SavePicture(Bitmap bitmap, String path){
        File file = new File(path);
        OutputStream out = null;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uploading_bitmap_front(file);

    }
    public static Bitmap rotateImage(Bitmap source, float angle) { //사진 변환
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    public void addItem(ArrayList<MedicineItem> list, Drawable icon, String n, int v, String t) {
        MedicineItem item = new MedicineItem();
        item.setIcon(icon);
        item.setName(n);
        item.setViewType(v);
        item.setText(t);
        list.add(item);
        Log.e("DD","5555");
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
            case R.id.line_front_dividing_line: {
                item= list_row3.get(position) ;
                checkSelectedItem(position,mSelectedItems3, item, front_dividing_line1, list_row3);
                break;
            }
            case R.id.line_back_dividing_line : {
                item= list_row4.get(position) ;
                checkSelectedItem(position,mSelectedItems4, item, back_dividing_line1, list_row4);
                break;
            }
        }
    }

    void checkSelectedItem(int position, SparseBooleanArray mSelectedItems, MedicineItem item, ArrayList<String> type, ArrayList<MedicineItem> list_row){

        if (mSelectedItems.get(position, false) ){
            mSelectedItems.put(position, false);
        }
        else {

           for (int i = 0; i < mSelectedItems.size(); i++)
               mSelectedItems.put(i, false);
            mSelectedItems.put(position, true);


        }
        for(int i=0; i <mSelectedItems.size(); i++){
            if(i==0)type.clear();
            item = list_row.get(i);
            if(mSelectedItems.get(i,false)){
                type.add(item.getText());
            }
        }
    }
    private void addList(){

        addItem(list_row1,getDrawable(R.drawable.ic_circle), "원형",5,"원형");
        addItem(list_row1,getDrawable(R.drawable.ic_triangle),
                "삼각형",5, "삼각형");
        addItem(list_row1,getDrawable(R.drawable.ic_rectangle),
                "사각형",5,"사각형");
        addItem(list_row1,getDrawable(R.drawable.ic_rhombus),
                "마름모",5,"마름모형");
        addItem(list_row1,getDrawable(R.drawable.ic_oblong),
                "장방형",5,"장방형");
        addItem(list_row1,getDrawable(R.drawable.ic_oval),
                "타원형",5,"타원형");
        addItem(list_row1,getDrawable(R.drawable.ic_semicircle),
                "반원형",5,"반원형");

        addItem(list_row1,getDrawable(R.drawable.ic_pentagon),
                "오각형",5,"오각형");

        addItem(list_row1,getDrawable(R.drawable.ic_hexagon),
                "육각형",5,"육각형");

        addItem(list_row1,getDrawable(R.drawable.ic_octagon),
                "팔각형",5,"팔각형");


        addItem(list_row1,getDrawable(R.drawable.ic_etc),
                "기타",5,"기타");

        //list_row2 - 색상
        addItem(list_row2,getDrawable(R.drawable.ic_white),
                "하양",5, "하양");
        addItem(list_row2,getDrawable(R.drawable.ic_yellow),
                "노랑",5,"노랑");
        addItem(list_row2,getDrawable(R.drawable.ic_orange),
                "주황",5,"주황");
        addItem(list_row2,getDrawable(R.drawable.ic_pink),
                "분홍",5,"분홍");
        addItem(list_row2,getDrawable(R.drawable.ic_red),
                "빨강",5,"빨강");
        addItem(list_row2,getDrawable(R.drawable.ic_brown),
                "갈색",5,"갈색");
        addItem(list_row2,getDrawable(R.drawable.ic_yellowgreen),
                "연두",5,"연두");
        addItem(list_row2,getDrawable(R.drawable.ic_purple),
                "보라",5,"보라");
        addItem(list_row2,getDrawable(R.drawable.ic_bluegreen),
                "청록",5,"청록");
        addItem(list_row2,getDrawable(R.drawable.ic_blue),
                "파랑",5,"파랑");
        addItem(list_row2,getDrawable(R.drawable.ic_navy),
                "남색",5,"남색");
        addItem(list_row2,getDrawable(R.drawable.ic_redviolet),
                "자주",5,"자주");
        addItem(list_row2,getDrawable(R.drawable.ic_gray),
                "회색",5,"회색");
        addItem(list_row2,getDrawable(R.drawable.ic_black),
                "검정",5,"검정");

        //list_row3 - 앞 분할선

        addItem(list_row3,getDrawable(R.drawable.ic_empty),
                "민무늬",5,"민무늬");
        addItem(list_row3,getDrawable(R.drawable.ic_minus),
                "(-)형",5,"-");
        addItem(list_row3,getDrawable(R.drawable.ic_line_plus),
                "(+)형",5,"+");
        addItem(list_row3,getDrawable(R.drawable.ic_line_etc),
                "문구",5,"문구");


        //list_row4 - 뒷 분할선
        addItem(list_row4,getDrawable(R.drawable.ic_empty),
                "민무늬",5,"민무늬");
        addItem(list_row4,getDrawable(R.drawable.ic_minus),
                "(-)형",5,"-");
        addItem(list_row4,getDrawable(R.drawable.ic_line_plus),
                "(+)형",5,"+");
        addItem(list_row4,getDrawable(R.drawable.ic_line_etc),
                "문구",5,"문구");

        mSelectedItems1.put(0,true);
        mSelectedItems2.put(0,true);
        mSelectedItems3.put(0,true);
        mSelectedItems4.put(0,true);
        for(int i =1 ; i < 11; i++) mSelectedItems1.put(i,false);
        for(int i =1 ; i < 14; i++) mSelectedItems2.put(i,false);
        for(int i =1 ; i < 4; i++) mSelectedItems3.put(i,false);
        for(int i =1 ; i < 4; i++) mSelectedItems4.put(i,false);

        Lst_shape.getItemAnimator().setChangeDuration(0);
        Lst_color.getItemAnimator().setChangeDuration(0);
        Lst_front_dividing_line.getItemAnimator().setChangeDuration(0);
        Lst_back_dividing_line.getItemAnimator().setChangeDuration(0);
        adapter_row1.notifyDataSetChanged();
        adapter_row2.notifyDataSetChanged();
        adapter_row3.notifyDataSetChanged();
        adapter_row4.notifyDataSetChanged();
    }
    private boolean checkItem(){
        if(shape1.isEmpty()) {
            Toast.makeText(this,"모양을 선택해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(color1.isEmpty()){
            Toast.makeText(this,"색상을 선택해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(front_dividing_line1.isEmpty()){
            Toast.makeText(this,"앞 분할선을 선택해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(back_dividing_line1.isEmpty()){
            Toast.makeText(this,"뒷 분할선을 선택해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }
}