package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.gol;

import java.util.Random;
import java.util.function.Predicate;

public class GameOfLife {

	private State[][] states;
	private int columns;
	private int rows;

	public GameOfLife(int columns, int rows, long seed) {
		this.columns = columns;
		this.rows = rows;
		State[] possibleStates = State.values();
		int maxRandom = possibleStates.length;
		Random random = new Random(seed);
		states = new State[rows][columns];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				states[r][c] = possibleStates[random.nextInt(maxRandom)];
			}
		}
	}

	public State[][] getStates() {
		return states;
	}

	public void calculateNextIter() {
		State[][] statesNew = new State[rows][columns];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				statesNew[r][c]=State.NOTHING;
			}
		}
		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				State oldState = states[r][c];
				if (oldState == State.RABBIT_GREEN) {
					statesNew[r][c] = State.RABBIT;
				}

				if (oldState == State.RABBIT_GREEN||oldState == State.RABBIT) {
					if(0<countNeighbors(r, c, 1, s->(s==State.RABBIT || s==State.RABBIT_GREEN))) {
						setNeighbors(r, c, 1, statesNew, s->s==State.GREEN, State.RABBIT_GREEN);
					}
				}
				
				if(oldState == State.GREEN || oldState == State.FOX_GREEN) {
					setNeighbors(r, c, 1, statesNew, s->s==State.NOTHING, State.GREEN);
					setNeighbors(r, c, 1, statesNew, s->s==State.FOX, State.FOX_GREEN);
				}
				
				if(oldState == State.FOX||oldState == State.FOX_GREEN) {
					int countRabbits = countNeighbors(r, c, 1, s->(s==State.RABBIT|| s==State.RABBIT_GREEN));
					int countFoxes = countNeighbors(r, c, 1, s->(s==State.FOX|| s==State.FOX_GREEN))+1;
					double foxRabbitRatio = ((double)countRabbits)/countFoxes;
					if(foxRabbitRatio<1.5) {
						setNewState(statesNew, oldState == State.FOX?State.NOTHING:State.GREEN, r, c);
					}else if(foxRabbitRatio>=2) {
						setNeighbors(r, c, 1, statesNew,s->s==State.RABBIT, State.FOX);
					}else {
						setNewState(statesNew, State.FOX, r, c);
					}
				}
			}
		}
		states = statesNew;
	}
	
	private int countNeighbors(int r, int c, int radius, Predicate<State> filter) {
		int xIndex = c;
		int yIndex = r;

		if (radius == 0) {
			return 0;
		}
		int count = 0; 
		int minX = Math.max(0, xIndex - radius);
		int maxX = Math.min(columns - 1, xIndex + radius);

		int minY = Math.max(0, yIndex - radius);
		int maxY = Math.min(rows - 1, yIndex + radius);

		if (minY == yIndex - radius) {
			for (int i = minX; i < maxX; i++) {
				State state = states[minY][i];
				count+=filter.test(state)?1:0;
			}
		}

		if (maxY == yIndex + radius) {
			for (int i = minX; i < maxX; i++) {
				State state = states[maxY][i];
				count+=filter.test(state)?1:0;
			}
		}

		if (minX == xIndex - radius) {
			for (int i = minY+1; i < maxY; i++) {
				State state = states[i][minX];
				count+=filter.test(state)?1:0;
			}
		}

		if (maxX == xIndex + radius) {
			for (int i = minY+1; i < maxY; i++) {
				State state = states[i][maxX];
				count+=filter.test(state)?1:0;
			}
		}
		
		return count;

	}
	
	
	private void setNeighbors(int r, int c, int radius, State[][] statesNew, Predicate<State> filter, State newState) {
		int xIndex = c;
		int yIndex = r;

		if (radius == 0) {
			return;
		}
		int minX = Math.max(0, xIndex - radius);
		int maxX = Math.min(columns - 1, xIndex + radius);

		int minY = Math.max(0, yIndex - radius);
		int maxY = Math.min(rows - 1, yIndex + radius);

		if (minY == yIndex - radius) {
			for (int i = minX; i < maxX; i++) {
				State state = states[minY][i];
				if(filter.test(state)) {
					setNewState(statesNew, newState, minY, i);
				}
			}
		}

		if (maxY == yIndex + radius) {
			for (int i = minX; i < maxX; i++) {
				State state = states[maxY][i];
				if(filter.test(state)) {
					setNewState(statesNew, newState, maxY, i);
				}
			}
		}

		if (minX == xIndex - radius) {
			for (int i = minY; i < maxY; i++) {
				State state = states[i][minX];
				if(filter.test(state)) {
					setNewState(statesNew, newState, i, minX);
				}
			}
		}

		if (maxX == xIndex + radius) {
			for (int i = minY; i < maxY; i++) {
				State state = states[i][maxX];
				if(filter.test(state)) {
					setNewState(statesNew, newState, i, maxX);
				}
			}
		}
		

	}

	private void setNewState(State[][] statesNew, State newState, int r, int c) {
		statesNew[r][c]=statesNew[r][c].merge(newState);
	}
}
