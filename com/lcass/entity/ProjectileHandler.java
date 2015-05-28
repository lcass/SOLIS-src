package com.lcass.entity;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;

public class ProjectileHandler {

	private Core core;
	public int ship = 0;
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public ProjectileHandler(Core core,int ship) {
		this.core = core;
		this.ship = ship;
	}
	public void remove(Projectile p){
		projectiles.remove(p);
	}
	public void add(Projectile p){
		p.set_handler(this);
		projectiles.add(p);
	}
	public void tick(){
		for(int i = 0; i < projectiles.size();i++){
			projectiles.get(i).tick();
		}
	}
	public void render(){
		for(int i = 0; i < projectiles.size();i++){
			projectiles.get(i).render();
		}
	}
	public Vertex2d calc_step(Vertex2d source , Vertex2d target){
		Vertex2d movement = target.whole().sub(source);
		float distance = (float) Math.sqrt(Math.pow(movement.x, 2)  + Math.pow(movement.y ,2));
		Vertex2d step = new Vertex2d(movement.x/distance, movement.y/distance);
		return step;
	}
}
