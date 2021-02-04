package com.mobatia.bskl.activity.datacollection_p2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.DataCollectionHome;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportImageModel;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class ThirdScreen extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

    ImageView checkStudentMalysianImg,CloseIcon;
    TextView visaPermitNumberTxt,visaPermitExpiryTxt,UploadPasspost;
    boolean isStudentMalasiyanChecked=false;
    TextView uploadVisa,uploadVisaTxt;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    TextView studentName;
    String stud_id = "",id="",passportnumber="",dateofIssue="",expdate="",visa="",photo_no_conset="",status="",request="",created_at="",updated="",StudentName="",passportCheck="0";
    String studClass = "";
    String alumini = "";
    String progressreport = "";
    String stud_img = "";
    String PassportImage = "";
    ImageView studImg;
    private Context mContext;
    LinearLayout mStudentSpinner;
    TextView passportNationalityTxt,passportNumberTxt,passportExpiryTxt;
    ArrayList<PassportDetailModel>mPassportDetailArrayList;
    String passportNationality="";
    String passportNumber="";
    String passportExpiry="";
    String visaPermitNumber="";
    String visaPermitExpiry="";
    String Status = "5";
    ArrayList<PassportDetailModel> passportArray = new ArrayList<>();
    ArrayList<String> PassposrtArra = new ArrayList<>();
    TextView PassportImageName,VisaImageName;
    String IsNational = "YES";

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CLICK_IMAGE_REQUEST = 2;

    private static final int VISA_CAMERA_REQUEST = 3;
    private static final int VISA_GALLERY_REQUEST = 4;

    File PassportCamera;
    File CompressPassportCamera;
    File VisaCamera;
    File CompressVisaCamera;

    private File actualImage;
    private File VisaactualImage;
    private File compressedImage;
    private File VisacompressedImage;
    Uri imageUri;
    private int IMAGE_CAPTURE_CODE = 1001;
    private String pictureImagePath = "";
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    ArrayList<PassportImageModel> ThirdSubmitArray = new ArrayList<>();
    ArrayList<VISAimageModel> VIASSubmitArray = new ArrayList<>();


    public ThirdScreen() {
        // Required empty public constructor
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
        PassportImageName = v.findViewById(R.id.PassImageName);
        VisaImageName = v.findViewById(R.id.VisaImageName);
        passportNationalityTxt=v.findViewById(R.id.passportNationalityTxt);
        passportNumberTxt=v.findViewById(R.id.passportNumberTxt);
        passportExpiryTxt=v.findViewById(R.id.passportExpiryTxt);
        mStudentSpinner = v.findViewById(R.id.studentSpinner);
        UploadPasspost = v.findViewById(R.id.uploadPassportTxt);
        CloseIcon = v.findViewById(R.id.closeImg);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mPassportDetailArrayList=PreferenceManager.getPassportDetailArrayList(mContext);
        if (AppUtils.isNetworkConnected(mContext)) {
            System.out.println("Api call working");
            getStudentsListAPI(URL_GET_STUDENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

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

        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
                if (studentsModelArrayList.size() > 0) {
                    showSocialmediaList(studentsModelArrayList);
//                    ConverToJson();
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
        checkStudentMalysianImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (isStudentMalasiyanChecked)
                {
                    System.out.println("if works");
                    isStudentMalasiyanChecked=false;
                    checkStudentMalysianImg.setImageResource(R.drawable.uncheck);
//                    visaPermitNumberTxt.setEnabled(true);
//                    visaPermitExpiryTxt.setEnabled(true);
//                    visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
//                    visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey);
                    uploadVisa.setAlpha(1.0f);
                    uploadVisaTxt.setAlpha(1.0f);
                    uploadVisa.setEnabled(true);
                    IsNational = "YES";
                }
                else
                {
                    System.out.println("if works else");
                    isStudentMalasiyanChecked=true;
                    checkStudentMalysianImg.setImageResource(R.drawable.checked);
                    visaPermitNumberTxt.setText("");
                    visaPermitExpiryTxt.setText("");
                    visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    uploadVisaTxt.setAlpha(0.5f);
                    uploadVisa.setAlpha(0.5f);
                    uploadVisa.setEnabled(false);
                    IsNational = "NO";
                }
            }
        });

        uploadVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write
                    chooseVisa();

                } else {
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


                ConverToJson();


        return v;
    }

    private void SaveData() {
        PassportDetailModel model = new PassportDetailModel();
        model.setIsNational(IsNational);
        model.setStudent_id(stud_id);
        passportArray.add(model);

        System.out.println("STATE: "+IsNational+" , "+stud_id);
    }

    private void ConverToJson(){

        PassportDetailModel model = new PassportDetailModel();
        model.setId(id);
        model.setStudent_id(stud_id);
        model.setStudent_name(StudentName);
        model.setPassport_number(passportNumber);
        model.setNationality(passportNationality);
        model.setDate_of_issue(dateofIssue);
        model.setExpiry_date(expdate);
        model.setNot_have_a_valid_passport(passportCheck);
        model.setVisa_permit_no(visaPermitNumber);
        model.setVisa_permit_expiry_date(visaPermitExpiry);
        model.setVisa_image("");
        model.setPhoto_no_consent(photo_no_conset);
//        model.setStatus(Status);
        if(id.equalsIgnoreCase(""))
        {
            model.setStatus("0");
            model.setRequest("1");
        }
        else
        {
            model.setStatus("1");
            model.setRequest("0");
        }

        model.setPassport_image(PassportImage);
        model.setCreated_at(created_at);
        model.setUpdated_at(updated);

        Gson gson   = new Gson();
        String json = gson.toJson(model);
        PassposrtArra.add(json);

        PreferenceManager.saveThirdFragmentJSONArrayList(PassposrtArra,mContext);
        for (int i = 0;i<PassposrtArra.size();i++){
            System.out.println("TJSON: "+PassposrtArra.get(i));
        }

    }

    private void chooseVisa() {
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

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
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

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                actualImage = FileUtil.from(mContext, data.getData());
                // actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
//                actualSizeTextView.setText(String.format("Actual Size : %s", getReadableFileSize(actualImage.length())));
//                String actualsize = actualSizeTextView.getText().toString();

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
                // actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
//                actualSizeTextView.setText(String.format("Actual Size : %s", getReadableFileSize(actualImage.length())));
//                String actualsize = actualSizeTextView.getText().toString();

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
                //clearImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }

    private void CompressPassportCamera() {
        PassportImageModel model = new PassportImageModel();
        model.setPassportPath(CompressPassportCamera.getPath());
        model.setStudent_id("upload_passport_"+stud_id);

        ThirdSubmitArray.add(model);
        PreferenceManager.savePassportPathArrayList(ThirdSubmitArray,mContext);
    }

    private void CompressVISAcamera() {

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



        VISAimageModel model = new VISAimageModel();
        model.setVisaPath(CompressVisaCamera.getPath());
        model.setStudent_id("upload_visa_"+stud_id);

        VIASSubmitArray.add(model);
        PreferenceManager.saveVisaPathArrayList(VIASSubmitArray,mContext);
    }

    private void setVisaCompressedImage() {
        Toast.makeText(mContext, "Compressed image save in " + VisacompressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + VisacompressedImage.getPath());

        if (VisacompressedImage.getPath() == null){
            Status = "5";
        }else {
            Status = "1";
        }

        VISAimageModel model = new VISAimageModel();
        model.setVisaPath(VisacompressedImage.getPath());
        model.setStudent_id("upload_visa_"+stud_id);

//        AppController.PassportPathArray.add(model);
        VIASSubmitArray.add(model);
        PreferenceManager.saveVisaPathArrayList(VIASSubmitArray,mContext);

        for (int i = 0;i<VIASSubmitArray.size();i++){
            if (VIASSubmitArray.get(i).getStudent_id().equalsIgnoreCase(stud_id)){
                System.out.println("IMAGE: VISA_Path: "+VIASSubmitArray.get(i).getVisaPath()+ " USER_ID===> "+VIASSubmitArray.get(i).getStudent_id());
            }
        }

    }

    public void showError(String errorMessage) {
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setCompressedImage() {
        Toast.makeText(mContext, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());

        if (compressedImage.getPath() == null){
            Status = "5";
        }else {
            Status = "1";
        }

        PassportImageModel model = new PassportImageModel();
        model.setPassportPath(compressedImage.getPath());
        model.setStudent_id("upload_passport_"+stud_id);

        AppController.PassportPathArray.add(model);

        ThirdSubmitArray.add(model);

        PreferenceManager.savePassportPathArrayList(ThirdSubmitArray,mContext);

        for (int i = 0;i<ThirdSubmitArray.size();i++){
//            System.out.println("IMAGE: Path: "+ThirdSubmitArray.get(i).getPassportPath()+" ID: "+ThirdSubmitArray.get(i).getStudent_id()+" Visa: "+ThirdSubmitArray.get(i).getVisaPath());
            if (ThirdSubmitArray.get(i).getStudent_id().equalsIgnoreCase(stud_id)){
//                System.out.println("IMAGE: Path: "+ThirdSubmitArray.get(i).getStudent_id().equalsIgnoreCase(stud_id));
                System.out.println("IMAGE: PASS_Path: "+ThirdSubmitArray.get(i).getPassportPath()+ " USER_ID===> "+VIASSubmitArray.get(i).getStudent_id());
            }

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
                System.out.println("PASS RESPO" + successResponse);
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
                                System.out.println("SIE: "+studentsModelArrayList.size());
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
                                //  belowViewRelative.setVisibility(View.VISIBLE);
                                //newRequest.setVisibility(View.VISIBLE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    //   getList(URL_GET_LEAVEREQUEST_LIST, stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                                 if (mPassportDetailArrayList.size()>0)
                                 {
                                     for(int i=0;i<mPassportDetailArrayList.size();i++)
                                     {
                                         if (stud_id.equalsIgnoreCase(mPassportDetailArrayList.get(i).getStudent_id()))
                                         {
                                             passportNationality=mPassportDetailArrayList.get(i).getNationality();
                                             passportNumber=mPassportDetailArrayList.get(i).getPassport_number();
                                             passportExpiry=mPassportDetailArrayList.get(i).getExpiry_date();
                                             visaPermitExpiry=mPassportDetailArrayList.get(i).getVisa_permit_expiry_date();
                                             visaPermitNumber=mPassportDetailArrayList.get(i).getVisa_permit_no();
                                             visaPermitNumber = mPassportDetailArrayList.get(i).getVisa_permit_no();
                                             visaPermitExpiry = mPassportDetailArrayList.get(i).getVisa_permit_expiry_date();
                                             id = mPassportDetailArrayList.get(i).getId();
                                             StudentName = mPassportDetailArrayList.get(i).getStudent_name();
                                             dateofIssue = mPassportDetailArrayList.get(i).getDate_of_issue();
                                             PassportImage = mPassportDetailArrayList.get(i).getPassport_image();
                                             expdate = mPassportDetailArrayList.get(i).getExpiry_date();
                                             passportCheck = mPassportDetailArrayList.get(i).getNot_have_a_valid_passport();
                                             visaPermitNumber = mPassportDetailArrayList.get(i).getVisa_permit_no();
                                             visaPermitExpiry = mPassportDetailArrayList.get(i).getVisa_permit_expiry_date();
                                             photo_no_conset = mPassportDetailArrayList.get(i).getPhoto_no_consent();
                                             created_at = mPassportDetailArrayList.get(i).getCreated_at();
                                             updated = mPassportDetailArrayList.get(i).getUpdated_at();


                                         }
                                     }
                                 }
                                 if (passportNationality.equalsIgnoreCase(""))
                                 {
                                     passportNationalityTxt.setText("");
                                 }
                                 else
                                 {
                                     passportNationalityTxt.setText(passportNationality);
                                 }
                                 if (passportNumber.equalsIgnoreCase(""))
                                 {
                                     passportNumberTxt.setText("");
                                 }
                                 else
                                 {
                                     passportNumberTxt.setText(passportNumber);
                                 }
                                 if (passportExpiry.equalsIgnoreCase(""))
                                 {
                                     passportExpiryTxt.setText("");
                                 }
                                 else
                                 {
                                     passportExpiryTxt.setText(passportExpiry);
                                 }
                                 if (visaPermitNumber.equalsIgnoreCase(""))
                                 {
                                     visaPermitNumberTxt.setText("");
                                 }
                                 else
                                 {
                                     visaPermitNumberTxt.setText(visaPermitNumber);
                                 }
                                 if (visaPermitExpiry.equalsIgnoreCase(""))
                                 {
                                     visaPermitExpiryTxt.setText("");
                                 }
                                 else
                                 {
                                     visaPermitExpiryTxt.setText(visaPermitExpiry);
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
                        dialog.dismiss();
                        IsNational = "YES";

                        isStudentMalasiyanChecked=false;
                        checkStudentMalysianImg.setImageResource(R.drawable.uncheck);
                        uploadVisa.setAlpha(1.0f);
                        uploadVisaTxt.setAlpha(1.0f);
                        uploadVisa.setEnabled(true);
                        visaPermitNumberTxt.setEnabled(true);
                        visaPermitExpiryTxt.setEnabled(true);
                        visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey);
                        visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey);

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

                            if (mPassportDetailArrayList.size()>0)
                            {
                                for(int i=0;i<mPassportDetailArrayList.size();i++)
                                {
                                    if (stud_id.equalsIgnoreCase(mPassportDetailArrayList.get(i).getStudent_id()))
                                    {
                                        passportNationality=mPassportDetailArrayList.get(i).getNationality();
                                        passportNumber=mPassportDetailArrayList.get(i).getPassport_number();
                                        passportExpiry=mPassportDetailArrayList.get(i).getExpiry_date();
                                        visaPermitExpiry=mPassportDetailArrayList.get(i).getVisa_permit_expiry_date();
                                        visaPermitNumber=mPassportDetailArrayList.get(i).getVisa_permit_no();
                                    }
                                }
                            }
                            if (passportNationality.equalsIgnoreCase(""))
                            {
                                passportNationalityTxt.setText("");
                            }
                            else
                            {
                                passportNationalityTxt.setText(passportNationality);
                            }
                            if (passportNumber.equalsIgnoreCase(""))
                            {
                                passportNumberTxt.setText("");
                            }
                            else
                            {
                                passportNumberTxt.setText(passportNumber);
                            }
                            if (passportExpiry.equalsIgnoreCase(""))
                            {
                                passportExpiryTxt.setText("");
                            }
                            else
                            {
                                passportExpiryTxt.setText(passportExpiry);
                            }
                            if (visaPermitNumber.equalsIgnoreCase(""))
                            {
                                visaPermitNumberTxt.setText("");
                            }
                            else
                            {
                                visaPermitNumberTxt.setText(visaPermitNumber);
                            }
                            if (visaPermitExpiry.equalsIgnoreCase(""))
                            {
                                visaPermitExpiryTxt.setText("");
                            }
                            else
                            {
                                visaPermitExpiryTxt.setText(visaPermitExpiry);
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ViewSaved(mStudentArray.get(position).getmId());
                                }
                            },1000);


                            if (AppUtils.isNetworkConnected(mContext)) {
                                //  getList(URL_GET_LEAVEREQUEST_LIST, mStudentArray.get(position).getmId());
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

    private void ViewSaved(String getmId) {
        System.out.println("STATEE: CAlled: "+getmId);
        for (int i = 0; i<passportArray.size();i++){
            System.out.println("STATEE: CAlled: "+passportArray.get(i).getIsNational()+" , "+passportArray.get(i).getStudent_id());
            if (passportArray.get(i).getStudent_id().equals(getmId)){
                System.out.println("STATEE: CAlled: "+passportArray.get(i).getStudent_id().equals(getmId));
                if (passportArray.get(i).getIsNational().equals("YES")){
                    System.out.println("STATEE: CAlled YES");
                    isStudentMalasiyanChecked=false;
                    checkStudentMalysianImg.setImageResource(R.drawable.uncheck);
                    uploadVisa.setAlpha(1.0f);
                    uploadVisaTxt.setAlpha(1.0f);
                    uploadVisa.setEnabled(true);
                    IsNational = "YES";
                }else{
                    System.out.println("STATEE: CAlled ELSE");
                    System.out.println("if works else");
                    isStudentMalasiyanChecked=true;
                    checkStudentMalysianImg.setImageResource(R.drawable.checked);
                    visaPermitNumberTxt.setText("");
                    visaPermitExpiryTxt.setText("");
                    visaPermitNumberTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    visaPermitExpiryTxt.setBackgroundResource(R.drawable.rect_background_grey_checked);
                    uploadVisaTxt.setAlpha(0.5f);
                    uploadVisa.setAlpha(0.5f);
                    uploadVisa.setEnabled(false);
                    IsNational = "NO";
                }
            }
        }
    }

}

