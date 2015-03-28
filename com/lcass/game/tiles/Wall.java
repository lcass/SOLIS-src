package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.Ship;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public class Wall extends Tile {
	public Vertex2d position , spritepos;
	private Core core;
	
	private world world;
	private int index;

	public String name = "wall";
	private int ship = 0;
	
	public Wall(){
		
	}
	public Wall(Vertex2d position, Core core,world world) {
		super(position, core,world);
		this.position = position;
		type = 5;
		spritepos = new Vertex2d(16,0,32,16);
		this.core = core;
		accepting = new int[]{3};
		is_wall = true;

	}
	public void init(Core core){
		this.core = core;
		type = 5;
		spritepos = new Vertex2d(16,0,32,16);
		this.position = new Vertex2d(0,0);
		accepting = new int[]{3};
		is_wall = true;
	}
	
	public Vertex2d getsprite(
	){
		return spritepos;
	}
	public void tick(){
		
		
	}
	public float get_mass(){
		if(sub_tile != null){
			return 2 + sub_tile.get_mass();
		}
		return 2;
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
		
		if(sub_tile != null){
			sub_tile.bind();
		}
			}
	public void bind_index(){
		
		world.bind_render(index,position,spritepos);
		if(sub_tile != null){
			
			sub_tile.bind_index();
		}
		
	}
	public void bind_null(){
		
		world.bind_empty_render(index);

		if(sub_tile != null){
			sub_tile.bind_null();
		}
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
	public void set_sub(Sub_Tile t){
		this.sub_tile = t;
	}
	public Sub_Tile get_sub(){
		return this.sub_tile;
	}
	public boolean damage(int damage){
		return true;
	}

	

}
