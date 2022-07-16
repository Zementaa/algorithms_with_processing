package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.koch;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class KochCurveSnowflakeFactory {
	ArrayList<Segment> segments;
	int maxIteration = 0;
	Map<Integer, PShape> snowflakes = new TreeMap<>();
	private PApplet pApplet;

	public KochCurveSnowflakeFactory(PApplet pApplet) {
		this.pApplet = pApplet;
		float len = 1000;
		float h = (float) (len * Math.sqrt(3) / 2);
		float h_2= h/2;
		segments = new ArrayList<Segment>();
		PVector a = new PVector(-len/2, -h_2);
		PVector b = new PVector(len/2, -h_2);
		Segment s1 = new Segment(a, b);

		PVector c = new PVector(0, h_2);

		Segment s2 = new Segment(b, c);
		Segment s3 = new Segment(c, a);
		segments.add(s1);
		segments.add(s2);
		segments.add(s3);
		PShape s = createShape();
		maxIteration = 1;
		snowflakes.put(maxIteration, s);
	}

	private PShape createShape() {
		PShape s = pApplet.createShape();
		s.beginShape();
		for (Segment segment : segments) {
			segment.draw(s);
		}
		s.endShape();
		return s;
	}

	void addAll(Segment[] arr, ArrayList<Segment> list) {
		for (Segment s : arr) {
			list.add(s);
		}
	}

	public PShape getSnowflake(int iteration) {
		if (iteration > 10 || iteration < 0) {
			throw new IllegalArgumentException("the calculation effort is to high");
		}
		while(maxIteration<iteration) {
			nextIteration();
		}
		return snowflakes.get(iteration);
	}

	private void nextIteration() {
		ArrayList<Segment> nextGeneration = new ArrayList<Segment>();

		for (Segment s : segments) {
			Segment[] children = s.generate();
			addAll(children, nextGeneration);
		}
		segments = nextGeneration;
		PShape shape = createShape();
		maxIteration++;
		snowflakes.put(maxIteration, shape);
	}

	private static class Segment {
		PVector a;
		PVector b;

		private Segment(PVector a_, PVector b_) {
			a = a_.copy();
			b = b_.copy();
		}

		private Segment[] generate() {
			Segment[] children = new Segment[4];

			PVector v = PVector.sub(b, a);
			v.div(3);

			// Segment 0
			PVector b1 = PVector.add(a, v);
			children[0] = new Segment(a, b1);

			// Segment 3
			PVector a1 = PVector.sub(b, v);
			children[3] = new Segment(a1, b);

			v.rotate(-(float) Math.PI / 3);
			PVector c = PVector.add(b1, v);
			// Segment 2
			children[1] = new Segment(b1, c);
			// Segment 3
			children[2] = new Segment(c, a1);
			return children;
		}

		private void draw(PShape shape) {
			shape.vertex(a.x, a.y);
			shape.vertex(b.x, b.y);
		}

	}
}
