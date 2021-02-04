package com.mobatia.bskl.activity.data_collection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.DataCollectionActivity;
import com.mobatia.bskl.activity.datacollection_p2.FirstScreen;
import com.mobatia.bskl.activity.datacollection_p2.FirstScreenNewData;
import com.mobatia.bskl.activity.datacollection_p2.SecondScreen;
import com.mobatia.bskl.activity.datacollection_p2.SecondScreenNew;
import com.mobatia.bskl.activity.datacollection_p2.ThirdScreen;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportImageModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.datacollection_p2.model.VISAimageModel;
import com.mobatia.bskl.activity.home.model.OwnDetailsModel;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.CustomDialog;
import com.mobatia.bskl.volleywrappermanager.CustomProgressBar;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.prefs.PreferenceChangeEvent;



import static com.mobatia.bskl.constants.JSONConstants.JTAG_RESPONSE;
import static com.mobatia.bskl.constants.JSONConstants.JTAG_RESPONSECODE;
import static com.mobatia.bskl.constants.JSONConstants.JTAG_STATUSCODE;
import static com.mobatia.bskl.constants.URLConstants.URL_SUBMIT_DATA_COLLECTION_NEW;


public class DataCollectionHome extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

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
    RadioButton radioButton1,radioButton2,radioButton3;
    ArrayList<InsuranceDetailModel>mInsuranceDetailArrayList;
    String JSONSTRING = null;
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
        pager = findViewById(R.id.viewPager);
        bottomLinear = findViewById(R.id.bottomLinear);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        pager.setOffscreenPageLimit(2);
        radioGroup = findViewById(R.id.radiogroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        submitBtn = findViewById(R.id.submit);
        nextBtn = findViewById(R.id.nextImg);
        backBtn = findViewById(R.id.backImg);
        radioGroup.setOnCheckedChangeListener(this);
        if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }
        else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5")  || PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }
        else if ((PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))|| PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton1.setVisibility(View.INVISIBLE);
            radioButton2.setVisibility(View.INVISIBLE);
            radioButton3.setVisibility(View.INVISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
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
            nextBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }

        closeMsgKin = "Next of Kin must not be existing parent contact for family (cross check email/phone number with mother/father/parent record)";
        closeMsgLocal = "Emergency Contact must not be existing parent contact for family (cross check email/phone number with mother/father/parent record)";
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
        SharedPreferences prefs = getSharedPreferences("BSKL", Context.MODE_PRIVATE);
        String data = prefs.getString("DATA_COLLECTION", null);

        try {
            JSONObject respObj = new JSONObject(data);
            own_details = respObj.getJSONArray("own_details");
            kin_details = respObj.getJSONArray("kin_details");
            emergency_details = respObj.getJSONArray("local_emergency_details");

        } catch (Exception e) {

        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                //    Log.e("onPageScrolled: ", String.valueOf(position));

                if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
                {
                    if (position==1)
                    {
                        if (checkOwnDetailEmpty().equalsIgnoreCase(""))
                        {

                            boolean isFound=false;
                            if (!isFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                     System.out.println();
                                    if (!AppController.kinArrayShow.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                    }

                                }
                            }
                           boolean isLocalFound=false;
                           boolean isKinFound=false;
                           boolean isRelationFound=false;
                           if (!isRelationFound)
                           {
                               for (int i=0;i<AppController.kinArrayShow.size();i++)
                               {
                                   if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                                   {
                                       isLocalFound=true;
                                   }
//                                   if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of Kin"))
//                                   {
//                                       isKinFound=true;
//                                   }
                               }
                               if (isLocalFound )
                               {
                                   isRelationFound=true;
                               }
                           }


                            if (isFound)
                            {
                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                            }
                            else
                            {
                                if (!isRelationFound)
                                {
                                    if (!isLocalFound)
                                    {
                                        showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                    }

//                                    if (!isLocalFound && !isKinFound)
//                                    {
//                                        showAlertOKButton(mContext, "Alert", "There should be at least one Next of Kin and one Local Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                    }
//                                    else
//                                    {
//
//                                        else
//                                        {
//                                            if (!isKinFound)
//                                            {
//                                                showAlertOKButton(mContext, "Alert", "There should be at least one Next of Kin contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                            }
//                                        }
//                                    }

                                }
                            }
                           // checkFamilConatctEmpty();
//                            if (PreferenceManager.getIsValueEmpty(mContext).equals("1"))
//                            {
//                                ShowCondition(PreferenceManager.getWhoValueEmpty(mContext));
//                            }
//                           else if (PreferenceManager.getConfirmButton(mContext).equals("1"))
//                            {
//                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);
//                            }
                        }
                        else
                        {
                            ShowCondition("Please confirm all mandator fields in Own Details");
                        }
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5"))
                {
                    if (position==1)
                    {
                        if (checkOwnDetailEmpty().equalsIgnoreCase(""))
                        {
                            // checkFamilConatctEmpty();
                            boolean isFound=false;
                            if (!isFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    System.out.println();
                                    if (!AppController.kinArrayShow.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                    }
                                }
                            }

                            if (isFound)
                            {
                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                            }
                        }
                        else
                        {
                            ShowCondition("Please confirm all mandator fields in Own Details");
                        }
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
                {
                    if (position==1)
                    {
                        if (checkOwnDetailEmpty().equalsIgnoreCase(""))
                        {
                            // checkFamilConatctEmpty();
                            boolean isFound=false;
                            if (!isFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    System.out.println();
                                    if (!AppController.kinArrayShow.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                    }
                                }
                            }

                            boolean isLocalFound=false;
                            boolean isKinFound=false;
                            boolean isRelationFound=false;
                            if (!isRelationFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                                    {
                                        isLocalFound=true;
                                    }
//                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of Kin"))
//                                    {
//                                        isKinFound=true;
//                                    }
                                }
                                if (isLocalFound)
                                {
                                    isRelationFound=true;
                                }
                            }


                            if (isFound)
                            {
                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                            }
                            else
                            {
                                if (!isRelationFound)
                                {

                                    if (!isLocalFound)
                                    {
                                        showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                    }
                                    else
                                    {
                                        if (!isLocalFound)
                                        {
                                            showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                        }
                                        else
                                        {
//                                            if (!isKinFound)
//                                            {
//                                                showAlertOKButton(mContext, "Alert", "TThere should be at least one Next of Kin contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                            }
                                        }
                                    }

                                }
                            }
                        }
                        else
                        {
                            ShowCondition("Please confirm all mandator fields in Own Details");
                        }
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //       Log.e("onPageState: ", String.valueOf(state));
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String triggerType="";
                String overallStatus="";
                System.out.println("PRESSED: TYPE: "+PreferenceManager.getDataCollectionTriggerType(mContext));

                if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
                {

                    int currentPage=pager.getCurrentItem();
                    if (currentPage==0)
                    {
                        if (checkOwnDetailEmpty().equalsIgnoreCase(""))
                        {
                            // checkFamilConatctEmpty();
                            boolean isFound=false;
                            if (!isFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    System.out.println();
                                    if (!AppController.kinArrayShow.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                    }
                                }
                            }
                            boolean isLocalFound=false;
                            boolean isKinFound=false;
                            boolean isRelationFound=false;
                            if (!isRelationFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact") || AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                                    {
                                        isLocalFound=true;
                                    }
//                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of Kin"))
//                                    {
//                                        isKinFound=true;
//                                    }
                                }
                                if (isLocalFound)
                                {
                                    isRelationFound=true;
                                }
                            }

                            if (isFound)
                            {
                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                            }
                            else
                            {
                                if (!isRelationFound)
                                {

                                    if (!isLocalFound && !isKinFound)
                                    {
                                        showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                    }
                                    else
                                    {
                                        if (!isLocalFound)
                                        {
                                            showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                        }
                                        else
                                        {
//                                            if (!isKinFound)
//                                            {
//                                                showAlertOKButton(mContext, "Alert", "TThere should be at least one Next of Kin contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                            }
                                        }
                                    }

                                }
                                else
                                {
                                    triggerType="6";
                                    overallStatus="1";
                                    callSubmitAPI(triggerType,overallStatus);
                                }
                            }

                        }
                        else
                        {
                            ShowCondition("Please confirm all mandatory fields in Own Details");

                        }
                    }
                    else
                    {
                        System.out.println("Current page item"+pager.getCurrentItem());
                        boolean isFound=false;
                        int foundPosition=-1;

                        if (AppController.mStudentDataArrayList.size()>0)
                        {
                            for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                            {
                                if (!isFound)
                                {
                                    if (!AppController.mStudentDataArrayList.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                        foundPosition=i;
                                    }
                                }

                            }
                        }
                        if (isFound)
                        {
                            ShowConditionnew("Please confirm all the details for "+AppController.mStudentDataArrayList.get(foundPosition).getmName());
                        }
                        else {

                            System.out.println("Current page item"+pager.getCurrentItem());
                            triggerType="1";
                            overallStatus="2";
                              callSubmitAPI(triggerType,overallStatus);
                        }
                    }



                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("2"))
                {
                    if (checkOwnDetailEmpty().equalsIgnoreCase(""))
                    {
                        // checkFamilConatctEmpty();
                        boolean isFound=false;
                        if (!isFound)
                        {
                            for (int i=0;i<AppController.kinArrayShow.size();i++)
                            {
                                System.out.println();
                                if (!AppController.kinArrayShow.get(i).isConfirmed())
                                {
                                    isFound=true;
                                }
                            }
                        }
                        boolean isLocalFound=false;
                        boolean isKinFound=false;
                        boolean isRelationFound=false;
                        if (!isRelationFound)
                        {
                            for (int i=0;i<AppController.kinArrayShow.size();i++)
                            {
                                if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                                {
                                    isLocalFound=true;
                                }
//                                if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of Kin"))
//                                {
//                                    isKinFound=true;
//                                }
                            }
                            if (isLocalFound )
                            {
                                isRelationFound=true;
                            }
                        }

                        if (isFound)
                        {
                            showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                        }
                        else
                        {
                            if (!isRelationFound)
                            {

                                if (!isLocalFound)
                                {
                                    showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                }
                                else
                                {
                                    if (!isLocalFound)
                                    {
                                        showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                    }
                                    else
                                    {
//                                        if (!isKinFound)
//                                        {
//                                            showAlertOKButton(mContext, "Alert", "TThere should be at least one Next of Kin contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                        }
                                    }
                                }

                            }
                            else
                            {
                                //1 , 2
                                triggerType="1";
                                overallStatus="2";
                                callSubmitAPI(triggerType,overallStatus);
                            }
                        }


                    }
                    else
                    {
                        ShowCondition("Please confirm all mandatory fields in Own Details");
                    }

                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
                {
                    boolean isFound=false;
                    int foundPosition=-1;

                    if (AppController.mStudentDataArrayList.size()>0)
                    {
                        for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                        {
                            if (!isFound)
                            {
                                if (!AppController.mStudentDataArrayList.get(i).isConfirmed())
                                {
                                    isFound=true;
                                    foundPosition=i;
                                }
                            }

                        }
                    }
                    if (isFound)
                    {
                        ShowConditionnew("Please confirm all the details for "+AppController.mStudentDataArrayList.get(foundPosition).getmName());
                    }
                    else {
                        triggerType="1";
                        overallStatus="2";
                        callSubmitAPI(triggerType,overallStatus);
                    }
                } else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("4"))
                {
                    boolean isFound=false;
                    int foundPosition=-1;

                    if (AppController.mStudentDataArrayList.size()>0)
                    {
                        for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                        {
                            if (!isFound)
                            {
                                if (!AppController.mStudentDataArrayList.get(i).isConfirmed())
                                {
                                    isFound=true;
                                    foundPosition=i;
                                }
                            }

                        }
                    }
                    if (isFound)
                    {
                        ShowConditionnew("Please confirm all the details for "+AppController.mStudentDataArrayList.get(foundPosition).getmName());
                    }
                    else {
                        triggerType="1";
                        overallStatus="2";
                        callSubmitAPI(triggerType,overallStatus);
                    }
                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5"))
                {
                    int currentPage=pager.getCurrentItem();
                    if (currentPage==0)
                    {

                        if (checkOwnDetailEmpty().equalsIgnoreCase("")) {
                            // checkFamilConatctEmpty();
                            boolean isFound = false;
                            if (!isFound) {
                                for (int i = 0; i < AppController.kinArrayShow.size(); i++) {
                                    System.out.println();
                                    if (!AppController.kinArrayShow.get(i).isConfirmed()) {
                                        isFound = true;
                                    }
                                }
                            }
                            boolean isLocalFound=false;
                            boolean isKinFound=false;
                            boolean isRelationFound=false;
                            if (!isRelationFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact") || AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact") )
                                    {
                                        isLocalFound=true;
                                    }
//                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of Kin"))
//                                    {
//                                        isKinFound=true;
//                                    }
                                }
                                if (isLocalFound )
                                {
                                    isRelationFound=true;
                                }
                            }
                            if (isFound) {
                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                            }

                            else
                            {
                                if (!isRelationFound)
                                {

                                    if (!isLocalFound && !isKinFound)
                                    {
                                        showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                    }
                                    else
                                    {
                                        if (!isLocalFound)
                                        {
                                            showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                        }
                                        else
                                        {
//                                            if (!isKinFound)
//                                            {
//                                                showAlertOKButton(mContext, "Alert", "TThere should be at least one Next of Kin contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                            }
                                        }
                                    }

                                }
                                else {
                                    //1 , 2
                                    triggerType = "3";
                                    overallStatus = "1";
                                    callSubmitAPI(triggerType, overallStatus);
                                }
                            }

                        }
                    }
                    else
                    {
                        boolean isFound=false;
                        int foundPosition=-1;

                        if (AppController.mStudentDataArrayList.size()>0)
                        {
                            for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                            {
                                if (!isFound)
                                {
                                    if (!AppController.mStudentDataArrayList.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                        foundPosition=i;
                                    }
                                }

                            }
                        }
                        if (isFound)
                        {
                            ShowConditionnew("Please confirm all the details for "+AppController.mStudentDataArrayList.get(foundPosition).getmName());
                        }
                        else
                        {
                            triggerType = "1";
                            overallStatus = "2";
                            callSubmitAPI(triggerType, overallStatus);
                        }
                    }


                }
                else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
                {

                    boolean isFound=false;
                    int foundPosition=-1;

                    if (AppController.mStudentDataArrayList.size()>0)
                    {
                        for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                        {
                            if (!isFound)
                            {
                                if (!AppController.mStudentDataArrayList.get(i).isConfirmed())
                                {
                                    isFound=true;
                                    foundPosition=i;
                                }
                            }

                        }
                    }
                    if (isFound)
                    {
                        ShowConditionnew("Please confirm all the details for "+AppController.mStudentDataArrayList.get(foundPosition).getmName());
                    }
                    else
                    {
                        triggerType = "1";
                        overallStatus = "2";
                        callSubmitAPI(triggerType, overallStatus);
                    }
                }

               if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
                {
                    int currentPage=pager.getCurrentItem();
                    if (currentPage==0)
                    {

                        if (checkOwnDetailEmpty().equalsIgnoreCase("")) {
                            // checkFamilConatctEmpty();
                            boolean isFound = false;
                            if (!isFound) {
                                for (int i = 0; i < AppController.kinArrayShow.size(); i++) {
                                    System.out.println();
                                    if (!AppController.kinArrayShow.get(i).isConfirmed()) {
                                        isFound = true;
                                    }
                                }
                            }
                            boolean isLocalFound=false;
                            boolean isKinFound=false;
                            boolean isRelationFound=false;
                            if (!isRelationFound)
                            {
                                for (int i=0;i<AppController.kinArrayShow.size();i++)
                                {
                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                                    {
                                        isLocalFound=true;
                                    }
//                                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of Kin"))
//                                    {
//                                        isKinFound=true;
//                                    }
                                }
                                if (isLocalFound)
                                {
                                    isRelationFound=true;
                                }
                            }
                            if (isFound) {
                                showAlertOKButton(mContext, "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                            }
                            else
                            {
                                if (!isRelationFound)
                                {

                                    if (!isLocalFound)
                                    {
                                        showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                    }
                                    else
                                    {
                                        if (!isLocalFound)
                                        {
                                            showAlertOKButton(mContext, "Alert", "There should be at least one Emergency contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);

                                        }
                                        else
                                        {
//                                            if (!isKinFound)
//                                            {
//                                                showAlertOKButton(mContext, "Alert", "TThere should be at least one Next of Kin contact for the family", "Ok", R.drawable.exclamationicon, R.drawable.round);
//
//                                            }
                                        }
                                    }

                                }
                                else {
                                    //1 , 2
                                    triggerType = "4";
                                    overallStatus = "1";
                                    callSubmitAPI(triggerType, overallStatus);
                                }
                            }


                        }
                    }
                    else
                    {
                        boolean isFound=false;
                        int foundPosition=-1;

                        if (AppController.mStudentDataArrayList.size()>0)
                        {
                            for (int i=0;i<AppController.mStudentDataArrayList.size();i++)
                            {
                                if (!isFound)
                                {
                                    if (!AppController.mStudentDataArrayList.get(i).isConfirmed())
                                    {
                                        isFound=true;
                                        foundPosition=i;
                                    }
                                }

                            }
                        }
                        if (isFound)
                        {
                            ShowConditionnew("Please confirm all the details for "+AppController.mStudentDataArrayList.get(foundPosition).getmName());
                        }
                        else
                        {
                            triggerType = "1";
                            overallStatus = "2";
                            callSubmitAPI(triggerType, overallStatus);
                        }
                    }

                }

            }
        });
    }

    private void callSubmitAPI(String trigger, String overall)
    {
        System.out.println("CAlling: Submit");

        String OWNDATA = null;
   //     String FIRSTDATA = null;
        String SECONDDATA = null;

//        ArrayList<String> FirstArray = new ArrayList<>();
//        FirstArray.add(String.valueOf(PreferenceManager.getFirstFragmentJSONArrayList(mContext)));
//        for (int i = 0; i<FirstArray.size();i++){
//            System.out.println("GEt: A "+FirstArray.get(i));
//            FIRSTDATA = FirstArray.get(i);
//        }

        ArrayList<KinModel> newArrayData=new ArrayList<>();
        for (int i=0;i<AppController.kinArrayPass.size();i++)
        {
            KinModel model = new KinModel();
            model.setStatus(AppController.kinArrayPass.get(i).getStatus());
            model.setRequest(AppController.kinArrayPass.get(i).getRequest());
            model.setName(AppController.kinArrayPass.get(i).getName());
            model.setLast_name(AppController.kinArrayPass.get(i).getLast_name());
            model.setEmail(AppController.kinArrayPass.get(i).getEmail());
            model.setTitle(AppController.kinArrayPass.get(i).getTitle());
            if (AppController.kinArrayPass.get(i).getKin_id().startsWith("Mobatia_"))
            {
                model.setKin_id("");
            }
            else {
                model.setKin_id(AppController.kinArrayPass.get(i).getKin_id());
            }

            model.setRelationship(AppController.kinArrayPass.get(i).getRelationship());
            model.setCode(AppController.kinArrayPass.get(i).getCode());
            model.setUser_mobile(AppController.kinArrayPass.get(i).getUser_mobile());
            model.setStudent_id(AppController.kinArrayPass.get(i).getStudent_id());
            model.setCreated_at(AppController.kinArrayPass.get(i).getCreated_at());
            model.setUpdated_at(AppController.kinArrayPass.get(i).getUpdated_at());
            model.setPhone(AppController.kinArrayPass.get(i).getPhone());
            model.setCorrespondencemailmerge(AppController.kinArrayPass.get(i).getCorrespondencemailmerge());
            model.setJustcontact(AppController.kinArrayPass.get(i).getJustcontact());
            model.setReportmailmerge(AppController.kinArrayPass.get(i).getReportmailmerge());
            if (AppController.kinArrayPass.get(i).getKin_id().startsWith("Mobatia_"))
            {
                model.setId("");
            }
            else {
                model.setId(AppController.kinArrayPass.get(i).getId());
            }
            model.setUser_id(AppController.kinArrayPass.get(i).getUser_id());
            model.setFullFilled(AppController.kinArrayPass.get(i).isFullFilled());
            model.setNewData(AppController.kinArrayPass.get(i).isNewData());
            model.setConfirmed(AppController.kinArrayPass.get(i).isConfirmed());
            newArrayData.add(model);
        }

        ArrayList<KinModel> FirstArray=new ArrayList<>();
        FirstArray=newArrayData;
        Gson newGson = new Gson();
        String FIRSTDATA = newGson.toJson(FirstArray);
        System.out.println("GEt: A "+FIRSTDATA);
        ArrayList<InsuranceDetailModel> SecondArray = new ArrayList<>();
        SecondArray=(PreferenceManager.getInsuranceDetailArrayList(mContext));
        Gson IsuGson = new Gson();
        String InsuranceGson = IsuGson.toJson(SecondArray);
        System.out.println("GEt: B "+InsuranceGson);

        ArrayList<PassportDetailModel> PassArray = new ArrayList<>();
        PassArray = (PreferenceManager.getPassportDetailArrayList(mContext));
        Gson PGson = new Gson();
        String PassGson = PGson.toJson(PassArray);
        System.out.println("GEt: C "+PassGson);
        Log.e("Pass",PassGson);
        if (PreferenceManager.getPassportDetailArrayList(mContext).size()>0)
        {
            for (int i=0;i<PreferenceManager.getPassportDetailArrayList(mContext).size();i++)
            {
                System.out.println("status "+i+PreferenceManager.getPassportDetailArrayList(mContext).get(i).getStatus());
                System.out.println("request "+i+PreferenceManager.getPassportDetailArrayList(mContext).get(i).getRequest());
            }
        }

        ArrayList<String> OwnData = new ArrayList<>();
        OwnData.add(String.valueOf(PreferenceManager.getOwnDetailsJSONArrayList(mContext)));
        for (int i = 0; i<OwnData.size();i++){
            System.out.println("GEt: D "+OwnData.get(i));
            OWNDATA = OwnData.get(i);
        }



        if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("1"))
        {
            if (overall.equalsIgnoreCase("2") && trigger.equalsIgnoreCase("1"))
            {
                JSONSTRING = "{\"data\":{\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+","+"\"health_details\""+":"+InsuranceGson+","+"\"passport_details\""+":"+PassGson+"} }";
                System.out.println("JSON_DATA: "+JSONSTRING);
                Log.e("JSON_DATA: ",JSONSTRING);
                CallApi(JSONSTRING,trigger,overall);
            }
            else
            {
                JSONSTRING= "{\"data\":{"+"\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+"}}";
                System.out.println("JSON_DATA: "+JSONSTRING);
                CallApi(JSONSTRING,trigger,overall);
            }
        }
       else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("2"))
        {
            JSONSTRING= "{\"data\":{"+"\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+"}}";
            System.out.println("JSON_DATA: "+JSONSTRING);
            CallApi(JSONSTRING,trigger,overall);
        }
       else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
        {
            JSONSTRING = "{\"data\":{"+"\"health_details\""+":"+InsuranceGson+"}}";
            System.out.println("JSON_DATA: "+JSONSTRING);
            CallApi(JSONSTRING,trigger,overall);
        }
       else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("4"))
        {   // -->  ONLY PASSPORT
            JSONSTRING = "{\"data\":{"+"\"passport_details\""+":"+PassGson+"}}";
            System.out.println("JSON_DATA: "+JSONSTRING);

            CallApi(JSONSTRING,trigger,overall);
        }
      else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5")){
            if (overall.equalsIgnoreCase("2") && trigger.equalsIgnoreCase("1"))
            {
                JSONSTRING = "{\"data\":{\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+","+"\"health_details\""+":"+InsuranceGson+"}}";
                System.out.println("JSON_DATA: "+JSONSTRING);
                CallApi(JSONSTRING,trigger,overall);
            }
            else
            {
                JSONSTRING= "{\"data\":{"+"\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+"}}";
                System.out.println("JSON_DATA: "+JSONSTRING);
                CallApi(JSONSTRING,trigger,overall);
            }
        }
      else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6")){   //  --> HEALTH + PASSPORT

            JSONSTRING = "{\"data\":{"+"\"health_details\""+":"+InsuranceGson+","+"\"passport_details\""+":"+PassGson+"}}";
            System.out.println("JSON_DATA: "+JSONSTRING);
            CallApi(JSONSTRING,trigger,overall);

        }else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
        {   //  --> OWN + PASSPORT
            if (overall.equalsIgnoreCase("2") && trigger.equalsIgnoreCase("1"))
            {
                JSONSTRING = "{\"data\":{\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+","+"\"passport_details\""+":"+PassGson+"}}";
                System.out.println("JSON_DATA: "+JSONSTRING);
                CallApi(JSONSTRING,trigger,overall);
            }
            else
            {
                JSONSTRING= "{\"data\":{"+"\"own_details\""+":"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+"}}";
                System.out.println("JSON_DATA: "+JSONSTRING);
                CallApi(JSONSTRING,trigger,overall);
            }


        }


    }

    private void CallApi(String jsonstring,String triggerType, String overAll) {

        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_SUBMIT_DATA_COLLECTION_NEW);
        String[] name = {"access_token","user_ids","overall_status","data","trigger_type"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext),overAll,jsonstring,triggerType};
        System.out.println("JSON String"+jsonstring);

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("Response: " + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);

                        if (status_code.equalsIgnoreCase("303"))
                        {
                            if (triggerType.equalsIgnoreCase("1") && overAll.equalsIgnoreCase("2"))
                            {
                                System.out.println("It enters inside");
                                AppController.kinArrayShow.clear();
                                AppController.kinArrayPass.clear();
                                AppController.mStudentDataArrayList.clear();
                                ArrayList<OwnContactModel>mOwnArrayList=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);
                                mOwnArrayList.clear();
                                PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).clear();
                                PreferenceManager.saveOwnDetailArrayList(mOwnArrayList,"OwnContact",mContext);
                                PreferenceManager.saveKinDetailsArrayListShow( AppController.kinArrayShow,mContext);
                                PreferenceManager.saveKinDetailsArrayList( AppController.kinArrayPass,mContext);
                                ArrayList<InsuranceDetailModel>mInsurance=PreferenceManager.getInsuranceDetailArrayList(mContext);
                                mInsurance.clear();
                                PreferenceManager.saveInsuranceDetailArrayList(mInsurance,mContext);
                                ArrayList<PassportDetailModel>mPassport=PreferenceManager.getPassportDetailArrayList(mContext);
                                mPassport.clear();
                                PreferenceManager.savePassportDetailArrayList(mPassport,mContext);
                                ArrayList<StudentModelNew>mStuudent=PreferenceManager.getInsuranceStudentList(mContext);
                                mStuudent.clear();
                                PreferenceManager.saveInsuranceStudentList(mStuudent,mContext);

                                showDataSuccess(mContext, "Alert", getString(R.string.data_collection_submission_success), R.drawable.exclamationicon, R.drawable.round);
                            }
                            else
                            {
                                PreferenceManager.setDataCollectionTriggerType(mContext,triggerType);
                                DataCollectionHome.this.finish();
                                Intent i = new Intent(mContext, DataCollectionHome.class);
                                startActivity(i);
                            }


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        CallApi(JSONSTRING,triggerType,overAll);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        CallApi(JSONSTRING,triggerType,overAll);
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        CallApi(JSONSTRING,triggerType,overAll);
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
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/

            }
        });


    }



    /*
    Calling Using Okhttp Method
     */


    private void ShowCondition(String whoValueEmpty)
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
        text.setText(whoValueEmpty);
        textHead.setText("Alert");
        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setText("Ok");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
//                    PreferenceManager.setIsValueEmpty(mContext,"0");

                pager.setCurrentItem(0,true);




            }
        });

        dialog.show();
    }
    private void ShowConditionnew(String whoValueEmpty)
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
        text.setText(whoValueEmpty);
        textHead.setText("Alert");
        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setText("Ok");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
//                    PreferenceManager.setIsValueEmpty(mContext,"0");

              //  pager.setCurrentItem(0,true);




            }
        });

        dialog.show();
    }

    public void showDialogCloseButton(final Activity activity, String msg, String msgHead, int ico, int bgIcon, final String page) {
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
        dialogButton.setText("Ok");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (page.equals("1")) {
                    pager.setCurrentItem(1); //go to kin details
                } else {
                    pager.setCurrentItem(2);  // go to local
                }
//				Intent homeIntent = new Intent(mContext, HomeListActivity.class);


            }
        });

        dialog.show();
    }
    public ArrayList<KinDetails.KinModel> getKinArrayList(String key) {
        SharedPreferences prefs = getSharedPreferences("BSKL", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<KinDetails.KinModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ArrayList<EmergencyDetails.EmergencyModel> getEmergencyArrayList(String key) {
        SharedPreferences prefs = getSharedPreferences("BSKL", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<EmergencyDetails.EmergencyModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
    /*************************************************************
     * Listeners for ViewPager
     *************************************************************/
    /**
     * When the current page is scrolled
     *
     * @param position
     * @param v
     * @param i
     */
    @Override
    public void onPageScrolled(int position, float v, int i) {

    }

    /**
     * When a new page becomes selected
     *
     * @param position
     */
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
                    submitBtn.setVisibility(View.VISIBLE);
                    break;
//                case 1:
//                    radioGroup.check(R.id.radioButton2);
//                    nextBtn.setVisibility(View.VISIBLE);
//                    backBtn.setVisibility(View.VISIBLE);
//                    submitBtn.setVisibility(View.INVISIBLE);
//                    break;
                case 1:
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.GONE);
                    radioGroup.check(R.id.radioButton2);
                    nextBtn.setVisibility(View.INVISIBLE);
                    backBtn.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton3.setVisibility(View.GONE);
                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
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
                    submitBtn.setVisibility(View.VISIBLE);
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
                    submitBtn.setVisibility(View.VISIBLE);
            }
        }
        else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
        {
            bottomLinear.setVisibility(View.VISIBLE);
            radioButton3.setVisibility(View.GONE);
            switch (position) {
                case 0:

                    radioGroup.check(R.id.radioButton1);
                    nextBtn.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.VISIBLE);
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
                    submitBtn.setVisibility(View.VISIBLE);
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
                    submitBtn.setVisibility(View.VISIBLE);
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
                    submitBtn.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onBackPressed() {
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
                DataCollectionHome.this.finish();


            }
        });

        dialog.show();

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

        }

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
                        return new FirstScreenNewData();
                    case 1:
                        return new SecondScreenNew();
                    default:
                        return new FirstScreenNewData();
                }
            }
          else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("2"))
            {
                switch (position) {

                    case 0:
                        return new FirstScreenNewData();
                    default:
                        return new FirstScreenNewData();
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("3"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNew();

                    default:
                        return new SecondScreenNew();
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("4"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNew();
                    default:
                        return new SecondScreenNew();
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5"))
            {
                switch (position) {

                    case 0:
                        return new FirstScreenNewData();
                    case 1:
                        return new SecondScreenNew();
                    default:
                        return new FirstScreenNewData();
                }
            }
            else  if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6"))
            {
                switch (position) {

                    case 0:
                        return new SecondScreenNew();
                    default:
                        return new SecondScreenNew();
                }
            }
            else
            {
                switch (position) {

                    case 0:
                        return new FirstScreenNewData();
                    case 1:
                        return new SecondScreenNew();
                    default:
                        return new SecondScreenNew();
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
            else if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("5") ||PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("7"))
            {
                return 2;
            }
            else
            {
                return 1;
            }
        }


    }

    public void showAlertOKButton(final Context activity, String msg, String msgHead, String button, int ico, int bgIcon) {
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
                pager.setCurrentItem(0,true);
            }
        });

        dialog.show();

    }
    public void showInsurance(final Context activity, String msg, String msgHead, String button, int ico, int bgIcon) {
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

                if (PreferenceManager.getDataCollectionTriggerType(mContext).equalsIgnoreCase("6")){
                    pager.setCurrentItem(0,true);
                }else {
                    pager.setCurrentItem(1,true);
                }


            }
        });

        dialog.show();

    }
public String checkOwnDetailEmpty()
{
    String feild="";
    ArrayList<OwnContactModel>mOwnContactarrayList=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);
    if (mOwnContactarrayList.get(0).getName().equalsIgnoreCase(""))
    {
        feild="First Name";
    }
    else
    {
        if (mOwnContactarrayList.get(0).getEmail().equalsIgnoreCase(""))
        {
            feild="Email ID";
        }
        else
        {
            if (mOwnContactarrayList.get(0).getPhone().equalsIgnoreCase(""))
            {
                feild="Phone Number";
            }
            else
            {
                if (mOwnContactarrayList.get(0).getAddress1().equalsIgnoreCase(""))
                {
                    feild="Address Line 1";
                }
                else
                {
                    if (mOwnContactarrayList.get(0).getAddress2().equalsIgnoreCase(""))
                    {
                        feild="Address Line 2";
                    }
                    else
                    {
                        if (mOwnContactarrayList.get(0).getTown().equalsIgnoreCase(""))
                        {
                            feild="Town";
                        }
                        else
                        {
                            if (mOwnContactarrayList.get(0).getState().equalsIgnoreCase(""))
                            {
                                feild="State";
                            }
                            else
                            {
                                if (mOwnContactarrayList.get(0).getCountry().equalsIgnoreCase(""))
                                {
                                    feild="Country";
                                }
                                else
                                {
                                    if (mOwnContactarrayList.get(0).getPincode().equalsIgnoreCase(""))
                                    {
                                        feild="Post Code";
                                    }
                                    else
                                    {
                                       if (mOwnContactarrayList.get(0).isConfirmed())
                                       {
                                           feild="";
                                       }
                                       else
                                       {
                                           feild="Not Confirmed";
                                       }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    return feild;
}
 public String checkFamilConatctEmpty()
 {
     String emptyFeild="";
     return emptyFeild;
 }
    private void showDataSuccess(Context mContext, String msgHead, String msg, int ico, int bgIcon)
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
                DataCollectionHome.this.finish();

            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_Cancel);
        dialog.show();
    }
}