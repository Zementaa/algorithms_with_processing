package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;

public class Rotate extends CalculationRule{

	private float rotation;

	public Rotate(AlphabetLetter input, float rotation) {
		super(input);
		this.rotation = rotation;
	}

	@Override
	public CalculationState calculate(AlphabetLetter letter, LSystem lSystem, CalculationState state, float size) {
		return state.rotate(rotation);
	}

}
