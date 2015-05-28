package com.lcass.game;

import com.lcass.game.tiles.Tile;

public class weaponset {
	public Tile[] weapons;
	public Tile type;
	public int size = 0;
	public weaponset(Tile type,int size){
		this.type = type;
		weapons = new Tile[100];
		this.size = size;
	}
	public void add_weapon(Tile t){
		for(int i = 0; i < weapons.length;i++){
			if(weapons[i] == null){
				weapons[i] = t;
				break;
			}
		}
	}
	public void clear(){
		weapons = new Tile[size];
	}
}
