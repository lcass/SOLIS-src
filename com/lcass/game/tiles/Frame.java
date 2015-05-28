package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public class Frame extends Tile {
	
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
		if (required.length != 0 && stored.size() != 0) {
			boolean[] checked = new boolean[required.length];
			for (int i = 0; i < checked.length; i++) {
				checked[i] = false;
			}
			for (int i = 0; i < required.length; i++) {
				for (int j = 0; j < stored.size(); j++) {
					if (stored.get(j).get_name() == required[i].get_name()) {
						checked[i] = true;
						break;
					}
				}
			}
			boolean all_true = true;
			for (int i = 0; i < checked.length; i++) {
				if (!checked[i]) {
					all_true = false;
					break;
				}
			}
			
			if (all_true) {
				Tile temp = world.getnew(final_tile);
				temp.init(core, world);
				temp.setpos((int) position.x, (int) position.y);
				world.add_tile(temp);
			}
		}

		else if (required.length == 0 && final_tile != null) {
			Tile temp = world.getnew(final_tile);
			temp.init(core, world);
			temp.setpos((int) position.x, (int) position.y);
			world.add_tile(temp);
		}
	}
}
