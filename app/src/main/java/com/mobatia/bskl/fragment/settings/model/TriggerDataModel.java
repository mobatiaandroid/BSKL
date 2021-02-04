package com.mobatia.bskl.fragment.settings.model;

import java.io.Serializable;

public class TriggerDataModel implements Serializable {
    String categoryName;
    boolean checkedCategory;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isCheckedCategory() {
        return checkedCategory;
    }

    public void setCheckedCategory(boolean checkedCategory) {
        this.checkedCategory = checkedCategory;
    }
}
