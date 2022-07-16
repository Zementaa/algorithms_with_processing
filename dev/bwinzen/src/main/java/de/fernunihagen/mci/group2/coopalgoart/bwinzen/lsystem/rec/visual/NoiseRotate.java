package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import processing.core.PApplet;
import processing.core.PGraphics;

public class NoiseRotate extends VisualizationRule {

	private PApplet p;
	private float rotation;

	public NoiseRotate(AlphabetLetter input, float rotation) {
		super(input);
		this.rotation = rotation;
	}

	@Override
	public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
		if (p == null) {
			p = new PApplet();
			p.noiseSeed(context.getSeed());
		}
		float screenX = pg.screenX(0, 0);
		float screenY = pg.screenY(0, 0);
		float z = lSystem.getIterationCounter();
		pg.rotate(rotation + p.noise(screenX * 0.01f, screenY * 0.01f, z * 0.0001f) - 0.5f);
	}

}
