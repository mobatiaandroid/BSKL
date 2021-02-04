package com.mobatia.bskl.activity.data_collection;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Owndetails extends Fragment {
    String closeMsg;

    public Owndetails() {
        // Required empty public constructor
    }

    private EditText emailOwnDetails, contactNoOwnDetails, addressLine1, addressLine2, addressLine3, townTxt, stateTxt, countryTxt, pinTxt;
    private TextView heading;
    private ImageView closeImg;
    private CountryCodePicker spinnerCode;
    private ScrollView Scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_owndetails, container, false);
        closeImg = v.findViewById(R.id.closeImg);
        heading = v.findViewById(R.id.dataCollectionDesc);
        emailOwnDetails = v.findViewById(R.id.emailOwnDetails);
        spinnerCode = v.findViewById(R.id.spinnerCode);
        contactNoOwnDetails = v.findViewById(R.id.contactNoOwnDetails);
        addressLine1 = v.findViewById(R.id.addressLine1);
        addressLine2 = v.findViewById(R.id.addressLine2);
        addressLine3 = v.findViewById(R.id.addressLine3);
        townTxt = v.findViewById(R.id.townTxt);
        stateTxt = v.findViewById(R.id.stateTxt);
        countryTxt = v.findViewById(R.id.countryTxt);
        pinTxt = v.findViewById(R.id.pinTxt);
        Scroll = v.findViewById(R.id.scroll1);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        String EmailgrayPart = "Email";
        String EmailredPart = "*";
        SpannableStringBuilder Emailbuilder = new SpannableStringBuilder();
        SpannableString EmailredColoredString = new SpannableString(EmailgrayPart);
        EmailredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, EmailgrayPart.length(), 0);
        Emailbuilder.append(EmailredColoredString);
        SpannableString EmailblueColoredString = new SpannableString(EmailredPart);
        EmailblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, EmailredPart.length(), 0);
        Emailbuilder.append(EmailblueColoredString);
        emailOwnDetails.setHint(Emailbuilder);

        String ContactgrayPart = "Contact Number";
        String ContactredPart = "*";
        SpannableStringBuilder Contactbuilder = new SpannableStringBuilder();
        SpannableString ContactredColoredString = new SpannableString(ContactgrayPart);
        ContactredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, ContactgrayPart.length(), 0);
        Contactbuilder.append(ContactredColoredString);
        SpannableString ContactblueColoredString = new SpannableString(ContactredPart);
        ContactblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, ContactredPart.length(), 0);
        Contactbuilder.append(ContactblueColoredString);
        contactNoOwnDetails.setHint(Contactbuilder);

        String Address1grayPart = "Address Line1";
        String Address1redPart = "*";
        SpannableStringBuilder Address1builder = new SpannableStringBuilder();
        SpannableString Address1redColoredString = new SpannableString(Address1grayPart);
        Address1redColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, Address1grayPart.length(), 0);
        Address1builder.append(Address1redColoredString);
        SpannableString Address1blueColoredString = new SpannableString(Address1redPart);
        Address1blueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, Address1redPart.length(), 0);
        Address1builder.append(Address1blueColoredString);
        addressLine1.setHint(Address1builder);

        String TowngrayPart = "Town";
        String TownredPart = "*";
        SpannableStringBuilder Townbuilder = new SpannableStringBuilder();
        SpannableString TownredColoredString = new SpannableString(TowngrayPart);
        TownredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, TowngrayPart.length(), 0);
        Townbuilder.append(TownredColoredString);
        SpannableString TownblueColoredString = new SpannableString(TownredPart);
        TownblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, TownredPart.length(), 0);
        Townbuilder.append(TownblueColoredString);
        townTxt.setHint(Townbuilder);

        String StategrayPart = "State";
        String StateredPart = "*";
        SpannableStringBuilder Statebuilder = new SpannableStringBuilder();
        SpannableString StateredColoredString = new SpannableString(StategrayPart);
        StateredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, StategrayPart.length(), 0);
        Statebuilder.append(StateredColoredString);
        SpannableString StateblueColoredString = new SpannableString(StateredPart);
        StateblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, StateredPart.length(), 0);
        Statebuilder.append(StateblueColoredString);
        stateTxt.setHint(Statebuilder);

        String CountrygrayPart = "Country";
        String CountryredPart = "*";
        SpannableStringBuilder Countrybuilder = new SpannableStringBuilder();
        SpannableString CountryredColoredString = new SpannableString(CountrygrayPart);
        CountryredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, CountrygrayPart.length(), 0);
        Countrybuilder.append(CountryredColoredString);
        SpannableString CountryblueColoredString = new SpannableString(CountryredPart);
        CountryblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, CountryredPart.length(), 0);
        Countrybuilder.append(CountryblueColoredString);
        countryTxt.setHint(Countrybuilder);

        String PostgrayPart = "Post Code";
        String PostredPart = "*";
        SpannableStringBuilder Postbuilder = new SpannableStringBuilder();
        SpannableString PostredColoredString = new SpannableString(PostgrayPart);
        PostredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, PostgrayPart.length(), 0);
        Postbuilder.append(PostredColoredString);
        SpannableString PostblueColoredString = new SpannableString(PostredPart);
        PostblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, PostredPart.length(), 0);
        Postbuilder.append(PostblueColoredString);
        pinTxt.setHint(Postbuilder);

        SharedPreferences prefs = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE);
        String data = prefs.getString("DATA_COLLECTION", null);

        try {
            JSONObject respObj = new JSONObject(data);
            heading.setText(respObj.getString("display_message"));
            respObj = respObj.getJSONArray("own_details").getJSONObject(0);
            emailOwnDetails.setText(respObj.optString("email"));
            SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
            editor.putString("own_email", respObj.optString("email"));
            emailOwnDetails.setSelection(emailOwnDetails.getText().length());
            try {
                spinnerCode.setCountryForPhoneCode(Integer.parseInt(respObj.optString("code")));
            } catch (NumberFormatException ex) { // handle your exception
                Log.e("spinner: ", ex.getMessage());
            }
            contactNoOwnDetails.setText(respObj.optString("user_mobile"));
            contactNoOwnDetails.setSelection(contactNoOwnDetails.getText().length());
            editor.putString("own_user_mobile", respObj.optString("user_mobile"));
            editor.putString("own_phone", respObj.optString("code") + respObj.optString("user_mobile"));
            addressLine1.setText(respObj.optString("address1"));
            addressLine1.setSelection(addressLine1.getText().length());
            editor.putString("own_addressline1", respObj.optString("address1"));
            addressLine2.setText(respObj.optString("address2"));
            editor.putString("own_addressline2", respObj.optString("address2"));
            addressLine2.setSelection(addressLine2.getText().length());
            addressLine3.setText(respObj.optString("address3"));
            editor.putString("own_addressline3", respObj.optString("address3"));
            addressLine3.setSelection(addressLine3.getText().length());
            townTxt.setText(respObj.optString("town"));
            editor.putString("own_town", respObj.optString("town"));
            townTxt.setSelection(townTxt.getText().length());
            stateTxt.setText(respObj.optString("state"));
            editor.putString("own_state", respObj.optString("state"));
            stateTxt.setSelection(stateTxt.getText().length());
            countryTxt.setText(respObj.optString("country"));
            editor.putString("own_country", respObj.optString("country"));
            countryTxt.setSelection(countryTxt.getText().length());
            pinTxt.setText(respObj.optString("pincode"));
            editor.putString("own_pincode", respObj.optString("pincode"));
            pinTxt.setSelection(pinTxt.getText().length());
            editor.putString("own_user_id", respObj.optString("user_id"));
            editor.putString("own_status", "5");
            editor.apply();

          /*  if (emailOwnDetails.getText().toString().equals("")) {
                emailOwnDetails.setBackgroundResource(R.drawable.rec_red);
            } else {
                emailOwnDetails.setBackgroundResource(R.drawable.edit_text_login);
            }
            if (contactNoOwnDetails.getText().toString().equals("")) {
                contactNoOwnDetails.setBackgroundResource(R.drawable.rec_red);
            } else {
                contactNoOwnDetails.setBackgroundResource(R.drawable.edit_text_login);
            }
            if (addressLine1.getText().toString().equals("")) {
                addressLine1.setBackgroundResource(R.drawable.rec_red);
            } else {
                addressLine1.setBackgroundResource(R.drawable.edit_text_login);
            }
            if (townTxt.getText().toString().equals("")) {
                townTxt.setBackgroundResource(R.drawable.rec_red);
            } else {
                townTxt.setBackgroundResource(R.drawable.edit_text_login);
            }
            if (countryTxt.getText().toString().equals("")) {
                countryTxt.setBackgroundResource(R.drawable.rec_red);
            } else {
                countryTxt.setBackgroundResource(R.drawable.edit_text_login);
            }
            if (pinTxt.getText().toString().equals("")) {
                pinTxt.setBackgroundResource(R.drawable.rec_red);
            } else {
                pinTxt.setBackgroundResource(R.drawable.edit_text_login);
            }
*/
            emailOwnDetails.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //   String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (emailOwnDetails.getText().toString().equals("")) {
                     //   emailOwnDetails.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_email", emailOwnDetails.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                        //  showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
                    } else {
                   //     emailOwnDetails.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_email", emailOwnDetails.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    }
                    // TODO Auto-generated method stub
                }
            });

            spinnerCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                @Override
                public void onCountrySelected() {
                    String code = spinnerCode.getSelectedCountryCodeWithPlus();
                    Log.e("onCountrySelected: ", code);
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                    editor.putString("own_code", code);
                    editor.putString("own_phone", String.valueOf(spinnerCode.getSelectedCountryCodeAsInt()) + contactNoOwnDetails.getText().toString());
                    editor.putString("own_status", "1");
                    editor.apply();

                }
            });
            contactNoOwnDetails.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (contactNoOwnDetails.getText().toString().equals("")) {
                 //       contactNoOwnDetails.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_user_mobile", contactNoOwnDetails.getText().toString());
                        editor.putString("own_phone", String.valueOf(spinnerCode.getSelectedCountryCodeAsInt()) + contactNoOwnDetails.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                        // showAlertButton(getActivity(), "Alert", "Please enter Contact number.", R.drawable.exclamationicon, R.drawable.round);
                    } else {
                 //       contactNoOwnDetails.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_user_mobile", contactNoOwnDetails.getText().toString());
                        editor.putString("own_phone", String.valueOf(spinnerCode.getSelectedCountryCodeAsInt()) + contactNoOwnDetails.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    }
                    // TODO Auto-generated method stub
                }
            });
            addressLine1.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (addressLine1.getText().toString().equals("")) {
                  //      addressLine1.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_addressline1", String.valueOf(s));
                        editor.putString("own_status", "1");
                        editor.apply();
                        // showAlertButton(getActivity(), "Alert", "Please enter Address Line 1.", R.drawable.exclamationicon, R.drawable.round);
                    } else {
                 //       addressLine1.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_addressline1", String.valueOf(s));
                        editor.putString("own_status", "1");
                        editor.apply();
                    }

                    // TODO Auto-generated method stub
                }
            });
            addressLine2.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!text.equals(String.valueOf(s))) {
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_addressline2", String.valueOf(s));
                        editor.putString("own_status", "1");
                        editor.apply();
                    }
                    // TODO Auto-generated method stub
                }
            });
            addressLine3.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!text.equals(String.valueOf(s))) {
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_addressline3", String.valueOf(s));
                        editor.putString("own_status", "1");
                        editor.apply();
                    }
                    // TODO Auto-generated method stub
                }
            });
            townTxt.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (townTxt.getText().toString().equals("")) {
                //        townTxt.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_town", townTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    } else {
                //        townTxt.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_town", townTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    }
                    // TODO Auto-generated method stub
                }
            });
            stateTxt.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (stateTxt.getText().toString().equals("")) {
                //        stateTxt.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_state", stateTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    } else {
                //        stateTxt.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_state", stateTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    }

                    // TODO Auto-generated method stub
                }
            });
            countryTxt.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (countryTxt.getText().toString().equals("")) {
               //         countryTxt.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_country", countryTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    } else {
             //           countryTxt.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_country", countryTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    }

                    // TODO Auto-generated method stub
                }
            });
            pinTxt.addTextChangedListener(new TextWatcher() {
                String text;

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    text = String.valueOf(s);
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (pinTxt.getText().toString().equals("")) {
              //          pinTxt.setBackgroundResource(R.drawable.rec_red);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_pincode", pinTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    } else {
              //          pinTxt.setBackgroundResource(R.drawable.edit_text_login);
                        SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                        editor.putString("own_pincode", pinTxt.getText().toString());
                        editor.putString("own_status", "1");
                        editor.apply();
                    }

                    // TODO Auto-generated method stub
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        closeMsg = "Please update this information next time";
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                editor.putString("data_collection_flag", "1");
                editor.apply();
                showDialogCloseButton(getActivity(), "Alert", closeMsg, R.drawable.exclamationicon, R.drawable.round);
            }
        });

        return v;
    }

    public void showDialogCloseButton(final Activity activity, String msg, String msgHead, int ico, int bgIcon) {
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
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferences.Editor editor = getContext().getSharedPreferences("BSKL", Context.MODE_PRIVATE).edit();
                editor.putString("data_collection_flag", "1");
                editor.apply();
                getActivity().finish();

            }
        });

        dialog.show();

    }

    public void showAlertButton(final Activity activity, String msg, String msgHead, int ico, int bgIcon) {
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
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });

        dialog.show();

    }
}
