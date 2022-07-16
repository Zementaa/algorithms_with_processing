package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.AlphabetLetter;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystem;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.VisualizationRule;
import lombok.Getter;
import processing.core.PGraphics;

public class RecursivLSystemTree extends LSystemTree{
	@Getter
	private float size;
	private Map<AlphabetLetter, VisualizationRule> visRules = new HashMap<>();
	
	private RecursivLSystemTree(LSystem lSystem, Map<AlphabetLetter, VisualizationRule> visRules) {
		super(lSystem);
		this.visRules = visRules;
	}
	
	public RecursivLSystemTree(LSystem lSystem, List<VisualizationRule> visRules) {
		super(lSystem);
		HashSet<AlphabetLetter> set = new HashSet<>(lSystem.getAlphabet());
		visRules.stream() //
			.map(VisualizationRule::getInput)// convert rule to the input it will be used for
			.forEach(set::remove); // removes all values which are covered by rules
		if(!set.isEmpty()) { //should be empty otherwise the alphabet has signs which have no rules
			throw new IllegalArgumentException("Visualization Rules does not fit to alphabet. Follwing letters have no rule "+set);
		}
		this.lSystem = lSystem;
		visRules.forEach(r->this.visRules.put(r.getInput(), r));
	}
	
	public void draw(CooperationContext context, PGraphics pg) {
		List<AlphabetLetter> currentSequence = lSystem.getCurrentSequence();
		currentSequence.forEach(l->this.visRules.get(l).draw(l, lSystem, context, pg));
	}
	
	public RecursivLSystemTree drawAndNext(CooperationContext context, PGraphics pg) {
		draw(context, pg);
		return next();
	}
	
	public RecursivLSystemTree next() {
		return new RecursivLSystemTree(lSystem.nextIteration(), visRules);
	}
	
	public boolean hasPrevious() {
		return lSystem.getParent() != null;
	}
	
	public RecursivLSystemTree previous() {
		return new RecursivLSystemTree(lSystem.getParent(), visRules);
	}
}
