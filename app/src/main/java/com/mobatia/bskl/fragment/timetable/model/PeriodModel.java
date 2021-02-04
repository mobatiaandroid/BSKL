package com.mobatia.bskl.fragment.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PeriodModel implements Serializable {
    String sortname;
    String starttime;
    String endtime;
    String period_id;
    String monday;
    String tuesday;
    String wednesday;
    String thursday;
    String friday;
    int countM;
    int countT;
    int countW;
    int countTh;
    int countF;
    int countBreak;
    ArrayList<DayModel> timeTableDayModel;

    public ArrayList<DayModel> getTimeTableDayModel() {
        return timeTableDayModel;
    }

    public void setTimeTableDayModel(ArrayList<DayModel> timeTableDayModel) {
        this.timeTableDayModel = timeTableDayModel;
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

    public String getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(String period_id) {
        this.period_id = period_id;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public int getCountM() {
        return countM;
    }

    public void setCountM(int countM) {
        this.countM = countM;
    }

    public int getCountT() {
        return countT;
    }

    public void setCountT(int countT) {
        this.countT = countT;
    }

    public int getCountW() {
        return countW;
    }

    public void setCountW(int countW) {
        this.countW = countW;
    }

    public int getCountTh() {
        return countTh;
    }

    public void setCountTh(int countTh) {
        this.countTh = countTh;
    }

    public int getCountF() {
        return countF;
    }

    public void setCountF(int countF) {
        this.countF = countF;
    }

    public int getCountBreak() {
        return countBreak;
    }

    public void setCountBreak(int countBreak) {
        this.countBreak = countBreak;
    }


    ArrayList<DayModel> timeTableListM;
    ArrayList<DayModel> timeTableListT;
    ArrayList<DayModel> timeTableListW;
    ArrayList<DayModel> timeTableListTh;
    ArrayList<DayModel> timeTableListF;
    public ArrayList<DayModel> getTimeTableListM() {
        return timeTableListM;
    }

    public void setTimeTableListM(ArrayList<DayModel> timeTableListM) {
        this.timeTableListM = timeTableListM;
    }

    public ArrayList<DayModel> getTimeTableListT() {
        return timeTableListT;
    }

    public void setTimeTableListT(ArrayList<DayModel> timeTableListT) {
        this.timeTableListT = timeTableListT;
    }

    public ArrayList<DayModel> getTimeTableListW() {
        return timeTableListW;
    }

    public void setTimeTableListW(ArrayList<DayModel> timeTableListW) {
        this.timeTableListW = timeTableListW;
    }

    public ArrayList<DayModel> getTimeTableListTh() {
        return timeTableListTh;
    }

    public void setTimeTableListTh(ArrayList<DayModel> timeTableListTh) {
        this.timeTableListTh = timeTableListTh;
    }

    public ArrayList<DayModel> getTimeTableListF() {
        return timeTableListF;
    }

    public void setTimeTableListF(ArrayList<DayModel> timeTableListF) {
        this.timeTableListF = timeTableListF;
    }
}
