package com.lcass.game.tiles;

import java.io.Serializable;

import com.lcass.core.Core;
import com.lcass.game.world.Ship;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public abstract class Tile implements Serializable{
	public Vertex2d position,spritepos;
	protected Core core;
	protected world world;
	protected Sub_Tile sub_tile;
	protected boolean supplied = true;
	protected boolean electric = false;
	protected boolean cable = false;
	protected boolean supplier = false;
	protected boolean consumer = false;
	protected boolean active = true;
	public String name = "empty";
	protected int[] accepting = new int[0];
	protected int power = 0;
	private int mass = 0;
	protected int netnum = 0;
	protected float resistance = 0;
	protected int type = 0;
	private int ship = 0;
	private boolean supports_sub = false;
	public Tile(){
		
	}
	public float get_thrust(){
		return 0;
	}
	public int get_dir(){
		return 0;
	}
	
	public Tile(Vertex2d position, Core core,world world){
		this.position = new Vertex2d(0,0);
		spritepos = new Vertex2d(0,0,16,16);
		this.core = core;
		this.world = world;
	}
	public float get_mass(){
		return this.mass;
	}
	
	public void init(Core core){
		this.core = core;
		spritepos = new Vertex2d(0,0,16,16);
		this.position = new Vertex2d(0,0);
	}
	public Vertex2d get_pos(){
		return position;
	}
	public void setpos(int x, int y){
		this.position.x = x;
		this.position.y = y;
	}
	public void translate(int x , int y){
		position.x += x;
		position.y += y;
	}
	public Vertex2d getsprite(){
		return spritepos;
	}
	public void tick(){//checking internal variables and binding to the main world buffer.
		
	}
	public void bind(){
		
	}
	public void bind_index(){
		
	}
	public void setup_bind(){
		
	}
	public void bind_null(){
		
	}
	public void set_index(int index){
		
	}
	public String get_name(){
		return this.name;
	}
	public int get_index(){
		return 0;
	}
	public void set_dir(int dir){
		
	}
	public void set_ship(int ship){
		this.ship = ship;
	}
	public int get_ship(){
		return this.ship;
	}
	public void set_world(world world){
		this.world = world;
	}
	public void set_sub(Sub_Tile t){
		this.sub_tile = t;
	}
	public Sub_Tile get_sub(){
		return this.sub_tile;
	}
	public boolean is_sub(){
		return false;
	}
	public world get_world(){
		return null;
	}
	public boolean supports_sub(){
		return false;
	}
	
	public boolean damage(int damage){
		return false;
	}
	public boolean resources_supplied(){
		return supplied;
	}
	public void set_resources_supplied(boolean supplied){
		this.supplied = supplied;
	}
	public void private_update(){
		if(sub_tile != null){
			this.sub_tile.sub_update();
		}
	}
	
	public boolean is_electric(){
		this.private_update();
		
		if(this.sub_tile == null){
			return electric|cable|supplier|consumer;
		}
		else{
			
			return this.electric|this.cable|this.supplier|this.consumer|this.sub_tile.is_electric();
		}
	}
	public boolean is_supplier(){
		this.private_update();
		if(sub_tile == null){
			return supplier;
		}
		else{
			return supplier|sub_tile.is_supplier();
		}
	}
	public boolean is_user(){
		
		this.private_update();
		if(sub_tile == null){
			return consumer;
		}
		else{
			return consumer|sub_tile.is_user();
		}
	}
	public boolean is_wire(){
		this.private_update();
		
		if(sub_tile == null){
			return supplier|cable|electric;
		}
		else{
			return supplier|cable|electric|this.sub_tile.is_wire();
		}
	}
	public void set_network(int in){
		this.private_update();
		netnum = in;
	}
	public int get_network(){
		return netnum;
	}
	public int get_power(){
		this.private_update();
		return power;
	}
	public float get_resistance(){
		this.private_update();
		float temp = this.resistance;
		
		if(this.sub_tile != null){
			
			temp += this.sub_tile.get_resistance();
		}
		return temp;
	}
	public void set_active(boolean active){
		this.private_update();
		this.active = active;
	}
	public boolean get_active(){
		return active;
	}
	public int[] get_accepting_types(){
		 return accepting;
	}public int get_type(){
		return type;
	}
	public boolean support_type(int type){
		for(int i = 0; i < accepting.length;i++){
			if(accepting[i] == type){
				return true;
			}
		}
		return false;
	}
	public void bind_sub(){
		this.private_update();
		this.sub_tile.sub.bind();
	}
	
}
