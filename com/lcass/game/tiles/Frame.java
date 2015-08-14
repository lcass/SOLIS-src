package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.entity.Entity;
import com.lcass.game.Items.Item;
import com.lcass.game.Items.construction.Construction;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public class Frame extends Tile {
	private Construction construct;
	private int ticker = -1;
	private Entity mob;
	private boolean finished = false;
	public Frame() {
		spritepos = new Vertex2d(16 * 3, 16 * 3, 16 * 4, 16 * 4);
		name = "Frame";
	}

	public Frame(Vertex2d position, Core core, world world) {
		super(position, core, world);
		spritepos = new Vertex2d(16 * 3, 16 * 3, 16 * 4, 16 * 4);
		name = "Frame";

	}

	public void init(Core core) {
		this.core = core;
		spritepos = new Vertex2d(16 * 3, 16 * 3, 16 * 4, 16 * 4);
		name = "Frame";
		this.position = new Vertex2d(0, 0);
	}
	public void assign_construct(Construction c, int timer,Entity mob){
		ticker = timer;
		construct = c;
		this.mob = mob;
	}

	public void bind() {

		index = world.bind_render(position, spritepos);

	}

	public void bind_index() {
		world.bind_render(index, position, spritepos);
		if (sub_tile != null) {
			sub_tile.bind_index();
		}

	}

	public void bind_null() {
		world.bind_empty_render(index);

	}
	public boolean finished(){
		return finished;
	}
	private boolean valid_inventory = false;
	public void tick() {
		
		if(finished){
			return;
		}
		if(construct == null){
			finished = true;
		}
		if(construct.check(mob.retreive_inventory()) && !valid_inventory){
			Item[] materials = construct.get_items();
			for(int i = 0; i < materials.length;i++){
				System.out.println("removing");
				mob.remove_item(materials[i]);
			}
			valid_inventory = true;
		}
		if(!mob.adjacent(ship, position)){
			System.out.println("called");
			finished = true;
			core.game.construction.kill_tick(ticker);
			Item[] materials = construct.get_items();
			for(int i = 0; i < materials.length;i++){
				//no null check , already done beforehand 
				mob.add_item(materials[i]);
			}
			clean();
			
		}
		else if(valid_inventory){
			System.out.println("inventory valid");
			if(core.game.construction.check_tick(ticker) >= construct.get_time()){
				core.game.construction.kill_tick(ticker);
				finish();
				System.out.println("finished");
			}
		}
		else{
			System.out.println("invalid nipventory");
			finished = true;
			core.game.construction.kill_tick(ticker);
		}
	}
	public void finish(){
		Tile temp = world.getnew(construct.get_result());
		temp.init(core, core.game.universe.get_ship(ship).get_world());
		temp.setpos((int) position.x, (int) position.y);
		world.add_tile(temp);
		finished = true;
	}
	public void clean(){
		core.game.universe.get_ship(ship).remove_tile(position);
	}
	
}
