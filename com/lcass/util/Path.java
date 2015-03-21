package com.lcass.util;

import java.util.ArrayList;

import com.lcass.graphics.Vertex2d;

public class Path {
	private ArrayList<Vertex2d> path = new ArrayList<Vertex2d>();
	private int position = 0;
	public Path(){
		
	}
	public void add_step(Vertex2d step){
		path.add(step);
	}
	public Vertex2d next(){
		if(position < path.size()){
			position += 1;
			return path.get(position - 1);
		}
		else{
			
			return null;
		}
	}

}
