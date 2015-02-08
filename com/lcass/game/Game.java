package com.lcass.game;

import java.lang.reflect.Method;

import com.lcass.core.Core;
import com.lcass.game.tiles.Corner;
import com.lcass.game.tiles.Floor;
import com.lcass.game.tiles.Gyroscope;
import com.lcass.game.tiles.Lattice;
import com.lcass.game.tiles.Sub_Tile;
import com.lcass.game.tiles.Thruster;
import com.lcass.game.tiles.Tile;
import com.lcass.game.tiles.Wall;
import com.lcass.game.world.Ship;
import com.lcass.game.world.shiphandler;
import com.lcass.game.world.world;
import com.lcass.graphics.GUI;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Encapsulated_method;

public class Game {
	public world world;
	public Core core;
	private GUI inventory_gui;
	public Inventory inventory;
	public float step = 2f, zoom = 1f;
	private float timeheld = 0, max = 8;
	public Vertex2d camera = new Vertex2d(0, 0);
	public boolean docked = true;
	public shiphandler ships;
	private int tile_dir = 0;

	public Game(world world, Core core) {
		this.world = world;
		this.core = core;
		spritesheet player = new spritesheet("textures/inventory.png");
		inventory_gui = new GUI(core.G.rectangle(core.width - (82 * 2),
				core.height - (293 * 2), 82 * 2, 293 * 2,
				player.getcoords(17, 0, 99, 293)), player.gettexture(), core);
		inventory = new Inventory(30, this);

		Method up = core.G.obtain_method(Inventory.class, "position_up");
		Method down = core.G.obtain_method(Inventory.class, "position_down");
		Method click1 = core.G.obtain_method(Inventory.class, "click1");
		Method click2 = core.G.obtain_method(Inventory.class, "click2");
		Method click3 = core.G.obtain_method(Inventory.class, "click3");
		Method click4 = core.G.obtain_method(Inventory.class, "click4");
		Method click5 = core.G.obtain_method(Inventory.class, "click5");
		Method click6 = core.G.obtain_method(Inventory.class, "click6");
		Method click7 = core.G.obtain_method(Inventory.class, "click7");
		Method click8 = core.G.obtain_method(Inventory.class, "click8");
		Method click9 = core.G.obtain_method(Inventory.class, "click9");
		Method click10 = core.G.obtain_method(Inventory.class, "click10");
		Method click11 = core.G.obtain_method(Inventory.class, "click11");
		Method click12 = core.G.obtain_method(Inventory.class, "click12");
		Method click13 = core.G.obtain_method(Inventory.class, "click13");
		Method click14 = core.G.obtain_method(Inventory.class, "click14");
		Method click15 = core.G.obtain_method(Inventory.class, "click15");
		Method pos0 = core.G.obtain_method(Inventory.class, "option_0");
		Method pos1 = core.G.obtain_method(Inventory.class, "option_1");
		Method pos2 = core.G.obtain_method(Inventory.class, "option_2");
		Method pos3 = core.G.obtain_method(Inventory.class, "option_3");
		Method pos4 = core.G.obtain_method(Inventory.class, "option_4");
		Method pos5 = core.G.obtain_method(Inventory.class, "option_5");
		Method pos6 = core.G.obtain_method(Inventory.class, "option_6");
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 0)), 32, 32), false, new Encapsulated_method(
				pos0, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 1)), 32, 32), false, new Encapsulated_method(
				pos1, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 2)), 32, 32), false, new Encapsulated_method(
				pos2, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 3)), 32, 32), false, new Encapsulated_method(
				pos3, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 4)), 32, 32), false, new Encapsulated_method(
				pos4, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 5)), 32, 32), false, new Encapsulated_method(
				pos5, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 6)), 32, 32), false, new Encapsulated_method(
				pos6, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 56,
				core.height - 26, 14, 16), false, new Encapsulated_method(up,
				null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 56,
				core.height - 44, 14, 16), false, new Encapsulated_method(down,
				null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 0)), 32, 32), false, new Encapsulated_method(
				click1, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 1)), 32, 32), false, new Encapsulated_method(
				click2, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 2)), 32, 32), false, new Encapsulated_method(
				click3, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 3)), 32, 32), false, new Encapsulated_method(
				click4, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 4)), 32, 32), false, new Encapsulated_method(
				click5, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 5)), 32, 32), false, new Encapsulated_method(
				click6, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 6)), 32, 32), false, new Encapsulated_method(
				click7, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 7)), 32, 32), false, new Encapsulated_method(
				click8, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 8)), 32, 32), false, new Encapsulated_method(
				click9, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 9)), 32, 32), false, new Encapsulated_method(
				click10, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 10)), 32, 32), false, new Encapsulated_method(
				click11, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 11)), 32, 32), false, new Encapsulated_method(
				click12, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 12)), 32, 32), false, new Encapsulated_method(
				click13, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 13)), 32, 32), false, new Encapsulated_method(
				click14, null, inventory));
		inventory_gui.bind_button(new Vertex2d(core.width - 42, core.height
				- (48 + (38 * 14)), 132, 32), false, new Encapsulated_method(
				click15, null, inventory));
		inventory.add_item(new Wall(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Thruster(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Corner(null, core), 500);
		inventory.add_item(new Lattice(new Vertex2d(0,0),core,world),1000);
		inventory.add_item(new Floor(new Vertex2d(0,0), core,world),1000);
		inventory.add_item(new Gyroscope(new Vertex2d(0,0),core,world), 1000);
	}

	public void init() {

		ships = new shiphandler(core, 100);
		world w = new world(core,64,64);
		w.add_tile(new Wall(new Vertex2d(0,0),core,w));
		w.add_tile(new Wall(new Vertex2d(1,0),core,w));
		w.add_tile(new Wall(new Vertex2d(2,0),core,w));
		w.add_tile(new Wall(new Vertex2d(3,0),core,w));
		w.add_tile(new Wall(new Vertex2d(4,0),core,w));
		w.add_tile(new Wall(new Vertex2d(1,1),core,w));
		w.add_tile(new Wall(new Vertex2d(2,1),core,w));
		w.add_tile(new Wall(new Vertex2d(3,1),core,w));
		w.add_tile(new Wall(new Vertex2d(4,1),core,w));
		w.add_tile(new Wall(new Vertex2d(1,2),core,w));
		Ship s = new Ship(64,64,core,w,ships);
		s.set_position(new Vertex2d(0,0));
		ships.add_ship(s);
	}

	public void tick() {
		if (!core.ih.sa && !core.ih.ss && !core.ih.sd && !core.ih.sw) {
			timeheld = 0;

		} else if (timeheld < max) {
			timeheld += 0.1f;
		}

		if (core.ih.sa) {
			if (camera.x < 4 * 32 * zoom) {
				camera.x += step + (step * timeheld);
			}
		}
		if (core.ih.ss) {
			if (camera.y < 4 * 32 * zoom) {
				camera.y += step + (step * timeheld);
			}
		}
		if (core.ih.sd) {
			if (camera.x > 0 - (world.mapwidth * 32) + (core.width - (4 * 32))) {
				camera.x -= step + (step * timeheld);
			}
		}
		if (core.ih.sw) {
			if (camera.y > 0 - (world.mapheight * 32)
					+ (core.height - (4 * 32))) {
				camera.y -= step + (step * timeheld);
			}
		}

		// placing handling
		if (!on_gui(core.ih.obtain_mouse()) && docked) {

			if (core.ih.sm1) {

				Vertex2d mousepos = core.ih.obtain_mouse();
				Vertex2d actual = new Vertex2d(
						(int) (((mousepos.x - (camera.x)) / (32))),
						(int) ((mousepos.y - ((camera.y + 8))) / (32)));

				if (mousepos.x - camera.x < 0 || mousepos.y - camera.y < 0) {
					actual.x = -1;
					actual.y = -1;
				}

				if (!world.within_map(actual)) {

				} else if (inventory.get_selectedposition() == 4) {
					if (!world.check_tile(actual, inventory.selected)) {

						if (!inventory.selected.is_sub()) {
							if (inventory.take_selected(1)) {
								Tile temp = world.getnew(inventory.selected);
								temp.init(core);
								temp.setpos((int) actual.x, (int) actual.y);
								temp.set_dir(tile_dir);
								temp.set_world(world);
								if (world.check_tile(actual)) {

									Tile check_temp = world.get_tile(actual);
									world.remove_tile(actual);
									world.add_tile(temp, check_temp.get_index());

									inventory.add_item(check_temp, 1);// readd
																		// the
																		// taken
																		// file
																		// back
																		// into
																		// the
																		// inventory;
								} else {

									world.add_tile(temp);
								}
							}
						} else if (world.get_tile(actual) != null) {
							if (world.get_tile(actual).get_sub() == null) {
								if (inventory.take_selected(1)) {

									Sub_Tile temp = world
											.getnew((Sub_Tile) inventory.selected);

									temp.init(core);
									temp.set_super(world.get_tile(actual));
									temp.set_dir(tile_dir);
									world.set_sub(temp, actual);
								}
							}

						}
					}

				} else if (inventory.get_selectedposition() == 3) {
					if (world.check_tile(actual) && world.within_map(actual)) {

						inventory.add_item(world.get_tile(actual), 1);
						world.remove_tile(actual);

					}
				}
			}
		}
		// other buttons
		if (inventory.get_selectedposition() == 0) {
			if (inventory.get_lastselected() == 0
					|| inventory.get_lastselected() == 6) {
				inventory.set_selectedposition(1);
			} else {
				inventory.set_selectedposition(inventory.get_lastselected());

			}
			if (docked) {

				world.rotate(0);
				ships.generate_ships(world.get_map(), world.mapwidth);

			} else {
				world.reset();
				world.set_world(world);
				world.rotate(0);
			}
			docked = !docked;
			tile_dir = 0;

		}
		if (inventory.get_selectedposition() == 6) {
			if (inventory.get_lastselected() == 0
					|| inventory.get_lastselected() == 6) {
				inventory.set_selectedposition(1);
			} else {
				inventory.set_selectedposition(inventory.get_lastselected());
			}
			tile_dir += 1;
			if (tile_dir > 3) {
				tile_dir = 0;
			}
		}
		if (!docked) {
			handle_ships();
		}
		Vertex2d corrected = core.G.convert_coordinates(camera);
		corrected.u = zoom;
		if (docked) {
			world.update_camera(corrected);
			world.tick();
		} else {
			ships.bind_camera(camera);
			ships.tick();

		}
		inventory_gui.tick();
		if (docked) {
			inventory.tick();
		}

	}

	public void render() {

		if (docked) {
			world.render();
		} else {

			ships.render();
		}
		inventory_gui.render();
		if (docked) {
			inventory.render();
		}

	}

	public void cleanup() {
		world.cleanup();
		ships.cleanup();

	}

	public boolean on_gui(Vertex2d mousepos) {
		if (mousepos.x > core.width - 100 && mousepos.y > core.height - 588) {
			return true;
		}
		return false;
	}

	public void handle_ships() {
		boolean left = false, right = false, up = false, down = false,leftr = false,rightr = false;
		if (core.ih.sleft) {
			left = true;
		}
		if (core.ih.sright) {
			right = true;
		}
		if (core.ih.sup) {
			up = true;
		}
		if (core.ih.sdown) {
			down = true;
		}
		if(core.ih.sq){
			leftr = true;
		}
		if(core.ih.se){
			rightr = true;
		}
		ships.move_core(leftr,rightr,left, right, up, down);

	}
}
