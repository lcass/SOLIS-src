package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;

public abstract class Sub_Tile extends Tile{
	private String name = "void_sub";
	protected Sub_Tile sub;
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
		
	}
	public void bind_index(){
		
	}
	public void bind_null(){

	}
	public String get_name(){
		return this.name;
	}
	public int get_index(){
		return 0;
	}
	public void set_index(int index){
		
	}
	public Tile get_super_tile(){
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
	public void attach_sub(Sub_Tile sub){
		this.sub = sub;
		this.sub.bind();
	}
	public Sub_Tile get_sub(){
		return this.sub;
	}
	public void private_update(){
		
	}
	public void sub_update(){
		
	}
	

}
