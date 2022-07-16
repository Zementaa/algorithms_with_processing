package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

import processing.core.PGraphics;

public class Artist {

	String todo;
	float len;
	float theta;

	public Artist(float l, double t) {
		this.len = l;
		this.theta = (float) t;
	}

	public void render(PGraphics pg) {

		pg.stroke(0, 175);
		for (int i = 0; i < this.todo.length(); i++) {
			char c = this.todo.charAt(i);
			if (c == 'F' || c == 'G') {
				pg.line(0, 0, this.len, 0);
				pg.translate(this.len, 0);
			} else if (c == '+') {
				pg.rotate(this.theta);
			} else if (c == '-') {
				pg.rotate(-this.theta);
			} else if (c == '[') {
				pg.pushMatrix();
			} else if (c == ']') {
				pg.popMatrix();
			}
		}
	}

	void setLen(float l) {
		this.len = l;
	}

	void changeLen(float percent) {
		this.len *= percent;
	}

	void setToDo(String s) {
		this.todo = s;
	}
}
