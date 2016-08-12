package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple on 15/8/14.
 */
public class ReservationDoctorListBean implements Serializable{

    private List<DoctorBean> doctorDtoS;
    private List<String> fullTime;
    private String orderTime;

    public List<DoctorBean> getDoctorDtoS() {
        return doctorDtoS;
    }

    public void setDoctorDtoS(List<DoctorBean> doctorDtoS) {
        this.doctorDtoS = doctorDtoS;
    }

    public List<String> getFullTime() {
        return fullTime;
    }

    public void setFullTime(List<String> fullTime) {
        this.fullTime = fullTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
