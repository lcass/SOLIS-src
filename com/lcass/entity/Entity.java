package com.lcass.entity;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.game.Items.Item;
import com.lcass.game.Items.construction.Construction;
import com.lcass.game.tiles.Frame;
import com.lcass.game.tiles.Tile;
import com.lcass.game.world.Ship;
import com.lcass.game.world.shiphandler;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Path;

public abstract class Entity {
	protected Vertex2d abs_pos, position, movement = new Vertex2d(0, 0, 0, 0),
			last_position = new Vertex2d(0, 0, 0, 0),
			render_pos = new Vertex2d(0, 0, 0, 0);
	protected Vertex2d sprite = new Vertex2d(0, 0, 16, 16);
	protected Vertex2d sprite_2 = new Vertex2d(16, 0, 32, 16);
	protected Vertex2d sprite_3 = new Vertex2d(32, 0, 48, 16);
	protected Vertex2d interaction = new Vertex2d(0, 0, 0, 0);
	protected int current_tile = 0;
	protected int ship = -1;
	protected int owner = -1;
	protected int movement_stage = 0;
	protected int action_timer = 0;
	protected int action_threshold = 60;
	protected int id = -1;// unset
	protected int target_tile = -1;
	protected boolean alive = true;
	protected boolean selected = false;
	protected boolean moving = false;
	protected boolean path_move = false;
	protected boolean interacting = false;
	protected shiphandler ships;
	protected Tile frame_to_build;
	protected Core core;
	protected Path path;
	protected CrewHandler handler;
	protected Item[] items = new Item[10];
	protected Item selected_item;

	public Entity(Core core, CrewHandler h, shiphandler ship, int id, int owner) {
		this.core = core;
		this.handler = h;
		ships = ship;
		this.owner = owner;
		abs_pos = new Vertex2d(0, 0, 0, 0);
		this.id = id;
	}

	public void move(Vertex2d addition) {
		moving = true;
		movement = addition;
	}

	public void set_position(Vertex2d position) {
		this.last_position = position;
		calculate_variables();

	}

	public boolean move_to_loc(Vertex2d finished) {
		if (ships.get_ship(ship) == null) {
			return false;
		}
		if (ships.get_ship(ship).get_tile(
				ships.get_ship(ship).tile_at(finished.whole().div(32))) == null) {
			return false;
		}
		calculate_variables();
		this.moving = true;
		Vertex2d finish = finished.whole().div(32);
		this.path_move = true;
		ArrayList<Integer> open = new ArrayList<Integer>();
		ArrayList<Integer> closed = new ArrayList<Integer>();
		closed.add(current_tile);
		int[] current_addition = moveable(current_tile, closed, open);
		if (current_addition[0] != -1) {
			open.add(current_addition[0]);
		}
		if (current_addition[1] != -1) {
			open.add(current_addition[1]);
		}
		if (current_addition[2] != -1) {
			open.add(current_addition[2]);
		}
		if (current_addition[3] != -1) {
			open.add(current_addition[3]);
		}
		Ship curr_ship = ships.get_ship(ship);
		while (!open.isEmpty() && curr_ship != null) {

			int lowest_pos = open.get(0);
			int lowest_heuristic = 0;
			int open_pos = 0;
			int heuristic = 0;
			for (int i = 0; i < open.size(); i++) {

				Tile ct = curr_ship.get_tile(open.get(i));
				heuristic = (int) Math.sqrt(Math.pow(finish.x - ct.get_pos().x,
						2) + Math.pow(finish.y - ct.get_pos().y, 2));
				if (i == 0) {
					lowest_heuristic = heuristic;
				}
				if (heuristic < lowest_heuristic) {
					open_pos = i;
					lowest_pos = open.get(i);
					lowest_heuristic = heuristic;
				}
			}
			if (heuristic == 0) {
				this.path = new Path();
				int current_t = lowest_pos;
				int super_t = curr_ship.get_tile(lowest_pos).get_super();
				while (current_t != current_tile) {
					Tile ct = curr_ship.get_tile(current_t);
					Tile st = curr_ship.get_tile(super_t);
					Vertex2d change = ct.get_pos().whole().sub(st.get_pos())
							.mult(16);
					path.add_step(change);
					current_t = super_t;
					super_t = curr_ship.get_tile(current_t).get_super();
				}
				path.add_step(movement);
				path.invert();
				return true;
			}
			closed.add(lowest_pos);
			open.remove(open_pos);
			current_addition = moveable(lowest_pos, closed, open);
			if (current_addition[0] != -1) {
				open.add(current_addition[0]);
			}
			if (current_addition[1] != -1) {
				open.add(current_addition[1]);
			}
			if (current_addition[2] != -1) {
				open.add(current_addition[2]);
			}
			if (current_addition[3] != -1) {
				open.add(current_addition[3]);
			}
		}
		this.path = new Path();
		return false;
	}

	protected int[] moveable(int current, ArrayList<Integer> closed,
			ArrayList<Integer> open) {
		int[] data = new int[4];
		data[0] = -1;
		data[1] = -1;
		data[2] = -1;
		data[3] = -1;
		Ship curr_ship = ships.get_ship(ship);
		if (current - curr_ship.width >= 0) {
			if (can_move(current - curr_ship.width, closed, open)) {
				data[0] = current - curr_ship.width;

			}
		}
		if (current + curr_ship.width < curr_ship.map.length) {

			if (can_move(current + curr_ship.width, closed, open)) {

				data[1] = current + curr_ship.width;
			}
		}
		if (current + 1 < curr_ship.map.length) {

			if (can_move(current + 1, closed, open)) {

				data[2] = current + 1;
			}
		}
		if (current - 1 >= 0) {
			if (can_move(current - 1, closed, open)) {

				data[3] = current - 1;
			}
		}
		if (data[0] != -1) {
			curr_ship.map[data[0]].set_super(current);

		}
		if (data[1] != -1) {
			curr_ship.map[data[1]].set_super(current);
		}
		if (data[2] != -1) {
			curr_ship.map[data[2]].set_super(current);
		}
		if (data[3] != -1) {
			curr_ship.map[data[3]].set_super(current);
		}

		return data;
	}

	//
	protected boolean can_move(int a, ArrayList<Integer> closed,
			ArrayList<Integer> open) {
		if (ships.get_ship(ship).get_tile(a) != null) {
			if (ships.get_ship(ship).get_tile(a).is_wall()) {
				return false;
			}
			for (int i = 0; i < closed.size(); i++) {
				if (closed.get(i) == a) {

					return false;
				}
			}
			for (int i = 0; i < open.size(); i++) {
				if (open.get(i) == a) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	public void damage() {

	}

	public int get_owner() {
		return owner;
	}

	public void set_owner(int owner) {
		this.owner = owner;
	}

	public boolean get_alive() {
		return this.alive;
	}

	public Vertex2d get_position() {
		return position;
	}

	public int get_ship() {
		return this.ship;
	}

	public void set_ship(int ship) {
		this.ship = ship;

	}

	protected void sub_tick() {

	}

	boolean set = false;

	public void calculate_variables() {
		abs_pos = this.position.whole().add(last_position);
	}

	private boolean constructing = false;
	private boolean call = false;
	private Frame frame;
	private int timer = -1;
	private boolean adj_build = false;
	private boolean inventory_setup = false;
	private boolean build_selected = false;
	private String recipe = "";
	private Vertex2d construction_pos = new Vertex2d(0, 0, 0, 0);

	public void construct(Vertex2d pos, int ship) {
		if(moving){
			return;
		}
		// updated
		if (ship != this.ship) {
			return;// no intership construction
		}
		if(inventory_setup && !build_selected){
			if(core.game.inventory.selected != null){
				System.out.println("selected");
				build_selected = true;
				recipe = core.game.inventory.selected.get_name();
				core.game.inventory.set_inventory(true);
			}
			else{
				build_selected = false;
			}
		}
		if (frame != null) {
			if (frame.finished()) {
				reset_construction();
				return;
			}
		}
		if (!constructing) {
			if (!inventory_setup) {
				construction_pos = pos.whole();
				core.game.inventory.set_inventory(false);
				core.game.inventory.clear_storage();
				Construction[] recipes = core.game.construction
						.check_recipes(items);
				System.out.println(recipes.length + "recipe");
				for (int i = 0; i < recipes.length; i++) {
					Tile t = core.game.world.getnew(recipes[i].get_result());
					t.init(core);
					core.game.inventory.add_item(t, 1);
				}
				core.game.inventory.selected = null;
				inventory_setup = true;
				call = true;
			} else if (build_selected) {

				constructing = true;
				if (!core.game.construction.get_recipe(recipe).check(items)) {
					reset_construction();
					return;
				}
				if (next_to_loc(pos)) {

				} else {
					reset_construction();
					return;
				}
			}
		}
		if (constructing) {
			if (!adj_build) {
				if (adjacent(ship, pos.whole().div(32))) {
					adj_build = true;
					timer = core.game.construction.setup_tick();
					frame = new Frame(pos.whole().div(32), core,
							core.game.universe.get_ship(ship).get_world());
					frame.assign_construct(
							core.game.construction.get_recipe(recipe), timer,
							this);
					ships.get_ship(ship).add_tile(frame);
				}
			}
		}
	}
	public void reset_construction(){
		constructing = false;
		frame = null;
		adj_build = false;
		inventory_setup = false;
		build_selected = false;
		call = false;
	}

	public void tick() {
		Ship curr_ship = ships.get_ship(ship);
		if (action_timer != 0) {
			action_timer++;
			if (action_timer >= action_threshold) {
				action_timer = 0;
			}
		}

		if (curr_ship == null) {
			Collision_package cp = ships.entity_collision_check(this);

			if (cp.s != null) {
				curr_ship = cp.s;

				if (curr_ship.get_tile(cp.tile) != null) {
					Vertex2d tile = curr_ship.get_tile(cp.tile).get_pos()
							.whole();
					current_tile = cp.tile;
					tile.mult(16);
					position = tile.whole();
					last_position = curr_ship.correct_pos.whole().div(2);
					tile.add(curr_ship.correct_pos);
				} else {
					last_position = curr_ship.correct_pos.whole().add(position);
					position = new Vertex2d(0, 0, 0, 0);
				}
				ship = curr_ship.get_ship_id();
				// movement = tile.whole().sub(abs_pos);

			}
		} else {

			current_tile = curr_ship.tile_at(position.whole().div(16));

			if (curr_ship.get_tile(current_tile) == null && !moving) {
				ship = -1;

			} else {
				last_position = curr_ship.correct_pos.whole().div(2);
				calculate_variables();

			}

		}
		//update inventory
		if(selected){
			core.game.inventory.load_items(items);
		}
		// construction , not very simple.
		if (call) {
			construct(construction_pos, ship);
		}

		// interaction , fairly simple
		if (interacting) {
			if (com.lcass.util.Util.adjacent(position, interaction)) {
				Ship ship_curr = core.game.universe.get_ship(ship);
				Tile t = ship_curr.get_tile(ship_curr.tile_at(interaction
						.whole().div(16)));
				t.interact(this);
				interacting = false;
			}
		}
		render_pos = abs_pos.whole().div(1);
		if (movement.x != 0 || movement.y != 0) {
			moving = true;
		}
		this.sub_tick();
		if (moving) {
			handler.update();
			boolean xtrue = false;
			boolean ytrue = false;

			if (movement == null) {
				if (path == null) {
					path_move = false;
					return;
				}
				Vertex2d temp_pos = path.next();
				if (temp_pos == null) {
					moving = false;
					path_move = false;
					movement_stage = 0;
					return;
				} else {
					movement = temp_pos;
				}
			}
			if (movement.x != 0 && movement.y != 0) {
				if (movement.x % 16 == 0 || movement.y % 16 == 0) {
					movement_stage++;
					if (movement_stage == 3) {
						movement_stage = 0;
					}
				}
			} else if (movement.x != 0) {
				if (movement.x % 16 == 0) {
					movement_stage++;
					if (movement_stage == 3) {
						movement_stage = 0;
					}
				}
			} else if (movement.y != 0) {
				if (movement.y % 16 == 0) {
					movement_stage++;
					if (movement_stage == 3) {
						movement_stage = 0;
					}
				}
			}
			if (movement.x > 0) {
				if (movement.x >= 1) {
					position.x += 1;
					movement.x -= 1;
				} else {
					position.x += movement.x;
					movement.x = 0;
				}
			} else {
				if (movement.x == 0) {
					xtrue = true;
				} else if (movement.x <= -1) {
					position.x -= 1;
					movement.x += 1;
				} else {
					position.x += movement.x;
					movement.x = 0;
				}
			}
			if (movement.y > 0) {
				if (movement.y >= 1) {
					position.y += 1;
					movement.y -= 1;
				} else {
					position.y += movement.y;
					movement.y = 0;
				}
			} else {
				if (movement.y == 0) {
					ytrue = true;
				} else if (movement.y <= -1) {
					position.y -= 1;
					movement.y += 1;
				} else {
					position.y += movement.y;
					movement.y = 0;
				}
			}
			if (ytrue && xtrue && !path_move) {
				moving = false;
				movement_stage = 0;
			}
			if (ytrue && xtrue && path_move) {
				if (path == null) {
					path_move = false;
					return;
				}
				Vertex2d temp = path.next();
				if (temp == null) {
					moving = false;
					path_move = false;
					movement_stage = 0;
				} else {
					movement = temp;
					Vertex2d target_loc = position.whole().add(movement);
					target_loc.div(16);
					core.game.universe.get_ship(ship).bump(target_loc,this);
				}
			}
		}
	}

	public spritecomponent get_sprite() {
		Vertex2d selected_sprite = sprite;
		switch (movement_stage) {
		case 0:
			selected_sprite = sprite;
			break;
		case 1:
			selected_sprite = sprite_2;
			break;
		case 2:
			selected_sprite = sprite_3;
			break;
		}
		return core.crew_sprite.getcoords((int) selected_sprite.x,
				(int) selected_sprite.y, (int) selected_sprite.u,
				(int) selected_sprite.v);
	}

	public void set_path(Path p) {
		this.path = p;
		moving = true;
		path_move = true;
	}

	public Vertex2d get_abs() {
		return this.abs_pos;
	}

	public void set_last(Vertex2d pos) {

	}

	public Vertex2d get_render_pos() {
		return render_pos;
	}

	public void set_selected(boolean selected) {
		this.selected = selected;
	}

	public boolean selected() {
		return this.selected;
	}

	public void set_target(int i) {
		target_tile = i;
	}

	public Vertex2d get_rotation() {
		if (ships == null) {
			return new Vertex2d(0, 0, 0, 0);
		}
		if (ships.get_ship(ship) == null) {
			return new Vertex2d(0, 0, 0, 0);
		}
		return ships
				.get_ship(ship)
				.get_rot_pos()
				.whole()
				.add(new Vertex2d(0, 0, ships.get_ship(ship).compound_rotation,
						0));
	}

	public void set_id(int id) {
		this.id = id;
	}

	public int get_id() {
		return id;
	}

	public void on_select() {

	}

	public void on_hit(Projectile p) {

	}

	public void on_splode(int power) {

	}

	public boolean try_loc(Vertex2d target) {
		Vertex2d right = target.whole().add(new Vertex2d(32, 0, 0, 0));
		Vertex2d left = target.whole().add(new Vertex2d(-32, 0, 0, 0));
		Vertex2d up = target.whole().add(new Vertex2d(0, 32, 0, 0));
		Vertex2d down = target.whole().add(new Vertex2d(0, -32, 0, 0));
		if (move_to_loc(target)) {
			return true;
		}
		if (move_to_loc(right)) {
			return true;
		}
		if (move_to_loc(left)) {
			return true;
		}
		if (move_to_loc(up)) {
			return true;
		}
		if (move_to_loc(down)) {
			return true;
		}
		return false;
	}

	public boolean next_to_loc(Vertex2d target) {
		Vertex2d right = target.whole().add(new Vertex2d(32, 0, 0, 0));
		Vertex2d left = target.whole().add(new Vertex2d(-32, 0, 0, 0));
		Vertex2d up = target.whole().add(new Vertex2d(0, 32, 0, 0));
		Vertex2d down = target.whole().add(new Vertex2d(0, -32, 0, 0));
		if (move_to_loc(right)) {
			return true;
		}
		if (move_to_loc(left)) {
			return true;
		}
		if (move_to_loc(up)) {
			return true;
		}
		if (move_to_loc(down)) {
			return true;
		}
		return false;
	}

	public void interact(Vertex2d target) {
		if (target == null) {
			return;

		}
		Ship current = core.game.universe.get_ship(ship);
		if (current == null) {
			return;

		}
		if (current.get_tile(current.tile_at(target.whole().div(32))) == null) {
			return;
		}

		else {
			if (try_loc(target)) {
				interaction = target.whole().div(2);
				interacting = true;
			}
		}
	}

	public Item[] retreive_inventory() {
		return items;
	}

	public void add_item(Item a) {

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].get_name() == a.get_name()) {
					items[i].set_stack(items[i].get_stack() + a.get_stack());
					return;
				}
			}
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = a;

				return;
			}
		}
	}

	public void remove_item(Item a) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].get_name() == a.get_name()) {
					items[i].set_stack(items[i].get_stack() - a.get_stack());
					if (items[i].get_stack() == 0) {
						items[i] = null;
					} else if (items[i].get_stack() < 0) {
						System.out
								.println("More of an item was removed from the inventory than there was availible");
					}
				}
			}
		}
	}

	public boolean adjacent(int ship_id, Vertex2d pos) {
		if (ship != ship_id) {
			return false;
		}
		int tile_pos = core.game.universe.get_ship(ship).tile_at(pos);
		if (tile_pos == -1) {
			return false;
		}

		Vertex2d tile = pos.whole();
		Vertex2d left = position.whole().to_int()
				.add(new Vertex2d(-16, 0, 0, 0));
		Vertex2d right = position.whole().to_int()
				.add(new Vertex2d(16, 0, 0, 0));
		Vertex2d up = position.whole().to_int().add(new Vertex2d(0, 16, 0, 0));
		Vertex2d down = position.whole().to_int()
				.add(new Vertex2d(0, -16, 0, 0));
		left.div(16);
		right.div(16);
		up.div(16);
		down.div(16);
		if (position.whole().div(16).to_int().equals(tile)) {
			return true;
		}
		if (left.equals(tile)) {
			return true;
		}
		if (right.equals(tile)) {
			return true;
		}
		if (up.equals(tile)) {
			return true;
		}
		if (down.equals(tile)) {
			return true;
		}
		System.out.println("false");
		return false;
	}
	public void unselect(){
		core.game.inventory.set_weapon(true);
	}
	public void select(){//init and cleanup functions basically
		core.game.inventory.set_inventory(true);
		core.game.inventory.load_items(items);
	}
	public void right_mouse(Vertex2d loc){
		
	}
	public void left_mouse(Vertex2d loc){
		
	}
	

}
