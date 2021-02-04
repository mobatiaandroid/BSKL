package com.mobatia.bskl.fragment.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RangeModel implements Serializable {


    ArrayList<DayModel> timeTableDayModel;
    ArrayList<DayModel> timeTableDayFridayModel;
    ArrayList<TimeTableModel> timeTableModel;
    ArrayList<PeriodModel> periodModel;

    public ArrayList<DayModel> getTimeTableDayFridayModel() {
        return timeTableDayFridayModel;
    }
    public void setTimeTableDayFridayModel(ArrayList<DayModel> timeTableDayFridayModel) {
        this.timeTableDayFridayModel = timeTableDayFridayModel;
    }

    public ArrayList<TimeTableModel> getTimeTableModel() {
        return timeTableModel;
    }

    public void setTimeTableModel(ArrayList<TimeTableModel> timeTableModel) {
        this.timeTableModel = timeTableModel;
    }

    public ArrayList<PeriodModel> getPeriodModel() {
        return periodModel;
    }

    public void setPeriodModel(ArrayList<PeriodModel> periodModel) {
        this.periodModel = periodModel;
    }

    public ArrayList<DayModel> getTimeTableDayModel() {
        return timeTableDayModel;
    }

    public void setTimeTableDayModel(ArrayList<DayModel> timeTableDayModel) {
        this.timeTableDayModel = timeTableDayModel;
    }

}
