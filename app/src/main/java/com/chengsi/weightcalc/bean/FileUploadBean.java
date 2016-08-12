package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * Created by apple on 15/8/11.
 */
public class FileUploadBean implements Serializable{
    private String origName;
    private String path;

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
