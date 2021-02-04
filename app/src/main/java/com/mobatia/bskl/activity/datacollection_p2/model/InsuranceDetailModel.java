package com.mobatia.bskl.activity.datacollection_p2.model;

import java.io.Serializable;

public class InsuranceDetailModel implements Serializable {

    String id;
    String student_id;
    String student_name;
    String health_detail;
    String no_personal_accident_insurance;
    String medical_insurence_policy_no;
    String medical_insurence_member_number;
    String medical_insurence_provider;
    String medical_insurence_expiry_date;
    String personal_accident_insurence_policy_no;
    String personal_accident_insurence_provider;
    String personal_acident_insurence_expiry_date;
    String preferred_hospital;
    String status;
    String request;
    String created_at;
    String updated_at;
    String HaveMedInsurance;
    String HavePersonalInsurance;
    String declaration;

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getHaveMedInsurance() {
        return HaveMedInsurance;
    }

    public void setHaveMedInsurance(String haveMedInsurance) {
        HaveMedInsurance = haveMedInsurance;
    }

    public String getHavePersonalInsurance() {
        return HavePersonalInsurance;
    }

    public void setHavePersonalInsurance(String havePersonalInsurance) {
        HavePersonalInsurance = havePersonalInsurance;
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

    public String getHealth_detail() {
        return health_detail;
    }

    public void setHealth_detail(String health_detail) {
        this.health_detail = health_detail;
    }

    public String getNo_personal_accident_insurance() {
        return no_personal_accident_insurance;
    }

    public void setNo_personal_accident_insurance(String no_personal_accident_insurance) {
        this.no_personal_accident_insurance = no_personal_accident_insurance;
    }

    public String getMedical_insurence_policy_no() {
        return medical_insurence_policy_no;
    }

    public void setMedical_insurence_policy_no(String medical_insurence_policy_no) {
        this.medical_insurence_policy_no = medical_insurence_policy_no;
    }

    public String getMedical_insurence_member_number() {
        return medical_insurence_member_number;
    }

    public void setMedical_insurence_member_number(String medical_insurence_member_number) {
        this.medical_insurence_member_number = medical_insurence_member_number;
    }

    public String getMedical_insurence_provider() {
        return medical_insurence_provider;
    }

    public void setMedical_insurence_provider(String medical_insurence_provider) {
        this.medical_insurence_provider = medical_insurence_provider;
    }

    public String getMedical_insurence_expiry_date() {
        return medical_insurence_expiry_date;
    }

    public void setMedical_insurence_expiry_date(String medical_insurence_expiry_date) {
        this.medical_insurence_expiry_date = medical_insurence_expiry_date;
    }

    public String getPersonal_accident_insurence_policy_no() {
        return personal_accident_insurence_policy_no;
    }

    public void setPersonal_accident_insurence_policy_no(String personal_accident_insurence_policy_no) {
        this.personal_accident_insurence_policy_no = personal_accident_insurence_policy_no;
    }

    public String getPersonal_accident_insurence_provider() {
        return personal_accident_insurence_provider;
    }

    public void setPersonal_accident_insurence_provider(String personal_accident_insurence_provider) {
        this.personal_accident_insurence_provider = personal_accident_insurence_provider;
    }

    public String getPersonal_acident_insurence_expiry_date() {
        return personal_acident_insurence_expiry_date;
    }

    public void setPersonal_acident_insurence_expiry_date(String personal_acident_insurence_expiry_date) {
        this.personal_acident_insurence_expiry_date = personal_acident_insurence_expiry_date;
    }

    public String getPreferred_hospital() {
        return preferred_hospital;
    }

    public void setPreferred_hospital(String preferred_hospital) {
        this.preferred_hospital = preferred_hospital;
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
