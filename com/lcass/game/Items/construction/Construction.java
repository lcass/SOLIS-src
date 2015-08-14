package com.lcass.game.Items.construction;

import com.lcass.game.Items.Item;
import com.lcass.game.tiles.Tile;

public abstract class Construction {
	protected Item[] items;
	protected int time = 1;
	protected String name = "null";
	protected Tile result;

	public Construction(Item[] items, int time, String name, Tile result) {
		this.items = items;
		this.time = time;
		this.name = name;
		this.result = result;
	}

	public boolean contains(Item a) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].get_name() == a.get_name()) {
				return true;
			}
		}
		return false;
	}

	public int get_time() {
		return time;
	}

	public Item[] get_items() {
		return items;
	}

	public int[] get_amount() {
		if (items == null) {
			return null;
		}
		int[] amounts = new int[items.length];
		int position = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				amounts[position] = items[i].get_stack();
				position++;
			}
		}
		return amounts;
	}

	public boolean check(Item[] held) {
		if (items == null) {
			return false;
		}
		if (held == null) {
			return false;
		}
		int items_present = 0;
		boolean[] checked = new boolean[held.length];
		for(int i = 0; i < checked.length;i++){
			checked[i] = true;
		}
		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < checked.length; j++) {
					if (held[j] != null) {
						if (items[i].get_name() == held[j].get_name()) {
							if (items[i].get_stack() > held[j].get_stack()) {
								checked[j] = false;
								System.out.println("item amount invalid");
							}
							items_present ++;
							System.out.println("Adding items");
						}
					
				}
			}
		}
		
		for (int i = 0; i < checked.length; i++) {
			if (!checked[i]) {
				return false;
			}
			
		}
		if(items_present < items.length){
			return false;
		}
		return true;
	}

	public Tile get_result() {
		return result;
	}

	public String get_name() {
		return name;
	}
}
