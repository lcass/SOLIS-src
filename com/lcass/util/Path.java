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
	public void invert(){
		Vertex2d[] finished = new Vertex2d[path.size()];
		for(int i =  path.size();i > 0;i --){
			finished[path.size() - i] = path.get(i- 1);
		}
		path.clear();
		for(int i = 0; i < finished.length;i++){
			path.add(finished[i]);
		}
	}

}
