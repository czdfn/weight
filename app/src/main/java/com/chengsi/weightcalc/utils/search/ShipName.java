package com.chengsi.weightcalc.utils.search;

/**
 * Created by Hook on 2016/2/28.
 */
public class ShipName {
    private String name;
    private String longname;
    private String shortname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Override
    public String toString() {
        return "ShipName{" +
                "name='" + name + '\'' +
                ", longname='" + longname + '\'' +
                ", shortname='" + shortname + '\'' +
                '}';
    }
}
