package com.lcass.graphics;

import com.lcass.util.Encapsulated_method;
import com.lcass.util.Progressive_buffer;

public class Button{
	private Vertex2d position;
	private GUI gui;
	private boolean check,input,actual,laststate = true;
	private Progressive_buffer[] active,inactive;
	private Encapsulated_method call;
	private boolean isactive = false,textured = false;
	public Button(Vertex2d position,boolean textured, GUI gui,Encapsulated_method callfunction){
		this.position = position;
		this.gui = gui;
		call = callfunction;
		this.textured = textured;
		if(textured){
			inactive = gui.graph.rectangle((int)position.x, (int)position.y, 128, 26, gui.button_sheet.getcoords(1, 1, 129, 27));
			active = gui.graph.rectangle((int)position.x, (int)position.y, 142, 26, gui.button_sheet.getcoords(1, 31, 143, 57));
		}
	}
	
	public void throw_at_GUI(){
		if(!textured){
			return;
		}
		if(isactive){
			gui.bind(active);
		}
		else{
			gui.bind(inactive);
		}
	}
	public void tick(){

		input = gui.ih.mouse1;
		
		if(input){
			if(check){
				actual = false;
			}
			if(!check){
				check = true;
				actual = true;
			}
		}
		else{
			check = false;
		}
		if(!input){//failsafe check
			actual = false;
			check = false;
		}
		
		float x = gui.ih.obtain_mouse().x;
		float y = gui.ih.obtain_mouse().y;
		
		
		if(x >= position.x && x <= position.u && y >= position.y && y <= position.v){
			isactive = true;
			
			if(actual){
				if(call != null){
					System.out.println("called");
					call.call();
				}
				
			}
		}
		else{
			isactive = false;
		}
		if(isactive != laststate){
			laststate = isactive;
			gui.update();
		}
		
		
	}
	public void set_instance(Object a){
		call.set_instance(a);
	}
	
}
