package com.mobatia.bskl.activity.datacollection_p2.model;

import java.util.ArrayList;

public class ContactTypeModel {
    String type;
    ArrayList<GlobalListSirname> mGlobalSirnameArray;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public ArrayList<GlobalListSirname> getmGlobalSirnameArray() {
        return mGlobalSirnameArray;
    }

    public void setmGlobalSirnameArray(ArrayList<GlobalListSirname> mGlobalSirnameArray) {
        this.mGlobalSirnameArray = mGlobalSirnameArray;
    }
}
