package de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public interface ColorFactory {

	int color(PVector position1, PVector position2);

	public static ColorFactory singleColorFactory(int color) {
		return new ColorFactory() {
			@Override
			public int color(PVector p1, PVector p2) {
				return color;
			}
		};
	}

	public static ColorFactory circleGradientColorFactory(int colorFrom, int colorTo, PVector start, float distance) {
		return new ColorFactory() {
			@Override
			public int color(PVector p1, PVector p2) {
				int color1 = circleGradient(p1.x, p1.y, p1.copy().sub(start).mag());
				int color2 = circleGradient(p2.x, p2.y, p2.copy().sub(start).mag());
				return (0xFF_000000 & colorFrom) | PApplet.lerpColor(color1, color2, 0.5f, PConstants.RGB);
			}

			int circleGradient(float x, float y, float radius) {
				float map = PApplet.map(radius, 0, distance, 0, 1);
				return PApplet.lerpColor(colorFrom, colorTo, map, PConstants.RGB);
			}
		};
	}

}
