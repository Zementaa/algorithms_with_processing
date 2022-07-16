package de.fernunihagen.mci.group2.coopalgoart.ccamier.automata;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PGraphics;

public class GOL {

	// Einteilung
	int w;
	int spalten, reihen;

	// Farben
	int farbe1;
	int farbe2;

	// Game of life board
	Cell[][] board;

	public GOL(PGraphics pg, int w, int farbe1, int farbe2, CooperationContext context) {
		this.w = w;

		this.farbe1 = farbe1;
		this.farbe2 = farbe2;

		this.spalten = pg.width / this.w;
		this.reihen = pg.height / this.w;
		this.board = new Cell[this.spalten][this.reihen];

		init(context);
	}

	void init(CooperationContext context) {
		Random rand = new Random(context.getSeed());
		for (int i = 0; i < this.spalten; i++) {
			for (int j = 0; j < this.reihen; j++) {
				this.board[i][j] = new Cell(i * this.w, j * this.w, this.w, farbe1, farbe2, rand);
			}
		}
	}

	// Neue Generation
	public void generate() {
		for (int i = 0; i < this.spalten; i++) {
			for (int j = 0; j < this.reihen; j++) {
				this.board[i][j].savePrevious();
			}
		}

		// Board abgleichen
		for (int x = 0; x < this.spalten; x++) {
			for (int y = 0; y < this.reihen; y++) {

				// Nachbarn abspeichern
				int neighbors = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						neighbors += this.board[(x + i + this.spalten) % this.spalten][(y + j + this.reihen)
								% this.reihen].vorherig;
					}
				}

				neighbors -= this.board[x][y].vorherig;

				// Rules of Life
				if ((this.board[x][y].state == 1) && (neighbors < 2))
					this.board[x][y].newState(0); // Einsamkeit
				else if ((this.board[x][y].state == 1) && (neighbors > 3))
					this.board[x][y].newState(0); // Überpopulation
				else if ((this.board[x][y].state == 0) && (neighbors == 3))
					this.board[x][y].newState(1); // Reproduktion
				// else do nothing!
			}
		}
	}

	// fill 255 als '1', fill 0 als '0'
	public void display(PGraphics pg) {
		for (int i = 0; i < this.spalten; i++) {
			for (int j = 0; j < this.reihen; j++) {
				this.board[i][j].display(pg);
			}
		}
	}

}
