package com.lcass.game.world;

import com.lcass.core.Core;
import com.lcass.game.tiles.Sub_Tile;
import com.lcass.game.tiles.Tile;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Progressive_buffer;

public class world {
	private Progressive_buffer[] renderdata = new Progressive_buffer[2];// initiate
																		// an
																		// empty
																		// buffer
																		// for
																		// the
																		// tiles.
	public int obwidth = 32;
	public int obheight = 32;
	public int texwidth = 16;
	public int texheight = 16;
	public int mapwidth;
	public int mapheight;
	public Vertex2d camera = new Vertex2d(0, 0, 0, 0);
	private Tile[] tilemap;
	private VBO drawdata;

	public boolean hasdata = false;
	private Core core;

	public world(Core core, int width, int height) {
		this.core = core;
		tilemap = new Tile[width * height];
		this.mapwidth = width;
		this.mapheight = height;
		renderdata[0] = new Progressive_buffer(null, false);
		renderdata[1] = new Progressive_buffer(null, true);
		drawdata = new VBO(core.G.mainvbo);
		drawdata.create(width * height * 12 * 2);
		drawdata.bind_texture(core.tile_sprite.gettexture());
		create_images();
	}

	public world(Core core, int width, int height, Tile[] tiles) {
		this.core = core;
		tilemap = new Tile[width * height];
		this.mapwidth = width;
		this.mapheight = height;
		renderdata[0] = new Progressive_buffer(null, false);
		renderdata[1] = new Progressive_buffer(null, true);
		drawdata = new VBO(core.G.mainvbo);
		drawdata.create(width * height * 12);
		drawdata.bind_texture(core.tile_sprite.gettexture());
		create_images();
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] != null) {
				tiles[i].set_world(this);

				add_tile(tiles[i]);
			}
		}

	}

	public void render() {

		drawdata.render();
		if (render_COM) {
			vCOM.render();
			vCOTl.render();
			vCOTr.render();
			vCOTu.render();
			vCOTd.render();
		}
	}

	public void add_tile(Tile t) {// should be passed as a
									// new Tile;
		Vertex2d pos = t.get_pos();

		int coordinate = (int) (pos.x + (pos.y * mapwidth));

		if (coordinate < tilemap.length) {

			tilemap[coordinate] = t;
			tilemap[coordinate].set_world(this);
			tilemap[coordinate].bind();
			tilemap[coordinate].set_array_pos(coordinate);
			if (render_COM) {
				update_COM();
			}
		} else {
			System.out.println("Error , position is outside the map bounds");
			return;
		}
		hasdata = true;
	}

	public void add_tile(Tile t, int index) {// should be passed as a
		// new Tile;
		Vertex2d pos = t.get_pos();// override function

		int coordinate = (int) (pos.x + (pos.y * mapwidth));

		if (coordinate < tilemap.length) {
			t.set_index(index);
			tilemap[coordinate] = t;
			tilemap[coordinate].bind_index();
			tilemap[coordinate].set_array_pos(coordinate);
			if (render_COM) {
				update_COM();
			}
		} else {
			System.out.println("Error , position is outside the map bounds");
			return;
		}
		hasdata = true;
	}

	public void set_map_size(Vertex2d size) {
		mapwidth = (int) size.x;
		mapheight = (int) size.y;
	}

	public Tile getnew(Tile t) {
		try {

			return t.getClass().newInstance();

		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Sub_Tile getnew(Sub_Tile t) {
		try {

			return t.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean set_sub(Sub_Tile sub, Vertex2d position) {
		int coordinate = (int) (position.x + (position.y * mapwidth));
		if (coordinate < tilemap.length) {
			if (tilemap[coordinate] != null) {
				if (tilemap[coordinate].support_type(sub.get_type())) {
					
					sub.bind();
					tilemap[coordinate].set_sub(sub);
					if (render_COM) {
						update_COM();
					}
					hasdata = true;
					return true;
				}
			}
		} else {
			System.out.println("Error , position is outside the map bounds");
			return false;
		}

		return false;

	}

	public boolean set_sub_sub(Sub_Tile sub, Vertex2d position) {
		int coordinate = (int) (position.x + (position.y * mapwidth));
		if (coordinate < tilemap.length) {
			if (tilemap[coordinate] != null) {
				if (tilemap[coordinate].get_sub() != null) {
					
					if(tilemap[coordinate].get_sub().get_sub() != null){
						
						return false;
						
					}
					if (tilemap[coordinate].get_sub().support_type(
							sub.get_type())) {
						
						tilemap[coordinate].get_sub().attach_sub(sub);
						
						if (render_COM) {
							update_COM();
						}
						hasdata = true;
						return true;
					} 

				}
			}
		}
		else {
			System.out
					.println("Error , position is outside the map bounds");
			return false;
		}
		return false;
	}

	public void remove_tile(Vertex2d position) {
		int coordinate = (int) (position.x + (position.y * mapwidth));

		if (coordinate < tilemap.length) {

			if (tilemap[coordinate] != null) {

				tilemap[coordinate].bind_null();
				tilemap[coordinate] = null;
				if (render_COM) {
					update_COM();
				}
			}
		} else {
			System.out.println("Out of bounds");
			return;
		}
		hasdata = true;

	}

	public void set_world(world w) {
		for (int i = 0; i < tilemap.length; i++) {
			if (tilemap[i] != null) {
				tilemap[i].set_world(w);
			}
		}
	}

	private Vertex2d forwardthrust, backthrust, upthrust, downthrust, COM;

	public void calculate_masscentre() {

		Tile[] temp_map = tilemap;
		float xsum = 0;
		float ysum = 0;
		float total = 0;
		for (int i = 0; i < temp_map.length; i++) {
			if (temp_map[i] != null) {

				Vertex2d temp_pos = temp_map[i].get_pos();
				float temp_mass = temp_map[i].get_mass();
				total += temp_mass;

				xsum += temp_pos.x * temp_mass;
				ysum += temp_pos.y * temp_mass;

			}
		}
		if (total > 0) {
			COM = new Vertex2d((xsum / total) + 1, (ysum / total) + 1);

		} else {
			COM = new Vertex2d(0, 0, 0, 0);
		}

	}

	private VBO vCOM, vCOTl, vCOTr, vCOTu, vCOTd;
	private boolean render_COM = true;

	public void create_images() {
		vCOM = new VBO(core.G.mainvbo);
		vCOTl = new VBO(core.G.mainvbo);
		vCOTr = new VBO(core.G.mainvbo);
		vCOTu = new VBO(core.G.mainvbo);
		vCOTd = new VBO(core.G.mainvbo);
		vCOM.bind_texture(core.tile_sprite.gettexture());
		vCOTl.bind_texture(core.tile_sprite.gettexture());
		vCOTr.bind_texture(core.tile_sprite.gettexture());
		vCOTu.bind_texture(core.tile_sprite.gettexture());
		vCOTd.bind_texture(core.tile_sprite.gettexture());
		vCOM.create(core.G.square(0, 0, 32,
				core.tile_sprite.getcoords(64, 16, 80, 32)));
		vCOTl.create(core.G.square(0, 0, 32,
				core.tile_sprite.getcoords(64, 16 * 4, 80, (16 * 5))));
		vCOTr.create(core.G.square(0, 0, 32,
				core.tile_sprite.getcoords(64, 16 * 2, 80, (16 * 3))));
		vCOTu.create(core.G.square(0, 0, 32,
				core.tile_sprite.getcoords(64, 16 * 5, 80, (16 * 6))));
		vCOTd.create(core.G.square(0, 0, 32,
				core.tile_sprite.getcoords(64, 16 * 3, 80, (16 * 4))));

	}

	public void toggle_COM() {
		render_COM = !render_COM;
	}

	public void update_COM() {
		calculate_masscentre();
		calculate_COT();
		vCOM.set_position(core.G.convert_coordinates(
				new Vertex2d((COM.x * 32) - 32, (COM.y * 32) - 16)).add(camera));
		vCOTr.set_position(core.G.convert_coordinates(
				new Vertex2d((forwardthrust.x * 32) - 32,
						(forwardthrust.y * 32) - 16)).add(camera));
		vCOTl.set_position(core.G
				.convert_coordinates(
						new Vertex2d((backthrust.x * 32) - 32,
								(backthrust.y * 32) - 16)).add(camera));
		vCOTu.set_position(core.G.convert_coordinates(
				new Vertex2d((upthrust.x * 32) - 32, (upthrust.y * 32) - 16))
				.add(camera));
		vCOTd.set_position(core.G
				.convert_coordinates(
						new Vertex2d((downthrust.x * 32) - 32,
								(downthrust.y * 32) - 16)).add(camera));
	}

	public void calculate_COT() {
		Tile[] temp = tilemap;

		for (int curdir = 0; curdir < 4; curdir++) {
			float xsum = 0;
			float ysum = 0;
			float total = 0;

			for (int i = 0; i < temp.length; i++) {
				if (temp[i] == null) {
					continue;
				}

				if (temp[i].get_name() == "thruster"
						&& temp[i].get_dir() == curdir) {
					total += temp[i].get_thrust();
					xsum += temp[i].get_pos().x * temp[i].get_thrust();
					ysum += temp[i].get_pos().y * temp[i].get_thrust();

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

	public boolean check_tile(Vertex2d position, Tile t) {
		int coordinate = (int) (position.x + (position.y * mapwidth));
		if (t == null) {
			return true;
		}
		if (coordinate < tilemap.length) {
			if (tilemap[coordinate] != null) {
				if (tilemap[coordinate].get_name() == t.get_name()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean within_map(Vertex2d pos) {

		if (pos.x > mapwidth - 1 || pos.y > mapheight - 1 || pos.x < 0
				|| pos.y < 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean check_tile(Vertex2d position) {
		int coordinate = (int) (position.x + (position.y * mapwidth));
		if (coordinate < tilemap.length) {
			if (tilemap[coordinate] != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public Tile get_tile(Vertex2d position) {
		int coordinate = (int) (position.x + (position.y * mapwidth));
		return tilemap[coordinate];
	}

	public void remove_tile(Vertex2d position, Tile t) {
		int coordinate = (int) (position.x + (position.y * mapwidth));
		if (coordinate < tilemap.length) {
			if (t.name == tilemap[coordinate].name) {
				tilemap[coordinate] = null;
			}
		} else {
			System.out.println("Out of bounds");
			return;
		}

	}

	// Must:: implement update to rendering and binding code , objects store
	// there graphics position then bind that on modification and closing.
	public int bind_render(Vertex2d position, Vertex2d sprite) {
		int out = renderdata[0].get_limit();
		Progressive_buffer[] temp = core.G.rectangle(
				(int) position.x * obwidth, (int) position.y * obheight,
				obwidth, obheight, core.tile_sprite.getcoords((int) sprite.x,
						(int) (sprite.y), (int) (texwidth + sprite.x),
						(int) (sprite.y + texheight)));

		renderdata[0].extend(temp[0]);
		renderdata[1].extend(temp[1]);
		hasdata = true;
		return out;
	}

	public void bind_render(int index, Vertex2d position, Vertex2d sprite) {
		Progressive_buffer[] temp = core.G.rectangle(
				(int) position.x * obwidth, (int) position.y * obheight,
				obwidth, obheight, core.tile_sprite.getcoords((int) sprite.x,
						(int) (sprite.y), (int) (texwidth + sprite.x),
						(int) (sprite.y + texheight)));

		renderdata[0].index_put(index, temp[0]);
		renderdata[1].index_put(index, temp[1]);

		hasdata = true;
	}

	public void bind_empty_render(int position) {
		Progressive_buffer[] temp = core.G.rectangle(0, 0, 0, 0,
				core.tile_sprite.getcoords(0, 0, 0, 0));

		renderdata[0].index_put(position, temp[0]);
		renderdata[1].index_put(position, temp[1]);

		hasdata = true;
	}

	public void cleanup() {
		drawdata.dispose();
	}

	public void update_camera(Vertex2d newpos) {
		this.camera = newpos;
	}

	public void tick() {

		drawdata.set_position(camera);
		if (render_COM) {
			if (COM != null) {
				vCOM.set_position(core.G.convert_coordinates(
						new Vertex2d((COM.x * 32) - 32, (COM.y * 32) - 16))
						.add(camera));
				vCOTr.set_position(core.G.convert_coordinates(
						new Vertex2d((forwardthrust.x * 32) - 32,
								(forwardthrust.y * 32) - 16)).add(camera));
				vCOTl.set_position(core.G.convert_coordinates(
						new Vertex2d((backthrust.x * 32) - 32,
								(backthrust.y * 32) - 16)).add(camera));
				vCOTu.set_position(core.G.convert_coordinates(
						new Vertex2d((upthrust.x * 32) - 32,
								(upthrust.y * 32) - 16)).add(camera));
				vCOTd.set_position(core.G.convert_coordinates(
						new Vertex2d((downthrust.x * 32) - 32,
								(downthrust.y * 32) - 16)).add(camera));
			}
		}
		if (reset) {
			renderdata[0].clear();
			renderdata[1].clear();
			drawdata.clear_data();
			for (int i = 0; i < tilemap.length; i++) {
				if (tilemap[i] != null) {

					tilemap[i].bind();

				}
			}
			hasdata = true;
			reset = false;
		}
		for (int i = 0; i < tilemap.length; i++) {
			if (tilemap[i] != null) {
				tilemap[i].tick();

			}
		}

		if (hasdata) {

			drawdata.edit_data(renderdata);
			
			hasdata = false;
		}
	}

	public void manual_data() {
		hasdata = true;
	}

	private boolean reset = true;

	public void reset() {
		reset = true;
	}

	public Tile[] get_map() {
		return tilemap;
	}

	public void translate(Vertex2d pos) {
		drawdata.translate(pos);
	}

	public void set_position(Vertex2d pos) {
		drawdata.set_position(pos);
	}

	public void rotate(float angle) {
		drawdata.rotate(angle);
	}

	public void set_rot_pos(Vertex2d pos) {
		drawdata.set_rot_pos(pos);
	}

	public void clear_rebuild(Tile[] tiles) {
		drawdata.clear_data();
		renderdata[0].clear();
		renderdata[1].clear();
		reset = true;
		for (int i = 0; i < tilemap.length; i++) {
			tilemap[i] = null;
		}
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] != null) {

				add_tile(tiles[i]);
			}
		}
	}
	public void set_data(Tile[] t){
		tilemap = new Tile[64 * 64];
		for(int i = 0;i < t.length;i++){
			tilemap[i] = t[i];
		}
	}
}
