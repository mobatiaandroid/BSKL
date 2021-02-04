package com.mobatia.bskl.activity.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.notification.adapter.StudentUnReadRecyclerAdapter;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.HeaderManager;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class VideoWebViewActivity extends AppCompatActivity implements OnClickListener,
		IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

	ArrayList<PushNotificationModel> videolist;
	WebView webView;
	int position;
	ProgressBar proWebView;
	private String title = null;

	ImageView back;
	ImageView home;
	RelativeLayout relativeMain;
	Activity mActivity;
	Context context;
	TextView textcontent;
	RecyclerView studentRecycleUnread;
	TextView msgTitle;

	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.videopush_web_view);
		mActivity = this;
		context = this;

		AppController.isfromUnread = false;

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			position = extra.getInt(POSITION);
			title = extra.getString("title");
			AppController.isfromUnread = extra.getBoolean("isfromUnread");
			AppController.isfromRead = extra.getBoolean("isfromRead");

			videolist = (ArrayList<PushNotificationModel>) extra
					.getSerializable(PASS_ARRAY_LIST);
		}		webView = findViewById(R.id.webView);

		proWebView = findViewById(R.id.proWebView);
		textcontent = findViewById(R.id.txtContent);
		textcontent.setVisibility(View.INVISIBLE);
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
		LinearLayoutManager llm = new LinearLayoutManager(mActivity);
		llm.setOrientation(LinearLayoutManager.HORIZONTAL);
		studentRecycleUnread.setLayoutManager(llm);
		StudentUnReadRecyclerAdapter mStudentRecyclerAdapter= new StudentUnReadRecyclerAdapter(mActivity,videolist.get(position).getStudentArray());
		studentRecycleUnread.setAdapter(mStudentRecyclerAdapter);
		webView.setWebViewClient(new HelloWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setDisplayZoomControls(true);
		String frameVideo = "<html>"+"<br><iframe width=\"320\" height=\"250\" src=\"";
		String url_Video=frameVideo+videolist.get(position).getUrl()+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
		String urlYoutube = url_Video.replace("watch?v=", "embed/");
		System.out.println("urlYoutube:"+urlYoutube);
		webView.loadData(urlYoutube, "text/html", "utf-8");
	
		textcontent.setText(videolist.get(position).getTitle());
		proWebView.setVisibility(View.VISIBLE);
		if(videolist.get(position).getStudent_name().equalsIgnoreCase(""))
		{
			studentRecycleUnread.setVisibility(View.GONE);

		}

	}


	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

			proWebView.setVisibility(View.GONE);
			textcontent.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}


	private void callstatusapi(final String statusread, final String pushId) {
		try {
			final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_STATUS_CHANGE);
			String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_ID, "status", JTAG_PUSH_ID};
			String[] value = {PreferenceManager.getAccessToken(mActivity), PreferenceManager.getUserId(mActivity), statusread, pushId};
			manager.getResponsePOST(mActivity, 11, name, value, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					String responsCode = "";
					System.out.println("NotifyRes:" + successResponse);
					if (successResponse != null) {
						try {
							JSONObject obj = new JSONObject(successResponse);
							String response_code = obj.getString(JTAG_RESPONSECODE);
							if (response_code.equalsIgnoreCase("200")) {
								JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
								String status_code = secobj.getString(JTAG_STATUSCODE);
								if (status_code.equalsIgnoreCase("303")) {
									callstatusapi(statusread, pushId);
								} else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
										status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
									AppUtils.postInitParam(mActivity, new AppUtils.GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
											callstatusapi(statusread, pushId);
										}
									});
								}
							} else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
									responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
								AppUtils.postInitParam(mActivity, new AppUtils.GetAccessTokenInterface() {
									@Override
									public void getAccessToken() {
										callstatusapi(statusread, pushId);
									}
								});
							} else {
								Toast.makeText(mActivity, "Some Error Occured", Toast.LENGTH_SHORT).show();

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
		} catch (Exception ex) {

		}
	}

	@Override
	public void onBackPressed() {

		AppController.pushId=videolist.get(position).getPushid();
		finish();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!PreferenceManager.getUserId(context).equalsIgnoreCase(""))
		{
			AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(context));
			AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(context));
			AppController.getInstance().trackScreenView("Video Notification Detail."+"("+PreferenceManager.getUserEmail(mActivity)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

		}

	}
}
