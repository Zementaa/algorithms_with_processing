package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.RecursivLSystemTree;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;

/**
 * @author bwinzen
 *
 */
public class LSystemSimulation extends PApplet {

	public void settings() {
		size(1000, 1000, FX2D);
	}

	private RecursivLSystemTree tree = SimpleLSystems.createOrderedChaos(1, (float) (Math.PI/6));
//	public void draw() {
//		background(64);
//		ellipse(mouseX, mouseY, 50, 50);
//	}
	@Override
	public void setup() {
		  background(51);
	}
	@Override
	public void mouseClicked(MouseEvent event) {
//		System.out.println("Click");
		  background(51);
//		  System.out.println(tree.getLSystem());
		  PGraphics graphics = this.createGraphics(width, height);
		  graphics.beginDraw();
		  graphics.translate(width/2, height);
		  graphics.stroke(255, 100);
		  tree.drawAndNext(new CooperationContext(this, 0, null, null),graphics);
		  graphics.endDraw();
		  this.image(graphics, 0, 0);
		  
   }

	public static void main(String[] args) {
		String[] processingArgs = { "LSystem" };
		LSystemSimulation mySketch = new LSystemSimulation();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
