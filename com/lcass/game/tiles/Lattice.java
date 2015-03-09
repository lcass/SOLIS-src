package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public class Lattice extends Tile {
	public Vertex2d position, spritepos;
	private Core core;
	private world world;
	private Sub_Tile sub_tile;
	private int index;
	public String name = "lattice";
	private int ship = 0;

	public Lattice() {

	}

	public Lattice(Vertex2d position, Core core, world world) {
		super(position, core, world);
		this.position = position;
		spritepos = com.lcass.util.Util.tex_coordinate(3, 1);
		this.core = core;
		accepting = new int[]{2};
	}

	public void init(Core core) {
		this.core = core;
		spritepos = com.lcass.util.Util.tex_coordinate(3, 1);
		this.position = new Vertex2d(0, 0);
		accepting = new int[]{2};
	}

	public Vertex2d getsprite() {
		return spritepos;
	}

	public void tick() {

	}

	public float get_mass() {
		if (sub_tile != null) {
			return 0.1f + sub_tile.get_mass();
		}
		return 0.1f;
	}

	public void translate(int x, int y) {
		position.x += x;
		position.y += y;
	}

	public void setpos(int x, int y) {
		this.position.x = x;
		this.position.y = y;
	}

	public void set_index(int index) {
		this.index = index;
	}

	public void bind() {

		if (sub_tile != null) {
			sub_tile.bind();
		} else {
			index = world.bind_render(position, spritepos);
		}
	}

	public void bind_index() {

		if (sub_tile != null) {

			sub_tile.bind_index();
		} else {
			world.bind_render(index, position, spritepos);
		}

	}

	public void bind_null() {

		
		if (sub_tile != null) {
			sub_tile.bind_null();
		}
		else{
			world.bind_empty_render(index);
		}
	}

	public Vertex2d get_pos() {

		return position;
	}

	public void set_world(world world) {
		this.world = world;
	}

	public String get_name() {
		return name;
	}

	public int get_index() {
		return index;
	}

	public void set_ship(int ship) {
		this.ship = ship;
	}

	public int get_ship() {
		return this.ship;
	}

	public world get_world() {
		return world;
	}

	public void set_sub(Sub_Tile t) {
		this.sub_tile = t;
		world.bind_empty_render(index);
	}

	public Sub_Tile get_sub() {
		return this.sub_tile;
	}

	public boolean supports_sub() {
		return true;
	}

	public boolean damage(int a) {
		return true;
	}

}