package com.lcass.game;

import java.lang.reflect.Method;

import com.lcass.core.Core;
import com.lcass.entity.CrewHandler;
import com.lcass.entity.Entity;
import com.lcass.entity.Human;
import com.lcass.game.tiles.Cable;
import com.lcass.game.tiles.Cable_holder;
import com.lcass.game.tiles.Corner;
import com.lcass.game.tiles.Floor;
import com.lcass.game.tiles.Frame;
import com.lcass.game.tiles.Gyroscope;
import com.lcass.game.tiles.Lattice;
import com.lcass.game.tiles.Machine_gun;
import com.lcass.game.tiles.Solar;
import com.lcass.game.tiles.Sub_Tile;
import com.lcass.game.tiles.Thruster;
import com.lcass.game.tiles.Tile;
import com.lcass.game.tiles.Vaultdoor;
import com.lcass.game.tiles.Vaultwall;
import com.lcass.game.tiles.Wall;
import com.lcass.game.world.Ship;
import com.lcass.game.world.shiphandler;
import com.lcass.game.world.world;
import com.lcass.graphics.GUI;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Encapsulated_method;

public class Game {
	public world world;
	public Core core;
	private spritesheet mouse_sprite;
	private GUI inventory_gui;
	private GUI power_control;
	private GUI interaction;
	public Inventory inventory;
	public CrewHandler Entities;
	public shiphandler ships;
	private Entity selected_entity;
	private VBO mouse_square;
	public float step = 2f, zoom = 1f;
	private float timeheld = 0, max = 8;
	public Vertex2d camera = new Vertex2d(0, 0);
	private Vertex2d interaction_pos = new Vertex2d(0, 0, 0, 0);
	private Vertex2d currently_selected = new Vertex2d(-1, -1);
	public boolean docked = true;
	private boolean show_power = true;
	private boolean render_interaction = false;
	private int resistance_loc, voltage_loc,inter_loc, power_loc = 0;
	private int tile_dir = 0;
	
	

	public Game(world world, Core core) {
		//variables
		this.world = world;
		this.core = core;
		//entities
		Entities = new CrewHandler(core);
		
		//sprite setup
		spritesheet interaction_sprite = new spritesheet(
				"textures/interaction.png");

		spritesheet player = new spritesheet("textures/inventory.png");
		spritesheet voltage_sprite = new spritesheet("textures/playerGUI.png");
		mouse_sprite = new spritesheet("textures/selected.png");
		//mouse setup
				mouse_square = new VBO(core.G.mainvbo);
				mouse_square.create(12);
				mouse_square.bind_texture(mouse_sprite.gettexture());
				mouse_square.edit_data(core.G.square(0, 0, 32,
						mouse_sprite.getcoords(0, 0, 16, 16)));
		//GUI setup
		interaction = new GUI(new Vertex2d(0, core.height - 141, 114, 140),
				core.G.rectangle(0, core.height - 141, 114, 140,
						interaction_sprite.getcoords(0, 0, 57, 70)), interaction_sprite.gettexture(),
				core);
		
		inventory_gui = new GUI(new Vertex2d(core.width - (82 * 2), core.height
				- (293 * 2), 82 * 2, 293 * 2), core.G.rectangle(core.width
				- (82 * 2), core.height - (293 * 2), 82 * 2, 293 * 2,
				player.getcoords(17, 0, 99, 293)), player.gettexture(), core);
		inventory = new Inventory(30, this);
		power_control = new GUI(new Vertex2d(0, core.height - (37 * 2), 57 * 2,
				37 * 2), core.G.rectangle(0, core.height - (37 * 2), 57 * 2,
				37 * 2, voltage_sprite.getcoords(0, 0, 58, 38)),
				voltage_sprite.gettexture(), core);
		//text location setup
		inter_loc = interaction.draw_text(new Vertex2d(0, 25, 0, 0), "Interact", 7);
		resistance_loc = power_control.draw_text(new Vertex2d(5, 60),
				"ohms  00000", 7);
		power_loc = power_control.draw_text(new Vertex2d(5, 50), "watts 00000",
				7);
		
		voltage_loc = power_control.draw_text(new Vertex2d(15, 20),
				"0000000000v", 7);
		//inventory methods
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
		//interaction methods
		Method inter = core.G.obtain_method(Game.class, "interact");
		Method buil = core.G.obtain_method(Game.class, "build");
		Method dest = core.G.obtain_method(Game.class, "destroy");

		//interaction GUI
		interaction.bind_button(new Vertex2d(0, core.height - 50, 0, 0), true, new Encapsulated_method(
				inter, null, this));
		//inventory buttons
		inventory_gui.bind_button(new Vertex2d(core.width - 92, core.height
				- (88 + (38 * 0)), 32, 32),false, new Encapsulated_method(
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
		//inventory item setup
		inventory
				.add_item(new Vaultdoor(new Vertex2d(0, 0), core, world), 1000);
		inventory
				.add_item(new Vaultwall(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Wall(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Thruster(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Corner(null, core), 500);
		inventory.add_item(new Lattice(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Floor(new Vertex2d(0, 0), core, world), 1000);
		inventory
				.add_item(new Gyroscope(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Cable_holder(null, core), 500);
		inventory.add_item(new Cable(null, core), 500);
		inventory.add_item(new Solar(new Vertex2d(0, 0), core, world), 1000);
		inventory.add_item(new Frame(new Vertex2d(0, 0), core, world), 100);
		inventory.add_item(new Machine_gun(new Vertex2d(0, 0), core, world),
				100);
	}

	public void init() {
		ships = new shiphandler(core, 100);
		Method voltage_up = core.G
				.obtain_method(Ship.class, "increase_voltage");
		Method voltage_down = core.G.obtain_method(Ship.class,
				"decrease_voltage");
		power_control.bind_button(new Vertex2d(16, core.height - 60, 26, 10),
				false, new Encapsulated_method(voltage_down, null,
						ships.coreship));
		power_control.bind_button(new Vertex2d(26, core.height - 60, 36, 10),
				false,
				new Encapsulated_method(voltage_up, null, ships.coreship));
		world w = new world(core, 64, 64, -2);
		w.add_tile(new Wall(new Vertex2d(0, 0), core, w));
		w.add_tile(new Wall(new Vertex2d(1, 0), core, w));
		w.add_tile(new Wall(new Vertex2d(2, 0), core, w));
		w.add_tile(new Wall(new Vertex2d(3, 0), core, w));
		w.add_tile(new Wall(new Vertex2d(4, 0), core, w));
		w.add_tile(new Wall(new Vertex2d(1, 1), core, w));
		w.add_tile(new Wall(new Vertex2d(2, 1), core, w));
		w.add_tile(new Wall(new Vertex2d(3, 1), core, w));
		w.add_tile(new Wall(new Vertex2d(4, 1), core, w));
		w.add_tile(new Wall(new Vertex2d(1, 2), core, w));
		Ship s = new Ship(64, 64, core, w, ships);
		s.set_position(new Vertex2d(0, 0));
		ships.add_ship(s);
		Entity a = new Human(core, Entities, ships, 0, -2);
		a.set_position(new Vertex2d(32, 32, 0, 0));
		Entities.add_crew(a);
	}

	public void tick() {
		//mouse world coord calculating
		Vertex2d mousepos = core.ih.obtain_mouse();
		Vertex2d actual = new Vertex2d(
				(int) (((mousepos.x - (camera.x)) / (32))),
				(int) ((mousepos.y - ((camera.y + com.lcass.core.DEFINES.Y_ADJUST_MOUSE))) / (32)));
		if (show_power) {
			power_control.tick();
			power_control.set_instance(ships.coreship);
			power_control.modify_text(resistance_loc, new Vertex2d(5, 60),
					"ohms " + ships.coreship.get_resistance(), 7);
			power_control.modify_text(power_loc, new Vertex2d(5, 50), "watts "
					+ ships.coreship.get_power(), 7);
			power_control.modify_text(voltage_loc, new Vertex2d(15, 20), ""
					+ ships.coreship.get_voltage() + "v", 7);

		}
		if (!core.ih.a && !core.ih.s && !core.ih.d && !core.ih.w) {
			timeheld = 0;

		} else if (timeheld < max) {
			timeheld += 0.1f;
		}

		if (core.ih.a) {
			if (camera.x < 4 * 32 * zoom) {
				camera.x += step + (step * timeheld);
			}
		}
		if (core.ih.s) {
			if (camera.y < 4 * 32 * zoom) {
				camera.y += step + (step * timeheld);
			}
		}
		if (core.ih.d) {
			if (camera.x > 0 - (world.mapwidth * 32) + (core.width - (4 * 32))) {
				camera.x -= step + (step * timeheld);
			}
		}
		if (core.ih.w) {
			if (camera.y > 0 - (world.mapheight * 32)
					+ (core.height - (4 * 32))) {
				camera.y -= step + (step * timeheld);
			}
		}
		
		// placing handling
		if (!on_gui(core.ih.obtain_mouse()) && docked) {

			if (core.ih.mouse1) {

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

							if (inventory.take_selected(1)) {

								Sub_Tile temp = world
										.getnew((Sub_Tile) inventory.selected);
								if (world.get_tile(actual).get_sub() == null) {
									temp.init(core);
									temp.set_super(world.get_tile(actual));
									temp.set_dir(tile_dir);
									if (!world.set_sub(temp, actual)) {
										inventory.add_item(inventory.selected,
												1);
									}
								} else {
									temp.init(core);
									temp.set_super(world.get_tile(actual));
									temp.set_dir(tile_dir);
									if (!world.set_sub_sub(temp, actual)) {
										inventory.add_item(inventory.selected,
												1);
									}
								}

							}

						}
					}

				} else if (inventory.get_selectedposition() == 3) {
					if (world.check_tile(actual) && world.within_map(actual)) {

						inventory.add_item(world.get_tile(actual), 1);
						world.remove_tile(actual);

					}
				} else if (inventory.get_selectedposition() == 5) {
					Entities.get_crew_id(selected_entity.get_id()).move_to_loc(
							actual.whole().mult(32));
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
				inventory.set_weapon(true);
				update_weps();

			} else {
				world = ships.coreship.get_world();
				world.reset();
				world.set_world(world);
				world.rotate(0);
				inventory.set_weapon(false);
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
			inventory.selected.set_dir(tile_dir);
			inventory.force_update();
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

		inventory.tick();

		if (!on_gui(core.ih.obtain_mouse()) && !docked) {

			if (core.ih.mouse1) {

				if (mousepos.x - camera.x < 0 || mousepos.y - camera.y < 0) {
					actual.x = -1;
					actual.y = -1;
				}
				
				if (inventory.get_selectedposition() == 4) {
					if (selected_entity != null) {
						Vertex2d check = ships.coreship.get_world().null_check(
								ships.coreship.to_position(actual));
						boolean found = false;
						int tile_pos = ships.coreship.to_position(actual);
						if (check.x != -1) {
							found = true;
							selected_entity.move_to_loc(actual.whole()
									.sub(new Vertex2d(1, 0, 0, 0)).mult(32));
							selected_entity.set_target(tile_pos);
							Tile temp = new Frame(actual, core,
									ships.coreship.ship);
							temp.setpos((int) actual.x, (int) actual.y);
							temp.set_final(inventory.selected);
							selected_entity.set_frame(temp);
						}
						if (check.y != -1 && !found) {
							found = true;
							selected_entity.move_to_loc(actual.whole()
									.sub(new Vertex2d(-1, 0, 0, 0)).mult(32));
							selected_entity.set_target(tile_pos);
							Tile temp = new Frame(actual, core,
									ships.coreship.ship);
							temp.set_final(inventory.selected);
							temp.setpos((int) actual.x, (int) actual.y);
							selected_entity.set_frame(temp);
						}
						if (check.u != -1 && !found) {
							found = true;
							selected_entity.move_to_loc(actual.whole()
									.sub(new Vertex2d(0, 1, 0, 0)).mult(32));
							selected_entity.set_target(tile_pos);
							Tile temp = new Frame(actual, core,
									ships.coreship.ship);
							temp.set_final(inventory.selected);
							temp.setpos((int) actual.x, (int) actual.y);
							selected_entity.set_frame(temp);
						}
						if (check.v != -1 && !found) {
							found = true;
							selected_entity.move_to_loc(actual.whole()
									.sub(new Vertex2d(0, -1, 0, 0)).mult(32));
							selected_entity.set_target(tile_pos);
							Tile temp = new Frame(actual, core,
									ships.coreship.ship);
							temp.set_final(inventory.selected);
							temp.setpos((int) actual.x, (int) actual.y);
							selected_entity.set_frame(temp);
						}
					}
				}

				if (inventory.get_selectedposition() == 5) {
					if (Entities.get_crew(actual, -2) == null
							&& selected_entity != null) {
						selected_entity.set_selected(false);
					}

					if (Entities.get_crew(actual, -2) != null) {
						if (selected_entity != null) {
							selected_entity.set_selected(false);
						}
						if (Entities.get_crew(actual, -2).get_owner() == -2) {
							selected_entity = Entities.get_crew(actual, -2);
						}
					}

					if (selected_entity != null) {
						if (currently_selected.x != -1
								&& currently_selected.x != -2) {
							ships.get_ship((int) currently_selected.x).map[(int) currently_selected.y]
									.set_selected(false);
						} else if (currently_selected.x == -2) {
							ships.coreship.map[(int) currently_selected.y]
									.set_selected(false);
						}
						if (currently_selected.x != -1) {
							if (currently_selected.x != -2) {
								ships.get_ship((int) currently_selected.x)
										.overlay_update();
							} else {
								ships.coreship.overlay_update();
							}
						}
						currently_selected = new Vertex2d(-1, -1);

						selected_entity.set_selected(true);
					}
					if (selected_entity == null) {

						Vertex2d tile = ships.get_ship_at(actual);

						if (tile != null) {

							if (currently_selected.x != -1
									&& currently_selected.x != -2) {
								ships.get_ship((int) currently_selected.x).map[(int) currently_selected.y]
										.set_selected(false);
							} else if (currently_selected.x == -2) {
								ships.coreship.map[(int) currently_selected.y]
										.set_selected(false);
							}
							if (tile.x != -2) {
								if (ships.get_ship((int) tile.x).map[(int) tile.y]
										.selectable()) {
									ships.get_ship((int) tile.x).map[(int) tile.y]
											.set_selected(true);
								}
							} else {
								if (ships.coreship.map[(int) tile.y]
										.selectable()) {
									ships.coreship.map[(int) tile.y]
											.set_selected(true);
								}

							}
							currently_selected = tile;
							if (tile.x != -1) {
								if (tile.x != -2) {
									ships.get_ship((int) tile.x)
											.overlay_update();
								} else {
									ships.coreship.overlay_update();
								}
							}

						} else if (currently_selected.x != -1
								&& currently_selected.x != -2) {

							ships.get_ship((int) currently_selected.x).map[(int) currently_selected.y]
									.set_selected(false);
							currently_selected = new Vertex2d(-1, -1);
						} else if (currently_selected.x == -2) {
							ships.coreship.map[(int) currently_selected.y]
									.set_selected(false);
						}
					}
				}

			}
			if (core.ih.mouse2) {
				if (inventory.get_selectedposition() == 1) {
					Vertex2d tile_pos = ships.coreship.get_world().null_check(
							ships.coreship.to_position(actual));
					interaction.set_translation(mousepos);
					render_interaction = true;
				}
				if (inventory.get_selectedposition() == 5) {
					if (selected_entity != null) {
						selected_entity.move_to_loc(actual.whole().mult(32));
					}

				} else if (selected_entity != null) {

				}
			}
		}
		if (!on_gui(core.ih.obtain_mouse())) {
			Vertex2d draw_pos = mousepos.whole().sub(new Vertex2d(0, 8))
					.sub(new Vertex2d(camera.x % 32, camera.y % 32)).div(32)
					.to_int().mult(32);
			mouse_square.set_position(core.G.convert_coordinates(draw_pos
					.whole()
					.add(new Vertex2d((camera.x % 32), (camera.y % 32) + 16))));
		}
		if (!on_gui(core.ih.obtain_mouse()) && !docked) {

			if (core.ih.mouse1) {

				if (mousepos.x - camera.x < 0 || mousepos.y - camera.y < 0) {
					actual.x = -1;
					actual.y = -1;
				}

				if (!world.within_map(actual)) {

				} else if (inventory.get_selectedposition() == 4
						&& selected_entity == null) {
					weaponset weapons = inventory.wep_storage[inventory
							.get_selected_wep()];
					if (weapons.type != null) {
						Tile[] shootable = weapons.weapons;
						for (int i = 0; i < shootable.length; i++) {
							if (shootable[i] != null) {
								Vertex2d move = ships.coreship.ph
										.calc_step(
												shootable[i].get_world_pos(),
												actual.whole()
														.mult(32)
														.add(ships.coreship.correct_pos));
								ships.coreship.ship.get_tile(
										shootable[i].get_pos()).set_movement(
										move);
								ships.coreship.ship.get_tile(
										shootable[i].get_pos()).fire();
							}
						}
					}
				}
			}
		}
		Entities.tick();
		if(render_interaction){
			interaction.tick();
		}
	}

	public void render() {

		if (docked) {
			world.render();
		} else {

			ships.render();
			Entities.render();
		}
		inventory_gui.render();

		inventory.render();
		mouse_square.render();
		if (show_power) {
			power_control.render();
		}
		if(render_interaction){
			interaction.render();
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
		boolean left = false, right = false, up = false, down = false, leftr = false, rightr = false;
		if (core.ih.left) {
			left = true;
		}
		if (core.ih.right) {
			right = true;
		}
		if (core.ih.up) {
			up = true;
		}
		if (core.ih.down) {
			down = true;
		}
		if (core.ih.q) {
			leftr = true;
		}
		if (core.ih.e) {
			rightr = true;
		}
		ships.move_core(leftr, rightr, left, right, up, down);

	}

	public void update_weps() {
		Tile[] weapons = ships.coreship.get_weapons();
		for (int i = 0; i < weapons.length; i++) {
			inventory.add_weapon(weapons[i]);
		}
	}

	public void interact() {

	}

	public void build() {

	}

	public void destroy() {

	}

}
