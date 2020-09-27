package com.example.smrpv2.ui.medicine.medshot;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.smrpv2.R;
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



public class GoogleOCRActivity extends AppCompatActivity implements Serializable {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDZfaBD1mddJVfGxgrhnUh0Lg02Mfc38KA";//구글 인증키
    private Bitmap bitmap;
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1080;//1080
    private Dialog dialog;
    private boolean bool_end = false;

    private ArrayList<String> itemseq_list;
    private Button add_Btn;
    private HashMap<Integer, String> select_pill_list; //사용자 선택한 약 정보를 담는 hashmap
    private String id ;
    private ImageView back_imgView;
    private Uri photoUri;

    TextView textView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_o_c_r);//activity_search_prescription

        String backImg =getIntent().getStringExtra("uri");
        Log.d("gggg",backImg);
        String frontImg=backImg.substring(0,backImg.lastIndexOf("/"))+"/picF.jpg";


        Bitmap rotatedBitmap = null;


        ImageView imageView = findViewById(R.id.image);

        textView = findViewById(R.id.textView);



        try {
            File file = new File(frontImg);
            Bitmap bitmap = MediaStore.Images.Media
                    .getBitmap(getContentResolver(), Uri.fromFile(file));

            if (bitmap != null) {
                ExifInterface ei = new ExifInterface(frontImg);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch (orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                //imageView.setImageBitmap(rotatedBitmap);
            }
        }catch(Exception err){
            err.printStackTrace();
        }


        // canvas.drawRect(width/4,width/4,(width/4)*3,(width/4)*3,paint);
        Log.d("size","width"+rotatedBitmap.getWidth()+"height="+rotatedBitmap.getHeight());

        Bitmap targetBitmap=Bitmap.createBitmap(rotatedBitmap,rotatedBitmap.getWidth()/2-250,rotatedBitmap.getHeight()/2-250,500,500);
        imageView.setImageBitmap(targetBitmap);

        Uploading_bitmap(targetBitmap);

    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
        //imageFilePath = image.getAbsolutePath();
        return image;
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
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }
        }
    }


    private void Uploading_bitmap(Bitmap bitmap){
        if(bitmap != null){

            Log.d("TAG", "bitmap width: "+bitmap.getWidth());
            Log.d("TAG", "bitmap height: "+bitmap.getHeight());
            bitmap = scaleBitmapDown(bitmap,MAX_DIMENSION);

            //cameraView.setVisibility(View.GONE);
            //imageView.setImageBitmap(bitmap);
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
        private final WeakReference<GoogleOCRActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        ProgressDialog progressDialog = new ProgressDialog(GoogleOCRActivity.this);
        LableDetectionTask(GoogleOCRActivity activity, Vision.Images.Annotate annotate) {
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
            //return "Cloud Vision API request failed. Check logs for details.";

            return "Cloud Vision API request failed. Check logs for details.";
        }
        @Override
        protected void onPreExecute() {
            /*ViewGroup group = (ViewGroup) root.getParent();
            if(group!=null)
                group.removeView(root);*/
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다..");

            // show dialog
            progressDialog.show();
            super.onPreExecute();
        }
        protected void onPostExecute(String result) { ///////////////////////////////// 객체에서 문자 추출한 결과: result
            GoogleOCRActivity activity = mActivityWeakReference.get();

            progressDialog.dismiss();
            if (activity != null && !activity.isFinishing()) {
                // TextView imageDetail = activity.findViewById(R.id.textViewResult);
                //imageDetail.setText(result);

                //st_result.append(result);
                ArrayList<String> pill_list = new ArrayList<>();


                StringTokenizer token = new StringTokenizer(result , "\n");
                while(token.hasMoreTokens()){
                    String temp = token.nextToken();
                    if(!pill_list.contains(temp))
                        pill_list.add(temp);
                }
                //pill_list.remove(pill_list.size() -1); //약 리스트의 마지막 리스트 삭제
                Log.d("TAG", "onPostExecute: "+result);
                Log.d("TAG", "size: "+ pill_list.size());


                for(int i = 0 ; i<pill_list.size();i++){
                    Log.d("TAG", "pil_name: ["+i+"]="+pill_list.get(i)+" /length==>"+pill_list.get(i).length());
                    textView.append(pill_list.get(i));
                }


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
    /*@Override
    public void onFinished() {
    }
    @Override
    public void onError(int code, String message) {
    }*/
    private void callCloudVision(final Bitmap bitmap) {
        Log.d("TAG", "callCloudVision: ");
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
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

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();//Texts에 들어있는 값들을 하나씩 꺼내 Text에 대입
        Log.d("TAG", "convertResponseToString: "+ response.getResponses().get(0).getFullTextAnnotation());
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
            // TODO 작업이 취소된후에 호출된다.
            super.onCancelled();
        }
    }*/

}