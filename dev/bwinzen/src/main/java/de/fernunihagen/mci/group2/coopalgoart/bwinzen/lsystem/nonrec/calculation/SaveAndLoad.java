package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation;

import java.util.LinkedList;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;

public class SaveAndLoad extends CalculationRule {
	private final AlphabetLetter save;
	private final AlphabetLetter load;
	private LinkedList<CalculationState> states = new LinkedList<>();

	public SaveAndLoad(AlphabetLetter save, AlphabetLetter load) {
		super(new AlphabetLetter[] {save, load});
		this.save = save;
		this.load = load;
	}
	@Override
	public CalculationState calculate(AlphabetLetter letter, LSystem lSystem, CalculationState state, float size) {
		if (letter == save) {
			states.addFirst(state);
			return state;
		} else if(letter == load) {
			return states.removeFirst().goBack();
		}
		return state;
	}
	
	@Override
	public CalculationRule cloneIfNeccessary() {
		return new SaveAndLoad(save, load);
	}

}
