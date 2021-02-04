package com.mobatia.bskl.fragment.calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.tutorial.adapter.TutorialViewPagerAdapter;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by krishnaraj on 30/08/18.
 */

public class CalendarInfoActivity extends Activity implements IntentPassValueConstants, NaisClassNameConstants, JSONConstants {
    private ImageView mImgCircle[];
    private LinearLayout mLinearLayout;
    ViewPager mTutorialViewPager;
    Context mContext;
    TutorialViewPagerAdapter mTutorialViewPagerAdapter;
    ArrayList<Integer> mPhotoList = new ArrayList<>(Arrays.asList(R.drawable.cal1, R.drawable.cal2, R.drawable.cal3, R.drawable.cal4, R.drawable.cal5));
    int dataType;
    ImageView imageSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dataType = bundle.getInt(TYPE, 0);

        }
        initialiseViewPagerUI();
    }

    /*******************************************************
     * Method name : initialiseViewPagerUI Description : initialise View pager
     * UI elements Parameters : nil Return type : void Date : Jan 13, 2015
     * Author : Rijo K Jose
     *****************************************************/
    private void initialiseViewPagerUI() {
        mTutorialViewPager = findViewById(R.id.tutorialViewPager);
        mLinearLayout = findViewById(R.id.linear);
        imageSkip = findViewById(R.id.imageSkip);

//        if (WissPreferenceManager.getUserName(mContext).equals("")) {
//            mPhotoList = new int[] { R.drawable.tut_1, R.drawable.tut_2,
//                    R.drawable.tut_3, R.drawable.tut_4, R.drawable.tut_5 };
//        } else {
//            mPhotoList = new int[] { R.drawable.tut_1, R.drawable.tut_2,
//                    R.drawable.tut_3, R.drawable.tut_4, R.drawable.tut_6 };
//        }
        mImgCircle = new ImageView[mPhotoList.size()];
        mTutorialViewPagerAdapter = new TutorialViewPagerAdapter(mContext, mPhotoList);
        mTutorialViewPager.setCurrentItem(0);
        mTutorialViewPager.setAdapter(mTutorialViewPagerAdapter);
        addShowCountView(0);
        imageSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mTutorialViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
//                for (int i = 0; i < mPhotoList.size(); i++) {
//                    mImgCircle[i].setBackgroundDrawable(getResources()
//                            .getDrawable(R.drawable.blackround));
//                }
//                if (position < mPhotoList.size()) {
//                    mImgCircle[position].setBackgroundDrawable(getResources()
//                            .getDrawable(R.drawable.redround));
//                    mLinearLayout.removeAllViews();
//                    addShowCountView(position);
//                } else {
////                    mLinearLayout.removeAllViews();
////                    finish();
//                    mLinearLayout.removeAllViews();
//                    if (dataType==0)
//                    {
//                        finish();
//
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(mContext, ParentsAssociationListActivity.class);
//                        intent.putExtra("tab_type", "Parents Association");
//                        mContext.startActivity(intent);
//                        finish();
//                    }
//
//                }
                for (int i = 0; i < mPhotoList.size(); i++) {
                    mImgCircle[i].setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.blackround));
                }
                if (position < mPhotoList.size()) {
                    mImgCircle[position].setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.redround));
                    mLinearLayout.removeAllViews();
                    addShowCountView(position);
                } else {
                    mLinearLayout.removeAllViews();
                    if (dataType == 0) {
                        overridePendingTransition(0, 0);

                        finish();

                    } else {

                        finish();
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mTutorialViewPager.getAdapter().notifyDataSetChanged();
    }

    /*******************************************************
     * Method name : addShowCountView Description : add show count view at
     * bottom Parameters : count Return type : void Date : Apr 17, 2015 Author :
     * Rijo K Jose
     *****************************************************/
    private void addShowCountView(int count) {
        for (int i = 0; i < mPhotoList.size(); i++) {
            mImgCircle[i] = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int) getResources()
                            .getDimension(R.dimen.home_circle_width),
                    (int) getResources().getDimension(
                            R.dimen.home_circle_height));
            mImgCircle[i].setLayoutParams(layoutParams);
            if (i == count) {
                mImgCircle[i].setBackgroundResource(R.drawable.redround);
            } else {
                mImgCircle[i].setBackgroundResource(R.drawable.blackround);
            }
            mLinearLayout.addView(mImgCircle[i]);
        }
    }

}
