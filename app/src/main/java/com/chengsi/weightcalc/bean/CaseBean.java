/**   
 * @Title: PatientDto.java 
 * @Package com.superflying.cn.web.sys.action.app 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Chen Meng   
 * @date 2015年7月21日 下午10:06:13 
 * @version V1.0   
 */

package com.chengsi.weightcalc.bean;


import java.io.Serializable;

/**
 * 病历 生殖中心接口使用
 */
public class CaseBean implements Serializable{
	private String name;// 就诊人
	private String complaint;// 主诉
	private String medicalHistory;// 现病史
	private String relative;// 婚育史（近亲结婚）
	private String child;// 婚育史（现有子女）
	private String adopt;// 领养子女”yyyy-mm-dd
	private String lastPregnancy;// 末次妊娠时间
	private String pregnancyLastTime;// 职业
	private String menarche;// 初潮
	private String menstrualCycle;// 月经周期
	private Integer type;// 0妻子,1丈夫

	public String getLastPregnancy() {
		return lastPregnancy;
	}

	public void setLastPregnancy(String lastPregnancy) {
		this.lastPregnancy = lastPregnancy;
	}

	public CaseBean(String name, String complaint, String medicalHistory,
					String relative, String child, String adopt,
					String pregnancyLastTime, String menarche, String menstrualCycle,
					Integer type) {
		super();
		this.name = name;
		this.complaint = complaint;
		this.medicalHistory = medicalHistory;
		this.relative = relative;
		this.child = child;
		this.adopt = adopt;
		this.pregnancyLastTime = pregnancyLastTime;
		this.menarche = menarche;
		this.menstrualCycle = menstrualCycle;
		this.type = type;
	}

	public CaseBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public String getRelative() {
		return relative;
	}

	public void setRelative(String relative) {
		this.relative = relative;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public String getAdopt() {
		return adopt;
	}

	public void setAdopt(String adopt) {
		this.adopt = adopt;
	}

	public String getPregnancyLastTime() {
		return pregnancyLastTime;
	}

	public void setPregnancyLastTime(String pregnancyLastTime) {
		this.pregnancyLastTime = pregnancyLastTime;
	}

	public String getMenarche() {
		return menarche;
	}

	public void setMenarche(String menarche) {
		this.menarche = menarche;
	}

	public String getMenstrualCycle() {
		return menstrualCycle;
	}

	public void setMenstrualCycle(String menstrualCycle) {
		this.menstrualCycle = menstrualCycle;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
