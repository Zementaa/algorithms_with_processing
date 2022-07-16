package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.gol;

import java.io.InputStream;

import processing.core.PApplet;
import processing.core.PImage;

public class GameOfLifeSimulation extends PApplet {
	public static int background = 0;
	int cols, rows;
	int cellSize = 100;
	private GameOfLife gameOfLife;
	private PImage rabbit;
	private PImage fox;
	private boolean next = true;

	public void settings() {
		size(1000, 1000, P3D);
	}

	public void setup() {
		// calc colums and rows
		cols = width / cellSize;
		rows = height / cellSize;
		gameOfLife = new GameOfLife(cols, rows, 0);
		rabbit = loadImage("rabbit.png");
		fox = loadImage("fox.png");
		frameRate(0.1f);
	}
	
	@Override
	public InputStream createInput(String filename) {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		if(resourceAsStream!= null) {
			return resourceAsStream;
		}
		return super.createInput(filename);
	}

	@Override
	public void mouseClicked() {
		next = true;
	}
	public void draw() {
		if(!next) {
			return;
		}
		next = false;
		
		background(background);
		stroke(0);
		fill(255);
		State[][] states = gameOfLife.getStates();
		gameOfLife.calculateNextIter();
		
		// rotate the pixels on the screen
//		translate(width / 2, height / 2);
//		rotateX(PI / 2.2f);
//		translate(-width / 2, -width / 2);

		
		// shaping among the sketch in x,y,z-Achse
		for (int y = 0; y < rows; y++) {
			beginShape();
			for (int x = 0; x < cols; x++) {
				State state = states[y][x];
				switch (state) {
				case FOX_GREEN:
				case GREEN:
				case RABBIT_GREEN:
					fill(0,255,0);
					break;
				default:
					fill(255);
					break;
				}
				rect(y*cellSize, x*cellSize, cellSize, cellSize);
				
				switch (state) {
				case FOX:
				case FOX_GREEN:
					image(fox, y*cellSize, x*cellSize, cellSize, cellSize);
					break;
				case RABBIT:
				case RABBIT_GREEN:
					image(rabbit, y*cellSize, x*cellSize, cellSize, cellSize);
				default:
					break;
				}
			}
			endShape();
		}
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		GameOfLifeSimulation mySketch = new GameOfLifeSimulation();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
