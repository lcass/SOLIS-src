package com.lcass.util;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;

public class InputHandler {

	public boolean sa, sb, sc, sd, se, sf, sg, sh, si, sj, sk, sl, sm, sn, so,
			sp, sq, sr, ss, st, su, sv, sw, sx, sy, sz, sm1, sm2, sm3,sright,sleft,sup,sdown,sspace, escape;// true
																				// value

	private PointerInfo mouse;
	private Point mousepos;
	private Core core;
	private Vertex2d mouseposition = new Vertex2d(0, 0);

	public InputHandler(Core core) {
		this.core = core;

	}

	public Vertex2d obtain_mouse() {
		return mouseposition;
	}

	private boolean pressed = false;

	public void tick() {

		mouse = MouseInfo.getPointerInfo();
		mousepos = mouse.getLocation();
		mouseposition.x = (int) ((mousepos.getX() - Display.getX()) - 3);
		mouseposition.y = core.height
				- (int) ((mousepos.getY() - Display.getY()) - 32);
		
		while (Mouse.next()) {
			if (Mouse.getEventButton() != -1) {
				if(Mouse.getEventButtonState()){
					if(Mouse.getEventButton() == 0){
						sm1 = true;
					}
					if(Mouse.getEventButton() == 1){
						sm2 = true;
					}
					if(Mouse.getEventButton() == 2){
						sm3 = true;
					}
				}
				else{
					sm1 = false;
					sm2 = false;
					sm3 = false;
				}
			}
		}
		
		sleft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		sright = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		sup = Keyboard.isKeyDown(Keyboard.KEY_UP);
		sdown = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		sspace = Keyboard.isKeyDown(Keyboard.KEY_SPACE );
		sa = Keyboard.isKeyDown(Keyboard.KEY_A);
		sb = Keyboard.isKeyDown(Keyboard.KEY_B);
		sc = Keyboard.isKeyDown(Keyboard.KEY_C);
		sd = Keyboard.isKeyDown(Keyboard.KEY_D);
		se = Keyboard.isKeyDown(Keyboard.KEY_E);
		sf = Keyboard.isKeyDown(Keyboard.KEY_F);
		sg = Keyboard.isKeyDown(Keyboard.KEY_G);
		sh = Keyboard.isKeyDown(Keyboard.KEY_H);
		si = Keyboard.isKeyDown(Keyboard.KEY_I);
		sj = Keyboard.isKeyDown(Keyboard.KEY_J);
		sk = Keyboard.isKeyDown(Keyboard.KEY_K);
		sl = Keyboard.isKeyDown(Keyboard.KEY_L);
		sm = Keyboard.isKeyDown(Keyboard.KEY_M);
		sn = Keyboard.isKeyDown(Keyboard.KEY_N);
		so = Keyboard.isKeyDown(Keyboard.KEY_O);
		sp = Keyboard.isKeyDown(Keyboard.KEY_P);
		sq = Keyboard.isKeyDown(Keyboard.KEY_Q);
		sr = Keyboard.isKeyDown(Keyboard.KEY_R);
		ss = Keyboard.isKeyDown(Keyboard.KEY_S);
		sy = Keyboard.isKeyDown(Keyboard.KEY_T);
		su = Keyboard.isKeyDown(Keyboard.KEY_U);
		sv = Keyboard.isKeyDown(Keyboard.KEY_V);
		sw = Keyboard.isKeyDown(Keyboard.KEY_W);
		sx = Keyboard.isKeyDown(Keyboard.KEY_X);
		sy = Keyboard.isKeyDown(Keyboard.KEY_Y);
		sz = Keyboard.isKeyDown(Keyboard.KEY_Z);

		escape = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);

	}

}
