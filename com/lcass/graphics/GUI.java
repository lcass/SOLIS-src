package com.lcass.graphics;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.graphics.texture.Texture;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Encapsulated_method;
import com.lcass.util.InputHandler;
import com.lcass.util.Progressive_buffer;

public class GUI {
	protected spritesheet button_sheet;
	protected InputHandler ih;
	protected graphics graph;
	private VBO coredata;
	public VBO backdata;
	private VBO textdata;
	private Progressive_buffer[] current_data = new Progressive_buffer[2];
	private Progressive_buffer[] text_data = new Progressive_buffer[2];
	private Core core;
	private Vertex2d position;
	private TextGenerator tg;
	private Vertex2d ac_translate = new Vertex2d(0,0,0,0);
	private Vertex2d translate = new Vertex2d(0,0,0,0);
	private boolean hasdata = false, updated = false;
	
	private ArrayList<Button> buttons = new ArrayList<Button>();

	public GUI(Vertex2d position,Progressive_buffer[] backdata, Texture t, Core core) {
		this.core = core;
		current_data[0] = new Progressive_buffer(null, false);
		current_data[1] = new Progressive_buffer(null, true);
		text_data[0] = new Progressive_buffer(null,false);
		text_data[1] = new Progressive_buffer(null,true);
		this.graph = core.G;
		this.backdata = new VBO(core.G.mainvbo);
		this.ih = core.ih;
		this.graph = core.G;
		this.backdata.create(backdata);
		this.backdata.bind_texture(t);
		button_sheet = new spritesheet("textures/Button.png");
		this.coredata = new VBO(core.G.mainvbo);
		this.coredata.create(12 * 500);
		this.coredata.bind_texture(button_sheet.gettexture());
		textdata = new VBO(core.G.mainvbo);
		textdata.create(12 * 5000);
		
		tg = new TextGenerator(core);
		textdata.bind_texture(tg.gettexture());
		this.position = position;
	}

	protected void bind(Progressive_buffer[] data) {
		current_data[0].extend(data[0]);
		current_data[1].extend(data[1]);

		hasdata = true;
	}

	public void tick() {
		for(int i = 0; i < buttons.size();i++){
			buttons.get(i).tick();
		}
		if (updated) {
			
			current_data[0].clear();
			current_data[1].clear();
		}
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).set_trans(ac_translate);
			buttons.get(i).throw_at_GUI();
			
		}
		if (updated) {
			coredata.edit_data(current_data);
			
		}

	}

	public void render() {
		backdata.set_position(new Vertex2d(translate.x,translate.y,0,0));
		backdata.render();
		coredata.set_position(new Vertex2d(translate.x,translate.y,0,0));
		textdata.set_position(new Vertex2d(translate.x,translate.y,0,0));
		if (hasdata) {
			
			coredata.render();
		}
		textdata.render();
	}

	public void bind_button(Vertex2d position, boolean textured,
			Encapsulated_method function) {
		if (textured) {
			buttons.add(new Button(new Vertex2d(position.x, position.y,
					position.x + 128, position.y + 26), textured, this,
					function));
		} else {
			buttons.add(new Button(new Vertex2d(position.x, position.y,
					position.x + position.u, position.y  +position.v), textured, this, function));
		}
		updated = true;
		
	}

	public void update() {
		updated = true;
	}
	public void modify_text(int index,Vertex2d position, String text, int fontsize){
		Progressive_buffer[] texttemp = tg.generate_text(text,this.position.whole().add(position), fontsize);
		
		text_data[0].index_put(index, texttemp[0]);
		text_data[1].index_put(index,texttemp[1]);
		textdata.edit_data(text_data);
	}
	public int draw_text(Vertex2d position , String text , int fontsize){
		Progressive_buffer[] texttemp = tg.generate_text(text,this.position.whole().add(position), fontsize);
		int stored = text_data[0].get_limit();
		text_data[0].extend(texttemp[0]);
		text_data[1].extend(texttemp[1]);
		textdata.edit_data(text_data);
		return stored;
	}
	public void set_instance(Object a){
		for(int i = 0;i < buttons.size();i++){
			buttons.get(i).set_instance(a);
		}
	}
	public void set_translation(Vertex2d translate){
		ac_translate = translate.whole();
		this.translate = core.G.convert_coordinates(ac_translate).sub(new Vertex2d(0,2,0,0));
		
	}
}
