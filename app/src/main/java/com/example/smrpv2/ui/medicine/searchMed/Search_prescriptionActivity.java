package com.example.smrpv2.ui.medicine.searchMed;

import android.app.ProgressDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smrpv2.R;
import com.example.smrpv2.model.common.KaKaoResult;
import com.example.smrpv2.model.common.KakaoDto;
import com.example.smrpv2.model.Message;

import com.example.smrpv2.model.medicine_model.Prescriptionitem;
import com.example.smrpv2.model.prescription_model.RegMedicineList;

import com.example.smrpv2.model.searchMed_model.MedicineInfoRsponDTO;
import com.example.smrpv2.model.user_model.UserInform;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;



import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * 처방전 & 약 봉투 사진 찍고 그 결과 알려주는 Activity
 *
 */
public class Search_prescriptionActivity extends AppCompatActivity implements Serializable {
    private static final int MAX_DIMENSION = 1080;//1080 //이미지 크기
    private String imageFilePath,imageFileName; //저장된 사진경로 및 저장된 사진 이름
    private ArrayList<Prescriptionitem> list; //구축 서버로 부터 받은 의약품 리스트
    private ArrayList<String> itemseq_list;
    private Button Btn_add;
    private ImageView iv_back;


    private File photoFile;
    private Uri photoUri;
    private HashMap<Integer, String> select_pill_list; //사용자 선택한 약 정보를 담는 hashmap
    private PrescriptionAdapter prescriptionAdapter;
    private RecyclerView recyclerView;


    private boolean bool_end = false;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pill);

        initView();

        Btn_add.setOnClickListener(new View.OnClickListener() { //추가하기 버튼 누를시
            @Override
            public void onClick(View v) { //추가하기
                if(select_pill_list.size()==0){ //선택한 약이 없을 경우
                    Toast.makeText(getApplicationContext(),"약을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    for(Map.Entry<Integer, String>elem : select_pill_list.entrySet())
                        itemseq_list.add(elem.getValue());
                    /**
                     *
                     * 서버 : 검색된 약 추가하기
                     */

                    RegMedicineList regMedicineList = new RegMedicineList(UserInform.getUserId(),itemseq_list);
                    Log.d("TAG", "itemseq_list.size: "+itemseq_list.size());
                    Call<Message> call = RetrofitHelper.getRetrofitService_server().medicineListAdd(regMedicineList);
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Log.d("onResponse", "onResponse: "+response.body().getResultCode());
                            if(response.body().getResultCode().equals("OK")){
                                //정상적으로 반영
                                Toast.makeText(getApplicationContext(), itemseq_list.size()+"건을 등록하였습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {

                        }
                    });
                }


            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });//뒤로가기

        prescriptionAdapter.setOnClickListener(new PrescriptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrescriptionAdapter.ViewHolder holder, View v, int position) { //리스트 이벤트 처리

                if(select_pill_list.size()==0){//사용자가 추가한 약 리스트가 없는 경우
                    select_pill_list.put(position,list.get(position).getItemSeq());

                }else{
                    if(select_pill_list.get(position)==null){ //선택한 약이 hashmap에 없을경우
                        select_pill_list.put(position,list.get(position).getItemSeq());
                    }else{
                        select_pill_list.remove(position);
                    }
                }

            }

        });




        sendTakePhotoIntent();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 672 && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            Log.d("exifDegree", "exifDegree: "+exifDegree);
            Bitmap temp_bitmap = rotate(bitmap, exifDegree);
            temp_bitmap = scaleBitmapDown(temp_bitmap, MAX_DIMENSION);
            callKakaoVision(temp_bitmap);
        }else if(resultCode==RESULT_OK){ //팝업창 종료시
            finish();
        }else
            onBackPressed();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int exifOrientationToDegrees(int exifOrientation) { //회전된 사진을 정상으로 전환하기 위한 각도를 반환
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("TAG", "createImageFile: ");
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        Log.d("getAbsolutePath", "image.getAbsolutePath(): "+image.getAbsolutePath());
        //D/getAbsolutePath: image.getAbsolutePath(): /storage/emulated/0/Android/data/com.example.smrpv2/files/Pictures/TEST_20201121_215600_4975616491941498734.jpg
        imageFilePath =  image.getAbsolutePath();
        return image;
    }
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 내장 카메라 켜기
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;

            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                    Log.d("TAG", "photoUri: "+photoUri);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, 672);
                }
            } catch (IOException ex) {
                Log.d("TAG","이미지 파일 생성 오류");
            }
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) { //카카오에서 승인한 2048 픽셀 내에 이미지 크기를 만들기위한 메소드

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;
        if (originalHeight > originalWidth) { // 촬영한 사진의 세로길이가 너비보다 크면
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //내장 카메라 접근 권한 확인
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

            }
        }
    } //퍼미션 접근 권한

    private void callKakaoVision(Bitmap bitmap){//카카오 ocr 통신
        File file = new File(imageFilePath);
        Log.d("TAG", "callKakaoVision: "+file.getAbsolutePath());
        Log.d("TAG", "imageFilePath: "+imageFilePath);
        Log.d("TAG", "imageFileName: "+imageFileName);
        String filename = imageFilePath.substring(imageFilePath.lastIndexOf('/')+1);
        Log.d("TAG", "filename: "+filename);
        /*callKakaoVision: /storage/emulated/0/Android/data/com.example.smrpv2/files/Pictures/TEST_20201121_221858_1761386531960783592.jpg
         imageFilePath: /storage/emulated/0/Android/data/com.example.smrpv2/files/Pictures/TEST_20201121_221858_1761386531960783592.jpg
         imageFileName: TEST_20201121_221858_
         filename: TEST_20201121_221858_1761386531960783592.jpg*/
        OutputStream os = null;

        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,os);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part Part = MultipartBody.Part.createFormData("image", filename, body);


        Call<KakaoDto> call = RetrofitHelper.getKaKaoOcr().create(RetrofitService_Server.class).sendKakaoOcr(Part);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("분석중입니다.\n잠시만 기다려 주십시오.");
        progressDialog.show();
        call.enqueue(new Callback<KakaoDto>() {
            @Override
            public void onResponse(Call<KakaoDto> call, Response<KakaoDto> response) {
                assert response.body() != null;
                String[] word_array = new String[response.body().getResult().size()];
                int count = 0;
                if(response.body()!=null){
                    for (int i = 0; i <response.body().getResult().size() ; i++) {
                        KaKaoResult kaKaoResult = response.body().getResult().get(i);
                        for( int j = 0 ; j <kaKaoResult.getRecognition_words().length;j++){
                            word_array[count++] = kaKaoResult.getRecognition_words()[j];
                        }

                    }
                    SendOcrdata(word_array);
                }else{ //인식된 글자가 없을경우
                    Toast.makeText(getApplicationContext(),"인식된 문자가 없습니다. 이전 페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }



            }

            @Override
            public void onFailure(Call<KakaoDto> call, Throwable t) {
                Log.d("실패카카오", t.toString());
            }
        });
    }

    private void SendOcrdata(String[] array){


        Call<ArrayList<MedicineInfoRsponDTO>> call = RetrofitHelper.getRetrofitService_server().sendWords(array);
        call.enqueue(new Callback<ArrayList<MedicineInfoRsponDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<MedicineInfoRsponDTO>> call, Response<ArrayList<MedicineInfoRsponDTO>> response) {

                for(int i = 0 ; i < response.body().size();i++){
                    MedicineInfoRsponDTO dto = response.body().get(i);
                    list.add(new Prescriptionitem(dto.getItemSeq(),dto.getItemImage(),dto.getItemName(),dto.getEntpName(),dto.getEtcOtcName()));//약 식별번호 / 약 이미지 / 약 이름 / 약 제조사 / 약 포장 /약 의약품정보(일반, 전문)
                    prescriptionAdapter.notifyDataSetChanged();
                }
                LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mlinearLayoutManager.getOrientation());//구분선을 넣기 위함
                recyclerView.addItemDecoration(dividerItemDecoration);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<MedicineInfoRsponDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"분석 결과가 없습니다. 이전 페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private class Dialog extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1 = new ProgressDialog(Search_prescriptionActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.setMessage("로딩중입니다..");

            progressDialog1.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            while(!bool_end)
                ;
            bool_end = false;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog1.dismiss();
            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    private void initView(){
        Btn_add = findViewById(R.id.add_btn);
        iv_back = findViewById(R.id.iv_back);
        select_pill_list = new HashMap<Integer, String>();
        itemseq_list = new ArrayList<String>();
        list = new ArrayList<Prescriptionitem>();
        prescriptionAdapter = new PrescriptionAdapter(list);
        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(prescriptionAdapter);
        progressDialog = new ProgressDialog(this);
    }
}