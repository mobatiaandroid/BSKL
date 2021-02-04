package com.mobatia.bskl.fragment.absence;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.absence.adapter.StrudentSpinnerAdapter;
import com.mobatia.bskl.fragment.absence.model.StudentModel;
import com.mobatia.bskl.fragment.safeguarding.SafeGuardingFragment;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.mobatia.bskl.manager.PreferenceManager.getUserId;

/**
 * Created by krishnaraj on 27/08/18.
 */

public class LeaveRequestSubmissionSafeguardActivity extends AppCompatActivity implements URLConstants, JSONConstants, NaisClassNameConstants {
    Button submitBtn;


    Context mContext;
    EditText enterMessage;
    TextView enterStratDate, enterEndDate;
    LinearLayout submitLayout;
    Calendar c;
    int mYear;
    int mMonth;
    int mDay;
    SimpleDateFormat df;
    String formattedDate;
    Calendar calendar;
    String fromDate = "", toDate = "";
    String tomorrowAsString;
    Date sdate, edate;
    long elapsedDays;
    String stud_img="";
    ImageView studImg;
    SimpleDateFormat outputFormats;
    Bundle extras;
    String studentNameStr = "";
    String studentClassStr = "";
    String studentIdStr = "";
    String studentIdForSafe = "";
    String attendance_id= "", status = "";
    TextView studentName;
    LinearLayout mStudentSpinner;

    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.leave_request_submission_safeguard_activity);
        mContext = this;
        initUI();
    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            studentNameStr = extras.getString("studentName");
            studentIdStr = extras.getString("studentId");
            studentIdForSafe = extras.getString("studentId");
            stud_img=extras.getString("studentImage");
            attendance_id=extras.getString("attendance_id");
            status=extras.getString("status");
            studentsModelArrayList = (ArrayList<StudentModel>) extras
                    .getSerializable("StudentModelArray");
        }
        calendar = Calendar.getInstance();

        outputFormats =
                new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        mStudentSpinner = findViewById(R.id.studentSpinner);
        studentName = findViewById(R.id.studentName);
        studImg= findViewById(R.id.studImg);
        enterMessage = findViewById(R.id.enterMessage);
        enterStratDate = findViewById(R.id.enterStratDate);
        enterStratDate.setText("Date of Absence "+AppUtils.dateConversionY(AppUtils.getCurrentDateToday()));
      /*  if (!(attendance_id.equalsIgnoreCase("")) && !(status.equalsIgnoreCase("")))
        {
            enterStratDate.setText(AppUtils.dateConversionY(AppUtils.getCurrentDateToday()));
            enterStratDate.setEnabled(false);
        }*/
        //enterEndDate = findViewById(R.id.enterEndDate);
        submitLayout = findViewById(R.id.submitLayout);
        submitBtn = findViewById(R.id.submitBtn);



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();
        Toolbar toolbar=(Toolbar)view.getParent();
        toolbar.setContentInsetsAbsolute(0,0);
        TextView headerTitle = findViewById(R.id.headerTitle);
        ImageView logoClickImgView = findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = findViewById(R.id.action_bar_forward);
        ImageView action_bar_back = findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        action_bar_forward.setVisibility(View.INVISIBLE);
        headerTitle.setText(ABSENCE);
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentsModelArrayList.size() > 0) {
                    //showSocialmediaList(studentsModelArrayList);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
        studentName.setText(studentNameStr);
        if (!(stud_img.equals(""))) {

            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
        }
        else

        {

            studImg.setImageResource(R.drawable.boy);
        }

        PreferenceManager.setLeaveStudentId(mContext, studentIdStr);

//studentClass.setText(studentClassStr);
        enterMessage.clearFocus();
        enterMessage.setFocusable(false);

        enterMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                enterMessage.setFocusableInTouchMode(true);

                return false;
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isEditTextFocused(LeaveRequestSubmissionSafeguardActivity.this)) {
                    AppUtils.hideKeyBoard(mContext);
                }
               /* if (enterStratDate.getText().toString().equals("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_startdate), R.drawable.infoicon, R.drawable.round);
                } else if (enterEndDate.getText().toString().equals("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_enddate), R.drawable.infoicon, R.drawable.round);
                }*/
                //else
                    if (enterMessage.getText().toString().trim().equals("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_reason), R.drawable.infoicon, R.drawable.round);

                }
                    else {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        submitLeave(URL_GET_LEAVEREQUESTSUBMISSION);
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
            }
        });
/*
        enterStratDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isEditTextFocused(LeaveRequestSubmissionSafeguardActivity.this)) {
                    AppUtils.hideKeyBoard(mContext);
                }
                enterMessage.setFocusable(false);
                enterMessage.clearFocus();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date strDate = null;
                String[] items1 = AppUtils.getCurrentDateToday().split("-");
                String date1 = items1[0];
                //if()
                String month = items1[1];
                String year = items1[2];
                try {
                    strDate = sdf.parse(AppUtils.getCurrentDateToday());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog strDatePicker = new DatePickerDialog(mContext, startDate, Integer.parseInt(year),
                        Integer.parseInt(month) - 1, Integer.parseInt(date1));
                strDatePicker.getDatePicker().setMinDate(strDate.getTime());
                strDatePicker.show();
            }
        });

        enterEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppUtils.isEditTextFocused(LeaveRequestSubmissionSafeguardActivity.this)) {
                    AppUtils.hideKeyBoard(mContext);
                }
                enterMessage.setFocusable(false);
                enterMessage.clearFocus();
                if (enterStratDate.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please select first day of absence.", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date strDate = null;
                    String[] items1 = AppUtils.getCurrentDateToday().split("-");
                    String date1 = items1[0];
                    //if()
                    String month = items1[1];
                    String year = items1[2];
                    try {
                        strDate = sdf.parse(AppUtils.getCurrentDateToday());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DatePickerDialog endDatePicker = new DatePickerDialog(mContext, endDate, Integer.parseInt(year),
                            Integer.parseInt(month) - 1, Integer.parseInt(date1));
                    endDatePicker.getDatePicker().setMinDate(strDate.getTime());
                    endDatePicker.show();
                }

            }
        });*/
    }


    private void submitLeave(String URL_API) {

        String Date = AppUtils.dateConversionY(AppUtils.getCurrentDateToday());
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_API);
        String[] name = {"access_token", "student_id", "users_id", "from_date", "to_date", "reason"};

        String[] value = {PreferenceManager.getAccessToken(mContext), studentIdStr, getUserId(mContext),
                Date, Date, enterMessage.getText().toString().trim()};

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("leaverequest" + fromDate);
                System.out.println("todate" + toDate);
                System.out.println("mconyext" + getUserId(mContext));
                System.out.println("studstr" + studentIdStr);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            if (attendance_id.equalsIgnoreCase("") && status.equalsIgnoreCase("")) {
                                showDialogSuccess((Activity) mContext, "Success", getString(R.string.succ_leave_submission), R.drawable.tick, R.drawable.round);
                            }else if (AppUtils.isNetworkConnected(mContext))
                            {
                                showDialogSuccess((Activity) mContext, "Success", getString(R.string.succ_leave_submission), R.drawable.tick, R.drawable.round);
                                SafeGuardingFragment.callSafeGuarding();

                            }
                            else
                            {
                                showDialogSuccess((Activity) mContext, "Success", getString(R.string.succ_leave_submission), R.drawable.tick, R.drawable.round);

                            }
                        } else if (status_code.equals("211")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Already exists", R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equals("313")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Already requested", R.drawable.infoicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.internal_server_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400") || response_code.equalsIgnoreCase("401") || response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        submitLeave(URL_GET_LEAVEREQUESTSUBMISSION);

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

    public void showDialogSuccess(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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
                finish();
            }
        });

        dialog.show();

    }

    DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fromDate = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            if (!toDate.equals("")) {
                SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                try {
                    sdate = dateFormatt.parse(fromDate);
                    edate = dateFormatt.parse(toDate);
                    printDifference(sdate, edate);
                } catch (Exception e) {

                }
            }
            if (elapsedDays < 0 && !toDate.equals("")) {
               // fromDate=AppUtils.dateConversionYToD(enterStratDate.getText().toString());

                AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose first day of absence(date) less than or equal to selected return to school(date)", R.drawable.infoicon, R.drawable.round);
                //break;
            } else {
               // enterStratDate.setText(AppUtils.dateConversionY(fromDate));
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date strDate = sdf.parse(fromDate);
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    formattedDate = df.format(c.getTime());
                    Date endDate = sdf.parse(formattedDate);

                    SimpleDateFormat dateFormatt = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    Date convertedDate = new Date();
//                    try {
//                        convertedDate = dateFormatt.parse(enterStratDate.getText().toString());
//                    }
//                    catch (ParseException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                    calendar.setTime(convertedDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 1);

                    Date tomorrow = calendar.getTime();


                    tomorrowAsString = dateFormatt.format(tomorrow);

                    System.out.println("Tomorrow--" + tomorrowAsString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


    };
    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            toDate = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            if (!toDate.equals("")) {
                SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    sdate = dateFormatt.parse(fromDate);
                    edate = dateFormatt.parse(toDate);
                    printDifference(sdate, edate);
                } catch (Exception e) {

                }
                if (elapsedDays < 0) {
                    //toDate=AppUtils.dateConversionYToD(enterEndDate.getText().toString());

                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose return to school(date) greater than or equal to first day of absence(date)", R.drawable.infoicon, R.drawable.round);

                    //break;
                } else {
                  //  enterEndDate.setText(AppUtils.dateConversionY(toDate));

                }

            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date strDate = sdf.parse(toDate);
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                formattedDate = df.format(c.getTime());
                Date endDate = sdf.parse(formattedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    };

    public void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }

    public void showSocialmediaList(final ArrayList<StudentModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_media_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView socialMediaList = dialog.findViewById(R.id.recycler_view_social_media);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button_new));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button_new));

        }
        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        StrudentSpinnerAdapter studentAdapter = new StrudentSpinnerAdapter(mContext, mStudentArray);
        socialMediaList.setAdapter(studentAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        dialog.dismiss();
                        studentName.setText(mStudentArray.get(position).getmName());

                        /****Rijo added code****/
                        if (!(studentIdForSafe.equalsIgnoreCase(mStudentArray.get(position).getmId())))
                        {

//                            enterStratDate.setText("");
//                            enterStratDate.setEnabled(true);
//                            enterEndDate.setText("");
                            enterMessage.setText("");
                        }
                        else
                        {
                            if (!(attendance_id.equalsIgnoreCase("")) && !(status.equalsIgnoreCase("")))
                            {
//                                enterStratDate.setEnabled(true);
//                                enterStratDate.setText(AppUtils.dateConversionY(AppUtils.getCurrentDateToday()));
//                                enterStratDate.setEnabled(false);
                            }
                            else
                            {
                               // enterStratDate.setEnabled(true);
                            }
                        }
                        /****Rijo added code****/
                        studentIdStr = mStudentArray.get(position).getmId();
                        studentClassStr = mStudentArray.get(position).getmClass();
                        stud_img = mStudentArray.get(position).getmPhoto();
                        PreferenceManager.setLeaveStudentId(mContext, studentIdStr);
                        PreferenceManager.setLeaveStudentName(mContext, mStudentArray.get(position).getmName());

                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        }
                        else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", getUserId(mContext));
            AppController.getInstance().trackScreenView("Leave Request Submission."+"("+PreferenceManager.getUserEmail(mContext)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

        }


    }
}
