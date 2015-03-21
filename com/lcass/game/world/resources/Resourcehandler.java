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
		
		for (network n : networks) {
			n.tick();
		}
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
		generate_power();

	}

	public void check_electricity(int pos, int curr_network) {
		Vertex2d positions = ship.cable_pos(pos);
	
		if (positions.x != -1) {
			if (ship.map[(int) positions.x].get_network() != curr_network) {
				ship.map[(int) positions.x].set_network(curr_network);
				check_electricity((int) positions.x, curr_network);
				
			}
		}
		if (positions.y != -1) {
			
			if (ship.map[(int) positions.y].get_network() != curr_network) {
				ship.map[(int) positions.y].set_network(curr_network);
				check_electricity((int) positions.y, curr_network);
			}
		}
		if (positions.u != -1) {
			if (ship.map[(int) positions.u].get_network() != curr_network) {
				ship.map[(int) positions.u].set_network(curr_network);
				check_electricity((int) positions.u, curr_network);
			}
		}
		if (positions.v != -1) {
			if (ship.map[(int) positions.v].get_network() != curr_network) {
				ship.map[(int) positions.v].set_network(curr_network);
				check_electricity((int) positions.v, curr_network);
			}
		}
	}

	public void generate_power() {
		ship.calculate_electronics();
		int[] electronics = ship.electronics;
		int curr_network = 1;
		networks.clear();
		for(int i = 0;i < electronics.length;i++){
			if(ship.map[electronics[i]] != null){
				ship.map[electronics[i]].set_network(0);
			}
		}
		for (int i = 0; i < electronics.length; i++) {

			if (ship.map[electronics[i]].is_supplier()
					&& ship.map[electronics[i]].get_network() == 0) {
				ship.map[electronics[i]].set_network(curr_network);
				networks.add(new network(false,ship));

				
				Vertex2d coordinatev = ship.map[electronics[i]].get_pos()
						.whole();
				
				int coordinate = (int) (coordinatev.x + (coordinatev.y * ship.ship.mapwidth));
				
				check_electricity(coordinate, curr_network);
			}
		}
		for (int i = 0; i < ship.electronics.length; i++) {
			int pos = electronics[i];
			
			if (ship.map[pos].get_network() != 0) {
				
				if (ship.map[pos].is_supplier()) {
					networks.get(ship.map[pos].get_network() - 1).add_source(
							ship.map[pos]);
				
				}
				if (ship.map[pos].is_wire()) {
					networks.get(ship.map[pos].get_network() - 1).add_wire(
							ship.map[pos]);
					
				}
				if (ship.map[pos].is_user()) {
					networks.get(ship.map[pos].get_network() - 1).add_consumer(
							ship.map[pos]);
				}
			}
		}
		this.ship.update_movement();

	}
	public void increase_voltage(int increment){
		for(network n: networks){
		
			n.changevoltage(increment);
		}
	}
	public void decrease_voltage(int increment){
		for(network n: networks){
			if(n.voltage > increment){
				n.changevoltage(-increment);
			}
		}
	}
	public float get_voltage(){
		if(networks.size()>0){
			return networks.get(0).voltage;
		}
		return 0;
	}
	public float get_resistance(){
		if(networks.size()>0){
			return networks.get(0).resistance;
		}
		return 0;
	}

	public float get_power(){
		if(networks.size()>0){
			return networks.get(0).current_power;
		}
		return 0;
	}


}
