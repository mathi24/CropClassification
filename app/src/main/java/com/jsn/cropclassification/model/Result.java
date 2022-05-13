package com.jsn.cropclassification.model;


import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;


import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Response;


public class Result {

    protected boolean success;

    public boolean isSuccess() {
        return success;
    }

    public static class Data<T> extends Result {

        private T data;

        public T getData() {
            return data;
        }

    }

    public static class Error extends Result {

        @SerializedName("error_msg")
        public String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

    }

/*
    public static <T extends Result> Error buildError(@NonNull Response<T> response) {
        try {
            if(!response.errorBody().string().equals(""))
                return cr(response.errorBody().string(),Error.class);
//            return EntityUtils.gson.fromJson(response.errorBody().string(), Error.class);
            else{
              return toastError(response);
            }
        } catch (IOException | JsonSyntaxException e) {
            return toastError(response);
        }
    }
*/


    public static <T extends Result> Error toastError(@NonNull Response<T> response){
        Error error = new Error();
        error.success = false;
        switch (response.code()) {
            case 400:
                error.errorMessage = "Request parameter is incorrect";
                break;
            case 403:
                error.errorMessage = "The request was rejected";
                break;
            case 404:
                error.errorMessage = "Resources not found";
                break;
            case 405:
                error.errorMessage = "The request mode is not allowed";
                break;
            case 408:
                error.errorMessage = "Request timed out";
                break;
            case 449:
                error.errorMessage = "Exception Occurred";
                break;
            case 499:
                error.errorMessage = "Insufficient Details";
                break;
            case 422:
                error.errorMessage = "Request semantic error";
                break;
            case 500:
                error.errorMessage = "Server logic error";
                break;
            case 502:
                error.errorMessage = "Server gateway error";
                break;
            case 504:
                error.errorMessage = "The server gateway timed out";
                break;
            default:
                error.errorMessage = response.message();
                break;
        }
        return error;
    }

    public static Error buildError(@NonNull Throwable t) {
        Error error = new Error();
        error.success = false;
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            error.errorMessage = "The network can not connect";
        } else if (t instanceof NoRouteToHostException) {
            error.errorMessage = "Unable to access the network";
        } else if (t instanceof SocketTimeoutException) {
            error.errorMessage = "Network access timeout";
        } else if (t instanceof JsonSyntaxException) {
            error.errorMessage = "Response data is malformed";
        } else {
            error.errorMessage = "unknown mistake：" + t.getLocalizedMessage();
        }
        return error;
    }

    public class QRUploadResponse extends Result {

        @SerializedName("ResponseCode")
        private String mResponseCode;

        @SerializedName("Message")
        private String mMessage;

        public String getmMessage() {
            return mMessage;
        }

        public String getmResponseCode() {
            return mResponseCode;
        }

    }

    public class BaseResponse extends Result {

        @SerializedName("ResponseCode")
        private String mResponseCode;

        @SerializedName("Message")
        private String mMessage;

        public String getmMessage() {
            return mMessage;
        }

        public String getmResponseCode() {
            return mResponseCode;
        }

    }

    public class ModelResponse extends Result {

        @SerializedName("ResponseCode")
        private String mResponseCode;

        @SerializedName("Message")
        private String mMessage;

        @SerializedName("activity_details")
        private VinDetails vinDetails;

        @SerializedName("Data")
        private List<ModelData> modelData;

        public String getmMessage() {
            return mMessage;
        }

        public String getmResponseCode() {
            return mResponseCode;
        }

        public List<ModelData> getModelData() {
            return modelData;
        }

        public VinDetails getVinDetails() {
            return vinDetails;
        }


    }

    public class VinDetails extends Result {


        @SerializedName("is_vin_mandatory")
        private int isVinMandat;

        @SerializedName("vin_max_length")
        private int vinMaxLength;

        @SerializedName("is_vin_compare")
        private int isVinCompare;

        @SerializedName("activity_response_message")
        private String mActivityResponseMsg;

        @SerializedName("activity_pass_message")
        private String mActivityPassMsg;

        @SerializedName("activity_fail_message")
        private String mActivityFailMsg;

        public int getIsVinMandat() {
            return isVinMandat;
        }

        public int getVinMaxLength() {
            return vinMaxLength;
        }

        public String getmActivityPassMsg() {
            return mActivityPassMsg;
        }

        public String getmActivityFailMsg() {
            return mActivityFailMsg;
        }

        public String getmActivityResponseMsg() {
            return mActivityResponseMsg;
        }

        public int getIsVinCompare() {
            return isVinCompare;
        }


    }

    public class ModelData extends Result {


        @SerializedName("ml_model_id")
        private String modelId;

        @SerializedName("model_name")
        private String modelName;

        @SerializedName("version_number")
        private String modelVersion;

        @SerializedName("ml_model_label_array")
        private List<String> modelLable;

        @SerializedName("model_pass_image_url")
        private String passImageUrl;

        @SerializedName("model_fail_image_url")
        private String failImageUrl;

        @SerializedName("ml_model_pass_color")
        private String passColor;

        @SerializedName("ml_model_fail_color")
        private String failColor;

        @SerializedName("ml_model_count")
        private String modelCount;

        @SerializedName("ml_model_mark")
        private String modelMark;

        @SerializedName("ml_model_download_url")
        private String modelDownloadUrl;

        @SerializedName("ml_model_type")
        private String modelType;

        @SerializedName("ml_model_file_name")
        private String modelFileName;

        @SerializedName("ml_model_preview")
        private String modelPreview;

        @SerializedName("ml_model_preview_label")
        private String previewLable;

        @SerializedName("ml_model_preview_confidence")
        private String previewConfidence;

        @SerializedName("ml_model_preview_size_height")
        private String previewHeight;

        @SerializedName("ml_model_preview_size_width")
        private String previewWidth;

        @SerializedName("ml_model_crop_x_axis")
        private String cropXAxis;

        @SerializedName("ml_model_crop_y_axis")
        private String cropYAxis;

        @SerializedName("ml_model_crop_width")
        private String cropWidth;

        @SerializedName("ml_model_crop_height")
        private String cropHeight;

        @SerializedName("screen_orientation")
        private String screenOrient;

        @SerializedName("bounding_box_count")
        private String boxCount;

        @SerializedName("sequence_id")
        private String seqNo;

        @SerializedName("pass_label")
        private List<String> passLabel;

        @SerializedName("pass_message")
        private String passMsg;

        @SerializedName("pass_confidence")
        private String passConfidence;

        @SerializedName("fail_label")
        private List<String> failLable;
        @SerializedName("fail_message")
        private String failMsg;

        @SerializedName("fail_confidence")
        private String failConficdence;
        @SerializedName("negative_label")
        private String negativeLabel;
        @SerializedName("negative_message")
        private String negativeMsg;
        @SerializedName("negative_confidence")
        private String negativeConfidence;
        @SerializedName("crop_boolean")
        private String cropBool;

        @SerializedName("ml_model_overlay_url")
        private String modelOverlayUrl;

        @SerializedName("pass_message_color")
        private String passMsgColor;

        @SerializedName("fail_message_color")
        private String failMsgColor;

        @SerializedName("negative_message_color")
        private String negativeMsgColor;

        @SerializedName("step_label")
        private String stepLable;

        @SerializedName("object_of_interest")
        private String objectOfIntrest;

        @SerializedName("model_weighting_time")
        private int waitTime;

        @SerializedName("testing_bottom_crop_height")
        private int bottomCropHeight;

        @SerializedName("is_testing_image_retake")
        private int retake;

        @SerializedName("testing_image_retake_confirm_message")
        private String reTakeMsg;

      /*  @SerializedName("color_name")
        private List<String> colorName;

        @SerializedName("diff_red")
        private int redDiff;

        @SerializedName("diff_green")
        private int greenDiff;

        @SerializedName("diff_blue")
        private int blueDiff;

        @SerializedName("is_color_check")
        private int isColorChek;*/

        @SerializedName("preview_crop_bool")
        private int previewCropBool;

        @SerializedName("preview_crop_x")
        private int pCropX;

        @SerializedName("preview_crop_y")
        private int pCropY;

        @SerializedName("preview_crop_width")
        private int pCropWidth;

        @SerializedName("preview_crop_height")
        private int pCropHeight;

        @SerializedName("model_scope")
        private int modelScope;

        @SerializedName("model_secondary_check")
        private int modelSecondaryChek;

        @SerializedName("api_pass_confidence")
        private int apiPassConfidence;

        @SerializedName("api_fail_confidence")
        private int apiFailConfidence;

        @SerializedName("api_negative_confidence")
        private int apiNegativeConfidence;

        @SerializedName("server_model_url")
        private String serverModelUrl;

        @SerializedName("api_prediction_key")
        private String apiPredictionKey;

        @SerializedName("display_message")
        private String displayMessage;

        private boolean checkStatus;

        public String getSeqNo() {
            return seqNo;
        }
        public String getModelId() {
            return modelId;
        }

        public String getModelName() {
            return modelName;
        }

        public String getModelVersion() {
            return modelVersion;
        }


        public List<String> getModelLable() {
            return modelLable;
        }

        public String getPassImageUrl() {
            return passImageUrl;
        }

        public String getFailImageUrl() {
            return failImageUrl;
        }

        public String getPassColor() {
            return passColor;
        }

        public String getFailColor() {
            return failColor;
        }

        public String getModelCount() {
            return modelCount;
        }

        public String getModelMark() {
            return modelMark;
        }

        public String getModelDownloadUrl() {
            return modelDownloadUrl;
        }

        public String getModelType() {
            return modelType;
        }

        public String getModelFileName() {
            return modelFileName;
        }

        public String getModelPreview() {
            return modelPreview;
        }

        public String getPreviewLable() {
            return previewLable;
        }

        public String getPreviewConfidence() {
            return previewConfidence;
        }

        public String getPreviewHeight() {
            return previewHeight;
        }

        public String getPreviewWidth() {
            return previewWidth;
        }

        public String getCropXAxis() {
            return cropXAxis;
        }

        public String getCropYAxis() {
            return cropYAxis;
        }

        public String getCropWidth() {
            return cropWidth;
        }

        public String getCropHeight() {
            return cropHeight;
        }

        public String getScreenOrient() {
            return screenOrient;
        }

        public String getBoxCount() {
            return boxCount;
        }

        public List<String> getPassLabel() {
            return passLabel;
        }

        public String getPassMsg() {
            return passMsg;
        }

        public String getPassConfidence() {
            return passConfidence;
        }

        public List<String> getFailLable() {
            return failLable;
        }

        public String getFailMsg() {
            return failMsg;
        }

        public String getFailConficdence() {
            return failConficdence;
        }

        public String getNegativeLabel() {
            return negativeLabel;
        }

        public String getNegativeMsg() {
            return negativeMsg;
        }

        public String getNegativeConfidence() {
            return negativeConfidence;
        }

        public String getCropBool() {
            return cropBool;
        }

        public String getModelOverlayUrl() {
            return modelOverlayUrl;
        }

        public boolean isCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(boolean checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getPassMsgColor() {
            return passMsgColor;
        }

        public String getFailMsgColor() {
            return failMsgColor;
        }

        public String getNegativeMsgColor() {
            return negativeMsgColor;
        }

        public String getStepLable() {
            return stepLable;
        }

        public String getObjectOfIntrest() {
            return objectOfIntrest;
        }

        public int getWaitTime() {
            return waitTime;
        }

        public int getBottomCropHeight() {
            return bottomCropHeight;
        }

        public int getModelSecondaryChek() {
            return modelSecondaryChek;
        }

        public int getRetake() {
            return retake;
        }

        public String getReTakeMsg() {
            return reTakeMsg;
        }

     /*   public List<String> getColorName() {
            return colorName;
        }

        public int getRedDiff() {
            return redDiff;
        }

        public int getGreenDiff() {
            return greenDiff;
        }

        public int getBlueDiff() {
            return blueDiff;
        }

        public int getIsColorChek() {
            return isColorChek;
        }*/

        public int getPreviewCropBool() {
            return previewCropBool;
        }

        public int getpCropX() {
            return pCropX;
        }

        public int getpCropY() {
            return pCropY;
        }

        public int getpCropWidth() {
            return pCropWidth;
        }

        public int getpCropHeight() {
            return pCropHeight;
        }

        public int getModelScope() {
            return modelScope;
        }

        public int getApiPassConfidence() {
            return apiPassConfidence;
        }

        public int getApiFailConfidence() {
            return apiFailConfidence;
        }

        public int getApiNegativeConfidence() {
            return apiNegativeConfidence;
        }

        public String getServerModelUrl() {
            return serverModelUrl;
        }

        public String getApiPredictionKey() {
            return apiPredictionKey;
        }

        public String getDisplayMessage() {
            return displayMessage;
        }

    }

}
