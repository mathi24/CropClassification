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

package com.jsn.cropclassification.model;

import static java.lang.Math.min;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Build;
import android.os.SystemClock;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.nnapi.NnApiDelegate;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.prefs.PreferenceChangeEvent;

/**
 * A classifier specialized to label images using TensorFlow Lite.
 */
public abstract class Classifier {
    public static final String TAG = "ClassifierWithSupport";

    /**
     * The model type used for classification.
     */
    public enum Model {
        FLOAT_MOBILENET,
        QUANTIZED_MOBILENET,
        FLOAT_EFFICIENTNET,
        QUANTIZED_EFFICIENTNET
    }

    /**
     * The runtime device type used for executing classification.
     */
    public enum Device {
        CPU,
        NNAPI,
        GPU
    }

    /**
     * Number of results to show in the UI.
     */
    private static final int MAX_RESULTS = 3;

    /** The loaded TensorFlow Lite model. */

    /**
     * Image size along the x axis.
     */
    private final int imageSizeX;

    /**
     * Image size along the y axis.
     */
    private final int imageSizeY;

//    MainModelDetails.ModelDetails modelDetails;
    /**
     * Optional GPU delegate for accleration.
     */
//   private GpuDelegate gpuDelegate = null;

    /**
     * Optional NNAPI delegate for accleration.
     */
    private NnApiDelegate nnApiDelegate = null;

    /**
     * An instance of the driver class to run model inference with Tensorflow Lite.
     */
    protected Interpreter tflite;


    /**
     * Options for configuring the Interpreter.
     */
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    /**
     * Labels corresponding to the output of the vision model.
     */
    private final List<String> labels;

    /**
     * Input image TensorBuffer.
     */
    private TensorImage inputImageBuffer;

    /**
     * Output probability TensorBuffer.
     */
    private final TensorBuffer outputProbabilityBuffer;

    /**
     * Processer to apply post processing of the output probability.
     */
    private final TensorProcessor probabilityProcessor;

    /**
     * Creates a classifier with the provided configuration.
     *
     * @param activity   The current Activity.
     * @param model      The model to use for classification.
     * @param device     The device to use for classification.
     * @param numThreads The number of threads to use for classification.
     * @return A classifier with the desired configuration.
     */
    public static Classifier create(Activity activity, Model model, Device device, int numThreads, String s, String modelFileName,
                                    List<String> modelLabel)
            throws IOException {
    /*if (model == Model.QUANTIZED_MOBILENET) {
      return new ClassifierQuantizedMobileNet(activity, device, numThreads, s);
    } else if (model == Model.FLOAT_MOBILENET) {
      return new ClassifierFloatMobileNet(activity, device, numThreads, s);
    } else if (model == Model.FLOAT_EFFICIENTNET) {
      return new ClassifierFloatEfficientNet(activity, device, numThreads, s);
    } else*/
        if (model == Model.QUANTIZED_EFFICIENTNET) {
          //  return new ClassifierQuantizedEfficientNet(activity, device, numThreads, s, modelFileName, modelLabel);
            return new ClassifierFloatEfficientNet(activity, device, numThreads, s, modelFileName,modelLabel);
        }

      /*  if (model == Model.FLOAT_EFFICIENTNET) {
            return new ClassifierFloatEfficientNet(activity, device, numThreads, s, modelFileName, modelLabel);
        }*/else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    public static class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private final String id;

        /**
         * Display name for the recognition.
         */
        private final String title;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private final Float confidence;

//        private final Double confidence;
        /**
         * Optional location within the source image for the location of the recognized object.
         */
        private RectF location;

        public Recognition(
                final String id, final String title, final Float confidence, final RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        public RectF getLocation() {
            return new RectF(location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }

            if (location != null) {
                resultString += location + " ";
            }

            return resultString.trim();
        }
    }

    private static MappedByteBuffer loadModelFile(@NonNull Context context, @NonNull String filePath) throws IOException {
        // SupportPreconditions.checkNotNull(context, "Context should not be null.");
        // SupportPreconditions.checkNotNull(filePath, "File path cannot be null.");
        // File file = new File(Environment.getExternalStorageDirectory() + "/" + Utils.downloadDirectory + "/" + filePath);
        File file = context.getExternalFilesDir(null);
        String fileDir = null;
        String dir = null;
        if (null != file) {
            dir = file.getAbsolutePath();
        }

        String packageName = context.getPackageName();
        if (!TextUtils.isEmpty(dir)) {
            if (!dir.endsWith(File.separator)) {
                fileDir = dir + File.separator  + filePath;
            } else {
                fileDir = dir + packageName + File.separator + filePath;
            }
        }
        File modelFile = new File(fileDir);
        MappedByteBuffer var9;
        try {
            FileInputStream inputStream = new FileInputStream(modelFile);
            try {
                FileChannel fileChannel = inputStream.getChannel();
                var9 = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, modelFile.length());
            } catch (Throwable var12) {
                try {
                    inputStream.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
                throw var12;
            }
            inputStream.close();
        } catch (Throwable var13) {
            throw var13;
        }
        return var9;
    }


    /**
     * Initializes a {@code Classifier}.
     */

    protected Classifier(Activity activity, Device device, int numThreads, String s, String modelFileName,
                         List<String> modelLabel) throws IOException {
        MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(activity,modelFileName);
//        MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(activity, "testmodel.tflite");
//        PreferenceStorage preferenceStorage = new PreferenceStorage(activity.getApplicationContext());
//        MappedByteBuffer tfliteModel =null;
/*        if(!preferenceStorage.getModelUrl().equals("")){
             tfliteModel = FileUtil.loadMappedFile(activity,preferenceStorage.getModelName());
            //tfliteModel = FileUtil.loadMappedFile(activity,"modelnew.tflite");
        }else {
             tfliteModel = FileUtil.loadMappedFile(activity, "modelnew.tflite");
        }*/
        switch (device) {
            case NNAPI:
                nnApiDelegate = new NnApiDelegate();
                tfliteOptions.addDelegate(nnApiDelegate);
                break;
/*            case GPU:
                gpuDelegate = new GpuDelegate();
                tfliteOptions.addDelegate(gpuDelegate);
                break;*/
            case CPU:
                tfliteOptions.setUseXNNPACK(true);
                break;
        }
        tfliteOptions.setNumThreads(numThreads);

       /* if (!preferenceStorage.getModelUrl().equals(""))
        tflite = new Interpreter(loadModelFile(activity, modelFileName), tfliteOptions);
        else
        tflite = new Interpreter(tfliteModel, tfliteOptions);
*/
     //   tflite = new Interpreter(loadModelFile(activity, modelFileName), tfliteOptions);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        labels = modelLabel;
       // labels = FileUtil.loadLabels(activity, getLabelPath());

        // Loads labels out from the label file.
      //  labels = FileUtil.loadLabels(activity, getLabelPath());

        // Reads type and shape of input and output tensors, respectively.
        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();
        int probabilityTensorIndex = 0;
        int[] probabilityShape =
                tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
        DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

        // Creates the input tensor.
        inputImageBuffer = new TensorImage(imageDataType);

        // Creates the output tensor and its processor.
        outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);

        // Creates the post processor for the output probability.
        probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

        Log.d(TAG, "Created a Tensorflow Lite Image Classifier.");
    }


    public static Bitmap cropedImage(Bitmap picture, int width, int height, int imageCropX, int imageCropY, int imageCropWidth, int imageCropHeight) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, width, height, true);
        Bitmap cropedBitmap = Bitmap.createBitmap(scaledBitmap, imageCropX, imageCropY, imageCropWidth, imageCropHeight);
        return cropedBitmap;
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    /**
     * Runs inference and returns the classification results.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public List<Recognition> recognizeImage(final Bitmap bitmap, int sensorOrientation) {
        // Logs this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage");
        Trace.beginSection("loadImage");
        long startTimeForLoadImage = SystemClock.uptimeMillis();
        inputImageBuffer = loadImage(bitmap, sensorOrientation);
        long endTimeForLoadImage = SystemClock.uptimeMillis();
        Trace.endSection();
        Log.v(TAG, "Timecost to load the image: " + (endTimeForLoadImage - startTimeForLoadImage));

        // Runs the inference call.
        Trace.beginSection("runInference");
        long startTimeForReference = SystemClock.uptimeMillis();
        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
        long endTimeForReference = SystemClock.uptimeMillis();
        Trace.endSection();
        Log.v(TAG, "Timecost to run model inference: " + (endTimeForReference - startTimeForReference));

        // Gets the map of label and probability.
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        Trace.endSection();

        // Gets top-k results.
        return getTopKProbability(labeledProbability);
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public List<Recognition> recognizeImage(final Bitmap bitmap, int sensorOrientation, boolean cropBool,
                                            int width, int height, int imageCropX, int imageCropY, int imageCropWidth, int imageCropHeight) {
        // Logs this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage");
        //Bitmap rightQtrBitmap = cropedImage(bitmap,width,height,220,0,200,160);
        Bitmap previewBitmap;
        if (cropBool)
            previewBitmap = cropedImage(bitmap, width, height, imageCropX, imageCropY, imageCropWidth, imageCropHeight);
        else
            previewBitmap = bitmap;

        int colorBitmap = getDominantColor(previewBitmap);
        int redValue = Color.red(colorBitmap);
        int blueValue = Color.blue(colorBitmap);
        int greenValue = Color.green(colorBitmap);

        ColorUtils colorUtils = new ColorUtils();
        String colorStr = colorUtils.getColorNameFromRgb(redValue,greenValue,blueValue);
        if(colorStr.equals("Blue")){
           System.out.println("COLORSCAN_BLUE");
           if(blueValue>greenValue && blueValue > redValue){
               if((blueValue-greenValue)>50){

               }
           }
        }
        Trace.beginSection("loadImage");
        long startTimeForLoadImage = SystemClock.uptimeMillis();
        inputImageBuffer = loadImage(previewBitmap, sensorOrientation);
        long endTimeForLoadImage = SystemClock.uptimeMillis();
        Trace.endSection();
        Log.v(TAG, "Timecost to load the image: " + (endTimeForLoadImage - startTimeForLoadImage));

        // Runs the inference call.
        Trace.beginSection("runInference");
        long startTimeForReference = SystemClock.uptimeMillis();
        tflite.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
        long endTimeForReference = SystemClock.uptimeMillis();
        Trace.endSection();
        Log.v(TAG, "Timecost to run model inference: " + (endTimeForReference - startTimeForReference));

        // Gets the map of label and probability.
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        Trace.endSection();

        // Gets top-k results.
        return getTopKProbability(labeledProbability);
    }
*/

    /**
     * Closes the interpreter and model to release resources.
     */
    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
      /*  if (gpuDelegate != null) {
            gpuDelegate.close();
            gpuDelegate = null;
        }*/
        if (nnApiDelegate != null) {
            nnApiDelegate.close();
            nnApiDelegate = null;
        }
    }

    /**
     * Get the image size along the x axis.
     */
    public int getImageSizeX() {
        return imageSizeX;
    }

    /**
     * Get the image size along the y axis.
     */
    public int getImageSizeY() {
        return imageSizeY;
    }

    /**
     * Loads input image, and applies preprocessing.
     */
    private TensorImage loadImage(final Bitmap bitmap, int sensorOrientation) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = min(bitmap.getWidth(), bitmap.getHeight());
        int numRotation = sensorOrientation / 90;
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        // TODO(b/169379396): investigate the impact of the resize algorithm on accuracy.
                        // To get the same inference results as lib_task_api, which is built on top of the Task
                        // Library, use ResizeMethod.BILINEAR.
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeMethod.NEAREST_NEIGHBOR))
                        .add(new Rot90Op(numRotation))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    /**
     * Gets the top-k results.
     */
    private static List<Recognition> getTopKProbability(Map<String, Float> labelProb) {
        // Find the best classifications.
        PriorityQueue<Recognition> pq =
                new PriorityQueue<>(
                        MAX_RESULTS,
                        new Comparator<Recognition>() {
                            @Override
                            public int compare(Recognition lhs, Recognition rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());

                            }
                        });

        for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
            pq.add(new Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
        }

        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

    /**
     * Gets the name of the model file stored in Assets.
     */
    protected abstract String getModelPath();

    /**
     * Gets the name of the label file stored in Assets.
     */
    protected abstract String getLabelPath();

    /**
     * Gets the TensorOperator to nomalize the input image in preprocessing.
     */
    protected abstract TensorOperator getPreprocessNormalizeOp();

    /**
     * Gets the TensorOperator to dequantize the output probability in post processing.
     *
     * <p>For quantized model, we need de-quantize the prediction with NormalizeOp (as they are all
     * essentially linear transformation). For float model, de-quantize is not required. But to
     * uniform the API, de-quantize is added to float model too. Mean and std are set to 0.0f and
     * 1.0f, respectively.
     */
    protected abstract TensorOperator getPostprocessNormalizeOp();
}
