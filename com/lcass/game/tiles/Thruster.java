package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Util;

public class Thruster extends Tile{
	public Vertex2d position , spritepos;
	private Core core;
	private world world;
	private Sub_Tile sub_tile;
	private int index;
	private int dir = 0;//1 left 0 right 2 up 3 down
	private float thrust = 0.01f;
	public String name = "thruster";
	private int ship = 0;
	public Thruster(){
		electric = true;
	}
	public Thruster(Vertex2d position, Core core,world world) {
		super(position, core,world);
		this.position = position;
		this.world = world;
		
		spritepos = Util.tex_coordinate(1, 1);
		this.core = core;
		electric = true;
	}
	public void init(Core core){
		this.core = core;
		spritepos = Util.tex_coordinate(1, 1);
		this.position = new Vertex2d(0,0);
		electric = true;
	}
	public void set_world(world world){
		this.world = world;
	}
	public Vertex2d getsprite(
	){
		return spritepos;
	}
	public void tick(){
		
		
	}
	public float get_mass(){
		if(sub_tile != null){
			return 3 + sub_tile.get_mass();
		}
		return 3;
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
		if(sub_tile != null){
			sub_tile.bind_index();
		}
		
	}
	public void bind_null(){
		world.bind_empty_render(index);
		
	}
	public Vertex2d get_pos(){
		
		return position;
	}
	public float get_thrust(){
		return thrust;
	}
	public int get_dir(){
		return dir;
	}
	public String get_name(){
		return name;
	}
	public int get_index(){
		return this.index;
	}
	public void set_dir(int dir){
		this.dir = dir;
		switch(dir){
		case 0:spritepos = Util.tex_coordinate(1, 1);
		break;
		case 1:spritepos = Util.tex_coordinate(1, 3);
		break;
		case 2:spritepos = Util.tex_coordinate(1, 4);
		break;
		case 3:spritepos = Util.tex_coordinate(1, 2);
		break;
		}
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
	public boolean damage(int a){
		return true;
	}
}
