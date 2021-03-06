package com.lcass.game.world;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.entity.Collision_package;
import com.lcass.entity.Entity;
import com.lcass.entity.Projectile;
import com.lcass.game.tiles.Tile;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritecomponent;

public class shiphandler {
	public Ship[] world_ships;
	public int[] positions_player;
	public Ship coreship;
	private Core core;
	private Vertex2d campos;
	private VBO v;
	private int curr_ship = 0;

	public shiphandler(Core core, int max_ship) {
		this.core = core;
		world_ships = new Ship[max_ship];
		positions_player = new int[max_ship];
		v = new VBO(core.G.mainvbo);
		v.bind_texture(core.effect_sprite.gettexture());
		v.create(50 * 12);
		coreship = new Ship(64, 64, core, new world(core, 64, 64, -2), this);

	}

	public void set_core_ship(Ship core) {
		coreship = core;
		coreship.set_ship_id(-2);
	}

	public void remove_ship(int pos) {
		if (pos <= world_ships.length) {
			world_ships[pos] = null;
		}
	}

	public int add_ship(Ship ship) {
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] == null) {
				ship.ship.toggle_COM();
				world_ships[i] = ship;
				world_ships[i].set_ship_id(i);
				return i;
			}
		}
		return -1;
	}

	public Ship get_ship(int pos) {
		if(pos == -1){
			return null;
		}
		if (pos <= world_ships.length && pos != -2) {
			return world_ships[pos];
		} else if (pos == -2) {
			return coreship;
		}
		return null;
	}

	public void move_core(boolean leftr, boolean rightr, boolean left,
			boolean right, boolean up, boolean down) {
		if (coreship != null) {
			coreship.move(leftr, rightr, left, right, up, down);
		}
	}

	public boolean check_collision(Ship a, Ship b) {
		if (Math.abs(a.absolute_position.x - b.absolute_position.x) < 2000
				&& Math.abs(a.absolute_position.y - b.absolute_position.y) < 2000) {
			return true;
		}
		return false;
	}

	public boolean check_collision(Projectile a, Ship b) {
		if (Math.abs(a.get_abs().x - b.absolute_position.x) < 2000
				&& Math.abs(a.get_abs().y - b.absolute_position.y) < 2000) {
			return true;
		}
		return false;
	}
	public boolean check_collision(Entity a, Ship b) {
		if (Math.abs(a.get_abs().x - b.absolute_position.x) < 2000
				&& Math.abs(a.get_abs().y - b.absolute_position.y) < 2000) {
			return true;
		}
		return false;
	}

	private int start = 0;

	public void check_ship_collision(Ship a) {
		if (a == null) {
			return;
		}
		for (int i = start; i < world_ships.length; i++) {

			if (a != world_ships[i] && world_ships[i] != null) {
				if (check_collision(a, world_ships[i])) {
					Ship attacking = world_ships[i];
					for (int ax = 0; ax < a.collision.length; ax++) {
						Vertex2d corner = a.collision[ax].whole();
						corner.add2(core.G
								.revert_coordinates(a.absolute_position.xy()));

						for (int at = 0; at < attacking.collision.length; at++) {
							Vertex2d attacker = attacking.collision[at].whole();
							attacker.add2(core.G
									.revert_coordinates(attacking.absolute_position));

							attacker = com.lcass.util.Util.rotate(attacker,
									a.rotpoint, -a.rotation);
							if (((attacker.x > corner.x) && (attacker.x < corner.u))
									|| ((attacker.u > corner.x) && (attacker.u < corner.u))) {
								if (((attacker.y > corner.y) && (attacker.y < corner.v))
										|| ((attacker.v > corner.y) && (attacker.v < corner.v))) {
									corner.sub2(core.G
											.revert_coordinates(a.absolute_position
													.xy()));
									corner.div(32);
									attacker.sub2(core.G
											.revert_coordinates(attacking.absolute_position
													.xy()));
									attacker.div(32);
									a.damage(a.edges[ax].get_pos(), 100);
									attacking.damage(attacker, 100);
								}
							}
						}
					}
				}

			}
		}
		if (coreship != null) {
			if (a != coreship) {
				if (check_collision(a, coreship)) {
					Ship attacking = coreship;
					for (int ax = 0; ax < a.collision.length; ax++) {
						Vertex2d corner = a.collision[ax].whole();
						corner.add2(core.G
								.revert_coordinates(a.absolute_position.xy()));
						corner.sub(new Vertex2d(32, 0, 0, 0));
						for (int at = 0; at < attacking.collision.length; at++) {
							Vertex2d attacker = attacking.collision[at].whole();
							attacker = com.lcass.util.Util.rotate(attacker,
									attacking.rotpoint,
									-attacking.compound_rotation);
							attacker = com.lcass.util.Util.rotate(attacker,
									a.rotpoint, -a.compound_rotation);
							attacker.add2(core.G
									.revert_coordinates(attacking.absolute_position
											.xy()));

							if (((attacker.x >= corner.x) && (attacker.x <= corner.u))
									|| ((attacker.u >= corner.x) && (attacker.u <= corner.u))) {

								if (((attacker.y >= corner.y) && (attacker.y <= corner.v))
										|| ((attacker.v >= corner.y) && (attacker.v <= corner.v))) {
									corner.sub2(core.G
											.revert_coordinates(a.absolute_position
													.xy()));
									corner.div(32);
									attacker.sub2(core.G
											.revert_coordinates(attacking.absolute_position
													.xy()));
									attacker.div(32);
									a.damage(a.edges[ax].get_pos(), 100);
									attacking.damage(
											attacking.edges[at].get_pos(), 100);

								}
							}
						}
					}
				}

			}
		}
		start += 1;
	}

	public void proj_collision_check(Projectile P) {
		for (int i = 0; i < world_ships.length; i++) {
			if(i == P.get_ship()){
				continue;
			}
			if (world_ships[i] != null) {
				
				if (check_collision(P, world_ships[i])) {
					Ship attacking = world_ships[i];

					Vertex2d corner = P.get_abs();
					corner = com.lcass.util.Util.rotate(corner,
							attacking.rotpoint, attacking.rotation);
					for (int at = 0; at < attacking.collision.length; at++) {
						Vertex2d attacker = attacking.collision[at].whole();
						attacker.add2(core.G
								.revert_coordinates(attacking.absolute_position));
						
						if (((corner.x >= attacker.x) && (corner.x <= attacker.u))) {
							
							if (((corner.y >= attacker.y) && (corner.y <= attacker.v))
									) {
								
								attacker.sub2(core.G
										.revert_coordinates(attacking.absolute_position
												.xy()));
								attacker.div(32);
								P.Destroy();
								attacking.damage(attacker, 100);
							}
						}
					}
				}

			}
		}
		if (coreship != null && P.get_ship() != -2) {
			if (check_collision(P, coreship)) {
				Ship attacking = coreship;

				Vertex2d corner = P.get_abs();
				corner = com.lcass.util.Util.rotate(corner,
						attacking.rotpoint, attacking.rotation);
				for (int at = 0; at < attacking.collision.length; at++) {
					Vertex2d attacker = attacking.collision[at].whole();
					attacker.add2(core.G
							.revert_coordinates(attacking.absolute_position));
				
					if (((corner.x >= attacker.x) && (corner.x <= attacker.u))) {
						
						if (((corner.y >= attacker.y) && (corner.y <= attacker.v))
								) {
						
							attacker.sub2(core.G
									.revert_coordinates(attacking.absolute_position
											.xy()));
							attacker.div(32);
							P.Destroy();
							attacking.damage(attacker, 100);
						}
					}
			 }
			

		}
		}
	}
	public Collision_package entity_collision_check(Entity P) {
		Collision_package collisionp = new Collision_package();
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] != null) {
				
				if (check_collision(P, world_ships[i])) {
					Ship attacking = world_ships[i];

					Vertex2d corner = P.get_abs().whole().mult(2);
					corner = com.lcass.util.Util.rotate(corner,
							attacking.rotpoint, attacking.rotation);
					for (int at = 0; at < attacking.collision.length; at++) {
						Vertex2d attacker = attacking.collision[at].whole();
						attacker.add2(core.G
								.revert_coordinates(attacking.absolute_position));
						
						if (((corner.x >= attacker.x) && (corner.x <= attacker.u))) {
							
							if (((corner.y >= attacker.y) && (corner.y <= attacker.v))
									) {
								
								attacker.sub2(core.G
										.revert_coordinates(attacking.absolute_position
												.xy()));
								attacker.div(32);
								
								collisionp.tile = attacking.tile_at(attacker);
								collisionp.s = attacking;
								return collisionp;
							}
						}
					}
				}

			}
		}
		if (coreship != null && P.get_ship() != -2) {
			if (check_collision(P, coreship)) {
				Ship attacking = coreship;

				Vertex2d corner = P.get_abs().whole().mult(2);
				corner = com.lcass.util.Util.rotate(corner,
						attacking.rotpoint, attacking.rotation);
				for (int at = 0; at < attacking.collision.length; at++) {
					Vertex2d attacker = attacking.collision[at].whole();
					attacker.add2(core.G
							.revert_coordinates(attacking.absolute_position));
				
					if (((corner.x >= attacker.x) && (corner.x <= attacker.u))) {
						
						if (((corner.y >= attacker.y) && (corner.y <= attacker.v))
								) {
						
							attacker.sub2(core.G
									.revert_coordinates(attacking.absolute_position
											.xy()));
							attacker.div(32);
							collisionp.tile = attacking.tile_at(attacker);
							collisionp.s = attacking;
								return collisionp;
						}
					}
			 }
			

		}
		}
		return collisionp;
	}

	public void tick() {
		Vertex2d temp_cam = new Vertex2d((coreship.COM.x * 32)  - 16,
				(coreship.COM.y * 32)  - 16);
			
		if (coreship != null) {
			coreship.tick();
			coreship.bind_camera(campos);
			check_ship_collision(coreship);
			coreship.bind_cam_rotation(new Vertex2d(temp_cam.x,temp_cam.y, -coreship.compound_rotation,0));
			//force the coreship to revert all camera rotation , bit hacky but it works and is cleaner than writing some horrid
			//checking code
		}
		start = 0;
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] != null) {
				if (coreship != null) {
					
					world_ships[i].bind_camera(new Vertex2d(
							-(coreship.absolute_position.x - campos.x),
							-(coreship.absolute_position.y - campos.y))
							.add(world_ships[i].absolute_position));
					world_ships[i].bind_cam_rotation(new Vertex2d(coreship.correct_pos.x,coreship.correct_pos.y,-coreship.compound_rotation,0).add(temp_cam));
				}
				world_ships[i].tick();
				check_ship_collision(world_ships[i]);
			}
		}

	}

	public void render() {
		if (coreship != null) {
			coreship.render();
		}
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] != null) {
				world_ships[i].render();
			}
		}
		v.render();
	}

	public void cleanup_player() {
		if (coreship != null) {
			coreship.cleanup();
		}

	}

	private Tile[] ships_tiles;
	private ArrayList<ArrayList<Tile>> tiles_list = new ArrayList<ArrayList<Tile>>();

	public void generate_ships(Tile[] tiles, int width) {
		cleanup_player();
		int start_curr = curr_ship;
		ships_tiles = tiles;
		int highest_weight = 0;// curr_ship
		int last_weight = 0;
		boolean locating = true;
		boolean source_found = false;
		boolean tile_updated = false;
		tiles_list = new ArrayList<ArrayList<Tile>>();

		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] != null) {
				tiles[i].set_ship(0);
			}
		}
		while (locating) {
			tile_updated = false;
			source_found = false;
			int weight = 0;
			for (int i = 0; i < ships_tiles.length; i++) {
				if (ships_tiles[i] != null) {
					if (ships_tiles[i].get_ship() == 0) {
						int id = check_tile(i, width);

						if (id != 0) {
							tile_updated = true;
							ships_tiles[i].set_ship((curr_ship - start_curr));
							weight += 1;
							infect(i, width, id, ((curr_ship - start_curr)) - 1);
						}
					}
				}
			}
			if (!tile_updated) {
				for (int i = 0; i < ships_tiles.length; i++) {
					if (ships_tiles[i] != null) {
						if (ships_tiles[i].get_ship() == 0) {
							curr_ship += 1;
							ships_tiles[i].set_ship(curr_ship);
							tiles_list.add(new ArrayList<Tile>());
							weight += 1;

							source_found = true;
							break;

						}
					}
				}
				if (!source_found) {

					locating = false;
				}
			}
			if (weight > last_weight) {
				last_weight = weight;
				highest_weight = curr_ship;
			}
		}
		;
		for (int i = start_curr; i < curr_ship; i++) {// ONLY ADD THE BIGGEST
														// SHIP!
			if (i + 1 == highest_weight) {

				Object[] temp = tiles_list.get((i) - start_curr).toArray();
				Tile[] data = com.lcass.util.Util.cast_tile(temp);
				set_core_ship(new Ship(64, 64, core, new world(core, 64, 64, data,
						-2), this));
				
				coreship.regen_world();
				break;
			}
		}

	}

	public void infect(int index, int width, int ship, int curr_ship) {
		int y = (int) Math.floor(index / width);
		int height = ships_tiles.length / width;
		int x = index - (y * width);
		for (int ix = x; ix < width; ix++) {
			if (ships_tiles[ix + (y * width)] == null) {
				break;
			}
			ships_tiles[ix + (y * width)].set_ship(ship);
			tiles_list.get(curr_ship).add(ships_tiles[ix + (y * width)]);
		}
		for (int ix = x; ix > 0; ix--) {
			if (ships_tiles[ix + (y * width)] == null) {
				break;
			}
			ships_tiles[ix + (y * width)].set_ship(ship);
			tiles_list.get(curr_ship).add(ships_tiles[ix + (y * width)]);
		}
		for (int iy = y; iy < height; iy++) {
			if (ships_tiles[x + (iy * width)] == null) {
				break;
			}
			ships_tiles[x + (iy * width)].set_ship(ship);
			tiles_list.get(curr_ship).add(ships_tiles[x + (iy * width)]);
		}
		for (int iy = y; iy > 0; iy--) {
			if (ships_tiles[x + (iy * width)] == null) {
				break;
			}
			ships_tiles[x + (iy * width)].set_ship(ship);
			tiles_list.get(curr_ship).add(ships_tiles[x + (iy * width)]);
		}
	}

	public int check_tile(int index, int width) {
		if (ships_tiles.length > index) {
			if (ships_tiles[index + 1] != null) {
				if (ships_tiles[index + 1].get_ship() != 0) {
					return ships_tiles[index + 1].get_ship();
				}
			}
		}
		if (index > 0) {
			if (ships_tiles[index - 1] != null) {
				if (ships_tiles[index - 1].get_ship() != 0) {
					return ships_tiles[index - 1].get_ship();
				}
			}
		}

		if (index + width <= ships_tiles.length) {
			if (ships_tiles[index + width] != null) {
				if (ships_tiles[index + width].get_ship() != 0) {
					return ships_tiles[index + width].get_ship();
				}
			}
		}
		if (index - width >= 0) {
			if (ships_tiles[index - width] != null) {
				if (ships_tiles[index - width].get_ship() != 0) {
					return ships_tiles[index - width].get_ship();
				}
			}

		}
		return 0;
	}

	public void bind_camera(Vertex2d camera) {
		campos = core.G.convert_coordinates(camera);
	}

	public void cleanup() {
		if (coreship != null) {
			coreship.cleanup();
		}
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] != null) {
				world_ships[i].cleanup();
			}
		}
	}

	public Vertex2d get_ship_at(Vertex2d position) {
		Vertex2d adjusted_pos = position.whole();
		if (coreship != null) {
			Tile[] map = coreship.map;
			for (int j = 0; j < map.length; j++) {
				if (map[j] != null) {
					Vertex2d adjusted = map[j].get_pos().whole()
							.add(coreship.correct_pos);

					if (adjusted_pos.x == adjusted.x
							&& adjusted_pos.y == adjusted.y) {

						return new Vertex2d(-2, j);
					}
				}
			}
		}
		for (int i = 0; i < world_ships.length; i++) {

			if (world_ships[i] != null) {
				Tile[] map = world_ships[i].map;
				for (int j = 0; j < map.length; j++) {
					if (map[j] != null) {
						Vertex2d adjusted = map[j].get_pos().whole()
								.add(world_ships[i].correct_pos).div(32)
								.to_int();
						if (adjusted_pos.x == adjusted.x
								&& adjusted_pos.y == adjusted.y) {
							return new Vertex2d(i, j);
						}
					}
				}
			}
		}
		return null;
	}
	public Vertex2d get_cam_rotation(){
		return new Vertex2d((coreship.COM.x * 32)  - 16,
				(coreship.COM.y * 32)  - 16,coreship.compound_rotation,0);
	}
}
