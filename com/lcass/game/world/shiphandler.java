package com.lcass.game.world;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.game.tiles.Tile;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Progressive_buffer;

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
		
	}

	public void set_core_ship(Ship core) {
		coreship = core;
	}

	public void remove_ship(int pos) {
		if (pos <= world_ships.length) {
			world_ships[pos] = null;
		}
	}

	public int add_ship(Ship ship) {
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] == null) {
				world_ships[i] = ship;
				return i;
			}
		}
		return -1;
	}

	public Ship get_ship(int pos) {
		if (pos <= world_ships.length) {
			return world_ships[pos];
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
	private int start = 0;
	public void check_ship_collision(Ship a) {
		if (a == null) {
			return;
		}
		for (int i = start; i < world_ships.length; i++) {

			if (a != world_ships[i] && world_ships[i] != null) {
				if (check_collision(a, world_ships[i])) {
					Ship attacking = world_ships[i];
					for(int ax =0;ax< a.collision.length;ax++){
						Vertex2d corner = a.collision[ax].whole();
						corner.add2(core.G.revert_coordinates(a.absolute_position.xy()));
						
						for(int at = 0;at < attacking.collision.length;at++){
							Vertex2d attacker = attacking.collision[at].whole();
							attacker.add2(core.G.revert_coordinates(attacking.absolute_position));
							
							attacker = com.lcass.util.Util.rotate(attacker, a.rotpoint, -a.rotation);
							if(((attacker.x > corner.x) && (attacker.x < corner.u))|| ((attacker.u > corner.x) && (attacker.u < corner.u))){
								if(((attacker.y > corner.y) && (attacker.y < corner.v))|| ((attacker.v > corner.y) && (attacker.v < corner.v))){
									corner.sub2(core.G.revert_coordinates(a.absolute_position.xy()));
									corner.div(32);
									attacker.sub2(core.G.revert_coordinates(attacking.absolute_position.xy()));
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
					for(int ax =0;ax< a.collision.length;ax++){
						Vertex2d corner = a.collision[ax].whole();
						corner.add2(core.G.revert_coordinates(a.absolute_position.xy()));
						corner.sub(new Vertex2d(32,0,0,0));
						for(int at = 0;at < attacking.collision.length;at++){
							Vertex2d attacker = attacking.collision[at].whole();
							attacker = com.lcass.util.Util.rotate(attacker, attacking.rotpoint, -attacking.compound_rotation);					
							attacker = com.lcass.util.Util.rotate(attacker, a.rotpoint, -a.compound_rotation);
							attacker.add2(core.G.revert_coordinates(attacking.absolute_position.xy()));
									
							if(((attacker.x >= corner.x) && (attacker.x <= corner.u))|| ((attacker.u >= corner.x) && (attacker.u <= corner.u))){
								
								if(((attacker.y >= corner.y) && (attacker.y <= corner.v))|| ((attacker.v >= corner.y) && (attacker.v <= corner.v))){
									corner.sub2(core.G.revert_coordinates(a.absolute_position.xy()));
									corner.div(32);
									attacker.sub2(core.G.revert_coordinates(attacking.absolute_position.xy()));
									attacker.div(32);
									a.damage(a.edges[ax].get_pos(), 100);
									attacking.damage(attacking.edges[at].get_pos(), 100);
									
								}
							}
						}
					}
				}
				
			}
		}
		start+=1;
	}

	public void tick() {
		if (coreship != null) {
			coreship.tick();
			coreship.bind_camera(campos);
			check_ship_collision(coreship);
		}
		start = 0;
		for (int i = 0; i < world_ships.length; i++) {
			if (world_ships[i] != null) {
				if (coreship != null) {
					world_ships[i].bind_camera(new Vertex2d(
							-(coreship.absolute_position.x - campos.x),
							-(coreship.absolute_position.y - campos.y)).add(world_ships[i].absolute_position));

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
				coreship = new Ship(64, 64, core,
						new world(core, 64, 64, data), this);
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
}
