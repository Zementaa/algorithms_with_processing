package de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class QuadTreeCheck extends PApplet {

	public QuadTree<TestElement> tree;
	List<TestElement> testList = new LinkedList<>();
	public void settings() {
		size(500, 500);
	}

	@Override
	public void setup() {
		tree = new QuadTree<>(0, 0, width, height, 4);
		for(int i = 0; i<1000; i++) {
			TestElement element = new TestElement(random(width), random(height));
			tree.add(element);
			testList.add(element);
		}
		background(65);
		stroke(255);
		noFill();
		ellipseMode(RADIUS);
		drawQuadTree(tree.getRoot());
	}

	public void draw() {
	}

	@SuppressWarnings("rawtypes")
	private void drawQuadTree(QuadTreeNode node) {
		rect((float)node.getX(), (float)node.getY(), (float)node.getWidth(), (float)node.getHeight());
		QuadTreeNode[] children = node.getChildren();
		List<QuadTreeElement> elements = node.getElements();
		if(children!=null) {
			for (QuadTreeNode child : children) {
				noFill();
				drawQuadTree(child);
			}
		}else if(elements != null) {
//			fill(255);
//			for (QuadratTreeElement object : elements) {
//				ellipse((float)object.getX(), (float)object.getY(), 1, 1);
//			}
		}
	}
	
	
	public void mousePressed() {
		System.out.println("Mouse pressed "+ mouseX+ ", " +mouseY);
		long start = System.currentTimeMillis();
		List<TestElement> query = tree.query(QuadTreeQuery.queryMaxDistance(mouseX, mouseY, 20));
		long time = System.currentTimeMillis()-start;
		System.out.println("Time quadtree: "+time+"ms");
		start = System.currentTimeMillis();
		testList.stream().filter(t->QuadTreeQuery.dist(mouseX, mouseY, t.getX(), t.getY())<=20).collect(Collectors.toList());
		time = System.currentTimeMillis()-start;
		System.out.println("Time list: "+time+"ms");
		fill(255, 0, 0);
		for (TestElement object : query) {
			ellipse((float)object.getX(), (float)object.getY(), 2, 2);
		}
		
		NavigableMap<Double, List<TestElement>> queryDistances = tree.queryDistances(mouseX, mouseY, QuadTreeQuery.queryMaxDistance(mouseX, mouseY, 20));
		Entry<Double, List<TestElement>> firstEntry = queryDistances.firstEntry();
		TestElement testElement = firstEntry.getValue().get(0);
		fill(0, 255, 0);
		ellipse((float)testElement.getX(), (float)testElement.getY(), 2, 2);
		fill(0, 255, 255);
		TestElement closest = tree.findClosest(testElement);
		ellipse((float)closest.getX(), (float)closest.getY(), 2, 2);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		QuadTreeCheck mySketch = new QuadTreeCheck();
		PApplet.runSketch(processingArgs, mySketch);
	}

	@AllArgsConstructor
	@Getter
	private static class TestElement implements QuadTreeElement {
		double x;
		double y;
	}
}
