package de.fernunihagen.mci.group2.coopalgoart.api.ui;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * @author bwinzen
 *
 */
public interface UIInteractionProvider {
	default void keyPressed(KeyEvent ev) {}
	
	default void keyReleased(KeyEvent ev) {}
	
	default void mouseDragged(MouseEvent ev) {}
	
	default void mouseMoved(MouseEvent event) {}
	
	default void mouseClicked(MouseEvent event) {}

	default void mouseWheel(MouseEvent event) {}
}
