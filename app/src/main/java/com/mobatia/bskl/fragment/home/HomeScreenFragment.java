package com.mobatia.bskl.fragment.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.DataCollectionHome;
import com.mobatia.bskl.activity.data_collection.model.NationalityModel;
import com.mobatia.bskl.activity.datacollection_p2.adapter.InsuranceStudentRecyclerAdapter;
import com.mobatia.bskl.activity.datacollection_p2.model.ContactTypeModel;
import com.mobatia.bskl.activity.datacollection_p2.model.GlobalListDataModel;
import com.mobatia.bskl.activity.datacollection_p2.model.GlobalListSirname;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.home.HomeActivity;
import com.mobatia.bskl.activity.home.model.TimeTableStudentSettings;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.absence.AbsenceFragment;
import com.mobatia.bskl.fragment.attendance.AttendenceFragment;
import com.mobatia.bskl.fragment.calendar.ListViewCalendar;
import com.mobatia.bskl.fragment.contact_us.ContactUsFragment;
import com.mobatia.bskl.fragment.news.NewsFragment;
import com.mobatia.bskl.fragment.notification.NotificationFragmentPagination;
import com.mobatia.bskl.fragment.report.ReportFragment;
import com.mobatia.bskl.fragment.safeguarding.SafeGuardingFragment;
import com.mobatia.bskl.fragment.settings.model.StudentDetailSettingModel;
import com.mobatia.bskl.fragment.socialmedia.SocialMediaFragment;
import com.mobatia.bskl.fragment.timetable.TimeTableFragmentN;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by krishnaraj on 27/06/18.
 */

public class HomeScreenFragment extends Fragment implements
        View.OnClickListener, NaisTabConstants, NaisClassNameConstants, JSONConstants, StatusConstants, URLConstants {
    private View mRootView;
    private Context mContext;
    private View viewTouched = null;
    private String TAB_ID;
    private String android_app_version;
    private String INTENT_TAB_ID;
    private String[] mSectionText;
    private boolean isDraggable;
    Button messageBtn;
    Button newsBtn;
    Button socialBtn;
    Button calendarBtn;
    ImageView img;
    int currentPage = 0;
    boolean isKinFound=false;
    boolean isLocalFound=false;
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
    String tabiDToProceed = "";
    private TextView firstTextView;
    private TextView secondTextView;
    private TextView thirdTextView;
    private TextView safeGuardingBadge;
    String alreadyTriggered;
    private TextView fourthTextView;
    ImageView secondImageView;
    ImageView thirdImageView;
    RelativeLayout firstRelativeLayout;
    RelativeLayout secondRelativeLayout;
    RelativeLayout thirdRelativeLayout;
    RelativeLayout fourthRelativeLayout;
    ArrayList<OwnContactModel>mOwnContactArrayList;
    ArrayList<StudentModelNew> studentsModelArrayList;
    ArrayList<GlobalListDataModel>mGlobalListDataArrayList;
    ArrayList<ContactTypeModel>ContactTypeArray;

    ArrayList<GlobalListSirname>mGlobalListSirnameArrayList;
    ArrayList<GlobalListSirname>ContactListArray;
    ArrayList<InsuranceDetailModel>InsuranceHealthListArray;
    ArrayList<PassportDetailModel>PassportDetailListArray;
    ArrayList<KinModel>KinArray;
    ArrayList<NationalityModel>mNationlityListArrrayList;

    public HomeScreenFragment(String title,
                              DrawerLayout mDrawerLayouts, ListView listView, LinearLayout linearLayout,
                              String[] mListItemArray, TypedArray mListImgArray) {
        AppController.mTitles = title;
        AppController.mDrawerLayouts = mDrawerLayouts;
        AppController.mLinearLayouts = linearLayout;
        AppController.mListViews = listView;
        AppController.listitemArrays = mListItemArray;
        AppController.mListImgArrays = mListImgArray;

    }

    public HomeScreenFragment() {
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_home_activity, container,
                false);
        mContext = getActivity();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageReceiver, new IntentFilter("badgenotify"));

        calendarPermissionStatus = getActivity().getSharedPreferences("calendarPermissionStatus", getActivity().MODE_PRIVATE);
        externalStoragePermissionStatus = getActivity().getSharedPreferences("externalStoragePermissionStatus", getActivity().MODE_PRIVATE);
        locationPermissionStatus = getActivity().getSharedPreferences("locationPermissionStatus", getActivity().MODE_PRIVATE);
        initUI();
        mSectionText = new String[4];
        Log.e("DEVICE ID", FirebaseInstanceId.getInstance().getToken());
        setDragListenersForButtons();
        getButtonBgAndTextImages();
        return mRootView;

    }

    private void getTabId(String text) {
        // System.out.println("text wiss " + text);
        if (text.equalsIgnoreCase(CALENDAR)) {
            TAB_ID = TAB_CALENDAR;
        } else if (text.equalsIgnoreCase(MESSAGE)) {
            TAB_ID = TAB_MESSAGES;
        } else if (text.equalsIgnoreCase(NEWS) || text.equalsIgnoreCase(BSKL_NEWS)) {
            TAB_ID = TAB_NEWS;
        } else if (text.equalsIgnoreCase(SOCIAL_MEDIA)) {
            TAB_ID = TAB_SOCIAL_MEDIA;
        } else if (text.equalsIgnoreCase(REPORT)) {
            TAB_ID = TAB_REPORT;
        } else if (text.equalsIgnoreCase(PROGRESS_REPORT)) {
            TAB_ID = TAB_REPORT;
        } else if (text.equalsIgnoreCase(ABSENCE)) {
            TAB_ID = TAB_ABSENCE;
        } else if (text.equalsIgnoreCase(SAFE_GUARDING)) {
            TAB_ID = TAB_SAFE_GUARDING;
        } else if (text.equalsIgnoreCase(ATTENDANCE)) {
            TAB_ID = TAB_ATTENDANCE;
        } else if (text.equalsIgnoreCase(TIMETABLE)) {
            TAB_ID = TAB_TIME_TABLE;
        } else if (text.equalsIgnoreCase(CONTACT_US)) {
            TAB_ID = TAB_CONTACT_US;
        }
        /*else if (text.equalsIgnoreCase(BEHAVIOUR)) {
            TAB_ID = TAB_BEHAVIOUR;
        }*/
//        else if (text.equalsIgnoreCase(TIMETABLE)) {
//            TAB_ID = TAB_TIME_TABLE;
//        }
        /* else if (text.equalsIgnoreCase(SCHOOLBUDDY)) {
            TAB_ID = TAB_SCHOOLBUDDY;
        }*/

    }

    private void initUI() {
        img = mRootView.findViewById(R.id.img);
        messageBtn = mRootView.findViewById(R.id.messageBtn);
        newsBtn = mRootView.findViewById(R.id.newsBtn);
        socialBtn = mRootView.findViewById(R.id.socialBtn);
        calendarBtn = mRootView.findViewById(R.id.calendarBtn);
        firstRelativeLayout = mRootView.findViewById(R.id.firstRelativeLayout);
        secondRelativeLayout = mRootView.findViewById(R.id.secondRelativeLayout);
        thirdRelativeLayout = mRootView.findViewById(R.id.thirdRelativeLayout);
        fourthRelativeLayout = mRootView.findViewById(R.id.fourthRelativeLayout);
        firstTextView = mRootView.findViewById(R.id.firstTextView);
        secondTextView = mRootView.findViewById(R.id.secondTextView);
        thirdTextView = mRootView.findViewById(R.id.thirdTextView);
        fourthTextView = mRootView.findViewById(R.id.fourthTextView);
        secondImageView = mRootView.findViewById(R.id.secondImageView);
        thirdImageView = mRootView.findViewById(R.id.thirdImageView);
        safeGuardingBadge = mRootView.findViewById(R.id.safeGuardingBadge);
        messageBtn.setOnClickListener(this);
        newsBtn.setOnClickListener(this);
        socialBtn.setOnClickListener(this);
        calendarBtn.setOnClickListener(this);
        if (AppUtils.isNetworkConnected(mContext)) {
            showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }


    }

    private void getButtonBgAndTextImages() {

        if (Integer.parseInt(PreferenceManager
                .getButtonTwoGuestTextImage(mContext)) != 0) {
            final int sdk = Build.VERSION.SDK_INT;

            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                secondImageView.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                        .parseInt(PreferenceManager
                                .getButtonTwoGuestTextImage(mContext))));
            } else {
                secondImageView.setBackground(AppController.mListImgArrays.getDrawable(Integer
                        .parseInt(PreferenceManager
                                .getButtonTwoGuestTextImage(mContext))));
            }

            String relOneStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonTwoGuestTextImage(mContext))].toString().equalsIgnoreCase(NEWS) || AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonTwoGuestTextImage(mContext))].toString().equalsIgnoreCase(BSKL_NEWS)) {
                relOneStr = BSKL_NEWS;
            } else {
                relOneStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonTwoGuestTextImage(mContext))];

            }
            secondTextView.setText(relOneStr);

        }
        if (Integer.parseInt(PreferenceManager
                .getButtonThreeGuestTextImage(mContext)) != 0) {
            final int sdk = Build.VERSION.SDK_INT;

            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                thirdImageView.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                        .parseInt(PreferenceManager
                                .getButtonThreeGuestTextImage(mContext))));
            } else {
                thirdImageView.setBackground(AppController.mListImgArrays.getDrawable(Integer
                        .parseInt(PreferenceManager
                                .getButtonThreeGuestTextImage(mContext))));
            }
            String relTwoStr = "";
            // System.out.println("three text " + PreferenceManager.getButtonThreeGuestTextImage(mContext));
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonThreeGuestTextImage(mContext))].toString().equalsIgnoreCase(NEWS) || AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonThreeGuestTextImage(mContext))].toString().equalsIgnoreCase(BSKL_NEWS)) {
                relTwoStr = BSKL_NEWS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonThreeGuestTextImage(mContext))];

            }
            thirdTextView.setText(relTwoStr);
        }


    }

    @SuppressLint("NewApi")
    private void setDragListenersForButtons() {
        firstRelativeLayout.setOnDragListener(new DropListener());
        secondRelativeLayout.setOnDragListener(new DropListener());
        thirdRelativeLayout.setOnDragListener(new DropListener());
        fourthRelativeLayout.setOnDragListener(new DropListener());
    }

    @SuppressLint("NewApi")
    private class DropListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    AppController.mDrawerLayouts.closeDrawer(AppController.mLinearLayouts);
                    int arr[] = new int[2];
                    view.getLocationInWindow(arr);
                    int x = arr[0];
                    int y = arr[1];
                    getButtonViewTouched(x, y);
                    if (viewTouched == secondRelativeLayout) {
                        mSectionText[0] = secondTextView.getText().toString();
                        mSectionText[1] = thirdTextView.getText().toString();
                        mSectionText[2] = thirdTextView.getText().toString();
                        mSectionText[3] = fourthTextView.getText().toString();
                        for (int i = 0; i < mSectionText.length; i++) {
                            isDraggable = true;

                            if (mSectionText[i]
                                    .equalsIgnoreCase(AppController.listitemArrays[HomeActivity.sPosition])) {
                                isDraggable = false;
                                break;
                            }
                        }
                        if (isDraggable) {
                            getButtonDrawablesAndText(viewTouched,
                                    HomeActivity.sPosition);
                        } else {
                            AppUtils.showAlert((Activity) mContext, mContext
                                            .getResources().getString(R.string.drag_duplicate),
                                    "",
                                    mContext.getResources().getString(R.string.ok),
                                    false);
                        }
                    } else {
                        AppUtils.showAlert((Activity) mContext,
                                getResources().getString(R.string.drag_impossible), "",
                                getResources().getString(R.string.ok), false);

                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                default:
                    break;
            }
            return true;

        }
    }

    private void getButtonViewTouched(float centerX, float centerY) {

        // button one
        int arr1[] = new int[2];
        firstRelativeLayout.getLocationInWindow(arr1);
        int x1 = arr1[0];
        int x2 = x1 + firstRelativeLayout.getWidth();
        int y1 = arr1[1];
        int y2 = y1 + firstRelativeLayout.getHeight();

        // button two
        int arr2[] = new int[2];
        secondRelativeLayout.getLocationInWindow(arr2);
        int x3 = arr2[0];
        int x4 = x3 + secondRelativeLayout.getWidth();
        int y3 = arr2[1];
        int y4 = y3 + secondRelativeLayout.getHeight();

        // button three
        int arr3[] = new int[2];
        thirdRelativeLayout.getLocationInWindow(arr3);
        int x5 = arr3[0];
        int x6 = x5 + thirdRelativeLayout.getWidth();
        int y5 = arr3[1];
        int y6 = y5 + thirdRelativeLayout.getHeight();

        // button four
        int arr4[] = new int[2];
        fourthRelativeLayout.getLocationInWindow(arr4);
        int x7 = arr4[0];
        int x8 = x7 + fourthRelativeLayout.getWidth();
        int y7 = arr4[1];
        int y8 = y7 + fourthRelativeLayout.getHeight();


        if (x7 <= centerX && centerX <= x8 && y7 <= centerY
                && centerY <= y8) {
            viewTouched = fourthRelativeLayout;
        } else if (x5 <= centerX && centerX <= x6 && y5 <= centerY
                && centerY <= y6) {
            viewTouched = thirdRelativeLayout;
        } else if (x3 <= centerX && centerX <= x4 && y3 <= centerY
                && centerY <= y4) {
            viewTouched = secondRelativeLayout;
        } else {
            viewTouched = null;
        }


    }

    PermissionListener permissionContactuslistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Fragment mFragment = null;

            mFragment = new ContactUsFragment(CONTACT_US,
                    TAB_CONTACT_US);
            fragmentIntent(mFragment);

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void getButtonDrawablesAndText(View v, int position) {
        // System.out.println("testing function:" + position);
        if (position >= 3) {
            if (v == secondRelativeLayout) {

                String relTwoStr = "";
                if (AppController.listitemArrays[position].equalsIgnoreCase(BSKL_NEWS)) {
                    relTwoStr = BSKL_NEWS;
                } else {
                    relTwoStr = AppController.listitemArrays[position];

                }
                secondTextView.setText(relTwoStr);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonTwoGuestTabId(mContext, TAB_ID);
                PreferenceManager.setButtonTwoGuestTextImage(mContext,
                        Integer.toString(position));
                final int sdk = Build.VERSION.SDK_INT;

                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    secondImageView.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                            .parseInt(PreferenceManager
                                    .getButtonTwoGuestTextImage(mContext))));
                } else {
                    secondImageView.setBackground(AppController.mListImgArrays.getDrawable(Integer
                            .parseInt(PreferenceManager
                                    .getButtonTwoGuestTextImage(mContext))));
                }
            } else if (v == thirdRelativeLayout) {
                String relTwoStr = "";
                if (AppController.listitemArrays[position].equalsIgnoreCase(BSKL_NEWS)) {

                    relTwoStr = BSKL_NEWS;
                } else {
                    relTwoStr = AppController.listitemArrays[position];

                }
                thirdTextView.setText(relTwoStr);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonThreeGuestTabId(mContext, TAB_ID);
                PreferenceManager.setButtonThreeGuestTextImage(mContext,
                        Integer.toString(position));
                final int sdk = Build.VERSION.SDK_INT;

                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    thirdImageView.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                            .parseInt(PreferenceManager
                                    .getButtonThreeGuestTextImage(mContext))));
                } else {
                    thirdImageView.setBackground(AppController.mListImgArrays.getDrawable(Integer
                            .parseInt(PreferenceManager
                                    .getButtonThreeGuestTextImage(mContext))));
                }
            } else {
                AppUtils.showAlert((Activity) mContext,
                        getResources().getString(R.string.drag_impossible), "",
                        getResources().getString(R.string.ok), false);

            }

        } else {
            AppUtils.showAlert((Activity) mContext,
                    getResources().getString(R.string.drag_impossible), "",
                    getResources().getString(R.string.ok), false);
        }
        v = null;
        viewTouched = null;
    }

    private void checkIntent(String tabId) {
        tabiDToProceed = tabId;
        // System.out.println("tabId::" + tabId);
        Fragment mFragment = null;
        if (tabId.equalsIgnoreCase(TAB_MESSAGES)) {
            mFragment = new NotificationFragmentPagination(NOTIFICATIONS, TAB_MESSAGES);
            fragmentIntent(mFragment);
        } else if (tabId.equalsIgnoreCase(TAB_CALENDAR)) {
            mFragment = new ListViewCalendar(CALENDAR, TAB_CALENDAR);
            fragmentIntent(mFragment);
        } else if (tabId.equalsIgnoreCase(TAB_SOCIAL_MEDIA)) {

            mFragment = new SocialMediaFragment(SOCIAL_MEDIA, TAB_SOCIAL_MEDIA);
            fragmentIntent(mFragment);

           /* if (AppUtils.isNetworkConnected(mContext)) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }*/
        } else if (tabId.equalsIgnoreCase(TAB_NEWS)) {
            mFragment = new NewsFragment(BSKL_NEWS, TAB_NEWS);
            fragmentIntent(mFragment);
        } else if (tabId.equalsIgnoreCase(TAB_REPORT)) {
            mFragment = new ReportFragment(PROGRESS_REPORT, TAB_REPORT);
            fragmentIntent(mFragment);
        } else if (tabId.equalsIgnoreCase(TAB_ABSENCE)) {

            if (PreferenceManager.getAbsence(mContext).equalsIgnoreCase("1")) {
                // System.out.println("ddddd" + PreferenceManager.getAbsence(mContext));
                mFragment = new AbsenceFragment(ABSENCE, TAB_ABSENCE);
                fragmentIntent(mFragment);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

            }
        } else if (tabId.equalsIgnoreCase(TAB_SAFE_GUARDING)) {
            if (PreferenceManager.getSafeGuarding(mContext).equalsIgnoreCase("1")) {
                mFragment = new SafeGuardingFragment(SAFE_GUARDING, TAB_SAFE_GUARDING);
                fragmentIntent(mFragment);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

            }
        } else if (tabId.equalsIgnoreCase(TAB_ATTENDANCE)) {

            if (PreferenceManager.getAttendance(mContext).equalsIgnoreCase("1")) {
                mFragment = new AttendenceFragment(ATTENDANCE, TAB_ATTENDANCE);
                fragmentIntent(mFragment);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

            }
        } else if (tabId.equalsIgnoreCase(TAB_TIME_TABLE)) {

            if (PreferenceManager.getTimeTable(mContext).equalsIgnoreCase("1")) {
                if (PreferenceManager.getTimeTableAvailable(mContext).equalsIgnoreCase("1"))
                {
                    mFragment = new TimeTableFragmentN(TIMETABLE, TAB_TIME_TABLE);
                    fragmentIntent(mFragment);
                }
                else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

                }

            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_feature), R.drawable.exclamationicon, R.drawable.round);

            }
        } else if (tabId.equalsIgnoreCase(TAB_CONTACT_US)) {
            if (Build.VERSION.SDK_INT >= 23) {
                TedPermission.with(mContext)
                        .setPermissionListener(permissionContactuslistener)
                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

//                                .setPermissions(Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
//                                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            } else {
                mFragment = new ContactUsFragment(CONTACT_US,
                        TAB_CONTACT_US);
                fragmentIntent(mFragment);
            }

        } /*else if (tabId.equalsIgnoreCase(TAB_SCHOOLBUDDY)) {
            if (AppUtils.isNetworkConnected(mContext)) {
                if (isPackageExisted("com.subsbuddy.schoolsbuddyasia")) {
                    launchApp("com.subsbuddy.schoolsbuddyasia");

                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.subsbuddy.schoolsbuddyasia")));
                }

            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
            fragmentIntent(mFragment);
        }*/ else {
            if (AppUtils.isNetworkConnected(mContext)) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        }

    }

    void fragmentIntent(Fragment mFragment) {

        if (mFragment != null) {

            // System.out.println("title:" + AppController.mTitles);
            FragmentManager fragmentManager = getActivity()
                    .getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_container, mFragment, AppController.mTitles)
                    .addToBackStack(AppController.mTitles).commitAllowingStateLoss();//commit

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
        {
            if (v == messageBtn)
            {
                if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonOneTabId(mContext);
                    checkIntent(INTENT_TAB_ID);
                }
                else
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonOneTabId(mContext);
                    checkIntent(INTENT_TAB_ID);
                }

            } else if (v == calendarBtn) {
                if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonFourTabId(mContext);
                    checkIntent(INTENT_TAB_ID);
                }
                else
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonFourTabId(mContext);
                    checkIntent(INTENT_TAB_ID);
                }

            } else if (v == newsBtn) {
                if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonTwoGuestTabId(mContext);
                    checkIntent(INTENT_TAB_ID);
                }
                else
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonTwoGuestTabId(mContext);
                    if (INTENT_TAB_ID.equalsIgnoreCase(TAB_NEWS) || INTENT_TAB_ID.equalsIgnoreCase(TAB_SOCIAL_MEDIA) || INTENT_TAB_ID.equalsIgnoreCase(TAB_CONTACT_US))
                    {
                        checkIntent(INTENT_TAB_ID);
                    }
                    else
                    {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    }
                }

            } else if (v == socialBtn) {
                if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    INTENT_TAB_ID = PreferenceManager.getButtonsixTabId(mContext);
                    checkIntent(INTENT_TAB_ID);
                }
              else
                {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                }
            }

        }
        else
        {
            if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("1"))
            {
                if (v == messageBtn) {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else if (v == calendarBtn)
                {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else if (v == socialBtn) {

                    if (AppUtils.isNetworkConnected(mContext)) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else if (v == newsBtn)
                {
                    if (AppUtils.isNetworkConnected(mContext))
                    {
                        INTENT_TAB_ID = PreferenceManager.getButtonTwoGuestTabId(mContext);
                        if (INTENT_TAB_ID.equalsIgnoreCase(TAB_REPORT) || INTENT_TAB_ID.equalsIgnoreCase(TAB_CONTACT_US)) {
                            checkIntent(INTENT_TAB_ID);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        }

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                }
            }
            else
            {
                if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                {
                    if (v == messageBtn) {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else if (v == calendarBtn) {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else if (v == socialBtn) {

                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else if (v == newsBtn)
                    {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            INTENT_TAB_ID = PreferenceManager.getButtonTwoGuestTabId(mContext);
                            if (INTENT_TAB_ID.equalsIgnoreCase(TAB_REPORT) || INTENT_TAB_ID.equalsIgnoreCase(TAB_CONTACT_US)) {
                                checkIntent(INTENT_TAB_ID);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            }

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    }
                }
                else
                {
                    if (v == messageBtn) {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else if (v == calendarBtn) {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else if (v == socialBtn) {

                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } else if (v == newsBtn)
                    {
                        if (AppUtils.isNetworkConnected(mContext)) {
                            INTENT_TAB_ID = PreferenceManager.getButtonTwoGuestTabId(mContext);
                            if (INTENT_TAB_ID.equalsIgnoreCase(TAB_CONTACT_US)) {
                                checkIntent(INTENT_TAB_ID);

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available), R.drawable.exclamationicon, R.drawable.round);

                            }

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    }
                }

            }




        }
    }

    private void proceedAfterPermission(String tabiDFromProceed) {

        Fragment mFragment = null;
        if (tabiDFromProceed.equalsIgnoreCase(TAB_CALENDAR)) {
            mFragment = new ListViewCalendar(CALENDAR, TAB_CALENDAR);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_MESSAGES)) {
            mFragment = new NotificationFragmentPagination(MESSAGE, TAB_MESSAGES);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_REPORT)) {
            mFragment = new ReportFragment(REPORT, TAB_REPORT);
           /* if (AppUtils.isNetworkConnected(mContext)) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.coming_soon), R.drawable.exclamationicon, R.drawable.round);

            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }*/

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_NEWS)) {
            mFragment = new NewsFragment(BSKL_NEWS, TAB_NEWS);

        }
        fragmentIntent(mFragment);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                proceedAfterPermission(tabiDToProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CALENDAR)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Calendar Permissions");
                builder.setMessage("This module needs calendar permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        calendarToSettings = false;

                        requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                    }
                });
                builder.setNegativeButton("Canzcel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendarToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Calendar Permissions");
                builder.setMessage("This module needs calendar permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
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
                calendarToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
                proceedAfterPermission(tabiDToProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permissions");
                builder.setMessage("This module needs storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        externalStorageToSettings = false;

                        requestPermissions(permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permissions");
                builder.setMessage("This module needs storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        externalStorageToSettings = false;

                        requestPermissions(permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
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
                externalStorageToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
                proceedAfterPermission(tabiDToProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This module needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This module needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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
                locationToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabiDToProceed);
            }
        } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabiDToProceed);
            }
        } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabiDToProceed);
            }
        }
    }


    private void showSettingUserDetail(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                 System.out.println("The response is count" + successResponse);
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
                            PreferenceManager.setsafenote(mContext, respObj.optString("safeguarding_notification"));
                            android_app_version = secobj.optString("android_version");
                            PreferenceManager.setDataCollection(mContext,respObj.optString("data_collection"));
                            PreferenceManager.setDataCollectionTriggerType(mContext,respObj.optString("trigger_type"));
                            String prefAlreadyTriggered=PreferenceManager.getAlreadyTriggered(mContext);
                             alreadyTriggered=respObj.optString("already_triggered");

                             if (prefAlreadyTriggered.equalsIgnoreCase(alreadyTriggered))
                             {
                                 PreferenceManager.setAlreadyTriggered(mContext,respObj.optString("already_triggered"));
                             }
                             else
                             {
                                 PreferenceManager.setAlreadyTriggered(mContext,respObj.optString("already_triggered"));
                                 PreferenceManager.setIsAlreadyEnteredOwnContact(mContext,false);
                                 PreferenceManager.setIsAlreadyEnteredKin(mContext,false);
                                 PreferenceManager.setIsAlreadyEnteredInsurance(mContext,false);
                                 PreferenceManager.setIsAlreadyEnteredPassport(mContext,false);
                                 PreferenceManager.setIsAlreadyEnteredStudentList(mContext,false);
                             }

//                            SharedPreferences prefs = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE);
//                            String data = prefs.getString("data_collection_flag"," ");
//                            if(data.equalsIgnoreCase("0"))
//                            {

                            if (PreferenceManager.getPreviousTriggerType(mContext).equalsIgnoreCase(""))
                            {
                                PreferenceManager.setPreviousTriggerType(mContext,respObj.optString("trigger_type"));
                            }
                            else
                            {
                                if (PreferenceManager.getPreviousTriggerType(mContext).equalsIgnoreCase(PreferenceManager.getDataCollectionTriggerType(mContext)))
                                {
                                    if (PreferenceManager.getAlreadyTriggered(mContext).equalsIgnoreCase("1"))
                                    {

                                    }
                                    else
                                    {
                                        AppController.kinArrayShow.clear();
                                        AppController.kinArrayPass.clear();
                                        ArrayList<OwnContactModel>mOwnArrayList=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);
                                        mOwnArrayList.clear();
                                        PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).clear();
                                        PreferenceManager.saveOwnDetailArrayList(mOwnArrayList,"OwnContact",mContext);
                                        PreferenceManager.saveKinDetailsArrayListShow( AppController.kinArrayShow,mContext);
                                        PreferenceManager.saveKinDetailsArrayList( AppController.kinArrayPass,mContext);
                                        ArrayList<InsuranceDetailModel>mInsurance=PreferenceManager.getInsuranceDetailArrayList(mContext);
                                        mInsurance.clear();
                                        PreferenceManager.saveInsuranceDetailArrayList(mInsurance,mContext);
                                        ArrayList<PassportDetailModel>mPassport=PreferenceManager.getPassportDetailArrayList(mContext);
                                        mPassport.clear();
                                        PreferenceManager.savePassportDetailArrayList(mPassport,mContext);
                                        AppController.mStudentDataArrayList.clear();
                                        ArrayList<StudentModelNew>mStuudent=PreferenceManager.getInsuranceStudentList(mContext);
                                        mStuudent.clear();
                                        PreferenceManager.saveInsuranceStudentList(mStuudent,mContext);

                                    }
                                }
                                else
                                {
                                    AppController.kinArrayShow.clear();
                                    AppController.kinArrayPass.clear();
                                    ArrayList<OwnContactModel>mOwnArrayList=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);
                                    mOwnArrayList.clear();
                                    PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).clear();
                                    PreferenceManager.saveOwnDetailArrayList(mOwnArrayList,"OwnContact",mContext);
                                    PreferenceManager.saveKinDetailsArrayListShow( AppController.kinArrayShow,mContext);
                                    PreferenceManager.saveKinDetailsArrayList( AppController.kinArrayPass,mContext);
                                    ArrayList<InsuranceDetailModel>mInsurance=PreferenceManager.getInsuranceDetailArrayList(mContext);
                                    mInsurance.clear();
                                    PreferenceManager.saveInsuranceDetailArrayList(mInsurance,mContext);
                                    ArrayList<PassportDetailModel>mPassport=PreferenceManager.getPassportDetailArrayList(mContext);
                                    mPassport.clear();
                                    PreferenceManager.savePassportDetailArrayList(mPassport,mContext);
                                    AppController.mStudentDataArrayList.clear();
                                    ArrayList<StudentModelNew>mStuudent=PreferenceManager.getInsuranceStudentList(mContext);
                                    mStuudent.clear();
                                    PreferenceManager.saveInsuranceStudentList(mStuudent,mContext);
                                    PreferenceManager.setPreviousTriggerType(mContext,respObj.optString("trigger_type"));

                                }
                            }

                            if (PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1")) {
                                    System.out.println("SHOW: 1");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (PreferenceManager.getSuspendTrigger(mContext).equalsIgnoreCase("0")) {
                                                showDataCollectionUserDetail();
                                                getStudentsListAPI(URL_GET_STUDENT_LIST);
                                                getCountryList();
                                            }else {
                                                System.out.println("Dont show triggered window");
                                            }
                                        }
                                    },3000);
                                }else {
                                    System.out.println("SHOW: <1");
                                }
//                            }

                            // System.out.println("app version from api" + android_app_version);
                            PreferenceManager.setVersionFromApi(mContext, android_app_version);
                            JSONObject respObjAppFeature = secobj.optJSONObject("app_feature");
                            PreferenceManager.setTimeTable(mContext, respObjAppFeature.optString("timetable"));
                            PreferenceManager.setTimeTableGroup(mContext, respObjAppFeature.optString("timetable_group"));
                            PreferenceManager.setSafeGuarding(mContext, respObjAppFeature.optString("safeguarding"));
                            PreferenceManager.setAttendance(mContext, respObjAppFeature.optString("attendance"));
                            PreferenceManager.setAbsence(mContext, respObjAppFeature.optString("report_absense"));
                            // // System.out.println("dddddddd"+  PreferenceManager.setAbsence(mContext, respObjAppFeature.optString("report_absense"));


                            // System.out.println("calendarBadge" + PreferenceManager.getCalenderBadge(mContext));
                            JSONArray studentDetail = respObj.getJSONArray("studentdetails");
                            ArrayList<StudentDetailSettingModel> mDatamodel = new ArrayList<StudentDetailSettingModel>();

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
                                } else {
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

                            if (PreferenceManager.getMessageBadge(mContext).equalsIgnoreCase("0")) {
                                messageBtn.setText("");
                            } else {
                                messageBtn.setText(PreferenceManager.getMessageBadge(mContext));
                            }
                            if (PreferenceManager.getCalenderBadge(mContext).equalsIgnoreCase("0")) {
                                calendarBtn.setText("");

                            } else {
                                calendarBtn.setText(PreferenceManager.getCalenderBadge(mContext));
                            }
                            if (PreferenceManager.getsafenote(mContext).equalsIgnoreCase("0")) {
                                safeGuardingBadge.setVisibility(View.GONE);
                                img.setVisibility(View.GONE);
                            } else {
                                safeGuardingBadge.setVisibility(View.VISIBLE);
                                img.setVisibility(View.VISIBLE);
                                safeGuardingBadge.setText(PreferenceManager.getsafenote(mContext));
                            }
                            // System.out.println("badgehomecountMessage:" + (Integer.valueOf(PreferenceManager.getMessageBadge(mContext))));
                            // System.out.println("badgehomecountCalender:" + Integer.valueOf(PreferenceManager.getCalenderBadge(mContext)));
                            // System.out.println("badgehomecount:" + (Integer.valueOf(PreferenceManager.getMessageBadge(mContext)) + Integer.valueOf(PreferenceManager.getCalenderBadge(mContext))));

                            ShortcutBadger.applyCount(mContext, Integer.valueOf(PreferenceManager.getMessageBadge(mContext)) + Integer.valueOf(PreferenceManager.getCalenderBadge(mContext))); //for 1.1.4+

                            JSONObject socialmediaObj = secobj.getJSONObject("socialmedia");

                            PreferenceManager.setFbkey(mContext, socialmediaObj.optString("fbkey"));
                            PreferenceManager.setInstaKey(mContext, socialmediaObj.optString("inkey"));
                            PreferenceManager.setYouKey(mContext, socialmediaObj.optString("youtubekey"));

                            //}
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
                    // System.out.println("The Exception in edit profile is" + ex.toString());
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
    public void onResume() {
        super.onResume();


        if (AppUtils.isNetworkConnected(mContext)) {
         //   showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

        } /*else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }*/
        if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Home Screen Fragment. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");

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
            Log.e("Launch", e.getMessage());
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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String identifierString = intent.getAction();

            if (identifierString.equals("badgenotify")) {
                if (AppUtils.isNetworkConnected(mContext)) {
                    showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                }

            }
        }
    };

    private void showDataCollectionUserDetail()
    {

        mOwnContactArrayList=new ArrayList<>();
        mGlobalListDataArrayList=new ArrayList<>();
        ContactTypeArray = new ArrayList<>();
        mGlobalListSirnameArrayList=new ArrayList<>();
        ContactListArray = new ArrayList<>();
        InsuranceHealthListArray = new ArrayList<>();
        PassportDetailListArray = new ArrayList<>();
        KinArray = new ArrayList<>();
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_GET_DATA_COLLECTION_DETAILS_NEW);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                 System.out.println("The response DATA COLLECTION" + successResponse);
                try
                {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303"))
                        {

                             JSONObject dataObj=secobj.optJSONObject("data");
                             PreferenceManager.setDisplayMessage(mContext,dataObj.optString("display_message"));
                             JSONArray ownDetailArray=dataObj.getJSONArray("own_details");
                             if(ownDetailArray.length()>0)
                             {
                                 for (int i = 0; i < ownDetailArray.length(); i++)
                                 {
                                     JSONObject dataObject = ownDetailArray.getJSONObject(i);
                                     OwnContactModel mModel=new OwnContactModel();
                                     mModel.setId(dataObject.optString("id"));
                                     mModel.setUser_id(dataObject.optString("user_id"));
                                     mModel.setTitle(dataObject.optString("title"));
                                     mModel.setName(dataObject.optString("name"));
                                     mModel.setLast_name(dataObject.optString("last_name"));
                                     mModel.setRelationship(dataObject.optString("relationship"));
                                     mModel.setEmail(dataObject.optString("email"));
                                     mModel.setPhone(dataObject.optString("phone"));
                                     mModel.setCode(dataObject.optString("code"));
                                     mModel.setUser_mobile(dataObject.optString("user_mobile"));
                                     mModel.setStudent_id(dataObject.optString("student_id"));
                                     mModel.setAddress1(dataObject.optString("address1"));
                                     mModel.setAddress2(dataObject.optString("address2"));
                                     mModel.setAddress3(dataObject.optString("address3"));
                                     mModel.setTown(dataObject.optString("town"));
                                     mModel.setState(dataObject.optString("state"));
                                     mModel.setCountry(dataObject.optString("country"));
                                     mModel.setPincode(dataObject.optString("pincode"));
                                     mModel.setCorrespondencemailmerge(dataObject.optString("correspondencemailmerge"));
                                     mModel.setReportmailmerge(dataObject.optString("reportmailmerge"));
                                     mModel.setJustcontact(dataObject.optString("justcontact"));
                                     mModel.setStatus(dataObject.optString("status"));
                                     mModel.setCreated_at(dataObject.optString("created_at"));
                                     mModel.setUpdated_at(dataObject.optString("updated_at"));
                                     mModel.setUpdated(false);
                                     mModel.setConfirmed(false);
                                     System.out.println("Works DATA COLLECTIOM");
                                     mOwnContactArrayList.add(mModel);
                                 }




                                 //PreferenceManager.saveOwnDetailArrayList(mOwnContactArrayList,"OwnContact",mContext);
                                 System.out.println("Works DATA COLLECTIOM before if works"+mOwnContactArrayList.size());
                                 if(PreferenceManager.getOwnDetailArrayList("OwnContact",mContext)==null || PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).size()==0)
                                 {
                                     System.out.println("Works DATA COLLECTIOM else works");
                                     PreferenceManager.setIsAlreadyEnteredOwnContact(mContext,true);
                                     PreferenceManager.saveOwnDetailArrayList(mOwnContactArrayList,"OwnContact",mContext);
                                     System.out.println("OwnContact array"+PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).size());
                                 }
                                 else if(!PreferenceManager.getIsAlreadyEnteredOwnContact(mContext))
                                 {
                                     System.out.println("Works DATA COLLECTIOM else works");
                                     PreferenceManager.setIsAlreadyEnteredOwnContact(mContext,true);
                                     PreferenceManager.saveOwnDetailArrayList(mOwnContactArrayList,"OwnContact",mContext);
                                     System.out.println("OwnContact array"+PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).size());
                                 }
                                 else
                                 {
                                     System.out.println("Works DATA COLLECTIOM else works");
                                 }

                             }
                            JSONArray KIN_Array = dataObj.getJSONArray("kin_details");
                            if (KIN_Array.length()>0)
                            {
                                for (int k=0;k<KIN_Array.length();k++){
                                    JSONObject KinObj = KIN_Array.getJSONObject(k);
                                    System.out.println("My Data: "+KinObj.optString("name"));
                                    KinModel model = new KinModel();
                                    model.setId(KinObj.optString("id"));
                                    model.setUser_id(KinObj.optString("user_id"));
                                    model.setKin_id(KinObj.optString("kin_id"));
                                    model.setTitle(KinObj.optString("title"));;
                                    model.setName(KinObj.optString("name"));
                                    model.setLast_name(KinObj.optString("last_name"));
                                    model.setRelationship(KinObj.optString("relationship"));
                                    model.setEmail(KinObj.optString("email"));
                                    model.setPhone(KinObj.optString("phone"));
                                    model.setCode(KinObj.optString("code"));
                                    model.setCorrespondencemailmerge(KinObj.optString("correspondencemailmerge"));
                                    model.setReportmailmerge(KinObj.optString("reportmailmerge"));
                                    model.setJustcontact(KinObj.optString("justcontact"));
                                    System.out.println("Works DATA COLLECTIOM549894");
                                    model.setUser_mobile(KinObj.optString("user_mobile"));
                                    model.setStudent_id(KinObj.optString("student_id"));
                                    model.setCreated_at(KinObj.optString("created_at"));
                                    model.setUpdated_at(KinObj.optString("updated_at"));
                                    model.setNewData("NO");
                                    model.setNewData(false);
                                    model.setConfirmed(false);
                                    KinArray.add(model);
                                }

                                for (int j=0;j<KinArray.size();j++)
                                {
                                    if (KinArray.get(j).getRelationship().equalsIgnoreCase("Next of kin"))
                                    {
                                        isKinFound=true;
                                        System.out.println("Kin found works");
                                    }
                                    else
                                    {
                                        if(KinArray.get(j).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| KinArray.get(j).getRelationship().equalsIgnoreCase("Emergency Contact"))
                                        {
                                            isLocalFound=true;
                                            System.out.println("Kin found works local");
                                        }
                                    }
                                }

                                if (!isLocalFound)
                                {
                                    KinModel model = new KinModel();
                                    model.setId("Mobatia_LOCAL");
                                    model.setUser_id("");
                                    model.setKin_id("Mobatia_LOCAL");
                                    model.setTitle("");;
                                    model.setName("");
                                    model.setLast_name("");
                                    model.setRelationship("Emergency Contact");
                                    model.setEmail("");
                                    model.setPhone("");
                                    model.setCode("");
                                    System.out.println("Works DATA COLLECTIOM549894");
                                    model.setUser_mobile("");
                                    model.setStudent_id("");
                                    model.setCreated_at("");
                                    model.setUpdated_at("");
                                    model.setReportmailmerge("");
                                    model.setCorrespondencemailmerge("");
                                    model.setJustcontact("");
                                    model.setNewData("YES");
                                    model.setNewData(true);
                                    model.setConfirmed(false);
                                    KinArray.add(model);
                                }


                            }
                            else
                            {

                                if (!isLocalFound)
                                {
                                    KinModel model = new KinModel();
                                    model.setId("Mobatia_LOCAL");
                                    model.setUser_id("");
                                    model.setKin_id("Mobatia_LOCAL");
                                    model.setTitle("");;
                                    model.setName("");
                                    model.setLast_name("");
                                    model.setRelationship("Emergency Contact");
                                    model.setEmail("");
                                    model.setPhone("");
                                    model.setCode("");
                                    System.out.println("Works DATA COLLECTIOM549894");
                                    model.setUser_mobile("");
                                    model.setStudent_id("");
                                    model.setCreated_at("");
                                    model.setUpdated_at("");
                                    model.setReportmailmerge("");
                                    model.setJustcontact("");
                                    model.setCorrespondencemailmerge("");
                                    model.setNewData("YES");
                                    model.setNewData(true);
                                    model.setConfirmed(false);
                                    KinArray.add(model);
                                }
                            }
                            if (PreferenceManager.getKinDetailsArrayList(mContext)==null || !PreferenceManager.getIsAlreadyEnteredKin(mContext) ||PreferenceManager.getKinDetailsArrayList(mContext).size()==0)
                            {
                                PreferenceManager.setIsAlreadyEnteredKin(mContext,true);
                                PreferenceManager.saveKinDetailsArrayList(KinArray,mContext);
                                PreferenceManager.saveKinDetailsArrayListShow(KinArray,mContext);
                            }
                            JSONArray globalArray=dataObj.getJSONArray("GlobalList");
                            if(globalArray.length()>0)
                            {
                                for (int i=0;i<globalArray.length();i++)
                                {
                                    JSONObject globalObject = globalArray.getJSONObject(i);

                                    if (globalObject.optString("Type").equalsIgnoreCase("Title")){
                                        GlobalListDataModel mModel=new GlobalListDataModel();
                                        mModel.setType(globalObject.optString("Type"));
                                        JSONArray globalSirArray=globalObject.getJSONArray("GlobalList");
                                        if (globalSirArray.length()>0)
                                        {
                                            for (int j=0;j<globalSirArray.length();j++)
                                            {
                                                JSONObject globalTypeObject = globalSirArray.getJSONObject(j);
                                                GlobalListSirname model=new GlobalListSirname();
                                                model.setName(globalTypeObject.optString("name"));
                                                mGlobalListSirnameArrayList.add(model);
                                            }
                                        }
                                        mModel.setmGlobalSirnameArray(mGlobalListSirnameArrayList);
                                        mGlobalListDataArrayList.add(mModel);
                                    }

                                    if (globalObject.optString("Type").equalsIgnoreCase("ContactType")){
                                        System.out.println("DAta: inside ContactType");
                                        ContactTypeModel model = new ContactTypeModel();
                                        model.setType(globalObject.optString("Type"));
                                        JSONArray ContactArray = globalObject.getJSONArray("GlobalList");
                                        if (ContactArray.length()>0){
                                            for (int j =0; j< ContactArray.length();j++){
                                                JSONObject job = ContactArray.getJSONObject(j);
                                                GlobalListSirname Gmodel = new GlobalListSirname();
                                                Gmodel.setName(job.optString("name"));
                                                ContactListArray.add(Gmodel);
                                            }
                                        }
                                        model.setmGlobalSirnameArray(ContactListArray);
                                        ContactTypeArray.add(model);
                                    }

                                }
                                PreferenceManager.saveGlobalListArrayList(mGlobalListDataArrayList,mContext);
                                PreferenceManager.saveContactTypeArrayList(ContactTypeArray,mContext);
                            }
                            JSONArray insuranceArray=dataObj.getJSONArray("health_and_insurence");
                             if(insuranceArray.length()>0)
                             {
                                 for (int in=0;in<insuranceArray.length();in++)
                                 {
                                     JSONObject insuranceObject = insuranceArray.getJSONObject(in);
                                     InsuranceDetailModel iModel=new InsuranceDetailModel();
                                     iModel.setId(insuranceObject.optString("id"));
                                     iModel.setStudent_id(insuranceObject.optString("student_id"));
                                     System.out.println("student id insurance"+insuranceObject.optString("student_id"));
                                     iModel.setStudent_name(insuranceObject.optString("student_name"));
                                     iModel.setHealth_detail(insuranceObject.optString("health_detail"));
                                     iModel.setNo_personal_accident_insurance(insuranceObject.optString("no_personal_accident_insurance"));//1 if student have both insurance // 2 if student have only medical insurance // 3 if student have only personal insurance
                                     iModel.setMedical_insurence_policy_no(insuranceObject.optString("medical_insurence_policy_no"));
                                     System.out.println("insurance data adding"+insuranceObject.optString("medical_insurence_policy_no"));
                                     iModel.setMedical_insurence_member_number(insuranceObject.optString("medical_insurence_member_number"));
                                     iModel.setMedical_insurence_provider(insuranceObject.optString("medical_insurence_provider"));
                                     iModel.setMedical_insurence_expiry_date(insuranceObject.optString("medical_insurence_expiry_date"));
                                     iModel.setPersonal_accident_insurence_policy_no(insuranceObject.optString("personal_accident_insurence_policy_no"));
                                     iModel.setPersonal_accident_insurence_provider(insuranceObject.optString("personal_accident_insurence_provider"));
                                     iModel.setPersonal_acident_insurence_expiry_date(insuranceObject.optString("personal_acident_insurence_expiry_date"));
                                     iModel.setPreferred_hospital(insuranceObject.optString("preferred_hospital"));
                                     iModel.setStatus("5");
                                     iModel.setRequest(insuranceObject.optString("request"));
                                     iModel.setCreated_at(insuranceObject.optString("created_at"));
                                     iModel.setUpdated_at(insuranceObject.optString("updated_at"));
                                     iModel.setDeclaration("0");
                                     InsuranceHealthListArray.add(iModel);

                                 }


                                 if (PreferenceManager.getInsuranceDetailArrayList(mContext)==null || !PreferenceManager.getIsAlreadyEnteredInsurance(mContext) || PreferenceManager.getInsuranceDetailArrayList(mContext).size()==0)
                                 {
                                     PreferenceManager.setIsAlreadyEnteredInsurance(mContext,true);
                                     PreferenceManager.saveInsuranceDetailArrayList(InsuranceHealthListArray,mContext);
                                 }

                                 JSONArray passportArray=dataObj.getJSONArray("passport_details");
                                 if (passportArray.length()>0)
                                 {
                                     for (int p=0;p<passportArray.length();p++) {

                                         JSONObject passportObject = passportArray.getJSONObject(p);
                                         PassportDetailModel pModel=new PassportDetailModel();
                                         pModel.setId(passportObject.optString("id"));
                                         pModel.setStudent_id(passportObject.optString("student_id"));
                                         pModel.setStudent_name(passportObject.optString("student_name"));
                                         pModel.setPassport_number(passportObject.optString("passport_number"));
                                         pModel.setOriginal_passport_number(passportObject.optString("passport_number"));
                                         pModel.setNationality(passportObject.optString("nationality"));
                                         pModel.setOriginal_nationality(passportObject.optString("nationality"));
                                         pModel.setPassport_image(passportObject.optString("passport_image"));
                                         pModel.setOriginal_passport_image(passportObject.optString("passport_image"));
                                         pModel.setPassport_expired(passportObject.optString("passport_expired"));
                                         pModel.setOriginal_expiry_date(passportObject.optString("expiry_date"));
                                         pModel.setDate_of_issue(passportObject.optString("date_of_issue"));
                                         pModel.setExpiry_date(passportObject.optString("expiry_date"));
                                         pModel.setVisa(passportObject.optString("visa"));
                                         System.out.println("Visa value"+passportObject.optString("visa"));
                                         pModel.setVisa_permit_no(passportObject.optString("visa_permit_no"));
                                         pModel.setVisa_expired(passportObject.optString("visa_expired"));
                                         pModel.setNot_have_a_valid_passport(passportObject.optString("not_have_a_valid_passport"));
                                         pModel.setVisa_image_name("");
                                         pModel.setVisa_image_path("");
                                         pModel.setPassport_image_path("");
                                         pModel.setPassport_image_name("");
                                         pModel.setVisa_permit_expiry_date(passportObject.optString("visa_permit_expiry_date"));
                                         pModel.setVisa_image(passportObject.optString("visa_image"));
                                         pModel.setPhoto_no_consent(passportObject.optString("photo_no_consent"));
                                         if (passportObject.optString("id").equalsIgnoreCase(""))
                                         {
                                             pModel.setStatus("0");
                                             pModel.setRequest("1");
                                         }
                                         else
                                         {
                                             pModel.setStatus("5");
                                             pModel.setRequest("0");
                                         }

                                         pModel.setCreated_at(passportObject.optString("created_at"));
                                         pModel.setUpdated_at(passportObject.optString("updated_at"));
                                         if(passportObject.optString("expiry_date").equalsIgnoreCase(""))
                                         {
                                             pModel.setPassportDateChanged(true);
                                         }
                                         else
                                         {
                                             pModel.setPassportDateChanged(false);
                                         }

                                         pModel.setVisaDateChanged(false);
                                         PassportDetailListArray.add(pModel);
                                     }

                                     if (PreferenceManager.getPassportDetailArrayList(mContext)==null || !PreferenceManager.getIsAlreadyEnteredPassport(mContext) || PreferenceManager.getPassportDetailArrayList(mContext).size()==0 )
                                     {
                                         PreferenceManager.setIsAlreadyEnteredPassport(mContext,true);
                                         PreferenceManager.savePassportDetailArrayList(PassportDetailListArray,mContext);
                                     }


                                 }

                             }
                             if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
                             {
                                 System.out.println("QWE: inside VISIBLE");
                                 if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                                 {
                                     System.out.println("QWE: inside Applicant");
                                     if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                                     {
                                         System.out.println("QWE: inside Mail");
                                         if (PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                                         {
                                             System.out.println("QWE: inside Collection");
                                             Intent i = new Intent(getActivity(), DataCollectionHome.class);
                                             startActivity(i);
                                         }
                                     }
                                 }
                                 else
                                 {
                                     System.out.println("QWE: outside Applicant");
                                 }
                             }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken()
                            {

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
    private void getStudentsListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list" + successResponse);
                try {
                    studentsModelArrayList = new ArrayList<>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    if (dataObject.optString("alumi").equalsIgnoreCase("0"))
                                    {
//                                        AppController.mStudentDataArrayList.add(addStudentDetails(dataObject));
                                        studentsModelArrayList.add(addStudentDetails(dataObject));

                                    }

                                }

                                if (PreferenceManager.getInsuranceStudentList(mContext)== null|| !PreferenceManager.getIsAlreadyEnteredStudentList(mContext) || PreferenceManager.getInsuranceStudentList(mContext).size()==0 )
                                {
                                    System.out.println("student list add works");
                                    PreferenceManager.setIsAlreadyEnteredStudentList(mContext,true);
                                    AppController.mStudentDataArrayList=studentsModelArrayList;
                                    PreferenceManager.saveInsuranceStudentList(studentsModelArrayList,mContext);
                                }
                                else
                                {
                                    AppController.mStudentDataArrayList=PreferenceManager.getInsuranceStudentList(mContext);

                                }



                            } else {
//                                mRecycleView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse)
            {


            }
        });


    }

    private StudentModelNew addStudentDetails(JSONObject dataObject)
    {
        StudentModelNew studentModel = new StudentModelNew();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));
        studentModel.setConfirmed(false);
        studentModel.setProgressReport(dataObject.optString("progressreport"));
        studentModel.setAlumini(dataObject.optString("alumi"));


        return studentModel;
    }


    private void getCountryList()
    {
        mNationlityListArrrayList=new ArrayList<>();

        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_GET_DATACOLLECTION_COUNTRIES);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("DATA COLLECTION COUNTRIES" + successResponse);
                try
                {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303"))
                        {
                            JSONArray data = secobj.getJSONArray("contries");
                            if (data.length() > 0)
                            {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    NationalityModel model=new NationalityModel();
                                    model.setId(dataObject.optString("id"));
                                    model.setName(dataObject.optString("name"));
                                    mNationlityListArrrayList.add(model);

                                }

                                AppController.mNationalityArrayList=mNationlityListArrrayList;
                            } else {

                            }


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken()
                            {

                            }
                        });
                        showSettingUserDetail(URL_GET_DATACOLLECTION_COUNTRIES);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }
                        });
                        showSettingUserDetail(URL_GET_DATACOLLECTION_COUNTRIES);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        showSettingUserDetail(URL_GET_DATACOLLECTION_COUNTRIES);
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
}
