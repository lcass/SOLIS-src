package com.lcass.graphics;

public class Vertex2d {//2d for texture and for vert coords
	public float x,y,u,v;
	public Vertex2d(float x, float y){
		this.x = x;
		this.y = y;
		u = 0;
		v = 0;
	}
	public Vertex2d(float x,float y , float u , float v){
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	public Vertex2d(int x, int y){
		this.x = x;
		this.y = y;
		u = 0;
		v = 0;
	}
	public Vertex2d(int x, int y, int u , int v){
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	public void setcoords(float x, float y, float u , float v){
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	public Vertex2d add(Vertex2d ad){
		this.x += ad.x;
		this.y += ad.y;
		this.u += ad.u;
		this.v += ad.v;
		return this;
	}
	public Vertex2d sub(Vertex2d ad){
		this.x -= ad.x;
		this.y -= ad.y;
		this.u -= ad.u;
		this.v -= ad.v;
		return this;
	}
	public void mult(Vertex2d ad){
		
	}
	public void mult(float a){
		this.x *= a;
		this.y *= a;
		this.u *= a;
		this.v *= a;
	}
	public void div(Vertex2d ad){
		
	}
}
