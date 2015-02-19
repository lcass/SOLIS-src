package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Util;

public class Cable_holder extends Sub_Tile {
	private Tile super_tile;
	private Core core;
	private Sub_Tile cable = null;
	private int dir = 0;
	private String name = "Cable holder";
	private int index = 0;
	private boolean has_cable = false;
	public Vertex2d sprite_pos;
	int accepting_types = 1;

	public Cable_holder() {
		sprite_pos = com.lcass.util.Util.tex_coordinate(7, 0);
	}

	public Cable_holder(Tile super_tile, Core core) {
		super(super_tile, core);
		sprite_pos = com.lcass.util.Util.tex_coordinate(7, 0);
	}

	public void tick() {

	}

	public void bind() {

		index = super_tile.get_world().bind_render(super_tile.get_pos(),
				sprite_pos);
	}

	public void bind_index() {

		super_tile.get_world().bind_render(index, super_tile.get_pos(),
				sprite_pos);

	}

	public void bind_null() {
		super_tile.get_world().bind_empty_render(index);
	}

	public String get_name() {
		return this.name;
	}

	public int get_index() {
		return this.index;
	}

	public void set_index(int index) {
		this.index = index;
	}

	public float get_mass() {
		return 0.2f;
	}

	public Tile get_super() {
		return this.super_tile;
	}

	public void set_super(Tile super_tile) {
		this.super_tile = super_tile;
	}

	public Vertex2d getsprite() {
		return this.sprite_pos;
	}

	public void init(Tile super_tile, Core core) {
		this.super_tile = super_tile;
		this.core = core;
	}

	public void setsprite(Vertex2d sprite_pos) {
		this.sprite_pos = sprite_pos;
	}

	public void set_dir(int dir) {
		this.dir = dir;

		switch (dir) {
		case 0:
			sprite_pos = Util.tex_coordinate(7, 0);
			break;
		case 1:
			sprite_pos = Util.tex_coordinate(7, 1);
			break;
		case 2:
			sprite_pos = Util.tex_coordinate(7, 0);
			break;
		case 3:
			sprite_pos = Util.tex_coordinate(7, 1);
			break;
		}
	}

	public int get_dir() {
		return dir;
	}

	public void attach_cable(Sub_Tile cable) {
		if (cable != null) {
			if (cable.get_name() == "cable") {
				this.cable = cable;
				has_cable = true;
			} else {
				this.cable = null;
			}
		}
		else{
			has_cable = false;
			this.cable = null;
		}
	}

	public boolean is_wire() {
		return has_cable;
	}

	public int get_type() {
		return 2;
	}

}
