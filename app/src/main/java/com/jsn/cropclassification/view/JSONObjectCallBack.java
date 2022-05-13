package com.jsn.cropclassification.view;

import com.jsn.cropclassification.model.Classifier;
import com.jsn.cropclassification.model.CropDetails;

import java.util.List;

public interface JSONObjectCallBack {
    public void onSuccess(List<CropDetails.Crops> details);

    public void onSuccessOffline(List<Classifier.Recognition> details);

    public void onFailure(String errorCode);
}
