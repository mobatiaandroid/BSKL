package com.mobatia.bskl.fragment.calendar.model;

import java.io.Serializable;

/**
 * Created by krishnaraj on 20/07/18.
 */

public class StudentDetailModel implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    private String studentName;
}
