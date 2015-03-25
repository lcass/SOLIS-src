package com.lcass.entity;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.core.DEFINES;
import com.lcass.game.world.Ship;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.Progressive_buffer;

public class CrewHandler {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private boolean update = true;
	private VBO draw;
	private Progressive_buffer[] draw_data = new Progressive_buffer[2];
	private Core core;
	public Ship ship;

	public CrewHandler(Core core, Ship s) {
		this.core = core;
		draw = new VBO(core.G.mainvbo);
		draw_data[0] = new Progressive_buffer(null, false);
		draw_data[1] = new Progressive_buffer(null, true);
		draw.create(1000);
		draw.bind_texture(core.crew_sprite.gettexture());
		this.ship = s;
	}

	public int add_crew(Entity e) {
		entities.add(e);
		update = true;
		return entities.size() - 1;
	}

	public void clear_crew() {

	}

	public Entity get_crew(int position) {
		return position > entities.size() ? null : entities.get(position);
	}

	public ArrayList<Entity> get_crew() {
		return this.entities;
	}

	public void tick() {
		for (Entity e : entities) {
			e.tick();
		}
		if (update) {
			draw_data[0].clear();
			draw_data[1].clear();
			for (Entity e : entities) {
				Vertex2d tempv = e.relative.whole().mult(2);
				
				Progressive_buffer[] temp = core.G.rectangle((int)(e.get_render_pos().x) * 2,(int)(e.get_render_pos().y) * 2, DEFINES.ENTITY_WIDTH, DEFINES.ENTITY_HEIGHT,e.get_sprite());
				draw_data[0].extend(temp[0]);
				draw_data[1].extend(temp[1]);
				}
			draw.edit_data(draw_data);
		}

		

	}

	public void render() {
		
		draw.set_position(ship.camera);
		draw.render();
	}

	public void update() {
		update = true;
	}

	public void dispose() {
		draw.dispose();
	}
	public void set_last(Vertex2d last){
		for(Entity e: entities){
			e.set_last(last);
		}
	}

}
