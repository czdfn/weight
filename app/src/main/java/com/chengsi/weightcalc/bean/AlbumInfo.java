package com.chengsi.weightcalc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class AlbumInfo {
	public int count = 0;
	public String id;
	public String albumName;
	public List<LocalImageInfo> imageList = new ArrayList<LocalImageInfo>();

}
