package com.lcass.game.world;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;

public class Universe {//this just handles everything in the game world , migrates away from game which is a cluster
	public shiphandler ships;
	public Planet[] planets;
	private boolean view_map = false;
	public Core core;
	public Universe(Core core){
		ships = new shiphandler(core,1000);
	}
	public void add_ship(Ship ship , Vertex2d position){
		ship.set_position(position);
		//ship.set_rotation();
		ships.add_ship(ship);
	}
	public void remove_ship(int id){
		ships.remove_ship(id);
	}
	public void generate_asteroid_field(Vertex2d position , int size){
		
	}
	public void tick(){
		ships.tick();
	}
	public void transfer_entity(){
		
	}
	public Planet[] get_planets(Ship ship){
		return null;
	}
	public int get_distance(Ship ship , Planet p){
		return 0;
	}
	public shiphandler get_ships(){
		return ships;
	}
	

	public Ship get_ship(int id){
		return ships.get_ship(id);
	}
	public void render(){
		ships.render();
	}
	public void cleanup(){
		ships.cleanup();
	}
	public void toggle_map(){
		view_map = !view_map;
	}
	
}
