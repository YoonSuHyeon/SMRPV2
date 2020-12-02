package com.example.smrpv2.ui.medicine.medshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smrpv2.R;

import com.example.smrpv2.ml.ShapeModelVer11;
import com.example.smrpv2.model.common.KaKaoResult;
import com.example.smrpv2.model.common.KakaoDto;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.DequantizeOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    Call<KakaoDto> call;
    StringBuilder ocr_result = new StringBuilder();

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
            sendKakaoOcr(frontfile, backfile, true); // 카카오 ocr 서버 전송(앞면 뒷면 사진)


            sendFile(frontfile, backfile);
            useModel(targetBitmap_front);
        } catch (Exception err) {
            err.printStackTrace();
        }


        //  Uploading_bitmap_front(targetBitmap_front);

    }


    private void useModel(Bitmap targetBitmap_front) { // 이미지 모델추출
        try {
            ImageProcessor imageProcessor =
                    new ImageProcessor.Builder()
                            .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                            .build();

            ShapeModelVer11 model = ShapeModelVer11.newInstance(this);


            // Creates inputs for reference.

            //Drawable drawable = getDrawable(R.drawable.image8);

            // Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            ByteBuffer buffer = ByteBuffer.allocate(targetBitmap_front.getByteCount()); //바이트 버퍼를 이미지 사이즈 만큼 선언

            targetBitmap_front.copyPixelsToBuffer(buffer);//비트맵의 픽셀을 버퍼에 저장

            //
            TensorBuffer inputI = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


            TensorImage tImage = new TensorImage(DataType.FLOAT32);


            tImage.load(targetBitmap_front);
            tImage = imageProcessor.process(tImage);


            TensorProcessor probabilityProcessor =
                    new TensorProcessor.Builder().add(new DequantizeOp(0, 1 / 255.0f)).build();


            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


            inputFeature0.loadBuffer(tImage.getBuffer());
            TensorBuffer dequantizedBuffer = probabilityProcessor.process(inputFeature0);


            // Runs model inference and gets result.
            ShapeModelVer11.Outputs outputs = model.process(dequantizedBuffer);
            TensorBuffer probabilityBuffer =
                    TensorBuffer.createFixedSize(new int[]{1, 20}, DataType.FLOAT32);
            probabilityBuffer = outputs.getOutputFeature0AsTensorBuffer();

            @NonNull float[] floatArray = probabilityBuffer.getFloatArray();

            float tempA = 0;
            int idxA = 0;

            float tempB = 0;
            int idxB = 0;

            float tempC = 0;
            int idxC = 0;
            for (int i = 0; i < floatArray.length; i++) {
                if (floatArray[i] > tempA) {
                    tempA = floatArray[i];
                    idxA = i;
                } else if (floatArray[i] > tempB) {
                    tempB = floatArray[i];
                    idxB = i;
                } else if (floatArray[i] > tempC) {
                    tempC = floatArray[i];
                    idxC = i;
                }
            }

            Log.d("idx처음 :", idxA + "   " + tempA);
            final String ASSOCIATED_AXIS_LABELS = "label.txt";
            List<String> associatedAxisLabels = null;

            try {
                associatedAxisLabels = FileUtil.loadLabels(this, ASSOCIATED_AXIS_LABELS);
            } catch (IOException e) {
                Log.e("tfliteSupport", "Error reading label file", e);
            }

            String ttt = associatedAxisLabels.get(idxA) + tempA + associatedAxisLabels.get(idxB) + tempB + associatedAxisLabels.get(idxC) + tempC;
            textView.setText(ttt);


            Log.d("gg:", associatedAxisLabels.get(idxA));
            // Releases model resources if no longer used.


            TensorProcessor probabilityProcessor1 =
                    new TensorProcessor.Builder().add(new NormalizeOp(0, 20)).build();

            if (null != associatedAxisLabels) {
                // Map of labels and their corresponding probability
                TensorLabel labels = new TensorLabel(associatedAxisLabels,
                        probabilityProcessor1.process(probabilityBuffer));

                // Create a map to access the result based on label
                Map<String, Float> floatMap = labels.getMapWithFloatValue();

            }
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    private void sendKakaoOcr(File frontImage, File backImage, boolean status) {
        //int count = flag;
        if (status) {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), frontImage);
            ///storage/emulated/0/Android/data/com.example.smrpv2/files/picF.jpg
            MultipartBody.Part fPart = MultipartBody.Part.createFormData("image", "front.jpg", body);
            call = RetrofitHelper.getKaKaoOcr().create(RetrofitService_Server.class).sendKakaoOcr(fPart);
        } else {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), backImage);
            MultipartBody.Part fPart = MultipartBody.Part.createFormData("image", "back.jpg", body);
            call = RetrofitHelper.getKaKaoOcr().create(RetrofitService_Server.class).sendKakaoOcr(fPart);


            call.enqueue(new Callback<KakaoDto>() {
                @Override
                public void onResponse(Call<KakaoDto> call, Response<KakaoDto> response) {

                    for (int i = 0; i < response.body().getResult().size(); i++) {
                        KaKaoResult kaKaoResult = response.body().getResult().get(i);
                        for (int j = 0; j < kaKaoResult.getRecognition_words().length; j++) {
                            Log.d("OCR", kaKaoResult.getRecognition_words()[j]);
                            ocr_result.append(kaKaoResult.getRecognition_words()[j]);
                        }

                    }

                    ocr_result.append("/");
                    if (status) {
                        sendKakaoOcr(frontImage, backImage, false);
                    }

                }

                @Override
                public void onFailure(Call<KakaoDto> call, Throwable t) {
                    Log.d("실패카카오", t.toString());
                }
            });
        }
    }

        private void sendFile (File frontfile, File backfile){ //구축서버에 이미지 파일 전송


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
        private void SaveBitmapToFileCache (Bitmap targetBitmap_front, String frontImgDate){

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


        public static Bitmap rotateImage (Bitmap source,float angle){
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        }
    }