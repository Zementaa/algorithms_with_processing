package de.fernunihagen.mci.group2.coopalgoart.api;

import processing.core.PGraphics;

/**
 * @author bwinzen
 *
 */
public interface Generator {
	
	/**
	 * Draws the next step on the applet;
	 * @param context 
	 * @param pg
	 */
	void nextStep(CooperationContext context, PGraphics pg);

	default boolean forceClearScreen() {
		return false;
	}

	default boolean use3D() {
		return false;
	}
}
