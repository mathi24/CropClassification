/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.jsn.cropclassification.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.jsn.cropclassification.model.Classifier;
import com.jsn.cropclassification.view.ResultsView;

import java.util.List;

public class RecognitionScoreView extends View implements ResultsView {
  private static final float TEXT_SIZE_DIP = 16;
  private final float textSizePx;
  private final Paint fgPaint;
  private final Paint bgPaint;
  private List<Classifier.Recognition> results;
  boolean isBeepSound = false;

  public RecognitionScoreView(final Context context, final AttributeSet set) {
    super(context, set);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    fgPaint = new Paint();
    fgPaint.setTextSize(textSizePx);

    bgPaint = new Paint();

  }

  @Override
  public void setResults(final List<Classifier.Recognition> results) {
    this.results = results;
    postInvalidate();
  }

  @Override
  public void onDraw(final Canvas canvas) {
    final int x = 10;
    int y = (int) (fgPaint.getTextSize() * 1.5f);
   // bgPaint.setColor(0xcc4285f4);
  //  bgPaint.setColor(getResources().getColor(R.color.colorPrimary));
  //  canvas.drawPaint(bgPaint);
    if (results != null) {
      for (final Classifier.Recognition recog : results) {
        canvas.drawText(recog.getTitle() + ": " + recog.getConfidence(), x, y, fgPaint);
        y += (int) (fgPaint.getTextSize() * 1.5f);
      }
    }

    /*fgPaint.setColor(getResources().getColor(R.color.colorPrimary));
    if (results != null && results.size() > 0) {
      int y = (int) (fgPaint.getTextSize() * 1.4f);
      final Classifier.Recognition recog = results.get(0);
      final int x = (int) (canvas.getWidth() - fgPaint.measureText(recog.getTitle())) / 2;
      // float w= fgPaint.measureText(recog.getTitle())/2;
      fgPaint.setTextSize(60);
      fgPaint.setTextAlign(Paint.Align.LEFT);
      if (ClassifierActivity.isVideoRecordStarted) {
        if (recog.getTitle().contains("Negative")) {
          fgPaint.setTextSize(40);
          fgPaint.setTextAlign(Paint.Align.CENTER);
          canvas.drawText("Please position camera", x, y - 25, fgPaint);
          canvas.drawText("to view complete label in landscape mode", x, y + 10, fgPaint);
        } else {
          fgPaint.setTextSize(60);
          fgPaint.setTextAlign(Paint.Align.LEFT);
          String strConf = String.format("%.1f", recog.getConfidence() * 100f);
          float fConf = Float.parseFloat(strConf)*100;
          String strLabel = recog.getTitle();
          int confident = 0;
          if (strLabel.equals("Green"))
            confident = CustomVisionStrConstant.CONFIDENCERANGE;
          else
            confident = CustomVisionStrConstant.CONFIDENCERANGEBLUE;
          Log.v("getConfidence", (Math.round(fConf)) + " " + CustomVisionStrConstant.CONFIDENCERANGE);
          if ((Math.round(fConf)) > confident && !isBeepSound) {
            ClassifierActivity.isVideoRecordStarted = false;
            fgPaint.setTextSize(60);
            fgPaint.setTextAlign(Paint.Align.LEFT);
            String tempTitle = recog.getTitle();
            if (tempTitle.equals("Green")) {
              tempTitle = "PASS";
            } else if (tempTitle.equals("Blue")) {
              tempTitle = "FAIL";
            }
            //String title = ((Math.round(fConf)) > CustomVisionStrConstant.CONFIDENCERANGE) ? "  " + recog.getTitle() + " " + "\u2713" + "  " : "  " + recog.getTitle() + "  ";
            String title = " " + tempTitle + " ";
            // canvas.drawText(recog.getTitle() + ", " + String.format("%.1f", recog.getConfidence() * 100f) + "%", x, y, fgPaint);
            //canvas.drawColor(getResources().getColor(R.color.colorBlue));
            fgPaint.setColor(0xFF7CB342);
            float w = fgPaint.measureText(title);
            float textSize = fgPaint.getTextSize();
            MediaPlayer ring = MediaPlayer.create(getContext(), R.raw.long_beep);
            ring.start();
            isBeepSound = true;

            Log.v("hhhhhhh", (x) + " " + (y - textSize) + " " + (x + w) + " " + (y) + " ");
              canvas.drawRoundRect(new RectF(x, y - textSize, x + w, y + 025), 15f, 15f, fgPaint);

              fgPaint.setColor(0xFFFFFFFF);
            canvas.drawText(title + " ", x, y, fgPaint);
          }
        }
      }
    }*/
  }
}
