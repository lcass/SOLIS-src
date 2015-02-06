package com.lcass.graphics;

import com.lcass.core.Core;
import com.lcass.util.Progressive_buffer;

public class Effect {
	public Progressive_buffer[] data = new Progressive_buffer[2];
	public int cycle;
	public boolean has_data = false;
	public Effect create(int cycle){
		data[0] = new Progressive_buffer(null, false);
		data[1] = new Progressive_buffer(null, true);
		this.cycle = cycle;
		has_data = false;
		return this;
	}
	public Effect extend(Progressive_buffer[] data){
		this.data[0].extend(data[0]);
		this.data[1].extend(data[1]);
		has_data = true;
		return this;
	}
	public void clear(){
		data[0].clear();
		data[1].clear();
		has_data = false;
	}
}
