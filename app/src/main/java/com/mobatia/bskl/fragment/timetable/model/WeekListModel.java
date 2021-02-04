package com.mobatia.bskl.fragment.timetable.model;

import java.io.Serializable;

public class WeekListModel implements Serializable {
    String mId;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }



    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }



    String mFileName,mTitle;


}
