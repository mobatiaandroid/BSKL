package com.mobatia.bskl.activity.datacollection_p2.model;

public class KinModel {
    String id;
    String kin_id;
    String phone;
    String created_at;
    String user_mobile;
    String user_id;
    String code;
    String updated_at;
    String request;
    String email;
    String status;
    String title;
    String name;
    String last_name;
    String relationship;
    String student_id;
    String correspondencemailmerge;
    String reportmailmerge;
    String justcontact;
    boolean IsClicked = false;
    boolean isFullFilled=false;
    String NewData;
    boolean isNewData;
    boolean isConfirmed;

    public boolean isNewData() {
        return isNewData;
    }

    public void setNewData(boolean newData) {
        isNewData = newData;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getNewData() {
        return NewData;
    }

    public void setNewData(String newData) {
        NewData = newData;
    }

    public boolean isFullFilled() {
        return isFullFilled;
    }

    public void setFullFilled(boolean fullFilled) {
        isFullFilled = fullFilled;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKin_id() {
        return kin_id;
    }

    public void setKin_id(String kin_id) {
        this.kin_id = kin_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public boolean isClicked() {
        return IsClicked;
    }

    public void setClicked(boolean clicked) {
        IsClicked = clicked;
    }

    public String getCorrespondencemailmerge() {
        return correspondencemailmerge;
    }

    public void setCorrespondencemailmerge(String correspondencemailmerge) {
        this.correspondencemailmerge = correspondencemailmerge;
    }

    public String getReportmailmerge() {
        return reportmailmerge;
    }

    public void setReportmailmerge(String reportmailmerge) {
        this.reportmailmerge = reportmailmerge;
    }

    public String getJustcontact() {
        return justcontact;
    }

    public void setJustcontact(String justcontact) {
        this.justcontact = justcontact;
    }
}
