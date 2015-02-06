package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;

public abstract class Sub_Tile extends Tile{
	private String name = "void_sub";
	private int index = -1;
	//private Vertex2d sprite_pos = new Vertex2d(0,0,0,0);
	private Tile super_tile = null;
	public Vertex2d sprite_pos;
	private Core core;
	public Sub_Tile(){
		
	}
	public Sub_Tile(Tile super_tile,Core core){
		
	}
	public void init(Tile super_tile, Core core){
		this.super_tile = super_tile;
		this.core = core;
	}
	public void tick(){
		
	}
	public void bind(){
		
		index = super_tile.get_world().bind_render(position, spritepos);
	}
	public void bind_index(){
		super_tile.get_world().bind_render(index,position,spritepos);
		
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
	public Tile get_super(){
		return this.super_tile;
	}
	public void set_super(Tile super_tile){
		this.super_tile = super_tile;
	}
	public Vertex2d getsprite(){
		return new Vertex2d(0,0,0,0);
	}
	public void setsprite(Vertex2d sprite_pos){
		
	}
	public void set_dir(int dir){
		
	}
	public int get_dir(){
		return 0;
	}
	public boolean is_sub(){
		return true;
	}
	public float get_mass(){
		return 0;
	}

}
