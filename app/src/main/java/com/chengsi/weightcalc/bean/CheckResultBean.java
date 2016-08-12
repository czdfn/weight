package com.chengsi.weightcalc.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by apple on 15/8/25.
 */
public class CheckResultBean implements Serializable {
    private String pId;// 系统患者编号

    private String oId;// 系统周期编号

    private String mCardNo;

    private String appId;//APP用户ID  15@6

    private String checkId;//检查ID

    private Date checkDate;//检查日期

    private String checkType;//检查类型 1：化验检查；2：B超检查，  100：孕激素化验结果接口

    private String checkDoctor;//检查医生

    private String itemCode;//检查项目代码

    private String itemName;//检查项目名称

    private String checkResult;//检查结果

    private String itemUnit;//检查项目单位

    private String resultRange;//检查项目结果范围


    private String bigOvares;//大卵泡数

    private String fsh;//卵泡生成激素

    private String lh;

    private String e2;

    private String p;

    private String t;

    private String prl;

    private String b;

    private String hcg;

    public Float getB() {

        if (TextUtils.isEmpty(b)) {
            return null;
        }
        try {
            return Float.parseFloat(b);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setB(String b) {
        this.b = b;
    }

    public Integer getBigOvares() {
        if (TextUtils.isEmpty(bigOvares)) {
            return null;
        }
        try {
            return Integer.parseInt(bigOvares);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setBigOvares(String bigOvares) {
        this.bigOvares = bigOvares;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Float getE2() {
        if (TextUtils.isEmpty(e2)) {
            return null;
        }
        try {
            return Float.parseFloat(e2);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setE2(String e2) {
        this.e2 = e2;
    }

    public Float getFsh() {
        if (TextUtils.isEmpty(fsh)) {
            return null;
        }
        try {
            return Float.parseFloat(fsh);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setFsh(String fsh) {
        this.fsh = fsh;
    }

    public String getHcg() {
        return hcg;
    }

    public void setHcg(String hcg) {
        this.hcg = hcg;
    }

    public Float getLh() {
        if (TextUtils.isEmpty(lh)) {
            return null;
        }
        try {
            return Float.parseFloat(lh);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setLh(String lh) {
        this.lh = lh;
    }

    public Float getP() {
        if (TextUtils.isEmpty(p)) {
            return null;
        }
        try {
            return Float.parseFloat(p);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setP(String p) {
        this.p = p;
    }

    public Float getPrl() {
        if (TextUtils.isEmpty(prl)) {
            return null;
        }
        try {
            return Float.parseFloat(prl);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setPrl(String prl) {
        this.prl = prl;
    }

    public Float getT() {
        if (TextUtils.isEmpty(t)) {
            return null;
        }
        try {
            return Float.parseFloat(t);
        } catch (Exception e) {
            return 0f;
        }
    }

    public void setT(String t) {
        this.t = t;
    }

    public enum CheckType {
        CHECK_TYPE_HUAYAN("1", "化验检查"), CHECK_TYPE_BCHAO("2", "B超检查"), CHECK_TYPE_YUNJISU("100", "孕激素化验结果");

        private String type, desc;

        CheckType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static CheckType getCheckType(String type) {
            if (TextUtils.isEmpty(type)) {
                return null;
            }
            for (CheckType s : CheckType.values()) {
                if (s.getType().equals(type)) {
                    return s;
                }
            }
            return null;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getmCardNo() {
        return mCardNo;
    }

    public void setmCardNo(String mCardNo) {
        this.mCardNo = mCardNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckDoctor() {
        return checkDoctor;
    }

    public void setCheckDoctor(String checkDoctor) {
        this.checkDoctor = checkDoctor;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getResultRange() {
        return resultRange;
    }

    public void setResultRange(String resultRange) {
        this.resultRange = resultRange;
    }
}
