package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/24.
 */
public class DailyTaskBean implements Serializable {

    private String content;

    private String createTime;

    private long id;

    private boolean isDeal;

    private long objectId;

    private String patient;

    private String remind;

    private String taskTime;

    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeal() {
        return isDeal;
    }

    public void setIsDeal(boolean isDeal) {
        this.isDeal = isDeal;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
