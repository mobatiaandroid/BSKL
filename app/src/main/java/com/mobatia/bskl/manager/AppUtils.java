package com.mobatia.bskl.manager;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.home.model.KinDetailsModel;
import com.mobatia.bskl.activity.login.LoginActivity;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NameValueConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.mobatia.bskl.constants.URLConstants.POST_APITOKENURL;

/**
 * Created by mobatia on 25/06/18.
 */

public class AppUtils implements JSONConstants, URLConstants, NameValueConstants, StatusConstants {
    private static GetAccessTokenInterface getTokenIntrface;
    private static Context mContext;
    private static int count = 0;

    public interface GetAccessTokenInterface {
        void getAccessToken();
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public static String durationInSecondsToString(int sec) {
        int hours = sec / 3600;
        int minutes = (sec / 60) - (hours * 60);
        int seconds = sec - (hours * 3600) - (minutes * 60);
        String formatted = String.format("%d:%02d:%02d", hours, minutes,
                seconds);
        return formatted;
    }

    public static String dateParsingToDdMmmYyyy(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static String dateParsingToTime(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }
    public static String dateParsingToDdMmYyyy(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static String dateParsingToDdMonthYyyy(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static String dateConversionY(String inputDate) {
        String mDate = "";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    public static String dateConversionMMM(String inputDate) {
        String mDate = "";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    public static String dateConversionYToD(String inputDate) {
        String mDate = "";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterFullDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    public static String getCurrentDateToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        String today = dateFormat.format(calendar.getTime());
        return today;
    }

    public static boolean isEditTextFocused(Activity context) {


        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = context.getCurrentFocus();
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         */
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            return true;

        } else {
            return false;

        }


		/*InputMethodManager imm = (InputMethodManager) context

				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isAcceptingText()) {

			return true;


		}
		else
			return false;*/


    }

    public static void setErrorForEditTextNull(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                edt.setError(null);

            }

        });
    }


    public static void setErrorForEditText(EditText edt, String msg) {
        edt.setError(msg);
    }


    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showAlert(final Activity activity, String message,
                                 String okBtnTitle, String cancelBtnTitle, boolean okBtnVisibility) {
        // custom dialog
        final Dialog dialog = new Dialog(activity, R.style.NewDialog);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image, button
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        int sdk = Build.VERSION.SDK_INT;

        Button dialogCancelButton = dialog
                .findViewById(R.id.dialogButtonCancel);
        dialogCancelButton.setText(cancelBtnTitle);
//		if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//			dialogCancelButton.setBackgroundDrawable(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		} else {
//			dialogCancelButton.setBackground(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		}
        // if button is clicked, close the custom dialog
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOkButton = dialog
                .findViewById(R.id.dialogButtonOK);
        dialogOkButton.setVisibility(View.GONE);
        dialogOkButton.setText(okBtnTitle);
        if (okBtnVisibility) {
            dialogOkButton.setVisibility(View.VISIBLE);
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        }

        dialog.show();
    }
    public static void showSingleButtonAlert(final Activity activity, String message,
                                 String okBtnTitle, boolean okBtnVisibility) {
        // custom dialog
        final Dialog dialog = new Dialog(activity, R.style.NewDialog);
        dialog.setContentView(R.layout.custom_single_alert_dialog);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image, button
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        Button dialogOkButton = dialog
                .findViewById(R.id.dialogButtonOK);
        dialogOkButton.setVisibility(View.GONE);
        dialogOkButton.setText(okBtnTitle);
        if (okBtnVisibility) {
            dialogOkButton.setVisibility(View.VISIBLE);
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }
    public static Drawable getButtonDrawableByScreenCathegory(Context con,
                                                              int normalStateResID, int pressedStateResID) {
        Drawable state_normal = con.getResources()
                .getDrawable(normalStateResID).mutate();
        Drawable state_pressed = con.getResources()
                .getDrawable(pressedStateResID).mutate();
        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_pressed},
                state_pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                state_normal);
        return drawable;
    }

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {

            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), 0);

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
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});

        dialog.show();

    }

    public static boolean checkInternet(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connec.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String replace(String str) {
        return str.replaceAll(" ", "%20");
    }

    public static String replaceam(String str) {
        return str.replaceAll("am", " ");
    }

    public static String replacepm(String str) {
        return str.replaceAll("pm", " ");
    }

    public static String replaceAM(String str) {
        return str.replaceAll("AM", " ");
    }

    public static String replacePM(String str) {
        return str.replaceAll("PM", " ");
    }

    public static String replaceamdot(String str) {
        return str.replaceAll("a.m.", " ");
    }

    public static String replacepmdot(String str) {
        return str.replaceAll("p.m.", " ");
    }

    public static String replaceAMDot(String str) {
        return str.replaceAll("A.M.", " ");
    }

    public static String replacePMDot(String str) {
        return str.replaceAll("P.M.", " ");
    }

    public static String replaceAmdot(String str) {
        return str.replaceAll("a.m.", "am");
    }

    public static String replacePmdot(String str) {
        return str.replaceAll("p.m.", "pm");
    }

    public static String replaceYoutube(String str) {
        return str.replaceAll("https://www.youtube.com/embed/", "");
    }

    public static String replacePdf(String str) {
        return str.replaceAll("http://mobicare2.mobatia.com/nais/media/images/", "");
    }

    /*public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
*/
    public static void showAlertFinish(final Activity activity, String message,
                                       String okBtnTitle, String cancelBtnTitle, boolean okBtnVisibility) {
        // custom dialog
        final Dialog dialog = new Dialog(activity, R.style.NewDialog);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image, button
        TextView text = dialog.findViewById(R.id.text);
        text.setText(message);
        int sdk = Build.VERSION.SDK_INT;

        Button dialogCancelButton = dialog
                .findViewById(R.id.dialogButtonCancel);
        dialogCancelButton.setText(cancelBtnTitle);

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();

            }
        });

        Button dialogOkButton = dialog
                .findViewById(R.id.dialogButtonOK);
        dialogOkButton.setVisibility(View.GONE);
        dialogOkButton.setText(okBtnTitle);
        if (okBtnVisibility) {
            dialogOkButton.setVisibility(View.VISIBLE);
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        }

        dialog.show();
    }

    public static void editTextValidationAlert(EditText edtText,
                                               String message, Context context) {
        edtText.setError(message);
    }

    public static void setEdtTextTextChangelistener(final EditText edtTxt,
                                                    Context context) {
        edtTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                edtTxt.setError(null);

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtTxt.setError(null);

            }
        });
    }

    public static void showDialogAlertLogout(final Activity activity,Context context, String msgHead, String msg, int ico, int bgIcon) {
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
                if (PreferenceManager.getUserId(activity).equalsIgnoreCase("")) {
                    PreferenceManager.setUserId(activity, "");
                    dialog.dismiss();
                    Intent mIntent = new Intent(activity, LoginActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(mIntent);
                } else {
                    callLogoutApi(activity,context, dialog);
                }
            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private static void callLogoutApi(final Activity mActivity,Context context, final Dialog dialog) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_LOGOUT);
        String[] name = {"access_token", "users_id", JTAG_DEVICE_iD, JTAG_DEVICE_tYPE};
        String[] value = {PreferenceManager.getAccessToken(mActivity), PreferenceManager.getUserId(mActivity), FirebaseInstanceId.getInstance().getToken(), "2"};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mActivity, 11, name, value, new VolleyWrapper.ResponseListener() {
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
                            NotificationManager nm = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.cancelAll();
                            PreferenceManager.setUserId(mActivity, "");
                            PreferenceManager.setDataCollection(mActivity,"0");
//                            SharedPreferences.Editor editor = mActivity.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
//                            editor.putString("data_collection_flag", "0");
//                            editor.apply();
                            System.out.println("insurance array size in logout1");
                            AppController.kinArrayShow.clear();
                            System.out.println("insurance array size in logout2");
                            AppController.kinArrayPass.clear();
                            System.out.println("insurance array size in logout3");
                            if(PreferenceManager.getOwnDetailArrayList("OwnContact",context)==null ||PreferenceManager.getOwnDetailArrayList("OwnContact",context).size()==0 )
                            {

                            }
                            else
                            {
                                ArrayList<OwnContactModel> mOwnArrayList=PreferenceManager.getOwnDetailArrayList("OwnContact",context);
                                System.out.println("insurance array size in logout4"+PreferenceManager.getOwnDetailArrayList("OwnContact",context).size());
                                mOwnArrayList.clear();
                                System.out.println("insurance array size in logout5");
                                PreferenceManager.saveOwnDetailArrayList(mOwnArrayList,"OwnContact",context);
                            }

                            if (PreferenceManager.getKinDetailsArrayListShow(context)==null ||PreferenceManager.getKinDetailsArrayListShow(context).size()==0)
                            {

                            }
                            else
                            {
                                System.out.println("insurance array size in logout6");
                                ArrayList<KinModel>mKinShowArray=PreferenceManager.getKinDetailsArrayListShow(context);
                                System.out.println("insurance array size in logout7");
                                mKinShowArray.clear();
                                System.out.println("insurance array size in logout8");
                                PreferenceManager.saveKinDetailsArrayListShow( mKinShowArray,context);
                                System.out.println("insurance array size in logout9");
                                ArrayList<KinModel>mKinPassArray=PreferenceManager.getKinDetailsArrayList(context);
                                System.out.println("insurance array size in logout10");
                                mKinPassArray.clear();
                                System.out.println("insurance array size in logout11");
                                PreferenceManager.saveKinDetailsArrayList( mKinPassArray,context);
                            }
                            if (PreferenceManager.getInsuranceDetailArrayList(context)==null ||PreferenceManager.getInsuranceDetailArrayList(context).size()==0)
                            {

                            }
                            else
                            {
                                System.out.println("insurance array size in logout12");
                                ArrayList<InsuranceDetailModel>mInsuranceDataArray=PreferenceManager.getInsuranceDetailArrayList(context);
                                System.out.println("insurance array size in logout13");
                                mInsuranceDataArray.clear();
                                System.out.println("insurance array size in logout14");
                                PreferenceManager.saveInsuranceDetailArrayList(mInsuranceDataArray,context);
                                System.out.println("insurance array size in logout"+ PreferenceManager.getInsuranceDetailArrayList(context).size());
                            }

                            if (PreferenceManager.getPassportDetailArrayList(context)==null ||PreferenceManager.getPassportDetailArrayList(context).size()==0)
                            {

                            }
                            else
                            {
                                System.out.println("insurance array size in logout15");
                                ArrayList<PassportDetailModel>mPassportDataArray=PreferenceManager.getPassportDetailArrayList(context);
                                System.out.println("insurance array size in logout");
                                mPassportDataArray.clear();
                                System.out.println("insurance array size in logout16");
                                PreferenceManager.savePassportDetailArrayList(mPassportDataArray,context);
                            }
                            if (PreferenceManager.getInsuranceStudentList(context)==null ||PreferenceManager.getInsuranceStudentList(context).size()==0)
                            {

                            }
                           else
                            {
                                System.out.println("insurance array size in logout17");
                                ArrayList<StudentModelNew>mStudentArray=PreferenceManager.getInsuranceStudentList(context);
                                mStudentArray.clear();
                                PreferenceManager.saveInsuranceStudentList(mStudentArray,context);

                            }

                            Intent mIntent = new Intent(mActivity, LoginActivity.class);
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mActivity.startActivity(mIntent);


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        dialog.dismiss();
                        AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mActivity, new GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        callLogoutApi(mActivity,context, dialog);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mActivity, new GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callLogoutApi(mActivity,context, dialog);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mActivity, new GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callLogoutApi(mActivity,context, dialog);

                    } else {
                        dialog.dismiss();
                        AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                dialog.dismiss();
                AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    public static void postInitParams(final Context mContext, GetAccessTokenInterface getTokenInterface) {
        getTokenIntrface = getTokenInterface;

        final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
        String name[] = {NAME_GRANT_TYPE, NAME_CLIENT_ID, NAME_CLIENT_SECRET, NAME_USERNAME, NAME_PASSWORD};
        String values[] = {VALUE_GRANT_TYPE, VALUE_CLIENT_ID, VALUE_CLIENT_SECRET, VALUE_USERNAME, VALUE_PASSWORD};

        manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("postInitParams " + successResponse);
                if (successResponse != null) {
                    try {
                        JSONObject rootObject = new JSONObject(successResponse);

                        if (rootObject != null) {
                            String acccessToken = rootObject.optString(JTAG_ACCESSTOKEN);
                            PreferenceManager.setAccessToken(mContext, acccessToken);
                            deviceRegistration(mContext);
//							if (PreferenceManager.getUserId(mContext).equals("")) {
//
//								deviceRegistration(mContext);//changed on 22-09-2017
//							}
                        } else {
                            // CustomStatusDialog(RESPONSE_CODE_NULL);
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
    }

    public static void deviceRegistration(final Context mContext) {

        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_DEVICE_REGISTRATION);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_DEVICE_ID, JTAG_DEVICE_TYPE};
            String[] value = {PreferenceManager.getAccessToken(mContext), FirebaseInstanceId.getInstance().getToken(), "2"};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                            if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

                                } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    deviceRegistration(mContext);

                                }
                            } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                AppUtils.postInitParam(mContext, new GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                deviceRegistration(mContext);

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void postInitParam(final Context mContext, GetAccessTokenInterface getTokenInterface) {
        getTokenIntrface = getTokenInterface;

        final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
        String name[] = {NAME_GRANT_TYPE, NAME_CLIENT_ID, NAME_CLIENT_SECRET, NAME_USERNAME, NAME_PASSWORD};
        String values[] = {VALUE_GRANT_TYPE, VALUE_CLIENT_ID, VALUE_CLIENT_SECRET, VALUE_USERNAME, VALUE_PASSWORD};

        manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                if (successResponse != null) {
                    try {
                        JSONObject rootObject = new JSONObject(successResponse);
                        System.out.println("postInitParamCalling " + successResponse);

                        if (rootObject != null) {
                            String acccessToken = rootObject.optString(JTAG_ACCESSTOKEN);
                            System.out.println("acccessTokenpostInitParamCalling: " + acccessToken);
                            PreferenceManager.setAccessToken(mContext, acccessToken);
                        } else {
                            // CustomStatusDialog(RESPONSE_CODE_NULL);
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
    }

    public static void hideKeyboard(Context context, EditText edtText) {
        if (edtText != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtText.getWindowToken(), 0);
        }
    }

    public static void getToken(Context context, GetTokenSuccess tokenobj) {
        mContext = context;
        AppUtils apputils = new AppUtils();
        Securitycode accesstoken = apputils.new Securitycode(context, tokenobj);
        accesstoken.getToken();
    }

    public interface GetTokenSuccess {
        void tokenrenewed();
    }

    private class Securitycode {
        private Context mContext;
        private GetTokenSuccess getTokenObj;

        public Securitycode(Context context, GetTokenSuccess getTokenObj) {
            this.mContext = context;
            this.getTokenObj = getTokenObj;
        }

        public void getToken() {


            final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
            String name[] = {NAME_GRANT_TYPE, NAME_CLIENT_ID, NAME_CLIENT_SECRET, NAME_USERNAME, NAME_PASSWORD};
            String values[] = {VALUE_GRANT_TYPE, VALUE_CLIENT_ID, VALUE_CLIENT_SECRET, VALUE_USERNAME, VALUE_PASSWORD};

            manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            System.out.println("security token:" + successResponse);
                            if (rootObject != null) {
                                String acccessToken = rootObject.optString(JTAG_ACCESSTOKEN);
                                PreferenceManager.setAccessToken(mContext, acccessToken);
                            } else {
                                // CustomStatusDialog(RESPONSE_CODE_NULL);
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

        }

    }

    /****************End Date*************/

    /************** Force Update Dialog********/
    public static void showDialogAlertUpdate(final Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_update_version);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        dialog.show();

    }

    public static String getVersionInfo(Context mContext) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
//    TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
//    textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }


    public static String timeParsingToAmPm(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static String timeParsingTo12Hour(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static String timeParsingToHours(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("HH", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static String timeParsingToMinutes(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("mm", Locale.ENGLISH);
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }

    public static void hideKeyboardOnTouchOutside(View view,
                                                  final Context context, final EditText edtText) {
        if (!(view instanceof EditText))
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyBoard(context);
                    return false;
                }
            });
    }

}

