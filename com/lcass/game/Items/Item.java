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
	protected boolean used_up = true;
	protected int stack = 1;
	protected int max_stack = 1;
	protected boolean live = true;
	protected Vertex2d sprite = new Vertex2d(0,0,0,0);
	public Item(Core core,CrewHandler owner,int stack,int max_stack){
		this.core = core;
		this.owner_handler = owner;
		this.stack = stack;
		this.max_stack = max_stack;
		if(stack > max_stack){
			this.stack = max_stack;
		}
	}
	public void set_owner(int id){
		owner = id;
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
	protected void co_tick(){//called by sup classes
		
	}
	public void interact(Entity e){
		
	}
	public boolean alive(){
		return live;
	}
	public int get_stack(){
		return stack;
	}
	public int get_max_stack(){
		return max_stack;
	}
	public void set_stack(int i){
		this.stack = i;
		if(stack > max_stack){
			stack = max_stack;
		}
	}
	public boolean used_up(){
		return used_up;
	}
}
