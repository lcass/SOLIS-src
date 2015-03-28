package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public class Solar extends Tile{
	public Vertex2d position , spritepos;
	private Core core;
	private world world;
	private Sub_Tile sub_tile;
	private int index;
	public String name = "solar";
	private int ship = 0;
	
	public Solar(){
		electric = true;
	}
	public Solar(Vertex2d position, Core core,world world) {
		super(position, core,world);
		this.position = position;
		
		spritepos = com.lcass.util.Util.tex_coordinate(0, 0);
		this.core = core;
		electric = true;
		supplier = true;
		power = 10;
		is_wall = true;

	}
	public void init(Core core){
		this.core = core;
		spritepos = com.lcass.util.Util.tex_coordinate(0, 0);
		this.position = new Vertex2d(0,0);
		electric = true;
		supplier = true;
		power = 10;
		is_wall = true;
	}
	
	public Vertex2d getsprite(
	){
		return spritepos;
	}
	public void tick(){
		
		
	}
	public float get_mass(){
		return 6;
	}
	public void translate(int x , int y){
		position.x += x;
		position.y += y;
	}
	public void setpos(int x, int y){
		this.position.x = x;
		this.position.y = y;
	}
	public void set_index(int index){
		this.index = index;
	}
	public void bind(){
		
		index = world.bind_render(position, spritepos);
		
		
			}
	public void bind_index(){
		
		world.bind_render(index,position,spritepos);
		
		
	}
	public void bind_null(){
		
		world.bind_empty_render(index);

		
	}
	public Vertex2d get_pos(){
		
		return position;
	}
	public void set_world(world world){
		this.world = world;
	}
	public String get_name(){
		return name;
	}
	public int get_index(){
		return index;
	}
	public void set_ship(int ship){
		this.ship = ship;
	}
	public int get_ship(){
		return this.ship;
	}
	public world get_world(){
		return world;
	}
	
	public Sub_Tile get_sub(){
		return this.sub_tile;
	}
	public boolean damage(int a) {
		return true;
	}
	
	
}
