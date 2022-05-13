package com.jsn.cropclassification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CropDetails implements Serializable {

    @SerializedName("StatusCode")
    @Expose
    private String statusCode;

    @SerializedName("Message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("CropDetails")
    @Expose
    private List<Crops> cropsList;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<Crops> getCropsList() {
        return cropsList;
    }

    public void setCropsList(List<Crops> cropsList) {
        this.cropsList = cropsList;
    }

    public class Crops {
    @SerializedName("crop_name")
    @Expose
    private String cropName;
    @SerializedName("crop_probability")
    @Expose
    private String cropProbability;

        public String getCropName() {
            return cropName;
        }

        public void setCropName(String cropName) {
            this.cropName = cropName;
        }

        public String getCropProbability() {
            return cropProbability;
        }

        public void setCropProbability(String cropProbability) {
            this.cropProbability = cropProbability;
        }
    }
}
