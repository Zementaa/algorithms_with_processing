package de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.noalg;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.api.ui.UIInteractionProvider;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.KeyEvent;

/**
 * @author bwinzen
 *
 */
@Builder
public class CooperationTestGenerator implements Generator, PixelCoordinateCalculationStrategy, UIInteractionProvider {
	private int x;
	private int y;

	private Random randomGenerator;
	@Builder.Default
	private int size = 50;
	@Builder.Default
	private boolean dirty = true;
	PGraphics cachedCircle;
	@Builder.Default
	char channelChange = ' ';
	@Builder.Default
	int colorFrom = 0xFF0000;
	@Builder.Default
	int colorTo = 0xFFFF00;
	@Builder.Default
	private char colorChoice = 'f';

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (randomGenerator == null) {
			randomGenerator = new Random(context.getSeed());
		}
		if (dirty) {
			cachedCircle = context.getPApplet().createGraphics(size * 2, size * 2);
			circleGradient(cachedCircle, size, size, size, colorFrom, colorTo);
		}

		pg.image(cachedCircle, x - size, y - size);

//		  for (int i = 1; i < 150; i++) {
//			  pg.fill(0xFFi*2);
//			  pg.ellipse(150, 150, 300-(2*i), 300-(2*i));
//		  }
//		pg.fill(0xFFFF0000);
//		pg.ellipse(x, y, 20, 20);
	}

	void circleGradient(PGraphics pg, int x, int y, float radius, int colorFrom, int colorTo) {
		pg.beginDraw();
		pg.ellipseMode(PApplet.RADIUS);
		pg.noStroke();
		for (float r = radius; r > 1; r -= 1) {
			int alpha = 0xFF;
//			if (r > radius - 20) {
//				alpha = (int) (alpha - 12 * (20 - (radius - r)));
//			}
			float map = PApplet.map(r, 0, radius, 0, 1);
			int lerpColor = PApplet.lerpColor(colorFrom, colorTo, map, PConstants.RGB);
			int color = (alpha << 24) | lerpColor;
//        	System.out.println(alpha+" "+lerpColor);
			pg.fill(color);
			pg.ellipse(x, y, r, r);
		}
		pg.endDraw();
	}

	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		return new PixelCoordinate((float) x, (float) y, 0);
	}

	@Override
	public void keyReleased(KeyEvent ev) {
		char keyCode = ("" + ((char) ev.getKeyCode())).toLowerCase().charAt(0);
		if (keyCode == 'r' || keyCode == 'g' || keyCode == 'b') {
			channelChange = keyCode;
		} else if (keyCode == 't' || keyCode == 'f') {
			colorChoice = keyCode;
		}
	}

	@Override
	public void keyPressed(KeyEvent ev) {
		int keyCode = ev.getKeyCode();
		if (keyCode == 139) {
			add(10);
			dirty = true;
		} else if (keyCode == 140) {
			add(-10);
			dirty = true;
		}
	}

	private void add(int num) {
		if (channelChange != 'r' && channelChange != 'g' && channelChange != 'b') {
			return;
		}
		int color = colorFrom;
		if (colorChoice == 't') {
			color = colorTo;
		}
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;
		switch (channelChange) {
		case 'r':
			r = Math.min(0xFF, Math.max(0, r + num));
			break;
		case 'g':
			g = Math.min(0xFF, Math.max(0, g + num));
			break;
		case 'b':
			b = Math.min(0xFF, Math.max(0, b + num));
			break;
		}
		color = (color & 0xFF_000000) | r << 16 | g << 8 | b;
		if (colorChoice == 't') {
			colorTo = color;
		} else {
			colorFrom = color;
		}
	}
}
