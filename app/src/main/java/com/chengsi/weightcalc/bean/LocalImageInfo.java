package com.chengsi.weightcalc.bean;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class LocalImageInfo implements Serializable, Comparable<LocalImageInfo>{
	public String imageId;
//	public String thumbnailPath;
	public String imagePath;
	public String dateModified;
	public boolean isSelected = false;
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LocalImageInfo) {
			return this.imagePath.equals(((LocalImageInfo) o).imagePath);
		}
		return super.equals(o);
	}
	
	@Override
	public int compareTo(LocalImageInfo another) {
		// TODO Auto-generated method stub
		long thisModified = dateModified == null ? 0 : Long.valueOf(dateModified);
		long anotherModified = another.dateModified == null ? 0 : Long.valueOf(another.dateModified);
		if (thisModified == anotherModified) {
			return 0;
		}else {
			return anotherModified > thisModified ? 1 : -1;
		}
	}
}
