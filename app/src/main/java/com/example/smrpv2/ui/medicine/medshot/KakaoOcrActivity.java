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

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.searchMed_model.KakaoOcrDto;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KakaoOcrActivity extends AppCompatActivity {
    private Bitmap targetBitmap_front,targetBitmap_back;
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

        }catch(Exception err){
            err.printStackTrace();
        }


        targetBitmap_back = Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-250,rotatedBitmap.getHeight()/2-250,500,500);

        Uploading_bitmap_front(targetBitmap_front);
    }
    private void Uploading_bitmap_front(Bitmap image){

        File file = new File(getCacheDir(),"front.jpg");
        try {

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }catch (IOException e2){
            e2.printStackTrace();
        }

        RetrofitService_Server retroft = RetrofitHelper.getOcr().create(RetrofitService_Server.class);
        Call<KakaoOcrDto> call = retroft.sendOcr(file);
        call.enqueue(new Callback<KakaoOcrDto>() {
            @Override
            public void onResponse(Call<KakaoOcrDto> call, Response<KakaoOcrDto> response) {
        //        Log.d("TAG", "onResponse0: "+response.body());
//                Log.d("TAG", "onResponse1: "+response.body().getList().size());
       //         Log.d("TAG", "onResponse2: "+response.body().getList().get(0));
            }

            @Override
            public void onFailure(Call<KakaoOcrDto> call, Throwable t) {

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