package com.mobatia.bskl.fragment.absence.model;

import java.io.Serializable;

/**
 * Created by krishnaraj on 27/08/18.
 */

public class StudentModel  implements Serializable {
    public static final int VIEW_TYPE1=1;// present / normal absence with AR register
    public static final int VIEW_TYPE2=2;// unauthorized absence with reply (3 difference colors)
    public static final int VIEW_TYPE3=3;//unauthorized absence ui of 3 buttons
    public static final int VIEW_TYPE4=4;
    String abscenceId;//attendance Id
    String absenceCodeId;
    String abRegister;
    String isPresent;
    String isLate;
    String date;
    String schoolId;
    String status;
    String parent_id;
    String parent_name;
    String app_updated_on;

    /****above keys are for absence safe guarding which uses some keys of student****/

    String mId;
    String mName;
    String mClass;
    String mSection;
    String progressreport;
    String alumini;
    String goingStatus;
    String mPhoto;
    String mHouse;
    String viewType;

    public String getRegistrationComment() {
        return registrationComment;
    }

    public void setRegistrationComment(String registrationComment) {
        this.registrationComment = registrationComment;
    }

    String registrationComment;


    public String getGoingStatus() {
        return goingStatus;
    }

    public void setGoingStatus(String goingStatus) {
        this.goingStatus = goingStatus;
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmHouse() {
        return mHouse;
    }

    public void setmHouse(String mHouse) {
        this.mHouse = mHouse;
    }

    public String getmSection() {
        return mSection;
    }

    public void setmSection(String mSection) {
        this.mSection = mSection;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }


    public String getProgressreport() {
        return progressreport;
    }

    public void setProgressreport(String progressreport) {
        this.progressreport = progressreport;
    }

    public String getAlumini() {
        return alumini;
    }

    public void setAlumini(String alumini) {
        this.alumini = alumini;
    }


    public String getAbscenceId() {
        return abscenceId;
    }

    public void setAbscenceId(String abscenceId) {
        this.abscenceId = abscenceId;
    }

    public String getAbsenceCodeId() {
        return absenceCodeId;
    }

    public void setAbsenceCodeId(String absenceCodeId) {
        this.absenceCodeId = absenceCodeId;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApp_updated_on() {
        return app_updated_on;
    }

    public void setApp_updated_on(String app_updated_on) {
        this.app_updated_on = app_updated_on;
    }

    public String getIsLate() {
        return isLate;
    }

    public void setIsLate(String isLate) {
        this.isLate = isLate;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getAbRegister() {
        return abRegister;
    }

    public void setAbRegister(String abRegister) {
        this.abRegister = abRegister;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
}
