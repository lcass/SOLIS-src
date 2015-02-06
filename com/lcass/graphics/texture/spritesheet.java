package com.lcass.graphics.texture;

import org.lwjgl.opengl.GL11;

public class spritesheet {
	private Texture tex;
	private float x,y,ex,ey,width,height;
	public spritesheet(String location){
		tex = Texture.loadTexture(location);
		this.width = tex.width;
		this.height = tex.height;
	}
	public spritecomponent getcoords(int x, int y, int ex,int ey){
		spritecomponent temp= new spritecomponent();
		this.x = x/width;
		this.y = y/height;
		this.ex = ex/width;
		this.ey = ey/height;
		temp.set(this.x, this.y, this.ex, this.ey, tex);
		return temp;
	}
	public Texture gettexture(){
		return tex;
	}
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.id);
	}
	public void unbind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
