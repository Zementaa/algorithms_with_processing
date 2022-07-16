package de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators;

import java.util.Random;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntPredicate;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.EdgeBehavior;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form.FlyingObjectFormPrinter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.swarm.Boid;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.swarm.Swarm;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
@Builder
public class SwarmGenerator implements Generator, PixelCoordinateCalculationStrategy {
	private int countOfBoids;
	private float alignment;
	private float cohesion;
	private float seperation;
	private double radiusAlignment;
	private double radiusCohesion;
	private double radiusSeperation;
	private int colorOfBoids;
	private Form form;
	@Builder.Default
	private ColorMode colorMode = ColorMode.STATIC;
	@Builder.Default
	private CoopMode coopMode = CoopMode.ONLY_BY_SEED;
	//Wich color should be taken from the underlying image
	private CoopColorMode coopColorMode;
	//Should the color be adapted too?
	private boolean colorTransition; 
	//true if all elements should have the same size
	private boolean solidSize;
	// size or max size of all elements
	private float size;
	@Builder.Default
	private float maxForce = 1;
	@Builder.Default
	private float maxSpeed = 4;
	private FlyingObjectFormPrinter formproducer;

	private Swarm swarm;

	private Boid contextCoordinateBoid;
	@Builder.Default
	private boolean noStroke = true;
	private boolean staticSpeed;
	private boolean forceNoColorTanks;
	@Builder.Default
	private int fading = 0;
	
	private EdgeBehavior edgeBehavior;

	PGraphics[] pgGraphicsColorSpots;
	private Random randomGenerator;
	@Builder.Default
	private boolean rotate = true;
	@Builder.Default
	private boolean spiral = true;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (swarm == null) {
			init(context, pg);
		}
		if(edgeBehavior == EdgeBehavior.TELEPORTATION) {
			swarm.setPositionAdapter(edgeBehavior.inCaseOfEdge(pg.width, pg.height, context));
		}
		if (fading != 0) {
			fade(pg);
		}
		swarm.forEach(b -> formproducer.print(pg,b));
		if (coopColorMode != CoopColorMode.NO) {
			adaptColor(context, pg);
		}
		swarm.nextStep(cooperation(context, pg));
	}

	private void fade(PGraphics pg) {
		pg.loadPixels();
		int[] pixels = pg.pixels;
		for (int i = 0; i < pixels.length; i++) {
			int color = pixels[i];
			int alpha = Math.max(0, (color>>24 & 0xFF)-fading);
			pixels[i] = (color & 0xFFFFFF) | alpha<<24;
		}
		pg.updatePixels();
		
	}

	private void adaptColor(CooperationContext context, PGraphics pg) {
		if (!forceNoColorTanks) {
			drawColorTanks(context, pg);
		}
		
		PGraphics prevPG = context.getPrevCanvas();
		if (prevPG != null) {
			IntPredicate colorPredicate = coopColorMode.getPredicate();
			int[] pixels = prevPG.pixels;
			swarm.forEachParallel(boid -> {
				 if(boid.getX()<0 ||  boid.getX()>=prevPG.width || boid.getY()<0 || boid.getY()>=prevPG.height) {
					 return;
				 }
				
				int index = (int) boid.getX() + (int) boid.getY() * prevPG.width;
				if (index >= pixels.length) { // only with rounding problems
					return;
				}
				int color = pixels[index];
				if (colorPredicate.test(color)) {
					boid.setColor(color);
				}
			});
		}
	}

	private void drawColorTanks(CooperationContext context, PGraphics pg) {
		int size = 60;
		
		//create colorspots which can be used for color adaption
		if (pgGraphicsColorSpots == null) {
			rotate = randomGenerator.nextBoolean();
			spiral = randomGenerator.nextBoolean();
			
			pgGraphicsColorSpots = new PGraphics[4];
			for (int i = 0; i < pgGraphicsColorSpots.length; i++) {
				int fromColor = coopColorMode.getRandomColorInRange(randomGenerator);
				int toColor =  coopColorMode.getRandomColorInRange(randomGenerator);
				pgGraphicsColorSpots[i] = circleGradient(context.getPApplet().createGraphics(size * 2, size * 2), size, size, size,
						fromColor, toColor);
				
			}
		}
		pg.pushMatrix();
		pg.translate(pg.width / 2, pg.height / 2);
		if (rotate) {
			pg.pushMatrix();
			pg.rotate(context.getPApplet().frameCount * 5 * PApplet.PI / 360);
		}
		pg.imageMode(PConstants.CENTER);
		float distance = size * 2;
		
		if(spiral) {
			int cyclus = 280; 
			int currentPosInCycle = (context.getPApplet().frameCount+140) % cyclus;
			currentPosInCycle = currentPosInCycle - cyclus/2;
			if(currentPosInCycle<=0) {
				distance = distance - distance *1.5f*2*currentPosInCycle/cyclus; 
			}else {
				distance = distance + distance *1.5f*2*currentPosInCycle/cyclus; 
			}
		}
		pg.image(pgGraphicsColorSpots[0], -distance, -distance);
		pg.image(pgGraphicsColorSpots[1], distance, -distance);
		pg.image(pgGraphicsColorSpots[2], -distance, distance);
		pg.image(pgGraphicsColorSpots[3], distance, distance);
		pg.popMatrix();
		if (rotate) {
			pg.popMatrix();
		}
	}

	PGraphics circleGradient(PGraphics pg, int x, int y, float radius, int colorFrom, int colorTo) {
		pg.beginDraw();
		pg.ellipseMode(PConstants.RADIUS);
		pg.noStroke();
		pg.smooth();
//		System.out.println(Integer.toHexString(colorFrom) + " " + Integer.toHexString(colorTo));
		for (float r = radius; r > 1; r -= 1) {
			int alpha = 0xFF;
			if (r > radius - 4) {
				alpha = (int) (alpha - 50 * (4 - (radius - r)));
			}
			float map = PApplet.map(r, 0, radius, 0, 1);
			int lerpColor = PApplet.lerpColor(colorFrom, colorTo, map, PConstants.RGB);
			int color = (alpha << 24) | lerpColor;
//			System.out.println(Integer.toHexString(color));
//			System.out.println(alpha + " " + lerpColor);
			pg.fill(color);
			pg.ellipse(x, y, r, r);
		}
		pg.endDraw();
		return pg;
	}

	private void init(CooperationContext context, PGraphics pg) {
		formproducer = form.formPrinter();
		swarm = new Swarm(context.getSeed(), countOfBoids, maxSpeed, staticSpeed, pg.width, pg.height);
		swarm.setPositionAdapter(edgeBehavior.inCaseOfEdge(pg.width, pg.height, context));
		contextCoordinateBoid = swarm.findClosest(pg.width / 2, pg.height / 2);
		if (colorTransition) {
			swarm.setCalculateColor(true);
		}
		swarm.setAlignValue(alignment);
		swarm.setCohesionValue(cohesion);
		swarm.setSeperationValue(seperation);
		swarm.setPerceptionRadiusAlignment(radiusAlignment);
		swarm.setPerceptionRadiusCohesion(radiusCohesion);
		swarm.setPerceptionRadiusSeperation(radiusSeperation);
		swarm.setMaxForce(maxForce);
		randomGenerator = new Random(context.getSeed());

		swarm.forEach(b -> {
			b.setColor(colorMode.getColor(randomGenerator, colorOfBoids));
			if (solidSize) {
				b.setSize(size);
			} else {
				b.setSize(randomGenerator.nextFloat() * size + 0.1f);
			}
		});
	}

	private Function<Boid, PVector> cooperation(CooperationContext context, PGraphics pg) {
		int vecFactor = 1;
		DoubleUnaryOperator disFactor = d -> d / 100;

		switch (coopMode) {
		case COORDINATE_AVOIDANCE:
			vecFactor = -1;
			disFactor = d -> 100 / Math.max(Double.MIN_VALUE, d);
		case COORDINATE_ATTRACTION:
			PixelCoordinate coordinate = context.getCoordinate();
			float x = coordinate.x;
			float y = coordinate.y;
			final int vecFactorFinal = vecFactor;
			final DoubleUnaryOperator disFactorFinal = disFactor;
			final int minDistance = 400;
			return new Function<Boid, PVector>() {
				@Override
				public PVector apply(Boid boid) {
					PVector pVector = new PVector((float) (x - boid.getX()), (float) (y - boid.getY()));
					float distance = pVector.mag();
					if (minDistance > distance) {
						double distanceFactor = disFactorFinal.applyAsDouble(distance);
						pVector.normalize();
						pVector.mult((float) (vecFactorFinal * distanceFactor));
						return pVector;
					}
					return null;
				}
			};
		case ONLY_BY_SEED:
		default:
			break;
		}
		return null;
	}


	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		if (contextCoordinateBoid == null) {
			return cooperationContext.getCoordinate();
		}
		return new PixelCoordinate((float) contextCoordinateBoid.getX(), (float) contextCoordinateBoid.getY(), 0);
	}
}
