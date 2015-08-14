package com.lcass.game.Items.construction;

import com.lcass.game.Items.Item;

public class Construction_handler {

	public Construction[] recipes;

	private int[] ticks = new int[100];// handling construction timers easily

	public Construction_handler() {

		recipes = new Construction[1000];
		for (int i = 0; i < ticks.length; i++) {
			ticks[i] = -1;
		}
	}

	public void add_recipe(Construction recipe) {
		for (int i = 0; i < recipes.length; i++) {
			if (recipes[i] == null) {
				recipes[i] = recipe;
				return;
			}
		}
	}

	public void tick() {
		for (int i = 0; i < ticks.length; i++) {
			if (ticks[i] != -1) {
				ticks[i]++;
			}
		}
	}

	public int setup_tick() {
		for (int i = 0; i < ticks.length; i++) {
			if (ticks[i] == -1) {
				ticks[i] = 0;
				return i;
			}
		}
		return -1;
	}

	public Construction[] get_recipes(Item a) {// recipes are retreived through
												// this function , quicker and
												// cleaner to recieve them all
		Construction[] data = new Construction[recipes.length];
		int data_pos = 0;
		for (int i = 0; i < recipes.length; i++) {
			if (recipes[i] != null) {
				if (recipes[i].contains(a)) {
					data[data_pos] = recipes[i];
					data_pos++;
				}
			}
		}
		Construction[] return_data = new Construction[data_pos - 1];// easily
																	// handled
																	// by the
																	// object
																	// instead
																	// of having
																	// to check
		for (int i = 0; i < return_data.length; i++) {
			return_data[i] = data[i];
		}
		return return_data;
	}

	public Construction[] check_recipes(Item[] a) {// recipes are retreived
													// through this function ,
													// quicker and cleaner to
													// recieve them all
		Construction[] data = new Construction[recipes.length];
		int data_pos = 0;
		for (int i = 0; i < recipes.length; i++) {
			if(recipes[i] != null){
			if (recipes[i].check(a)) {
				data[data_pos] = recipes[i];
				data_pos++;
			}
			}
		}
		Construction[] return_data = new Construction[data_pos];// easily
																	// handled
																	// by the
																	// object
																	// instead
																	// of having
																	// to check
		for (int i = 0; i < return_data.length; i++) {
			return_data[i] = data[i];
		}
		return return_data;
	}

	public void kill_tick(int id) {
		ticks[id] = -1;
	}

	public int check_tick(int id) {
		return ticks[id];
	}

	public Construction get_recipe(String name) {
		for (int i = 0; i < recipes.length; i++) {
			if (recipes[i] != null) {
				System.out.println(recipes[i].get_name() + " " + name);
				if (recipes[i].get_name() == name) {
					return recipes[i];
				}
			}
		}
		return null;
	}
}
