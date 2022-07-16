package de.fernunihagen.mci.group2.coopalgoart.bwinzen.swarm;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.FlyingObject;
import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
public class Boid extends FlyingObject {
	@Setter
	private PVector acceleration;
	private float maxSpeed;
	
	public Boid(PVector position, PVector velocity) {
		this.position = position;
		this.prevPosition = this.position;
		this.velocity = velocity;
		this.acceleration = new PVector();
		this.maxSpeed = velocity.mag();
	}

	public void update(int width, int height, PVector additionalForce) {
		this.prevPosition = this.position.copy();
		if(additionalForce != null) {
			this.velocity.add(additionalForce);
		}
		this.velocity.add(this.acceleration);
		this.velocity.limit(this.maxSpeed);
		this.position.add(this.velocity);
		this.acceleration.mult(0);
		color = nextColor;
	}

	public double angle() {
		double angle = (double) Math.acos(velocity.y / velocity.mag());
		if (velocity.x > 0) {
			angle = (double) (2 * Math.PI - angle);
		}
		return angle;
	}
}