package com.mobatia.bskl.activity.login.model;

import java.io.Serializable;

/**
 * Created by krishnaraj on 03/08/18.
 */

public class CodeModel implements Serializable {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    String dial_code;
    String code;


}
