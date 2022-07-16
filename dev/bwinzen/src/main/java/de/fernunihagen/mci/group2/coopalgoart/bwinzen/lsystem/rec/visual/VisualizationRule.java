package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import processing.core.PGraphics;

@Getter
@RequiredArgsConstructor
public abstract class VisualizationRule {
	private final AlphabetLetter input;
	/**
	 * @param letter
	 * @param lSystem TODO
	 * @param context TODO
	 * @param pg
	 * @param state TODO
	 */
	public abstract void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg);
	
	
	@Override
	public String toString() {
		return input.toString() +" -> " +getClass().getSimpleName();
	}
}
