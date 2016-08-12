package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.List;

public class TreatHistoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3239343826466539952L;

	private String date;
	private List<ObjectType> objectList;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<ObjectType> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<ObjectType> objectList) {
		this.objectList = objectList;
	}

}
