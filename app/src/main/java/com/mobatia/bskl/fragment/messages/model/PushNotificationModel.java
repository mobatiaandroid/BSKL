package com.mobatia.bskl.fragment.messages.model;


import com.mobatia.bskl.fragment.notification.NotificationFragmentPagination;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by krishnaraj on 10/07/18.
 */

public class PushNotificationModel implements Serializable,Comparable<PushNotificationModel> {
    int id;
    String pushid;
    String message;
    String url;
    String favourite;
    boolean isChecked;
    boolean isMarked;
    ArrayList<String> studentArray;
    String status;
    String pushtime;
    ArrayList<PushNotificationModel> mMessageUnreadList;
    String date;
    String push_from;
    String type;
    String student_name;
    String dayOfTheWeek;
    String day;
    String monthString;
    String monthNumber;
    String year;
    String title;
    String user_id;
    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPushtime() {
        return pushtime;
    }

    public void setPushtime(String pushtime) {
        this.pushtime = pushtime;
    }


    //    boolean isMarkAll;
//    boolean isMark;
    public ArrayList<PushNotificationModel> getmMessageUnreadList() {
        return mMessageUnreadList;
    }

    public void setmMessageUnreadList(ArrayList<PushNotificationModel> mMessageUnreadList) {
        this.mMessageUnreadList = mMessageUnreadList;
    }


    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }


    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPush_from() {
        return push_from;
    }

    public void setPush_from(String push_from) {
        this.push_from = push_from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public ArrayList<String> getStudentArray() {
        return studentArray;
    }

    public void setStudentArray(ArrayList<String> studentArray) {
        this.studentArray = studentArray;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(PushNotificationModel o) {
        return getDateTime().compareTo(o.getDateTime());
    }

    /*
    public boolean isMarkAll() {
        return isMarkAll;
    }

    public void setMarkAll(boolean markAll) {
        isMarkAll = markAll;
    }

    public boolean isMark() {
        return isMark;
    }

    public void setMark(boolean mark) {
        isMark = mark;
    }*/
}
