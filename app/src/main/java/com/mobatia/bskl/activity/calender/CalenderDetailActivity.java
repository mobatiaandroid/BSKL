package com.mobatia.bskl.activity.calender;

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
import com.mobatia.bskl.activity.calender.adapter.StudentRecyclerCalenderAdapter;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.calendar.model.CalendarModel;
import com.mobatia.bskl.fragment.calendar.model.StudentDetailModel;
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
import java.util.Locale;

/**
 * Created by krishnaraj on 25/07/18.
 */

public class CalenderDetailActivity  extends AppCompatActivity implements
        IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

    TextView txtmsg;
    TextView mDateTv;
    TextView mTimeTv;
    ImageView img;
    TextView studName;
    ImageView home;
    Bundle extras;
    ArrayList<CalendarModel> eventArraylist;
    ArrayList<StudentDetailModel> mStudentModel;
    int position;
    ImageView add_cl;
    Context context = this;
    Activity mActivity;
    RelativeLayout header;
    ImageView back;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    RecyclerView studentRecycleUnread;
    TextView msgTitle;
    String title="";
//    String date="";
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private boolean mErrorFlag = false;
    RotateAnimation anim;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.calender_detail_layout);
        mActivity = this;
        mContext = this;
        add_cl = findViewById(R.id.add_cl);
        studName = findViewById(R.id.studName);
        msgTitle= findViewById(R.id.msgTitle);
        studentRecycleUnread= findViewById(R.id.studentRecycle);
        img = findViewById(R.id.image);
        txtmsg = findViewById(R.id.txt);
        mDateTv = findViewById(R.id.mDateTv);
        mTimeTv = findViewById(R.id.mTimeTv);
        mWebView = findViewById(R.id.webView);
        mProgressRelLayout = findViewById(R.id.progressDialog);

        extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt(POSITION);

          title=extras.getString(TITLE);
//          date=extras.getString("date");
            eventArraylist = (ArrayList<CalendarModel>) extras
                    .getSerializable(PASS_ARRAY_LIST);
            mLoadUrl= "<!DOCTYPE html>\n" +
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
                    ".venue {"+

                    "font-family: SourceSansPro-Regular;"+
                    "font-size:16px;"+
                    "text-align:left;"+
                    "color:	#A9A9A9;"+
                    "}"+
                    ".title {"+
                    "font-family: SourceSansPro-Regular;"+
                    "font-size:16px;"+
                    "text-align:left;"+
                    "color:	#46C1D0;"+
                    "}"+
                    ".description {"+
                    "font-family: SourceSansPro-Semibold;"+
                    "text-align:justify;"+
                    "font-size:14px;"+
                    "color: #000000;"+
                    "}"+".date {"+
                    "font-family: SourceSansPro-Semibold;"+
                    "text-align:right;"+
                    "font-size:12px;"+

                    "color: #A9A9A9;"+
                    "}"+
                    "</style>\n"+"</head>"+
                    "<body>";
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            String mdate =eventArraylist.get(position).getDateCalendar();
            String mEndDate =eventArraylist.get(position).getEnddate();
            Date date = null;
            Date endDate = null;
            try {
                date = inputFormat.parse(mdate);
                endDate = inputFormat.parse(mEndDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(date);
            String outputEndDateStr = outputFormat.format(endDate);

            mDateTv.setText((outputDateStr));

            DateFormat startFormat= new SimpleDateFormat("H:mm:ss");
            DateFormat newStart= new SimpleDateFormat("h:mm a");
            String starttime=eventArraylist.get(position).getStarttime();
            Date newSTime = null;
            try {
                newSTime = startFormat.parse(starttime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String newstart = newStart.format(newSTime);
            String endtime=eventArraylist.get(position).getEndtime();
            Date newETime = null;
            try {
                newETime = startFormat.parse(endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String newend = newStart.format(newETime);
            if(eventArraylist.get(position).getAllday().equalsIgnoreCase("0"))
            {
                mTimeTv.setText((newstart)+" "+"to"+" "+(newend));

            }
            else
            {
                mTimeTv.setText("(All day)");

            }
            if (eventArraylist.get(position).getVenue().toString().length()>0) {
                mLoadUrl=mLoadUrl+"<p class='venue'>Venue: "+eventArraylist.get(position).getVenue()+"</p>";
            }
            mLoadUrl=mLoadUrl+
                    "<p class='description'>"+eventArraylist.get(position).getDescription()+"</p>";


            if (eventArraylist.get(position).getDaterange().equalsIgnoreCase(""))
            {
                if (mdate.equalsIgnoreCase(mEndDate))
                {
                    mLoadUrl=mLoadUrl+"<p class='date'>"+outputDateStr +"</p>"+"<p class='date'>"+mTimeTv.getText().toString() +"</p>"+
                            "</body>\n</html>";
                }
                else
                {
                    mLoadUrl=mLoadUrl+"<p class='date'>"+outputDateStr+" to "+ outputEndDateStr+"</p>"+"<p class='date'>"+mTimeTv.getText().toString() +"</p>"+
                            "</body>\n</html>";
                }
            }
            else
            {
                if (mdate.equalsIgnoreCase(mEndDate))
                {
                    mLoadUrl=mLoadUrl+"<p class='date'>"+outputDateStr +"</p>"+"<p class='date'>"+mTimeTv.getText().toString() +"</p>"+
                            "</body>\n</html>";
                }
                else
                {
                    mLoadUrl=mLoadUrl+"<p class='date'>"+outputDateStr+" to "+ outputEndDateStr+"</p>"+"<p class='date'>"+eventArraylist.get(position).getDayrange() +"</p>"+"<p class='date'>"+mTimeTv.getText().toString() +"</p>"+
                            "</body>\n</html>";
                }
            }

        }

        try {
            initialiseUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getWebViewSettings();

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
        headerTitle.setText("Calendar");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_forward.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msgTitle.setText(title);


        studentRecycleUnread.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        studentRecycleUnread.setLayoutManager(llm);
        StudentRecyclerCalenderAdapter mStudentRecyclerCalenderAdapter= new StudentRecyclerCalenderAdapter(context,eventArraylist.get(position).getmStudentModel());
        studentRecycleUnread.setAdapter(mStudentRecyclerCalenderAdapter);
        add_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = 0, endTime = 0;

                try {
                    Date dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(eventArraylist.get(position).getStartDatetime());
                    startTime = dateStart.getTime();
                    Date dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(eventArraylist.get(position).getEndDatetime());
                    endTime = dateEnd.getTime();
                } catch (Exception e) {
                }

                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", startTime);
                if(eventArraylist.get(position).getAllday().equalsIgnoreCase("1"))
                {
                    intent.putExtra("allDay", true);

                }
                else
                {
                    intent.putExtra("allDay", false);

                }
                intent.putExtra("rule", "FREQ=YEARLY");
                intent.putExtra("endTime", endTime);
                intent.putExtra("title", eventArraylist.get(position).getTittle());
                startActivity(intent);
            }
        });
        setDetails();

    }

    private void setDetails() throws ParseException {
        msgTitle.setText(title);
        txtmsg.setText(eventArraylist.get(position).getDescription());
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String mdate =eventArraylist.get(position).getDateCalendar();
        Date date = inputFormat.parse(mdate);
        String outputDateStr = outputFormat.format(date);
        mDateTv.setText((outputDateStr));

        DateFormat startFormat= new SimpleDateFormat("H:mm:ss");
        DateFormat newStart= new SimpleDateFormat("h:mm a");
        String starttime=eventArraylist.get(position).getStarttime();
        Date newSTime = startFormat.parse(starttime);
        String newstart = newStart.format(newSTime);
        String endtime=eventArraylist.get(position).getEndtime();
        Date newETime = startFormat.parse(endtime);
        String newend = newStart.format(newETime);
        if(eventArraylist.get(position).getAllday().equalsIgnoreCase("0"))
        {
            mTimeTv.setText((newstart)+" "+"to"+" "+(newend));

        }
        else
        {
            mTimeTv.setText("(All day)");

        }
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
                            responsCode = obj.getString(JTAG_RESPONSECODE);
                            if (responsCode.equalsIgnoreCase("200")) {
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
                            } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                    responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mActivity, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callstatusapi(statusread, pushId);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

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
    private void getWebViewSettings() {


        mProgressRelLayout.setVisibility(View.GONE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(context, android.R.interpolator.linear);
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
                context.getCacheDir().getAbsolutePath());
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
                if (AppUtils.checkInternet(context) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    view.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

                    loadingFlag = false;
                } else if (!AppUtils.checkInternet(context) && loadingFlag) {
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
                if (AppUtils.checkInternet(context)) {
                    AppUtils.showAlertFinish((Activity) context, getResources()
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
            AppUtils.showAlertFinish((Activity) context, getResources()
                            .getString(R.string.common_error_loading_page), "",
                    getResources().getString(R.string.ok), false);
        }

    }

    @Override
    protected void onResume() {
        if(!(PreferenceManager.getUserId(mContext).equalsIgnoreCase(" ")))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Calendar Event Detail. "+ "("+PreferenceManager.getUserEmail(mContext)+")"+" "+ "("+Calendar.getInstance().getTime()+ ")");
        }

        super.onResume();
    }
}
