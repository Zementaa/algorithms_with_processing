package de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.api.ui.UIInteractionProvider;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.imageevolution.PrioQueue;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.imageevolution.PrioQueue.EvolutionContainer;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.FlyingObject;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form.FlyingObjectFormPrinter;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

/**
 * Generator which uses evolution principles do draw a picture.
 * 
 * @author bwinzen
 *
 */
@Builder
public class ImageEvolution3dGenerator implements Generator, PixelCoordinateCalculationStrategy, UIInteractionProvider {
	private String imagePath;
	private String mode;
	@Builder.Default
	private ColorMode colorMode = ColorMode.RANDOM;
	private int color;
	@Builder.Default
	private CoopMode coopMode = CoopMode.ONLY_BY_SEED;
	@Builder.Default
	private CoopMode coopModeShots = CoopMode.ONLY_BY_SEED;

	private boolean solidSize;
	@Builder.Default
	private float size = 2.f;
	@Builder.Default
	private float elementSpeed = 10.f;

	private float perlinoiseItStep;

	private Form form;

	@Builder.Default
	private int distanceOfImage = -100;

	@Builder.Default
	private float maxZSpeed = 10;

	@Builder.Default
	private int maxShots = 10000;

	private int[] targetPixels;
	private FitnessCheck fitnessCheck;

	@Builder.Default
	private List<Shot> shots = new LinkedList<>();
	private PImage targetImage;
	private PGraphics currentStateImage;

	@Builder.Default
	private PApplet pAppletForNoise = new PApplet();
	private Random randomGenerator;
	private float xFirePosition;
	private float yFirePosition;

	private float leftBorder;
	private float rightBorder;
	private float upperBorder;
	private float lowerBorder;

	@Builder.Default
	private boolean shooterEvolution = false;
	private boolean shooterStopped;

	private int iterationCounter;

	private FlyingObjectFormPrinter formPrinter;

	private int width;
	private int height;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (targetImage == null) {
			init(context, pg);
			float bufferSize = 0.2f;
			leftBorder = -pg.width * bufferSize;
			rightBorder = pg.width + pg.width * bufferSize;
			upperBorder = -pg.height * bufferSize;
			lowerBorder = pg.height + pg.height * bufferSize;
		}
//		System.out.println(queue.size());
		PixelCoordinate coordinate = context.getCoordinate();
		fillShots(context, pg, coordinate);
		setPositionOfShooter(0, 10, context, pg);
		List<Shot> drawList = Collections.synchronizedList(new LinkedList<>());

		if (!shooterStopped) {
//			OptionalInt min = balls.stream().mapToInt(s -> (int) s.position.z).min();
//			if (min.isPresent()) {
//				System.out.println(min);
//			}
			shots
//			.parallelStream() //to make it deterministic parallel is not allowed
					.forEach(s -> {
						s.addVelocity();
						if (s.velocity.z >= 0) {
							System.out.println(s.velocity.z);
						}
						if (s.position.x < leftBorder || s.position.x >= rightBorder || s.position.y < upperBorder
								|| s.position.y >= lowerBorder) {
							s.init(xFirePosition, yFirePosition, coordinate.x, coordinate.y);
						} else if (s.position.z < distanceOfImage) {
							if (s.position.x >= 0 && s.position.x < pg.width && s.position.y >= 0
									&& s.position.y < pg.height) {
								drawList.add(s);
							} else {
								s.init(xFirePosition, yFirePosition, coordinate.x, coordinate.y);
							}
						}
					});
		}
		currentStateImage.beginDraw();
		currentStateImage.ellipseMode(PConstants.RADIUS);
		currentStateImage.rectMode(PConstants.CENTER);
//		System.out.println(drawList.size());
		currentStateImage.loadPixels();
		int[] currentPixels = currentStateImage.pixels;
		for (Shot s : drawList) {
			drawOnCurrentImage(s, pg.width, pg.height, currentPixels, coordinate);
		}
		currentStateImage.endDraw();
//		pg.image(targetImage, 0, 0);
		pg.image(currentStateImage, 0, 0);
		if (!shooterStopped) {
			PGraphics buffer = context.getPApplet().createGraphics(pg.width, pg.height, PApplet.P3D);
			buffer.beginDraw();
			buffer.ellipseMode(PConstants.RADIUS);
			buffer.rectMode(PConstants.CENTER);
			for (Shot s : shots) {
				buffer.pushMatrix();
				buffer.translate(0, 0, s.position.z);
				formPrinter.print(buffer, s);
				buffer.popMatrix();
//				renderCircle(buffer, s);
			}
			buffer.endDraw();
			pg.image(buffer, 0, 0);
		}
	}

	private void setPositionOfShooter(float x1, float x2, CooperationContext context, PGraphics pg) {
		iterationCounter++;
		xFirePosition = (int) (pAppletForNoise.noise(x1, iterationCounter * perlinoiseItStep) * pg.width);
		yFirePosition = (int) (pAppletForNoise.noise(x2, iterationCounter * perlinoiseItStep) * pg.height);
		if (coopMode != CoopMode.ONLY_BY_SEED) {
			PixelCoordinate coopCoord = context.getCoordinate();
			PVector coopCoordinate = new PVector(coopCoord.x, coopCoord.y);
			PVector distanceVector = new PVector(coopCoord.x - xFirePosition, coopCoord.y - yFirePosition);

			switch (coopMode) {
			case COORDINATE_ATTRACTION:
				float minDistance = pg.width / 4.f;
				if (distanceVector.mag() > minDistance) {
					distanceVector.mult(minDistance * (randomGenerator.nextFloat()) / distanceVector.mag());
					xFirePosition = coopCoordinate.x - distanceVector.x;
					yFirePosition = coopCoordinate.y - distanceVector.y;
				}

				break;
			case COORDINATE_AVOIDANCE:
				float maxDistance = pg.width / 2.f;
				if (distanceVector.mag() < maxDistance) {
					distanceVector.mult(maxDistance * (randomGenerator.nextFloat() + 1) / distanceVector.mag());
					xFirePosition = coopCoordinate.x - distanceVector.x;
					yFirePosition = coopCoordinate.y - distanceVector.y;
				}

				break;
			default:
				break;

			}

			xFirePosition = (int) Math.min(pg.width - 1, Math.max(0, xFirePosition));
			yFirePosition = (int) Math.min(pg.height - 1, Math.max(0, yFirePosition));
		}
		if (colorMode == ColorMode.STEAL_COLOR) {
			stealColor(context);
		}
	}

	private void stealColor(CooperationContext context) {
		PGraphics prevCanvas = context.getPrevCanvas();
		if (prevCanvas == null) {
			return;
		}
		prevCanvas.loadPixels();
		int index = (int) xFirePosition + (int) yFirePosition * prevCanvas.width;
		color = prevCanvas.pixels[index];
	}

	private void drawOnCurrentImage(Shot s, int width, int height, int[] currentPixels, PixelCoordinate coordinate) {
		int screenX = (int) currentStateImage.screenX(s.position.x, s.position.y, s.position.z);
		int screenY = (int) currentStateImage.screenY(s.position.x, s.position.y, s.position.z);
		if (screenX < 0 || screenX >= width || screenY < 0 || screenY >= height) {
			return;
		}
		int index = screenX + screenY * currentStateImage.width;
		int currentPixel = currentPixels[index];
		int targetPixel = targetPixels[index];

		int currentFitness = fitnessCheck.checkFitness(null, currentPixel, targetPixel);
		if (currentPixel >> 24 == 0) {
			currentFitness += 255;
		}
		int newFitness = fitnessCheck.checkFitness(null, s.color, targetPixel);
		if (currentFitness >= newFitness) {
			currentStateImage.pushMatrix();
			currentStateImage.translate(0, 0, s.position.z);
			formPrinter.print(currentStateImage, s);
			currentStateImage.popMatrix();
//			System.out.print("("+(targetPixel>>16&0xFF )+","+(targetPixel>>8&0xFF)+","+(targetPixel&0xFF)+") : " );
//			System.out.print(currentFitness+"("+(currentPixel>>16&0xFF )+","+(currentPixel>>8&0xFF)+","+(currentPixel&0xFF) +")->");
//			System.out.println(newFitness+"("+(s.color>>16&0xFF )+","+(s.color>>8&0xFF)+","+(s.color&0xFF)+")" );
		}
		if (shooterEvolution) {
			s.initWithEvolution(xFirePosition, yFirePosition, coordinate.x, coordinate.y, currentFitness, newFitness);
		} else {
			s.init(xFirePosition, yFirePosition, coordinate.x, coordinate.y);
		}

	}

	private void init(CooperationContext context, PGraphics pg) {
		width = pg.width;
		height = pg.height;
		randomGenerator = new Random(context.getSeed());
		pAppletForNoise.noiseSeed(context.getSeed());
		pAppletForNoise.randomSeed(context.getSeed());
		formPrinter = form.formPrinter();
		float minFramesToHit = 10;
		float framesToMoveOverScreen = Math.abs(pg.width / 8 / elementSpeed);
		framesToMoveOverScreen = Math.min(framesToMoveOverScreen, minFramesToHit);
		maxZSpeed = Math.abs(distanceOfImage / framesToMoveOverScreen);

		fitnessCheck = FitnessCheck.find(mode);

		PApplet contextPApplet = context.getPApplet();
		PImage img = contextPApplet.loadImage(imagePath);
		float xOffset = 0;
		float yOffset = 0;

		// fill most of the graphic container without losing a pixel
		if (pg.width / (float) pg.height <= img.width / (float) img.height) {
			if (img.width > img.height) {
				float scale = ((float) pg.width) / img.width;
				yOffset = (img.width - img.height) / 2f * scale + (pg.height - pg.width) / 2f;
			} else {
				float scale = ((float) pg.height) / img.height;
				xOffset = (img.height - img.width) / 2f * scale + (pg.width - pg.height) / 2f;
			}
		} else {
			if (img.width < img.height) {
				float scale = ((float) pg.width) / img.width;
				yOffset = (img.width - img.height) / 2f * scale + (pg.height - pg.width) / 2f;
			} else {
				float scale = ((float) pg.height) / img.height;
				xOffset = (img.height - img.width) / 2f * scale + (pg.width - pg.height) / 2f;
			}
		}

		PGraphics targetImage = contextPApplet.createGraphics(pg.width, pg.height, PApplet.P3D);
		targetImage.beginDraw();
		targetImage.background(0xFF << 24);
		targetImage.noStroke();
		targetImage.textureMode(PApplet.NORMAL);
		targetImage.beginShape();
		targetImage.texture(img);
		targetImage.vertex(xOffset, yOffset, distanceOfImage, 0, 0);
		targetImage.vertex(pg.width - xOffset, yOffset, distanceOfImage, 1, 0);
		targetImage.vertex(pg.width - xOffset, pg.height - yOffset, distanceOfImage, 1, 1);
		targetImage.vertex(xOffset, pg.height - yOffset, distanceOfImage, 0, 1);
		targetImage.endShape();
		targetImage.endDraw();
		this.targetImage = targetImage.get();
		targetPixels = targetImage.pixels;

		currentStateImage = contextPApplet.createGraphics(pg.width, pg.height, PApplet.P3D);
		currentStateImage.beginDraw();
		currentStateImage.background(0x080808);
		currentStateImage.endDraw();
	}

	private void fillShots(CooperationContext context, PGraphics pg, PixelCoordinate coordinate) {
		if (shots.size() >= maxShots) {
			return;
		}
		int iterationSize = 10;
		int maxIterations = Math.max(1, (int) ((maxShots / iterationSize) // PositionChanges to fill
				/ (Math.abs(distanceOfImage) / maxZSpeed))); // Iterations for first hit
		for (int j = 0; shots.size() < maxShots && j < maxIterations; j++) {
			setPositionOfShooter(0 + j * 0.01f, 10 + j * 0.01f, context, pg);
			for (int i = 0, end = Math.min(maxShots - shots.size(), iterationSize); i < end; i++) {
				shots.add(new Shot(xFirePosition, yFirePosition, coordinate.x, coordinate.y));
			}
		}
	}

	@Override
	public boolean use3D() {
		return true;
	}

	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		return new PixelCoordinate(xFirePosition, yFirePosition, 0);
	}

	@Override
	public void keyPressed(KeyEvent t) {
		if (t.getKeyCode() == 97) { // f1-Key
			shooterStopped = !shooterStopped;
		}
	}

	private static int maxCapacity = 5000;
	@Builder.Default
	private PrioQueue queue = new PrioQueue(maxCapacity);

	/**
	 * Single shot in Animation. Will be recycled.
	 * @author bwinzen
	 *
	 */
	private class Shot extends FlyingObject {
		public Shot(float x, float y, float coopCoordX, float coopCoordY) {
			init(x, y, coopCoordX, coopCoordY);
		}

		public void init(float x, float y, float coopCoordX, float coopCoordY) {
			setPosition(x, y);

			setSize();
			this.color = colorMode.getColor(randomGenerator, ImageEvolution3dGenerator.this.color);

			PVector tmpVec = PVector.fromAngle(randomGenerator.nextFloat() * 2 * PApplet.PI);
			switch (coopModeShots) {
			case COORDINATE_ATTRACTION: { // A pixel in 50 distance from attractionPoint
				float factorForDistancePointer = 50 * randomGenerator.nextFloat();
				setVelocity(x, y, coopCoordX, coopCoordY, tmpVec, factorForDistancePointer);
			}
				break;
			case COORDINATE_AVOIDANCE: {
				float factorForDistancePointer = 100 + Math.max(width, height) / 2 * randomGenerator.nextFloat();
				setVelocity(x, y, coopCoordX, coopCoordY, tmpVec, factorForDistancePointer);
			}
				break;
			case ONLY_BY_SEED:
			default:
				tmpVec.setMag(randomGenerator.nextFloat() * elementSpeed * 5);
				this.velocity.x = tmpVec.x;
				this.velocity.y = tmpVec.y;
				this.velocity.z = -(randomGenerator.nextFloat() * (maxZSpeed - 1) + 1);
				break;

			}
		}

		public void initWithEvolution(float x, float y, float coopCoordX, float coopCoordY, int lastFitness,
				int newFitness) {
			EvolutionContainer evContainer = null;
			if (lastFitness > newFitness) {
				float correcture = (position.z + 100f) / velocity.z; //shift point on z axis & correct calculation errors. 
				evContainer = new EvolutionContainer(position.copy().add(velocity.mult(-correcture)), color,
						newFitness);
			}
			if (lastFitness - 5 > newFitness && newFitness < 50) {
				int end = 1;
				if (newFitness < 5) {
					end = 3;
				} else if (newFitness < 30) {
					end = 2;
				}
				if (lastFitness - newFitness > 50) {
					end++;
				}

				for (int i = 0; i < end; i++) {
					queue.offer(evContainer);
				}
			}
			if (evContainer == null) {
				evContainer = queue.poll();
				if (evContainer == null) {
					init(x, y, coopCoordX, coopCoordY);
					return;
				}
			}
			PVector lastTarget = evContainer.target;
			setSize();
			setPosition(x, y);

			float zSpeed = -(randomGenerator.nextFloat() * (maxZSpeed - 1) + 1);
			PVector div = lastTarget.copy()
					.add(randomGenerator.nextFloat() * size * 2 - size, randomGenerator.nextFloat() * size * 2 - size)
					.sub(x, y, 0);
			float factor = div.z / zSpeed;
			div.div(factor);
			div.z = zSpeed;
			this.velocity = div;
			this.color = colorMode.varColor(randomGenerator, evContainer.color, 30);
		}

		private void setVelocity(float x, float y, float coopCoordX, float coopCoordY, PVector tmpVec,
				float factorForDistancePointer) {
			tmpVec.mult(factorForDistancePointer) //
					.add(coopCoordX, coopCoordY);
			velocity.z = -(randomGenerator.nextFloat() * (maxZSpeed - 1) + 1);
			float factor = distanceOfImage / velocity.z;
			tmpVec.sub(x, y).div(factor);
			velocity.x = tmpVec.x;
			velocity.y = tmpVec.y;
		}

		private void setSize() {
			this.size = solidSize ? ImageEvolution3dGenerator.this.size
					: randomGenerator.nextFloat() * (ImageEvolution3dGenerator.this.size - 0.5f) + 0.5f;
		}
	}

}
