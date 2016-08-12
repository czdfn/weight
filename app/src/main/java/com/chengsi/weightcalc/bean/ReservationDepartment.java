package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by XuYingjian on 15/9/14.
 */
public class ReservationDepartment implements Serializable{

    private String unit_sn;//科室代码
    private String unit_name;//科室名称

    public ReservationDepartment(String unit_sn, String unit_name) {
        this.unit_name = unit_name;
        this.unit_sn = unit_sn;
    }

    public String getUnit_sn() {
        return unit_sn;
    }

    public void setUnit_sn(String unit_sn) {
        this.unit_sn = unit_sn;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
