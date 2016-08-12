package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/17.
 */
public class ChatGroup implements Serializable {
    private String memo;
    private String roomId;//环信群组ID
    private long hospitalId;
    private String roomName;
    private int peopleCount;
    private int peopleLimit;
    private String hospitalName;
    private String joined;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public int getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        final ChatGroup other = (ChatGroup) obj;
        if (roomId == null){
            if (other.roomId != null) return false;
        }
        else if (!roomId.equals(other.roomId)) return false;
        return true;
    }
}
