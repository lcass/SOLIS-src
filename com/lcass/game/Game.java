package com.lcass.game;

import java.lang.reflect.Method;

import com.lcass.core.Core;
import com.lcass.entity.CrewHandler;
import com.lcass.entity.Entity;
import com.lcass.entity.Human;
import com.lcass.game.Items.Metal;
import com.lcass.game.Items.construction.Construction_handler;
import com.lcass.game.Items.construction.wall;
import com.lcass.game.tiles.Cable;
import com.lcass.game.tiles.Cable_holder;
import com.lcass.game.tiles.Corner;
import com.lcass.game.tiles.Floor;
import com.lcass.game.tiles.Frame;
import com.lcass.game.tiles.Gyroscope;
import com.lcass.game.tiles.Lattice;
import com.lcass.game.tiles.Machine_gun;
import com.lcass.game.tiles.Solar;
import com.lcass.game.tiles.Thruster;
import com.lcass.game.tiles.Tile;
import com.lcass.game.tiles.Vaultdoor;
import com.lcass.game.tiles.Vaultwall;
import com.lcass.game.tiles.Wall;
import com.lcass.game.world.Ship;
import com.lcass.game.world.Universe;
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
	public GUI inventory_gui;
	private GUI power_control;
	public GUI interaction;
	public Inventory inventory;
	public CrewHandler Entities;
	public Entity selected_entity;
	private VBO mouse_square;
	public Interaction_Controller IC;
	public float step = 2f, zoom = 1f;
	private float timeheld = 0, max = 8;
	public Vertex2d camera = new Vertex2d(0, 0);
	public Vertex2d interaction_pos = new Vertex2d(0, 0, 0, 0);
	public Vertex2d currently_selected = new Vertex2d(-1, -1);
	public boolean docked = true;
	private boolean show_power = true;
	public Construction_handler construction;
	public boolean render_interaction = false;
	private int resistance_loc, voltage_loc,inter_loc, power_loc = 0;
	public int tile_dir = 0;
	
	public Universe universe;
	

	public Game(world world, Core core) {
		//variables
		this.world = world;
		this.core = core;
		//entities
		Entities = new CrewHandler(core);
		//entities with construction
		construction = new Construction_handler();
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
		interaction = new GUI(new Vertex2d(0,0, 114, 140),
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
		resistance_loc = power_control.draw_text(new Vertex2d(5, 60),
				"ohms  00000", 7);
		power_loc = power_control.draw_text(new Vertex2d(5, 50), "watts 00000",
				7);
		
		voltage_loc = power_control.draw_text(new Vertex2d(15, 20),
				"0000000000v", 7);
		//initiate universe
		universe = new Universe(core);
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
		interaction.bind_button(new Vertex2d(0, core.height - 40, 0, 0), true, new Encapsulated_method(
				inter, null, this),"Interact");
		interaction.bind_button(new Vertex2d(0, core.height - 70, 0, 0), true, new Encapsulated_method(
				buil, null, this),"Build");
		interaction.bind_button(new Vertex2d(0, core.height - 100, 0, 0), true, new Encapsulated_method(
				dest, null, this),"Destroy");
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
		IC = new Interaction_Controller(core,this,universe.ships);
		Method voltage_up = core.G
				.obtain_method(Ship.class, "increase_voltage");
		Method voltage_down = core.G.obtain_method(Ship.class,
				"decrease_voltage");
		power_control.bind_button(new Vertex2d(16, core.height - 60, 26, 10),
				false, new Encapsulated_method(voltage_down, null,
						universe.ships.coreship));
		power_control.bind_button(new Vertex2d(26, core.height - 60, 36, 10),
				false,
				new Encapsulated_method(voltage_up, null, universe.ships.coreship));
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
		Ship s = new Ship(64, 64, core, w, universe.ships);
		s.set_position(new Vertex2d(0, 0));
		universe.ships.add_ship(s);
		Entity a = new Human(core, Entities, universe.ships, 0, -2);
		a.add_item(new Metal(core,Entities,50));
		a.set_position(new Vertex2d(32,32));
		Entities.add_crew(a);
		//setup recipes
		construction.add_recipe(new wall());
	}

	public void tick() {
		//mouse world coord calculating
		Vertex2d mousepos = core.ih.obtain_mouse();
		Vertex2d actual = new Vertex2d(
				(int) (((mousepos.x - (camera.x)) / (32))),
				(int) ((mousepos.y - ((camera.y + com.lcass.core.DEFINES.Y_ADJUST_MOUSE))) / (32)));
		Vertex2d mouse_tile = actual;
		if(universe.ships.coreship != null){
			mouse_tile = actual.whole().mult(32).add(universe.ships.coreship.correct_pos);
		}
		if (show_power) {
			power_control.tick();
			power_control.set_instance(universe.ships.coreship);
			power_control.modify_text(resistance_loc, new Vertex2d(5, 60),
					"ohms " + universe.ships.coreship.get_resistance(), 7);
			power_control.modify_text(power_loc, new Vertex2d(5, 50), "watts "
					+ universe.ships.coreship.get_power(), 7);
			power_control.modify_text(voltage_loc, new Vertex2d(15, 20), ""
					+ universe.ships.coreship.get_voltage() + "v", 7);

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
		IC.tick();
		inventory_gui.tick();
		construction.tick();
		inventory.tick();
		Vertex2d corrected = core.G.convert_coordinates(camera);
		corrected.u =zoom;
		Entities.tick();
		if(render_interaction){
			interaction.tick();
		}
		if (docked) {
			world.update_camera(corrected);
			world.tick();
		} else {
			universe.ships.bind_camera(camera);
			universe.tick();
		}
		if (!IC.on_gui(core.ih.obtain_mouse())) {
			Vertex2d draw_pos = mousepos.whole().sub(new Vertex2d(0, 8))
					.sub(new Vertex2d(camera.x % 32, camera.y % 32)).div(32)
					.to_int().mult(32);
			mouse_square.set_position(core.G.convert_coordinates(draw_pos
					.whole()
					.add(new Vertex2d((camera.x % 32), (camera.y % 32) + 16))));
		}
	}

	public void render() {

		if (docked) {
			world.render();
		} else {

			universe.render();
			Entities.render();
		}
		inventory_gui.render();

		inventory.render();
		mouse_square.render();
		IC.render();
		if (show_power) {
			power_control.render();
		}
		if(render_interaction){
			interaction.render();
		}
		

	}

	public void cleanup() {
		world.cleanup();
		universe.cleanup();

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
		universe.ships.move_core(leftr, rightr, left, right, up, down);

	}

	public void update_weps() {
		Tile[] weapons = universe.ships.coreship.get_weapons();
		for (int i = 0; i < weapons.length; i++) {
			inventory.add_weapon(weapons[i]);
		}
	}

	public void interact() {
		if(selected_entity != null){
			selected_entity.interact(interaction_pos);
			
			
		}
	}

	public void build() {
		if(selected_entity != null){
			selected_entity.construct(interaction_pos,-2);
		}
	}

	public void destroy() {

	}
	public Inventory get_inventory(){
		return this.inventory;
	}

}
