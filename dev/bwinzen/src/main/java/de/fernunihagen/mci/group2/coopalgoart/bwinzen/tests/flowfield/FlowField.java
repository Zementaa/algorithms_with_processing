package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.flowfield;

import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class FlowField extends PApplet {
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
		for (int i = 0; i<10000; i++) {
		    PVector start = new PVector(random(width), random(height));
		    particles.add(new Particle(start, random(1, 8)));
		}

	}

	public void draw() {
//		background(255);
//		noFill();

		stroke(0);
//		if(frameCount == 1) {
			drawCells();
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
		FlowField mySketch = new FlowField();
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
		fill(0);
		strokeWeight(1);
		ellipse(particle.pos.x, particle.pos.y, 5, 5);
//		line(particle.pos.x, particle.pos.y, particle.previousPos.x, particle.previousPos.y);
		// point(pos.x, pos.y);
		fill(255);
		particle.updatePreviousPos();
	}
}
