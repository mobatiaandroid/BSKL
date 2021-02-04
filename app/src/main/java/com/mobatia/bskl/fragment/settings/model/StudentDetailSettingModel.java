package com.mobatia.bskl.fragment.settings.model;

import java.io.Serializable;

/**
 * Created by Aparna on 27/08/18.
 */

public class StudentDetailSettingModel implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlumi() {
        return alumi;
    }

    public void setAlumi(String alumi) {
        this.alumi = alumi;
    }

    String id;
    String alumi;
    String type;
    String applicant;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProgressreport() {
        return progressreport;
    }

    public void setProgressreport(String progressreport) {
        this.progressreport = progressreport;
    }

    String progressreport;
}
