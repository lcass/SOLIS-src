package com.lcass.util;

import java.lang.reflect.Array;

public class Caster<T>{
	private Class<T> t;
	public Caster(Class<T> t){
		this.t = t;
	}
	public T[] cast(Object[] in){
		
		if(t == null){
			return null;
		}
		
		@SuppressWarnings("unchecked")
		T[] temp = (T[])Array.newInstance(t, in.length);
		for(int i = 0;i < in.length;i++){
			if(in[i] != null){
				temp[i] = (T)in[i];
			}
		}
		return temp;
	}
	public T cast(Object i){
		return (T)i;
	}
		
}