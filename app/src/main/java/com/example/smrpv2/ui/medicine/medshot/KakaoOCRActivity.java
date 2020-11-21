package com.example.smrpv2.ui.medicine.medshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smrpv2.R;
import com.example.smrpv2.model.KaKaoResult;
import com.example.smrpv2.model.KakaoDto;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.pharmcy_model.Header;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;

import java.io.File;
import java.io.FileNotFoundException;
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



    public class KakaoOCRActivity extends AppCompatActivity {
        private Bitmap targetBitmap_front, targetBitmap_back;
        Context context;
        TextView textView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_kakao_ocr);//activity_search_prescription

            String frontImg = getIntent().getStringExtra("frontImg");
            String backImg = getIntent().getStringExtra("backImg");
            assert frontImg != null;
            Log.d("gggg", frontImg);
            assert backImg != null;
            Log.d("gggg", backImg);
            String frontImgDate = backImg.substring(0, frontImg.lastIndexOf("/")) + "/picF.jpg"; //앞면이미지
            String backImgDate = backImg.substring(0, backImg.lastIndexOf("/")) + "/picB.jpg"; //뒷면이미지

            Log.d("TAG", "frontImg: " + frontImgDate);
            Log.d("TAG", "backImg: " + backImgDate);
          /*  D/gggg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picF.jpg
            D/gggg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picB.jpg
            D/TAG: frontImg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picF.jpg
            backImg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picB.jpg*/
            context = this;

            Bitmap rotatedBitmap = null;

            ImageView imageView = findViewById(R.id.image_front);

            textView = findViewById(R.id.textView);


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


                targetBitmap_front = Bitmap.createBitmap(rotatedBitmap, rotatedBitmap.getWidth() / 2 - 112, rotatedBitmap.getHeight() / 2 - 112, 224, 224);
                imageView.setImageBitmap(targetBitmap_front);


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
                targetBitmap_back = Bitmap.createBitmap(rotatedBitmap, rotatedBitmap.getWidth() / 2 - 112, rotatedBitmap.getHeight() / 2 - 112, 224, 224);
                Log.d("하기전", frontfile.toString());
                Log.d("하기전", backfile.toString());
                SaveBitmapToFileCache(targetBitmap_front, frontImgDate);
                SaveBitmapToFileCache(targetBitmap_back, backImgDate);
                Log.d("한후", frontfile.toString());
                Log.d("한후", backfile.toString());

                //카카오 OCR
                sendKakaoOcr(frontfile, backfile);
                sendFile(frontfile, backfile);

            } catch (Exception err) {
                err.printStackTrace();
            }


            //  Uploading_bitmap_front(targetBitmap_front);

        }

        private void sendKakaoOcr(File frontfile, File backfile) {
            Log.d("TAG", "frontfile.getAbsolutePath(): "+frontfile.getAbsolutePath());
            ///storage/emulated/0/Android/data/com.example.smrpv2/files/picF.jpg
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), frontfile);
            MultipartBody.Part fPart = MultipartBody.Part.createFormData("image", "front.jpg", body);


            Call<KakaoDto> call = RetrofitHelper.getKaKaoOcr().create(RetrofitService_Server.class).sendKakaoOcr(fPart);

            call.enqueue(new Callback<KakaoDto>() {
                @Override
                public void onResponse(Call<KakaoDto> call, Response<KakaoDto> response) {

                    for (int i = 0; i <response.body().getResult().size() ; i++) {
                        KaKaoResult kaKaoResult = response.body().getResult().get(i);
                        for( int j = 0 ; j <kaKaoResult.getRecognition_words().length;j++){
                            Log.d("OCR",kaKaoResult.getRecognition_words()[j]);
                        }

                    }


                }

                @Override
                public void onFailure(Call<KakaoDto> call, Throwable t) {
                    Log.d("실패카카오", t.toString());
                }
            });
        }

        private void sendFile(File frontfile,File backfile){ //구축서버에 이미지 파일 전송


            ArrayList<MultipartBody.Part> list = new ArrayList<>();
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), frontfile);
            RequestBody body2 = RequestBody.create(MediaType.parse("image/*"), backfile);
            MultipartBody.Part fPart = MultipartBody.Part.createFormData("files", "front.jpg", body);
            MultipartBody.Part bPart = MultipartBody.Part.createFormData("files", "back.jpg", body2);
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
        private void SaveBitmapToFileCache(Bitmap targetBitmap_front, String frontImgDate) {

            File fileCacheItem = new File(frontImgDate);

            OutputStream out = null;

            try {
                fileCacheItem.createNewFile();
                out = new FileOutputStream(fileCacheItem);
                targetBitmap_front.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
