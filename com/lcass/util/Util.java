package com.lcass.util;


import java.lang.reflect.Array;

import com.lcass.game.tiles.Tile;
import com.lcass.graphics.Vertex2d;

public class Util {
	public static Vertex2d tex_coordinate(int xin,int yin){
		return new Vertex2d(xin * 16,yin * 16,(xin+ 1) * 16,(yin+1) * 16);
	} 
	public static Tile[] cast_tile(Object[] in){
		Tile[] temp = new Tile[in.length];
		for(int i = 0; i < temp.length;i++){
			if(in[i] != null){
				temp[i] = (Tile)in[i];
			}
		}
		return temp;
	}
	public static Vertex2d[] rotate(Vertex2d[] data, Vertex2d centre,float angle){
		Vertex2d[] out = new Vertex2d[data.length];
		for(int i = 0;i < data.length;i++){
			out[i] = rotate(data[i],centre,angle);
		}
		return out;
	}
	public static Vertex2d rotate(Vertex2d target,Vertex2d rotation_point,float angle){
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		float nx = target.x - rotation_point.x;
		float ny = target.y - rotation_point.y;
		float nu = target.u - rotation_point.x;
		float nv = target.v - rotation_point.y;
		
		return new Vertex2d(((nx * cos) - (ny * sin)) + rotation_point.x,((nx * sin) + (ny * cos))+ rotation_point.y,((nu * cos) - (nv * sin))+rotation_point.x,((nu * sin) + (nv *cos)) + rotation_point.y);
	}
	public static boolean adjacent(Vertex2d position_in , Vertex2d target){
		System.out.println(position_in.x);
		System.out.println(target.x);
		Vertex2d position = position_in.whole();
		position.sub(new Vertex2d(position.x % 32, position.y %32));
		if(position.equals(target)){
			return true;
		}
		if(position.whole().add(new Vertex2d(32,0,0,0)).equals(target)){
			return true;
		}
		if(position.whole().add(new Vertex2d(-32,0,0,0)).equals(target)){
			return true;
		}
		if(position.whole().add(new Vertex2d(0,32,0,0)).equals(target)){
			return true;
		}
		if(position.whole().add(new Vertex2d(0,-32,0,0)).equals(target)){
			return true;
		}
		return false;
	}
	
	
}
