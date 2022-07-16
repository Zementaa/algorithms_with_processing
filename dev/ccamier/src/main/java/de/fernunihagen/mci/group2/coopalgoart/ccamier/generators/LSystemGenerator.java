package de.fernunihagen.mci.group2.coopalgoart.ccamier.generators;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals.LSystem;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals.Rule;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals.Startpunkt;
import lombok.Builder;
import processing.core.PGraphics;

@Builder
public class LSystemGenerator implements Generator {

	char[][] sequences;
	float len;
	float theta;

	@Builder.Default
	int it = 5;

	String ruleset;

	private int farbeZweige;
	private Startpunkt startpunkt;

	private LSystem lsys;
	private Random randomGenerator;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {

		if (this.lsys == null) {
			Rule[] ruleset = new Rule[1];
			randomGenerator = new Random(context.getSeed());

			switch (this.ruleset) {
			case "Veraestelung":
				ruleset[0] = new Rule('F', "F[F]-F+F[--F]+F-F");
				this.lsys = new LSystem("F-F-F-F", ruleset);
				System.out.println(randomGenerator.nextFloat());
				this.theta = (float) Math.toRadians((randomGenerator.nextFloat() + 1) * 45);
				break;
			case "Sierpinksi":
				it = 10;
				ruleset = new Rule[2];
				ruleset[0] = new Rule('F', "F--F--F--G");
				ruleset[1] = new Rule('G', "GG");
				this.lsys = new LSystem("F--F--F", ruleset);
				this.theta = (float) Math.toRadians(randomGenerator.nextFloat() * 25);
				break;
			case "Koch":
				ruleset[0] = new Rule('F', "F+F-F-F+F");
				this.lsys = new LSystem("F", ruleset);
				this.theta = (float) Math.toRadians((randomGenerator.nextFloat() + 1) * 45);
				break;
			case "Fractal":
				ruleset[0] = new Rule('F', "FF-F-F-F-F-F+F");
				this.lsys = new LSystem("F-F-F-F", ruleset);
				float angdeg = (randomGenerator.nextFloat() + 1) * 90;
//				System.out.println(angdeg);
				this.theta = (float) Math.toRadians(angdeg);
				break;
			default:
				it = 6;
				ruleset[0] = new Rule('F', "FF+[+F-F-F]-[-F+F+F]");
				this.lsys = new LSystem("F", ruleset);
				this.theta = (float) Math.toRadians(randomGenerator.nextFloat() * 25);
			}

			sequences = new char[it][];
			this.len = pg.height / 4;

		}

		int index = context.getPApplet().frameCount % it;

		if (sequences[index] == null) {
			sequences[index] = lsys.getSentence().toCharArray();

			if (index < it - 1) {
				lsys.generate();
			}

		}

		pg.translate(startpunkt.getX(), startpunkt.getY());
		if (randomGenerator.nextBoolean()) {
			pg.rotate(PGraphics.PI / 2);
		} else {
			pg.rotate(-PGraphics.PI / 2);
		}

		float localLength = this.len * (float) Math.pow(2, -index);

		render(pg, sequences[index], localLength);

	}

	public void render(PGraphics pg, char[] seq, float localLength) {
		pg.stroke(this.farbeZweige);
		for (int i = 0; i < seq.length; i++) {
			char c = seq[i];
			if (c == 'F' || c == 'G') {
				pg.line(0, 0, localLength, 0);
				pg.translate(localLength, 0);
			} else if (c == '+') {
				pg.rotate(this.theta);
			} else if (c == '-') {
				pg.rotate(-this.theta);
			} else if (c == '[') {
				pg.pushMatrix();
			} else if (c == ']') {
				pg.popMatrix();
			}
		}
	}

}
