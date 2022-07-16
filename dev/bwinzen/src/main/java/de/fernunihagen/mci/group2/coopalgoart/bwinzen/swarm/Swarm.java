package de.fernunihagen.mci.group2.coopalgoart.bwinzen.swarm;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.EdgeBehavior.PositionAdapter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree.QuadTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree.QuadTreeQuery;
import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class Swarm {
	// The value which brings boids together
	@Setter
	private float alignValue = 2f;

	// The value which brings boids together by centrizise them
	@Setter
	private float cohesionValue = 0.5f;

	// The value which
	@Setter
	float seperationValue = 1f;
	@Setter
	private double perceptionRadiusAlignment = 50;
	@Setter
	private double perceptionRadiusCohesion = 100;
	@Setter
	private double perceptionRadiusSeperation = 50;

	@Setter
	private PositionAdapter positionAdapter;

	@Setter
	private boolean calculateColor;

	@Setter
	private float maxForce = 1;

	private QuadTree<Boid> boids;
	@Getter
	private List<Boid> boidList = new LinkedList<>();

	private Random randomGenerator;

	@Getter
	private long round;

	private int width;
	private int height;

	public Swarm(long seed, int numberOfBirds, float maxSpeed, boolean staticSpeed, int width, int height) {
		this.width = width;
		this.height = height;
		this.boids = new QuadTree<>(0, 0, width, height, 4);
		randomGenerator = new Random(seed);
		for (int i = 0; i < numberOfBirds; i++) {
			PVector position = new PVector(randomGenerator.nextInt(width), randomGenerator.nextInt(height));
			PVector velocity = new PVector(randomGenerator.nextFloat() * 2 - 1, randomGenerator.nextFloat() * 2 - 1);
			float speed = randomGenerator.nextFloat() * maxSpeed + 2;
			velocity.setMag(staticSpeed ? maxSpeed : speed);
			Boid boid = new Boid(position, velocity);
			boids.add(boid);
			boidList.add(boid);
		}
	}

	public void forEachParallel(Consumer<Boid> boidConsumer) {
		boidList.stream().parallel().forEach(boidConsumer);
	}

	public void forEach(Consumer<Boid> boidConsumer) {
		boidList.forEach(boidConsumer);
	}

	public void nextStep(Function<Boid, PVector> calculateAdditionalForce) {
		round++;
		forEachParallel(this::flock);
		if (calculateAdditionalForce == null) {
			forEachParallel(b -> {
				b.update(width, height, null);
				positionAdapter.checkEdges(b);
			});
		} else {
			forEachParallel(b -> {
				b.update(width, height, calculateAdditionalForce.apply(b));
				positionAdapter.checkEdges(b);
			});
		}
	}

	private void flock(Boid b) {
		double maxDistance = Math.max(perceptionRadiusAlignment,
				Math.max(perceptionRadiusCohesion, perceptionRadiusSeperation));

		double mediumDistance = 10;

		NavigableMap<Double, List<Boid>> queryDistances = boids.queryDistances(b.getX(), b.getY(),
				QuadTreeQuery.queryMaxDistance(b.getX(), b.getY(), maxDistance));
		Set<Entry<Double, List<Boid>>> set = queryDistances.entrySet();
		PVector alignment = new PVector();
		PVector cohesion = new PVector();
		PVector separation = new PVector();
		int color = b.color;
		double red = color >> 16 & 0xFF;
		double green = color >> 8 & 0xFF;
		double blue = color & 0xFF;
		int totalAlign = 0;
		int totalCohesion = 0;
		int totalSeperation = 0;
		double totalColors = 1;
		if (calculateColor) {
			for (Entry<Double, List<Boid>> e : set) {
				double d = e.getKey();
				List<Boid> boids = e.getValue();
				for (Boid other : boids) {
					if (other == b) {
						continue;
					}
					if (perceptionRadiusAlignment > d) {
						alignment.add(other.velocity);
						totalAlign++;
					}
					if (perceptionRadiusCohesion > d) {
						cohesion.add(other.position);
						totalCohesion++;
					}
					if (perceptionRadiusSeperation > d) {
						PVector diff = PVector.sub(b.position, other.position);
						float diffMag = diff.mag();
						if (diffMag < 0.1) {
							diffMag = 0.1f;
						}
						diff.div(diffMag * diffMag);
						separation.add(diff);
						totalSeperation++;
					}
					if (mediumDistance > d) {
						double factor = d / mediumDistance;
						factor = factor * factor * factor * factor;
						int colorOther = other.color;
						red += (colorOther >> 16 & 0xFF) * factor;
						green += (colorOther >> 8 & 0xFF) * factor;
						blue += (colorOther & 0xFF) * factor;
						totalColors += factor;
					}
				}
			}
		} else { // Code doubled to have better performance
			for (Entry<Double, List<Boid>> e : set) {
				double d = e.getKey();
				List<Boid> boids = e.getValue();
				for (Boid other : boids) {
					if (other == b) {
						continue;
					}
					if (perceptionRadiusAlignment > d) {
						alignment.add(other.velocity);
						totalAlign++;
					}
					if (perceptionRadiusCohesion > d) {
						cohesion.add(other.position);
						totalCohesion++;
					}
					if (perceptionRadiusSeperation > d) {
						PVector diff = PVector.sub(b.position, other.position);
						float diffMag = diff.mag();
						if (diffMag < 0.1) {
							diffMag = 0.1f;
						}
						diff.div(diffMag * diffMag);
						separation.add(diff);
						totalSeperation++;
					}
				}
			}
		}
		if (totalAlign > 0) {
			alignment.div(totalAlign);
			alignment.setMag(b.getMaxSpeed());
			alignment.sub(b.velocity);
//			alignment.limit(maxForce);
		}
		if (totalCohesion > 0) {
			cohesion.div(totalCohesion);
			cohesion.sub(b.position);
			cohesion.setMag(b.getMaxSpeed());
			cohesion.sub(b.velocity);
//			cohesion.limit(this.maxForce);
		}
		if (totalSeperation > 0) {
			separation.div(totalSeperation);
			separation.setMag(b.getMaxSpeed());
			separation.sub(b.velocity);
//			separation.limit(this.maxForce);
		}
		if (calculateColor) {
			b.setNextColor(0xFF000000 | (int) (red / totalColors) << 16 | (int) (green / totalColors) << 8
					| (int) (blue / totalColors));
		}

//		PVector alignment = this.align(b);
//		PVector cohesion = this.cohesion(b);
//		PVector separation = this.separation(b);

		alignment.mult(alignValue);
		cohesion.mult(cohesionValue);
		separation.mult(seperationValue);

		PVector acceleration = new PVector();
		acceleration.add(alignment);
		acceleration.add(cohesion);
		acceleration.add(separation);
		acceleration.limit(this.maxForce);
		b.setAcceleration(acceleration);
	}

	public Boid findClosest(double x, double y) {
		return boids.findClosest(x, y);
	}
}
