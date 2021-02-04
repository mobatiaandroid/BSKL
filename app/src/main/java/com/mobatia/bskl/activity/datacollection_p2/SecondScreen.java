package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.absence.adapter.StrudentSpinnerAdapter;
import com.mobatia.bskl.fragment.absence.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SecondScreen extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    String closeMsg;
    final Calendar myCalendar = Calendar.getInstance();
    int C_positoin;
    String HEALTH_ID = "";
    String DATE = "";
    boolean isMedicalInsuranceClicked=false;
    boolean isPersonalInsuranceClicked=false;
     ImageView checknoMedicalInsuranceImg,checkpersonalInsuranceImg,CloseIcon;
     Button submitBtn,submitBtnasdas;
     EditText insurancePolicyNumberTxt,insuranceMemberNumberTxt,insuranceProviderTxt,insuranceExpiryDate;
     EditText personalInsuranceNumberTxt,personalInsuranceProviderTxt,personalInsuranceExpiryDateTxt,preferredHsptlTxt;
     String insuranceType="0";
     int STUDENT_SIZE;
     int STUDENT_CLICK = 0;
    private Context mContext;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    TextView studentName;
    String stud_id = "";
    String studClass = "";
    String alumini = "";
    String progressreport = "";
    String stud_img = "";
    ImageView studImg;
    LinearLayout mStudentSpinner;
    String SAVE_STATE="0";
    String insurancePolicyNumber="";
    String inuranceMemberNumber="";
    String insuranceProvider="";
    String medicalExpiryDate="";
    String personalAccidentInsuranceNumber="";
    String personalAccidentProviderNumber="";
    String personalAccidentExpiryDate="";
    String preferredHsptl="";
    ArrayList<InsuranceDetailModel>mInsuranceDetailArrayList;
    String FieldEdits = "5";
    String CurrectIncpolicyNum;
    ArrayList<InsuranceDetailModel> InsureArray = new ArrayList<>();
    ArrayList<String> SecondArray = new ArrayList<>();
    String HaveMedINS = "YES";
    String HavePerINS = "YES";

    public SecondScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_insurance_new_detail_data_collection, container, false);

        mContext = getActivity();
        //Medical Insurance
        checknoMedicalInsuranceImg=v.findViewById(R.id.checknoMedicalInsuranceImg);
        insurancePolicyNumberTxt=v.findViewById(R.id.insurancePolicyNumberTxt);
        insuranceMemberNumberTxt=v.findViewById(R.id.insuranceMemberNumberTxt);
        insuranceProviderTxt=v.findViewById(R.id.insuranceProviderTxt);
        insuranceExpiryDate=v.findViewById(R.id.insuranceExpiryDate);
        studentName=v.findViewById(R.id.studentName);
        studImg=v.findViewById(R.id.imagicon);
        CloseIcon = v.findViewById(R.id.closeImg);
        submitBtnasdas=v.findViewById(R.id.submitBtnasdas);
        preferredHsptlTxt=v.findViewById(R.id.preferredHsptlTxt);
        preferredHsptl = preferredHsptlTxt.getText().toString();
        //Medical Insurance Policy Number
        String InsurancePolicyNograyPart = "Insurance Policy Number";
        String InsurancePolicyNoredPart = "*";
        SpannableStringBuilder InsurancePolicyNobuilder = new SpannableStringBuilder();
        SpannableString InsurancePolicyNoredColoredString = new SpannableString(InsurancePolicyNograyPart);
        InsurancePolicyNoredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, InsurancePolicyNograyPart.length(), 0);
        InsurancePolicyNobuilder.append(InsurancePolicyNoredColoredString);
        SpannableString InsurancePolicyNoblueColoredString = new SpannableString(InsurancePolicyNoredPart);
        InsurancePolicyNoblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, InsurancePolicyNoredPart.length(), 0);
        InsurancePolicyNobuilder.append(InsurancePolicyNoblueColoredString);
        insurancePolicyNumberTxt.setHint(InsurancePolicyNobuilder);
        //Insurance Member Number
        String InsuranceMemberNograyPart = "Insurance Member Number";
        String InsuranceMemberNoredPart = "*";
        SpannableStringBuilder InsuranceMemberNobuilder = new SpannableStringBuilder();
        SpannableString InsuranceMemberNoredColoredString = new SpannableString(InsuranceMemberNograyPart);
        InsuranceMemberNoredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, InsuranceMemberNograyPart.length(), 0);
        InsuranceMemberNobuilder.append(InsuranceMemberNoredColoredString);
        SpannableString InsuranceMemberNoblueColoredString = new SpannableString(InsuranceMemberNoredPart);
        InsuranceMemberNoblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, InsuranceMemberNoredPart.length(), 0);
        InsuranceMemberNobuilder.append(InsuranceMemberNoblueColoredString);
        insuranceMemberNumberTxt.setHint(InsuranceMemberNobuilder);
        //Medical Insurance Provider
        String InsuranceProvidergrayPart = "Insurance Provider";
        String InsuranceProviderredPart = "*";
        SpannableStringBuilder InsuranceProviderbuilder = new SpannableStringBuilder();
        SpannableString InsuranceProviderredColoredString = new SpannableString(InsuranceProvidergrayPart);
        InsuranceProviderredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, InsuranceProvidergrayPart.length(), 0);
        InsuranceProviderbuilder.append(InsuranceProviderredColoredString);
        SpannableString InsuranceProviderblueColoredString = new SpannableString(InsuranceProviderredPart);
        InsuranceProviderblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, InsuranceProviderredPart.length(), 0);
        InsuranceProviderbuilder.append(InsuranceProviderblueColoredString);
        insuranceProviderTxt.setHint(InsuranceProviderbuilder);
        //Medical Insurance Provider
        String InsuranceExpiryDategrayPart = "Insurance Expiry Date";
        String InsuranceExpiryDateredPart = "*";
        SpannableStringBuilder InsuranceExpiryDatebuilder = new SpannableStringBuilder();
        SpannableString InsuranceExpiryDateredColoredString = new SpannableString(InsuranceExpiryDategrayPart);
        InsuranceExpiryDateredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, InsuranceExpiryDategrayPart.length(), 0);
        InsuranceExpiryDatebuilder.append(InsuranceExpiryDateredColoredString);
        SpannableString InsuranceExpiryDateblueColoredString = new SpannableString(InsuranceExpiryDateredPart);
        InsuranceExpiryDateblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, InsuranceExpiryDateredPart.length(), 0);
        InsuranceExpiryDatebuilder.append(InsuranceExpiryDateblueColoredString);
        insuranceExpiryDate.setHint(InsuranceExpiryDatebuilder);


        //Personal Insurance
        checkpersonalInsuranceImg=v.findViewById(R.id.checkpersonalInsuranceImg);
        personalInsuranceNumberTxt=v.findViewById(R.id.personalInsuranceNumberTxt);
        personalInsuranceProviderTxt=v.findViewById(R.id.personalInsuranceProviderTxt);
        personalInsuranceExpiryDateTxt=v.findViewById(R.id.personalInsuranceExpiryDateTxt);
        mStudentSpinner = v.findViewById(R.id.studentSpinner);
        submitBtn=v.findViewById(R.id.submitBtn);
        System.out.println("Api call not working");

        //Personal Accident insurance Policy Number
        String PersonalInsNograyPart = "Insurance Policy Number";
        String personalInsNoredPart = "*";
        SpannableStringBuilder personalInsNobuilder = new SpannableStringBuilder();
        SpannableString personalInsNoredColoredString = new SpannableString(PersonalInsNograyPart);
        personalInsNoredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, PersonalInsNograyPart.length(), 0);
        personalInsNobuilder.append(personalInsNoredColoredString);
        SpannableString personalInsNoblueColoredString = new SpannableString(personalInsNoredPart);
        personalInsNoblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, personalInsNoredPart.length(), 0);
        personalInsNobuilder.append(personalInsNoblueColoredString);
        personalInsuranceNumberTxt.setHint(personalInsNobuilder);
        mInsuranceDetailArrayList=PreferenceManager.getInsuranceDetailArrayList(mContext);

        //Personal Accident insurance Provider
        String PersonalInsprovidergrayPart = "Insurance Provider";
        String PersonalInsproviderredPart = "*";
        SpannableStringBuilder PersonalInsproviderbuilder = new SpannableStringBuilder();
        SpannableString PersonalInsproviderredColoredString = new SpannableString(PersonalInsprovidergrayPart);
        PersonalInsproviderredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, PersonalInsprovidergrayPart.length(), 0);
        PersonalInsproviderbuilder.append(PersonalInsproviderredColoredString);
        SpannableString PersonalInsproviderblueColoredString = new SpannableString(PersonalInsproviderredPart);
        PersonalInsproviderblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, PersonalInsproviderredPart.length(), 0);
        PersonalInsproviderbuilder.append(PersonalInsproviderblueColoredString);
        personalInsuranceProviderTxt.setHint(PersonalInsproviderbuilder);

        //Personal Accident insurance Provider
        String PersonalInsExDategrayPart = "Insurance Expiry Date";
        String PersonalInsExDateredPart = "*";
        SpannableStringBuilder PersonalInsExDatebuilder = new SpannableStringBuilder();
        SpannableString PersonalInsExDateredColoredString = new SpannableString(PersonalInsExDategrayPart);
        PersonalInsExDateredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, PersonalInsExDategrayPart.length(), 0);
        PersonalInsExDatebuilder.append(PersonalInsExDateredColoredString);
        SpannableString PersonalInsExDateblueColoredString = new SpannableString(PersonalInsExDateredPart);
        PersonalInsExDateblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, PersonalInsExDateredPart.length(), 0);
        PersonalInsExDatebuilder.append(PersonalInsExDateblueColoredString);
        personalInsuranceExpiryDateTxt.setHint(PersonalInsExDatebuilder);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();

            }

        };

        insuranceExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        personalInsuranceExpiryDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (AppUtils.isNetworkConnected(mContext)) {
            System.out.println("Api call working");
            getStudentsListAPI(URL_GET_STUDENT_LIST);


        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
//        if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5") ||PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
//        {
//            submitBtnasdas.setVisibility(View.VISIBLE);
//            submitBtn.setVisibility(View.GONE);
//        }
//        else
//        {
//            submitBtnasdas.setVisibility(View.GONE);
//            submitBtn.setVisibility(View.VISIBLE);
//        }
        checknoMedicalInsuranceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMedicalInsuranceClicked)
                {
                    isMedicalInsuranceClicked=false;
                    checknoMedicalInsuranceImg.setImageResource(R.drawable.uncheck);
                    insurancePolicyNumberTxt.setEnabled(true);
                    insuranceMemberNumberTxt.setEnabled(true);
                    insuranceProviderTxt.setEnabled(true);
                    insuranceExpiryDate.setEnabled(true);
                    insurancePolicyNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    insuranceMemberNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    insuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    insuranceExpiryDate.setBackgroundResource(R.drawable.rect_background_grey);
                    HaveMedINS = "YES";
                }
                else {
                    isMedicalInsuranceClicked=true;
                    checknoMedicalInsuranceImg.setImageResource(R.drawable.checked);
                    insurancePolicyNumberTxt.setEnabled(false);
                    insuranceMemberNumberTxt.setEnabled(false);
                    insuranceProviderTxt.setEnabled(false);
                    insuranceExpiryDate.setEnabled(false);
                    insurancePolicyNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    insuranceMemberNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    insuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    insuranceExpiryDate.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    HaveMedINS = "NO";
                }
                System.out.println("STATE: "+HaveMedINS);
            }
        });
        checkpersonalInsuranceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPersonalInsuranceClicked) {
                    isPersonalInsuranceClicked=false;
                    checkpersonalInsuranceImg.setImageResource(R.drawable.uncheck);
                    personalInsuranceNumberTxt.setEnabled(true);
                    personalInsuranceProviderTxt.setEnabled(true);
                    personalInsuranceExpiryDateTxt.setEnabled(true);
                    personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    HavePerINS = "YES";
                }
                else {
                    isPersonalInsuranceClicked=true;
                    checkpersonalInsuranceImg.setImageResource(R.drawable.checked);
                    personalInsuranceNumberTxt.setEnabled(false);
                    personalInsuranceProviderTxt.setEnabled(false);
                    personalInsuranceExpiryDateTxt.setEnabled(false);
                    personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    HavePerINS = "NO";
                }
                System.out.println("STATE: "+HavePerINS);
            }
        });

        String IncPolicyNum = insurancePolicyNumberTxt.getText().toString();
        String IncMemNum = insuranceMemberNumberTxt.getText().toString();
        String IncProText = insuranceProviderTxt.getText().toString();
        String IncExpDate = insuranceExpiryDate.getText().toString();

        String PrsIncNum = personalInsuranceNumberTxt.getText().toString();
        String PrsIncPro = personalInsuranceProviderTxt.getText().toString();
        String PrsIncExp = personalInsuranceExpiryDateTxt.getText().toString();
        String PrefHsptl = preferredHsptlTxt.getText().toString();

        for (int in=0;in<mInsuranceDetailArrayList.size();in++)
        {
            if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(in).getStudent_id()))
            {
                C_positoin=in;
            }
        }
         HEALTH_ID = mInsuranceDetailArrayList.get(C_positoin).getId();
//        if (mInsuranceDetailArrayList.get(C_positoin).getId().equalsIgnoreCase("")){
//            HEALTH_ID = "";
//        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                STUDENT_CLICK++;
//                System.out.println("StudentSize: "+STUDENT_SIZE+" , "+STUDENT_CLICK);
                if(!isMedicalInsuranceClicked && !isPersonalInsuranceClicked)
                {
                    //User have Both Personal and medical Insuranse
                    insuranceType="1";
                    CheckValueEmpty(insuranceType,FieldEdits);

                }
                else if (!isMedicalInsuranceClicked && isPersonalInsuranceClicked)
                {
                    //User have no Personal and  have medical Insuranse
                    insuranceType="2";
                    CheckValueEmpty(insuranceType,FieldEdits);

                }
                else if (isMedicalInsuranceClicked && !isPersonalInsuranceClicked)
                {
                    //User have  Personal and  no medical Insuranse
                    insuranceType="3";
                    CheckValueEmpty(insuranceType,FieldEdits);

                }
                else
                {
                    //User have no Personal and  no medical Insuranse
                    insuranceType="0";
                    CheckValueEmpty(insuranceType,FieldEdits);

                }

            }
        });
        submitBtnasdas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isMedicalInsuranceClicked && !isPersonalInsuranceClicked)
                {
                    //User have Both Personal and medical Insuranse
                    insuranceType="1";
                    CheckValueEmpty(insuranceType,FieldEdits);
                }
                else if (!isMedicalInsuranceClicked && isPersonalInsuranceClicked)
                {
                    //User have no Personal and  have medical Insuranse
                    insuranceType="2";
                    CheckValueEmpty(insuranceType,FieldEdits);
                }
                else if (isMedicalInsuranceClicked && !isPersonalInsuranceClicked)
                {
                    //User have  Personal and  no medical Insuranse
                    insuranceType="3";
                    CheckValueEmpty(insuranceType,FieldEdits);
                }
                else
                {
                    //User have no Personal and  no medical Insuranse
                    insuranceType="0";
                    CheckValueEmpty(insuranceType,FieldEdits);
                }
//                Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
//                SAVE_STATE = "1";
            }
        });

        insurancePolicyNumberTxt.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable s) {
                int dataPos=0;
                Log.e("INSURANCE ", "Student ID"+stud_id);

               // Log.e("INSURANCE ", "Current text before change"+firstPolicyNumber);
                for (int in=0;in<mInsuranceDetailArrayList.size();in++)
                {
                    if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(in).getStudent_id()))
                    {
                        dataPos=in;
                    }
                }
                String currentData=mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no();
                String newData=insurancePolicyNumberTxt.getText().toString();

              //  Log.e("INSURANCE ", "textChange"+);
                Log.e("INSURANCE ", "Position"+dataPos);
                Log.e("INSURANCE ", "currentData"+currentData);
                Log.e("INSURANCE ", "newData"+newData);
                Log.e("INSURANCE ", "insurance no"+mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no());
                String changedName=insurancePolicyNumberTxt.getText().toString();
                if (currentData.equalsIgnoreCase(newData))
                {
                    FieldEdits = "0";
                }
                else
                {
                    FieldEdits = "1";
                    System.out.println("Change: "+FieldEdits);
                }

            }


        });

        insuranceMemberNumberTxt.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable s) {
                int dataPos=0;
                Log.e("INSURANCE ", "Student ID"+stud_id);

               // Log.e("INSURANCE ", "Current text before change"+firstPolicyNumber);
                for (int in=0;in<mInsuranceDetailArrayList.size();in++)
                {
                    if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(in).getStudent_id()))
                    {
                        dataPos=in;
                    }
                }
                String currentData=mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no();
                String newData=insuranceMemberNumberTxt.getText().toString();

              //  Log.e("INSURANCE ", "textChange"+);
                Log.e("INSURANCE ", "Position"+dataPos);
                Log.e("INSURANCE ", "currentData"+currentData);
                Log.e("INSURANCE ", "newData"+newData);
                Log.e("INSURANCE ", "insurance no"+mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no());
             //   String changedName=insuranceMemberNumberTxt.getText().toString();
                if (currentData.equalsIgnoreCase(newData))
                {
                    FieldEdits = "0";
                }
                else
                {
                    FieldEdits = "1";
                    System.out.println("Change: "+FieldEdits);
                }

            }


        });

        insuranceProviderTxt.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable s) {
                int dataPos=0;
                Log.e("INSURANCE ", "Student ID"+stud_id);

               // Log.e("INSURANCE ", "Current text before change"+firstPolicyNumber);
                for (int in=0;in<mInsuranceDetailArrayList.size();in++)
                {
                    if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(in).getStudent_id()))
                    {
                        dataPos=in;
                    }
                }
                String currentData=mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no();
                String newData=insuranceProviderTxt.getText().toString();

              //  Log.e("INSURANCE ", "textChange"+);
                Log.e("INSURANCE ", "Position"+dataPos);
                Log.e("INSURANCE ", "currentData"+currentData);
                Log.e("INSURANCE ", "newData"+newData);
                Log.e("INSURANCE ", "insurance no"+mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no());
             //   String changedName=insuranceMemberNumberTxt.getText().toString();
                if (currentData.equalsIgnoreCase(newData))
                {
                    FieldEdits = "0";
                }
                else
                {
                    FieldEdits = "1";
                    System.out.println("Change: "+FieldEdits);
                }

            }


        });

       insuranceExpiryDate.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable s) {
                int dataPos=0;
                Log.e("INSURANCE ", "Student ID"+stud_id);

               // Log.e("INSURANCE ", "Current text before change"+firstPolicyNumber);
                for (int in=0;in<mInsuranceDetailArrayList.size();in++)
                {
                    if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(in).getStudent_id()))
                    {
                        dataPos=in;
                    }
                }
                String currentData=mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no();
                String newData=insuranceExpiryDate.getText().toString();

              //  Log.e("INSURANCE ", "textChange"+);
                Log.e("INSURANCE ", "Position"+dataPos);
                Log.e("INSURANCE ", "currentData"+currentData);
                Log.e("INSURANCE ", "newData"+newData);
                Log.e("INSURANCE ", "insurance no"+mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no());
             //   String changedName=insuranceMemberNumberTxt.getText().toString();
                if (currentData.equalsIgnoreCase(newData))
                {
                    FieldEdits = "0";
                }
                else
                {
                    FieldEdits = "1";
                    System.out.println("Change: "+FieldEdits);
                }

            }


        });

       insurancePolicyNumberTxt.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable s) {
                int dataPos=0;
                Log.e("INSURANCE ", "Student ID"+stud_id);

               // Log.e("INSURANCE ", "Current text before change"+firstPolicyNumber);
                for (int in=0;in<mInsuranceDetailArrayList.size();in++)
                {
                    if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(in).getStudent_id()))
                    {
                        dataPos=in;
                    }
                }
                String currentData=mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no();
                String newData=insuranceExpiryDate.getText().toString();

              //  Log.e("INSURANCE ", "textChange"+);
                Log.e("INSURANCE ", "Position"+dataPos);
                Log.e("INSURANCE ", "currentData"+currentData);
                Log.e("INSURANCE ", "newData"+newData);
                Log.e("INSURANCE ", "insurance no"+mInsuranceDetailArrayList.get(dataPos).getMedical_insurence_policy_no());
             //   String changedName=insuranceMemberNumberTxt.getText().toString();
                if (currentData.equalsIgnoreCase(newData))
                {
                    FieldEdits = "0";
                }
                else
                {
                    FieldEdits = "1";
                    System.out.println("Change: "+FieldEdits);
                }

            }


        });

        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (studentsModelArrayList.size() > 0) {
//                    if (FieldEdits.equals("1")){
//                        Toast.makeText(mContext, "Cannot Change", Toast.LENGTH_SHORT).show();
//                    }else {
                    if(!isMedicalInsuranceClicked && !isPersonalInsuranceClicked)
                    {
                        //User have Both Personal and medical Insuranse
                        insuranceType="1";
                    }
                    else if (!isMedicalInsuranceClicked && isPersonalInsuranceClicked)
                    {
                        //User have no Personal and  have medical Insuranse
                        insuranceType="2";
                    }
                    else if (isMedicalInsuranceClicked && !isPersonalInsuranceClicked)
                    {
                        //User have  Personal and  no medical Insuranse
                        insuranceType="3";
                    }
                    else
                    {
                        //User have no Personal and  no medical Insuranse
                        insuranceType="0";
                    }

                    if (studentsModelArrayList.size()>1){
//                        Toast.makeText(mContext, "Contains More "+studentsModelArrayList.size(), Toast.LENGTH_SHORT).show();
                        if (SAVE_STATE.equals("0")){ // --> did not Saved
                            showAlertOKButton(getActivity(), "Alert", "Please save data", "Ok", R.drawable.exclamationicon, R.drawable.round);
                        }else {
                            //Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                            showSocialmediaList(studentsModelArrayList);
                        }
                    }else {
                        if (SAVE_STATE.equals("0")){ // --> did not Saved
                            showAlertOKButton(getActivity(), "Alert", "Please save data", "Ok", R.drawable.exclamationicon, R.drawable.round);
                        }else {
                            //Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                            showSocialmediaList(studentsModelArrayList);
                        }
                    }

//                    CheckValueEmpty(insuranceType,FieldEdits);

//                    }

                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });



        return v;
    }

    private void updateLabel2() {
        String myFormat = "yyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DATE = sdf.format(myCalendar.getTime());
        personalInsuranceExpiryDateTxt.setText(DATE);
    }

    private void updateLabel() {
        String myFormat = "yyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DATE = sdf.format(myCalendar.getTime());
        insuranceExpiryDate.setText(DATE);
    }

    private void CheckValueEmpty(String type, String FieldStatus) {

        Gson gson;
        String json = null;
        
        System.out.println("Check: Inside"+" status: "+FieldStatus + "ID: "+HEALTH_ID+" ID2: "+stud_id);
        String IncPolicyNum = insurancePolicyNumberTxt.getText().toString();
        String IncMemNum = insuranceMemberNumberTxt.getText().toString();
        String IncProText = insuranceProviderTxt.getText().toString();
        String IncExpDate = insuranceExpiryDate.getText().toString();
        String PrsIncNum = personalInsuranceNumberTxt.getText().toString();
        String PrsIncPro = personalInsuranceProviderTxt.getText().toString();
        String PrsIncExp = personalInsuranceExpiryDateTxt.getText().toString();
        String PrefHsptl = preferredHsptlTxt.getText().toString();

        InsuranceDetailModel INC_Saved = new InsuranceDetailModel();
        INC_Saved.setMedical_insurence_policy_no(IncPolicyNum);
        INC_Saved.setMedical_insurence_member_number(IncMemNum);
        INC_Saved.setMedical_insurence_provider(IncProText);
        INC_Saved.setMedical_insurence_expiry_date(IncExpDate);
        INC_Saved.setPersonal_accident_insurence_policy_no(PrsIncNum);
        INC_Saved.setPersonal_accident_insurence_provider(PrsIncPro);
        INC_Saved.setPersonal_acident_insurence_expiry_date(PrsIncExp);
        INC_Saved.setPreferred_hospital(PrefHsptl);
        INC_Saved.setStudent_id(stud_id);
        INC_Saved.setHaveMedInsurance(HaveMedINS);
        INC_Saved.setHavePersonalInsurance(HavePerINS);
        InsureArray.add(INC_Saved);

        System.out.println("STATE: "+HaveMedINS+" , "+HavePerINS);
        /*
            FieldEdits = 0 -->Empty
            FieldEdits = 1 -->Change
         */
        String REQUEST = null;

        if (HEALTH_ID.equalsIgnoreCase("")){
            FieldStatus = "0";
            REQUEST = "1";
            System.out.println("HEALTH STAT: "+HEALTH_ID+" STATUS: "+FieldStatus+" REQ: "+REQUEST);
        }else {
            REQUEST = "0";
            FieldStatus = "1";
            System.out.println("HEALTH STAT: "+HEALTH_ID+" STATUS: "+FieldStatus+" REQ: "+REQUEST);
        }

        System.out.println("IDSS: Feild: "+FieldStatus+" REQ: "+REQUEST);



        InsuranceDetailModel model = new InsuranceDetailModel();
        model.setId(HEALTH_ID);
        model.setMedical_insurence_policy_no(IncPolicyNum);
        model.setMedical_insurence_member_number(IncMemNum);
        model.setMedical_insurence_provider(IncProText);
        model.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString());
        model.setPersonal_accident_insurence_policy_no(PrsIncNum);
        model.setPersonal_accident_insurence_provider(PrsIncPro);
        model.setPersonal_acident_insurence_expiry_date(PrsIncExp);
        model.setPreferred_hospital(PrefHsptl);
        model.setStatus(FieldStatus);
        model.setNo_personal_accident_insurance(type);
        model.setRequest(REQUEST);
        model.setStudent_id(stud_id);
        model.setStudent_name(studentName.getText().toString());
        model.setHealth_detail("");
        model.setCreated_at("");
        model.setUpdated_at("");


//        PreferenceManager.saveInsureSwitchArrayList(InsureArray,mContext);

//        for (int i = 0;i<InsureArray.size();i++){
//            System.out.println("INSURE: "+InsureArray.get(i).getMedical_insurence_provider().equals(stud_id));
//        }


        if (type.equalsIgnoreCase("1"))
        {
            if (IncPolicyNum.isEmpty()
                    ||IncMemNum .isEmpty()
                    ||IncProText.isEmpty()
                    ||IncExpDate.isEmpty()
                    ||PrsIncNum .isEmpty()
                    ||PrsIncPro .isEmpty()
                    ||PrsIncExp .isEmpty()){

                showAlertOKButton(getActivity(), "Alert", "Please Enter all Mandatory Fields", "Ok", R.drawable.exclamationicon, R.drawable.round);

                System.out.println("Check: There is Empty");
            }else {
                 gson = new Gson();
                 json = gson.toJson(model);
                System.out.println("JSON: "+json);

                Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
                SAVE_STATE = "1";

                STUDENT_CLICK++;
                System.out.println("StudentSize: "+STUDENT_SIZE+" , "+STUDENT_CLICK);

                if (STUDENT_SIZE == STUDENT_CLICK  || STUDENT_SIZE < STUDENT_CLICK){
                    System.out.println("VALDI: set to 0");
                    PreferenceManager.setInsuranceCompleted(mContext,"0");
                    SAVE_STATE = "2";
                }else{
                    System.out.println("VALDI: set to 1");
                    PreferenceManager.setInsuranceCompleted(mContext,"1");
                }
            }
        }
        else  if (type.equalsIgnoreCase("2"))
        {
            if (IncPolicyNum.isEmpty()
                    ||IncMemNum .isEmpty()
                    ||IncProText.isEmpty()
                    ||IncExpDate.isEmpty()){

                showAlertOKButton(getActivity(), "Alert", "Please Enter all Mandatory Fields", "Ok", R.drawable.exclamationicon, R.drawable.round);

                System.out.println("Check: There is Empty");
            }else {
                 gson = new Gson();
                 json = gson.toJson(model);
                System.out.println("JSON: "+json);

                Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
                SAVE_STATE = "1";

                STUDENT_CLICK++;
                System.out.println("StudentSize: "+STUDENT_SIZE+" , "+STUDENT_CLICK);

                if (STUDENT_SIZE == STUDENT_CLICK || STUDENT_SIZE < STUDENT_CLICK){
                    System.out.println("VALDI: set to 0");
                    PreferenceManager.setInsuranceCompleted(mContext,"0");
                    SAVE_STATE = "2";
                }else{
                    System.out.println("VALDI: set to 1");
                    PreferenceManager.setInsuranceCompleted(mContext,"1");
                }
            }
        } else  if (type.equalsIgnoreCase("3"))
        {
            if (PrsIncNum .isEmpty()
                    ||PrsIncPro .isEmpty()
                    ||PrsIncExp .isEmpty()){

                showAlertOKButton(getActivity(), "Alert", "Please Enter all Mandatory Fields", "Ok", R.drawable.exclamationicon, R.drawable.round);

                System.out.println("Check: There is Empty");
            }else {
                 gson = new Gson();
                 json = gson.toJson(model);
                System.out.println("JSON: "+json);

                Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
                SAVE_STATE = "1";

                STUDENT_CLICK++;
                System.out.println("StudentSize: "+STUDENT_SIZE+" , "+STUDENT_CLICK);

                if (STUDENT_SIZE == STUDENT_CLICK || STUDENT_SIZE < STUDENT_CLICK){
                    System.out.println("VALDI: set to 0");
                    PreferenceManager.setInsuranceCompleted(mContext,"0");
                    SAVE_STATE = "2";
                }else{
                    System.out.println("VALDI: set to 1");
                    PreferenceManager.setInsuranceCompleted(mContext,"1");
                }
            }
        } else {
             gson   = new Gson();
             json = gson.toJson(model);
            System.out.println("JSON: "+json);

            Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
            SAVE_STATE = "1";

            STUDENT_CLICK++;
            System.out.println("StudentSize: "+STUDENT_SIZE+" , "+STUDENT_CLICK);

            if (STUDENT_SIZE == STUDENT_CLICK || STUDENT_SIZE < STUDENT_CLICK){
                System.out.println("VALDI: set to 0");
                PreferenceManager.setInsuranceCompleted(mContext,"0");
                SAVE_STATE = "2";
            }else{
                System.out.println("VALDI: set to 1");
                PreferenceManager.setInsuranceCompleted(mContext,"1");
            }
        }

        SecondArray.add(json);
        PreferenceManager.saveSecondFragmentJSONArrayList(SecondArray,mContext);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ;i <SecondArray.size();i++){
                    System.out.println("GSON: "+SecondArray.get(i));
                }
            }
        },2000);

    }

    private void showAlertOKButton(final Activity activity, String msg, String msgHead, String button, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msgHead);
        textHead.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setText(button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void getStudentsListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is INS: " + successResponse);
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
                            if (data.length() > 0)
                            {
                                //studentsModelArrayList.add(0,);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    if (dataObject.optString("alumi").equalsIgnoreCase("0")) {
                                        studentsModelArrayList.add(addStudentDetails(dataObject));
                                    }
                                }

                                STUDENT_SIZE = studentsModelArrayList.size();
                                if (STUDENT_SIZE == STUDENT_CLICK || STUDENT_SIZE < STUDENT_CLICK){
                                    System.out.println("VALDI: set to 0 Size: "+STUDENT_SIZE+" CLICK: "+STUDENT_CLICK);
                                    PreferenceManager.setInsuranceCompleted(mContext,"0");
                                }else{
                                    System.out.println("VALDI: set to 1 "+STUDENT_SIZE+" CLICK: "+STUDENT_CLICK);
                                    PreferenceManager.setInsuranceCompleted(mContext,"1");
                                }

                                alumini = studentsModelArrayList.get(0).getAlumini();
                                progressreport = studentsModelArrayList.get(0).getProgressreport();
                                studentName.setText(studentsModelArrayList.get(0).getmName());
                                stud_img = studentsModelArrayList.get(0).getmPhoto();
                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else

                                {

                                    studImg.setImageResource(R.drawable.boy);
                                }



                                stud_id = studentsModelArrayList.get(0).getmId();
                                PreferenceManager.setLeaveStudentId(mContext, stud_id);
                                PreferenceManager.setLeaveStudentName(mContext, studentsModelArrayList.get(0).getmName());
                                studClass = studentsModelArrayList.get(0).getmClass();

                                if (mInsuranceDetailArrayList.size()>0)
                                {
                                    System.out.println("SIZE: "+mInsuranceDetailArrayList.size());

                                    for (int i=0;i<mInsuranceDetailArrayList.size();i++)
                                    {
                                        System.out.println("insurance"+mInsuranceDetailArrayList.get(i).getMedical_insurence_policy_no());
                                        if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(i).getStudent_id()))
                                        {
                                             insurancePolicyNumber=mInsuranceDetailArrayList.get(i).getMedical_insurence_policy_no();
                                             inuranceMemberNumber=mInsuranceDetailArrayList.get(i).getMedical_insurence_member_number();
                                             insuranceProvider=mInsuranceDetailArrayList.get(i).getMedical_insurence_provider();
                                             medicalExpiryDate=mInsuranceDetailArrayList.get(i).getMedical_insurence_expiry_date();
                                             personalAccidentInsuranceNumber=mInsuranceDetailArrayList.get(i).getPersonal_accident_insurence_policy_no();
                                             personalAccidentProviderNumber=mInsuranceDetailArrayList.get(i).getPersonal_accident_insurence_provider();
                                             personalAccidentExpiryDate=mInsuranceDetailArrayList.get(i).getPersonal_acident_insurence_expiry_date();
                                             preferredHsptl=mInsuranceDetailArrayList.get(i).getPreferred_hospital();

                                             InsuranceDetailModel model = new InsuranceDetailModel() ;
                                                model.setMedical_insurence_policy_no(insurancePolicyNumber);
                                                model.setMedical_insurence_member_number(inuranceMemberNumber);
                                                model.setMedical_insurence_provider(insuranceProvider);
                                                model.setMedical_insurence_expiry_date(medicalExpiryDate);
                                                model.setPersonal_accident_insurence_policy_no(personalAccidentInsuranceNumber);
                                                model.setPersonal_accident_insurence_provider(personalAccidentProviderNumber);
                                                model.setPersonal_acident_insurence_expiry_date(personalAccidentExpiryDate);
                                                model.setPreferred_hospital(preferredHsptl);
                                                model.setStudent_id(stud_id);
//                                            InsureArray.add(model);


                                        }
                                    }
                                }
                                if (insurancePolicyNumber.equalsIgnoreCase(""))
                                {
                                    insurancePolicyNumberTxt.setText("");
                                }
                                else
                                {
                                    insurancePolicyNumberTxt.setText(insurancePolicyNumber);
                                }
                                if (inuranceMemberNumber.equalsIgnoreCase(""))
                                {
                                    insuranceMemberNumberTxt.setText("");
                                }
                                else
                                {
                                    insuranceMemberNumberTxt.setText(inuranceMemberNumber);
                                }
                                if (insuranceProvider.equalsIgnoreCase(""))
                                {
                                    insuranceProviderTxt.setText("");
                                }
                                else
                                {
                                    insuranceProviderTxt.setText(insuranceProvider);
                                }
                                if (medicalExpiryDate.equalsIgnoreCase(""))
                                {
                                    insuranceExpiryDate.setText("");
                                }
                                else
                                {
                                    insuranceExpiryDate.setText(medicalExpiryDate);
                                }
                                if (personalAccidentInsuranceNumber.equalsIgnoreCase(""))
                                {
                                    personalInsuranceNumberTxt.setText("");
                                }
                                else
                                {
                                    personalInsuranceNumberTxt.setText(personalAccidentInsuranceNumber);
                                }
                                if (personalAccidentProviderNumber.equalsIgnoreCase(""))
                                {
                                    personalInsuranceProviderTxt.setText("");
                                }
                                else
                                {
                                    personalInsuranceProviderTxt.setText(personalAccidentProviderNumber);
                                }
                                if (personalAccidentExpiryDate.equalsIgnoreCase(""))
                                {
                                    personalInsuranceExpiryDateTxt.setText("");
                                }
                                else
                                {
                                    personalInsuranceExpiryDateTxt.setText(personalAccidentExpiryDate);
                                }
                                if (preferredHsptl.equalsIgnoreCase(""))
                                {
                                    preferredHsptlTxt.setText("");
                                }
                                else
                                {
                                    preferredHsptlTxt.setText(preferredHsptl);
                                }
                              //  belowViewRelative.setVisibility(View.VISIBLE);
                                //newRequest.setVisibility(View.VISIBLE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                 //   getList(URL_GET_LEAVEREQUEST_LIST, stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            } else {
                              //  belowViewRelative.setVisibility(View.INVISIBLE);
                               // newRequest.setVisibility(View.INVISIBLE);

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
                         HaveMedINS = "YES";
                         HavePerINS = "YES";

                            isMedicalInsuranceClicked=false;
                            checknoMedicalInsuranceImg.setImageResource(R.drawable.uncheck);
                            insurancePolicyNumberTxt.setEnabled(true);
                            insuranceMemberNumberTxt.setEnabled(true);
                            insuranceProviderTxt.setEnabled(true);
                            insuranceExpiryDate.setEnabled(true);
                            insurancePolicyNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                            insuranceMemberNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                            insuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                            insuranceExpiryDate.setBackgroundResource(R.drawable.rect_background_grey);



                            isPersonalInsuranceClicked=false;
                            checkpersonalInsuranceImg.setImageResource(R.drawable.uncheck);
                            personalInsuranceNumberTxt.setEnabled(true);
                            personalInsuranceProviderTxt.setEnabled(true);
                            personalInsuranceExpiryDateTxt.setEnabled(true);
                            personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                            personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                            personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey);



                        dialog.dismiss();
                        if (AppUtils.isNetworkConnected(mContext)) {

                            if (PreferenceManager.getInsuranceCompleted(mContext).equalsIgnoreCase("0")){
                                SAVE_STATE = "1";
                            }else {
                                SAVE_STATE = "0";
                            }
//                            SAVE_STATE = "0";
                            FieldEdits = "0";
                            HEALTH_ID = "";
                            studentName.setText(mStudentArray.get(position).getmName());
                            stud_id = mStudentArray.get(position).getmId();
                            studClass = mStudentArray.get(position).getmClass();
                            PreferenceManager.setLeaveStudentId(mContext, stud_id);
                            PreferenceManager.setLeaveStudentName(mContext, mStudentArray.get(position).getmName());
                            stud_img = studentsModelArrayList.get(position).getmPhoto();



                            System.out.println("stud_id id****"+stud_id);
                            if (!(stud_img.equals(""))) {

                                Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                            } else

                            {

                                studImg.setImageResource(R.drawable.boy);
                            }
                            if (AppUtils.isNetworkConnected(mContext)) {
                              //  getList(URL_GET_LEAVEREQUEST_LIST, mStudentArray.get(position).getmId());
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                            System.out.println("mInsuranceDetailArrayList size"+mInsuranceDetailArrayList.size());

                            if (mInsuranceDetailArrayList.size()>0)
                            {
                                for (int i=0;i<mInsuranceDetailArrayList.size();i++)
                                {
                                    if (stud_id.equalsIgnoreCase(mInsuranceDetailArrayList.get(i).getStudent_id()))
                                    {
                                        System.out.println("mInsuranceDetailArrayList size"+mInsuranceDetailArrayList.size()+"  "+i);
                                        insurancePolicyNumber=mInsuranceDetailArrayList.get(i).getMedical_insurence_policy_no();
                                        System.out.println("mInsuranceDetailArrayList size"+mInsuranceDetailArrayList.size()+"  "+mInsuranceDetailArrayList.get(i).getMedical_insurence_policy_no());
                                        inuranceMemberNumber=mInsuranceDetailArrayList.get(i).getMedical_insurence_member_number();
                                        insuranceProvider=mInsuranceDetailArrayList.get(i).getMedical_insurence_provider();
                                        medicalExpiryDate=mInsuranceDetailArrayList.get(i).getMedical_insurence_expiry_date();
                                        personalAccidentInsuranceNumber=mInsuranceDetailArrayList.get(i).getPersonal_accident_insurence_policy_no();
                                        personalAccidentProviderNumber=mInsuranceDetailArrayList.get(i).getPersonal_accident_insurence_provider();
                                        personalAccidentExpiryDate=mInsuranceDetailArrayList.get(i).getPersonal_acident_insurence_expiry_date();
                                        preferredHsptl=mInsuranceDetailArrayList.get(i).getPreferred_hospital();
                                    }
                                }
                            }



                            if (insurancePolicyNumber.equalsIgnoreCase(""))
                            {
                                insurancePolicyNumberTxt.setText("");
                            }
                            else
                            {
                                insurancePolicyNumberTxt.setText(insurancePolicyNumber);
                            }
                            if (inuranceMemberNumber.equalsIgnoreCase(""))
                            {
                                insuranceMemberNumberTxt.setText("");
                            }
                            else
                            {
                                insuranceMemberNumberTxt.setText(inuranceMemberNumber);
                            }
                            if (insuranceProvider.equalsIgnoreCase(""))
                            {
                                insuranceProviderTxt.setText("");
                            }
                            else
                            {
                                insuranceProviderTxt.setText(insuranceProvider);
                            }
                            if (medicalExpiryDate.equalsIgnoreCase(""))
                            {
                                insuranceExpiryDate.setText("");
                            }
                            else
                            {
                                insuranceExpiryDate.setText(medicalExpiryDate);
                            }
                            if (personalAccidentInsuranceNumber.equalsIgnoreCase(""))
                            {
                                personalInsuranceNumberTxt.setText("");
                            }
                            else
                            {
                                personalInsuranceNumberTxt.setText(personalAccidentInsuranceNumber);
                            }
                            if (personalAccidentProviderNumber.equalsIgnoreCase(""))
                            {
                                personalInsuranceProviderTxt.setText("");
                            }
                            else
                            {
                                personalInsuranceProviderTxt.setText(personalAccidentProviderNumber);
                            }
                            if (personalAccidentExpiryDate.equalsIgnoreCase(""))
                            {
                                personalInsuranceExpiryDateTxt.setText("");
                            }
                            else
                            {
                                personalInsuranceExpiryDateTxt.setText(personalAccidentExpiryDate);
                            }
                            if (preferredHsptl.equalsIgnoreCase(""))
                            {
                                preferredHsptlTxt.setText("");
                            }
                            else
                            {
                                preferredHsptlTxt.setText(preferredHsptl);
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ViewSaved(mStudentArray.get(position).getmId());
                                }
                            },1000);

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

    private void ViewSaved(String stud_id) {

        for (int i = 0;i<InsureArray.size();i++){
            if (InsureArray.get(i).getStudent_id().equals(stud_id)){
                System.out.println("INSURE: "+" STATE "+InsureArray.get(i).getHaveMedInsurance()+" , "+InsureArray.get(i).getHavePersonalInsurance());

                insurancePolicyNumberTxt.setText(InsureArray.get(i).getMedical_insurence_policy_no());
                System.out.println("mInsuranceDetailArrayList DATA: "+InsureArray.get(i).getMedical_insurence_policy_no());
                insuranceMemberNumberTxt.setText(InsureArray.get(i).getMedical_insurence_member_number());
                insuranceProviderTxt.setText(InsureArray.get(i).getMedical_insurence_provider());
                insuranceExpiryDate.setText(InsureArray.get(i).getMedical_insurence_expiry_date());
                personalInsuranceNumberTxt.setText(InsureArray.get(i).getPersonal_accident_insurence_policy_no());
                personalInsuranceProviderTxt.setText(InsureArray.get(i).getPersonal_accident_insurence_provider());
                personalInsuranceExpiryDateTxt.setText(InsureArray.get(i).getPersonal_acident_insurence_expiry_date());
                preferredHsptlTxt.setText(InsureArray.get(i).getPreferred_hospital());

                if (InsureArray.get(i).getHaveMedInsurance().equals("YES")){
                    isMedicalInsuranceClicked=false;
                    checknoMedicalInsuranceImg.setImageResource(R.drawable.uncheck);
                    insurancePolicyNumberTxt.setEnabled(true);
                    insuranceMemberNumberTxt.setEnabled(true);
                    insuranceProviderTxt.setEnabled(true);
                    insuranceExpiryDate.setEnabled(true);
                    insurancePolicyNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    insuranceMemberNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    insuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    insuranceExpiryDate.setBackgroundResource(R.drawable.rect_background_grey);

                } else {
                    isMedicalInsuranceClicked=true;
                    checknoMedicalInsuranceImg.setImageResource(R.drawable.checked);
                    insurancePolicyNumberTxt.setEnabled(false);
                    insuranceMemberNumberTxt.setEnabled(false);
                    insuranceProviderTxt.setEnabled(false);
                    insuranceExpiryDate.setEnabled(false);
                    insurancePolicyNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    insuranceMemberNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    insuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    insuranceExpiryDate.setBackgroundResource(R.drawable.rect_background_grey_checked);

                }

                if (InsureArray.get(i).getHavePersonalInsurance().equals("YES")){
                    isPersonalInsuranceClicked=false;
                    checkpersonalInsuranceImg.setImageResource(R.drawable.uncheck);
                    personalInsuranceNumberTxt.setEnabled(true);
                    personalInsuranceProviderTxt.setEnabled(true);
                    personalInsuranceExpiryDateTxt.setEnabled(true);
                    personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey);

                } else {
                    isPersonalInsuranceClicked=true;
                    checkpersonalInsuranceImg.setImageResource(R.drawable.checked);
                    personalInsuranceNumberTxt.setEnabled(false);
                    personalInsuranceProviderTxt.setEnabled(false);
                    personalInsuranceExpiryDateTxt.setEnabled(false);
                    personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);

                }

                System.out.println("INSURE: "+insurancePolicyNumber+" , "
                        +inuranceMemberNumber+" , "
                        +insuranceProvider+" , "+
                        medicalExpiryDate+" , "+
                        personalAccidentInsuranceNumber+" , "+
                        personalAccidentProviderNumber+" , "+
                        personalAccidentExpiryDate+" , "+preferredHsptl);
            }
        }
    }
}

