package com.lcass.game.world.resources;

public class Resource {
	private int amount = 0;
	private String name = "null";
	private boolean usable = false;
	public Resource(int amount , String name,boolean usable){
		this.amount = amount;
		this.name = name;
		this.usable = usable;
	}
	public void set(int amount){
		this.amount = amount;
	}
	public void set(String name){
		this.name = name;
	}
	public void set(boolean usable){
		this.usable = usable;
	}
	public boolean get_state(){
		return usable;
	}
	public String get_name(){
		return name;
	}
	public int get_amount(){
		return amount;
	}
	
}
