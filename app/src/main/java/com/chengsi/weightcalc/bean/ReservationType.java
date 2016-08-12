package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/13.
 */
public class ReservationType implements Serializable{
    private long hospitalId;
    private long typeId;
    private String typeName;

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
