package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/8/13.
 */
public class OutReservationInfo implements Serializable{
    private List<ReservationCategory> clincList;
    private List<ReservationDepartment> unitList;

    public OutReservationInfo(){
        clincList = new ArrayList<>();
        ReservationCategory c1 = new ReservationCategory("0", "普通", false);
        ReservationCategory c2 = new ReservationCategory("1", "专家", true);
        clincList.add(c1);
        clincList.add(c2);
        unitList = new ArrayList<>();
        ReservationDepartment d1 = new ReservationDepartment("0", "男科");
        ReservationDepartment d2 = new ReservationDepartment("1", "男科");
        unitList.add(d1);
        unitList.add(d2);
    }

    public List<ReservationCategory> getClincList() {
        return clincList;
    }

    public void setClincList(List<ReservationCategory> clincList) {
        this.clincList = clincList;
    }

    public List<ReservationDepartment> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<ReservationDepartment> unitList) {
        this.unitList = unitList;
    }
}
