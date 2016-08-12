package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * @author Marco 2015年8月25日 上午11:43:19 建档记录
 */
public class ArchiveHistoryBean implements Serializable{

    private Long logId;
    private String hospitalName;
    private Long hospitalId;
    private String createTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
