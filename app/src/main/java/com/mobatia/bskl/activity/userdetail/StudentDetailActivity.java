package com.mobatia.bskl.activity.userdetail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.home.model.KinDetailsModel;
import com.mobatia.bskl.activity.home.model.LocalEmergencyModel;
import com.mobatia.bskl.activity.home.model.OwnDetailsModel;
import com.mobatia.bskl.activity.userdetail.adapter.LocalEmergencyRecyclerAdapter;
import com.mobatia.bskl.activity.userdetail.adapter.NextOfKinRecyclerAdapter;
import com.mobatia.bskl.activity.userdetail.adapter.PassportDetailRecyclerAdapter;
import com.mobatia.bskl.activity.userdetail.adapter.RelationRecyclerAdapter;
import com.mobatia.bskl.activity.userdetail.adapter.StudentDetailRecyclerAdapter;
import com.mobatia.bskl.activity.userdetail.model.RelationshipModel;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.KeyConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.activity.userdetail.model.StudentDetailModel;
import com.mobatia.bskl.fragment.settings.model.PassportDetailModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.GONE;

/**
 * Created by krishnaraj on 05/09/18.
 */

public class StudentDetailActivity extends AppCompatActivity implements JSONConstants, IntentPassValueConstants, KeyConstants, StatusConstants, URLConstants {
    String studPhoto = "";
    String stud_id = "";
    Context mContext = this;
    Context mActivity = this;
    ImageView iconImgProfile;
    Bundle extras;
    RecyclerView studentDetail,passportDetailRecycler,visaPermitDetailRecycler,medicalInsDetailRecycler,personalInsDetailRecycler;
    RecyclerView studentRelationship;
    RecyclerView nextOfKinRecycler;
    RecyclerView localEmergencyRecycler;
    ArrayList<StudentDetailModel> mStudentDetailModelList;
    ArrayList<PassportDetailModel> mPassportDetailArrayList;
    ArrayList<PassportDetailModel> mVisaPermitDetailArrayList;
    ArrayList<PassportDetailModel> mMedicalInsuranceDetailArrayList;
    ArrayList<PassportDetailModel> mPersonalInsuranceDetailArrayList;
    ArrayList<RelationshipModel> mRelationshipModelList;
    ArrayList<KinDetailsModel> mKinDetailsModelList;
    ArrayList<OwnDetailsModel> mOwnDetailsModelList;
    ArrayList<LocalEmergencyModel> mLocalEmergencyModelList;
    LinearLayout otherContactLinear;
    LinearLayout nextOfKinLinear;
    LinearLayout localEmergencyLinear,passportDetailLinear,visaPermitDetailLinear,medicalInsDetailLinear,personalInsDetailLinear;

    TextView noNxtOfKin,noLocalEmergency,infoTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.student_detail_activity);

        extras = getIntent().getExtras();
        mContext = this;
        mActivity = this;
        if (extras != null) {
            stud_id = extras.getString("stud_id");
        }

//        StudentListRecyclerAdapter mStudentListRecyclerAdapter=new StudentListRecyclerAdapter(mContext,studentsModelArrayList);
//        studentList.setAdapter(mStudentListRecyclerAdapter);

        try {
            initialiseUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (AppUtils.checkInternet(mActivity)) {
            getStudentUserProfileApi(URL_USER_PROFILE_STUDENT_DETAILS_V1);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mActivity, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initialiseUI() throws ParseException {


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view = getSupportActionBar().getCustomView();
        Toolbar toolbar = (Toolbar) view.getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        TextView headerTitle = view.findViewById(R.id.headerTitle);
        ImageView logoClickImgView = view.findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = view.findViewById(R.id.action_bar_forward);
        ImageView action_bar_back = view.findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        headerTitle.setText("Student Information");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_forward.setVisibility(View.INVISIBLE);

        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mStudentDetailModelList = new ArrayList<>();
        mMedicalInsuranceDetailArrayList = new ArrayList<>();
        mPersonalInsuranceDetailArrayList = new ArrayList<>();
        mVisaPermitDetailArrayList = new ArrayList<>();
        mPassportDetailArrayList = new ArrayList<>();
        mRelationshipModelList = new ArrayList<>();
        mKinDetailsModelList = new ArrayList<>();
        mOwnDetailsModelList = new ArrayList<>();
        mLocalEmergencyModelList = new ArrayList<>();
        medicalInsDetailLinear = findViewById(R.id.medicalInsDetailLinear);
        personalInsDetailLinear = findViewById(R.id.personalInsDetailLinear);
        otherContactLinear = findViewById(R.id.otherContactLinear);
        nextOfKinLinear = findViewById(R.id.nextOfKinLinear);
        localEmergencyLinear = findViewById(R.id.localEmergencyLinear);
        iconImgProfile = findViewById(R.id.iconImg);
        noNxtOfKin = findViewById(R.id.noNxtOfKin);
        noLocalEmergency = findViewById(R.id.noLocalEmergency);
        visaPermitDetailLinear = findViewById(R.id.visaPermitDetailLinear);
        infoTxt = findViewById(R.id.infoTxt);
        noLocalEmergency.setVisibility(GONE);
        infoTxt.setVisibility(GONE);
        noNxtOfKin.setVisibility(GONE);

        studentDetail = findViewById(R.id.studentDetail);
        passportDetailLinear = findViewById(R.id.passportDetailLinear);
        studentDetail.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        studentDetail.setLayoutManager(llm);
        passportDetailRecycler = findViewById(R.id.passportDetailRecycler);
        passportDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager llp = new LinearLayoutManager(this);
        llp.setOrientation(LinearLayoutManager.VERTICAL);
        passportDetailRecycler.setLayoutManager(llp);
        visaPermitDetailRecycler = findViewById(R.id.visaPermitDetailRecycler);
        visaPermitDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager llvp = new LinearLayoutManager(this);
        llvp.setOrientation(LinearLayoutManager.VERTICAL);
        visaPermitDetailRecycler.setLayoutManager(llvp);
        medicalInsDetailRecycler = findViewById(R.id.medicalInsDetailRecycler);
        medicalInsDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager llmi = new LinearLayoutManager(this);
        llmi.setOrientation(LinearLayoutManager.VERTICAL);
        medicalInsDetailRecycler.setLayoutManager(llmi);
        personalInsDetailRecycler = findViewById(R.id.personalInsDetailRecycler);
        personalInsDetailRecycler.setHasFixedSize(true);
        LinearLayoutManager llpi = new LinearLayoutManager(this);
        llpi.setOrientation(LinearLayoutManager.VERTICAL);
        personalInsDetailRecycler.setLayoutManager(llpi);


        studentRelationship = findViewById(R.id.studentRelationship);
        localEmergencyRecycler = findViewById(R.id.localEmergencyRecycler);
        nextOfKinRecycler = findViewById(R.id.nextOfKinRecycler);
        //other contact recycler
        studentRelationship.setHasFixedSize(true);
        LinearLayoutManager llmstudentRelationship = new LinearLayoutManager(this);
        llmstudentRelationship.setOrientation(LinearLayoutManager.VERTICAL);
        studentRelationship.setLayoutManager(llmstudentRelationship);

        //next of kin recycler

        nextOfKinRecycler.setHasFixedSize(true);
        LinearLayoutManager llmnextOfKinRecycler = new LinearLayoutManager(this);
        llmnextOfKinRecycler.setOrientation(LinearLayoutManager.VERTICAL);
        nextOfKinRecycler.setLayoutManager(llmnextOfKinRecycler);

        //Local emergency recycler

        localEmergencyRecycler.setHasFixedSize(true);
        LinearLayoutManager llmlocalEmergencyRecycler = new LinearLayoutManager(this);
        llmlocalEmergencyRecycler.setOrientation(LinearLayoutManager.VERTICAL);
        localEmergencyRecycler.setLayoutManager(llmlocalEmergencyRecycler);
    }

    private void getStudentUserProfileApi(final String URLHEAD) {
        mLocalEmergencyModelList.clear();
        mRelationshipModelList.clear();
        mKinDetailsModelList.clear();
        mOwnDetailsModelList.clear();
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        final String[] name = {"access_token", "user_ids", "stud_id"};
        String[] value = {PreferenceManager.getAccessToken(mActivity), PreferenceManager.getUserId(mActivity), stud_id};
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
                        if (status_code.equalsIgnoreCase("303"))
                        {
                            studPhoto = secobj.optString("stud_photo");
                            JSONArray studentDetailsJsonArray = secobj.optJSONArray("studentdetails");
                            for (int i = 0; i < studentDetailsJsonArray.length(); i++) {
                                System.out.println("working 11");
                                JSONObject studObj = studentDetailsJsonArray.optJSONObject(i);
                                StudentDetailModel studentDetailModel = new StudentDetailModel();
                                studentDetailModel.setTitle(studObj.optString("title"));
                                studentDetailModel.setValue(studObj.optString("value"));
                                studentDetailModel.setIs_expired("");
                                if (studObj.optString("title").equalsIgnoreCase("")||studObj.optString("title").equalsIgnoreCase("null") ||studObj.optString("title").equalsIgnoreCase("undefined") || studObj.optString("value").equalsIgnoreCase("null")|| studObj.optString("value").equalsIgnoreCase("")|| studObj.optString("value").equalsIgnoreCase("undefined"))
                                {

                                }
                                else
                                {
                                    mStudentDetailModelList.add(studentDetailModel);

                                }
                            }
                            System.out.println("before passport works");
                            if(secobj.has("passport"))
                            {
                                JSONArray passportDetailArray = secobj.optJSONArray("passport");
                                for (int i = 0; i < passportDetailArray.length(); i++) {
                                    System.out.println("working 11");
                                    JSONObject passObj = passportDetailArray.optJSONObject(i);
                                    PassportDetailModel mModel = new PassportDetailModel();
                                    mModel.setTitle(passObj.optString("title"));
                                    mModel.setValue(passObj.optString("value"));
                                    mModel.setIs_expired(passObj.optString("is_expired"));
                                    System.out.println("before passport works111");
                                    if (passObj.optString("title").equalsIgnoreCase("")||passObj.optString("title").equalsIgnoreCase("null") ||passObj.optString("title").equalsIgnoreCase("undefined") || passObj.optString("value").equalsIgnoreCase("null")|| passObj.optString("value").equalsIgnoreCase("")|| passObj.optString("value").equalsIgnoreCase("undefined"))
                                    {

                                    }
                                    else
                                    {
                                        mPassportDetailArrayList.add(mModel);

                                    }
                                }
                            }
                            else
                            {
                                passportDetailLinear.setVisibility(GONE);
                            }

                            if(secobj.has("visa_permit"))
                            {
                                JSONArray visaPermitDetailArray = secobj.optJSONArray("visa_permit");
                                for (int i = 0; i < visaPermitDetailArray.length(); i++) {
                                    System.out.println("working 11");
                                    JSONObject visaObj = visaPermitDetailArray.optJSONObject(i);
                                    PassportDetailModel mModel = new PassportDetailModel();
                                    mModel.setTitle(visaObj.optString("title"));
                                    mModel.setValue(visaObj.optString("value"));
                                    mModel.setIs_expired(visaObj.optString("is_expired"));
                                    System.out.println("before passport works111");
                                    if (visaObj.optString("title").equalsIgnoreCase("")||visaObj.optString("title").equalsIgnoreCase("null") ||visaObj.optString("title").equalsIgnoreCase("undefined") || visaObj.optString("value").equalsIgnoreCase("null")|| visaObj.optString("value").equalsIgnoreCase("")|| visaObj.optString("value").equalsIgnoreCase("undefined"))
                                    {

                                    }
                                    else
                                    {
                                        mVisaPermitDetailArrayList.add(mModel);

                                    }
                                }
                            }
                            else
                            {
                                visaPermitDetailLinear.setVisibility(GONE);
                            }
                            if(secobj.has("medical_insurance"))
                            {
                                JSONArray medicalInsuranceArray = secobj.optJSONArray("medical_insurance");
                                for (int i = 0; i < medicalInsuranceArray.length(); i++) {
                                    System.out.println("working 11");
                                    JSONObject medInsuObj = medicalInsuranceArray.optJSONObject(i);
                                    PassportDetailModel mModel = new PassportDetailModel();
                                    mModel.setTitle(medInsuObj.optString("title"));
                                    mModel.setValue(medInsuObj.optString("value"));
                                    mModel.setIs_expired(medInsuObj.optString("is_expired"));
                                    System.out.println("before passport works111");
                                    if (medInsuObj.optString("title").equalsIgnoreCase("")||medInsuObj.optString("title").equalsIgnoreCase("null") ||medInsuObj.optString("title").equalsIgnoreCase("undefined") || medInsuObj.optString("value").equalsIgnoreCase("null")|| medInsuObj.optString("value").equalsIgnoreCase("")|| medInsuObj.optString("value").equalsIgnoreCase("undefined"))
                                    {

                                    }
                                    else
                                    {
                                        mMedicalInsuranceDetailArrayList.add(mModel);

                                    }
                                }
                            }
                            else
                            {
                                medicalInsDetailLinear.setVisibility(GONE);
                            }
                            if(secobj.has("personal_accident_insurance")) {
                                JSONArray personalInsuranceArray = secobj.optJSONArray("personal_accident_insurance");
                                for (int i = 0; i < personalInsuranceArray.length(); i++) {
                                    System.out.println("working 11");
                                    JSONObject personalInsuObj = personalInsuranceArray.optJSONObject(i);
                                    PassportDetailModel mModel = new PassportDetailModel();
                                    mModel.setTitle(personalInsuObj.optString("title"));
                                    mModel.setValue(personalInsuObj.optString("value"));
                                    mModel.setIs_expired(personalInsuObj.optString("is_expired"));
                                    System.out.println("before passport works111");
                                    if (personalInsuObj.optString("title").equalsIgnoreCase("")||personalInsuObj.optString("title").equalsIgnoreCase("null") ||personalInsuObj.optString("title").equalsIgnoreCase("undefined") || personalInsuObj.optString("value").equalsIgnoreCase("null")|| personalInsuObj.optString("value").equalsIgnoreCase("")|| personalInsuObj.optString("value").equalsIgnoreCase("undefined"))
                                    {

                                    }
                                    else
                                    {
                                        mPersonalInsuranceDetailArrayList.add(mModel);

                                    }
                                }
                            }
                            else
                            {
                                personalInsDetailLinear.setVisibility(GONE);
                            }


                            System.out.println("working 12"+mVisaPermitDetailArrayList.size());
                            JSONObject mContactDetails = secobj.optJSONObject("contact_details");
                            JSONArray ownDetailArray=mContactDetails.getJSONArray("other_contact");
                            for (int j=0;j<ownDetailArray.length();j++)
                            {
                                if (ownDetailArray.length()>0)
                                {

                                    JSONObject ownDetailObj=ownDetailArray.optJSONObject(j);
                                    OwnDetailsModel mOwnDetailsModel=new OwnDetailsModel();
                                    mOwnDetailsModel.setEmail(ownDetailObj.optString("email"));
                                    mOwnDetailsModel.setFullName(ownDetailObj.optString("firstname"));
                                    mOwnDetailsModel.setContactNo(ownDetailObj.optString("mobile"));
                                    mOwnDetailsModel.setRelationShip(ownDetailObj.optString("relationship"));
                                    mOwnDetailsModelList.add(mOwnDetailsModel);
                                }
                            }
                            if(secobj.has("next_of_kin_contact")) {
                                JSONArray nextOfKinArray = mContactDetails.getJSONArray("next_of_kin_contact");
                                for (int j = 0; j < nextOfKinArray.length(); j++) {
                                    if (nextOfKinArray.length() > 0) {
                                        JSONObject kinDetailObj = nextOfKinArray.optJSONObject(j);
                                        KinDetailsModel mKinDetailModel = new KinDetailsModel();
                                        mKinDetailModel.setEmial(kinDetailObj.optString("email"));
                                        mKinDetailModel.setFullName(kinDetailObj.optString("firstname"));
                                        mKinDetailModel.setContactNumber(kinDetailObj.optString("mobile"));
                                        mKinDetailModel.setRelationShip(kinDetailObj.optString("relationship"));
                                        mKinDetailsModelList.add(mKinDetailModel);
                                    }

                                }
                            }

                            JSONArray localEmergencyArray=mContactDetails.getJSONArray("local_emergency_contact");
                            for (int j=0;j<localEmergencyArray.length();j++)
                            {
                                if (localEmergencyArray.length()>0)
                                {
                                    JSONObject localEmergencyDetailObj=localEmergencyArray.optJSONObject(j);
                                    LocalEmergencyModel mLocalEmergencyModel=new LocalEmergencyModel();
                                    mLocalEmergencyModel.setEmial(localEmergencyDetailObj.optString("email"));
                                    mLocalEmergencyModel.setFullName(localEmergencyDetailObj.optString("firstname"));
                                    mLocalEmergencyModel.setContactNumber(localEmergencyDetailObj.optString("mobile"));
                                    mLocalEmergencyModel.setRelationShip(localEmergencyDetailObj.optString("relationship"));
                                    mLocalEmergencyModelList.add(mLocalEmergencyModel);
                                }

                                }
                            System.out.println("working 12"+mStudentDetailModelList.size());
                            if (mStudentDetailModelList.size() > 0)
                            {
                                System.out.println("working 12++");
                                StudentDetailRecyclerAdapter studentDetailRecyclerAdapter = new StudentDetailRecyclerAdapter(mContext, mStudentDetailModelList);
                                studentDetail.setAdapter(studentDetailRecyclerAdapter);

                                studentDetail.setVisibility(View.VISIBLE);
                                System.out.println("working 13");
                            } else {
                                studentDetail.setVisibility(GONE);
                            }
                            if (mPassportDetailArrayList.size() > 0)
                            {
                                System.out.println("working passport 12++");
                                PassportDetailRecyclerAdapter studentDetailRecyclerAdapter = new PassportDetailRecyclerAdapter(mContext, mPassportDetailArrayList);
                                passportDetailRecycler.setAdapter(studentDetailRecyclerAdapter);

                                passportDetailRecycler.setVisibility(View.VISIBLE);
                                passportDetailLinear.setVisibility(View.VISIBLE);
                                System.out.println("working passport 13");
                            } else {
                                passportDetailRecycler.setVisibility(GONE);
                                passportDetailLinear.setVisibility(GONE);
                            }
                            if (mVisaPermitDetailArrayList.size() > 0)
                            {
                                System.out.println("working passport 12++");
                                PassportDetailRecyclerAdapter studentDetailRecyclerAdapter = new PassportDetailRecyclerAdapter(mContext, mVisaPermitDetailArrayList);
                                visaPermitDetailRecycler.setAdapter(studentDetailRecyclerAdapter);

                                visaPermitDetailRecycler.setVisibility(View.VISIBLE);
                                visaPermitDetailLinear.setVisibility(View.VISIBLE);
                                System.out.println("working passport 13");
                            } else {
                                visaPermitDetailRecycler.setVisibility(GONE);
                                visaPermitDetailLinear.setVisibility(GONE);
                            }
                            if (mMedicalInsuranceDetailArrayList.size() > 0)
                            {
                                System.out.println("working passport 12++");
                                PassportDetailRecyclerAdapter studentDetailRecyclerAdapter = new PassportDetailRecyclerAdapter(mContext, mMedicalInsuranceDetailArrayList);
                                medicalInsDetailRecycler.setAdapter(studentDetailRecyclerAdapter);

                                medicalInsDetailRecycler.setVisibility(View.VISIBLE);
                                medicalInsDetailLinear.setVisibility(View.VISIBLE);
                                System.out.println("working passport 13");
                            } else {
                                medicalInsDetailRecycler.setVisibility(GONE);
                                medicalInsDetailLinear.setVisibility(GONE);
                            }
                            if (mPersonalInsuranceDetailArrayList.size() > 0)
                            {
                                System.out.println("working passport 12++");
                                PassportDetailRecyclerAdapter studentDetailRecyclerAdapter = new PassportDetailRecyclerAdapter(mContext, mPersonalInsuranceDetailArrayList);
                                personalInsDetailRecycler.setAdapter(studentDetailRecyclerAdapter);

                                personalInsDetailRecycler.setVisibility(View.VISIBLE);
                                personalInsDetailLinear.setVisibility(View.VISIBLE);
                                System.out.println("working passport 13");
                            } else {
                                personalInsDetailRecycler.setVisibility(GONE);
                                personalInsDetailLinear.setVisibility(GONE);
                            }
                            if (mOwnDetailsModelList.size() > 0) {
                                RelationRecyclerAdapter relationRecyclerAdapter = new RelationRecyclerAdapter(mContext, mOwnDetailsModelList);
                                studentRelationship.setAdapter(relationRecyclerAdapter);
                                otherContactLinear.setVisibility(View.VISIBLE);
                            } else {
                                otherContactLinear.setVisibility(GONE);
                            }

                            if (mKinDetailsModelList.size() > 0) {
                                NextOfKinRecyclerAdapter nextOfKinRecyclerAdapter = new NextOfKinRecyclerAdapter(mContext, mKinDetailsModelList);
                                nextOfKinRecycler.setAdapter(nextOfKinRecyclerAdapter);
                                nextOfKinLinear.setVisibility(View.VISIBLE);
                                nextOfKinRecycler.setVisibility(View.VISIBLE);
                                noNxtOfKin.setVisibility(GONE);
                            }
                            else {
                                nextOfKinLinear.setVisibility(View.GONE);
                                nextOfKinRecycler.setVisibility(GONE);
                                noNxtOfKin.setVisibility(View.GONE);
                            }


                            if (mLocalEmergencyModelList.size() > 0) {
                                LocalEmergencyRecyclerAdapter localEmergencyRecyclerAdapter = new LocalEmergencyRecyclerAdapter(mContext, mLocalEmergencyModelList);
                                localEmergencyRecycler.setAdapter(localEmergencyRecyclerAdapter);
                                localEmergencyLinear.setVisibility(View.VISIBLE);
                                localEmergencyRecycler.setVisibility(View.VISIBLE);
                                noLocalEmergency.setVisibility(GONE);
                            } else {
                                localEmergencyLinear.setVisibility(View.VISIBLE);
                                localEmergencyRecycler.setVisibility(GONE);
                                noLocalEmergency.setVisibility(View.VISIBLE);

                            }
                            infoTxt.setVisibility(View.VISIBLE);
                            if (studPhoto.equalsIgnoreCase(""))
                            {

                            }else
                            {
//                                Glide.with(mContext).load(AppUtils.replace(studPhoto)).centerCrop().into(iconImgProfile);
                                Picasso.with(mContext).load(AppUtils.replace(studPhoto)).placeholder(R.drawable.boy).fit().into(iconImgProfile);

                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mActivity, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mActivity, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentUserProfileApi(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mActivity, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentUserProfileApi(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mActivity, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentUserProfileApi(URLHEAD);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mActivity, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mActivity, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Student Detail."+" "+"("+PreferenceManager.getUserEmail(mActivity)+")"+" "+ Calendar.getInstance().getTime()+")");
        }


    }
}
