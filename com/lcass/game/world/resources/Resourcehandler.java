package com.lcass.game.world.resources;

import java.util.ArrayList;

import com.lcass.game.tiles.Tile;
import com.lcass.game.world.Ship;
import com.lcass.graphics.Vertex2d;

public class Resourcehandler {// designed to handle all resources on a ship.
	private Resource electricity;
	private Resource fuel;
	private Resource air;
	private ArrayList<Tile> machines = new ArrayList<Tile>();
	private ArrayList<network> networks = new ArrayList<network>();
	private Ship ship;

	public Resourcehandler(Ship s) {
		ship = s;
	}

	public void tick() {

	}

	public void calculate_resources() {
		machines.clear();
		for (int i = 0; i < ship.map.length; i++) {
			Tile dealing = ship.map[i];
			if (dealing == null) {
				continue;
			}
			if (dealing.is_electric()) {
				machines.add(dealing);
			}
			// Other bits are for air n engines
		}

	}
	public void check_electricity(int pos ,int curr_network){
		Vertex2d positions = ship.cable_pos(pos);
		if (positions.x != -1) {
			if (ship.map[(int) positions.x].get_network() != curr_network) {
				ship.map[(int) positions.x].set_network(curr_network);
				check_electricity((int)positions.x,curr_network);
			}
		}
		if (positions.y != -1) {
			if (ship.map[(int) positions.y].get_network() != curr_network) {
				ship.map[(int) positions.y].set_network(curr_network);
				check_electricity((int)positions.y,curr_network);
			}
		}
		if (positions.u != -1) {
			if (ship.map[(int) positions.u].get_network() != curr_network) {
				ship.map[(int) positions.u].set_network(curr_network);
				check_electricity((int)positions.u,curr_network);
			}
		}
		if (positions.v != -1) {
			if (ship.map[(int) positions.v].get_network() != curr_network) {
				ship.map[(int) positions.v].set_network(curr_network);
				check_electricity((int)positions.v,curr_network);
			}
		}
	}

	public void generate_power() {
		
		int curr_network = 1;
		for (int i = 0; i < machines.size(); i++) {
			Tile handling = machines.get(i);
			if (handling.is_supplier() && handling.get_network() == 0) {
				handling.set_network(curr_network);
				networks.add(new network());
				int netpos = curr_network - 1;
				networks.get(netpos).add_source(handling);
				Vertex2d coordinatev = handling.get_pos().whole().div(32);
				int coordinate = (int) (coordinatev.x + (coordinatev.y * ship.ship.mapwidth));
				check_electricity(coordinate,curr_network);
			}
		}
			
	}

}
