package com.mobatia.bskl.activity.terms_of_service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.HeaderManager;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.CustomDialog;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by krishnaraj on 29/06/18.
 */

public class TermsOfServiceActivity extends AppCompatActivity implements URLConstants,JSONConstants {
    Context mContext=this;
    RelativeLayout mProgressRelLayout;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type;
    WebView web;
    RotateAnimation anim;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private boolean mErrorFlag = false;
    String termsTitle;
    String termsDescription;
    String mLoadData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_terms_of_service);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)){
            callAboutUsApi(URL_TERMS_OF_SERVICE);
        }else{
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            tab_type=extras.getString("tab_type");
        }
        web = findViewById(R.id.webView);
        web.getSettings().setJavaScriptEnabled(true);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();
        Toolbar toolbar=(Toolbar)view.getParent();
        toolbar.setContentInsetsAbsolute(0,0);
        TextView headerTitle = view.findViewById(R.id.headerTitle);
        ImageView logoClickImgView = view.findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = view.findViewById(R.id.action_bar_forward);
        ImageView action_bar_back = view.findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        action_bar_forward.setVisibility(View.INVISIBLE);
        headerTitle.setText("Terms of Service");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);

    }

    private void callAboutUsApi(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token"};
        String[] value={PreferenceManager.getAccessToken(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                // System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONObject data = secobj.optJSONObject("data");
                            termsTitle=data.optString("title");
                            termsDescription=data.optString("description");
                            mLoadData= "<!DOCTYPE html>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    "<style>\n" +
                                    "\n" +
                                    "@font-face {\n" +
                                    "font-family: SourceSansPro-Semibold;"+
                                    "src: url(SourceSansPro-Semibold.ttf);"+

                                    "font-family: SourceSansPro-Regular;"+
                                    "src: url(SourceSansPro-Regular.ttf);"+
                                    "}"+
                                    ".title {"+
                                    "font-family: SourceSansPro-Regular;"+
                                    "font-size:16px;"+
                                    "text-align:left;"+
                                    "margin-left:4px;"+
                                    "margin-right:4px;"+
                                    "color:	#46C1D0;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+

                                    ".description {"+
                                    "font-family: SourceSansPro-Light;"+
                                    "text-align:justify;"+
                                    "font-size:14px;"+
                                    "margin-left:4px;"+
                                    "margin-right:4px;"+
                                    "color: #000000;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+
                                    "</style>\n"+"</head>"+
                                    "<body>"+
                                    "<p class='title'>"+termsTitle+"</p>"+"<p class='description'>"+termsDescription+"</p>"+
                                    "</body>\n</html>";
                            getWebViewSettings();

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_TERMS_OF_SERVICE);


                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_TERMS_OF_SERVICE);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_TERMS_OF_SERVICE);

                    } else {
                        CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                                , getResources().getString(R.string.ok));
                        dialog.show();
                    }
                } catch (Exception ex) {
                    // System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }


    private void getWebViewSettings() {



        web.setFocusable(true);
        web.setFocusableInTouchMode(true);
        web.setBackgroundColor(0X00000000);
        web.setVerticalScrollBarEnabled(false);
        web.setHorizontalScrollBarEnabled(false);
        web.setWebChromeClient(new WebChromeClient());
//        int sdk = Build.VERSION.SDK_INT;
//        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
//            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
//        }
        mwebSettings = web.getSettings();
        mwebSettings.setSaveFormData(true);
        mwebSettings.setBuiltInZoomControls(false);
        mwebSettings.setSupportZoom(false);

        mwebSettings.setPluginState(WebSettings.PluginState.ON);
        mwebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mwebSettings.setDomStorageEnabled(true);
        mwebSettings.setDatabaseEnabled(true);
        mwebSettings.setDefaultTextEncodingName("utf-8");
        mwebSettings.setLoadsImagesAutomatically(true);

        web.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        web.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAppCacheEnabled(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        web.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    view.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");

                    loadingFlag = false;
                } else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);
                    view.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");

                    // System.out.println("CACHE LOADING");
                    loadingFlag = false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            /*
             * (non-Javadoc)
             *
             * @see
             * android.webkit.WebViewClient#onReceivedError(android.webkit.WebView
             * , int, java.lang.String, java.lang.String)
             */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                if (AppUtils.checkInternet(mContext)) {
                    AppUtils.showAlertFinish((Activity) mContext, getResources()
                                    .getString(R.string.common_error), "",
                            getResources().getString(R.string.ok), false);
                }

                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        mErrorFlag = mLoadData.equals("");
        if (mLoadData != null && !mErrorFlag) {
            web.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");

        } else {
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_page), "",
                    getResources().getString(R.string.ok), false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Terms of Service"+" "+PreferenceManager.getUserEmail(mContext)+" "+ Calendar.getInstance().getTime());

        }

    }
}
