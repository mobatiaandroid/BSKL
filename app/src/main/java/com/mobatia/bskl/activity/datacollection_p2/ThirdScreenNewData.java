package com.mobatia.bskl.activity.datacollection_p2;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.model.NationalityModel;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportImageModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.activity.datacollection_p2.model.VISAimageModel;
import com.mobatia.bskl.appcontroller.AppController;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.function.Predicate;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class ThirdScreenNewData extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants{

    ImageView checkStudentMalysianImg,CloseIcon;
    TextView UploadPasspost;
    EditText visaPermitNumberTxt,visaPermitExpiryTxt;
    boolean isStudentMalasiyanChecked=false;
    TextView uploadVisa,uploadVisaTxt;
    TextView studentName;
    ImageView studImg;
    private Context mContext;
    LinearLayout mStudentSpinner;
    EditText passportNumberTxt,passportExpiryTxt;
    String passportNationality="";
    String passportNumber="";
    String passportExpiry="";
    String visaPermitNumber="";
    String visaPermitExpiry="";
    String studentId="";
    String studentImage="";
    String studentNamePass="";
    String visaImageData="";
    String passportImageData="";
    String DATE = "";
    final Calendar myCalendar = Calendar.getInstance();
    TextView PassportImageName,VisaImageName;
    String IsNational = "YES";
    ImageView ViewSelectedVisa,ViewSelectedPassport;
    String visa_image_name_path="";
    String passport_image_name_path="";
    String passport_image_path="";
    String visa_image_path="";
    String itemId="";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CLICK_IMAGE_REQUEST = 2;
    private static final int VISA_CAMERA_REQUEST = 3;
    private static final int VISA_GALLERY_REQUEST = 4;

    File PassportCamera;
    File CompressPassportCamera;
    File VisaCamera;
    File CompressVisaCamera;
    SpinnerDialog spinnerDialog;

    private File actualImage;
    private File VisaactualImage;
    private File compressedImage;
    private File VisacompressedImage;
    String visaValue="";
    String visaImgName="";
    String passportImgName="";
    String visaImgPath="";
    String passportImgPath="";
    private String pictureImagePath = "";
    private static final int CAMERA_REQUEST = 1888;
    LinearLayout passportLinear;
   TextView passportNationalityTxt;
 //   SearchableSpinner spinner;

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
     int dataPosition;

    public ThirdScreenNewData(String studentImage,String studentId,String studentName) {
        // Required empty public constructor

        this.studentId=studentId;
        this.studentImage=studentImage;
        this.studentNamePass=studentName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_passport_new_data_collection, container, false);
        mContext = getActivity();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (EasyPermissions.hasPermissions(mContext, galleryPermissions)) {
//            Toast.makeText(mContext, "permisssion granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

        checkStudentMalysianImg=v.findViewById(R.id.checkStudentMalysianImg);
        visaPermitNumberTxt=v.findViewById(R.id.visaPermitNumberTxt);
        visaPermitExpiryTxt=v.findViewById(R.id.visaPermitExpiryTxt);
        uploadVisa=v.findViewById(R.id.uploadVisa);
        uploadVisaTxt=v.findViewById(R.id.uploadVisaTxt);
        studentName=v.findViewById(R.id.studentName);
        studImg=v.findViewById(R.id.imagicon);
        passportLinear=v.findViewById(R.id.passportLinear);
        PassportImageName = v.findViewById(R.id.PassImageName);
        VisaImageName = v.findViewById(R.id.VisaImageName);
        passportNationalityTxt=v.findViewById(R.id.passportNationalityTxt);
//        spinner=v.findViewById(R.id.spinner);
//        spinner.setAlpha(0.0f);
        passportNumberTxt=v.findViewById(R.id.passportNumberTxt);
        passportExpiryTxt=v.findViewById(R.id.passportExpiryTxt);
        mStudentSpinner = v.findViewById(R.id.studentSpinner);
        UploadPasspost = v.findViewById(R.id.uploadPassportTxt);
        ViewSelectedPassport = v.findViewById(R.id.ViewSelectedPassport);
        ViewSelectedVisa = v.findViewById(R.id.ViewSelectedVisa);
        CloseIcon = v.findViewById(R.id.closeImg);
        visaPermitNumberTxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passportNumberTxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppController.mPassportDetailArrayList=PreferenceManager.getPassportDetailArrayList(mContext);
        visaPermitNumberTxt.setEnabled(true);
        visaPermitExpiryTxt.setEnabled(true);
        if (!(studentImage.equals(""))) {

            Picasso.with(mContext).load(AppUtils.replace(studentImage)).placeholder(R.drawable.boy).fit().into(studImg);
        }
        else

        {

            studImg.setImageResource(R.drawable.boy);
        }

        studentName.setText(studentNamePass);
        if ( AppController.mPassportDetailArrayList.size()>0)
        {
            for (int i=0;i< AppController.mPassportDetailArrayList.size();i++)
            {
                String mID= AppController.mPassportDetailArrayList.get(i).getStudent_id();
                if (studentId.equalsIgnoreCase(mID))

                {
                    dataPosition=i;
                    AppController.confirmingPosition=dataPosition;

                    passportNationality= AppController.mPassportDetailArrayList.get(i).getNationality();
                    passportNumber= AppController.mPassportDetailArrayList.get(i).getPassport_number();
                    passportExpiry= AppController.mPassportDetailArrayList.get(i).getExpiry_date();
                    visaPermitNumber= AppController.mPassportDetailArrayList.get(i).getVisa_permit_no();
                    visaPermitExpiry= AppController.mPassportDetailArrayList.get(i).getVisa_permit_expiry_date();
                    visaValue= AppController.mPassportDetailArrayList.get(i).getVisa();
                    System.out.println("visa value "+AppController.mPassportDetailArrayList.get(i).getVisa());
                    visaImgName=AppController.mPassportDetailArrayList.get(i).getVisa_image_name();
                    passportImgName=AppController.mPassportDetailArrayList.get(i).getPassport_image_name();
                    visaImgPath=AppController.mPassportDetailArrayList.get(i).getVisa_image_path();
                    passportImgPath=AppController.mPassportDetailArrayList.get(i).getPassport_image_path();
                    itemId=AppController.mPassportDetailArrayList.get(i).getId();

                }
            }
        }
        System.out.println(" passport visa details passportImgPath"+AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
        System.out.println(" passport visa details visaImgPath"+visaImgPath);
        System.out.println(" passport visa details passportImgName"+passportImgName);
        System.out.println(" passport visa details visaImgName"+visaImgName);
        System.out.println(" passport visa details visavalue"+visaValue);
        System.out.println("Default nationality"+passportNationality);
        if(passportNationality.equalsIgnoreCase(""))
        {
            passportNationalityTxt.setHint(R.string.AST_COUNTRY);
        }
        else
        {
            passportNationalityTxt.setText(passportNationality);
        }

        passportNumberTxt.setText(passportNumber);
        ArrayList<String>nationalStringArray=new ArrayList<>();
        for (int i=0;i<AppController.mNationalityArrayList.size();i++)
        {
            nationalStringArray.add(AppController.mNationalityArrayList.get(i).getName());
        }
        spinnerDialog=new SpinnerDialog(getActivity(),nationalStringArray,"Select Country","Close");// With No Animation
        spinnerDialog=new SpinnerDialog(getActivity(),nationalStringArray,"Select Country",R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation
       // setDataToAdapter(nationalStringArray);

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
            //    Toast.makeText(mContext, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                passportNationalityTxt.setText(item);
                 AppController.isPassportEdited=true;
                 String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        String newData=passportExpiryTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(passportNationalityTxt.getText().toString());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
        mModel.setVisa_permit_no(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_permit_no());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }
        mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
        mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
        mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
        mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
        mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setStatus(dataStatus);
        mModel.setRequest(dataRequest);
        mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
        for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
        {
            Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

        }

        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
            }
        });

        passportLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });
        if (!passportExpiry.equalsIgnoreCase(""))
        {
            passportExpiryTxt.setText(AppUtils.dateConversionMMM(passportExpiry));
        }


        System.out.println("Visa value initially"+visaValue);
//        isStudentMalasiyanChecked=false;
//        visaPermitNumberTxt.setHint(R.string.AST_VISA_PERMIT_TEXT_WITH_RED);
//        visaPermitExpiryTxt.setHint(R.string.AST_VISA_PERMIT_EXPIRY_WITH_RED);
//        checkStudentMalysianImg.setImageResource(R.drawable.check_box_header);
//        visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
//        visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey);
//        uploadVisa.setAlpha(1.0f);
//        uploadVisaTxt.setAlpha(1.0f);
//        uploadVisa.setEnabled(true);
//        visaPermitNumberTxt.setEnabled(true);
//        visaPermitExpiryTxt.setEnabled(true);
        System.out.println("Visa value starting"+visaValue);
        if (visaValue.equalsIgnoreCase("1"))
        {
            isStudentMalasiyanChecked=false;
            visaPermitNumberTxt.setHint(R.string.AST_VISA_PERMIT_TEXT_WITH_RED);
            visaPermitExpiryTxt.setHint(R.string.AST_VISA_PERMIT_EXPIRY_WITH_RED);
            checkStudentMalysianImg.setImageResource(R.drawable.check_box_header);
            visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
            visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey);
            uploadVisa.setAlpha(1.0f);
            uploadVisaTxt.setAlpha(1.0f);
            uploadVisa.setEnabled(true);
            visaPermitNumberTxt.setEnabled(true);
            visaPermitExpiryTxt.setEnabled(true);
            visaPermitNumberTxt.setText(visaPermitNumber);
            if(!visaPermitExpiry.equalsIgnoreCase(""))
            {
                visaPermitExpiryTxt.setText(AppUtils.dateConversionMMM(visaPermitExpiry));
            }
        }
        else
        {
            if (itemId.equalsIgnoreCase(""))
            {
                isStudentMalasiyanChecked=false;
                visaPermitNumberTxt.setHint(R.string.AST_VISA_PERMIT_TEXT_WITH_RED);
                visaPermitExpiryTxt.setHint(R.string.AST_VISA_PERMIT_EXPIRY_WITH_RED);
                checkStudentMalysianImg.setImageResource(R.drawable.check_box_header);
                visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey);
                uploadVisa.setAlpha(1.0f);
                uploadVisaTxt.setAlpha(1.0f);
                uploadVisa.setEnabled(true);
                visaPermitNumberTxt.setEnabled(true);
                visaPermitExpiryTxt.setEnabled(true);
                visaPermitNumberTxt.setText(visaPermitNumber);
                if(!visaPermitExpiry.equalsIgnoreCase(""))
                {
                    visaPermitExpiryTxt.setText(AppUtils.dateConversionMMM(visaPermitExpiry));
                }
            }
            else
            {
                isStudentMalasiyanChecked=true;
                visaPermitNumberTxt.setHint(R.string.VISA_PERMIT_TXT);
                visaPermitExpiryTxt.setHint(R.string.VISA_PERMIT_EXPIRY);
                checkStudentMalysianImg.setImageResource(R.drawable.check_box_header_tick);
                visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                uploadVisaTxt.setAlpha(0.5f);
                uploadVisa.setAlpha(0.5f);
                visaPermitNumberTxt.setEnabled(false);
                visaPermitExpiryTxt.setEnabled(false);
            }


        }
        if(visaImgName.equalsIgnoreCase(""))
        {
            VisaImageName.setText(visaImgName);
        }
        else
        {
            VisaImageName.setText(visaImgName);
        }
        if(passportImgName.equalsIgnoreCase(""))
        {
            PassportImageName.setText(passportImgName);
        }
        else
        {
            PassportImageName.setText(passportImgName);
        }
        if(visaImgPath.equalsIgnoreCase(""))
        {

        }
        else
        {
            ViewSelectedVisa.setImageBitmap(BitmapFactory.decodeFile(visaImgPath));
        }
        System.out.println("passportImgPath"+passportImgPath);
        System.out.println("passportImgPath name"+passportImgName);
        if(passportImgPath.equalsIgnoreCase(""))
        {

        }
        else
        {
            ViewSelectedPassport.setImageBitmap(BitmapFactory.decodeFile(passportImgPath));
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
               // view.setMinDate(System.currentTimeMillis() - 1000);
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };
   DatePickerDialog.OnDateSetListener datePassport = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                //view.setMinDate(System.currentTimeMillis() - 1000);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelPassport();

            }

        };

        visaPermitExpiryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(new Date().getTime());
                dpd.show();
//                new DatePickerDialog(getContext(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        passportExpiryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
               // ShowDatePicker(v);

                DatePickerDialog dpd = new DatePickerDialog(getContext(), datePassport, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(new Date().getTime());
                dpd.show();

            }
        });

         CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

//                if (AppController.isInsuranceEdited || AppController.isPassportEdited)
//                {
//                    ShowDiscardDialog(mContext, "Confirm?", "Do you want to Discard changes?", R.drawable.questionmark_icon, R.drawable.round);
//                }
//                else
//                {
//                    getActivity().finish();
//                }


                String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                String dataStatus="";
                String dataRequest="";
                if(dataId.equalsIgnoreCase(""))
                {
                     dataStatus="0";
                     dataRequest="1";
                }
                else
                {
                     dataStatus="1";
                     dataRequest="0";
                }

                String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
                String newData=passportNationalityTxt.getText().toString();
                System.out.println("Nationality"+passportNationalityTxt.getText().toString());

                //  Log.e("INSURANCE ", "textChange"+);

                PassportDetailModel mModel=new PassportDetailModel();
                mModel.setId(dataId);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
                mModel.setNationality(passportNationalityTxt.getText().toString());
                mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
                mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
                mModel.setVisa_permit_no(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_permit_no());
                mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
                {
                    //  AppUtils.dateConversionYToD
                    mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
                }
                else
                {
                    mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
                }
                mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
                mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
                mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
                mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
                mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
                mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
                mModel.setVisa(visaValue);
                mModel.setStatus(dataStatus);
                mModel.setRequest(dataRequest);
                mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
                mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
                AppController.mPassportDetailArrayList.remove(dataPosition);
                AppController.mPassportDetailArrayList.add(dataPosition,mModel);
                Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
                for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
                {
                    Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

                }
                PreferenceManager.getPassportDetailArrayList(mContext).clear();
                System.out.println("close passport detail size"+PreferenceManager.getPassportDetailArrayList(mContext).size());
                PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);

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
                }




                getActivity().finish();

            }
        });

        checkStudentMalysianImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Visa value starting"+visaValue);
                if (isStudentMalasiyanChecked)
                {
                    visaPermitNumberTxt.setHint(R.string.AST_VISA_PERMIT_TEXT_WITH_RED);
                    visaPermitExpiryTxt.setHint(R.string.AST_VISA_PERMIT_EXPIRY_WITH_RED);
                    System.out.println("if works");
                    isStudentMalasiyanChecked=false;
                    visaPermitNumberTxt.setEnabled(true);
                    visaPermitExpiryTxt.setEnabled(true);
                    checkStudentMalysianImg.setImageResource(R.drawable.check_box_header);
                    visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    uploadVisa.setAlpha(1.0f);
                    uploadVisaTxt.setAlpha(1.0f);
                    uploadVisa.setEnabled(true);
                    uploadVisa.setClickable(true);
                    IsNational = "YES";
                    visaValue="1";
                    AppController.isPassportEdited=true;
                    String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                    String dataStatus="";
                    String dataRequest="";
                    if(dataId.equalsIgnoreCase(""))
                    {
                        dataStatus="0";
                        dataRequest="1";
                    }
                    else
                    {
                        dataStatus="1";
                        dataRequest="0";
                    }

                    String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
                    String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
                    PassportDetailModel mModel=new PassportDetailModel();
                    mModel.setId(dataId);
                    mModel.setStudent_id(studentId);
                    mModel.setStudent_name(studentNamePass);
                    mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
                    mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
                    mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
                    mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
                    mModel.setVisa_permit_no("");
                    if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
                    {
                      //  AppUtils.dateConversionYToD
                        mModel.setVisa_permit_expiry_date("");
                    }
                    else
                    {
                        mModel.setVisa_permit_expiry_date("");
                    }
                    visaImageData="";
                    visa_image_name_path="";
                    visa_image_path="";
                    VisaImageName.setText("");
                    ViewSelectedVisa.setImageResource(R.color.white);
                    mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
                    mModel.setVisa(visaValue);
                    mModel.setVisa_image("");
                    mModel.setVisa_image_name("");
                    mModel.setVisa_image_path("");
                    mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
                    mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
                    mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
                    mModel.setStatus(dataStatus);
                    mModel.setRequest(dataRequest);
                    mModel.setCreated_at(dataCreatedAt);
                    mModel.setUpdated_at(dataUpdatedAt);
                    mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                    mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                    mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
                    mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
                    AppController.mPassportDetailArrayList.remove(dataPosition);
                    AppController.mPassportDetailArrayList.add(dataPosition,mModel);
                    PreferenceManager.getPassportDetailArrayList(mContext).clear();
                    PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                }
                else
                {
                    visaPermitNumberTxt.setHint(R.string.VISA_PERMIT_TXT);
                    visaPermitExpiryTxt.setHint(R.string.VISA_PERMIT_EXPIRY);
                    System.out.println("if works else");
                    isStudentMalasiyanChecked=true;
                    checkStudentMalysianImg.setImageResource(R.drawable.check_box_header_tick);
                    visaPermitNumberTxt.setText("");
                    visaPermitExpiryTxt.setText("");
                    visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    uploadVisaTxt.setAlpha(0.5f);
                    uploadVisa.setAlpha(0.5f);
                    uploadVisa.setEnabled(false);
                    uploadVisa.setClickable(false);
                    visaPermitNumberTxt.setEnabled(false);
                    visaPermitExpiryTxt.setEnabled(false);
                    IsNational = "NO";
                    visaValue="0";
                    AppController.isPassportEdited=true;
                    String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                    String dataStatus="";
                    String dataRequest="";
                    if(dataId.equalsIgnoreCase(""))
                    {
                        dataStatus="0";
                        dataRequest="1";
                    }
                    else
                    {
                        dataStatus="1";
                        dataRequest="0";
                    }
                    String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
                    String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
                    PassportDetailModel mModel=new PassportDetailModel();
                    mModel.setId(dataId);
                    mModel.setStudent_id(studentId);
                    mModel.setStudent_name(studentNamePass);
                    mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                    mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                    mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
                    mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
                    mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
                    mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
                    mModel.setVisa_permit_no("");
                    if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
                    {
                        //  AppUtils.dateConversionYToD
                        System.out.println("data");
                        mModel.setVisa_permit_expiry_date("");
                    }
                    else
                    {
                        mModel.setVisa_permit_expiry_date("");
                    }
                    visaImageData="";
                    visa_image_name_path="";
                    visa_image_path="";
                    VisaImageName.setText("");
                    ViewSelectedVisa.setImageResource(R.color.white);
                    mModel.setVisa_image("");
                    mModel.setVisa_image_name("");
                    mModel.setVisa_image_path("");
                    mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
                    mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
                    mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
                    mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
                    mModel.setVisa(visaValue);
                    mModel.setStatus(dataStatus);
                    mModel.setRequest(dataRequest);
                    mModel.setCreated_at(dataCreatedAt);
                    mModel.setUpdated_at(dataUpdatedAt);
                    mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                    mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                    mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
                    mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
                    AppController.mPassportDetailArrayList.remove(dataPosition);
                    AppController.mPassportDetailArrayList.add(dataPosition,mModel);
                    PreferenceManager.getPassportDetailArrayList(mContext).clear();
                    PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
                }
            }
        });

        uploadVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write
                    chooseVisa();

                } else
                    {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                }
            }
        });

        UploadPasspost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write

                    chooseImage();

                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                }
            }
        });


             //   ConverToJson();
        visaPermitNumberTxt.addTextChangedListener(new TextWatcher()
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
                AppController.isPassportEdited=true;
                String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                String dataStatus="";
                String dataRequest="";
                if(dataId.equalsIgnoreCase(""))
                {
                    dataStatus="0";
                    dataRequest="1";
                }
                else
                {
                    dataStatus="1";
                    dataRequest="0";
                }
                String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
                String newData=visaPermitNumberTxt.getText().toString();

                //  Log.e("INSURANCE ", "textChange"+);

                PassportDetailModel mModel=new PassportDetailModel();
                mModel.setId(dataId);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
                mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
                mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
                mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
                mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                mModel.setVisa_permit_no(newData);
                if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
                {
                    //  AppUtils.dateConversionYToD
                    mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
                }
                else
                {
                    mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
                }
                mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
                mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
                mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
                mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
                mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
                mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
                mModel.setVisa(visaValue);
                mModel.setStatus(dataStatus);
                mModel.setRequest(dataRequest);
                mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
                mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
                AppController.mPassportDetailArrayList.remove(dataPosition);
                AppController.mPassportDetailArrayList.add(dataPosition,mModel);
                Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
                for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
                {
                    Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

                }
                PreferenceManager.getPassportDetailArrayList(mContext).clear();
                PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext
                );
            }


        });
        passportNumberTxt.addTextChangedListener(new TextWatcher()
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
                AppController.isPassportEdited=true;
                String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                String dataStatus="";
                String dataRequest="";
                if(dataId.equalsIgnoreCase(""))
                {
                    dataStatus="0";
                    dataRequest="1";
                }
                else
                {
                    dataStatus="1";
                    dataRequest="0";
                }
                String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
                String newData=passportNumberTxt.getText().toString();

                //  Log.e("INSURANCE ", "textChange"+);

                PassportDetailModel mModel=new PassportDetailModel();
                mModel.setId(dataId);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setPassport_number(newData);
                mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
                mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
                mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
                mModel.setVisa_permit_no(newData);
                if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
                {
                    //  AppUtils.dateConversionYToD
                    mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
                }
                else
                {
                    mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
                }
                mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
                mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
                mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
                mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
                mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
                mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
                mModel.setVisa(visaValue);
                mModel.setStatus(dataStatus);
                mModel.setRequest(dataRequest);
                mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
                mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
                AppController.mPassportDetailArrayList.remove(dataPosition);
                AppController.mPassportDetailArrayList.add(dataPosition,mModel);
                Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
                for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
                {
                    Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

                }
                PreferenceManager.getPassportDetailArrayList(mContext).clear();
                PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
            }


        });

//        passportNationalityTxt.addTextChangedListener(new TextWatcher()
//        {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // TODO Auto-generated method stub
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//
//            {
//                AppController.isPassportEdited=true;
//                String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
//                String dataStatus="";
//                String dataRequest="";
//                if(dataId.equalsIgnoreCase(""))
//                {
//                    dataStatus="0";
//                    dataRequest="1";
//                }
//                else
//                {
//                    dataStatus="1";
//                    dataRequest="0";
//                }
//                String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
//                String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
//                String newData=passportNationalityTxt.getText().toString();
//
//                //  Log.e("INSURANCE ", "textChange"+);
//
//                PassportDetailModel mModel=new PassportDetailModel();
//                mModel.setId(dataId);
//                mModel.setStudent_id(studentId);
//                mModel.setStudent_name(studentNamePass);
//                mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
//                mModel.setNationality(passportNationalityTxt.getText().toString());
//                mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
//                mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
//                mModel.setVisa_permit_no(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_permit_no());
//                if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
//                {
//                    //  AppUtils.dateConversionYToD
//                    mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
//                }
//                else
//                {
//                    mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
//                }
//                mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
//                mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
//                mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
//                mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
//                mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
//                mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
//                mModel.setVisa(visaValue);
//                mModel.setStatus(dataStatus);
//                mModel.setRequest(dataRequest);
//                mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
//                mModel.setCreated_at(dataCreatedAt);
//                mModel.setUpdated_at(dataUpdatedAt);
//                mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
//                mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
//                mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
//                mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
//                AppController.mPassportDetailArrayList.remove(dataPosition);
//                AppController.mPassportDetailArrayList.add(dataPosition,mModel);
//                Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
//                for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
//                {
//                    Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);
//
//                }
//
//                PreferenceManager.getPassportDetailArrayList(mContext).clear();
//                PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
//            }
//
//
//        });
        visaPermitExpiryTxt.addTextChangedListener(new TextWatcher()
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
                AppController.isPassportEdited=true;
                String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
                String dataStatus="";
                String dataRequest="";
                if(dataId.equalsIgnoreCase(""))
                {
                    dataStatus="0";
                    dataRequest="1";
                }
                else
                {
                    dataStatus="1";
                    dataRequest="0";
                }
//                String dataStatus="1";
//                String dataRequest="0";
                String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
                String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
                String newData=visaPermitExpiryTxt.getText().toString();

                //  Log.e("INSURANCE ", "textChange"+);

                PassportDetailModel mModel=new PassportDetailModel();
                mModel.setId(dataId);
                mModel.setStudent_id(studentId);
                mModel.setStudent_name(studentNamePass);
                mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
                mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
                mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
                mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
                mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
                if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
                {
                    //  AppUtils.dateConversionYToD
                    mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
                }
                else
                {
                    mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
                }
                mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
                mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
                mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
                mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
                mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
                mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
                mModel.setStatus(dataStatus);
                mModel.setRequest(dataRequest);
                mModel.setVisa(visaValue);
                mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
                mModel.setCreated_at(dataCreatedAt);
                mModel.setUpdated_at(dataUpdatedAt);
                mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
                mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
                mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
                mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
                AppController.mPassportDetailArrayList.remove(dataPosition);
                AppController.mPassportDetailArrayList.add(dataPosition,mModel);
                Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
                for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
                {
                    Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

                }
                PreferenceManager.getPassportDetailArrayList(mContext).clear();
                PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
            }


        });


        return v;
    }

    private void updateLabel() {
        String myFormat = "yyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DATE = sdf.format(myCalendar.getTime());
        visaPermitExpiryTxt.setText(AppUtils.dateConversionMMM(DATE));
        AppController.isPassportEdited=true;
        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
        String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        String newData=passportExpiryTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }
        if (passportExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setExpiry_date(passportExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setExpiry_date(AppUtils.dateConversionYToD(passportExpiryTxt.getText().toString().trim()));
        }
        mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
        mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
        mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
        mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
        mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setStatus(dataStatus);
        mModel.setRequest(dataRequest);
        mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
        mModel.setVisaDateChanged(true);
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
        for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
        {
            Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

        }
        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
    }
    private void updateLabelPassport() {
        String myFormat = "yyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        DATE = sdf.format(myCalendar.getTime());
        passportExpiryTxt.setText(AppUtils.dateConversionMMM(DATE));
        AppController.isPassportEdited=true;
        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
        String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        String newData=passportExpiryTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }
        if (passportExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setExpiry_date(passportExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setExpiry_date(AppUtils.dateConversionYToD(passportExpiryTxt.getText().toString().trim()));
        }
        mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
        mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
        mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
        mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
        mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setStatus(dataStatus);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
        mModel.setRequest(dataRequest);
        mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setPassportDateChanged(true);
        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
        for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
        {
            Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);

        }
        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
    }

    private void chooseVisa(){
        final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera"))
                {
                    String imageFileName = System.currentTimeMillis() + ".jpg";
                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                    File file = new File(pictureImagePath);
                    Uri outputFileUri = Uri.fromFile(file);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                    startActivityForResult(cameraIntent, VISA_CAMERA_REQUEST);
                }

                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, VISA_GALLERY_REQUEST);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseImage() {
        final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera")) {

                    String imageFileName = System.currentTimeMillis() + ".jpg";
                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                    File file = new File(pictureImagePath);
                    Uri outputFileUri = Uri.fromFile(file);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                    startActivityForResult(cameraIntent, CLICK_IMAGE_REQUEST);
                }

                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK)
        {
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                actualImage = FileUtil.from(mContext, data.getData());
                compressedImage = new Compressor.Builder(mContext)
                        .setMaxWidth(940)
                        .setMaxHeight(800)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .build()
                        .compressToFile(actualImage);
                setCompressedImage();
                passport_image_name_path=compressedImage.getName();
                PassportImageName.setText(compressedImage.getName());

                //clearImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }if (requestCode == CLICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            PassportCamera = new File(pictureImagePath);
              if (PassportCamera.exists()) {
              try {
                  if (data != null) {
                      PassportCamera = FileUtil.from(mContext, data.getData());
                  }
                  CompressPassportCamera = new Compressor.Builder(mContext)
                          .setMaxWidth(940)
                          .setMaxHeight(800)
                          .setQuality(100)
                          .setCompressFormat(Bitmap.CompressFormat.JPEG)
                          .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                  Environment.DIRECTORY_PICTURES).getAbsolutePath())
                          .build()
                          .compressToFile(PassportCamera);
                  CompressPassportCamera();
                  PassportImageName.setText(CompressPassportCamera.getName());
                  passport_image_name_path=CompressPassportCamera.getName();

//                  Toast.makeText(mContext, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        }
        if (requestCode == VISA_CAMERA_REQUEST && resultCode == RESULT_OK){
            VisaCamera = new File(pictureImagePath);
            if (VisaCamera.exists()) {
                try {
                    if (data != null) {
                        VisaCamera = FileUtil.from(mContext, data.getData());
                    }
                    CompressVisaCamera = new Compressor.Builder(mContext)
                            .setMaxWidth(940)
                            .setMaxHeight(800)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(VisaCamera);
                    CompressVISAcamera();
                    VisaImageName.setText(CompressVisaCamera.getName());
                    visa_image_name_path=CompressVisaCamera.getName();
//                    Toast.makeText(mContext, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == VISA_GALLERY_REQUEST && resultCode == RESULT_OK){
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                VisaactualImage = FileUtil.from(mContext, data.getData());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    VisacompressedImage = new Compressor.Builder(mContext)
                            .setMaxWidth(940)
                            .setMaxHeight(800)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(VisaactualImage);
                }
                setVisaCompressedImage();

                VisaImageName.setText(VisacompressedImage.getName());
                visa_image_name_path=VisacompressedImage.getName();
                //clearImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }

    private void CompressPassportCamera()
    {

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(CompressPassportCamera.getPath(), options);
        ViewSelectedPassport.setImageBitmap(BitmapFactory.decodeFile(CompressPassportCamera.getPath()));
        passport_image_path=CompressPassportCamera.getPath();
        passport_image_name_path=CompressPassportCamera.getName();

        InputStream inputStream = null; // You can get an inputStream using any I/O API
        try {
            inputStream = new FileInputStream(CompressPassportCamera.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        passportImageData = encodedString;
        AppController.isPassportEdited=true;
        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
        String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        //   String newData=visaPermitNumberTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
        mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }

        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setPassport_image(passportImageData);
        mModel.setPassport_image_path(passport_image_path);
        mModel.setPassport_image_name(passport_image_name_path);
        mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
        mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
        mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());

        mModel.setStatus(dataStatus);
        mModel.setRequest(dataRequest);

        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);

//        PassportImageModel model = new PassportImageModel();
//        model.setPassportPath(CompressPassportCamera.getPath());
//        model.setStudent_id("upload_passport_"+stud_id);
//
//        ThirdSubmitArray.add(model);
//        PreferenceManager.savePassportPathArrayList(ThirdSubmitArray,mContext);
    }

    private void CompressVISAcamera() {

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(CompressVisaCamera.getPath(), options);
        ViewSelectedVisa.setImageBitmap(BitmapFactory.decodeFile(CompressVisaCamera.getPath()));
        visa_image_path=CompressVisaCamera.getPath();
        visa_image_name_path=CompressVisaCamera.getName();
        InputStream inputStream = null; // You can get an inputStream using any I/O API
        try {
            inputStream = new FileInputStream(CompressVisaCamera.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        visaImageData = encodedString;
        AppController.isPassportEdited=true;
        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
        String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        //   String newData=visaPermitNumberTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
        mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }


        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setStatus(dataStatus);
        mModel.setRequest(dataRequest);

        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());

        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
        mModel.setVisa_image(visaImageData);
        mModel.setVisa_image_name(visa_image_name_path);
        mModel.setVisa_image_path(visa_image_path);
        mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
        mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
        mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
    }
    private void setVisaCompressedImage()
    {
        Toast.makeText(mContext, "Compressed image save in " + VisacompressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + VisacompressedImage.getPath());

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(VisacompressedImage.getPath(), options);
        ViewSelectedVisa.setImageBitmap(BitmapFactory.decodeFile(VisacompressedImage.getPath()));
        visa_image_path=VisacompressedImage.getPath();
        visa_image_name_path=VisacompressedImage.getName();

        InputStream inputStream = null; // You can get an inputStream using any I/O API
        try
        {
            inputStream = new FileInputStream(VisacompressedImage.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        visaImageData = encodedString;
        AppController.isPassportEdited=true;
        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
        String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        //   String newData=visaPermitNumberTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
        mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }

        mModel.setVisa_image(visaImageData);
        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setVisa_image_name(visa_image_name_path);
        mModel.setVisa_image_path(visa_image_path);
        mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
        mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
        mModel.setStatus(dataStatus);
        mModel.setRequest(dataRequest);
        mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
    }

    public void showError(String errorMessage) {
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setCompressedImage() {
        Toast.makeText(mContext, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(compressedImage.getPath(), options);
        ViewSelectedPassport.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getPath()));
        passport_image_path=compressedImage.getPath();
        passport_image_name_path=compressedImage.getName();

        InputStream inputStream = null; // You can get an inputStream using any I/O API
        try {
            inputStream = new FileInputStream(compressedImage.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        passportImageData = encodedString;
        AppController.isPassportEdited=true;
        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
        String dataStatus="";
        String dataRequest="";
        if(dataId.equalsIgnoreCase(""))
        {
            dataStatus="0";
            dataRequest="1";
        }
        else
        {
            dataStatus="1";
            dataRequest="0";
        }
        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
        //   String newData=visaPermitNumberTxt.getText().toString();

        //  Log.e("INSURANCE ", "textChange"+);

        PassportDetailModel mModel=new PassportDetailModel();
        mModel.setId(dataId);
        mModel.setStudent_id(studentId);
        mModel.setStudent_name(studentNamePass);
        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
        mModel.setNationality(AppController.mPassportDetailArrayList.get(dataPosition).getNationality());
        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
        mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
        mModel.setVisa_permit_no(visaPermitNumberTxt.getText().toString().trim());
        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
        {
            //  AppUtils.dateConversionYToD
            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
        }
        else
        {
            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
        }

        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
        mModel.setVisa(visaValue);
        mModel.setStatus(dataStatus);
        mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
        mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
        mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
        mModel.setPassport_image_path(passport_image_path);
        mModel.setPassport_image_name(passport_image_name_path);
        mModel.setRequest(dataRequest);
        mModel.setPassport_image(passportImageData);
        mModel.setCreated_at(dataCreatedAt);
        mModel.setUpdated_at(dataUpdatedAt);
        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
        AppController.mPassportDetailArrayList.remove(dataPosition);
        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
        PreferenceManager.getPassportDetailArrayList(mContext).clear();
        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
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
//    public void ShowDatePicker(View view){
//        DatePickerDialog dpd = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//            }
//        }, year, month, day);
//        //disaple past date
//        dpd.getDatePicker().setMinDate(new Date().getTime());
//        dpd.show();
//    }


//    private void setDataToAdapter(ArrayList<String> arrayList)
//    {
//        spinner.setAlpha(0.0f);
//        // Creating ArrayAdapter using the string array and default spinner layout
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayList);
//        // Specify layout to be used when list of choices appears
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Applying the adapter to our spinner
//        spinner.setAdapter(arrayAdapter);
//        spinner.setOnItemSelectedListener(this);
//    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String selectedItemText = parent.getItemAtPosition(position).toString();
//        spinner.setTitle("Select Nationality");
//        System.out.println("This is working");
//        Toast.makeText(mContext, " You select >> " + selectedItemText, Toast.LENGTH_SHORT).show();
//        passportNationalityTxt.setText(selectedItemText);
//        AppController.isPassportEdited=true;
//        String dataId=AppController.mPassportDetailArrayList.get(dataPosition).getId();
//        String dataStatus="";
//        String dataRequest="";
//        if(dataId.equalsIgnoreCase(""))
//        {
//            dataStatus="0";
//            dataRequest="1";
//        }
//        else
//        {
//            dataStatus="1";
//            dataRequest="0";
//        }
//        String dataCreatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getCreated_at();
//        String dataUpdatedAt=AppController.mPassportDetailArrayList.get(dataPosition).getUpdated_at();
//        String newData=selectedItemText;
//
//        //  Log.e("INSURANCE ", "textChange"+);
//
//        PassportDetailModel mModel=new PassportDetailModel();
//        mModel.setId(dataId);
//        mModel.setStudent_id(studentId);
//        mModel.setStudent_name(studentNamePass);
//        mModel.setPassport_number(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_number());
//        mModel.setNationality(passportNationalityTxt.getText().toString());
//        mModel.setDate_of_issue(AppController.mPassportDetailArrayList.get(dataPosition).getDate_of_issue());
//        mModel.setExpiry_date(AppController.mPassportDetailArrayList.get(dataPosition).getExpiry_date());
//        mModel.setVisa_permit_no(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_permit_no());
//        if (visaPermitExpiryTxt.getText().toString().equalsIgnoreCase(""))
//        {
//            //  AppUtils.dateConversionYToD
//            mModel.setVisa_permit_expiry_date(visaPermitExpiryTxt.getText().toString().trim());
//        }
//        else
//        {
//            mModel.setVisa_permit_expiry_date(AppUtils.dateConversionYToD(visaPermitExpiryTxt.getText().toString().trim()));
//        }
//        mModel.setVisa_image(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image());
//        mModel.setVisa_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_name());
//        mModel.setVisa_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_image_path());
//        mModel.setPassport_image_path(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_path());
//        mModel.setPassport_image_name(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image_name());
//        mModel.setPhoto_no_consent(AppController.mPassportDetailArrayList.get(dataPosition).getPhoto_no_consent());
//        mModel.setVisa(visaValue);
//        mModel.setStatus(dataStatus);
//        mModel.setRequest(dataRequest);
//        mModel.setPassport_image(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_image());
//        mModel.setCreated_at(dataCreatedAt);
//        mModel.setUpdated_at(dataUpdatedAt);
//        mModel.setVisa_expired(AppController.mPassportDetailArrayList.get(dataPosition).getVisa_expired());
//        mModel.setPassport_expired(AppController.mPassportDetailArrayList.get(dataPosition).getPassport_expired());
//        mModel.setPassportDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isPassportDateChanged());
//        mModel.setVisaDateChanged(AppController.mPassportDetailArrayList.get(dataPosition).isVisaDateChanged());
//        AppController.mPassportDetailArrayList.remove(dataPosition);
//        AppController.mPassportDetailArrayList.add(dataPosition,mModel);
//        Log.e("Ins asize", String.valueOf(AppController.mPassportDetailArrayList.size()));
//        for (int j=0;j<AppController.mPassportDetailArrayList.size();j++)
//        {
//            Log.e("Ins after",AppController.mPassportDetailArrayList.get(j).getId() +j);
//
//        }
//
//        PreferenceManager.getPassportDetailArrayList(mContext).clear();
//        PreferenceManager.savePassportDetailArrayList(AppController.mPassportDetailArrayList,mContext);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}

