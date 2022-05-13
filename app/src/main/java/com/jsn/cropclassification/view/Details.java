package com.jsn.cropclassification.view;

import android.app.Application;
import android.content.Context;


public class Details extends Application {
    protected JSONObjectCallBack mFACallback;

    public static String result="";

    public  JSONObjectCallBack getCallback() {
        return mFACallback;
    }

    public  void setCallback(JSONObjectCallBack FACallback) {
        mFACallback = FACallback;
    }

    private static Details instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        /*try {
            CaocConfig.Builder.create()
                    .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                    .enabled(true) //default: true
                    //.showErrorDetails(false) //default: true
                    .showRestartButton(false) //default: true
//                    .trackActivities(false) //default: false
                    .minTimeBetweenCrashesMs(1) //default: 3000
                    .errorActivity(VchekErrorActivity.class)
                 //   .restartActivity(VinNumberActivity.class)
                    .showErrorDetails(false)
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public Context getContext() {
        if (instance == null)
            instance = new Details();
        return instance;
    }

    public static synchronized Details getInstance() {
        if (instance == null) {
            instance = new Details();
        }
       // ApplockManager.getInstance().enableDefaultAppLockIfAvailable(instance);
        return instance;
    }

}
