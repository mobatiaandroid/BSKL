package com.mobatia.bskl.fragment.calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.calendar.adapter.CustomList;
import com.mobatia.bskl.fragment.calendar.calendarutils.CustomCalendarView;
import com.mobatia.bskl.recyclerviewmanager.RecyclerItemListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.mobatia.bskl.fragment.calendar.ListViewCalendar.calendarVisibleGone;

public class CalendarYearActivity extends Activity {

    Context mContext;
    MyRecyclerViewAdapter adapter;
    Boolean isCalendarVisible = false;
    TextView titleTxt;
    ImageView closeCalendarYear;
    private String[] dayNames = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
    private String[] monthNames = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_main_activity);
        mContext=this;
        titleTxt=(TextView)findViewById(R.id.titleTxt);
        closeCalendarYear=(ImageView)findViewById(R.id.closeCalendarYear);
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat year_date = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String yearName = year_date.format(cal.getTime());
        final int currentYearNumber = Integer.parseInt(yearName);

        SimpleDateFormat month_date = new SimpleDateFormat("MM", Locale.ENGLISH);
        String currentMonthName = month_date.format(cal.getTime());
        int currentMonthNumber = Integer.parseInt(currentMonthName); // Device current month
        closeCalendarYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        String titleString = "";
        if (currentMonthNumber<7) { // JAN - JUL
            titleString = (currentYearNumber-1)+" - "+currentYearNumber;
            titleTxt.setText(titleString);
        }else {                     // AUG - DEC
            titleString = currentYearNumber+" - "+(currentYearNumber+1);
            titleTxt.setText(titleString);

        }
      //  Log.d("LOG11","titleString= "+titleString);

        final String[] data = {"7", "8", "9", "10", "11", "0", "1", "2", "3", "4", "5", "6",};
        //Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<String> holidaysArray = (ArrayList<String>) getIntent().getSerializableExtra("BUNDLE");
        System.out.println("holiday arraylist"+holidaysArray.size());
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        //recyclerView.setAlpha(0.0f);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data, holidaysArray);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, recyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        int monthNumber = Integer.parseInt(data[position].toString());
                        System.out.println("printed sucessfully");
                        String monthName = monthNames[monthNumber];


                        Calendar cal=Calendar.getInstance();
                        SimpleDateFormat year_date = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                        String yearName = year_date.format(cal.getTime());
                        int actualYearNumber = Integer.parseInt(yearName);
                        int yearNumber = Integer.parseInt(yearName);

                        SimpleDateFormat month_date = new SimpleDateFormat("MM", Locale.ENGLISH);
                        String currentMonthName = month_date.format(cal.getTime());
                        int currentMonthNumber = Integer.parseInt(currentMonthName); // Device current month


                        if (currentMonthNumber<7) { // JAN - JUL
                            if (monthNumber>=7) {
                                yearNumber = actualYearNumber-1;
                            }else {
                                yearNumber = actualYearNumber;
                            }
                        }else {                     // AUG - DEC
                            if (monthNumber>=7) {
                                yearNumber = actualYearNumber;
                            }else {
                                yearNumber = actualYearNumber+1;
                            }
                        }

                        String input_date = "01/"+(monthNumber+1)+"/"+yearNumber; // Since monthNumber=0 => Jan

                        //Log.d("startDay","startDay===="+input_date);

                        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
                        Date dt1 = null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat format2 = new SimpleDateFormat("EEEE",Locale.ENGLISH);
                        String startDay = format2.format(dt1).toUpperCase();

                        int dayNumber = 1;
                        for (int i=0;i<dayNames.length;i++) {
                            if (dayNames[i].equals(startDay)) {
                                dayNumber = i+1;
                                break;
                            }
                        }
                        //Log.d("dayNumber","dayNumber===="+dayNumber);
/*
                        System.out.println("****monthnumber ::"+monthNumber);
                        System.out.println("****monthname ::"+monthName);
                        System.out.println("****yearname ::"+yearNumber);
                        System.out.println("****current ::"+currentYearNumber);
                        System.out.println("****currentmonth ::"+currentMonthName);*/


                        int monthToBePassed = (monthNumber - currentMonthNumber)+1;
                        int yearToBePassed  = (yearNumber-currentYearNumber);
                        ListViewCalendar.calendar.nextView(monthToBePassed,yearToBePassed);






                        finish();
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }




//    public void displayCalendarInMainView(View v) {
//        Log.d("LOG22","displayCalendarInMainView...");
//        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
//        if (isCalendarVisible == true) {
//            isCalendarVisible = false;
//            recyclerView.setAlpha(0.0f);
//        }else {
//            isCalendarVisible = true;
//            recyclerView.setAlpha(1.0f);
//        }
//    }


    public void onBackPressed() {
        ListViewCalendar.listSpinner.setVisibility(View.GONE);
        ListViewCalendar.daySpinner.setText("Month View");
        ListViewCalendar.viewType = 1;

        calendarVisibleGone();
        ListViewCalendar.mEventArrayListFilterList = new ArrayList<>();
        final Date date = new Date(); // your date

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        for (int j = 0; j < ListViewCalendar.eventArrayList.size(); j++) {
            if ((ListViewCalendar.currentMonth(calendar.get(Calendar.MONTH))) == Integer.valueOf(ListViewCalendar.eventArrayList.get(j).getMonthNumber()) && (1900 + CustomCalendarView.currentCalendar.getTime().getYear()) == Integer.valueOf(ListViewCalendar.eventArrayList.get(j).getYear())) {
                ListViewCalendar.mEventArrayListFilterList.add(ListViewCalendar.eventArrayList.get(j));
            }
        }

        CustomList mCustomListAdapter = new CustomList(mContext, ListViewCalendar.mEventArrayListFilterList);
        ListViewCalendar.list.setAdapter(mCustomListAdapter);
        ListViewCalendar.txtMYW.setText("This Month");
        ListViewCalendar.calendar.next((ListViewCalendar.currentMonth(ListViewCalendar.currentMonth(calendar.getTime().getMonth()) - 1) + ListViewCalendar.currentMonth(calendar.getTime().getMonth()) - 1) - (calendar.get(Calendar.MONTH) + ListViewCalendar.currentMonth(ListViewCalendar.currentMonth(calendar.getTime().getMonth()) - 1)));

        finish();
    }
}
