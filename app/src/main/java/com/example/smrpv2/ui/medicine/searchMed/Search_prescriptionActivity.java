package com.example.smrpv2.ui.medicine.searchMed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;
import com.example.smrpv2.model.PillName;
import com.example.smrpv2.retrofit.RetrofitService_Server;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * 처방전 & 약 봉투 사진 찍고 그 결과 알려주는 Activity
 *
 */
public class Search_prescriptionActivity extends AppCompatActivity implements Serializable {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDZfaBD1mddJVfGxgrhnUh0Lg02Mfc38KA";//구글 인증키
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1080;

    private ArrayList<MedicineItem> list;
    private ArrayList<String> itemseq_list;
    private Button Btn_add;
    private ImageView iv_back;

    private Bitmap bitmap;
    private Uri photoUri;
    private HashMap<Integer, String> select_pill_list; //사용자 선택한 약 정보를 담는 hashmap
    private PrescriptionAdapter prescriptionAdapter;
    private RecyclerView recyclerView;

    private String id ;
    SharedPreferences sharedPreferences;
    private boolean bool_end = false;
    private RetrofitService_Server retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pill);

        initView();

        Btn_add.setOnClickListener(new View.OnClickListener() { //추가하기 버튼 누를시
            @Override
            public void onClick(View v) { //추가하기
                if(select_pill_list.size()==0){
                    Toast.makeText(getApplicationContext(),"약을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    for(Map.Entry<Integer, String>elem : select_pill_list.entrySet())
                        itemseq_list.add(elem.getValue());
                    /**
                     *
                     * 서버 : 검색ㄱ된 약 추가하기
                     */
                }


            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        prescriptionAdapter.setOnClickListener(new PrescriptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrescriptionAdapter.ViewHolder holder, View v, int position) {

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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 672 && resultCode == RESULT_OK) {

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uploading_bitmap(bitmap);
        }else{
            onBackPressed();
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        return image;
    }
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 내장 카메라 켜기
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.smrp/files/Pictures");//Android/data/com.raonstudio.cameratest/files

            if (!file.exists()) {
                file.mkdir();
            }

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, 672);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            }
        }
    }

    private void Uploading_bitmap(Bitmap bitmap){
        if(bitmap != null){
            bitmap = scaleBitmapDown(bitmap,MAX_DIMENSION);
            callCloudVision(bitmap);
        }
    }
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

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

    private class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<Search_prescriptionActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        ProgressDialog progressDialog = new ProgressDialog(Search_prescriptionActivity.this);
        LableDetectionTask(Search_prescriptionActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }
        @Override
        protected String doInBackground(Object... params) {
            try {
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d("TAG", "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d("TAG", "failed to make API request because of other IOException " +
                        e.getMessage());
            }

            return "Cloud Vision API request failed. Check logs for details.";
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다..");
            progressDialog.show();
            super.onPreExecute();
        }
        protected void onPostExecute(String result) {
            Search_prescriptionActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                ArrayList<String> pill_list = new ArrayList();
                StringTokenizer token = new StringTokenizer(result , "\n");

                while(token.hasMoreTokens()){
                    pill_list.add(token.nextToken());
                }
                pill_list.remove(pill_list.size() -1);
                /**
                 * 약이름에 대한 것을 서버에 요청하기 위해 필요
                 */
                PillName pillname = new PillName(pill_list);

                /**
                 *
                 * 서버 : 약품명을 서버에게 요청하기 위한 코드가 들어가야함
                 *
                 */


                bool_end = true;

            }
        }
    }
    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);
                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);
                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);
                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);
        Vision vision = builder.build();
        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("TEXT_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);

        annotateRequest.setDisableGZipContent(true);
        return annotateRequest;
    }
    private void callCloudVision(final Bitmap bitmap) {
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d("TAG", "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder();

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();

        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.KOREAN, "%s", label.getDescription()));
            }
        } else {
            message.append("nothing");
        }
        return message.toString();
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
        list = new ArrayList<MedicineItem>();
        prescriptionAdapter = new PrescriptionAdapter(list);
        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(prescriptionAdapter);
        sharedPreferences = getSharedPreferences("setting",0);
        id = sharedPreferences.getString("id","");
    }
}