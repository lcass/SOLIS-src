package com.lcass.entity;

import com.lcass.core.Core;
import com.lcass.game.world.shiphandler;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Path;

public abstract class Entity {
	protected Vertex2d abs_pos, position, movement = new Vertex2d(0,0,0,0),
			last_position = new Vertex2d(0, 0, 0, 0), relative = new Vertex2d(
					0, 0, 0, 0),render_pos = new Vertex2d(0,0,0,0);
	
	protected int current_tile = 0;
	protected int ship;
	protected int crew_position = -1;
	protected boolean alive = true;
	protected shiphandler ships;
	protected Vertex2d sprite = new Vertex2d(0, 0, 0, 0);
	protected Core core;
	protected Path path;
	protected boolean moving = false;
	protected boolean path_move = false;
	protected CrewHandler handler;

	public Entity(Core core, CrewHandler h) {
		this.core = core;
		this.handler = h;
		abs_pos = new Vertex2d(0, 0, 0, 0);
	}

	public void move(Vertex2d addition) {
		moving = true;
		movement = addition;
	}

	public void set_position(Vertex2d position) {
		this.position = position;
	}

	public void move_to_loc() {
		this.moving = true;
	}

	public void damage() {

	}

	public void set_crew_position(int pos) {
		this.crew_position = pos;
	}

	public int get_crew_position() {
		return this.crew_position;
	}

	public boolean get_alive() {
		return this.alive;
	}

	public Vertex2d get_position() {
		return this.position;
	}

	public int get_ship() {
		return this.ship;
	}

	public void set_ship(int ship) {
		this.ship = ship;

	}

	protected void sub_tick() {

	}
	boolean set = false;
	public void tick() {
		if(handler.ship.get_tile(current_tile) != null && !set){
			last_position = handler.ship.correct_pos;
			position = new Vertex2d(Math.round(relative.x),Math.round(relative.y));
			movement = new Vertex2d((handler.ship.get_tile(current_tile).get_pos().x * 16) - position.x , (handler.ship.get_tile(current_tile).get_pos().y * 16) - position.y);
			
			render_pos = position;
			
			set = true;
		}
		else{
			render_pos = position;
		}
		if(handler.ship.get_tile(current_tile) == null){
			render_pos = abs_pos.whole().sub(handler.ship.correct_pos);
			set = false;
		}
		render_pos.to_int();
		abs_pos = this.position.whole().add(last_position);
		relative = abs_pos.whole().sub(core.G.revert_coordinates(handler.ship.absolute_position));
		current_tile = Math.round((relative.x + movement.x)/16) + (Math.round((relative.y + movement.y)/16) * handler.ship.width);
		
		if(movement.x != 0 || movement.y != 0){
			moving = true;
		}
		this.sub_tick();
		if (moving) {
			handler.update();
			boolean xtrue = false;
			boolean ytrue = false;
			if (movement == null) {
				Vertex2d temp_pos = path.next();
				if (temp_pos == null) {
					moving = false;
					path_move = false;
					return;
				} else {
					movement = temp_pos;
				}
			}
			if (movement.x > 0) {
				position.x += 1;
				movement.x -= 1;
			} else {
				if (movement.x == 0) {
					xtrue = true;
				} else {
					position.x -= 1;
					movement.x += 1;
				}
			}
			if (movement.y > 0) {
				position.y += 1;
				movement.y -= 1;
			} else {
				if (movement.y == 0) {
					ytrue = true;
				} else {
					position.y -= 1;
					movement.y += 1;
				}
			}
			if (ytrue && xtrue && !path_move) {
				moving = false;
			}
			if (ytrue && xtrue && path_move) {
				Vertex2d temp = path.next();
				if (temp == null) {
					moving = false;
					path_move = false;
				} else {
					movement = temp;
				}
			}
		}
	}

	public spritecomponent get_sprite() {
		return core.crew_sprite.getcoords((int) sprite.x, (int) sprite.y,
				(int) sprite.u, (int) sprite.v);
	}

	public void set_path(Path p) {
		this.path = p;
		moving = true;
		path_move = true;
	}

	public Vertex2d get_abs() {
		return this.abs_pos;
	}
	public void set_last(Vertex2d pos){
		
	}
	public Vertex2d get_render_pos(){
		return render_pos;
	}

}
