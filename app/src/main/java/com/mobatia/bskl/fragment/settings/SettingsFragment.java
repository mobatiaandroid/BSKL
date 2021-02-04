package com.mobatia.bskl.fragment.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.DataCollectionHome;
import com.mobatia.bskl.activity.login.LoginActivity;
import com.mobatia.bskl.activity.tutorial.TutorialActivity;
import com.mobatia.bskl.activity.userdetail.UserDetailActivity;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.settings.adapter.CustomSettingsAdapter;
import com.mobatia.bskl.fragment.settings.adapter.TriggerRecyclerAdapter;
import com.mobatia.bskl.fragment.settings.model.TriggerDataModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by krishnaraj on 27/06/18.
 */

public class SettingsFragment extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants {

    private View mRootView;
    private static Context mContext;
    private static ListView mSettingsList;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    EditText text_currentpswd, newpassword, confirmpassword;
    static boolean isRegUser;
    Dialog dialog;
    TextView versionCode;
    String valueTrigger="";
    static ArrayList<String> mSettingsListArrayDataCollection = new ArrayList<String>() {
        {

            add("Change App Settings");
            add("Calendar Notifications");
            add("Email Notifications");

            add("Tutorial");
//            add("Terms of Service");
            add("Change Password");
            add("Update Account Details");
            add("Account Details");
            add("Logout");
        }
    };
    static ArrayList<String> mSettingsListArray = new ArrayList<String>() {
        {

            add("Change App Settings");
            add("Calendar Notifications");
            add("Email Notifications");
            add("Tutorial");
//            add("Terms of Service");
            add("Change Password");

            add("Account Details");
            add("Logout");
        }
    };

    public SettingsFragment() {

    }

    public SettingsFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
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
        mRootView = inflater.inflate(R.layout.fragment_settings_list, container,
                false);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = actionBar.getCustomView().findViewById(R.id.action_bar_forward);
        headerTitle.setText("Settings");
        action_bar_forward.setVisibility(View.INVISIBLE);
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        dialog = new Dialog(mContext, R.style.NewDialog);
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mSettingsList = mRootView.findViewById(R.id.mSettingsListView);
        versionCode = mRootView.findViewById(R.id.versionText);
        relMain = mRootView.findViewById(R.id.relMain);

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode.setText("V "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
            {
                //user is not alumini
                if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                {
                    // user is not applicant
                    if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                    {
                        // current user with report mail merge enable
                        if (!PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                        {
                            // data collection trigger available
                            isRegUser = true;
                            mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArrayDataCollection));
                        }
                        else {
                            // data collection trigger not available
                            isRegUser = true;
                            mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
                        }
                    }
                    else
                    {
                        // current user with report mail merge enable
                        isRegUser = true;
                        mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
                    }
                }
                else
                {
                    // user is applicant
                    isRegUser = true;
                    mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
                }
            }
            else
            {
                // user is alumini
                isRegUser = true;
                mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
            }
        }
        mSettingsList.setOnItemClickListener(this);

        /* if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
            {
                if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                {
                    if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                    {
                        if (!PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                        isRegUser = true;
                        mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArrayDataCollection));
                    }
                    else
                    {
                        isRegUser = true;
                        mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
                    }
                }
                else
                {
                    isRegUser = true;
                    mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
                }
            }
            else
            {
                isRegUser = true;
                mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
            }*/

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (isRegUser) {

            if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
            {
                // user is current user

                if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                {
                    // user is not applicant

                    if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                    {
                        // Report mail merge enable ==== Access to all feature
                        if (!PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                        {
                            // user have no data collection=== view trigger data collection
                            switch (position) {
                                case 0://go to app settings
                                    PreferenceManager.setGoToSettings(mContext, "1");
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    mContext.startActivity(intent);
                                    break;
                                case 1://terms of service
                                    break;
                                case 2:
                                    break;

                                case 3:
                                    Intent mintent = new Intent(mContext, TutorialActivity.class);
                                    mintent.putExtra(TYPE, 0);
                                    mContext.startActivity(mintent);
                                    break;
                                case 4://change password
                                    if (AppUtils.isNetworkConnected(mContext)) {
                                        showChangePasswordAlert();
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                    }
                                    break;
                                case 5:
                                    if (AppUtils.isNetworkConnected(mContext))
                                    {
                                        showDialogAlertTriggerDataCollection(getActivity(), "Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round);
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                    }
                                    break;
                                case 6:
                                    System.out.println("cases 111111");
                                    Intent mIntent = new Intent(mContext, UserDetailActivity.class);
                                    mIntent.putExtra("tab_type", mSettingsListArrayDataCollection.get(position).toString());
                                    mContext.startActivity(mIntent);
                                    break;
                                case 7:
                                    if (AppUtils.isNetworkConnected(mContext))
                                    {
                                        AppUtils.showDialogAlertLogout(getActivity(), mContext,"Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round);
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                    }
                                    break;
                                default:
                                    break;
                            }

                        }
                        else
                        {
                            // user have  data collection=== no view trigger data collection
                            switch (position) {
                                case 0://go to app settings
                                    PreferenceManager.setGoToSettings(mContext, "1");
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    mContext.startActivity(intent);
                                    break;
                                case 1://terms of service
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    Intent mintent = new Intent(mContext, TutorialActivity.class);
                                    mintent.putExtra(TYPE, 0);
                                    mContext.startActivity(mintent);
                                    break;
                                case 4://change password
                                    if (AppUtils.isNetworkConnected(mContext)) {
                                        showChangePasswordAlert();
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                    }
                                    break;
                                case 5:
                                    System.out.println("cases 2222222");
                                    Intent mIntent = new Intent(mContext, UserDetailActivity.class);
                                    mIntent.putExtra("tab_type", mSettingsListArray.get(position).toString());
                                    mContext.startActivity(mIntent);
                                    break;
                                case 6:
                                    if (AppUtils.isNetworkConnected(mContext))
                                    {
                                        AppUtils.showDialogAlertLogout(getActivity(),mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round);
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    else {
                        // Report mail merge disable ==== no data collection and no view detail contact
                        switch (position) {
                            case 0://go to app settings
                                PreferenceManager.setGoToSettings(mContext, "1");
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                                break;
                            case 1://terms of service
                                break;
                            case 2:
                                break;
                            case 3:
                                Intent mintent = new Intent(mContext, TutorialActivity.class);
                                mintent.putExtra(TYPE, 0);
                                mContext.startActivity(mintent);
                                break;
                            case 4://change password
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    showChangePasswordAlert();
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                }
                                break;
                            case 5:
                                System.out.println("cases 3333333");
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);
                                }
                                else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                }
                                break;
                            case 6:
                                if (AppUtils.isNetworkConnected(mContext))
                                {
                                    AppUtils.showDialogAlertLogout(getActivity(), mContext,"Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                else
                {
                    // user is applicant
                    switch (position) {
                        case 0://go to app settings
                            PreferenceManager.setGoToSettings(mContext, "1");
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            mContext.startActivity(intent);
                            break;
                        case 1://terms of service
                            break;
                        case 2:
                            break;
                        case 3:
                            Intent mintent = new Intent(mContext, TutorialActivity.class);
                            mintent.putExtra(TYPE, 0);
                            mContext.startActivity(mintent);
                            break;
                        case 4://change password
                            if (AppUtils.isNetworkConnected(mContext)) {
                                showChangePasswordAlert();
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                            }
                            break;
                        case 5:
                            System.out.println("cases 444444");
                            if (AppUtils.isNetworkConnected(mContext)) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                            }
                            break;
                        case 6:
                            if (AppUtils.isNetworkConnected(mContext))
                            {
                                AppUtils.showDialogAlertLogout(getActivity(),mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                            break;
                        default:
                            break;
                    }
                }

            }
            else
            {
                // Left user
                switch (position) {
                    case 0://go to app settings
                        PreferenceManager.setGoToSettings(mContext, "1");
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        mContext.startActivity(intent);
                        break;
                    case 1://terms of service
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent mintent = new Intent(mContext, TutorialActivity.class);
                        mintent.putExtra(TYPE, 0);
                        mContext.startActivity(mintent);
                        break;
                    case 4://change password
                        if (AppUtils.isNetworkConnected(mContext)) {
                            showChangePasswordAlert();
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                        }
                        break;
                    case 5:
                        System.out.println("cases 555555");
                        if (AppUtils.isNetworkConnected(mContext)) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.not_available_limitted_access), R.drawable.exclamationicon, R.drawable.round);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                        }
                        break;
                    case 6:
                        if (AppUtils.isNetworkConnected(mContext))
                        {
                            AppUtils.showDialogAlertLogout(getActivity(),mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void showChangePasswordAlert()
    {
        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_changepassword);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        text_currentpswd = dialog.findViewById(R.id.text_currentpassword);
        newpassword = dialog.findViewById(R.id.text_currentnewpassword);
        confirmpassword = dialog.findViewById(R.id.text_confirmpassword);

        Button dialogSubmitButton = dialog
                .findViewById(R.id.btn_changepassword);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      AppUtils.hideKeyboard(mContext, newpassword);
                                                      AppUtils.hideKeyboard(mContext, text_currentpswd);
                                                      AppUtils.hideKeyboard(mContext, confirmpassword);
                                                      if (text_currentpswd.getText().toString().trim().length() == 0) {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_current_password), R.drawable.infoicon, R.drawable.round);

                                                      } else if (newpassword.getText().toString().trim().length() == 0) {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_new_password), R.drawable.infoicon, R.drawable.round);

                                                          //newpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (confirmpassword.getText().toString().trim().length() == 0) {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_confirm_password), R.drawable.infoicon, R.drawable.round);

                                                          //confirmpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (!newpassword.getText().toString().trim().equals(confirmpassword.getText().toString().trim())) {
                                                          //confirmpassword.setError(mContext.getResources().getString(R.string.password_mismatch));
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.password_mismatch), R.drawable.infoicon, R.drawable.round);

                                                      } else {
                                                          if (AppUtils.isNetworkConnected(mContext)) {
                                                              callChangePasswordAPI(URL_CHANGEPSAAWORD);
                                                          } else {
                                                              AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                          }
                                                      }
                                                  }
                                              }

        );

        Button dialogCancel = dialog.findViewById(R.id.btn_cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener()

                                        {
                                            @Override
                                            public void onClick(View v) {
                                                AppUtils.hideKeyboard(mContext, newpassword);
                                                AppUtils.hideKeyboard(mContext, text_currentpswd);
                                                AppUtils.hideKeyboard(mContext, confirmpassword);

                                                dialog.dismiss();
                                            }
                                        }

        );
        dialog.show();
    }

    private void callChangePasswordAPI(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "users_id", "current_password", "new_password", "email", "deviceid", "devicetype"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext),
                text_currentpswd.getText().toString(),
                newpassword.getText().toString(), PreferenceManager.getUserEmail(mContext),
                FirebaseInstanceId.getInstance().getToken(), "2"};

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
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

                            dialog.dismiss();

                            showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.succ_pswd), R.drawable.tick, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("305")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.incrct_pswd), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equals("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email_normal), R.drawable.infoicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        callChangePasswordAPI(URL_CHANGEPSAAWORD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callChangePasswordAPI(URL_CHANGEPSAAWORD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callChangePasswordAPI(URL_CHANGEPSAAWORD);

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

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }


    public void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                PreferenceManager.setUserId(activity, "");
                dialog.dismiss();
                Intent mIntent = new Intent(activity, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(mIntent);
            }
        });

        dialog.show();

    }

    public static void showSettingUserDetail(String URL)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {"access_token", "user_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};

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
                            JSONObject respObj = secobj.getJSONObject(JTAG_RESPONSE_ARRAY);
                            // PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USER_ID));
                            String userId=respObj.optString(JTAG_USER_ID);
                            PreferenceManager.setPhoneNo(mContext, respObj.optString(JTAG_PHONE_NO));
                            PreferenceManager.setFullName(mContext, respObj.optString("name"));
                            PreferenceManager.setCalenderPush(mContext, respObj.optString("calenderpush"));
                            PreferenceManager.setEmailPush(mContext, respObj.optString("emailpush"));
                            PreferenceManager.setMessageBadge(mContext, respObj.optString("messagebadge"));
                            PreferenceManager.setCalenderBadge(mContext, respObj.optString("calenderbadge"));
                            JSONObject respObjAppFeature = secobj.optJSONObject("app_feature");
                            PreferenceManager.setTimeTable(mContext, respObjAppFeature.optString("timetable"));
                            PreferenceManager.setSafeGuarding(mContext, respObjAppFeature.optString("safeguarding"));
                            PreferenceManager.setAttendance(mContext, respObjAppFeature.optString("attendance"));
                            if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                            {
                                if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
                                {
                                    //user is not alumini
                                    if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                                    {
                                        // user is not applicant
                                        if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                                        {
                                            // current user with report mail merge enable
                                            if (!PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                                            {
                                                // data collection trigger available
                                                isRegUser = true;
                                                mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArrayDataCollection));
                                            }
                                            else {
                                                // data collection trigger not available
                                                isRegUser = true;
                                                mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                            }
                                        }
                                        else
                                        {
                                            // current user with report mail merge enable
                                            isRegUser = true;
                                            mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                        }
                                    }
                                    else
                                    {
                                        // user is applicant
                                        isRegUser = true;
                                        mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                    }
                                }
                                else
                                {
                                    // user is alumini
                                    isRegUser = true;
                                    mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                }
                            }if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                            {
                                if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
                                {
                                    //user is not alumini
                                    if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                                    {
                                        // user is not applicant
                                        if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                                        {
                                            // current user with report mail merge enable
                                            if (!PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                                            {
                                                // data collection trigger available
                                                isRegUser = true;
                                                mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArrayDataCollection));
                                            }
                                            else {
                                                // data collection trigger not available
                                                isRegUser = true;
                                                mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                            }
                                        }
                                        else
                                        {
                                            // current user with report mail merge enable
                                            isRegUser = true;
                                            mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                        }
                                    }
                                    else
                                    {
                                        // user is applicant
                                        isRegUser = true;
                                        mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                    }
                                }
                                else
                                {
                                    // user is alumini
                                    isRegUser = true;
                                    mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                }
                            }if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                            {
                                if (PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("0"))
                                {
                                    //user is not alumini
                                    if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("0"))
                                    {
                                        // user is not applicant
                                        if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
                                        {
                                            // current user with report mail merge enable
                                            if (!PreferenceManager.getDataCollection(mContext).equalsIgnoreCase("1"))
                                            {
                                                // data collection trigger available
                                                isRegUser = true;
                                                mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArrayDataCollection));
                                            }
                                            else {
                                                // data collection trigger not available
                                                isRegUser = true;
                                                mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                            }
                                        }
                                        else
                                        {
                                            // current user with report mail merge enable
                                            isRegUser = true;
                                            mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                        }
                                    }
                                    else
                                    {
                                        // user is applicant
                                        isRegUser = true;
                                        mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                    }
                                }
                                else
                                {
                                    // user is alumini
                                    isRegUser = true;
                                    mSettingsList.setAdapter(new CustomSettingsAdapter(mContext, mSettingsListArray));
                                }
                            }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }
    public void showDialogAlertTriggerDataCollection(final Activity activity, String msgHead, String msg, int ico, int bgIcon)
    {
        valueTrigger="0";
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_trigger_data_collection);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        RecyclerView checkRecycler=dialog.findViewById(R.id.checkRecycler);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        checkRecycler.setHasFixedSize(true);
        LinearLayoutManager mRead = new LinearLayoutManager(mContext);
        mRead.setOrientation(LinearLayoutManager.VERTICAL);
        checkRecycler.setLayoutManager(mRead);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);
        ArrayList<String>categoryList=new ArrayList<>();
        categoryList.add("All");
        categoryList.add("Student - Contact Details");
        categoryList.add("Student - Passport & visa");
        final ArrayList<TriggerDataModel>mTriggerModelArrayList=new ArrayList<>();
        for (int i=0;i<categoryList.size();i++)
        {
            TriggerDataModel model=new TriggerDataModel();
            model.setCategoryName(categoryList.get(i));
            model.setCheckedCategory(false);
            mTriggerModelArrayList.add(model);
        }
        System.out.println("Trigger data model arraylist size"+mTriggerModelArrayList.size());
        final TriggerRecyclerAdapter mAdapter=new TriggerRecyclerAdapter(mContext,mTriggerModelArrayList);
        checkRecycler.setAdapter(mAdapter);
        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTriggerModelArrayList.get(0).isCheckedCategory())
                {
                    valueTrigger="1";
                }
                else if (mTriggerModelArrayList.get(1).isCheckedCategory() && !mTriggerModelArrayList.get(2).isCheckedCategory() )
                {
                    valueTrigger="2";
                }
//                else if (mTriggerModelArrayList.get(2).isCheckedCategory() && !mTriggerModelArrayList.get(1).isCheckedCategory() && !mTriggerModelArrayList.get(3).isCheckedCategory())
//                {
//                    valueTrigger="3";
//                }
                else if (mTriggerModelArrayList.get(2).isCheckedCategory() && !mTriggerModelArrayList.get(1).isCheckedCategory())
                {
                    valueTrigger="4";
                }
//                else if (mTriggerModelArrayList.get(1).isCheckedCategory() && mTriggerModelArrayList.get(2).isCheckedCategory())
//                {
//                    valueTrigger="5";
//                }
//                else if (mTriggerModelArrayList.get(2).isCheckedCategory() && mTriggerModelArrayList.get(3).isCheckedCategory())
//                {
//                    valueTrigger="6";
//                }
                else if (mTriggerModelArrayList.get(1).isCheckedCategory() && mTriggerModelArrayList.get(2).isCheckedCategory())
                {
                    valueTrigger="7";
                }
                else
                {
                    valueTrigger="0";
                }

                if (valueTrigger.equalsIgnoreCase("0"))
                {
                    Toast.makeText(mContext,  "Please select any trigger type before confiming", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        dialog.dismiss();
                        callDataTriggerApi(URL_TRIGGER_USER,valueTrigger);
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }

                    // Toast.makeText(mContext,  "Reached"+valueTrigger, Toast.LENGTH_SHORT).show();
                }

            }
        });
        checkRecycler.addOnItemTouchListener(new RecyclerItemListener(mContext, checkRecycler,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (position==0)
                        {
                            if (mTriggerModelArrayList.get(position).isCheckedCategory())
                            {
                                mTriggerModelArrayList.get(position).setCheckedCategory(false);
                            }
                            else
                            {
                                mTriggerModelArrayList.get(0).setCheckedCategory(true);
                                mTriggerModelArrayList.get(1).setCheckedCategory(false);
                                mTriggerModelArrayList.get(2).setCheckedCategory(false);
                            }
                        }
                        else
                        {
                            if(mTriggerModelArrayList.get(position).isCheckedCategory())
                            {
                                mTriggerModelArrayList.get(position).setCheckedCategory(false);
                            }
                            else
                            {
                                mTriggerModelArrayList.get(position).setCheckedCategory(true);
                                mTriggerModelArrayList.get(0).setCheckedCategory(false);
                            }
                            int j=0;

                            for (int i=1;i<mTriggerModelArrayList.size();i++)
                            {
                                if (mTriggerModelArrayList.get(i).isCheckedCategory())
                                {
                                    j=j+1;
                                }
                            }
                            if (j==2)
                            {
                                mTriggerModelArrayList.get(0).setCheckedCategory(true);
                                mTriggerModelArrayList.get(1).setCheckedCategory(false);
                                mTriggerModelArrayList.get(2).setCheckedCategory(false);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            //AppController.getInstance().trackScreenView("Settings Screen" +" " +PreferenceManager.getUserEmail(mContext)+ Calendar.getInstance().getTime());
            AppController.getInstance().trackScreenView("Settings Screen Fragment. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");
        }
    }
    public static void callDataTriggerApi(String URL, final String triggerType)
    {
        System.out.println("Trigger type"+triggerType);
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);

        String[] name = {"access_token", "user_ids","trigger_type"};

        final String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext),triggerType};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response for trigger" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303"))
                        {
                            showDataSuccess(mContext, "Alert","\"Update Account Details\" will start next time the Parent App is opened.", R.drawable.exclamationicon, R.drawable.round);

                           // Toast.makeText(mContext,  "Data collection for the selected type has been triggered", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        callDataTriggerApi(URL_TRIGGER_USER,triggerType);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callDataTriggerApi(URL_TRIGGER_USER,triggerType);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callDataTriggerApi(URL_TRIGGER_USER,triggerType);
                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }


    private static void showDataSuccess(Context mContext, String msgHead, String msg, int ico, int bgIcon)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(R.drawable.round);
        icon.setImageResource(R.drawable.exclamationicon);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText("\"Update Account Details\" will start next time the Parent App is opened.");
        textHead.setText("Alert");

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
}