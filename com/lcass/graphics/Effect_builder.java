package com.lcass.graphics;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.graphics.texture.Texture;
import com.lcass.util.Progressive_buffer;

public class Effect_builder {
	private Core core;
	private int length = 0;
	private int cycle = 0;
	private int max_cycle = 0;
	private Vertex2d transformation, rotpos,rotpos_2;
	private ArrayList<Effect> Effects = new ArrayList<Effect>();
	boolean[] indexs;
	private VBO draw_data;
	private Progressive_buffer[] data = new Progressive_buffer[2];
	private boolean data_updated = false;

	/**
	 * Constructor
	 * 
	 * @param core
	 *            Core object
	 * @param start_len
	 *            initial maximum effect size
	 */
	public Effect_builder(Core core, int start_len) {
		length = start_len;
		draw_data = new VBO(core.G.mainvbo);
		draw_data.create(start_len * 6);
		data[0] = new Progressive_buffer(null, false);
		data[1] = new Progressive_buffer(null, true);
		transformation = new Vertex2d(0, 0, 0, 0);
		rotpos = new Vertex2d(0, 0, 0, 0);
	}

	public void set_len(int len) {
		length = len;
	}

	public void bind_texture(Texture t) {
		draw_data.bind_texture(t);
	}

	public int create_effect(Progressive_buffer[] data, int cycle) {
		if (cycle > max_cycle) {
			max_cycle = cycle;
		}
		for (int i = 0; i < Effects.size(); i++) {
			if (Effects.get(i).cycle == cycle) {
				Effects.get(i).extend(data);
				return i;
			}
		}
		Effects.add(new Effect().create(cycle).extend(data));
		return Effects.size();
	}
	
	public int create_effect(Progressive_buffer[] data, int cycle, int amount) {
		if(cycle + (amount-1) > max_cycle){
			max_cycle = cycle + (amount-1);
		}
		for (int i = 0; i <= amount; i++) {
			Effects.add(new Effect().create((cycle-1) + i).extend(data));
		}
		return Effects.size() - amount;
	}

	public void delete_effect(int index) {
		Effects.remove(index);
	}

	public void transform(Vertex2d transformation) {
		transformation = new Vertex2d(transformation.x,transformation.y,transformation.u,0);
		this.transformation = transformation;
	}

	public void rotate(float angle) {
		transformation.u = angle;
	}

	public void set_rot_pos(Vertex2d position) {
		rotpos = position;
	}
	public void rotate_2(float angle) {
		transformation.v = angle;
	}

	public void set_rot_pos_2(Vertex2d position) {
		rotpos_2 = position;
	}

	public void tick() {
		draw_data.set_position(transformation);
		draw_data.rotate(transformation.u);
		draw_data.set_rot_pos_2(rotpos_2);
		draw_data.rotate_2(transformation.v);
		data_updated = false;
		draw_data.clear_data();
		draw_data.set_rot_pos(rotpos);
		cycle += 1;
		if (cycle > max_cycle) {
			cycle = 0;
		}
		indexs = new boolean[Effects.size()];
		for (int i = 0; i < Effects.size(); i++) {
			if (Effects.get(i).cycle == cycle) {
				if (Effects.get(i).has_data) {
					data_updated = true;
					indexs[i] = true;
				}
			}
		}

		if (data_updated == true) {
			data[0].clear();
			data[1].clear();
			int b = 0;
			Progressive_buffer[] temp = new Progressive_buffer[2];
			for (boolean a : indexs) {
				if (a) {
					temp = Effects.get(b).data;
					data[0].extend(temp[0]);
					data[1].extend(temp[1]);
				}
				b++;
			}
			draw_data.edit_data(data);
		}

	}


	public void render() {

		draw_data.render();
	}
	public void clear(){
		max_cycle = 0;
		Effects.clear();
	}

}
