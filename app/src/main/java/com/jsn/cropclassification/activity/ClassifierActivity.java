/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsn.cropclassification.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.location.Location;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsn.cropclassification.ModelCheckActivity;
import com.jsn.cropclassification.PreferenceStorage;
import com.jsn.cropclassification.model.BorderedText;
import com.jsn.cropclassification.model.Classifier;
import com.jsn.cropclassification.model.Label;
import com.jsn.cropclassification.model.MainModelDetails;
import com.jsn.cropclassification.utils.GPSTracker;
import com.jsn.cropclassification.utils.Logger;
import com.jsn.cropclassification.R;
import com.jsn.cropclassification.utils.StepBean;
import com.jsn.cropclassification.model.Result;
import com.jsn.cropclassification.utils.Utility;
import com.jsn.cropclassification.view.Details;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public class ClassifierActivity extends Camera2Activity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();
   //private static Size DESIRED_PREVIEW_SIZE = new Size(480, 640);
   //private static final Size DESIRED_PREVIEW_SIZE = new Size(1544, 720);
  private static final Size DESIRED_PREVIEW_SIZE = new Size(1920, 1080);
  //private static Size DESIRED_PREVIEW_SIZE = new Size(1544, 720);
//   private static Size DESIRED_PREVIEW_SIZE = new Size(2448, 3264);
   //private static Size DESIRED_PREVIEW_SIZE = new Size(720, 1544);
//    private static Size DESIRED_PREVIEW_SIZE = new Size(1360, 960);

//    private static final Size DESIRED_PREVIEW_SIZE = new Size(960, 1360);
  //  private static final Size DESIRED_PREVIEW_SIZE= new Size(1280, 960);;
    private static final float TEXT_SIZE_DIP = 10;
    private Bitmap rgbFrameBitmap = null;
//   private Bitmap rgbFrameBitmap ;

    private long lastProcessingTimeMs;
    private Integer sensorOrientation;
    private Classifier classifier;
    private BorderedText borderedText;
    AlertDialog confirmationDialog;
    Location currentLocation;
    /**
     * Input image size of the model along x axis.
     */
    private int imageSizeX;
    /**
     * Input image size of the model along y axis.
     */
    private int imageSizeY;
    public static boolean isVideoRecordStarted = false;

    ImageView startCapture;
    ImageView ivClose;
    boolean isSent = false;
    public byte[] mondelImage = null;
    public boolean isDetect = false;
    public String vinNumber = "";
    public int modelId;
    public String modelName;
    public String iteration_id;
    public String modelUrl;
    public String modelConfidence;
    public String modelVersion;

    public String AOI;

    private int width ;
    private int height ;

    private String modelFileName;
    private String modelOverlayUrl;
//    private String[] modelLabel;
   private List<String> modelLabelName;
    private static List<StepBean> stepsBeanList = null;
    private String activityModelString;
    private String stepString;
//    private List<Result.ModelData> modelData;

    MainModelDetails.ModelDetails modelDetails;
    Gson gson;
    MediaPlayer ring;
    String apiDisplayMsg;

    ProgressBar progressBar;

    private boolean dataBool;

    Handler failHandler;
    Switch switch_icon_tflite;


    private Boolean pauseCall = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Offline Mode", Toast.LENGTH_SHORT).show();
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        Bundle bundle = getIntent().getExtras();
        ring = MediaPlayer.create(ClassifierActivity.this, R.raw.long_beep);
        if (bundle != null) {
            vinNumber = bundle.getString(getString(R.string.key_vin_no));
        }
//        activityModelString = PreferenceStorage.getInstance(ClassifierActivity.this).getActivityModel();

//        stepString = PreferenceStorage.getInstance(ClassifierActivity.this).getStep();

        gson = new Gson();
        Type modelType = new TypeToken<ArrayList<Result.ModelData>>() {
        }.getType();



       /* PreferenceStorage preferenceStorage = new PreferenceStorage(this);

        modelName = preferenceStorage.getInstance(this).getModelName();
*/
      //  preferenceStorage.setModelName(modelName);*/
//        modelData = gson.fromJson(activityModelString, modelType);

        /*preferenceStorage = new PreferenceStorage(this);

       preferenceStorage.getModelLabel();
       preferenceStorage.getModelName();*/


      /*  for (int i = 0; i <modelDetails.toString().lastIndexOf(modelId); i++){
            modelId = modelDetails.getModelId();
            modelName = modelDetails.getModelName();
            modelVersion = modelDetails.getModelVersion();
        }*/
/*
         for (int i = 0; i < modelData.size(); i++) {
            if (!modelData.get(i).isCheckStatus() && !dataBool) {
                modelId = modelData.get(i).getModelId();
                modelName = modelData.get(i).getModelName();
                modelVersion = modelData.get(i).getModelVersion();
                AOI = modelData.get(i).getModelMark();
                width = Integer.parseInt(modelData.get(i).getPreviewWidth());
                PreferenceStorage.getInstance(this).setPreviewWidth(width);
                height = Integer.parseInt(modelData.get(i).getPreviewHeight());
                PreferenceStorage.getInstance(this).setPreviewHeight(height);
*/


//               modelFileName = modelData.get(i).getModelFileName();

      /*  PreferenceStorage preferenceStorage = new PreferenceStorage(this);
       *//* modelDetails = new MainModelDetails.ModelDetails(int modelId, String modelVersion,
                String iteration_id, String modelUrl, String modelLabel, String modelConfidence, String modelName);*//*
        modelLabel = new ArrayList<>();
        if (!preferenceStorage.getModelUrl().equals("")){//check from local storage
            modelFileName = preferenceStorage.getModelName();
            //modelFileName = "modelnew.tflite";
            String[] labelVal  = {preferenceStorage.getModelLabel()};
           *//* for(int i =0;i<preferenceStorage.getModelLabel().length();i++){
                Label label = new Label();
                label.setCropLabel(preferenceStorage.getModelLabel()[i]);

            }*//*
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
            modelLabel.add("Tur Red Gram");
            //modelLabel.add(preferenceStorage.getModelLabel());

       }else*///check from asset folder
            //modelLableName = modelData.get(i).getModelLable();

        modelFileName = "modelnew.tflite";

          /*  modelLabel = new String[]{"Arecanut", "Coconut", "Groundnut", "Jowar", "Moong Green", "Negative", "Onion", "Paddy",
                    "Ragi", "Rose", "Soyabean", "Sugarcane", "Tomato", "Tur Red Gram"};

            for (int i = 0; i  <modelLabel.length; i++){

            }*/
        modelLabelName = new ArrayList<>();

        modelLabelName.add("Arecanut");
        modelLabelName.add("Coconut");
        modelLabelName.add("Groundnut");
        modelLabelName.add("Jowar");
        modelLabelName.add("Moong Green");
        modelLabelName.add("Negative");
        modelLabelName.add("Onion");
        modelLabelName.add("Paddy");
        modelLabelName.add("Ragi");
        modelLabelName.add("Rose");
        modelLabelName.add("Soyabean");
        modelLabelName.add("Sugarcane");
        modelLabelName.add("Tomato");
        modelLabelName.add("Tur Red Gram");

    /*  modelLabel.add("Arecanut");
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
        modelLabel.add("Tur Red Gram");

        String[] strObjects = modelLabel.toArray(new String[0]);
        for(String obj: strObjects) {
            System.out.println(obj);
        }*/
       /* String[] array = new String[modelLabel.size()];
        int index = 0;
        for (Object value : modelLabel) {
            array[index] = (String) value;
            index++;
        }*/
            /*modelLabel.addAll(Arrays.asList("Arecanut","Coconut","Groundnut","Jowar","Moong Green","Negative","Onion","Paddy",
                    "Ragi","Rose","Soyabean","Sugarcane","Tomato","Tur Red Gram"));*/
           /* modelLabel.add("Arecanut");
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
            modelLabel.add("Tur Red Gram");
*/
                /*modelOverlayUrl = modelData.get(i).getModelOverlayUrl();
                apiDisplayMsg = modelData.get(i).getDisplayMessage();*/

                dataBool = true;
           /* }
        }*/
        failHandler = new Handler(getMainLooper());
/*
        try {
            CaocConfig.Builder.create()
                    .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                    .enabled(true) //default: true
                    //.showErrorDetails(false) //default: true
                    .showRestartButton(false) //default: true
                    //.trackActivities(false) //default: false
                    .minTimeBetweenCrashesMs(1) //default: 3000
                    .errorActivity(VchekErrorActivity.class)
                    //.restartActivity(VinNumberActivity.class)
                    .showErrorDetails(false)
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        currentLocation = new GPSTracker(this).getLocation();
      //  Toast.makeText(this,currentLocation.getLatitude() + "" +currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();

//        initViewAction();


    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_ic_camera_connection_fragment;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {

       /*DESIRED_PREVIEW_SIZE = new Size(
                getScreenWidth(getApplicationContext()),getScreenHeight(getApplicationContext()));
        System.out.println("DESIRED_PREVIEW_SIZE " + DESIRED_PREVIEW_SIZE);*/
        return DESIRED_PREVIEW_SIZE;

    }

    @Override
    protected String getOverlayUrl() {
        return modelOverlayUrl;
    }

    @Override
    protected List<StepBean> getStepBean() {
        return stepsBeanList;
    }

    public  void intentAction(){
            Intent intent = new Intent(ClassifierActivity.this, CameraActivity.class);
            startActivity(intent);
            finish();

    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        recreateClassifier(getModel(), getDevice(), getNumThreads());
        if (classifier == null) {
            LOGGER.e("No classifier on preview!");
            return;
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    }

    @Override
    public void processImage() {
        progressBar = findViewById(R.id.progress1);
 /*      PreferenceStorage preferenceStorage = new PreferenceStorage(this);
        modelName = preferenceStorage.getModelName();
*/


//        modelLableName = prefereneStorage.getModelcLabel();
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final int cropSize = Math.min(previewWidth, previewHeight);

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        if (classifier != null) {
                            final long startTime = SystemClock.uptimeMillis();

                                final List<Classifier.Recognition> previewResults = classifier.recognizeImage(rgbFrameBitmap, sensorOrientation);
                                LOGGER.v("Detect: %s", previewResults);

                                runOnUiThread(
                                        new Runnable() {
                                            @SuppressLint({"WrongConstant", "SetTextI18n"})
                                            @Override
                                            public void run() {
                                                /*resultTextView.setText(String.format("%.2f", previewResults.get(0).getConfidence() * 100f)+"----"+
                                                        previewResults.get(0).getTitle()+"\n"+
                                                        String.format("%.2f", previewResults.get(1).getConfidence() * 100f)+"----"+
                                                        previewResults.get(1).getTitle()+"\n");*//*+
                                                        String.format("%.2f", previewResults.get(2).getConfidence() * 100f)+"----"+
                                                        previewResults.get(2).getTitle()+"\n")*//*;*/
                                               /* PreferenceStorage preferenceStorage = new PreferenceStorage(ClassifierActivity.this);
                                                preferenceStorage.setModelName(modelDetails.getModelName());
                                                preferenceStorage.setModelLabel(modelDetails.getModelLabel());*/
//                                                        preferenceStorage.setModelName(modelName);

                                                progressBar.setVisibility(View.GONE);
                                                Details.getInstance().getCallback().onSuccessOffline(previewResults);
                                                finish();
                                            }

                                        });
                               readyForNextImage();
                        }
                    }
                });
    }



    @Override
    protected void onInferenceConfigurationChanged() {
        if (rgbFrameBitmap == null) {
            // Defer creation until we're getting camera frames.
            return;
        }
        final Classifier.Device device = getDevice();
        final Classifier.Model model = getModel();
        final int numThreads = getNumThreads();
        runInBackground(() -> recreateClassifier(model, device, numThreads));
    }

    private void recreateClassifier(Classifier.Model model, Classifier.Device device, int numThreads) {
        if (classifier != null) {
            LOGGER.d("Closing classifier.");
            classifier.close();
            classifier = null;
        }
        if (device == Classifier.Device.GPU
                && (model == Classifier.Model.QUANTIZED_MOBILENET || model == Classifier.Model.QUANTIZED_EFFICIENTNET)) {
            LOGGER.d("Not creating classifier: GPU doesn't support quantized models.");
            runOnUiThread(
                    () -> {
                        Toast.makeText(this, R.string.tfe_ic_gpu_quant_error, Toast.LENGTH_LONG).show();
                    });
            return;
        }
        try {
            LOGGER.d(
                    "Creating classifier (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
            classifier = Classifier.create(this, model, device, numThreads, "new", modelFileName, modelLabelName);
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.e(e, "Failed to create classifier.");
            runOnUiThread(
                    () -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    });
            return;
        }

        // Updates the input image size.
        imageSizeX = classifier.getImageSizeX();
        imageSizeY = classifier.getImageSizeY();
    }

    void initViewAction() {
        startCapture = findViewById(R.id.startCapture);
        ivClose = findViewById(R.id.image_close);
       /* isVideoRecordStarted = false;*/
        startCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processImage();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(!pauseCall)
            closeActivity();
    }

    private void closeActivity() {
        finish();
    }


    public static byte[] convertBitmapToByte(Bitmap image) {
        Bitmap bmp = image;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static String encryptImage(byte[] imageArray) {
        String s = Base64.encodeToString(imageArray, Base64.NO_WRAP).trim();
        return s;
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        // EventBus.getDefault().register(this);
        initViewAction();
    }

    @Override
    public void onBackPressed() {
        pauseCall=true;
        closeActivity();
    }

}
