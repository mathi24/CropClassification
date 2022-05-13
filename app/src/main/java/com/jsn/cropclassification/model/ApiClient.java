package com.jsn.cropclassification.model;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public String BASE_URL ="https://cropclassificationapi.azurewebsites.net/";
    private static ApiClient mInstance;
    private Retrofit retrofit;

    private ApiClient(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.MINUTES);
        httpClient.readTimeout(15, TimeUnit.SECONDS);
        httpClient.writeTimeout(15, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized ApiClient getInstance(){
     if (mInstance == null){
         mInstance = new ApiClient();
     }
     return mInstance;
    }
    public Api getApi(){

        return retrofit.create(Api.class);
    }
}
