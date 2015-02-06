package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public abstract class Tile {
	public Vertex2d position,spritepos;
	private Core core;
	private world world;
	private Sub_Tile sub_tile;
	public String name = "empty";
	private int mass = 0;
	private int index =0;
	private int ship = 0;
	private boolean supports_sub = false;
	public Tile(){
		
	}
	public float get_thrust(){
		return 0;
	}
	public int get_dir(){
		return 0;
	}
	
	public Tile(Vertex2d position, Core core,world world){
		this.position = new Vertex2d(0,0);
		spritepos = new Vertex2d(0,0,16,16);
		this.core = core;
		this.world = world;
	}
	public float get_mass(){
		return this.mass;
	}
	
	public void init(Core core){
		this.core = core;
		spritepos = new Vertex2d(0,0,16,16);
		this.position = new Vertex2d(0,0);
	}
	public Vertex2d get_pos(){
		return position;
	}
	public void setpos(int x, int y){
		this.position.x = x;
		this.position.y = y;
	}
	public void translate(int x , int y){
		position.x += x;
		position.y += y;
	}
	public Vertex2d getsprite(){
		return spritepos;
	}
	public void tick(){//checking internal variables and binding to the main world buffer.
		
	}
	public void bind(){
		
	}
	public void bind_index(){
		
	}
	public void setup_bind(){
		
	}
	public void bind_null(){
		
	}
	public void set_index(int index){
		
	}
	public String get_name(){
		return this.name;
	}
	public int get_index(){
		return this.index;
	}
	public void set_dir(int dir){
		
	}
	public void set_ship(int ship){
		this.ship = ship;
	}
	public int get_ship(){
		return this.ship;
	}
	public void set_world(world world){
		this.world = world;
	}
	public void set_sub(Sub_Tile t){
		this.sub_tile = t;
	}
	public Sub_Tile get_sub(){
		return this.sub_tile;
	}
	public boolean is_sub(){
		return false;
	}
	public world get_world(){
		return null;
	}
	public boolean supports_sub(){
		return false;
	}
}
