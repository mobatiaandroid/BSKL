package com.mobatia.bskl.fragment.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DayModel implements Serializable {
    String id;
    String day;
    String period_id;
    int isBreak;
    String sortname;
    String starttime;
    String endtime;
    String subject_name;
    String student_id;
    String staff;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(String period_id) {
        this.period_id = period_id;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

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

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }



    public int getIsBreak() {
        return isBreak;
    }

    public void setIsBreak(int isBreak) {
        this.isBreak = isBreak;
    }

}
