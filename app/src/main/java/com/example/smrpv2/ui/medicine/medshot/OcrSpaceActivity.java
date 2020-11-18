package com.example.smrpv2.ui.medicine.medshot;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.searchMed_model.OcrSpaceDto;

import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcrSpaceActivity extends AppCompatActivity {
    private Bitmap targetBitmap_front,targetBitmap_back;
    private final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_ocr);

        String frontImg = getIntent().getStringExtra("frontImg");
        String backImg =getIntent().getStringExtra("backImg");
        assert frontImg != null;
        Log.d("gggg",frontImg);
        assert backImg != null;
        Log.d("gggg",backImg);
        String frontImgDate=backImg.substring(0,frontImg.lastIndexOf("/"))+"/picF.jpg"; //앞면이미지
        String backImgDate=backImg.substring(0,backImg.lastIndexOf("/"))+"/picB.jpg"; //뒷면이미지
        Bitmap rotatedBitmap = null;
        ImageView im1,im2;



        im1 = findViewById(R.id.image_front);
        im2 = findViewById(R.id.image_back);
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


            targetBitmap_front=Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-250,rotatedBitmap.getHeight()/2-250,500,500);
            im1.setImageBitmap(targetBitmap_front);
            im2.setImageBitmap(targetBitmap_back);

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
            Uploading_bitmap_front(frontfile);
        }catch(Exception err){
            err.printStackTrace();
        }


        //targetBitmap_back = Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-250,rotatedBitmap.getHeight()/2-250,500,500);


    }
    private void Uploading_bitmap_front(File file){
        
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part fPart = MultipartBody.Part.createFormData("files","front.jpg",body);
        RetrofitService_Server retrofit = RetrofitHelper.getOcr().create(RetrofitService_Server.class);

        Call<OcrSpaceDto> call = retrofit.sendOcr(fPart,"kor");


        call.enqueue(new Callback<OcrSpaceDto>() {
            @Override
            public void onResponse(Call<OcrSpaceDto> call, Response<OcrSpaceDto> response) {

                if(response.body().getParsedResults().get(0).getParsedText() != null)
                    Log.d(TAG, "onResponse: "+response.body().getParsedResults().get(0).getParsedText());
                else
                    Toast.makeText(getApplicationContext(),"인식 불가",Toast.LENGTH_SHORT);
                

            }

            @Override
            public void onFailure(Call<OcrSpaceDto> call, Throwable t) {
                Log.d(TAG, "onFailureonFailure: ");
                Toast.makeText(getApplicationContext(),"OCR 서버 통신 오류",Toast.LENGTH_SHORT);
            }
        });



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
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}