package com.lcass.graphics;

import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Progressive_buffer;

public class particle_spawn {
	public VBO particles;
	public float decay, wait;
	public int location = 0, creation_rate = 0, velocity, amount, offset_x,
			offset_y;
	private Vertex2d spawn;
	private spritecomponent s;
	private Vertex2d[] data;
	private Particles particle;
	private boolean creating = false, initial_creating = false,
			vbo_generated = true;

	// As noted in the shader this is set for the preference of an 8 by 8 effect
	// grid so each effect with 1/8th the width and 1/8th the height of the
	// entire texture
	/**
	 * 
	 * @param particle
	 * @param data
	 * @param s
	 * @param decay
	 * @param size
	 * @param amount
	 * @param location
	 * @param wait
	 * @param velocity
	 * @param offset_x
	 * @param offset_y
	 * @param creating
	 * @param creation_rate
	 */
	public particle_spawn(Particles particle, Vertex2d data, spritecomponent s,
			float decay, int size, int amount, int location, int wait,
			float velocity, int offset_x, int offset_y, boolean creating,
			int creation_rate) {// location
		// is
		// done
		// with
		// a
		// recursive
		// nature,
		// the
		// particle
		// kills
		// itself
		spawn = data;

		this.s = s;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		this.velocity = (int) (velocity);
		this.amount = amount;
		this.initial_creating = creating;
		this.decay = decay;
		this.particle = particle;
		particles = new VBO(particle.core.G.mainvbo);
		this.creating = creating;
		particles.set_decay(decay);
		particles.set_spawn(particle.core.G.convert_coordinates(data));
		particles.set_point();
		particles.set_size(size);
		particles.bind_texture(s.t);
		particles.set_velocity(velocity);
		particles.set_alpha(0.4f);
		times = new int[amount];
		this.creation_rate = creation_rate;
		this.wait = wait;
		this.data = new Vertex2d[amount];
		if (!creating) {
			for (int i = 0; i < amount; i++) {
				this.data[i] = particle.core.G
						.convert_coordinates(new Vertex2d(
								(float) ((particle.random
										.nextInt((int) velocity) - ((velocity / 2)))
										+ data.x + ((particle.random
										.nextInt((int) velocity) - ((velocity / 2))) * Math
										.sin(i)))
										- offset_x,
								(float) ((particle.random
										.nextInt((int) velocity) - ((velocity / 2)))
										+ data.y + ((particle.random
										.nextInt((int) velocity) - ((velocity / 2))) * Math
										.cos(i)))
										- offset_y));
				this.data[i].u = s.x;
				this.data[i].v = s.y;

			}

			particles.create_point(this.data);
		} else {
			vbo_generated = false;
		}

	}

	int[] times;
	private int personal_ticks = 0;

	public void tick() {

		if ((!is_alive() && !initial_creating) || particles == null ) {
			kill();
			particle.remove(location);// it kills itself.
			return;
		}
		if (creating && vbo_generated) {
			personal_ticks += 1;
			int position = ((personal_ticks / creation_rate) % (amount / 10)) * 10;

			int end = position + 10;
			if (end > amount - 1) {
				position = 0;
				end = position + 10;
			}

			for (int i = position; i < end; i++) {

				data[i] = particle.core.G
						.convert_coordinates(new Vertex2d(
								(float) ((particle.random
										.nextInt((int) velocity) - ((velocity / 2)))
										+ spawn.x + ((particle.random
										.nextInt((int) velocity) - ((velocity / 2))) * Math
										.sin(position)))
										- offset_x,
								(float) ((particle.random
										.nextInt((int) velocity) - ((velocity / 2)))
										+ spawn.y + ((particle.random
										.nextInt((int) velocity) - ((velocity / 2))) * Math
										.cos(position)))
										- offset_y));
				data[i].u = s.x;
				data[i].v = s.y;
				times[i] = particles.get_time();
			}
			if (data[0] != null) {
				particles.edit_point(data, times);
			}

		}

	}

	public void clear_data() {
		data = new Vertex2d[amount];
		for (int i = 0; i < data.length; i++) {
			data[i] = new Vertex2d(-particle.core.width, 0);
		}
	}

	public boolean is_alive() {
		if (particles == null) {
			return false;
		}
		if (decay + (wait * decay) > particles.get_render_ticks()) {
			return true;
		} else {
			return false;
		}
	}

	public void set_offset_x(int off) {
		offset_x = off;
	}

	public void set_offset_y(int off) {
		offset_y = off;
	}

	public void kill() {
		if (particles != null) {
			particles.dispose();
			particles = null;
		}
	}

	public void set_creation(boolean val) {
		creating = val;

		if (!vbo_generated && creating) {
			vbo_generated = true;
			data = new Vertex2d[amount];
			for (int i = 0; i < amount; i++) {
				this.data[i] = particle.core.G
						.convert_coordinates(new Vertex2d(
								(float) ((particle.random
										.nextInt((int) velocity) - ((velocity / 2)))
										+ spawn.x + ((particle.random
										.nextInt((int) velocity) - ((velocity / 2))) * Math
										.sin(i)))
										- offset_x,
								(float) ((particle.random
										.nextInt((int) velocity) - ((velocity / 2)))
										+ spawn.y + ((particle.random
										.nextInt((int) velocity) - ((velocity / 2))) * Math
										.cos(i)))
										- offset_y));
				this.data[i].u = s.x;
				this.data[i].v = s.y;

			}

			particles.create_point(this.data);
		}
		if (val) {
			for (int i = 0; i < amount; i++) {
				times[i] += particles.get_time();
			}
			particles.edit_point(data, times);
		}

	}

	public void clean_kill() {
		if (particles != null) {
			particles.dispose();
			particles = null;
		}
		particle.remove(location);
	}

	public void render() {
		if (particles != null && vbo_generated) {
			particles.render();
		}
	}

	public void translate(Vertex2d pos) {
		if (particles != null && vbo_generated) {
			particles.set_position(pos);
		}
	}

	public void set_spawn(Vertex2d spawn) {
		this.spawn = spawn;
		if (vbo_generated) {
			particles.set_spawn(particle.core.G.convert_coordinates(spawn));
		}
	}
	public void move(Vertex2d addition){
		if(particles != null && vbo_generated){
			particles.translate(addition);
		}
	}
}
