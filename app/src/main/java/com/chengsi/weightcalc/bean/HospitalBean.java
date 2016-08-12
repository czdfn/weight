package com.chengsi.weightcalc.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple on 15/8/4.
 */
public class HospitalBean implements Serializable{

    private Long id;
    private String brief;// 简介
    private String serviceItem;// 服务项目
    private String city;// 所在城市
    private String name;// 中心名称
    private String abbreviation;//简称
    private String imgUrl;
    private String pinyin;
    private String website;
    private String website2;
    private String website3;
    private List<DoctorBean> doctors;
    private boolean attented;
    private Boolean isOpen;
    private Boolean isOutRegister;//是否从外部挂号
    private String provice;//省
    private String longitude;//经度
    private String latitude;//纬度
    private String cityCode;
    private int popularity;
    private int cooperativeType;
    private String address;
    private String transportation;
    private String reservation_url;

    public Boolean isOutRegister() {
        return isOutRegister == null ? false : isOutRegister;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getCooperativeType() {
        return cooperativeType;
    }

    public void setCooperativeType(int cooperativeType) {
        this.cooperativeType = cooperativeType;
    }



    public String getCityCode() {
        return cityCode;
    }

    public Boolean isOpen() {
        return isOpen;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(String serviceItem) {
        this.serviceItem = serviceItem;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DoctorBean> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorBean> doctors) {
        this.doctors = doctors;
    }

    public boolean isAttented() {
        return attented;
    }

    public void setAttented(boolean attented) {
        this.attented = attented;
    }

    public Boolean getIsOpen() {
        return isOpen == null ? false : isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsOutRegister() {
        return isOutRegister;
    }

    public void setIsOutRegister(Boolean isOutRegister) {
        this.isOutRegister = isOutRegister;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAbbreviation() {
        if(TextUtils.isEmpty(abbreviation)){
            return this.name;
        }
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getReservation_url() {
        return reservation_url;
    }

    public void setReservation_url(String reservation_url) {
        this.reservation_url = reservation_url;
    }

    public String getWebsite2() {
        return website2;
    }

    public void setWebsite2(String website2) {
        this.website2 = website2;
    }

    public String getWebsite3() {
        return website3;
    }

    public void setWebsite3(String website3) {
        this.website3 = website3;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (!(o instanceof HospitalBean)){
            return false;
        }
        HospitalBean other = (HospitalBean) o;
        return this.id == other.getId();
    }
}
