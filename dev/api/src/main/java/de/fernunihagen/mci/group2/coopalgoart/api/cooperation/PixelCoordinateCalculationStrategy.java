package de.fernunihagen.mci.group2.coopalgoart.api.cooperation;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;

/**
 * @author bwinzen
 *
 */
public interface PixelCoordinateCalculationStrategy {
	/**
	 * @param cooperationContext
	 * @return
	 */
	PixelCoordinate calculate(CooperationContext cooperationContext);
}
