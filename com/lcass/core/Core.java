package com.lcass.core;

import java.lang.reflect.Method;

import org.lwjgl.opengl.Display;

import com.lcass.game.Game;
import com.lcass.game.world.world;
import com.lcass.graphics.Particles;
import com.lcass.graphics.VBO;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.graphics;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.InputHandler;
import com.lcass.util.Progressive_buffer;

public class Core{
	public static int width = 1600;
	public static int height = (width /16) * 9;
	public graphics G;

	public Game game;
	private world a, b;
	public InputHandler ih;
	private Progressive_buffer[] background;
	private spritesheet background_sprite;
	public spritesheet effect_sprite,particle_sprite;
	public spritesheet crew_sprite;
	public spritesheet tile_sprite;
	public spritesheet proj_sprite;
	public Core(){
		G = new graphics(this);
		G.create_display(width, height, "SOLIS",200, 60);
		
		ih = new InputHandler(this);
		crew_sprite = new spritesheet("textures/Entities.png");
		//bind close function
		Method close = G.obtain_method(this.getClass(), "cleanup");
		G.bind_close_function(close);
		//bind inventory tick function
		Method ihtick = G.obtain_method(ih.getClass(),"tick");
		G.bind_function(ihtick,ih);
		//int texture
		tile_sprite = new spritesheet("textures/blocksprites.png");
		background_sprite = new spritesheet("textures/planet.png");
		effect_sprite = new spritesheet("textures/effects.png");
		particle_sprite = new spritesheet("textures/particles.png");
		proj_sprite = new spritesheet("textures/projectiles.png");
		game = new Game(new world(this,64,64 , -2),this);
		//bind resize method
		Method resize = G.obtain_method(this.getClass(), "resize");
		G.bind_resize_method(resize);
		
		//setup gamefunctions
		Method gametick = G.obtain_method(game.getClass(),"tick");
		Method gamerender = G.obtain_method(game.getClass(),"render");
		//
		G.bind_function(gametick,game);
		G.bind_render_function(gamerender, game);
		//setup background
		VBO backgroundtemp = new VBO(G.mainvbo);
		background = G.rectangle(0, 0, this.width, this.height, background_sprite.getcoords(0,0,1920,1080));
		backgroundtemp.create(background);
		backgroundtemp.bind_texture(background_sprite.gettexture());
		backgroundtemp.set_bloom(true);
		G.bind_background(backgroundtemp);
		game.init();
		G.start_tick();
		
	}
	public void set_background(String location){
		background_sprite = new spritesheet(location);
		resize();
	}
	
	public void resize(){
		this.width = G.getwidth();
		this.height = G.getheight();
		background = G.rectangle(0,0, this.width, this.height, background_sprite.getcoords(0, 0, 1920 ,1080));
		VBO backgroundtemp = new VBO(G.mainvbo);
		backgroundtemp.create(background);
		backgroundtemp.bind_texture(background_sprite.gettexture());
	//	G.bind_background(backgroundtemp);
	}
	public static void main(String[] args){
		new Core();
	}
	public void cleanup(){
		game.cleanup();
		
		G.cleanup();
		
		Display.destroy();
		
	}
	public static Vertex2d get_dimensions(){
		return new Vertex2d(width,height);
	}

}
