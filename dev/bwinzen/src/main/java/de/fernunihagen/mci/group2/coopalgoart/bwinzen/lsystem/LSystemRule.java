package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LSystemRule {
	private AlphabetLetter input;
	private List<AlphabetLetter> output;
	
	@Override
	public String toString() {
		return input.toString() + "->" +output.toString();
	}
}
