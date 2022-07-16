package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.swarm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * @author bwinzen
 *
 */
public class Swarm {
	private static final double MIN_DISTANCE = 5;
	private static final double MAX_DISTANCE = 30;
	private int width;
	private int height;

	private List<Bird>[][] swarmGrid;

	private List<Bird> birds = new LinkedList<>();

	private Random randomGenerator;

	private long round;

	@SuppressWarnings("unchecked")
	public Swarm(long seed, int numberOfBirds, int width, int height) {
		this.width = width;
		this.height = height;
		swarmGrid = new List[width][height];
		for (int i = 0; i < swarmGrid.length; i++) {
			List<Bird>[] lists = swarmGrid[i];
			for (int j = 0; j < lists.length; j++) {
				lists[j] = new LinkedList<>();
			}
		}
		randomGenerator = new Random(seed);
		for (int i = 0; i < numberOfBirds; i++) {
			Bird bird = new Bird(randomGenerator.nextDouble() * width, randomGenerator.nextDouble() * height,
					randomGenerator.nextDouble() * 20 - 10, randomGenerator.nextDouble() * 20 - 10);
			addBird(bird);
		}

//		Bird bird = new Bird(width / 2, height / 2, 0, -10);
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, 0, 10);
//		bird.debug = true;
//		bird.name="Gustav";
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, 10, 0);
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, -10, 0);
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, 10, 10);
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, 10, -10);
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, -10, 10);
//		addBird(bird);
//		bird = new Bird(width / 2, height / 2, -10, -10);
//		addBird(bird);
//			}
	}

	public void nextStep() {
		round++;
		forEach(this::calculateNextIteration);
		clearGrid();
		forEach(b -> {
			b.nextState();
			addBirdToGrid(b);
		});
	}

	private void addBird(Bird b) {
		addBirdToGrid(b);
		birds.add(b);
	}

	private void addBirdToGrid(Bird b) {
		int xIndex = (int) b.x;
		int yIndex = (int) b.y;
		List<Bird> list = swarmGrid[xIndex][yIndex];
		list.add(b);
	}

	private void clearGrid() {
		for (int i = 0; i < swarmGrid.length; i++) {
			List<Bird>[] lists = swarmGrid[i];
			for (int j = 0; j < lists.length; j++) {
				lists[j].clear();
			}
		}
	}

	private void calculateNextIteration(Bird b) {
		double xNext = b.x + b.xSpeed;
		double yNext = b.y + b.ySpeed;
		double xSpeedNext = b.xSpeed;
		double ySpeedNext = b.ySpeed;

		NavigableMap<Double, Bird> closest = findClosest(b, 10);
		double avgSpeed = b.speed();
		double avgAngle = b.angle();
		int counter = 1;
		Set<Entry<Double, Bird>> entrySet = closest.entrySet();
		for (Entry<Double, Bird> entry : entrySet) {
			if (entry.getKey() < 200) {
				counter++;
				Bird neightbour = entry.getValue();
				avgAngle += neightbour.angle();
				avgSpeed += neightbour.speed();
			}
		}
		avgAngle /= counter;
		avgSpeed /= counter;
		xSpeedNext = avgSpeed * Math.cos(avgAngle + 0.5 * Math.PI);
		ySpeedNext = avgSpeed * Math.sin(avgAngle + 0.5 * Math.PI);

		if (xNext < 0 || xNext >= width) {
			xSpeedNext *= -1;
		}

		if (xNext < 0) {
			xNext = Math.abs(xNext);
		} else if (xNext >= width) {
			xNext = 2 * width - xNext - 0.0001f;
		}

		if (yNext < 0 || yNext >= height) {
			ySpeedNext *= -1;
		}

		if (yNext < 0) {
			yNext = Math.abs(yNext);
		} else if (yNext >= height) {
			yNext = 2 * height - yNext - 0.0001f;
		}

		b.setNext(xNext, yNext, xSpeedNext, ySpeedNext);
	}

//	public static void main(String[] args) {
//		Bird bird = new Bird(2, 2, 0, -10);
//		calculateByAngle(bird, bird.speed(), bird.angle());
//		
//		System.out.println(bird);
//		bird.nextState();
//		System.out.println(bird);
//	}

	private static void calculateByAngle(Bird b, double speed, double angle) {
		double xSpeedNext = speed * Math.cos(angle + 0.5 * Math.PI);
		double ySpeedNext = speed * Math.sin(angle + 0.5 * Math.PI);
		double xNext = b.x + b.xSpeed;
		double yNext = b.y + b.ySpeed;
		b.setNext(xNext, yNext, xSpeedNext, ySpeedNext);
	}

	public void forEach(Consumer<Bird> birdConsumer) {
		birds.forEach(birdConsumer);
	}

	public NavigableMap<Double, Bird> findClosest(Bird b, int minCount) {
		Set<Bird> setOfNeighbours = new HashSet<>();
		for (int r = 0; r < Math.max(width, height); r++) {
			addBirdsAround(setOfNeighbours, b, r);
			if (setOfNeighbours.size() >= minCount || setOfNeighbours.size() == birds.size()-1) {
				break;
			}
		}
		NavigableMap<Double, Bird> result = new TreeMap<>();
		setOfNeighbours.forEach(n -> {
			double a = b.x - n.x;
			double c = b.y - n.y;
			double dist = Math.sqrt(a * a + c * c);
			result.put(dist, n);
		});
		return result;
	}

	private void addBirdsAround(Set<Bird> setOfNeighbours, Bird b, int indexRadius) {
		int xIndex = (int) b.x;
		int yIndex = (int) b.y;

		if (indexRadius == 0) {
			setOfNeighbours.addAll(swarmGrid[xIndex][yIndex]);
			setOfNeighbours.remove(b);
			return;
		}
		int minX = Math.max(0, xIndex - indexRadius);
		int maxX = Math.min(width - 1, xIndex + indexRadius);

		int minY = Math.max(0, yIndex - indexRadius);
		int maxY = Math.min(height - 1, yIndex + indexRadius);

		if (minY == yIndex - indexRadius) {
			for (int i = minX; i < maxX; i++) {
				setOfNeighbours.addAll(swarmGrid[i][minY]);
			}
		}

		if (maxY == yIndex + indexRadius) {
			for (int i = minX; i < maxX; i++) {
				setOfNeighbours.addAll(swarmGrid[i][maxY]);
			}
		}

		if (minX == xIndex - indexRadius) {
			for (int i = minY; i < maxY; i++) {
				setOfNeighbours.addAll(swarmGrid[minX][i]);
			}
		}

		if (maxX == xIndex + indexRadius) {
			for (int i = minY; i < maxY; i++) {
				setOfNeighbours.addAll(swarmGrid[maxX][i]);
			}
		}

	}

}
