package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.RecursivLSystemTree;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.DoNothing;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Forward;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Load;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.NoiseRotate;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Rotate;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.Save;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.rec.visual.VisualizationRule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleLSystems {
	public static final LSystemTree createCodingTrainExample(float size, float angle) {
		LSystemTree tree = new LSystemBuilder() //
				.addRule("F=FF+[+F-F-F]-[-F+F+F]") //
				.addAxiome("F") //
				.angle(angle) //
				.sizeFactor(250f) //
				.lengthScalePerIteration(0.5f) //
				.offsetY((int) (size*250)) //
				.buildNonRecursiveTree(size);
		return tree;
	}
	
	public static final LSystemTree createTree1Example(float size) {
		LSystemTree tree = new LSystemBuilder() //
				.addRule("A=B[+A][-A]BA") //
				.addRule("B=BB") //
				.addAxiome("B[+A][-A]BA") //
				.angle(PConstants.QUARTER_PI) //
				.sizeFactor(10f) //
				.offsetY((int) (size*250)) //
				.buildNonRecursiveTree(size);
		return tree;
	}
	
	
	public static final LSystemTree createSiepinskiArrowHeadCurveExample(float size) {
		LSystemTree tree = new LSystemBuilder() //
				.addAxiome("A") //
				.addRule("A=B-A-B")
				.addRule("B=A+B+A") //
				.angle(PConstants.THIRD_PI) //
				.sizeFactor(10f) //
//				.offsetY((int) (size*250)) //
				.buildNonRecursiveTree(size);
		return tree;
	}
	
	public static final LSystemTree createHilbertCurveExample(float size) {
		LSystemTree tree = new LSystemBuilder() //
				.addAxiome("A") //
				.addRule("A=-BC+ACA+CB-")
				.addRule("B=+AC-BCB-CA+") //
				.angle(PConstants.HALF_PI) //
				.sizeFactor(10f) //
//				.offsetY((int) (size*250)) //
				.buildNonRecursiveTree(size);
		return tree;
	}
	
	public static final RecursivLSystemTree createOrderedChaos(float size, float angle) {
		Set<AlphabetLetter> alphabetLetters = new LinkedHashSet<>();
		AlphabetLetter f = new AlphabetLetter("F");
		alphabetLetters.add(f);
		AlphabetLetter r = new AlphabetLetter("+");
		alphabetLetters.add(r);
		AlphabetLetter l = new AlphabetLetter("-");
		alphabetLetters.add(l);
		AlphabetLetter save = new AlphabetLetter("[");
		alphabetLetters.add(save);
		AlphabetLetter load = new AlphabetLetter("]");
		alphabetLetters.add(load);
		AlphabetLetter a = new AlphabetLetter("A");
		alphabetLetters.add(a);

		List<LSystemRule> rules = new LinkedList<>();
		rules.add(new LSystemRule(f,
				Arrays.asList(f, f, r, save, r, f, l, f, l, f, load, l, save, l, f, r, f, r, f, load)));
		rules.add(new LSystemRule(f,Arrays.asList(f, a)));
		rules.add(new LSystemRule(f,Arrays.asList(r, f, a)));
		
		LSystem lSystem = new LSystem(Arrays.asList(f), rules, alphabetLetters);
		
		float length = 250*size;
		List<VisualizationRule>  visRules = new LinkedList<>();
		visRules.add(new Forward(f, length, 0.5f));
		visRules.add(new Save(save));
		visRules.add(new Load(load));
		visRules.add(new NoiseRotate(r, angle));
		visRules.add(new NoiseRotate(l, -angle));
		visRules.add(new VisualizationRule(a) {
			private Random r;
			@Override
			public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
				if(r == null) {
					r = new Random(context.getSeed());
				}
				pg.pushStyle();
				pg.fill((int)r.nextInt(256),(int)r.nextInt(256),(int)r.nextInt(256));
				pg.noStroke();
				pg.ellipseMode(PApplet.RADIUS);
				pg.ellipse(0f, 0f, pg.strokeWeight * 2f, pg.strokeWeight * 3.5f);
//				applet.translate(0,  len);	
				pg.popStyle();
			}
		});
		
		return new RecursivLSystemTree(lSystem, visRules);
	}
	
	public static final RecursivLSystemTree createSpiral(float size, float angle) {
		Set<AlphabetLetter> alphabetLetters = new LinkedHashSet<>();
		AlphabetLetter f = new AlphabetLetter("F");
		alphabetLetters.add(f);
		AlphabetLetter r = new AlphabetLetter("+");
		alphabetLetters.add(r);
		AlphabetLetter l = new AlphabetLetter("-");
		alphabetLetters.add(l);
		AlphabetLetter r2 = new AlphabetLetter("L");
		alphabetLetters.add(r2);
		AlphabetLetter save = new AlphabetLetter("[");
		alphabetLetters.add(save);
		AlphabetLetter load = new AlphabetLetter("]");
		alphabetLetters.add(load);
		List<LSystemRule> rules = new LinkedList<>();
//		rules.add(new LSystemRule(f,Arrays.asList(f, r)));
		rules.add(new LSystemRule(r,Arrays.asList(r, f, r)));
		rules.add(new LSystemRule(l,Arrays.asList(l, f, l)));

		LSystem lSystem = new LSystem(Arrays.asList(save, f, r, load,save, f, l, load, r2,save, f,r, load,save, f, l, load, r2,save, f,r, load,save, f, l, load, r2,save, f,r, load, save, f, l, load), rules, alphabetLetters);
		
		float length = 25*size;
		List<VisualizationRule>  visRules = new LinkedList<>();
		visRules.add(new Forward(f, length, 1.1f));
		visRules.add(new Rotate(r, angle));
		visRules.add(new Rotate(l, -angle));
		visRules.add(new Rotate(r2, PApplet.HALF_PI));
		visRules.add(new Save(save));
		visRules.add(new Load(load));
		return new RecursivLSystemTree(lSystem, visRules);
	}
	
	public static final LSystemTree createKochkurve(float size) {
		float startLength = size*25;
		return new LSystemBuilder() // not centered
				.addAxiome("F++F++F") //
				.addRule("F=F-F++F-F") //
				.angle(PApplet.THIRD_PI) //
				.lengthScalePerIteration( 1/3.f) //
				.buildNonRecursiveTree(startLength);
	}
	
	public static final LSystemTree createLeafs(float size) {
		Set<AlphabetLetter> alphabetLetters = new LinkedHashSet<>();
		AlphabetLetter f = new AlphabetLetter("F");
		AlphabetLetter x = new AlphabetLetter("X");
		AlphabetLetter r = new AlphabetLetter("+");
		AlphabetLetter r2 = new AlphabetLetter("*");
		AlphabetLetter l = new AlphabetLetter("-");
		AlphabetLetter l2 = new AlphabetLetter("-");
		AlphabetLetter load = new AlphabetLetter("]");
		AlphabetLetter save = new AlphabetLetter("[");
		AlphabetLetter leaf1 = new AlphabetLetter("L");
		AlphabetLetter leaf2 = new AlphabetLetter("l");
		AlphabetLetter knosp = new AlphabetLetter("D");
		alphabetLetters.add(f);
		alphabetLetters.add(r);
		alphabetLetters.add(r2);
		alphabetLetters.add(l);
		alphabetLetters.add(l2);
		alphabetLetters.add(save);
		alphabetLetters.add(load);
		alphabetLetters.add(leaf1);
		alphabetLetters.add(leaf2);
		alphabetLetters.add(knosp);

		List<LSystemRule> rules = new LinkedList<>();
		rules.add(new LSystemRule(f,
				Arrays.asList(f, f, r, save, r, f, l, f, l, f, load, l, save, l, f, r, f, r, f, load)));
		rules.add(new LSystemRule(f,
				Arrays.asList(f, save, l, f, load, f, save, r, f,load,save,f,load)));
		rules.add(new LSystemRule(f,
				Arrays.asList(f, save, l, f, load, f, save, r, knosp ,load,save,knosp,load)));
		rules.add(new LSystemRule(x,
				Arrays.asList(f, save, l, x, load, f, save, r, x,load,l,x)));
		rules.add(new LSystemRule(f,
				Arrays.asList(f, f)));
		rules.add(new LSystemRule(x,
				Arrays.asList(f, save, r, x, load,save, l, x,load,f,x)));
		rules.add(new LSystemRule(x,
				Arrays.asList(f, save, r, x, load,save, l, knosp,load,f,x)));
		rules.add(new LSystemRule(x,
				Arrays.asList(f, l, save, save, x, load,r ,x,load,r,f,save,r,f,knosp,load,l,x)));
		rules.add(new LSystemRule(x,
				Arrays.asList(f, l, save, save, x, load,r ,knosp,load,r,f,save,r,f,x,load,l,x)));
		rules.add(new LSystemRule(x,
				Arrays.asList(f, l, save, save, knosp, load,r ,x,load,r,f,save,r,f,x,load,l,x)));
		rules.add(new LSystemRule(knosp,Arrays.asList(leaf1)));
		rules.add(new LSystemRule(knosp,Arrays.asList(leaf2)));
		
		LSystem lSystem = new LSystem(Arrays.asList(f), rules, alphabetLetters);
		float angle = PApplet.PI / 8;
		float angle2 =  PApplet.PI / 6;
		float length = 250*size;
		List<VisualizationRule>  visRules = new LinkedList<>();
		visRules.add(new Forward(f, length, 0.5f));
		visRules.add(new Forward(x, length, 1f));
		visRules.add(new Save(save));
		visRules.add(new Load(load));
		visRules.add(new Rotate(r, angle));
		visRules.add(new Rotate(l, -angle));
		visRules.add(new Rotate(r2, angle2));
		visRules.add(new Rotate(l2, -angle2));
		visRules.add(new DoNothing(knosp));
		visRules.add(new VisualizationRule(leaf1) {
			@Override
			public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
				pg.ellipseMode(PApplet.RADIUS);
				pg.ellipse(pg.strokeWeight*1.75f, 0f, pg.strokeWeight * 3.5f, pg.strokeWeight * 2f);
				pg.ellipse(-pg.strokeWeight*1.75f, 0f, pg.strokeWeight * 3.5f, pg.strokeWeight * 2f);
				pg.ellipse(0, pg.strokeWeight, pg.strokeWeight * 2f, pg.strokeWeight * 3.5f);
			}
		});
		visRules.add(new VisualizationRule(leaf2) {
			@Override
			public void draw(AlphabetLetter letter, LSystem lSystem, CooperationContext context, PGraphics pg) {
				pg.ellipseMode(PApplet.RADIUS);
				pg.ellipse(pg.strokeWeight*1.75f, 0f, pg.strokeWeight * 3.5f, pg.strokeWeight * 2f);
				pg.ellipse(-pg.strokeWeight*1.75f, 0f, pg.strokeWeight * 3.5f, pg.strokeWeight * 2f);
			}
		});

		RecursivLSystemTree recursivLSystemTree = new RecursivLSystemTree(lSystem, visRules);
		recursivLSystemTree.setOffsetY(250);
		return recursivLSystemTree;
	}
}
