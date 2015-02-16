package com.lcass.game.world.resources;

import java.util.ArrayList;

import com.lcass.game.tiles.Tile;

public class network {
	public ArrayList<Tile> sources = new ArrayList<Tile>();
	public ArrayList<Tile> wire = new ArrayList<Tile>();
	public ArrayList<Tile> consumers = new ArrayList<Tile>();
	public network(){
		
	}
	public void reset(){
		sources.clear();
		wire.clear();
		consumers.clear();
	}
	public void add_source(Tile a){
		sources.add(a);
	}
	public void add_wire(Tile a){
		wire.add(a);
	}
	public void add_consumer(Tile a){
		consumers.add(a);
	}
}
