package com.jsn.cropclassification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CropDetailsTflite{

    @SerializedName("StatusCode")
    @Expose
    private String statusCode;

    @SerializedName("ModelDetails")
    @Expose
    private String modelDetails;
}