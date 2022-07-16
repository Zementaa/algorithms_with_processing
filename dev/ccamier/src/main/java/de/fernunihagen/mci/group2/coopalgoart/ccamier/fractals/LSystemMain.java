package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

import processing.core.PApplet;
import processing.core.PGraphics;

/*
 * Alphabet
 * Axiom
 * Ruleset
 */
public class LSystemMain extends PApplet {

	LSystem lsys;
	Artist artist;

	PGraphics pg = new PGraphics();

	@Override
	public void settings() {
		size(600, 600);

	}

	@Override
	public void setup() {

		// Rule[] ruleset = new Rule[2];
		// Fill with two rules (These are rules for the Sierpinksi Gasket Triangle)
		// ruleset[0] = new Rule('F',"F--F--F--G");
		// ruleset[1] = new Rule('G',"GG");
		// Create LSystem with axiom and ruleset
		// this.lsys = new LSystem("F--F--F",ruleset);
		// this.artist = new Artist(lsys.getSentence(),width*2,TWO_PI/3);

		// Rule[] ruleset = new Rule[1];
		// ruleset[0] = new Rule('F',"F[F]-F+F[--F]+F-F");
		// ruleset[0] = new Rule['F',"FF+[+F-F-F]-[-F+F+F]");
		// this.lsys = new LSystem("F-F-F-F",ruleset);
		// this.artist = new Artist(lsys.getSentence(),width-1,PI/2);

		Rule[] ruleset = new Rule[2];
		ruleset[0] = new Rule('1', "11");
		ruleset[1] = new Rule('0', "1[0]0");
		this.lsys = new LSystem("0", ruleset);

		// Rule[] ruleset = new Rule[1];
		// ruleset[0] = new Rule('F', "FF+[+F-F-F]-[-F+F+F]");
		// this.lsys = new LSystem("F", ruleset);
		this.artist = new Artist(height / 3, radians(25));
	}

	@Override
	public void draw() {
		background(255);
		fill(0);
		// text("Click mouse to generate", 10, height-10);

		translate(width / 2, height);
		rotate(-PI / 2);
		this.artist.render(pg);
		noLoop();
	}

	int counter = 0;

	@Override
	public void mousePressed() {
		if (counter < 5) {
			pushMatrix();
			this.lsys.generate();
			// println(this.lsys.getSentence());
			this.artist.setToDo(lsys.getSentence());
			this.artist.changeLen(0.5f);
			popMatrix();
			redraw();
			counter++;
		}
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		LSystemMain mySketch = new LSystemMain();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
