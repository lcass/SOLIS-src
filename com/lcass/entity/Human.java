package com.lcass.entity;

import com.lcass.core.Core;
import com.lcass.game.world.shiphandler;
import com.lcass.graphics.Vertex2d;

public class Human extends Entity{
	public Human(Core core,CrewHandler h,shiphandler ship,int id,int owner){
		super(core,h,ship,id,owner);
		sprite = new Vertex2d(0,0,16,16);
		position = new Vertex2d(0,0,0,0);
	}
	
}
