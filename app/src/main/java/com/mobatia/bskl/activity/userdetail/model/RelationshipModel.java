package com.mobatia.bskl.activity.userdetail.model;

import java.io.Serializable;

/**
 * Created by mobatia on 06/09/18.
 */

public class RelationshipModel implements Serializable {

    String relationship;
    String firstname;

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
