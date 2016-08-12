package com.chengsi.weightcalc.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class MobinSoftReference extends SoftReference {

	public final  String key ;
	@SuppressWarnings("unchecked")
	public MobinSoftReference(Object r, ReferenceQueue q ,String key) {
		super(r, q);
		this.key =key;
	}

	public String getKey()
	{
		return key;
	}
}
