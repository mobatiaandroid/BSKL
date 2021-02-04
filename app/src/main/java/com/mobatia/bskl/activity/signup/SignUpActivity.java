package com.mobatia.bskl.activity.signup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.OTP_Receiver.AppSMSBroadcastReceiver;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

/**
 * Created by krishnaraj on 21/09/18.
 */

public class SignUpActivity extends AppCompatActivity implements
        View.OnClickListener, IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

    EditText mMailEdtText;
    EditText verificationCodeTxt;
    Button emailNext;
    static Button emailCancel;
    Context mContext;
    String phoneno = "";
    String users_id = "";
    static LinearLayout enterEmail;
    static LinearLayout verificationCode;
    static LinearLayout successfullLinear;
    static LinearLayout changeNoLinear;
    static LinearLayout phnoNoSuccess;
    static Button button1;
    static Button button2;
    static Button button3;
    static Button btn_change;
    static Button btn_maybelater;
    Button loginClick;
    Button btn_close;
    Button verificationSubmit;
    static LinearLayout btnLinear;
    TextView mAlertphone;
    TextView successfullTxt;
    TextView changePhoneNo;
    TextView text_phone;
    TextView verifyTxt;
    TextView mEmailText;
    TextView verifiedTxt;
    CountryCodePicker spinnerCode;
    static int displayPage = 1;
    private String code;
    ImageView action_bar_forward;
    ImageView action_bar_back;
    ImageView logoClickImgView;
    TextView headerTitle;

    private IntentFilter intentFilter;
    private AppSMSBroadcastReceiver appSMSBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_email_signup_layout);
        mContext = this;
        initUI();
    }


    private void initUI() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view = getSupportActionBar().getCustomView();
        Toolbar toolbar = (Toolbar) view.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        headerTitle = view.findViewById(R.id.headerTitle);
        logoClickImgView = view.findViewById(R.id.logoClickImgView);
        action_bar_forward = view.findViewById(R.id.action_bar_forward);
        action_bar_back = view.findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        headerTitle.setText("Sign Up");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_forward.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mMailEdtText = findViewById(R.id.emailEditTxt);
        verificationCodeTxt = findViewById(R.id.verificationCodeTxt);
        mAlertphone = findViewById(R.id.mAlertPhone);
        successfullTxt = findViewById(R.id.successfullTxt);
        text_phone = findViewById(R.id.text_phone);
        changePhoneNo = findViewById(R.id.changePhoneNo);
        mEmailText = findViewById(R.id.mEmailText);
        emailNext = findViewById(R.id.emailNext);
        emailCancel = findViewById(R.id.emailCancel);
        btnLinear = findViewById(R.id.btnLinear);
        enterEmail = findViewById(R.id.enterEmail);
        verificationCode = findViewById(R.id.verificationCode);
        verifiedTxt = findViewById(R.id.verifiedTxt);
        verifyTxt = findViewById(R.id.verifyTxt);
        successfullLinear = findViewById(R.id.successfullLinear);
        changeNoLinear = findViewById(R.id.changeNoLinear);
        phnoNoSuccess = findViewById(R.id.phnoNoSuccess);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        btn_close = findViewById(R.id.btn_close);
        loginClick = findViewById(R.id.loginClick);
        btn_change = findViewById(R.id.btn_change);
        btn_maybelater = findViewById(R.id.btn_maybelater);
        spinnerCode = findViewById(R.id.spinnerCode);
        verificationSubmit = findViewById(R.id.verificationSubmit);
        emailNext.setOnClickListener(this);
        emailCancel.setOnClickListener(this);
        loginClick.setOnClickListener(this);
        changePhoneNo.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        emailCancel.setVisibility(View.GONE);

        smsListener();
        InitiateOTPreciver();

        verificationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyboard(mContext, verificationCodeTxt);
                if (!verificationCodeTxt.getText().toString().trim().equalsIgnoreCase("")) {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }

                } else {

                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_otp), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == emailNext) {
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
        } else if (v == emailCancel) {
            finish();
        } else if (v == loginClick) {
            finish();
        } else if (v == changePhoneNo) {
            displayPage = 4;
            successfullLinear.setVisibility(View.GONE);
            enterEmail.setVisibility(View.GONE);
            enterEmail.setVisibility(View.GONE);
            verificationCode.setVisibility(View.GONE);
            changeNoLinear.setVisibility(View.VISIBLE);
            button1.setBackgroundResource(R.drawable.shape_circle_signup_border);
            button2.setBackgroundResource(R.drawable.shape_circle_signup);
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
            btn_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.hideKeyboard(mContext, verificationCodeTxt);
                    if (!text_phone.getText().toString().trim().equalsIgnoreCase("")) {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phone_no), R.drawable.exclamationicon, R.drawable.round);

                    }
                }
            });
            btn_maybelater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogAlertEmailMaybelater((Activity) mContext, "Alert", "Do you want to go back?", R.drawable.questionmark_icon, R.drawable.round);

                }
            });
        } else if (v == btn_close) {
            AppUtils.hideKeyBoard(mContext);
            finish();
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void sendSignUpRequest(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "deviceid", "devicetype", "mobile_no","hash_key"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mMailEdtText.getText().toString(), FirebaseInstanceId.getInstance().getToken(), "2", PreferenceManager.getPhoneNo(mContext),PreferenceManager.getOTPSignature(mContext)};
        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                            phoneno = secobj.optString("mobile_no");
                            users_id = secobj.optString("users_id");

                            AppUtils.hideKeyboard(mContext, mMailEdtText);
                            if (AppUtils.checkInternet(mContext)) {
                                System.out.println("Email Next");

                                enterEmail.setVisibility(View.GONE);
                                verificationCode.setVisibility(View.VISIBLE);
                                verifiedTxt.setVisibility(View.VISIBLE);
                                displayPage = 2;
                                button1.setBackgroundResource(R.drawable.shape_circle_signup_border);
                                button2.setBackgroundResource(R.drawable.shape_circle_signup);
                                verifiedTxt.setVisibility(View.VISIBLE);
                                mAlertphone.setVisibility(View.VISIBLE);
                                changePhoneNo.setVisibility(View.VISIBLE);
                                mAlertphone.setText(" An SMS with a verification code has been sent to your registered mobile number" + " \n" + phoneno);
//                                new OTP_Receiver().setEditText(verificationCodeTxt);
//                                InitiateOTPreciver();
                                verifyTxt.setVisibility(View.VISIBLE);
                                verifyTxt.setText("The verification code should appear below");

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        } else if (status_code.equalsIgnoreCase("300")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_phoneno), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_already_reg_login), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("306")) {
                            showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email) + "(" + mMailEdtText.getText().toString() + ")" + " " + getString(R.string.invalid_email_end), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("209")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.already_exist), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendSignUpRequest(URL_PARENT_SIGNUP);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendSignUpRequest(URL_PARENT_SIGNUP);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendSignUpRequest(URL_PARENT_SIGNUP);

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

    private void InitiateOTPreciver() {
        intentFilter = new IntentFilter("com.google.android.gms.auth.api.phone.SMS_RETRIEVED");
        appSMSBroadcastReceiver = new AppSMSBroadcastReceiver();
        appSMSBroadcastReceiver.setOnSmsReceiveListener(new AppSMSBroadcastReceiver.OnSmsReceiveListener() {
            @Override
            public void onReceive(String code) {
//                Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
                String SMS = code;
//                SMS = SMS.replace("<#> Your BSKL code is : ","").split(". ")[0];
                if(SMS.contains("RM0 BSKL: <#> Your Mobile verification code for Parent Signup is:"))
                {
                    SMS = SMS.replace("RM0 BSKL: <#> Your Mobile verification code for Parent Signup is: ","").split(". ")[0];
                }
                else if (SMS.contains("RM0.00 BSKL: <#> Your Mobile verification code for Parent Signup is:"))
                {
                    SMS = SMS.replace("RM0.00 BSKL: <#> Your Mobile verification code for Parent Signup is: ","").split(". ")[0];
                }
                else if (SMS.contains("BSKL: <#> Your Mobile verification code for Parent Signup is:"))
                {
                    SMS = SMS.replace("BSKL: <#> Your Mobile verification code for Parent Signup is: ","").split(". ")[0];
                }

             //   SMS = SMS.replace("RM0 <#> Your Mobile verification code for BSKL Parent Signup is: ","").split(". ")[0];

                verificationCodeTxt.setText(SMS);
                verifyTxt.setText("Verification code successfully received");
                verifiedTxt.setVisibility(View.GONE);
                mAlertphone.setVisibility(View.GONE);
                changePhoneNo.setVisibility(View.GONE);

            }
        });
    }

    private void sendOtpRequest(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "otp", "email", "deviceid", "devicetype","users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), verificationCodeTxt.getText().toString(), mMailEdtText.getText().toString(), FirebaseInstanceId.getInstance().getToken(), "2",users_id};
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
                            System.out.println("otp working otp");
                            successfullLinear.setVisibility(View.VISIBLE);
                            displayPage = 3;

                            enterEmail.setVisibility(View.GONE);
                            verificationCode.setVisibility(View.GONE);
                            button1.setBackgroundResource(R.drawable.shape_circle_signup_border);
                            button2.setBackgroundResource(R.drawable.shape_circle_signup_border);
                            button3.setBackgroundResource(R.drawable.shape_circle_signup);
                            successfullTxt.setVisibility(View.VISIBLE);
                            action_bar_back.setVisibility(View.INVISIBLE);
                            successfullTxt.setText(getString(R.string.sign_up_successfull) + " " + "(" + mMailEdtText.getText().toString() + ")" + " " + getString(R.string.sign_up_successfull_last));

                            //AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.signup_success_alert), R.drawable.tick, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.otp_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendOtpRequest(URL_GET_OTP_VERIFICATION);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
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
        String[] name = {"access_token", "email", "code", "mobile","users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), mEmailText.getText().toString(), code, text_phone.getText().toString(),users_id};
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
                            displayPage = 5;
                            btnLinear.setVisibility(View.GONE);
                            enterEmail.setVisibility(View.GONE);
                            verificationCode.setVisibility(View.GONE);
                            successfullLinear.setVisibility(View.GONE);
                            changeNoLinear.setVisibility(View.GONE);
                            phnoNoSuccess.setVisibility(View.VISIBLE);
                            action_bar_back.setVisibility(View.INVISIBLE);

                            // AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.update_success), R.drawable.tick, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendPhoneNoRequest(URL_GET_OTP_NEWPHONENUMBER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
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

    public static void showDialogAlertDismiss(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                emailCancel.setVisibility(View.VISIBLE);
                dialog.dismiss();

            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }

    @Override
    public void onBackPressed() {

        if (displayPage == 2) {

            showDialogAlertEmailSuccess((Activity) mContext, "Alert", "Do you want to go back?", R.drawable.questionmark_icon, R.drawable.round);
            verificationCodeTxt.setText("");

        } else if (displayPage == 4) {
            showDialogAlertEmailMaybelater((Activity) mContext, "Alert", "Do you want to go back?", R.drawable.questionmark_icon, R.drawable.round);


        }

        else {
            AppUtils.hideKeyBoard(mContext);
            finish();
        }

    }


    public static void showDialogAlertEmail(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
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

                displayPage = 2;
                enterEmail.setVisibility(View.GONE);
                verificationCode.setVisibility(View.VISIBLE);
                successfullLinear.setVisibility(View.GONE);
                changeNoLinear.setVisibility(View.GONE);
                phnoNoSuccess.setVisibility(View.GONE);
                button2.setBackgroundResource(R.drawable.shape_circle_signup_border);
                button1.setBackgroundResource(R.drawable.shape_circle_signup);
                dialog.dismiss();

            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public static void showDialogAlertEmailMaybelater(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
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

                displayPage = 2;
                enterEmail.setVisibility(View.GONE);
                verificationCode.setVisibility(View.VISIBLE);
                successfullLinear.setVisibility(View.GONE);
                changeNoLinear.setVisibility(View.GONE);
                phnoNoSuccess.setVisibility(View.GONE);
                button1.setBackgroundResource(R.drawable.shape_circle_signup_border);
                button2.setBackgroundResource(R.drawable.shape_circle_signup);
                dialog.dismiss();

            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void showDialogAlertEmailSuccess(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
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

                displayPage = 1;
                enterEmail.setVisibility(View.VISIBLE);
                verificationCode.setVisibility(View.GONE);
                successfullLinear.setVisibility(View.GONE);
                changeNoLinear.setVisibility(View.GONE);
                phnoNoSuccess.setVisibility(View.GONE);
                button2.setBackgroundResource(R.drawable.shape_circle_signup_border);
                button1.setBackgroundResource(R.drawable.shape_circle_signup);
                dialog.dismiss();

            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void smsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsRetriever();
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(appSMSBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(appSMSBroadcastReceiver);
    }
}

