package com.example.smrpv2.ui.medicine.medshot;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Message;
import com.example.smrpv2.model.searchMed_model.MedicineInfoRsponDTO;
import com.example.smrpv2.retrofit.RetrofitHelper;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleOCRActivity extends AppCompatActivity implements Serializable {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDZfaBD1mddJVfGxgrhnUh0Lg02Mfc38KA";//구글 인증키
    private Bitmap bitmap;
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1080;//1080
    private Dialog dialog;
    private boolean bool_end = false;
    Context context;
    private ArrayList<String> itemseq_list;
    private Button add_Btn;
    private HashMap<Integer, String> select_pill_list; //사용자 선택한 약 정보를 담는 hashmap
    private String id, pillname1 = "", pillname2 = "";
    private ImageView back_imgView;
    private Uri photoUri;
    private Bitmap targetBitmap_front, targetBitmap_back;
    private ArrayList<String> pill_list_front;
    TextView textView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_o_c_r);//activity_search_prescription

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
        context = this;

        Bitmap rotatedBitmap = null;

        ImageView imageView = findViewById(R.id.image);

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
            targetBitmap_back = Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-112,rotatedBitmap.getHeight()/2-112,224,224);
            Log.d("하기전", frontfile.toString());
            Log.d("하기전", backfile.toString());
            SaveBitmapToFileCache(targetBitmap_front, frontImgDate);
            SaveBitmapToFileCache(targetBitmap_back, backImgDate);
            Log.d("한후", frontfile.toString());
            Log.d("한후", backfile.toString());
            sendFile(frontfile, backfile);

        } catch (Exception err) {
            err.printStackTrace();
        }





        //  Uploading_bitmap_front(targetBitmap_front);

    }

    private void  SaveBitmapToFileCache(Bitmap targetBitmap_front, String frontImgDate) {

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

    /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
     }*/
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        //imageFilePath = image.getAbsolutePath();
        return image;
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

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 내장 카메라 켜기
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d("TAG", "sendTakePhotoIntentsendTakePhotoIntentsendTakePhotoIntent: ");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.smrp/files/Pictures");//Android/data/com.raonstudio.cameratest/files

            if (!file.exists()) {

                file.mkdir();

            }

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
                //resume tasks needing this permission
            }
        }
    }


    private void Uploading_bitmap_front(Bitmap bitmap) { //앞면의 이미지를 final로 정의한 상수에 맞게 이미지 처리
        if (bitmap != null) {

            Log.d("TAG", "bitmap width: " + bitmap.getWidth());
            Log.d("TAG", "bitmap height: " + bitmap.getHeight());
            bitmap = scaleBitmapDown(bitmap, MAX_DIMENSION);

            //cameraView.setVisibility(View.GONE);
            //imageView.setImageBitmap(bitmap);
            front_callCloudVision(bitmap);
        }
    }

    private void Uploading_bitmap_back(Bitmap bitmap) {//뒷면의 이미지를 final로 정의한 상수에 맞게 이미지 처리
        if (bitmap != null) {

            Log.d("TAG", "bitmap width: " + bitmap.getWidth());
            Log.d("TAG", "bitmap height: " + bitmap.getHeight());
            bitmap = scaleBitmapDown(bitmap, MAX_DIMENSION);

            //cameraView.setVisibility(View.GONE);
            //imageView.setImageBitmap(bitmap);
            back_callCloudVision(bitmap);
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

    private class front_LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<GoogleOCRActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        ProgressDialog progressDialog = new ProgressDialog(GoogleOCRActivity.this);

        front_LableDetectionTask(GoogleOCRActivity activity, Vision.Images.Annotate annotate) {
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

           /* progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다..");

            // show dialog
            progressDialog.show();*/
            super.onPreExecute();
        }

        protected void onPostExecute(String result) { ///////////////////////////////// 객체에서 문자 추출한 결과: result
            GoogleOCRActivity activity = mActivityWeakReference.get();

            progressDialog.dismiss();
            if (activity != null && !activity.isFinishing()) {
                pill_list_front = new ArrayList<>();
                result = result.replaceAll("nothing", "");
                StringTokenizer token = new StringTokenizer(result, "\n");
                while (token.hasMoreTokens()) {
                    String temp = token.nextToken();
                    if (!pill_list_front.contains(temp))
                        pill_list_front.add(temp);
                }

                for (int i = 0; i < pill_list_front.size(); i++) {
                    Log.d("TAG", "pil_name: [" + i + "]=" + pill_list_front.get(i) + " /length==>" + pill_list_front.get(i).length());
                    textView.append(pill_list_front.get(i));
                }


                Uploading_bitmap_back(targetBitmap_back);

            }
        }
    }

    private class back_LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<GoogleOCRActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        ProgressDialog progressDialog = new ProgressDialog(GoogleOCRActivity.this);

        back_LableDetectionTask(GoogleOCRActivity activity, Vision.Images.Annotate annotate) {
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

            /*progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다..");

            // show dialog
            progressDialog.show();*/
            super.onPreExecute();
        }

        protected void onPostExecute(String result) { ///////////////////////////////// 객체에서 문자 추출한 결과: result
            GoogleOCRActivity activity = mActivityWeakReference.get();

            //progressDialog.dismiss();
            if (activity != null && !activity.isFinishing()) {
                ArrayList<String> pill_list_back = new ArrayList<String>();
                result = result.replaceAll("nothing", "");
                StringTokenizer token = new StringTokenizer(result, "\n");
                while (token.hasMoreTokens()) { //서버에서 보내준 응답에 대해 list에 담겨있는 String과 중복값을 배제
                    String temp = token.nextToken();
                    if (!pill_list_back.contains(temp))
                        pill_list_back.add(temp);
                }

                for (int i = 0; i < pill_list_back.size(); i++) {
                    Log.d("TAG", "pil_name: [" + i + "]=" + pill_list_back.get(i) + " /length==>" + pill_list_back.get(i).length());
                    textView.append(pill_list_back.get(i));
                }


                Log.d("TAG", "pill_list.size(): " + pill_list_back.size());


                if (pill_list_front.size() != 0)
                    pillname1 = pill_list_front.get(0);


                if (pill_list_back.size() != 0)
                    pillname2 = pill_list_back.get(0);

                Log.d("TAG", "pill_list.get(1): " + pillname1);
                Log.d("TAG", "pill_list.get(2): " + pillname2);
                /**
                 * 의약품 앞, 뒷면을 OCR결과물을 서버에 보냄
                 **/
                String[] medicineLogo = {pillname1, pillname2};
                Call<MedicineInfoRsponDTO> call = RetrofitHelper.getRetrofitService_server().medicineOcr(medicineLogo);
                call.enqueue(new Callback<MedicineInfoRsponDTO>() {
                    @Override
                    public void onResponse(Call<MedicineInfoRsponDTO> call, Response<MedicineInfoRsponDTO> response) {

                        //Log.d("ItenName",response.body().toString());
                        //assert response.body() != null;
                        if (response.body().getItemName() == null) {
                            Toast.makeText(context, "없음", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, response.body().getItemName(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MedicineInfoRsponDTO> call, Throwable t) {
                        Log.d("error", t.toString());
                    }
                });


            }
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
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

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("TEXT_DETECTION");//LABEL_DETECTION:이미지 Text_DETECTION
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d("TAG", "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private void front_callCloudVision(final Bitmap bitmap) { //앞면 이미지를 구글서버에 요청
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new front_LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d("TAG", "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private void back_callCloudVision(final Bitmap bitmap) {//뒷면 이미지를 구글서버에 요청
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new back_LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d("TAG", "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder();

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();//Texts에 들어있는 값들을 하나씩 꺼내 Text에 대입
        Log.d("TAG", "convertResponseToString: " + response.getResponses().get(0).getFullTextAnnotation());
        if (labels != null) {
            for (EntityAnnotation label : labels) {

                message.append(String.format(Locale.KOREAN, "%s", label.getDescription()));//%.3f: , label.getScore()
                //message.append("\n");
            }
        } else {
            //message.append("nothing");
            message.append("nothing");
        }

        return message.toString();
    }
/*
    private class Dialog extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog1 = new ProgressDialog(GoogleOcrActivitiy.this);
        @Override
        protected void onPreExecute() {
            ViewGroup group = (ViewGroup) root.getParent();
            if(group!=null)
                group.removeView(root);
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.setMessage("로딩중입니다..");

            // show dialog
            progressDialog1.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2500); // 2초 지속
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(!bool_end)
                ;
            bool_end = false;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog1.dismiss();

            //finish();

            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled() {

            super.onCancelled();
        }
    }*/

}