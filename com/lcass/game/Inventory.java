package com.lcass.game;

import com.lcass.game.Items.Item;
import com.lcass.game.tiles.Tile;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Progressive_buffer;

public class Inventory {
	public slot[] storage;
	public weaponset[] wep_storage;
	public Tile selected;
	public Item[] stored_items;// dynamic
	public Item selected_item;
	private Game game;
	private Progressive_buffer[] data = new Progressive_buffer[2];
	private VBO drawdata, item_drawdata, selecteddata, selectedtiledata,
			selecteditemdata;
	private boolean hastile = false, hasitem = false;
	private Vertex2d render_position = new Vertex2d(0, 0, 0, 0);
	private boolean weapon = false;
	private boolean item = false;
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
		item_drawdata = new VBO(game.core.G.mainvbo);
		item_drawdata.create(size * 12);
		item_drawdata.bind_texture(game.core.item_sprite.gettexture());
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
		selecteditemdata = new VBO(game.core.G.mainvbo);
		selecteditemdata.bind_texture(game.core.item_sprite.gettexture());
		selecteditemdata.create(12);
		wep_storage = new weaponset[size];
		for (int i = 0; i < wep_storage.length; i++) {
			wep_storage[i] = new weaponset(null, 50);
		}
	}

	public void render() {
		if (!item) {
			drawdata.render();
		} else {
			item_drawdata.render();
		}
		selecteddata.render();
		if (hastile && !item) {
			selectedtiledata.render();

		}
		if (hasitem && item) {
			selecteditemdata.render();

		}
	}

	public void load_items(Item[] items) {
		int total_length = 0;
		int[] locations = new int[items.length];
		for (int i = 0; i < locations.length; i++) {
			locations[i] = -1;
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				locations[total_length] = i;
				total_length++;

			}
		}
		stored_items = new Item[total_length];
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] == -1) {
				break;
			} else {
				stored_items[i] = items[locations[i]];
			}
		}
		force_update();
	}

	public void set_render_position(Vertex2d render_pos) {
		this.render_position = game.core.G.convert_coordinates(render_pos);
		drawdata.set_position(render_position);
		item_drawdata.set_position(render_position);
		selecteddata.set_position(render_position);
		selectedtiledata.set_position(render_position);
		selecteditemdata.set_position(render_position);
	}

	public void set_selectedposition(int i) {
		selectedposition = i;
	}

	public int get_lastselected() {
		return lastselected;
	}

	public int get_selected_wep() {
		if (weapon) {
			for (int i = 0; i < wep_storage.length; i++) {
				if (wep_storage[i].type != null) {
					if (wep_storage[i].type.get_name() == selected.get_name()) {
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
				if (item) {
					for (int i = 0; i < stored_items.length; i++) {
						Vertex2d spritepos = new Vertex2d(0, 0, 0, 0);
						if (stored_items[i] != null) {
							spritepos = stored_items[i].get_sprite();

						} else {
							continue;
						}

						temp = game.core.G
						// just dont mess with these values ,
						// they
						// work leave them...
								.rectangle(game.core.width - 40,
										game.core.height - 48 - (i * 38),
										game.world.obwidth,
										game.world.obheight,
										game.core.tile_sprite.getcoords(
												(int) spritepos.x,
												(int) spritepos.y,
												(int) spritepos.x + 8,
												(int) spritepos.y + 8));
						data[0].extend(temp[0]);
						data[1].extend(temp[1]);
					}
				} else {
					for (int i = 0; i < storage.length; i++) {
						if (storage[i + position] != null) {
							Vertex2d spritepos = new Vertex2d(0, 0, 0, 0);
							if (!item) {
								if (storage[i + position].stored != null) {
									spritepos = storage[i + position].stored
											.getsprite();
								} else {

								}

								temp = game.core.G
										// just dont mess with these values
										// ,
										// they
										// work leave them...
										.rectangle(
												game.core.width - 40,
												game.core.height - 48
														- (i * 38),
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
						}
					}
				}

				if (selected != null && !item) {

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
				if (selected_item != null && item) {

					selecteditemdata.edit_data(game.core.G.rectangle(
							game.core.width - 90, game.core.height - 48,
							game.world.obwidth, game.world.obheight,
							game.core.tile_sprite.getcoords(
									(int) selected_item.get_sprite().x,
									(int) selected_item.get_sprite().y,
									(int) (selected_item.get_sprite().u / 4),
									(int) (selected_item.get_sprite().v / 4))));
					hasitem = true;

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
			temp = game.core.G.square(0, 0, 0,
					game.core.tile_sprite.getcoords(0, 0, 0, 0));
			data[0].extend(temp[0]);
			data[1].extend(temp[1]);
			drawdata.clear_data();
			if (!item) {
				drawdata.edit_data(data);
			} else {
				item_drawdata.edit_data(data);
			}
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
		if (selected != null && !item) {
			if (take_item(selected, size)) {
				return true;
			} else {
				return false;
			}

		} else if (selected_item != null && item) {
			if (take_item(selected, size)) {
				return true;
			}

		} else {
			return false;
		}
		return false;
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

	public void update_selected() {
		if (weapon) {
			if (wep_storage[selected_slot] != null) {
				selected = wep_storage[selected_slot].type;
			}
		} else if (item) {
			if (storage[selected_slot] != null) {

				selected_item = stored_items[selected_slot];
			}
		} else {
			if (storage[selected_slot] != null) {
				selected = storage[selected_slot].stored;
			}
		}
		force_update();
	}

	private int selected_slot = 0;

	public void click1() {
		selected_slot = position;
		update_selected();
	}

	public void click2() {
		selected_slot = position + 1;
		update_selected();
	}

	public void click3() {
		selected_slot = position + 2;
		update_selected();
	}

	public void click4() {
		selected_slot = position + 3;
		update_selected();
	}

	public void click5() {
		selected_slot = position + 4;
		update_selected();
	}

	public void click6() {
		selected_slot = position + 5;
		update_selected();
	}

	public void click7() {
		selected_slot = position + 6;
		update_selected();
	}

	public void click8() {
		selected_slot = position + 7;
		update_selected();
	}

	public void click9() {
		selected_slot = position + 8;
		update_selected();
	}

	public void click10() {
		selected_slot = position + 9;
		update_selected();
	}

	public void click11() {
		selected_slot = position + 10;
		update_selected();
	}

	public void click12() {
		selected_slot = position + 11;
		update_selected();
	}

	public void click13() {
		selected_slot = position + 12;
		update_selected();
	}

	public void click14() {
		selected_slot = position + 13;
		update_selected();
	}

	public void click15() {
		selected_slot = position + 14;
		update_selected();
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
		item = false;
		selected = null;
		selected_item = null;
		dataupdate = true;
	}

	public void set_inventory(boolean invent) {
		weapon = false;
		item = invent;
		selected = null;
		selected_item = null;
	}

	public void clear_storage() {
		int storage_size = storage.length;
		storage = new slot[storage_size];
	}

}
