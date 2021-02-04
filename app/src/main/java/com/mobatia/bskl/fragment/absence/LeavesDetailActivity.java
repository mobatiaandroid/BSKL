package com.mobatia.bskl.fragment.absence;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;

import java.util.Calendar;

/**
 * Created by krishnaraj on 27/08/18.
 */

public class LeavesDetailActivity extends AppCompatActivity implements NaisClassNameConstants {
    ImageView back;
    ImageView home;
    Context mContext = this;
    LinearLayout fromlayout,reasonlayout;
    TextView stnameValue,leaveDateFromValue,leaveDateToValue,reasonValue,studClassValue,studStaffValue;
    Bundle extras;
    int position;
    String studentNameStr="";
    String studentClassStr="";
    String fromDate="";
    String toDate="";
    String reasonForAbsence="";
    String staff="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leave_detailpage);
        initUI();
        setValues();
    }
    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            studentNameStr=extras.getString("studentName");
            studentClassStr=extras.getString("studentClass");
            fromDate=extras.getString("fromDate");
            toDate=extras.getString("toDate");
            reasonForAbsence=extras.getString("reasonForAbsence");
            staff=extras.getString("staff");
        }
        fromlayout= findViewById(R.id.fromlayout);
        reasonlayout= findViewById(R.id.reasonlayout);
        stnameValue= findViewById(R.id.stnameValue);
        studClassValue= findViewById(R.id.studClassValue);
        leaveDateFromValue= findViewById(R.id.leaveDateFromValue);
        leaveDateToValue= findViewById(R.id.leaveDateToValue);
        reasonValue= findViewById(R.id.reasonValue);
        studStaffValue= findViewById(R.id.studStaffValue);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();
        Toolbar toolbar=(Toolbar)view.getParent();
        toolbar.setContentInsetsAbsolute(0,0);
        TextView headerTitle = findViewById(R.id.headerTitle);
        ImageView logoClickImgView = findViewById(R.id.logoClickImgView);
        ImageView action_bar_forward = findViewById(R.id.action_bar_forward);
        ImageView action_bar_back = findViewById(R.id.action_bar_back);
        action_bar_back.setImageResource(R.drawable.back_new);
        action_bar_forward.setVisibility(View.INVISIBLE);
        headerTitle.setText("Absence");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void setValues(){
        stnameValue.setText(studentNameStr);
        studClassValue.setText(studentClassStr);
        leaveDateFromValue.setText(AppUtils.dateConversionY(fromDate));
        leaveDateToValue.setText(AppUtils.dateConversionY(toDate));
        reasonValue.setText(reasonForAbsence);
        studStaffValue.setText(staff);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Leave Detail Screen."+"("+ PreferenceManager.getUserEmail(mContext)+")"+" "+"("+ Calendar.getInstance().getTime()+")");

        }

    }
}
