package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlphabetLetter {
	private final String textualRepresentatioon;
	
	@Override
	public String toString() {
		return textualRepresentatioon;
	}
	
	
	@Override
	public int hashCode() {
		return textualRepresentatioon.hashCode();
	}
	
	
}
