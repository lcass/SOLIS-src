package com.lcass.graphics;

import org.lwjgl.opengl.GL20;

public class CoreVBO {
	public float fps;
	public float particle_sim_rate = 300;
	public static int locationtime,locationbloom,locationtransform, locationzoom, locationrotatepos,locationdimension,locationrotate;
	public static int shader_id,vert_id,tex_id,particle_shader,particle_vert,particle_tex,particle_geom;
	public static int locationattribtex, locationattribvertex;
	public static int location_color;
	public static int particle_transform,particle_dimension,particle_time,particleattribvert,particleattribtex,particle_spawn,particle_decay,particle_alpha,particle_size,particle_velocity;
	private Shaderclass shaderclass,line_shader_class,particle_shader_class;
	public CoreVBO(){
		shaderclass = new Shaderclass();
		shaderclass.attachFragmentShader("./src/shaders/default_frag.frag");
		shaderclass.attachVertexShader("./src/shaders/default_vert.vert");
		line_shader_class = new Shaderclass();
		line_shader_class.attachFragmentShader("./src/shaders/line_frag.frag");
		line_shader_class.attachVertexShader("./src/shaders/default_vert.vert");
		particle_shader_class = new Shaderclass();
		particle_shader_class.attachFragmentShader("./src/shaders/particle_frag.frag");
		particle_shader_class.attachVertexShader("./src/shaders/particle_vert.vert");
		particle_shader_class.attachGeometryShader("./src/shaders/particle_geom.geom");
		particle_shader_class.link();
		shaderclass.link();
		line_shader_class.link();
		
		
		shader_id = shaderclass.programID;
		vert_id = shaderclass.vertexShaderID;
		tex_id = shaderclass.fragmentShaderID;
		particle_shader = particle_shader_class.programID;
		particle_vert = particle_shader_class.vertexShaderID;
		particle_tex = particle_shader_class.fragmentShaderID;
		particle_geom = particle_shader_class.geometryShaderID;
		locationrotatepos = GL20.glGetUniformLocation(shader_id, "rotpos");
		locationtime = GL20.glGetUniformLocation(shader_id, "timein");
		locationbloom = GL20.glGetUniformLocation(shader_id, "bloomval");
		locationtransform = GL20.glGetUniformLocation(shader_id, "transform");
		locationzoom = GL20.glGetUniformLocation(shader_id,"zoom");
		
		locationdimension = GL20.glGetUniformLocation(shader_id,"dimension");
		locationrotate = GL20.glGetUniformLocation(shader_id,"rotation");
		locationattribtex = GL20.glGetAttribLocation(shader_id,"texin");
		locationattribvertex = GL20.glGetAttribLocation(shader_id, "position");

		particle_transform = GL20.glGetUniformLocation(particle_shader, "transformation");
		particle_size = GL20.glGetUniformLocation(particle_shader, "size");
		particle_dimension = GL20.glGetUniformLocation(particle_shader,"dimension");
		particle_time = GL20.glGetUniformLocation(particle_shader,"time");
		particle_spawn = GL20.glGetUniformLocation(particle_shader, "spawn");
		particle_decay = GL20.glGetUniformLocation(particle_shader, "decay");
		particle_alpha = GL20.glGetUniformLocation(particle_shader,"alpha");
		particleattribvert = GL20.glGetAttribLocation(particle_shader, "vecin");
		particleattribtex = GL20.glGetAttribLocation(particle_shader, "texin");
		particle_velocity = GL20.glGetUniformLocation(particle_shader, "velocity");
		System.out.println(particle_size);
		
		
	}
	public void bind_shader(){
		shaderclass.bind();
	}
	public void un_bind_shader(){
		shaderclass.unbind();
	}
	public void bind_particle(){
		particle_shader_class.bind();
	}
	public void un_bind_particle(){
		particle_shader_class.unbind();
	}
	public void dispose(){
		particle_shader_class.dispose();
		line_shader_class.dispose();
		shaderclass.dispose();
	}
	public void set_fps(int fps){
		this.fps = fps;
	}
	public float fps_mod(){
		return fps/particle_sim_rate;
		
	}
}
