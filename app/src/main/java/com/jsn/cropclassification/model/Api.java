package com.jsn.cropclassification.model;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @Headers({"Content_Type: application/octet-stream"})
    @POST("CropClassification")
   Call<CropDetails> newUploadImage(@Body RequestBody Photo );

   @POST("CropClassificationlocalmodel")
    Call<MainModelDetails> modelTflite();



//    Call<MainModelDetails> modelDetails = null;
}
