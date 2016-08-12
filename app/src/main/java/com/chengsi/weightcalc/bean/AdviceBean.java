package com.chengsi.weightcalc.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by apple on 15/8/25.
 */
public class AdviceBean implements Serializable {
    private String billingDate;

    private String content;

    private int day;

    private String doctorName;

    private float dose;

    private String executeDate;

    private String frequency;

    private String hospital;

    private long id;

    private String medCard;

    private String num;

    private String patient;

    private String remark;

    private String syncTime;

    private String unit;

    private String usage;

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public float getDose() {
        return dose;
    }

    public void setDose(float dose) {
        this.dose = dose;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }


    /**
     * QOD : 1次/隔日
     * Q8H : 1次/8小时
     * QN  : 1次/晚
     * ONCE: 1次
     * BID : 2次/日
     * TID : 3次/日
     * QD  : 1次/日
     */
    public String getFrequency() {
        if (!TextUtils.isEmpty(frequency)){
            switch (frequency.trim()){
                case "QOD":
                    return "1次/隔日";
                case "Q8H":
                    return "1次/8小时";
                case "QN":
                    return "1次/晚";
                case "ONCE":
                    return "1次";
                case "BID":
                    return "2次/日";
                case "TID":
                    return "3次/日";
                case "QD":
                    return "1次/日";
            }
        }
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedCard() {
        return medCard;
    }

    public void setMedCard(String medCard) {
        this.medCard = medCard;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
