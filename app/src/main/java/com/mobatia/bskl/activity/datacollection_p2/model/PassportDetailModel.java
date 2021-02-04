package com.mobatia.bskl.activity.datacollection_p2.model;

import java.io.Serializable;

public class PassportDetailModel implements Serializable {
    String id;
    String student_id;
    String student_name;
    String passport_number;
    String nationality;
    String passport_image;
    String date_of_issue;
    String expiry_date;
    String visa_permit_no;
    String visa_permit_expiry_date;
    String visa_image;
    String photo_no_consent;
    String status;
    String request;
    String created_at;
    String updated_at;
    String IsNational;
    String visa;
    String visa_image_path;
    String passport_image_path;
    String visa_image_name;
    String passport_image_name;
    String passport_expired;
    String visa_expired;
    boolean isPassportDateChanged;
    boolean isVisaDateChanged;

    public boolean isPassportDateChanged() {
        return isPassportDateChanged;
    }

    public void setPassportDateChanged(boolean passportDateChanged) {
        isPassportDateChanged = passportDateChanged;
    }

    public boolean isVisaDateChanged() {
        return isVisaDateChanged;
    }

    public void setVisaDateChanged(boolean visaDateChanged) {
        isVisaDateChanged = visaDateChanged;
    }

    public String getVisa_expired() {
        return visa_expired;
    }

    public void setVisa_expired(String visa_expired) {
        this.visa_expired = visa_expired;
    }

    public String getPassport_expired() {
        return passport_expired;
    }

    public void setPassport_expired(String passport_expired) {
        this.passport_expired = passport_expired;
    }

    public String getVisa_image_path() {
        return visa_image_path;
    }

    public void setVisa_image_path(String visa_image_path) {
        this.visa_image_path = visa_image_path;
    }

    public String getPassport_image_path() {
        return passport_image_path;
    }

    public void setPassport_image_path(String passport_image_path) {
        this.passport_image_path = passport_image_path;
    }

    public String getVisa_image_name() {
        return visa_image_name;
    }

    public void setVisa_image_name(String visa_image_name) {
        this.visa_image_name = visa_image_name;
    }

    public String getPassport_image_name() {
        return passport_image_name;
    }

    public void setPassport_image_name(String passport_image_name) {
        this.passport_image_name = passport_image_name;
    }

    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getIsNational() {
        return IsNational;
    }

    public void setIsNational(String isNational) {
        IsNational = isNational;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getPassport_number() {
        return passport_number;
    }

    public void setPassport_number(String passport_number) {
        this.passport_number = passport_number;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassport_image() {
        return passport_image;
    }

    public void setPassport_image(String passport_image) {
        this.passport_image = passport_image;
    }

    public String getDate_of_issue() {
        return date_of_issue;
    }

    public void setDate_of_issue(String date_of_issue) {
        this.date_of_issue = date_of_issue;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getVisa_permit_no() {
        return visa_permit_no;
    }

    public void setVisa_permit_no(String visa_permit_no) {
        this.visa_permit_no = visa_permit_no;
    }

    public String getVisa_permit_expiry_date() {
        return visa_permit_expiry_date;
    }

    public void setVisa_permit_expiry_date(String visa_permit_expiry_date) {
        this.visa_permit_expiry_date = visa_permit_expiry_date;
    }

    public String getVisa_image() {
        return visa_image;
    }

    public void setVisa_image(String visa_image) {
        this.visa_image = visa_image;
    }

    public String getPhoto_no_consent() {
        return photo_no_consent;
    }

    public void setPhoto_no_consent(String photo_no_consent) {
        this.photo_no_consent = photo_no_consent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
