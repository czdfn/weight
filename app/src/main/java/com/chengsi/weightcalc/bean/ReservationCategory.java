package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by XuYingjian on 15/9/14.
 */
public class ReservationCategory implements Serializable{

    private String clinic_flag;//类别代码
    private boolean check_flag;//是否需要选择医生
    private String clinic_name;//类别名称

    public ReservationCategory(String clinic_flag, String clinic_name, boolean check_flag){
        this.clinic_flag = clinic_flag;
        this.clinic_name = clinic_name;
        this.check_flag = check_flag;
    }

    public String getClinic_flag() {
        return clinic_flag;
    }

    public void setClinic_flag(String clinic_flag) {
        this.clinic_flag = clinic_flag;
    }

    public boolean isCheck_flag() {
        return check_flag;
    }

    public void setCheck_flag(boolean check_flag) {
        this.check_flag = check_flag;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }
}
