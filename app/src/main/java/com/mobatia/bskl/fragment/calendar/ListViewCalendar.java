package com.mobatia.bskl.fragment.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.calender.CalenderDetailActivity;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.NaisClassNameConstants;
import com.mobatia.bskl.constants.NaisTabConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.calendar.adapter.CustomList;
import com.mobatia.bskl.fragment.calendar.adapter.ListViewSpinnerAdapter;
import com.mobatia.bskl.fragment.calendar.calendarutils.CalendarListener;
import com.mobatia.bskl.fragment.calendar.calendarutils.CustomCalendarView;
import com.mobatia.bskl.fragment.calendar.model.CalendarModel;
import com.mobatia.bskl.fragment.calendar.model.ListViewSpinnerModel;
import com.mobatia.bskl.fragment.calendar.model.StudentDetailModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.mobatia.bskl.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.bskl.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.mobatia.bskl.appcontroller.AppController.holidayArrayListYear;


/**
 * Created by krishnaraj on 27/06/18.
 */

@SuppressLint("ValidFragment")
public class ListViewCalendar extends Fragment implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String mTitle;
    private String mTabId;
    private View mRootView;
    public static RecyclerView list;
    String[] event = {"Maths assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm", "Maths assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm", "English assessment will be conducted from: 10 am to 1 pm"};
    static com.mobatia.bskl.fragment.calendar.calendarutils.CustomCalendarView calendar;
    static ImageView arrow;
    static ImageView arrowUp;
    static RelativeLayout listRelative;
    static RelativeLayout listMainRelative;
    static RelativeLayout mainRelative;
    static Context mContext;
    public static ArrayList<String> dateArrayList;
    public static ArrayList<String> holidayArrayList;
    public static ArrayList<CalendarModel> eventArrayList;
    static SimpleDateFormat sdfcalender;
    String myFormatCalender = "yyyy-MM-dd";
    public static ArrayList<CalendarModel> mEventArrayListFilterList;
    GridLayoutManager recyclerViewLayoutManager;
    public static int viewType = 1;
    Calendar startDate;
    Calendar currentDate;
    String[] listItem;
    Calendar endDate;
    public static TextView txtMYW;
    private ListView spinnnerList;
    public static RelativeLayout listSpinner;
    LinearLayout txtSpinner;
    public static TextView daySpinner;
    public static Spinner spinner;
    public static RelativeLayout alertRelative;
    public static TextView noDataAlertTxt;
    Calendar startCalendar, endCalendar;
    private ArrayList<ListViewSpinnerModel> mListViewArray;
    String[] data = {"7", "8", "9", "10", "11", "0", "1", "2", "3", "4", "5", "6",};
    MyRecyclerViewAdapter adapter;
    int diffMonth, diffYear, diffInc;
    private boolean daySpinSelect = true;
    int startMonth = 7;
    int startYear = 2018;
    int endYear = 2019;
    int endMonth = 6;
    boolean isShowCalendar=true;

    public ListViewCalendar(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    ArrayList<String> dataArrayStrings = new ArrayList<String>() {
        {
            add("Year View");
            add("Month View");
            add("Week View");
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == mainRelative.getId()) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.calendar_list_event, container,
                false);
        mContext = getActivity();
        mEventArrayListFilterList = new ArrayList<>();
        eventArrayList = new ArrayList<>();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView headerTitle = actionBar.getCustomView().findViewById(R.id.headerTitle);
        ImageView logoClickImgView = actionBar.getCustomView().findViewById(R.id.logoClickImgView);
        ImageView imageButton2 = actionBar.getCustomView().findViewById(R.id.action_bar_forward);
        imageButton2.setImageResource(R.drawable.tutorial_icon);
        headerTitle.setText("Calendar");
        headerTitle.setVisibility(View.VISIBLE);
        logoClickImgView.setVisibility(View.INVISIBLE);
        myFormatCalender = "yyyy-MM-dd";
        sdfcalender = new SimpleDateFormat(myFormatCalender, Locale.ENGLISH);
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            clearBadge();
            callCalendarListMonth();
            callCalendarList();
            if (isShowCalendar) {
                calendarVisibleGone();
                System.out.println("test1"); //hiding
            } else {
                calendarVisible();
                System.out.println("test2"); //showing
                if (daySpinSelect == false) {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;
                }

                calendarVisible();

            }


        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
        if (PreferenceManager.getIsFirstTimeInCalendar(mContext)) {
            PreferenceManager.setIsFirstTimeInCalendar(mContext, false);
            Intent mintent = new Intent(mContext, CalendarInfoActivity.class);
            mintent.putExtra("type", 1);
            mContext.startActivity(mintent);
        }
        startDate = Calendar.getInstance();
        currentDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        if (endMonth < (currentDate.getTime().getMonth())) {
//           diffInc= currentDate.getTime().getYear()+ 1900-startYear;
            startYear = currentDate.getTime().getYear() + 1900;
            endYear = (currentDate.getTime().getYear() + 1900) + 1;

        } else {
            startYear = (currentDate.getTime().getYear() + 1900) - 1;
            endYear = (currentDate.getTime().getYear() + 1900);
        }
        startDate.set(startYear, startMonth, 01);
        endDate.set(endYear, endMonth, 31);//+1
        startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate.getTime());
        endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate.getTime());
        diffYear = (endCalendar.get(Calendar.YEAR)) - startCalendar.get(Calendar.YEAR);
        diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        list.addOnItemTouchListener(new RecyclerItemListener(mContext, list, new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                if (daySpinSelect == false) {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;


                }
                if (isShowCalendar) {

                    calendarVisible();
                    System.out.println("test1"); //hiding
                } else {
                    calendarVisibleGone();
                    System.out.println("test2"); //showing
                }
                Intent mIntent;
                mIntent = new Intent(mContext, CalenderDetailActivity.class);
                mIntent.putExtra(POSITION, position);
                mIntent.putExtra("tittle", mEventArrayListFilterList.get(position).getTittle());
                mIntent.putExtra("date", mEventArrayListFilterList.get(position).getDate());
                mIntent.putExtra(PASS_ARRAY_LIST, mEventArrayListFilterList);
                mContext.startActivity(mIntent);


            }

            @Override
            public void onLongClickItem(View v, int position) {

            }

        }));

        return mRootView;
    }

    private void initialiseUI() {
        dateArrayList = new ArrayList<>();
        holidayArrayList = new ArrayList<>();
        eventArrayList = new ArrayList<CalendarModel>();
        AppController.dateArrayListYear = new ArrayList<>();
        holidayArrayListYear = new ArrayList<>();

        arrow = mRootView.findViewById(R.id.arrowDwnImg);
        arrowUp = mRootView.findViewById(R.id.arrowUpImg);
        list = mRootView.findViewById(R.id.mEventList);
        txtMYW = mRootView.findViewById(R.id.txtMYW);
        daySpinner = mRootView.findViewById(R.id.daySpinner);
        spinner = mRootView.findViewById(R.id.spinner);
        spinnnerList = mRootView.findViewById(R.id.eventSpinner);
        listSpinner = mRootView.findViewById(R.id.listSpinner);
        txtSpinner = mRootView.findViewById(R.id.txtSpinner);
        noDataAlertTxt = mRootView.findViewById(R.id.noDataAlertTxt);
        alertRelative = mRootView.findViewById(R.id.alertRelative);
        noDataAlertTxt.setText(getString(R.string.no_calendar_data));
        txtMYW.setText("This Month");
        spinner.setOnItemSelectedListener(this);

        mListViewArray = new ArrayList<>();
        for (int i = 0; i < dataArrayStrings.size(); i++) {
            ListViewSpinnerModel mListViewSpinnerModel = new ListViewSpinnerModel();
            mListViewSpinnerModel.setId(String.valueOf(i));
            mListViewSpinnerModel.setFilename("");
            mListViewSpinnerModel.setTitle(dataArrayStrings.get(i));
            mListViewArray.add(mListViewSpinnerModel);

        }
        spinnnerList.setAdapter(new ListViewSpinnerAdapter(getActivity(), mListViewArray));

        txtSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("dayspinner click");
                if (daySpinSelect == true) {
                    listSpinner.setVisibility(View.VISIBLE);
                    daySpinSelect = false;
                } else {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;

                }
            }
        });
        daySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("dayspinner click");
                if (daySpinSelect == true) {
                    listSpinner.setVisibility(View.VISIBLE);
                    daySpinSelect = false;
                } else {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;

                }


            }
        });

        spinnnerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("positiontime1", "positiontime1 = " + position);

                // TODO Auto-generated method stub
                if (position == 1) {

                    listSpinner.setVisibility(View.GONE);
                    daySpinner.setText("Month View");

                    //calendarVisibleGone();
                    mEventArrayListFilterList = new ArrayList<>();
                    final Date date = new Date(); // your date

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    for (int j = 0; j < eventArrayList.size(); j++) {
                        if ((currentMonth(calendar.get(Calendar.MONTH))) == Integer.valueOf(eventArrayList.get(j).getMonthNumber()) && (1900 + CustomCalendarView.currentCalendar.getTime().getYear()) == Integer.valueOf(eventArrayList.get(j).getYear())) {
                            mEventArrayListFilterList.add(eventArrayList.get(j));
                        }
                    }
                    viewType = 1;

                    CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
                    list.setAdapter(mCustomListAdapter);
                    txtMYW.setText("This Month");
                    ListViewCalendar.calendar.next((ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1) + currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1)));
                } else if (position == 2) {
                    listSpinner.setVisibility(View.GONE);
                    daySpinner.setText("Week View");

                    //calendarVisibleGone();
                    viewType = 2;
                    SimpleDateFormat formatEEE = new SimpleDateFormat("EEE", Locale.ENGLISH);
                    SimpleDateFormat formatMM = new SimpleDateFormat("MM", Locale.ENGLISH);
                    SimpleDateFormat formatDD = new SimpleDateFormat("dd", Locale.ENGLISH);
                    SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


                    String[] dayOfTheWeek = new String[7];
                    String[] month = new String[7];
                    String[] year = new String[7];
                    String[] dd = new String[7];

                    final Date date = new Date(); // your date

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    System.out.println("calendar:" + calendar.get(Calendar.DAY_OF_MONTH) + ":" + calendar.get(Calendar.MONTH) + ":" + calendar.get(Calendar.YEAR));

                    String[] days = new String[7];


                    for (int i = 0; i < 7; i++) {
                        days[i] = sdf.format(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                        calendar.set(Calendar.DAY_OF_WEEK, i + 1);

                        Date msgDate = new Date();
                        try {

                            msgDate = sdf.parse(calendar.get(Calendar.YEAR) + "-" + currentMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                        } catch (ParseException ex) {
                            Log.e("Date", "Parsing error");

                        }

                        dayOfTheWeek[i] = formatEEE.format(msgDate); // Thu

                        year[i] = formatyyyy.format(msgDate); // yyyy
                        month[i] = formatMM.format(msgDate); // 15
                        dd[i] = formatDD.format(msgDate); // 15
                    }
                    ListViewCalendar.calendar.nexWeek((ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1) + currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1)));

                    mEventArrayListFilterList = new ArrayList<>();
                    for (int i = 0; i < dayOfTheWeek.length; i++) {
                        for (int j = 0; j < eventArrayList.size(); j++) {
                            if (dd[i].equalsIgnoreCase(eventArrayList.get(j).getDay()) && dayOfTheWeek[i].equalsIgnoreCase(eventArrayList.get(j).getDayOfTheWeek()) && month[i].equalsIgnoreCase(eventArrayList.get(j).getMonthNumber()) && (year[i].equalsIgnoreCase(eventArrayList.get(j).getYear()))) {
                                mEventArrayListFilterList.add(eventArrayList.get(j));
//                                Collections.reverse(mEventArrayListFilterList);
                            }
                        }
                    }
                    txtMYW.setText("This Week");

                    CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
                    list.setAdapter(mCustomListAdapter);

                    if (mEventArrayListFilterList.size() == 0) {
                        alertRelative.setVisibility(View.VISIBLE);
                    } else {
                        alertRelative.setVisibility(View.GONE);

                    }
                } else if (position == 0) {
                    listSpinner.setVisibility(View.GONE);
                    viewType = 0;
                    txtMYW.setText("This Year");
                    calendarVisibleGone();
//                    if (isShowCalendar)
//                    {
//                        calendarVisible();
//                    }
//                    else
//                    {
//                        calendarVisibleGone();
//                    }

                    Intent intent = new Intent(getActivity(), CalendarYearActivity.class);
                    intent.putExtra("BUNDLE", (Serializable) AppController.holidayArrayListYear);
                    startActivity(intent);

                    //intent.putExtra("holidaysArray",holidayArrayList);
                    /*intent.putExtra("diffYear", diffYear);
                    intent.putExtra("startMonth", startCalendar.getTime().getMonth());
                    intent.putExtra("startYear", startCalendar.getTime().getYear() + 1900);
                    intent.putExtra("endYear", endCalendar.getTime().getYear() + 1900);
                    intent.putExtra("diffMonth", diffMonth);
                    intent.putExtra("startTime", startCalendar.getTimeInMillis());*/
                    //startActivity(intent);
                    // showAttendanceList(holidayArrayList);
                }
                if (daySpinSelect == false) {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;
                }


            }
        });

        calendar = mRootView.findViewById(R.id.calendar_view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        listRelative = mRootView.findViewById(R.id.listRelative);
        listMainRelative = mRootView.findViewById(R.id.listMainRelative);
        mainRelative = mRootView.findViewById(R.id.mainRelative);
        mainRelative.setOnClickListener(this);
        calendar.setOnClickListener(this);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        list.setLayoutManager(recyclerViewLayoutManager);
        list.setHasFixedSize(true);
        if (isShowCalendar)
        {
            calendarVisible();
        }
        else
        {
            calendarVisibleGone();
        }
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                if (daySpinSelect == false) {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;
                }
                isShowCalendar=false;
                calendarVisibleGone();

            }
        });


        arrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daySpinSelect == false) {
                    listSpinner.setVisibility(View.GONE);
                    daySpinSelect = true;
                }
                isShowCalendar=true;
                calendarVisible();

            }
        });


        calendar.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {


            }

            @Override
            public void onMonthChanged(Date time, TextView btnWeekView, TextView btnMonthView, TextView btnYearView) {
                System.out.println("date time in main::" + time.getDay() + "-" + time.getMonth() + "-" + "-" + time.getYear());
                btnYearView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnMonthView.setBackgroundResource(R.drawable.rounded_rec_slected_calendar);
                btnWeekView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                mEventArrayListFilterList = new ArrayList<>();
                for (int j = 0; j < eventArrayList.size(); j++) {
                    if (currentMonth(time.getMonth()) == Integer.valueOf(eventArrayList.get(j).getMonthNumber()) && (1900 + time.getYear()) == Integer.valueOf(eventArrayList.get(j).getYear())) {
                        mEventArrayListFilterList.add(eventArrayList.get(j));
                    }
                }

                CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
                list.setAdapter(mCustomListAdapter);
                if (mEventArrayListFilterList.size() == 0) {
                    alertRelative.setVisibility(View.VISIBLE);
                } else {
                    alertRelative.setVisibility(View.GONE);

                }

                SimpleDateFormat formatMMM = new SimpleDateFormat("MMMM", Locale.ENGLISH);

                final Date date = new Date(); // your date

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (((1900 + time.getYear()) == calendar.get(Calendar.YEAR)) && (currentMonth(calendar.get(Calendar.MONTH)) == currentMonth(time.getMonth()))) {
                    txtMYW.setText("This Month");

                } else {
                    txtMYW.setText(formatMMM.format(time) + " " + ((1900 + time.getYear())));

                }

                daySpinner.setText("Month View");


            }

            @Override
            public void onWeekChanged(Date time, TextView btnWeekView, TextView btnMonthView, TextView btnYearView) {
                viewType = 2;
                btnYearView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnMonthView.setBackgroundResource(R.drawable.rounded_rec_slected_calendar);
                btnWeekView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                mEventArrayListFilterList = new ArrayList<>();
                for (int j = 0; j < eventArrayList.size(); j++) {
                    if (currentMonth(time.getMonth()) == Integer.valueOf(eventArrayList.get(j).getMonthNumber()) && (1900 + time.getYear()) == Integer.valueOf(eventArrayList.get(j).getYear())) {
                        mEventArrayListFilterList.add(eventArrayList.get(j));
                    }
                }

                CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
                list.setAdapter(mCustomListAdapter);

                SimpleDateFormat formatMMM = new SimpleDateFormat("MMMM", Locale.ENGLISH);

                final Date date = new Date(); // your date

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (((1900 + time.getYear()) == calendar.get(Calendar.YEAR)) && (currentMonth(calendar.get(Calendar.MONTH)) == currentMonth(time.getMonth()))) {
                    txtMYW.setText("This Week");

                } else {
                    txtMYW.setText(formatMMM.format(time) + " " + ((1900 + time.getYear())));

                }

                daySpinner.setText("Week View");
                if (mEventArrayListFilterList.size() == 0) {
                    alertRelative.setVisibility(View.VISIBLE);
                } else {
                    alertRelative.setVisibility(View.GONE);

                }

            }

            @Override
            public void onMonthView(Date time, TextView btnWeekView, TextView btnMonthView, TextView btnYearView) {
                calendarVisibleGone();
                btnYearView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnMonthView.setBackgroundResource(R.drawable.rounded_rec_slected_calendar);
                btnWeekView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                mEventArrayListFilterList = new ArrayList<>();
                final Date date = new Date(); // your date

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                for (int j = 0; j < eventArrayList.size(); j++) {
                    if ((currentMonth(calendar.get(Calendar.MONTH))) == Integer.valueOf(eventArrayList.get(j).getMonthNumber()) && (1900 + time.getYear()) == Integer.valueOf(eventArrayList.get(j).getYear())) {
                        mEventArrayListFilterList.add(eventArrayList.get(j));
                    }
                }

                CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
                list.setAdapter(mCustomListAdapter);
                txtMYW.setText("This Month");
                ListViewCalendar.calendar.next((ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1) + currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1)));

            }

            @Override
            public void onWeekView(Date time, TextView btnWeekView, TextView btnMonthView, TextView btnYearView) {
                System.out.println("week Clicks : ");
                calendarVisibleGone();
                SimpleDateFormat formatEEE = new SimpleDateFormat("EEE", Locale.ENGLISH);
                SimpleDateFormat formatMM = new SimpleDateFormat("MM", Locale.ENGLISH);
                SimpleDateFormat formatDD = new SimpleDateFormat("dd", Locale.ENGLISH);
                SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                btnYearView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnMonthView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnWeekView.setBackgroundResource(R.drawable.rounded_rec_slected_calendar);

                String[] dayOfTheWeek = new String[7];
                String[] month = new String[7];
                String[] year = new String[7];
                String[] dd = new String[7];

                final Date date = new Date(); // your date

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                System.out.println("calendar:" + calendar.get(Calendar.DAY_OF_MONTH) + ":" + calendar.get(Calendar.MONTH) + ":" + calendar.get(Calendar.YEAR));

                String[] days = new String[7];


                for (int i = 0; i < 7; i++) {
                    days[i] = sdf.format(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                    calendar.set(Calendar.DAY_OF_WEEK, i + 1);

                    Date msgDate = new Date();
                    try {

                        msgDate = sdf.parse(calendar.get(Calendar.YEAR) + "-" + currentMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                    } catch (ParseException ex) {
                        Log.e("Date", "Parsing error");

                    }

                    dayOfTheWeek[i] = formatEEE.format(msgDate); // Thu
                    year[i] = formatyyyy.format(msgDate); // yyyy
                    month[i] = formatMM.format(msgDate); // 15
                    dd[i] = formatDD.format(msgDate); // 15
                }
                ListViewCalendar.calendar.nexWeek((ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1) + currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1)));

                mEventArrayListFilterList = new ArrayList<>();
                for (int i = 0; i < dayOfTheWeek.length; i++) {
                    System.out.println("testing");
                    System.out.println("week:**" + dayOfTheWeek[i] + "_year:" + year[i] + "_month:" + month[i] + "_dd:" + dd[i]);
                    for (int j = 0; j < eventArrayList.size(); j++) {
                        if (dd[i].equalsIgnoreCase(eventArrayList.get(j).getDay()) && dayOfTheWeek[i].equalsIgnoreCase(eventArrayList.get(j).getDayOfTheWeek()) && month[i].equalsIgnoreCase(eventArrayList.get(j).getMonthNumber()) && (year[i].equalsIgnoreCase(eventArrayList.get(j).getYear()))) {
                            mEventArrayListFilterList.add(eventArrayList.get(j));
                        }
                    }
                }


                CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
                list.setAdapter(mCustomListAdapter);
                btnYearView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnMonthView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnWeekView.setBackgroundResource(R.drawable.rounded_rec_slected_calendar);
                txtMYW.setText("This Week");
                if (mEventArrayListFilterList.size() == 0) {
                    alertRelative.setVisibility(View.VISIBLE);
                } else {
                    alertRelative.setVisibility(View.GONE);

                }
            }

            @Override
            public void onYearView(Date time, TextView btnWeekView, TextView btnMonthView, TextView btnYearView) {
                viewType = 0;
                btnYearView.setBackgroundResource(R.drawable.rounded_rec_slected_calendar);
                btnMonthView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                btnWeekView.setBackgroundResource(R.drawable.rounded_rec_calendar);
                calendarVisibleGone();
                alertRelative.setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), CalendarYearRecyclerActivity.class);
                intent.putExtra("diffYear", diffYear);
                intent.putExtra("startMonth", startCalendar.getTime().getMonth());
                intent.putExtra("startYear", startCalendar.getTime().getYear() + 1900);
                intent.putExtra("endYear", endCalendar.getTime().getYear() + 1900);
                intent.putExtra("diffMonth", diffMonth);
                intent.putExtra("startTime", startCalendar.getTimeInMillis());
                startActivity(intent);
            }
        });
    }

    public static void calendarVisibleGone() {

        calendar.setVisibility(View.GONE);
        arrow.setVisibility(View.GONE);
        arrowUp.setVisibility(View.VISIBLE);
        arrowUp.setImageResource(R.drawable.up_calendar);
        arrow.setImageResource(R.drawable.down_calendar);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) listRelative.getLayoutParams();
        lp.addRule(RelativeLayout.ABOVE, arrowUp.getId());
        lp.addRule(RelativeLayout.BELOW, listMainRelative.getId());
    }

    public static void calendarVisible() {
        calendar.setVisibility(View.VISIBLE);
        arrow.setVisibility(View.VISIBLE);
        arrow.setImageResource(R.drawable.down_calendar);
        arrowUp.setImageResource(R.drawable.up_calendar);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) listRelative.getLayoutParams();
        lp.addRule(RelativeLayout.ABOVE, arrow.getId());
        lp.addRule(RelativeLayout.BELOW, listMainRelative.getId());
        arrowUp.setVisibility(View.GONE);

    }

    private void callCalendarListMonth() {
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_CALENDAR_MONTH);
            String[] name = {JTAG_ACCESSTOKEN, "user_ids"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccessMonth: ", successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.getString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.getString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
                                    JSONArray dataArray = secobj.optJSONArray("data");
                                    if (dataArray.length() > 0) {
                                        dateArrayList = new ArrayList<>();
                                        holidayArrayList = new ArrayList<>();
                                        eventArrayList = new ArrayList<CalendarModel>();
                                        AppController.dateArrayListYear = new ArrayList<>();
                                        holidayArrayListYear = new ArrayList<>();
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            JSONObject eventObject = dataArray.optJSONObject(i);
                                            CalendarModel model = new CalendarModel();
                                            model.setId(eventObject.optString("id"));
                                            model.setTittle(eventObject.optString("tittle"));
                                            model.setDescription(eventObject.optString("htmldescription"));
                                            model.setVenue(eventObject.optString("venue"));
                                            model.setDaterange(eventObject.optString("daterange"));
                                            model.setWeekRange(eventObject.optString("selectweek"));
                                            model.setEnddate(eventObject.optString("enddate"));
                                            model.setEndDateList(eventObject.optString("enddate"));
                                            model.setEndDateNew(eventObject.optString("enddate"));
                                            model.setDate(eventObject.optString("date"));
                                            model.setStartDateNew(eventObject.optString("date"));
                                            model.setHoliday(eventObject.optString("public_hoilday"));
                                            if (eventObject.optString("public_hoilday").equalsIgnoreCase("1")) {
                                                holidayArrayList.add(eventObject.optString("date"));
                                                holidayArrayListYear.add(eventObject.optString("date"));
                                            }
                                            model.setDateCalendar(eventObject.optString("date"));
                                            model.setStarttime(eventObject.optString("starttime"));
                                            model.setEndtime(eventObject.optString("endtime"));
                                            model.setStartDatetime(eventObject.optString("date") + " " + eventObject.optString("starttime"));
                                            model.setEndDatetime(eventObject.optString("enddate") + " " + eventObject.optString("endtime"));
                                            model.setAllday(eventObject.optString("isAllday"));
                                            String mDate = model.getDate();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgDate = new Date();
                                            try {

                                                msgDate = sdf.parse(mDate);
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String day = (String) DateFormat.format("dd", msgDate); // 20
                                            if (day.endsWith("1") && !day.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'st' MMMM");

                                            } else if (day.endsWith("2") && !day.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'nd' MMMM");

                                            } else if (day.endsWith("3") && !day.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("EEEE  d'rd' MMMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("EEEE  d'th' MMMM");

                                            String newday = sdfcalender.format(msgDate);
                                            model.setDate(newday);
                                            /*********End Date****************/
                                            String mEndDate = model.getEndDateList();
                                            SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgEndDate = new Date();
                                            try {

                                                msgEndDate = sdfEnd.parse(mEndDate);
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String dayEnd = (String) DateFormat.format("dd", msgEndDate); // 20
                                            if (dayEnd.endsWith("1") && !dayEnd.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'st' MMMM");

                                            } else if (dayEnd.endsWith("2") && !dayEnd.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'nd' MMMM");

                                            } else if (dayEnd.endsWith("3") && !dayEnd.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("EEEE  d'rd' MMMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("EEEE  d'th' MMMM");

                                            String newEndday = sdfcalender.format(msgEndDate);
                                            model.setEndDateList(newEndday);
                                            /*********End Date****************/
                                            /************** start date new*************/

                                            String mStartDateNew = model.getStartDateNew();
                                            SimpleDateFormat sdfstartDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgStartDate = new Date();
                                            try {

                                                msgStartDate = sdfstartDate.parse(mStartDateNew);//
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String dayStart = (String) DateFormat.format("dd", msgDate); // 20
                                            if (dayStart.endsWith("1") && !dayStart.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'st' MMM");

                                            } else if (dayStart.endsWith("2") && !dayStart.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'nd' MMM");

                                            } else if (dayStart.endsWith("3") && !dayStart.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("EEEE  d'rd' MMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("EEEE  d'th' MMM");

                                            String newdayStart = sdfcalender.format(msgStartDate);
                                            model.setStartDateNew(newdayStart);

                                            /************************end of start date new*****************/

                                            /************** end date new*************/

                                            String mEndDateNew = model.getEndDateNew();
                                            SimpleDateFormat sdfendDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgEndDateNew = new Date();
                                            try {

                                                msgEndDateNew = sdfendDate.parse(mEndDateNew);//
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String dayEndNew = (String) DateFormat.format("dd", msgEndDateNew); // 20
                                            if (dayEndNew.endsWith("1") && !dayEndNew.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("d'st' MMM");

                                            } else if (dayEndNew.endsWith("2") && !dayEndNew.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("d'nd' MMM");

                                            } else if (dayEndNew.endsWith("3") && !dayEndNew.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("d'rd' MMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("d'th' MMM");

                                            String newdayEnd = sdfcalender.format(msgEndDateNew);
                                            model.setEndDateNew(newdayEnd);

                                            /************************ end of end date new*****************/

                                            SimpleDateFormat formatEEE = new SimpleDateFormat("EEE", Locale.ENGLISH);
                                            SimpleDateFormat formatdd = new SimpleDateFormat("dd", Locale.ENGLISH);
                                            SimpleDateFormat formatMMM = new SimpleDateFormat("MMM", Locale.ENGLISH);
                                            SimpleDateFormat formatMM = new SimpleDateFormat("MM", Locale.ENGLISH);
                                            SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                                            Date date = null;
                                            try {
                                                date = sdf.parse(eventObject.optString("date"));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String dayOfTheWeek = formatEEE.format(date); // Thu
                                            String days = formatdd.format(date); // 20
                                            String monthString = formatMMM.format(date); // Jun
                                            String monthNumber = formatMM.format(date); // 06
                                            String year = formatyyyy.format(date); // 2

                                            model.setDayOfTheWeek(dayOfTheWeek);
                                            model.setDay(days);
                                            model.setMonthString(monthString);
                                            model.setMonthNumber(monthNumber);
                                            model.setYear(year);
                                            JSONArray studentArray = eventObject.optJSONArray("students");
                                            ArrayList<StudentDetailModel> mStudentModel = new ArrayList<>();


                                            for (int x = 0; x < studentArray.length(); x++) {
                                                JSONObject obj2 = studentArray.optJSONObject(x);
                                                StudentDetailModel xmodel = new StudentDetailModel();
                                                xmodel.setId(obj2.optString("student_id"));
                                                xmodel.setStudentName(obj2.optString("name"));
                                                mStudentModel.add(xmodel);
                                            }
                                            model.setmStudentModel(mStudentModel);
                                            if (eventObject.optString("daterange").equalsIgnoreCase("")) {
                                                dateArrayList.add(eventObject.optString("date"));

                                                AppController.dateArrayListYear.add(eventObject.optString("date"));

                                            } else {
                                                ArrayList<String> dayRepeatList = new ArrayList<String>();
                                                String[] daterangeListValues = eventObject.optString("daterange").split(",");
                                                String[] weekRangeValues = eventObject.optString("selectweek").split(",");
                                                dateArrayList.add(eventObject.optString("date"));
                                                AppController.dateArrayListYear.add(eventObject.optString("date"));

                                                for (int j = 0; j < daterangeListValues.length; j++) {
                                                    dateArrayList.add(daterangeListValues[j]);
                                                    if (eventObject.optString("public_hoilday").equalsIgnoreCase("1")) {
                                                        holidayArrayList.add(daterangeListValues[j]);
                                                        holidayArrayListYear.add(daterangeListValues[j]);
                                                    }
                                                    AppController.dateArrayListYear.add(daterangeListValues[j]);
//                                                    Date dateRepeat = null;
//                                                    try {
//                                                        dateRepeat = sdf.parse(daterangeListValues[j]);
//                                                    } catch (ParseException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    String dayOfTheWeekRepeat = (String) formatEEE.format(dateRepeat);
//                                                    dayRepeatList.add(dayOfTheWeekRepeat);
                                                }
//                                                Set<String> hs = new HashSet<>();
//                                                hs.addAll(dayRepeatList);
//                                                dayRepeatList.clear();
//                                                dayRepeatList.addAll(hs);
                                                for (int k = 0; k < weekRangeValues.length; k++) {
                                                    switch (weekRangeValues[k]) {
                                                        case "0":
                                                            dayRepeatList.add("Sun");
                                                            break;
                                                        case "1":
                                                            dayRepeatList.add("Mon");
                                                            break;
                                                        case "2":
                                                            dayRepeatList.add("Tue");
                                                            break;
                                                        case "3":
                                                            dayRepeatList.add("Wed");
                                                            break;
                                                        case "4":
                                                            dayRepeatList.add("Thu");
                                                            break;
                                                        case "5":
                                                            dayRepeatList.add("Fri");
                                                            break;
                                                        case "6":
                                                            dayRepeatList.add("Sat");
                                                            break;

                                                    }
                                                }
                                                String dayRepeatString = dayRepeatList.toString().replace("[", "").replace("]", "");

                                                dateArrayList.add(eventObject.optString("enddate"));
                                                AppController.dateArrayListYear.add(eventObject.optString("enddate"));

                                                if (dayRepeatList.size() < 7) {
                                                    model.setDayrange(dayRepeatString);
                                                } else {
                                                    model.setDayrange("Everyday");

                                                }
                                            }
                                            eventArrayList.add(model);


                                        }
                                        if (isShowCalendar) {

                                            calendarVisible();
                                            System.out.println("test1"); //showing
                                        } else {
                                            calendarVisibleGone();
                                            System.out.println("test2"); //hiding
                                        }
                                        callCalendarList();
                                        calendar.calendarRefreshing(mContext);

                                    } else {
                                        callCalendarList();
                                        alertRelative.setVisibility(View.VISIBLE);


                                    }
                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callCalendarListMonth();

                                }
                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callCalendarListMonth();

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void callCalendarList() {
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_CALENDAR);
            String[] name = {JTAG_ACCESSTOKEN, "user_ids"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccessYear: ", successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject obj = new JSONObject(successResponse);
                            String response_code = obj.getString(JTAG_RESPONSECODE);
                            if (response_code.equalsIgnoreCase("200")) {
                                JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                                String status_code = secobj.getString(JTAG_STATUSCODE);
                                if (status_code.equalsIgnoreCase("303")) {
                                    JSONArray dataArray = secobj.optJSONArray("data");
                                    if (dataArray.length() > 0) {
                                        dateArrayList = new ArrayList<>();
                                        holidayArrayList = new ArrayList<>();
                                        eventArrayList = new ArrayList<CalendarModel>();
                                        AppController.dateArrayListYear = new ArrayList<>();
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            JSONObject eventObject = dataArray.optJSONObject(i);
                                            CalendarModel model = new CalendarModel();
                                            model.setId(eventObject.optString("id"));
                                            model.setTittle(eventObject.optString("tittle"));
                                            model.setHoliday(eventObject.optString("public_hoilday"));
                                            model.setDescription(eventObject.optString("htmldescription"));
                                            model.setVenue(eventObject.optString("venue"));
                                            model.setDaterange(eventObject.optString("daterange"));
                                            model.setWeekRange(eventObject.optString("selectweek"));
                                            model.setEnddate(eventObject.optString("enddate"));
                                            model.setEndDateList(eventObject.optString("enddate"));
                                            model.setEndDateNew(eventObject.optString("enddate"));
                                            model.setDate(eventObject.optString("date"));
                                            model.setStartDateNew(eventObject.optString("date"));
                                            model.setDateCalendar(eventObject.optString("date"));
                                            if (eventObject.optString("public_hoilday").equalsIgnoreCase("1")) {
                                                holidayArrayList.add(eventObject.optString("date"));
                                                holidayArrayListYear.add(eventObject.optString("date"));
                                            }
                                            model.setStarttime(eventObject.optString("starttime"));
                                            model.setEndtime(eventObject.optString("endtime"));
                                            model.setStartDatetime(eventObject.optString("date") + " " + eventObject.optString("starttime"));
                                            model.setEndDatetime(eventObject.optString("enddate") + " " + eventObject.optString("endtime"));
                                            model.setAllday(eventObject.optString("isAllday"));
                                            String mDate = model.getDate();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgDate = new Date();
                                            try {

                                                msgDate = sdf.parse(mDate);
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String day = (String) DateFormat.format("dd", msgDate); // 20
                                            if (day.endsWith("1") && !day.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'st' MMMM");

                                            } else if (day.endsWith("2") && !day.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'nd' MMMM");

                                            } else if (day.endsWith("3") && !day.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("EEEE  d'rd' MMMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("EEEE  d'th' MMMM");

                                            String newday = sdfcalender.format(msgDate);
                                            model.setDate(newday);
                                            /*********End Date****************/
                                            String mEndDate = model.getEndDateList();
                                            SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgEndDate = new Date();
                                            try {

                                                msgEndDate = sdfEnd.parse(mEndDate);
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String dayEnd = (String) DateFormat.format("dd", msgEndDate); // 20
                                            if (dayEnd.endsWith("1") && !dayEnd.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'st' MMMM");

                                            } else if (dayEnd.endsWith("2") && !dayEnd.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'nd' MMMM");

                                            } else if (dayEnd.endsWith("3") && !dayEnd.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("EEEE  d'rd' MMMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("EEEE  d'th' MMMM");

                                            String newEndday = sdfcalender.format(msgEndDate);
                                            model.setEndDateList(newEndday);
                                            /*********End Date****************/
                                            /************** start date new*************/

                                            String mStartDateNew = model.getStartDateNew();
                                            SimpleDateFormat sdfstartDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgStartDate = new Date();
                                            try {

                                                msgStartDate = sdfstartDate.parse(mStartDateNew);//
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String dayStart = (String) DateFormat.format("dd", msgDate); // 20
                                            if (dayStart.endsWith("1") && !dayStart.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'st' MMM");

                                            } else if (dayStart.endsWith("2") && !dayStart.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("EEEE  d'nd' MMM");

                                            } else if (dayStart.endsWith("3") && !dayStart.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("EEEE  d'rd' MMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("EEEE  d'th' MMM");

                                            String newdayStart = sdfcalender.format(msgStartDate);
                                            model.setStartDateNew(newdayStart);

                                            /************************end of start date new*****************/


                                            /************** end date new*************/

                                            String mEndDateNew = model.getEndDateNew();
                                            SimpleDateFormat sdfendDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                                            Date msgEndDateNew = new Date();
                                            try {

                                                msgEndDateNew = sdfendDate.parse(mEndDateNew);//
                                            } catch (ParseException ex) {
                                                Log.e("Date", "Parsing error");

                                            }

                                            //String dayOfTheWeek = (String) DateFormat.format("EEEE", msgDate); // Thursday
                                            String dayEndNew = (String) DateFormat.format("dd", msgEndDateNew); // 20
                                            if (dayEndNew.endsWith("1") && !dayEndNew.endsWith("11")) {
                                                sdfcalender = new SimpleDateFormat("d'st' MMM");

                                            } else if (dayEndNew.endsWith("2") && !dayEndNew.endsWith("12")) {
                                                sdfcalender = new SimpleDateFormat("d'nd' MMM");

                                            } else if (dayEndNew.endsWith("3") && !dayEndNew.endsWith("13"))
                                                sdfcalender = new SimpleDateFormat("d'rd' MMM");
                                            else
                                                sdfcalender = new SimpleDateFormat("d'th' MMM");

                                            String newdayEnd = sdfcalender.format(msgEndDateNew);
                                            model.setEndDateNew(newdayEnd);

                                            /************************end of end date new*****************/


                                            SimpleDateFormat formatEEE = new SimpleDateFormat("EEE", Locale.ENGLISH);
                                            SimpleDateFormat formatdd = new SimpleDateFormat("dd", Locale.ENGLISH);
                                            SimpleDateFormat formatMMM = new SimpleDateFormat("MMM", Locale.ENGLISH);
                                            SimpleDateFormat formatMM = new SimpleDateFormat("MM", Locale.ENGLISH);
                                            SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                                            Date date = null;
                                            try {
                                                date = sdf.parse(eventObject.optString("date"));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String dayOfTheWeek = formatEEE.format(date); // Thu
                                            String days = formatdd.format(date); // 20
                                            String monthString = formatMMM.format(date); // Jun
                                            String monthNumber = formatMM.format(date); // 06
                                            String year = formatyyyy.format(date); // 2

                                            model.setDayOfTheWeek(dayOfTheWeek);
                                            model.setDay(days);
                                            model.setMonthString(monthString);
                                            model.setMonthNumber(monthNumber);
                                            model.setYear(year);
                                            JSONArray studentArray = eventObject.optJSONArray("students");
                                            ArrayList<StudentDetailModel> mStudentModel = new ArrayList<>();


                                            for (int x = 0; x < studentArray.length(); x++) {
                                                JSONObject obj2 = studentArray.optJSONObject(x);
                                                StudentDetailModel xmodel = new StudentDetailModel();
                                                xmodel.setId(obj2.optString("student_id"));
                                                xmodel.setStudentName(obj2.optString("name"));
                                                mStudentModel.add(xmodel);
                                            }
                                            model.setmStudentModel(mStudentModel);
//                                            eventArrayList.add(model);
                                            if (eventObject.optString("daterange").equalsIgnoreCase("")) {
                                                dateArrayList.add(eventObject.optString("date"));

                                                AppController.dateArrayListYear.add(eventObject.optString("date"));

                                            } else {
                                                ArrayList<String> dayRepeatList = new ArrayList<String>();
                                                String[] daterangeListValues = eventObject.optString("daterange").split(",");
                                                String[] weekRangeValues = eventObject.optString("selectweek").split(",");
                                                dateArrayList.add(eventObject.optString("date"));

                                                AppController.dateArrayListYear.add(eventObject.optString("date"));
                                                for (int j = 0; j < daterangeListValues.length; j++) {
                                                    dateArrayList.add(daterangeListValues[j]);
                                                    if (eventObject.optString("public_hoilday").equalsIgnoreCase("1")) {
                                                        holidayArrayList.add(daterangeListValues[j]);
                                                        holidayArrayListYear.add(daterangeListValues[j]);
                                                    }
                                                    AppController.dateArrayListYear.add(daterangeListValues[j]);
//                                                    Date dateRepeat = null;
//                                                    try {
//                                                        dateRepeat = sdf.parse(daterangeListValues[j]);
//                                                    } catch (ParseException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    String dayOfTheWeekRepeat = (String) formatEEE.format(dateRepeat);
//                                                    dayRepeatList.add(dayOfTheWeekRepeat);
                                                }
                                                adapter = new MyRecyclerViewAdapter(mContext, data, dateArrayList);
//                                                Set<String> hs = new HashSet<>();
//                                                hs.addAll(dayRepeatList);
//                                                dayRepeatList.clear();
//                                                dayRepeatList.addAll(hs);
                                                for (int k = 0; k < weekRangeValues.length; k++) {
                                                    switch (weekRangeValues[k]) {
                                                        case "0":
                                                            dayRepeatList.add("Sun");
                                                            break;
                                                        case "1":
                                                            dayRepeatList.add("Mon");
                                                            break;
                                                        case "2":
                                                            dayRepeatList.add("Tue");
                                                            break;
                                                        case "3":
                                                            dayRepeatList.add("Wed");
                                                            break;
                                                        case "4":
                                                            dayRepeatList.add("Thu");
                                                            break;
                                                        case "5":
                                                            dayRepeatList.add("Fri");
                                                            break;
                                                        case "6":
                                                            dayRepeatList.add("Sat");
                                                            break;

                                                    }
                                                }
                                                String dayRepeatString = dayRepeatList.toString().replace("[", "").replace("]", "");

                                                dateArrayList.add(eventObject.optString("enddate"));
                                                AppController.dateArrayListYear.add(eventObject.optString("enddate"));
//                                                model.setDayrange(dayRepeatString);

                                                if (dayRepeatList.size() < 7) {
                                                    model.setDayrange(dayRepeatString);
                                                } else {
                                                    model.setDayrange("Everyday");

                                                }
                                            }

                                            eventArrayList.add(model);

                                        }
                                        calendar.calendarRefreshing(mContext);

                                    } else {
                                        alertRelative.setVisibility(View.VISIBLE);

                                    }
                                } else if (status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        status_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callCalendarList();

                                }
                            } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callCalendarList();

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void clearBadge() {
        eventArrayList = new ArrayList<>();

        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_CLEAR_BADGE);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USERS_ID};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    System.out.println("NofifyRes:" + successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                            if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

                                } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    clearBadge();

                                }
                            } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                clearBadge();

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static int currentMonths(int month) {
        String currentMonth = "00";
        switch (month) {
            case 5:
                currentMonth = "00";
                break;
            case 6:
                currentMonth = "01";
                break;
            case 7:
                currentMonth = "02";
                break;
            case 8:
                currentMonth = "03";
                break;
            case 9:
                currentMonth = "04";
                break;
            case 10:
                currentMonth = "05";
                break;
            case 11:
                currentMonth = "06";
                break;
            case 0:
                currentMonth = "07";
                break;
            case 1:
                currentMonth = "08";
                break;
            case 2:
                currentMonth = "09";
                break;
            case 3:
                currentMonth = "10";
                break;
            case 4:
                currentMonth = "11";
                break;

        }
        return Integer.valueOf(currentMonth);
    }

    public static int currentMonth(int month) {
        String currentMonth = "01";
        switch (month) {
            case 0:
                currentMonth = "01";
                break;
            case 1:
                currentMonth = "02";
                break;
            case 2:
                currentMonth = "03";
                break;
            case 3:
                currentMonth = "04";
                break;
            case 4:
                currentMonth = "05";
                break;
            case 5:
                currentMonth = "06";
                break;
            case 6:
                currentMonth = "07";
                break;
            case 7:
                currentMonth = "08";
                break;
            case 8:
                currentMonth = "09";
                break;
            case 9:
                currentMonth = "10";
                break;
            case 10:
                currentMonth = "11";
                break;
            case 11:
                currentMonth = "12";
                break;

        }
        return Integer.valueOf(currentMonth);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        if (position == 1) {
            Log.e("position", "position = 1");
            listSpinner.setVisibility(View.GONE);
            daySpinner.setText("Month View");
            viewType = 1;

            calendarVisibleGone();
            mEventArrayListFilterList = new ArrayList<>();
            final Date date = new Date(); // your date

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            for (int j = 0; j < eventArrayList.size(); j++) {
                if ((currentMonth(calendar.get(Calendar.MONTH))) == Integer.valueOf(eventArrayList.get(j).getMonthNumber()) && (1900 + CustomCalendarView.currentCalendar.getTime().getYear()) == Integer.valueOf(eventArrayList.get(j).getYear())) {
                    mEventArrayListFilterList.add(eventArrayList.get(j));
                }
            }

            CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
            list.setAdapter(mCustomListAdapter);
            txtMYW.setText("This Month");
            ListViewCalendar.calendar.next((ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1) + currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1)));
        } else if (position == 2) {
            Log.e("position", "position = 2");
            listSpinner.setVisibility(View.GONE);
            daySpinner.setText("Week View");

            calendarVisibleGone();
            viewType = 2;
            SimpleDateFormat formatEEE = new SimpleDateFormat("EEE", Locale.ENGLISH);
            SimpleDateFormat formatMM = new SimpleDateFormat("MM", Locale.ENGLISH);
            SimpleDateFormat formatDD = new SimpleDateFormat("dd", Locale.ENGLISH);
            SimpleDateFormat formatyyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


            String[] dayOfTheWeek = new String[7];
            String[] month = new String[7];
            String[] year = new String[7];
            String[] dd = new String[7];

            final Date date = new Date(); // your date

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            String[] days = new String[7];


            for (int i = 0; i < 7; i++) {
                days[i] = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.set(Calendar.DAY_OF_WEEK, i + 1);

                Date msgDate = new Date();
                try {

                    msgDate = sdf.parse(calendar.get(Calendar.YEAR) + "-" + currentMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                } catch (ParseException ex) {
                    Log.e("Date", "Parsing error");

                }

                dayOfTheWeek[i] = formatEEE.format(msgDate); // Thu

                year[i] = formatyyyy.format(msgDate); // yyyy
                month[i] = formatMM.format(msgDate); // 15
                dd[i] = formatDD.format(msgDate); // 15
            }
            ListViewCalendar.calendar.nexWeek((ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1) + currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(currentMonth(calendar.getTime().getMonth()) - 1)));

            mEventArrayListFilterList = new ArrayList<>();
            Log.i("TEST", "testing:::::");
            for (int i = 0; i < dayOfTheWeek.length; i++) {
                System.out.println("week::::" + dayOfTheWeek[i] + "_year:" + year[i] + "_month:" + month[i] + "_dd:" + dd[i]);
                for (int j = 0; j < eventArrayList.size(); j++) {
                    if (dd[i].equalsIgnoreCase(eventArrayList.get(j).getDay())
                            && dayOfTheWeek[i].equalsIgnoreCase(eventArrayList.get(j).getDayOfTheWeek())
                            && month[i].equalsIgnoreCase(eventArrayList.get(j).getMonthNumber())
                            && (year[i].equalsIgnoreCase(eventArrayList.get(j).getYear()))) {
                        mEventArrayListFilterList.add(eventArrayList.get(j));
                    }
                }
            }
            Log.e("TEST", "mEventArrayListFilterList ==== " + mEventArrayListFilterList.toString());


            CustomList mCustomListAdapter = new CustomList(mContext, mEventArrayListFilterList);
            list.setAdapter(mCustomListAdapter);
            txtMYW.setText("This Week");
            if (mEventArrayListFilterList.size() == 0) {
                alertRelative.setVisibility(View.VISIBLE);
            } else {
                alertRelative.setVisibility(View.GONE);

            }
        } else if (position == 0) {
            Log.e("position", "position = 0");
            listSpinner.setVisibility(View.GONE);
            viewType = 0;
            calendarVisibleGone();
            txtMYW.setText("This Year");
            System.out.println("value calendar visible position 0"+isShowCalendar);
//            if (isShowCalendar)
//            {
//                calendarVisible();
//            }
//            else
//            {
//                calendarVisibleGone();
//            }


            alertRelative.setVisibility(View.GONE);
            Intent intent = new Intent(getActivity(), CalendarYearActivity.class);
            intent.putExtra("BUNDLE", (Serializable) AppController.holidayArrayListYear);
            startActivity(intent);
            //intent.putExtra("holidaysArray",holidayArrayList);
           /* intent.putExtra("diffYear", diffYear);
            intent.putExtra("startMonth", startCalendar.getTime().getMonth());
            intent.putExtra("startYear", startCalendar.getTime().getYear() + 1900);
            intent.putExtra("endYear", endCalendar.getTime().getYear() + 1900);

            intent.putExtra("diffMonth", diffMonth);
            intent.putExtra("startTime", startCalendar.getTimeInMillis());*/
            startActivity(intent);
        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {


            AppController.getInstance().getGoogleAnalyticsTracker().set("&uid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().getGoogleAnalyticsTracker().set("&cid", PreferenceManager.getUserId(mContext));
            AppController.getInstance().trackScreenView("Calendar Screen " + PreferenceManager.getUserEmail(mContext) + Calendar.getInstance().getTime());
        }

        if (isShowCalendar)
        {
            calendarVisible();
        }
        else

        {
            calendarVisibleGone();
        }
    }

    public void showAttendanceList(final ArrayList<String> dateArrayList) {
        final Dialog dialogAttendance = new Dialog(mContext, R.style.DialogTheme);
        dialogAttendance.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager manager = (WindowManager) mContext.getSystemService(Activity.WINDOW_SERVICE);
        int width, height;
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        width = point.x;
        height = point.y;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogAttendance.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        dialogAttendance.getWindow().setAttributes(lp);


//        dialogAttendance.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);

        dialogAttendance.getWindow().getAttributes().windowAnimations = R.style.DialogAnimatioUp;
        dialogAttendance.setCancelable(false);
//        dialogAttendance.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogAttendance.setContentView(R.layout.recycle_main_activity);
        ImageView closeImageView = dialogAttendance.findViewById(R.id.closeCalendarYear);
        RecyclerView recycler_view_attendance = dialogAttendance.findViewById(R.id.rvNumbers);

//        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));

       /* recycler_view_attendance.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_attendance.setLayoutManager(llm);*/
        GridLayoutManager recyclerViewLayout = new GridLayoutManager(mContext, 3);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, 3);
        recycler_view_attendance.addItemDecoration(itemDecoration);
        recycler_view_attendance.setLayoutManager(recyclerViewLayout);
        recycler_view_attendance.setAdapter(adapter);

        closeImageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialogAttendance.dismiss();

            }

        });

        dialogAttendance.show();
        dialogAttendance.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialogAttendance.dismiss();
                }
                return true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (AppController.isShowCalendar==true){

            calendarVisibleGone();
            AppController.isShowCalendar=false;
            System.out.println("test1"); //hiding
        }else {
            AppController.isShowCalendar=true;
            calendarVisible();
            System.out.println("test2"); //showing
        }*/
        System.out.println("test3");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShowCalendar) {

            calendarVisible();
            System.out.println("test1"); //showing
        } else {
            calendarVisibleGone();
            System.out.println("test2"); //hiding
        }
        System.out.println("value"+isShowCalendar);
    }
}

