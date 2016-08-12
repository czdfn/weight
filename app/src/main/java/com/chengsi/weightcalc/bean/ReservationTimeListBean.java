package com.chengsi.weightcalc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple on 15/8/14.
 */
public class ReservationTimeListBean implements Serializable{
    private List<ReservationTime> arrangeS;

    public List<ReservationTime> getArrangeS() {
        return arrangeS;
    }

    public void setArrangeS(List<ReservationTime> arrangeS) {
        this.arrangeS = arrangeS;
    }
}
