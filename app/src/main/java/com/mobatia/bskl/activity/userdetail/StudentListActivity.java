package com.mobatia.bskl.activity.userdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.userdetail.adapter.StudentListRecyclerAdapter;
import com.mobatia.bskl.activity.userdetail.model.StudentUserModel;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.ResultConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by krishnaraj on 05/09/18.
 */

public class StudentListActivity extends AppCompatActivity implements JSONConstants,ResultConstants,StatusConstants,URLConstants ,IntentPassValueConstants {
    Context mContext=this;
    Context mActivity=this;
    ImageView back;
    ImageView home;
    Bundle extras;

    RecyclerView studentList;
    ArrayList<StudentUserModel> studentsModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.student_list_activity);
        extras = getIntent().getExtras();
         mContext=this;
         mActivity=this;
        if (extras != null) {
            studentsModelArrayList=new ArrayList<>();
            /*   mYoutubeModelArrayList = (ArrayList<YoutubeModel>) extras
                    .getSerializable("youtubeArray");*/
            studentsModelArrayList=(ArrayList<StudentUserModel>) extras.getSerializable("studentlist");

        }
        studentList = findViewById(R.id.studentList);
        studentList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        studentList.setLayoutManager(llm);
        StudentListRecyclerAdapter mStudentListRecyclerAdapter=new StudentListRecyclerAdapter(mContext,studentsModelArrayList);
        studentList.setAdapter(mStudentListRecyclerAdapter);

        try {
            initialiseUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void initialiseUI() throws ParseException {


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();
        Toolbar toolbar=(Toolbar)view.getParent();
        toolbar.setContentInsetsAbsolute(0,0);
        TextView headerTitle = view.findViewById(R.id.headerTitle);
        ImageView logoClickImgView = view.findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = view.findViewById(R.id.action_bar_forward);
        ImageView action_bar_back = view.findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        headerTitle.setText("Students");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_forward.setVisibility(View.INVISIBLE);

        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        studentList.addOnItemTouchListener(new RecyclerItemListener(mContext, studentList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (studentsModelArrayList.get(position).getAlumini().equalsIgnoreCase("0")) {
                            Intent mIntent = new Intent(mContext, StudentDetailActivity.class);
                            mIntent.putExtra("stud_id", studentsModelArrayList.get(position).getStud_id());
                            startActivity(mIntent);
                        }
                        else
                        {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Student List."+" "+"("+PreferenceManager.getUserEmail(mActivity)+")"+" "+"("+ Calendar.getInstance().getTime()+")");
        }


    }




}
