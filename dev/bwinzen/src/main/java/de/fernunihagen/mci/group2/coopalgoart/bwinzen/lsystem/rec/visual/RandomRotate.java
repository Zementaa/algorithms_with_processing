package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import processing.core.PGraphics;

public class RandomRotate extends VisualizationRule {

	private Random r;
	private float rotation;

	public RandomRotate(AlphabetLetter input, float rotation) {
		super(input);
		this.rotation = rotation;
	}

	@Override
	public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
		if (r == null) {
			r = new Random(context.getSeed());
		}
		pg.rotate(rotation + r.nextFloat() * 10 - 5);
	}

}
