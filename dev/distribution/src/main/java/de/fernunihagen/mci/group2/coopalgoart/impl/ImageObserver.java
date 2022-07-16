package de.fernunihagen.mci.group2.coopalgoart.impl;

import processing.core.PGraphics;

public interface ImageObserver {
	
	public void onImageChange(int frame, PGraphics canvas);
	
	public default void exhibitClosed() {}; 

}
