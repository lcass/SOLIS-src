package com.lcass.game.world.resources;

import java.util.ArrayList;

import com.lcass.game.tiles.Tile;

public class network {
	public ArrayList<Tile> sources = new ArrayList<Tile>();
	public ArrayList<Tile> wire = new ArrayList<Tile>();
	public ArrayList<Tile> consumers = new ArrayList<Tile>();
	public int total_power = 0;
	public int current_power = 0;
	public int voltage = 50;
	public int requested_power = 0;
	public float resistance = 0;// ohms
	private boolean updated = false;
	private boolean pipe = false;

	public network(boolean pipe) {
		this.pipe = pipe;
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
				float volts_lost = amps * resistance;
				if (!(volts_lost >= voltage)) {
					current_power = (int) (amps * (voltage - volts_lost));
				} else {
					current_power = 0;// total power-net failure.
				}
			} else {
				current_power = total_power;
			}
			for (int i = 0; i < consumers.size(); i++) {
				consumers.get(i).set_active(false);
				requested_power += consumers.get(i).get_power();
				if (consumers.get(i).get_power() > current_power) {
					current_power -= consumers.get(i).get_power();
					consumers.get(i).set_active(true);
				}
			}

		}

	}
}
