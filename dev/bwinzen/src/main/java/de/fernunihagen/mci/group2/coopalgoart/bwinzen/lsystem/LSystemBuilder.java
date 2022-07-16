package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.NonRecursiveLSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.CalculationRule;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.SaveAndLoad;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.RecursivLSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Forward;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Load;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Rotate;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Save;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.VisualizationRule;
import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class LSystemBuilder {
	private float angle;
	private float lengthScalePerIteration = 1.f;
	Map<Character, AlphabetLetter> alphabetLetters = new HashMap<>();
	List<AlphabetLetter> axiom = new LinkedList<>();
	List<LSystemRule> rules = new LinkedList<>();
	AlphabetLetter r = new AlphabetLetter("+");
	AlphabetLetter l = new AlphabetLetter("-");
	AlphabetLetter save = new AlphabetLetter("[");
	AlphabetLetter load = new AlphabetLetter("]");
	private float sizeFactor = 10;
	private int offsetX;
	private int offsetY;

	public LSystemBuilder() {
		alphabetLetters.put('+', r);
		alphabetLetters.put('-', l);
		alphabetLetters.put('[', save);
		alphabetLetters.put(']', load);

	}

	public LSystemBuilder addRule(String rule) {
		rule = rule.replaceAll("\\s", "");
		if (rule.isEmpty()) {
			return this;
		}
		char[] charArray = rule.toCharArray();
		if (charArray[1] != '=') {
			throw new IllegalArgumentException("Wrong format " + rule);
		}
		AlphabetLetter replaced = alphabetLetters.get(charArray[0]);
		if (replaced == null) {
			replaced = new AlphabetLetter(charArray[0] + "");
			alphabetLetters.put(charArray[0], replaced);
		}

		List<AlphabetLetter> replacement = new LinkedList<>();
		for (int i = 2; i < charArray.length; i++) {
			AlphabetLetter letter = alphabetLetters.get(charArray[i]);
			if (letter == null) {
				letter = new AlphabetLetter(charArray[i] + "");
				alphabetLetters.put(charArray[i], letter);
			}
			replacement.add(letter);
		}
		rules.add(new LSystemRule(replaced, replacement));
		return this;
	}

	public LSystemBuilder addAxiome(String axiome) {
		axiome = axiome.replaceAll("\\s", "");
		char[] charArray = axiome.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			AlphabetLetter letter = alphabetLetters.get(charArray[i]);
			if (letter == null) {
				letter = new AlphabetLetter(charArray[i] + "");
				alphabetLetters.put(charArray[i], letter);
			}
			axiom.add(letter);
		}
		return this;
	}

	public LSystemBuilder lengthScalePerIteration(float lengthScalePerIteration) {
		this.lengthScalePerIteration = lengthScalePerIteration;
		return this;
	}

	public LSystemBuilder angle(float angle) {
		this.angle = angle;
		return this;
	}
	
	public LSystemBuilder sizeFactor(float f) {
		this.sizeFactor = f;
		return this;
	}

	public LSystemBuilder angle360(float floatValue) {
		this.angle = PApplet.PI * 2 * floatValue / 360;
		return this;
	}

	public LSystemBuilder offsetX(int offsetX) {
		this.offsetX=offsetX;
		return this;
	}
	
	public LSystemBuilder offsetY(int offsetY) {
		this.offsetY=offsetY;
		return this;
	}
	public LSystemTree buildRecursiveTree(float size) {
		LSystem lSystem = new LSystem(axiom, rules, new HashSet<>(alphabetLetters.values()));

		float length = sizeFactor * size;
		List<VisualizationRule> visRules = new LinkedList<>();
		visRules.add(new Save(save));
		visRules.add(new Load(load));
		visRules.add(new Rotate(r, angle));
		visRules.add(new Rotate(l, -angle));

		alphabetLetters.values().forEach(letter -> {
			if (letter != save && letter != load && letter != r && letter != l) {
				visRules.add(new Forward(letter, length, lengthScalePerIteration));
			}
		});

		RecursivLSystemTree recursivLSystemTree = new RecursivLSystemTree(lSystem, visRules);
		recursivLSystemTree.setOffsetX(offsetX);
		recursivLSystemTree.setOffsetY(offsetY);
		return recursivLSystemTree;
	}
	
	public LSystemTree buildNonRecursiveTree(float size) {
		LSystem lSystem = new LSystem(axiom, rules, new HashSet<>(alphabetLetters.values()));

		List<CalculationRule> visRules = new LinkedList<>();
		SaveAndLoad saveAndLoad = new SaveAndLoad(save, load);
		visRules.add(saveAndLoad);
		visRules.add(new de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.Rotate(r, angle));
		visRules.add(new de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.Rotate(l, -angle));

		alphabetLetters.values().forEach(letter -> {
			if (letter != save && letter != load && letter != r && letter != l) {
				visRules.add(new  de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation.Forward(letter, lengthScalePerIteration));
			}
		});

		NonRecursiveLSystemTree nonRecursiveLSystemTree = new NonRecursiveLSystemTree(lSystem, visRules, size*sizeFactor);
		nonRecursiveLSystemTree.setOffsetX(offsetX);
		nonRecursiveLSystemTree.setOffsetY(offsetY);
		return nonRecursiveLSystemTree;
	}

}
