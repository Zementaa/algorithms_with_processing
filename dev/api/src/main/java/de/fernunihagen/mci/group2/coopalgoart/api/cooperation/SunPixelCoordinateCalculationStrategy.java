package de.fernunihagen.mci.group2.coopalgoart.api.cooperation;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class SunPixelCoordinateCalculationStrategy implements PixelCoordinateCalculationStrategy {
	private float alpha = 0;
	@Override
	public PixelCoordinate calculate(CooperationContext cooperationContext) {
		PApplet pApplet = cooperationContext.getPApplet();
		int min = Math.min(pApplet.width, pApplet.height);
		float r = min/3.f;
		
		alpha -= PApplet.PI/180;
		PVector pVector = PVector.fromAngle(alpha).mult(r).add(pApplet.width/2, pApplet.height);
		
		PixelCoordinate coordinate = cooperationContext.getCoordinate();
		coordinate.setX(pVector.x);
		coordinate.setY(pVector.y);
		return coordinate;
	}

}
