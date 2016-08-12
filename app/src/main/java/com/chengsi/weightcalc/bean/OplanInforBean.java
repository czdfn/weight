package com.chengsi.weightcalc.bean;

// default package

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 12、周期信息接口
 */
public class OplanInforBean implements Serializable {
    private static final long serialVersionUID = 1402341713312460109L;
    private long id;
    private String pId;// 系统患者编号

    private String oId;// 系统周期编号

    private String mCardNo;

    private String appId;// APP用户ID 15@6--医院id@用户id

    private Date startDate; // 周期开始日期

    private Date endDate; // 周期结束日期

    private String opsName; // 周期方案

    private String doctorName; // 主治医生
    private String medicinePlan;// 用药方案

    private boolean isHidden;

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public List<TreatHistoryBean> treatHistory;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOpsName() {
        return opsName;
    }

    public void setOpsName(String opsName) {
        this.opsName = opsName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicinePlan() {
        return medicinePlan;
    }

    public void setMedicinePlan(String medicinePlan) {
        this.medicinePlan = medicinePlan;
    }

    public String getMCardNo() {
        return mCardNo;
    }

    public void setMCardNo(String mCardNo) {
        this.mCardNo = mCardNo;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getOId() {
        return oId;
    }

    public void setOId(String oId) {
        this.oId = oId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getmCardNo() {
        return mCardNo;
    }

    public void setmCardNo(String mCardNo) {
        this.mCardNo = mCardNo;
    }

    public List<TreatHistoryBean> getTreatHistory() {
        return treatHistory;
    }

    public void setTreatHistory(List<TreatHistoryBean> treatHistory) {
        this.treatHistory = treatHistory;
    }
}