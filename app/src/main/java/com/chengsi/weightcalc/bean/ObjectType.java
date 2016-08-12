package com.chengsi.weightcalc.bean;

import java.io.Serializable;

public class ObjectType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 61014497443017790L;
	
	private long id;
	private String content;
	private String desp;
	private String type;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(!(obj instanceof ObjectType)){
			return false;
		}
		ObjectType other = (ObjectType) obj;
		if(other.getId() == this.id && other.type != null
				&& other.type.equals(this.type)){
			return true;
		}
		return false;
	}
}
