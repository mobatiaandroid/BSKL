package com.mobatia.bskl.fragment.timetable.model;

import java.io.Serializable;

public class FieldModel implements Serializable {
    String sortname;
    String starttime;
    String endtime;
    String periodId;
    int countBreak;
     String fridyaStartTime;
     String fridayEndTime;
     int fridayBreakCount;

    public String getFridyaStartTime() {
        return fridyaStartTime;
    }

    public void setFridyaStartTime(String fridyaStartTime) {
        this.fridyaStartTime = fridyaStartTime;
    }

    public String getFridayEndTime() {
        return fridayEndTime;
    }

    public void setFridayEndTime(String fridayEndTime) {
        this.fridayEndTime = fridayEndTime;
    }

    public int getFridayBreakCount() {
        return fridayBreakCount;
    }

    public void setFridayBreakCount(int fridayBreakCount) {
        this.fridayBreakCount = fridayBreakCount;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
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

    public int getCountBreak() {
        return countBreak;
    }

    public void setCountBreak(int countBreak) {
        this.countBreak = countBreak;
    }
}
