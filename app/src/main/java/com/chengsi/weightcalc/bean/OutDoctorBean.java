/**   
* @Title: OutDoctorDto.java 
* @Package com.superflying.cn.driver.inter 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年9月30日 上午12:20:37 
*/
package com.chengsi.weightcalc.bean;

import java.io.Serializable;

public class OutDoctorBean implements Serializable{

	private static final long serialVersionUID = 2235178201255957841L;

	private String record_sn;
	private String doctor_sn;
	private String doctor_name;
	private String charge_price;
	private String headImg;


	private String duty;
	private String brief;
	/**
	 * @return the record_sn
	 */
	public String getRecord_sn() {
		return record_sn;
	}
	/**
	 * @param record_sn the record_sn to set
	 */
	public void setRecord_sn(String record_sn) {
		this.record_sn = record_sn;
	}
	/**
	 * @return the doctor_sn
	 */
	public String getDoctor_sn() {
		return doctor_sn;
	}
	/**
	 * @param doctor_sn the doctor_sn to set
	 */
	public void setDoctor_sn(String doctor_sn) {
		this.doctor_sn = doctor_sn;
	}
	/**
	 * @return the doctor_name
	 */
	public String getDoctor_name() {
		return doctor_name;
	}
	/**
	 * @param doctor_name the doctor_name to set
	 */
	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}
	public String getCharge_price() {
		return charge_price;
	}
	/**
	 * @param charge_price the charge_price to set
	 */
	public void setCharge_price(String charge_price) {
		this.charge_price = charge_price;
	}
	/**
	 * @return the duty
	 */
	public String getDuty() {
		return duty;
	}
	/**
	 * @param duty the duty to set
	 */
	public void setDuty(String duty) {
		this.duty = duty;
	}
	/**
	 * @return the brief
	 */
	public String getBrief() {
		return brief;
	}
	/**
	 * @param brief the brief to set
	 */
	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
}
