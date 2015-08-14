package com.lcass.game.Items.construction;

import com.lcass.game.Items.Item;
import com.lcass.game.Items.Metal;
import com.lcass.game.tiles.Wall;

public class wall extends Construction{
	public wall(){
		super(null , 60,"wall",new Wall());
		items = new Item[1];
		items[0] = new Metal(null,null,5);
	}
}
