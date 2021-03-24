package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.EmergencyDetails;
import com.mobatia.bskl.activity.data_collection.KinDetails;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportImageModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.datacollection_p2.model.VISAimageModel;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.report.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.CustomDialog;
import com.mobatia.bskl.volleywrappermanager.CustomProgressBar;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mobatia.bskl.constants.JSONConstants.JTAG_RESPONSE;
import static com.mobatia.bskl.constants.JSONConstants.JTAG_RESPONSECODE;
import static com.mobatia.bskl.constants.JSONConstants.JTAG_STATUSCODE;

public class SecondScreenDataCollection extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    /**
     * fields
     */
    private static final int NUMBER_OF_PAGES = 3;
    private RadioGroup radioGroup;
    public ViewPager pager;
    private ImageView nextBtn, backBtn;
    private TextView submitBtn;
    private String OverallValue;
    Context mContext;
    String closeMsgKin;
    String closeMsgLocal;
    JSONArray own_details = null;
    JSONArray kin_details = null;
    JSONArray emergency_details = null;
    LinearLayout bottomLinear;
    String studentId="";
    String studentImage="";
    String studentName="";
    Bundle extras;
    RadioButton radioButton1,radioButton2,radioButton3;
    ArrayList<InsuranceDetailModel> mInsuranceDetailArrayList;
    /**
     * {@inheritDoc}
     */

    int previousPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection_home);
        mContext = this;
        extras=getIntent().getExtras();
        if(extras!=null){
            studentId=extras.getString("studentId");
            studentName=extras.getString("studentName");
            studentImage=extras.getString("studentImage");
        }
        pager = findViewById(R.id.viewPager);
        bottomLinear = findViewById(R.id.bottomLinear);
        pager.setAdapter(new SecondScreenDataCollection.MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        pager.setOffscreenPageLimit(4);
        radioGroup = findViewById(R.id.radiogroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        submitBtn = findViewById(R.id.submit);
        nextBtn = findViewById(R.id.nextImg);
        backBtn = findViewById(R.id.backImg);
        radioGroup.setOnCheckedChangeListener(this);
        if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1")|| PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
        else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5")  || PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
        {
//            bottomLinear.setVisibility(View.VISIBLE);
//            radioButton1.setVisibility(View.GONE);
//            radioButton2.setVisibility(View.VISIBLE);
//            radioButton3.setVisibility(View.GONE);
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.INVISIBLE);
            radioButton2.setVisibility(View.INVISIBLE);
            radioButton3.setVisibility(View.INVISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
            submitBtn.setText("Confirm");
            submitBtn.setBackgroundResource(R.drawable.rect_confirm_blue_button);
            nextBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
        else if ((PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3")))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.INVISIBLE);
            radioButton2.setVisibility(View.INVISIBLE);
            radioButton3.setVisibility(View.INVISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
            submitBtn.setText("Confirm");
            submitBtn.setBackgroundResource(R.drawable.rect_confirm_blue_button);
            nextBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
        else
        {

            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.GONE);
            radioButton2.setVisibility(View.GONE);
            radioButton3.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
            submitBtn.setText("Confirm");
            submitBtn.setBackgroundResource(R.drawable.rect_confirm_blue_button);
            nextBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);

        }

        closeMsgKin = "Next of Kin must not be existing parent contact for family (cross check email/phone number with mother/father/parent record)";
        closeMsgLocal = "Local Emergency Contact must not be existing parent contact for family (cross check email/phone number with mother/father/parent record)";
        SharedPreferences.Editor editor = getSharedPreferences("BSKL", MODE_PRIVATE).edit();
        editor.putString("validationkin", "0");
        editor.putString("validationlocal", "0");
        editor.apply();
        // System.out.println("access_token getting " + PreferenceManager.getAccessToken(mContext));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });



        previousPage = 1;



        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //       Log.e("onPageState: ", String.valueOf(state));
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
                {
                    if (emptyValueCheckInsurance().equalsIgnoreCase(""))
                    {
                       System.out.println("Insrance empty works");
                        if (emptyValueCheckPassport().equalsIgnoreCase(""))
                        {
                            System.out.println("Insrance empty works passport");
                            int studentPos=-1;
                            String id="";
                            String studName="";
                            String studClass="";
                            String studSection="";
                            String studHouse="";
                            String studPhoto="";
                            String studProgressReport="";
                            String studAlumini="";
                            String studentID=AppController.mPassportDetailArrayList.get(AppController.confirmingPosition).getStudent_id();
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
                            model.setConfirmed(true);
                            AppController.mStudentDataArrayList.remove(studentPos);
                            AppController.mStudentDataArrayList.add(studentPos,model);
                            AppController.isInsuranceEdited=false;
                            AppController.isPassportEdited=false;
                            AppController.confirmingPosition=-1;
                            PreferenceManager.getInsuranceStudentList(mContext).clear();
                            PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                            PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                            PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                            finish();
                           // Toast.makeText(mContext,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String emptyFeild=emptyValueCheckPassport();
                            ShowCondition(emptyFeild,"1");
                        }

                    }
                    else
                    {
                        String emptyFeild=emptyValueCheckInsurance();
                        System.out.println("Empty value check"+emptyFeild);
                        ShowCondition(emptyFeild,"0");
                    }
                    //emptyValueCheck();
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
                {
                    if (emptyValueCheckInsurance().equalsIgnoreCase(""))
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
                        String studentID=AppController.mInsuranceDetailArrayList.get(AppController.confirmingPosition).getStudent_id();
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
                        model.setConfirmed(true);
                        AppController.mStudentDataArrayList.remove(studentPos);
                        AppController.mStudentDataArrayList.add(studentPos,model);
                        AppController.isInsuranceEdited=false;
                        AppController.isPassportEdited=false;
                        AppController.confirmingPosition=-1;
                        PreferenceManager.getInsuranceStudentList(mContext).clear();
                        PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                        PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                       // PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                        finish();
                       // Toast.makeText(mContext,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String emptyFeild=emptyValueCheckInsurance();
                        System.out.println("Empty value check"+emptyFeild);
                        ShowCondition(emptyFeild,"0");
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("4"))
                {
                    if (emptyValueCheckPassport().equalsIgnoreCase(""))
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
                        String studentID=AppController.mPassportDetailArrayList.get(AppController.confirmingPosition).getStudent_id();
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
                        model.setConfirmed(true);
                        AppController.mStudentDataArrayList.remove(studentPos);
                        AppController.mStudentDataArrayList.add(studentPos,model);
                        AppController.isInsuranceEdited=false;
                        AppController.isPassportEdited=false;
                        AppController.confirmingPosition=-1;
                        PreferenceManager.getInsuranceStudentList(mContext).clear();
                        PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                       // PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                        finish();
                        //Toast.makeText(mContext,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String emptyFeild=emptyValueCheckPassport();
                        ShowCondition(emptyFeild,"1");
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5"))
                {
                    if (emptyValueCheckInsurance().equalsIgnoreCase(""))
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
                        String studentID=AppController.mInsuranceDetailArrayList.get(AppController.confirmingPosition).getStudent_id();
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
                        model.setConfirmed(true);
                        AppController.mStudentDataArrayList.remove(studentPos);
                        AppController.mStudentDataArrayList.add(studentPos,model);
                        AppController.isInsuranceEdited=false;
                        AppController.isPassportEdited=false;
                        AppController.confirmingPosition=-1;
                        PreferenceManager.getInsuranceStudentList(mContext).clear();
                        PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                        PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                        // PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                        finish();
                     //   Toast.makeText(mContext,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String emptyFeild=emptyValueCheckInsurance();
                        System.out.println("Empty value check"+emptyFeild);
                        ShowCondition(emptyFeild,"0");
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
                {
                    if (emptyValueCheckInsurance().equalsIgnoreCase(""))
                    {

                        if (emptyValueCheckPassport().equalsIgnoreCase(""))
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
                            String studentID=AppController.mPassportDetailArrayList.get(AppController.confirmingPosition).getStudent_id();
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
                            model.setConfirmed(true);
                            AppController.mStudentDataArrayList.remove(studentPos);
                            AppController.mStudentDataArrayList.add(studentPos,model);
                            AppController.isInsuranceEdited=false;
                            AppController.isPassportEdited=false;
                            AppController.confirmingPosition=-1;
                            PreferenceManager.getInsuranceStudentList(mContext).clear();
                            PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                            PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                            PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                            finish();
                          //  Toast.makeText(mContext,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String emptyFeild=emptyValueCheckPassport();
                            ShowCondition(emptyFeild,"1");
                        }

                    }
                    else
                    {
                        String emptyFeild=emptyValueCheckInsurance();
                        System.out.println("Empty value check"+emptyFeild);
                        ShowCondition(emptyFeild,"0");
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
                {
                    if (emptyValueCheckPassport().equalsIgnoreCase(""))
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
                        String studentID=AppController.mPassportDetailArrayList.get(AppController.confirmingPosition).getStudent_id();
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
                        model.setConfirmed(true);
                        AppController.mStudentDataArrayList.remove(studentPos);
                        AppController.mStudentDataArrayList.add(studentPos,model);
                        AppController.isInsuranceEdited=false;
                        AppController.isPassportEdited=false;
                        AppController.confirmingPosition=-1;
                        PreferenceManager.getInsuranceStudentList(mContext).clear();
                        PreferenceManager.saveInsuranceStudentList(AppController.mStudentDataArrayList,mContext);
                        // PreferenceManager.saveInsuranceDetailArrayList(AppController.mInsuranceDetailArrayList,mContext);
                        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                        finish();
                       // Toast.makeText(mContext,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String emptyFeild=emptyValueCheckPassport();
                        ShowCondition(emptyFeild,"1");
                    }
                }
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.GONE);
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    radioGroup.check(R.id.radioButton2);
                    nextBtn.setVisibility(View.INVISIBLE);
                    backBtn.setVisibility(View.VISIBLE);
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                    submitBtn.setText("Confirm");
                    submitBtn.setBackgroundResource(R.drawable.rect_confirm_blue_button);
                    break;
//                case 2:
//                    radioGroup.check(R.id.radioButton3);
//                    nextBtn.setVisibility(View.INVISIBLE);
//                    backBtn.setVisibility(View.VISIBLE);
//                    submitBtn.setVisibility(View.VISIBLE);
//                    break;
                default:
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
            }
        }
        else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("2"))
        {
            bottomLinear.setVisibility(View.GONE);

        }
        else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
        {
            bottomLinear.setVisibility(View.GONE);

        }
        else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("4"))
        {
            bottomLinear.setVisibility(View.GONE);

        }
        else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            switch (position) {
                case 0:

                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    radioGroup.check(R.id.radioButton2);
                    nextBtn.setVisibility(View.INVISIBLE);
                    backBtn.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
            }
        }
        else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            switch (position) {
                case 0:
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.GONE);
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    radioGroup.check(R.id.radioButton2);
                    nextBtn.setVisibility(View.INVISIBLE);
                    backBtn.setVisibility(View.VISIBLE);
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                    submitBtn.setText("Confirm");
                    submitBtn.setBackgroundResource(R.drawable.rect_confirm_blue_button);
                    break;
//                case 2:
//                    radioGroup.check(R.id.radioButton3);
//                    nextBtn.setVisibility(View.INVISIBLE);
//                    backBtn.setVisibility(View.VISIBLE);
//                    submitBtn.setVisibility(View.VISIBLE);
//                    break;
                default:
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            switch (position) {

                case 0:

                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    radioGroup.check(R.id.radioButton2);
                    nextBtn.setVisibility(View.INVISIBLE);
                    backBtn.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();

    }

    /**
     * When the pager is automatically setting to the current page
     *
     * @param position
     */
    @Override
    public void onPageScrollStateChanged(int position) {

    }

    /**
     * On checked listener to Radio Buttons.
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                pager.setCurrentItem(0);
                break;
            case R.id.radioButton2:
                pager.setCurrentItem(1);
                break;
//            case R.id.radioButton3:
//                pager.setCurrentItem(2);
//                break;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * Custom PagerAdapter class
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        /**
         * Constructor
         *
         * @param fm
         */
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment based on the position.
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNewData(studentImage,studentId,studentName);
                    case 1:
                        return new ThirdScreenNewData(studentImage,studentId,studentName);

                    default:
                        return new SecondScreenNewData(studentImage,studentId,studentName);
                }
            }
//            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("2"))
//            {
//                switch (position) {
//
//                    case 0:
//                        return new FirstScreen();
//                    default:
//                        return new FirstScreen();
//                }
//            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNewData(studentImage,studentId,studentName);

                    default:
                        return new SecondScreenNewData(studentImage,studentId,studentName);
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("4"))
            {
                switch (position) {

                    case 0:
                        return new ThirdScreenNewData(studentImage,studentId,studentName);
                    default:
                        return new ThirdScreenNewData(studentImage,studentId,studentName);
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNewData(studentImage,studentId,studentName);

                    default:
                        return new SecondScreenNewData(studentImage,studentId,studentName);
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNewData(studentImage,studentId,studentName);
                    case 1:
                        return new ThirdScreenNewData(studentImage,studentId,studentName);
                    default:
                        return new SecondScreenNewData(studentImage,studentId,studentName);
                }
            }
            else
            {
                switch (position) {

                    case 0:
                        return new ThirdScreenNewData(studentImage,studentId,studentName);
                    default:
                        return new ThirdScreenNewData(studentImage,studentId,studentName);
                }
            }

        }

        /**
         * Return the number of pages.
         *
         * @return
         */
        @Override
        public int getCount() {

            if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
            {
                return 2;
            }
            else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5")||PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
            {
                return 1;
            }
            else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
            {
                return 2;
            }
            else
            {
                return 1;
            }
        }


    }

    public String emptyValueCheckInsurance()
    {
        int pos=AppController.confirmingPosition;
        String feild="";
        return feild;
    }
public String emptyValueCheckPassport()
{
    int pos=AppController.confirmingPosition;
    String feild="";
    System.out.println("Insrance empty works passport 101010dd");
        if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getNot_have_a_valid_passport().equalsIgnoreCase("1"))
        {
            System.out.println("Insrance empty works passport 101010ff");
            if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa().equalsIgnoreCase("0"))
            {
                System.out.println("Insrance empty works passport 101010");
                if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_permit_no().equalsIgnoreCase(""))
                {
                    feild="Visa/Permit Number";
                }
                else
                {
                    if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_expired().equalsIgnoreCase("1"))
                    {
                        if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).isVisaDateChanged())
                        {
                            if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_image().equalsIgnoreCase(""))
                            {
                                feild="Upload Visa Image";
                            }
                        }
                        else
                        {
                            feild="Visa/Permit Expiry Date";
                        }

                    }
                    else
                    {
                        feild="";
                    }
                }
            }
            else
            {
                feild="";
            }
        }
        else
        {
            System.out.println("Insrance empty works passport 101010 hdd");

            if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getNationality().equalsIgnoreCase(""))
            {
                feild="Nationality";
            }
            else
            {
                if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getPassport_number().equalsIgnoreCase(""))
                {
                    feild="Passport Number";
                }
                else
                {
                    if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getExpiry_date().equalsIgnoreCase(""))
                    {
                        feild="Passport Expiry Date";
                    }
                    else
                    {
                        if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getPassport_expired().equalsIgnoreCase("1"))
                        {
                            if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).isPassportDateChanged())
                            {
                                if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getPassport_image().equalsIgnoreCase(""))
                                {
                                    feild="Upload Passport Image";
                                }
                                else
                                {
                                    //visa data

                                    if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa().equalsIgnoreCase("0"))
                                    {
                                        System.out.println("Insrance empty works passport 101010");
                                        if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_permit_no().equalsIgnoreCase(""))
                                        {
                                            feild="Visa/Permit Number";
                                        }
                                        else
                                        {
                                            if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_expired().equalsIgnoreCase("1"))
                                            {
                                                if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).isVisaDateChanged())
                                                {
                                                    if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_image().equalsIgnoreCase(""))
                                                    {
                                                        feild="Upload Visa Image";
                                                    }
                                                }
                                                else
                                                {
                                                    feild="Visa/Permit Expiry Date";
                                                }

                                            }
                                            else
                                            {
                                                feild="";
                                            }
                                        }
                                    }
                                    else
                                    {
                                        feild="";
                                    }
                                }
                            }
                            else
                            {
                                feild="Passport Expiry Date";
                            }
                        }
                        else
                        {
                            //check visa
                            if (!PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa().equalsIgnoreCase("1"))
                            {
                                if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_permit_no().equalsIgnoreCase(""))
                                {
                                    feild="Visa/Permit Number";
                                }
                                else
                                {
                                    System.out.println("Insrance empty works passport 161616");
                                    if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_expired().equalsIgnoreCase("1"))
                                    {
                                        System.out.println("Insrance empty works passport 17171717");
                                        if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).isVisaDateChanged())
                                        {
                                            System.out.println("Insrance empty works passport 181818");
                                            if (PreferenceManager.getPassportDetailArrayList(mContext).get(pos).getVisa_image().equalsIgnoreCase(""))
                                            {
                                                System.out.println("Insrance empty works passport 19191919");
                                                feild="Upload Visa Image";
                                            }
                                        }
                                        else
                                        {
                                            feild="Visa/Permit Expiry Date";
                                        }

                                    }
                                    else
                                    {
                                        feild="";
                                    }
                                }
                            }
                            else
                            {
                                feild="";
                            }
                        }
                    }
                }
            }
        }


    return feild;
}
    private void ShowCondition(String whoValueEmpty,String page)
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
        if (whoValueEmpty.equalsIgnoreCase("Passport Expiry Date"))
        {
            text.setText("Kindly update the passport expiry date.");
        }
        else if (whoValueEmpty.equalsIgnoreCase("Upload Passport Image"))
        {
            text.setText("Kindly upload the passport image.");
        }
        else if (whoValueEmpty.equalsIgnoreCase("Visa/Permit Expiry Date"))
        {
            text.setText("Kindly update the Visa/Permit expiry date.");
        }
        else if (whoValueEmpty.equalsIgnoreCase("Upload Visa Image"))
        {
            text.setText("Kindly upload the Visa image");
        }
        else if(whoValueEmpty.equalsIgnoreCase("Please Tick the Agreement."))
        {
            text.setText("Please Tick the Agreement.");
        }
        else
        {
            text.setText("Please enter the "+whoValueEmpty);
        }

        textHead.setText("Alert");
        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setText("Ok");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
//                    PreferenceManager.setIsValueEmpty(mContext,"0");
                if (page.equalsIgnoreCase("0"))
                {
                    pager.setCurrentItem(0,true);
                }
                else {
                    pager.setCurrentItem(1,true);
                }





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
                finish();

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
}