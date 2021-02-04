/**
 * 
 */
package com.mobatia.bskl.activity.userdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.userdetail.model.StudentUserModel;
import com.mobatia.bskl.activity.userdetail.model.UserProfileModel;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.HeaderManager;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * @author rahul
 * 
 */

public class UserDetailActivity extends AppCompatActivity implements
		 OnClickListener,IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

	TextView txtmsg;
	TextView mDateTv;
	ImageView img;
	ImageView home;
	Bundle extras;
	int position;
	Context context;
	Activity mActivity;
	RelativeLayout header;
	ImageView back;
	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	LinearLayout relSub;
	TextView stnameValue;
	TextView email;
	TextView relationship;
	TextView mobileno;
	TextView useraddress;
	TextView telephoneNo;
	TextView relationShip;
	String parentname="";
	String emailID="";
	String mobile="";
	String teleNo="";
	String relation="";
	String useraddressdata="";
	LinearLayout mobileLinear;
	LinearLayout Telephone;
	LinearLayout addresslinear;
	LinearLayout relationShipLinear;
	ArrayList<StudentUserModel> studentsModelArrayList = new ArrayList<>();
ScrollView scrollView;
	ArrayList<UserProfileModel> muserData = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_user_detail);
		mActivity = this;
		context = this;
		extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt(POSITION);


		}

		try {
			initialiseUI();
			if (AppUtils.checkInternet(mActivity)) {
				getUserProfileApi(URL_USER_PROFILE);
			} else {
				AppUtils.showDialogAlertDismiss(mActivity, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	private void getUserProfileApi(final String URLHEAD) {
		VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
		final String[] name = {"access_token", "user_ids"};
		String[] value = {PreferenceManager.getAccessToken(mActivity), PreferenceManager.getUserId(mActivity)};
		volleyWrapper.getResponsePOST(mActivity, 11, name, value, new VolleyWrapper.ResponseListener() {
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
							JSONObject profileObj = secobj.getJSONObject("profile");
							parentname=profileObj.optString("name");
							emailID=profileObj.optString("email");
							mobile=profileObj.optString("mobile_no");
							teleNo=profileObj.optString("telephone_no");
							relation=profileObj.optString("relationship");
							useraddressdata = profileObj.optString("address");
							stnameValue.setText(parentname);
							email.setText(emailID);
							relationship.setText(relation);
							useraddress.setText(useraddressdata);
							mobileno.setText(mobile);
							telephoneNo.setText(teleNo);
							relationShip.setText(relation);
							if(profileObj.optString("mobile_no").equalsIgnoreCase(""))
							{
								Telephone.setVisibility(View.GONE);
							}
							else
								{
								Telephone.setVisibility(View.VISIBLE);
							}
							if(profileObj.optString("telephone_no").equalsIgnoreCase(""))
							{
                               mobileLinear.setVisibility(View.GONE);
							}
							else {
								mobileLinear.setVisibility(View.VISIBLE);


							}
							if (profileObj.optString("address").equalsIgnoreCase("")){
								addresslinear.setVisibility(View.VISIBLE);
								useraddress.setText("No Data");
								useraddress.setTextColor(getResources().getColor(R.color.red));
							}else {
								addresslinear.setVisibility(View.VISIBLE);
								useraddress.setTextColor(getResources().getColor(R.color.black));
							}
//							if(profileObj.optString("relationship").equalsIgnoreCase(""))
//							{
//								relationShipLinear.setVisibility(View.GONE);
//
//							}
//							else {
//								relationShipLinear.setVisibility(View.GONE);
//
//							}
							scrollView.setVisibility(View.VISIBLE);

						JSONArray studentArray=secobj.getJSONArray("studentlist");
							if(studentArray.length()>0)
							{
								for(int i=0;i<studentArray.length();i++)
								{
									JSONObject dataObject = studentArray.getJSONObject(i);
//									if (dataObject.optString("alumi").equalsIgnoreCase("0")) {
//									}
									studentsModelArrayList.add(addStudentDetails(dataObject));

								}
							}

						}
					} else if (response_code.equalsIgnoreCase("500")) {
						AppUtils.showDialogAlertDismiss(mActivity, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mActivity, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						getUserProfileApi(URLHEAD);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mActivity, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						getUserProfileApi(URLHEAD);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mActivity, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						getUserProfileApi(URLHEAD);

					} else {

						AppUtils.showDialogAlertDismiss(mActivity, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					}
				} catch (Exception ex) {
					System.out.println("The Exception in edit profile is" + ex.toString());
				}

			}

			@Override
			public void responseFailure(String failureResponse) {
				AppUtils.showDialogAlertDismiss(mActivity, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

			}
		});


	}

	private void initialiseUI() throws ParseException {


		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
		getSupportActionBar().setElevation(0);

		View view = getSupportActionBar().getCustomView();
		Toolbar toolbar = (Toolbar) view.getParent();
		toolbar.setContentInsetsAbsolute(0, 0);
		TextView headerTitle = view.findViewById(R.id.headerTitle);
		ImageView logoClickImgView = view.findViewById(R.id.logoClickImgView);
		ImageView action_bar_forward = view.findViewById(R.id.action_bar_forward);
		ImageView action_bar_back = view.findViewById(R.id.action_bar_back);
		action_bar_back.setImageResource(R.drawable.back_new);
		headerTitle.setText("Account Details");
		headerTitle.setVisibility(View.VISIBLE);
		logoClickImgView.setVisibility(View.INVISIBLE);
		action_bar_forward.setVisibility(View.INVISIBLE);
		relSub = findViewById(R.id.relSub);
		scrollView = findViewById(R.id.scrollView);
		relSub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(UserDetailActivity.this, StudentListActivity.class);
				intent.putExtra("studentlist",studentsModelArrayList);
				startActivity(intent);

			}
		});

		action_bar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		stnameValue = findViewById(R.id.stnameValue);
		email = findViewById(R.id.email);
		relationship = findViewById(R.id.relationship);
		mobileno = findViewById(R.id.mobileno);
		telephoneNo = findViewById(R.id.telephoneNo);
		Telephone = findViewById(R.id.fromlayout);
		addresslinear = findViewById(R.id.addresslinear);
		relationShip = findViewById(R.id.relationShip);
		mobileLinear = findViewById(R.id.mobileLinear);
		useraddress = findViewById(R.id.useraddress);
		relationShipLinear = findViewById(R.id.relationShipLinear);


	}

	@Override
	public void onClick(View v) {

	}


	private StudentUserModel addStudentDetails(JSONObject dataObject) {
		StudentUserModel studentModel = new StudentUserModel();
	studentModel.setStudName(dataObject.optString("studentname"));
	studentModel.setPhoto(dataObject.optString("photo"));
	studentModel.setStud_id(dataObject.optString("stud_id"));
	studentModel.setAlumini(dataObject.optString("alumi"));
	studentModel.setClassAndSection(dataObject.optString("classAndSection"));
		return studentModel;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!PreferenceManager.getUserId(context).equalsIgnoreCase(""))
		{
			AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(context));
			AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(context));
			AppController.getInstance().trackScreenView("User Detail."+" "+"("+PreferenceManager.getUserEmail(mActivity)+")"+" "+ "("+Calendar.getInstance().getTime()+")");

		}

	}
}
