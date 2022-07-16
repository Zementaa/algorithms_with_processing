package de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.EdgeBehavior.PositionAdapter;
import processing.core.PVector;

public class EdgeBehaviorTest {

	@Test
	public void testEndlessScreen() {
		EdgeBehavior behavior = EdgeBehavior.ENDLESS_SCREEN;
		FlyingObject right = new FlyingObject();
		FlyingObject left = new FlyingObject();
		FlyingObject top = new FlyingObject();
		FlyingObject bottom = new FlyingObject();

		right.position = new PVector(51, 25);
		left.position = new PVector(-1, 25);
		top.position = new PVector(25, -1);
		bottom.position = new PVector(25, 51);
		PositionAdapter positionAdapter = behavior.inCaseOfEdge(50, 50, null);
		
		positionAdapter.checkEdges(right);
		assertEquals(1, right.getX(),0.01);
		assertEquals(25, right.getY(),0.01);
		
		positionAdapter.checkEdges(left);
		assertEquals(49, left.getX(),0.01);
		assertEquals(25, left.getY(),0.01);
		
		positionAdapter.checkEdges(top);
		assertEquals(25, top.getX(),0.01);
		assertEquals(49, top.getY(),0.01);
		
		positionAdapter.checkEdges(bottom);
		assertEquals(25, bottom.getX(),0.01);
		assertEquals(1, bottom.getY(),0.01);
//		fail("Not yet implemented");
	}
	
	@Test
	public void testMirror() {
		EdgeBehavior behavior = EdgeBehavior.MIRROR;
		FlyingObject right = new FlyingObject();
		FlyingObject left = new FlyingObject();
		FlyingObject top = new FlyingObject();
		FlyingObject bottom = new FlyingObject();

		right.position = new PVector(51, 25);
		left.position = new PVector(-1, 25);
		top.position = new PVector(25, -1);
		bottom.position = new PVector(25, 51);
		
		right.prevPosition = new PVector(49, 25);
		left.prevPosition = new PVector(1, 25);
		top.prevPosition = new PVector(25, 1);
		bottom.prevPosition = new PVector(25, 49);
		PositionAdapter positionAdapter = behavior.inCaseOfEdge(50, 50, null);
		
		positionAdapter.checkEdges(right);
		assertEquals(49, right.getX(),0.01);
		assertEquals(25, right.getY(),0.01);
		
		positionAdapter.checkEdges(left);
		assertEquals(1, left.getX(),0.01);
		assertEquals(25, left.getY(),0.01);
		
		positionAdapter.checkEdges(top);
		assertEquals(25, top.getX(),0.01);
		assertEquals(1, top.getY(),0.01);
		
		positionAdapter.checkEdges(bottom);
		assertEquals(25, bottom.getX(),0.01);
		assertEquals(49, bottom.getY(),0.01);
//		fail("Not yet implemented");
	}
	
	
	@Test
	public void rotation() {
		System.out.println(PVector.angleBetween(new PVector(50,50), new PVector(10,50)));
		System.out.println(PVector.angleBetween(new PVector(50,50), new PVector(50,10)));
	}
}
