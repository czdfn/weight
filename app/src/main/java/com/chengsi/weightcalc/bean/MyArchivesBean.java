package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/26.
 */
public class MyArchivesBean implements Serializable{
    private UserArchiveBean husbandArchive;
    private UserArchiveBean wifeArchive;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserArchiveBean getHusbandArchive() {
        return husbandArchive;
    }

    public void setHusbandArchive(UserArchiveBean husbandArchive) {
        this.husbandArchive = husbandArchive;
    }

    public UserArchiveBean getWifeArchive() {
        return wifeArchive;
    }

    public void setWifeArchive(UserArchiveBean wifeArchive) {
        this.wifeArchive = wifeArchive;
    }
}
