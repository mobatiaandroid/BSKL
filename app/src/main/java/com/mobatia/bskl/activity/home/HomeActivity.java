package com.mobatia.bskl.activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.home.adapter.HomeListAdapter;
import com.mobatia.bskl.activity.home.model.TimeTableStudentSettings;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisDBConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.absence.AbsenceFragment;
import com.mobatia.bskl.fragment.attendance.AttendanceInfoActivity;
import com.mobatia.bskl.fragment.attendance.AttendenceFragment;
import com.mobatia.bskl.fragment.calendar.CalendarInfoActivity;
import com.mobatia.bskl.fragment.calendar.ListViewCalendar;
import com.mobatia.bskl.fragment.contact_us.ContactUsFragment;
import com.mobatia.bskl.fragment.home.HomeScreenFragment;
import com.mobatia.bskl.fragment.news.NewsFragment;
import com.mobatia.bskl.fragment.notification.NotificationFragmentPagination;
import com.mobatia.bskl.fragment.notification.NotificationInfoActivity;
import com.mobatia.bskl.fragment.report.ReportFragment;
import com.mobatia.bskl.fragment.safeguarding.SafeGuardingFragment;
import com.mobatia.bskl.fragment.settings.SettingsFragment;
import com.mobatia.bskl.fragment.settings.model.StudentDetailSettingModel;
import com.mobatia.bskl.fragment.socialmedia.SocialMediaFragment;
import com.mobatia.bskl.fragment.timetable.TimeTableFragmentN;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, GestureDetector.OnGestureListener
        , NaisDBConstants, NaisTabConstants, View.OnClickListener,
        NaisClassNameConstants ,JSONConstants,URLConstants{

    LinearLayout linearLayout;
    public static ListView mHomeListView;
    private HomeListAdapter mListAdapter;
    private Context mContext;
    private Activity mActivity;
    private String android_app_version;
    private String replaceAndroidVersion;
    private int replaceCurrentVersion;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public static String[] mListItemArray;
    private GestureDetector mDetector;
    private Fragment mFragment = null;
    public static TypedArray mListImgArray;
    public static int sPosition;
    ImageView downarrow;
    private int preLast;
    int notificationRecieved = 0;
    Bundle extras;
    ImageView imageButton;
    TextView headerTitle;
    private static final int PERMISSION_CALLBACK_CONSTANT_CALENDAR = 1;
    private static final int PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2;
    private static final int PERMISSION_CALLBACK_CONSTANT_LOCATION = 3;
    private static final int REQUEST_PERMISSION_CALENDAR = 101;
    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 102;
    private static final int REQUEST_PERMISSION_LOCATION = 103;
    String[] permissionsRequiredCalendar = new String[]{Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR};
    String[] permissionsRequiredExternalStorage = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] permissionsRequiredLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private SharedPreferences calendarPermissionStatus;
    private SharedPreferences externalStoragePermissionStatus;
    private SharedPreferences locationPermissionStatus;
    private boolean calendarToSettings = false;
    private boolean externalStorageToSettings = false;
    private boolean locationToSettings = false;
    int tabPositionProceed = 0;
    ImageView imageButton2;
    ImageView logoClickImgView;
    boolean fromsplash = false;
    ImageView homePageLogoImg;
    View view;
    Toolbar toolbar;
    RelativeLayout maintopRel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        mContext = this;
        mActivity = this;
        calendarPermissionStatus = getSharedPreferences("calendarPermissionStatus", MODE_PRIVATE);
        externalStoragePermissionStatus = getSharedPreferences("externalStoragePermissionStatus", MODE_PRIVATE);
        locationPermissionStatus = getSharedPreferences("locationPermissionStatus", MODE_PRIVATE);

        if (PreferenceManager.getUpdate(mContext).equalsIgnoreCase("0"))
        {
            if (AppUtils.isNetworkConnected(mContext))
            {
                AppUtils.deviceRegistration(mContext);
                PreferenceManager.setUpdate(mContext,"1");
            }
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            notificationRecieved = extras.getInt("Notification_Recieved", 0);
            fromsplash = extras.getBoolean("fromsplash");
            Log.e("NOTIFY",String.valueOf(notificationRecieved));
        }
        initialiseUI();
        initialSettings();

        if (notificationRecieved == 1) {
            displayView(0);
            displayView(2);

        } else if (notificationRecieved == 2) {
            displayView(0);
            displayView(1);

        }else if (notificationRecieved == 3) {
            displayView(0);
            displayView(7);

        } else {
            displayView(0);
        }
        if (fromsplash) {
//            maintopRel.setBackgroundColor(mContext.getResources().getColor(R.color.rel_one));
//            homePageLogoImg.setImageResource(R.drawable.logo_teal);
            homePageLogoImg.setImageResource(R.drawable.logo);
            Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_out);
//        animSlide.setDuration(2200);
// Start the animation like this
            animSlide.setStartOffset(500);

            homePageLogoImg.startAnimation(animSlide);

            animSlide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 20 seconds
                            homePageLogoImg.setImageResource(R.drawable.logo);


                        }

                    }, 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                        }
                    },4000);


                }


                @Override
                public void onAnimationEnd(Animation animation) {
                    homePageLogoImg.setVisibility(View.GONE);
                    maintopRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    view.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    mDrawerLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            maintopRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }
    }

    /*******************************************************
     * Method name : initialiseUI() Description : initialise UI elements
     * Parameters : nil Return type : void Date : Oct 29, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        homePageLogoImg = (ImageView) findViewById(R.id.homePageLogoImg);
        maintopRel = (RelativeLayout) findViewById(R.id.maintopRel);

        mHomeListView = findViewById(R.id.homeList);
        downarrow = findViewById(R.id.downarrow);
        linearLayout = findViewById(R.id.linearLayout);
        mListItemArray = mContext.getResources().getStringArray(
                R.array.home_list_content_items);
        mListImgArray = mContext.getResources().obtainTypedArray(
                R.array.home_list_reg_icons);
        mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                R.layout.custom_list_adapter, true);
        mHomeListView.setAdapter(mListAdapter);
        mHomeListView.setBackgroundColor(getResources().getColor(
                R.color.split_bg));
        mDrawerLayout = findViewById(R.id.drawer_layout);
        /** change home list width */
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) linearLayout
                .getLayoutParams();
        params.width = width;
        linearLayout.setLayoutParams(params);
        mHomeListView.setOnItemClickListener(this);
        mHomeListView.setOnItemLongClickListener(this);

        mDetector = new GestureDetector(this);
        mDrawerToggle = new ActionBarDrawerToggle((Activity) mContext,
                mDrawerLayout, R.drawable.menu, R.string.null_value,
                R.string.null_value) {

            public void onDrawerClosed(View view) {

                mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mDetector.onTouchEvent(event);
                    }
                });
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mDetector.onTouchEvent(event);
                    }
                });
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });

        mHomeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (view.getId() == mHomeListView.getId()) {
                    final int currentFirstVisibleItem = mHomeListView.getLastVisiblePosition();
//                    System.out.println("currentFirstVisibleItem---" + currentFirstVisibleItem);
//                    System.out.println("currentFirstVisibleItem---" + firstVisibleItem);
//                    System.out.println("currentFirstVisibleItem---" + mHomeListView.getCount());
                    if (currentFirstVisibleItem == totalItemCount - 1) {
                        downarrow.setVisibility(View.INVISIBLE);
                    } else {
                        downarrow.setVisibility(View.VISIBLE);

                    }

                }
            }
        });

    }

    /*******************************************************
     * Method name : initialSettings Description : initial settings in home
     * screen Parameters : nil Return type : void Date : Oct 29, 2014 Author :
     * Rijo K Jose
     *****************************************************/
    private void initialSettings() {


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);
        view = getSupportActionBar().getCustomView();
        toolbar = (Toolbar) view.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        imageButton = view.findViewById(R.id.action_bar_back);
        imageButton2 = view.findViewById(R.id.action_bar_forward);
        headerTitle = view.findViewById(R.id.headerTitle);
        ImageView logoClickImgViewTransparent = view.findViewById(R.id.logoClickImgViewTransparent);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("bisad current fragment "
                        + currentFragment.getClass().toString());
                if (mDrawerLayout.isDrawerOpen(linearLayout)) {
                    mDrawerLayout.closeDrawer(linearLayout);
                } else {
                    mDrawerLayout.openDrawer(linearLayout);
                }

            }
        });


        if (fromsplash) {

            view.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);

        } else {
            homePageLogoImg.setVisibility(View.GONE);
            maintopRel.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            view.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            mDrawerLayout.setVisibility(View.VISIBLE);
        }
        logoClickImgViewTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("BSKL current fragment "
                        + currentFragment.getClass().toString());
                if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.notification.NotificationFragmentPagination") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.calendar.ListViewCalendar") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.absence.AbsenceFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.contact_us.ContactUsFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.report.ReportFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.socialmedia.SocialMediaFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.settings.SettingsFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.attendance.AttendenceFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.timetable.TimeTableFragmentN")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.safeguarding.SafeGuardingFragment")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.news.NewsFragment")){
//                    onBackPressed();

                    displayView(0);

                }
            }
        });
        logoClickImgView = view.findViewById(R.id.logoClickImgView);

        if (!PreferenceManager.getUserId(mContext).equals("")) {
        }
        imageButton2.setVisibility(View.VISIBLE);


        logoClickImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("bskl current fragment "
                        + currentFragment.getClass().toString());
                if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.settings.SettingsFragment")) {
//                    onBackPressed();

                    displayView(0);

                }


            }
        });


        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Forward Button is clicked", Toast.LENGTH_LONG).show();
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("bskl current fragment "
                        + currentFragment.getClass().toString());
                if (!(currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.settings.SettingsFragment")) && !(currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.notification.NotificationFragmentPagination")) && !(currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.calendar.ListViewCalendar")) && !(currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.attendance.AttendenceFragment"))) {
                    mFragment = new SettingsFragment(SETTINGS, TAB_SETTINGS);
                    if (mFragment != null) {
                        imageButton2.setVisibility(View.INVISIBLE);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.frame_container, mFragment, SETTINGS)
                                .addToBackStack(SETTINGS).commit();
                        mDrawerLayout.closeDrawer(linearLayout);
                        getSupportActionBar().setTitle(R.string.null_value);
                        imageButton2.setVisibility(View.INVISIBLE);
                    }

                } else if (currentFragment.getClass().toString().equalsIgnoreCase("class com.mobatia.bskl.fragment.notification.NotificationFragmentPagination")) {
                    Intent intent = new Intent(HomeActivity.this, NotificationInfoActivity.class);
                    startActivity(intent);
                } else if (currentFragment.getClass().toString().equalsIgnoreCase("class com.mobatia.bskl.fragment.calendar.ListViewCalendar")) {
                    Intent intent = new Intent(HomeActivity.this, CalendarInfoActivity.class);
                    startActivity(intent);
                } else if (currentFragment.getClass().toString().equalsIgnoreCase("class com.mobatia.bskl.fragment.attendance.AttendenceFragment")) {
                    Intent intent = new Intent(HomeActivity.this, AttendanceInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        mDrawerToggle.syncState();


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    private  void showSettingUserDetail(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response User Detail" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);

                        if (status_code.equalsIgnoreCase("303")) {
                            //for(int i=0;i<responseArray.length();i++){
                            JSONObject respObj = secobj.getJSONObject(JTAG_RESPONSE_ARRAY);
                            //String userId=respObj.optString(JTAG_USER_ID);
                            //PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USER_ID));
                            PreferenceManager.setPhoneNo(mContext, respObj.optString(JTAG_PHONE_NO));
                            PreferenceManager.setFullName(mContext, respObj.optString("name"));
                            PreferenceManager.setCalenderPush(mContext, respObj.optString("calenderpush"));
                            PreferenceManager.setEmailPush(mContext, respObj.optString("emailpush"));
                            PreferenceManager.setMessageBadge(mContext, respObj.optString("messagebadge"));
                            PreferenceManager.setCalenderBadge(mContext, respObj.optString("calenderbadge"));
                            PreferenceManager.setReportMailMerge(mContext, respObj.optString("reportmailmerge"));
                            PreferenceManager.setCorrespondenceMailMerge(mContext, respObj.optString("correspondencemailmerge"));
                            //log.e( "data: ",respObj.optString("data_collection"));
                          /*  if(respObj.optString("data_collection").equals("1")){
                                showDataCollectionUserDetail();
                            }*/
                            android_app_version = secobj.optString("android_version");
                            System.out.println("app version from api"+android_app_version);
                            PreferenceManager.setVersionFromApi(mContext,android_app_version);
                            //  replaceAndroidVersion=Integer.parseInt(PreferenceManager.getVersionFromApi(mContext).replaceAll("."," "));
                            System.out.println("calendarBadge" + PreferenceManager.getCalenderBadge(mContext));
                            JSONArray studentDetail = respObj.getJSONArray("studentdetails");
                            ArrayList<StudentDetailSettingModel> mDatamodel = new ArrayList<StudentDetailSettingModel>();


                            String versionFromPreference = PreferenceManager.getVersionFromApi(mContext).replace(".","");
                            Log.d("LOG","versionFromPreference = "+versionFromPreference);

                            int versionNumberAsInteger = Integer.parseInt(versionFromPreference);
                            Log.d("LOG","versionNumberAsInteger = "+versionNumberAsInteger);

                            if (studentDetail.length() > 0) {

                                for (int i = 0; i < studentDetail.length(); i++)

                                {
                                    JSONObject dataObject = studentDetail.getJSONObject(i);
                                    StudentDetailSettingModel xmodel = new StudentDetailSettingModel();
                                    xmodel.setId(dataObject.optString("id"));
                                    xmodel.setAlumi(dataObject.optString("alumi"));
                                    xmodel.setApplicant(dataObject.optString("applicant"));
                                    //  xmodel.setType(dataObject.optString("type"));
                                    mDatamodel.add(xmodel);

                                }
                            }


                            int alumini = 0;
                            for (int x = 0; x < mDatamodel.size(); x++) {
                                if (mDatamodel.get(x).getAlumi().equalsIgnoreCase("0")) {
                                    alumini = 0;
                                    break;
                                }
                                else {
                                    alumini = 1;

                                }
                            }
                            int applicant=0;
                            for (int x = 0; x < mDatamodel.size(); x++) {
                                if (mDatamodel.get(x).getApplicant().equalsIgnoreCase("0")) {
                                    applicant = 0;
                                    break;
                                }
                                else {
                                    applicant = 1;

                                }
                            }
                            PreferenceManager.setIsVisible(mContext, String.valueOf(alumini));
                            PreferenceManager.setIsApplicant(mContext, String.valueOf(applicant));

                           /* if (PreferenceManager.getMessageBadge(mContext).equalsIgnoreCase("0")) {
                                messageBtn.setText("");
                            } else {
                                messageBtn.setText(PreferenceManager.getMessageBadge(mContext));
                            }
                            if (PreferenceManager.getCalenderBadge(mContext).equalsIgnoreCase("0")) {
                                calendarBtn.setText("");

                            } else {
                                calendarBtn.setText(PreferenceManager.getCalenderBadge(mContext));
                            }*/
                            System.out.println("badgehomecountMessage:"+(Integer.valueOf(PreferenceManager.getMessageBadge(mContext))));
                            System.out.println("badgehomecountCalender:" + Integer.valueOf(PreferenceManager.getCalenderBadge(mContext)));
                            System.out.println("badgehomecount:"+(Integer.valueOf(PreferenceManager.getMessageBadge(mContext))+ Integer.valueOf(PreferenceManager.getCalenderBadge(mContext))));

                            ShortcutBadger.applyCount(mContext, Integer.valueOf(PreferenceManager.getMessageBadge(mContext))+ Integer.valueOf(PreferenceManager.getCalenderBadge(mContext))); //for 1.1.4+

                            JSONObject socialmediaObj = secobj.getJSONObject("socialmedia");

                            PreferenceManager.setFbkey(mContext, socialmediaObj.optString("fbkey"));
                            PreferenceManager.setInstaKey(mContext, socialmediaObj.optString("inkey"));
                            PreferenceManager.setYouKey(mContext, socialmediaObj.optString("youtubekey"));

                            //}
                            String replaceVersion = AppUtils.getVersionInfo(mContext).replace(".","");
                            replaceCurrentVersion=Integer.parseInt(replaceVersion);
                            Log.d("LOG","versionFrom = "+replaceVersion);
                            Log.d("LOG","versionFrom:: = "+replaceCurrentVersion);

                            //  System.out.println("Replace current version"+replaceVersion);
                            if (!(PreferenceManager.getVersionFromApi(mContext).equalsIgnoreCase(""))) {
                                if(versionNumberAsInteger >replaceCurrentVersion) {

                                    AppUtils.showDialogAlertUpdate(mContext);
                                }

                            }
                            JSONArray timeTableStudArray = secobj.getJSONArray("timetable_student");
                            ArrayList<TimeTableStudentSettings> mTimeTableStudArraylist = new ArrayList<TimeTableStudentSettings>();

                            if (timeTableStudArray.length() > 0) {

                                for (int i = 0; i < timeTableStudArray.length(); i++)

                                {
                                    JSONObject dataObject = timeTableStudArray.getJSONObject(i);
                                    TimeTableStudentSettings xmodel = new TimeTableStudentSettings();
                                    xmodel.setId(dataObject.optString("id"));
                                    xmodel.setStudent_name(dataObject.optString("student_name"));
                                    xmodel.setType(dataObject.optString("type"));
                                    //  xmodel.setType(dataObject.optString("type"));
                                    mTimeTableStudArraylist.add(xmodel);

                                }
                            }


                            Boolean isAvailable=false;
                            if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("1"))
                            {
                                System.out.println("working if datamodel");

                                for (int i=0;i<mTimeTableStudArraylist.size();i++)
                                {
                                    System.out.println("working if datamodel");
                                    System.out.println("working if datamodel type"+mTimeTableStudArraylist.get(i).getType());
                                    if (mTimeTableStudArraylist.get(i).getType().equalsIgnoreCase("Primary"))
                                    {
                                        System.out.println("working if datamodel");
                                        isAvailable=true;
                                    }
                                }
                            }
                            else if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("2"))
                            {
                                System.out.println("working if datamodel");

                                for (int i=0;i<mTimeTableStudArraylist.size();i++)
                                {
                                    System.out.println("working if datamodel");
                                    System.out.println("working if datamodel type"+mTimeTableStudArraylist.get(i).getType());
                                    if (mTimeTableStudArraylist.get(i).getType().equalsIgnoreCase("Secondary"))
                                    {
                                        System.out.println("working if datamodel");
                                        isAvailable=true;
                                    }
                                }
                            }
                            else if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("3"))
                            {
                                System.out.println("working if datamodel");

                                for (int i=0;i<mTimeTableStudArraylist.size();i++)
                                {
                                    System.out.println("working if datamodel");

                                    if (mTimeTableStudArraylist.get(i).getType().equalsIgnoreCase("Secondary") || mTimeTableStudArraylist.get(i).getType().equalsIgnoreCase("Primary") )
                                    {
                                        System.out.println("working if datamodel");
                                        isAvailable=true;
                                    }
                                }
                            }
                            if (isAvailable)
                            {
                                PreferenceManager.setTimeTableAvailable(mContext,"1");
                            }
                            else
                            {
                                PreferenceManager.setTimeTableAvailable(mContext,"0");
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
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
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/

            }
        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        System.out.println("Position working:");

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            final int position, long id) {
        if (PreferenceManager.getIfHomeItemClickEnabled(mContext)) {
            System.out.println("Position homelist:" + position);
//            imageButton2.setVisibility(View.VISIBLE);
            displayView(position);
        }
    }

    /*******************************************************
     * Method name : displayView Description : display fragment view according
     * to position Parameters : position Return type : void Date : Oct 29, 2014
     * Author : Rijo K Jose
     *****************************************************/
    private void displayView(int position) {
        mFragment = null;
        tabPositionProceed = position;

        if (!(PreferenceManager.getUserId(mContext).equals(""))) {
            if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
            {
                if(PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    switch (position) {

                        case 0:
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new ListViewCalendar(mListItemArray[position],
                                    TAB_CALENDAR);
                            replaceFragmentsSelected(position);

                            break;
                        case 2:
                            // Messages
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new NotificationFragmentPagination(mListItemArray[position],
                                    TAB_MESSAGES);
                            replaceFragmentsSelected(position);

                            break;
                        case 3:
                            // News
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment= new NewsFragment(mListItemArray[position],TAB_NEWS);
                            replaceFragmentsSelected(position);
                        /*if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }*/
                            break;
                        case 4:
                            // Social media
                      /*  imageButton2.setImageResource(R.drawable.settings);
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }*/
                            imageButton2.setVisibility(View.VISIBLE);

                            imageButton2.setImageResource(R.drawable.settings);

                            mFragment = new SocialMediaFragment(mListItemArray[position],
                                    TAB_SOCIAL_MEDIA);
                            replaceFragmentsSelected(position);
                            break;
                        case 5:
                            // Report
                            imageButton2.setVisibility(View.VISIBLE);
                            imageButton2.setImageResource(R.drawable.settings);
                            mFragment = new ReportFragment(mListItemArray[position],
                                    TAB_REPORT);
                            replaceFragmentsSelected(position);

                            break;

                        case 8:
                            // Attendance


                            if (PreferenceManager.getAttendance(mContext).equalsIgnoreCase("1")) {
                                imageButton2.setImageResource(R.drawable.tutorial_icon);
                                imageButton2.setVisibility(View.VISIBLE);

                                mFragment = new AttendenceFragment(mListItemArray[position],
                                        TAB_ATTENDANCE);
                                replaceFragmentsSelected(position);
                            }else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }
                            break;
                        case 6:
                            // Absence
                            if (PreferenceManager.getAbsence(mContext).equalsIgnoreCase("1")) {
                                imageButton2.setImageResource(R.drawable.settings);
                                imageButton2.setVisibility(View.VISIBLE);

                                mFragment = new AbsenceFragment(mListItemArray[position],
                                        TAB_ABSENCE);
                                replaceFragmentsSelected(position);
                            }else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }
                            break;
                        case 7:
                            // TAB_SAFE_GUARDING


                            if (PreferenceManager.getSafeGuarding(mContext).equalsIgnoreCase("1")) {
                                imageButton2.setImageResource(R.drawable.settings);
                                imageButton2.setVisibility(View.VISIBLE);

                                mFragment = new SafeGuardingFragment(mListItemArray[position],
                                        TAB_SAFE_GUARDING);
                                replaceFragmentsSelected(position);
                            }else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }
                            break;
                    /*case 8:
                        // Behaviour
                        imageButton2.setImageResource(R.drawable.settings);
                        imageButton2.setVisibility(View.VISIBLE);

                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        break;*/
                        case 9:
                            // TimeTable
                            if (PreferenceManager.getTimeTable(mContext).equalsIgnoreCase("1")) {
                                if (PreferenceManager.getTimeTableAvailable(mContext).equalsIgnoreCase("1"))
                                {
                                    imageButton2.setImageResource(R.drawable.settings);
                                    imageButton2.setVisibility(View.VISIBLE);

                                    mFragment = new TimeTableFragmentN(mListItemArray[position],
                                            TAB_TIME_TABLE);
                                    replaceFragmentsSelected(position);
                                }
                                else
                                {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                                }

                            }
                            else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }

                            break;

                  /*  case 10:
                        // News
                        imageButton2.setImageResource(R.drawable.settings);
                        imageButton2.setVisibility(View.VISIBLE);

                        mFragment= new TimeTableFragment(mListItemArray[position],TAB_TIME_TABLE);
                        replaceFragmentsSelected(position);
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        break;*/
                        case 10:
                            // Contact Us

                   /* mFragment = new ContactUsFragment(mListItemArray[position],
                            TAB_CONTACT_US);
                    replaceFragmentsSelected(position);*/
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            if (Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }

                            break;


                  /*  case 10:
                        //school buddy
                        if (AppUtils.isNetworkConnected(mContext)) {
                            if (isPackageExisted("com.everybuddy.schoolsbuddyv2")) {
                                launchApp("com.everybuddy.schoolsbuddyv2");

                            } else {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.everybuddy.schoolsbuddyv2")));
                            }

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        replaceFragmentsSelected(position);
                        break;*/

                        default:
                            break;
                    }
                }
                else
                {
                    switch (position) {

                        case 0:
                            // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new ListViewCalendar(mListItemArray[position],
                                    TAB_CALENDAR);
                            replaceFragmentsSelected(position);

                            break;
                        case 2:
                            // Messages
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new NotificationFragmentPagination(mListItemArray[position],
                                    TAB_MESSAGES);
                            replaceFragmentsSelected(position);

                            break;
                        case 3:
                            // News
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment= new NewsFragment(mListItemArray[position],TAB_NEWS);
                            replaceFragmentsSelected(position);
                            break;
                        case 4:
                            // Social media
                            imageButton2.setVisibility(View.VISIBLE);

                            imageButton2.setImageResource(R.drawable.settings);

                            mFragment = new SocialMediaFragment(mListItemArray[position],
                                    TAB_SOCIAL_MEDIA);
                            replaceFragmentsSelected(position);
                            break;

                        case 5:
                            // Report
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 6:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 7:
                            // SafeGuarding
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 8:
                            // Attendance
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 9:
                            // TimeTable
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

//                            if (PreferenceManager.getTimeTable(mContext).equalsIgnoreCase("1"))
//                            {
//                                if (PreferenceManager.getTimeTableAvailable(mContext).equalsIgnoreCase("1"))
//                                {
//                                    imageButton2.setImageResource(R.drawable.settings);
//                                    imageButton2.setVisibility(View.VISIBLE);
//
//                                    mFragment = new TimeTableFragmentN(mListItemArray[position],
//                                            TAB_TIME_TABLE);
//                                    replaceFragmentsSelected(position);
//                                }
//                                else
//                                {
//                                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);
//
//                                }
//
//                            }
//                            else
//                            {
//                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);
//
//                            }

                            break;
                        case 10:
                            // Contact Us

                            mFragment = new ContactUsFragment(mListItemArray[position],
                                    TAB_CONTACT_US);
                            replaceFragmentsSelected(position);
                            if ((int) Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }
                            break;

                        default:
                            break;
                    }
                }


            } else
            {

                if(PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("0"))
                {
                    switch (position) {

                        case 0:
                            // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 2:
                            // Messages
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 3:
                            // News
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 4:
                            // Social media
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                            break;

                        case 5:
                            // Report
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 6:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 7:
                            // Attendance
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 8:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;

                        case 9:
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 10:
                            // Contact Us

                            mFragment = new ContactUsFragment(mListItemArray[position],
                                    TAB_CONTACT_US);
                            replaceFragmentsSelected(position);
                            if ((int) Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }
                      /*  if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
*/
                            break;
                        /*case 11:

                         *//* if(AppUtils.isNetworkConnected(mContext)){
                            if(isPackageExisted("com.subsbuddy.schoolsbuddyasia"))
                            {
                                launchApp("com.subsbuddy.schoolsbuddyasia");

                            }
                            else
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.subsbuddy.schoolsbuddyasia" )));
                            }

                        }else{
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        replaceFragmentsSelected(position);*//*
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }


                        break;*/


                        default:
                            break;
                    }
                }
                else
                {
                    switch (position) {

                        case 0:
                            // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 2:
                            // Messages
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 3:
                            // News
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 4:
                            // Social media
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                            break;

                        case 5:
                            // Report
                            imageButton2.setVisibility(View.VISIBLE);
                            imageButton2.setImageResource(R.drawable.settings);
                            mFragment = new ReportFragment(mListItemArray[position],
                                    TAB_REPORT);
                            replaceFragmentsSelected(position);

                            break;
                        case 6:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 7:
                            // Attendance
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 8:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;

                        case 9:
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 10:
                            // Contact Us

                            mFragment = new ContactUsFragment(mListItemArray[position],
                                    TAB_CONTACT_US);
                            replaceFragmentsSelected(position);
                            if ((int) Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
        }
    }

    /*******************************************************
     * Method name : replaceFragmentsSelected Description : replace fragments
     * Parameters : position Return type : void Date : Nov 4, 2014 Author :
     * Rijo K Jose
     *****************************************************/
    private void replaceFragmentsSelected(final int position) {
        if (mFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, mFragment,
                            mListItemArray[position])
                    .addToBackStack(mListItemArray[position]).commitAllowingStateLoss();
            mHomeListView.setItemChecked(position, true);
            mHomeListView.setSelection(position);
            mDrawerLayout.closeDrawer(linearLayout);
            getSupportActionBar().setTitle(R.string.null_value);
        }
    }

    PermissionListener permissionContactuslistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
            mFragment = new ContactUsFragment(mListItemArray[6],
                    TAB_CONTACT_US);
            replaceFragmentsSelected(6);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.AppCompatActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        ActionBar actionBar = HomeActivity.this.getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(linearLayout);

        } else {
            imageButton2.setImageResource(R.drawable.settings);
            headerTitle.setVisibility(View.GONE);
            logoClickImgView.setVisibility(View.VISIBLE);
            System.out.println("popstack count:::" + getSupportFragmentManager().getBackStackEntryCount());
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm
                        .findFragmentById(R.id.frame_container);
                System.out.println("bskl current fragment "
                        + currentFragment.getClass().toString());

                if (!(currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.settings.SettingsFragment"))) {
                    imageButton2.setVisibility(View.VISIBLE);
                } else {
                    imageButton2.setVisibility(View.INVISIBLE);

                }
                if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.home.HomeScreenFragment")
                ) {
                    AppUtils.showAlert((Activity) mContext, getResources()
                            .getString(R.string.exit_alert), getResources()
                            .getString(R.string.ok), getResources()
                            .getString(R.string.cancel), true);
                } else if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.contact_us.ContactUsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.notification.NotificationFragmentPagination")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.calendar.ListViewCalendar") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.bskl.fragment.attendance.AttendenceFragment")) {
//                    imageButton.setImageResource(R.drawable.hamburgerbtn);
                    imageButton2.setVisibility(View.VISIBLE);


                    displayView(0);
                    /*|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.settings.SettingsFragment")*/
                } else {
                    imageButton2.setVisibility(View.VISIBLE);
//                    getSupportFragmentManager().popBackStack();
                    displayView(0);


                }
            }
            // Default action on back pressed
            else {
                AppUtils.showAlert((Activity) mContext, getResources()
                                .getString(R.string.exit_alert), getResources()
                                .getString(R.string.ok),
                        getResources().getString(R.string.cancel), true);
            }

        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onShowPress(android.view
     * .MotionEvent)
     */
    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.
     * view.MotionEvent)
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onScroll(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onLongPress(android.view
     * .MotionEvent)
     */
    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        return false;
    }


    @Override
    public void onClick(View v) {
        // settings
        mFragment = new SettingsFragment(SETTINGS, TAB_SETTINGS);
        if (mFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, mFragment, SETTINGS)
                    .addToBackStack(SETTINGS).commit();
            mDrawerLayout.closeDrawer(linearLayout);
            getSupportActionBar().setTitle(R.string.null_value);
        }
    }


    private void proceedAfterPermission(int position) {

        System.out.println("workingPerm1");
        if (!PreferenceManager.getUserId(mContext).equals("")) {
            if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
            {
                if(PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    switch (position) {

                        case 0:
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new ListViewCalendar(mListItemArray[position],
                                    TAB_CALENDAR);
                            replaceFragmentsSelected(position);

                            break;
                        case 2:
                            // Messages
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new NotificationFragmentPagination(mListItemArray[position],
                                    TAB_MESSAGES);
                            replaceFragmentsSelected(position);

                            break;
                        case 3:
                            // News
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment= new NewsFragment(mListItemArray[position],TAB_NEWS);
                            replaceFragmentsSelected(position);
                        /*if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }*/
                            break;
                        case 4:
                            // Social media
                      /*  imageButton2.setImageResource(R.drawable.settings);
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }*/
                            imageButton2.setVisibility(View.VISIBLE);

                            imageButton2.setImageResource(R.drawable.settings);

                            mFragment = new SocialMediaFragment(mListItemArray[position],
                                    TAB_SOCIAL_MEDIA);
                            replaceFragmentsSelected(position);
                            break;
                        case 5:
                            // Report
                            imageButton2.setVisibility(View.VISIBLE);
                            imageButton2.setImageResource(R.drawable.settings);
                            mFragment = new ReportFragment(mListItemArray[position],
                                    TAB_REPORT);
                            replaceFragmentsSelected(position);

                            break;

                        case 8:
                            // Attendance


                            if (PreferenceManager.getAttendance(mContext).equalsIgnoreCase("1")) {
                                imageButton2.setImageResource(R.drawable.tutorial_icon);
                                imageButton2.setVisibility(View.VISIBLE);

                                mFragment = new AttendenceFragment(mListItemArray[position],
                                        TAB_ATTENDANCE);
                                replaceFragmentsSelected(position);
                            }else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }
                            break;
                        case 6:
                            // Absence
                            if (PreferenceManager.getAbsence(mContext).equalsIgnoreCase("1")) {
                                imageButton2.setImageResource(R.drawable.settings);
                                imageButton2.setVisibility(View.VISIBLE);

                                mFragment = new AbsenceFragment(mListItemArray[position],
                                        TAB_ABSENCE);
                                replaceFragmentsSelected(position);
                            }else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }
                            break;
                        case 7:
                            // TAB_SAFE_GUARDING


                            if (PreferenceManager.getSafeGuarding(mContext).equalsIgnoreCase("1")) {
                                imageButton2.setImageResource(R.drawable.settings);
                                imageButton2.setVisibility(View.VISIBLE);

                                mFragment = new SafeGuardingFragment(mListItemArray[position],
                                        TAB_SAFE_GUARDING);
                                replaceFragmentsSelected(position);
                            }else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }
                            break;
                    /*case 8:
                        // Behaviour
                        imageButton2.setImageResource(R.drawable.settings);
                        imageButton2.setVisibility(View.VISIBLE);

                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        break;*/
                        case 9:
                            // TimeTable
                            if (PreferenceManager.getTimeTable(mContext).equalsIgnoreCase("1")) {
                                if (PreferenceManager.getTimeTableAvailable(mContext).equalsIgnoreCase("1"))
                                {
                                    imageButton2.setImageResource(R.drawable.settings);
                                    imageButton2.setVisibility(View.VISIBLE);

                                    mFragment = new TimeTableFragmentN(mListItemArray[position],
                                            TAB_TIME_TABLE);
                                    replaceFragmentsSelected(position);
                                }
                                else
                                {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                                }

                            }
                            else
                            {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                            }

                            break;

                  /*  case 10:
                        // News
                        imageButton2.setImageResource(R.drawable.settings);
                        imageButton2.setVisibility(View.VISIBLE);

                        mFragment= new TimeTableFragment(mListItemArray[position],TAB_TIME_TABLE);
                        replaceFragmentsSelected(position);
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        break;*/
                        case 10:
                            // Contact Us

                   /* mFragment = new ContactUsFragment(mListItemArray[position],
                            TAB_CONTACT_US);
                    replaceFragmentsSelected(position);*/
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            if (Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }

                            break;


                  /*  case 10:
                        //school buddy
                        if (AppUtils.isNetworkConnected(mContext)) {
                            if (isPackageExisted("com.everybuddy.schoolsbuddyv2")) {
                                launchApp("com.everybuddy.schoolsbuddyv2");

                            } else {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.everybuddy.schoolsbuddyv2")));
                            }

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        replaceFragmentsSelected(position);
                        break;*/

                        default:
                            break;
                    }
                }
                else
                {
                    switch (position) {

                        case 0:
                            // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new ListViewCalendar(mListItemArray[position],
                                    TAB_CALENDAR);
                            replaceFragmentsSelected(position);

                            break;
                        case 2:
                            // Messages
                            imageButton2.setImageResource(R.drawable.tutorial_icon);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new NotificationFragmentPagination(mListItemArray[position],
                                    TAB_MESSAGES);
                            replaceFragmentsSelected(position);

                            break;
                        case 3:
                            // News
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment= new NewsFragment(mListItemArray[position],TAB_NEWS);
                            replaceFragmentsSelected(position);
                            break;
                        case 4:
                            // Social media
                            imageButton2.setVisibility(View.VISIBLE);

                            imageButton2.setImageResource(R.drawable.settings);

                            mFragment = new SocialMediaFragment(mListItemArray[position],
                                    TAB_SOCIAL_MEDIA);
                            replaceFragmentsSelected(position);
                            break;

                        case 5:
                            // Report
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 6:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 7:
                            // SafeGuarding
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 8:
                            // Attendance
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 9:
                            // TimeTable
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
//                            if (PreferenceManager.getTimeTable(mContext).equalsIgnoreCase("1")) {
//                                if (PreferenceManager.getTimeTableAvailable(mContext).equalsIgnoreCase("1"))
//                                {
//                                    imageButton2.setImageResource(R.drawable.settings);
//                                    imageButton2.setVisibility(View.VISIBLE);
//
//                                    mFragment = new TimeTableFragmentN(mListItemArray[position],
//                                            TAB_TIME_TABLE);
//                                    replaceFragmentsSelected(position);
//                                }
//                                else
//                                {
//                                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);
//
//                                }
//
//                            }
//                            else
//                            {
//                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);
//
//                            }

                            break;
                        case 10:
                            // Contact Us

                            mFragment = new ContactUsFragment(mListItemArray[position],
                                    TAB_CONTACT_US);
                            replaceFragmentsSelected(position);
                            if ((int) Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }
                            break;

                        default:
                            break;
                    }
                }


            } else
            {

                if(PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("0"))
                {
                    switch (position) {

                        case 0:
                            // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 2:
                            // Messages
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 3:
                            // News
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 4:
                            // Social media
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                            break;

                        case 5:
                            // Report
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 6:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 7:
                            // Attendance
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 8:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;

                        case 9:
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 10:
                            // Contact Us

                            mFragment = new ContactUsFragment(mListItemArray[position],
                                    TAB_CONTACT_US);
                            replaceFragmentsSelected(position);
                            if ((int) Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }
                      /*  if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
*/
                            break;
                        /*case 11:

                         *//* if(AppUtils.isNetworkConnected(mContext)){
                            if(isPackageExisted("com.subsbuddy.schoolsbuddyasia"))
                            {
                                launchApp("com.subsbuddy.schoolsbuddyasia");

                            }
                            else
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.subsbuddy.schoolsbuddyasia" )));
                            }

                        }else{
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        replaceFragmentsSelected(position);*//*
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }


                        break;*/


                        default:
                            break;
                    }
                }
                else
                {
                    switch (position) {

                        case 0:
                            // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                            imageButton2.setImageResource(R.drawable.settings);
                            imageButton2.setVisibility(View.VISIBLE);

                            mFragment = new HomeScreenFragment(
                                    mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                    mListItemArray, mListImgArray);
                            replaceFragmentsSelected(position);
                            headerTitle.setVisibility(View.GONE);
                            logoClickImgView.setVisibility(View.VISIBLE);
                            HomeListAdapter mListAdapter = new HomeListAdapter(mContext, mListItemArray, mListImgArray,
                                    R.layout.custom_list_adapter, true);
                            mHomeListView.setAdapter(mListAdapter);
                            break;
                        case 1:
                            // Calendar
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 2:
                            // Messages
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 3:
                            // News
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 4:
                            // Social media
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                            break;

                        case 5:
                            // Report
                            imageButton2.setVisibility(View.VISIBLE);
                            imageButton2.setImageResource(R.drawable.settings);
                            mFragment = new ReportFragment(mListItemArray[position],
                                    TAB_REPORT);
                            replaceFragmentsSelected(position);

                            break;
                        case 6:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 7:
                            // Attendance
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 8:
                            // Absence
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;

                        case 9:
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        case 10:
                            // Contact Us

                            mFragment = new ContactUsFragment(mListItemArray[position],
                                    TAB_CONTACT_US);
                            replaceFragmentsSelected(position);
                            if ((int) Build.VERSION.SDK_INT >= 23) {
                                TedPermission.with(mContext)
                                        .setPermissionListener(permissionContactuslistener)
                                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .check();
                            } else {
                                mFragment = new ContactUsFragment(mListItemArray[position],
                                        TAB_CONTACT_US);
                                replaceFragmentsSelected(position);
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
        }
//        replaceFragmentsSelected(position);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT_CALENDAR) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(tabPositionProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_CALENDAR)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Calendar Permissions");
                builder.setMessage("This module needs calendar permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        calendarToSettings = false;

                        requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendarToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_CALENDAR)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Calendar Permissions");
                builder.setMessage("This module needs calendar permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        calendarToSettings = false;

                        requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendarToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
//                Toast.makeText(mActivity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                calendarToSettings = true;
                System.out.println("Permission4");

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_CALENDAR);
                Toast.makeText(mContext, "Go to settings and grant access to calendar", Toast.LENGTH_LONG).show();

            }
        } else if (requestCode == PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(tabPositionProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Storage Permissions");
                builder.setMessage("This module needs storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        externalStorageToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        externalStorageToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_CALENDAR)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Storage Permissions");
                builder.setMessage("This module needs storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        externalStorageToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        externalStorageToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
//                Toast.makeText(mActivity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                externalStorageToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
                Toast.makeText(mContext, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();

            }
        } else if (requestCode == PERMISSION_CALLBACK_CONSTANT_LOCATION) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(tabPositionProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This module needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This module needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
//                Toast.makeText(mActivity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                locationToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                Toast.makeText(mContext, "Go to settings and grant access to location", Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CALENDAR) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                System.out.println("Permission5");

                proceedAfterPermission(tabPositionProceed);
            }
        } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabPositionProceed);
            }
        } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabPositionProceed);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
        System.out.println("bskl current fragment "
                + currentFragment.getClass().toString());
        if ((currentFragment
                .getClass()
                .toString()
                .equalsIgnoreCase(
                        "class com.mobatia.bskl.fragment.home.HomeScreenFragment"))) {
            if (position >= 3) {
                // drag list view item
                PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
                sPosition = position;

                view.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));

                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                ClipData data = ClipData.newPlainText("", "");
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                mDrawerLayout.closeDrawer(linearLayout);

            }

            else {
                // if home in list view is selected
                AppUtils.showAlert((Activity) mContext,
                        getResources().getString(R.string.drag_impossible), "",
                        getResources().getString(R.string.ok), false);
                PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
            }
            return true;

        } else {
            return false;

        }
    }

    protected void launchApp(String packageName) {
       /*
           PackageManager
               Class for retrieving various kinds of information related to the application
               packages that are currently installed on the device.
       */
       /*
           public abstract PackageManager getPackageManager ()
               Return PackageManager instance to find global package information.
       */
        // Get an instance of PackageManager
        PackageManager pm = mContext.getPackageManager();

        // Try to launch an app
        try {
           /*
               public abstract Intent getLaunchIntentForPackage (String packageName)
                   Returns a "good" intent to launch a front-door activity in a package. This is used,
                   for example, to implement an "open" button when browsing through packages.
                   The current implementation looks first for a main activity in the category
                   CATEGORY_INFO, and next for a main activity in the category CATEGORY_LAUNCHER.
                   Returns null if neither are found.

               Parameters
                   packageName : The name of the package to inspect.
               Returns
                   A fully-qualified Intent that can be used to launch the main activity in the
                   package. Returns null if the package does not contain such an activity,
                   or if packageName is not recognized.
           */
            // Initialize a new Intent
            Intent intent = pm.getLaunchIntentForPackage(packageName);

           /*
               public Intent addCategory (String category)
                   Add a new category to the intent. Categories provide additional detail about
                   the action the intent performs. When resolving an intent, only activities that
                   provide all of the requested categories will be used.

               Parameters
                   category : The desired category. This can be either one of the predefined
                   Intent categories, or a custom category in your own namespace.
               Returns
                   Returns the same Intent object, for chaining multiple calls into a single statement.
           */
           /*
               public static final String CATEGORY_LAUNCHER
                   Should be displayed in the top-level launcher.

                   Constant Value: "android.intent.category.LAUNCHER"
           */
            // Add category to intent
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            if (intent == null) {
                // Throw PackageManager NameNotFoundException
                throw new PackageManager.NameNotFoundException();
            } else {
                // Start the app
                mContext.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
           /*
               public static int e (String tag, String msg)
                   Send an ERROR log message.

               Parameters
                   tag : Used to identify the source of a log message. It usually identifies the
                       class or activity where the log call occurs.
                   msg : The message you would like logged.
           */
            // Log the exception
            //log.e("Launch", e.getMessage());
        }
    }

    public boolean isPackageExisted(String targetPackage) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
//        AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
//        AppController.getInstance().trackScreenView("Home Page" + " " + PreferenceManager.getUserEmail(mContext) + " " + Calendar.getInstance().getTime());

        super.onResume();
    }
   /* private  void showDataCollectionUserDetail() {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_GET_DATA_COLLECTION_DETAILS);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response DATA COLLECTION" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONObject respObj = secobj.getJSONObject("data");
                            //log.e("shared: ", String.valueOf(respObj));
                            SharedPreferences.Editor editor = getSharedPreferences("BSKL", MODE_PRIVATE).edit();
                            editor.putString("DATA_COLLECTION", String.valueOf(respObj));
                            editor.apply();
                            Intent i = new Intent(HomeActivity.this, DataCollectionHome.class);
                            startActivity(i);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                       // showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                      //  showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                      //  showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else {
                        *//*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*//*
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				*//*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*//*

            }
        });


    }
*/
}
