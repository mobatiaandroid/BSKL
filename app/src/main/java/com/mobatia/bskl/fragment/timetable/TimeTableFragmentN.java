package com.mobatia.bskl.fragment.timetable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.bskl.R;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.report.adapter.StudentSpinnerAdapter;
import com.mobatia.bskl.fragment.report.model.StudentModel;
import com.mobatia.bskl.fragment.timetable.adapter.TimeTableAllWeekSelectionAdapterNew;
import com.mobatia.bskl.fragment.timetable.adapter.TimeTableSingleWeekSelectionAdapter;
import com.mobatia.bskl.fragment.timetable.adapter.TimeTableWeekListAdapter;
import com.mobatia.bskl.fragment.timetable.model.DayModel;
import com.mobatia.bskl.fragment.timetable.model.FieldModel;
import com.mobatia.bskl.fragment.timetable.model.PeriodModel;
import com.mobatia.bskl.fragment.timetable.model.RangeModel;
import com.mobatia.bskl.fragment.timetable.model.TimeTableModel;
import com.mobatia.bskl.fragment.timetable.model.WeekModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;
import com.ryanharter.android.tooltips.ToolTipLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.piruin.quickaction.QuickAction;

public class TimeTableFragmentN extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

    RelativeLayout alertTxtRelative;
    RelativeLayout CCAFRegisterRel;
    ArrayList<StudentModel> studentsModelArrayList;
    ArrayList<StudentModel> studentsModelArrayListCopy;
    ListView studentDetail;
    TextView studentName;
    TextView textViewYear;
    String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img = "";
    String progressReport = "";
    String section = "";
    String alumini = "";
    String type = "";
    RelativeLayout noDataRelative;
    TextView noDataTxt;
    ImageView noDataImg;
    LinearLayout mStudentSpinner;
    ImageView back;
    ImageView home;
    ImageView studImg;
    Bundle extras;
    private View mRootView;
    private Context mContext;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    private RecyclerView timeTableSingleRecycler;
    private RecyclerView timeTableAllRecycler;
    private RecyclerView weekRecyclerList;
    private TextView alertText;
    private ArrayList<WeekModel> weekListArray;
    ArrayList<RangeModel> mRangeModel;
    ArrayList<DayModel>mFridayModelArraylist;
    ArrayList<PeriodModel> mPeriodModel;
    ArrayList<String> breakArrayList;
    ArrayList<FieldModel>mBreakArrayList;
    ArrayList<StudentModel>timeTableArrayList;
    ArrayList<StudentModel>primaryArrayList;
    ArrayList<StudentModel>secondaryArrayList;
    ArrayList<StudentModel>bothArrayList;
    CardView card_viewAll;
    ArrayList<FieldModel> mFieldModel;
    ArrayList<TimeTableModel> mTimeTableModelArrayList;
    int weekPosition = 0;
    TimeTableWeekListAdapter TimeTableWeekListAdapter;
    String dayOfTheWeek;
    ToolTipLayout tipContainer;

    public static QuickAction quickAction;
    public static QuickAction quickIntent;

    public TimeTableFragmentN() {

    }

    public TimeTableFragmentN(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    ArrayList<String> weekListArrayString = new ArrayList<String>() {
        {
            //add("Notices");
            add("ALL");
            add("MONDAY");
            add("TUESDAY");
            add("WEDNESDAY");
            add("THURSDAY");
            add("FRIDAY");
            //add("Events");
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.time_table_fragment, container,
                false);
        mContext = getActivity();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        headerTitle.setText("Student Timetable");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        initialiseUI();

        weekListArray = new ArrayList<>();
        for (int i = 0; i < weekListArrayString.size(); i++) {
            WeekModel mWeekModel = new WeekModel();
            //mWeekModel.setmId(String.valueOf(i));
            mWeekModel.setWeekName(weekListArrayString.get(i));
            mWeekModel.setPositionSelected(-1);
            weekListArray.add(mWeekModel);
            //mWeekModel.setmTitle(weekListArrayString.get(i));
           /* if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
                mRecyclerViewArray.add(mWeekModel);
            } else {
                if (i != 3) {
                    mRecyclerViewArray.add(mWeekModel);

                }
            }*/
        }
        TimeTableWeekListAdapter = new TimeTableWeekListAdapter(getActivity(), weekListArray, weekPosition);
        weekRecyclerList.setAdapter(TimeTableWeekListAdapter);
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialmediaList(studentsModelArrayList);
            }
        });
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }


        return mRootView;
    }


    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        if (extras != null) {
        }
//
        breakArrayList = new ArrayList<>();
        mRangeModel = new ArrayList<>();
        mFridayModelArraylist = new ArrayList<>();
        mPeriodModel = new ArrayList<>();
        timeTableArrayList=new ArrayList<>();
        primaryArrayList=new ArrayList<>();
        secondaryArrayList=new ArrayList<>();
        bothArrayList=new ArrayList<>();
        alertText = mRootView.findViewById(R.id.noDataTxt);
        mStudentSpinner = mRootView.findViewById(R.id.studentSpinner);
        studentName = mRootView.findViewById(R.id.studentName);
        studImg = mRootView.findViewById(R.id.imagicon);
        noDataImg = mRootView.findViewById(R.id.noDataImg);
        timeTableSingleRecycler = mRootView.findViewById(R.id.timeTableSingleRecycler);
        weekRecyclerList = mRootView.findViewById(R.id.weekRecyclerList);
        timeTableAllRecycler = mRootView.findViewById(R.id.timeTableAllRecycler);
        noDataRelative = mRootView.findViewById(R.id.noDataRelative);
        relMain = mRootView.findViewById(R.id.relMain);
        alertTxtRelative = mRootView.findViewById(R.id.noDataRelative);
        textViewYear = mRootView.findViewById(R.id.textViewYear);
        noDataTxt = mRootView.findViewById(R.id.noDataTxt);
        card_viewAll = mRootView.findViewById(R.id.card_viewAll);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        weekRecyclerList.setLayoutManager(llm);
        tipContainer = (ToolTipLayout) mRootView.findViewById(R.id.tooltip_container);
        LinearLayoutManager llmAll = new LinearLayoutManager(getActivity());
        llmAll.setOrientation(LinearLayoutManager.VERTICAL);
        timeTableAllRecycler.setLayoutManager(llmAll);
        /*LayoutInflater.from(mContext).inflate(R.layout.layout_timetable_popup, null);*/

//        timeTableAllRecycler.addOnItemTouchListener(new RecyclerItemListener(mContext, timeTableAllRecycler, new RecyclerItemListener.RecyclerTouchListener() {
//            @Override
//            public void onClickItem(View v, int position) {
//
//
//            }
//
//            @Override
//            public void onLongClickItem(View v, int position) {
//
//            }
//        }));
        LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
        llmm.setOrientation(LinearLayoutManager.VERTICAL);
        timeTableSingleRecycler.setLayoutManager(llmm);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        System.out.println("Current day" + dayOfTheWeek);
        switch (dayOfTheWeek) {
            case "Monday":
                weekPosition = 1;
                break;
            case "Tuesday":
                weekPosition = 2;
                break;
            case "Wednesday":
                weekPosition = 3;
                break;
            case "Thursday":
                weekPosition = 4;

                break;
            case "Friday":
                weekPosition = 5;
                break;
            default:
                weekPosition = 0;
                break;

        }



        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        weekRecyclerList.addOnItemTouchListener(new RecyclerItemListener(mContext, weekRecyclerList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        System.out.println("clicked position" + position);
                        weekPosition = position;
                        if (weekPosition<3) {
                            weekRecyclerList.scrollToPosition(0);
                        }
                        else
                        {
                            weekRecyclerList.scrollToPosition(5);

                        }

                        for (int i = 0; i < weekListArray.size(); i++) {
                            if (i == position) {
                                weekListArray.get(i).setPositionSelected(i);

                            } else {
                                weekListArray.get(i).setPositionSelected(-1);

                            }

                        }
                        TimeTableWeekListAdapter.notifyDataSetChanged();
                        if (position != 0) {
                            card_viewAll.setVisibility(View.GONE);
                            timeTableAllRecycler.setVisibility(View.GONE);
                            tipContainer.setVisibility(View.GONE);
                            timeTableSingleRecycler.setVisibility(View.VISIBLE);
                            if (mRangeModel.size() > 0) {
                                TimeTableSingleWeekSelectionAdapter mRecyclerViewMainAdapter = new TimeTableSingleWeekSelectionAdapter(mContext, mRangeModel.get(position - 1).getTimeTableDayModel());
                                timeTableSingleRecycler.setAdapter(mRecyclerViewMainAdapter);
                            }

                        } else {
                            timeTableSingleRecycler.setVisibility(View.GONE);
                            timeTableAllRecycler.setVisibility(View.VISIBLE);
                            tipContainer.setVisibility(View.VISIBLE);
                            card_viewAll.setVisibility(View.VISIBLE);
                            if (mPeriodModel.size()>0)
                            {
                                TimeTableAllWeekSelectionAdapterNew mRecyclerAllAdapter = new TimeTableAllWeekSelectionAdapterNew(mContext, mPeriodModel,timeTableAllRecycler,tipContainer,mFieldModel);
                            timeTableAllRecycler.setAdapter(mRecyclerAllAdapter);
                            }
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));

    }

    private StudentModel addStudentDetails(JSONObject dataObject) {
        StudentModel studentModel = new StudentModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));
        studentModel.setProgressReport(dataObject.optString("progressreport"));
        studentModel.setAlumini(dataObject.optString("alumi"));
        studentModel.setType(dataObject.optString("type"));

        return studentModel;
    }

    private void getStudentsListAPI(final String URLHEAD) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list" + successResponse);
                try {
                    studentsModelArrayList = new ArrayList<>();
                    studentsModelArrayListCopy = new ArrayList<>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    if (dataObject.optString("alumi").equalsIgnoreCase("0"))
                                    {
                                        studentsModelArrayListCopy.add(addStudentDetails(dataObject));
                                        System.out.println("Student list size first"+studentsModelArrayListCopy.size());

                                    }

                                }
                                if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("1"))
                                {
                                    for (int i = 0; i < studentsModelArrayListCopy.size(); i++) {
                                        if (studentsModelArrayListCopy.get(i).getType().equalsIgnoreCase("primary")) {
                                            StudentModel studentModel = new StudentModel();
                                            studentModel.setmId(studentsModelArrayListCopy.get(i).getmId());
                                            studentModel.setmName(studentsModelArrayListCopy.get(i).getmName());
                                            studentModel.setmClass(studentsModelArrayListCopy.get(i).getmClass());
                                            studentModel.setmSection(studentsModelArrayListCopy.get(i).getmSection());
                                            studentModel.setmHouse(studentsModelArrayListCopy.get(i).getmHouse());
                                            studentModel.setmPhoto(studentsModelArrayListCopy.get(i).getmPhoto());
                                            studentModel.setProgressReport(studentsModelArrayListCopy.get(i).getProgressReport());
                                            studentModel.setAlumini(studentsModelArrayListCopy.get(i).getAlumini());
                                            studentModel.setType(studentsModelArrayListCopy.get(i).getType());
                                            studentsModelArrayList.add(studentModel);
                                        }
                                    }
                                    for (int i = 0; i < studentsModelArrayListCopy.size(); i++)
                                    {
                                        if (!(studentsModelArrayListCopy.get(i).getType().equalsIgnoreCase("primary"))) {
                                            StudentModel studentModel = new StudentModel();
                                            studentModel.setmId(studentsModelArrayListCopy.get(i).getmId());
                                            studentModel.setmName(studentsModelArrayListCopy.get(i).getmName());
                                            studentModel.setmClass(studentsModelArrayListCopy.get(i).getmClass());
                                            studentModel.setmSection(studentsModelArrayListCopy.get(i).getmSection());
                                            studentModel.setmHouse(studentsModelArrayListCopy.get(i).getmHouse());
                                            studentModel.setmPhoto(studentsModelArrayListCopy.get(i).getmPhoto());
                                            studentModel.setProgressReport(studentsModelArrayListCopy.get(i).getProgressReport());
                                            studentModel.setAlumini(studentsModelArrayListCopy.get(i).getAlumini());
                                            studentModel.setType(studentsModelArrayListCopy.get(i).getType());
                                            studentsModelArrayList.add(studentModel);
                                        }
                                    }
                                }
                                else   if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("2"))
                                {
                                    for (int i = 0; i < studentsModelArrayListCopy.size(); i++) {
                                        if (studentsModelArrayListCopy.get(i).getType().equalsIgnoreCase("secondary")) {
                                            StudentModel studentModel = new StudentModel();
                                            studentModel.setmId(studentsModelArrayListCopy.get(i).getmId());
                                            studentModel.setmName(studentsModelArrayListCopy.get(i).getmName());
                                            studentModel.setmClass(studentsModelArrayListCopy.get(i).getmClass());
                                            studentModel.setmSection(studentsModelArrayListCopy.get(i).getmSection());
                                            studentModel.setmHouse(studentsModelArrayListCopy.get(i).getmHouse());
                                            studentModel.setmPhoto(studentsModelArrayListCopy.get(i).getmPhoto());
                                            studentModel.setProgressReport(studentsModelArrayListCopy.get(i).getProgressReport());
                                            studentModel.setAlumini(studentsModelArrayListCopy.get(i).getAlumini());
                                            studentModel.setType(studentsModelArrayListCopy.get(i).getType());
                                            studentsModelArrayList.add(studentModel);
                                        }
                                    }
                                    for (int i = 0; i < studentsModelArrayListCopy.size(); i++)
                                    {
                                        if (!(studentsModelArrayListCopy.get(i).getType().equalsIgnoreCase("secondary"))) {
                                            StudentModel studentModel = new StudentModel();
                                            studentModel.setmId(studentsModelArrayListCopy.get(i).getmId());
                                            studentModel.setmName(studentsModelArrayListCopy.get(i).getmName());
                                            studentModel.setmClass(studentsModelArrayListCopy.get(i).getmClass());
                                            studentModel.setmSection(studentsModelArrayListCopy.get(i).getmSection());
                                            studentModel.setmHouse(studentsModelArrayListCopy.get(i).getmHouse());
                                            studentModel.setmPhoto(studentsModelArrayListCopy.get(i).getmPhoto());
                                            studentModel.setProgressReport(studentsModelArrayListCopy.get(i).getProgressReport());
                                            studentModel.setAlumini(studentsModelArrayListCopy.get(i).getAlumini());
                                            studentModel.setType(studentsModelArrayListCopy.get(i).getType());
                                            studentsModelArrayList.add(studentModel);
                                        }
                                    }
                                }

                                else   if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("3"))
                                {
                                    for (int i = 0; i < studentsModelArrayListCopy.size(); i++) {
                                            StudentModel studentModel = new StudentModel();
                                            studentModel.setmId(studentsModelArrayListCopy.get(i).getmId());
                                            studentModel.setmName(studentsModelArrayListCopy.get(i).getmName());
                                            studentModel.setmClass(studentsModelArrayListCopy.get(i).getmClass());
                                            studentModel.setmSection(studentsModelArrayListCopy.get(i).getmSection());
                                            studentModel.setmHouse(studentsModelArrayListCopy.get(i).getmHouse());
                                            studentModel.setmPhoto(studentsModelArrayListCopy.get(i).getmPhoto());
                                            studentModel.setProgressReport(studentsModelArrayListCopy.get(i).getProgressReport());
                                            studentModel.setAlumini(studentsModelArrayListCopy.get(i).getAlumini());
                                            studentModel.setType(studentsModelArrayListCopy.get(i).getType());
                                            studentsModelArrayList.add(studentModel);
                                        }


                                }



                                if (PreferenceManager.getStudIdForCCA(mContext).equalsIgnoreCase(""))
                                {
                                    studentName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_id = studentsModelArrayList.get(0).getmId();
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();
                                    stud_img = studentsModelArrayList.get(0).getmPhoto();
                                    progressReport = studentsModelArrayList.get(0).getProgressReport();
                                    section = studentsModelArrayList.get(0).getmSection();
                                    alumini = studentsModelArrayList.get(0).getAlumini();
                                    type = studentsModelArrayList.get(0).getType();
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    } else

                                    {
                                        studImg.setImageResource(R.drawable.boy);
                                    }


                                    textViewYear.setText("Class : " + studentsModelArrayList.get(0).getmClass());

                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0");
                                } else
                                    {

                                    int studentSelectPosition = Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    studentName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();
                                    stud_name = studentsModelArrayList.get(studentSelectPosition).getmName();
                                    stud_class = studentsModelArrayList.get(studentSelectPosition).getmClass();
                                    progressReport = studentsModelArrayList.get(studentSelectPosition).getProgressReport();
                                    stud_img = studentsModelArrayList.get(studentSelectPosition).getmPhoto();
                                    alumini = studentsModelArrayList.get(studentSelectPosition).getAlumini();
                                    type = studentsModelArrayList.get(studentSelectPosition).getType();
                                    System.out.println("selected student image" + studentsModelArrayList.get(studentSelectPosition).getmPhoto());
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    } else

                                    {
                                        studImg.setImageResource(R.drawable.boy);
                                    }
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(studentSelectPosition).getmClass());

                                }
                                  if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("1"))
                                  {
                                      if (type.equalsIgnoreCase("Primary"))
                                      {
                                          timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                          timeTableAllRecycler.setVisibility(View.GONE);
                                          if (AppUtils.isNetworkConnected(mContext))
                                          {
                                              System.out.println("test working");
                                              getReportListAPI(URL_GET_TIME_TABLE_LIST, 0);
                                          } else {
                                              AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                          }
                                      }
                                      else
                                      {
                                          timeTableSingleRecycler.setVisibility(View.GONE);
                                          timeTableAllRecycler.setVisibility(View.GONE);
                                         // AppUtils.showDialogAlertDismiss((Activity) mContext, " no primary timetable", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                      }
                                  }
                                  else if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("2"))
                                  {
                                      if (type.equalsIgnoreCase("Secondary"))
                                      {
                                          timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                          timeTableAllRecycler.setVisibility(View.GONE);
                                          if (AppUtils.isNetworkConnected(mContext)) {
                                              System.out.println("test working");
                                              getReportListAPI(URL_GET_TIME_TABLE_LIST, 0);
                                          } else {
                                              AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                          }
                                      }
                                      else
                                      {
                                          timeTableSingleRecycler.setVisibility(View.GONE);
                                          timeTableAllRecycler.setVisibility(View.GONE);
                                         // AppUtils.showDialogAlertDismiss((Activity) mContext, "no timetable secondary Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                      }
                                  }
                                  else if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("3"))
                                  {
                                      if (type.equalsIgnoreCase("Primary")|| type.equalsIgnoreCase("Secondary"))
                                      {
                                          timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                          timeTableAllRecycler.setVisibility(View.GONE);
                                          if (AppUtils.isNetworkConnected(mContext)) {
                                              System.out.println("test working");
                                              getReportListAPI(URL_GET_TIME_TABLE_LIST, 0);
                                          } else {
                                              AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                          }
                                      }
                                      else
                                      {
                                          timeTableSingleRecycler.setVisibility(View.GONE);
                                          timeTableAllRecycler.setVisibility(View.GONE);
                                         // AppUtils.showDialogAlertDismiss((Activity) mContext, "no both Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);


                                      }
                                  }
                                  else {
                                   //   AppUtils.showDialogAlertDismiss((Activity) mContext, "No timetable", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                  }

                            } else {
//                                mRecycleView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

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

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {


            }
        });


    }


    public void showSocialmediaList(final ArrayList<StudentModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView socialMediaList = dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
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

        StudentSpinnerAdapter studentAdapter = new StudentSpinnerAdapter(mContext, mStudentArray);
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

                        if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("1"))
                        {
                            if (mStudentArray.get(position).getType().equalsIgnoreCase("Primary"))
                            {
                                studentName.setText(mStudentArray.get(position).getmName());
                                stud_id = mStudentArray.get(position).getmId();
                                stud_name = mStudentArray.get(position).getmName();
                                stud_class = mStudentArray.get(position).getmClass();
                                stud_img = mStudentArray.get(position).getmPhoto();
                                section = mStudentArray.get(position).getmSection();
                                progressReport = mStudentArray.get(position).getProgressReport();
                                alumini = mStudentArray.get(position).getAlumini();
                                textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else

                                {

                                    studImg.setImageResource(R.drawable.boy);
                                }
                                timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                timeTableAllRecycler.setVisibility(View.GONE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    System.out.println("test working");
                                    getReportListAPI(URL_GET_TIME_TABLE_LIST, 1);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }
                            else
                            {

                              //  timeTableSingleRecycler.setVisibility(View.GONE);
                               // timeTableAllRecycler.setVisibility(View.GONE);
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.only_primary), R.drawable.exclamationicon, R.drawable.round);

                            }

                        }
                        else if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("2"))
                        {
                            if (mStudentArray.get(position).getType().equalsIgnoreCase("Secondary"))
                            {
                                studentName.setText(mStudentArray.get(position).getmName());
                                stud_id = mStudentArray.get(position).getmId();
                                stud_name = mStudentArray.get(position).getmName();
                                stud_class = mStudentArray.get(position).getmClass();
                                stud_img = mStudentArray.get(position).getmPhoto();
                                section = mStudentArray.get(position).getmSection();
                                progressReport = mStudentArray.get(position).getProgressReport();
                                alumini = mStudentArray.get(position).getAlumini();
                                textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else

                                {

                                    studImg.setImageResource(R.drawable.boy);
                                }
                                timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                timeTableAllRecycler.setVisibility(View.GONE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    System.out.println("test working");
                                    getReportListAPI(URL_GET_TIME_TABLE_LIST, 1);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }
                            else
                            {
                              //  timeTableSingleRecycler.setVisibility(View.GONE);
                               // timeTableAllRecycler.setVisibility(View.GONE);
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.only_secondary), R.drawable.exclamationicon, R.drawable.round);

                                //AppUtils.showDialogAlertDismiss((Activity) mContext, " no secondary Alert", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        }
                        else if (PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("3"))
                        {
                            if (mStudentArray.get(position).getType().equalsIgnoreCase("Secondary") || mStudentArray.get(position).getType().equalsIgnoreCase("Primary"))
                            {
                                studentName.setText(mStudentArray.get(position).getmName());
                                stud_id = mStudentArray.get(position).getmId();
                                stud_name = mStudentArray.get(position).getmName();
                                stud_class = mStudentArray.get(position).getmClass();
                                stud_img = mStudentArray.get(position).getmPhoto();
                                section = mStudentArray.get(position).getmSection();
                                progressReport = mStudentArray.get(position).getProgressReport();
                                alumini = mStudentArray.get(position).getAlumini();
                                textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else

                                {

                                    studImg.setImageResource(R.drawable.boy);
                                }
                                timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                timeTableAllRecycler.setVisibility(View.GONE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    System.out.println("test working");
                                    getReportListAPI(URL_GET_TIME_TABLE_LIST, 1);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }
                            else
                            {
                               // timeTableSingleRecycler.setVisibility(View.GONE);
                               // timeTableAllRecycler.setVisibility(View.GONE);
                              //  AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.both_secondary_primary), R.drawable.exclamationicon, R.drawable.round);

                                //    AppUtils.showDialogAlertDismiss((Activity) mContext, " no Both Alert", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        }




                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    private void getReportListAPI(final String URLHEAD, final int fromStud) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "student_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), stud_id};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list progress" + successResponse);
                try {

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray feildArray = secobj.getJSONArray("field1");
                            System.out.println("feild array"+feildArray.length());
                            mFieldModel = new ArrayList<FieldModel>();
                            if (feildArray.length() > 0)
                            {
                                int lun = 0;
                                for (int i = 0; i < feildArray.length(); i++)
                                {
                                    JSONObject listObject = feildArray.optJSONObject(i);
                                    FieldModel xmodel = new FieldModel();
                                    xmodel.setSortname(listObject.optString("sortname"));
                                    xmodel.setStarttime(listObject.optString("starttime"));
                                    xmodel.setEndtime(listObject.optString("endtime"));
                                    if (listObject.has("period_id")) {
                                        xmodel.setPeriodId(listObject.optString("period_id"));
                                        lun = lun + 1;
                                        System.out.println("lun::" + lun);
                                        xmodel.setCountBreak(lun);
                                    } else {
                                        xmodel.setPeriodId(listObject.optString(""));
                                    }
                                    mFieldModel.add(xmodel);
                                }
                            }
                            JSONObject rangeObj = secobj.getJSONObject("range");
                            String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                            mRangeModel = new ArrayList<>();
                            mFridayModelArraylist = new ArrayList<>();
                            mBreakArrayList=new ArrayList<>();
                            for (int i = 0; i < 5; i++)
                            {
                                System.out.println("i value" + i);
                                JSONArray monday = rangeObj.optJSONArray(days[i]);
                                System.out.println("size of monday array" + monday.length());
                                RangeModel rangeModel = new RangeModel();
                                ArrayList<DayModel> mDayModel = new ArrayList<>();
                                if (monday.length() > 0)
                                {
                                    timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                    System.out.println("working success");

                                    for (int j = 0; j < monday.length(); j++)
                                    {
                                        JSONObject dataObject = monday.getJSONObject(j);
                                        DayModel model = new DayModel();
                                        model.setId(dataObject.optString("id"));
                                        model.setPeriod_id(dataObject.optString("period_id"));
                                        model.setEndtime(dataObject.optString("endtime"));
                                        model.setStarttime(dataObject.optString("starttime"));
                                        if (dataObject.has("staff")) {
                                            model.setIsBreak(0);

                                        } else {
                                            model.setIsBreak(1);

                                        }
                                        model.setSubject_name(dataObject.optString("subject_name"));
                                        model.setStaff(dataObject.optString("staff"));
                                        model.setSortname(dataObject.optString("sortname"));
                                        model.setStudent_id(dataObject.optString("student_id"));
                                        model.setDay(dataObject.optString("day"));
                                         if (i==4)
                                         {
                                             mFridayModelArraylist.add(model);
                                         }
                                        if (!(dataObject.optString("sortname").equalsIgnoreCase("BUS")))
                                            mDayModel.add(model);
                                    }

                                }
                                System.out.println("table range model size" + mDayModel.size());
                                rangeModel.setTimeTableDayModel(mDayModel);
                                mRangeModel.add(rangeModel);


                            }
                            System.out.println("array size break friday"+mFridayModelArraylist.size());

                            for (int b=0;b<mFridayModelArraylist.size();b++)
                            {
                                FieldModel mBreakModel= new FieldModel();
                                if (mFridayModelArraylist.get(b).getSortname().equalsIgnoreCase("Break")||mFridayModelArraylist.get(b).getSortname().equalsIgnoreCase("L1")||mFridayModelArraylist.get(b).getSortname().equalsIgnoreCase("L2"))
                                {
                                    mBreakModel.setPeriodId(mFridayModelArraylist.get(b).getPeriod_id());
                                    mBreakModel.setSortname(mFridayModelArraylist.get(b).getSortname());
                                    mBreakModel.setStarttime(mFridayModelArraylist.get(b).getStarttime());
                                    mBreakModel.setEndtime(mFridayModelArraylist.get(b).getEndtime());
                                    mBreakArrayList.add(mBreakModel);
                                }
                            }
                            System.out.println("array size break"+mBreakArrayList.size());
                            for (int f=0;f<mFieldModel.size();f++)
                            {
                                String sortName=mFieldModel.get(f).getSortname();
                                for (int br=0;br<mBreakArrayList.size();br++)
                                {
                                    if (sortName.equalsIgnoreCase(mBreakArrayList.get(br).getSortname()))
                                    {
                                        mFieldModel.get(f).setFridyaStartTime(mBreakArrayList.get(br).getStarttime());
                                        mFieldModel.get(f).setFridayEndTime(mBreakArrayList.get(br).getEndtime());
                                    }

                                }
                            }
                            System.out.println("Array size"+mFridayModelArraylist.size());
                            System.out.println("Array size feild"+mFieldModel.size());

                            mTimeTableModelArrayList=new ArrayList<>();
                            JSONArray timeTableArray=secobj.getJSONArray("Timetable");
                            int m = 0, tu = 0, w = 0, th = 0, fr = 0;
                            if (timeTableArray.length()>0)
                            {
                                for (int t=0;t<timeTableArray.length();t++)
                                {
                                    TimeTableModel mTimeModel= new TimeTableModel();
                                    JSONObject timeObj= timeTableArray.getJSONObject(t);
                                    mTimeModel.setId(timeObj.optString("id"));
                                    mTimeModel.setPeriod_id(timeObj.optString("period_id"));
                                    mTimeModel.setSubject_name(timeObj.optString("subject_name"));
                                    mTimeModel.setStaff(timeObj.optString("staff"));
                                    mTimeModel.setDay(timeObj.optString("day"));
                                    mTimeModel.setStudent_id(timeObj.optString("student_id"));
                                    mTimeModel.setSortname(timeObj.optString("sortname"));
                                    mTimeModel.setStarttime(timeObj.optString("starttime"));
                                    mTimeModel.setEndtime(timeObj.optString("endtime"));
                                    mTimeTableModelArrayList.add(mTimeModel);

                                }
                            }
                            mPeriodModel=new ArrayList<>();
                            ArrayList<DayModel> mDataModelArrayList = new ArrayList<DayModel>();
                            for (int f=0;f<mFieldModel.size();f++)
                            {

                                PeriodModel mPeriod= new PeriodModel();
                                DayModel mDayModel= new DayModel();
                                ArrayList<DayModel> timeTableListM = new ArrayList<>();
                                ArrayList<DayModel> timeTableListT = new ArrayList<>();
                                ArrayList<DayModel> timeTableListW = new ArrayList<>();
                                ArrayList<DayModel> timeTableListTh = new ArrayList<>();
                                ArrayList<DayModel> timeTableListF = new ArrayList<>();

                                for (int t=0;t<mTimeTableModelArrayList.size();t++)
                                {
                                    if (mFieldModel.get(f).getSortname().equalsIgnoreCase(mTimeTableModelArrayList.get(t).getSortname()))
                                    {
                                        mDayModel.setId(mTimeTableModelArrayList.get(t).getId());
                                        mDayModel.setPeriod_id(mTimeTableModelArrayList.get(t).getPeriod_id());
                                        mDayModel.setSubject_name(mTimeTableModelArrayList.get(t).getSubject_name());
                                        mDayModel.setStaff(mTimeTableModelArrayList.get(t).getStaff());
                                        mDayModel.setStudent_id(mTimeTableModelArrayList.get(t).getStudent_id());
                                        mDayModel.setDay(mTimeTableModelArrayList.get(t).getDay());
                                        mDayModel.setSortname(mTimeTableModelArrayList.get(t).getSortname());
                                        mDayModel.setStarttime(mTimeTableModelArrayList.get(t).getStarttime());
                                        mDayModel.setEndtime(mTimeTableModelArrayList.get(t).getEndtime());

                                        if (mTimeTableModelArrayList.get(t).getDay().equalsIgnoreCase("Monday"))
                                        {
                                            m = m + 1;
//                                            ArrayList<DayModel>timeTableMondayList=new ArrayList<>();
                                            DayModel dayModel= new DayModel();
                                            dayModel.setId(mTimeTableModelArrayList.get(t).getId());
                                            dayModel.setPeriod_id(mTimeTableModelArrayList.get(t).getPeriod_id());
                                            dayModel.setSubject_name(mTimeTableModelArrayList.get(t).getSubject_name());
                                            dayModel.setStaff(mTimeTableModelArrayList.get(t).getStaff());
                                            dayModel.setStudent_id(mTimeTableModelArrayList.get(t).getStudent_id());
                                            dayModel.setDay(mTimeTableModelArrayList.get(t).getDay());
                                            dayModel.setSortname(mTimeTableModelArrayList.get(t).getSortname());
                                            dayModel.setStarttime(mTimeTableModelArrayList.get(t).getStarttime());
                                            dayModel.setEndtime(mTimeTableModelArrayList.get(t).getEndtime());
                                            timeTableListM.add(dayModel);
                                            mPeriod.setMonday(mTimeTableModelArrayList.get(t).getSubject_name());
                                           // timeTableListM.add(dayModel);

                                        }
                                       else if (mTimeTableModelArrayList.get(t).getDay().equalsIgnoreCase("Tuesday"))
                                        {
                                            tu = tu + 1;
//                                            ArrayList<DayModel>timeTableTuesadayList=new ArrayList<>();
                                            DayModel dayModel= new DayModel();
                                            dayModel.setId(mTimeTableModelArrayList.get(t).getId());
                                            dayModel.setPeriod_id(mTimeTableModelArrayList.get(t).getPeriod_id());
                                            dayModel.setSubject_name(mTimeTableModelArrayList.get(t).getSubject_name());
                                            dayModel.setStaff(mTimeTableModelArrayList.get(t).getStaff());
                                            dayModel.setStudent_id(mTimeTableModelArrayList.get(t).getStudent_id());
                                            dayModel.setDay(mTimeTableModelArrayList.get(t).getDay());
                                            dayModel.setSortname(mTimeTableModelArrayList.get(t).getSortname());
                                            dayModel.setStarttime(mTimeTableModelArrayList.get(t).getStarttime());
                                            dayModel.setEndtime(mTimeTableModelArrayList.get(t).getEndtime());
                                            timeTableListT.add(dayModel);
                                          //  mPeriod.setTimeTableListT(timeTableTuesadayList);
                                            mPeriod.setTuesday(mTimeTableModelArrayList.get(t).getSubject_name());

                                        }
                                        else if (mTimeTableModelArrayList.get(t).getDay().equalsIgnoreCase("wednesday"))
                                        {
                                            w = w + 1;
                                           // ArrayList<DayModel>timeTablewednesdayList=new ArrayList<>();
                                            DayModel dayModel= new DayModel();
                                            dayModel.setId(mTimeTableModelArrayList.get(t).getId());
                                            dayModel.setPeriod_id(mTimeTableModelArrayList.get(t).getPeriod_id());
                                            dayModel.setSubject_name(mTimeTableModelArrayList.get(t).getSubject_name());
                                            dayModel.setStaff(mTimeTableModelArrayList.get(t).getStaff());
                                            dayModel.setStudent_id(mTimeTableModelArrayList.get(t).getStudent_id());
                                            dayModel.setDay(mTimeTableModelArrayList.get(t).getDay());
                                            dayModel.setSortname(mTimeTableModelArrayList.get(t).getSortname());
                                            dayModel.setStarttime(mTimeTableModelArrayList.get(t).getStarttime());
                                            dayModel.setEndtime(mTimeTableModelArrayList.get(t).getEndtime());
                                            timeTableListW.add(dayModel);
                                            //mPeriod.setTimeTableListW(timeTablewednesdayList);
                                            mPeriod.setWednesday(mTimeTableModelArrayList.get(t).getSubject_name());

                                        }
                                        else if (mTimeTableModelArrayList.get(t).getDay().equalsIgnoreCase("thursday"))
                                        {
                                            th = th + 1;
                                        //    ArrayList<DayModel>timeTablethursdayList=new ArrayList<>();
                                            DayModel dayModel= new DayModel();
                                            dayModel.setId(mTimeTableModelArrayList.get(t).getId());
                                            dayModel.setPeriod_id(mTimeTableModelArrayList.get(t).getPeriod_id());
                                            dayModel.setSubject_name(mTimeTableModelArrayList.get(t).getSubject_name());
                                            dayModel.setStaff(mTimeTableModelArrayList.get(t).getStaff());
                                            dayModel.setStudent_id(mTimeTableModelArrayList.get(t).getStudent_id());
                                            dayModel.setDay(mTimeTableModelArrayList.get(t).getDay());
                                            dayModel.setSortname(mTimeTableModelArrayList.get(t).getSortname());
                                            dayModel.setStarttime(mTimeTableModelArrayList.get(t).getStarttime());
                                            dayModel.setEndtime(mTimeTableModelArrayList.get(t).getEndtime());
                                            timeTableListTh.add(dayModel);
                                           // mPeriod.setTimeTableListTh(timeTablethursdayList);
                                            mPeriod.setThursday(mTimeTableModelArrayList.get(t).getSubject_name());

                                        }
                                        else if (mTimeTableModelArrayList.get(t).getDay().equalsIgnoreCase("friday"))
                                        {
                                            fr = fr + 1;
                                            //ArrayList<DayModel>timeTablefridayList=new ArrayList<>();
                                            DayModel dayModel= new DayModel();
                                            dayModel.setId(mTimeTableModelArrayList.get(t).getId());
                                            dayModel.setPeriod_id(mTimeTableModelArrayList.get(t).getPeriod_id());
                                            dayModel.setSubject_name(mTimeTableModelArrayList.get(t).getSubject_name());
                                            dayModel.setStaff(mTimeTableModelArrayList.get(t).getStaff());
                                            dayModel.setStudent_id(mTimeTableModelArrayList.get(t).getStudent_id());
                                            dayModel.setDay(mTimeTableModelArrayList.get(t).getDay());
                                            dayModel.setSortname(mTimeTableModelArrayList.get(t).getSortname());
                                            dayModel.setStarttime(mTimeTableModelArrayList.get(t).getStarttime());
                                            dayModel.setEndtime(mTimeTableModelArrayList.get(t).getEndtime());
                                            timeTableListF.add(dayModel);
                                          //  mPeriod.setTimeTableListF(timeTablefridayList);
                                            mPeriod.setFriday(mTimeTableModelArrayList.get(t).getSubject_name());

                                        }
                                        else {
                                            mPeriod.setMonday("");
                                            mPeriod.setTuesday("");
                                            mPeriod.setWednesday("");
                                            mPeriod.setThursday("");
                                            mPeriod.setFriday("");
                                        }
                                        mPeriod.setCountM(m);
                                        mPeriod.setCountT(t);
                                        mPeriod.setCountW(w);
                                        mPeriod.setCountTh(th);
                                        mPeriod.setCountF(fr);
                                        mPeriod.setTimeTableListM(timeTableListM);
                                        mPeriod.setTimeTableListT(timeTableListT);
                                        mPeriod.setTimeTableListW(timeTableListW);
                                        mPeriod.setTimeTableListTh(timeTableListTh);
                                        mPeriod.setTimeTableListF(timeTableListF);
                                    }

                                }

                                mDataModelArrayList.add(mDayModel);
                                mPeriod.setTimeTableDayModel(mDataModelArrayList);
                                mPeriodModel.add(mPeriod);
                            }
                            for (int i = 0; i < weekListArray.size(); i++) {
                                if (i == weekPosition) {
                                    weekListArray.get(i).setPositionSelected(i);

                                } else {
                                    weekListArray.get(i).setPositionSelected(-1);

                                }
                            }
                            TimeTableWeekListAdapter.notifyDataSetChanged();
                            timeTableSingleRecycler.setVisibility(View.GONE);
                            timeTableAllRecycler.setVisibility(View.VISIBLE);
                            card_viewAll.setVisibility(View.VISIBLE);

                            if (weekPosition != 0) {
                                card_viewAll.setVisibility(View.GONE);
                                timeTableAllRecycler.setVisibility(View.GONE);
                                timeTableSingleRecycler.setVisibility(View.VISIBLE);
                                if (mRangeModel.size() > 0)
                                {
                                    TimeTableSingleWeekSelectionAdapter mRecyclerViewMainAdapter = new TimeTableSingleWeekSelectionAdapter(mContext, mRangeModel.get(weekPosition - 1).getTimeTableDayModel());
                                    timeTableSingleRecycler.setAdapter(mRecyclerViewMainAdapter);
                                }

                            }
                            else {
                                timeTableSingleRecycler.setVisibility(View.GONE);
                                timeTableAllRecycler.setVisibility(View.VISIBLE);
                                card_viewAll.setVisibility(View.VISIBLE);
                                TimeTableAllWeekSelectionAdapterNew mRecyclerAllAdapter = new TimeTableAllWeekSelectionAdapterNew(mContext, mPeriodModel,timeTableAllRecycler,tipContainer,mFieldModel);
                                timeTableAllRecycler.setAdapter(mRecyclerAllAdapter);

                            }
                            if (weekPosition<3) {
                                weekRecyclerList.scrollToPosition(0);
                            }
                            else
                            {
                                weekRecyclerList.scrollToPosition(5);

                            }
                            System.out.println("Range" + mRangeModel.size());


//                            TimeTableSingleWeekSelectionAdapter mRecyclerViewMainAdapter = new TimeTableSingleWeekSelectionAdapter(mContext, mRangeModel);
//                            timeTableSingleRecycler.setAdapter(mRecyclerViewMainAdapter);
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD, fromStud);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD, fromStud);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD, fromStud);

                    } else {


                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            //AppController.getInstance().trackScreenView("Report Screen Fragment"+" "+ PreferenceManager.getUserEmail(mContext)+ Calendar.getInstance().getTime());
            AppController.getInstance().trackScreenView("Report Screen Fragment. " + "(" + PreferenceManager.getUserEmail(mContext) + ")" + " " + "(" + Calendar.getInstance().getTime() + ")");
        }
    }
}
