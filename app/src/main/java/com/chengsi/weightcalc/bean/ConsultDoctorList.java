package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jiadao.corelibs.utils.ListUtils;

public class ConsultDoctorList implements Serializable {

	private static final long serialVersionUID = 5232667796823568272L;

	private List<DoctorBean> attentList;
	private List<DoctorBean> myDoctorList;
	private DoctorBean myDoctor;
	public List<DoctorBean> getAttentList() {
		return attentList;
	}
	public void setAttentList(List<DoctorBean> attentList) {
		this.attentList = attentList;
	}
	public List<DoctorBean> getMyDoctorList() {
		return myDoctorList;
	}
	public void setMyDoctorList(List<DoctorBean> myDoctorList) {
		this.myDoctorList = myDoctorList;
	}
	public DoctorBean getMyDoctor() {
		return myDoctor;
	}
	public void setMyDoctor(DoctorBean myDoctor) {
		this.myDoctor = myDoctor;
	}
	public List<DoctorBean> getAllDoctorList(){
		List<DoctorBean> list = new ArrayList<>();
		if (myDoctor != null){
			list.add(myDoctor);
		}
		if (!ListUtils.isEmpty(myDoctorList)){
			list.addAll(myDoctorList);
		}
		if (!ListUtils.isEmpty(attentList)){
			list.addAll(attentList);
		}
		List<DoctorBean> resultList = new ArrayList<>();
		for (DoctorBean doctorBean : list){
			if (!resultList.contains(doctorBean)){
				resultList.add(doctorBean);
			}
		}
		return resultList;
	}
}
