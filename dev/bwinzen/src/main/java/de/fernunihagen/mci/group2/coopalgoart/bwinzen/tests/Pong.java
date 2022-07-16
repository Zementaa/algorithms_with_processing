package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import processing.core.PApplet;
import processing.core.PFont;
import processing.event.KeyEvent;

/**
 * @author bwinzen
 */
public class Pong extends PApplet {
	private Bat left = new Bat(this, 5, Bat.BAT_WIDTH);
	private Bat right = new Bat(this, 975, 0);
	private Ball ball = new Ball(this, left, right);
	private int rightPoints = 0;
	private int leftPoints = 0;

	@Override
	public void setup() {
		PFont f = createFont("Times New Roman", 50);
		textFont(f);
		textAlign(CENTER, CENTER);
	}

	@Override
	public void settings() {
		size(1000, 1000, FX2D);
	}

	/**
	 * Redraw every ~10ms -30ms
	 */
	@Override
	public void draw() {
		background(64);
		text(leftPoints + " : " + rightPoints, 500 - 50, 500 - 50);
		ball.nextStepAndRender();
		left.nextStepAndRender();
		right.nextStepAndRender();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (keyCode == 87) {
			left.up();
		} else if (keyCode == 83) {
			left.down();
		}
		if (keyCode == 40) {
			right.down();
		} else if (keyCode == 38) {
			right.up();
		}
//		System.out.println(keyCode);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (keyCode == 83 || keyCode == 87) {
			left.stop();
		}
		if (keyCode == 38 || keyCode == 40) {
			right.stop();
		}
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Pong mySketch = new Pong();
		PApplet.runSketch(processingArgs, mySketch);
	}

	private static class Ball {

		private Pong sketch;

		private float x;
		private float y;
		private float size;
		private float xSpeed;
		private float ySpeed;
		private float speedFactor;

		private Bat right;
		private Bat left;

		public Ball(Pong sketch, Bat right, Bat left) {
			this.sketch = sketch;
			this.right = right;
			this.left = left;
			this.size = 10;
			reset();
		}

		public void reset() {
			this.speedFactor = 1;
			this.x = 500;
			this.y = 500;
			this.xSpeed = Math.random() > 0.5 ? -6 : 6;
			this.ySpeed = sketch.random(-2, 2);
		}

		public void nextStepAndRender() {
			speedFactor += 0.001;
			float xOld = x;
			float yOld = y;
			x += xSpeed * speedFactor;
			y += ySpeed * speedFactor;

			if (x < 0) {
				x = Math.abs(x);
				sketch.rightPoints++;
				reset();
			} else if (x > sketch.width) {
				x = 2 * sketch.width - x;
				sketch.leftPoints++;
				reset();
			} else if (right.coolid(xOld, yOld, x, y)) {
				x += 2 * -1 * Math.signum(xSpeed) * right.dist(xOld, yOld, x, y);
				xSpeed *= -1;
			} else if (left.coolid(xOld, yOld, x, y)) {
				x += 2 * -1 * Math.signum(xSpeed) * left.dist(xOld, yOld, x, y);
				xSpeed *= -1;
			}

			if (y < 0) {
				ySpeed *= -1;
			} else if (y > sketch.height) {
				ySpeed *= -1;
			}
			setColor();
//			sketch.ellipse(x, y, (float)(size*Math.pow(speedFactor, 10)), (float)(size*Math.pow(speedFactor, 10))); Ball gets bigger
			sketch.ellipse(x, y, size, size);
		}

		private void setColor() {
			if (speedFactor > 5) {
				sketch.fill(0xFF, 0, 0);
			} else if (speedFactor > 3) {
				sketch.fill(0, 0, 0XFF);
			} else if (speedFactor > 1) {
				sketch.fill(0, 0xFF, 0);
			} else {
				sketch.fill(0xFF, 0xFF, 0xFF);
			}
		}
	}

	private static class Bat {
		private static final int BAT_WIDTH = 20;

		private static final int BAT_LENGTH = 320;

		private PApplet sketch;

		private float x;
		private float y;
		private float ySpeed;

		private float xBatSightOffset;

		public Bat(PApplet sketch, float x, float xBatSightOffset) {
			this.sketch = sketch;
			this.x = x;
			this.xBatSightOffset = xBatSightOffset;
			this.y = 0;
		}

		public boolean coolid(float xOld, float yOld, float xNew, float yNew) {
			return !(xNew < x || xNew > x + BAT_WIDTH || yNew < y || yNew > y + BAT_LENGTH);
		}

		public float dist(float xOld, float yOld, float xNew, float yNew) {
			return 0.0f;
		}

		public void down() {
			ySpeed = 10;
		}

		public void up() {
			ySpeed = -10;
		}

		public void stop() {
			ySpeed = 0;
		}

		public void nextStepAndRender() {
			y += ySpeed;
			if (y < 0) {
				y = 0;
			} else if (y > sketch.height - BAT_LENGTH) {
				y = sketch.height - BAT_LENGTH;
			}
			sketch.fill(0xFF, 0xFF, 0xFF);
			sketch.rect(x, y, BAT_WIDTH, BAT_LENGTH);
		}
	}
}