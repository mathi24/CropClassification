package com.jsn.cropclassification.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListenableFuture;
import com.jsn.cropclassification.model.CropDetails;
import com.jsn.cropclassification.tflite.LiteClassifierActivity;
import com.jsn.cropclassification.view.Details;
import com.jsn.cropclassification.R;
import com.jsn.cropclassification.adapter.RetrofitItemAdapter;
import com.jsn.cropclassification.utils.Utility;
import com.jsn.cropclassification.model.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int SELECT_IMAGE = 7;
    private static final int SELECT_FILE1 = 3;
    private static final int SELECT_FILE2 = 10;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 100;
    private final String[] REQUEST_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    PreviewView mPreviewView;
    FrameLayout captureImage, galleryImage;
    ImageView takenImage, ivFlash, ivBack, taken_gallery, imgClose;
    Switch switchIcon;
    Camera camera;
    ProgressBar progressBar;
    boolean isTourchOnOff = false;
    //    BottomSheetDialog bottomSheetDialog;
    LinearLayout linearLayout;
    BottomSheetBehavior bottomSheetBehavior;
    private ListView listView;
    private List<CropDetails.Crops> mCropDetails;
    private RetrofitItemAdapter adapter;
    View shadowView;
    String selectedImage;
    Button btnCancel, btnSubmit;
    TextView cropName1, cropName2, cropName3, cropProbability1, cropProbability2, cropProbability3;
    RadioButton radioButton;
    RadioGroup radioGroup;
    String selectedCropName = "", selectedCropProbability = "";
    boolean switchflag = false;
    private String modelFileName;
    private List<String> modelLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);


     /*   Bundle extras = getIntent().getExtras();
        selectedImage = extras.getString("selectedImage");
*/
        mPreviewView = findViewById(R.id.previewView);
        captureImage = findViewById(R.id.frame_layout);
        galleryImage = findViewById(R.id.frame_layout1);
        takenImage = findViewById(R.id.imageCapture);
        progressBar = findViewById(R.id.progress);
        ivFlash = findViewById(R.id.iv_flash);
        ivBack = findViewById(R.id.iv_back);
        switchIcon = findViewById(R.id.switch_icon);
//        taken_gallery = findViewById(R.id.image_Capture_Gallery);
        TextView tvCropName = findViewById(R.id.crop_name);
        TextView tvProbability = findViewById(R.id.probability);

        linearLayout = findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        listView = findViewById(R.id.list_view);
        shadowView = findViewById(R.id.shadow_view);
        imgClose = findViewById(R.id.img_close);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSubmit = findViewById(R.id.btn_submit);
        cropName1 = findViewById(R.id.crop_name1);
        cropName2 = findViewById(R.id.crop_name2);
        cropName3 = findViewById(R.id.crop_name3);
        cropProbability1 = findViewById(R.id.crop_probability1);
        cropProbability2 = findViewById(R.id.crop_probability2);
        cropProbability3 = findViewById(R.id.crop_probability3);

//        Boolean switchState = switchIcon.isChecked();
        imgClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ivFlash.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
//        switchIcon.setOnClickListener(this);


       /* if (switchIcon.isChecked()){
            if (Utility.isNetworkConnected(this)){
                Toast.makeText(this, "Online Mode", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Offline Mode", Toast.LENGTH_SHORT).show();
            }
        }*/
            if (Utility.isNetworkConnected(this)) {
                Toast.makeText(this, "Online Mode", Toast.LENGTH_SHORT).show();
            switchIcon.setChecked(true);
        }else{
           switchIcon.setChecked(false);
               Intent intent = new Intent(CameraActivity.this, ClassifierActivity.class);
             //  Intent intent = new Intent(CameraActivity.this, LiteClassifierActivity.class);
                startActivity(intent);
                finish();
        }

        switchIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    switchIcon.setChecked(true);

                }else{
                    switchIcon.setChecked(false);
                   Intent intent = new Intent(CameraActivity.this, ClassifierActivity.class);
                   // Intent intent = new Intent(CameraActivity.this, LiteClassifierActivity.class);
                    startActivity(intent);
                    finish();

                    /*modelFileName = "modelnew.tflite";
                    modelLabel = new ArrayList<>();

                    modelLabel.add("Arecanut");
                    modelLabel.add("Coconut");
                    modelLabel.add("Groundnut");
                    modelLabel.add("Jowar");
                    modelLabel.add("Moong Green");
                    modelLabel.add("Negative");
                    modelLabel.add("Onion");
                    modelLabel.add("Paddy");
                    modelLabel.add("Ragi");
                    modelLabel.add("Rose");
                    modelLabel.add("Soyabean");
                    modelLabel.add("Sugarcane");
                    modelLabel.add("Tomato");
                    modelLabel.add("Tur Red Gram");*/
                }
            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        shadowView.setVisibility(View.GONE);
                        break;

                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        shadowView.setVisibility(View.VISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {

                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        final ImageCapture imageCapture = builder
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();

        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);

        captureImage.setOnClickListener(v -> {
            if (Utility.isNetworkConnected(this)){
                captureImage.setEnabled(false);
                takenImage.setVisibility(View.VISIBLE);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSS", Locale.US);
                //File file =new File(getBatchDirectoryName(),"cropclassification" + ".jpg");
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cropclassification" + ".jpg");//above os11

                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                if (file.exists()) {

                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                                    takenImage.setImageBitmap(bitmap);

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to 50% of original image quality
                                    byte[] byte_arr = stream.toByteArray();
                                    final RequestBody requestBody = RequestBody
                                            .create(MediaType.parse("application/octet-stream"), byte_arr);
                                    cameraProvider.unbindAll();
                                    callClassificationAPI(requestBody);

                                }
                            }
                        });

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                        captureImage.setEnabled(true);
                        takenImage.setVisibility(View.GONE);
                        exception.printStackTrace();
                    }
                });
            }else{
                Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                switchIcon.setChecked(false);
            }


        });
      /*  galleryImage.setOnClickListener(v -> {
            galleryImage.setEnabled(true);
            taken_gallery.setVisibility(View.VISIBLE);

            File file = new File(getPath(Uri.fromFile(getFilesDir(getPath()))));
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            takenImage.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to 50% of original image quality
            byte[] byte_arr = stream.toByteArray();
            final RequestBody requestBody = RequestBody
                    .create(MediaType.parse("application/octet-stream"), byte_arr);
            cameraProvider.unbindAll();


        });
*/

    }


    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void openGallery(int SELECT_IMAGE) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);

    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(CameraActivity.this.getContentResolver(), data.getData());
                        captureImage.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(CameraActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

        *//*else if (requestCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            takenImage.setImageURI(selectedImage);
        }*//*
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            if (requestCode == SELECT_FILE1) {
                String selectedPath1 = getPath(selectedImageUri);
                File file = new File(selectedPath1, "cropcassification" + ".jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                taken_gallery.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to 50% of original image quality
                byte[] byte_arr = stream.toByteArray();
                final RequestBody requestBody = RequestBody
                        .create(MediaType.parse("application/octet-stream"), byte_arr);
                callClassificationAPI(requestBody);

                System.out.println("selectedPath1 : " + selectedPath1);
            }

            if (requestCode == SELECT_FILE2) {
                String selectedPath2 = getPath(selectedImageUri);
                File file = new File(selectedPath2, "cropclassification" + ".jpg");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(CameraActivity.this.getContentResolver(), data.getData());
                    captureImage.setVisibility(View.GONE);
                    System.out.println("selectedPath2 : " + selectedPath2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }


  /*  public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
*/
  /*  public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }*/


    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";

        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return app_folder_path;
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUEST_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permission not granted by the user", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    public void callClassificationAPI(RequestBody requestBody) {

        if (Utility.isNetworkConnected(this)) {
            progressBar.setVisibility(View.VISIBLE);
            Call<CropDetails> call = ApiClient.getInstance().getApi().newUploadImage(requestBody);

            call.enqueue(new Callback<CropDetails>() {
                @Override
                public void onResponse(Call<CropDetails> call, Response<CropDetails> response) {
                    captureImage.setEnabled(true);
                    progressBar.setVisibility(View.GONE);

                    int status = Integer.parseInt(response.body().getStatusCode());
                    if (response.isSuccessful()) {
                        if (response.body() != null && status == 200) {
//                     Log.i("onSuccess", response.body().toString()); && Integer.parseInt(response.body().getStatusCode()) == 200
                            mCropDetails = new ArrayList<>();
                            mCropDetails = response.body().getCropsList();

//                      Toast.makeText(CameraActivity.this, String.valueOf(mCropDetails.size()), Toast.LENGTH_SHORT).show();
//                      startCamera();
                            if (mCropDetails.size() > 0) {
                                Details.getInstance().getCallback().onSuccess(response.body().getCropsList());
                                finish();
                            } else {
                                Toast.makeText(CameraActivity.this, "Crop Not Found", Toast.LENGTH_SHORT).show();
                                startCamera();
                            }
                      /*Intent intent = new Intent(CameraActivity.this,LoginActivity.class);
                      startActivity(intent);*/
//                      showBottomSheet();

                        } else {
                            Toast.makeText(CameraActivity.this, "Crop Not Found, Please try again with a different crop", Toast.LENGTH_LONG).show();
                            startCamera();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CropDetails> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
            startCamera();
        }






   /*   Call<CropDetails> call = ApiClient.getInstance().getApi().newUploadImage(requestBody);

      call.enqueue(new Callback<CropDetails>() {
          @Override
          public void onResponse(Call<CropDetails> call, Response<CropDetails> response) {

              captureImage.setEnabled(true);
              progressBar.setVisibility(View.GONE);

              if (response.isSuccessful()){
                 if (response.body() != null && Integer.parseInt(response.body().getStatusCode()) == 200){
//                     Log.i("onSuccess", response.body().toString());
                     startCamera();

                     showBottomSheet(response.body());



                 }
              }
          }

          @Override
          public void onFailure(Call<CropDetails> call, Throwable t) {

              progressBar.setVisibility(View.GONE);
              captureImage.setEnabled(true);
              takenImage.setVisibility(View.GONE);
              System.out.println("errorCustom " + t.getMessage());
              if (t instanceof SocketTimeoutException) {

                  Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
              } else {
                  Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
              }
              startCamera();
          }
      });

*/
    }

    @Override
    public void onClick(View view) {

        if (view == imgClose) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            shadowView.setVisibility(View.GONE);
        } else if (view == galleryImage) {
            openGallery(SELECT_FILE1);
        } else if (view == ivFlash) {
            if (camera != null) {
                if (!isTourchOnOff && camera.getCameraInfo().hasFlashUnit()) {
                    isTourchOnOff = true;
                    ivFlash.setImageDrawable(getDrawable(R.drawable.ic_zap_on));
                    camera.getCameraControl().enableTorch(true); // or false
                } else if (isTourchOnOff && camera.getCameraInfo().hasFlashUnit()) {
                    isTourchOnOff = false;
                    ivFlash.setImageDrawable(getDrawable(R.drawable.ic_zap_off));
                    camera.getCameraControl().enableTorch(false);
                }
            }

        } else if (view == ivBack) {
            finish();
        } else if (view == btnSubmit) {

           /* if (!selectedCropName.equals("") && !selectedCropProbability.equals("") ){
                Details.getInstance().getCallback().onSuccess(selectedCropName,selectedCropProbability,selectedImage);
                finish();
            }else{
                Toast.makeText(this, "Please select any crop", Toast.LENGTH_SHORT).show();

            }*/
        } else if (view == btnCancel) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            shadowView.setVisibility(View.GONE);
        }
    }

    public void selectedCropFromList(String cropName, String cropProbability) {
        selectedCropName = cropName;
        selectedCropProbability = cropProbability;

    }

    private void showBottomSheet() {
/*

        View view = getLayoutInflater().inflate(R.layout.crop_list_bottom_sheet,null);
        bottomSheetDialog = new BottomSheetDialog(this);
        linearLayout = (LinearLayout) view.findViewById(R.id.crop_layout);
        TextView tvCropName = (TextView)view.findViewById(R.id.crop_name);
        TextView tvProbability = (TextView)view.findViewById(R.id.probability);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
*/

//        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
       /* Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);*/
//        listView = (ListView) view.findViewById(R.id.list_view);
        /*bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();*/
       /* bottomSheetDialog.getBehavior().setPeekHeight(0);
        bottomSheetDialog.getBehavior().setHideable(true);
        relativeLayout = findViewById(R.id.bottom_sheet_layout);
        relativeLayout.setBackground(getDrawable(R.drawable.top_rounded_corner));*/


        /*tvCropName.setText(body.getCropName());
        tvProbability.setText(body.getCropProbability());*/
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        shadowView.setVisibility(View.VISIBLE);
        adapter = new RetrofitItemAdapter(mCropDetails, this, CameraActivity.this);
        listView.setAdapter(adapter);

    }




/*
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            startCamera();
        }
    }
*/

}