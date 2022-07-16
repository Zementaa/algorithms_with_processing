package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;

public class Forward extends CalculationRule {

	private float iterationFactor = 1f;

	public Forward(AlphabetLetter input) {
		super(input);
	}
	
	public Forward(AlphabetLetter input, float iterationFactor) {
		super(input);
		this.iterationFactor = iterationFactor;
	}

	@Override
	public CalculationState calculate(AlphabetLetter letter, LSystem lSystem, CalculationState state, float size) {
		float len = (float) (-size*Math.pow(iterationFactor, lSystem.getIterationCounter()));
		return state.forward(len);
	}

}
