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
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() != -1) {
				if (Keyboard.getEventCharacter() == 'a') {
					a = true;
				}
				if (Keyboard.getEventCharacter() == 'b') {
					b = true;
				}
				if (Keyboard.getEventCharacter() == 'c') {
					c = true;
				}
				if (Keyboard.getEventCharacter() == 'd') {
					d = true;

					if (Keyboard.getEventCharacter() == 'e') {
						e = true;
					}
					if (Keyboard.getEventCharacter() == 'f') {
						f = true;
					}
					if (Keyboard.getEventCharacter() == 'g') {
						g = true;
					}
					if (Keyboard.getEventCharacter() == 'h') {
						h = true;
					}
					if (Keyboard.getEventCharacter() == 'i') {
						i = true;
					}
					if (Keyboard.getEventCharacter() == 'j') {
						j = true;
					}
					if (Keyboard.getEventCharacter() == 'k') {
						k = true;
					}
					if (Keyboard.getEventCharacter() == 'l') {
						l = true;
					}
					if (Keyboard.getEventCharacter() == 'm') {
						m = true;
					}

					if (Keyboard.getEventCharacter() == 'n') {
						n = true;
					}
					if (Keyboard.getEventCharacter() == 'o') {
						o = true;
					}
					if (Keyboard.getEventCharacter() == 'p') {
						p = true;
					}
					if (Keyboard.getEventCharacter() == 'q') {
						q = true;
					}
					if (Keyboard.getEventCharacter() == 'r') {
						r = true;
					}
					if (Keyboard.getEventCharacter() == 's') {
						s = true;
					}
					if (Keyboard.getEventCharacter() == 't') {
						t = true;
					}
					if (Keyboard.getEventCharacter() == 'u') {
						u = true;
					}
					if (Keyboard.getEventCharacter() == 'v') {
						v = true;
					}
					if (Keyboard.getEventCharacter() == 'w') {
						w = true;
					}
					if (Keyboard.getEventCharacter() == 'x') {
						x = true;
					}
					if (Keyboard.getEventCharacter() == 'y') {
						y = true;
					}
					if (Keyboard.getEventCharacter() == 'z') {
						z = true;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
						left = true;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
						right = true;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
						up = true;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
						down = true;
					} else {
						a = false;
						b = false;
						c = false;
						d = false;
						e = false;
						f = false;
						g = false;
						h = false;
						i = false;
						j = false;
						k = false;
						l = false;
						m = false;
						n = false;
						o = false;
						p = false;
						q = false;
						r = false;
						s = false;
						t = false;
						u = false;
						v = false;
						w = false;
						x = false;
						y = false;
						z = false;
						left = false;
						up = false;
						down = false;
						right = false;

					}
				}

			}

		}

	}
}
