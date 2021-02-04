package com.mobatia.bskl.fragment.attendance.model;

import java.io.Serializable;

/**
 * Created by mobatia on 05/09/18.
 */

public class AttendanceChartListModel implements Serializable {


   String name;
   String count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
