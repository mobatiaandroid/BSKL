package com.mobatia.bskl.fragment.settings.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mobatia.bskl.R;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.settings.SettingsFragment;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.CustomDialog;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 29/06/18.
 */

public class CustomSettingsAdapter extends BaseAdapter implements
        CacheDIRConstants, IntentPassValueConstants, URLConstants, JSONConstants {

    private static Context mContext;
    private ArrayList<String> mSettingsList;
    public static String emailStatus="";
    String calenderStatus = "";
    private String mTitle;
    private String mTabId;
    static ViewHolder mViewHolder;


    public CustomSettingsAdapter(Context context,
                                 ArrayList<String> arrList, String title, String tabId) {
        mContext = context;
        this.mSettingsList = arrList;
        this.mTitle = title;
        this.mTabId = tabId;
    }

    public CustomSettingsAdapter(Context context,
                                 ArrayList<String> arrList) {
        mContext = context;
        this.mSettingsList = arrList;

    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSettingsList.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mSettingsList.get(position);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflate = LayoutInflater.from(mContext);
            convertView = inflate.inflate(R.layout.custom_settings_list_adapter, null);
            mViewHolder = new ViewHolder();
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();

        }
        try {
            mViewHolder.mTitleTxt = convertView.findViewById(R.id.listTxtTitle);
            mViewHolder.txtUser = convertView.findViewById(R.id.txtUser);
            mViewHolder.mImageView = convertView.findViewById(R.id.arrowImg);
            mViewHolder.toggle = convertView.findViewById(R.id.toggle);
            mViewHolder.mTitleTxt.setText(mSettingsList.get(position).toString());
            if (mSettingsList.size()==7)
            {
                if (position==1)
                {
                    if (PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1")) {

                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                    } else {
                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
                    }
                }
                if (position==2) {
                    if (PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1")) {
                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                    } else {
                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
                    }
                }
                if (position == 6)
                {
                    mViewHolder.txtUser.setVisibility(View.VISIBLE);
                    mViewHolder.mTitleTxt.setText("Logout");
                    mViewHolder.txtUser.setText("("+PreferenceManager.getUserEmail(mContext)+")");
                    mViewHolder.mImageView.setBackgroundResource(R.drawable.logout);


                } else {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mViewHolder.mTitleTxt.getLayoutParams();
                    lp.addRule(RelativeLayout.CENTER_VERTICAL);
                    mViewHolder.mTitleTxt.setLayoutParams(lp);


                }
                if (position == 1) {
                    mViewHolder.toggle.setVisibility(View.VISIBLE);
                    mViewHolder.mImageView.setVisibility(View.GONE);
                    mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1"))) {
                                mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                                calenderStatus = " 0";
                                calenderPush(URL_CALENDER_PUSH, calenderStatus);

                            } else {
                                mViewHolder.toggle.setImageResource(R.drawable.switch_on);

                                calenderStatus = "1";
                                calenderPush(URL_CALENDER_PUSH, calenderStatus);


                            }

                        }
                    });

                }
                if(position==2)
                {
                    mViewHolder.toggle.setVisibility(View.VISIBLE);
                    mViewHolder.mImageView.setVisibility(View.GONE);
                    mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1"))) {


                                mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                                emailStatus = " 0";
                                emailPush(URL_EMAIL_PUSH, emailStatus);

                            } else {
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    showDialogAlertEmail((Activity) mContext, "Confirm?", "If you trun off email notifications, you won't receive the copy of notification messages as email", R.drawable.questionmark_icon, R.drawable.round);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            }


                        }
                    });
                }
            }
            else
            {
                if (position==1)
                {
                    if (PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1")) {

                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                    } else {
                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
                    }
                }
                if (position==2) {
                    if (PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1")) {
                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                    } else {
                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
                    }
                }
                if (position == 7)
                {
                    mViewHolder.txtUser.setVisibility(View.VISIBLE);
                    mViewHolder.mTitleTxt.setText("Logout");
                    mViewHolder.txtUser.setText("("+PreferenceManager.getUserEmail(mContext)+")");
                    mViewHolder.mImageView.setBackgroundResource(R.drawable.logout);


                } else {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mViewHolder.mTitleTxt.getLayoutParams();
                    lp.addRule(RelativeLayout.CENTER_VERTICAL);
                    mViewHolder.mTitleTxt.setLayoutParams(lp);


                }
                if (position == 1) {
                    mViewHolder.toggle.setVisibility(View.VISIBLE);
                    mViewHolder.mImageView.setVisibility(View.GONE);
                    mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1"))) {
                                mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                                calenderStatus = " 0";
                                calenderPush(URL_CALENDER_PUSH, calenderStatus);

                            } else {
                                mViewHolder.toggle.setImageResource(R.drawable.switch_on);

                                calenderStatus = "1";
                                calenderPush(URL_CALENDER_PUSH, calenderStatus);


                            }

                        }
                    });

                }
                if(position==2)
                {
                    mViewHolder.toggle.setVisibility(View.VISIBLE);
                    mViewHolder.mImageView.setVisibility(View.GONE);
                    mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1"))) {


                                mViewHolder.toggle.setImageResource(R.drawable.switch_off);

                                emailStatus = " 0";
                                emailPush(URL_EMAIL_PUSH, emailStatus);

                            } else {
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    showDialogAlertEmail((Activity) mContext, "Confirm?", "If you trun off email notifications, you won't receive the copy of notification messages as email", R.drawable.questionmark_icon, R.drawable.round);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            }


                        }
                    });
                }
            }
//            if (PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("0"))
//            {
//                // Currently user have no data collection
//                if (position==1)
//                {
//                    if (PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1")) {
//
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                    } else {
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
//                    }
//                }
//                if (position==2) {
//                    if (PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1")) {
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                    } else {
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
//                    }
//                }
//                if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
//                {
//
//                    if (position == 7)
//                    {
//                        mViewHolder.txtUser.setVisibility(View.VISIBLE);
//                        mViewHolder.mTitleTxt.setText("Logout");
//                        mViewHolder.txtUser.setText("("+PreferenceManager.getUserEmail(mContext)+")");
//                        mViewHolder.mImageView.setBackgroundResource(R.drawable.logout);
//
//
//                    } else {
//                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mViewHolder.mTitleTxt.getLayoutParams();
//                        lp.addRule(RelativeLayout.CENTER_VERTICAL);
//                        mViewHolder.mTitleTxt.setLayoutParams(lp);
//
//
//                    }
//                    if (position == 1) {
//                        mViewHolder.toggle.setVisibility(View.VISIBLE);
//                        mViewHolder.mImageView.setVisibility(View.GONE);
//                        mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if ((PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1"))) {
//                                    mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                                    calenderStatus = " 0";
//                                    calenderPush(URL_CALENDER_PUSH, calenderStatus);
//
//                                } else {
//                                    mViewHolder.toggle.setImageResource(R.drawable.switch_on);
//
//                                    calenderStatus = "1";
//                                    calenderPush(URL_CALENDER_PUSH, calenderStatus);
//
//
//                                }
//
//                            }
//                        });
//
//                    }
//                    if(position==2)
//                    {
//                        mViewHolder.toggle.setVisibility(View.VISIBLE);
//                        mViewHolder.mImageView.setVisibility(View.GONE);
//                        mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if ((PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1"))) {
//
//
//                                    mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                                    emailStatus = " 0";
//                                    emailPush(URL_EMAIL_PUSH, emailStatus);
//
//                                } else {
//                                    if (AppUtils.isNetworkConnected(mContext)) {
//                                        showDialogAlertEmail((Activity) mContext, "Confirm?", "If you trun off email notifications, you won't receive the copy of notification messages as email", R.drawable.questionmark_icon, R.drawable.round);
//                                    } else {
//                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//                                    }
//
//
//                                }
//
//
//                            }
//                        });
//                    }
//
//                }
//            }
//            else
//            {
            // The user have data collection ie, no trigger option

//                if (position==1)
//                {
//                    if (PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1")) {
//
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                    } else {
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
//                    }
//                }
//                if (position==2) {
//                    if (PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1")) {
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                    } else {
//                        mViewHolder.toggle.setImageResource(R.drawable.switch_on);
//                    }
//                }
//                if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
//                {

//                    if (position == 6) {
//                        mViewHolder.txtUser.setVisibility(View.VISIBLE);
//                        mViewHolder.mTitleTxt.setText("Logout");
//                        mViewHolder.txtUser.setText("("+PreferenceManager.getUserEmail(mContext)+")");
//                        mViewHolder.mImageView.setBackgroundResource(R.drawable.logout);
//
//
//                    } else {
//                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mViewHolder.mTitleTxt.getLayoutParams();
//                        lp.addRule(RelativeLayout.CENTER_VERTICAL);
//                        mViewHolder.mTitleTxt.setLayoutParams(lp);
//
//
//                    }
//                    if (position == 1) {
//                        mViewHolder.toggle.setVisibility(View.VISIBLE);
//                        mViewHolder.mImageView.setVisibility(View.GONE);
//                        mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if ((PreferenceManager.getCalenderPush(mContext).equalsIgnoreCase("1"))) {
//                                    mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                                    calenderStatus = " 0";
//                                    calenderPush(URL_CALENDER_PUSH, calenderStatus);
//
//                                } else {
//                                    mViewHolder.toggle.setImageResource(R.drawable.switch_on);
//
//                                    calenderStatus = "1";
//                                    calenderPush(URL_CALENDER_PUSH, calenderStatus);
//
//
//                                }
//
//                            }
//                        });
//
//                    }
//                    if(position==2)
//                    {
//                        mViewHolder.toggle.setVisibility(View.VISIBLE);
//                        mViewHolder.mImageView.setVisibility(View.GONE);
//                        mViewHolder.toggle.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if ((PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("1"))) {
//
//
//                                    mViewHolder.toggle.setImageResource(R.drawable.switch_off);
//
//                                    emailStatus = " 0";
//                                    emailPush(URL_EMAIL_PUSH, emailStatus);
//
//                                } else {
//                                    if (AppUtils.isNetworkConnected(mContext)) {
//                                        showDialogAlertEmail((Activity) mContext, "Confirm?", "If you trun off email notifications, you won't receive the copy of notification messages as email", R.drawable.questionmark_icon, R.drawable.round);
//                                    } else {
//                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//                                    }
//
//
//                                }
//
//
//                            }
//                        });
//                    }

            //              }
            //    }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }




        return convertView;
    }

    public void calenderPush(String URL, String calenderStatus) {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = {JTAG_ACCESSTOKEN, "user_ids", "status"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), calenderStatus};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("NotifyRes:" + successResponse);
                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.getString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.getString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
                                    SettingsFragment.showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                                }
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
    public static void emailPush(String URL, final String emailStatus) {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = {JTAG_ACCESSTOKEN, "user_ids", "status"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), emailStatus};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("NotifyRes:" + successResponse);
                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.getString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.getString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
                                    if ((PreferenceManager.getEmailPush(mContext).equalsIgnoreCase("0"))) {

                                    }


                                    SettingsFragment.showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);


                                }
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
    public static void showDialogAlertDismiss(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    class ViewHolder {
        private TextView mTitleTxt;
        private ImageView mImageView;
        private ImageView toggle;

        private TextView txtUser;

    }
    public static void showDialogAlertEmail(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.toggle.setImageResource(R.drawable.switch_on);
                emailStatus = " 1";


                if (AppUtils.checkInternet(mContext)) {
                    emailPush(URL_EMAIL_PUSH, emailStatus);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }



                dialog.dismiss();

            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.toggle.setImageResource(R.drawable.switch_on);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
