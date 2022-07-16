package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.koch;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PShape;

/**
 * @author bwinzen
 *
 */
public class KochCurveSnowflakes extends PApplet {

	KochCurveSnowflakeFactory factory;

	List<Snowflake> snowflakes = new LinkedList<>();
	
	@Override
	public void setup() {
		factory = new KochCurveSnowflakeFactory(this);
		for(int i= 0; i<10; i++) {
			snowflakes.add(new Snowflake(this, factory));
		}

	}

	public void settings() {
		size(1000, 1000, FX2D);
	}

	public void draw() {
		background(64);
		if(frameCount%100==0) {
			snowflakes.add(new Snowflake(this, factory, -50));
			snowflakes.sort((o1, o2) -> Double.compare(o1.scale, o2.scale));
		}
		for(Snowflake s: snowflakes) {
			s.draw(this);
		}
	}

	


	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		KochCurveSnowflakes mySketch = new KochCurveSnowflakes();
		PApplet.runSketch(processingArgs, mySketch);
	}
	
	private static class Snowflake{
		private float x;
		private float y;
		private float scale;
		private PShape shape;
		private float angle;
		private Random random;
		
		public Snowflake(PApplet applet, KochCurveSnowflakeFactory factory) {
			random = new Random();
			scale = (float) (random.nextFloat()*0.05f+0.05f);
			x = random.nextFloat() * applet.width;
			y = random.nextFloat() * applet.height;
			angle = (float) (random.nextFloat()*Math.PI*2);
			shape=factory.getSnowflake(random.nextInt(5)+3);
		}
		
		public Snowflake(PApplet applet, KochCurveSnowflakeFactory factory, float y) {
			random = new Random();
			scale = (float) (random.nextFloat()*0.05+0.05);
			x = random.nextFloat() * applet.width;
			this.y = y;
			angle = (float) (random.nextFloat()*Math.PI*2);
			shape=factory.getSnowflake(random.nextInt(5)+3);
		}
		
		public void draw(PApplet applet) {
			applet.pushMatrix();
			applet.translate(x, y);
			applet.rotate(angle);
			applet.scale(scale);
			applet.shape(shape);
			applet.popMatrix();
			
			y+= 100*scale;
			if(y>applet.height+100) {
				y = -50;
				x = random.nextFloat() * applet.width;
			}
		}
	}
}
