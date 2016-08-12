package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/13.
 */
public class ReservationItem implements Serializable{
    private long hospitalId;
    private long itemId;
    private String itemName;

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
