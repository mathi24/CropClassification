package com.jsn.cropclassification;

import android.content.Context;
import android.content.SharedPreferences;

import com.jsn.cropclassification.model.MainModelDetails;

import java.nio.charset.StandardCharsets;


public class PreferenceStorage {

    public static final String KEY_MODEL_NAME = "model_name";
    public static final String KEY_MODEL_LABEL = "model_label";
    public static final String KEY_MODEL_URL= "model_url";
    public static final String KEY_MODEL_CONFIDENCE = "model_confidence";
    public static final String KEY_MODEL_VERSION = "model_version";
    private static PreferenceStorage instance = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MainModelDetails.ModelDetails modelDetails;
    int PRIVATE_MODE = 0;
    Context context;

    public PreferenceStorage(Context context){
        this.context = context;
    }

    public static PreferenceStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (PreferenceStorage.class) {
                if (instance == null) {
                    instance = new PreferenceStorage(context);
                }
            }
        }
        return instance;
    }


    public String getModelName(){
      SharedPreferences sharedPreferences = context.getApplicationContext()
              .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MODEL_NAME,"");
    }

    public void setModelName(String modelName) {

//            SharedPreferences sharedPreferences1 = modelDetails.setModelName(modelName);
            SharedPreferences sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_MODEL_NAME, modelName);
            editor.commit();
    }

    public String getModelVersion(){
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MODEL_VERSION,"");
    }

    public void setModelversion(String modelVersion) {
            SharedPreferences sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_MODEL_VERSION, modelVersion);
            editor.commit();
    }
    public String getModelLabel(){
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MODEL_LABEL,"");
    }

    public void setModelLabel(String modelLabel) {
            SharedPreferences sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_MODEL_LABEL, modelLabel);
            editor.commit();

    }

    public String getModelUrl(){
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MODEL_URL,"");
    }

    public void setModelUrl(String modelUrl) {
            SharedPreferences sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_MODEL_URL, modelUrl);
            editor.commit();

    }
    public String getModelConfidence(){
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MODEL_CONFIDENCE,"");
    }

    public void setModelConfidence(String modelConfidence){
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("modelTflite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MODEL_CONFIDENCE,modelConfidence);
        editor.commit();
    }
}
