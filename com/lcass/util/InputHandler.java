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

	public boolean a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t,
			u, v, w, x, y, z, left, right, up, down, mouse1, mouse2, mouse3;
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
				if (Mouse.getEventButtonState()) {
					if (Mouse.getEventButton() == 0) {
						mouse1 = true;
					}
					if (Mouse.getEventButton() == 1) {
						mouse2 = true;
					}
					if (Mouse.getEventButton() == 2) {
						mouse3 = true;
					}
				} else {
					mouse1 = false;
					mouse2 = false;
					mouse3 = false;
				}
			}
		}
		a = Keyboard.isKeyDown(Keyboard.KEY_A);
		b = Keyboard.isKeyDown(Keyboard.KEY_B);
		c = Keyboard.isKeyDown(Keyboard.KEY_C);
		d = Keyboard.isKeyDown(Keyboard.KEY_D);
		e = Keyboard.isKeyDown(Keyboard.KEY_E);
		f = Keyboard.isKeyDown(Keyboard.KEY_F);
		g = Keyboard.isKeyDown(Keyboard.KEY_G);
		h = Keyboard.isKeyDown(Keyboard.KEY_H);
		i = Keyboard.isKeyDown(Keyboard.KEY_I);
		j = Keyboard.isKeyDown(Keyboard.KEY_J);
		k = Keyboard.isKeyDown(Keyboard.KEY_K);
		l = Keyboard.isKeyDown(Keyboard.KEY_L);
		m = Keyboard.isKeyDown(Keyboard.KEY_M);
		n = Keyboard.isKeyDown(Keyboard.KEY_N);
		o = Keyboard.isKeyDown(Keyboard.KEY_O);
		p = Keyboard.isKeyDown(Keyboard.KEY_P);
		q = Keyboard.isKeyDown(Keyboard.KEY_Q);
		r = Keyboard.isKeyDown(Keyboard.KEY_R);
		s = Keyboard.isKeyDown(Keyboard.KEY_S);
		t = Keyboard.isKeyDown(Keyboard.KEY_T);
		u = Keyboard.isKeyDown(Keyboard.KEY_U);
		v = Keyboard.isKeyDown(Keyboard.KEY_V);
		w = Keyboard.isKeyDown(Keyboard.KEY_W);
		x = Keyboard.isKeyDown(Keyboard.KEY_X);
		y = Keyboard.isKeyDown(Keyboard.KEY_Y);
		z = Keyboard.isKeyDown(Keyboard.KEY_Z);
		left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		up = Keyboard.isKeyDown(Keyboard.KEY_UP);
		down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);


	}
}
