package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/30.
 */
public class GoodNewsBean implements Serializable{
    //孩子数量
    private int count;

    private String createTime;

    //分娩日期
    private String deliver;

    private long hid;

    private String hospAbbreviation;

    private String hospName;

    private long id;

    private String medCard;

    //患者姓名
    private String name;
    //类型 childbirth分娩，pregnant怀孕
    private String type;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public long getHid() {
        return hid;
    }

    public void setHid(long hid) {
        this.hid = hid;
    }

    public String getHospAbbreviation() {
        return hospAbbreviation;
    }

    public void setHospAbbreviation(String hospAbbreviation) {
        this.hospAbbreviation = hospAbbreviation;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedCard() {
        return medCard;
    }

    public void setMedCard(String medCard) {
        this.medCard = medCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription(){
        String str = "";
//        UserBean userBean = MyApplication.getInstance().userManager.getUserBean();
//        if (userBean != null && userBean.getHospital() != null){
//            str += "恭喜" + this.name + "于";
//        }else {
//            str += "恭喜" + this.hospName + "患者" + this.name + "于";
//        }
//        if (this.type != null && this.type.equals("pregnant")){
//            str += this.createTime;
//            str += "成功怀孕！";
//        }else{
//            str += this.deliver;
//            str += "成功生育" + this.count + "个孩子！";
//        }
        return str;
    }
}
