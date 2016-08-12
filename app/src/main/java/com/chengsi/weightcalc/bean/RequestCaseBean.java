package com.chengsi.weightcalc.bean;

/**
 * @author Marco 2015年8月24日 下午12:58:42
 * 
 */
public class RequestCaseBean {
	private CaseBean husbandCase;
	private CaseBean wifeCase;
	private String token;

	public CaseBean getHusbandCase() {
		return husbandCase;
	}

	public void setHusbandCase(CaseBean husbandCase) {
		this.husbandCase = husbandCase;
	}

	public CaseBean getWifeCase() {
		return wifeCase;
	}

	public void setWifeCase(CaseBean wifeCase) {
		this.wifeCase = wifeCase;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
