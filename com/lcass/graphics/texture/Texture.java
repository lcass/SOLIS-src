package com.lcass.graphics.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
//Many thanks to SHC for the tutorial on this
public class Texture {
	public int id;

	public int width;

	public int height;

	public ByteBuffer buffer;
	private Texture(int id, int width, int height, ByteBuffer buffer) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	public Texture(){
		
	}

	public static Texture loadTexture(String name) {

		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(Texture.class.getClassLoader()
					.getResourceAsStream(name));
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println("Texture loading failure! " + name);

		}

		int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
		bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0,
				bimg.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(bimg.getWidth()
				* bimg.getHeight() * 4);
		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {
				if (pixels[y * bimg.getWidth() + x] != -65281
						) {
					int pixel = pixels[y * bimg.getWidth() + x];

					buffer.put((byte) ((pixel >> 16) & 0xFF));

					buffer.put((byte) ((pixel >> 8) & 0xFF));

					buffer.put((byte) (pixel & 0xFF));

					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
				else if(pixels[y * bimg.getWidth() + x] != -1){
					buffer.put((byte)0);
					buffer.put((byte)0);
					buffer.put((byte)0);
					buffer.put((byte)0);
				}
				else{
					buffer.put((byte)-1);
					buffer.put((byte)-1);
					buffer.put((byte)-1);
					buffer.put((byte)-1);
				}
			}
		}
		buffer.flip();
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(),
				bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		return new Texture(textureID, bimg.getWidth(), bimg.getHeight(),buffer);
	}
	public void bindtex(int textureID,int width , int height ,ByteBuffer buffer){
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width,
				height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
}
