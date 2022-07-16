package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * @author bwinzen
 *
 */
public class ColorBallsStarter extends PApplet{
	
	public static final long maxTimeSec = 30;
	private static final String PSurfaceFX = null;
	private ArrayList<Ball> balls = new ArrayList<>();
	
	@Override
	public void setup() {
		super.setup();
//		printArray(PFont.list());
		PFont f = createFont("Times New Roman", 12);
		textFont(f);
		textAlign(CENTER, CENTER);
	}
	
	@Override
	public void settings(){
		size(1000, 1000, P3D);
//		size(1000, 1000);
	}
	
	/**
	 * Redraw every ~10ms -30ms 
	 */
	public void draw(){
//		PSurfaceFX surface2 = (PSurfaceFX) getSurface();
//		ResizableCanvas c = (ResizableCanvas)surface2.getNative();
		background(64);
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
	
	/**
	*{@inheritDoc}
	*/
	@Override
	public void mouseDragged(){
		balls.add(new Ball(this, mouseX, mouseY));
	}
	
	public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		ColorBallsStarter mySketch = new ColorBallsStarter();
		PApplet.runSketch(processingArgs, mySketch);
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