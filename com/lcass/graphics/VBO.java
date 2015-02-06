package com.lcass.graphics;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.lcass.core.Core;
import com.lcass.graphics.texture.Texture;
import com.lcass.util.Progressive_buffer;

public class VBO {
	public int vertid, texid, texcoordid, vertcount;
	private Texture t;
	private Vertex2d transformation, color, rotation_pos;
	private int bloom = 0;
	private boolean functional = false;
	private boolean point_vbo = false;
	private FloatBuffer vertex, texture;
	private Shaderclass shader;
	private CoreVBO super_vbo;
	private boolean is_line = false;
	private int width, height;
	private float rotate = 0;
	private float time = 0;//used by particles only but useful if implementing similar effects
	private float render_ticks = 1;
	private float decay = 0;
	private float alpha = 1;
	private float size = 1;
	private float velocity = 1;
	private Vertex2d spawn;
	private boolean tick = true;
	private Random r;

	public VBO(CoreVBO main) {
		r = new Random();
		r.setSeed(System.nanoTime() * 2);
		super_vbo = main;
		transformation = new Vertex2d(0, 0, 1, 0);
		if (super_vbo == null) {
			System.out
					.println("No core VBO set or issue setting up! /n Cannot setup VBO");
		} else {
			functional = true;
		}
		// locationtransform =
		// GL20.glGetUniformLocation(shader.vertexShaderID,"rotation");
		Vertex2d temp = Core.get_dimensions();
		width = (int) temp.x;
		rotation_pos = new Vertex2d(0, 0, 0, 0);
		height = (int) temp.y;

	}

	public void create(Vertex2d[] coordinates) {
		vertex = BufferUtils.createFloatBuffer(coordinates.length * 2);
		texture = BufferUtils.createFloatBuffer(coordinates.length * 2);
		vertcount = coordinates.length;

		for (int i = 0; i < coordinates.length; i++) {
			vertex.put(coordinates[i].x);
			vertex.put(coordinates[i].y);
			texture.put(coordinates[i].u);
			texture.put(coordinates[i].v);
		}
		vertid = GL15.glGenBuffers();
		texcoordid = GL15.glGenBuffers();
		vertex.rewind();
		texture.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texture, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}

	public void create_point(Vertex2d[] coordinates) {
		vertex = BufferUtils.createFloatBuffer(coordinates.length * 4);
		texture = BufferUtils.createFloatBuffer(coordinates.length * 3);
		vertcount = coordinates.length;
		randoms = new float[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			vertex.put(coordinates[i].x);
			vertex.put(coordinates[i].y);
			float temp = r.nextFloat();
			randoms[i] = temp;
			vertex.put(temp);
			
			vertex.put(i);
			texture.put(coordinates[i].u);
			texture.put(coordinates[i].v);
			texture.put(0);
		}
		vertid = GL15.glGenBuffers();
		texcoordid = GL15.glGenBuffers();
		vertex.rewind();
		texture.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texture, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}
	private float[] randoms;
	public void edit_point(Vertex2d[] coordinates,int[] created) {
		vertex = BufferUtils.createFloatBuffer(coordinates.length * 4);
		texture = BufferUtils.createFloatBuffer(coordinates.length * 3);

		for (int i = 0; i < coordinates.length; i++) {
			vertex.put(coordinates[i].x);
			vertex.put(coordinates[i].y);
			vertex.put(randoms[i]);
			vertex.put(i);
			
			texture.put(coordinates[i].u);
			texture.put(coordinates[i].v);
			texture.put(created[i]);
		}
		
		vertex.rewind();
		texture.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0,vertex);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0,texture);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}

	public void create(Progressive_buffer[] b) {// coordinates are NOT bound by
												// this

		vertex = b[0].get_data();
		texture = b[1].get_data();
		vertcount = b[0].get_data().limit();
		vertex.rewind();
		texture.rewind();
		vertid = GL15.glGenBuffers();
		texcoordid = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texture, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void create(int size) {// coordinates are NOT bound by this

		vertex = BufferUtils.createFloatBuffer(size);
		texture = BufferUtils.createFloatBuffer(size);
		vertcount = 0;
		vertex.rewind();
		texture.rewind();
		vertid = GL15.glGenBuffers();
		texcoordid = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texture, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}


	public void bind_texture(Texture t) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.t = t;
		this.texid = t.id;
	}

	public void edit_data(Vertex2d[] coordinates) {

		vertex = BufferUtils.createFloatBuffer(coordinates.length * 2);
		texture = BufferUtils.createFloatBuffer(coordinates.length * 2);
		vertcount = coordinates.length;
		for (int i = 0; i < coordinates.length; i++) {
			vertex.put(coordinates[i].x);
			vertex.put(coordinates[i].y);
			texture.put(coordinates[i].u);
			texture.put(coordinates[i].v);
		}
		vertex.rewind();
		texture.rewind();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertex);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, texture);// doesnt
																// reassing
																// meaning its
																// more
																// efficient,
																// only for
																// removing
																// objects
	}

	public void edit_data(Progressive_buffer[] b) {
		if (b[0].get_data() == null || b[1].get_data() == null) {
			return;
		}
		vertex.clear();
		texture.clear();
		vertex = BufferUtils.createFloatBuffer(b[0].get_data().capacity());
		texture = BufferUtils.createFloatBuffer(b[1].get_data().capacity());
		b[0].get_data().rewind();
		b[1].get_data().rewind();
		vertex = b[0].get_data();
		texture = b[1].get_data();
		vertcount = b[0].get_data().capacity();

		vertex.rewind();
		texture.rewind();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertex);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, texture);// doesnt
																// reassing
																// meaning its
																// more
																// efficient,
																// only for
																// removing
																// objects
	}

	public void clear_data() {
		vertex.clear();
		texture.clear();
		vertex = BufferUtils.createFloatBuffer(vertcount);
		texture = BufferUtils.createFloatBuffer(vertcount);
		vertex.rewind();
		texture.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertex);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, texture);
		vertcount = 0;

	}
	public void rebind(int size){
		reset();
		FloatBuffer temp = BufferUtils.createFloatBuffer(size);
		FloatBuffer temp_tex = BufferUtils.createFloatBuffer(size);
		vertex.rewind();
		texture.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vertid);//clear and generate two new memory locations with a large amount of space
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, temp, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vertid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, temp_tex, GL15.GL_DYNAMIC_DRAW);
		//reassign the previous data to this , useful for string generation.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertex);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texid);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0,texture);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}

	public void reset_count() {
		vertcount = 0;
	}

	public void dispose() {
		if (vertid != 0) {
			GL15.glDeleteBuffers(vertid);
			GL15.glDeleteBuffers(texcoordid);
		}
		if (shader != null) {
			shader.dispose();
		}
	}
	public void reset(){
		if (vertid != 0) {
			GL15.glDeleteBuffers(vertid);
			GL15.glDeleteBuffers(texcoordid);
		}
	}

	public void set_color(Vertex2d RGB) {
		color = RGB;
	}

	public void attach_shader(Shaderclass shader) {
		this.shader = shader;
	}

	public void translate(Vertex2d position) {
		transformation.x += position.x;
		transformation.y += position.y;

	}

	public void set_position(Vertex2d position) {
		transformation
				.setcoords(position.x, position.y, position.u, position.v);
		
	}

	public void rotate(float angle) {
		rotate = angle;
	}

	public void set_rot_pos(Vertex2d rotpos) {
		this.rotation_pos = rotpos;
	}

	public void set_zoom(float zoom) {
		transformation.u = zoom;

	}

	public void set_line(boolean line) {
		is_line = line;
	}

	public void increase_zoom(float zoom) {
		transformation.u += zoom;
	}

	public void set_bloom(boolean bloom) {
		if (bloom) {
			this.bloom = 1;
		} else {
			this.bloom = 0;
		}
	}

	public void set_time(float time) {
		this.time = time;
	}

	public void pause() {
		functional = !functional;
	}

	public void set_point() {
		point_vbo = true;
	}

	public void set_size(int i) {
		size = i * 2;
	}
	public void set_velocity(float v){
		velocity = v;
	}

	public void set_spawn(Vertex2d pos) {
		spawn = pos;
	}

	public void set_decay(float decay) {
		this.decay = decay;
	}

	public void set_alpha(float alpha) {
		this.alpha = alpha;
	}

	public float get_render_ticks() {
		return render_ticks;
	}

	public void toggle_tick() {
		tick = !tick;
	}
	public int get_time(){
		return (int)time;
	}

	public void render() {
	
		vertex.rewind();
		if (tick) {
			render_ticks += 1;
		}
		if(render_ticks % super_vbo.fps_mod() < 1){
			time+=1;
		}
		if (vertid != -1 && functional) {
			if (!point_vbo) {
				
				super_vbo.bind_shader();
				// GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glEnable(GL11.GL_BLEND);

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texid);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
				GL20.glEnableVertexAttribArray(super_vbo.locationattribvertex);
				GL20.glEnableVertexAttribArray(super_vbo.locationattribtex);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
				GL20.glVertexAttribPointer(super_vbo.locationattribvertex, 2,
						GL11.GL_FLOAT, false, 0, 0);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
				GL20.glVertexAttribPointer(super_vbo.locationattribtex, 2,
						GL11.GL_FLOAT, false, 0, 0);
				// Draw the textured rectangle
				GL20.glUniform2f(super_vbo.locationdimension, width, height);
				GL20.glUniform1f(super_vbo.locationzoom, transformation.u);
				GL20.glUniform2f(super_vbo.locationtransform, transformation.x,
						transformation.y);
				GL20.glUniform1f(super_vbo.locationrotate, rotate);
				GL20.glUniform1f(super_vbo.locationtime, time);

				GL20.glUniform2f(super_vbo.locationrotatepos, rotation_pos.x,
						rotation_pos.y);
				GL20.glUniform1i(super_vbo.locationbloom, bloom);
				if (is_line) {
					GL20.glUniform3f(super_vbo.location_color, color.x,
							color.y, color.u);
					GL11.glDrawArrays(GL11.GL_LINES, 0, vertcount);
				} else {
					GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertcount);
				}
				GL20.glDisableVertexAttribArray(super_vbo.locationattribvertex);
				GL20.glDisableVertexAttribArray(super_vbo.locationattribtex);

				GL11.glEnable(GL11.GL_BLEND);
				super_vbo.un_bind_shader();
			}
		} else {

			System.out.println("Error no bound vertid");
			return;
		}

		if (point_vbo) {
			
			super_vbo.bind_particle();
			// GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL11.glEnable(GL11.GL_BLEND);
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texid);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
			GL20.glEnableVertexAttribArray(super_vbo.particleattribvert);
			GL20.glEnableVertexAttribArray(super_vbo.particleattribtex);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertid);
			GL20.glVertexAttribPointer(super_vbo.particleattribvert, 4,
					GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texcoordid);
			GL20.glVertexAttribPointer(super_vbo.particleattribtex, 3,
					GL11.GL_FLOAT, false, 0, 0);
			// Draw the textured rectangle
			GL20.glUniform2f(super_vbo.particle_dimension, width, height);
			GL20.glUniform1f(super_vbo.particle_decay, decay);
			GL20.glUniform1f(super_vbo.particle_alpha, alpha);
			GL20.glUniform1f(super_vbo.particle_time, time);
			GL20.glUniform1f(super_vbo.particle_size, size);
			GL20.glUniform1f(super_vbo.particle_velocity, velocity);
			GL20.glUniform2f(super_vbo.particle_spawn, spawn.x,
					spawn.y);
			GL20.glUniform2f(super_vbo.particle_transform, transformation.x, transformation.y);

			GL11.glDrawArrays(GL11.GL_POINTS, 0, vertcount);

			GL20.glDisableVertexAttribArray(super_vbo.particleattribvert);
			GL20.glDisableVertexAttribArray(super_vbo.particleattribtex);

			GL11.glEnable(GL11.GL_BLEND);
			super_vbo.un_bind_particle();
		} else if (!functional) {

		}

	}

	public int get_vertcount() {
		return vertcount;
	}

}
