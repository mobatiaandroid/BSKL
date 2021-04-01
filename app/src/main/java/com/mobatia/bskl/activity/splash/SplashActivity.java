package com.mobatia.bskl.activity.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.home.HomeActivity;
import com.mobatia.bskl.activity.login.LoginActivity;
import com.mobatia.bskl.activity.tutorial.TutorialActivity;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NameValueConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;


public class SplashActivity extends Activity implements
        IntentPassValueConstants, JSONConstants, URLConstants , NameValueConstants, StatusConstants {

    private Context mContext;
    VideoView videoHolder;
    private static final int APP_API = 17;
    RelativeLayout reltativeTop;
    ImageView reltativeTopIn;
    String FirebaseID;

    /*class C00951 implements MediaPlayer.OnCompletionListener {
        C00951() {
        }

        public void onCompletion(MediaPlayer mp) {
            SplashActivity.this.jump();
        }
    }*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(1);
//        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        setContentView(R.layout.activity_splash);
        mContext = this;
        reltativeTop = findViewById(R.id.reltativeTop);
        reltativeTopIn = findViewById(R.id.reltativeTopIn);
        PreferenceManager.setDataCollection(mContext,"0");
        PreferenceManager.setSuspendTrigger(mContext,"0");
//        PreferenceManager.setDataCollectionTriggerType(mContext,respObj.optString("trigger_type"));
//        SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
//        editor.putString("data_collection_flag", "0");
//        editor.apply();
        if (AppUtils.isNetworkConnected(mContext)) {
            if (FirebaseInstanceId.getInstance().getToken() == null) {
                Intent i = new Intent(SplashActivity.this, SplashActivity.class);
                startActivity(i);
                SplashActivity.this.finish();
            } else {
                FirebaseID = FirebaseInstanceId.getInstance().getToken();
            }
//        try
//        {
//            PackageInfo info = getPackageManager().getPackageInfo( getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures)
//            {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//
//        }
//        catch (NoSuchAlgorithmException e) {
//
//        }
            if (PreferenceManager.getIsFirstLaunch(mContext) && PreferenceManager.getUserId(mContext).equals("")) {
//            reltativeTop.setBackgroundResource(R.color.rel_one);
//            reltativeTopIn.setImageResource(R.drawable.logo_teal);

                reltativeTop.setBackgroundResource(R.color.white);
                reltativeTopIn.setImageResource(R.drawable.logo);
            } else {
                reltativeTop.setBackgroundResource(R.color.black);
                reltativeTopIn.setImageResource(R.color.black);

            }
//            goToNextView();
            postInitParams(mContext);

//        if (isLowerDeviceApi()) {
//            Log.e("Lower", "Lower Device Api");
//            jump();
//            return;
//        }
//        try {
//             videoHolder = (VideoView) findViewById(R.id.videoView);
//            videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash));
//            videoHolder.setOnCompletionListener(new C00951());
//            videoHolder.start();
//        } catch (Exception e) {
//            jump();
//        }
        } else {
            goToNextView();
        }
    }


    private boolean isLowerDeviceApi () {
            try {
                Integer i = Integer.valueOf(Build.VERSION.SDK);
//			LogUtils.logError("Device Api", BuildConfig.VERSION_NAME + i.intValue());
                if (i.intValue() <= APP_API) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        private void jump () {
            if (!isFinishing()) {
                if (AppUtils.checkInternet(mContext)) {
//                AppUtils.postInitParams(mContext, new AppUtils.GetAccessTokenInterface() {
//                    @Override
//                    public void getAccessToken() {
//                    }
//                });
                    postInitParams(mContext);
//                goToNextView();

                /*if (Build.VERSION.SDK_INT < 23) {
                    //Do not need to check the permission
                    goToNextView();

                } else {

                    if (hasPermissions(mContext, permissions)) {
                        goToNextView();

                    } else {
                        ActivityCompat.requestPermissions(this, permissions, 100);
                    }
                }*/
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        }


        public void goToNextView () {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (PreferenceManager.getIsFirstLaunch(mContext) && PreferenceManager.getUserId(mContext).equals("")) {
                        Intent tutorialIntent = new Intent(mContext,
                                TutorialActivity.class);
                        tutorialIntent.putExtra(TYPE, 1);
                        startActivity(tutorialIntent);
                        finish();
                    }
                    else if (PreferenceManager.getUserId(mContext).equals("")) {
                        Intent loginIntent = new Intent(mContext,
                                LoginActivity.class);
                        loginIntent.putExtra("fromsplash", true);
                        startActivity(loginIntent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                    else {
//                    Intent loginIntent = new Intent(mContext,
//                            HomeListActivity.class);
                        if (PreferenceManager.getLoggedInStatus(mContext).equalsIgnoreCase(""))
                        {
                            PreferenceManager.setUserId(mContext,"");
                            ArrayList<OwnContactModel> dummyOwn=new ArrayList<>();
                            ArrayList<KinModel> dummyKin=new ArrayList<>();
                            ArrayList<InsuranceDetailModel> dummyInsurance=new ArrayList<>();
                            ArrayList<PassportDetailModel> dummyPassport=new ArrayList<>();
                            ArrayList<StudentModelNew> dummyStudent=new ArrayList<>();
                            PreferenceManager.saveOwnDetailArrayList(dummyOwn,"OwnContact",mContext);
                            PreferenceManager.saveKinDetailsArrayListShow( dummyKin,mContext);
                            PreferenceManager.saveKinDetailsArrayList( dummyKin,mContext);
                            PreferenceManager.saveInsuranceDetailArrayList(dummyInsurance,mContext);
                            PreferenceManager.savePassportDetailArrayList(dummyPassport,mContext);
                            PreferenceManager.saveInsuranceStudentList(dummyStudent,mContext);
                            Intent loginIntent = new Intent(mContext,
                                    LoginActivity.class);
                            loginIntent.putExtra("fromsplash", true);
                            startActivity(loginIntent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                        else
                        {
                            Intent loginIntent = new Intent(mContext,
                                    HomeActivity.class);
                            loginIntent.putExtra("fromsplash", true);
                            startActivity(loginIntent);
                            overridePendingTransition(0, 0);
                            finish();
                        }

                    }
                }
            }, 500);
        }

        @Override
        protected void onRestart () {
            super.onRestart();
            if (isLowerDeviceApi()) {
                //log.e("Lower", "Lower Device Api");
                jump();
                return;
            }
         /*   try {
                if (videoHolder.isPlaying()) {
                    videoHolder.stopPlayback();
                }
                videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash));
                videoHolder.setOnCompletionListener(new C00951());
                videoHolder.start();
            } catch (Exception e) {
                jump();
            }*/
        }


        public void postInitParams ( final Context mContext){

            final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
            String name[] = {NAME_GRANT_TYPE, NAME_CLIENT_ID, NAME_CLIENT_SECRET, NAME_USERNAME, NAME_PASSWORD};
            String values[] = {VALUE_GRANT_TYPE, VALUE_CLIENT_ID, VALUE_CLIENT_SECRET, VALUE_USERNAME, VALUE_PASSWORD};
            manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    //log.e("response: ", successResponse);
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);

                            if (rootObject != null) {
                                String acccessToken = rootObject.optString(JTAG_ACCESSTOKEN);
                                PreferenceManager.setAccessToken(mContext, acccessToken);
                                if (PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                                {
                                    goToNextView();
                                }
                                else
                                {
                                    deviceRegistration(mContext);
                                }

//							if (PreferenceManager.getUserId(mContext).equals("")) {
//
//								deviceRegistration(mContext);//changed on 22-09-2017
//							}
                            } else {
                                // CustomStatusDialog(RESPONSE_CODE_NULL);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                }
            });
        }
        public void deviceRegistration ( final Context mContext)
        {

            try {
                String androidId = Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                final VolleyWrapper manager = new VolleyWrapper(URL_DEVICE_REGISTRATION);
                String[] name = {JTAG_ACCESSTOKEN, JTAG_DEVICE_ID, JTAG_DEVICE_TYPE,"device_identifier","user_ids"};
                String[] value = {PreferenceManager.getAccessToken(mContext), FirebaseID, "2",androidId,PreferenceManager.getUserId(mContext)};
                //log.e("acc: ", PreferenceManager.getAccessToken(mContext));
                //log.e("dev: ", FirebaseID);
                manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        //log.e("responseREG: ", successResponse);
                        if (successResponse != null) {
                            try {
                                JSONObject rootObject = new JSONObject(successResponse);
                                String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                                if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                    JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                    String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                    if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {
                                        goToNextView();

                                    } else {
                                        goToNextView();

                                    }
                                } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    postInitParams(mContext);


                                }


                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // CustomStatusDialog(RESPONSE_FAILURE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
}




