package com.mobatia.bskl.fragment.absence;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.mobatia.bskl.fragment.absence.adapter.AbsenceRecyclerAdapter;
import com.mobatia.bskl.fragment.absence.adapter.StrudentSpinnerAdapter;
import com.mobatia.bskl.fragment.absence.model.LeavesModel;
import com.mobatia.bskl.fragment.absence.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.bskl.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by krishnaraj on 27/08/18.
 */

public class AbsenceFragment extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

    private View mRootView;
    private Context mContext;
    private RecyclerView mAbsenceListView;
    private String mTitle;
    private String mTabId;

    TextView newRequest;
    private RelativeLayout relMain;
    RelativeLayout noDataRelative;
    TextView noDataTxt;
    ImageView noDataImg;
    String stud_img = "";
    ImageView studImg;
    private RelativeLayout belowViewRelative;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    private ArrayList<LeavesModel> mAbsenceListViewArray;
    LinearLayout mStudentSpinner;
    TextView studentName;
    String stud_id = "";
    String studClass = "";
    String alumini = "";
    String progressreport = "";
    ArrayList<String> studentList = new ArrayList<>();
    boolean firstVisit;

    public AbsenceFragment() {

    }

    public AbsenceFragment(String title, String tabId) {
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
        mRootView = inflater.inflate(R.layout.fragment_absence_list, container,
                false);
        setHasOptionsMenu(true);
        mContext = getActivity();
        firstVisit = true;
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        headerTitle.setText("Absence");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        initialiseUI();
        if (AppUtils.checkInternet(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
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
        mAbsenceListView = mRootView.findViewById(R.id.mAbsenceListView);
        relMain = mRootView.findViewById(R.id.relMain);
        mStudentSpinner = mRootView.findViewById(R.id.studentSpinner);
        belowViewRelative = mRootView.findViewById(R.id.belowViewRelative);
        noDataRelative = mRootView.findViewById(R.id.noDataRelative);
        noDataTxt = mRootView.findViewById(R.id.noDataTxt);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        studentName = mRootView.findViewById(R.id.studentName);
        studImg = mRootView.findViewById(R.id.studImg);
        newRequest = mRootView.findViewById(R.id.newRequest);

        mAbsenceListView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAbsenceListView.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, 2);
        mAbsenceListView.addItemDecoration(itemDecoration);
        mAbsenceListView.addItemDecoration(
                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));

        mAbsenceListView.addOnItemTouchListener(new RecyclerItemListener(mContext, mAbsenceListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (mAbsenceListViewArray.size() > 0) {
                            Intent mIntent = new Intent(mContext, LeavesDetailActivity.class);
                            mIntent.putExtra("studentName", studentName.getText().toString());
                            mIntent.putExtra("studentClass", studClass);
                            mIntent.putExtra("fromDate", mAbsenceListViewArray.get(position).getFrom_date());
                            mIntent.putExtra("toDate", mAbsenceListViewArray.get(position).getTo_date());
                            mIntent.putExtra("reasonForAbsence", mAbsenceListViewArray.get(position).getReason());
                            mIntent.putExtra("staff", mAbsenceListViewArray.get(position).getStaff());
                            mContext.startActivity(mIntent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentsModelArrayList.size() > 0) {
                    showSocialmediaList(studentsModelArrayList);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
        newRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, LeaveRequestSubmissionActivity.class);
                mIntent.putExtra("studentName", studentName.getText().toString());
                mIntent.putExtra("studentImage", stud_img);
                mIntent.putExtra("studentId", PreferenceManager.getLeaveStudentId(mContext));
                mIntent.putExtra("StudentModelArray", studentsModelArrayList);
                mIntent.putExtra("attendance_id", "");
                mIntent.putExtra("status", "");
                mContext.startActivity(mIntent);
            }
        });
    }

    public void showSocialmediaList(final ArrayList<StudentModel> mStudentArray)
    {

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
                        if (AppUtils.isNetworkConnected(mContext)) {

                            studentName.setText(mStudentArray.get(position).getmName());
                            stud_id = mStudentArray.get(position).getmId();
                            studClass = mStudentArray.get(position).getmClass();
                            PreferenceManager.setLeaveStudentId(mContext, stud_id);
                            PreferenceManager.setLeaveStudentName(mContext, mStudentArray.get(position).getmName());
                            stud_img = studentsModelArrayList.get(position).getmPhoto();
                            if (!(stud_img.equals(""))) {

                                Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                            } else

                            {

                                studImg.setImageResource(R.drawable.boy);
                            }
                            if (AppUtils.isNetworkConnected(mContext)) {
                                getList(URL_GET_LEAVEREQUEST_LIST, mStudentArray.get(position).getmId());
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }
                        } else if (!(stud_id.equalsIgnoreCase(mStudentArray.get(position).getmId()))){
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }


    public void getList(final String URL, final String student_id) {

        try {
            mAbsenceListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = new String[]{JTAG_ACCESSTOKEN, "users_id",
                    "student_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), student_id};


            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("Response absence"+successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                String response_code = rootObject.optString(JTAG_RESPONSECODE);
                                if (response_code.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {

                                        JSONArray dataArray = respObject.optJSONArray(JTAG_REQUESTS);

                                        if (dataArray.length() > 0) {
                                            noDataRelative.setVisibility(View.GONE);

                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                LeavesModel mLeavesModel = new LeavesModel();
                                                mLeavesModel.setTo_date(dataObject.optString(JTAG_TODATE));
                                                mLeavesModel.setFrom_date(dataObject.optString(JTAG_FROMDATE));
                                                mLeavesModel.setReason(dataObject.optString(JTAG_REASON));
                                                mLeavesModel.setStatus(dataObject.optString(JTAG_STATUs));
                                                mLeavesModel.setStaff(dataObject.optString("staff"));
                                                mAbsenceListViewArray.add(mLeavesModel);
                                            }
                                            AbsenceRecyclerAdapter mAbsenceRecyclerAdapter = new AbsenceRecyclerAdapter(mContext, mAbsenceListViewArray);
                                            mAbsenceListView.setAdapter(mAbsenceRecyclerAdapter);
                                        } else {
                                            AbsenceRecyclerAdapter mAbsenceRecyclerAdapter = new AbsenceRecyclerAdapter(mContext, mAbsenceListViewArray);
                                            mAbsenceListView.setAdapter(mAbsenceRecyclerAdapter);
                                            noDataRelative.setVisibility(View.VISIBLE);
                                            noDataTxt.setText("Currently you have no absence records");
                                            // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No data available.", R.drawable.exclamationicon, R.drawable.round);

                                        }

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }
                                } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getList(URL, student_id);

                                } else if (response_code.equals(RESPONSE_ERROR)) {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getStudentsListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
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
                            JSONArray data = secobj.getJSONArray("data");
                            studentsModelArrayList.clear();
                            studentList.clear();
                            if (data.length() > 0) {
                                //studentsModelArrayList.add(0,);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    if (dataObject.optString("alumi").equalsIgnoreCase("0")) {
                                        studentsModelArrayList.add(addStudentDetails(dataObject));
                                    }
//                                    studentList.add(studentsModelArrayList.get(i).getmName());
                                /*    studentList.add(studentsModelArrayList.get(i).getAlumini());
                                    studentList.add(studentsModelArrayList.get(i).getProgressreport());*/
                                }
                                studentName.setText(studentsModelArrayList.get(0).getmName());
                                stud_img = studentsModelArrayList.get(0).getmPhoto();
                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else

                                {

                                    studImg.setImageResource(R.drawable.boy);
                                }


                                stud_id = studentsModelArrayList.get(0).getmId();
                                alumini = studentsModelArrayList.get(0).getAlumini();
                                progressreport = studentsModelArrayList.get(0).getProgressreport();
                                PreferenceManager.setLeaveStudentId(mContext, stud_id);
                                PreferenceManager.setLeaveStudentName(mContext, studentsModelArrayList.get(0).getmName());
                                studClass = studentsModelArrayList.get(0).getmClass();
                                belowViewRelative.setVisibility(View.VISIBLE);
                                newRequest.setVisibility(View.VISIBLE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getList(URL_GET_LEAVEREQUEST_LIST, stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            } else {
                                belowViewRelative.setVisibility(View.INVISIBLE);
                                newRequest.setVisibility(View.INVISIBLE);

                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

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

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private StudentModel addStudentDetails(JSONObject dataObject) {
        StudentModel studentModel = new StudentModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));
        studentModel.setProgressreport(dataObject.optString("progressreport"));
        studentModel.setAlumini(dataObject.optString("alumi"));
        return studentModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Absence Fragment"+" "+"("+PreferenceManager.getUserEmail(mContext)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

        }

        if (firstVisit) {

            firstVisit = false;
        } else {
            if (AppUtils.isNetworkConnected(mContext)) {
                studentName.setText(PreferenceManager.getLeaveStudentName(mContext));
                getList(URL_GET_LEAVEREQUEST_LIST, PreferenceManager.getLeaveStudentId(mContext));
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        }


    }
}
