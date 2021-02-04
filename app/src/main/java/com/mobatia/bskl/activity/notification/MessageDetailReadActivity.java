package com.mobatia.bskl.activity.notification;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.ResultConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

/**
 * Created by krishnaraj on 17/07/18.
 */

public class MessageDetailReadActivity  extends AppCompatActivity implements URLConstants, JSONConstants,NaisClassNameConstants,ResultConstants,StatusConstants {
    Bundle extras;
    private Context mContext = this;
    private String mLoadUrl = null;
    private String message = "";
    String tab_type;
    private String date = "";
    private String title = "";
    TextView msgTitle;
    TextView msgDate;
    TextView mWebView;
    int position;
    RecyclerView studentRecycleUnread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_detail_desc);
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("message");
            date = extras.getString("date");
            message = extras.getString("title");
            position = extras.getInt("position");
        }
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
        TextView headerTitle = view.findViewById(R.id.headerTitle);
        ImageView logoClickImgView = view.findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = view.findViewById(R.id.action_bar_forward);
        ImageView action_bar_back = view.findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        headerTitle.setText("Messages");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msgDate = findViewById(R.id.msgDate);
        msgTitle = findViewById(R.id.msgTitle);
        studentRecycleUnread = findViewById(R.id.studentRecycle);
        msgDate.setText(AppController.mMessageUnreadList.get(position).getDay());
        msgTitle.setText(AppController.mMessageUnreadList.get(position).getTitle());
        studentRecycleUnread.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        studentRecycleUnread.setLayoutManager(llm);


    }


	private void callstatusapi(final String statusread, final String pushId) {
		try {
			final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_STATUS_CHANGE);
			String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_ID, "status", JTAG_PUSH_ID};
			String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), statusread, pushId};
			manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

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
									AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
										}
									});
                                    callstatusapi(statusread, pushId);

                                }
							} else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
									responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
								AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
									@Override
									public void getAccessToken() {
									}
								});
                                callstatusapi(statusread, pushId);

                            } else {
								Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

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
}
