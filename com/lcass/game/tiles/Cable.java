package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Util;

public class Cable extends Sub_Tile{
	private Tile super_tile;
	private int dir = 0;
	private String name = "Cable";
	private int index = 0;
	public Vertex2d sprite_pos;


	public Cable() {
		sprite_pos = com.lcass.util.Util.tex_coordinate(7, 0);
		type = 1;
		resistance = 1;
		
	}

	public Cable(Tile super_tile, Core core) {
		super(super_tile, core);
		sprite_pos = com.lcass.util.Util.tex_coordinate(7, 0);
		type = 1;
		resistance = 1;
	
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
			sprite_pos = Util.tex_coordinate(7, 2);
			break;
		case 2:
			sprite_pos = Util.tex_coordinate(7, 1);
			break;
		case 3:
			sprite_pos = Util.tex_coordinate(7, 2);
			break;
		}
	}

	public int get_dir() {
		return dir;
	}




	public int get_type() {
		return 1;
	}
}
