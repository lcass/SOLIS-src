package com.lcass.game.Items;

import com.lcass.core.Core;
import com.lcass.entity.CrewHandler;

public abstract class weapon extends Item{

	public weapon(Core core, CrewHandler owner, int ownerid) {
		super(core, owner,1,1);
		max_stack = 1;
		name = "weapon";
		// TODO Auto-generated constructor stub
	}

}
