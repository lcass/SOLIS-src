package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public class Vaultdoor extends Tile {
	public Vaultdoor() {
		spritepos = new Vertex2d(16 * 5, 16 * 3, 16 * 6, 16 * 4);
		mass = 40;
		name = "Vault Door";
	}

	public Vaultdoor(Vertex2d position, Core core, world world) {
		super(position, core, world);
		spritepos = new Vertex2d(16 * 5, 16 * 3, 16 * 6, 16 * 4);
		mass = 40;
		name = "Vault Door";

	}

	public void init(Core core) {
		this.core = core;
		spritepos = new Vertex2d(16 * 5, 16 * 3, 16 * 6, 16 * 4);
		this.position = new Vertex2d(0, 0);
		mass = 40;
		name = "Vault Door";
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

	public void tick() {
		
	}
}

