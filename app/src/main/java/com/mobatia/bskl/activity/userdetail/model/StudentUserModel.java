package com.mobatia.bskl.activity.userdetail.model;

import java.io.Serializable;

/**
 * Created by krishnaraj on 05/09/18.
 */

public class StudentUserModel implements Serializable{
    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    String studName;
    String classAndSection;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    String photo;
    String stud_id;

    public String getAlumini() {
        return alumini;
    }

    public void setAlumini(String alumini) {
        this.alumini = alumini;
    }

    String alumini;

    public String getClassAndSection() {
        return classAndSection;
    }

    public void setClassAndSection(String classAndSection) {
        this.classAndSection = classAndSection;
    }
}
