package com.jsn.cropclassification.model;

import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

public class MainModelDetails {

    @SerializedName("StatusCode")
    private String SatutsCode;

    public String getSatutsCode() {
        return SatutsCode;
    }

    public ModelDetails getModelDetails() {
        return modelDetails;
    }

    @SerializedName("ModelDetails")
    private ModelDetails modelDetails;


    public static class ModelDetails{
        @SerializedName("ModelId")
        int ModelId;
        @SerializedName("ModelVersion")
        String ModelVersion;
        @SerializedName("iteration_id")
        String iteration_id;
        @SerializedName("ModelUrl")
        String ModelUrl;
        @SerializedName("ModelLabel")
        String ModelLabel;
        @SerializedName("ModelConfidence")
        String ModelConfidence;
        @SerializedName("ModelName")
        String ModelName;

        public void setModelId(int modelId) {
            ModelId = modelId;
        }

        public void setModelVersion(String modelVersion) {
            ModelVersion = modelVersion;
        }

        public void setIteration_id(String iteration_id) {
            this.iteration_id = iteration_id;
        }

        public void setModelUrl(String modelUrl) {
            ModelUrl = modelUrl;
        }

        public void setModelLabel(String modelLabel) {
            ModelLabel = modelLabel;
        }

        public void setModelConfidence(String modelConfidence) {
            ModelConfidence = modelConfidence;
        }

        /*public SharedPreferences setModelName(String modelName) {
            ModelName = modelName;
            return null;
        }*/

        public int getModelId() {
            return ModelId;
        }

        public String getModelVersion() {
            return ModelVersion;
        }

        public String getIteration_id() {
            return iteration_id;
        }

        public String getModelUrl() {
            return ModelUrl;
        }

        public String getModelLabel() {
            return ModelLabel;
        }

        public String getModelConfidence() {
            return ModelConfidence;
        }

        public String getModelName() {
            return ModelName;
        }
/* public ModelDetails(int modelId, int modelVersion, int iteration_id,
                             String modelUrl, String modelLabel, int modelConfidence, String modelName) {
             ModelId = modelId;
             ModelVersion = modelVersion;
             this.iteration_id = iteration_id;
             ModelUrl = modelUrl;
             ModelLabel = modelLabel;
             ModelConfidence = modelConfidence;
             ModelName = modelName;
         }
     */

    }


}
