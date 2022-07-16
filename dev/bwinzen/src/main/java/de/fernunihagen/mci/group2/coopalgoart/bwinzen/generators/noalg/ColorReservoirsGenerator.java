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
public class ColorReservoirsGenerator implements Generator, PixelCoordinateCalculationStrategy, UIInteractionProvider {
	private Form form;
	@Builder.Default
	private ColorMode colorMode = ColorMode.STATIC;
	//Wich color should be taken from the underlying image
	private CoopColorMode coopColorMode;
	
	private FlyingObjectFormPrinter formproducer;
	
	private float speed;
	@Builder.Default
	private int fading = 0;

	PGraphics[] pgGraphicsColorSpots;
	private Random randomGenerator;
	@Builder.Default
	private boolean rotate = true;
	@Builder.Default
	private boolean spiral = true;

	private boolean initialized;
	@Builder.Default
	private PVector center = new PVector();
	@Builder.Default
	private int countOfTanks = 8;
	@Builder.Default
	private int size = 60;
	
	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (!initialized) {
			init(context, pg);
			initialized = true;
		}
		if (fading != 0) {
			fade(pg);
		}
		drawColorTanks(context, pg);
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

	private void drawColorTanks(CooperationContext context, PGraphics pg) {
		
		//create colorspots which can be used for color adaption
		if (pgGraphicsColorSpots == null) {
			pgGraphicsColorSpots = new PGraphics[countOfTanks];
			int[][] colors = createColors();
			for (int i = 0; i <countOfTanks; i++) {
				pgGraphicsColorSpots[i] = formGradient(context.getPApplet().createGraphics(size * 2, size * 2), size, size, size,
						colors[i][0], colors[i][1]);
				
			}
		}
		pg.pushMatrix();
		pg.translate(pg.width / 2, pg.height / 2);
		if (rotate) {
			pg.pushMatrix();
			pg.rotate(context.getPApplet().frameCount * speed);
		}
		pg.imageMode(PConstants.CENTER);
		float baseDistance = size * 2;
		float distance = pg.width/3;
		if(spiral) {
			int cyclus = 280; 
			int currentPosInCycle = (context.getPApplet().frameCount+140) % cyclus;
			currentPosInCycle = currentPosInCycle - cyclus/2;
			if(currentPosInCycle<=0) {
				distance = baseDistance - distance *1.5f*2*currentPosInCycle/cyclus; 
			}else {
				distance = baseDistance + distance *1.5f*2*currentPosInCycle/cyclus; 
			}
		}
		float angle = PConstants.TWO_PI/pgGraphicsColorSpots.length;
		PVector pVector = new PVector(-distance, -distance);
		for(int i = 0; i< countOfTanks; i++) {
			pg.image(pgGraphicsColorSpots[i],pVector.x, pVector.y);
			pVector.rotate(angle);
		}
		pg.popMatrix();
		if (rotate) {
			pg.popMatrix();
		}
	}

	private int[][] createColors() {
		int[][] colors = new int[countOfTanks][2];
		for (int i = 0; i < countOfTanks; i++) {
		int fromColor = coopColorMode.getRandomColorInRange(randomGenerator);
		int toColor =  coopColorMode.getRandomColorInRange(randomGenerator);
		colors[i][0] = fromColor;
		colors[i][1] = toColor;
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
		formproducer = form.formPrinter();
		randomGenerator = new Random(context.getSeed());
		center.set(pg.width/2, pg.height/2);
	}

	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		return new PixelCoordinate((float) center.x, (float) center.y, 0);
	}
	
	@Override
	public void keyPressed(KeyEvent t) {
		if (t.getKeyCode() == 97 || t.getKeyCode() == 112) { // f1-Key
			pgGraphicsColorSpots = null;
		}
	}
}
