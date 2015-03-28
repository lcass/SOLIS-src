package com.lcass.entity;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.game.tiles.Tile;
import com.lcass.game.world.shiphandler;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Path;

public abstract class Entity {
	protected Vertex2d abs_pos, position, movement = new Vertex2d(0, 0, 0, 0),
			last_position = new Vertex2d(0, 0, 0, 0), relative = new Vertex2d(
					0, 0, 0, 0), render_pos = new Vertex2d(0, 0, 0, 0);

	protected int current_tile = 0;
	protected int ship;
	protected int crew_position = -1;
	protected boolean alive = true;
	protected shiphandler ships;
	protected Vertex2d sprite = new Vertex2d(0, 0, 0, 0);
	protected Core core;
	protected Path path;
	protected boolean moving = false;
	protected boolean path_move = false;
	protected CrewHandler handler;

	public Entity(Core core, CrewHandler h) {
		this.core = core;
		this.handler = h;
		abs_pos = new Vertex2d(0, 0, 0, 0);
	}

	public void move(Vertex2d addition) {
		moving = true;
		movement = addition;
	}

	public void set_position(Vertex2d position) {
		this.position = position;
	}

	public void move_to_loc(Vertex2d finished) {
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
		int position = 0;

		while (position < open.size()) {
			boolean skip = false;
			for (int i = 0; i < closed.size(); i++) {
				if (closed.get(i) == open.get(position)) {
					skip = true;

				}
			}
			if (skip) {

				position++;
				continue;
			}

			int selected_tile = open.get(position);

			closed.add(selected_tile);
			Tile current = handler.ship.get_tile(selected_tile);
			Tile super_tile = handler.ship.get_tile(current.get_super());
			int heuris = (int) Math.sqrt(Math.pow(current.get_pos().x
					- finish.x, 2)
					+ Math.pow(current.get_pos().y - finish.y, 2));
			int super_heuris = (int) Math.sqrt(Math.pow(super_tile.get_pos().x
					- finish.x, 2)
					+ Math.pow(super_tile.get_pos().y - finish.y, 2));
			System.out.println(heuris);
			current_addition = moveable(selected_tile, closed, open);
			if (heuris == 0) {
				System.out.println("found");
				this.path = new Path();
				return;
			}
			if (heuris <= super_heuris) {
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
			position += 1;
		}
		this.path = new Path();
	}

	protected int[] moveable(int current, ArrayList<Integer> closed,
			ArrayList<Integer> open) {
		int[] data = new int[4];
		data[0] = -1;
		data[1] = -1;
		data[2] = -1;
		data[3] = -1;

		if (current - handler.ship.width >= 0) {
			if (can_move(current - handler.ship.width, closed, open)) {
				data[0] = current - handler.ship.width;

			}
		}
		if (current + handler.ship.width < handler.ship.map.length) {

			if (can_move(current + handler.ship.width, closed, open)) {

				data[1] = current + handler.ship.width;
			}
		}
		if (current + 1 < handler.ship.map.length) {

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
			handler.ship.map[data[0]].set_super(current);

		}
		if (data[1] != -1) {
			handler.ship.map[data[1]].set_super(current);
		}
		if (data[2] != -1) {
			handler.ship.map[data[2]].set_super(current);
		}
		if (data[3] != -1) {
			handler.ship.map[data[3]].set_super(current);
		}

		return data;
	}

	protected boolean can_move(int a, ArrayList<Integer> closed,
			ArrayList<Integer> open) {
		if (handler.ship.get_tile(a) != null) {

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

	public void set_crew_position(int pos) {
		this.crew_position = pos;
	}

	public int get_crew_position() {
		return this.crew_position;
	}

	public boolean get_alive() {
		return this.alive;
	}

	public Vertex2d get_position() {
		return this.position;
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
		relative = abs_pos.whole().sub(
				core.G.revert_coordinates(handler.ship.absolute_position));
		current_tile = Math.round((relative.x + movement.x) / 16)
				+ (Math.round((relative.y + movement.y) / 16) * handler.ship.width);

	}

	public void tick() {
		if (handler.ship.get_tile(current_tile) != null && !set) {
			last_position = handler.ship.correct_pos.whole();
			position = new Vertex2d(Math.round(relative.x),
					Math.round(relative.y));
			movement = new Vertex2d((handler.ship.get_tile(current_tile)
					.get_pos().x * 16) - position.x, (handler.ship.get_tile(
					current_tile).get_pos().y * 16)
					- position.y);

			render_pos = position;

			set = true;
		} else {
			render_pos = position;
		}
		if (handler.ship.get_tile(current_tile) == null) {
			render_pos = abs_pos.whole().sub(handler.ship.correct_pos);
			set = false;
		} else {
			last_position = handler.ship.correct_pos.whole();
		}
		calculate_variables();
		render_pos.to_int();

		if (movement.x != 0 || movement.y != 0) {
			moving = true;
		}
		this.sub_tick();
		if (moving) {
			handler.update();
			boolean xtrue = false;
			boolean ytrue = false;
			if (movement == null) {
				Vertex2d temp_pos = path.next();
				if (temp_pos == null) {
					moving = false;
					path_move = false;
					return;
				} else {
					movement = temp_pos;
				}
			}
			if (movement.x > 0) {
				position.x += 1;
				movement.x -= 1;
			} else {
				if (movement.x == 0) {
					xtrue = true;
				} else {
					position.x -= 1;
					movement.x += 1;
				}
			}
			if (movement.y > 0) {
				position.y += 1;
				movement.y -= 1;
			} else {
				if (movement.y == 0) {
					ytrue = true;
				} else {
					position.y -= 1;
					movement.y += 1;
				}
			}
			if (ytrue && xtrue && !path_move) {
				moving = false;
			}
			if (ytrue && xtrue && path_move) {
				Vertex2d temp = path.next();
				if (temp == null) {
					moving = false;
					path_move = false;
				} else {
					movement = temp;
				}
			}
		}
	}

	public spritecomponent get_sprite() {
		return core.crew_sprite.getcoords((int) sprite.x, (int) sprite.y,
				(int) sprite.u, (int) sprite.v);
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

}
