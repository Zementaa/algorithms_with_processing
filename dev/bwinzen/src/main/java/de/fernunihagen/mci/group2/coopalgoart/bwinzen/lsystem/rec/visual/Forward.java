package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import processing.core.PGraphics;

public class Forward extends VisualizationRule {

	private float length;
	private float iterationFactor = 0.5f;

	public Forward(AlphabetLetter input, float length) {
		super(input);
		this.length = length;
	}
	
	public Forward(AlphabetLetter input, float length, float iterationFactor) {
		super(input);
		this.length = length;
		this.iterationFactor = iterationFactor;
	}

	@Override
	public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
//		float screenX = pg.screenX(0, 0);
//		float screenY = pg.screenY(0, 0);
		float len = (float) (-length*Math.pow(iterationFactor, lSystem.getIterationCounter()));
//		System.out.println(screenX+ "/"+ screenY+"  "+len);
		pg.line(0, 0, 0, len);
		pg.translate(0,  len);
	}

}
