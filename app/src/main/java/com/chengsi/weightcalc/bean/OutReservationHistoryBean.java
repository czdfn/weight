/**   
* @Title: OutDoctorDto.java 
* @Package com.superflying.cn.driver.inter 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年9月30日 上午12:20:37 
*/
package com.chengsi.weightcalc.bean;

import java.io.Serializable;

public class OutReservationHistoryBean implements Serializable{

	private static final long serialVersionUID = 2235178201255957841L;

	private String record_sn;
	private String patient_id;
	private String request_date;
	private String name;
	private String gh_sequence;
	private String unit_name;
	private String doctor_sn;
	private String doctor_name;
	private String charge_price;
	private float balance;
	private int visit_flag;
	private String visit_name;
	private String order_id;
	private String his_order_id;
	private String gh_date;
	private String pay_time;
	private String time_code;
	private String time_name;
	private int clinic_flag;
	private String unit_type;
	private String social_no;
	private String mobile;
	private String card_no;

	private String remark;

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

	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	public String getRequest_date() {
		return request_date;
	}

	public void setRequest_date(String request_date) {
		this.request_date = request_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGh_sequence() {
		return gh_sequence;
	}

	public void setGh_sequence(String gh_sequence) {
		this.gh_sequence = gh_sequence;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getVisit_flag() {
		return visit_flag;
	}

	public void setVisit_flag(int visit_flag) {
		this.visit_flag = visit_flag;
	}

	public String getVisit_name() {
		if (this.visit_flag == 0){
			this.visit_name = "待支付";
		}else if(this.visit_flag == 1){
			this.visit_name = "挂号成功，已支付";
		}else if(this.visit_flag == 9){
			this.visit_name = "已取消挂号";
		}
		return visit_name;
	}

	public void setVisit_name(String visit_name) {
		this.visit_name = visit_name;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getHis_order_id() {
		return his_order_id;
	}

	public void setHis_order_id(String his_order_id) {
		this.his_order_id = his_order_id;
	}

	public String getGh_date() {
		return gh_date;
	}

	public void setGh_date(String gh_date) {
		this.gh_date = gh_date;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getTime_code() {
		return time_code;
	}

	public void setTime_code(String time_code) {
		this.time_code = time_code;
	}

	public String getTime_name() {
		return time_name;
	}

	public void setTime_name(String time_name) {
		this.time_name = time_name;
	}

	public int getClinic_flag() {
		return clinic_flag;
	}

	public void setClinic_flag(int clinic_flag) {
		this.clinic_flag = clinic_flag;
	}

	public String getUnit_type() {
		return unit_type;
	}

	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}

	public String getSocial_no() {
		return social_no;
	}

	public void setSocial_no(String social_no) {
		this.social_no = social_no;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
