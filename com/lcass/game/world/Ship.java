package com.lcass.game.world;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.entity.CrewHandler;
import com.lcass.entity.Human;
import com.lcass.game.tiles.Tile;
import com.lcass.game.world.resources.Resourcehandler;
import com.lcass.graphics.Effect_builder;
import com.lcass.graphics.Particles;
import com.lcass.graphics.TextGenerator;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Path;

public class Ship {
	public world ship;
	private Core core;
	public int width, height;
	public float mass;

	private float velocity_vert = 0, velocity_horiz = 0;
	private float powerright = 0, powerleft = 0, powerup = 0, powerdown = 0;
	private boolean is_ai = false;
	private int[] id_up, id_down, id_left, id_right;
	private boolean last_left = false, last_right = false, last_up = false,
			last_down = false;
	private Vertex2d forwardstep, upstep, backstep, downstep;
	private VBO position_string;
	public Tile[] map, edges;
	public Vertex2d[] collision;
	private shiphandler sh;
	public float compound_rotation = 0;
	private TextGenerator textgen;
	private float rotation_offset = 0;
	private int array_position = 0;
	private boolean effects_generated = false, final_move = false;
	private Effect_builder effects;
	private boolean particles_generated = false;
	private Particles particles;
	private Resourcehandler resources;
	private CrewHandler crew_handler;
	public Vertex2d absolute_position, position, BackCOM, ForeCOM, UpCOM,
			DownCOM, COM, camera, forwardthrust, upthrust, downthrust,// position
																		// is a
																		// progressive
																		// vector
																		// where
																		// a
																		// tick
																		// function
																		// moves
																		// the
																		// ship
			backthrust;// for thrust x and y is the position and u is the
						// force//position.u is rotation

	public Ship(int width, int height, Core core, world w, shiphandler sh) {
		this.core = core;
		this.width = width;
		this.height = height;
		position = new Vertex2d(0, 0, 0, 0);
		absolute_position = new Vertex2d(0, 0, 0, 0);
		camera = new Vertex2d(0, 0, 0, 0);
		// define direction values
		forwardthrust = new Vertex2d(0, 0, 0, 0);
		upthrust = new Vertex2d(0, 0, 0, 0);
		downthrust = new Vertex2d(0, 0, 0, 0);
		backthrust = new Vertex2d(0, 0, 0, 0);
		effects = new Effect_builder(core, 10000);
		effects.bind_texture(core.effect_sprite.gettexture());
		particles = new Particles(core, 1000);
		ship = w;
		map = w.get_map();
		calculate_masscentre();
		calculate_COT();
		calculate_steps();
		textgen = new TextGenerator(core);
		position_string = new VBO(core.G.mainvbo);
		position_string.create(12 * 32);
		position_string.edit_data(textgen.generate_text("Position "
				+ position.x + " " + position.y, new Vertex2d(0, 0), 12));
		position_string.bind_texture(textgen.gettexture());
		this.sh = sh;
		generate_edges();
		resources = new Resourcehandler(this);
		crew_handler = new CrewHandler(core,this);
		crew_handler.add_crew(new Human(core,crew_handler));
		Path temp = new Path();
		temp.add_step(new Vertex2d(32,0));
		temp.add_step(new Vertex2d(0,32));
		temp.add_step(new Vertex2d(32,0));
		temp.add_step(new Vertex2d(32,0));
		temp.add_step(new Vertex2d(32,0));
		temp.add_step(new Vertex2d(0,32));
		temp.add_step(new Vertex2d(0,32));
		temp.add_step(new Vertex2d(0,32));
		temp.add_step(new Vertex2d(0,32));
		temp.add_step(new Vertex2d(32,0));
		temp.add_step(new Vertex2d(32,0));
		temp.add_step(new Vertex2d(32,0));
		crew_handler.get_crew(0).set_path(temp);
	}

	public void damage(Vertex2d position, int damage) {
		int coordinate = (int) (position.x + (ship.mapwidth * position.y));

		if (map[coordinate] != null) {

			if (map[coordinate].damage(damage)) {
				remove_tile(position);
				update();

			}
		}
		generate_edges();
	}

	public void calculate_steps() {

		if (forwardthrust.x > 0 || forwardthrust.y > 0) {
			forwardstep = new Vertex2d(powerright / mass,
					(COM.y - forwardthrust.y) / mass, (COM.x - forwardthrust.x)
							/ mass, 0);

			forwardthrust.u = (float) ((Math.atan(1 / forwardstep.y)));

			if (forwardthrust.u > 0) {
				forwardthrust.u = (float) (Math.toRadians(90) - forwardthrust.u)
						* forwardstep.x;

			} else {
				forwardthrust.u = (float) (Math.toRadians(-90) - forwardthrust.u)
						* forwardstep.x;

			}

			forwardthrust.u = core.G.make_accurate(forwardthrust.u);
		} else {
			forwardstep = new Vertex2d(0, 0);

		}
		if (backthrust.x > 0 || backthrust.y > 0) {// Ill do this later ugh
			backstep = new Vertex2d(powerleft / mass, (COM.y - backthrust.y)
					/ mass, (COM.x - backthrust.x) / mass, 0);

			backthrust.u = (float) ((Math.atan(1 / backstep.y)));

			if (backthrust.u > 0) {
				backthrust.u = (float) -(Math.toRadians(90) - backthrust.u)
						* backstep.x;

			} else {
				backthrust.u = (float) -(Math.toRadians(-90) - backthrust.u)
						* backstep.x;

			}

			backthrust.u = core.G.make_accurate(backthrust.u);

		} else {
			backstep = new Vertex2d(0, 0);
		}
		if (upthrust.x > 0 || upthrust.y > 0) {
			upstep = new Vertex2d(powerup / mass, (COM.y - upthrust.y) / mass,
					(COM.x - upthrust.x) / mass, 0);

			upthrust.u = (float) ((Math.atan(1 / upstep.u)));

			if (upthrust.u > 0) {
				upthrust.u = (float) -(Math.toRadians(90) - upthrust.u)
						* upstep.x;

			} else {
				upthrust.u = (float) -(Math.toRadians(-90) - upthrust.u)
						* upstep.x;

			}

			upthrust.u = core.G.make_accurate(upthrust.u);
		} else {
			upstep = new Vertex2d(0, 0);
		}
		if (downthrust.x > 0 || downthrust.y > 0) {
			downstep = new Vertex2d(powerdown / mass, (COM.y - downthrust.y)
					/ mass, (COM.x - downthrust.x) / mass, 0);

			downthrust.u = (float) ((Math.atan(1 / downstep.u)));

			if (downthrust.u > 0) {
				downthrust.u = (float) (Math.toRadians(90) - downthrust.u)
						* downstep.x;

			} else {
				downthrust.u = (float) (Math.toRadians(-90) - downthrust.u)
						* downstep.x;
			}
			downthrust.u = core.G.make_accurate(downthrust.u);

		} else {
			downstep = new Vertex2d(0, 0);
		}

	}

	public void calculate_COT() {
		Tile[] temp = map;
		int id_up_len = 0, id_down_len = 0, id_right_len = 0, id_left_len = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null) {
				if (temp[i].get_active()) {
					if (temp[i].get_name() == "thruster") {
						switch (temp[i].get_dir()) {
						case 0:
							id_right_len += 1;
							break;
						case 1:
							id_left_len += 1;
							break;
						case 2:
							id_up_len += 1;
							break;
						case 3:
							id_down_len += 1;
							break;
						}
					}
				}
			}
		}
		particle_thrust_up = new int[id_up_len];
		particle_thrust_down = new int[id_down_len];
		particle_thrust_right = new int[id_right_len];
		particle_thrust_left = new int[id_left_len];
		id_up = new int[id_up_len];
		id_down = new int[id_down_len];
		id_right = new int[id_right_len];
		id_left = new int[id_left_len];
		id_up_len = 0;
		id_down_len = 0;
		id_right_len = 0;
		id_left_len = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != null) {
				if (temp[i].get_active()) {
					if (temp[i].get_name() == "thruster") {
						switch (temp[i].get_dir()) {
						case 0:
							id_right[id_right_len] = i;
							id_right_len += 1;
							break;
						case 1:
							id_left[id_left_len] = i;
							id_left_len += 1;
							break;
						case 2:
							id_up[id_up_len] = i;
							id_up_len += 1;
							break;
						case 3:
							id_down[id_down_len] = i;
							id_down_len += 1;
							break;
						}
					}
				}
			}
		}
		for (int curdir = 0; curdir < 4; curdir++) {
			float xsum = 0;
			float ysum = 0;
			float total = 0;
			if (curdir == 0) {
				powerright = 0;
			} else if (curdir == 1) {
				powerleft = 0;
			} else if (curdir == 2) {
				powerup = 0;
			} else if (curdir == 3) {
				powerdown = 0;
			}
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] == null) {
					continue;
				}
				if (temp[i].get_active()) {
					if (temp[i].get_name() == "thruster"
							&& temp[i].get_dir() == curdir) {
						total += temp[i].get_thrust();
						xsum += temp[i].get_pos().x * temp[i].get_thrust();
						ysum += temp[i].get_pos().y * temp[i].get_thrust();

						if (curdir == 0) {
							powerright += temp[i].get_thrust();
						} else if (curdir == 1) {
							powerleft += temp[i].get_thrust();
						} else if (curdir == 2) {
							powerup += temp[i].get_thrust();
						} else if (curdir == 3) {
							powerdown += temp[i].get_thrust();
						}
					}
				}
			}

			if (total > 0) {
				if (curdir == 0) {
					forwardthrust = new Vertex2d((xsum / total) + 1,
							(ysum / total) + 1);

				} else if (curdir == 1) {
					backthrust = new Vertex2d((xsum / total) + 1,
							(ysum / total) + 1);
				} else if (curdir == 2) {
					upthrust = new Vertex2d((xsum / total) + 1,
							(ysum / total) + 1);
				} else if (curdir == 3) {
					downthrust = new Vertex2d((xsum / total) + 1,
							(ysum / total) + 1);
				}
			} else {
				if (curdir == 0) {
					forwardthrust = new Vertex2d(0, 0);
				} else if (curdir == 1) {
					backthrust = new Vertex2d(0, 0);
				} else if (curdir == 2) {
					upthrust = new Vertex2d(0, 0);
				} else if (curdir == 3) {
					downthrust = new Vertex2d(0, 0);
				}
			}
		}

	}

	public void set_position(Vertex2d pos) {
		absolute_position = pos;

	}

	Vertex2d rotated = new Vertex2d(0, 0, 0, 0);
	private float gyro_force = 0;

	public void calculate_gyro() {
		gyro_force = 0;
		for (Tile t : map) {
			if (t != null) {
				if (t.get_name() == "gyro") {

					gyro_force += (com.lcass.core.DEFINES.DEFAULT_GYRO_POWER / mass) / 10;

				}
			}
		}
	}

	public void move(boolean leftr, boolean rightr, boolean left,
			boolean right, boolean up, boolean down) {
		handle_particles(right, left, up, down);
		if (leftr) {
			rotation_offset -= gyro_force;
		}
		if (rightr) {
			rotation_offset += gyro_force;
		}
		if (right != last_right) {
			last_right = right;
			effects_generated = false;

		}
		if (left != last_left) {
			last_left = left;
			effects_generated = false;
		}
		if (up != last_up) {
			last_up = up;
			effects_generated = false;
		}
		if (down != last_down) {
			last_down = down;
			effects_generated = false;
		}
		if (right) {
			rotated = com.lcass.util.Util.rotate(COM, ForeCOM,
					compound_rotation);
			velocity_horiz += forwardstep.x
					* (Math.abs(rotated.x - ForeCOM.x))
					* Math.cos(compound_rotation);// current velocity
			velocity_vert += forwardstep.x
					* (-Math.abs(rotated.y - ForeCOM.y))
					* Math.sin(compound_rotation);
			Vertex2d temp = core.G.central_product(COM, forwardthrust);
			position.u += forwardthrust.u * forwardstep.x * temp.y * temp.x;// multiply
																			// by
																			// the
																			// power
																			// that
																			// the
																			// product
																			// outputs

		}

		if (left) {
			rotated = com.lcass.util.Util.rotate(COM, BackCOM,
					compound_rotation);
			velocity_horiz += backstep.x
					* (-Math.abs(rotated.x - BackCOM.x) )
					* Math.cos(compound_rotation);// current velocity
			velocity_vert += backstep.x
					* ((Math.abs(rotated.y - BackCOM.y) ))
					* Math.sin(compound_rotation);// its not x and y they are
													// just the
			// positions in the vector
			Vertex2d temp = core.G.central_product(COM, backthrust);//
			position.u += backthrust.u * backstep.x * temp.y * temp.x;// multiply
																		// by
																		// the
																		// power
																		// that
																		// the
																		// product
																		// outputs

		}
		if (up) {
			rotated = com.lcass.util.Util.rotate(COM, UpCOM, compound_rotation);
			velocity_horiz += upstep.x * (Math.abs(rotated.x - UpCOM.x))
					* Math.sin(compound_rotation);// current velocity
			velocity_vert += upstep.x * (Math.abs(rotated.y - UpCOM.y))
					* Math.cos(compound_rotation);
			Vertex2d temp = core.G.central_product(COM, upthrust);
			position.u += upthrust.u * upstep.x * temp.y * temp.x;// multiply by
																	// the power
																	// that the
																	// product
																	// outputs

		}
		if (down) {
			rotated = com.lcass.util.Util.rotate(COM, DownCOM,
					compound_rotation);
			velocity_horiz += downstep.x
					* (-Math.abs(rotated.x - DownCOM.x))
					* Math.sin(compound_rotation);// current velocity
			velocity_vert += downstep.x
					* (-Math.abs(rotated.y - DownCOM.y))
					* Math.cos(compound_rotation);
			Vertex2d temp = core.G.central_product(COM, downthrust);
			position.u += downthrust.u * downstep.x * temp.y * temp.x;// multiply
																		// by
																		// the
																		// power
																		// that
																		// the
																		// product
																		// outputs

		}
		if (right || left || up || down) {

			position.x = velocity_horiz;
			position.y = velocity_vert;
			final_move = false;
		} else if (!final_move) {
			final_move = true;
			effects_generated = false;
		}
		if (!effects_generated) {
			effects_generated = true;
			generate_effects(right, left, up, down);
		}

	}

	private int[] particle_thrust_left, particle_thrust_right,
			particle_thrust_up, particle_thrust_down;

	public void generate_effects(boolean right, boolean left, boolean up,
			boolean down) {

		Tile[] temp = map;
		effects.clear();
		if (!particles_generated) {
			particles_generated = true;
			particles.clear();// cleanup
			for (int i = 0; i < id_right.length; i++) {
				Tile temp_tile = temp[id_right[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)),
						new Vertex2d((((COM.x) * 32) - camera.x * 2),
								((COM.y * 32) - camera.y * 2)),
						-compound_rotation);
				particle_thrust_right[i] = particles.create_spawn(new Vertex2d(
						(rotated.x - core.width / 2), rotated.y - core.height
								/ 2), core.particle_sprite
						.getcoords(0, 0, 0, 0), 200, 1000, 2, 50, 50, 80, 0, 1);
				particles.set_creating(false, particle_thrust_right[i]);
			}
			for (int i = 0; i < id_left.length; i++) {
				Tile temp_tile = temp[id_left[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)),
						new Vertex2d((((COM.x) * 32) - camera.x * 2),
								((COM.y * 32) - camera.y * 2)),
						-compound_rotation);
				particle_thrust_left[i] = particles.create_spawn(new Vertex2d(
						(rotated.x - core.width / 2), rotated.y - core.height
								/ 2), core.particle_sprite
						.getcoords(0, 0, 0, 0), 200, 1000, 2, 5, 50, -80, 0, 1);
				particles.set_creating(false, particle_thrust_left[i]);
			}
			for (int i = 0; i < id_up.length; i++) {
				Tile temp_tile = temp[id_up[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)),
						new Vertex2d((((COM.x) * 32) - camera.x * 2),
								((COM.y * 32) - camera.y * 2)),
						-compound_rotation);
				particle_thrust_up[i] = particles.create_spawn(new Vertex2d(
						(rotated.x - core.width / 2), rotated.y - core.height
								/ 2), core.particle_sprite
						.getcoords(0, 0, 0, 0), 200, 1000, 2, 5, 50, 0, 80, 1);
				particles.set_creating(false, particle_thrust_up[i]);
			}
			for (int i = 0; i < id_down.length; i++) {
				Tile temp_tile = temp[id_down[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)),
						new Vertex2d((((COM.x) * 32) - camera.x * 2),
								((COM.y * 32) - camera.y * 2)),
						-compound_rotation);
				particle_thrust_down[i] = particles.create_spawn(new Vertex2d(
						(rotated.x - core.width / 2), rotated.y - core.height
								/ 2), core.particle_sprite
						.getcoords(0, 0, 0, 0), 200, 1000, 2, 5, 50, 0, -80, 1);
				particles.set_creating(false, particle_thrust_down[i]);
			}
		}

		if (right) {
			for (int i = 0; i < id_right.length; i++) {
				Tile temp_tile = temp[id_right[i]];

				particles.set_creating(true, particle_thrust_right[i]);

				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 31,
						((int) temp_tile.get_pos().y * 32) + 1, 63, 31,
						core.effect_sprite.getcoords(0, 80, 31, 95)), 1, 4);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 31,
						((int) temp_tile.get_pos().y * 32) + 1, 63, 31,
						core.effect_sprite.getcoords(0, 64, 31, 79)), 5, 4);
			}
		} else {

			for (int i = 0; i < id_right.length; i++) {
				Tile temp_tile = temp[id_right[i]];

				particles.set_creating(false, particle_thrust_right[i]);

				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 31,
						((int) temp_tile.get_pos().y * 32) + 1, 63, 31,
						core.effect_sprite.getcoords(0, 80, 31, 95)), 1, 9);
			}
		}
		if (up) {
			for (int i = 0; i < id_up.length; i++) {

				Tile temp_tile = temp[id_up[i]];

				particles.set_creating(true, particle_thrust_up[i]);

				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32),
						((int) temp_tile.get_pos().y * 32) - 31, 31, 63,
						core.effect_sprite.getcoords(16, 32, 32, 64)), 1, 4);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32),
						((int) temp_tile.get_pos().y * 32) - 31, 31, 63,
						core.effect_sprite.getcoords(0, 32, 16, 64)), 5, 4);
			}
		} else {
			for (int i = 0; i < id_up.length; i++) {
				Tile temp_tile = temp[id_up[i]];
				particles.set_creating(false, particle_thrust_up[i]);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32),
						((int) temp_tile.get_pos().y * 32) - 31, 31, 63,
						core.effect_sprite.getcoords(16, 32, 32, 64)), 1, 9);
			}
		}
		if (down) {
			for (int i = 0; i < id_down.length; i++) {
				Tile temp_tile = temp[id_down[i]];

				particles.set_creating(true, particle_thrust_down[i]);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 1,
						((int) temp_tile.get_pos().y * 32) + 1, 31, 63,
						core.effect_sprite.getcoords(0, 96, 15, 127)), 1, 4);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 1,
						((int) temp_tile.get_pos().y * 32) + 1, 31, 63,
						core.effect_sprite.getcoords(16, 96, 31, 127)), 5, 4);
			}
		} else {
			for (int i = 0; i < id_down.length; i++) {
				Tile temp_tile = temp[id_down[i]];
				particles.set_creating(false, particle_thrust_down[i]);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 1,
						((int) temp_tile.get_pos().y * 32) + 1, 31, 63,
						core.effect_sprite.getcoords(0, 96, 15, 127)), 1, 9);
			}
		}
		if (left) {
			for (int i = 0; i < id_left.length; i++) {
				Tile temp_tile = temp[id_left[i]];

				particles.set_creating(true, particle_thrust_left[i]);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 1,
						((int) temp_tile.get_pos().y * 32) + 1, 63, 31,
						core.effect_sprite.getcoords(0, 0, 31, 15)), 1, 4);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 1,
						((int) temp_tile.get_pos().y * 32) + 1, 63, 31,
						core.effect_sprite.getcoords(0, 16, 31, 31)), 5, 4);
			}
		} else {
			for (int i = 0; i < id_left.length; i++) {
				Tile temp_tile = temp[id_left[i]];
				particles.set_creating(false, particle_thrust_left[i]);
				effects.create_effect(core.G.rectangle(
						(((int) temp_tile.get_pos().x) * 32) - 1,
						((int) temp_tile.get_pos().y * 32) + 1, 63, 31,
						core.effect_sprite.getcoords(0, 0, 31, 15)), 1, 9);
			}
		}

	}

	public void render() {
		
		ship.render();
		position_string.render();
		effects.render();
		particles.render();
		crew_handler.render();
		

	}

	public float rotation = 0;

	public void reset_position() {

		absolute_position = new Vertex2d(0, 0);
		position = new Vertex2d(0, 0);
		rotated = new Vertex2d(0, 0, 0, 0);
		velocity_vert = 0;
		velocity_horiz = 0;
		rotation = 0;
		rotation_offset = 0;
	}

	private boolean regenerate_particles = true;

	public void handle_particles(boolean right, boolean left, boolean up,
			boolean down) {
		if (!particles_generated) {
			return;
		}
		float cos = (float) Math.cos(compound_rotation);
		float sin = (float) Math.sin(compound_rotation);
		if (regenerate_particles) {
			particles.reset_spawn_data();
		}
		if (right) {
			for (int i = 0; i < id_right.length; i++) {
				Tile temp_tile = map[id_right[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)), rotpoint,
						-compound_rotation);
				particles.set_spawn(new Vertex2d((rotated.x - core.width / 2),
						rotated.y - core.height / 2), particle_thrust_right[i]);
				particles.set_offset(new Vertex2d(80 * cos, (-80) * sin),
						particle_thrust_right[i]);
			}
		}
		if (left) {
			for (int i = 0; i < id_left.length; i++) {
				Tile temp_tile = map[id_left[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)), rotpoint,
						-compound_rotation);
				particles.set_spawn(new Vertex2d((rotated.x - core.width / 2),
						rotated.y - core.height / 2), particle_thrust_left[i]);
				particles.set_offset(new Vertex2d((-80) * cos, 80 * sin),
						particle_thrust_left[i]);
			}
		}
		if (up) {
			for (int i = 0; i < id_up.length; i++) {
				Tile temp_tile = map[id_up[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)), rotpoint,
						-compound_rotation);
				particles.set_spawn(new Vertex2d((rotated.x - core.width / 2),
						rotated.y - core.height / 2), particle_thrust_up[i]);
				particles.set_offset(new Vertex2d(80 * sin, 80 * cos),
						particle_thrust_up[i]);
			}
		}
		if (down) {
			for (int i = 0; i < id_down.length; i++) {
				Tile temp_tile = map[id_down[i]];
				Vertex2d rotated = com.lcass.util.Util.rotate(
						new Vertex2d((((int) temp_tile.get_pos().x) * 32),
								((int) temp_tile.get_pos().y * 32)), rotpoint,
						-compound_rotation);
				particles.set_spawn(new Vertex2d((rotated.x - core.width / 2),
						rotated.y - core.height / 2), particle_thrust_down[i]);
				particles.set_offset(new Vertex2d(80 * sin, (-80) * cos),
						particle_thrust_down[i]);
			}
		}
		if ((right || left || up || down) && regenerate_particles) {
			regenerate_particles = false;
		}
		if (!(right || left || up || down) && !regenerate_particles) {

			regenerate_particles = true;
		}

	}

	public Vertex2d rotpoint = new Vertex2d(0, 0, 0, 0);
	public Vertex2d correct_pos = new Vertex2d(0,0,0,0);
	public void tick() {
		
		
		
		resources.tick();
		if (ship.hasdata) {
			update_network();
		}
		rotpoint = new Vertex2d(((COM.x * 32) - camera.x * 2) - 16,
				((COM.y * 32) - camera.y * 2) - 16);
		if (!position_manual && particles_generated) {
			particles.tick();
			particles.translate(new Vertex2d(camera.x, camera.y));
		}
		Vertex2d adjusted = core.G.convert_coordinates(position);
		absolute_position.x += adjusted.x;
		absolute_position.y += adjusted.y;
		correct_pos.x += position.x;
		correct_pos.y += position.y;
		crew_handler.set_last(correct_pos);
		crew_handler.tick();
		if (!position_manual) {
			effects.transform(camera);
		}
		effects.rotate(compound_rotation);

		ship.rotate(compound_rotation);
		ship.set_rot_pos(rotpoint);

		effects.set_rot_pos(rotpoint);

		rotation += -(position.u /100);
		compound_rotation = rotation + rotation_offset;

		if (is_ai) {

			// do ai stuff like swim and buy shoes
		}
		if (!position_manual) {
			ship.update_camera(camera);
			ship.tick();
			effects.tick();
		}

	}

	private boolean position_manual = false;

	public void update_pos() {
		position_manual = true;
		Vertex2d campos = core.G.convert_coordinates((int)(-camera.x + absolute_position.x),(int)(
				-camera.y + absolute_position.y));
		particles.translate(campos);
		effects.transform(campos);
		particles.tick();
		ship.update_camera(core.G.convert_coordinates(campos));
		ship.tick();
		effects.tick();
	}

	public void generate_edges() {
		ArrayList<Tile> edge = new ArrayList<Tile>();
		for (int i = 0; i < map.length; i++) {
			if (map[i] != null) {
				if (check_edge(i)) {
					edge.add(map[i]);
				}
			}
		}
		edges = com.lcass.util.Util.cast_tile(edge.toArray());
		collision = new Vertex2d[edges.length];
		for (int i = 0; i < edges.length; i++) {
			Vertex2d temp = edges[i].get_pos().xy();
			temp.mult(32);
			collision[i] = new Vertex2d(temp.x, temp.y, temp.x + 32,
					temp.y + 32);
		}
	}

	public boolean check_edge(int pos) {
		int up = pos - ship.mapwidth;
		int down = pos + ship.mapwidth;
		int left = pos - 1;
		int right = pos + 1;
		if (up >= 0) {
			if (map[up] == null) {
				return true;
			}
		} else {
			return true;
		}
		if (down < ship.get_map().length) {
			if (map[down] == null) {
				return true;
			}
		} else {
			return true;
		}
		if (left >= 0) {
			if (map[left] == null) {
				return true;
			}
		} else {
			return true;
		}
		if (right < ship.get_map().length) {
			if (map[right] == null) {
				return true;
			}
		} else {
			return true;
		}
		return false;

	}

	public void bind_camera(Vertex2d position) {
		camera = position;
	}

	public void add_tile(Tile t) {
		ship.add_tile(t);
		generate_edges();
		effects_generated = false;
		particles_generated = false;
		calculate_COT();
		calculate_masscentre();
		calculate_steps();
		generate_edges();
	}

	public void remove_tile(Vertex2d position) {
		ship.remove_tile(position);
		effects_generated = false;
		particles_generated = false;
		calculate_COT();
		calculate_masscentre();
		calculate_steps();
		generate_edges();
		reset_particles();

	}

	public void calculate_masscentre() {

		if (ship != null) {

			Tile[] temp_map = ship.get_map();
			float xsum = 0;
			mass = 0;
			float ysum = 0;
			float total = 0;
			for (int i = 0; i < temp_map.length; i++) {
				if (temp_map[i] != null) {

					Vertex2d temp_pos = temp_map[i].get_pos();
					float temp_mass = temp_map[i].get_mass();
					total += temp_mass;

					xsum += temp_pos.x * temp_mass;
					ysum += temp_pos.y * temp_mass;
					this.mass += temp_mass;

				}
			}
			if (total > 0) {
				COM = new Vertex2d((xsum / total) + 1, (ysum / total) + 1);// adjust
																			// to
																			// 0,0
																			// coordinates
																			// with
																			// -1

			} else {
				COM = new Vertex2d(0, 0, 0, 0);

			}
			ForeCOM = new Vertex2d(COM.x - 1, COM.y);
			BackCOM = new Vertex2d(COM.x + 1, COM.y);
			UpCOM = new Vertex2d(COM.x, COM.y - 1);
			DownCOM = new Vertex2d(COM.x, COM.y + 1);
			calculate_gyro();
		}

	}

	public void set_world(Tile[] t) {

		map = t;
		ship.set_data(t);
		generate_edges();
		update();
		calculate_masscentre();
		calculate_COT();
	}

	public void set_array_position(int pos) {
		array_position = pos;
	}

	public int get_array_position() {
		return array_position;
	}

	public void set_world(world w) {
		ship.set_world(w);
	}

	public void update() {
		calculate_masscentre();
		calculate_COT();
		calculate_steps();
		update_network();
		particles_generated = false;
		effects_generated = false;

	}
	public void update_movement(){
		calculate_masscentre();
		calculate_COT();
		calculate_steps();
		particles_generated = false;
		effects_generated = false;
	}

	public void toggle_ai() {
		is_ai = !is_ai;
	}

	public void cleanup() {
		ship.cleanup();
	}

	public void reset_particles() {
		particles.clear();
		effects.clear();
	}

	public Vertex2d cable_pos(int pos) {

		Vertex2d poses = new Vertex2d(-1, -1, -1, -1);

		if (pos > 0) {
			if (map[pos - 1] != null) {
				if (map[pos - 1].is_electric()) {
					poses.set_x(pos - 1);
				}
			}
		}
		if (pos < map.length) {
			if (map[pos + 1] != null) {
				if (map[pos + 1].is_electric()) {
					poses.set_y(pos + 1);
				}
			}
		}
		if (pos > ship.mapwidth) {
			if (map[pos - ship.mapwidth] != null) {
				if (map[pos - ship.mapwidth].is_electric()) {
					poses.set_u(pos - ship.mapwidth);
				}
			}
		}
		if (pos < map.length - ship.mapwidth) {
			if (map[pos + ship.mapwidth] != null) {
				if (map[pos + ship.mapwidth].is_electric()) {
					poses.set_v(pos + ship.mapwidth);
				}
			}
		}
		return poses;
	}

	public int[] electronics;

	public void calculate_electronics() {
		ArrayList<Integer> checked = new ArrayList<Integer>();
		for (int i = 0; i < map.length; i++) {

			if (map[i] != null) {

				if (map[i].is_electric()) {

					checked.add(i);

				}
			}
		}
		electronics = new int[checked.size()];
		for (int i = 0; i < electronics.length; i++) {
			electronics[i] = checked.get(i);
		}
	}

	public void update_network() {
		calculate_electronics();
		resources.calculate_resources();

	}

	public void increase_voltage() {

		resources.increase_voltage(10);
	}

	public void decrease_voltage() {
		resources.decrease_voltage(10);
	}

	public float get_voltage() {
		return resources.get_voltage();
	}

	public float get_resistance() {
		return resources.get_resistance();
	}

	public float get_power() {
		return resources.get_power();
	}
	public Tile get_tile(int position){
		if(position >= 0){
			if(position < map.length){
				return map[position];
			}
		}
		return null;
	}

}
