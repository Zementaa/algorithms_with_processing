package de.fernunihagen.mci.group2.coopalgoart.shopf;

import processing.core.PApplet;

public class Test extends PApplet {
	
	public void settings(){
		size(500, 500);
	}
	
	public void draw(){
		ellipse(mouseX, mouseY, 50, 50);
	}
	
	public void mousePressed(){
		background(64);
	}
	
	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Test mySketch = new Test();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
