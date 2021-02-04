package com.mobatia.bskl.activity.datacollection_p2.model;

import java.io.Serializable;

public class InsuranceDataCollectionModel implements Serializable {
    String medicalInsuranceChecked;
    String personalInsuranceChecked;
    String insurancePolicyNumber;
    String insuranceMemberNumber;
    String insuranceProviderNumber;
    String insuranceExpiryDate;
    String personalProviderNumber;
    String personalExpiryDate;
    String personalInsuranceNumber;
    String preferredGHospital;
    String status;
    String request;
    String declaration;

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
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

    public String getMedicalInsuranceChecked() {
        return medicalInsuranceChecked;
    }

    public void setMedicalInsuranceChecked(String medicalInsuranceChecked) {
        this.medicalInsuranceChecked = medicalInsuranceChecked;
    }

    public String getPersonalInsuranceChecked() {
        return personalInsuranceChecked;
    }

    public void setPersonalInsuranceChecked(String personalInsuranceChecked) {
        this.personalInsuranceChecked = personalInsuranceChecked;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public String getInsuranceMemberNumber() {
        return insuranceMemberNumber;
    }

    public void setInsuranceMemberNumber(String insuranceMemberNumber) {
        this.insuranceMemberNumber = insuranceMemberNumber;
    }

    public String getInsuranceProviderNumber() {
        return insuranceProviderNumber;
    }

    public void setInsuranceProviderNumber(String insuranceProviderNumber) {
        this.insuranceProviderNumber = insuranceProviderNumber;
    }

    public String getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(String insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public String getPersonalProviderNumber() {
        return personalProviderNumber;
    }

    public void setPersonalProviderNumber(String personalProviderNumber) {
        this.personalProviderNumber = personalProviderNumber;
    }

    public String getPersonalExpiryDate() {
        return personalExpiryDate;
    }

    public void setPersonalExpiryDate(String personalExpiryDate) {
        this.personalExpiryDate = personalExpiryDate;
    }

    public String getPersonalInsuranceNumber() {
        return personalInsuranceNumber;
    }

    public void setPersonalInsuranceNumber(String personalInsuranceNumber) {
        this.personalInsuranceNumber = personalInsuranceNumber;
    }

    public String getPreferredGHospital() {
        return preferredGHospital;
    }

    public void setPreferredGHospital(String preferredGHospital) {
        this.preferredGHospital = preferredGHospital;
    }
}
