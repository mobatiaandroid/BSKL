package com.mobatia.bskl.activity.datacollection_p2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.adapter.FamilyKinRecyclerAdapter;
import com.mobatia.bskl.activity.datacollection_p2.model.ContactTypeModel;
import com.mobatia.bskl.activity.datacollection_p2.model.GlobalListDataModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.OwnContactModel;
import com.mobatia.bskl.activity.datacollection_p2.model.PassportDetailModel;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.manager.countrypicker.CountryCodePicker;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class FirstScreenNewData extends Fragment {
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
    String corresspondanceOwn="";
    String contactOwn="";
    String justContact="";
    int recyclerPosition;
    int ClickedRecyclerPosition;
    public static String IsValueEmpty = "";
    public static String WhoValueEmpty = "";
    boolean LALA = false;
    RelativeLayout ownDetailViewRelative;
//    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+";
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    ArrayList<OwnContactModel>mOwnContactArrayList;
    ArrayList<GlobalListDataModel> GlobalList;
    ArrayList<ContactTypeModel> ContactList;
    ArrayList<KinModel> KinArray = new ArrayList<>();
    LinearLayout NoDataLayout,RecyclerLayout;
    RecyclerView mRecyclerView;
    FamilyKinRecyclerAdapter familyKinRecyclerAdapter;
    EditText Fname;
    SpinnerDialog spinnerDialog;
    EditText Lname;
    EditText Email,Concatc_details_phone;
    ImageView NoContentPlusIcon,RecyclerPlusIcon,CloseIcon;
    ArrayList<KinModel>SubmitKinArray;
    ArrayList<String> GSONarray = new ArrayList<>();
    ArrayList<String> OwnDetailArray = new ArrayList<>();
    int tempId=0;
    public FirstScreenNewData() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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
            public void onClick(View v)
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

        //Created By Aparna

       if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null)
       {
           System.out.println("null works");
       }

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
  //    System.out.println("Kin array bshow nsize"+PreferenceManager.getKinDetailsArrayListShow(mContext).size());

        if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null )
        {

        }
        else
        {
            AppController.kinArrayShow=PreferenceManager.getKinDetailsArrayListShow(mContext);
            AppController.kinArrayPass=PreferenceManager.getKinDetailsArrayList(mContext);
        }
        if (AppController.kinArrayShow.size()>0)
        {
            RecyclerLayout.setVisibility(View.VISIBLE);
            NoDataLayout.setVisibility(View.GONE);
            for (int i = 0;i<AppController.kinArrayShow.size();i++){
                System.out.println("A_DATA: "+AppController.kinArrayShow.get(i).getName());
            }
            familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,AppController.kinArrayShow);
            mRecyclerView.setAdapter(familyKinRecyclerAdapter);

        }
        else
            {
            NoDataLayout.setVisibility(View.VISIBLE);
            RecyclerLayout.setVisibility(View.GONE);
        }



        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClickItem(View v, int position)
                    {
                        System.out.println("Kin array show Size"+ AppController.kinArrayShow.size());
                        System.out.println("Kin array pass Size"+ AppController.kinArrayPass.size());
                        System.out.println("Kin array pass Size clicked position"+ position);
                        System.out.println("Kin array show name"+AppController.kinArrayShow.get(position).getName());
                        int clickedPosition=position;
                        showKinDetailDialog(clickedPosition);

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
// this plus icon will show if there is  kin details
        RecyclerPlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showKinDetailAdd();
            }
        });
// this plus icon will show if there is no kin details
        NoContentPlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showKinDetailAdd();

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

    private void ShowHelpDialog(FragmentActivity activity, String help, int questionmark_icon, int round)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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








    private void ShowDiscardDialog(Context mContext, String msgHead, String msg, int ico, int bgIcon,final Dialog contactDialogue) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                AppController.isKinEdited=false;
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



    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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
        final TextView Country = dialog.findViewById(R.id.countryTxt);
        final LinearLayout passportLinear = dialog.findViewById(R.id.passportLinear);
        final EditText PostCode = dialog.findViewById(R.id.pinTxt);
        final EditText Phone = dialog.findViewById(R.id.dataCollect_Phone);
        final LinearLayout correspondanceLinear = dialog.findViewById(R.id.correspondanceLinear);
        final LinearLayout contactLinear = dialog.findViewById(R.id.contactLinear);
        FirstName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        LastName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Email.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Adr1.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Adr2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Adr3.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Twn.setImeOptions(EditorInfo.IME_ACTION_DONE);
        State.setImeOptions(EditorInfo.IME_ACTION_DONE);
       // Country.setImeOptions(EditorInfo.IME_ACTION_DONE);
        PostCode.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Phone.setImeOptions(EditorInfo.IME_ACTION_DONE);
        final AutoCompleteTextView DropEdt = dialog.findViewById(R.id.DropEdt);
        final CountryCodePicker CountryCode = dialog.findViewById(R.id.dataCollect_Code);
        final Button confirmBtn = dialog.findViewById(R.id.confirmBtn);
        final TextView communicationCheck2Txt = dialog.findViewById(R.id.communicationCheck2Txt);
        final ImageView communicationCheck1 = dialog.findViewById(R.id.communicationCheck1);
        final ImageView communicationCheck2 = dialog.findViewById(R.id.communicationCheck2);
        LinearLayout communicationPreferenceLinear = dialog.findViewById(R.id.communicationPreferenceLinear);
        TextView communicationCheck1Txt = dialog.findViewById(R.id.communicationCheck1Txt);
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

        String emailNograyPart = "E-mail";
        String emailredPart = "*";
        SpannableStringBuilder emailNobuilder = new SpannableStringBuilder();
        SpannableString emailredColoredString = new SpannableString(emailNograyPart);
        emailredColoredString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, emailNograyPart.length(), 0);
        emailNobuilder.append(emailredColoredString);
        SpannableString emailblueColoredString = new SpannableString(emailredPart);
        emailblueColoredString.setSpan(new ForegroundColorSpan(Color.RED), 0, emailredPart.length(), 0);
        emailNobuilder.append(emailblueColoredString);
        Email.setHint(emailNobuilder);

        LastName.setHint(R.string.AST_LAST_NAME);

        id=mOwnContactArray.get(0).getId();
        userId=mOwnContactArray.get(0).getUser_id();
        user_mobile=mOwnContactArray.get(0).getUser_mobile();
        studentId=mOwnContactArray.get(0).getStudent_id();
        created_at=mOwnContactArray.get(0).getCreated_at();
        updated_at=mOwnContactArray.get(0).getUpdated_at();
        corresspondanceOwn=mOwnContactArray.get(0).getCorrespondencemailmerge();
         contactOwn=mOwnContactArray.get(0).getReportmailmerge();
         justContact=mOwnContactArray.get(0).getJustcontact();
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
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position) {
                //    Toast.makeText(mContext, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                Country.setText(item);
                final  String country=Country.getText().toString();
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
            }
        });

        passportLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });
        if(mOwnContactArray.get(0).getCorrespondencemailmerge().equalsIgnoreCase("0") && mOwnContactArray.get(0).getReportmailmerge().equalsIgnoreCase("0") && mOwnContactArray.get(0).getJustcontact().equalsIgnoreCase("1"))
        {
            contactLinear.setVisibility(View.GONE);
            correspondanceLinear.setVisibility(View.VISIBLE);
            communicationPreferenceLinear.setVisibility(View.VISIBLE);
            communicationCheck1Txt.setText("Contact does not have access to the BSKL Parent App");
        }
        else if(mOwnContactArray.get(0).getCorrespondencemailmerge().equalsIgnoreCase("0") && mOwnContactArray.get(0).getReportmailmerge().equalsIgnoreCase("0") && mOwnContactArray.get(0).getJustcontact().equalsIgnoreCase("0"))
        {
            contactLinear.setVisibility(View.VISIBLE);
            correspondanceLinear.setVisibility(View.VISIBLE);
            communicationPreferenceLinear.setVisibility(View.VISIBLE);
            communicationCheck1Txt.setText("Contact does not receive school correspondence");
            communicationCheck2Txt.setText("Contact does not receive school reports and cannot access student details via the BSKL Parent App");
        }
        else if(mOwnContactArray.get(0).getCorrespondencemailmerge().equalsIgnoreCase("1") && mOwnContactArray.get(0).getReportmailmerge().equalsIgnoreCase("1") && mOwnContactArray.get(0).getJustcontact().equalsIgnoreCase("0"))
        {
            contactLinear.setVisibility(View.VISIBLE);
            correspondanceLinear.setVisibility(View.VISIBLE);
            communicationPreferenceLinear.setVisibility(View.VISIBLE);
            communicationCheck1Txt.setText("Contact receives school correspondence");
            communicationCheck2Txt.setText("Contact receives school reports and has access to student details via the BSKL Parent App");
        }
        else if(mOwnContactArray.get(0).getCorrespondencemailmerge().equalsIgnoreCase("0") && mOwnContactArray.get(0).getReportmailmerge().equalsIgnoreCase("1") && mOwnContactArray.get(0).getJustcontact().equalsIgnoreCase("0"))
        {
            contactLinear.setVisibility(View.VISIBLE);
            correspondanceLinear.setVisibility(View.VISIBLE);
            communicationPreferenceLinear.setVisibility(View.VISIBLE);
            communicationCheck1Txt.setText("Contact does not receive school correspondence");
            communicationCheck2Txt.setText("Contact receives school reports and has access to student details via the BSKL Parent App");
        }
        else if(mOwnContactArray.get(0).getCorrespondencemailmerge().equalsIgnoreCase("1") && mOwnContactArray.get(0).getReportmailmerge().equalsIgnoreCase("0") && mOwnContactArray.get(0).getJustcontact().equalsIgnoreCase("0"))
        {
            contactLinear.setVisibility(View.VISIBLE);
            correspondanceLinear.setVisibility(View.VISIBLE);
            communicationPreferenceLinear.setVisibility(View.VISIBLE);
            communicationCheck1Txt.setText("Contact receives school correspondence");
            communicationCheck2Txt.setText("Contact does not receive school reports and cannot access student details via the BSKL Parent App");
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
        if (mOwnContactArray.get(0).getUser_mobile().equalsIgnoreCase("")){
            Phone.setText("");
            PreferenceManager.setIsValueEmpty(mContext,"1");
        }else {
            Phone.setText(mOwnContactArray.get(0).getUser_mobile());
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

        if (mOwnContactArray.get(0).getState().equalsIgnoreCase(""))
        {
            State.setText("");
//            PreferenceManager.setIsValueEmpty(mContext,"1");
//           PreferenceManager.setWhoValueEmpty(mContext,"Please add one contact as a minimum.");
        }
        else
        {
            State.setText(mOwnContactArray.get(0).getState());
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
                   // showDialogAlertTriggerDataCollection(getActivity(), "Confirm?", "Do you want to Discard your changes?", R.drawable.questionmark_icon, R.drawable.round,dialog);
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
                    mModel.setReportmailmerge(contactOwn);
                    mModel.setJustcontact(justContact);
                    mModel.setCorrespondencemailmerge(corresspondanceOwn);
                    mModel.setUpdated(false);
                    mModel.setConfirmed(false);
                    mOwnContactData.add(mModel);
                    ownDetailViewRelative.setBackgroundResource(R.drawable.rect_data_collection_red);
                    confirmBtnMain.setVisibility(View.VISIBLE);
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
                else if (Phone.getText().toString().equalsIgnoreCase(""))
                {
                    showAlertOKButton(getActivity(), "Alert", "Please Enter Phone Number", "Ok", R.drawable.exclamationicon, R.drawable.round);
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

                    String emailData= PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getEmail();
                    boolean isFound =false;
                    if (!isFound)
                    {
                     for (int i=0;i<AppController.kinArrayShow.size();i++)
                     {

                         String emaildata=AppController.kinArrayShow.get(i).getEmail();
                         if (emailData.equalsIgnoreCase(emaildata))
                         {
                             isFound=true;
                         }
                     }
                    }
                    if (isFound)
                    {
                        showAlertOKButton(getActivity(), "Alert", "Email ID Already Exists", "Ok", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        String phoneData= Phone.getText().toString().trim();
                        boolean isPhoneFound =false;
                        if (!isPhoneFound)
                        {
                            for (int i=0;i<AppController.kinArrayShow.size();i++)
                            {

                                String phonedata=AppController.kinArrayShow.get(i).getUser_mobile();
                                if (phoneData.equalsIgnoreCase(phonedata))
                                {
                                    isPhoneFound=true;
                                }
                            }
                        }
                        if (isPhoneFound)
                        {
                            showAlertOKButton(getActivity(), "Alert", "Phone Number Already Exists", "Ok", R.drawable.exclamationicon, R.drawable.round);

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
                            mModel.setReportmailmerge(contactOwn);
                            mModel.setJustcontact(justContact);
                            mModel.setCorrespondencemailmerge(corresspondanceOwn);
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
    public void showKinDetailAdd() {

        ContactList = new ArrayList<>();
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_contact_details);
        dialog.show();
        EditText firstName = dialog.findViewById(R.id.contactDetails_fname);
        EditText lastName = dialog.findViewById(R.id.ContactDetails_Lastname);
        EditText emailKin = dialog.findViewById(R.id.ContactDetails_Email);
        EditText contactNumber = dialog.findViewById(R.id.ContactDetails_Phone);
        Button confirmKinDetail = dialog.findViewById(R.id.ContactDetails_Submit);

        LinearLayout communicationPreferenceLinear = dialog.findViewById(R.id.communicationPreferenceLinear);
        TextView communicationCheck2Txt = dialog.findViewById(R.id.communicationCheck2Txt);
        ImageView communicationCheck1 = dialog.findViewById(R.id.communicationCheck1);
        ImageView communicationCheck2 = dialog.findViewById(R.id.communicationCheck2);
        AutoCompleteTextView ContactSpinner = dialog.findViewById(R.id.ContactDetails_Spinnertype);
        AutoCompleteTextView RelationalSpinner = dialog.findViewById(R.id.relationshipSpinner);
        CountryCodePicker countryCode = dialog.findViewById(R.id.spinnerCode);
        ImageView Close = dialog.findViewById(R.id.imageView4);
        firstName.setHint(R.string.AST_FIRST_NAME);
        lastName.setHint(R.string.AST_LAST_NAME);
     //   lastName.setHint(R.string.AST_FIRST_NAME);
        emailKin.setHint(R.string.AST_EMAIL);
        RelationalSpinner.setHint(R.string.AST_RELATIONSHIP);
        contactNumber.setHint(R.string.AST_CONTACT_NUMBER);
        ImageView removekinImg = dialog.findViewById(R.id.remove_kin);
        removekinImg.setVisibility(View.GONE);
        communicationPreferenceLinear.setVisibility(View.GONE);
//        if (PreferenceManager.getCorrespondenceMailMerge(mContext).equalsIgnoreCase("1"))
//        {
//            communicationCheck1.setImageResource(R.drawable.full_filled_circle);
//        }
//        else
//        {
//            communicationCheck1.setImageResource(R.drawable.full_filled_circle);
//        }
//        if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
//        {
//            communicationCheck2.setImageResource(R.drawable.full_filled_circle);
//            communicationCheck2Txt.setVisibility(View.VISIBLE);
//            communicationCheck2.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            communicationCheck2.setImageResource(R.drawable.full_filled_circle);
//            communicationCheck2Txt.setVisibility(View.GONE);
//            communicationCheck2.setVisibility(View.GONE);
//        }
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
        ContactSpinner.setCursorVisible(false);
        ContactSpinner.setFocusable(false);
        ContactSpinner.setAdapter(DROP);
        ContactSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactSpinner.showDropDown();
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
        RelationalSpinner.setAdapter(relationalArray);
        RelationalSpinner.setFocusable(false);
        RelationalSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelationalSpinner.showDropDown();
                if (!RelationalSpinner.getText().toString().equalsIgnoreCase("Driver") && !RelationalSpinner.getText().toString().equalsIgnoreCase("Domestic Helper"))
                {
                    emailKin.setHint(R.string.AST_EMAIL);
                }
                else
                {
                    emailKin.setHint(R.string.AST_EMAIL_NO);
                }
            }
        });

        ContactSpinner.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;
            }
        });

        firstName.addTextChangedListener(new TextWatcher()
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
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;

            }


        });

        lastName.addTextChangedListener(new TextWatcher() {

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
                AppController.isKinEdited=true;

            }


        });

        emailKin.addTextChangedListener(new TextWatcher() {

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
                AppController.isKinEdited=true;

            }


        });

        RelationalSpinner.addTextChangedListener(new TextWatcher() {

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
                AppController.isKinEdited=true;
                if (!RelationalSpinner.getText().toString().equalsIgnoreCase("Driver") && !RelationalSpinner.getText().toString().equalsIgnoreCase("Domestic Helper"))
                {
                    emailKin.setHint(R.string.AST_EMAIL);
                }
                else
                {
                    emailKin.setHint(R.string.AST_EMAIL_NO);
                }
            }


        });
        contactNumber.addTextChangedListener(new TextWatcher()
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
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;

            }


        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.isKinEdited)
                {
                    ShowDiscardDialog(mContext, "Confirm?", "Do you want to Discard changes?", R.drawable.questionmark_icon, R.drawable.round,dialog);

                }
                else
                {
                    dialog.dismiss();
                }

            }
        });


        confirmKinDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFound=false;
                if (firstName.getText().toString().trim().equalsIgnoreCase(""))
                {
                    ShowCondition("Please enter the First name");
                }
                else
                {
                 if(lastName.getText().toString().trim().equalsIgnoreCase(""))
                 {
                     ShowCondition("Please enter the Last name");
                 }
                 else
                 {
                     if (!RelationalSpinner.getText().toString().trim().equalsIgnoreCase("Driver") && !RelationalSpinner.getText().toString().trim().equalsIgnoreCase("Domestic Helper"))
                     {
                         if (emailKin.getText().toString().equalsIgnoreCase(""))
                         {
                             ShowCondition("Please enter the Email ID");
                         }
                         else
                         {
                             if (!emailKin.getText().toString().trim().matches(emailPattern))
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
                             else
                             {
                                 if (RelationalSpinner.getText().toString().trim().equalsIgnoreCase(""))
                                 {
                                     ShowCondition("Please enter the Relationship");
                                 }
                                 else
                                 {
                                     if (contactNumber.getText().toString().trim().equalsIgnoreCase(""))
                                     {
                                         ShowCondition("Please enter the Contact Number");
                                     }
                                     else
                                     {
                                         String emailData=emailKin.getText().toString();
                                         if (!isFound)
                                         {
                                             if (PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getEmail().equalsIgnoreCase(emailData))
                                             {
                                                 isFound=true;
                                             }
                                             else
                                             {
                                                 for (int i=0;i<AppController.kinArrayShow.size();i++)
                                                 {
                                                     String kinEmail=AppController.kinArrayShow.get(i).getEmail();

                                                     if (kinEmail.equalsIgnoreCase(emailData))
                                                     {
                                                         isFound=true;

                                                     }
                                                 }
                                             }
                                         }

                                         if (isFound)
                                         {
                                             ShowCondition("Email ID Already Exist");
                                             isFound=false;

                                         }


                                         else
                                         {
                                             boolean isPhoneFound=false;
                                             String phoneData=contactNumber.getText().toString();
                                             if (!isPhoneFound)
                                             {
                                                 if (PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getUser_mobile().equalsIgnoreCase(phoneData))
                                                 {
                                                     isPhoneFound=true;
                                                 }
                                                 else
                                                 {
                                                     for (int i=0;i<AppController.kinArrayShow.size();i++)
                                                     {
                                                         String kinEmail=AppController.kinArrayShow.get(i).getUser_mobile();

                                                         if (kinEmail.equalsIgnoreCase(phoneData))
                                                         {
                                                             isPhoneFound=true;

                                                         }
                                                     }
                                                 }
                                             }

                                             if (isPhoneFound)
                                             {
                                                 ShowCondition("Phone Number Already Exist");
                                                 isPhoneFound=false;

                                             }
                                             else
                                             {
                                                 String countryCOdeCheck=countryCode.getTextView_selectedCountry().getText().toString();
                                                 if (RelationalSpinner.getText().toString().equalsIgnoreCase("Local Emergency Contact")|| RelationalSpinner.getText().toString().equalsIgnoreCase("Emergency Contact"))
                                                 {
                                                     tempId=tempId+1;
                                                     String TempId="Mobatia_"+tempId;
                                                     KinModel model = new KinModel();
                                                     model.setStatus("0");
                                                     model.setRequest("1");
                                                     model.setName(firstName.getText().toString().trim());
                                                     model.setLast_name(lastName.getText().toString().trim());
                                                     model.setEmail(emailKin.getText().toString().trim());
                                                     model.setTitle(ContactSpinner.getText().toString());
                                                     model.setKin_id(TempId);
                                                     model.setRelationship(RelationalSpinner.getText().toString());
                                                     model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                                     model.setUser_mobile(contactNumber.getText().toString());
                                                     model.setStudent_id("");
                                                     model.setCreated_at("");
                                                     model.setUpdated_at("");
                                                     model.setCorrespondencemailmerge("");
                                                     model.setReportmailmerge("");
                                                     model.setJustcontact("");
                                                     model.setPhone(contactNumber.getText().toString());
                                                     model.setId(TempId);
                                                     model.setUser_id(PreferenceManager.getUserId(mContext));
                                                     model.setFullFilled(true);
                                                     model.setNewData(true);
                                                     model.setConfirmed(true);
                                                     AppController.kinArrayPass.add(model);
                                                     AppController.kinArrayShow.add(model);
                                                     if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null)
                                                     {
                                                         PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                                         PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                                     }
                                                     else
                                                     {
                                                         PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                                         PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                                         PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                                         PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                                     }

                                                     System.out.println("Kin array show Size"+ AppController.kinArrayShow.size());
                                                     System.out.println("Kin array bshow nsize"+PreferenceManager.getKinDetailsArrayListShow(mContext).size());
                                                     RecyclerLayout.setVisibility(View.VISIBLE);
                                                     NoDataLayout.setVisibility(View.GONE);
                                                     familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,AppController.kinArrayShow);
                                                     mRecyclerView.setAdapter(familyKinRecyclerAdapter);
                                                     // familyKinRecyclerAdapter.notifyDataSetChanged();
                                                     dialog.dismiss();
                                                     System.out.println("It eneters into country code");
//                                                if (!countryCOdeCheck.equalsIgnoreCase("+60"))
//                                                {
//                                                    System.out.println("It eneters into country codeffffffaaaa");
//                                                    ShowCondition("The “Emergency Contact” must have a Malaysian mobile number");
//                                                }
//                                                else
//                                                {
//                                                    tempId=tempId+1;
//                                                    String TempId="Mobatia_"+tempId;
//                                                    KinModel model = new KinModel();
//                                                    model.setStatus("0");
//                                                    model.setRequest("1");
//                                                    model.setName(firstName.getText().toString().trim());
//                                                    model.setLast_name(lastName.getText().toString().trim());
//                                                    model.setEmail(emailKin.getText().toString().trim());
//                                                    model.setTitle(ContactSpinner.getText().toString());
//                                                    model.setKin_id(TempId);
//                                                    model.setRelationship(RelationalSpinner.getText().toString());
//                                                    model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
//                                                    model.setUser_mobile(contactNumber.getText().toString());
//                                                    model.setStudent_id("");
//                                                    model.setCreated_at("");
//                                                    model.setUpdated_at("");
//                                                    model.setCorrespondencemailmerge("");
//                                                    model.setReportmailmerge("");
//                                                    model.setJustcontact("");
//                                                    model.setPhone(contactNumber.getText().toString());
//                                                    model.setId(TempId);
//                                                    model.setUser_id(PreferenceManager.getUserId(mContext));
//                                                    model.setFullFilled(true);
//                                                    model.setNewData(true);
//                                                    model.setConfirmed(true);
//                                                    AppController.kinArrayPass.add(model);
//                                                    AppController.kinArrayShow.add(model);
//                                                    if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null)
//                                                    {
//                                                        PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
//                                                        PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
//                                                    }
//                                                    else
//                                                    {
//                                                        PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
//                                                        PreferenceManager.getKinDetailsArrayList(mContext).clear();
//                                                        PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
//                                                        PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
//                                                    }
//
//                                                    System.out.println("Kin array show Size"+ AppController.kinArrayShow.size());
//                                                    System.out.println("Kin array bshow nsize"+PreferenceManager.getKinDetailsArrayListShow(mContext).size());
//                                                    RecyclerLayout.setVisibility(View.VISIBLE);
//                                                    NoDataLayout.setVisibility(View.GONE);
//                                                    familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,AppController.kinArrayShow);
//                                                    mRecyclerView.setAdapter(familyKinRecyclerAdapter);
//                                                    // familyKinRecyclerAdapter.notifyDataSetChanged();
//                                                    dialog.dismiss();
//
//                                                }
                                                 }
                                                 else
                                                 {
                                                     System.out.println("It eneters into country codedddd");
                                                     tempId=tempId+1;
                                                     String TempId="Mobatia_"+tempId;
                                                     KinModel model = new KinModel();
                                                     model.setStatus("0");
                                                     model.setRequest("1");
                                                     model.setName(firstName.getText().toString().trim());
                                                     model.setLast_name(lastName.getText().toString().trim());
                                                     model.setEmail(emailKin.getText().toString().trim());
                                                     model.setTitle(ContactSpinner.getText().toString());
                                                     model.setKin_id(TempId);
                                                     model.setRelationship(RelationalSpinner.getText().toString());
                                                     model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                                     model.setUser_mobile(contactNumber.getText().toString());
                                                     model.setStudent_id("");
                                                     model.setCreated_at("");
                                                     model.setUpdated_at("");
                                                     model.setPhone(contactNumber.getText().toString());
                                                     model.setId(TempId);
                                                     model.setUser_id(PreferenceManager.getUserId(mContext));
                                                     model.setFullFilled(true);
                                                     model.setNewData(true);
                                                     model.setConfirmed(true);
                                                     AppController.kinArrayPass.add(model);
                                                     AppController.kinArrayShow.add(model);
                                                     if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null)
                                                     {
                                                         PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                                         PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                                     }
                                                     else
                                                     {
                                                         PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                                         PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                                         PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                                         PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                                     }

                                                     System.out.println("Kin array show Size"+ AppController.kinArrayShow.size());
                                                     System.out.println("Kin array bshow nsize"+PreferenceManager.getKinDetailsArrayListShow(mContext).size());
                                                     RecyclerLayout.setVisibility(View.VISIBLE);
                                                     NoDataLayout.setVisibility(View.GONE);
                                                     familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,AppController.kinArrayShow);
                                                     mRecyclerView.setAdapter(familyKinRecyclerAdapter);
                                                     // familyKinRecyclerAdapter.notifyDataSetChanged();
                                                     dialog.dismiss();
                                                 }
                                             }

                                         }



                                     }
                                 }
                             }



                         }
                     }
                     else
                     {
                         if (RelationalSpinner.getText().toString().trim().equalsIgnoreCase(""))
                         {
                             ShowCondition("Please enter the Relationship");
                         }
                         else
                         {
                             if (contactNumber.getText().toString().trim().equalsIgnoreCase(""))
                             {
                                 ShowCondition("Please enter the Contact Number");
                             }
                             else
                             {
//                                String emailData=emailKin.getText().toString();
//                                if (!isFound)
//                                {
//                                    if (PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getEmail().equalsIgnoreCase(emailData))
//                                    {
//                                        isFound=true;
//                                    }
//                                    else
//                                    {
//                                        for (int i=0;i<AppController.kinArrayShow.size();i++)
//                                        {
//                                            String kinEmail=AppController.kinArrayShow.get(i).getEmail();
//
//                                            if (kinEmail.equalsIgnoreCase(emailData))
//                                            {
//                                                isFound=true;
//
//                                            }
//                                        }
//                                    }
//                                }

//                                if (isFound)
//                                {
//                                    ShowCondition("Email ID Already Exist");
//                                    isFound=false;
//
//                                }
//
//
//                                else
//                                {
                                 boolean isPhoneFound=false;
                                 String phoneData=contactNumber.getText().toString();
                                 if (!isPhoneFound)
                                 {
                                     if (PreferenceManager.getOwnDetailArrayList("OwnContact",mContext).get(0).getUser_mobile().equalsIgnoreCase(phoneData))
                                     {
                                         isPhoneFound=true;
                                     }
                                     else
                                     {
                                         for (int i=0;i<AppController.kinArrayShow.size();i++)
                                         {
                                             String kinEmail=AppController.kinArrayShow.get(i).getUser_mobile();

                                             if (kinEmail.equalsIgnoreCase(phoneData))
                                             {
                                                 isPhoneFound=true;

                                             }
                                         }
                                     }
                                 }

                                 if (isPhoneFound)
                                 {
                                     ShowCondition("Phone Number Already Exist");
                                     isPhoneFound=false;

                                 }
                                 else
                                 {
                                     String countryCOdeCheck=countryCode.getTextView_selectedCountry().getText().toString();
                                     if (RelationalSpinner.getText().toString().equalsIgnoreCase("Local Emergency Contact")|| RelationalSpinner.getText().toString().equalsIgnoreCase("Emergency Contact"))
                                     {
                                         tempId=tempId+1;
                                         String TempId="Mobatia_"+tempId;
                                         KinModel model = new KinModel();
                                         model.setStatus("0");
                                         model.setRequest("1");
                                         model.setName(firstName.getText().toString().trim());
                                         model.setLast_name(lastName.getText().toString().trim());
                                         model.setEmail(emailKin.getText().toString().trim());
                                         model.setTitle(ContactSpinner.getText().toString());
                                         model.setKin_id(TempId);
                                         model.setRelationship(RelationalSpinner.getText().toString());
                                         model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                         model.setUser_mobile(contactNumber.getText().toString());
                                         model.setStudent_id("");
                                         model.setCreated_at("");
                                         model.setUpdated_at("");
                                         model.setCorrespondencemailmerge("");
                                         model.setReportmailmerge("");
                                         model.setJustcontact("");
                                         model.setPhone(contactNumber.getText().toString());
                                         model.setId(TempId);
                                         model.setUser_id(PreferenceManager.getUserId(mContext));
                                         model.setFullFilled(true);
                                         model.setNewData(true);
                                         model.setConfirmed(true);
                                         AppController.kinArrayPass.add(model);
                                         AppController.kinArrayShow.add(model);
                                         if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null)
                                         {
                                             PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                             PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                         }
                                         else
                                         {
                                             PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                             PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                             PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                             PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                         }

                                         System.out.println("Kin array show Size"+ AppController.kinArrayShow.size());
                                         System.out.println("Kin array bshow nsize"+PreferenceManager.getKinDetailsArrayListShow(mContext).size());
                                         RecyclerLayout.setVisibility(View.VISIBLE);
                                         NoDataLayout.setVisibility(View.GONE);
                                         familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,AppController.kinArrayShow);
                                         mRecyclerView.setAdapter(familyKinRecyclerAdapter);
                                         // familyKinRecyclerAdapter.notifyDataSetChanged();
                                         dialog.dismiss();
                                         System.out.println("It eneters into country code");
                                     }
                                     else
                                     {
                                         System.out.println("It eneters into country codedddd");
                                         tempId=tempId+1;
                                         String TempId="Mobatia_"+tempId;
                                         KinModel model = new KinModel();
                                         model.setStatus("0");
                                         model.setRequest("1");
                                         model.setName(firstName.getText().toString().trim());
                                         model.setLast_name(lastName.getText().toString().trim());
                                         model.setEmail(emailKin.getText().toString().trim());
                                         model.setTitle(ContactSpinner.getText().toString());
                                         model.setKin_id(TempId);
                                         model.setRelationship(RelationalSpinner.getText().toString());
                                         model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                         model.setUser_mobile(contactNumber.getText().toString());
                                         model.setStudent_id("");
                                         model.setCreated_at("");
                                         model.setUpdated_at("");
                                         model.setPhone(contactNumber.getText().toString());
                                         model.setId(TempId);
                                         model.setUser_id(PreferenceManager.getUserId(mContext));
                                         model.setFullFilled(true);
                                         model.setNewData(true);
                                         model.setConfirmed(true);
                                         AppController.kinArrayPass.add(model);
                                         AppController.kinArrayShow.add(model);
                                         if (PreferenceManager.getKinDetailsArrayListShow(mContext)==null)
                                         {
                                             PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                             PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                         }
                                         else
                                         {
                                             PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                             PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                             PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                             PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                         }

                                         System.out.println("Kin array show Size"+ AppController.kinArrayShow.size());
                                         System.out.println("Kin array bshow nsize"+PreferenceManager.getKinDetailsArrayListShow(mContext).size());
                                         RecyclerLayout.setVisibility(View.VISIBLE);
                                         NoDataLayout.setVisibility(View.GONE);
                                         familyKinRecyclerAdapter = new FamilyKinRecyclerAdapter(mContext,AppController.kinArrayShow);
                                         mRecyclerView.setAdapter(familyKinRecyclerAdapter);
                                         // familyKinRecyclerAdapter.notifyDataSetChanged();
                                         dialog.dismiss();
                                     }
                                 }

                                 //     }



                             }
                         }
                     }
                 }


                }

            }
        });


    }
    public void showKinDetailDialog(int position)
    {
        AppController.isKinEdited=false;
        ContactList = new ArrayList<>();
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_contact_details);
        dialog.show();
        EditText firstName = dialog.findViewById(R.id.contactDetails_fname);
        EditText lastName = dialog.findViewById(R.id.ContactDetails_Lastname);
        EditText emailKin = dialog.findViewById(R.id.ContactDetails_Email);
        EditText contactNumber = dialog.findViewById(R.id.ContactDetails_Phone);
        Button confirmKinDetail = dialog.findViewById(R.id.ContactDetails_Submit);
        ImageView removekinImg = dialog.findViewById(R.id.remove_kin);
        TextView communicationCheck2Txt = dialog.findViewById(R.id.communicationCheck2Txt);
        TextView communicationCheck1Txt = dialog.findViewById(R.id.communicationCheck1Txt);
        ImageView communicationCheck1 = dialog.findViewById(R.id.communicationCheck1);
        ImageView communicationCheck2 = dialog.findViewById(R.id.communicationCheck2);
        AutoCompleteTextView ContactSpinner = dialog.findViewById(R.id.ContactDetails_Spinnertype);
        AutoCompleteTextView RelationalSpinner = dialog.findViewById(R.id.relationshipSpinner);
        CountryCodePicker countryCode = dialog.findViewById(R.id.spinnerCode);
        ImageView Close = dialog.findViewById(R.id.imageView4);
        LinearLayout communicationPreferenceLinear = dialog.findViewById(R.id.communicationPreferenceLinear);
        LinearLayout contactLinear = dialog.findViewById(R.id.contactLinear);
        LinearLayout correspondanceLinear = dialog.findViewById(R.id.correspondanceLinear);
        firstName.setHint(R.string.AST_FIRST_NAME);
        lastName.setHint(R.string.AST_LAST_NAME);
        System.out.println("Relationship "+AppController.kinArrayShow.get(position).getRelationship());
        if (!AppController.kinArrayShow.get(position).getRelationship().equalsIgnoreCase("Driver") && !AppController.kinArrayShow.get(position).getRelationship().equalsIgnoreCase("Domestic Helper"))
        {
            emailKin.setHint(R.string.AST_EMAIL);
        }
        else
        {
            emailKin.setHint(R.string.AST_EMAIL_NO);
        }
        RelationalSpinner.setHint(R.string.AST_RELATIONSHIP);
        contactNumber.setHint(R.string.AST_CONTACT_NUMBER);

        if (AppController.kinArrayShow.get(position).isNewData())
        {
            communicationPreferenceLinear.setVisibility(View.GONE);
        }
        else
        {
            if(AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("0") && AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("0") && AppController.kinArrayShow.get(position).getJustcontact().equalsIgnoreCase("1"))
            {
                contactLinear.setVisibility(View.GONE);
                correspondanceLinear.setVisibility(View.VISIBLE);
                communicationPreferenceLinear.setVisibility(View.VISIBLE);
                communicationCheck1Txt.setText("Contact does not have access to the BSKL Parent App");
            }
            else if(AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("0") && AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("0") && AppController.kinArrayShow.get(position).getJustcontact().equalsIgnoreCase("0"))
            {
                contactLinear.setVisibility(View.VISIBLE);
                correspondanceLinear.setVisibility(View.VISIBLE);
                communicationPreferenceLinear.setVisibility(View.VISIBLE);
                communicationCheck1Txt.setText("Contact does not receive school correspondence");
                communicationCheck2Txt.setText("Contact does not receive school reports and cannot access student details via the BSKL Parent App");
            }
            else if(AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("1") && AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("1") && AppController.kinArrayShow.get(position).getJustcontact().equalsIgnoreCase("0"))
            {
                contactLinear.setVisibility(View.VISIBLE);
                correspondanceLinear.setVisibility(View.VISIBLE);
                communicationPreferenceLinear.setVisibility(View.VISIBLE);
                communicationCheck1Txt.setText("Contact receives school correspondence");
                communicationCheck2Txt.setText("Contact receives school reports and has access to student details via the BSKL Parent App");
            }
            else if(AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("0") && AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("1") && AppController.kinArrayShow.get(position).getJustcontact().equalsIgnoreCase("0"))
            {
                contactLinear.setVisibility(View.VISIBLE);
                correspondanceLinear.setVisibility(View.VISIBLE);
                communicationPreferenceLinear.setVisibility(View.VISIBLE);
                communicationCheck1Txt.setText("Contact does not receive school correspondence");
                communicationCheck2Txt.setText("Contact receives school reports and has access to student details via the BSKL Parent App");
            }
            else if(AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("1") && AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("0") && AppController.kinArrayShow.get(position).getJustcontact().equalsIgnoreCase("0"))
            {
                contactLinear.setVisibility(View.VISIBLE);
                correspondanceLinear.setVisibility(View.VISIBLE);
                communicationPreferenceLinear.setVisibility(View.VISIBLE);
                communicationCheck1Txt.setText("Contact receives school correspondence");
                communicationCheck2Txt.setText("Contact does not receive school reports and cannot access student details via the BSKL Parent App");
            }
//            if (AppController.kinArrayShow.get(position).getJustcontact().equalsIgnoreCase("1"))
//            {
//                contactLinear.setVisibility(View.GONE);
//                correspondanceLinear.setVisibility(View.VISIBLE);
//                communicationPreferenceLinear.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                contactLinear.setVisibility(View.VISIBLE);
//                correspondanceLinear.setVisibility(View.VISIBLE);
//                communicationPreferenceLinear.setVisibility(View.VISIBLE);
//            }

//            if (AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("1") || AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("1") )
//            {
//                communicationPreferenceLinear.setVisibility(View.VISIBLE);
//                if (AppController.kinArrayShow.get(position).getCorrespondencemailmerge().equalsIgnoreCase("1"))
//                {
//                    correspondanceLinear.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    correspondanceLinear.setVisibility(View.GONE);
//                }
//                if (AppController.kinArrayShow.get(position).getReportmailmerge().equalsIgnoreCase("1"))
//                {
//                    contactLinear.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    contactLinear.setVisibility(View.GONE);
//                }
//            }
//            else
//            {
//                communicationPreferenceLinear.setVisibility(View.GONE);
//            }


        }
//        if (PreferenceManager.getCorrespondenceMailMerge(mContext).equalsIgnoreCase("1"))
//        {
//            communicationCheck1.setImageResource(R.drawable.full_filled_circle);
//        }
//        else
//        {
//            communicationCheck1.setImageResource(R.drawable.full_filled_circle);
//        }
//        if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
//        {
//            communicationCheck2.setImageResource(R.drawable.full_filled_circle);
//            communicationCheck2Txt.setVisibility(View.VISIBLE);
//            communicationCheck2.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            communicationCheck2.setImageResource(R.drawable.full_filled_circle);
//            communicationCheck2Txt.setVisibility(View.GONE);
//            communicationCheck2.setVisibility(View.GONE);
//        }
            if (!AppController.kinArrayShow.get(position).getName().equalsIgnoreCase(""))
            {
                firstName.setText(AppController.kinArrayShow.get(position).getName());
            }

            if (!AppController.kinArrayShow.get(position).getLast_name().equalsIgnoreCase(""))
            {
                lastName.setText(AppController.kinArrayShow.get(position).getLast_name());
            }
            if (!AppController.kinArrayShow.get(position).getEmail().equalsIgnoreCase(""))
            {
                emailKin.setText(AppController.kinArrayShow.get(position).getEmail());
            }
            if (!AppController.kinArrayShow.get(position).getUser_mobile().equalsIgnoreCase(""))
            {
                contactNumber.setText(AppController.kinArrayShow.get(position).getUser_mobile());
            }
            if (!AppController.kinArrayShow.get(position).getRelationship().equalsIgnoreCase(""))
            {
                RelationalSpinner.setText(AppController.kinArrayShow.get(position).getRelationship());
            }
            if (!AppController.kinArrayShow.get(position).getTitle().equalsIgnoreCase(""))
            {
                ContactSpinner.setText(AppController.kinArrayShow.get(position).getTitle());
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
        ContactSpinner.setCursorVisible(false);
        ContactSpinner.setFocusable(false);
        ContactSpinner.setAdapter(DROP);
        ContactSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactSpinner.showDropDown();
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
        RelationalSpinner.setAdapter(relationalArray);
        RelationalSpinner.setFocusable(false);
        RelationalSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int localCount=0;
                int kinCount=0;
                for (int i=0;i<AppController.kinArrayShow.size();i++)
                {
                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of kin"))
                    {
                        kinCount++;
                    }
                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                    {
                        localCount++;
                    }
                }
                System.out.println("Value for count kin"+kinCount);
                System.out.println("Value for count local"+localCount);
                System.out.println("Value for count current data"+RelationalSpinner.getText().toString());
                if (RelationalSpinner.getText().toString().equalsIgnoreCase("Next of kin"))
                {
                    if (kinCount>1)
                    {
                        RelationalSpinner.showDropDown();
                    }
                    else
                    {
                        ShowCondition("Relationship cannot be changed, there must be at least one Next of Kin contact associated with your family");
                    }
                }
                else  if (RelationalSpinner.getText().toString().equalsIgnoreCase("Local Emergency Contact") || RelationalSpinner.getText().toString().equalsIgnoreCase("Emergency Contact") )
                {
                    if (localCount>1)
                    {
                        RelationalSpinner.showDropDown();
                        if (!RelationalSpinner.getText().toString().equalsIgnoreCase("Driver") && !RelationalSpinner.getText().toString().equalsIgnoreCase("Domestic Helper"))
                        {
                            emailKin.setHint(R.string.AST_EMAIL);
                        }
                        else
                        {
                            emailKin.setHint(R.string.AST_EMAIL_NO);
                        }
                    }
                    else
                    {
                        ShowCondition("Relationship cannot be changed, there must be at least one Emergency Contact associated with your family");
                    }
                }
                else
                    {
                    RelationalSpinner.showDropDown();
                }
            }
        });
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.isKinEdited)
                {
                   // ShowDiscardDialog(mContext, "Confirm?", "Do you want to Discard changes?", R.drawable.questionmark_icon, R.drawable.round,dialog);
                    String status="";
                    String request="";
                    int kinArrayPassPos=-1;
                    if (!AppController.kinArrayShow.get(position).isNewData())
                    {
                        if (AppController.isKinEdited)
                        {
                            status="1";
                            request="0";
                        }
                        else
                        {
                            status="5";
                            request="0";
                        }
                    }
                    else
                    {
                        if (AppController.isKinEdited)
                        {
                            status="0";
                            request="1";
                        }
                        else
                        {
                            status="0";
                            request="1";
                        }
                    }
                    String kinID=AppController.kinArrayShow.get(position).getKin_id();
                    String IDs=AppController.kinArrayShow.get(position).getId();
                    String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                    String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                    String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                    String userID=AppController.kinArrayShow.get(position).getUser_id();
                    String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                    String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                    String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                    KinModel model = new KinModel();
                    model.setStatus(status);
                    model.setRequest(request);
                    model.setName(firstName.getText().toString().trim());
                    model.setLast_name(lastName.getText().toString().trim());
                    model.setEmail(emailKin.getText().toString().trim());
                    model.setTitle(ContactSpinner.getText().toString());
                    model.setKin_id(kinID);
                    model.setRelationship(RelationalSpinner.getText().toString());
                    model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                    model.setUser_mobile(contactNumber.getText().toString());
                    model.setStudent_id(studentID);
                    model.setCreated_at(createdAT);
                    model.setUpdated_at(updatedAT);
                    model.setPhone(contactNumber.getText().toString());
                    model.setId(IDs);
                    model.setUser_id(userID);
                    // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                    //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                    model.setCorrespondencemailmerge(corresspondence);
                    model.setJustcontact(justcontact);
                    model.setReportmailmerge(contacts);
                    model.setFullFilled(false);
                    if (AppController.kinArrayShow.get(position).isNewData())
                    {
                        model.setNewData(true);
                    }
                    else
                    {
                        model.setNewData(false);
                    }
                    model.setConfirmed(false);
                    for (int j=0;j<AppController.kinArrayPass.size();j++)
                    {
                        String dataId=AppController.kinArrayPass.get(j).getId();
                        if (IDs.equalsIgnoreCase(dataId))
                        {
                            kinArrayPassPos=j;
                        }
                    }
                    AppController.kinArrayShow.remove(position);
                    AppController.kinArrayPass.remove(kinArrayPassPos);
                    AppController.kinArrayShow.add(position,model);
                    AppController.kinArrayPass.add(kinArrayPassPos,model);
                    PreferenceManager.getKinDetailsArrayList(mContext).clear();
                    PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                    PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                    PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                    familyKinRecyclerAdapter.notifyDataSetChanged();
                    dialog.dismiss();


                }
                else
                {
                    dialog.dismiss();
                }

            }
        });
        removekinImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int localCount=0;
                int kinCount=0;
                for (int i=0;i<AppController.kinArrayShow.size();i++)
                {
                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Next of kin"))
                    {
                        kinCount++;
                    }
                    if (AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Local Emergency Contact")|| AppController.kinArrayShow.get(i).getRelationship().equalsIgnoreCase("Emergency Contact"))
                    {
                        localCount++;
                    }
                }
                System.out.println("Value for count kin"+kinCount);
                System.out.println("Value for count local"+localCount);
                System.out.println("Value for count current data"+RelationalSpinner.getText().toString());

                if (RelationalSpinner.getText().toString().equalsIgnoreCase("Local Emergency Contact")|| RelationalSpinner.getText().toString().equalsIgnoreCase("Emergency Contact"))
                {
                    if (localCount>1)
                    {
                        final Dialog Deletedialog = new Dialog(mContext);
                        Deletedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Deletedialog.setCancelable(false);
                        Deletedialog.setContentView(R.layout.dialogue_discard_data);
                        ImageView icon = Deletedialog.findViewById(R.id.iconImageView);
                        icon.setBackgroundResource(R.drawable.round);
                        icon.setImageResource(R.drawable.questionmark_icon);
                        TextView text = Deletedialog.findViewById(R.id.text_dialog);
                        TextView textHead = Deletedialog.findViewById(R.id.alertHead);
                        text.setText("Are you sure you want to delete this Contact?");
                        textHead.setText("Confirm?");
                        Button dialogButton = Deletedialog.findViewById(R.id.btn_Ok);
                        dialogButton.setText("Delete");
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (AppController.kinArrayShow.get(position).isNewData())
                                {
                                    int deletePos=-1;
                                    String newID=AppController.kinArrayShow.get(position).getId();
                                    AppController.kinArrayShow.remove(position);
                                    for (int i=0;i<AppController.kinArrayPass.size();i++)
                                    {
                                        String passArrayId=AppController.kinArrayPass.get(i).getId();
                                        if (newID.equalsIgnoreCase(passArrayId))
                                        {
                                            deletePos=i;
                                        }
                                    }

                                    AppController.kinArrayPass.remove(deletePos);
                                    PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                    PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                    PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                    PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                    familyKinRecyclerAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    Deletedialog.dismiss();
                                }
                                else
                                {
                                    String newID=AppController.kinArrayShow.get(position).getId();


                                    int deletePos=-1;
                                    for (int i=0;i<AppController.kinArrayPass.size();i++)
                                    {
                                        String passArrayId=AppController.kinArrayPass.get(i).getId();
                                        if (newID.equalsIgnoreCase(passArrayId))
                                        {
                                            deletePos=i;
                                        }
                                    }
                                    KinModel model = new KinModel();
                                    model.setStatus("2");
                                    model.setRequest("0");
                                    model.setName(AppController.kinArrayPass.get(deletePos).getName());
                                    model.setLast_name(AppController.kinArrayPass.get(deletePos).getLast_name());
                                    model.setEmail(AppController.kinArrayPass.get(deletePos).getEmail());
                                    model.setTitle(AppController.kinArrayPass.get(deletePos).getTitle());
                                    model.setKin_id(AppController.kinArrayPass.get(deletePos).getId());
                                    model.setRelationship(AppController.kinArrayPass.get(deletePos).getRelationship());
                                    model.setCode(AppController.kinArrayPass.get(deletePos).getCode());
                                    model.setUser_mobile(AppController.kinArrayPass.get(deletePos).getUser_mobile());
                                    model.setStudent_id(AppController.kinArrayPass.get(deletePos).getStudent_id());
                                    model.setCreated_at(AppController.kinArrayPass.get(deletePos).getCreated_at());
                                    model.setUpdated_at(AppController.kinArrayPass.get(deletePos).getUpdated_at());
                                    model.setPhone(AppController.kinArrayPass.get(deletePos).getPhone());
                                    model.setId(AppController.kinArrayPass.get(deletePos).getId());
                                    model.setUser_id(AppController.kinArrayPass.get(deletePos).getUser_id());
                                    model.setFullFilled(AppController.kinArrayPass.get(deletePos).isFullFilled());
                                    model.setNewData(AppController.kinArrayPass.get(deletePos).isNewData());
                                    model.setConfirmed(true);
                                    AppController.kinArrayShow.remove(position);
                                    AppController.kinArrayPass.remove(deletePos);
                                    AppController.kinArrayPass.add(deletePos,model);
                                    PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                    PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                    PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                    PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                    familyKinRecyclerAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    Deletedialog.dismiss();
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
                    else
                    {
                        ShowCondition("Contact cannot be deleted, there must be at least one Emergency Contact associated with your family");
                    }
                }
                else
                {
                    final Dialog Deletedialog = new Dialog(mContext);
                    Deletedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Deletedialog.setCancelable(false);
                    Deletedialog.setContentView(R.layout.dialogue_discard_data);
                    ImageView icon = Deletedialog.findViewById(R.id.iconImageView);
                    icon.setBackgroundResource(R.drawable.round);
                    icon.setImageResource(R.drawable.questionmark_icon);
                    TextView text = Deletedialog.findViewById(R.id.text_dialog);
                    TextView textHead = Deletedialog.findViewById(R.id.alertHead);
                    text.setText("Are you sure you want to delete this Contact?");
                    textHead.setText("Confirm?");
                    Button dialogButton = Deletedialog.findViewById(R.id.btn_Ok);
                    dialogButton.setText("Delete");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AppController.kinArrayShow.get(position).isNewData())
                            {
                                int deletePos=-1;
                                String newID=AppController.kinArrayShow.get(position).getId();
                                AppController.kinArrayShow.remove(position);
                                for (int i=0;i<AppController.kinArrayPass.size();i++)
                                {
                                    String passArrayId=AppController.kinArrayPass.get(i).getId();
                                    if (newID.equalsIgnoreCase(passArrayId))
                                    {
                                        deletePos=i;
                                    }
                                }

                                AppController.kinArrayPass.remove(deletePos);
                                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                familyKinRecyclerAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                Deletedialog.dismiss();
                            }
                            else
                            {
                                String newID=AppController.kinArrayShow.get(position).getId();


                                int deletePos=-1;
                                for (int i=0;i<AppController.kinArrayPass.size();i++)
                                {
                                    String passArrayId=AppController.kinArrayPass.get(i).getId();
                                    if (newID.equalsIgnoreCase(passArrayId))
                                    {
                                        deletePos=i;
                                    }
                                }
                                KinModel model = new KinModel();
                                model.setStatus("2");
                                model.setRequest("0");
                                model.setName(AppController.kinArrayPass.get(deletePos).getName());
                                model.setLast_name(AppController.kinArrayPass.get(deletePos).getLast_name());
                                model.setEmail(AppController.kinArrayPass.get(deletePos).getEmail());
                                model.setTitle(AppController.kinArrayPass.get(deletePos).getTitle());
                                model.setKin_id(AppController.kinArrayPass.get(deletePos).getId());
                                model.setRelationship(AppController.kinArrayPass.get(deletePos).getRelationship());
                                model.setCode(AppController.kinArrayPass.get(deletePos).getCode());
                                model.setUser_mobile(AppController.kinArrayPass.get(deletePos).getUser_mobile());
                                model.setStudent_id(AppController.kinArrayPass.get(deletePos).getStudent_id());
                                model.setCreated_at(AppController.kinArrayPass.get(deletePos).getCreated_at());
                                model.setUpdated_at(AppController.kinArrayPass.get(deletePos).getUpdated_at());
                                model.setPhone(AppController.kinArrayPass.get(deletePos).getPhone());
                                model.setId(AppController.kinArrayPass.get(deletePos).getId());
                                model.setUser_id(AppController.kinArrayPass.get(deletePos).getUser_id());
                                model.setFullFilled(AppController.kinArrayPass.get(deletePos).isFullFilled());
                                model.setNewData(AppController.kinArrayPass.get(deletePos).isNewData());
                                model.setConfirmed(true);
                                AppController.kinArrayShow.remove(position);
                                AppController.kinArrayPass.remove(deletePos);
                                AppController.kinArrayPass.add(deletePos,model);
                                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
                                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                                familyKinRecyclerAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                Deletedialog.dismiss();
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


            }
        });
        confirmKinDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFound=false;
                if (firstName.getText().toString().trim().equalsIgnoreCase(""))
                {
                    ShowCondition("Please enter the First name");
                }
                else {
                    if (lastName.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        ShowCondition("Please enter the Last name");
                    }
                    else
                    {
                        System.out.println("It enters into the condition"+RelationalSpinner.getText().toString());
                        if (!RelationalSpinner.getText().toString().trim().equalsIgnoreCase("Driver") && !RelationalSpinner.getText().toString().trim().equalsIgnoreCase("Domestic Helper"))
                        {
                            if (emailKin.getText().toString().equalsIgnoreCase("")) {
                                System.out.println("It enters into the condition not driver not helper");
                                ShowCondition("Please enter the Email");
                            }
                            else
                            {
                                if (!emailKin.getText().toString().trim().matches(emailPattern)) {
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
                                } else
                                {
                                    if (RelationalSpinner.getText().toString().trim().equalsIgnoreCase("")) {
                                        ShowCondition("Please enter the Relationship");
                                    } else {
                                        if (contactNumber.getText().toString().trim().equalsIgnoreCase("")) {
                                            ShowCondition("Please enter the Contact Number");
                                        } else {
                                            String emailData = emailKin.getText().toString();
                                            if (!isFound) {
                                                if (PreferenceManager.getOwnDetailArrayList("OwnContact", mContext).get(0).getEmail().equalsIgnoreCase(emailData)) {
                                                    isFound = true;
                                                } else {
                                                    for (int i = 0; i < AppController.kinArrayShow.size(); i++) {
                                                        String kinEmail = AppController.kinArrayShow.get(i).getEmail();

                                                        if (kinEmail.equalsIgnoreCase(emailData)) {
                                                            if (position == i) {

                                                            } else {
                                                                isFound = true;
                                                            }

                                                        }
                                                    }
                                                }
                                            }

                                            if (isFound) {
                                                ShowCondition("Email ID Already Exist");
                                                isFound = false;

                                            } else {
                                                boolean isPhoneFound = false;
                                                String phoneData = contactNumber.getText().toString();
                                                if (!isPhoneFound) {
                                                    if (PreferenceManager.getOwnDetailArrayList("OwnContact", mContext).get(0).getUser_mobile().equalsIgnoreCase(phoneData)) {
                                                        isPhoneFound = true;
                                                    } else {
                                                        for (int i = 0; i < AppController.kinArrayShow.size(); i++) {
                                                            String kinEmail = AppController.kinArrayShow.get(i).getUser_mobile();

                                                            if (kinEmail.equalsIgnoreCase(phoneData)) {
                                                                if (position == i) {

                                                                } else {
                                                                    isPhoneFound = true;
                                                                }

                                                            }
                                                        }
                                                    }
                                                }

                                                if (isPhoneFound) {
                                                    ShowCondition("Phone Number Already Exist");
                                                    isPhoneFound = false;

                                                } else {
                                                    if (RelationalSpinner.getText().toString().equalsIgnoreCase("Local Emergency Contact") || RelationalSpinner.getText().toString().equalsIgnoreCase("Emergency Contact")) {
                                                        System.out.println("It eneters into country codeffffff");
                                                        String status = "";
                                                        String request = "";
                                                        int kinArrayPassPos = -1;
                                                        if (!AppController.kinArrayShow.get(position).isNewData()) {
                                                            if (AppController.isKinEdited) {
                                                                status = "1";
                                                                request = "0";
                                                            } else {
                                                                status = "5";
                                                                request = "0";
                                                            }
                                                        } else {
                                                            if (AppController.isKinEdited) {
                                                                status = "0";
                                                                request = "1";
                                                            } else {
                                                                status = "0";
                                                                request = "1";
                                                            }
                                                        }
                                                        String kinID = AppController.kinArrayShow.get(position).getKin_id();
                                                        String IDs = AppController.kinArrayShow.get(position).getId();
                                                        String studentID = AppController.kinArrayShow.get(position).getStudent_id();
                                                        String createdAT = AppController.kinArrayShow.get(position).getCreated_at();
                                                        String updatedAT = AppController.kinArrayShow.get(position).getUpdated_at();
                                                        String userID = AppController.kinArrayShow.get(position).getUser_id();
                                                        String corresspondence = AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                                                        String justcontact = AppController.kinArrayShow.get(position).getJustcontact();
                                                        String contacts = AppController.kinArrayShow.get(position).getReportmailmerge();
                                                        KinModel model = new KinModel();
                                                        model.setStatus(status);
                                                        model.setRequest(request);
                                                        model.setName(firstName.getText().toString().trim());
                                                        model.setLast_name(lastName.getText().toString().trim());
                                                        model.setEmail(emailKin.getText().toString().trim());
                                                        model.setTitle(ContactSpinner.getText().toString());
                                                        model.setKin_id(kinID);
                                                        model.setRelationship(RelationalSpinner.getText().toString());
                                                        model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                                        model.setUser_mobile(contactNumber.getText().toString());
                                                        model.setStudent_id(studentID);
                                                        model.setCreated_at(createdAT);
                                                        model.setUpdated_at(updatedAT);
                                                        model.setPhone(contactNumber.getText().toString());
                                                        model.setId(IDs);
                                                        model.setUser_id(userID);
                                                        model.setFullFilled(true);
                                                        model.setCorrespondencemailmerge(corresspondence);
                                                        model.setJustcontact(justcontact);
                                                        model.setReportmailmerge(contacts);
                                                        if (AppController.kinArrayShow.get(position).isNewData()) {
                                                            model.setNewData(true);
                                                        } else {
                                                            model.setNewData(false);
                                                        }
                                                        model.setConfirmed(true);
                                                        for (int j = 0; j < AppController.kinArrayPass.size(); j++) {
                                                            String dataId = AppController.kinArrayPass.get(j).getId();
                                                            if (IDs.equalsIgnoreCase(dataId)) {
                                                                kinArrayPassPos = j;
                                                            }
                                                        }
                                                        AppController.kinArrayShow.remove(position);
                                                        AppController.kinArrayPass.remove(kinArrayPassPos);
                                                        AppController.kinArrayShow.add(position, model);
                                                        AppController.kinArrayPass.add(kinArrayPassPos, model);
                                                        PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                                        PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                                        PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow, mContext);
                                                        PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass, mContext);
                                                        dialog.dismiss();
                                                        familyKinRecyclerAdapter.notifyDataSetChanged();
                                                        System.out.println("It eneters into country codeffffffdddd" + countryCode.getTextView_selectedCountry().getText().toString());

//                                                }

                                                    } else {
                                                        System.out.println("It eneters into country codevvvvvvv");
                                                        String status = "";
                                                        String request = "";
                                                        int kinArrayPassPos = -1;
                                                        if (!AppController.kinArrayShow.get(position).isNewData()) {
                                                            if (AppController.isKinEdited) {
                                                                status = "1";
                                                                request = "0";
                                                            } else {
                                                                status = "5";
                                                                request = "0";
                                                            }
                                                        } else {
                                                            if (AppController.isKinEdited) {
                                                                status = "0";
                                                                request = "1";
                                                            } else {
                                                                status = "0";
                                                                request = "1";
                                                            }
                                                        }
                                                        String kinID = AppController.kinArrayShow.get(position).getKin_id();
                                                        String IDs = AppController.kinArrayShow.get(position).getId();
                                                        String studentID = AppController.kinArrayShow.get(position).getStudent_id();
                                                        String createdAT = AppController.kinArrayShow.get(position).getCreated_at();
                                                        String updatedAT = AppController.kinArrayShow.get(position).getUpdated_at();
                                                        String userID = AppController.kinArrayShow.get(position).getUser_id();
                                                        String corresspondence = AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                                                        String justcontact = AppController.kinArrayShow.get(position).getJustcontact();
                                                        String contacts = AppController.kinArrayShow.get(position).getReportmailmerge();
                                                        KinModel model = new KinModel();
                                                        model.setStatus(status);
                                                        model.setRequest(request);
                                                        model.setName(firstName.getText().toString().trim());
                                                        model.setLast_name(lastName.getText().toString().trim());
                                                        model.setEmail(emailKin.getText().toString().trim());
                                                        model.setTitle(ContactSpinner.getText().toString());
                                                        model.setKin_id(kinID);
                                                        model.setRelationship(RelationalSpinner.getText().toString());
                                                        model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                                        model.setUser_mobile(contactNumber.getText().toString());
                                                        model.setStudent_id(studentID);
                                                        model.setCreated_at(createdAT);
                                                        model.setUpdated_at(updatedAT);
                                                        model.setPhone(contactNumber.getText().toString());
                                                        model.setId(IDs);
                                                        model.setUser_id(userID);
                                                        // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                                                        //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                                                        model.setCorrespondencemailmerge(corresspondence);
                                                        model.setJustcontact(justcontact);
                                                        model.setReportmailmerge(contacts);
                                                        model.setFullFilled(true);
                                                        if (AppController.kinArrayShow.get(position).isNewData()) {
                                                            model.setNewData(true);
                                                        } else {
                                                            model.setNewData(false);
                                                        }
                                                        model.setConfirmed(true);
                                                        for (int j = 0; j < AppController.kinArrayPass.size(); j++) {
                                                            String dataId = AppController.kinArrayPass.get(j).getId();
                                                            if (IDs.equalsIgnoreCase(dataId)) {
                                                                kinArrayPassPos = j;
                                                            }
                                                        }
                                                        AppController.kinArrayShow.remove(position);
                                                        AppController.kinArrayPass.remove(kinArrayPassPos);
                                                        AppController.kinArrayShow.add(position, model);
                                                        AppController.kinArrayPass.add(kinArrayPassPos, model);
                                                        PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                                        PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                                        PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow, mContext);
                                                        PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass, mContext);
                                                        dialog.dismiss();
                                                        familyKinRecyclerAdapter.notifyDataSetChanged();
                                                    }

                                                }

                                            }

                                        }
                                    }
                                }


                            }
                        } else
                        {
                            if (RelationalSpinner.getText().toString().trim().equalsIgnoreCase("")) {
                                ShowCondition("Please enter the Relationship");
                            } else {
                                if (contactNumber.getText().toString().trim().equalsIgnoreCase("")) {
                                    ShowCondition("Please enter the Contact Number");
                                } else {
//                                String emailData = emailKin.getText().toString();
//                                if (!isFound) {
//                                    if (PreferenceManager.getOwnDetailArrayList("OwnContact", mContext).get(0).getEmail().equalsIgnoreCase(emailData)) {
//                                        isFound = true;
//                                    } else {
//                                        for (int i = 0; i < AppController.kinArrayShow.size(); i++) {
//                                            String kinEmail = AppController.kinArrayShow.get(i).getEmail();
//
//                                            if (kinEmail.equalsIgnoreCase(emailData)) {
//                                                if (position == i) {
//
//                                                } else {
//                                                    isFound = true;
//                                                }
//
//                                            }
//                                        }
//                                    }
//                                }
//
//                                if (isFound) {
//                                    ShowCondition("Email ID Already Exist");
//                                    isFound = false;
//
//                                } else {
                                    boolean isPhoneFound = false;
                                    String phoneData = contactNumber.getText().toString();
                                    if (!isPhoneFound) {
                                        if (PreferenceManager.getOwnDetailArrayList("OwnContact", mContext).get(0).getUser_mobile().equalsIgnoreCase(phoneData)) {
                                            isPhoneFound = true;
                                        } else {
                                            for (int i = 0; i < AppController.kinArrayShow.size(); i++) {
                                                String kinEmail = AppController.kinArrayShow.get(i).getUser_mobile();

                                                if (kinEmail.equalsIgnoreCase(phoneData)) {
                                                    if (position == i) {

                                                    } else {
                                                        isPhoneFound = true;
                                                    }

                                                }
                                            }
                                        }
                                    }

                                    if (isPhoneFound) {
                                        ShowCondition("Phone Number Already Exist");
                                        isPhoneFound = false;

                                    } else {
                                        if (RelationalSpinner.getText().toString().equalsIgnoreCase("Local Emergency Contact") || RelationalSpinner.getText().toString().equalsIgnoreCase("Emergency Contact")) {
                                            System.out.println("It eneters into country codeffffff");
                                            String status = "";
                                            String request = "";
                                            int kinArrayPassPos = -1;
                                            if (!AppController.kinArrayShow.get(position).isNewData()) {
                                                if (AppController.isKinEdited) {
                                                    status = "1";
                                                    request = "0";
                                                } else {
                                                    status = "5";
                                                    request = "0";
                                                }
                                            } else {
                                                if (AppController.isKinEdited) {
                                                    status = "0";
                                                    request = "1";
                                                } else {
                                                    status = "0";
                                                    request = "1";
                                                }
                                            }
                                            String kinID = AppController.kinArrayShow.get(position).getKin_id();
                                            String IDs = AppController.kinArrayShow.get(position).getId();
                                            String studentID = AppController.kinArrayShow.get(position).getStudent_id();
                                            String createdAT = AppController.kinArrayShow.get(position).getCreated_at();
                                            String updatedAT = AppController.kinArrayShow.get(position).getUpdated_at();
                                            String userID = AppController.kinArrayShow.get(position).getUser_id();
                                            String corresspondence = AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                                            String justcontact = AppController.kinArrayShow.get(position).getJustcontact();
                                            String contacts = AppController.kinArrayShow.get(position).getReportmailmerge();
                                            KinModel model = new KinModel();
                                            model.setStatus(status);
                                            model.setRequest(request);
                                            model.setName(firstName.getText().toString().trim());
                                            model.setLast_name(lastName.getText().toString().trim());
                                            model.setEmail(emailKin.getText().toString().trim());
                                            model.setTitle(ContactSpinner.getText().toString());
                                            model.setKin_id(kinID);
                                            model.setRelationship(RelationalSpinner.getText().toString());
                                            model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                            model.setUser_mobile(contactNumber.getText().toString());
                                            model.setStudent_id(studentID);
                                            model.setCreated_at(createdAT);
                                            model.setUpdated_at(updatedAT);
                                            model.setPhone(contactNumber.getText().toString());
                                            model.setId(IDs);
                                            model.setUser_id(userID);
                                            model.setFullFilled(true);
                                            model.setCorrespondencemailmerge(corresspondence);
                                            model.setJustcontact(justcontact);
                                            model.setReportmailmerge(contacts);
                                            if (AppController.kinArrayShow.get(position).isNewData()) {
                                                model.setNewData(true);
                                            } else {
                                                model.setNewData(false);
                                            }
                                            model.setConfirmed(true);
                                            for (int j = 0; j < AppController.kinArrayPass.size(); j++) {
                                                String dataId = AppController.kinArrayPass.get(j).getId();
                                                if (IDs.equalsIgnoreCase(dataId)) {
                                                    kinArrayPassPos = j;
                                                }
                                            }
                                            AppController.kinArrayShow.remove(position);
                                            AppController.kinArrayPass.remove(kinArrayPassPos);
                                            AppController.kinArrayShow.add(position, model);
                                            AppController.kinArrayPass.add(kinArrayPassPos, model);
                                            PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                            PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                            PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow, mContext);
                                            PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass, mContext);
                                            dialog.dismiss();
                                            familyKinRecyclerAdapter.notifyDataSetChanged();
                                            System.out.println("It eneters into country codeffffffdddd" + countryCode.getTextView_selectedCountry().getText().toString());
//                                                if (!countryCode.getTextView_selectedCountry().getText().toString().equalsIgnoreCase("+60"))
//                                                {
//                                                    System.out.println("It eneters into country codeffffffdddd");
//                                                    ShowCondition("The “Emergency Contact” must have a Malaysian mobile number");
//
//                                                }
//                                                else
//                                                {
//                                                    System.out.println("It eneters into country codeffffff");
//                                                    String status="";
//                                                    String request="";
//                                                    int kinArrayPassPos=-1;
//                                                    if (!AppController.kinArrayShow.get(position).isNewData())
//                                                    {
//                                                        if (AppController.isKinEdited)
//                                                        {
//                                                            status="1";
//                                                            request="0";
//                                                        }
//                                                        else
//                                                        {
//                                                            status="5";
//                                                            request="0";
//                                                        }
//                                                    }
//                                                    else
//                                                    {
//                                                        if (AppController.isKinEdited)
//                                                        {
//                                                            status="0";
//                                                            request="1";
//                                                        }
//                                                        else
//                                                        {
//                                                            status="0";
//                                                            request="1";
//                                                        }
//                                                    }
//                                                    String kinID=AppController.kinArrayShow.get(position).getKin_id();
//                                                    String IDs=AppController.kinArrayShow.get(position).getId();
//                                                    String studentID=AppController.kinArrayShow.get(position).getStudent_id();
//                                                    String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
//                                                    String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
//                                                    String userID=AppController.kinArrayShow.get(position).getUser_id();
//                                                    String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
//                                                    String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
//                                                    String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
//                                                    KinModel model = new KinModel();
//                                                    model.setStatus(status);
//                                                    model.setRequest(request);
//                                                    model.setName(firstName.getText().toString().trim());
//                                                    model.setLast_name(lastName.getText().toString().trim());
//                                                    model.setEmail(emailKin.getText().toString().trim());
//                                                    model.setTitle(ContactSpinner.getText().toString());
//                                                    model.setKin_id(kinID);
//                                                    model.setRelationship(RelationalSpinner.getText().toString());
//                                                    model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
//                                                    model.setUser_mobile(contactNumber.getText().toString());
//                                                    model.setStudent_id(studentID);
//                                                    model.setCreated_at(createdAT);
//                                                    model.setUpdated_at(updatedAT);
//                                                    model.setPhone(contactNumber.getText().toString());
//                                                    model.setId(IDs);
//                                                    model.setUser_id(userID);
//                                                    model.setFullFilled(true);
//                                                    model.setCorrespondencemailmerge(corresspondence);
//                                                    model.setJustcontact(justcontact);
//                                                    model.setReportmailmerge(contacts);
//                                                    if (AppController.kinArrayShow.get(position).isNewData())
//                                                    {
//                                                        model.setNewData(true);
//                                                    }
//                                                    else
//                                                    {
//                                                        model.setNewData(false);
//                                                    }
//                                                    model.setConfirmed(true);
//                                                    for (int j=0;j<AppController.kinArrayPass.size();j++)
//                                                    {
//                                                        String dataId=AppController.kinArrayPass.get(j).getId();
//                                                        if (IDs.equalsIgnoreCase(dataId))
//                                                        {
//                                                            kinArrayPassPos=j;
//                                                        }
//                                                    }
//                                                    AppController.kinArrayShow.remove(position);
//                                                    AppController.kinArrayPass.remove(kinArrayPassPos);
//                                                    AppController.kinArrayShow.add(position,model);
//                                                    AppController.kinArrayPass.add(kinArrayPassPos,model);
//                                                    PreferenceManager.getKinDetailsArrayList(mContext).clear();
//                                                    PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
//                                                    PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
//                                                    PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
//                                                    dialog.dismiss();
//                                                    familyKinRecyclerAdapter.notifyDataSetChanged();
//                                                }

                                        } else {
                                            System.out.println("It eneters into country codevvvvvvv");
                                            String status = "";
                                            String request = "";
                                            int kinArrayPassPos = -1;
                                            if (!AppController.kinArrayShow.get(position).isNewData()) {
                                                if (AppController.isKinEdited) {
                                                    status = "1";
                                                    request = "0";
                                                } else {
                                                    status = "5";
                                                    request = "0";
                                                }
                                            } else {
                                                if (AppController.isKinEdited) {
                                                    status = "0";
                                                    request = "1";
                                                } else {
                                                    status = "0";
                                                    request = "1";
                                                }
                                            }
                                            String kinID = AppController.kinArrayShow.get(position).getKin_id();
                                            String IDs = AppController.kinArrayShow.get(position).getId();
                                            String studentID = AppController.kinArrayShow.get(position).getStudent_id();
                                            String createdAT = AppController.kinArrayShow.get(position).getCreated_at();
                                            String updatedAT = AppController.kinArrayShow.get(position).getUpdated_at();
                                            String userID = AppController.kinArrayShow.get(position).getUser_id();
                                            String corresspondence = AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                                            String justcontact = AppController.kinArrayShow.get(position).getJustcontact();
                                            String contacts = AppController.kinArrayShow.get(position).getReportmailmerge();
                                            KinModel model = new KinModel();
                                            model.setStatus(status);
                                            model.setRequest(request);
                                            model.setName(firstName.getText().toString().trim());
                                            model.setLast_name(lastName.getText().toString().trim());
                                            model.setEmail(emailKin.getText().toString().trim());
                                            model.setTitle(ContactSpinner.getText().toString());
                                            model.setKin_id(kinID);
                                            model.setRelationship(RelationalSpinner.getText().toString());
                                            model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                                            model.setUser_mobile(contactNumber.getText().toString());
                                            model.setStudent_id(studentID);
                                            model.setCreated_at(createdAT);
                                            model.setUpdated_at(updatedAT);
                                            model.setPhone(contactNumber.getText().toString());
                                            model.setId(IDs);
                                            model.setUser_id(userID);
                                            // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                                            //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                                            model.setCorrespondencemailmerge(corresspondence);
                                            model.setJustcontact(justcontact);
                                            model.setReportmailmerge(contacts);
                                            model.setFullFilled(true);
                                            if (AppController.kinArrayShow.get(position).isNewData()) {
                                                model.setNewData(true);
                                            } else {
                                                model.setNewData(false);
                                            }
                                            model.setConfirmed(true);
                                            for (int j = 0; j < AppController.kinArrayPass.size(); j++) {
                                                String dataId = AppController.kinArrayPass.get(j).getId();
                                                if (IDs.equalsIgnoreCase(dataId)) {
                                                    kinArrayPassPos = j;
                                                }
                                            }
                                            AppController.kinArrayShow.remove(position);
                                            AppController.kinArrayPass.remove(kinArrayPassPos);
                                            AppController.kinArrayShow.add(position, model);
                                            AppController.kinArrayPass.add(kinArrayPassPos, model);
                                            PreferenceManager.getKinDetailsArrayList(mContext).clear();
                                            PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                                            PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow, mContext);
                                            PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass, mContext);
                                            dialog.dismiss();
                                            familyKinRecyclerAdapter.notifyDataSetChanged();
                                        }

                                    }

                                    //   }

                                }
                            }
                        }

                    }

                }

            }
        });



        ContactSpinner.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;

                String status="";
                String request="";
                int kinArrayPassPos=-1;
                if (!AppController.kinArrayShow.get(position).isNewData())
                {
                    if (AppController.isKinEdited)
                    {
                        status="1";
                        request="0";
                    }
                    else
                    {
                        status="5";
                        request="0";
                    }
                }
                else
                {
                    if (AppController.isKinEdited)
                    {
                        status="0";
                        request="1";
                    }
                    else
                    {
                        status="0";
                        request="1";
                    }
                }
                String kinID=AppController.kinArrayShow.get(position).getKin_id();
                String IDs=AppController.kinArrayShow.get(position).getId();
                String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                String userID=AppController.kinArrayShow.get(position).getUser_id();
                String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                KinModel model = new KinModel();
                model.setStatus(status);
                model.setRequest(request);
                model.setName(firstName.getText().toString().trim());
                model.setLast_name(lastName.getText().toString().trim());
                model.setEmail(emailKin.getText().toString().trim());
                model.setTitle(ContactSpinner.getText().toString());
                model.setKin_id(kinID);
                model.setRelationship(RelationalSpinner.getText().toString());
                model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                model.setUser_mobile(contactNumber.getText().toString());
                model.setStudent_id(studentID);
                model.setCreated_at(createdAT);
                model.setUpdated_at(updatedAT);
                model.setPhone(contactNumber.getText().toString());
                model.setId(IDs);
                model.setUser_id(userID);
                model.setFullFilled(true);
                // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                model.setCorrespondencemailmerge(corresspondence);
                model.setJustcontact(justcontact);
                model.setReportmailmerge(contacts);
                if (AppController.kinArrayShow.get(position).isNewData())
                {
                    model.setNewData(true);
                }
                else
                {
                    model.setNewData(false);
                }
                model.setConfirmed(false);
                for (int j=0;j<AppController.kinArrayPass.size();j++)
                {
                    String dataId=AppController.kinArrayPass.get(j).getId();
                    if (IDs.equalsIgnoreCase(dataId))
                    {
                        kinArrayPassPos=j;
                    }
                }
                AppController.kinArrayShow.remove(position);
                AppController.kinArrayPass.remove(kinArrayPassPos);
                AppController.kinArrayShow.add(position,model);
                AppController.kinArrayPass.add(kinArrayPassPos,model);
                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
            }
        });

        firstName.addTextChangedListener(new TextWatcher()
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
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;
                String status="";
                String request="";
                int kinArrayPassPos=-1;
                if (!AppController.kinArrayShow.get(position).isNewData())
                {
                    if (AppController.isKinEdited)
                    {
                        status="1";
                        request="0";
                    }
                    else
                    {
                        status="5";
                        request="0";
                    }
                }
                else
                {
                    if (AppController.isKinEdited)
                    {
                        status="0";
                        request="1";
                    }
                    else
                    {
                        status="0";
                        request="1";
                    }
                }
                String kinID=AppController.kinArrayShow.get(position).getKin_id();
                String IDs=AppController.kinArrayShow.get(position).getId();
                String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                String userID=AppController.kinArrayShow.get(position).getUser_id();
                String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                KinModel model = new KinModel();
                model.setStatus(status);
                model.setRequest(request);
                model.setName(firstName.getText().toString().trim());
                model.setLast_name(lastName.getText().toString().trim());
                model.setEmail(emailKin.getText().toString().trim());
                model.setTitle(ContactSpinner.getText().toString());
                model.setKin_id(kinID);
                model.setRelationship(RelationalSpinner.getText().toString());
                model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                model.setUser_mobile(contactNumber.getText().toString());
                model.setStudent_id(studentID);
                model.setCreated_at(createdAT);
                model.setUpdated_at(updatedAT);
                model.setPhone(contactNumber.getText().toString());
                model.setId(IDs);
                model.setUser_id(userID);
                model.setFullFilled(false);
                // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                model.setCorrespondencemailmerge(corresspondence);
                model.setJustcontact(justcontact);
                model.setReportmailmerge(contacts);
                if (AppController.kinArrayShow.get(position).isNewData())
                {
                    model.setNewData(true);
                }
                else
                {
                    model.setNewData(false);
                }
                model.setConfirmed(false);
                for (int j=0;j<AppController.kinArrayPass.size();j++)
                {
                    String dataId=AppController.kinArrayPass.get(j).getId();
                    if (IDs.equalsIgnoreCase(dataId))
                    {
                        kinArrayPassPos=j;
                    }
                }
                AppController.kinArrayShow.remove(position);
                AppController.kinArrayPass.remove(kinArrayPassPos);
                AppController.kinArrayShow.add(position,model);
                AppController.kinArrayPass.add(kinArrayPassPos,model);
                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);

            }


        });

        lastName.addTextChangedListener(new TextWatcher() {

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
                AppController.isKinEdited=true;
                String status="";
                String request="";
                int kinArrayPassPos=-1;
                if (!AppController.kinArrayShow.get(position).isNewData())
                {
                    if (AppController.isKinEdited)
                    {
                        status="1";
                        request="0";
                    }
                    else
                    {
                        status="5";
                        request="0";
                    }
                }
                else
                {
                    if (AppController.isKinEdited)
                    {
                        status="0";
                        request="1";
                    }
                    else
                    {
                        status="0";
                        request="1";
                    }
                }
                String kinID=AppController.kinArrayShow.get(position).getKin_id();
                String IDs=AppController.kinArrayShow.get(position).getId();
                String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                String userID=AppController.kinArrayShow.get(position).getUser_id();
                String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                KinModel model = new KinModel();
                model.setStatus(status);
                model.setRequest(request);
                model.setName(firstName.getText().toString().trim());
                model.setLast_name(lastName.getText().toString().trim());
                model.setEmail(emailKin.getText().toString().trim());
                model.setTitle(ContactSpinner.getText().toString());
                model.setKin_id(kinID);
                model.setRelationship(RelationalSpinner.getText().toString());
                model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                model.setUser_mobile(contactNumber.getText().toString());
                model.setStudent_id(studentID);
                model.setCreated_at(createdAT);
                model.setUpdated_at(updatedAT);
                model.setPhone(contactNumber.getText().toString());
                model.setId(IDs);
                model.setUser_id(userID);
                model.setFullFilled(false);
                // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                model.setCorrespondencemailmerge(corresspondence);
                model.setJustcontact(justcontact);
                model.setReportmailmerge(contacts);
                if (AppController.kinArrayShow.get(position).isNewData())
                {
                    model.setNewData(true);
                }
                else
                {
                    model.setNewData(false);
                }
                model.setConfirmed(false);
                for (int j=0;j<AppController.kinArrayPass.size();j++)
                {
                    String dataId=AppController.kinArrayPass.get(j).getId();
                    if (IDs.equalsIgnoreCase(dataId))
                    {
                        kinArrayPassPos=j;
                    }
                }
                AppController.kinArrayShow.remove(position);
                AppController.kinArrayPass.remove(kinArrayPassPos);
                AppController.kinArrayShow.add(position,model);
                AppController.kinArrayPass.add(kinArrayPassPos,model);
                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
            }


        });

        emailKin.addTextChangedListener(new TextWatcher() {

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
                AppController.isKinEdited=true;
                String status="";
                String request="";
                int kinArrayPassPos=-1;
                if (!AppController.kinArrayShow.get(position).isNewData())
                {
                    if (AppController.isKinEdited)
                    {
                        status="1";
                        request="0";
                    }
                    else
                    {
                        status="5";
                        request="0";
                    }
                }
                else
                {
                    if (AppController.isKinEdited)
                    {
                        status="0";
                        request="1";
                    }
                    else
                    {
                        status="0";
                        request="1";
                    }
                }
                String kinID=AppController.kinArrayShow.get(position).getKin_id();
                String IDs=AppController.kinArrayShow.get(position).getId();
                String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                String userID=AppController.kinArrayShow.get(position).getUser_id();
               String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
               String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                KinModel model = new KinModel();
                model.setStatus(status);
                model.setRequest(request);
                model.setName(firstName.getText().toString().trim());
                model.setLast_name(lastName.getText().toString().trim());
                model.setEmail(emailKin.getText().toString().trim());
                model.setTitle(ContactSpinner.getText().toString());
                model.setKin_id(kinID);
                model.setRelationship(RelationalSpinner.getText().toString());
                model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                model.setUser_mobile(contactNumber.getText().toString());
                model.setStudent_id(studentID);
                model.setCreated_at(createdAT);
                model.setUpdated_at(updatedAT);
                model.setPhone(contactNumber.getText().toString());
                model.setId(IDs);
                model.setUser_id(userID);
                model.setFullFilled(false);
                // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                model.setCorrespondencemailmerge(corresspondence);
                model.setJustcontact(justcontact);
                model.setReportmailmerge(contacts);
                if (AppController.kinArrayShow.get(position).isNewData())
                {
                    model.setNewData(true);
                }
                else
                {
                    model.setNewData(false);
                }
                model.setConfirmed(false);
                for (int j=0;j<AppController.kinArrayPass.size();j++)
                {
                    String dataId=AppController.kinArrayPass.get(j).getId();
                    if (IDs.equalsIgnoreCase(dataId))
                    {
                        kinArrayPassPos=j;
                    }
                }
                AppController.kinArrayShow.remove(position);
                AppController.kinArrayPass.remove(kinArrayPassPos);
                AppController.kinArrayShow.add(position,model);
                AppController.kinArrayPass.add(kinArrayPassPos,model);
                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);
            }


        });

        RelationalSpinner.addTextChangedListener(new TextWatcher() {

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
                if (!RelationalSpinner.getText().toString().equalsIgnoreCase("Driver") && !RelationalSpinner.getText().toString().equalsIgnoreCase("Domestic Helper"))
                {
                    emailKin.setHint(R.string.AST_EMAIL);
                }
                else
                {
                    emailKin.setHint(R.string.AST_EMAIL_NO);
                }
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;
                String status="";
                String request="";
                int kinArrayPassPos=-1;
                if (!AppController.kinArrayShow.get(position).isNewData())
                {
                    if (AppController.isKinEdited)
                    {
                        status="1";
                        request="0";
                    }
                    else
                    {
                        status="5";
                        request="0";
                    }
                }
                else
                {
                    if (AppController.isKinEdited)
                    {
                        status="0";
                        request="1";
                    }
                    else
                    {
                        status="0";
                        request="1";
                    }
                }
                String kinID=AppController.kinArrayShow.get(position).getKin_id();
                String IDs=AppController.kinArrayShow.get(position).getId();
                String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                String userID=AppController.kinArrayShow.get(position).getUser_id();
                String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                KinModel model = new KinModel();
                model.setStatus(status);
                model.setRequest(request);
                model.setName(firstName.getText().toString().trim());
                model.setLast_name(lastName.getText().toString().trim());
                model.setEmail(emailKin.getText().toString().trim());
                model.setTitle(ContactSpinner.getText().toString());
                model.setKin_id(kinID);
                model.setRelationship(RelationalSpinner.getText().toString());
                model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                model.setUser_mobile(contactNumber.getText().toString());
                model.setStudent_id(studentID);
                model.setCreated_at(createdAT);
                model.setUpdated_at(updatedAT);
                model.setPhone(contactNumber.getText().toString());
                model.setId(IDs);
                model.setUser_id(userID);
                model.setFullFilled(false);
                // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                model.setCorrespondencemailmerge(corresspondence);
                model.setJustcontact(justcontact);
                model.setReportmailmerge(contacts);
                if (AppController.kinArrayShow.get(position).isNewData())
                {
                    model.setNewData(true);
                }
                else
                {
                    model.setNewData(false);
                }
                model.setConfirmed(false);
                for (int j=0;j<AppController.kinArrayPass.size();j++)
                {
                    String dataId=AppController.kinArrayPass.get(j).getId();
                    if (IDs.equalsIgnoreCase(dataId))
                    {
                        kinArrayPassPos=j;
                    }
                }
                AppController.kinArrayShow.remove(position);
                AppController.kinArrayPass.remove(kinArrayPassPos);
                AppController.kinArrayShow.add(position,model);
                AppController.kinArrayPass.add(kinArrayPassPos,model);
                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);

            }


        });
        contactNumber.addTextChangedListener(new TextWatcher()
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
                Log.e("afterTextChanged: ", "name Chaange");
                AppController.isKinEdited=true;
                String status="";
                String request="";
                int kinArrayPassPos=-1;
                if (!AppController.kinArrayShow.get(position).isNewData())
                {
                    if (AppController.isKinEdited)
                    {
                        status="1";
                        request="0";
                    }
                    else
                    {
                        status="5";
                        request="0";
                    }
                }
                else
                {
                    if (AppController.isKinEdited)
                    {
                        status="0";
                        request="1";
                    }
                    else
                    {
                        status="0";
                        request="1";
                    }
                }
                String kinID=AppController.kinArrayShow.get(position).getKin_id();
                String IDs=AppController.kinArrayShow.get(position).getId();
                String studentID=AppController.kinArrayShow.get(position).getStudent_id();
                String createdAT=AppController.kinArrayShow.get(position).getCreated_at();
                String updatedAT=AppController.kinArrayShow.get(position).getUpdated_at();
                String userID=AppController.kinArrayShow.get(position).getUser_id();
                String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                String justcontact=AppController.kinArrayShow.get(position).getJustcontact();
                String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                KinModel model = new KinModel();
                model.setStatus(status);
                model.setRequest(request);
                model.setName(firstName.getText().toString().trim());
                model.setLast_name(lastName.getText().toString().trim());
                model.setEmail(emailKin.getText().toString().trim());
                model.setTitle(ContactSpinner.getText().toString());
                model.setKin_id(kinID);
                model.setRelationship(RelationalSpinner.getText().toString());
                model.setCode(countryCode.getTextView_selectedCountry().getText().toString());
                model.setUser_mobile(contactNumber.getText().toString());
                model.setStudent_id(studentID);
                model.setCreated_at(createdAT);
                model.setUpdated_at(updatedAT);
                model.setPhone(contactNumber.getText().toString());
                model.setId(IDs);
                model.setUser_id(userID);
                model.setFullFilled(false);
                // String corresspondence=AppController.kinArrayShow.get(position).getCorrespondencemailmerge();
                //String contacts=AppController.kinArrayShow.get(position).getReportmailmerge();
                model.setCorrespondencemailmerge(corresspondence);
                model.setJustcontact(justcontact);
                model.setReportmailmerge(contacts);
                if (AppController.kinArrayShow.get(position).isNewData())
                {
                    model.setNewData(true);
                }
                else
                {
                    model.setNewData(false);
                }
                model.setConfirmed(false);
                for (int j=0;j<AppController.kinArrayPass.size();j++)
                {
                    String dataId=AppController.kinArrayPass.get(j).getId();
                    if (IDs.equalsIgnoreCase(dataId))
                    {
                        kinArrayPassPos=j;
                    }
                }
                AppController.kinArrayShow.remove(position);
                AppController.kinArrayPass.remove(kinArrayPassPos);
                AppController.kinArrayShow.add(position,model);
                AppController.kinArrayPass.add(kinArrayPassPos,model);
                PreferenceManager.getKinDetailsArrayList(mContext).clear();
                PreferenceManager.getKinDetailsArrayListShow(mContext).clear();
                PreferenceManager.saveKinDetailsArrayListShow(AppController.kinArrayShow,mContext);
                PreferenceManager.saveKinDetailsArrayList(AppController.kinArrayPass,mContext);

            }


        });
    }
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



            }
        });

        dialog.show();
    }
}