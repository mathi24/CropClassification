package com.jsn.cropclassification.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsn.cropclassification.ModelCheckActivity;
import com.jsn.cropclassification.model.Classifier;
import com.jsn.cropclassification.model.CropDetails;
import com.jsn.cropclassification.tflite.LiteClassifierActivity;
import com.jsn.cropclassification.view.Details;
import com.jsn.cropclassification.view.JSONObjectCallBack;
import com.jsn.cropclassification.R;
import com.jsn.cropclassification.utils.Utility;
import com.jsn.cropclassification.adapter.CropListAdapter;
import com.jsn.cropclassification.adapter.CropListOfflineAdapter;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

//    private static final String TAG = LoginActivity.class.getSimpleName();
    public Button btnLogin;
    ListView lstVCrops,lstVCropOffline;
    RelativeLayout relCrops;
    private CropListAdapter adapter;
    private CropListOfflineAdapter offlineAdapter;
    private List<CropDetails.Crops> details;
    TextView crop_name_new;
    List<Classifier.Recognition> previewResults;
    List<Classifier.Recognition> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.bt_login);
        crop_name_new = findViewById(R.id.crop_name_new);
        relCrops = findViewById(R.id.rel_crops);
        lstVCrops = findViewById(R.id.lstV_Crops);
        lstVCropOffline = findViewById(R.id.lstV_Crops1);


        Details.getInstance().setCallback(jsonObjectCallBack);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                startActivity(intent);
               /* if (Utility.isNetworkConnected(LoginActivity.this)){
                    Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(LoginActivity.this, ClassifierActivity.class);
//                    Intent intent = new Intent(LoginActivity.this, LiteClassifierActivity.class);
                    startActivity(intent);
                }*/
            }
        });
    }

    JSONObjectCallBack jsonObjectCallBack = new JSONObjectCallBack() {

        @Override
        public void onSuccess(List<CropDetails.Crops> details1) {
            details = new ArrayList<>();
            details=details1;

            if(details.size()>0){
                relCrops.setVisibility(View.VISIBLE);
            }else{
                relCrops.setVisibility(View.GONE);
            }
            lstVCropOffline.setVisibility(View.GONE);
            lstVCrops.setVisibility(View.VISIBLE);
            adapter = new CropListAdapter(details, LoginActivity.this);
            lstVCrops.setAdapter(adapter);

           /* if (selectedImage.equals("1")){

                cropName1.setText(response);
                cropProbability1.setText(response1);

            }else if (selectedImage.equals("2")){
                cropName2.setText(response);
                cropProbability2.setText(response1);


            }else if (selectedImage.equals("3")){
                cropName3.setText(response);
                cropProbability3.setText(response1);

            }*/
        }

        @Override
        public void onSuccessOffline(List<Classifier.Recognition> details) {
            previewResults = new ArrayList<>();
            previewResults=details;

            if(details.size()>0){
                relCrops.setVisibility(View.VISIBLE);
            }else{
                relCrops.setVisibility(View.GONE);
            }
            lstVCropOffline.setVisibility(View.VISIBLE);
            lstVCrops.setVisibility(View.GONE);
            offlineAdapter = new CropListOfflineAdapter(previewResults, LoginActivity.this);
            lstVCropOffline.setAdapter(offlineAdapter);
        }

        @Override
        public void onFailure(String s) {

        }
    };

      /*  @Override
        public void onSuccessOffline(List<Classifier.Recognition> details) {
            results = new ArrayList<>();

            results=details;

            if(details.size()>0){
                relCrops.setVisibility(View.VISIBLE);
            }else{
                relCrops.setVisibility(View.GONE);
            }
            lstVCropOffline.setVisibility(View.VISIBLE);
            lstVCrops.setVisibility(View.GONE);
            offlineAdapter = new CropListOfflineAdapter(results, LoginActivity.this);
            lstVCropOffline.setAdapter(offlineAdapter);
        }

        @Override
        public void onFailure(String s) {

        }
    };*/
}