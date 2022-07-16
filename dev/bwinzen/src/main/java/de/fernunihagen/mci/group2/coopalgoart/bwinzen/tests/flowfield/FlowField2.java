package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.flowfield;

import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class FlowField2 extends PApplet {
	private int sizeOfCell = 25;
	private PVector[][] cells;
	private int cellCols;
	private int cellRows;
	private float noiseDiv = 0.01f;
	private float zNoiseDiv = 0.00001f;
	private int z = 0;
	
	private List<Particle> particles = new LinkedList<>();

	public void settings() {
		size(1000, 1000, FX2D);
	}

	@Override
	public void setup() {
		cellRows = height / sizeOfCell;
		cellCols = width / sizeOfCell;
		cells = new PVector[cellRows][cellCols];
		for (int r = 0; r < cellRows; r++) {
			for (int c = 0; c < cellCols; c++) {
				PVector pVector = PVector.fromAngle(noise(c * noiseDiv, r * noiseDiv, z * noiseDiv) * TWO_PI * 4);
				cells[r][c] = pVector;
			}
		}
		int colorOfBoids = 0xFF056010;
		int red = colorOfBoids >> 16 & 0xFF;
		int green = colorOfBoids >> 8 & 0xFF;
		int blue = colorOfBoids & 0xFF;
		for (int i = 0; i<10000; i++) {
		    PVector start = new PVector(random(width), random(height));
		    Particle e = new Particle(start, random(1, 8));
		    int color = 0xFF000000 | (findGaussColor(red, randomGaussian()) << 16)
					| (findGaussColor(green, randomGaussian()) << 8)
					| (findGaussColor(blue, randomGaussian()));
		    e.color = color;
			particles.add(e);
		}
	}
	
	private int findGaussColor(int colorChannel, float randomGaussian) {
		return (int) Math.max(0, Math.min(255, colorChannel + 50 * randomGaussian));
	}
	
	public void draw() {
//		background(255);
//		noFill();

		stroke(0);
//		if(frameCount == 1) {
//			drawCells();
//		}
		updateCells();
		particles.forEach(p->updateAndDrawParticle(p));
	}

	private void updateCells() {
		for (int r = 0; r < cellRows; r++) {
			for (int c = 0; c < cellCols; c++) {
				z++;
				float newAngle = noise(c * noiseDiv, r * noiseDiv, z * zNoiseDiv) * TWO_PI * 4;
				PVector pVector = PVector.fromAngle(newAngle);
				pVector.mult(sizeOfCell / 2);
				cells[r][c] = pVector;
			}
		}
	}

	private void drawCells() {
		for (int r = 0; r < cellRows; r++) {
			for (int c = 0; c < cellCols; c++) {
				rect(c * sizeOfCell, r * sizeOfCell, sizeOfCell, sizeOfCell);

			}
		}
		for (int r = 0; r < cellRows; r++) {
			for (int c = 0; c < cellCols; c++) {
				PVector pVector = cells[r][c];
				pVector = pVector.copy().setMag(sizeOfCell / 2);
				int x = c * sizeOfCell + sizeOfCell / 2;
				int y = r * sizeOfCell + sizeOfCell / 2;
//				line(x - pVector.x, y - pVector.y, x + pVector.x, y + pVector.y);
				line(x, y , x + pVector.x, y + pVector.y);
			}
		}

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		FlowField2 mySketch = new FlowField2();
		PApplet.runSketch(processingArgs, mySketch);
	}

	private void updateAndDrawParticle(Particle p) {
		p.edges(width, height);
		follow(p);
		p.update();
		drawParticle(p);
	}

	private void follow(Particle particle) {
		int c = floor(particle.pos.x / sizeOfCell);
		int r = floor(particle.pos.y / sizeOfCell);
		try {
		PVector force = cells[r][c];
		particle.applyForce(force);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void drawParticle(Particle particle) {
		stroke(0, 5);
		fill(particle.color);
		strokeWeight(1);
		ellipse(particle.pos.x, particle.pos.y, 1, 1);
		particle.updatePreviousPos();
	}
}
