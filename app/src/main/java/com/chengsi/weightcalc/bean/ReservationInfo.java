package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple on 15/8/13.
 */
public class ReservationInfo implements Serializable{
    private long hospitalId;
    private List<ReservationItem> hospitalItemS;
    private List<ReservationType> hospitalTypeS;

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public List<ReservationItem> getHospitalItemS() {
        return hospitalItemS;
    }

    public void setHospitalItemS(List<ReservationItem> hospitalItemS) {
        this.hospitalItemS = hospitalItemS;
    }

    public List<ReservationType> getHospitalTypeS() {
        return hospitalTypeS;
    }

    public void setHospitalTypeS(List<ReservationType> hospitalTypeS) {
        this.hospitalTypeS = hospitalTypeS;
    }
}
