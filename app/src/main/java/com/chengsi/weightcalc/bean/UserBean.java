package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yingjianxu on 15/6/18.
 */
public class UserBean implements Serializable{

    private String phone;
    private String token;
    private String headImg;
    private String idNo;
    private int sex;	// 性别0:无 1：女 2.男
    private String nickname;
    private String realname;
    private String visitCard;
    private DoctorBean doctorBean;// 绑定医院主治医生名称

    private String hxAccount;
    private String hxPwd;
    private HospitalBean hospital;
    private String pinyin;
    private List<HospitalBean> attenHospitalList;
    private List<DoctorBean> attenDoctorList;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHxAccount() {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount) {
        this.hxAccount = hxAccount;
    }

    public String getHxPwd() {
        return hxPwd;
    }

    public void setHxPwd(String hxPwd) {
        this.hxPwd = hxPwd;
    }

    public HospitalBean getHospital() {
        return hospital;
    }

    public void setHospital(HospitalBean hospital) {
        this.hospital = hospital;
    }

    public List<HospitalBean> getAttenHospitalList() {
        return attenHospitalList;
    }

    public void setAttenHospitalList(List<HospitalBean> attenHospitalList) {
        this.attenHospitalList = attenHospitalList;
    }

    public List<DoctorBean> getAttenDoctorList() {
        if (attenDoctorList == null){
            attenDoctorList = new ArrayList<>();
        }
        return attenDoctorList;
    }

    public void setAttenDoctorList(List<DoctorBean> attenDoctorList) {
        this.attenDoctorList = attenDoctorList;
    }

    public String getVisitCard() {
        return visitCard;
    }

    public void setVisitCard(String visitCard) {
        this.visitCard = visitCard;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public DoctorBean getDoctorBean() {
        return doctorBean;
    }

    public void setDoctorBean(DoctorBean doctorBean) {
        this.doctorBean = doctorBean;
    }
}
