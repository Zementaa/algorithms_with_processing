package de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree.QuadTreeElement;
import lombok.Setter;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class FlyingObject implements QuadTreeElement {
	private static int ID_COUNTER = 0;
	public final int id = (ID_COUNTER++);
	public PVector position= new PVector();
	public PVector prevPosition= new PVector();
	public PVector velocity= new PVector();
	public float rotationAngle = 0;
	
	public int color;
	@Setter
	public int nextColor;

	@Setter
	public float size = 1;

	@Override
	public double getX() {
		return position.x;
	}

	@Override
	public double getY() {
		return position.y;
	}

	public void setColor(int color) {
		this.nextColor = color;
		this.color = color;
	}

	@Override
	public String toString() {
		return position + " " + color;
	}

	public double angle() {
		double angle = (double) Math.acos(velocity.y / velocity.mag());
		if (velocity.x > 0) {
			angle = (double) (2 * Math.PI - angle);
		}
		return angle;
	}
	
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = 0;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void addVelocity() {
		PVector tmp = prevPosition;
		prevPosition = position;
		position = tmp.set(velocity.x + prevPosition.x, velocity.y + prevPosition.y, velocity.z + prevPosition.z);
	}
}
