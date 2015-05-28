package com.lcass.game;

import com.lcass.game.tiles.Tile;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Progressive_buffer;

public class Inventory {
	private slot[] storage;
	public weaponset[] wep_storage;
	public Tile selected;
	private Game game;
	private Progressive_buffer[] data = new Progressive_buffer[2];
	private VBO drawdata, selecteddata, selectedtiledata;
	private boolean hastile = false;
	private boolean weapon = false;
	private int position;
	private int lastselected = 0, selectedposition = 4;// 0 dock 1 block info 2
														// person info 3 delete
														// 4 place 5 select
	private spritesheet selectedsprite;
	private boolean dataupdate = true;

	public Inventory(int size, Game game) {
		this.game = game;
		storage = new slot[size];
		data[0] = new Progressive_buffer(null, false);
		data[1] = new Progressive_buffer(null, true);
		selectedsprite = new spritesheet("textures/selected.png");
		drawdata = new VBO(game.core.G.mainvbo);
		drawdata.create(size * 12);
		drawdata.bind_texture(game.core.tile_sprite.gettexture());
		selecteddata = new VBO(game.core.G.mainvbo);
		selecteddata.create(game.core.G.square(
				(int) (game.core.width - (45 * 2)),
				(int) (game.core.height - 74), game.world.obwidth,
				selectedsprite.getcoords(0, 0, game.world.texwidth,
						game.world.texheight)));
		selecteddata.bind_texture(selectedsprite.gettexture());
		selectedtiledata = new VBO(game.core.G.mainvbo);
		selectedtiledata.bind_texture(game.core.tile_sprite.gettexture());
		selectedtiledata.create(12);
		wep_storage = new weaponset[size];
		for (int i = 0; i < wep_storage.length; i++) {
			wep_storage[i] = new weaponset(null, 50);
		}
	}

	public void render() {
		drawdata.render();
		selecteddata.render();
		if (hastile) {
			selectedtiledata.render();

		}
	}

	public void set_selectedposition(int i) {
		selectedposition = i;
	}

	public int get_lastselected() {
		return lastselected;
	}
	public int get_selected_wep(){
		if(weapon){
			for(int i = 0; i < wep_storage.length;i++){
				if(wep_storage[i].type != null){
					if(wep_storage[i].type.get_name()==selected.get_name()){
						return i;
					}
				}
			}
		}
		return -1;
	}

	public void tick() {
		selecteddata.set_position(game.core.G.convert_coordinates(0,
				-(selectedposition * 76)));

		if (dataupdate) {

			dataupdate = false;
			data[0].clear();
			data[1].clear();
			Progressive_buffer[] temp = new Progressive_buffer[2];
			if (!weapon) {
				for (int i = 0; i < 15; i++) {
					if (i + position < storage.length) {

						if (storage[i + position] != null) {

							Vertex2d spritepos = storage[i + position].stored
									.getsprite();
							temp = game.core.G
									// just dont mess with these values , they
									// work leave them...
									.rectangle(
											game.core.width - 40,
											game.core.height - 48 - (i * 38),
											game.world.obwidth,
											game.world.obheight,
											game.core.tile_sprite
													.getcoords(
															(int) spritepos.x,
															(int) spritepos.y,
															(int) spritepos.x
																	+ game.world.texwidth,
															(int) spritepos.y
																	+ game.world.texheight));
							data[0].extend(temp[0]);
							data[1].extend(temp[1]);
						}
					} else {
						break;
					}
				}
				if (selected != null) {

					selectedtiledata
							.edit_data(game.core.G.rectangle(
									game.core.width - 90,
									game.core.height - 48,
									game.world.obwidth,
									game.world.obheight,
									game.core.tile_sprite.getcoords(
											(int) selected.getsprite().x,
											(int) selected.getsprite().y,
											(int) (selected.getsprite().x + game.world.texwidth),
											(int) (selected.getsprite().y + game.world.texheight))));
					hastile = true;

				}
			} else {
				for (int i = 0; i < 15; i++) {
					if (i + position < wep_storage.length) {

						if (wep_storage[i + position].type != null) {

							Vertex2d spritepos = wep_storage[i + position].type
									.getsprite();
							temp = game.core.G
									// just dont mess with these values , they
									// work leave them...
									.rectangle(
											game.core.width - 40,
											game.core.height - 48 - (i * 38),
											game.world.obwidth,
											game.world.obheight,
											game.core.tile_sprite
													.getcoords(
															(int) spritepos.x,
															(int) spritepos.y,
															(int) spritepos.x
																	+ game.world.texwidth,
															(int) spritepos.y
																	+ game.world.texheight));
							data[0].extend(temp[0]);
							data[1].extend(temp[1]);
						}
					} else {
						break;
					}
				}
				if (selected != null) {

					selectedtiledata
							.edit_data(game.core.G.rectangle(
									game.core.width - 90,
									game.core.height - 48,
									game.world.obwidth,
									game.world.obheight,
									game.core.tile_sprite.getcoords(
											(int) selected.getsprite().x,
											(int) selected.getsprite().y,
											(int) (selected.getsprite().x + game.world.texwidth),
											(int) (selected.getsprite().y + game.world.texheight))));
					hastile = true;

				}
			}
			temp = game.core.G.square(0, 0, game.world.obwidth,
					game.core.tile_sprite.getcoords(0, 0, 0, 0));
			data[0].extend(temp[0]);
			data[1].extend(temp[1]);
			drawdata.clear_data();
			drawdata.edit_data(data);
		}
	}

	public void add_item(Tile t, int size) {

		for (int i = 0; i < storage.length; i++) {
			if (storage[i] != null) {
				if (storage[i].stored.get_name() == t.get_name()) {

					storage[i].amount += size;
					if (t.is_sub()) {
						storage[i].sub_tile = true;
					}
					return;
				}
			} else {
				dataupdate = true;
				storage[i] = new slot();
				storage[i].stored = t;
				storage[i].amount = size;
				if (t.is_sub()) {
					storage[i].sub_tile = true;
				}
				return;
			}

		}

	}

	public boolean take_selected(int size) {
		dataupdate = true;
		if (selected != null) {
			if (take_item(selected, size)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean take_item(Tile t, int size) {
		dataupdate = true;
		for (int i = 0; i < storage.length; i++) {
			if (storage[i] != null) {
				if (t.get_name() == storage[i].stored.get_name()) {
					if (size <= storage[i].amount) {
						storage[i].amount -= size;
						if (storage[i].amount == 0) {
							storage[i] = null;
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public int add_weapon(Tile t) {
		for (int i = 0; i < wep_storage.length; i++) {
			if (wep_storage[i].type != null) {
				if (wep_storage[i].type.get_name() == t.get_name()) {
					wep_storage[i].add_weapon(t);
					return i;
				}
			}
		}
		for (int i = 0; i < wep_storage.length; i++) {
			if (wep_storage[i].type == null) {
				wep_storage[i].type = t;
				wep_storage[i].add_weapon(t);
				return i;
			}
		}
		return -1;
	}

	public void remove_weapon(int i) {
		wep_storage[i].type = null;
		wep_storage[i].clear();
		dataupdate = true;
	}

	public void position_down() {

		if (position < storage.length - 15) {

			position += 1;
			dataupdate = true;
		}
	}

	public void position_up() {
		if (position > 0) {
			position -= 1;
			dataupdate = true;
		}
	}

	public void click1() {
		if (!weapon) {
			if (storage[position] != null) {
				selected = storage[position].stored;
				dataupdate = true;
			}
		} else {
			selected = wep_storage[position].type;
			dataupdate = true;
		}

	}

	public void click2() {
		if (!weapon) {

			if (storage[position + 1] != null) {
				selected = storage[position + 1].stored;

				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 1].type;
			dataupdate = true;
		}
	}

	public void click3() {
		if (!weapon) {
			if (storage[position + 2] != null) {
				selected = storage[position + 2].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 2].type;
			dataupdate = true;
		}
	}

	public void click4() {
		if (!weapon) {
			if (storage[position + 3] != null) {
				selected = storage[position + 3].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 3].type;
			dataupdate = true;
		}
	}

	public void click5() {
		if (!weapon) {
			if (storage[position + 4] != null) {
				selected = storage[position + 4].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 4].type;
			dataupdate = true;
		}
	}

	public void click6() {
		if (!weapon) {
			if (storage[position + 5] != null) {
				selected = storage[position + 5].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 5].type;
			dataupdate = true;
		}
	}

	public void click7() {
		if (!weapon) {
			if (storage[position + 6] != null) {
				selected = storage[position + 6].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 6].type;
			dataupdate = true;
		}
	}

	public void click8() {
		if (!weapon) {
			if (storage[position + 7] != null) {
				selected = storage[position + 7].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 7].type;
			dataupdate = true;
		}
	}

	public void click9() {
		if (!weapon) {
			if (storage[position + 8] != null) {
				selected = storage[position + 8].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 8].type;
			dataupdate = true;
		}
	}

	public void click10() {
		if (!weapon) {
			if (storage[position + 9] != null) {
				selected = storage[position + 9].stored;
				dataupdate = true;
			}
		}else{
			selected = wep_storage[position + 9].type;
			dataupdate = true;
		}
	}

	public void click11() {
		if (!weapon) {
			if (storage[position + 10] != null) {
				selected = storage[position + 10].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 10].type;
			dataupdate = true;
		}
	}

	public void click12() {
		if (!weapon) {
			if (storage[position + 11] != null) {
				selected = storage[position + 11].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 11].type;
			dataupdate = true;
		}
	}

	public void click13() {
		if (!weapon) {
			if (storage[position + 12] != null) {
				selected = storage[position + 12].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 12].type;
			dataupdate = true;
		}
	}

	public void click14() {
		if (!weapon) {
			if (storage[position + 13] != null) {
				selected = storage[position + 13].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 13].type;
			dataupdate = true;
		}
	}

	public void click15() {
		if (!weapon) {
			if (storage[position + 14] != null) {
				selected = storage[position + 14].stored;
				dataupdate = true;
			}
		}
		else{
			selected = wep_storage[position + 14].type;
			dataupdate = true;
		}
	}

	public int get_selectedposition() {
		return selectedposition;
	}

	public void option_0() {
		lastselected = selectedposition;
		selectedposition = 0;
	}

	public void option_1() {
		lastselected = selectedposition;
		selectedposition = 1;
	}

	public void option_2() {
		lastselected = selectedposition;
		selectedposition = 2;
	}

	public void option_3() {
		lastselected = selectedposition;
		selectedposition = 3;
	}

	public void option_4() {
		lastselected = selectedposition;
		selectedposition = 4;
	}

	public void option_5() {
		lastselected = selectedposition;
		selectedposition = 5;
	}

	public void option_6() {
		lastselected = selectedposition;
		selectedposition = 6;
	}

	public void force_update() {
		dataupdate = true;
	}

	public void set_weapon(boolean wep) {
		this.weapon = wep;
		dataupdate = true;
	}

}
