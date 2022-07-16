package de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorFactory;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystemBuilder;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.PrecalculationableLSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.SimpleLSystems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

@Builder
public class LSystemGenerator implements Generator, PixelCoordinateCalculationStrategy {
	private List<LSystemTreeContainer> lSystemContainers;
	private String lSystemId;

	@Builder.Default
	private int treeCount = 1;
	@Builder.Default
	private float size = 1;
	@Builder.Default
	private int iteration = 5;
	@Builder.Default
	private int iterationdelay = 10;
	@Builder.Default
	private int alpha = 255;
	@Builder.Default
	private ColorMode colorMode = ColorMode.STATIC;
	@Builder.Default
	private CoopMode coopMode = CoopMode.ONLY_BY_SEED;
	private boolean solidSize;
	private boolean rotation;
	private boolean reverseMode;

	private int strokeWeight;

	private boolean forceClear;
	private int strokeColor;
	private Random randomGenerator;
	private LSystemBuilder lSystemBuilder;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (lSystemContainers == null) {
			init(context, pg);
		}

		int frameCount = context.getPApplet().frameCount;

		if ((frameCount - 1) % (iterationdelay + 1) == 0) {
			updateSystem(context, pg);
		} else if (context.isScreenWillBeCleaned()) {
			drawLSystem(context, pg);
		}
	}

	private void init(CooperationContext context, PGraphics pg) {
		randomGenerator = new Random(context.getSeed());
		lSystemContainers = new ArrayList<>(treeCount);
		for (int i = 0; i < treeCount; i++) {
			LSystemTreeContainer lsystemContainer = createTree(context, pg);
			try {
				if (lsystemContainer.tree instanceof PrecalculationableLSystemTree) {
					((PrecalculationableLSystemTree) lsystemContainer.tree).calculateUpToIteration(iteration);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			lsystemContainer.tree.getLSystem().setSeed(randomGenerator.nextLong());
			lSystemContainers.add(lsystemContainer);
		}
	}

	private void updateSystem(CooperationContext context, PGraphics pg) {
		if (reverseMode) {
			for (LSystemTreeContainer lsystemContainer : lSystemContainers) {
				resetSystemReverseMode(lsystemContainer, context, pg);
				drawLSystem(context, pg);
				lsystemContainer.tree = lsystemContainer.tree.previous();
			}
		} else {
			for (LSystemTreeContainer lsystemContainer : lSystemContainers) {
				resetSystem(lsystemContainer, context, pg);
				drawLSystem(context, pg);
				lsystemContainer.tree = lsystemContainer.tree.next();
			}
		}
//		System.out.println("Update done");
	}

	private void resetSystemReverseMode(LSystemTreeContainer lsystemContainer, CooperationContext context,
			PGraphics pg) {
		if (lsystemContainer.tree.getLSystem().getIterationCounter() == 0) {
			updateLSystemTreeContainer(lsystemContainer, context, pg);
			for (int i = 0; i < iteration; i++) {
				lsystemContainer.tree = lsystemContainer.tree.next();
			}
//			System.out.println("Reset done");
		}
	}

	private void resetSystem(LSystemTreeContainer lsystemContainer, CooperationContext context, PGraphics pg) {
		if (lsystemContainer.tree.getLSystem().getIterationCounter() == iteration) {
			updateLSystemTreeContainer(lsystemContainer, context, pg);
			while (lsystemContainer.tree.hasPrevious()) {
				lsystemContainer.tree = lsystemContainer.tree.previous();
			}
//			System.out.println("Reset done");
		}
	}

	private void drawLSystem(CooperationContext context, PGraphics pg) {
//		pg.translate(pg.width / 2, pg.height);
//		pg.stroke(strokeColor);
		pg.strokeWeight(strokeWeight);
		for (LSystemTreeContainer lsystemContainer : lSystemContainers) {
			pg.resetMatrix();
			LSystemTree tree = lsystemContainer.tree;
			int color = 0;
			if (colorMode == ColorMode.STEAL_COLOR) {
				color = stealColor(context, lsystemContainer);
			} else {
				color = colorMode.getColor(randomGenerator, strokeColor);
			}
			color = alpha << 24 | (0xFFFFFF & color);
			if (tree.isMultiColorAble()) {
				int color2 = colorMode.getColor(randomGenerator, color);
				color2 = alpha << 24 | (0xFFFFFF & color2);
				tree.setColorFactory(
						ColorFactory.circleGradientColorFactory(color, color2, new PVector(), pg.width / 2));
			} else {
				pg.stroke(color);
				pg.fill(color);
			}
			pg.translate(lsystemContainer.x + tree.getOffsetX(), lsystemContainer.y + tree.getOffsetY());

			if (rotation) {
				pg.rotate(lsystemContainer.angle);
			}
			lsystemContainer.tree.draw(context, pg);
		}
	}

	private int stealColor(CooperationContext context, LSystemTreeContainer lsystemContainer) {
		PGraphics prevCanvas = context.getPrevCanvas();
		if (prevCanvas == null) {
			return 0;
		}
		LSystemTree tree = lsystemContainer.tree;
		prevCanvas.loadPixels();
		int index = Math.max(0, Math.min(prevCanvas.width - 1, (int) lsystemContainer.x + tree.getOffsetX()))
				+ Math.max(0, Math.min(prevCanvas.height - 1, (int) lsystemContainer.y + tree.getOffsetY()))
						* prevCanvas.width;

		return prevCanvas.pixels[index];
	}

	public LSystemTreeContainer createTree(CooperationContext context, PGraphics pg) {
		LSystemTree lSystemTree = null;
		float treeSize = solidSize ? size : size * randomGenerator.nextFloat();
		if (lSystemId == null) {
			lSystemTree = lSystemBuilder.buildNonRecursiveTree(treeSize);
		} else if (lSystemId.equalsIgnoreCase("Coding Train Beispiel")) {
			lSystemTree = SimpleLSystems.createCodingTrainExample(treeSize, (float) Math.PI / 6);
		} else if (lSystemId.equalsIgnoreCase("Geordnetes Chaos")) {
			lSystemTree = SimpleLSystems.createOrderedChaos(treeSize, (float) Math.PI / 6);
		} else if (lSystemId.equalsIgnoreCase("Spiralen")) {
			lSystemTree = SimpleLSystems.createSpiral(treeSize, (float) Math.PI / 8);
		} else if (lSystemId.equalsIgnoreCase("Kochkurve")) {
			lSystemTree = SimpleLSystems.createKochkurve(treeSize);
		} else if (lSystemId.equalsIgnoreCase("Mutterbaum")) {
			lSystemTree = SimpleLSystems.createLeafs(treeSize);
		} else if (lSystemId.equalsIgnoreCase("Ein Baum")) {
			lSystemTree = SimpleLSystems.createTree1Example(treeSize);
		}else if (lSystemId.equalsIgnoreCase("Sierpinski Arrow Head")) {
			lSystemTree = SimpleLSystems.createSiepinskiArrowHeadCurveExample(treeSize);
		}else if (lSystemId.equalsIgnoreCase("Hilbert")) {
			lSystemTree = SimpleLSystems.createHilbertCurveExample(treeSize);
		}
		LSystemTreeContainer lSystemTreeContainer = new LSystemTreeContainer(lSystemTree,
				randomGenerator.nextFloat() * pg.width, randomGenerator.nextFloat() * pg.height,
				randomGenerator.nextFloat() * 2 * PApplet.PI);
//		System.out.println("Create Tree: " + lSystemTreeContainer.x + " " + lSystemTreeContainer.y);
		updateLSystemTreeContainer(lSystemTreeContainer, context, pg);
		return lSystemTreeContainer;
	}

	private void updateLSystemTreeContainer(LSystemTreeContainer lSystemTreeContainer, CooperationContext context,
			PGraphics pg) {
		if (coopMode == CoopMode.ONLY_BY_SEED) {
			return;
		}
		float x = randomGenerator.nextFloat() * pg.width;
		float y = randomGenerator.nextFloat() * pg.height;

		float angle = randomGenerator.nextFloat() * 2 * PApplet.PI;
		PixelCoordinate coordinate = context.getCoordinate();
		PVector coopCoordinate = new PVector(coordinate.x, coordinate.y);
		PVector pVectorAxe = new PVector(0, 1);
		PVector distanceVector = new PVector(coordinate.x - x, coordinate.y - y);
		float angleBetween = PVector.angleBetween(coopCoordinate, pVectorAxe);

		switch (coopMode) {
		case COORDINATE_ATTRACTION:
			if (rotation) {
				if (Math.abs(angle - angleBetween) < PApplet.PI / 4) {
					if (angle < angleBetween) {
						angle -= angleBetween;
					} else {
						angle += angleBetween;
					}
				}
			}
			float minDistance = pg.width / 4.f;
			if (distanceVector.mag() > minDistance) {
				distanceVector.mult(minDistance * (randomGenerator.nextFloat()) / distanceVector.mag());
				x = coopCoordinate.x - distanceVector.x;
				y = coopCoordinate.y - distanceVector.y;
				if (x < 0 || x > pg.width) {
					x = coopCoordinate.x + distanceVector.x;
				}
				if (y < 0 || y > pg.height) {
					y = coopCoordinate.y + distanceVector.y;
				}
			}

			break;
		case COORDINATE_AVOIDANCE:
			if (rotation) {
				if (Math.abs(angle - angleBetween) > PApplet.PI / 2) {
					if (angle < angleBetween) {
						angle += angleBetween;
					} else {
						angle -= angleBetween;
					}
				}
			}
			float maxDistance = pg.width / 2.f;
			if (distanceVector.mag() < maxDistance) {
				distanceVector.mult(maxDistance * (randomGenerator.nextFloat() * randomGenerator.nextFloat() + 1)
						/ distanceVector.mag());
				x = coopCoordinate.x - distanceVector.x;
				y = coopCoordinate.y - distanceVector.y;
				if (x < 0 || x > pg.width) {
					x = coopCoordinate.x + distanceVector.x;
				}
				if (y < 0 || y > pg.height) {
					y = coopCoordinate.y + distanceVector.y;
				}
			}

			break;
		default:
			return;

		}
		lSystemTreeContainer.x = x;
		lSystemTreeContainer.y = y;
		lSystemTreeContainer.angle = angle;

//		System.out.println(lSystemTreeContainer.x + " " + lSystemTreeContainer.y);
	}

	@Override
	public boolean forceClearScreen() {
		boolean tmpForceClear = forceClear;
		forceClear = false;
		return tmpForceClear;
	}

	@AllArgsConstructor
	private static class LSystemTreeContainer {
		LSystemTree tree;
		float x;
		float y;
		float angle;
	}

	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		if (lSystemContainers != null) {
			LSystemTreeContainer c = lSystemContainers.get(0);
			LSystemTree tree = c.tree;
			return new PixelCoordinate(c.x+ tree.getOffsetX(), c.y+ tree.getOffsetY(), 0);
		}
		return cooperationContext.getCoordinate();
	}
}
