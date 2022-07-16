package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import processing.core.PGraphics;

public class Rotate extends VisualizationRule{

	private float rotation;

	public Rotate(AlphabetLetter input, float rotation) {
		super(input);
		this.rotation = rotation;
	}

	@Override
	public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
		pg.rotate(rotation);
	}

}
