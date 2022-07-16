package de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.noalg;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.api.ui.UIInteractionProvider;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form.FlyingObjectFormPrinter;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.KeyEvent;

/**
 * @author bwinzen
 *
 */
@Builder
public class ColorBorderGenerator implements Generator, PixelCoordinateCalculationStrategy, UIInteractionProvider {
	// Wich color should be taken from the underlying image
	@Builder.Default
	private CoopColorMode coopColorMode = CoopColorMode.HIGH_GREEN;
	private Random randomGenerator;

	private boolean initialized;
	@Builder.Default
	private PVector center = new PVector();
	@Builder.Default
	private int countOfElements = 4;
	@Builder.Default
	private int weight = 40;

	private PGraphics cached;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (!initialized) {
			init(context, pg);
			initialized = true;
		}
		drawColorTanks(context, pg);
	}

	private void drawColorTanks(CooperationContext context, PGraphics pg) {

		// create colorspots which can be used for color adaption
		if (cached == null) {
			cached = context.getPApplet().createGraphics(pg.width, pg.height);
			cached.beginDraw();

			for (int j = 0; j < 4; j++) {
				int[] colors = createColors();
				float length = (j % 2 == 0 ? pg.width : pg.height) / countOfElements;
				cached.pushMatrix();
			if(j == 1) {
				cached.translate(pg.width, 0);
			}else if(j == 2) {
				cached.translate(pg.width, pg.height);
			}else if(j == 3) {
				cached.translate(0, pg.height);
			}
			cached.rotate(PApplet.HALF_PI * j);
				
				for (int i = 0; i < countOfElements; i++) {
					cached.fill(colors[i]);
					cached.beginShape();
					if (i == 0) {
						cached.vertex(length * i, 0);
						cached.vertex(weight, weight);
					} else {
						cached.vertex(length * i, 0);
						cached.vertex(length * i, weight);
					}

					if (i == countOfElements - 1) {
						cached.vertex(length * (i + 1) - weight, weight);
						cached.vertex(length * (i + 1), 0);
					} else {
						cached.vertex(length * (i + 1), weight);
						cached.vertex(length * (i + 1), 0);
					}
					cached.endShape();
				}
				cached.popMatrix();
			}
			cached.endDraw();
		}
		pg.image(cached, 0, 0);
	}

	private int[] createColors() {
		int[] colors = new int[countOfElements];
		for (int i = 0; i < countOfElements; i++) {
			colors[i] = 0xFF_000000 | coopColorMode.getRandomColorInRange(randomGenerator);
		}
		return colors;
	}

	PGraphics formGradient(PGraphics pg, int x, int y, float radius, int colorFrom, int colorTo) {
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
		randomGenerator = new Random(context.getSeed());
		center.set(pg.width / 2, pg.height / 2);
	}

	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		return new PixelCoordinate((float) center.x, (float) center.y, 0);
	}

	@Override
	public void keyPressed(KeyEvent t) {
		if (t.getKeyCode() == 97 || t.getKeyCode() == 112) { // f1-Key
			cached = null;
		}
	}
}
