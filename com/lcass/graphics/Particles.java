package com.lcass.graphics;

import java.util.Random;

import com.lcass.core.Core;
import com.lcass.graphics.texture.spritecomponent;

public class Particles {
	public Core core;

	public Random random;
	private particle_spawn[] particle_spawns;

	public Particles(Core core, int max_effect) {
		this.core = core;
		random = new Random();
		random.setSeed(System.nanoTime());
	
		particle_spawns = new particle_spawn[max_effect];
	}

	public void tick() {
		for (particle_spawn a : particle_spawns) {
			if (a != null) {
				a.tick();
				
			}
		}
	}

	public void render() {
		for (particle_spawn a : particle_spawns) {
			if (a != null) {
				a.render();
			}
		}
	}

	public int create_spawn(Vertex2d data, spritecomponent s, int amount,
			float decay, int size) {
		for (int i = 0; i < particle_spawns.length; i++) {
			if (particle_spawns[i] == null) {
				particle_spawns[i] = new particle_spawn(this, data, s, decay,
						size, amount, i,1,1, 0, 0, false, 0);
				return i;
			}
		}
		return 0;
	}
	public int create_spawn(Vertex2d data, spritecomponent s, int amount,
			float decay, int size,int wait) {
		for (int i = 0; i < particle_spawns.length; i++) {
			if (particle_spawns[i] == null) {
				particle_spawns[i] = new particle_spawn(this, data, s, decay,
						size, amount, i,wait,1,0,0,false,0);
				return i;
			}
		}
		return 0;
	}
	public int create_spawn(Vertex2d data, spritecomponent s, int amount,
			float decay, int size,int wait,float velocity) {
		
		for (int i = 1; i < particle_spawns.length; i++) {
			if (particle_spawns[i] == null) {
				
				particle_spawns[i] = new particle_spawn(this, data, s, decay,
						size, amount, i,wait,velocity,0,0,false,0);
				return i;
			}
		}
		return 0;
	}
	public int create_spawn(Vertex2d data, spritecomponent s, int amount,
			float decay, int size,int wait,float velocity,int offset_x,int offset_y,int creation_rate) {
		
		for (int i = 1; i < particle_spawns.length; i++) {
			if (particle_spawns[i] == null) {
				
				particle_spawns[i] = new particle_spawn(this, data, s, decay,
						size, amount, i,wait,velocity,offset_x,offset_y,true,creation_rate);
				return i;
			}
		}
		return 0;
	}

	public void remove(int location) {// called by spawns ONLY otherwise you
										// have a memory leak
		particle_spawns[location] = null;
	}

	public void clear() {
		for (particle_spawn a : particle_spawns) {
			if (a != null) {
				a.clean_kill();
			}
		}
	}
	public void translate(Vertex2d new_pos){
		for(particle_spawn i : particle_spawns){
			if(i != null){
				i.translate(new_pos);
			}
		}
	}
	public void move(Vertex2d addition){
		for(particle_spawn i: particle_spawns){
			if(i != null){
				i.move(addition);
			}
		}
	}
	public void set_spawn(Vertex2d position,int index){
		if(particle_spawns[index] != null){
			particle_spawns[index].set_spawn(position);
		}
	}
	public void set_offset(Vertex2d offset,int index){
		if(particle_spawns[index] != null){
			particle_spawns[index].set_offset_x((int)offset.x);
			particle_spawns[index].set_offset_y((int)offset.y);
			
		}
	}
	public void set_creating(boolean creating, int index){
		
		if(particle_spawns[index] != null){
			particle_spawns[index].set_creation(creating);
		}
	}
	public void reset_spawn_data(){
		for(particle_spawn i : particle_spawns){
			if(i != null){
				i.clear_data();
				
			}
		}
	}
}
