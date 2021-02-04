package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.adapter.FamilyKinRecyclerAdapter;
import com.mobatia.bskl.activity.datacollection_p2.model.ContactTypeModel;
import com.mobatia.bskl.activity.datacollection_p2.model.GlobalListDataModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

public class FirstScreen extends Fragment {
    String ID,UserID,KinID,Relationship,Phone,StudentId,CreatedAt,UpdatedAt;
    Button confirmBtnMain;
    TextView messageTxt,nameOwnDetailTxt,contactTypeOwnDetailTxt,HelpView;
    Context mContext;
    String changedField="";
    String request_state = "";
    String NewChangeState = "5";
    String id="";
    String EmailExist;
    String NEW = "NO";
    String userId="";
    String studentId="";
    String updated_at="";
    String user_mobile="";
    String created_at;
    String EventStatus = "5";
    int Kin_Size;
    int KinClicked_Size = 0;
    int recyclerPosition;
    int ClickedRecyclerPosition;
    public static String IsValueEmpty = "";
    public static String WhoValueEmpty = "";
    boolean LALA = false;
    RelativeLayout ownDetailViewRelative;
//    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+";
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    ArrayList<OwnContactModel>mOwnContactArrayList;
    ArrayList<GlobalListDataModel> GlobalList;
    ArrayList<ContactTypeModel> ContactList;
    ArrayList<KinModel> KinArray = new ArrayList<>();
    LinearLayout NoDataLayout,RecyclerLayout;
    RecyclerView mRecyclerView;
    FamilyKinRecyclerAdapter familyKinRecyclerAdapter;
    EditText Fname;
    EditText Lname;
    EditText Email,Concatc_details_phone;
    ImageView NoContentPlusIcon,RecyclerPlusIcon,CloseIcon;
    ArrayList<KinModel>SubmitKinArray;
    ArrayList<String> GSONarray = new ArrayList<>();
    ArrayList<String> OwnDetailArray = new ArrayList<>();
    public FirstScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first_screen, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mContext=getActivity();
        confirmBtnMain=v.findViewById(R.id.confirmBtn);
        messageTxt=v.findViewById(R.id.messageTxt);
        nameOwnDetailTxt=v.findViewById(R.id.nameOwnDetailTxt);
        ownDetailViewRelative=v.findViewById(R.id.ownDetailViewRelative);
        contactTypeOwnDetailTxt=v.findViewById(R.id.contactTypeOwnDetailTxt);
        HelpView = v.findViewById(R.id.helpView);
        NoContentPlusIcon = v.findViewById(R.id.plusImgNoContent);
        RecyclerPlusIcon = v.findViewById(R.id.RecyclerPlusBtn);
        CloseIcon = v.findViewById(R.id.closeImg);
        NoDataLayout = v.findViewById(R.id.NoDataLinearLayout);
        RecyclerLayout = v.findViewById(R.id.RecyclerLinearLayout);
        mRecyclerView = v.findViewById(R.id.familyContactRecycler);

        HelpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHelpDialog(getActivity(), "Help", R.drawable.questionmark_icon, R.drawable.round);
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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        changedField = "3";
        mOwnContactArrayList=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);
        System.out.println("OwnDetail ArrayList"+mOwnContactArrayList.size());
        if (mOwnContactArrayList.size()>0)
        {
            if (!mOwnContactArrayList.get(0).getName().equalsIgnoreCase("") && !mOwnContactArrayList.get(0).getLast_name().equalsIgnoreCase(""))
            {
                nameOwnDetailTxt.setText(mOwnContactArrayList.get(0).getName()+" "+mOwnContactArrayList.get(0).getLast_name());
            }
            else if (!mOwnContactArrayList.get(0).getName().equalsIgnoreCase("") && mOwnContactArrayList.get(0).getLast_name().equalsIgnoreCase(""))
            {
                nameOwnDetailTxt.setText(mOwnContactArrayList.get(0).getName());
            }
            else
            {
                nameOwnDetailTxt.setText(mOwnContactArrayList.get(0).getName());
            }

            if (!mOwnContactArrayList.get(0).getRelationship().equalsIgnoreCase(""))
            {
                contactTypeOwnDetailTxt.setText(mOwnContactArrayList.get(0).getRelationship());
            }
            else
            {
                contactTypeOwnDetailTxt.setText("");
            }

            if (mOwnContactArrayList.get(0).isUpdated())
            {
                ownDetailViewRelative.setBackgroundResource(R.drawable.rect_background_grey);
                confirmBtnMain.setVisibility(View.GONE);
            }
            else
            {
                ownDetailViewRelative.setBackgroundResource(R.drawable.rect_data_collection_red);
                confirmBtnMain.setVisibility(View.VISIBLE);
            }
        }
        if (PreferenceManager.getDisplayMessage(mContext).equalsIgnoreCase(""))
        {
            messageTxt.setText("");
        }
        else
        {
            messageTxt.setText(PreferenceManager.getDisplayMessage(mContext));
        }
            try {
                KinArray.addAll(PreferenceManager.getKinDetailsArrayList(mContext));
            }catch (Exception e){

            }

        if (KinArray.size()>0)
        {
            RecyclerLayout.setVisibility(View.VISIBLE);
            NoDataLayout.setVisibility(View.GONE);
            for (int i = 0;i<KinArray.size();i++){
                System.out.println("A_DATA: "+KinArray.get(i).getName());
            }
            Kin_Size = KinArray.size()+1;
            familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,KinArray);
            mRecyclerView.setAdapter(familyKinRecyclerAdapter);

        }else {
            PreferenceManager.setIsValueEmpty(mContext,"1");
            PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
            NoDataLayout.setVisibility(View.VISIBLE);
            RecyclerLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i<KinArray.size();i++){
            if (KinArray.get(i).getEmail().isEmpty()||
                    KinArray.get(i).getTitle().isEmpty()||
                    KinArray.get(i).getName().isEmpty()||
                    KinArray.get(i).getLast_name().isEmpty()||
                    KinArray.get(i).getPhone().isEmpty()){

                System.out.println("CheckEmpty: Empty "+KinArray.get(i).getEmail());
                System.out.println("CheckEmpty: Empty for "+KinArray.get(i).getName());
                PreferenceManager.setIsValueEmpty(mContext,"1");
                String aWhoValueEmpty = KinArray.get(i).getName()+""+KinArray.get(i).getLast_name();
                PreferenceManager.setWhoValueEmpty(mContext,"Please fill all mandatory fields for "+aWhoValueEmpty);
                System.out.println("CheckEmpty: All: "+"Please fill all mandatory fields for "+aWhoValueEmpty);
                break;
            }

        }

        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClickItem(View v, int position) {
                        recyclerPosition = position;
                        NEW = "NO";
                        System.out.println("ARSDA :"+KinArray.get(position).getName());
                        String Fname = KinArray.get(position).getName();
                        String Lname = KinArray.get(position).getLast_name();
                        String Email = KinArray.get(position).getEmail();
//                        String Phone = KinArray.get(position).getPhone();
                        String Title = KinArray.get(position).getTitle();
                        String Relation = KinArray.get(position).getRelationship();
                        ID = KinArray.get(position).getId();
                        UserID = KinArray.get(position).getUser_id();
                        KinID = KinArray.get(position).getKin_id();
                        Relationship = KinArray.get(position).getRelationship();
                        StudentId = KinArray.get(position).getStudent_id();
                        CreatedAt = KinArray.get(position).getCreated_at();
                        UpdatedAt = KinArray.get(position).getUpdated_at();

                        Phone = KinArray.get(position).getPhone();
//                        KinArray.get(position).setClicked(true);
                        ClickedRecyclerPosition = position;
                        request_state = "0";
                        NewChangeState = "5";
//                        v.setBackgroundDrawable(getActivity().getDrawable(R.drawable.rect_background_grey));
                        //      v.setBackground(getActivity().getDrawable(R.drawable.rect_background_grey));
//                        for (int i = 0; i<KinArray.size();i++){
//                            if (KinArray.get(i).isClicked()) {
//                                KinArray.get(i).setClicked(true);
//                            }
//                        }
//                        KinArray.get(position).setClicked(true);
                        familyKinRecyclerAdapter.notifyDataSetChanged();
                        String pos=String.valueOf(position);
                        ShowDialog(Fname,Lname,Email,Phone,Title,pos,Relation);

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));

        RecyclerPlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedField = "4";
                request_state = "1";
                NewChangeState = "3";
                NEW = "YES";

                ShowDialog("","","","", "","", "");
            }
        });

        NoContentPlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedField = "4";
                request_state = "1";
                NewChangeState = "3";
                ShowDialog("","","","", "","", "");
            }
        });



        confirmBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmButtonDialogue(getActivity());

            }
        });
        ownDetailViewRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmButtonDialogue(getActivity());

            }
        });

        return v;
    }

    private void ShowHelpDialog(FragmentActivity activity, String help, int questionmark_icon, int round) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_help_layout);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(round);
        icon.setImageResource(questionmark_icon);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        textHead.setText(help);
        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void ShowDialog(final String fname, String lname, final String email, final String phone, final String title, final String pos, String relation)
    {
        ContactList = new ArrayList<>();
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_contact_details);
        dialog.show();

        Fname = dialog.findViewById(R.id.contactDetails_fname);
        Lname = dialog.findViewById(R.id.ContactDetails_Lastname);
        Email = dialog.findViewById(R.id.ContactDetails_Email);
        Concatc_details_phone = dialog.findViewById(R.id.ContactDetails_Phone);
        final ImageView Remove = dialog.findViewById(R.id.remove_kin);
        final RelativeLayout REL_ONE = dialog.findViewById(R.id.BBA);
        final RelativeLayout REL_TWO = dialog.findViewById(R.id.phoneNo);
        final AutoCompleteTextView ContactSp = dialog.findViewById(R.id.ContactDetails_Spinnertype);
        final AutoCompleteTextView RelationalSp = dialog.findViewById(R.id.relationshipSpinner);
        ContactSp.setHint("Mr");
        final CountryCodePicker countryCode = dialog.findViewById(R.id.spinnerCode);
//        final ImageView AddBtn = dialog.findViewById(R.id.contact_details_plus);
        final ImageView Close = dialog.findViewById(R.id.imageView4);
        final Button Submit = dialog.findViewById(R.id.ContactDetails_Submit);
        String fnameNograyPart = "First name";
        String fnameredPart = "*";
        SpannableStringBuilder fnameNobuilder = new SpannableStringBuilder();
        SpannableString fnameredColoredString = new SpannableString(fnameNograyPart);
        fnameredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, fnameNograyPart.length(), 0);
        fnameNobuilder.append(fnameredColoredString);
        SpannableString fnameblueColoredString = new SpannableString(fnameredPart);
        fnameblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, fnameredPart.length(), 0);
        fnameNobuilder.append(fnameblueColoredString);
        Fname.setHint(fnameNobuilder);
        String emailgrayPart = "E-Mail ID";
        String  emailredPart = "*";
        SpannableStringBuilder emailNobuilder = new SpannableStringBuilder();
        SpannableString emailredColoredString = new SpannableString(emailgrayPart);
        emailredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, emailgrayPart.length(), 0);
        emailNobuilder.append(emailredColoredString);
        SpannableString emailblueColoredString = new SpannableString(emailredPart);
        emailblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, emailredPart.length(), 0);
        emailNobuilder.append(emailblueColoredString);
        Email.setHint(emailNobuilder);
        String phonegrayPart = "Contact Number";
        String  phoneredPart = "*";
        SpannableStringBuilder phoneNobuilder = new SpannableStringBuilder();
        SpannableString phoneredColoredString = new SpannableString(phonegrayPart);
        phoneredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, phonegrayPart.length(), 0);
        phoneNobuilder.append(phoneredColoredString);
        SpannableString phoneblueColoredString = new SpannableString(phoneredPart);
        phoneblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, phoneredPart.length(), 0);
        phoneNobuilder.append(phoneblueColoredString);
        Concatc_details_phone.setHint(phoneNobuilder);

        String RelationNograyPart = "Relationship";
        String RelationredPart = "*";
        SpannableStringBuilder RelationNobuilder = new SpannableStringBuilder();
        SpannableString RelationColoredString = new SpannableString(RelationNograyPart);
        RelationColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, RelationNograyPart.length(), 0);
        RelationNobuilder.append(RelationColoredString);
        SpannableString RelationblueColoredString = new SpannableString(RelationredPart);
        RelationblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, RelationredPart.length(), 0);
        RelationNobuilder.append(RelationblueColoredString);
        RelationalSp.setHint(RelationNobuilder);


        Fname.setText(fname);
        Lname.setText(lname);
        Email.setText(email);
        ContactSp.setText(title);
        RelationalSp.setText(relation);
        Concatc_details_phone.setText(phone);
        changedField = "";

        if (NEW.equalsIgnoreCase("YES")){
            Remove.setVisibility(View.GONE);
        }

        GlobalList = new ArrayList<>();
        GlobalList.addAll(PreferenceManager.getGlobalListArrayList(mContext));
        ArrayList<String> SpinnerData = new ArrayList<>();
        for (int i =0; i <GlobalList.size();i++){

            for (int j =0; j<GlobalList.get(i).getmGlobalSirnameArray().size();j++){
                System.out.println("Global: "+ GlobalList.get(i).getmGlobalSirnameArray().get(j).getName());
                SpinnerData.add(GlobalList.get(i).getmGlobalSirnameArray().get(j).getName());
//                System.out.println("Global: TYPE: "+ GlobalList.get(i).getType().equalsIgnoreCase("Title"));
            }

        }
        final ArrayAdapter<String> DROP = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SpinnerData);
        ContactSp.setCursorVisible(false);
        ContactSp.setFocusable(false);
        ContactSp.setAdapter(DROP);
        ContactSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactSp.showDropDown();
            }
        });
        ContactList.addAll(PreferenceManager.getContactTypeArrayList(mContext));
        ArrayList<String> RelationalSpinnerData = new ArrayList<>();
        for (int i =0; i <ContactList.size();i++){
            for (int j =0; j<ContactList.get(i).getmGlobalSirnameArray().size();j++){
                System.out.println("Contact: "+ ContactList.get(i).getmGlobalSirnameArray().get(j).getName());
                RelationalSpinnerData.add(ContactList.get(i).getmGlobalSirnameArray().get(j).getName());
//                System.out.println("Global: Contact: "+ ContactList.get(i).getType().equalsIgnoreCase("Title"));
            }

        }

        final ArrayAdapter<String> relationalArray = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, RelationalSpinnerData);
        RelationalSp.setAdapter(relationalArray);
        RelationalSp.setFocusable(false);
        RelationalSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelationalSp.showDropDown();
            }
        });

        final  String firstName=Fname.getText().toString();
        final  String lastName=Lname.getText().toString();
        final  String mEmail=Email.getText().toString();
        final String Contactphone = Concatc_details_phone.getText().toString();
        final String Title = ContactSp.getText().toString();
        final String CountryCode = countryCode.getSelectedCountryCode();
        final String Relation = RelationalSp.getText().toString();
        EventStatus = "";



        ContactSp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged: ", "name Chaange");
                String changedName=ContactSp.getText().toString();
                System.out.println("Name test change"+changedName);
                System.out.println("Name test no change"+Title);
                if (Title.equalsIgnoreCase(changedName))
                {
                    changedField="";
                    EventStatus = "";
                    NewChangeState = "5";
                }
                else
                {
                    changedField="1";
//                    EventStatus = "1";
                    NewChangeState = "1";

                }
            }
        });

        RelationalSp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged: ", "name Chaange");
                String changedName=RelationalSp.getText().toString();
                System.out.println("Name test change"+changedName);
                System.out.println("Name test no change"+Relation);
                if (Relation.equalsIgnoreCase(changedName))
                {
                    changedField="";
                    EventStatus = "";
                    NewChangeState = "5";
                }
                else
                {
                    changedField="1";
//                    EventStatus = "1";
                    NewChangeState = "1";

                }
            }
        });



        Fname.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedName=Fname.getText().toString();
                System.out.println("Name test change"+changedName);
                System.out.println("Name test no change"+firstName);
                if (firstName.equalsIgnoreCase(changedName))
                {
                    changedField="";
                    EventStatus = "";
                }
                else
                {
                    changedField="1";
//                    EventStatus = "1";
                    NewChangeState = "1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
//        Lname.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.e("afterTextChanged: ", "name Chaange");
//                String changedNameLast=Lname.getText().toString();
//                System.out.println("Name test change"+changedNameLast);
//                System.out.println("Name test no change"+lastName);
//                if (lastName.equalsIgnoreCase(changedNameLast))
//                {
//                    changedField="";
//                    EventStatus = "";
//                }
//                else
//                {
//                    changedField="1";
////                    EventStatus = "1";
//                    NewChangeState = "1";
//                }
//                System.out.println("Name test no change value"+changedField);
//                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
//            }
//
//
//        });
        Email.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameEmail=Email.getText().toString();
                System.out.println("Name test change"+changedNameEmail);
                System.out.println("Name test no change"+mEmail);
                if (mEmail.equalsIgnoreCase(changedNameEmail))
                {
                    changedField="";
                    EventStatus = "";
                }
                else
                {
                    changedField="1";
//                    EventStatus = "1";
                    NewChangeState = "1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });

        Concatc_details_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String Changed_Contact=Email.getText().toString();
                System.out.println("Name test change"+Changed_Contact);
                System.out.println("Name test no change"+Contactphone);
                if (Contactphone.equalsIgnoreCase(Changed_Contact))
                {
                    changedField="";
                    EventStatus = "";
                }
                else
                {
                    changedField="1";
                    EventStatus = "1";
                    NewChangeState = "1";
                }
                System.out.println("Name test no change value"+changedField);
            }
        });





        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KinClicked_Size++;
                EventStatus = "2";
                changedField = "2";
                NewChangeState = "2";

                final Dialog Deletedialog = new Dialog(mContext);
                Deletedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Deletedialog.setCancelable(false);
                Deletedialog.setContentView(R.layout.dialogue_discard_data);
                ImageView icon = Deletedialog.findViewById(R.id.iconImageView);
                icon.setBackgroundResource(R.drawable.round);
                icon.setImageResource(R.drawable.questionmark_icon);
                TextView text = Deletedialog.findViewById(R.id.text_dialog);
                TextView textHead = Deletedialog.findViewById(R.id.alertHead);
                text.setText("Do you want to delete this details?");
                textHead.setText("Confirm?");
                Button dialogButton = Deletedialog.findViewById(R.id.btn_Ok);
                dialogButton.setText("Delete");
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Deletedialog.dismiss();

                        if (KinArray.size()>1){

                            familyKinRecyclerAdapter.notifyItemRemoved(recyclerPosition);
                            KinArray.remove(recyclerPosition);
                            SubmittingKinDetails(firstName,lastName,mEmail,Contactphone,Title,CountryCode);
                        }else {

                            ShowCannotDeleteDialog();
                        }

                    }
                });
                Button dialogButtonCancel = Deletedialog.findViewById(R.id.btn_Cancel);
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Deletedialog.dismiss();
                    }
                });
                Deletedialog.show();




            }
        });

//        AddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Lname.setVisibility(View.VISIBLE);
//                Email.setVisibility(View.VISIBLE);
//                REL_ONE.setVisibility(View.VISIBLE);
//                REL_TWO.setVisibility(View.VISIBLE);
//                AddBtn.setVisibility(View.GONE);
//                Remove.setVisibility(View.VISIBLE);
//            }
//        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(email.equalsIgnoreCase("")||firstName.equalsIgnoreCase("")||lastName.equalsIgnoreCase("")||phone.equalsIgnoreCase("")){
//                    KinArray.get(ClickedRecyclerPosition).setClicked(true);
//                    KinArray.get(ClickedRecyclerPosition).setFullFilled(true);
//                }else {
//                    KinArray.get(ClickedRecyclerPosition).setClicked(false);
//                    KinArray.get(ClickedRecyclerPosition).setFullFilled(true);
//                }


                if (changedField.equalsIgnoreCase("1")){
//                    System.out.println("RETURE: "+DetectChanges());
                    ShowDiscardDialog(mContext, "Confirm?", "Do you want to Discard changes?", R.drawable.questionmark_icon, R.drawable.round,dialog);
                }else {
                    if(email.equalsIgnoreCase("")||firstName.equalsIgnoreCase("")||lastName.equalsIgnoreCase("")||phone.equalsIgnoreCase(""))
                    {
                        if (NEW.equalsIgnoreCase("YES")){
                            Log.d("Problem","NOTHING");
                        }else {
//                            KinArray.get(Integer.valueOf(pos)).setFullFilled(false);
                        }

                    }
                    else
                    {
//                        KinArray.get(Integer.valueOf(pos)).setFullFilled(false);
                    }
                    familyKinRecyclerAdapter.notifyDataSetChanged();
                    dialog.dismiss();
//                    SubmittingKinDetails(firstName,lastName,mEmail,Contactphone,EventStatus);
                }

            }
        });



        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KinClicked_Size++;

                if (NEW.equalsIgnoreCase("YES")){
//                    KinArray.setClicked(true);
//                    KinArray.get(Kin_Size+1).setFullFilled(true);
                }else {
                    KinArray.get(ClickedRecyclerPosition).setClicked(true);
                    KinArray.get(ClickedRecyclerPosition).setFullFilled(true);
                }

                if (NEW.equalsIgnoreCase("YES")){
                    NewChangeState = "0";
                }

                ArrayList<OwnContactModel>mOwnContactArray;
                mOwnContactArray=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);

                System.out.println("KINSIZE: "+Kin_Size+" : "+KinClicked_Size);
                if (countryCode.getTextView_selectedCountry().getText().equals("")||ContactSp.getText().toString().isEmpty()||Concatc_details_phone.getText().toString().isEmpty()||Email.getText().toString().isEmpty()||Lname.getText().toString().isEmpty()||Fname.getText().toString().isEmpty()){
                    showAlertOKButton(getActivity(), "Alert", "Please Enter all Mandatory Fields", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }



                if (Kin_Size == KinClicked_Size || Kin_Size < KinClicked_Size)
                {
                    PreferenceManager.setConfirmButton(mContext,"0");
                }
                else{
                    PreferenceManager.setConfirmButton(mContext,"1");
                }
//                NewChangeState = "0";
                if (ContactSp.getText().toString().equalsIgnoreCase("")||Fname.getText().toString().equalsIgnoreCase("")||Concatc_details_phone.getText().toString().equalsIgnoreCase("")||Email.getText().toString().equalsIgnoreCase(""))
                {

//                    KinArray.get(Integer.parseInt(pos)).setFullFilled(false);

//                    showAlertOKButton(getActivity(), "Alert", "Please Enter all Mandatory Fields", "Ok", R.drawable.exclamationicon, R.drawable.round);
                    showAlertOKButton(getActivity(), "Alert", "Please Confirm all Contact", "Ok", R.drawable.exclamationicon, R.drawable.round);
                } else if (!Email.getText().toString().trim().matches(emailPattern)){

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
                    text.setText("Please enter valid email");
                    textHead.setText("Alert");
                    Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                    dialogButton.setText("Ok");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //pager.setCurrentItem(0);
                        }
                    });

                    dialog.show();

                } else {
                    PreferenceManager.setIsValueEmpty(mContext,"0");
                    boolean isFound=false;
                    for (int j=0;j<KinArray.size();j++)
                    {
                        if (Email.getText().toString().trim().equalsIgnoreCase(KinArray.get(j).getEmail()))
                        {
                            if (NEW.equals("YES")){
//                                if (j==Integer.valueOf(pos)) {
//                                    isFound=false;
//                                } else
//                                {
//                                    isFound=true;
//                                    break;
//                                }
                            }else {
//                                if (j==Integer.valueOf(pos)) {
//                                    isFound=false;
//                                } else
//                                {
//                                    isFound=true;
//                                    break;
//                                }
                            }

                        }
                        else
                        {
//                            isFound=false;
                        }
                    }

                    if (isFound)
                    {
//                        KinArray.get(Integer.valueOf(pos)).setFullFilled(false);
                        showAlertOKButton(getActivity(), "Alert", "Email Id Already exists with another contact", "Ok", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        if (NEW.equalsIgnoreCase("YES")){
                         /// set to first

                            AddtoRecyclerArray(Fname.getText().toString(),Lname.getText().toString(),Email.getText().toString(),Concatc_details_phone.getText().toString(),ContactSp.getText().toString(),CountryCode,RelationalSp.getText().toString());
//                            if (KinArray.get(0).isClicked()==true){
//                                KinArray.get(0).setClicked(true);
//                            }else {
//                                KinArray.get(0).setClicked(false);
//                            }

                        }else {
//                            KinArray.get(Integer.valueOf(pos)).setFullFilled(true);
                        }

                        dialog.dismiss();
                        SubmittingKinDetails(Fname.getText().toString(),Lname.getText().toString(),Email.getText().toString(),Concatc_details_phone.getText().toString(), ContactSp.getText().toString(), CountryCode);
                        familyKinRecyclerAdapter.notifyDataSetChanged();
                    }
//                    KinArray.get(ClickedRecyclerPosition).setClicked(true);
                    if (NEW.equalsIgnoreCase("YES")){
                        // set to first

                    }else {
//                        KinArray.get(Integer.valueOf(pos)).setFullFilled(true);
                    }

//                    dialog.dismiss();
//                    SubmittingKinDetails(Fname.getText().toString(),Lname.getText().toString(),Email.getText().toString(),Concatc_details_phone.getText().toString(),EventStatus, ContactSp.getText().toString(), CountryCode);
//                    familyKinRecyclerAdapter.notifyDataSetChanged();
                }




            }
        });

    }

    private void AddtoRecyclerArray(String firstName, String lastName, String mEmail, String contactphone, String title, String countryCode, String relationship) {
        KinModel model = new KinModel();
        model.setName(firstName);
        model.setLast_name(lastName);
        model.setEmail(mEmail);
        model.setPhone(contactphone);
        model.setRelationship(relationship);
        model.setTitle(title);
        model.setCode(countryCode);
        model.setNewData("YES");
        model.setClicked(true);
        model.setFullFilled(true);
        KinArray.add(KinArray.size(),model);

//        if (!KinArray.get(0).isClicked()) {
//            KinArray.get(0).setClicked(false);
//            KinArray.get(0).setFullFilled(false);
//        }else {
//            KinArray.get(0).setClicked(true);
//            KinArray.get(0).setFullFilled(true);
//        }
        familyKinRecyclerAdapter.notifyDataSetChanged();

    }

    private void ShowCannotDeleteDialog() {
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
        text.setText("Must have atleast one contact, deletion is not possible");
        textHead.setText("Alert");

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setText("OK");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void SubmittingKinDetails(String firstName, String lastName, String mEmail, String contactphone, String title, String countryCode) {

        String REQUEST = "";
        if (request_state.equals("0")){
            REQUEST = "0";
        }else if (request_state.equals("1")){
            REQUEST = "1";
        }else {
            REQUEST = "0";
        }


        String STATUS = "";
//        if (changedField.equals("1")){
//            STATUS = "1";
//        }else
//            if (changedField.equals("2")){
//            STATUS = "2";
//        }else
        if (changedField.equals("5")){
            STATUS = "5";
        }if (changedField.equals("3")){
            STATUS = "5";
        }else if (changedField.equals("4")){
            STATUS = "0";
        }

        if (NewChangeState.equals("1")){
            STATUS = "1"; // Edit
        }else if (NewChangeState.equals("2")){
            STATUS = "2"; // Delete
        }else if (NewChangeState.equals("3")){
            STATUS = "0"; // New
        }else if (NewChangeState.equals("5")){
            STATUS = "5";
        }else if (NewChangeState.equals("0")){
            STATUS = "0"; // NEW
        }




        System.out.println("REQ_STAT: STAT==> "+STATUS+" REQ ==> "+REQUEST);

        SubmitKinArray = new ArrayList<>();
        KinModel model = new KinModel();
        model.setStatus(STATUS);
        model.setRequest(REQUEST);
        model.setName(firstName);
        model.setLast_name(lastName);
        model.setEmail(mEmail);
        model.setTitle(title);
        model.setKin_id(KinID);
        model.setRelationship(Relationship);
        model.setCode(countryCode);
        model.setUser_mobile(contactphone);
        model.setStudent_id(StudentId);
        model.setCreated_at(CreatedAt);
        model.setUpdated_at(UpdatedAt);
        model.setPhone(Phone);
        model.setId(ID);
        model.setUser_id(UserID);
        model.setFullFilled(true);
        SubmitKinArray.add(model);
        System.out.println("Submit kin array size"+SubmitKinArray.size());
//        PreferenceManager.saveKinDetailsArrayList(SubmitKinArray,mContext);
//        familyKinRecyclerAdapter.notifyDataSetChanged();
        for (int i=0;i<PreferenceManager.getKinDetailsArrayList(mContext).size();i++)
        {
            System.out.println("Name"+PreferenceManager.getKinDetailsArrayList(mContext).get(i).getName());
        }
        Gson gson = new Gson();
        final String json = gson.toJson(model);
        System.out.println("KIN_JSON: "+json);

        GSONarray.add(json);
        PreferenceManager.saveFirstFragmentJSONArrayList(GSONarray,mContext);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                GSONarray.add(json);
//                for (int i = 0;i<GSONarray.size();i++){
//                    System.out.println("INSERTED: "+GSONarray.get(i));
//                }
//            }
//        },3000);

    }


    private void ShowDiscardDialog(Context mContext, String msgHead, String msg, int ico, int bgIcon,final Dialog contactDialogue) {
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

                contactDialogue.dismiss();
                dialog.dismiss();

                changedField = "";
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



    public void showConfirmButtonDialogue(final Activity activity) {
        GlobalList = new ArrayList<>();
        ContactList = new ArrayList<>();
        getData();

        ArrayList<OwnContactModel>mOwnContactArray;
        mOwnContactArray=PreferenceManager.getOwnDetailArrayList("OwnContact",mContext);
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.data_collection_confirm);
        ImageView backImg=dialog.findViewById(R.id.backImg);
        changedField="";
        final EditText FirstName = dialog.findViewById(R.id.firstNameTxt);
        final EditText LastName = dialog.findViewById(R.id.lastNameTxt);
        final EditText Email = dialog.findViewById(R.id.emailTxt);
        final EditText Adr1 = dialog.findViewById(R.id.addressLine1);
        final EditText Adr2 = dialog.findViewById(R.id.addressLine2);
        final EditText Adr3 = dialog.findViewById(R.id.addressLine3);
        final EditText Twn = dialog.findViewById(R.id.townTxt);
        final EditText State = dialog.findViewById(R.id.stateTxt);
        final EditText Country = dialog.findViewById(R.id.countryTxt);
        final EditText PostCode = dialog.findViewById(R.id.pinTxt);
        final EditText Phone = dialog.findViewById(R.id.dataCollect_Phone);
        final AutoCompleteTextView DropEdt = dialog.findViewById(R.id.DropEdt);
        final CountryCodePicker CountryCode = dialog.findViewById(R.id.dataCollect_Code);
        final Button confirmBtn = dialog.findViewById(R.id.confirmBtn);
        final ImageView communicationCheck1 = dialog.findViewById(R.id.communicationCheck1);
        final ImageView communicationCheck2 = dialog.findViewById(R.id.communicationCheck2);
        final AutoCompleteTextView RelationalSp = dialog.findViewById(R.id.relationshipSpinner);
        //Manadsajdsj
        // First name mandatory
        String fnameNograyPart = "First name";
        String fnameredPart = "*";
        SpannableStringBuilder fnameNobuilder = new SpannableStringBuilder();
        SpannableString fnameredColoredString = new SpannableString(fnameNograyPart);
        fnameredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, fnameNograyPart.length(), 0);
        fnameNobuilder.append(fnameredColoredString);
        SpannableString fnameblueColoredString = new SpannableString(fnameredPart);
        fnameblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, fnameredPart.length(), 0);
        fnameNobuilder.append(fnameblueColoredString);
        FirstName.setHint(fnameNobuilder);

        // Email id mandatory
        String emailNograyPart = "E-mail ID";
        String emailredPart = "*";
        SpannableStringBuilder emailNobuilder = new SpannableStringBuilder();
        SpannableString emailredColoredString = new SpannableString(emailNograyPart);
        emailredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, emailNograyPart.length(), 0);
        emailNobuilder.append(emailredColoredString);
        SpannableString emailblueColoredString = new SpannableString(emailredPart);
        emailblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, emailredPart.length(), 0);
        emailNobuilder.append(emailblueColoredString);
        Email.setHint(emailNobuilder);
        // Email id mandatory
//        String emailNograyPart = "E-mail ID";
//        String emailredPart = "*";
//        SpannableStringBuilder emailNobuilder = new SpannableStringBuilder();
//        SpannableString emailredColoredString = new SpannableString(emailNograyPart);
//        emailredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, emailNograyPart.length(), 0);
//        emailNobuilder.append(emailredColoredString);
//        SpannableString emailblueColoredString = new SpannableString(emailredPart);
//        emailblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, emailredPart.length(), 0);
//        emailNobuilder.append(emailblueColoredString);
//        Email.setHint(emailNobuilder);




        id=mOwnContactArray.get(0).getId();
        userId=mOwnContactArray.get(0).getUser_id();
        user_mobile=mOwnContactArray.get(0).getUser_mobile();
        studentId=mOwnContactArray.get(0).getStudent_id();
        created_at=mOwnContactArray.get(0).getCreated_at();
        updated_at=mOwnContactArray.get(0).getUpdated_at();
        if (PreferenceManager.getCorrespondenceMailMerge(mContext).equalsIgnoreCase("1"))
        {
            communicationCheck1.setImageResource(R.drawable.checked);
        }
        else
        {
            communicationCheck1.setImageResource(R.drawable.uncheck);
        }
        if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
        {
            communicationCheck2.setImageResource(R.drawable.checked);
        }
        else
        {
            communicationCheck2.setImageResource(R.drawable.uncheck);
        }
        if (mOwnContactArray.get(0).getTitle().equalsIgnoreCase("")){
            DropEdt.setText("");
            PreferenceManager.setIsValueEmpty(mContext,"1");
        }else {
            DropEdt.setText(mOwnContactArray.get(0).getTitle());
        }
        if (mOwnContactArray.get(0).getRelationship().equalsIgnoreCase("")){
            RelationalSp.setText("");
            PreferenceManager.setIsValueEmpty(mContext,"1");
        }else {
            RelationalSp.setText(mOwnContactArray.get(0).getRelationship());
        }
        if (mOwnContactArray.get(0).getPhone().equalsIgnoreCase("")){
            Phone.setText("");
            PreferenceManager.setIsValueEmpty(mContext,"1");
        }else {
            Phone.setText(mOwnContactArray.get(0).getPhone());
        }
        if (mOwnContactArray.get(0).getName().equalsIgnoreCase(""))
        {
            FirstName.setText("");
            PreferenceManager.setIsValueEmpty(mContext,"1");
            PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            FirstName.setText(mOwnContactArray.get(0).getName());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getLast_name().equalsIgnoreCase(""))
        {
            LastName.setText("");
            PreferenceManager.setIsValueEmpty(mContext,"1");
            PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            LastName.setText(mOwnContactArray.get(0).getLast_name());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getEmail().equalsIgnoreCase(""))
        {
            Email.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Email.setText(mOwnContactArray.get(0).getEmail());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getEmail().equalsIgnoreCase(""))
        {
            Email.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Email.setText(mOwnContactArray.get(0).getEmail());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getAddress1().equalsIgnoreCase(""))
        {
            Adr1.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Adr1.setText(mOwnContactArray.get(0).getAddress1());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getAddress2().equalsIgnoreCase(""))
        {
            Adr2.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Adr2.setText(mOwnContactArray.get(0).getAddress2());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getAddress3().equalsIgnoreCase(""))
        {
            Adr3.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Adr3.setText(mOwnContactArray.get(0).getAddress3());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }

        if (mOwnContactArray.get(0).getTown().equalsIgnoreCase(""))
        {
            Twn.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Twn.setText(mOwnContactArray.get(0).getTown());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getTown().equalsIgnoreCase(""))
        {
            Twn.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Twn.setText(mOwnContactArray.get(0).getTown());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getState().equalsIgnoreCase(""))
        {
            State.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            State.setText(mOwnContactArray.get(0).getTown());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getCountry().equalsIgnoreCase(""))
        {
            Country.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            Country.setText(mOwnContactArray.get(0).getCountry());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }
        if (mOwnContactArray.get(0).getPincode().equalsIgnoreCase(""))
        {
            PostCode.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            PostCode.setText(mOwnContactArray.get(0).getPincode());
//            PreferenceManager.setIsValueEmpty(mContext,"0");
        }

        dialog.show();

//        GlobalList.add(PreferenceManager.getGlobalListArrayList(mContext));

        GlobalList.addAll(PreferenceManager.getGlobalListArrayList(mContext));
        ContactList.addAll(PreferenceManager.getContactTypeArrayList(mContext));

        ArrayList<String> SpinnerData = new ArrayList<>();
        for (int i =0; i <GlobalList.size();i++){

            for (int j =0; j<GlobalList.get(i).getmGlobalSirnameArray().size();j++){
                System.out.println("Global: "+ GlobalList.get(i).getmGlobalSirnameArray().get(j).getName());
                SpinnerData.add(GlobalList.get(i).getmGlobalSirnameArray().get(j).getName());
//                System.out.println("Global: TYPE: "+ GlobalList.get(i).getType().equalsIgnoreCase("Title"));
            }

        }
        final ArrayAdapter<String> DROP = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SpinnerData);
        DropEdt.setCursorVisible(false);
        DropEdt.setFocusable(false);
        DropEdt.setAdapter(DROP);
        DropEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DropEdt.showDropDown();
            }
        });

        ArrayList<String> RelationalSpinnerData = new ArrayList<>();
        for (int i =0; i <ContactList.size();i++){
            for (int j =0; j<ContactList.get(i).getmGlobalSirnameArray().size();j++){
                System.out.println("Contact: "+ ContactList.get(i).getmGlobalSirnameArray().get(j).getName());
                RelationalSpinnerData.add(ContactList.get(i).getmGlobalSirnameArray().get(j).getName());
//                System.out.println("Global: Contact: "+ ContactList.get(i).getType().equalsIgnoreCase("Title"));
            }

        }
//        ArrayAdapter relationalArray = new ArrayAdapter(mContext,android.R.layout.simple_expandable_list_item_1,RelationalSpinnerData);
//        RelationalSp.setAdapter(relationalArray);

        final ArrayAdapter<String> relationalArray = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, RelationalSpinnerData);
        RelationalSp.setAdapter(relationalArray);
        RelationalSp.setFocusable(false);
        RelationalSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelationalSp.showDropDown();
            }
        });

        final  String firstName=FirstName.getText().toString();
        final  String lastName=LastName.getText().toString();
        final  String email=Email.getText().toString();
        final  String adr1=Adr1.getText().toString();
        final  String adr2=Adr2.getText().toString();
        final  String adr3=Adr3.getText().toString();
        final  String town=Twn.getText().toString();
        final  String state=State.getText().toString();
        final  String country=Country.getText().toString();
        final  String postCode=PostCode.getText().toString();

        FirstName.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedName=FirstName.getText().toString();
                System.out.println("Name test change"+changedName);
                System.out.println("Name test no change"+firstName);
                if (firstName.equalsIgnoreCase(changedName))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        LastName.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameLast=LastName.getText().toString();
                System.out.println("Name test change"+changedNameLast);
                System.out.println("Name test no change"+lastName);
                if (lastName.equalsIgnoreCase(changedNameLast))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        Email.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameEmail=Email.getText().toString();
                System.out.println("Name test change"+changedNameEmail);
                System.out.println("Name test no change"+email);
                if (email.equalsIgnoreCase(changedNameEmail))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        Adr1.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameAdr1=Adr1.getText().toString();
                System.out.println("Name test change"+changedNameAdr1);
                System.out.println("Name test no change"+adr1);
                if (adr1.equalsIgnoreCase(changedNameAdr1))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        Adr2.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameAdr2=Adr2.getText().toString();
                System.out.println("Name test change"+changedNameAdr2);
                System.out.println("Name test no change"+adr2);
                if (adr2.equalsIgnoreCase(changedNameAdr2))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        Adr3.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameAdr3=Adr3.getText().toString();
                System.out.println("Name test change"+changedNameAdr3);
                System.out.println("Name test no change"+adr3);
                if (adr3.equalsIgnoreCase(changedNameAdr3))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        Twn.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameTown=Twn.getText().toString();
                System.out.println("Name test change"+changedNameTown);
                System.out.println("Name test no change"+town);
                if (town.equalsIgnoreCase(changedNameTown))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        State.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameState=State.getText().toString();
                System.out.println("Name test change"+changedNameState);
                System.out.println("Name test no change"+state);
                if (town.equalsIgnoreCase(changedNameState))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        Country.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNameCountry=Country.getText().toString();
                System.out.println("Name test change"+changedNameCountry);
                System.out.println("Name test no change"+country);
                if (country.equalsIgnoreCase(changedNameCountry))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        PostCode.addTextChangedListener(new TextWatcher() {

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
                Log.e("afterTextChanged: ", "name Chaange");
                String changedNamePostCode=PostCode.getText().toString();
                System.out.println("Name test change"+changedNamePostCode);
                System.out.println("Name test no change"+postCode);
                if (country.equalsIgnoreCase(changedNamePostCode))
                {
                    changedField="";
                }
                else
                {
                    changedField="1";
                }
                System.out.println("Name test no change value"+changedField);
                //     showAlertButton(getActivity(), "Alert", "Please enter Email.", R.drawable.exclamationicon, R.drawable.round);
            }


        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Name test no change value click"+changedField);
                if (changedField.equalsIgnoreCase("1"))
                {
                    showDialogAlertTriggerDataCollection(getActivity(), "Confirm?", "Do you want to Discard your changes?", R.drawable.questionmark_icon, R.drawable.round,dialog);

                }
                else {
                    dialog.dismiss();
                }

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KinClicked_Size++;
                System.out.println("Name test no change value click"+changedField);
                if (FirstName.getText().toString().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter First name", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (LastName.getText().toString().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter Last name", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (Email.getText().toString().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter Email-ID", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (!Email.getText().toString().trim().matches(emailPattern))
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
                    text.setText("Please enter valid email");
                    textHead.setText("Alert");
                    Button dialogButton = dialog.findViewById(R.id.btn_Ok);
                    dialogButton.setText("Ok");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //pager.setCurrentItem(0);
                        }
                    });

                    dialog.show();
                }
                else if (Adr1.getText().toString().trim().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter address Line 1", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (Adr2.getText().toString().trim().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter address Line 2", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (Twn.getText().toString().trim().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter Town", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (State.getText().toString().trim().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter State", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (Country.getText().toString().trim().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter Country.", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else if (PostCode.getText().toString().trim().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter Post Code", "Ok", R.drawable.exclamationicon, R.drawable.round);
                }
                else
                {
                    PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).clear();
                    ArrayList<OwnContactModel>mOwnContactData=new ArrayList<>();
                    OwnContactModel mModel=new OwnContactModel();
                    mModel.setId(id);
                    mModel.setUser_id(userId);
                    mModel.setTitle(DropEdt.getText().toString());
                    mModel.setName(FirstName.getText().toString().trim());
                    mModel.setLast_name(LastName.getText().toString().trim());
                    mModel.setRelationship(RelationalSp.getText().toString());
                    mModel.setEmail(Email.getText().toString().trim());
                    mModel.setPhone(Phone.getText().toString());
                    mModel.setCode(CountryCode.getSelectedCountryCode());
                    mModel.setUser_mobile(user_mobile);
                    mModel.setStudent_id(studentId);
                    mModel.setAddress1(Adr1.getText().toString().trim());
                    mModel.setAddress2(Adr2.getText().toString().trim());
                    mModel.setAddress3(Adr3.getText().toString().trim());
                    mModel.setTown(Twn.getText().toString().trim());
                    mModel.setState(State.getText().toString().trim());
                    mModel.setCountry(Country.getText().toString().trim());
                    mModel.setPincode(PostCode.getText().toString().trim());
                    mModel.setStatus("1");
                    mModel.setCreated_at(created_at);
                    mModel.setUpdated_at(updated_at);
                    mModel.setUpdated(true);
                    mModel.setConfirmed(true);
                    mOwnContactData.add(mModel);

                    Gson gson   = new Gson();
                    String json = gson.toJson(mModel);
                    OwnDetailArray.add(json);
                    PreferenceManager.saveOwnDetailsJSONArrayList(OwnDetailArray,mContext);
                    ownDetailViewRelative.setBackgroundResource(R.drawable.rect_background_grey);
                    confirmBtnMain.setVisibility(View.GONE);
                    PreferenceManager.saveOwnDetailArrayList(mOwnContactData,"OwnContact",mContext);
                    if (!PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getName().equalsIgnoreCase("") && !PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getLast_name().equalsIgnoreCase(""))
                    {
                        nameOwnDetailTxt.setText(PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getName()+" "+PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getLast_name());
                    }
                    else if (!PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getName().equalsIgnoreCase("") && PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getLast_name().equalsIgnoreCase(""))
                    {
                        nameOwnDetailTxt.setText(PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getName());
                    }
                    else
                    {
                        nameOwnDetailTxt.setText(PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getName());
                    }

                    if (!PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getRelationship().equalsIgnoreCase(""))
                    {
                        contactTypeOwnDetailTxt.setText(PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getRelationship());
                    }
                    else
                    {
                        contactTypeOwnDetailTxt.setText("");
                    }


                    dialog.dismiss();
                }





            }
        });
    }

    private void getData() {


    }
    public void showAlertOKButton(final Activity activity, String msg, String msgHead, String button, int ico, int bgIcon) {
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

    public void showDialogAlertTriggerDataCollection(final Activity activity, String msgHead, String msg, int ico, int bgIcon,final Dialog contactDialogue)
    {
        final Dialog dialog = new Dialog(activity);
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

                contactDialogue.dismiss();
                dialog.dismiss();
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
    private void showAlertOKButtonMandatory(final Activity activity, String msg, String msgHead, String button, int ico, int bgIcon) {
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
}