package com.lcass.entity;

import com.lcass.core.Core;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;

public abstract class Projectile {
	protected Core core;
	protected int speed = 10;
	protected float rotation = 0;
	protected int ticklimit = 500;
	protected int tickcount = 0;
	protected Vertex2d sprite = new Vertex2d(0,0,16,16);
	protected Vertex2d position = new Vertex2d(0,0);
	protected Vertex2d abs_pos = new Vertex2d(0,0);
	protected Vertex2d move = new Vertex2d(0,0);
	protected ProjectileHandler ph = null;
	protected VBO render;//only because this thing moves a lot and changes directions a lot.
	public Projectile(Core core){
		this.core = core;
		render = new VBO(core.G.mainvbo);
		render.bind_texture(core.proj_sprite.gettexture());
		render.create(12);
		render.edit_data(core.G.square(0, 0, 32, core.proj_sprite.getcoords((int)sprite.x, (int)sprite.y, (int)sprite.u, (int)sprite.v)));
	}
	public void tick(){
		override_tick();
		position.add(move);
		abs_pos.add(move);
		tickcount ++;
		
		core.game.ships.proj_collision_check(this);
		if(tickcount >= 500){
			ph.remove(this);
		}
	}
	protected void override_tick(){
		
	}
	public void set_position(Vertex2d pos){
		this.position = pos;
		abs_pos = pos;
	}
	public void set_handler(ProjectileHandler p){
		ph = p;
	}
	public int get_speed(){
		return this.speed;
	}
	public Vertex2d get_sprite(){
		return this.sprite;
	}
	public void render(){
		
		render.set_position(core.G.convert_coordinates(position.whole().sub(core.game.ships.get_ship(ph.ship).correct_pos)).add(core.game.ships.get_ship(ph.ship).camera));

		//render.set_rot_pos(x);
		//render.set_rotation(y);
		render.render();
	}
	public void set_move(Vertex2d move){
		this.move = move;
		this.move.mult(speed);
	}
	public void set_tick_limit(int i){
		this.ticklimit= i;
	}
	public Vertex2d get_abs(){
		return this.abs_pos;
	}
	public void Destroy(){
		ph.remove(this);
	}
	public int get_ship(){
		return this.ph.ship;
	}
}
