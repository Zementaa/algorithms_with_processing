package de.fernunihagen.mci.group2.coopalgoart.api.cooperation;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class PerlinoiseForceFieldCoordCalcStrategy implements PixelCoordinateCalculationStrategy {
	private int sizeOfCell = 25;
	private PVector[][] cells;
	private int cellCols;
	private int cellRows;
	private float noiseDiv = 0.01f;
	private float zNoiseDiv = 0.00001f;
	private int z = 0;

	private Particle p;
	private PApplet noiseFactory = new PApplet();
	

	private void updateCells() {
		for (int r = 0; r < cellRows; r++) {
			for (int c = 0; c < cellCols; c++) {
				z++;
				float newAngle = noiseFactory.noise(c * noiseDiv, r * noiseDiv, z * zNoiseDiv) * PApplet.TWO_PI * 4;
				PVector pVector = PVector.fromAngle(newAngle);
				pVector.mult(sizeOfCell / 2);
				cells[r][c] = pVector;
			}
		}
	}

	private void follow(Particle particle) {
		int c = PApplet.floor(particle.pos.x / sizeOfCell);
		int r = PApplet.floor(particle.pos.y / sizeOfCell);
		try {
			PVector force = cells[r][c];
			particle.applyForce(force);
		} catch (Exception e) {
		}
	}

	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		if (p == null) {
			setup(cooperationContext);
			noiseFactory.noiseSeed(cooperationContext.getSeed());
		}
		PApplet pApplet = cooperationContext.getPApplet();
		p.edges(pApplet.width, pApplet.height);
		follow(p);
		p.update();
		PixelCoordinate coordinate = cooperationContext.getCoordinate();
		coordinate.setX(p.pos.x);
		coordinate.setY(p.pos.y);
		coordinate.setZ(p.pos.z);
		updateCells();
		return coordinate;
	}

	private void setup(CooperationContext cooperationContext) {
		PApplet pApplet = cooperationContext.getPApplet();
		cellRows = pApplet.height / sizeOfCell;
		cellCols = pApplet.width / sizeOfCell;
		cells = new PVector[cellRows][cellCols];
		for (int r = 0; r < cellRows; r++) {
			for (int c = 0; c < cellCols; c++) {
				PVector pVector = PVector
						.fromAngle(pApplet.noise(c * noiseDiv, r * noiseDiv, z * noiseDiv) * PApplet.TWO_PI * 4);
				cells[r][c] = pVector;
			}
		}
		PixelCoordinate coordinate = cooperationContext.getCoordinate();
		PVector start = new PVector(coordinate.getX(), coordinate.getY(), coordinate.getZ());
		p = new Particle(start, 5);
	}

	/**
	 * @author bwinzen
	 *
	 */
	private static class Particle {
		PVector pos;
		PVector vel;
		PVector acc;
		PVector previousPos;
		float maxSpeed;

		Particle(PVector start, float maxspeed) {
			maxSpeed = maxspeed;
			pos = start;
			vel = new PVector(0, 0);
			acc = new PVector(0, 0);
			previousPos = pos.copy();
		}

		public void update() {
			pos.add(vel);
			vel.add(acc);
			vel.limit(maxSpeed);
			acc.mult(0);
		}

		void applyForce(PVector force) {
			acc.add(force);
		}

		void edges(int width, int height) {
			if (pos.x > width) {
				pos.x = 0;
				updatePreviousPos();
			}
			if (pos.x < 0) {
				pos.x = width - 0.0001f;
				updatePreviousPos();
			}
			if (pos.y > height) {
				pos.y = 0;
				updatePreviousPos();
			}
			if (pos.y < 0) {
				pos.y = height - 0.0001f;
				updatePreviousPos();
			}
		}

		public void updatePreviousPos() {
			this.previousPos.x = pos.x;
			this.previousPos.y = pos.y;
		}

	}
}
