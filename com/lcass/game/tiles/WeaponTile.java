package com.lcass.game.tiles;

import com.lcass.core.Core;
import com.lcass.entity.Bullet;
import com.lcass.entity.Projectile;
import com.lcass.game.world.world;
import com.lcass.graphics.Vertex2d;

public abstract class WeaponTile extends Tile{
	protected Vertex2d proj_mov = new Vertex2d(0,0,0,0);
	
	public WeaponTile(){
		is_weapon = true;
	}
	public void init(Vertex2d position, Core core,world world){
		
		this.selectable = true;
		is_weapon = true;
	}
	public WeaponTile(Vertex2d position, Core core,world world){
		super(position,core,world);
		this.selectable = true;
	}
	public void tick(){
		
	}
	public void set_movement(Vertex2d mov){
		this.proj_mov = mov;
	}
	public void fire(){//override thisn
		Projectile round = new Bullet(core);
		round.set_move(proj_mov);
		round.set_position(get_world_pos().whole());
		round.set_handler(core.game.ships.get_ship(ship).ph);
		core.game.ships.get_ship(ship).ph.add(round);
		
	}
	
	
}
