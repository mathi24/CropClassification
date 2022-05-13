package com.jsn.cropclassification.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsn.cropclassification.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image1,image2,image3;
    private TextView selectedCrop,cropName1,cropName2,cropName3,cropProbability1,cropProbability2,cropProbability3;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById();

        //Details.getInstance().setCallback(jsonObjectCallBack);

    }

/*
    JSONObjectCallBack jsonObjectCallBack = new JSONObjectCallBack() {

        @Override
        public void onSuccess(String response, String response1,String selectedImage) {
         if (selectedImage.equals("1")){

             cropName1.setText(response);
             cropProbability1.setText(response1);

         }else if (selectedImage.equals("2")){
             cropName2.setText(response);
             cropProbability2.setText(response1);


         }else if (selectedImage.equals("3")){
             cropName3.setText(response);
             cropProbability3.setText(response1);

            }
        }

        @Override
        public void onFailure(String s) {

        }
    };
*/

    private void findViewById(){
        image1 = findViewById(R.id.image_view1);
        image2 = findViewById(R.id.image_view2);
        image3 = findViewById(R.id.image_view3);
//        selectedCrop = findViewById(R.id.selected_crop);
        spinner = findViewById(R.id.drop_down);
        cropName1 = findViewById(R.id.crop_name1);
        cropName2 = findViewById(R.id.crop_name2);
        cropName3 = findViewById(R.id.crop_name3);
        cropProbability1 = findViewById(R.id.crop_probability1);
        cropProbability2 = findViewById(R.id.crop_probability2);
        cropProbability3 = findViewById(R.id.crop_probability3);




        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);

/*
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selected_val=spinner.getSelectedItem().toString();
                    selectedCrop.setText(selected_val);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
*/
    }


    @Override
    public void onClick(View view) {
        if(view == image1){
            Intent intent = new Intent(StartActivity.this, CameraActivity.class);
            intent.putExtra("selectedImage","1");
            startActivity(intent);
        }else  if(view == image2){
            Intent intent = new Intent(StartActivity.this,CameraActivity.class);
            intent.putExtra("selectedImage","2");
            startActivity(intent);
        }else  if(view == image3){
            Intent intent = new Intent(StartActivity.this,CameraActivity.class);
            intent.putExtra("selectedImage","3");
            startActivity(intent);
        }
    }
}