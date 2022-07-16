package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import processing.core.PGraphics;

public class Load extends VisualizationRule{

	public Load(AlphabetLetter input) {
		super(input);
	}

	@Override
	public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
		pg.popMatrix();
	}

}
