package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.DataCollectionHome;
import com.mobatia.bskl.activity.datacollection_p2.adapter.InsuranceStudentRecyclerAdapter;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.report.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondScreenNew extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    String closeMsg;
    ImageView CloseIcon;
    private Context mContext;
    RecyclerView studentInfoRecycler;
    ArrayList<InsuranceDetailModel>mInsuranceDetailList;
    ArrayList<StudentModelNew> studentsModelArrayList;
    InsuranceStudentRecyclerAdapter mInsuranceStudentAdapter;
    String medInsurancePolicyNo="";
    String medInsuranceMemberNo="";
    String medInsuranceProvider="";
    String medInsuranceExpiryDate="";
    String perAccidentInsPolicyNo="";
    String perAccidentInsProvider="";
    String perAccidentInsExpiryDate="";
    String prefferedHsptl="";
    public SecondScreenNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second_screen_new, container, false);

        mContext = getActivity();
        //Medical Insurance
        CloseIcon = v.findViewById(R.id.closeImg);
        studentInfoRecycler = v.findViewById(R.id.studentInfoRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        studentInfoRecycler.setLayoutManager(mLayoutManager);
        studentInfoRecycler.setItemAnimator(new DefaultItemAnimator());
        mInsuranceDetailList=PreferenceManager.getInsuranceDetailArrayList(mContext);
        System.out.println("Student List in screentwo"+PreferenceManager.getInsuranceStudentList(mContext).size());
        if (AppUtils.isNetworkConnected(mContext)) {
             mInsuranceStudentAdapter=new InsuranceStudentRecyclerAdapter(mContext,PreferenceManager.getInsuranceStudentList(mContext));
            studentInfoRecycler.setAdapter(mInsuranceStudentAdapter);
            System.out.println("Api call working");
          //  getStudentsListAPI(URL_GET_STUDENT_LIST);


        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        if (mInsuranceDetailList.size()>0)
        {
            for (int i=0;i<mInsuranceDetailList.size();i++)
            {
                System.out.println("Student name"+mInsuranceDetailList.get(i).getStudent_name());
            }
        }
        CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                text.setText("Please update this information next time");
                textHead.setText("Alert");
                Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceManager.setSuspendTrigger(mContext,"1");
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        });
        studentInfoRecycler.addOnItemTouchListener(new RecyclerItemListener(mContext, studentInfoRecycler,
                new RecyclerItemListener.RecyclerTouchListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClickItem(View v, int position) {

                          String studId=PreferenceManager.getInsuranceStudentList(mContext).get(position).getmId();
                          System.out.println("student ID"+studId);
                           /* String studentId="";
                                       String studentImage="";
                                       String studentName="";*/

                        Intent intent = new Intent(getActivity(), SecondScreenDataCollection.class);
                        intent.putExtra("studentId",PreferenceManager.getInsuranceStudentList(mContext).get(position).getmId());
                        intent.putExtra("studentImage",PreferenceManager.getInsuranceStudentList(mContext).get(position).getmPhoto());
                        intent.putExtra("studentName",PreferenceManager.getInsuranceStudentList(mContext).get(position).getmName());
                        startActivity(intent);

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return v;
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

                                if (PreferenceManager.getInsuranceStudentList(mContext)== null|| !PreferenceManager.getIsAlreadyEnteredStudentList(mContext))
                                {
                                    System.out.println("student list add works");
                                    PreferenceManager.setIsAlreadyEnteredStudentList(mContext,true);
                                    AppController.mStudentDataArrayList=studentsModelArrayList;
                                    PreferenceManager.saveInsuranceStudentList(studentsModelArrayList,mContext);
                                }
                                AppController.mStudentDataArrayList=PreferenceManager.getInsuranceStudentList(mContext);
                                mInsuranceStudentAdapter=new InsuranceStudentRecyclerAdapter(mContext,PreferenceManager.getInsuranceStudentList(mContext));
                                studentInfoRecycler.setAdapter(mInsuranceStudentAdapter);


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

    @Override
    public void onResume() {
        super.onResume();
        mInsuranceStudentAdapter=new InsuranceStudentRecyclerAdapter(mContext,PreferenceManager.getInsuranceStudentList(mContext));
        studentInfoRecycler.setAdapter(mInsuranceStudentAdapter);

    }
}

