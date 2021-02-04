package com.mobatia.bskl.activity.home.model;

import java.io.Serializable;

public class TimeTableStudentSettings implements Serializable {
    String id;
    String student_name;
    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
