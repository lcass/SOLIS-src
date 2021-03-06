package com.lcass.game.world.resources;

import java.util.ArrayList;

import com.lcass.game.tiles.Tile;
import com.lcass.game.world.Ship;

public class network {
	public ArrayList<Tile> sources = new ArrayList<Tile>();
	public ArrayList<Tile> wire = new ArrayList<Tile>();
	public ArrayList<Tile> consumers = new ArrayList<Tile>();
	public float total_power = 0;
	public float current_power = 0;
	public float voltage = 50;
	public float requested_power = 0;
	public float resistance = 0;// ohms
	private boolean updated = false;
	private boolean pipe = false;
	private Ship ship;

	public network(boolean pipe,Ship s) {
		this.pipe = pipe;
		ship = s;
	}

	public void reset() {
		sources.clear();
		wire.clear();
		consumers.clear();
	}

	public void add_source(Tile a) {
		sources.add(a);
		update();
	}

	public void add_wire(Tile a) {
		wire.add(a);
		update();
	}

	public void add_consumer(Tile a) {
		consumers.add(a);

		update();
	}

	public void update() {
		updated = true;
	}

	public void tick() {
		if (updated) {
			updated = false;

			total_power = 0;
			current_power = 0;
			requested_power = 0;
			resistance = 0;
			for (int i = 0; i < sources.size(); i++) {

				total_power += sources.get(i).get_power();

			}
			for (int i = 0; i < wire.size(); i++) {

				resistance += wire.get(i).get_resistance();
			}
			// asuming watts = total_power and initial voltage is 50v
			if (!pipe) {
				float amps = total_power / voltage;
				float power_lost = amps * amps * resistance;
				if (!(power_lost >= total_power)) {
					current_power = total_power - power_lost;
				} else {
					current_power = 0;// total power-net failure.
				}
			} else {
				current_power = total_power;
			}
			for (int i = 0; i < consumers.size(); i++) {
				//if (ship.map[consumers.get(i).get_array_pos()].get_active()) {
					ship.map[consumers.get(i).get_array_pos()].set_active(false);
					requested_power += consumers.get(i).get_power();
					if (consumers.get(i).get_power() <= current_power) {
						current_power -= consumers.get(i).get_power();
						ship.map[consumers.get(i).get_array_pos()].set_active(true);

					//}
				}
			}
			ship.update_movement();

		}

	}

	public void changevoltage(float value) {
		voltage += value;
		updated = true;
	}
}
