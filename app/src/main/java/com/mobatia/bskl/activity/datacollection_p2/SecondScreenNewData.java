package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.absence.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.EditTextFontSansProRegular;
import com.mobatia.bskl.manager.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SecondScreenNewData extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
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
    int STUDENT_SIZE;
    int STUDENT_CLICK = 0;
    private Context mContext;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    TextView studentName,emptyTxt;
    String stud_id = "";
    String studClass = "";
    String alumini = "";
    String progressreport = "";
    String stud_img = "";
    ImageView studImg;
    LinearLayout mStudentSpinner,insuranceLinear;
    String SAVE_STATE="0";
    String preferredHsptl="";
    ArrayList<InsuranceDetailModel>mInsuranceDetailArrayList;
    ArrayList<InsuranceDetailModel> InsureArray = new ArrayList<>();
    ArrayList<String> SecondArray = new ArrayList<>();
    String HaveMedINS = "YES";
    String HavePerINS = "YES";
    String studentId="";
    String studentImage="";
    String studentNamePass="";
    String medInsurancePolicyNo="";
    String medInsuranceMemberNo="";
    String medInsuranceProvider="";
    String medInsuranceExpiryDate="";
    String perAccidentInsPolicyNo="";
    String perAccidentInsProvider="";
    String perAccidentInsExpiryDate="";
    String prefferedHsptl="";
    String insuranceTypeData="0";
    int dataPosition;
    String itemID="";
    boolean isInsuranceDataFound=false;
    ImageView checkInsuranceDeclaration;
    boolean isInsuranceDeclared=false;
    String declaration="0";
    String declarationValue="";
    String healthNote="";
    TextView MedicalNoteTxt;
    public SecondScreenNewData(String studentImage,String studentId,String studentName) {
        // Required empty public constructor
        this.studentId=studentId;
        this.studentImage=studentImage;
        this.studentNamePass=studentName;
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
        checkpersonalInsuranceImg=v.findViewById(R.id.checkpersonalInsuranceImg);
        personalInsuranceNumberTxt=v.findViewById(R.id.personalInsuranceNumberTxt);
        personalInsuranceProviderTxt=v.findViewById(R.id.personalInsuranceProviderTxt);
        personalInsuranceExpiryDateTxt=v.findViewById(R.id.personalInsuranceExpiryDateTxt);
        mStudentSpinner = v.findViewById(R.id.studentSpinner);
        insuranceLinear = v.findViewById(R.id.insuranceLinear);
        MedicalNoteTxt = v.findViewById(R.id.MedicalNoteTxt);
        emptyTxt = v.findViewById(R.id.emptyTxt);
        submitBtn=v.findViewById(R.id.submitBtn);
        checkInsuranceDeclaration=v.findViewById(R.id.checkInsuranceDeclaration);
        preferredHsptl = preferredHsptlTxt.getText().toString();
        System.out.println("Student Data Passed ID :"+studentId+" Image "+studentImage+"  name  "+studentNamePass);
        if (!(studentImage.equals(""))) {

            Picasso.with(mContext).load(AppUtils.replace(studentImage)).placeholder(R.drawable.boy).fit().into(studImg);
        } else

        {

            studImg.setImageResource(R.drawable.boy);
        }
        studentName.setText(studentNamePass);
    //    mInsuranceDetailArrayList= PreferenceManager.getInsuranceDetailArrayList(mContext);
        AppController.mInsuranceDetailArrayList=PreferenceManager.getInsuranceDetailArrayList(mContext);;
        if (AppController.mInsuranceDetailArrayList.size()>0)
        {
            System.out.println("if insurance array");
            for (int i=0;i<AppController.mInsuranceDetailArrayList.size();i++)
            {
                System.out.println("if insurance array inside for");
                String mID=AppController.mInsuranceDetailArrayList.get(i).getStudent_id();
                System.out.println("if insurance array inside for student id"+mID);
                if (studentId.equalsIgnoreCase(mID))

                {
                    System.out.println("if insurance array inside if for if");
                    dataPosition=i;
                    AppController.confirmingPosition=dataPosition;
                    itemID=AppController.mInsuranceDetailArrayList.get(i).getId();
                     System.out.println("item id"+AppController.mInsuranceDetailArrayList.get(i).getId());
                    medInsurancePolicyNo=AppController.mInsuranceDetailArrayList.get(i).getMedical_insurence_policy_no();
                    medInsuranceMemberNo=AppController.mInsuranceDetailArrayList.get(i).getMedical_insurence_member_number();
                    medInsuranceProvider=AppController.mInsuranceDetailArrayList.get(i).getMedical_insurence_provider();
                    medInsuranceExpiryDate=AppController.mInsuranceDetailArrayList.get(i).getMedical_insurence_expiry_date();
                    perAccidentInsPolicyNo=AppController.mInsuranceDetailArrayList.get(i).getPersonal_accident_insurence_policy_no();
                    perAccidentInsProvider=AppController.mInsuranceDetailArrayList.get(i).getPersonal_accident_insurence_provider();
                    perAccidentInsExpiryDate=AppController.mInsuranceDetailArrayList.get(i).getPersonal_acident_insurence_expiry_date();
                    prefferedHsptl=AppController.mInsuranceDetailArrayList.get(i).getPreferred_hospital();
                    insuranceTypeData=AppController.mInsuranceDetailArrayList.get(i).getNo_personal_accident_insurance();
                    declarationValue=AppController.mInsuranceDetailArrayList.get(i).getDeclaration();
                    healthNote=AppController.mInsuranceDetailArrayList.get(i).getHealth_detail();
                }
            }
        }
        System.out.println("if insurance array inside if for if fdfdfde"+perAccidentInsProvider);
        if (declarationValue.equalsIgnoreCase("1"))
        {
            System.out.println("Declaration if ");
            checkInsuranceDeclaration.setImageResource(R.drawable.check_box_header_tick);
            isInsuranceDeclared=true;
            declaration="1";
        }
        else
        {
            System.out.println("Declaration if else");
            checkInsuranceDeclaration.setImageResource(R.drawable.check_box_header);
            isInsuranceDeclared=false;
            declaration="0";
        }
        if (!medInsurancePolicyNo.equalsIgnoreCase(""))
        {
            isInsuranceDataFound=true;
        }
        else
        {
            if (!medInsuranceMemberNo.equalsIgnoreCase(""))
            {
                isInsuranceDataFound=true;
            }
            else
            {
                if (!medInsuranceProvider.equalsIgnoreCase(""))
                {
                    isInsuranceDataFound=true;
                }
                else
                {
                    if (!medInsuranceExpiryDate.equalsIgnoreCase(""))
                    {
                        isInsuranceDataFound=true;
                    }
                    else
                    {
                       if (!perAccidentInsPolicyNo.equalsIgnoreCase(""))
                       {
                           isInsuranceDataFound=true;
                       }
                       else
                       {
                           if (!perAccidentInsProvider.equalsIgnoreCase(""))
                           {
                               isInsuranceDataFound=true;
                           }
                           else {
                               if (!perAccidentInsExpiryDate.equalsIgnoreCase(""))
                               {
                                   isInsuranceDataFound=true;
                               }
                               else
                               {
                                   isInsuranceDataFound=false;
                               }
                           }
                       }
                    }
                }
            }
        }

        if(healthNote.equalsIgnoreCase(""))
        {
            MedicalNoteTxt.setText("No medical concerns, please contact the school nurse if circumstances have changed");
        }
        else
        {
            MedicalNoteTxt.setText(healthNote);
        }
        MedicalNoteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showInsuranceInfo(mContext, "Alert", getString(R.string.insurance_data_click_popup), R.drawable.exclamationicon, R.drawable.round);

            }
        });
        System.out.println("Item id"+itemID);
//        if (!isInsuranceDataFound)
//        {
//            insuranceLinear.setVisibility(View.GONE);
//            emptyTxt.setVisibility(View.VISIBLE);
//            emptyTxt.setText("No medical concerns, please contact the school nurse if circumstances have changed");
//        }
//        else
//        {
//            insuranceLinear.setVisibility(View.VISIBLE);
//            emptyTxt.setVisibility(View.GONE);
//        }

        System.out.println("insurance data type on page start"+insuranceTypeData);
        if (insuranceTypeData.equalsIgnoreCase("1"))
        {
            System.out.println("It works inside insurance 1");
            isPersonalInsuranceClicked=true;
            checkpersonalInsuranceImg.setImageResource(R.drawable.check_box_header_tick);
            personalInsuranceNumberTxt.setEnabled(false);
            personalInsuranceProviderTxt.setEnabled(false);
            personalInsuranceExpiryDateTxt.setEnabled(false);
            personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
            personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
            personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
            personalInsuranceNumberTxt.setHint(R.string.personal_insurance_policy_number);
            personalInsuranceProviderTxt.setHint(R.string.personal_insurance_provider);
            personalInsuranceExpiryDateTxt.setHint(R.string.personal_insurance_expiry_date);
        }
        else
        {
            isPersonalInsuranceClicked=false;
            checkpersonalInsuranceImg.setImageResource(R.drawable.check_box_header);
            personalInsuranceNumberTxt.setEnabled(true);
            personalInsuranceProviderTxt.setEnabled(true);
            personalInsuranceExpiryDateTxt.setEnabled(true);
            personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
            personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
            personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey);
            personalInsuranceNumberTxt.setHint(R.string.personal_insurance_policy_number_red);
            personalInsuranceProviderTxt.setHint(R.string.personal_insurance_provider_red);
            personalInsuranceExpiryDateTxt.setHint(R.string.personal_insurance_expiry_date_red);
        }

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
        if (!medInsurancePolicyNo.equalsIgnoreCase(""))
        {
            insurancePolicyNumberTxt.setText(medInsurancePolicyNo);
        }
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
        if (!medInsuranceMemberNo.equalsIgnoreCase(""))
        {
            insuranceMemberNumberTxt.setText(medInsuranceMemberNo);
        }
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
        if (!medInsuranceProvider.equalsIgnoreCase(""))
        {
            insuranceProviderTxt.setText(medInsuranceProvider);
        }
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

        if (!medInsuranceExpiryDate.equalsIgnoreCase(""))
        {
            insuranceExpiryDate.setText(AppUtils.dateConversionMMM(medInsuranceExpiryDate));
        }
        //Personal Insurance

        System.out.println("Api call not working");

        if (!perAccidentInsPolicyNo.equalsIgnoreCase(""))
        {
            personalInsuranceNumberTxt.setText(perAccidentInsPolicyNo);
        }

        if (!perAccidentInsProvider.equalsIgnoreCase(""))
        {
            personalInsuranceProviderTxt.setText(perAccidentInsProvider);
        }
        if (!perAccidentInsExpiryDate.equalsIgnoreCase(""))
        {
            personalInsuranceExpiryDateTxt.setText(AppUtils.dateConversionMMM(perAccidentInsExpiryDate));
        }
        if (!prefferedHsptl.equalsIgnoreCase(""))
        {
            preferredHsptlTxt.setText(prefferedHsptl);
        }
        CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //ShowDiscardDialog(mContext, "Confirm?", "Do you want to Discard changes?", R.drawable.questionmark_icon, R.drawable.round);
                    if (AppController.isInsuranceEdited || AppController.isPassportEdited)
                    {
                        int studentPos=-1;
                        String id="";
                        String studName="";
                        String studClass="";
                        String studSection="";
                        String studHouse="";
                        String studPhoto="";
                        String studProgressReport="";
                        String studAlumini="";
                        String studentID=studentId;
                        if (AppController.mStudentDataArrayList.size()>0)
                        {

                            for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                            {
                                if (AppController.mStudentDataArrayList.get(i).getmId().equalsIgnoreCase(studentID))
                                {
                                    studentPos=i;
                                    id= AppController.mStudentDataArrayList.get(i).getmId();
                                    studName= AppController.mStudentDataArrayList.get(i).getmName();
                                    studClass= AppController.mStudentDataArrayList.get(i).getmClass();
                                    studSection= AppController.mStudentDataArrayList.get(i).getmSection();
                                    studHouse= AppController.mStudentDataArrayList.get(i).getmHouse();
                                    studPhoto= AppController.mStudentDataArrayList.get(i).getmPhoto();
                                    studProgressReport=AppController.mStudentDataArrayList.get(i).getProgressReport();
                                    studAlumini= AppController.mStudentDataArrayList.get(i).getAlumini();
                                }
                            }
                        }
                        StudentModelNew model=new StudentModelNew();
                        model.setmId(id);
                        model.setmName(studName);
                        model.setmClass(studClass);
                        model.setmSection(studSection);
                        model.setmHouse(studHouse);
                        model.setmPhoto(studPhoto);
                        model.setProgressReport(studProgressReport);
                        model.setAlumini(studAlumini);
                        model.setConfirmed(false);
                        AppController.mStudentDataArrayList.remove(studentPos);
                        AppController.mStudentDataArrayList.add(studentPos,model);
                        AppController.isInsuranceEdited=false;
                        AppController.isPassportEdited=false;
                        AppController.confirmingPosition=-1;
                        PreferenceManager.getInsuranceStudentList(mContext).clear();
                        PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                        PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                        getActivity().finish();
                    }


                else
                {
                    getActivity().finish();
                }
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        for (int in=0;in<AppController.mInsuranceDetailArrayList.size();in++)
        {
            if (stud_id.equalsIgnoreCase(AppController.mInsuranceDetailArrayList.get(in).getStudent_id()))
            {
                C_positoin=in;
            }
        }
        HEALTH_ID = AppController.mInsuranceDetailArrayList.get(C_positoin).getId();

//        insurancePolicyNumberTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showInsuranceInfo(mContext, "Alert", getString(R.string.insurance_data_click_popup), R.drawable.exclamationicon, R.drawable.round);
//
//            }
//        });
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
            public void afterTextChanged(Editable s)
            {
                String newData=insuranceProviderTxt.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //   String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();
//
//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(newData);
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setDeclaration(declaration);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);

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
            public void afterTextChanged(Editable s)

            {
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //            String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();
                String newData=insurancePolicyNumberTxt.getText().toString();
//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                //  Log.e("INSURANCE ", "textChange"+);

                System.out.println("InsuranceData before change"+AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_policy_no());
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(newData);
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                mModel.setDeclaration(declaration);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                Log.e("Ins asize", String.valueOf(AppController.mInsuranceDetailArrayList.size()));
                for (int j=0;j<AppController.mInsuranceDetailArrayList.size();j++)
                {
                    Log.e("Ins after",AppController.mInsuranceDetailArrayList.get(j).getId() +j);

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
                String newData=insuranceExpiryDate.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //       String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                mModel.setDeclaration(declaration);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);

            }


        });
        personalInsuranceNumberTxt.addTextChangedListener(new TextWatcher()
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
            public void afterTextChanged(Editable s)
            {
                String newData=personalInsuranceNumberTxt.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //   String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(newData);
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setDeclaration(declaration);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
            }


        });
        personalInsuranceProviderTxt.addTextChangedListener(new TextWatcher()
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
            public void afterTextChanged(Editable s)
            {
                String newData=personalInsuranceProviderTxt.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //    String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(newData);
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setDeclaration(declaration);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
            }


        });
        personalInsuranceExpiryDateTxt.addTextChangedListener(new TextWatcher()
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
            public void afterTextChanged(Editable s)
            {
                String newData=personalInsuranceExpiryDateTxt.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //    String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setDeclaration(declaration);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
            }


        });
        preferredHsptlTxt.addTextChangedListener(new TextWatcher()
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
            public void afterTextChanged(Editable s)
            {
                String newData=preferredHsptlTxt.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                //     String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();
//
//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(newData);
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setDeclaration(declaration);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
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
                String newData=insuranceMemberNumberTxt.getText().toString();
                AppController.isInsuranceEdited=true;
                String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                String dataStatus="1";
                // String dataInsuranceType="0";
                String dataRequest="0";
                String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
                InsuranceDetailModel mModel=new InsuranceDetailModel();
                mModel.setId(dataId);
                mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                mModel.setMedical_insurence_member_number(newData);
                mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                }
                else
                {
                    mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                }
                mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                {
                    mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                }
                else
                {
                    mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                }
                mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                mModel.setStatus(dataStatus);
                mModel.setNo_personal_accident_insurance(insuranceTypeData);
                mModel.setRequest(dataRequest);
                mModel.setStudent_id(studentId);
                mModel.setDeclaration(declaration);
                mModel.setStudent_name(studentNamePass);
                mModel.setHealth_detail(dataHealthDetail);
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                AppController.mInsuranceDetailArrayList.remove(dataPosition);
                AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
            }


        });

        checkpersonalInsuranceImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (isPersonalInsuranceClicked) {
                    insuranceTypeData="0";
                    isPersonalInsuranceClicked=false;
                    checkpersonalInsuranceImg.setImageResource(R.drawable.check_box_header);
                    personalInsuranceNumberTxt.setEnabled(true);
                    personalInsuranceProviderTxt.setEnabled(true);
                    personalInsuranceExpiryDateTxt.setEnabled(true);
                    personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    personalInsuranceNumberTxt.setHint(R.string.personal_insurance_policy_number_red);
                    personalInsuranceProviderTxt.setHint(R.string.personal_insurance_provider_red);
                    personalInsuranceExpiryDateTxt.setHint(R.string.personal_insurance_expiry_date_red);
                    HavePerINS = "YES";
                    AppController.isInsuranceEdited=true;
                    String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                    String dataStatus="1";
                    String dataRequest="0";
                    String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                    String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                    String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();
                    InsuranceDetailModel mModel=new InsuranceDetailModel();
                    mModel.setId(dataId);
                    mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                    mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                    mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                    if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                    }
                    else
                    {
                        mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                    }
                    mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                    mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                    if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                    }
                    else
                    {
                        mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                    }
                    mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                    mModel.setStatus(dataStatus);
                    mModel.setNo_personal_accident_insurance(insuranceTypeData);
                    mModel.setRequest(dataRequest);
                    mModel.setStudent_id(studentId);
                    mModel.setStudent_name(studentNamePass);
                    mModel.setDeclaration(declaration);
                    mModel.setHealth_detail(dataHealthDetail);
                    mModel.setCreated_at(dataCreatedAt);
                    mModel.setUpdated_at(dataUpdatedAt);
                    AppController.mInsuranceDetailArrayList.remove(dataPosition);
                    AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                    PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                    PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                }
                else {


                    insuranceTypeData="1";
                    isPersonalInsuranceClicked=true;
                    checkpersonalInsuranceImg.setImageResource(R.drawable.check_box_header_tick);
                    personalInsuranceNumberTxt.setEnabled(false);
                    personalInsuranceProviderTxt.setEnabled(false);
                    personalInsuranceExpiryDateTxt.setEnabled(false);
                    personalInsuranceNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceProviderTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceExpiryDateTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    personalInsuranceNumberTxt.setHint(R.string.personal_insurance_policy_number);
                    personalInsuranceProviderTxt.setHint(R.string.personal_insurance_provider);
                    personalInsuranceExpiryDateTxt.setHint(R.string.personal_insurance_expiry_date);
                    HavePerINS = "NO";
                    AppController.isInsuranceEdited=true;
                    String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
                    String dataStatus="1";
                    String dataRequest="0";
                    String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
                    String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
                    String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();
                    InsuranceDetailModel mModel=new InsuranceDetailModel();
                    mModel.setId(dataId);
                    mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
                    mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
                    mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
                    if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

                    }
                    else
                    {
                        mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

                    }
                    mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
                    mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
                    if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

                    }
                    else
                    {
                        mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

                    }
                    //  mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());
                    mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
                    mModel.setStatus(dataStatus);
                    mModel.setNo_personal_accident_insurance(insuranceTypeData);
                    mModel.setRequest(dataRequest);
                    mModel.setDeclaration(declaration);
                    mModel.setStudent_id(studentId);
                    mModel.setStudent_name(studentNamePass);
                    mModel.setHealth_detail(dataHealthDetail);
                    mModel.setCreated_at(dataCreatedAt);
                    mModel.setUpdated_at(dataUpdatedAt);
                    AppController.mInsuranceDetailArrayList.remove(dataPosition);
                    AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                    PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                    PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                }
                System.out.println("STATE: "+HavePerINS);
            }
        });
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
                AppUtils.hideKeyBoard(mContext);
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        personalInsuranceExpiryDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        checkInsuranceDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInsuranceDeclared)
                {
                    checkInsuranceDeclaration.setImageResource(R.drawable.check_box_header);
                    isInsuranceDeclared=false;
                    declaration="0";
                    InsuranceDetailModel mModel=new InsuranceDetailModel();
                    mModel.setId(AppController.mInsuranceDetailArrayList.get(dataPosition).getId());
                    mModel.setStudent_id(AppController.mInsuranceDetailArrayList.get(dataPosition).getStudent_id());
                    mModel.setStudent_name(AppController.mInsuranceDetailArrayList.get(dataPosition).getStudent_name());
                    mModel.setHealth_detail(AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail());
                    mModel.setNo_personal_accident_insurance(AppController.mInsuranceDetailArrayList.get(dataPosition).getNo_personal_accident_insurance());
                    mModel.setMedical_insurence_policy_no(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_policy_no());
                    mModel.setMedical_insurence_member_number(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_member_number());
                    mModel.setMedical_insurence_provider(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_provider());
                    mModel.setMedical_insurence_expiry_date(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_expiry_date());
                    mModel.setPersonal_accident_insurence_policy_no(AppController.mInsuranceDetailArrayList.get(dataPosition).getPersonal_accident_insurence_policy_no());
                    mModel.setPersonal_accident_insurence_provider(AppController.mInsuranceDetailArrayList.get(dataPosition).getPersonal_accident_insurence_provider());
                    mModel.setPersonal_acident_insurence_expiry_date(AppController.mInsuranceDetailArrayList.get(dataPosition).getPersonal_acident_insurence_expiry_date());
                    mModel.setPreferred_hospital(AppController.mInsuranceDetailArrayList.get(dataPosition).getPreferred_hospital());
                    mModel.setCreated_at(AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at());
                    mModel.setUpdated_at(AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at());
                    mModel.setDeclaration(declaration);
                    mModel.setStatus("1");
                    mModel.setRequest("0");
                    AppController.isInsuranceEdited=true;
                    AppController.mInsuranceDetailArrayList.remove(dataPosition);
                    AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                    PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                    PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);


                }
                else
                {
                    checkInsuranceDeclaration.setImageResource(R.drawable.check_box_header_tick);
                    isInsuranceDeclared=true;
                    declaration="1";
                    AppController.isInsuranceEdited=true;
                    InsuranceDetailModel mModel=new InsuranceDetailModel();
                    mModel.setId(AppController.mInsuranceDetailArrayList.get(dataPosition).getId());
                    mModel.setStudent_id(AppController.mInsuranceDetailArrayList.get(dataPosition).getStudent_id());
                    mModel.setStudent_name(AppController.mInsuranceDetailArrayList.get(dataPosition).getStudent_name());
                    mModel.setHealth_detail(AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail());
                    mModel.setNo_personal_accident_insurance(AppController.mInsuranceDetailArrayList.get(dataPosition).getNo_personal_accident_insurance());
                    mModel.setMedical_insurence_policy_no(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_policy_no());
                    mModel.setMedical_insurence_member_number(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_member_number());
                    mModel.setMedical_insurence_provider(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_provider());
                    mModel.setMedical_insurence_expiry_date(AppController.mInsuranceDetailArrayList.get(dataPosition).getMedical_insurence_expiry_date());
                    mModel.setPersonal_accident_insurence_policy_no(AppController.mInsuranceDetailArrayList.get(dataPosition).getPersonal_accident_insurence_policy_no());
                    mModel.setPersonal_accident_insurence_provider(AppController.mInsuranceDetailArrayList.get(dataPosition).getPersonal_accident_insurence_provider());
                    mModel.setPersonal_acident_insurence_expiry_date(AppController.mInsuranceDetailArrayList.get(dataPosition).getPersonal_acident_insurence_expiry_date());
                    mModel.setPreferred_hospital(AppController.mInsuranceDetailArrayList.get(dataPosition).getPreferred_hospital());
                    mModel.setCreated_at(AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at());
                    mModel.setUpdated_at(AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at());
                    mModel.setDeclaration(declaration);
                    mModel.setStatus("1");
                    mModel.setRequest("0");
                    AppController.mInsuranceDetailArrayList.remove(dataPosition);
                    AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
                    PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
                    PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                }
            }
        });

        return v;
    }
    private void updateLabel2() {
        String myFormat = "yyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DATE = sdf.format(myCalendar.getTime());
        personalInsuranceExpiryDateTxt.setText(AppUtils.dateConversionMMM(DATE));
        String newData=insuranceExpiryDate.getText().toString();
        AppController.isInsuranceEdited=true;
        String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
        String dataStatus="1";
        //       String dataInsuranceType="0";
        String dataRequest="0";
        String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
        String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
        InsuranceDetailModel mModel=new InsuranceDetailModel();
        mModel.setId(dataId);
        mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
        mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
        mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
        if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
        {
            mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

        }
        else
        {
            mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

        }
        mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
        mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
        if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
        {
            mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

        }
        else
        {
            mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

        }
        mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
        mModel.setStatus(dataStatus);
        mModel.setNo_personal_accident_insurance(insuranceTypeData);
        mModel.setRequest(dataRequest);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setHealth_detail(dataHealthDetail);
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setDeclaration(declaration);
        AppController.mInsuranceDetailArrayList.remove(dataPosition);
        AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
        PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
        PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
    }

    private void updateLabel() {
        String myFormat = "yyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DATE = sdf.format(myCalendar.getTime());
        insuranceExpiryDate.setText(AppUtils.dateConversionMMM(DATE));
        String newData=insuranceExpiryDate.getText().toString();
        AppController.isInsuranceEdited=true;
        String dataId=AppController.mInsuranceDetailArrayList.get(dataPosition).getId();
        String dataStatus="1";
        //       String dataInsuranceType="0";
        String dataRequest="0";
        String dataHealthDetail=AppController.mInsuranceDetailArrayList.get(dataPosition).getHealth_detail();
        String dataCreatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mInsuranceDetailArrayList.get(dataPosition).getUpdated_at();

//                if (isPersonalInsuranceClicked)
//                {
//                    System.out.println("Personal clicked");
//                    dataInsuranceType="2";
//                }
//                else
//                {
//                    System.out.println("Personal clicked not");
//                    dataInsuranceType="0";
//                }
        InsuranceDetailModel mModel=new InsuranceDetailModel();
        mModel.setId(dataId);
        mModel.setMedical_insurence_policy_no(insurancePolicyNumberTxt.getText().toString().trim());
        mModel.setMedical_insurence_member_number(insuranceMemberNumberTxt.getText().toString().trim());
        mModel.setMedical_insurence_provider(insuranceProviderTxt.getText().toString().trim());
        if (!insuranceExpiryDate.getText().toString().trim().equalsIgnoreCase(""))
        {
            mModel.setMedical_insurence_expiry_date(AppUtils.dateConversionYToD(insuranceExpiryDate.getText().toString().trim()));

        }
        else
        {
            mModel.setMedical_insurence_expiry_date(insuranceExpiryDate.getText().toString().trim());

        }
        mModel.setPersonal_accident_insurence_policy_no(personalInsuranceNumberTxt.getText().toString().trim());
        mModel.setPersonal_accident_insurence_provider(personalInsuranceProviderTxt.getText().toString().trim());
        if (!personalInsuranceExpiryDateTxt.getText().toString().trim().equalsIgnoreCase(""))
        {
            mModel.setPersonal_acident_insurence_expiry_date(AppUtils.dateConversionYToD(personalInsuranceExpiryDateTxt.getText().toString().trim()));

        }
        else
        {
            mModel.setPersonal_acident_insurence_expiry_date(personalInsuranceExpiryDateTxt.getText().toString().trim());

        }
        mModel.setPreferred_hospital(preferredHsptlTxt.getText().toString().trim());
        mModel.setStatus(dataStatus);
        mModel.setNo_personal_accident_insurance(insuranceTypeData);
        mModel.setRequest(dataRequest);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setHealth_detail(dataHealthDetail);
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setDeclaration(declaration);
        AppController.mInsuranceDetailArrayList.remove(dataPosition);
        AppController.mInsuranceDetailArrayList.add(dataPosition,mModel);
        PreferenceManager.getInsuranceDetailArrayList(mContext).clear();
        PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);

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
    private void ShowDiscardDialog(Context mContext, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_discard_data);
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
                AppController.isInsuranceEdited=false;
                AppController.isPassportEdited=false;
                AppController.confirmingPosition=-1;
                dialog.dismiss();
                getActivity().finish();

             //   changedField = "";
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
    private void showInsuranceInfo(Context mContext, String msgHead, String msg, int ico, int bgIcon)
    {
        final Dialog dialog = new Dialog(mContext);
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
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialog.show();
    }

}

