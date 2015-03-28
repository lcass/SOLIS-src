package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Util;

public class Corner extends Sub_Tile{
	private Tile super_tile;
	private Core core;
	private int dir = 0;
	private String name = "Corner";
	private int index =0;
	public Vertex2d sprite_pos;
	public Corner(){
		sprite_pos = com.lcass.util.Util.tex_coordinate(2,1);
		type = 2;
	}
	public Corner(Tile super_tile, Core core) {
		super(super_tile, core);
		sprite_pos = com.lcass.util.Util.tex_coordinate(2, 1);
		type = 2;
		this.super_tile = super_tile;
	}
	public void tick(){
		
	}
	public void bind(){
		System.out.println(super_tile);
		index = super_tile.get_world().bind_render(super_tile.get_pos(), sprite_pos);
	}
	public void bind_index(){
		
		super_tile.get_world().bind_render(index,super_tile.get_pos(),sprite_pos);
		
	}
	public void bind_null(){
		super_tile.get_world().bind_empty_render(index);
	}
	public String get_name(){
		return this.name;
	}
	public int get_index(){
		return this.index;
	}
	public void set_index(int index){
		this.index = index;
	}
	public float get_mass(){
		return 1;
	}

	public void set_super(Tile super_tile){
		this.super_tile = super_tile;
	}
	public Vertex2d getsprite(){
		return this.sprite_pos;
	}
	public void init(Tile super_tile, Core core){
		this.super_tile = super_tile;
		this.core = core;
	}
	public void setsprite(Vertex2d sprite_pos){
		this.sprite_pos = sprite_pos;
	}
	public void set_dir(int dir){
		this.dir = dir;
		
		switch(dir){
		case 0:sprite_pos = Util.tex_coordinate(2, 1);
		break;
		case 1:sprite_pos = Util.tex_coordinate(2, 2);
		break;
		case 2:sprite_pos = Util.tex_coordinate(2, 3);
		break;
		case 3:sprite_pos = Util.tex_coordinate(2, 4);
		break;
		}
	}
	public int get_dir(){
		return dir;
	}
	

}
