/**
 * 
 */
package com.mobatia.bskl.activity.notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * @author rahul
 * 
 */

public class TextalertActivity extends AppCompatActivity implements
		 OnClickListener,IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

	TextView txtmsg;
	TextView mDateTv;
	ImageView img;
	ImageView home;
	Bundle extras;
	ArrayList<PushNotificationModel> imglist;
	int position;
	String date="";

	Context context ;
	Activity mActivity;
	RelativeLayout header;
	ImageView back;
	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	RecyclerView studentRecycleUnread;
	TextView msgTitle;
	String title="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.text_push);
		mActivity = this;
		context = this;
		AppController.isfromUnread = false;

		extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt(POSITION);
          date= extras.getString("date");
			AppController.isfromUnread = extras.getBoolean("isfromUnread");

			title= extras.getString("title");
			imglist = (ArrayList<PushNotificationModel>) extras
					.getSerializable(PASS_ARRAY_LIST);

		}

		try {
			initialiseUI();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void initialiseUI() throws ParseException {


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
		LinearLayoutManager llm = new LinearLayoutManager(context);
		llm.setOrientation(LinearLayoutManager.HORIZONTAL);
		studentRecycleUnread.setLayoutManager(llm);
		StudentUnReadRecyclerAdapter mStudentRecyclerAdapter= new StudentUnReadRecyclerAdapter(context,imglist.get(position).getStudentArray());
		studentRecycleUnread.setAdapter(mStudentRecyclerAdapter);
		img = findViewById(R.id.image);
		txtmsg = findViewById(R.id.txt);
		mDateTv = findViewById(R.id.mDateTv);

		setDetails();

	}

	@TargetApi(Build.VERSION_CODES.N)
	private void setDetails() throws ParseException {
		if(imglist.get(position).getStudent_name().equalsIgnoreCase(""))
		{
			studentRecycleUnread.setVisibility(View.GONE);

		}

			msgTitle.setText(title);
		final int sdk = Build.VERSION.SDK_INT;

		if (sdk < Build.VERSION_CODES.N) {
			txtmsg.setText(Html.fromHtml(Html.fromHtml("<!DOCTYPE html>\n" +
					"<html>\n" +
							"<body>"+imglist.get(position).getMessage())+"</body>\n" +"</html>"));
		}
		else
		{
			txtmsg.setText(Html.fromHtml("<!DOCTYPE html>\n" +
					"<html>\n" +
					"<body>"+imglist.get(position).getMessage()+"</body>\n" +"</html>",Html.FROM_HTML_MODE_LEGACY));

		}
		txtmsg.setMovementMethod(LinkMovementMethod.getInstance());

		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
		String mdate =imglist.get(position).getDate();
		Date date = inputFormat.parse(mdate);
		String outputDateStr = outputFormat.format(date);
		mDateTv.setText((outputDateStr));





	}

	@Override
	public void onClick(View v) {

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
										}
									});
									callstatusapi(statusread, pushId);

								}
							} else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
									responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
								AppUtils.postInitParam(mActivity, new AppUtils.GetAccessTokenInterface() {
									@Override
									public void getAccessToken() {
									}
								});
								callstatusapi(statusread, pushId);

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

		AppController.pushId=imglist.get(position).getPushid();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!PreferenceManager.getUserId(context).equalsIgnoreCase(""))
		{
			AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(context));
			AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(context));
			AppController.getInstance().trackScreenView("Text Notification Detail."+"("+PreferenceManager.getUserEmail(context)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

		}

	}
}
