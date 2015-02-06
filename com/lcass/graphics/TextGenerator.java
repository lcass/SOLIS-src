package com.lcass.graphics;

import java.nio.FloatBuffer;

import com.lcass.core.Core;
import com.lcass.graphics.texture.Texture;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Progressive_buffer;

public class TextGenerator {
	private String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789. ";
	private spritesheet alphabetsprite;
	private Core core;
	public TextGenerator(Core core){
		this.core = core;
		alphabetsprite = new spritesheet("textures/font1.png");
	}
	public Progressive_buffer[] generate_text(String text , Vertex2d position,int size){
	
		if(text == null || text.length() == 0 || position == null){
			return null;
		}
		Progressive_buffer[] drawdata = new Progressive_buffer[2];
		drawdata[0] = new Progressive_buffer(null,false);
		drawdata[1] = new Progressive_buffer(null,true);
		for(int i = 0; i < text.length();i++){
			char charat = text.charAt(i);
			int code = alphabet.indexOf(charat);
			if(code == -1){
				continue;
			}
			
			Progressive_buffer[] tempdata = core.G.rectangle((int)(position.x + (i * size)) ,(int)(position.y),size,(int)(size * 1.5), alphabetsprite.getcoords(code * 6, 0, (code+1)*6, 8));
			
			
			drawdata[0].extend(tempdata[0]);
			drawdata[1].extend(tempdata[1]);
	
		
		
		}
		
		
		return drawdata;
	}
	public Texture gettexture(){
		return alphabetsprite.gettexture();
	}
	public spritesheet getsprite(){
		return alphabetsprite;
	}
}
