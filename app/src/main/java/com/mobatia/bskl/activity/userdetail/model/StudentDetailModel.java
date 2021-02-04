package com.mobatia.bskl.activity.userdetail.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mobatia on 06/09/18.
 */

public class StudentDetailModel implements Serializable {
    String title;
    String value;
    String is_expired;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(String is_expired) {
        this.is_expired = is_expired;
    }
}
