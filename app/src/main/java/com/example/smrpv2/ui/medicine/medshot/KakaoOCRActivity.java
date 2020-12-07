package com.example.smrpv2.ui.medicine.medshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smrpv2.R;

import com.example.smrpv2.ml.ShapeModelVer11;

import com.example.smrpv2.ml.Splitline;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.common.KaKaoResult;
import com.example.smrpv2.model.common.KakaoDto;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_Server;
import com.example.smrpv2.ui.start.StartActivity;

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
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 수
 */
public class KakaoOCRActivity extends AppCompatActivity implements MedicineResultRecyclerAdapter.OnItemClickListener{
    private Bitmap targetBitmap_front, targetBitmap_back;
    Context context;

    Call<KakaoDto> call;
    StringBuilder ocr_result = new StringBuilder();
    private EditText frontEditText,backEditText;
    Button btn_confirm;
    ImageView img_front, img_back;

    ArrayList<String> shape1 = new ArrayList<String>();
    ArrayList<String> front_dividing_line1 = new ArrayList<String>();
    ArrayList<String> back_dividing_line1 = new ArrayList<String>();
    ArrayList<String> color1 = new ArrayList<String>();


    RecyclerView Lst_shape= null ;
    RecyclerView Lst_color= null ;
    RecyclerView Lst_front_dividing_line = null ;
    RecyclerView Lst_back_dividing_line = null ;
    ImageView iv_back;

    MedicineResultRecyclerAdapter adapter_row1 = null ; //색 어댑터
    MedicineResultRecyclerAdapter adapter_row2  = null ; // 모양 어댑터
    MedicineResultRecyclerAdapter adapter_row3  = null ; // 앞 분할선 어댑터
    MedicineResultRecyclerAdapter adapter_row4 = null ; //뒷 분할선 어댑터

    ArrayList<MedicineItem> list_row1 = new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row2= new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row3 = new ArrayList<MedicineItem>();
    ArrayList<MedicineItem> list_row4 = new ArrayList<MedicineItem>();


    private SparseBooleanArray mSelectedItems1 = new SparseBooleanArray(0); //모양
    private SparseBooleanArray mSelectedItems2 = new SparseBooleanArray(0); //색상
    private SparseBooleanArray mSelectedItems3 = new SparseBooleanArray(0); //앞면
    private SparseBooleanArray mSelectedItems4 = new SparseBooleanArray(0); //뒷면

    private int[] row_images1 = {R.drawable.ic_circle_green,R.drawable.ic_triangle_green, R.drawable.ic_rectangle_green,R.drawable.ic_rhombus_green, R.drawable.ic_oblong_green,R.drawable.ic_oval_green,R.drawable.ic_semicircle_green,R.drawable.ic_pentagon_green,R.drawable.ic_hexagon_green,R.drawable.ic_octagon_green,R.drawable.ic_etc_green};
    private int[] row_images2 = {R.drawable.ic_white_green,R.drawable.ic_yellow_green,R.drawable.ic_orange_green,R.drawable.ic_pink_green,R.drawable.ic_red_green,R.drawable.ic_brown_green,R.drawable.ic_yellowgreen_green, R.drawable.ic_purple_green,R.drawable.ic_bluegreen_green,R.drawable.ic_blue_green,R.drawable.ic_navy_green,R.drawable.ic_redviolet_green,R.drawable.ic_gray_green,R.drawable.ic_black_green};
    private int[] row_images3 = {R.drawable.ic_empty_green,R.drawable.ic_line_minus_green,R.drawable.ic_line_pluse_green, R.drawable.ic_line_etc_green};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_ocr);//activity_search_prescription

        String frontImg = getIntent().getStringExtra("frontImg");
        String backImg = getIntent().getStringExtra("backImg");

        int recWidth = getIntent().getIntExtra("recWidth", 0);
        int recHeight=getIntent().getIntExtra("recHeight", 0);
        int width=getIntent().getIntExtra("width",0);
        int height=getIntent().getIntExtra("height",0);



        assert frontImg != null;
        Log.d("gggg", frontImg);
        assert backImg != null;
        Log.d("gggg", backImg);
        String frontImgDate = backImg.substring(0, frontImg.lastIndexOf("/")) + "/picF.jpg"; //앞면이미지
        String backImgDate = backImg.substring(0, backImg.lastIndexOf("/")) + "/picB.jpg"; //뒷면이미지


        init();
        context = this;
        Log.d("TAG", "frontImg: " + frontImgDate);
        Log.d("TAG", "backImg: " + backImgDate);
          /*  D/gggg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picF.jpg
            D/gggg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picB.jpg
            D/TAG: frontImg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picF.jpg
            backImg: /storage/emulated/0/Android/data/com.example.smrpv2/files/picB.jpg*/





        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkItem()) {
                    Intent intent = new Intent(context, CameraResultActivity.class);

                    String frontText = frontEditText.getText().toString();
                    String backText = backEditText.getText().toString();
                    String color = "";
                    String shape = "";
                    String frontDividing = "";
                    String backDividing = "";
                    for (int i = 0; i < mSelectedItems1.size(); i++) {
                        if (mSelectedItems1.get(i) == true) {
                            shape = list_row1.get(i).getText();
                            Log.d("kakoActivity", "shape: " + shape);
                            break;
                        }
                    }
                    for (int i = 0; i < mSelectedItems2.size(); i++) {
                        if (mSelectedItems2.get(i) == true) {
                            color = list_row2.get(i).getText();
                            Log.d("kakoActivity", "color: " + color);
                            break;
                        }
                    }
                    for (int i = 0; i < mSelectedItems3.size(); i++) {
                        if (mSelectedItems3.get(i) == true) {
                            frontDividing = list_row3.get(i).getText();
                            Log.d("kakoActivity", "frontDividing: " + frontDividing);
                            break;
                        }
                    }
                    for (int i = 0; i < mSelectedItems4.size(); i++) {
                        if (mSelectedItems4.get(i) == true) {
                            backDividing = list_row4.get(i).getText();
                            Log.d("kakoActivity", "backDividing: " + backDividing);
                            break;
                        }
                    }
                /*String color = color1.get(0);
                String shape = shape1.get(0);
                String frontDividing = front_dividing_line1.get(0);
                String backDividing = back_dividing_line1.get(0);*/

                    intent.putExtra("frontText", frontText);
                    intent.putExtra("backText", backText);
                    intent.putExtra("color", color);
                    intent.putExtra("shape", shape);
                    intent.putExtra("frontDividing", frontDividing);
                    intent.putExtra("backDividing", backDividing);
                    Log.d("total", "total: "+frontText+","+backText+","+color+","+shape+","+frontDividing+","+backDividing);
                    startActivity(intent);
                }
            }
        });




        Bitmap rotatedBitmap = null;




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

            int tempWidth;
            int tempHeight;
            tempWidth = rotatedBitmap.getWidth() * recWidth / width;
            tempHeight = rotatedBitmap.getHeight() * recHeight / height;

            Log.d("KakaoOCRActivityre", (recWidth) + "");
            Log.d("KakaoOCRActivityrec", (recHeight) + "");

            Log.d("KakaoOCRActivitymm", rotatedBitmap.getWidth()+"");
            Log.d("KakaoOCRActivitymh", rotatedBitmap.getHeight() + "");

            Log.d("KakaoOCRActivitymm", rotatedBitmap.getWidth()/2-122+"");
            Log.d("KakaoOCRActivitymh", rotatedBitmap.getHeight()/2-122 + "");
            targetBitmap_front = Bitmap.createBitmap(rotatedBitmap,  tempWidth,tempHeight, 224, 224);



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
            targetBitmap_back = Bitmap.createBitmap(rotatedBitmap, tempWidth, tempHeight, 224, 224);
            Log.d("하기전", frontfile.toString());
            Log.d("하기전", backfile.toString());
            SaveBitmapToFileCache(targetBitmap_front, frontImgDate);
            SaveBitmapToFileCache(targetBitmap_back, backImgDate);
            Log.d("한후", frontfile.toString());
            Log.d("한후", backfile.toString());


            //카카오 OCR
            sendKakaoOcr(frontfile, backfile, true); // 카카오 ocr 서버 전송(앞면 뒷면 사진)


            sendFile(frontfile, backfile);
            img_front.setImageBitmap(targetBitmap_front);
            img_back.setImageBitmap(targetBitmap_back);

            useModel(targetBitmap_front);


            useDividingModel(targetBitmap_front,"앞");
            useDividingModel(targetBitmap_back,"뒤");
        } catch (Exception err) {
            err.printStackTrace();
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //  Uploading_bitmap_front(targetBitmap_front);

    }

    private void useDividingModel(Bitmap bitmap,String string) {
        try {
            ImageProcessor imageProcessor =
                    new ImageProcessor.Builder()
                            .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                            .build();

            Splitline model = Splitline.newInstance(this);


            // Creates inputs for reference.

            //Drawable drawable = getDrawable(R.drawable.image8);

            // Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount()); //바이트 버퍼를 이미지 사이즈 만큼 선언

            bitmap.copyPixelsToBuffer(buffer);//비트맵의 픽셀을 버퍼에 저장

            //
            TensorBuffer inputI = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


            TensorImage tImage = new TensorImage(DataType.FLOAT32);


            tImage.load(bitmap);
            tImage = imageProcessor.process(tImage);


            TensorProcessor probabilityProcessor =
                    new TensorProcessor.Builder().add(new DequantizeOp(0, 1 / 255.0f)).build();


            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


            inputFeature0.loadBuffer(tImage.getBuffer());
            TensorBuffer dequantizedBuffer = probabilityProcessor.process(inputFeature0);


            // Runs model inference and gets result.
            Splitline.Outputs outputs = model.process(dequantizedBuffer);
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
            final String ASSOCIATED_AXIS_LABELS = "split.txt";
            List<String> associatedAxisLabels = null;

            try {
                associatedAxisLabels = FileUtil.loadLabels(this, ASSOCIATED_AXIS_LABELS);
            } catch (IOException e) {
                Log.e("tfliteSupport", "Error reading label file", e);
            }

            String ttt = associatedAxisLabels.get(idxA) + tempA + associatedAxisLabels.get(idxB) + tempB + associatedAxisLabels.get(idxC) + tempC;


            String s = associatedAxisLabels.get(idxA);
        //    editText.setText(s);
            Log.d("gg:", associatedAxisLabels.get(idxA));
            // Releases model resources if no longer used.





            //분류작업
            String dividing = associatedAxisLabels.get(idxA);

            int i;

            for(i=0; i < list_row3.size();i++){
                if(list_row3.get(i).getText().contains(dividing))
                    break;
            }




            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);





        int k;


            if(string.contains("앞")){
                Log.d("ggg","앞"+i);
                adapter_row3  = new MedicineResultRecyclerAdapter(list_row3,this, Lst_front_dividing_line,4,row_images3,i) ;
                Lst_front_dividing_line.setLayoutManager(layoutManager) ;
                mSelectedItems3.put(i,true); //앞 모양
                for(k =0 ; k < 4; k++){
                    if(i!=k)
                        mSelectedItems3.put(k,false);
                }

                Lst_front_dividing_line.setAdapter(adapter_row3);
                Lst_front_dividing_line.getItemAnimator().setChangeDuration(0);
                Lst_front_dividing_line.getAdapter().notifyDataSetChanged();

            }else{
                Log.d("kkk","뒤"+i);
                adapter_row4  = new MedicineResultRecyclerAdapter(list_row4,this, Lst_back_dividing_line,4,row_images3,i) ;
                Lst_back_dividing_line.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;
                mSelectedItems4.put(i,true); //뒷 모암
                for(k =0 ; k < 4; k++){
                    if(i!=k)
                        mSelectedItems4.put(k,false);
                }

                Lst_back_dividing_line.setAdapter(adapter_row4);
                Lst_back_dividing_line.getItemAnimator().setChangeDuration(0);
                Lst_back_dividing_line.getAdapter().notifyDataSetChanged();
            }





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


    private void useModel(Bitmap bitmap) { // 이미지 모양 모델
        try {
            ImageProcessor imageProcessor =
                    new ImageProcessor.Builder()
                            .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                            .build();

            ShapeModelVer11 model = ShapeModelVer11.newInstance(context);


            // Creates inputs for reference.

            //Drawable drawable = getDrawable(R.drawable.image8);

            // Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount()); //바이트 버퍼를 이미지 사이즈 만큼 선언

            bitmap.copyPixelsToBuffer(buffer);//비트맵의 픽셀을 버퍼에 저장

            //
            TensorBuffer inputI = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


            TensorImage tImage = new TensorImage(DataType.FLOAT32);


            tImage.load(bitmap);
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



           // shaEditText.setText(associatedAxisLabels.get(idxA));
            Log.d("gg:", associatedAxisLabels.get(idxA));
            // Releases model resources if no longer used.





            //
            String str_shape = associatedAxisLabels.get(idxA);
            StringTokenizer st = new StringTokenizer(str_shape, "형",true);
            str_shape = st.nextToken();

            Log.d("str_shape", str_shape);
            int i;

            for(i=0; i < list_row1.size();i++){
                Log.d("aaa", list_row1.get(i).getText());
                if(list_row1.get(i).getText().contains(str_shape))
                    break;
            }

            if(i==list_row1.size()){
                i=10;
            }else{
                Log.d("lst", list_row1.get(i).getText());

                Log.d("lst", list_row1.get(i).getText());
            }

            //itemView.performClick()


           // adapter_row1.setStatus(list_row1.get(i).getText());


           /*adapter_row1.click.onClick(Lst_shape.getChildAt(i));*/

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            adapter_row1  = new MedicineResultRecyclerAdapter(list_row1,this, Lst_shape,11, row_images1,i) ;

            mSelectedItems1.put(i,true); //모양
            for(int k =0 ; k < 14; k++) {
                if(k!=i)
                    mSelectedItems2.put(i,false);
            }



            Lst_shape .setLayoutManager(layoutManager ) ;
            Lst_shape .setAdapter(adapter_row1);
            Lst_shape.getItemAnimator().setChangeDuration(0);
            Lst_shape.getAdapter().notifyDataSetChanged();
           // adapter_row1.getmListener().onClick();






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
            MultipartBody.Part bPart = MultipartBody.Part.createFormData("image", "back.jpg", body);
            call = RetrofitHelper.getKaKaoOcr().create(RetrofitService_Server.class).sendKakaoOcr(bPart);
        }

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

                //ocr_result.append("/");
                if (status) {
                    Log.d("TAG", "onResponse: "+status);
                    frontEditText.setText(ocr_result.toString());
                    ocr_result.setLength(0);
                    sendKakaoOcr(frontImage, backImage, false);

                }else
                    backEditText.setText(ocr_result.toString());
                Log.d("TAG", "onResponse: "+status);
                Log.d("TAG", "result_result: "+ ocr_result.toString());




            }

            @Override
            public void onFailure(Call<KakaoDto> call, Throwable t) {
                Log.d("실패카카오", t.toString());
            }
        });

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
    private void init(){
        img_front = findViewById(R.id.img_front);
        img_back = findViewById(R.id.img_back);
        frontEditText = findViewById(R.id.frontOcrEditText);
        backEditText = findViewById(R.id.backOcrEditText);
        btn_confirm = findViewById(R.id.btn_confirm);
        iv_back = findViewById(R.id.iv_back);

        Lst_shape = findViewById(R.id.Lst_shape);
        Lst_color = findViewById(R.id.Lst_color);
        Lst_front_dividing_line = findViewById(R.id.Lst_front_dividing_line);
        Lst_back_dividing_line = findViewById(R.id.Lst_back_dividing_line);

        shape1.add("원형");
        color1.add("흰색");
        front_dividing_line1.add("민무늬");
        back_dividing_line1.add("민무늬");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        adapter_row2  = new MedicineResultRecyclerAdapter(list_row2,this, Lst_color,14,row_images2,0) ;

        addList();



        Lst_color .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;
        Lst_color .setAdapter(adapter_row2);



       // shaEditText = findViewById(R.id.shapeEditText);
       // frontLineEditText = findViewById(R.id.et_line_front_dividing);
       // backLineEditText = findViewById(R.id.et_line_back_dividing);
    }
    public void addItem(ArrayList<MedicineItem> list, Drawable icon, String n, int v, String t) {
        MedicineItem item = new MedicineItem();
        item.setIcon(icon); //아이콘
        item.setName(n); //이름
        item.setViewType(v);
        item.setText(t); //
        list.add(item);
        Log.e("DD","5555");
    }
    @Override
    public void onItemClick(View v, int position, RecyclerView rList) {


        MedicineItem item;
        switch(rList.getId()){

            case R.id.Lst_shape : {
                Log.d("position", position+"");
                item= list_row1.get(position) ;
                checkSelectedItem(position,mSelectedItems1, item, shape1, list_row1);
                break;
            }
            case R.id.Lst_color : {
                item= list_row2.get(position);
                checkSelectedItem(position,mSelectedItems2, item, color1, list_row2);
                break;
            }
            case R.id.Lst_front_dividing_line: {
                item= list_row3.get(position) ;
                checkSelectedItem(position,mSelectedItems3, item, front_dividing_line1, list_row3);
                break;
            }
            case R.id.Lst_back_dividing_line : {
                item= list_row4.get(position) ;
                checkSelectedItem(position,mSelectedItems4, item, back_dividing_line1, list_row4);
                break;
            }
        }
    }

    void checkSelectedItem(int position, SparseBooleanArray mSelectedItems, MedicineItem item, ArrayList<String> type, ArrayList<MedicineItem> list_row){


        if (mSelectedItems.get(position, false) ){
            Log.d("checkSelectedItem", "position: "+position);
            mSelectedItems.put(position, false);
        }
        else {
            for(int i = 0; i < mSelectedItems.size(); i++)
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
        addItem(list_row1,getDrawable(R.drawable.ic_triangle), "삼각형",5, "삼각형");
        addItem(list_row1,getDrawable(R.drawable.ic_rectangle), "사각형",5,"사각형");
        addItem(list_row1,getDrawable(R.drawable.ic_rhombus), "마름모",5,"마름모형");
        addItem(list_row1,getDrawable(R.drawable.ic_oblong), "장방형",5,"장방형");
        addItem(list_row1,getDrawable(R.drawable.ic_oval), "타원형",5,"타원형");
        addItem(list_row1,getDrawable(R.drawable.ic_semicircle), "반원형",5,"반원형");

        addItem(list_row1,getDrawable(R.drawable.ic_pentagon), "오각형",5,"오각형");

        addItem(list_row1,getDrawable(R.drawable.ic_hexagon), "육각형",5,"육각형");

        addItem(list_row1,getDrawable(R.drawable.ic_octagon), "팔각형",5,"팔각형");


        addItem(list_row1,getDrawable(R.drawable.ic_etc), "기타",5,"기타");

        //list_row2 - 색상
        addItem(list_row2,getDrawable(R.drawable.ic_white), "하양",5, "하양");
        addItem(list_row2,getDrawable(R.drawable.ic_yellow), "노랑",5,"노랑");
        addItem(list_row2,getDrawable(R.drawable.ic_orange), "주황",5,"주황");
        addItem(list_row2,getDrawable(R.drawable.ic_pink), "분홍",5,"분홍");
        addItem(list_row2,getDrawable(R.drawable.ic_red), "빨강",5,"빨강");
        addItem(list_row2,getDrawable(R.drawable.ic_brown), "갈색",5,"갈색");
        addItem(list_row2,getDrawable(R.drawable.ic_yellowgreen), "연두",5,"연두");
        addItem(list_row2,getDrawable(R.drawable.ic_purple), "보라",5,"보라");
        addItem(list_row2,getDrawable(R.drawable.ic_bluegreen), "청록",5,"청록");
        addItem(list_row2,getDrawable(R.drawable.ic_blue), "파랑",5,"파랑");
        addItem(list_row2,getDrawable(R.drawable.ic_navy), "남색",5,"남색");
        addItem(list_row2,getDrawable(R.drawable.ic_redviolet), "자주",5,"자주");
        addItem(list_row2,getDrawable(R.drawable.ic_gray), "회색",5,"회색");
        addItem(list_row2,getDrawable(R.drawable.ic_black), "검정",5,"검정");

        //list_row3 - 앞 분할선

        addItem(list_row3,getDrawable(R.drawable.ic_empty), "민무늬",5,"민무늬");
        addItem(list_row3,getDrawable(R.drawable.ic_minus), "(-)형",5,"-");
        addItem(list_row3,getDrawable(R.drawable.ic_line_plus), "(+)형",5,"+");
        addItem(list_row3,getDrawable(R.drawable.ic_line_etc), "문구",5,"문구");


        //list_row4 - 뒷 분할선
        addItem(list_row4,getDrawable(R.drawable.ic_empty), "민무늬",5,"민무늬");
        addItem(list_row4,getDrawable(R.drawable.ic_minus), "(-)형",5,"-");
        addItem(list_row4,getDrawable(R.drawable.ic_line_plus), "(+)형",5,"+");
        addItem(list_row4,getDrawable(R.drawable.ic_line_etc), "문구",5,"문구");


        mSelectedItems2.put(0,true); //색상
        for(int i =1 ; i < 11; i++) mSelectedItems1.put(i,false);



        Lst_color.getItemAnimator().setChangeDuration(0);


        adapter_row2.notifyDataSetChanged();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onBackPressed();
    }
}