package com.mobatia.bskl.fragment.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.notification.MessageDetail;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.ClickListener;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.fragment.notification.adapter.ReadMessageAdapter;
import com.mobatia.bskl.fragment.notification.adapter.UnReadMessageAdapter;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.OnBottomReachedListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by krishnaraj on 12/07/18.
 */

public class NotificationFragmentPagination extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants, View.OnClickListener, AdapterView.OnItemClickListener {
    private View mRootView;
    private static Context mContext;
    private static RecyclerView readList;
    private static RecyclerView unReadList;
    CardView cardView;
    Intent mIntent;
    private String mTitle;
    private String mTabId;
    static TextView markRead,unreadTxt,readTxt;
    static RotateAnimation anim;
    static TextView markUnRead,msgUnRead,msgReadSelect;
    TextView msgRead;
    static TextView msgUnread;
    static RelativeLayout noMsgAlertRelative;
    static LinearLayout maniRelative;
    static TextView noMsgText;
    static ImageView messageRead;
    static ImageView messageUnRead;
    static LinearLayout msgReadLinear;
    static LinearLayout msgUnreadLinear;
    static SimpleDateFormat sdfcalender;
     static RelativeLayout mProgressRelLayout;
    String myFormatCalender = "yyyy-MM-dd";
    String tab_type;
    String push_id = "";
    static UnReadMessageAdapter unReadMessageAdapter;
    static ReadMessageAdapter readMessageAdapter;
    static int dataLength=0;
    static boolean isAvailable=false;
    static boolean isReadAvailable=false;
    static boolean isFromReadBottom=false;
    static boolean isFromUnReadBottom=false;
    static String typeValue="1";
   static int limitValue=20;
   static int pageReadValue=0;
   static int pageUnReadValue=0;
    //   static int click_count = 0;
    //static int click_count_read = 0;
    NestedScrollView nestedScroll;
    static boolean isProgressVisible=false;

    public NotificationFragmentPagination() {

    }

    public NotificationFragmentPagination(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    @Override
    public void onClick(View v) {
        if (v == markRead) {
            markRead.setVisibility(View.GONE);
            markUnRead.setVisibility(View.GONE);
            messageRead.setVisibility(View.GONE);
            msgReadSelect.setVisibility(View.GONE);
            messageUnRead.setVisibility(View.GONE);
            msgUnread.setVisibility(View.GONE);
            AppController.isVisibleUnreadBox = false;
            AppController.isVisibleReadBox = false;
            if (AppUtils.isNetworkConnected(mContext)) {
                //clearBadge();
                String readList = "";
                int k = 0;
                ArrayList<PushNotificationModel>mTempArray=new ArrayList<>();
                mTempArray.addAll(AppController.mMessageUnreadList);

                for (int i = 0; i < mTempArray.size(); i++) {
                    boolean isFoundId=false;
                    if (mTempArray.get(i).getChecked()) {
                        if (k == 0) {
                            readList = mTempArray.get(i).getPushid();

                        } else {
                            readList = readList + "," + mTempArray.get(i).getPushid();

                        }
                        for (int j = 0; j<AppController.mMessageUnreadList.size(); j++)
                        {
                            String pushReadId=AppController.mMessageUnreadList.get(j).getPushid();
                            if (pushReadId.equalsIgnoreCase(mTempArray.get(i).getPushid()))
                            {
                              //  isFoundId=true;
                                AppController.mMessageUnreadList.remove(j);

                            }
                        }
                        k = k + 1;
                       // AppController.mMessageUnreadList.remove(i);
                    }
                }

               //  System.out.println("Read List" + readList);
                callstatusapi("1", readList);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
            }

        } else if (v == markUnRead) {
            markRead.setVisibility(View.GONE);
            markUnRead.setVisibility(View.GONE);
            messageRead.setVisibility(View.GONE);
            msgReadSelect.setVisibility(View.GONE);
            messageUnRead.setVisibility(View.GONE);
            msgUnread.setVisibility(View.GONE);
            AppController.isVisibleUnreadBox = false;
            AppController.isVisibleReadBox = false;
            if (AppUtils.isNetworkConnected(mContext)) {

                String readList = "";
                int k = 0;
                ArrayList<PushNotificationModel>mTempReadArray=new ArrayList<>();
                mTempReadArray.addAll(AppController.mMessageReadList);
                for (int i = 0; i < mTempReadArray.size(); i++) {
                    if (mTempReadArray.get(i).isMarked()) {

                        if (k == 0) {
                            readList = mTempReadArray.get(i).getPushid();

                        } else {
                            readList = readList + "," + mTempReadArray.get(i).getPushid();

                        }
                        for (int j = 0; j<AppController.mMessageReadList.size(); j++)
                        {
                            String pushReadId=AppController.mMessageReadList.get(j).getPushid();
                            if (pushReadId.equalsIgnoreCase(mTempReadArray.get(i).getPushid()))
                            {
                                //  isFoundId=true;
                                AppController.mMessageReadList.remove(j);

                            }
                        }
                        k = k + 1;

                    }
                   // AppController.mMessageReadList.remove(i);
                }
           //      System.out.println("Unread List" + readList);
                callstatusapi("0", readList);

                // System.out.println("click next");

            } else if (v == maniRelative) {

            } else if (v == noMsgAlertRelative) {

            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
            }

        } else if (v == messageRead) {
            int mCount = AppController.click_count_read;
            if (AppController.click_count_read % 2 == 0)
            {

                AppController.isVisibleReadBox = true;
                markUnRead.setVisibility(View.GONE);
                markRead.setVisibility(View.GONE);

                AppController.isSelectedRead = false;
                for (int i = 0; i < AppController.mMessageReadList.size(); i++) {

                    AppController.mMessageReadList.get(i).setMarked(true);


                }

                boolean selected = false;
                for (int i = 0; i < AppController.mMessageReadList.size(); i++) {
                    if (AppController.mMessageReadList.get(i).isMarked()) {
                        selected = true;

                    } else {
                        selected = false;

                        break;

                    }
                }
                if (selected) {
                    messageRead.setBackgroundResource(R.drawable.check_box_header_tick);
                    markUnRead.setVisibility(View.VISIBLE);
                    markRead.setVisibility(View.GONE);
                } else {
                    messageRead.setBackgroundResource(R.drawable.check_box_header);
                    markRead.setVisibility(View.GONE);
                    markUnRead.setVisibility(View.VISIBLE);
                }
                readMessageAdapter.notifyDataSetChanged();
                //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());
            } else {


                AppController.isVisibleReadBox = false;
                for (int i = 0; i < AppController.mMessageReadList.size(); i++) {
                    if (AppController.mMessageReadList.get(i).getChecked()) {
                        AppController.mMessageReadList.get(i).setChecked(false);

                    }
                }
                markUnRead.setVisibility(View.GONE);

                messageRead.setVisibility(View.GONE);
                msgReadSelect.setVisibility(View.GONE);


                readMessageAdapter.notifyDataSetChanged();
                //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

            }

            AppController.click_count_read++;


        } else if (v == messageUnRead) {


            if (AppController.click_count % 2 == 0) {

                AppController.isVisibleUnreadBox = true;
                for (int i = 0; i < AppController.mMessageUnreadList.size(); i++) {

                    AppController.mMessageUnreadList.get(i).setChecked(true);

                }
                boolean selected = false;
                for (int i = 0; i < AppController.mMessageUnreadList.size(); i++) {
                    if (AppController.mMessageUnreadList.get(i).getChecked()) {
                        selected = true;

                    } else {
                        selected = false;
                        break;

                    }
                }
                if (selected) {
                    messageUnRead.setBackgroundResource(R.drawable.check_box_header_tick);
                    markUnRead.setVisibility(View.GONE);
                    markRead.setVisibility(View.VISIBLE);

                } else {
                    messageUnRead.setBackgroundResource(R.drawable.check_box_header);
                    markUnRead.setVisibility(View.GONE);
                    markRead.setVisibility(View.GONE);

                }
                unReadMessageAdapter.notifyDataSetChanged();
                //unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());


            } else {


                AppController.isVisibleUnreadBox = false;
                for (int i = 0; i < AppController.mMessageUnreadList.size(); i++) {
                    if (AppController.mMessageUnreadList.get(i).getChecked()) {
                        AppController.mMessageUnreadList.get(i).setChecked(false);

                    }
                }

                messageUnRead.setVisibility(View.GONE);
                msgUnread.setVisibility(View.GONE);
                markRead.setVisibility(View.GONE);
                messageUnRead.setBackgroundResource(R.drawable.check_box_header);

                unReadMessageAdapter.notifyDataSetChanged();
                //unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());


            }
            AppController.click_count++;
        }
        else if (v==unreadTxt)
        {
            AppController.callTypeStatus="1";
            markRead.setVisibility(View.GONE);
            messageUnRead.setVisibility(View.GONE);
            msgReadSelect.setVisibility(View.GONE);
            msgUnread.setVisibility(View.GONE);
            messageRead.setVisibility(View.GONE);
            markUnRead.setVisibility(View.GONE);
           // messageUnRead.setBackgroundResource(R.drawable.check_box_header);
            AppController.isVisibleUnreadBox = false;
            AppController.isVisibleReadBox = false;
           // System.out.println(" size of array"+AppController.mMessageUnreadList.size());
             if (isProgressVisible)
             {
                 mProgressRelLayout.clearAnimation();
                 mProgressRelLayout.setVisibility(View.GONE);
                 isProgressVisible=false;
             }
            typeValue="1";
            limitValue=20;
            pageUnReadValue=0;
            isAvailable=false;
            if(AppController.mMessageUnreadList.size()>0)
            {
                unreadTxt.setBackgroundResource(R.drawable.notification_read);

                readTxt.setBackgroundResource(R.drawable.notification_unread);
                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                readTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                unReadMessageAdapter = new UnReadMessageAdapter(mContext, AppController.mMessageUnreadList, messageUnRead, markRead, markUnRead,msgUnread, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {


                    }

                    @Override
                    public void onLongClicked(int position) {
                        markRead.setVisibility(View.GONE);
                        markUnRead.setVisibility(View.GONE);
                        messageUnRead.setBackgroundResource(R.drawable.check_box_header);
                        AppController.isVisibleUnreadBox = true;
                        AppController.isVisibleReadBox = false;
                        AppController.click_count = 0;

                        for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                        {
                            AppController.mMessageReadList.get(i).setMarked(false);
                        }
                        for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                        {
                            AppController.mMessageUnreadList.get(i).setChecked(false);
                        }
                        if (AppController.mMessageReadList.size() > 0) {
                            readMessageAdapter.notifyDataSetChanged();
                            // readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                        }


                        unReadMessageAdapter.notifyDataSetChanged();
                        //unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());


                        messageUnRead.setVisibility(View.VISIBLE);
                        msgUnread.setVisibility(View.VISIBLE);
                        messageRead.setVisibility(View.GONE);
                        msgReadSelect.setVisibility(View.GONE);

                    }
                });

                msgUnreadLinear.setVisibility(View.VISIBLE);
                msgReadLinear.setVisibility(View.GONE);
                unReadMessageAdapter.notifyDataSetChanged();
                if (isFromUnReadBottom)
                {
                    unReadList.scrollToPosition(pageUnReadValue-2);
                }
                // unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());
                unReadList.setAdapter(unReadMessageAdapter);
                unReadMessageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                    @Override
                    public void onBottomReached(int position) {
                        if (position==AppController.mMessageUnreadList.size()-1)
                        {
                            if (!isAvailable)
                            {
                                isFromUnReadBottom=true;
                                if (AppController.isVisibleUnreadBox)
                                {
                                    markRead.setVisibility(View.GONE);
                                    markUnRead.setVisibility(View.GONE);
                                    messageUnRead.setBackgroundResource(R.drawable.check_box_header);
                                    AppController.isVisibleUnreadBox = true;
                                    AppController.isVisibleReadBox = false;
                                    AppController.click_count = 0;

                                    for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                    {
                                        AppController.mMessageReadList.get(i).setMarked(false);
                                    }
                                    for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                    {
                                        AppController.mMessageUnreadList.get(i).setChecked(false);
                                    }
                                }

                                typeValue="1";
                                limitValue=20;
                                pageUnReadValue=pageUnReadValue+limitValue;
                                callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageUnReadValue));
                                // Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();
                            }



                        }

                        //   System.out.println("position bottom"+position);




                    }
                });
                callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageUnReadValue));
            }
            else
            {
                callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageUnReadValue));
            }

        }
        else if (v==readTxt)
        {
            AppController.callTypeStatus="2";
            markRead.setVisibility(View.GONE);
            msgReadSelect.setVisibility(View.GONE);
            messageUnRead.setVisibility(View.GONE);
            msgUnread.setVisibility(View.GONE);
            messageRead.setVisibility(View.GONE);
            markUnRead.setVisibility(View.GONE);
            if (isProgressVisible)
            {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                isProgressVisible=false;

            }
            // messageUnRead.setBackgroundResource(R.drawable.check_box_header);
            AppController.isVisibleUnreadBox = false;
            AppController.isVisibleReadBox = false;
          //  System.out.println(" size of array"+AppController.mMessageReadList.size());

            typeValue="2";
            limitValue=20;
            pageReadValue=0;
            isReadAvailable=false;
            if (AppController.mMessageReadList.size()>0)
            {
                readTxt.setBackgroundResource(R.drawable.notification_read);
                unreadTxt.setBackgroundResource(R.drawable.notification_unread);
                readTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                //set adapter
                readMessageAdapter = new ReadMessageAdapter(mContext, AppController.mMessageReadList, messageRead, markUnRead, markRead,msgReadSelect, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {
                        markRead.setVisibility(View.GONE);
                        markUnRead.setVisibility(View.GONE);
                        messageRead.setVisibility(View.GONE);
                        msgReadSelect.setVisibility(View.GONE);
                        messageUnRead.setVisibility(View.GONE);
                        msgUnread.setVisibility(View.GONE);
                        AppController.isVisibleUnreadBox = false;
                        AppController.isVisibleReadBox = false;
                        Intent intent = new Intent(mContext, MessageDetail.class);
                        intent.putExtra("title", AppController.mMessageReadList.get(position).getTitle());
                        intent.putExtra("message", AppController.mMessageReadList.get(position).getMessage());
                        intent.putExtra("date", AppController.mMessageReadList.get(position).getDay());
                        ///intent.putExtra("position",position);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onLongClicked(int position) {
                        markRead.setVisibility(View.GONE);
                        markUnRead.setVisibility(View.GONE);
                        AppController.click_count_read = 0;
                        // System.out.println("checkbox read visible:::::");
                        messageRead.setBackgroundResource(R.drawable.check_box_header);
                        AppController.isVisibleReadBox = true;
                        AppController.isVisibleUnreadBox = false;
                        for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                        {
                            AppController.mMessageReadList.get(i).setMarked(false);
                        }
                        for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                        {
                            AppController.mMessageUnreadList.get(i).setChecked(false);
                        }

                        if (AppController.mMessageUnreadList.size() > 0) {
                            unReadMessageAdapter.notifyDataSetChanged();
                            // unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());

                        }

                        readMessageAdapter.notifyDataSetChanged();
                        //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                        messageRead.setVisibility(View.VISIBLE);
                        msgReadSelect.setVisibility(View.VISIBLE);
                        messageUnRead.setVisibility(View.GONE);
                        msgUnread.setVisibility(View.GONE);

                        noMsgAlertRelative.setVisibility(View.GONE);
                        noMsgText.setVisibility(View.GONE);
                    }
                });
                msgUnreadLinear.setVisibility(View.GONE);
                msgReadLinear.setVisibility(View.VISIBLE);
                if (isFromReadBottom)
                {
                    readList.scrollToPosition(pageReadValue-1);
                }
                readMessageAdapter.notifyDataSetChanged();
                //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                readList.setAdapter(readMessageAdapter);
                readMessageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                    @Override
                    public void onBottomReached(int position) {
                        if (position==AppController.mMessageReadList.size()-2)
                        {
                            if (!isReadAvailable)
                            {
                                isFromReadBottom=true;
                                if (AppController.isVisibleReadBox)
                                {
                                    markRead.setVisibility(View.GONE);
                                    markUnRead.setVisibility(View.GONE);
                                    AppController.click_count_read = 0;
                                    // System.out.println("checkbox read visible:::::");
                                    messageRead.setBackgroundResource(R.drawable.check_box_header);
                                    AppController.isVisibleReadBox = true;
                                    AppController.isVisibleUnreadBox = false;
                                    for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                    {
                                        AppController.mMessageReadList.get(i).setMarked(false);
                                    }
                                    for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                    {
                                        AppController.mMessageUnreadList.get(i).setChecked(false);
                                    }
                                }

                                typeValue="2";
                                limitValue=20;
                                pageReadValue=pageReadValue+limitValue;
                                callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageReadValue));
                                //  Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();

                        }

                        //    System.out.println("position bottom"+position);




                    }
                });
                callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageReadValue));
            }
            else
            {
                callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageReadValue));
            }

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.message_pagination_layout, container,
                false);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        ImageView imageButton2 = actionBar.getCustomView().findViewById(R.id.action_bar_forward);
        imageButton2.setImageResource(R.drawable.tutorial_icon);
         AppController.mMessageReadList.clear();
         AppController.mMessageUnreadList.clear();
        headerTitle.setText("Messages");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        initialiseUI();
        AppController.isVisibleUnreadBox = false;
        AppController.isVisibleReadBox = false;
        myFormatCalender = "yyyy-MM-dd HH:mm:ss";
        sdfcalender = new SimpleDateFormat(myFormatCalender);
        AppController.isSelectedUnRead = false;
        AppController.isSelectedRead = false;
        dataLength=0;
        typeValue="1";
        limitValue=20;
        isAvailable=false;
        isReadAvailable=false;
        pageReadValue=0;
        pageUnReadValue=0;
        isFromReadBottom=false;
        isFromUnReadBottom=false;
        isProgressVisible=false;
        AppController.callTypeStatus="1";
        if (AppUtils.isNetworkConnected(mContext)) {
            callPushNotification(typeValue,String .valueOf(limitValue),String.valueOf(pageUnReadValue));
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
        if (PreferenceManager.getIsFirstTimeInNotification(mContext)) {
            PreferenceManager.setIsFirstTimeInNotification(mContext, false);
            Intent mintent = new Intent(mContext, NotificationInfoActivity.class);
            mintent.putExtra("type", 1);
            mContext.startActivity(mintent);
        }

        return mRootView;
    }


    public static void callPushNotification(final String type, final String limit, final String page) {
        try {

            AppController.mMessageReadListFav = new ArrayList<>();
            AppController.mMessageUnreadListFav = new ArrayList<>();
            AppController.mMessageReadListFav = (ArrayList<PushNotificationModel>) AppController.mMessageReadList.clone();
            AppController.mMessageUnreadListFav = (ArrayList<PushNotificationModel>) AppController.mMessageUnreadList.clone();
           // AppController.mMessageReadList.clear();
           // AppController.mMessageUnreadList.clear();
          //  AppController.mDataArray.clear();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_LIST_NEW);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_DEVICE_iD, JTAG_DEVICE_tYPE, JTAG_USERS_ID,"type","limit","page"};
            String[] value = {PreferenceManager.getAccessToken(mContext), FirebaseInstanceId.getInstance().getToken(), "2", PreferenceManager.getUserId(mContext),type,limit,page};
               mProgressRelLayout.setVisibility(View.VISIBLE);
                 anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
                   anim.setInterpolator(mContext, android.R.interpolator.linear);
                    anim.setRepeatCount(Animation.INFINITE);
                     anim.setDuration(1000);
                      mProgressRelLayout.setAnimation(anim);
                   mProgressRelLayout.startAnimation(anim);
                   isProgressVisible=true;
            manager.getResponsePOST(mContext, 16, name, value, new VolleyWrapper.ResponseListener() {

              @Override
                public void responseSuccess(String successResponse) {
                  //   System.out.println("NofifyRes:" + successResponse);
                  if (isProgressVisible)
                  {
                      mProgressRelLayout.clearAnimation();
                      mProgressRelLayout.setVisibility(View.GONE);
                       isProgressVisible=false;
                  }

                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.getString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.getString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303"))
                                {
                                    final JSONArray dataArray = secobj.getJSONArray("data");
                                    if (type.equalsIgnoreCase("1"))
                                    {
                                        if (dataArray.length()<20)
                                        {
                                            isAvailable=true;
                                        }
                                    }
                                    else
                                    {
                                        if (dataArray.length()<20)
                                        {
                                            isReadAvailable=true;
                                        }
                                    }

                                  //  System.out.println("dtaa"+dataArray.length());

                                    if(dataArray.length()>0)
                                    {
                                      //  System.out.println("working");
                                        dataLength=dataArray.length();
                                     //   System.out.println("type value"+type);
                                      //  System.out.println("data length"+dataLength);
                                        for(int i=0;i<dataArray.length();i++)
                                        {
                                            if (type.equalsIgnoreCase("1"))
                                            {
                                                JSONObject unreadObject = dataArray.getJSONObject(i);
                                                if (AppController.mMessageUnreadList.size()==0)
                                                {
                                                    AppController.mMessageUnreadList.add(addUnreadMessage(unreadObject, i, dataArray.length()));
                                                }
                                                else
                                                {
                                                    String pushId=unreadObject.getString("pushid");
                                                    boolean isFound=false;
                                                    for (int j = 0; j<AppController.mMessageUnreadList.size(); j++)
                                                    {
                                                        if (pushId.equalsIgnoreCase(AppController.mMessageUnreadList.get(j).getPushid()))
                                                        {
                                                            isFound=true;
                                                        }

                                                    }
                                                    if (!isFound)
                                                    {
                                                        AppController.mMessageUnreadList.add(addUnreadMessage(unreadObject, i, dataArray.length()));
                                                        Collections.sort(AppController.mMessageUnreadList, new Comparator<PushNotificationModel>() {
                                                            public int compare(PushNotificationModel o1, PushNotificationModel o2) {
                                                                return o1.getDateTime().compareTo(o2.getDateTime());
                                                            }
                                                        });
                                                    }
                                                }

                                            }
                                            else
                                            {
                                                JSONObject readObject = dataArray.getJSONObject(i);
                                                if (AppController.mMessageReadList.size()==0)
                                                {
                                                    AppController.mMessageReadList.add(addReadMessage(readObject, i, dataArray.length()));
                                                   // Collections.sort(AppController.mMessageReadList);
                                                }
                                                else
                                                {
                                                    String pushID=readObject.getString("pushid");;
                                                    boolean isFoundPush=false;
                                                    for (int j = 0; j<AppController.mMessageReadList.size(); j++)
                                                    {
                                                        if (pushID.equalsIgnoreCase(AppController.mMessageReadList.get(j).getPushid()))
                                                        {
                                                            isFoundPush=true;
                                                        }

                                                    }
                                                    if (!isFoundPush)
                                                    {
                                                        AppController.mMessageReadList.add(addUnreadMessage(readObject, i, dataArray.length()));

                                                    }
                                                }

                                            }
                                        }
                                        if (AppController.callTypeStatus.equalsIgnoreCase("1"))
                                        {
                                            msgUnreadLinear.setVisibility(View.VISIBLE);
                                            msgReadLinear.setVisibility(View.GONE);
                                            noMsgAlertRelative.setVisibility(View.GONE);
                                            noMsgText.setVisibility(View.GONE);
                                          //  System.out.println("case worked"+type);
                                            if (AppController.mMessageUnreadList.size()>0)
                                            {
                                           //     System.out.println("case worked if");
                                                //set adapter
                                                unreadTxt.setBackgroundResource(R.drawable.notification_read);

                                                readTxt.setBackgroundResource(R.drawable.notification_unread);
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                                Collections.sort(AppController.mMessageUnreadList, new Comparator<PushNotificationModel>() {
                                                    public int compare(PushNotificationModel o1, PushNotificationModel o2) {
                                                        return o1.getDateTime().compareTo(o2.getDateTime());
                                                    }
                                                });
                                                Collections.reverse(AppController.mMessageUnreadList);
                                                unReadMessageAdapter = new UnReadMessageAdapter(mContext, AppController.mMessageUnreadList, messageUnRead, markRead, markUnRead,msgUnread, new ClickListener() {
                                                    @Override
                                                    public void onPositionClicked(int position) {


                                                    }

                                                    @Override
                                                    public void onLongClicked(int position) {
                                                        markRead.setVisibility(View.GONE);
                                                        markUnRead.setVisibility(View.GONE);
                                                        messageUnRead.setBackgroundResource(R.drawable.check_box_header);
                                                        AppController.isVisibleUnreadBox = true;
                                                        AppController.isVisibleReadBox = false;
                                                        AppController.click_count = 0;

                                                        for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                                        {
                                                            AppController.mMessageReadList.get(i).setMarked(false);
                                                        }
                                                        for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                                        {
                                                            AppController.mMessageUnreadList.get(i).setChecked(false);
                                                        }
                                                        if (AppController.mMessageReadList.size() > 0) {
                                                            readMessageAdapter.notifyDataSetChanged();
                                                            // readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                                                        }


                                                        unReadMessageAdapter.notifyDataSetChanged();
                                                        //unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());


                                                        messageUnRead.setVisibility(View.VISIBLE);
                                                        msgUnread.setVisibility(View.VISIBLE);
                                                        messageRead.setVisibility(View.GONE);
                                                        msgReadSelect.setVisibility(View.GONE);

                                                    }
                                                });

                                                msgUnreadLinear.setVisibility(View.VISIBLE);
                                                msgReadLinear.setVisibility(View.GONE);
                                                unReadMessageAdapter.notifyDataSetChanged();
                                                if (isFromUnReadBottom)
                                                {
                                                    unReadList.scrollToPosition(pageUnReadValue-2);
                                                }
                                                // unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());
                                                unReadList.setAdapter(unReadMessageAdapter);
                                                unReadMessageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                                    @Override
                                                    public void onBottomReached(int position) {
                                                        if (position==AppController.mMessageUnreadList.size()-1)
                                                        {
                                                            if (!isAvailable)
                                                            {
                                                                isFromUnReadBottom=true;
                                                                if (AppController.isVisibleUnreadBox)
                                                                {
                                                                    markRead.setVisibility(View.GONE);
                                                                    markUnRead.setVisibility(View.GONE);
                                                                    messageUnRead.setBackgroundResource(R.drawable.check_box_header);
                                                                    AppController.isVisibleUnreadBox = true;
                                                                    AppController.isVisibleReadBox = false;
                                                                    AppController.click_count = 0;

                                                                    for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                                                    {
                                                                        AppController.mMessageReadList.get(i).setMarked(false);
                                                                    }
                                                                    for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                                                    {
                                                                        AppController.mMessageUnreadList.get(i).setChecked(false);
                                                                    }
                                                                }

                                                                typeValue="1";
                                                                limitValue=20;
                                                                pageUnReadValue=pageUnReadValue+limitValue;
                                                                callPushNotification(type,String.valueOf(limitValue),String.valueOf(pageUnReadValue));
                                                               // Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();
                                                            }



                                                        }

                                                     //   System.out.println("position bottom"+position);




                                                    }
                                                });
                                            }
                                            else
                                            {
                                            //    System.out.println("case worked if");
                                                unreadTxt.setBackgroundResource(R.drawable.notification_read);
                                                readTxt.setBackgroundResource(R.drawable.notification_unread);
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                                noMsgAlertRelative.setVisibility(View.VISIBLE);
                                                noMsgText.setVisibility(View.VISIBLE);
                                                noMsgText.setText("Currently you have no messages");
                                            }
                                        }
                                        else
                                        {
                                            noMsgAlertRelative.setVisibility(View.GONE);
                                            noMsgText.setVisibility(View.GONE);
                                           // System.out.println("working else");
                                            if (AppController.mMessageReadList.size()>0)
                                            {
                                                readTxt.setBackgroundResource(R.drawable.notification_read);
                                                unreadTxt.setBackgroundResource(R.drawable.notification_unread);
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                                //set adapter
                                                Collections.sort(AppController.mMessageReadList, new Comparator<PushNotificationModel>() {
                                                    public int compare(PushNotificationModel o1, PushNotificationModel o2) {
                                                        return o1.getDateTime().compareTo(o2.getDateTime());
                                                    }
                                                });
                                                Collections.reverse(AppController.mMessageReadList);
                                                readMessageAdapter = new ReadMessageAdapter(mContext, AppController.mMessageReadList, messageRead, markUnRead, markRead,msgReadSelect, new ClickListener() {
                                                    @Override
                                                    public void onPositionClicked(int position) {
                                                        markRead.setVisibility(View.GONE);
                                                        markUnRead.setVisibility(View.GONE);
                                                        messageRead.setVisibility(View.GONE);
                                                        msgReadSelect.setVisibility(View.GONE);
                                                        messageUnRead.setVisibility(View.GONE);
                                                        msgUnread.setVisibility(View.GONE);
                                                        AppController.isVisibleUnreadBox = false;
                                                        AppController.isVisibleReadBox = false;
                                                        Intent intent = new Intent(mContext, MessageDetail.class);
                                                        intent.putExtra("title", AppController.mMessageReadList.get(position).getTitle());
                                                        intent.putExtra("message", AppController.mMessageReadList.get(position).getMessage());
                                                        intent.putExtra("date", AppController.mMessageReadList.get(position).getDay());
                                                        ///intent.putExtra("position",position);
                                                        mContext.startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onLongClicked(int position) {
                                                        markRead.setVisibility(View.GONE);
                                                        markUnRead.setVisibility(View.GONE);
                                                        AppController.click_count_read = 0;
                                                        // System.out.println("checkbox read visible:::::");
                                                        messageRead.setBackgroundResource(R.drawable.check_box_header);
                                                        AppController.isVisibleReadBox = true;
                                                        AppController.isVisibleUnreadBox = false;
                                                        for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                                        {
                                                            AppController.mMessageReadList.get(i).setMarked(false);
                                                        }
                                                        for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                                        {
                                                            AppController.mMessageUnreadList.get(i).setChecked(false);
                                                        }

                                                        if (AppController.mMessageUnreadList.size() > 0) {
                                                            unReadMessageAdapter.notifyDataSetChanged();
                                                            // unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());

                                                        }

                                                        readMessageAdapter.notifyDataSetChanged();
                                                        //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                                                        messageRead.setVisibility(View.VISIBLE);
                                                        msgReadSelect.setVisibility(View.VISIBLE);
                                                        messageUnRead.setVisibility(View.GONE);
                                                        msgUnread.setVisibility(View.GONE);

                                                        noMsgAlertRelative.setVisibility(View.GONE);
                                                        noMsgText.setVisibility(View.GONE);
                                                    }
                                                });
                                                msgUnreadLinear.setVisibility(View.GONE);
                                                msgReadLinear.setVisibility(View.VISIBLE);
                                                if (isFromReadBottom)
                                                {
                                                    readList.scrollToPosition(pageReadValue-1);
                                                }
                                                readMessageAdapter.notifyDataSetChanged();
                                                //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                                                readList.setAdapter(readMessageAdapter);
                                                readMessageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                                    @Override
                                                    public void onBottomReached(int position) {
                                                        if (position==AppController.mMessageReadList.size()-2)
                                                        {
                                                            if (!isReadAvailable)
                                                            {
                                                                isFromReadBottom=true;
                                                                if (AppController.isVisibleReadBox)
                                                                {
                                                                    markRead.setVisibility(View.GONE);
                                                                    markUnRead.setVisibility(View.GONE);
                                                                    AppController.click_count_read = 0;
                                                                    // System.out.println("checkbox read visible:::::");
                                                                    messageRead.setBackgroundResource(R.drawable.check_box_header);
                                                                    AppController.isVisibleReadBox = true;
                                                                    AppController.isVisibleUnreadBox = false;
                                                                    for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                                                    {
                                                                        AppController.mMessageReadList.get(i).setMarked(false);
                                                                    }
                                                                    for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                                                    {
                                                                        AppController.mMessageUnreadList.get(i).setChecked(false);
                                                                    }
                                                                }

                                                                typeValue="2";
                                                                limitValue=20;
                                                                pageReadValue=pageReadValue+limitValue;
                                                                callPushNotification(type,String.valueOf(limitValue),String.valueOf(pageReadValue));
                                                              //  Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();
                                                            }
                                                            //Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();

                                                        }

                                                    //    System.out.println("position bottom"+position);




                                                    }
                                                });
                                            }
                                            else
                                            {
                                                readTxt.setBackgroundResource(R.drawable.notification_read);
                                                unreadTxt.setBackgroundResource(R.drawable.notification_unread);
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                               // System.out.println("working else");
                                                msgUnreadLinear.setVisibility(View.GONE);
                                                msgReadLinear.setVisibility(View.GONE);
                                                noMsgAlertRelative.setVisibility(View.VISIBLE);
                                                noMsgText.setVisibility(View.VISIBLE);
                                                noMsgText.setText("Currently you have no messages");
                                            }
                                        }


                                    }
                                    else
                                    {
                                        if (AppController.callTypeStatus.equalsIgnoreCase("1"))
                                        {
                                            if (AppController.mMessageUnreadList.size()>0)
                                            {
                                                System.out.println("case worked if");
                                                //set adapter
                                                unreadTxt.setBackgroundResource(R.drawable.notification_read);
                                                readTxt.setBackgroundResource(R.drawable.notification_unread);
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                                Collections.sort(AppController.mMessageUnreadList, new Comparator<PushNotificationModel>() {
                                                    public int compare(PushNotificationModel o1, PushNotificationModel o2) {
                                                        return o1.getDateTime().compareTo(o2.getDateTime());
                                                    }
                                                });
                                                Collections.reverse(AppController.mMessageUnreadList);
                                                unReadMessageAdapter = new UnReadMessageAdapter(mContext, AppController.mMessageUnreadList, messageUnRead, markRead, markUnRead,msgUnread, new ClickListener() {
                                                    @Override
                                                    public void onPositionClicked(int position) {


                                                    }

                                                    @Override
                                                    public void onLongClicked(int position) {
                                                        markRead.setVisibility(View.GONE);
                                                        markUnRead.setVisibility(View.GONE);
                                                        messageUnRead.setBackgroundResource(R.drawable.check_box_header);
                                                        AppController.isVisibleUnreadBox = true;
                                                        AppController.isVisibleReadBox = false;
                                                        AppController.click_count = 0;

                                                        for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                                        {
                                                            AppController.mMessageReadList.get(i).setMarked(false);
                                                        }
                                                        for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                                        {
                                                            AppController.mMessageUnreadList.get(i).setChecked(false);
                                                        }
                                                        if (AppController.mMessageReadList.size() > 0) {
                                                            readMessageAdapter.notifyDataSetChanged();
                                                            // readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                                                        }


                                                        unReadMessageAdapter.notifyDataSetChanged();
                                                        //unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());


                                                        messageUnRead.setVisibility(View.VISIBLE);
                                                        msgUnread.setVisibility(View.VISIBLE);
                                                        messageRead.setVisibility(View.GONE);
                                                        msgReadSelect.setVisibility(View.GONE);

                                                    }
                                                });

                                                msgUnreadLinear.setVisibility(View.VISIBLE);
                                                msgReadLinear.setVisibility(View.GONE);
                                                unReadMessageAdapter.notifyDataSetChanged();
                                                // unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());
                                                unReadList.setAdapter(unReadMessageAdapter);
                                                unReadMessageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                                    @Override
                                                    public void onBottomReached(int position) {
                                                        if (position==AppController.mMessageUnreadList.size()-2)
                                                        {
//                                                            if (dataLength<20)
//                                                            {
//                                                                if (!isAvailable)
//                                                                {
//                                                                    typeValue="1";
//                                                                    limitValue=20;
//                                                                    pageValue=pageValue+limitValue;
//                                                                    callPushNotification(type,String.valueOf(limitValue),String.valueOf(pageValue));
//                                                                    Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();
//                                                                }
//                                                            }


                                                        }

                                                     //   System.out.println("position bottom"+position);




                                                    }
                                                });
                                            }
                                            else
                                            {
                                                unreadTxt.setBackgroundResource(R.drawable.notification_read);
                                                readTxt.setBackgroundResource(R.drawable.notification_unread);
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                             //   System.out.println("working else");
                                                msgUnreadLinear.setVisibility(View.GONE);
                                                msgReadLinear.setVisibility(View.GONE);
                                                noMsgAlertRelative.setVisibility(View.VISIBLE);
                                                noMsgText.setVisibility(View.VISIBLE);
                                                noMsgText.setText("Currently you have no messages");
                                            }

                                        }
                                        else
                                        {
                                            if (AppController.mMessageReadList.size()>0)
                                            {
                                                readTxt.setBackgroundResource(R.drawable.notification_read);
                                                unreadTxt.setBackgroundResource(R.drawable.notification_unread);
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                                Collections.sort(AppController.mMessageReadList, new Comparator<PushNotificationModel>() {
                                                    public int compare(PushNotificationModel o1, PushNotificationModel o2) {
                                                        return o1.getDateTime().compareTo(o2.getDateTime());
                                                    }
                                                });
                                                Collections.reverse(AppController.mMessageReadList);
                                                //set adapter
                                                readMessageAdapter = new ReadMessageAdapter(mContext, AppController.mMessageReadList, messageRead, markUnRead, markRead,msgReadSelect, new ClickListener() {
                                                    @Override
                                                    public void onPositionClicked(int position) {
                                                        markRead.setVisibility(View.GONE);
                                                        markUnRead.setVisibility(View.GONE);
                                                        messageRead.setVisibility(View.GONE);
                                                        msgReadSelect.setVisibility(View.GONE);
                                                        messageUnRead.setVisibility(View.GONE);
                                                        msgUnread.setVisibility(View.GONE);
                                                        AppController.isVisibleUnreadBox = false;
                                                        AppController.isVisibleReadBox = false;
                                                        Intent intent = new Intent(mContext, MessageDetail.class);
                                                        intent.putExtra("title", AppController.mMessageReadList.get(position).getTitle());
                                                        intent.putExtra("message", AppController.mMessageReadList.get(position).getMessage());
                                                        intent.putExtra("date", AppController.mMessageReadList.get(position).getDay());
                                                        ///intent.putExtra("position",position);
                                                        mContext.startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onLongClicked(int position) {
                                                        markRead.setVisibility(View.GONE);
                                                        markUnRead.setVisibility(View.GONE);
                                                        AppController.click_count_read = 0;
                                                        // System.out.println("checkbox read visible:::::");
                                                        messageRead.setBackgroundResource(R.drawable.check_box_header);
                                                        AppController.isVisibleReadBox = true;
                                                        AppController.isVisibleUnreadBox = false;
                                                        for (int i = 0; i < AppController.mMessageReadList.size(); i++)

                                                        {
                                                            AppController.mMessageReadList.get(i).setMarked(false);
                                                        }
                                                        for (int i = 0; i < AppController.mMessageUnreadList.size(); i++)

                                                        {
                                                            AppController.mMessageUnreadList.get(i).setChecked(false);
                                                        }

                                                        if (AppController.mMessageUnreadList.size() > 0) {
                                                            unReadMessageAdapter.notifyDataSetChanged();
                                                            // unReadMessageAdapter.notifyItemRangeChanged(0, unReadMessageAdapter.getItemCount());

                                                        }
                                                        readMessageAdapter.notifyDataSetChanged();
                                                        //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                                                        messageRead.setVisibility(View.VISIBLE);
                                                        msgReadSelect.setVisibility(View.VISIBLE);
                                                        messageUnRead.setVisibility(View.GONE);
                                                        msgUnread.setVisibility(View.GONE);

                                                        noMsgAlertRelative.setVisibility(View.GONE);
                                                        noMsgText.setVisibility(View.GONE);
                                                    }
                                                });
                                                msgUnreadLinear.setVisibility(View.GONE);
                                                msgReadLinear.setVisibility(View.VISIBLE);
                                                readMessageAdapter.notifyDataSetChanged();
                                                //readMessageAdapter.notifyItemRangeChanged(0, readMessageAdapter.getItemCount());

                                                readList.setAdapter(readMessageAdapter);
                                                readMessageAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                                    @Override
                                                    public void onBottomReached(int position) {
                                                        if (position==AppController.mMessageReadList.size()-2)
                                                        {
//                                                            if (dataLength>=19)
//                                                            {
//                                                                typeValue="2";
//                                                                limitValue=20;
//                                                                pageValue=pageValue+limitValue;
//                                                                callPushNotification(type,String.valueOf(limitValue),String.valueOf(pageValue));
//                                                                Toast.makeText(mContext,  "Reached", Toast.LENGTH_SHORT).show();
//                                                            }


                                                        }

                                                      //  System.out.println("position bottom"+position);




                                                    }
                                                });
//                                                System.out.println("working else");
//                                                msgUnreadLinear.setVisibility(View.GONE);
//                                                msgReadLinear.setVisibility(View.GONE);
//                                                noMsgAlertRelative.setVisibility(View.VISIBLE);
//                                                noMsgText.setVisibility(View.VISIBLE);
//                                                noMsgText.setText("Currently you have no messages");
                                            }
                                            else
                                            {
                                                readTxt.setBackgroundResource(R.drawable.notification_read);
                                                unreadTxt.setBackgroundResource(R.drawable.notification_unread);
                                                readTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                                                unreadTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
                                                System.out.println("working else");
                                                msgUnreadLinear.setVisibility(View.GONE);
                                                msgReadLinear.setVisibility(View.GONE);
                                                noMsgAlertRelative.setVisibility(View.VISIBLE);
                                                noMsgText.setVisibility(View.VISIBLE);
                                                noMsgText.setText("Currently you have no messages");
                                            }

                                        }
                                    }
                                }
                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callPushNotification(type,limit,page);

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception ex) {
                            if (isProgressVisible)
                            {    mProgressRelLayout.clearAnimation();
                                mProgressRelLayout.setVisibility(View.GONE);
                                isProgressVisible=false;
                            }
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                  if (isProgressVisible)
                  {
                      mProgressRelLayout.clearAnimation();
                      mProgressRelLayout.setVisibility(View.GONE);
                      isProgressVisible=false;
                  }
                }
            });
        } catch (Exception e) {
            if (isProgressVisible)
            {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                isProgressVisible=false;
            }
            e.printStackTrace();
        }


    }


    private void initialiseUI() {
        mProgressRelLayout = (RelativeLayout)mRootView.findViewById(R.id.progressDialog);
        nestedScroll = mRootView.findViewById(R.id.nestedScroll);
        markRead = mRootView.findViewById(R.id.markReadTxt);
        unreadTxt = mRootView.findViewById(R.id.unreadTxt);
        msgReadSelect = mRootView.findViewById(R.id.msgReadSelect);
        readTxt = mRootView.findViewById(R.id.readTxt);
        markUnRead = mRootView.findViewById(R.id.markUnreadTxt);
        msgRead = mRootView.findViewById(R.id.msgRead);
        msgUnread = mRootView.findViewById(R.id.msgUnRead);
        messageRead = mRootView.findViewById(R.id.messageRead);
        messageUnRead = mRootView.findViewById(R.id.messageUnread);
        readList = mRootView.findViewById(R.id.mMessageReadList);
        noMsgAlertRelative = mRootView.findViewById(R.id.noMsgAlert);
        maniRelative = mRootView.findViewById(R.id.maniRelative);
        noMsgText = mRootView.findViewById(R.id.noMsgAlertTxt);
        readList.setHasFixedSize(true);
        LinearLayoutManager mRead = new LinearLayoutManager(mContext);
        mRead.setOrientation(LinearLayoutManager.VERTICAL);
        readList.setLayoutManager(mRead);
        unReadList = mRootView.findViewById(R.id.mMessageUnReadList);
        unReadList.setHasFixedSize(true);
        LinearLayoutManager mUnRead = new LinearLayoutManager(mContext);
        mUnRead.setOrientation(LinearLayoutManager.VERTICAL);
        unReadList.setLayoutManager(mUnRead);
        // mMessageUnreadList=new ArrayList<PushNotificationModel>();
        // mMessageReadList = new ArrayList<PushNotificationModel>();
        msgReadLinear = mRootView.findViewById(R.id.msgReadLinear);
        msgUnreadLinear = mRootView.findViewById(R.id.msgUnreadLinear);
        markRead.setOnClickListener(this);
        markUnRead.setOnClickListener(this);
        messageRead.setOnClickListener(this);
        messageUnRead.setOnClickListener(this);
        noMsgAlertRelative.setOnClickListener(this);
        maniRelative.setOnClickListener(this);
        unreadTxt.setOnClickListener(this);
        readTxt.setOnClickListener(this);

    }

    private static PushNotificationModel addUnreadMessage(JSONObject obj, int i, int length) {
        PushNotificationModel model = new PushNotificationModel();
        SimpleDateFormat sdfcalender1 = null;
        SimpleDateFormat sdfcalender2 = null;

        sdfcalender1 = new SimpleDateFormat("yyyy-MM-dd");
        sdfcalender2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            model.setPushid(obj.getString("pushid"));
            model.setMessage(obj.getString("htmlmessage"));//message
            model.setUrl(obj.getString("url"));
            model.setDate(obj.getString("date"));
            model.setType(obj.getString("type"));
            model.setPush_from(obj.getString("push_from"));
            model.setFavourite(obj.getString("favourite"));
            model.setStudent_name(obj.getString("student_name"));
            model.setStatus("0");
            ArrayList<String> studList = new ArrayList<>();
            String[] namesList = obj.optString("student_name").split(",");
            for (String name : namesList) {
                // System.out.println(name);
                studList.add(name);
            }
            model.setStudentArray(studList);
            model.setTitle(obj.optString("title"));

            String mDate = obj.optString("date");
            System.out.println("compareDate"+mDate);
            sdfcalender2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date compareDate= new Date();
            compareDate=sdfcalender2.parse(mDate);
            Date msgDate = new Date();
            System.out.println("compareDate"+compareDate);
            model.setDateTime(compareDate);
            try {

                msgDate = sdfcalender.parse(mDate);
            } catch (ParseException ex) {
                //log.e("Date", "Parsing error");

            }
            String day = (String) DateFormat.format("dd", msgDate); // 20
            if (day.endsWith("1") && !day.endsWith("11")) {
                sdfcalender1 = new SimpleDateFormat("EEEE  d'st' MMMM");

            } else if (day.endsWith("2") && !day.endsWith("12")) {
                sdfcalender1 = new SimpleDateFormat("EEEE  d'nd' MMMM");

            } else if (day.endsWith("3") && !day.endsWith("13"))
                sdfcalender1 = new SimpleDateFormat("EEEE  d'rd' MMMM");
            else
                sdfcalender1 = new SimpleDateFormat("EEEE  d'th' MMMM");
            Date mEventDate = new Date();

            try {
                mEventDate = sdfcalender.parse(mDate);
                SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                String startTime = format2.format(mEventDate);
                model.setPushtime(startTime);
            } catch (ParseException ex) {
                //log.e("Date", "Parsing error2");
            }
            String newday = sdfcalender1.format(mEventDate);
            model.setDay(newday);

            if (AppController.mMessageUnreadListFav.size() > 0 && i < length) {
                if (AppController.mMessageUnreadListFav.get(i).getChecked() != null) {
                    model.setChecked(AppController.mMessageUnreadListFav.get(i).getChecked());

                } else {
                    model.setChecked(false);

                }
            } else {
                model.setChecked(false);

            }
        } catch (Exception ex) {
            // System.out.println("Exception is" + ex);
        }

        return model;
    }

    private static PushNotificationModel addReadMessage(JSONObject obj, int i, int length) {
        PushNotificationModel mRead = new PushNotificationModel();
        SimpleDateFormat sdfcalender1 = null;
        SimpleDateFormat sdfcalender2 = null;

        sdfcalender1 = new SimpleDateFormat("yyyy-MM-dd");
        sdfcalender2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            mRead.setPushid(obj.getString("pushid"));
            mRead.setMessage(obj.getString("htmlmessage"));//message
            mRead.setUrl(obj.getString("url"));
            mRead.setDate(obj.getString("date"));
            mRead.setType(obj.getString("type"));
            mRead.setPush_from(obj.getString("push_from"));
            mRead.setFavourite(obj.getString("favourite"));
            mRead.setTitle(obj.optString("title"));

            mRead.setStudent_name(obj.optString("student_name"));
            ArrayList<String> studList = new ArrayList<>();
            String[] namesList = obj.optString("student_name").split(",");
            for (String name : namesList) {
                // System.out.println(name);
                studList.add(name);
            }
            mRead.setStudentArray(studList);

            String mDate = obj.optString("date");
            // System.out.println("DatesArray:" + mDate);
            Date msgDate = new Date();
            sdfcalender2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date compareDate= new Date();
            compareDate=sdfcalender2.parse(mDate);
            System.out.println("compare date"+compareDate);
            mRead.setDateTime(compareDate);

            try {

                msgDate = sdfcalender.parse(mDate);
            } catch (ParseException ex) {
                //log.e("Date", "Parsing error3");

            }
            String day = (String) DateFormat.format("dd", msgDate); // 20
            if (day.endsWith("1") && !day.endsWith("11")) {
                sdfcalender1 = new SimpleDateFormat("EEEE  d'st' MMMM");

            } else if (day.endsWith("2") && !day.endsWith("12")) {
                sdfcalender1 = new SimpleDateFormat("EEEE  d'nd' MMMM");

            } else if (day.endsWith("3") && !day.endsWith("13"))
                sdfcalender1 = new SimpleDateFormat("EEEE  d'rd' MMMM");
            else
                sdfcalender1 = new SimpleDateFormat("EEEE  d'th' MMMM");
            Date mEventDate = new Date();

            try {

                mEventDate = sdfcalender.parse(mDate);
                SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                String startTime = format2.format(mEventDate);
                mRead.setPushtime(startTime);
            } catch (ParseException ex) {
                //log.e("Date", "Parsing error4");
            }
            String newday = sdfcalender1.format(mEventDate);
            mRead.setDay(newday);
            if (AppController.mMessageReadListFav.size() > 0 && i < length) {
                if (AppController.mMessageReadListFav.get(i).isMarked() != null) {
                    mRead.setMarked(AppController.mMessageReadListFav.get(i).isMarked());

                } else {
                    mRead.setMarked(false);

                }
            } else {
                mRead.setMarked(false);

            }
        } catch (Exception ex) {
            // System.out.println("Exception is" + ex);
        }

        return mRead;
    }

    public static void callfavouriteStatusApi(final String favourite, final String pushId) {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_FAVOURITE_CHANGE);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_ID, "favourite", JTAG_PUSH_ID};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), favourite, pushId};
            manager.getResponsePOST(mContext, 16, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    // System.out.println("NotifyRes:" + successResponse);
                    // System.out.println("push ");
                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.getString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.getString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
//                                    if (typeValue.equalsIgnoreCase("1"))
//                                    {
//                                    }
//                                    else
//                                    {
//                                        typeValue="2";
//                                        limitValue=20;
//                                        pageReadValue=0;
//                                        callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageReadValue));
//                                    }


                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callfavouriteStatusApi(favourite, pushId);

                                }
                            } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callfavouriteStatusApi(favourite, pushId);

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


    public static void callstatusapi(final String statusread, final String pushId) {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_STATUS_CHANGE);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_ID, "status", JTAG_PUSH_ID};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), statusread, pushId};
            manager.getResponsePOST(mContext, 16, name, value, new VolleyWrapper.ResponseListener() {

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

                                    markRead.setVisibility(View.GONE);
                                    markUnRead.setVisibility(View.GONE);
                                    messageRead.setVisibility(View.GONE);
                                    msgReadSelect.setVisibility(View.GONE);
                                    messageUnRead.setVisibility(View.GONE);
                                    msgUnread.setVisibility(View.GONE);
                                    AppController.isVisibleUnreadBox = false;
                                    AppController.isVisibleReadBox = false;
                                    System.out.print("type"+typeValue);
                                    if (statusread.equalsIgnoreCase("1"))
                                    {
                                        typeValue="1";
                                        limitValue=20;
                                        pageUnReadValue=0;
                                        callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageUnReadValue));
                                    }
                                    else
                                    {
                                        typeValue="2";
                                        limitValue=20;
                                        pageReadValue=0;
                                        callPushNotification(typeValue,String.valueOf(limitValue),String.valueOf(pageReadValue));
                                    }

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

    @Override
    public void onResume() {
        super.onResume();

        if (AppController.isfromUnread) {
            unreadTxt.setBackgroundResource(R.drawable.notification_read);
            readTxt.setBackgroundResource(R.drawable.notification_unread);
//            for(int i = 0; i<AppController.mMessageUnreadList.size(); i++)
//            {
//                if (AppController.pushId.equalsIgnoreCase(AppController.mMessageUnreadList.get(i).getPushid()))
//                {
//                    AppController.mMessageUnreadList.remove(i);
//                }
//            }
            unReadMessageAdapter.notifyDataSetChanged();
            AppController.isfromUnread = false;
            callstatusapi("1", AppController.pushId);

        } else if (AppController.isfromRead) {
            unreadTxt.setBackgroundResource(R.drawable.notification_unread);
            readTxt.setBackgroundResource(R.drawable.notification_read);
            readTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            unreadTxt.setTextColor(mContext.getResources().getColor(R.color.split_bg));
            AppController.isfromRead = false;
            markRead.setVisibility(View.GONE);
            markUnRead.setVisibility(View.GONE);
            messageRead.setVisibility(View.GONE);
            msgReadSelect.setVisibility(View.GONE);
            messageUnRead.setVisibility(View.GONE);
            msgUnread.setVisibility(View.GONE);
            AppController.isVisibleUnreadBox = false;
            AppController.isVisibleReadBox = false;
            readMessageAdapter.notifyDataSetChanged();
        }
        if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Notification Screen Fragment. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");
        }
        // System.out.println("testinngresume");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
// Save state
        Parcelable recyclerViewState;
        recyclerViewState = readList.getLayoutManager().onSaveInstanceState();

// Restore state
        readList.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        Parcelable recyclerViewUnReadListState;
        recyclerViewUnReadListState = unReadList.getLayoutManager().onSaveInstanceState();

// Restore state
        unReadList.getLayoutManager().onRestoreInstanceState(recyclerViewUnReadListState);
    }
}

