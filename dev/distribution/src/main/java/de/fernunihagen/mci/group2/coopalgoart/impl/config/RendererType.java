package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import lombok.Getter;

/**
 * @author bwinzen
 *
 */
@Getter
public enum RendererType {
	JAVA2D("processing.awt.PGraphicsJava2D"), P2D("processing.opengl.PGraphics2D"),
	P3D("processing.opengl.PGraphics3D"), FX2D("processing.javafx.PGraphicsFX2D");

	private String processingConstant;

	RendererType(String processingConstant) {
		this.processingConstant = processingConstant;
	}

}
