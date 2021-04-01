package com.mobatia.bskl.activity.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.home.HomeActivity;
import com.mobatia.bskl.activity.login.model.CodeModel;
import com.mobatia.bskl.activity.signup.SignUpActivity;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.settings.model.StudentDetailSettingModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by krishnaraj on 26/06/18.
 */

public class LoginActivity extends Activity implements View.OnTouchListener,
        View.OnClickListener, URLConstants, JSONConstants {

    private Context mContext;
    private EditText mUserNameEdtTxt;
    private EditText mPasswordEdtTxt;
    private Button mNeedpasswordBtn;
    private Button mLoginBtn;
    private Button mSignUpBtn;
    private EditText mMailEdtText;
    private EditText mOtpEditText;
    private EditText text_phone;
    private EditText mEmailText;
    private TextView mAlertphone;
    private Button mHelpButton;
    private RelativeLayout mPrgrsRel;
    String phoneno = "";
    ArrayList<CodeModel> listCode;
    ArrayList<String> listCodeString;
    CountryCodePicker spinnerCode;
    private String code;
    Dialog dialogSignUp;
    Dialog forgotDialog;

    Bundle extras;
    boolean fromsplash = false;
    private RelativeLayout relLogin;
    private RelativeLayout topmainLoginRel;
    private RelativeLayout loginMain;
    ImageView homePageLogoImg;
    ImageView homePageLogoIm2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        PreferenceManager.setIsFirstLaunch(mContext, false);

        initialiseUI();
        setListeners();
        if (fromsplash) {
//            topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.rel_one));
//            loginMain.setBackgroundResource(R.drawable.loginbg_teal);
//            homePageLogoImg.setImageResource(R.drawable.logo_teal);


//            homePageLogoImg.animate().translationY(-100).setDuration(1500).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    Log.d("LOG","onAnimationStart...");
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    Log.d("LOG","onAnimationEnd...");
//                    loginMain.setBackgroundResource(R.color.white);
//                    homePageLogoImg.setImageResource(R.drawable.logo);
//                    topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//                    relLogin.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//                    Log.d("LOG","onAnimationCancel...");
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//                    Log.d("LOG","onAnimationRepeat...");
//                }
//            }).start();
            Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            loginMain.startAnimation(fadein);
            fadein.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    loginMain.setBackgroundResource(R.drawable.loginbg);
                    homePageLogoImg.setImageResource(R.drawable.logo);
                    topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    relLogin.setVisibility(View.VISIBLE);
                 /*   Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_up_to_login);

//        animSlide.setDuration(2200);
                    // Start the animation like this
                    loginMain.startAnimation(animSlide);
                    animSlide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {


                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            loginMain.setBackgroundResource(R.drawable.loginbg);
                            homePageLogoImg.setImageResource(R.drawable.logo);
                            topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                            relLogin.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });*/
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        } else {
            relLogin.setVisibility(View.VISIBLE);
            loginMain.setBackgroundResource(R.drawable.loginbg);
            homePageLogoImg.setImageResource(R.drawable.logo);
            topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }
            /*animSlide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    loginMain.setBackgroundResource(R.color.white);
                    homePageLogoImg.setImageResource(R.drawable.logo);
                    topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    relLogin.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        else {
            relLogin.setVisibility(View.VISIBLE);
            loginMain.setBackgroundResource(R.color.white);
            homePageLogoImg.setImageResource(R.drawable.logo);
            topmainLoginRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }*/
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    @SuppressWarnings("deprecation")
    public void initialiseUI() {

        extras = getIntent().getExtras();
        if (extras != null) {
            fromsplash = extras.getBoolean("fromsplash");
        }
        homePageLogoIm2 = findViewById(R.id.homePageLogoIm2);
        homePageLogoImg = findViewById(R.id.homePageLogoImg);
        relLogin = findViewById(R.id.relLogin);
        topmainLoginRel = findViewById(R.id.topmainLoginRel);
        loginMain = findViewById(R.id.loginMain);
        mPrgrsRel = findViewById(R.id.progressDialog);
        mUserNameEdtTxt = findViewById(R.id.userEditText);
        mPasswordEdtTxt = findViewById(R.id.passwordEditText);

        mUserNameEdtTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                mUserNameEdtTxt.setFocusable(false);
                mUserNameEdtTxt.setFocusableInTouchMode(false);
                mPasswordEdtTxt.setNextFocusForwardId(R.id.passwordEditText);
                mPasswordEdtTxt.setFocusable(true);
                mPasswordEdtTxt.setFocusableInTouchMode(true);
                return false;
            }
        });
        mPasswordEdtTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                mPasswordEdtTxt.setFocusable(false);
                mPasswordEdtTxt.setFocusableInTouchMode(false);
                return false;
            }
        });
        mHelpButton = findViewById(R.id.helpButton);
        mNeedpasswordBtn = findViewById(R.id.forgotPasswordButton);
        mLoginBtn = findViewById(R.id.loginBtn);
        mSignUpBtn = findViewById(R.id.signUpButton);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mNeedpasswordBtn.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.forgot_password,
                            R.drawable.forgot_password_press));
            mLoginBtn.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.login, R.drawable.login_press));
            mSignUpBtn.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.parent_signup, R.drawable.parent_signup));
            mHelpButton.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.email_help, R.drawable.email_help_press));
        } else {
            mNeedpasswordBtn.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.forgot_password,
                            R.drawable.forgot_password_press));
            mLoginBtn.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.login, R.drawable.login_press));
            mSignUpBtn.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.parent_signup, R.drawable.parent_signup));
            mHelpButton.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(mContext,
                            R.drawable.email_help, R.drawable.email_help_press));
        }
    }

    /*******************************************************
     * Method name : setListeners Description : set listeners for UI elements
     * Parameters : nil Return type : void Date :  March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    public void setListeners() {
        mUserNameEdtTxt.setOnTouchListener(this);
        mPasswordEdtTxt.setOnTouchListener(this);
        mNeedpasswordBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mHelpButton.setOnClickListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View,
     * android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v == mUserNameEdtTxt) {
                mUserNameEdtTxt.setFocusable(true);
                mUserNameEdtTxt.setFocusableInTouchMode(true);
            } else if (v == mPasswordEdtTxt) {
                mPasswordEdtTxt.setFocusable(true);
                mPasswordEdtTxt.setFocusableInTouchMode(true);
            } else if (v == mMailEdtText) {
                mMailEdtText.setFocusable(true);
                mMailEdtText.setFocusableInTouchMode(true);
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v == mLoginBtn) {
            // login button
            AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);

            if (mUserNameEdtTxt.getText().toString().trim().equalsIgnoreCase("")) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.exclamationicon, R.drawable.round);

            } else if (!AppUtils.isValidEmail(mUserNameEdtTxt.getText().toString())) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_valid_email), R.drawable.exclamationicon, R.drawable.round);

            } else if (mPasswordEdtTxt.getText().toString().equalsIgnoreCase("")) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_password), R.drawable.exclamationicon, R.drawable.round);

            } else {
                if (AppUtils.isNetworkConnected(mContext)) {
                    LoginApiCall(URL_LOGIN);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }

            }


        } else if (v == mSignUpBtn) {
            // sign up
            AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);

        } else if (v == mNeedpasswordBtn) {
            // need password button
            AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
            if (AppUtils.checkInternet(mContext)) {
                //showForgotpasswordAlertDialog();
                forgotPasswordApiCall();
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        } else if (v == mHelpButton) {
            Intent emailIntent = new Intent(
                    Intent.ACTION_SEND_MULTIPLE);
            String[] deliveryAddress = {"portal@britishschool.edu.my"};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
            emailIntent.setType("text/plain");
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            PackageManager pm = v.getContext().getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(
                    emailIntent, 0);
            System.out.println("packge size" + activityList.size());
            for (final ResolveInfo app : activityList) {
                System.out.println("packge name" + app.activityInfo.name);
                if ((app.activityInfo.name).contains("com.google.android.gm")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(
                            activity.applicationInfo.packageName, activity.name);
                    emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    emailIntent.setComponent(name);
                    v.getContext().startActivity(emailIntent);
                    break;
                }
            }
        }
    }

    /*******************************************************
     * Method name : doValidation Description : do validation of edit texts
     * Parameters : nil Return type : boolean Date :  March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    private boolean doValidation() {
        if (mUserNameEdtTxt.getText().toString().trim().equalsIgnoreCase("")) {
            AppUtils.editTextValidationAlert(mUserNameEdtTxt, getResources()
                    .getString(R.string.mandatory_field), mContext);
            AppUtils.setEdtTextTextChangelistener(mUserNameEdtTxt, mContext);
            return false;
        } else if (!AppUtils
                .isValidEmail(mUserNameEdtTxt.getText().toString())) {
            AppUtils.editTextValidationAlert(mUserNameEdtTxt, getResources()
                    .getString(R.string.enter_valid_email), mContext);
            AppUtils.setEdtTextTextChangelistener(mUserNameEdtTxt, mContext);
            return false;
        } else if (mPasswordEdtTxt.getText().toString().equalsIgnoreCase("")) {
            AppUtils.editTextValidationAlert(mPasswordEdtTxt, getResources()
                    .getString(R.string.mandatory_field), mContext);
            AppUtils.setEdtTextTextChangelistener(mPasswordEdtTxt, mContext);
            return false;
        } else {
            return true;
        }
    }

    /*******************************************************
     * Method name : loginApiCall Description : login api call Parameters : nil
     * Return type : void Date : Dec 11, 2014 Author : Rijo K Jose
     *****************************************************/
    private void loginApiCall() {
    }


    /*******************************************************
     * Method name : forgotPasswordApiCall Description : forgot password api
     * call Parameters : emailId Return type : void Date : March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    private void forgotPasswordApiCall() {
        forgotDialog = new Dialog(mContext, R.style.NewDialog);
        forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotDialog.setContentView(R.layout.dialog_forgot_password);
        forgotDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forgotDialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        mMailEdtText = forgotDialog.findViewById(R.id.text_dialog);
        mMailEdtText.setOnTouchListener(this);
        TextView alertHead = forgotDialog.findViewById(R.id.alertHead);
        Button dialogSubmitButton = forgotDialog
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mMailEdtText);
                if (!mMailEdtText.getText().toString().trim().equalsIgnoreCase("")) {
                    if (AppUtils.isValidEmail(mMailEdtText.getText()
                            .toString())) {
                        // sign up api call
                        if (AppUtils.isNetworkConnected(mContext)) {
                            sendForGotpassWord(URL_FORGOTPASSWORD);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_valid_email), R.drawable.exclamationicon, R.drawable.round);

                    }
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

        Button dialogMayBelaterutton = forgotDialog.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mMailEdtText);

                forgotDialog.dismiss();
            }
        });
        forgotDialog.show();
    }

    /*******************************************************
     * Method name : showAlreadyLoginAlert Description : show alert - user is
     * already logged in another device Return Type : void Parameters type :
     * activity, message, okBtnTitle, cancelBtnTitle, Date :  March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    @SuppressLint("NewApi")
    private void showAlreadyLoginAlert(final Activity activity, String message,
                                       String okBtnTitle, String cancelBtnTitle, final String userName,
                                       final String password, final RelativeLayout progresslayout) {
        // custom dialog
        final Dialog dialog = new Dialog(activity, R.style.NewDialog);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image, button
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        int sdk = android.os.Build.VERSION.SDK_INT;

        Button dialogCancelButton = dialog
                .findViewById(R.id.dialogButtonCancel);
        dialogCancelButton.setText(cancelBtnTitle);
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogCancelButton.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        } else {
            dialogCancelButton.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        }
        // if button is clicked, close the custom dialog
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOkButton = dialog
                .findViewById(R.id.dialogButtonOK);
        dialogOkButton.setText(okBtnTitle);
        dialogOkButton.setVisibility(View.VISIBLE);
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogOkButton.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        } else {
            dialogOkButton.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        }
        // if button is clicked, close the custom dialog
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new LoginActivity().confirmLoginApiCall(activity, userName,
                        password, progresslayout);
            }
        });
        dialog.show();
    }

    /*******************************************************
     * Method name : confirmLoginApiCall Description : confirm login api call in
     * new device Parameters : context, userName, password, progressLayout
     * Return type : void Date :  March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    private void confirmLoginApiCall(Context context, String userName,
                                     String password, RelativeLayout progressLayout) {
    }


    /*******************************************************
     * Method name : showSignUpAlertDialog Description : show signup alert
     * dialog Parameters : nil Return type : void Date : Mar 11, 2015 Author :
     * Rijo K Jose
     *****************************************************/
    private void showSignUpAlertDialog() {
        // custom dialog
        dialogSignUp = new Dialog(mContext, R.style.NewDialog);
        dialogSignUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSignUp.setContentView(R.layout.dialog_layout_signup);
        dialogSignUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSignUp.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        mMailEdtText = dialogSignUp.findViewById(R.id.text_dialog);
        mMailEdtText.setOnTouchListener(this);
        TextView alertHead = dialogSignUp.findViewById(R.id.alertHead);
//
        Button dialogSubmitButton = dialogSignUp
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mMailEdtText);
                if (!mMailEdtText.getText().toString().trim().equalsIgnoreCase("")) {
                    if (AppUtils.isValidEmail(mMailEdtText.getText()
                            .toString())) {
                        // sign up api call
                        if (AppUtils.isNetworkConnected(mContext)) {
                            sendSignUpRequest(URL_PARENT_SIGNUP);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_valid_email), R.drawable.exclamationicon, R.drawable.round);

                    }
                } else {

                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

        Button dialogMayBelaterutton = dialogSignUp.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mMailEdtText);
                dialogSignUp.dismiss();
            }
        });
        dialogSignUp.show();
    }

    private void sendSignUpRequest(String URL)
    {
        String androidId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "deviceid", "devicetype", "mobile_no","device_identifier"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mMailEdtText.getText().toString(), FirebaseInstanceId.getInstance().getToken(), "2", PreferenceManager.getPhoneNo(mContext),androidId};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            String otp = secobj.optString("otp");
                            phoneno = secobj.optString("mobile_no");

                            AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
                            if (AppUtils.checkInternet(mContext)) {
                                showOtpAlertDialog();
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        } else if (status_code.equalsIgnoreCase("300")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_phoneno), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email) + "(" + mMailEdtText.getText().toString() + ")" + " " + getString(R.string.invalid_email_end), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("209")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.already_exist), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendSignUpRequest(URL_PARENT_SIGNUP);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendSignUpRequest(URL_PARENT_SIGNUP);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendSignUpRequest(URL_PARENT_SIGNUP);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private void LoginApiCall(String URL) {
        String androidId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "password", "deviceid", "devicetype","device_identifier"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mUserNameEdtTxt.getText().toString(), mPasswordEdtTxt.getText().toString(), FirebaseInstanceId.getInstance().getToken(), "2",androidId};
        System.out.println("acccessTokenpostInitPost: " + PreferenceManager.getAccessToken(mContext));

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response Login is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONObject respObj = secobj.getJSONObject(JTAG_RESPONSE_ARRAY);
                            PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USER_ID));
                            PreferenceManager.setUserEmail(mContext, mUserNameEdtTxt.getText().toString());
                            PreferenceManager.setPhoneNo(mContext, respObj.optString(JTAG_PHONE_NO));
                            PreferenceManager.setLoggedInStatus(mContext,"1");
                            PreferenceManager.setFullName(mContext, respObj.optString("name"));
                            showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
//                            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
//                            AppController.getInstance().trackEvent(PreferenceManager.getUserEmail(mContext), "Login" + " " + Calendar.getInstance().getTime(), "Login");


                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("208")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.not_active), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("305")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.incrct_pswd), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email) + "(" + mUserNameEdtTxt.getText().toString() + ")" + " " + getString(R.string.invalid_email_end), R.drawable.infoicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                                System.out.println("Calling or not");

                            }
                        });

                        LoginApiCall(URL_LOGIN);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                                System.out.println("Calling or not");

                            }

                        });
                        LoginApiCall(URL_LOGIN);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                                System.out.println("Calling or not");

                            }
                        });
                        LoginApiCall(URL_LOGIN);
                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private void showOtpAlertDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(mContext, R.style.NewDialogs);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_otp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        mOtpEditText = dialog.findViewById(R.id.text_otp);
        mAlertphone = dialog.findViewById(R.id.alertphone);
        mOtpEditText.setOnTouchListener(this);
        TextView txt_update = dialog.findViewById(R.id.txt_update);
        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.hideKeyboard(mContext, mMailEdtText);
                dialog.dismiss();
            }

        });
        TextView alertHead = dialog.findViewById(R.id.alertHead);
        mAlertphone.setText(" An SMS will be sent to the following registered mobile phone number" + " \n" + phoneno);
        Button dialogSubmitButton = dialog
                .findViewById(R.id.btn_submit);
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mOtpEditText);
                if (!mOtpEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                    dialog.dismiss();

                } else {

                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_otp), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

        Button dialogMayBelaterutton = dialog.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePhoneAlert();
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void sendOtpRequest(String URL) {
        String androidId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //device_identifier
        //androidId
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "otp", "email", "deviceid", "devicetype","device_identifier"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mOtpEditText.getText().toString(), mMailEdtText.getText().toString(), FirebaseInstanceId.getInstance().getToken(), "2",androidId};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            dialogSignUp.dismiss();

                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.signup_success_alert), R.drawable.tick, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_otp), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private void sendPhoneNoRequest(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "code", "mobile"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mEmailText.getText().toString(), code, text_phone.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.update_success), R.drawable.tick, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private void showChangePhoneAlert() {
        // custom dialog
        final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_change_phoneno);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        mEmailText = dialog.findViewById(R.id.text_email);
        text_phone = dialog.findViewById(R.id.text_phone);
        spinnerCode = dialog.findViewById(R.id.spinnerCode);
        mOtpEditText.setOnTouchListener(this);
        mEmailText.setText(mMailEdtText.getText().toString());
        text_phone.getText().toString();

        spinnerCode.resetToDefaultCountry();
        code = spinnerCode.getDefaultCountryCodeWithPlus();
        spinnerCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                code = spinnerCode.getSelectedCountryCodeWithPlus();
            }
        });

        Button dialogSubmitButton = dialog
                .findViewById(R.id.btn_submit);
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mOtpEditText);
                if (!text_phone.getText().toString().trim().equalsIgnoreCase("")) {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                    dialog.dismiss();

                } else {

                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phone_no), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

        Button dialogMayBelaterutton = dialog.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, mMailEdtText);
                dialog.dismiss();
                showOtpAlertDialog();
            }
        });


        dialog.show();

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("codejson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void countryCode() {
        listCodeString = new ArrayList<>();
        listCode = new ArrayList<>();
        try {
            JSONArray codeArray = new JSONArray(loadJSONFromAsset());
            int firstSelectedPos = 0;
            for (int i = 0; i < codeArray.length(); i++) {
                JSONObject obj = codeArray.getJSONObject(i);
                CodeModel model = new CodeModel();
                model.setCode(obj.optString("code"));
                if (obj.optString("code").equalsIgnoreCase("MY")) {
                    firstSelectedPos = i;
                }

                model.setDial_code(obj.optString("dial_code"));
                model.setName(obj.optString("name"));
                listCode.add(model);
                listCodeString.add(obj.optString("name") + " (" + obj.optString("dial_code") + " )");

            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }

    public void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

//				Intent homeIntent = new Intent(mContext, HomeListActivity.class);
                Intent homeIntent = new Intent(mContext, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });

        dialog.show();

    }

    private void sendForGotpassWord(String URL) {
        String androidId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //device_identifier
        //androidId
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "deviceid", "devicetype","device_identifier"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mMailEdtText.getText().toString(), FirebaseInstanceId.getInstance().getToken(), "2",androidId};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            forgotDialog.dismiss();

                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("208")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.not_active), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("305")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.incrct_usernamepswd), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email_normal), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendForGotpassWord(URL_FORGOTPASSWORD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendForGotpassWord(URL_FORGOTPASSWORD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        sendForGotpassWord(URL_FORGOTPASSWORD);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });
    }

    private void showSettingUserDetail(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is count" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONObject respObj = secobj.getJSONObject(JTAG_RESPONSE_ARRAY);
                            // PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USER_ID));
                            //  String userId=respObj.optString(JTAG_USER_ID);
                            PreferenceManager.setPhoneNo(mContext, respObj.optString(JTAG_PHONE_NO));
                            PreferenceManager.setFullName(mContext, respObj.optString("name"));
                            PreferenceManager.setEmailPush(mContext, respObj.optString("emailpush"));
                            PreferenceManager.setCalenderPush(mContext, respObj.optString("calenderpush"));
                            PreferenceManager.setMessageBadge(mContext, respObj.optString("messagebadge"));
                            PreferenceManager.setCalenderBadge(mContext, respObj.optString("calenderbadge"));
                            PreferenceManager.setReportMailMerge(mContext, respObj.optString("reportmailmerge"));
                            PreferenceManager.setCorrespondenceMailMerge(mContext, respObj.optString("correspondencemailmerge"));
                            JSONObject respObjAppFeature = secobj.optJSONObject("app_feature");
                            PreferenceManager.setTimeTable(mContext, respObjAppFeature.optString("timetable"));
                            PreferenceManager.setSafeGuarding(mContext, respObjAppFeature.optString("safeguarding"));
                            PreferenceManager.setAttendance(mContext, respObjAppFeature.optString("attendance"));
                            JSONArray studentDetail = respObj.getJSONArray("studentdetails");
                            ArrayList<StudentDetailSettingModel> mDatamodel = new ArrayList<StudentDetailSettingModel>();

                            if (studentDetail.length() > 0) {

                                for (int i = 0; i < studentDetail.length(); i++)

                                {
                                    JSONObject dataObject = studentDetail.getJSONObject(i);
                                    StudentDetailSettingModel xmodel = new StudentDetailSettingModel();
                                    xmodel.setId(dataObject.optString("id"));
                                    xmodel.setAlumi(dataObject.optString("alumi"));
                                    mDatamodel.add(xmodel);

                                }
                            }
                            int alumini = 0;
                            for (int x = 0; x < mDatamodel.size(); x++) {
                                if (mDatamodel.get(x).getAlumi().equalsIgnoreCase("0")) {
                                    alumini = 0;
                                    break;
                                } else {
                                    alumini = 1;

                                }
                            }
                            PreferenceManager.setIsVisible(mContext, String.valueOf(alumini));
                            JSONObject socialmediaObj = secobj.getJSONObject("socialmedia");
                            PreferenceManager.setFbkey(mContext, socialmediaObj.optString("fbkey"));
                            PreferenceManager.setInstaKey(mContext, socialmediaObj.optString("inkey"));
                            PreferenceManager.setYouKey(mContext, socialmediaObj.optString("youtubekey"));


                            GoogleAnalytics analytics = GoogleAnalytics.getInstance(mContext);
                            Tracker tracker;
                            //  tracker = analytics.newTracker("UA-128359311-1");   //LiveAccount
                            tracker = analytics.newTracker("UA-128379282-1");   //New LiveAccount
//                            tracker = analytics.newTracker("UA-126664424-1");//testaccount
                            tracker.setScreenName("Login Page");
                            tracker.set("&uid", PreferenceManager.getUserId(mContext));
                            tracker.set("&cid", PreferenceManager.getUserId(mContext));
                            tracker.setClientId(PreferenceManager.getUserId(mContext));
//                            tracker.send(new HitBuilders.ScreenViewBuilder().build());

                            tracker.send(new HitBuilders.EventBuilder().setCategory(PreferenceManager.getUserEmail(mContext))  // category i.e. Player Buttons
                                    .setAction("Login." + " " + Calendar.getInstance().getTime())    // action i.e.  Play
                                    .setLabel("Login.").set("&uid", PreferenceManager.getUserId(mContext)).set("&cid", PreferenceManager.getUserId(mContext)).build());   // label i.e.  any meta-data);
                            analytics.reportActivityStart(LoginActivity.this);

//                            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
//                            AppController.getInstance().trackEvent(PreferenceManager.getUserEmail(mContext), "Login" + " " + Calendar.getInstance().getTime(), "Login");


                            showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.login_success_alert), R.drawable.tick, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    @Override
    protected void onResume() {
//        AppController.getInstance().trackScreenView("Login Page" + PreferenceManager.getUserEmail(mContext));

        super.onResume();
    }
}
