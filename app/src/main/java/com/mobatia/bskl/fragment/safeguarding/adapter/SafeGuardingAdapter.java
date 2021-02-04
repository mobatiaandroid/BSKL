package com.mobatia.bskl.fragment.safeguarding.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.absence.LeaveRequestSubmissionSafeguardActivity;
import com.mobatia.bskl.fragment.absence.model.StudentModel;
import com.mobatia.bskl.fragment.safeguarding.SafeGuardingFragment;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Rijo 02-04-2019
 */
public class SafeGuardingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StudentModel> dataSet;
    Context mContext;
    String details = "";
    int mHour,mMinute;

    public static class PresentType1ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewStudentName;
        TextView textViewStudentYear;
        TextView registrationcomment;
        TextView textViewAMRegisterValue;
        TextView textViewLastUpdatedValue;
        TextView comment;
        TextView commentdot;
//        View viewBottom;
        View viewTop;
        ImageView imgStudentImage;
        RelativeLayout relativeBg;
        ConstraintLayout mainConstraint;


        public PresentType1ViewHolder(View itemView) {
            super(itemView);

            this.mainConstraint =  itemView.findViewById(R.id.mainConstraint);
            this.relativeBg =  itemView.findViewById(R.id.relativeBg);
            this.registrationcomment = (TextView) itemView.findViewById(R.id.commentvalue);
            this.comment = (TextView) itemView.findViewById(R.id.comment);
            this.commentdot = (TextView) itemView.findViewById(R.id.commentdot);
            this.textViewStudentName = (TextView) itemView.findViewById(R.id.textViewStudentName);
            this.textViewStudentYear = (TextView) itemView.findViewById(R.id.textViewStudentYear);
            this.textViewAMRegisterValue = (TextView) itemView.findViewById(R.id.textViewAMRegisterValue);
            this.textViewLastUpdatedValue = (TextView) itemView.findViewById(R.id.textViewLastUpdatedValue);
//            this.viewBottom = (View) itemView.findViewById(R.id.viewBottom);
            this.viewTop = (View) itemView.findViewById(R.id.viewTop);
            this.imgStudentImage = (ImageView) itemView.findViewById(R.id.iconImg);
           /* if (registrationcomment.getText().toString().trim().equals("")) {
                registrationcomment.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                commentdot.setVisibility(View.VISIBLE);
            }
            else{
                registrationcomment.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
                commentdot.setVisibility(View.GONE);
            }*/

        }

    }

    public static class ReportType2ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStudentName;
        TextView textViewStudentYear;
        TextView textViewAMRegisterValue;
        TextView textViewLastUpdatedValue;
        TextView textViewReportStatus;
        TextView textViewReportedTime;
        ImageView imgStudentImage;
//        View viewBottom;
        View viewTop;
        RelativeLayout relativeBg;
        ConstraintLayout mainConstraint;
        TextView comment;
        TextView commentdot;
        TextView commentvalue;

        public ReportType2ViewHolder(View itemView) {
            super(itemView);
            this.mainConstraint =  itemView.findViewById(R.id.mainConstraint);

            this.relativeBg =  itemView.findViewById(R.id.relativeBg);
            this.textViewStudentName = (TextView) itemView.findViewById(R.id.textViewStudentName);
            this.comment = (TextView) itemView.findViewById(R.id.comment);
            this.commentdot = (TextView) itemView.findViewById(R.id.commentdot);
            this.commentvalue = (TextView) itemView.findViewById(R.id.commentvalue);
            this.textViewStudentYear = (TextView) itemView.findViewById(R.id.textViewStudentYear);
            this.textViewAMRegisterValue = (TextView) itemView.findViewById(R.id.textViewAMRegisterValue);
            this.textViewLastUpdatedValue = (TextView) itemView.findViewById(R.id.textViewLastUpdatedValue);
//            this.viewBottom = (View) itemView.findViewById(R.id.viewBottom);
            this.viewTop = (View) itemView.findViewById(R.id.viewTop);
            this.textViewReportStatus = (TextView) itemView.findViewById(R.id.textViewReportStatus);
            this.textViewReportedTime = (TextView) itemView.findViewById(R.id.textViewReportedTime);
            this.imgStudentImage = (ImageView) itemView.findViewById(R.id.iconImg);
           /* if (commentvalue.getText().toString().trim().equals("")) {
                commentvalue.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                commentdot.setVisibility(View.VISIBLE);
            }
            else{
                commentvalue.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
                commentdot.setVisibility(View.GONE);
            }*/

        }

    }

    public static class ReportedType3ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStudentName;
        TextView textViewStudentYear;
        TextView textViewAMRegisterValue;
        TextView textViewLastUpdatedValue;
        TextView textViewReportAbsence;
        TextView textViewStudentLate;
        TextView textViewStudentSent;
        ImageView imgStudentImage;
        TextView commentthree;
        TextView comment;
        TextView commentdot;
//        View viewBottom;
        View viewTop;
        RelativeLayout relativeBg;
        ConstraintLayout mainConstraint;

        public ReportedType3ViewHolder(View itemView) {
            super(itemView);
            this.mainConstraint =  itemView.findViewById(R.id.mainConstraint);
            this.relativeBg =  itemView.findViewById(R.id.relativeBg);
            this.textViewStudentName = (TextView) itemView.findViewById(R.id.textViewStudentName);
            this.commentthree = (TextView) itemView.findViewById(R.id.commentvaluesec);
            this.comment = (TextView) itemView.findViewById(R.id.comment);
            this.commentdot = (TextView) itemView.findViewById(R.id.commentdot);
            this.textViewStudentYear = (TextView) itemView.findViewById(R.id.textViewStudentYear);
            this.textViewAMRegisterValue = (TextView) itemView.findViewById(R.id.textViewAMRegisterValue);
            this.textViewLastUpdatedValue = (TextView) itemView.findViewById(R.id.textViewLastUpdatedValue);
//            this.viewBottom = (View) itemView.findViewById(R.id.viewBottom);
            this.viewTop = (View) itemView.findViewById(R.id.viewTop);
            this.textViewReportAbsence = (TextView) itemView.findViewById(R.id.textViewReportAbsence);
            this.textViewStudentLate = (TextView) itemView.findViewById(R.id.textViewStudentLate);
            this.textViewStudentSent = (TextView) itemView.findViewById(R.id.textViewStudentSent);
            this.imgStudentImage = (ImageView) itemView.findViewById(R.id.iconImg);

      /*      if (commentthree.getText().toString().trim().equals("")) {
                commentthree.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
                commentdot.setVisibility(View.GONE);
            }
            else{
                commentthree.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                commentdot.setVisibility(View.VISIBLE);
            }*/


        }


    }

    public SafeGuardingAdapter(ArrayList<StudentModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("viewType:",""+viewType);

        switch (viewType) {
            case StudentModel.VIEW_TYPE1:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_safe_guarding_type1, parent, false);
                int height = parent.getMeasuredHeight();
                view1.setMinimumHeight(height);
                return new PresentType1ViewHolder(view1);
            case StudentModel.VIEW_TYPE2:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_safe_guarding_type2, parent, false);
                int height2 = parent.getMeasuredHeight();
                view2.setMinimumHeight(height2);
                return new ReportType2ViewHolder(view2);
            case StudentModel.VIEW_TYPE3:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_safe_guarding_type3, parent, false);
                int height3 = parent.getMeasuredHeight();
                view3.setMinimumHeight(height3);
                return new ReportedType3ViewHolder(view3);
        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {
        return  Integer.parseInt(dataSet.get(position).getViewType());
//        switch (Integer.parseInt(dataSet.get(position).getViewType())) {
//            case 1:
//                return StudentModel.VIEW_TYPE1;
//            case 2:
//                return StudentModel.VIEW_TYPE2;
//            case 3:
//                return StudentModel.VIEW_TYPE3;
//            case 4:
//                return StudentModel.VIEW_TYPE4;
//            default:
//                return -1;
//        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final StudentModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (Integer.parseInt(object.getViewType())) {
                case StudentModel.VIEW_TYPE1:
                    ((PresentType1ViewHolder) holder).textViewStudentName.setText(object.getmName());
                    ((PresentType1ViewHolder) holder).registrationcomment.setText(object.getRegistrationComment());

                    ((PresentType1ViewHolder) holder).textViewStudentYear.setText(object.getmClass() + " " + object.getmSection());
                    ((PresentType1ViewHolder) holder).textViewAMRegisterValue.setText(object.getAbRegister());
                    ((PresentType1ViewHolder) holder).textViewLastUpdatedValue.setText(AppUtils.dateParsingToDdMmmYyyy(object.getDate()));
                    if (!object.getmPhoto().equals("")) {

                        Picasso.with(mContext).load(AppUtils.replace(object.getmPhoto())).placeholder(R.drawable.boy).fit().into(((PresentType1ViewHolder) holder).imgStudentImage);
                    } else

                    {

                        ((PresentType1ViewHolder) holder).imgStudentImage.setImageResource(R.drawable.boy);
                    }
//                    if (listPosition == (dataSet.size() - 1)) {
//                        ((PresentType1ViewHolder) holder).viewBottom.setVisibility(View.VISIBLE);
//                    } else {
//                        ((PresentType1ViewHolder) holder).viewBottom.setVisibility(View.GONE);
//
//                    }
                    if (object.getRegistrationComment().equalsIgnoreCase(""))
                    {
                        ((PresentType1ViewHolder) holder).registrationcomment.setVisibility(View.GONE);
                        ((PresentType1ViewHolder) holder).comment.setVisibility(View.GONE);
                        ((PresentType1ViewHolder) holder).commentdot.setVisibility(View.GONE);
                    }
                    else {
                        ((PresentType1ViewHolder) holder).registrationcomment.setVisibility(View.VISIBLE);
                        ((PresentType1ViewHolder) holder).comment.setVisibility(View.VISIBLE);
                        ((PresentType1ViewHolder) holder).commentdot.setVisibility(View.VISIBLE);
                    }
                    break;
                case StudentModel.VIEW_TYPE2:

                    ((ReportType2ViewHolder) holder).textViewStudentName.setText(object.getmName());
                    ((ReportType2ViewHolder) holder).commentvalue.setText(object.getRegistrationComment());
                    ((ReportType2ViewHolder) holder).textViewStudentYear.setText(object.getmClass() + " " + object.getmSection());
                    ((ReportType2ViewHolder) holder).textViewAMRegisterValue.setText(object.getAbRegister());
                    ((ReportType2ViewHolder) holder).textViewLastUpdatedValue.setText(AppUtils.dateParsingToDdMmmYyyy(object.getDate()));
                    if (object.getStatus().equalsIgnoreCase("1")) {
                        ((ReportType2ViewHolder) holder).textViewReportStatus.setText("STUDENT \nRUNNING LATE");
                        ((ReportType2ViewHolder) holder).textViewReportStatus.setBackgroundResource(R.drawable.right_open_rounded_rect_red);
                        ((ReportType2ViewHolder) holder).textViewReportedTime.setBackgroundResource(R.drawable.left_open_rounded_rect_red);
                        if (object.getParent_id().equalsIgnoreCase(PreferenceManager.getUserId(mContext)))
                        {
                            ((ReportType2ViewHolder) holder).textViewReportedTime.setText("REPORTED BY YOU AT : " + AppUtils.dateParsingToTime(object.getApp_updated_on()));

                        }
                        else
                        {
                            ((ReportType2ViewHolder) holder).textViewReportedTime.setText("REPORTED BY "+object.getParent_name()+" AT : " + AppUtils.dateParsingToTime(object.getApp_updated_on()));

                        }

                    } else if (object.getStatus().equalsIgnoreCase("2")) {
                        ((ReportType2ViewHolder) holder).textViewReportStatus.setText("STUDENT HAS \nBEEN SENT TO SCHOOL");
                        ((ReportType2ViewHolder) holder).textViewReportStatus.setBackgroundResource(R.drawable.right_open_rounded_rect_violet);
                        ((ReportType2ViewHolder) holder).textViewReportedTime.setBackgroundResource(R.drawable.left_open_rounded_rect_violet);
                        if (object.getParent_id().equalsIgnoreCase(PreferenceManager.getUserId(mContext)))
                        {
                            ((ReportType2ViewHolder) holder).textViewReportedTime.setText("REPORTED BY YOU AT : " + AppUtils.dateParsingToTime(object.getApp_updated_on()));

                        }
                        else
                        {
                            ((ReportType2ViewHolder) holder).textViewReportedTime.setText("REPORTED BY "+object.getParent_name()+" AT : " + AppUtils.dateParsingToTime(object.getApp_updated_on()));

                        }
                    } else if (object.getStatus().equalsIgnoreCase("3")) {
                        ((ReportType2ViewHolder) holder).textViewReportStatus.setText("REPORT ABSENCE");
                        ((ReportType2ViewHolder) holder).textViewReportStatus.setBackgroundResource(R.drawable.right_open_rounded_rect_blue);
                        ((ReportType2ViewHolder) holder).textViewReportedTime.setBackgroundResource(R.drawable.left_open_rounded_rect_blue);
                        if (object.getParent_id().equalsIgnoreCase(PreferenceManager.getUserId(mContext)))
                        {
                            ((ReportType2ViewHolder) holder).textViewReportedTime.setText("REPORTED BY YOU AT : " + AppUtils.dateParsingToTime(object.getApp_updated_on()));

                        }
                        else
                        {
                            ((ReportType2ViewHolder) holder).textViewReportedTime.setText("REPORTED BY "+object.getParent_name()+" AT : " + AppUtils.dateParsingToTime(object.getApp_updated_on()));

                        }
                    }

                    if (!object.getmPhoto().equals("")) {

                        Picasso.with(mContext).load(AppUtils.replace(object.getmPhoto())).placeholder(R.drawable.boy).fit().into(((ReportType2ViewHolder) holder).imgStudentImage);
                    } else

                    {

                        ((ReportType2ViewHolder) holder).imgStudentImage.setImageResource(R.drawable.boy);
                    }
//                    if (listPosition == (dataSet.size() - 1)) {
//                        ((ReportType2ViewHolder) holder).viewBottom.setVisibility(View.VISIBLE);
//                    } else {
//                        ((ReportType2ViewHolder) holder).viewBottom.setVisibility(View.GONE);
//
//                    }
                    if (object.getRegistrationComment().equalsIgnoreCase(""))
                    {
                        ((ReportType2ViewHolder) holder).commentvalue.setVisibility(View.GONE);
                        ((ReportType2ViewHolder) holder).comment.setVisibility(View.GONE);
                        ((ReportType2ViewHolder) holder).commentdot.setVisibility(View.GONE);
                    }
                    else {
                        ((ReportType2ViewHolder) holder).commentvalue.setVisibility(View.VISIBLE);
                        ((ReportType2ViewHolder) holder).comment.setVisibility(View.VISIBLE);
                        ((ReportType2ViewHolder) holder).commentdot.setVisibility(View.VISIBLE);
                    }
                    break;
                case StudentModel.VIEW_TYPE3:

                    ((ReportedType3ViewHolder) holder).textViewStudentName.setText(object.getmName());
                    ((ReportedType3ViewHolder) holder).commentthree.setText(object.getRegistrationComment());
                    ((ReportedType3ViewHolder) holder).textViewStudentYear.setText(object.getmClass() + " " + object.getmSection());
                    ((ReportedType3ViewHolder) holder).textViewAMRegisterValue.setText(object.getAbRegister());
                    ((ReportedType3ViewHolder) holder).textViewLastUpdatedValue.setText(AppUtils.dateParsingToDdMmmYyyy(object.getDate()));
                    if (!object.getmPhoto().equals("")) {

                        Picasso.with(mContext).load(AppUtils.replace(object.getmPhoto())).placeholder(R.drawable.boy).fit().into(((ReportedType3ViewHolder) holder).imgStudentImage);
                    } else

                    {

                        ((ReportedType3ViewHolder) holder).imgStudentImage.setImageResource(R.drawable.boy);
                    }
                    if (object.getRegistrationComment().equalsIgnoreCase(""))
                    {
                        ((ReportedType3ViewHolder) holder).commentthree.setVisibility(View.GONE);
                        ((ReportedType3ViewHolder) holder).comment.setVisibility(View.GONE);
                        ((ReportedType3ViewHolder) holder).commentdot.setVisibility(View.GONE);
                    }
                    else {
                        ((ReportedType3ViewHolder) holder).commentthree.setVisibility(View.VISIBLE);
                        ((ReportedType3ViewHolder) holder).comment.setVisibility(View.VISIBLE);
                        ((ReportedType3ViewHolder) holder).commentdot.setVisibility(View.VISIBLE);
                    }
//                    if (listPosition == (dataSet.size() - 1)) {
//                        ((ReportedType3ViewHolder) holder).viewBottom.setVisibility(View.VISIBLE);
//                    } else {
//                        ((ReportedType3ViewHolder) holder).viewBottom.setVisibility(View.GONE);
//
//                    }
                    ((ReportedType3ViewHolder) holder).textViewReportAbsence.setGravity(Gravity.CENTER);
                    ((ReportedType3ViewHolder) holder).textViewStudentLate.setGravity(Gravity.CENTER);
                    ((ReportedType3ViewHolder) holder).textViewStudentSent.setGravity(Gravity.CENTER);
                    ((ReportedType3ViewHolder) holder).textViewReportAbsence.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent mIntent = new Intent(mContext, LeaveRequestSubmissionSafeguardActivity.class);
                            mIntent.putExtra("studentName", object.getmName());
                            mIntent.putExtra("studentImage", object.getmPhoto());
                            mIntent.putExtra("studentId", object.getmId());
                            mIntent.putExtra("attendance_id", object.getAbscenceId());
                            mIntent.putExtra("status", "3");
                            mIntent.putExtra("StudentModelArray", dataSet);
                            mContext.startActivity(mIntent);
                        }
                    });
                    ((ReportedType3ViewHolder) holder).textViewStudentLate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogStudentLate(mContext, "1", object.getAbscenceId(), object.getmId());
                        }
                    });

                    ((ReportedType3ViewHolder) holder).textViewStudentSent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogStudentSent(mContext, "2", object.getAbscenceId(),object.getmId());


                        }
                    });


                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void dialogStudentLate(final Context context, final String status, final String attendance_id,final String student_id) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_safe_guarding_student_late);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final EditText textViewComments = dialog.findViewById(R.id.textViewComments);
        final TextView textViewTime = dialog.findViewById(R.id.textViewTime);
        details = "";
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                if (textViewTime.getText().toString().equalsIgnoreCase("")) {
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                }
                else
                {
                    mHour = Integer.parseInt(AppUtils.timeParsingToHours(textViewTime.getText().toString()));
                    mMinute = Integer.parseInt(AppUtils.timeParsingToMinutes(textViewTime.getText().toString()));
                }
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                textViewTime.setText(AppUtils.timeParsingTo12Hour(hourOfDay + ":" + minute));
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if (AppUtils.isNetworkConnected(mContext)) {
                    if (!(textViewTime.getText().toString().equalsIgnoreCase("")) && !(textViewComments.getText().toString().equalsIgnoreCase(""))) {
                        details = "Expected arrival time - "+textViewTime.getText().toString() + " "+"<br>" + textViewComments.getText().toString();

                        SafeGuardingFragment.submitAbsence(attendance_id,student_id, status, details);
                        dialog.dismiss();
                    }else if (textViewTime.getText().toString().equalsIgnoreCase(""))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Please select time.", R.drawable.exclamationicon, R.drawable.round);

                    }else if (textViewComments.getText().toString().equalsIgnoreCase(""))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Please enter your comments.", R.drawable.exclamationicon, R.drawable.round);

                    }
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }

            }

        });

        dialog.show();
    }

    public void dialogStudentSent(Context context, final String status, final String attendance_id,final String student_id) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_safe_guarding_student_sent);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final EditText textViewComments = dialog.findViewById(R.id.textViewComments);
        details = "";

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (checkedId == R.id.radioButton1) {
                    details = rb.getText().toString();
                    textViewComments.setVisibility(View.GONE);

                } else if (checkedId == R.id.radioButton2) {
                    details = rb.getText().toString();
                    textViewComments.setVisibility(View.GONE);

                } else if (checkedId == R.id.radioButton3) {
                    details = rb.getText().toString();
                    textViewComments.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioButton4) {
                    details = "";
                    textViewComments.setText("");
                    textViewComments.setVisibility(View.VISIBLE);

                } else {
                    textViewComments.setVisibility(View.GONE);

                }


            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if (AppUtils.isNetworkConnected(mContext)) {

                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton4) {
                        details = textViewComments.getText().toString();
                        if (details.trim().equalsIgnoreCase(""))
                        {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Please enter your comments.", R.drawable.exclamationicon, R.drawable.round);

                        }
                        else
                        {
                            SafeGuardingFragment.submitAbsence(attendance_id,student_id, status, details);
                            dialog.dismiss();
                        }
                    }
                    else if (details.equalsIgnoreCase(""))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Please select any of the options.", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        SafeGuardingFragment.submitAbsence(attendance_id,student_id, status, details);
                        dialog.dismiss();
                    }

                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getResources().getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }

        });

        dialog.show();
    }

    @Override
    public long getItemId(int position) {
        return  Integer.parseInt(dataSet.get(position).getViewType());

//        switch (Integer.parseInt(dataSet.get(position).getViewType())) {
//            case 1:
//                return StudentModel.VIEW_TYPE1;
//            case 2:
//                return StudentModel.VIEW_TYPE2;
//            case 3:
//                return StudentModel.VIEW_TYPE3;
//            case 4:
//                return StudentModel.VIEW_TYPE4;
//            default:
//                return -1;
//        }
    }



}
