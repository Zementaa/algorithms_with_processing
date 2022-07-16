package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation;

import java.util.Arrays;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class CalculationRule {
	private final AlphabetLetter[] inputs;
	/**
	 * @param letter
	 * @param lSystem TODO
	 * @param state TODO
	 * @param size TODO
	 * @return TODO
	 */
	public abstract CalculationState calculate(AlphabetLetter letter, LSystem lSystem, CalculationState state, float size);
	
	
	public CalculationRule(AlphabetLetter l) {
		this(new AlphabetLetter[] {l});
	}
	
	@Override
	public String toString() {
		return Arrays.toString(inputs) +" -> " +getClass().getSimpleName();
	}
	
	public CalculationRule cloneIfNeccessary() {
		return this;
	}
}
