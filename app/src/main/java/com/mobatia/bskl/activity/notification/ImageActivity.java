/**
 * 
 */
package com.mobatia.bskl.activity.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.mobatia.bskl.activity.notification.adapter.StudentUnReadRecyclerAdapter;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.HeaderManager;
import com.mobatia.bskl.manager.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * @author archana.s
 * 
 */
public class ImageActivity extends AppCompatActivity implements
		CacheDIRConstants, NaisClassNameConstants,IntentPassValueConstants {

	private Context mContext;
	private WebView mWebView;
	private RelativeLayout mProgressRelLayout;
	private WebSettings mwebSettings;
	private boolean loadingFlag = true;
	private String mLoadUrl = null;
	private String title = null;
	private boolean mErrorFlag = false;
	RotateAnimation anim;

	Bundle extras;
	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	ImageView back;
	ImageView home;
	TextView msgTitle;
	ArrayList<PushNotificationModel>alertlist;
	RecyclerView studentRecycleUnread;
	int position=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.comingup_detailweb_view_layout);
		AppController.isfromUnread = false;
		AppController.isfromUnreadSingle = false;

		mContext = this;
		extras = getIntent().getExtras();
		if (extras != null) {
			mLoadUrl = extras.getString("webViewComingDetail");
			title = extras.getString("title");
			position = extras.getInt(POSITION);
			AppController.isfromUnread = extras.getBoolean("isfromUnread");
			AppController.isfromUnreadSingle = extras.getBoolean("isfromUnreadSingle");
			AppController.isfromRead = extras.getBoolean("isfromRead");

			alertlist = (ArrayList<PushNotificationModel>) extras
					.getSerializable(PASS_ARRAY_LIST);

		}
		initialiseUI();
		getWebViewSettings();
	}


	/*******************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : Oct 30, 2014 Author : Vandana
	 * Surendranath
	 *****************************************************/
	private void initialiseUI() {
        if(AppController.isfromUnread)
		{
			for(int i = 0; i<AppController.mMessageUnreadList.size(); i++)
			{
				if (AppController.mMessageUnreadList.get(position).getPushid().equalsIgnoreCase(AppController.mMessageUnreadList.get(i).getPushid()))
				{
					AppController.mMessageUnreadList.remove(i);
				}
			}
		}
		relativeHeader = findViewById(R.id.relativeHeader);
		mWebView = findViewById(R.id.webView);
		mProgressRelLayout = findViewById(R.id.progressDialog);
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
		headerTitle.setText("Message");
		headerTitle.setVisibility(View.VISIBLE);
		logoClickImgView.setVisibility(View.INVISIBLE);
		action_bar_forward.setVisibility(View.INVISIBLE);

		action_bar_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		msgTitle= findViewById(R.id.msgTitle);
		studentRecycleUnread= findViewById(R.id.studentRecycle);
		msgTitle.setText(title);
		studentRecycleUnread.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(mContext);
		llm.setOrientation(LinearLayoutManager.HORIZONTAL);
		studentRecycleUnread.setLayoutManager(llm);
		StudentUnReadRecyclerAdapter mStudentRecyclerAdapter= new StudentUnReadRecyclerAdapter(mContext,alertlist.get(position).getStudentArray());
		studentRecycleUnread.setAdapter(mStudentRecyclerAdapter);
		if(alertlist.get(position).getStudent_name().equalsIgnoreCase(""))
		{
			studentRecycleUnread.setVisibility(View.GONE);

		}
	}

	/*******************************************************
	 * Method name : getWebViewSettings Description : get web view settings
	 * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
	 * Surendranath
	 *****************************************************/
	private void getWebViewSettings() {


		mProgressRelLayout.setVisibility(View.GONE);
		anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(mContext, android.R.interpolator.linear);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		mProgressRelLayout.setAnimation(anim);
		mProgressRelLayout.startAnimation(anim);
		mWebView.setFocusable(true);
		mWebView.setFocusableInTouchMode(true);
		mWebView.setBackgroundColor(0X00000000);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebChromeClient(new WebChromeClient());
//        int sdk = Build.VERSION.SDK_INT;
//        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
//            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
//        }
		mwebSettings = mWebView.getSettings();
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
		mwebSettings.setAllowFileAccessFromFileURLs(true);

		mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
		mWebView.getSettings().setAppCachePath(
				mContext.getCacheDir().getAbsolutePath());
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings()
				.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if( url.startsWith("http:") || url.startsWith("https:") || url.startsWith("www.") ) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
					return true;
				}
				else
				{
					view.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");
					return true;

				}
			}

			public void onPageFinished(WebView view, String url) {
				mProgressRelLayout.clearAnimation();
				mProgressRelLayout.setVisibility(View.GONE);
				if (AppUtils.checkInternet(mContext) && loadingFlag) {
					view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

					view.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

					loadingFlag = false;
				} else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
					view.getSettings()
							.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
					view.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

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
			System.out.println("NAS load url " + mLoadUrl);
			mWebView.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

		} else {
			mProgressRelLayout.clearAnimation();
			mProgressRelLayout.setVisibility(View.GONE);
			AppUtils.showAlertFinish((Activity) mContext, getResources()
							.getString(R.string.common_error_loading_page), "",
					getResources().getString(R.string.ok), false);
		}

	}



	@Override
	public void onBackPressed() {

		AppController.pushId=alertlist.get(position).getPushid();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
		{
			AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
			AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
			AppController.getInstance().trackScreenView("Image Notification Detail. "+"("+PreferenceManager.getUserEmail(mContext)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

		}

	}
}

