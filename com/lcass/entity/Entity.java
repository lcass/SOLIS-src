package com.lcass.entity;

import com.lcass.core.Core;
import com.lcass.game.world.shiphandler;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Path;

public abstract class Entity {
	protected Vertex2d position,movement;
	protected int ship;
	protected int crew_position = -1;
	protected boolean alive = true;
	protected shiphandler ships;
	protected Vertex2d sprite = new Vertex2d(0,0,0,0);
	protected Core core;
	protected Path path;
	protected boolean moving = false;
	protected boolean path_move = false;
	protected CrewHandler handler;
	public Entity(Core core,CrewHandler h){
		this.core = core;
		this.handler = h;
	}
	public void move(Vertex2d addition){
		moving = true;
		movement = addition;
	}
	public void set_position(Vertex2d position){
		this.position = position;
	}
	public void move_to_loc(){
		this.moving = true;
	}
	public void damage(){
		
	}
	public void set_crew_position(int pos){
		this.crew_position = pos;
	}
	public int get_crew_position(){
		return this.crew_position;
	}
	public boolean get_alive(){
		return this.alive;
	}
	public Vertex2d get_position(){
		return this.position;
	}
	public int get_ship(){
		return this.ship;
	}
	public void set_ship(int ship){
		this.ship = ship;
		
	}
	protected void sub_tick(){
		
	}
	public void tick(){
		this.sub_tick();
		if(moving){
			handler.update();
			boolean xtrue = false;
			boolean ytrue = false;
			if(movement == null){
				Vertex2d temp_pos = path.next();
				if(temp_pos == null){
					moving = false;
					path_move = false;
					return;
				}
				else{
					movement = temp_pos;
				}
			}
			if(movement.x > 0){
				position.x += 1;
				movement.x -=1;
			}
			else{
				if(movement.x == 0){
					xtrue = true;
				}
				else{
					position.x -= 1;
					movement.x +=1;
				}
			}
			if(movement.y > 0){
				position.y += 1;
				movement.y -=1;
			}
			else{
				if(movement.y == 0){
					ytrue = true;
				}
				else{
					position.y -=1;
					movement.y += 1;
				}
			}
			if(ytrue && xtrue && !path_move){
				moving = false;
			}
			if(ytrue && xtrue && path_move){
				Vertex2d temp = path.next();
				if(temp == null){
					moving = false;
					path_move = false;
				}
				else{
					movement = temp;
				}
			}
		}
	}
	public spritecomponent get_sprite(){
		return core.crew_sprite.getcoords((int)sprite.x,(int)sprite.y,(int)sprite.u,(int)sprite.v);
	}
	public void set_path(Path p){
		this.path = p;
		moving = true;
		path_move = true;
	}
	
}
