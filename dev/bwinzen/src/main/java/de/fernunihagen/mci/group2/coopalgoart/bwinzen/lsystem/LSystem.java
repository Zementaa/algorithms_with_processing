package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LSystem {
	private LSystem parent;
	private List<AlphabetLetter> currentSequence;
	private List<LSystemRule> rules;
	private Set<AlphabetLetter> alphabet;
	private int iterationCounter = 0;
	@Setter
	private long seed; 

	public LSystem(List<AlphabetLetter> startSequence, List<LSystemRule> rules, Set<AlphabetLetter> alphabet) {
		this.currentSequence = startSequence;
		this.rules = rules;
		this.alphabet = alphabet;
	}
	
	private LSystem(LSystem parent, List<AlphabetLetter> newIteration) {
		this.parent = parent;
		this.currentSequence = newIteration;
		this.rules = parent.rules;
		this.alphabet = parent.alphabet;
		this.seed = parent.seed;
		this.iterationCounter = parent.iterationCounter+1;
	}

	public LSystem nextIteration() {
		return new LSystem(this, nextIteration(seed));
	}

	private List<AlphabetLetter> nextIteration(long seed) {
		Random random = new Random(seed);
		List<AlphabetLetter> newSequence = new LinkedList<>();
		for (AlphabetLetter alphabetLetter : currentSequence) {
			List<LSystemRule> collect = rules.stream().filter(r -> r.getInput() == alphabetLetter)
					.collect(Collectors.toList());
			if (collect.isEmpty()) {
				newSequence.add(alphabetLetter);
			} else if (collect.size() == 1) {
				newSequence.addAll(collect.get(0).getOutput());
			} else {
				int randomRuleIndex = random.nextInt(collect.size());
				newSequence.addAll(collect.get(randomRuleIndex).getOutput());
			}
		}
		return newSequence;
	}

	@Override
	public String toString() {
		return currentSequence.stream().map(Object::toString).collect(Collectors.joining());
	}
}
