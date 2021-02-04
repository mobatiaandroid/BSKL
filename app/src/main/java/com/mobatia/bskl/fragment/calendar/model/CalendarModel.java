package com.mobatia.bskl.fragment.calendar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by krishnaraj on 10/07/18.
 */

public class CalendarModel implements Serializable {
    String id;
    String tittle;
    String description;
    String dayOfTheWeek;
    String day  ;
    String monthString  ;
    String monthNumber ;
    String year;
    String dateCalendar;
    String enddate;
    String daterange;
    String dayrange;
    String weekRange;

    public String getEndDateList() {
        return endDateList;
    }

    public void setEndDateList(String endDateList) {
        this.endDateList = endDateList;
    }

    String endDateList;

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    String holiday;

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    String venue;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    String studentName;
    String date;
    String starttime;
    String startDatetime;
    String endtime;
    String endDatetime;

    public String getStartDateNew() {
        return startDateNew;
    }

    public void setStartDateNew(String startDateNew) {
        this.startDateNew = startDateNew;
    }

    String startDateNew;
    public String getEndDateNew() {
        return endDateNew;
    }

    public void setEndDateNew(String endDateNew) {
        this.endDateNew = endDateNew;
    }

    String endDateNew;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAllday() {
        return allday;
    }

    public void setAllday(String allday) {
        this.allday = allday;
    }

    String allday;


    public ArrayList<StudentDetailModel> getmStudentmodel() {
        return mStudentmodel;
    }

    public void setmStudentmodel(ArrayList<StudentDetailModel> mStudentmodel) {
        this.mStudentmodel = mStudentmodel;
    }

    ArrayList<StudentDetailModel>mStudentmodel;



    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<StudentDetailModel> getmStudentModel() {
        return mStudentModel;
    }

    public void setmStudentModel(ArrayList<StudentDetailModel> mStudentModel) {
        this.mStudentModel = mStudentModel;
    }

    ArrayList<StudentDetailModel>mStudentModel;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDateCalendar() {
        return dateCalendar;
    }

    public void setDateCalendar(String dateCalendar) {
        this.dateCalendar = dateCalendar;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDaterange() {
        return daterange;
    }

    public void setDaterange(String daterange) {
        this.daterange = daterange;
    }

    public String getDayrange() {
        return dayrange;
    }

    public void setDayrange(String dayrange) {
        this.dayrange = dayrange;
    }

    public String getWeekRange() {
        return weekRange;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
    }
}
