package com.mobatia.bskl.activity.notification;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.notification.adapter.StudentUnReadRecyclerAdapter;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.ResultConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import tcking.github.com.giraffeplayer.GiraffePlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by Rijo on 25/1/17.
 */
public class AudioPlayerViewActivity extends AppCompatActivity implements IntentPassValueConstants, ResultConstants, StatusConstants, URLConstants, JSONConstants {
    Bundle extras;
    String url;
    Context mContext;
    GiraffePlayer player;
    ArrayList<PushNotificationModel> alertlist;
    Activity activity;
    int position;
    ImageView backImg;
    ImageView home;
    private String title = null;
    TextView msgTitle;
    RecyclerView studentRecycleUnread;
    ImageView action_bar_forward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.audio_player_push_activity);
        mContext = this;
        activity = this;
        AppController.isfromUnread = false;
        extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt(POSITION);
            AppController.isfromUnread = extras.getBoolean("isfromUnread");
            title = extras.getString("title");
            AppController.isfromRead = extras.getBoolean("isfromRead");

            alertlist = (ArrayList<PushNotificationModel>) extras
                    .getSerializable(PASS_ARRAY_LIST);
            url = alertlist.get(position).getUrl();
        }
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
        headerTitle.setText("Message");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_forward.setVisibility(View.INVISIBLE);

        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppUtils.hideKeyBoard(mContext);
                //finish();
                onBackPressed();

            }
        });
        msgTitle = findViewById(R.id.msgTitle);
        studentRecycleUnread = findViewById(R.id.studentRecycle);
        msgTitle.setText(title);
        studentRecycleUnread.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        studentRecycleUnread.setLayoutManager(llm);
        StudentUnReadRecyclerAdapter mStudentRecyclerAdapter = new StudentUnReadRecyclerAdapter(mContext, alertlist.get(position).getStudentArray());
        studentRecycleUnread.setAdapter(mStudentRecyclerAdapter);
        if (alertlist.get(position).getStudent_name().equalsIgnoreCase("")) {
            studentRecycleUnread.setVisibility(View.GONE);

        }

        player = new GiraffePlayer(this);
        player.play(url);
        player.onComplete(new Runnable() {
            @Override
            public void run() {
//callback when video is finish
                Toast.makeText(getApplicationContext(), "Play completed", Toast.LENGTH_SHORT).show();
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
//do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
//do something when buffering end
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
//download speed
//                        ((TextView) findViewById(R.id.tv_speed)).setText(Formatter.formatFileSize(getApplicationContext(), extra)+"/s");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//do something when video rendering
//                        findViewById(R.id.tv_speed).setVisibility(View.GONE);
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                Toast.makeText(getApplicationContext(), "Can't play this audio", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Audio Notification Detail."+"("+PreferenceManager.getUserEmail(mContext)+")"+" "+"("+ Calendar.getInstance().getTime()+")");
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }




    public String replace(String str) {
        return str.replaceAll(" ", "%20");
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
                                            callstatusapi(statusread, pushId);
                                        }
                                    });
                                }
                            } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                        callstatusapi(statusread, pushId);
                                    }
                                });
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

    @Override
    public void onBackPressed() {
        AppController.pushId=alertlist.get(position).getPushid();
        finish();

    }
}
