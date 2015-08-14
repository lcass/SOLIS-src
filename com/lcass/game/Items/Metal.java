package com.lcass.game.Items;

import com.lcass.core.Core;
import com.lcass.entity.CrewHandler;
import com.lcass.entity.Entity;
import com.lcass.graphics.Vertex2d;

public class Metal extends Item{
	
	public Metal(Core core, CrewHandler owner,int stack) {
		
		super(core,owner,stack,100);
		sprite = new Vertex2d(0,0,32,32);
		name = "metal";
		
	}
	public void Interact(Entity e){
		
	}

}
