package com.mobatia.bskl.activity.notification;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.notification.adapter.StudentRecyclerAdapter;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.URLConstants;

/**
 * Created by krishnaraj on 17/07/18.
 */

public class MessageDetail extends AppCompatActivity implements URLConstants, JSONConstants,NaisClassNameConstants {
    Bundle extras;
    private Context mContext = this;
    private String mLoadUrl = null;
    private String message="";
    String tab_type;
    private String date="";
    private String title="";
    TextView msgTitle;
    TextView msgDate;
    TextView mWebView;
    int position;
    RecyclerView studentRecycle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_detail_desc);
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("message");
            date=extras.getString("date");
            message=extras.getString("title");
            position=extras.getInt("position");
        }
        mContext = this;

        initUI();

    }
    private void initUI()
    {
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
        headerTitle.setText("Messages");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        action_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        msgDate= findViewById(R.id.msgDate);
        msgTitle= findViewById(R.id.msgTitle);
        studentRecycle= findViewById(R.id.studentRecycle);
        msgDate.setText(AppController.mMessageReadList.get(position).getDay());
        msgTitle.setText(AppController.mMessageReadList.get(position).getTitle());
        studentRecycle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        studentRecycle.setLayoutManager(llm);
        StudentRecyclerAdapter mStudentRecyclerAdapter= new StudentRecyclerAdapter(mContext,AppController.mMessageReadList);
        studentRecycle.setAdapter(mStudentRecyclerAdapter);


    }
}
