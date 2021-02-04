package com.mobatia.bskl.appcontroller;

import android.content.res.TypedArray;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.mobatia.bskl.activity.OTP_Receiver.AppSignatureHelper;
import com.mobatia.bskl.activity.data_collection.model.NationalityModel;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportImageModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.google_analytics.AnalyticsTrackers;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.manager.PreferenceManager;

import java.util.ArrayList;

public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    public static TypedArray mListImgArrays;
    public static String mTitles;
    public static DrawerLayout mDrawerLayouts;
    public static LinearLayout mLinearLayouts;
    public static ListView mListViews;
    public static String[] listitemArrays;
    public static boolean isVisibleUnreadBox=false;
    public static boolean isVisibleReadBox=false;
    public static Boolean isfromUnread=false;
    public static Boolean isfromUnreadSingle=false;
    public static Boolean isfromRead=false;
    public static Boolean isfromMarkRead=false;
    public static String pushId="";
    public static String callTypeStatus="1";
    public static ArrayList<String> dateArrayListYear;
    public static ArrayList<String> holidayArrayListYear;
    public static boolean isSelectedUnRead=false;
    public static boolean isFirst=true;
    public static boolean isSelectedRead=false;
    public static   ArrayList<PushNotificationModel>mMessageUnreadList=new ArrayList<>();
    public static ArrayList<PushNotificationModel>mMessageReadList= new ArrayList<>();
   public static int click_count_read = 0;
    public static int click_count = 0;

    public static ArrayList<InsuranceDetailModel>mInsuranceDetailArrayList=new ArrayList<>();
    public static boolean isInsuranceEdited=false;
    public static boolean isPassportEdited=false;
    public static ArrayList<PassportDetailModel>mPassportDetailArrayList=new ArrayList<>();
    public static int confirmingPosition=-1;
    //Data Collection
    public static ArrayList<PassportImageModel> PassportPathArray = new ArrayList<>();
    public static boolean isSubmitClicked=false;
    public static String emptyStudentId="";
    public static int k = 0;
    public static ArrayList<StudentModelNew>mStudentDataArrayList=new ArrayList<>();
    public static ArrayList<NationalityModel>mNationalityArrayList=new ArrayList<>();
    public static   ArrayList<PushNotificationModel>mMessageUnreadListFav=new ArrayList<>();
    public static ArrayList<PushNotificationModel>mMessageReadListFav= new ArrayList<>();
    public static ArrayList<KinModel> kinArrayShow = new ArrayList<>();
    public static ArrayList<KinModel> kinArrayPass= new ArrayList<>();
    public static boolean isKinEdited=false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(this);
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        AppSignatureHelper appSignature = new AppSignatureHelper(this);
        for (String signature : appSignature.getAppSignatures()) {
        Log.e("MyRespo", "onCreate: " + signature );
            PreferenceManager.setOTPSignature(this,signature);
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
}


    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }


}

