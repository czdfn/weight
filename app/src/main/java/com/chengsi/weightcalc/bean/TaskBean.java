package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.Date;

/**
 *每日任务及治疗历史
 *已完成的任务是治疗历史
 */
public class TaskBean implements Serializable{
	private static final long serialVersionUID = 6009953270006512731L;
	private long id;
	private Date taskTime;
	private String content;
	private String remind;
	
	private String type;
	
	private Long objectId;
	
	private Date createTime;
	private Boolean isDeal;
	
	private Date dealTime;
	private Integer pushStatus;//null 或 0未推送， 1推送失败，200推送成功
	
	private Integer pushCount;//推送次数
	
	private Date pushTime;//上次推送时间
	private Long pushMills;//上次推送毫秒

	public Date getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(Boolean isDeal) {
		this.isDeal = isDeal;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Integer getPushCount() {
		return pushCount;
	}

	public void setPushCount(Integer pushCount) {
		this.pushCount = pushCount;
	}
	
	public void setPushCountPlus() {
		if(null == this.pushCount){
			this.pushCount = 1;
		}else{
			this.pushCount++;
		}
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public Long getPushMills() {
		return pushMills;
	}

	public void setPushMills(Long pushMills) {
		this.pushMills = pushMills;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}