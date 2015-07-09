package com.lcass.entity;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.core.DEFINES;
import com.lcass.game.world.Ship;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Progressive_buffer;

public class CrewHandler {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private boolean update = true;
	private VBO draw;
	private Progressive_buffer[] draw_data = new Progressive_buffer[2];
	private Core core;

	public CrewHandler(Core core) {
		this.core = core;
		draw = new VBO(core.G.mainvbo);
		draw_data[0] = new Progressive_buffer(null, false);
		draw_data[1] = new Progressive_buffer(null, true);
		draw.create(1000);
		draw.bind_texture(core.crew_sprite.gettexture());
	}

	public int add_crew(Entity e) {
		e.set_id(entities.size());
		entities.add(e);
		update = true;
		return entities.size() - 1;
	}

	public void clear_crew() {

	}

	public Entity get_crew(int position) {
		return position > entities.size() ? null : entities.get(position);
	}
	public Entity get_crew_id(int id) {
		for(Entity e : entities){
			if(e.get_id() == id){
				return e;
			}
		}
		return null;
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
			draw.clear_data();
			for (Entity e : entities) {
				Vertex2d[] temp = core.G.rectangle_vert((int)(e.get_render_pos().x) * 2,(int)(e.get_render_pos().y) * 2, DEFINES.ENTITY_WIDTH, DEFINES.ENTITY_HEIGHT,e.get_sprite());
				Vertex2d rot = e.get_rotation();
				if(rot.u != 0){
					temp = com.lcass.util.Util.rotate(temp, rot, rot.u);
				}
				Progressive_buffer[] temp_prog = core.G.to_buffer(temp);
				draw_data[0].extend(temp_prog[0]);
				draw_data[1].extend(temp_prog[1]);
				if(e.selected()){
					
					Vertex2d[] tempselected = core.G.rectangle_vert((int)(e.get_render_pos().x) * 2,(int)(e.get_render_pos().y) * 2, DEFINES.ENTITY_WIDTH, DEFINES.ENTITY_HEIGHT,core.crew_sprite.getcoords(48, 0, 64, 16));
					if(rot.u != 0){
						temp = com.lcass.util.Util.rotate(tempselected, rot, rot.u);
					}
					Progressive_buffer[] temp_selected = core.G.to_buffer(tempselected);
					draw_data[0].extend(temp_selected[0]);
					draw_data[1].extend(temp_selected[1]);
				}
				}
			draw.edit_data(draw_data);
		}
		Vertex2d rotation = core.game.ships.get_cam_rotation();
		draw.set_rot_pos(rotation);
		draw.rotate(rotation.u);
		

	}
	public Entity get_crew(Vertex2d position,int owner){
		for(int i = 0;i < entities.size();i++){
			Entity a= entities.get(i);
			Vertex2d pos = a.get_abs().whole().sub(core.game.ships.get_ship(owner).correct_pos);
			pos.div(16);
			System.out.println(pos.x);
			if(pos.x == position.x && pos.y == position.y){
				return entities.get(i);
			}
		}
		return null;
	}

	public void render() {
		
		draw.set_position(core.game.ships.coreship.camera.whole().sub(core.game.ships.coreship.absolute_position));
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
