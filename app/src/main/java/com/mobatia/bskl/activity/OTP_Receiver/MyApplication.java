package com.mobatia.bskl.activity.OTP_Receiver;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    private static final String TAG = "MyResponse";
    @Override
    public void onCreate() {
        super.onCreate();
        AppSignatureHelper appSignature = new AppSignatureHelper(this);
        for (String signature : appSignature.getAppSignatures()) {
            Log.e(TAG, "onCreate: " + signature );
        }
    }
}
