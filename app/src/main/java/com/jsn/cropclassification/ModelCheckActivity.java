package com.jsn.cropclassification;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jsn.cropclassification.activity.CameraActivity;
import com.jsn.cropclassification.activity.ClassifierActivity;
import com.jsn.cropclassification.database.SQLiteHandler;
import com.jsn.cropclassification.model.ApiClient;
import com.jsn.cropclassification.model.CropDetails;
import com.jsn.cropclassification.model.MainModelDetails;
import com.jsn.cropclassification.utils.Utility;
import com.jsn.cropclassification.view.Details;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelCheckActivity extends AppCompatActivity {

    SQLiteHandler sqLiteHandler;
    TextView responseText;
    protected CoordinatorLayout coordinatorLayout;
    File externalFilesDir ;
    int modelCheckCount;
    public String modelName;
    public int modelId;
    private RequestBody requestBody;
//    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_check);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        responseText = findViewById(R.id.text_config);

        sqLiteHandler = new SQLiteHandler(this);
        externalFilesDir = getExternalFilesDir(null);

        callClassificationTflite(requestBody);


    }

    private void callClassificationTflite(RequestBody requestBody) {

        {

            if (Utility.isNetworkConnected(this)) {
//                progressBar.setVisibility(View.VISIBLE);
               Call<MainModelDetails> call = ApiClient.getInstance().getApi().modelTflite();

              call.enqueue(new Callback<MainModelDetails>() {
                  @Override
                  public void onResponse(Call<MainModelDetails> call, Response<MainModelDetails> response) {
                      int status = Integer.parseInt(response.body().getSatutsCode());

                      if (response.isSuccessful()) {
                          if (response.body() != null && status == 200) {
                           MainModelDetails.ModelDetails modelDetails = new MainModelDetails.ModelDetails();
                             modelDetails = response.body().getModelDetails();

                             PreferenceStorage preferenceStorage = new PreferenceStorage(ModelCheckActivity.this);
                             preferenceStorage.setModelName(modelDetails.getModelName());
                             preferenceStorage.setModelLabel(modelDetails.getModelLabel());
                             preferenceStorage.setModelUrl(modelDetails.getModelUrl());
                             modelCheck(modelDetails);

                             /* modelDetails.getModelId();
                              modelDetails.getModelVersion();*/
                            Intent intent = new Intent(ModelCheckActivity.this, CameraActivity.class);
                            startActivity(intent);
                            finish();

                          }
                      }
                  }

                  @Override
                  public void onFailure(Call<MainModelDetails> call, Throwable t) {

                  }

              });
            }else{

                Intent intent = new Intent(ModelCheckActivity.this, ClassifierActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }

    public void modelCheck(MainModelDetails.ModelDetails modelDetails){

//        callClassificationTflite(requestBody);

       int modelId = Integer.parseInt(String.valueOf(modelDetails.getModelId()));
       String modelVersion = (modelDetails.getModelVersion());

        if (sqLiteHandler.getModelDownloadStatus(1,modelId,modelVersion)){
              if (sqLiteHandler.getModelDownloadStatus(2, modelId,modelVersion)){
                    if (modelExist(modelDetails.getModelName()));
//                        modelCheckCount++;
                 else
                     new DownloadingTask().execute(new MyTaskParams(modelDetails.getModelUrl(),modelDetails.getModelName(),
                            modelDetails.getModelId(), modelDetails.getModelVersion()));

              } else {
                  new DownloadingTask().execute(new MyTaskParams(modelDetails.getModelUrl(),modelDetails.getModelName(),
                          modelDetails.getModelId(), modelDetails.getModelVersion()));
            }
          } else {
                sqLiteHandler.tflite(modelDetails);
                        /*(Integer.parseInt(modelDetails.getModelId()),modelDetails.getModelVersion(),
                        modelDetails.getModelName(),modelDetails.getModelConfidence(),modelDetails.getModelLabel(),modelDetails.getIteration_id()
                ,modelDetails.getModelUrl()));*/
                new DownloadingTask().execute(new MyTaskParams(modelDetails.getModelUrl(),modelDetails.getModelName(),
                        modelDetails.getModelId(), modelDetails.getModelVersion()));

        }

    }

    public class MyTaskParams {
        String modelUrl;
        String modelName;
        int modelId;
        String modelVersion;

        public MyTaskParams(String modelUrl, String modelName, int modelId, String modelVersion) {
            this.modelUrl = modelUrl;
            this.modelName = modelName;
            this.modelId = modelId;
            this.modelVersion = modelVersion;
        }
    }

    private class DownloadingTask extends AsyncTask<MyTaskParams, Void, Void> {
        // SQLiteHandler  db = new SQLiteHandler(getApplicationContext());
        String apkStorage = null;
        File externalFilesDir = getExternalFilesDir(null);
        File outputFile = null;
        String dir = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
//                    modelCheckCount++;
                    //  Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();

                } else {
                    Log.e(TAG, "Download Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());
            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(MyTaskParams... params) {
            try {
                URL url = new URL(params[0].modelUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
               c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
//                c.setRequestMethod("POST");
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                if (null != externalFilesDir) {
                    dir = externalFilesDir.getAbsolutePath();
                }
                String packageName = getPackageName();
                if (!TextUtils.isEmpty(dir)) {
                    if (!dir.endsWith(File.separator)) {
                        apkStorage = dir + File.separator ;
                    } else {
                        apkStorage = dir + packageName + File.separator;
                    }
                    try {
                        outputFile = new File(apkStorage,params[0].modelName);
                        if (!outputFile.exists()) {
                            outputFile.createNewFile();
                            Log.e(TAG, "File Created");
                        }

                       /* FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();*/

                        FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                        InputStream is = c.getInputStream();//Get InputStream for connection

                        byte[] buffer = new byte[1024];//Set buffer type
                        int len1 = 0;//init length
                        while ((len1 = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len1);//Write new file
                        }

                        //Close all connection after doing task
                        fos.close();
                        is.close();
//                        sqLiteHandler.updateDownloadStatus(Integer.parseInt(params[0].modelId), Integer.parseInt(params[0].modelVersion));

                    } catch (Exception e) {
                        //  ToastUtil.showToast(context, e.getMessage(), true);
                        e.printStackTrace();
                        outputFile = null;
//                        snackBarRefresh(ModelCheckActivity.this, coordinatorLayout, getString(R.string.error_msg_not_downloaded));
                        Log.e(TAG, "Download Error Exception " + e.getMessage());
                    }
                }


            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
//                snackBarRefresh(ModelCheckActivity.this, coordinatorLayout, getString(R.string.error_msg_not_downloaded));
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }


    private boolean modelExist(String modelName) {
        boolean modelExist = true;
        String filePath = null;
        File externalFilesDir = getExternalFilesDir(null);
        String dir = null;

        if (null != externalFilesDir) {
            dir = externalFilesDir.getAbsolutePath();
        }
        String packageName = getPackageName();
        if (!TextUtils.isEmpty(dir)) {
            if (!dir.endsWith(File.separator)) {
                filePath = dir + File.separator;
            } else {
                filePath = dir + packageName + File.separator;
            }

            File file = new File(filePath, modelName);
            if (!file.exists()) {
                return false;
            }
        }
        return modelExist;
    }
}