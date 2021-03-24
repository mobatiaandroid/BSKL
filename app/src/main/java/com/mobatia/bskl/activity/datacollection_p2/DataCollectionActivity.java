package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.data_collection.EmergencyDetails;
import com.mobatia.bskl.activity.data_collection.KinDetails;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.volleywrappermanager.CustomDialog;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.mobatia.bskl.constants.JSONConstants.JTAG_RESPONSE;
import static com.mobatia.bskl.constants.JSONConstants.JTAG_RESPONSECODE;
import static com.mobatia.bskl.constants.JSONConstants.JTAG_STATUSCODE;

public class DataCollectionActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

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
    /**
     * {@inheritDoc}
     */

    int previousPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_collection_first_view);
        mContext = this;
        pager = findViewById(R.id.viewPager);
        pager.setAdapter(new DataCollectionActivity.MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        pager.setOffscreenPageLimit(4);
        radioGroup = findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        nextBtn = findViewById(R.id.nextImg);
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
        backBtn = findViewById(R.id.backImg);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });
        submitBtn = findViewById(R.id.submit);


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
                boolean isShown = true, isEmailShown = true;
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-zA-Z0-9._-]+";
                final SharedPreferences prefs = getSharedPreferences("BSKL", MODE_PRIVATE);
                if (position == 2) {
                    //   if (kin_details.length() != 0) {
                    if (previousPage != position) {
                        previousPage = position;
                        ArrayList<KinDetails.KinModel> kinarray = getKinArrayList("kinmodel");
                        ArrayList<EmergencyDetails.EmergencyModel> emergencyarray = getEmergencyArrayList("emergencymodel");

                        if (kinarray == null) {
                            final Dialog dialog = new Dialog(DataCollectionActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                            ImageView icon = dialog.findViewById(R.id.iconImageView);
                            icon.setBackgroundResource(R.drawable.round);
                            icon.setImageResource(R.drawable.exclamationicon);
                            TextView text = dialog.findViewById(R.id.text_dialog);
                            TextView textHead = dialog.findViewById(R.id.alertHead);
                            text.setText("Please add a contact.");
                            textHead.setText("Alert");
                            Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                            dialogButton.setText("Ok");
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    pager.setCurrentItem(1);
                                }
                            });

                            dialog.show();
                        } else {
                            for (int i = 0; i < kinarray.size(); i++) {
                                if ((kinarray.get(i).getRequest().equals("0") && kinarray.get(i).getStatus().equals("1")) || (kinarray.get(i).getRequest().equals("0") && kinarray.get(i).getStatus().equals("2")) ||
                                        (kinarray.get(i).getRequest().equals("1") && kinarray.get(i).getStatus().equals("0"))) {
                                    OverallValue = "2";
                                    Log.e("overall Status: ", OverallValue);
                                } else {
                                    OverallValue = "5";
                                    Log.e("overall Status: ", OverallValue);
                                }
                                if (!OverallValue.equals("2")) {
                                    for (int j = 0; j < emergencyarray.size(); j++) {
                                        if ((emergencyarray.get(j).getRequest().equals("0") && emergencyarray.get(j).getStatus().equals("1")) || (emergencyarray.get(j).getRequest().equals("0") && emergencyarray.get(j).getStatus().equals("2")) ||
                                                (emergencyarray.get(j).getRequest().equals("1") && emergencyarray.get(j).getStatus().equals("0"))) {
                                            OverallValue = "2";
                                            Log.e("overall Status: ", OverallValue);
                                        } else {
                                            OverallValue = "5";
                                            Log.e("overall Status: ", OverallValue);
                                        }
                                    }
                                }
                                if (!kinarray.get(i).getDeleted().equals("1")) {
                                    if (kinarray.get(i).getName().equals("") || kinarray.get(i).getUser_mobile().equals("")) {
                                        if (isShown) {
                                            final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.setCancelable(false);
                                            dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                            ImageView icon = dialog.findViewById(R.id.iconImageView);
                                            icon.setBackgroundResource(R.drawable.round);
                                            icon.setImageResource(R.drawable.exclamationicon);
                                            TextView text = dialog.findViewById(R.id.text_dialog);
                                            TextView textHead = dialog.findViewById(R.id.alertHead);
                                            text.setText("Please fill all the fields marked in red color.");
                                            textHead.setText("Alert");
                                            Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                            dialogButton.setText("Ok");
                                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    SharedPreferences.Editor editor = getSharedPreferences("BSKL", MODE_PRIVATE).edit();
                                                    editor.putString("validationkin", "1");
                                                    editor.apply();
                                                    dialog.dismiss();
                                                    pager.setCurrentItem(1);
                                                }

                                            });
                                            isShown = false;
                                            dialog.show();

                                        } else {
                                            isShown = true;
                                        }

                                    } else {
                                        if (!kinarray.get(i).getEmail().equals("")) {
                                            if (!kinarray.get(i).getEmail().matches(emailPattern)) {
                                                if (isEmailShown) {
                                                    final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    dialog.setCancelable(false);
                                                    dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                                    ImageView icon = dialog.findViewById(R.id.iconImageView);
                                                    icon.setBackgroundResource(R.drawable.round);
                                                    icon.setImageResource(R.drawable.exclamationicon);
                                                    TextView text = dialog.findViewById(R.id.text_dialog);
                                                    TextView textHead = dialog.findViewById(R.id.alertHead);
                                                    text.setText("Please enter valid email.");
                                                    textHead.setText("Alert");
                                                    Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                                    dialogButton.setText("Ok");
                                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            SharedPreferences.Editor editor = getSharedPreferences("BSKL", MODE_PRIVATE).edit();
                                                            editor.putString("validationkin", "1");
                                                            editor.apply();
                                                            dialog.dismiss();
                                                            pager.setCurrentItem(1);
                                                        }
                                                    });
                                                    isEmailShown = false;
                                                    dialog.show();
                                                } else {
                                                    isEmailShown = true;
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                } else if (position == 1) {
                    if (previousPage != position) {
                        previousPage = position;
                        //======================================================================================================================
                        if (!prefs.getString("own_email", "").equals("")) {
                            if (prefs.getString("own_email", "").matches(emailPattern)) {
                                if (!prefs.getString("own_user_mobile", "").equals("")) {
                                    if (!prefs.getString("own_addressline1", "").equals("")) {
                                        if (!prefs.getString("own_town", "").equals("")) {
                                            if (!prefs.getString("own_state", "").equals("")) {
                                                if (!prefs.getString("own_country", "").equals("")) {
                                                    if (!prefs.getString("own_pincode", "").equals("")) {

                                                    } else {
                                                        final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        dialog.setCancelable(false);
                                                        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                                        ImageView icon = dialog.findViewById(R.id.iconImageView);
                                                        icon.setBackgroundResource(R.drawable.round);
                                                        icon.setImageResource(R.drawable.exclamationicon);
                                                        TextView text = dialog.findViewById(R.id.text_dialog);
                                                        TextView textHead = dialog.findViewById(R.id.alertHead);
                                                        text.setText("Please enter the post code.");
                                                        textHead.setText("Alert");
                                                        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                                        dialogButton.setText("Ok");
                                                        dialogButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog.dismiss();
                                                                pager.setCurrentItem(0);
                                                            }
                                                        });

                                                        dialog.show();
                                                    }

                                                } else {
                                                    final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    dialog.setCancelable(false);
                                                    dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                                    ImageView icon = dialog.findViewById(R.id.iconImageView);
                                                    icon.setBackgroundResource(R.drawable.round);
                                                    icon.setImageResource(R.drawable.exclamationicon);
                                                    TextView text = dialog.findViewById(R.id.text_dialog);
                                                    TextView textHead = dialog.findViewById(R.id.alertHead);
                                                    text.setText("Please enter the country.");
                                                    textHead.setText("Alert");
                                                    Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                                    dialogButton.setText("Ok");
                                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                            pager.setCurrentItem(0);
                                                        }
                                                    });

                                                    dialog.show();
                                                }

                                            } else {
                                                final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                dialog.setCancelable(false);
                                                dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                                ImageView icon = dialog.findViewById(R.id.iconImageView);
                                                icon.setBackgroundResource(R.drawable.round);
                                                icon.setImageResource(R.drawable.exclamationicon);
                                                TextView text = dialog.findViewById(R.id.text_dialog);
                                                TextView textHead = dialog.findViewById(R.id.alertHead);
                                                text.setText("Please enter the state.");
                                                textHead.setText("Alert");
                                                Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                                dialogButton.setText("Ok");
                                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                        pager.setCurrentItem(0);
                                                    }
                                                });

                                                dialog.show();
                                            }

                                        } else {
                                            final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.setCancelable(false);
                                            dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                            ImageView icon = dialog.findViewById(R.id.iconImageView);
                                            icon.setBackgroundResource(R.drawable.round);
                                            icon.setImageResource(R.drawable.exclamationicon);
                                            TextView text = dialog.findViewById(R.id.text_dialog);
                                            TextView textHead = dialog.findViewById(R.id.alertHead);
                                            text.setText("Please enter the town.");
                                            textHead.setText("Alert");
                                            Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                            dialogButton.setText("Ok");
                                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    pager.setCurrentItem(0);
                                                }
                                            });

                                            dialog.show();
                                        }

                                    } else {
                                        final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.setCancelable(false);
                                        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                        ImageView icon = dialog.findViewById(R.id.iconImageView);
                                        icon.setBackgroundResource(R.drawable.round);
                                        icon.setImageResource(R.drawable.exclamationicon);
                                        TextView text = dialog.findViewById(R.id.text_dialog);
                                        TextView textHead = dialog.findViewById(R.id.alertHead);
                                        text.setText("Please enter the address.");
                                        textHead.setText("Alert");
                                        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                        dialogButton.setText("Ok");
                                        dialogButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                pager.setCurrentItem(0);
                                            }
                                        });

                                        dialog.show();
                                    }

                                } else {
                                    final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                    ImageView icon = dialog.findViewById(R.id.iconImageView);
                                    icon.setBackgroundResource(R.drawable.round);
                                    icon.setImageResource(R.drawable.exclamationicon);
                                    TextView text = dialog.findViewById(R.id.text_dialog);
                                    TextView textHead = dialog.findViewById(R.id.alertHead);
                                    text.setText("Please enter the contact number.");
                                    textHead.setText("Alert");
                                    Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                    dialogButton.setText("Ok");
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            pager.setCurrentItem(0);
                                        }
                                    });

                                    dialog.show();
                                }
                            } else {
                                final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                ImageView icon = dialog.findViewById(R.id.iconImageView);
                                icon.setBackgroundResource(R.drawable.round);
                                icon.setImageResource(R.drawable.exclamationicon);
                                TextView text = dialog.findViewById(R.id.text_dialog);
                                TextView textHead = dialog.findViewById(R.id.alertHead);
                                text.setText("Please enter valid email");
                                textHead.setText("Alert");
                                Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                dialogButton.setText("Ok");
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        pager.setCurrentItem(0);
                                    }
                                });

                                dialog.show();
                            }
                        } else {
                            final Dialog dialog = new Dialog(DataCollectionActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                            ImageView icon = dialog.findViewById(R.id.iconImageView);
                            icon.setBackgroundResource(R.drawable.round);
                            icon.setImageResource(R.drawable.exclamationicon);
                            TextView text = dialog.findViewById(R.id.text_dialog);
                            TextView textHead = dialog.findViewById(R.id.alertHead);
                            text.setText("Please enter the email");
                            textHead.setText("Alert");
                            Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                            dialogButton.setText("Ok");
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    pager.setCurrentItem(0);
                                }
                            });

                            dialog.show();
                        }
                    }
                }
                {
                    if (previousPage != position) {
                        previousPage = position;
                        Log.e("onPageSelected: ", "kins");
                    }
                }
                Log.e("onPageSelected: ", String.valueOf(position));


                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<KinDetails.KinModel> kinarray = getKinArrayList("kinmodel");
                        ArrayList<EmergencyDetails.EmergencyModel> emergencyarray = getEmergencyArrayList("emergencymodel");
                        if (emergencyarray == null) {
                            final Dialog dialog = new Dialog(DataCollectionActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                            ImageView icon = dialog.findViewById(R.id.iconImageView);
                            icon.setBackgroundResource(R.drawable.round);
                            icon.setImageResource(R.drawable.exclamationicon);
                            TextView text = dialog.findViewById(R.id.text_dialog);
                            TextView textHead = dialog.findViewById(R.id.alertHead);
                            text.setText("Please add a contact.");
                            textHead.setText("Alert");
                            Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                            dialogButton.setText("Ok");
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    pager.setCurrentItem(2);
                                }
                            });

                            dialog.show();
                        } else {
                            Log.e("kindetails: ", String.valueOf(kinarray));

                            for (int i = 0; i < kinarray.size(); i++) {
                                if ((kinarray.get(i).getRequest().equals("0") && kinarray.get(i).getStatus().equals("1")) || (kinarray.get(i).getRequest().equals("0") && kinarray.get(i).getStatus().equals("2")) ||
                                        (kinarray.get(i).getRequest().equals("1") && kinarray.get(i).getStatus().equals("0"))) {
                                    OverallValue = "2";
                                    Log.e("overall Status: ", OverallValue);
                                }
                                else
                                {
                                    OverallValue = "5";
                                    Log.e("overall Status: ", OverallValue);
                                }
                            }
                            if (!OverallValue.equals("2")) {
                                for (int j = 0; j < emergencyarray.size(); j++) {
                                    if ((emergencyarray.get(j).getRequest().equals("0") && emergencyarray.get(j).getStatus().equals("1")) || (emergencyarray.get(j).getRequest().equals("0") && emergencyarray.get(j).getStatus().equals("2")) ||
                                            (emergencyarray.get(j).getRequest().equals("1") && emergencyarray.get(j).getStatus().equals("0"))) {
                                        OverallValue = "2";
                                        Log.e("overall Status: ", OverallValue);
                                    }
                                    else {
                                        OverallValue = "5";
                                        Log.e("overall Status: ", OverallValue);
                                    }
                                }
                            }

                            boolean isFieldEmpty = false, isEmailPattern = false;
                            if (prefs.getString("own_email", "").equals("")) {
                                final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                ImageView icon = dialog.findViewById(R.id.iconImageView);
                                icon.setBackgroundResource(R.drawable.round);
                                icon.setImageResource(R.drawable.exclamationicon);
                                TextView text = dialog.findViewById(R.id.text_dialog);
                                TextView textHead = dialog.findViewById(R.id.alertHead);
                                text.setText("Please enter email.");
                                textHead.setText("Alert");
                                Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                dialogButton.setText("Ok");
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        pager.setCurrentItem(0);
                                    }
                                });

                                dialog.show();

                            } else {
                                if (!prefs.getString("own_email", "").matches(emailPattern)) {

                                    final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                    ImageView icon = dialog.findViewById(R.id.iconImageView);
                                    icon.setBackgroundResource(R.drawable.round);
                                    icon.setImageResource(R.drawable.exclamationicon);
                                    TextView text = dialog.findViewById(R.id.text_dialog);
                                    TextView textHead = dialog.findViewById(R.id.alertHead);
                                    text.setText("Please enter valid email.");
                                    textHead.setText("Alert");
                                    Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                    dialogButton.setText("Ok");
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            pager.setCurrentItem(0);
                                        }
                                    });

                                    dialog.show();

                                } else {
                                    if (prefs.getString("own_user_mobile", "").equals("")) {
                                        Log.e("onClick: ", prefs.getString("own_user_mobile", ""));
                                        Log.e("onClick: ", "working");
                                        final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.setCancelable(false);
                                        dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                        ImageView icon = dialog.findViewById(R.id.iconImageView);
                                        icon.setBackgroundResource(R.drawable.round);
                                        icon.setImageResource(R.drawable.exclamationicon);
                                        TextView text = dialog.findViewById(R.id.text_dialog);
                                        TextView textHead = dialog.findViewById(R.id.alertHead);
                                        text.setText("Please enter contact number.");
                                        textHead.setText("Alert");
                                        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                        dialogButton.setText("Ok");
                                        dialogButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                pager.setCurrentItem(0);
                                            }
                                        });

                                        dialog.show();

                                    } else {
                                        ArrayList<EmergencyDetails.EmergencyModel> emergencyarray1 = getEmergencyArrayList("emergencymodel");
                                        for (int i = 0; i < emergencyarray1.size(); i++) {
                                            if (!emergencyarray1.get(i).getDeleted().equals("1")) {
                                                if (emergencyarray1.get(i).getName().equals("") ||
                                                        emergencyarray1.get(i).getUser_mobile().equals("")) {

                                                    isFieldEmpty = true;
                                                } else {
                                                    isFieldEmpty = false;
                                                }
                                          /*  if (!emergencyarray.get(i).getEmail().equals("")) {
                                                if (!emergencyarray.get(i).getEmail().matches(emailPattern)) {
                                                    isEmailPattern = true;
                                                } else {
                                                    isEmailPattern = false;
                                                }
                                            }*/
                                            }
                                        }

                                        if (isFieldEmpty) {

                                            final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.setCancelable(false);
                                            dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                            ImageView icon = dialog.findViewById(R.id.iconImageView);
                                            icon.setBackgroundResource(R.drawable.round);
                                            icon.setImageResource(R.drawable.exclamationicon);
                                            TextView text = dialog.findViewById(R.id.text_dialog);
                                            TextView textHead = dialog.findViewById(R.id.alertHead);
                                            text.setText("Please fill all the fields marked in red color.");
                                            textHead.setText("Alert");
                                            Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                            dialogButton.setText("Ok");
                                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    SharedPreferences.Editor editor = getSharedPreferences("BSKL", MODE_PRIVATE).edit();
                                                    editor.putString("validationlocal", "1");
                                                    editor.apply();
                                                    dialog.dismiss();
                                                    pager.setCurrentItem(2);
                                                }
                                            });

                                            dialog.show();

                                        } else {
                                            if (isEmailPattern) {


                                                final Dialog dialog = new Dialog(DataCollectionActivity.this);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                dialog.setCancelable(false);
                                                dialog.setContentView(R.layout.alert_dialogue_ok_layout_data_collection);
                                                ImageView icon = dialog.findViewById(R.id.iconImageView);
                                                icon.setBackgroundResource(R.drawable.round);
                                                icon.setImageResource(R.drawable.exclamationicon);
                                                TextView text = dialog.findViewById(R.id.text_dialog);
                                                TextView textHead = dialog.findViewById(R.id.alertHead);
                                                text.setText("Please enter valid email.");
                                                textHead.setText("Alert");
                                                Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                                dialogButton.setText("Ok");
                                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Log.e("swipe: ", "last");
                                                        dialog.dismiss();
                                                        pager.setCurrentItem(2);
                                                    }
                                                });

                                                dialog.show();
                                            } else {
                                                apiCall();

                                            }

                                        }
                                    }
                                }


                            }
                            //

                        }
                    }
                });

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //    Log.e("onPageScrolled: ", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //       Log.e("onPageState: ", String.valueOf(state));
            }
        });

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

    public void apiCall() {
        String own_user_id, own_email, own_code, own_user_mobile, own_phone, own_addressline1, own_addressline2, own_addressline3, own_town,
                own_state, own_country, own_pincode, own_status, own_name, own_relationship, own_student_id;
        SharedPreferences prefs = getSharedPreferences("BSKL", MODE_PRIVATE);
        String data = prefs.getString("DATA_COLLECTION", null);
        try {
            //DATA from JSON
            JSONObject respObj = new JSONObject(data);

            respObj = respObj.getJSONArray("own_details").getJSONObject(0);

            //===========================OWN DETAILS=====================================

            if (prefs.getString("own_user_id", "").equals("")) {
                own_user_id = respObj.optString("user_id");
            } else {
                own_user_id = prefs.getString("own_user_id", "");
            }

            if (prefs.getString("own_email", "").equals("")) {
                own_email = respObj.optString("email");
            } else {
                own_email = prefs.getString("own_email", "");
            }

            if (prefs.getString("own_code", "").equals("")) {
                own_code = respObj.optString("code");
            } else {
                own_code = prefs.getString("own_code", "");
            }

            if (prefs.getString("own_user_mobile", "").equals("")) {
                own_user_mobile = respObj.optString("user_mobile");
            } else {
                own_user_mobile = prefs.getString("own_user_mobile", "");
            }

            if (prefs.getString("own_phone", "").equals("")) {
                own_phone = respObj.optString("phone");
            } else {
                own_phone = prefs.getString("own_phone", "");
                own_phone = own_phone.replace("+", "");
            }

            if (prefs.getString("own_addressline1", "").equals("")) {
                own_addressline1 = respObj.optString("address1");
            } else {
                own_addressline1 = prefs.getString("own_addressline1", "");
            }

            if (prefs.getString("own_addressline2", "").equals("")) {
                own_addressline2 = respObj.optString("address2");
            } else {
                own_addressline2 = prefs.getString("own_addressline2", "");
            }

            if (prefs.getString("own_addressline3", "").equals("")) {
                own_addressline3 = respObj.optString("address3");
            } else {
                own_addressline3 = prefs.getString("own_addressline3", "");
            }

            if (prefs.getString("own_town", "").equals("")) {
                own_town = respObj.optString("town");
            } else {
                own_town = prefs.getString("own_town", "");
            }

            if (prefs.getString("own_state", "").equals("")) {
                own_state = respObj.optString("state");
            } else {
                own_state = prefs.getString("own_state", "");
            }

            if (prefs.getString("own_country", "").equals("")) {
                own_country = respObj.optString("country");
            } else {
                own_country = prefs.getString("own_country", "");
            }

            if (prefs.getString("own_pincode", "").equals("")) {
                own_pincode = respObj.optString("pincode");
            } else {
                own_pincode = prefs.getString("own_pincode", "");
            }

            own_status = prefs.getString("own_status", "");
            own_name = respObj.optString("name");
            own_relationship = respObj.optString("relationship");
            own_student_id = respObj.optString("student_id");

            //========================Kin Details========================================================
            // respObj = new JSONObject(data);
            // respObj = respObj.getJSONArray("kin_details").getJSONObject(0);
            // Gson gson = new Gson();
            // String json = prefs.getString("kinmodel", null);
            //    Log.e("kinmodel: ", String.valueOf(json));
            ArrayList<KinDetails.KinModel> kinarray = getKinArrayList("kinmodel");
            ArrayList<EmergencyDetails.EmergencyModel> emergencyarray = getEmergencyArrayList("emergencymodel");


            //   Type type = new TypeToken<ArrayList<String>>() {}.getType();
            //    ArrayList<KinDetails.KinModel> array = gson.fromJson(json, type);
            //=================================JsonArray=================================================
            JSONObject InnerData = new JSONObject();
            JSONObject Data = new JSONObject();
            JSONArray own_details_array = new JSONArray();
            JSONObject own_detailsObject = new JSONObject();
            own_detailsObject.put("id", "");
            own_detailsObject.put("user_id", own_user_id);
            own_detailsObject.put("name", own_name);
            own_detailsObject.put("relationship", own_relationship);
            own_detailsObject.put("email", own_email);
            own_detailsObject.put("phone", own_phone);
            own_detailsObject.put("code", own_code);
            own_detailsObject.put("user_mobile", own_user_mobile);
            own_detailsObject.put("student_id", own_student_id);
            own_detailsObject.put("address1", own_addressline1);
            own_detailsObject.put("address2", own_addressline2);
            own_detailsObject.put("address3", own_addressline3);
            own_detailsObject.put("town", own_town);
            own_detailsObject.put("state", own_state);
            own_detailsObject.put("country", own_country);
            own_detailsObject.put("pincode", own_pincode);
            own_detailsObject.put("status", own_status);
            own_detailsObject.put("created_at", "");
            own_detailsObject.put("updated_at", "");
            own_details_array.put(own_detailsObject);
            Data.put("own_details", own_details_array);


            JSONArray kin_details_array = new JSONArray();
            JSONObject jGroup = null;
            for (int i = 0; i < kinarray.size(); i++) {
                jGroup = new JSONObject();// /sub Object
                jGroup.put("id", kinarray.get(i).getId());
                jGroup.put("user_id", kinarray.get(i).getUser_id());
                jGroup.put("kin_id", kinarray.get(i).getKin_id());
                jGroup.put("name", kinarray.get(i).getName());
                jGroup.put("email", kinarray.get(i).getEmail());
                jGroup.put("phone", kinarray.get(i).getPhone());
                jGroup.put("code", kinarray.get(i).getCode());
                jGroup.put("user_mobile", kinarray.get(i).getUser_mobile());
                jGroup.put("student_id", kinarray.get(i).getStudent_id());
                jGroup.put("status", kinarray.get(i).getStatus());
                jGroup.put("request", kinarray.get(i).getRequest());
                jGroup.put("created_at", "");
                jGroup.put("updated_at", "");
                kin_details_array.put(jGroup);
            }
            Data.put("kin_details", kin_details_array);

            JSONArray emergency_details_array = new JSONArray();
            JSONObject jGroup1 = null;
            for (int i = 0; i < emergencyarray.size(); i++) {
                jGroup1 = new JSONObject();// /sub Object
                jGroup1.put("id", emergencyarray.get(i).getId());
                jGroup1.put("user_id", emergencyarray.get(i).getUser_id());
                jGroup1.put("local_emergency_id", emergencyarray.get(i).getEmergency_id());
                jGroup1.put("name", emergencyarray.get(i).getName());
                jGroup1.put("email", emergencyarray.get(i).getEmail());
                jGroup1.put("phone", emergencyarray.get(i).getPhone());
                jGroup1.put("code", emergencyarray.get(i).getCode());
                jGroup1.put("user_mobile", emergencyarray.get(i).getUser_mobile());
                //    jGroup1.put("student_id", emergencyarray.get(i).getStudent_id());
                jGroup1.put("status", emergencyarray.get(i).getStatus());
                jGroup1.put("request", emergencyarray.get(i).getRequest());
                jGroup1.put("created_at", "");
                jGroup1.put("updated_at", "");
                emergency_details_array.put(jGroup1);
            }
            Data.put("local_emergency_details", emergency_details_array);
            InnerData.put("data", Data);
            JSONObject finalArray = new JSONObject();
            // System.out.println("access_token getting " + PreferenceManager.getAccessToken(mContext));
            finalArray.put("access_token", PreferenceManager.getAccessToken(mContext));
            finalArray.put("data", InnerData);
            finalArray.put("user_ids", PreferenceManager.getUserId(mContext));
            finalArray.put("overall_status", "2");
            String JsonData = finalArray.toString();
            Log.e("finalarray: ", JsonData);
            //========================

            VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_SUBMIT_DATA_COLLECTION_NEW);

            String[] name = {"access_token", "data", "user_ids", "overall_status"};
            String[] value = {PreferenceManager.getAccessToken(DataCollectionActivity.this), String.valueOf(InnerData), PreferenceManager.getUserId(DataCollectionActivity.this), OverallValue};
            volleyWrapper.getResponsePOST(DataCollectionActivity.this, 11, name, value, new VolleyWrapper.ResponseListener() {
                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess:", successResponse);
                    try {
                        JSONObject obj = new JSONObject(successResponse);
                        String response_code = obj.getString(JTAG_RESPONSECODE);
                        if (response_code.equalsIgnoreCase("200")) {
                            JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                            String status_code = secobj.getString(JTAG_STATUSCODE);
                            if (status_code.equalsIgnoreCase("303")) {

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
                                text.setText("You have successfully submitted the details");
                                textHead.setText("Success");
                                Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                                dialogButton.setText("Ok");
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                                        editor.putString("data_collection_flag", "1");
                                        editor.putString("kinmodel", " ");
                                        editor.putString("emergencymodel", " ");
                                        editor.apply();
                                        dialog.dismiss();
                                        DataCollectionActivity.this.finish();
                                    }
                                });

                                dialog.show();
                            } else if (status_code.equalsIgnoreCase("98")) {
                                // System.out.println("98 working ");
                                showDialogCloseButton((Activity) mContext, "Alert", closeMsgKin, R.drawable.exclamationicon, R.drawable.round, "1");
                            } else if (status_code.equalsIgnoreCase("99")) {
                                // System.out.println("99 working ");
                                showDialogCloseButton((Activity) mContext, "Alert", closeMsgLocal, R.drawable.exclamationicon, R.drawable.round, "2");
                            }
                        } else if (response_code.equalsIgnoreCase("500")) {
                        } else if (response_code.equalsIgnoreCase("400")) {
                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                @Override
                                public void getAccessToken() {
                                }
                            });
                            //       showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);

                        } else if (response_code.equalsIgnoreCase("401")) {
                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                @Override
                                public void getAccessToken() {
                                }
                            });
                            //      showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                        } else if (response_code.equalsIgnoreCase("402")) {
                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                @Override
                                public void getAccessToken() {
                                }
                            });
                            //      showSettingUserDetail(URL_GET_SETTINGS_USER_DETAIL);
                        } else {
                            CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                                    , getResources().getString(R.string.ok));
                            dialog.show();
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                        }
                    } catch (Exception ex) {
                        // System.out.println("The Exception in edit profile is" + ex.toString());
                    }

                }

                @Override
                public void responseFailure(String failureResponse) {
                    Log.e("responseFailure: ", failureResponse);
                    CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                            , getResources().getString(R.string.ok));
                    dialog.show();

                }
            });
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


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
        switch (position) {
            case 0:
                radioGroup.check(R.id.radioButton1);
                nextBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.INVISIBLE);
                break;
            case 1:
                radioGroup.check(R.id.radioButton2);
                nextBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.INVISIBLE);
                break;
            case 2:
                radioGroup.check(R.id.radioButton3);
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
                SharedPreferences.Editor editor = mContext.getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                editor.putString("data_collection_flag", "1");
                editor.apply();
                dialog.dismiss();
                DataCollectionActivity.this.finish();


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
            case R.id.radioButton3:
                pager.setCurrentItem(2);
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
            switch (position) {
                case 0:
                    return new FirstScreen();
                case 1:
                    return new KinDetails();
                case 2:
                    return new EmergencyDetails();
                default:
                    return new FirstScreen();
            }
        }

        /**
         * Return the number of pages.
         *
         * @return
         */
        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }


    }
}