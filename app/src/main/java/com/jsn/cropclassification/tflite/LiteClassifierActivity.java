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

package com.jsn.cropclassification.tflite;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.camera.core.Camera;
import com.jsn.cropclassification.R;
import com.jsn.cropclassification.model.BorderedText;
import com.jsn.cropclassification.model.Classifier;
import com.jsn.cropclassification.utils.Logger;
import com.jsn.cropclassification.view.Details;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LiteClassifierActivity extends CameraActivity implements OnImageAvailableListener {
  private static final Logger LOGGER = new Logger();
  private static Size DESIRED_PREVIEW_SIZE = new Size(720,1544);
  private static final float TEXT_SIZE_DIP = 10;
  private Bitmap rgbFrameBitmap = null;
  private long lastProcessingTimeMs;
  private Integer sensorOrientation;
  private Classifier classifier;
  private BorderedText borderedText;
  /** Input image size of the model along x axis. */
  private int imageSizeX;
  /** Input image size of the model along y axis. */
  private int imageSizeY;
  private String modelFileName;
  private String modelOverlayUrl;

  ImageView startCapture;
  ImageView ivClose,iv_flash;
  Camera camera1;
  boolean isTourchOnOff1 = false;
  ProgressBar progressBar;

  private List<String> modelLableName;



  @Override
  protected int getLayoutId() {
    return R.layout.tfe_ic_camera_connection_fragment;
  }
 /* void initViewAction1() {
    iv_flash = findViewById(R.id.img_flash_light);

    iv_flash.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (camera1 != null) {
          if (!isTourchOnOff1 && camera1.getCameraInfo().hasFlashUnit()) {
            isTourchOnOff1 = true;
            iv_flash.setImageDrawable(getDrawable(R.drawable.ic_zap_on));
            camera1.getCameraControl().enableTorch(true); // or false
          } else if (isTourchOnOff1 && camera1.getCameraInfo().hasFlashUnit()) {
            isTourchOnOff1 = false;
            iv_flash.setImageDrawable(getDrawable(R.drawable.ic_zap_off));
            camera1.getCameraControl().enableTorch(false);
          }
        }
      }
    });
  }*/
  void initViewAction() {
    startCapture = findViewById(R.id.startCapture);
    ivClose = findViewById(R.id.image_close);
//    iv_flash = findViewById(R.id.img_flash_light);

   /* iv_flash.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (camera1 != null) {
          if (!isTourchOnOff1 && camera1.getCameraInfo().hasFlashUnit()) {
            isTourchOnOff1 = true;
            iv_flash.setImageDrawable(getDrawable(R.drawable.ic_zap_on));
            camera1.getCameraControl().enableTorch(true); // or false
          } else if (isTourchOnOff1 && camera1.getCameraInfo().hasFlashUnit()) {
            isTourchOnOff1 = false;
            iv_flash.setImageDrawable(getDrawable(R.drawable.ic_zap_off));
            camera1.getCameraControl().enableTorch(false);
          }
        }
      }
    });*/

    startCapture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        processImage();
//        progressBar.setVisibility(View.VISIBLE);

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
  public synchronized void onResume() {
    super.onResume();
    // EventBus.getDefault().register(this);
    initViewAction();
  }



  @Override
  protected Size getDesiredPreviewFrameSize() {
    DESIRED_PREVIEW_SIZE = new Size(
            getScreenWidth(getApplicationContext()),getScreenHeight(getApplicationContext()));
    System.out.println("DESIRED_PREVIEW_SIZE " + DESIRED_PREVIEW_SIZE);
    return DESIRED_PREVIEW_SIZE;
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
  protected void processImage() {
//    progressBar = findViewById(R.id.progress1);
    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
    final int cropSize = Math.min(previewWidth, previewHeight);

    runInBackground(
        new Runnable() {
          @Override
          public void run() {
            if (classifier != null) {
              final long startTime = SystemClock.uptimeMillis();
              final List<Classifier.Recognition> results =
                  classifier.recognizeImage(rgbFrameBitmap, sensorOrientation);
              lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
              LOGGER.v("Detect: %s", results);

              runOnUiThread(
                  new Runnable() {
                    @Override
                    public void run() {
//                      progressBar.setVisibility(View.GONE);
                      Details.getInstance().getCallback().onSuccessOffline(results);
                      finish();
                    }
                  });
            }
//            readyForNextImage();
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
        && (model ==Classifier. Model.QUANTIZED_MOBILENET || model ==Classifier. Model.QUANTIZED_EFFICIENTNET)) {
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
      modelFileName = "modelnew.tflite";
      //modelLableName = modelData.get(i).getModelLable();
      modelLableName = new ArrayList<>();
        modelLableName.add("Arecanut");
        modelLableName.add("Coconut");
        modelLableName.add("Groundnut");
        modelLableName.add("Jowar");
        modelLableName.add("Moong Green");
        modelLableName.add("Negative");
        modelLableName.add("Onion");
        modelLableName.add("Paddy");
        modelLableName.add("Ragi");
        modelLableName.add("Rose");
        modelLableName.add("Soyabean");
        modelLableName.add("Sugarcane");
        modelLableName.add("Tomato");
        modelLableName.add("Tur Red Gram");
      classifier = Classifier.create(this, model, device, numThreads,"new", modelFileName, modelLableName);
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
}
