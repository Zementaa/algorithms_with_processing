package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * @author bwinzen
 *
 */
public class ColorBallsStarter2{
	
	public static final long maxTimeSec = 30;
	private static final String PSurfaceFX = null;
	private ArrayList<Ball> balls = new ArrayList<>();
	public void settings(){
	}
	
	/**
	 * Redraw every ~10ms -30ms 
	 */
	public void draw(){
//		PSurfaceFX surface2 = (PSurfaceFX) getSurface();
//		ResizableCanvas c = (ResizableCanvas)surface2.getNative();
		LinkedList<Ball> removeList = new LinkedList<>();
		long currentTime = System.currentTimeMillis();
		for(Ball b : balls){
			b.step();
			b.render(currentTime);
			if(b.bornAt<currentTime-maxTimeSec*1000) {
				removeList.add(b);
			}
		}
		balls.removeAll(removeList);
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};

		
		
		PApplet pApplet = new PApplet() {
			@Override
			public void settings() {
				size(1000, 1000, JAVA2D);
			}
			
			public void draw() {
				PGraphics pg  = createGraphics(width, height);
				 pg = createGraphics(width, height);
				 pg.setParent(this);
				pg.beginDraw();
				pg.background(0xFF000000);
				pg.fill(0xFF000000| 120);
				pg.ellipse(50, 50, 10, 10);
				pg.endDraw();
				PGraphics pg2  = createGraphics(width, height);
				pg2.beginDraw();
				pg2.image(pg,0, 0);
				pg2.endDraw();
				image(pg2,0, 0);
			}
		};
		PApplet.runSketch(processingArgs, pApplet);
	}
	
	private static class Ball {
		
		private PApplet sketch;
		
		private float x;
		private float y;
		private float size;
		private float xSpeed;
		private float ySpeed;
		private int color;
		private long bornAt = System.currentTimeMillis();
		
		public Ball(PApplet sketch, float x, float y){
			this.sketch = sketch;
			this.x = x;
			this.y = y;
			this.size = sketch.random(10, 100);
			this.xSpeed = sketch.random(-10, 10);
			this.ySpeed = sketch.random(-10, 10);
			this.color = (int)(Math.random()*Integer.MAX_VALUE);
		}
		
		public void step(){
			x += xSpeed;
			if(x < 0 || x > sketch.width){
				xSpeed *= -1;
			}
			
			y += ySpeed;
			if(y < 0 || y > sketch.height){
				ySpeed *= -1;
			}
		}
		
		public void render(long currentTime){
			sketch.fill(this.color & 0xFF, (this.color>>8) & 0xFF, (this.color>>16) & 0xFF);
			sketch.ellipse(x, y, size, size);
			sketch.fill(0);
			sketch.text(String.valueOf(maxTimeSec-(currentTime-bornAt)/1000), x, y);
		}
	}
}