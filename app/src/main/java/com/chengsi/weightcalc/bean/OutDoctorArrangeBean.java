/**   
* @Title: OutDoctorDto.java 
* @Package com.superflying.cn.driver.inter 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年9月30日 上午12:20:37 
*/
package com.chengsi.weightcalc.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class OutDoctorArrangeBean implements Serializable, Comparable<OutDoctorArrangeBean>{

	private static final long serialVersionUID = 2235178201255957841L;

	private String record_sn;
	private String doctor_sn;
	private String doctor_name;
	private int surplus_numbers;
	private int total_numbers;
	private String timeCode;
	private String timeName;

	public String getRecord_sn() {
		return record_sn;
	}

	public void setRecord_sn(String record_sn) {
		this.record_sn = record_sn;
	}

	public String getDoctor_sn() {
		return doctor_sn;
	}

	public void setDoctor_sn(String doctor_sn) {
		this.doctor_sn = doctor_sn;
	}

	public String getDoctor_name() {
		return doctor_name;
	}

	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}

	public int getSurplus_numbers() {
		return surplus_numbers;
	}

	public void setSurplus_numbers(int surplus_numbers) {
		this.surplus_numbers = surplus_numbers;
	}

	public int getTotal_numbers() {
		return total_numbers;
	}

	public void setTotal_numbers(int total_numbers) {
		this.total_numbers = total_numbers;
	}

	public String getTimeCode() {
		return timeCode;
	}

	public void setTimeCode(String timeCode) {
		this.timeCode = timeCode;
	}

	public String getTimeName() {
		return timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	@Override
	public int compareTo(OutDoctorArrangeBean another) {
		if (another == null){
			return 1;
		}
		if (TextUtils.isEmpty(this.timeCode) || TextUtils.isEmpty(another.getTimeCode())){
			return 1;
		}
		return this.timeCode.toLowerCase().compareTo(another.getTimeCode().toLowerCase());
	}
}
