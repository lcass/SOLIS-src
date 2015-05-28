package com.lcass.game.Items;

import com.lcass.core.Core;
import com.lcass.entity.CrewHandler;
import com.lcass.entity.Entity;
import com.lcass.graphics.Vertex2d;

public abstract class Item {
	protected String name = "Error";
	protected int owner = 0;
	protected CrewHandler owner_handler;
	protected Core core;
	protected boolean live = true;
	protected Vertex2d sprite = new Vertex2d(0,0,0,0);
	public Item(Core core, CrewHandler owner, int ownerid,String name){
		this.name = name;
		this.core = core;
		this.owner = ownerid;
		this.owner_handler = owner;
	}
	public Vertex2d get_sprite(){
		return sprite;
	}
	public String get_name(){
		return name;
	}
	public void tick(){
		co_tick();
	}
	protected void co_tick(){
		
	}
	public void interact(Entity e){
		
	}
	public boolean alive(){
		return live;
	}
}
