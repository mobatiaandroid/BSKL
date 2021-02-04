package com.mobatia.bskl.fragment.safeguarding;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.absence.model.StudentModel;
import com.mobatia.bskl.fragment.safeguarding.adapter.SafeGuardingAdapter;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by krishnaraj on 12/07/18.//////////////////svn////////////////////////////
 */

public class SafeGuardingFragment extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    private View mRootView;
    private static Context mContext;

    TextView currentDateTextView;
    static RecyclerView mStudentRecyclerView;
    static View viewBottom;
    Intent mIntent;
    private String mTitle;
    private String mTabId;
    static ArrayList<StudentModel> mStudentAttendanceList;
    static RelativeLayout noDataRelative;
    TextView noDataTxt;
    static SafeGuardingAdapter mSafeGuardingAdapter;
    LinearLayout linearMain;
    public SafeGuardingFragment() {

    }

    public SafeGuardingFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // reset();
        mRootView = inflater.inflate(R.layout.fragment_safe_guarding_layout, container,
                false);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        ImageView imageButton2 = actionBar.getCustomView().findViewById(R.id.action_bar_forward);
        imageButton2.setImageResource(R.drawable.settings);
        headerTitle.setText(SAFE_GUARDING);
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        initialiseUI();

//        String myFormatCalender = "EEE dd MMMM yyyy";
        String dayEnd = (String) DateFormat.format("dd", Calendar.getInstance().getTime()); // 20
        SimpleDateFormat sdfcalender;

        if (dayEnd.endsWith("1") && !dayEnd.endsWith("11")) {
            sdfcalender = new SimpleDateFormat("EEE dd'st' MMMM yyyy", Locale.ENGLISH);

        } else if (dayEnd.endsWith("2") && !dayEnd.endsWith("12")) {
            sdfcalender = new SimpleDateFormat("EEE dd'nd' MMMM yyyy", Locale.ENGLISH);

        } else if (dayEnd.endsWith("3") && !dayEnd.endsWith("13"))
            sdfcalender = new SimpleDateFormat("EEE dd'rd' MMMM yyyy", Locale.ENGLISH);
        else
            sdfcalender = new SimpleDateFormat("EEE dd'th' MMMM yyyy", Locale.ENGLISH);

        String dateString = sdfcalender.format(Calendar.getInstance().getTime());
        currentDateTextView.setText(dateString);
        if (AppUtils.isNetworkConnected(mContext)) {
            callSafeGuarding();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
//        if (PreferenceManager.getIsFirstTimeInNotification(mContext)) {
//            PreferenceManager.setIsFirstTimeInNotification(mContext, false);
//            Intent mintent = new Intent(mContext, NotificationInfoActivity.class);
//            mintent.putExtra("type", 1);
//            mContext.startActivity(mintent);
//        }

        return mRootView;
    }


    public static void callSafeGuarding() {
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_GET_SAFE_GUARDING_LIST);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_IDS};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.v("Response ", "SafeGuarding: " + successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.optString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.optJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.optString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
                                    JSONArray attendance_data = secobj.optJSONArray("attendance_data");
                                    mStudentAttendanceList = new ArrayList<>();
                                    for (int i = 0; i < attendance_data.length(); i++) {
                                        JSONObject mJsonObject = attendance_data.optJSONObject(i);
                                        StudentModel mStudentAttendanceModel = new StudentModel();
                                        mStudentAttendanceModel.setAbscenceId(mJsonObject.optString("id"));
                                        mStudentAttendanceModel.setAbsenceCodeId(mJsonObject.optString("absenceCodeId"));
                                        mStudentAttendanceModel.setIsPresent(mJsonObject.optString("isPresent"));
                                        mStudentAttendanceModel.setDate(mJsonObject.optString("date"));//last updated date
                                        mStudentAttendanceModel.setSchoolId(mJsonObject.optString("schoolId"));
                                        mStudentAttendanceModel.setIsLate(mJsonObject.optString("isLate"));
                                        mStudentAttendanceModel.setmName(mJsonObject.optString("student_name"));
                                        mStudentAttendanceModel.setmClass(mJsonObject.optString("class"));
                                        mStudentAttendanceModel.setmSection(mJsonObject.optString("form"));
                                        mStudentAttendanceModel.setmId(mJsonObject.optString("stud_id"));
                                        mStudentAttendanceModel.setmPhoto(mJsonObject.optString("photo"));
                                        mStudentAttendanceModel.setAlumini("0");
                                        mStudentAttendanceModel.setStatus(mJsonObject.optString("status"));
                                        mStudentAttendanceModel.setParent_id(mJsonObject.optString("parent_id"));
                                        mStudentAttendanceModel.setParent_name(mJsonObject.optString("parent_name"));
                                        mStudentAttendanceModel.setApp_updated_on(mJsonObject.optString("app_updated_on"));//reported time
                                        mStudentAttendanceModel.setRegistrationComment(mJsonObject.optString("registrationComment"));//reported time

                                        if(mJsonObject.optString("isPresent").equalsIgnoreCase("0")){
                                            mStudentAttendanceModel.setAbRegister("");
                                        }
                                        else{
                                            mStudentAttendanceModel.setAbRegister("Present");
                                        }

                                        if (mJsonObject.optString("isPresent").equalsIgnoreCase("0")) {
                                            String absenceCodedId = mJsonObject.optString("absenceCodeId");
                                            String code = "";
                                            JSONArray codesArray = secobj.optJSONArray("codes");
                                            for (int j = 0; j < codesArray.length(); j++) {
                                                JSONObject mCodesArrayObject = codesArray.optJSONObject(j);
                                                if (absenceCodedId.equalsIgnoreCase(mCodesArrayObject.optString("absenceCodeId"))) {
                                                    mStudentAttendanceModel.setAbRegister(mCodesArrayObject.optString("name"));
                                                    code = mCodesArrayObject.optString("code");
                                                    break;
                                                }
                                            }
                                            if (mJsonObject.optString("status").equalsIgnoreCase("") && code.equalsIgnoreCase("A")) {
                                                mStudentAttendanceModel.setViewType("3");

                                            } else if (!(mJsonObject.optString("status").equalsIgnoreCase("")) && code.equalsIgnoreCase("A")) {
                                                mStudentAttendanceModel.setViewType("2");
                                            } else {
                                                mStudentAttendanceModel.setViewType("1");

                                            }
                                        } else {
                                            mStudentAttendanceModel.setViewType("1");

                                        }
                                        mStudentAttendanceList.add(mStudentAttendanceModel);
                                    }
                                     mSafeGuardingAdapter = new SafeGuardingAdapter(mStudentAttendanceList, mContext);
                                    mSafeGuardingAdapter.notifyDataSetChanged();
                                    mStudentRecyclerView.setAdapter(mSafeGuardingAdapter);
                                    if (mStudentAttendanceList.size()<=0)
                                    {
                                        noDataRelative.setVisibility(View.VISIBLE);
                                        viewBottom.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        noDataRelative.setVisibility(View.GONE);
                                        viewBottom.setVisibility(View.VISIBLE);

                                    }
                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || status_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callSafeGuarding();

                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);

                                }

                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callSafeGuarding();

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public static void reset() {
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_GET_SAFEGUARD_RESET);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_ID};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {

                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.optString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.optJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.optString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {


                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || status_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {

                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);

                                }

                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                //reset();
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    private void initialiseUI() {

        currentDateTextView = mRootView.findViewById(R.id.currentDateTextView);
         noDataRelative = mRootView.findViewById(R.id.noDataRelative);
         noDataTxt = mRootView.findViewById(R.id.noDataTxt);
         linearMain = mRootView.findViewById(R.id.linearMain);
        linearMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mStudentRecyclerView = mRootView.findViewById(R.id.mStudentRecyclerView);
        viewBottom = mRootView.findViewById(R.id.viewBottom);
//        mStudentRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, OrientationHelper.VERTICAL, false);

        mStudentRecyclerView.setLayoutManager(mLayoutManager);
        mStudentRecyclerView.getLayoutManager().setMeasurementCacheEnabled(false);

    }


    public static void submitAbsence(final String attendance_id,final String student_id, final String status, final String details) {
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_POST_SUBMIT_SAFE_GUARDING);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_IDS, "student_id", "attendance_id", "status", "details"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), student_id, attendance_id, status, details};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.v("Response ", "SafeGuarding:Submit: " + successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.optString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.optJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.optString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
                                    showDialogSuccess((Activity) mContext, "Alert", "Successfully reported.", R.drawable.exclamationicon, R.drawable.round);

                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || status_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    submitAbsence(attendance_id,student_id, status, details);

                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);

                                }

                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                submitAbsence(attendance_id,student_id, status, details);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
//    public static void submitAbsenceFromLeaveRequest(final Activity activity,final String attendance_id, final String status, final String details) {
//        try {
//
//            final VolleyWrapper manager = new VolleyWrapper(URL_POST_SUBMIT_SAFE_GUARDING);
//            String[] name = {JTAG_ACCESSTOKEN, JTAG_USER_IDS, "attendance_id", "status", "details"};
//            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), attendance_id, status, details};
//            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
//
//                @Override
//                public void responseSuccess(String successResponse) {
//                    Log.v("Response ", "SafeGuarding:Submit: " + successResponse);
//
//                    if (successResponse != null) {
//                        try {
//                            JSONObject obj = new JSONObject(successResponse);
//                            String response_code = obj.optString(JTAG_RESPONSECODE);
//                            if (response_code.equalsIgnoreCase("200")) {
//                                JSONObject secobj = obj.optJSONObject(JTAG_RESPONSE);
//                                String status_code = secobj.optString(JTAG_STATUSCODE);
//                                if (status_code.equalsIgnoreCase("303")) {
//                                    showDialogSuccess(activity, "Success", mContext.getResources().getString(R.string.succ_leave_submission), R.drawable.tick, R.drawable.round);
//
//                                    callSafeGuarding();
//
//                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || status_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
//                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
//                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
//                                        @Override
//                                        public void getAccessToken() {
//                                        }
//                                    });
//                                    submitAbsenceFromLeaveRequest(activity,attendance_id, status, details);
//
//                                } else {
//                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);
//
//                                }
//
//                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN) ||
//                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
//                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
//                                    @Override
//                                    public void getAccessToken() {
//                                    }
//                                });
//                                submitAbsenceFromLeaveRequest(activity,attendance_id, status, details);
//                            } else {
//                                AppUtils.showDialogAlertDismiss(activity, "Alert", mContext.getResources().getString(R.string.some_error), R.drawable.exclamationicon, R.drawable.round);
//
//                            }
//
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void responseFailure(String failureResponse) {
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void showDialogSuccess(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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
                dialog.dismiss();
                callSafeGuarding();

            }
        });

        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();


        if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Safe Guarding Screen Fragment. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");
        }
        // System.out.println("testinngresume");
    }

}

