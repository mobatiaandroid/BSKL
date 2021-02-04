package com.mobatia.bskl.fragment.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;

import java.util.Calendar;

/**
 * Created by mobatia on 03/10/18.
 */

public class NewsFragment extends Fragment implements View.OnClickListener{
    private String mTitle;
    private String mTabId;
    private View mRootView;
    Context mContext;
    ImageView newsLetterImg;
    WebView newsWebView;
    RelativeLayout relMain;
    private WebSettings mwebSettings;
    private RelativeLayout mProgressRelLayout;
    RotateAnimation anim;
    private boolean loadingFlag = true;
    private boolean mErrorFlag = false;
    private String mLoadUrl = "https://www.nordangliaeducation.com/our-schools/malaysia/kuala-lumpur/british-international/news-and-insights";

    public NewsFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_news_layout, container,
                false);
        setHasOptionsMenu(true);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        headerTitle.setText("BSKL News");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        initialiseUI();
        getWebViewSettings();

        return mRootView;
    }

    private void initialiseUI() {
        newsLetterImg=(ImageView)mRootView.findViewById(R.id.newsLetterImg);
        newsWebView=(WebView)mRootView.findViewById(R.id.newsWebView);
        mProgressRelLayout = (RelativeLayout)mRootView.findViewById(R.id.progressDialog);
        relMain = (RelativeLayout)mRootView.findViewById(R.id.relMain);

        newsLetterImg.setOnClickListener(this);
        relMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==newsLetterImg)
        {
            Uri uri = Uri.parse("https://drive.google.com/drive/folders/0B_xZr_OAuRnDNG9fSUxyaE5NaHM"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if(v==relMain)
        {

        }
    }
    private void getWebViewSettings() {
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        newsWebView.setFocusable(true);
        newsWebView.setFocusableInTouchMode(true);
        newsWebView.setBackgroundColor(0X00000000);
        newsWebView.setVerticalScrollBarEnabled(false);
        newsWebView.setHorizontalScrollBarEnabled(false);
        newsWebView.setWebChromeClient(new WebChromeClient());

        mwebSettings = newsWebView.getSettings();
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

        newsWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        newsWebView.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        newsWebView.getSettings().setAllowFileAccess(true);
        newsWebView.getSettings().setAppCacheEnabled(true);
        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsWebView.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        newsWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    view.loadUrl(url);

                    loadingFlag = false;
                } else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);
                    view.loadUrl(url);

                    System.out.println("CACHE LOADING");
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
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtils.checkInternet(mContext)) {
                    AppUtils.showAlertFinish((Activity) mContext, getResources()
                                    .getString(R.string.common_error), "",
                            getResources().getString(R.string.ok), false);
                }

                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        mErrorFlag = mLoadUrl.equals("");
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("BSKL load url " + mLoadUrl);
            newsWebView.loadUrl(mLoadUrl);
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_page), "",
                    getResources().getString(R.string.ok), false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("News Fragment. " + "("+PreferenceManager.getUserEmail(mContext)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

        }



    }
}
