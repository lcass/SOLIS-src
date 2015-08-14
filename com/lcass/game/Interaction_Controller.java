package com.lcass.game;

import com.lcass.core.Core;
import com.lcass.game.tiles.Frame;
import com.lcass.game.tiles.Sub_Tile;
import com.lcass.game.tiles.Tile;
import com.lcass.game.world.shiphandler;
import com.lcass.graphics.Vertex2d;

public class Interaction_Controller {//handles GUI stuff and entity based things
	private Core core;
	private Game game;
	private shiphandler ships;
	public Interaction_Controller(Core core , Game game , shiphandler ships){
		this.core = core;
		this.game = game;
		this.ships = ships;
		if(core == null || game == null || ships == null){
			if(core != null){
				System.out.println("Error setting up interaction controller");
				core.G.kill();
			}
			else{
				System.out.println("fatal error , shut this down while possible");
			}
		}
	}
	public void tick(){
		Vertex2d camera = game.camera;
		Vertex2d mousepos = core.ih.obtain_mouse();
		Vertex2d actual = new Vertex2d(
				(int) (((mousepos.x - (camera.x)) / (32))),
				(int) ((mousepos.y - ((camera.y + com.lcass.core.DEFINES.Y_ADJUST_MOUSE))) / (32)));
		Vertex2d mouse_tile = actual;
		if(ships.coreship != null){
			mouse_tile = actual.whole().mult(32).add(ships.coreship.correct_pos);
		}
		if (!on_gui(core.ih.obtain_mouse()) && game.docked) {

			if (core.ih.mouse1) {

				if (mousepos.x - camera.x < 0 || mousepos.y - camera.y < 0) {
					actual.x = -1;
					actual.y = -1;
				}

				if (!game.world.within_map(actual)) {

				} else if (game.inventory.get_selectedposition() == 4) {
					if (!game.world.check_tile(actual, game.inventory.selected)) {

						if (!game.inventory.selected.is_sub()) {
							if (game.inventory.take_selected(1)) {
								Tile temp = game.world.getnew(game.inventory.selected);
								temp.init(core);
								temp.setpos((int) actual.x, (int) actual.y);
								temp.set_dir(game.tile_dir);
								temp.set_world(game.world);
								if (game.world.check_tile(actual)) {

									Tile check_temp = game.world.get_tile(actual);
									game.world.remove_tile(actual);
									game.world.add_tile(temp, check_temp.get_index());

									game.inventory.add_item(check_temp, 1);// readd
																		// the
																		// taken
																		// file
																		// back
																		// into
																		// the
																		// game.inventory;
								} else {

									game.world.add_tile(temp);
								}
							}
						} else if (game.world.get_tile(actual) != null) {

							if (game.inventory.take_selected(1)) {

								Sub_Tile temp = game.world
										.getnew((Sub_Tile) game.inventory.selected);
								if (game.world.get_tile(actual).get_sub() == null) {
									temp.init(core);
									temp.set_super(game.world.get_tile(actual));
									temp.set_dir(game.tile_dir);
									if (!game.world.set_sub(temp, actual)) {
										game.inventory.add_item(game.inventory.selected,
												1);
									}
								} else {
									temp.init(core);
									temp.set_super(game.world.get_tile(actual));
									temp.set_dir(game.tile_dir);
									if (!game.world.set_sub_sub(temp, actual)) {
										game.inventory.add_item(game.inventory.selected,
												1);
									}
								}

							}

						}
					}

				} else if (game.inventory.get_selectedposition() == 3) {
					if (game.world.check_tile(actual) && game.world.within_map(actual)) {

						game.inventory.add_item(game.world.get_tile(actual), 1);
						game.world.remove_tile(actual);

					}
				} else if (game.inventory.get_selectedposition() == 5) {
					game.Entities.get_crew_id(game.selected_entity.get_id()).move_to_loc(
							actual.whole().mult(32));
				}
			}
		}
		// other buttons
		if (game.inventory.get_selectedposition() == 0) {
			if (game.inventory.get_lastselected() == 0
					|| game.inventory.get_lastselected() == 6) {
				game.inventory.set_selectedposition(1);
			} else {
				game.inventory.set_selectedposition(game.inventory.get_lastselected());

			}
			if (game.docked) {

				game.world.rotate(0);
				ships.generate_ships(game.world.get_map(), game.world.mapwidth);
				game.inventory.set_weapon(true);
				game.update_weps();

			} else {
				game.world = ships.coreship.get_world();
				game.world.reset();
				game.world.set_world(game.world);
				game.world.rotate(0);
				game.inventory.set_weapon(false);
			}
			game.docked = !game.docked;
			game.tile_dir = 0;

		}
		if (game.inventory.get_selectedposition() == 6) {
			if (game.inventory.get_lastselected() == 0
					|| game.inventory.get_lastselected() == 6) {
				game.inventory.set_selectedposition(1);
			} else {
				game.inventory.set_selectedposition(game.inventory.get_lastselected());
			}
			game.tile_dir += 1;
			if (game.tile_dir > 3) {
				game.tile_dir = 0;
			}
			game.inventory.selected.set_dir(game.tile_dir);
			game.inventory.force_update();
		}
		if (!game.docked) {
			game.handle_ships();
		}
		
		
		

		if (!on_gui(core.ih.obtain_mouse()) && !game.docked) {

			if (core.ih.mouse1) {

				if (mousepos.x - camera.x < 0 || mousepos.y - camera.y < 0) {
					actual.x = -1;
					actual.y = -1;
				}
				
				if (game.inventory.get_selectedposition() == 4) {
					//build stuff
				}

				if (game.inventory.get_selectedposition() == 5) {
					if (game.Entities.get_crew(mouse_tile.whole().div(32).ceil(), -2) == null
							&& game.selected_entity != null) {
						game.selected_entity.set_selected(false);
						game.selected_entity = null;
					}
					

					if (game.Entities.get_crew(mouse_tile.whole().div(32).ceil(), -2) != null) {
						if (game.selected_entity != null) {
							game.selected_entity.set_selected(false);
							game.selected_entity = null;
						}
						if (game.Entities.get_crew(mouse_tile.whole().div(32).ceil(), -2).get_owner() == -2) {
							game.selected_entity = game.Entities.get_crew(mouse_tile.whole().div(32).ceil(), -2);
							game.selected_entity.set_selected(true);
							game.selected_entity.select();
						}
					}
				}

			}
			
			if (core.ih.mouse2) {
				if (game.inventory.get_selectedposition() == 1) {
					game.interaction.set_translation(mousepos);
					game.interaction_pos = actual.whole().mult(32);
					game.render_interaction = true;
				}
				if (game.inventory.get_selectedposition() == 5) {
					if (game.selected_entity != null) {
						game.selected_entity.move_to_loc(actual.whole().mult(32));
					}

				} else if (game.selected_entity != null) {

				}
			}
			if(game.render_interaction){
				if(!game.interaction.on_gui(mousepos)){
					game.render_interaction = false;
				}
			}
		}
		
		if (!on_gui(core.ih.obtain_mouse()) && !game.docked) {

			if (core.ih.mouse1) {

				if (game.inventory.get_selectedposition() == 4
						&& game.selected_entity == null) {
					weaponset weapons = game.inventory.wep_storage[game.inventory
							.get_selected_wep()];
					if (weapons.type != null) {
						Tile[] shootable = weapons.weapons;
						for (int i = 0; i < shootable.length; i++) {
							if (shootable[i] != null) {
								Vertex2d move = ships.coreship.ph
										.calc_step(
												shootable[i].get_world_pos(),
												mouse_tile.whole()
										);
								
								
								ships.coreship.ship.get_tile(
										shootable[i].get_pos()).set_movement(
										move);
								ships.coreship.ship.get_tile(
										shootable[i].get_pos()).fire();
							}
						}
					}
				}
			}
		}
		//selected entity stuff for the ITEM handling
		
	}
	public boolean on_gui(Vertex2d mousepos) {
		if (mousepos.x > core.width - 100 && mousepos.y > core.height - 588) {
			return true;
		}
		return false;
	}
	public void render(){
		
	}
}
