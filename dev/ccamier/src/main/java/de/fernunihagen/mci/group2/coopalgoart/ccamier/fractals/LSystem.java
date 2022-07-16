package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

public class LSystem {
	String sentence; // The sentence (a String)
	Rule[] ruleset; // The ruleset (an array of Rule objects)
	int generation; // Keeping track of the generation #

	// Construct an LSystem with a starting sentence and a ruleset
	public LSystem(String axiom, Rule[] r) {
		this.sentence = axiom;
		this.ruleset = r;
		this.generation = 0;
	}

	// Generate the next generation
	public void generate() {
		// An empty StringBuffer that we will fill
		StringBuffer nextgen = new StringBuffer();
		// For every character in the sentence
		for (int i = 0; i < this.sentence.length(); i++) {
			// What is the character
			char curr = this.sentence.charAt(i);
			// We will replace it with itself unless it matches one of our rules
			String replace = "" + curr;
			// Check every rule
			for (int j = 0; j < this.ruleset.length; j++) {
				char a = this.ruleset[j].getA();
				// if we match the Rule, get the replacement String out of the Rule
				if (a == curr) {
					replace = this.ruleset[j].getB();
					break;
				}
			}
			// Append replacement String
			nextgen.append(replace);
		}
		// Replace sentence
		this.sentence = nextgen.toString();
		// Increment this.generation
		this.generation++;
	}

	public String getSentence() {
		return this.sentence;
	}

	int getGeneration() {
		return this.generation;
	}
}
