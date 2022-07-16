package de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import processing.core.PVector;

public enum EdgeBehavior {
	ENDLESS_SCREEN, MIRROR, HIDDEN_FORCE, TELEPORTATION;

	public PositionAdapter inCaseOfEdge(int width, int height, CooperationContext context) {
		switch (this) {
		case ENDLESS_SCREEN:
			return (flyOb) -> {
				if (flyOb.position.x >= width) {
					flyOb.position.x = flyOb.position.x - width;
					flyOb.prevPosition = flyOb.position;
				} else if (flyOb.position.x < 0) {
					flyOb.position.x = flyOb.position.x + width; // -1.f;
					flyOb.prevPosition = flyOb.position;
				}
				if (flyOb.position.y >= height) {
					flyOb.position.y = flyOb.position.y - height;
					flyOb.prevPosition = flyOb.position;
				} else if (flyOb.position.y < 0) {
					flyOb.position.y = flyOb.position.y + height; // -1.f;
					flyOb.prevPosition = flyOb.position;
				}
			};
		case MIRROR:
			return (flyOb) -> {
				if (flyOb.position.x >= width) {
					flyOb.position.x = 2 * width - flyOb.position.x;
					flyOb.prevPosition = flyOb.position;
					flyOb.velocity.x *= -1;
				} else if (flyOb.position.x < 0) {
					flyOb.position.x = -flyOb.position.x; // -1.f;
					flyOb.prevPosition = flyOb.position;
					flyOb.velocity.x *= -1;
				}
				if (flyOb.position.y >= height) {
					flyOb.position.y = 2 * height - flyOb.position.y;
					flyOb.prevPosition = flyOb.position;
					flyOb.velocity.y *= -1;
				} else if (flyOb.position.y < 0) {
					flyOb.position.y = -flyOb.position.y; // -1.f;
					flyOb.prevPosition = flyOb.position;
					flyOb.velocity.y *= -1;
				}
			};
		case TELEPORTATION:
			PixelCoordinate c = context.getCoordinate();
			PVector teleportationPoint = new PVector(c.x, c.y);
			if (teleportationPoint.x >= width || teleportationPoint.x < 0 || teleportationPoint.y >= height
					|| teleportationPoint.y < 0) {
				teleportationPoint.sub(width / 2, height / 2);
//				Calculate Intersection
//					x=x y=0
//				________________
//				| y<0	|	y<0	|
//				| x<0	|	x>0	|
//	x=0 y=y		|_______|_______| x=width y=y
//				|	y>0	|	y>0	|
//				|	x<0	|	x>0	|
//				|_______|_______|
//					x=x y=height
				if (teleportationPoint.x > 0) {
					double factor = (width/2.) / teleportationPoint.x; 
					float y = (float) (height/2. + factor * teleportationPoint.y);
					if(y >=0 && y<height) {
						teleportationPoint.set(width, y);
					} else if (teleportationPoint.y > 0) {
						factor = (height/2.) / teleportationPoint.y; 
						float x = (float) (width/2. + factor * teleportationPoint.x);
						teleportationPoint.set(x, height);
					} else {
						factor = (-height/2.) / teleportationPoint.y; 
						float x = (float) (width/2. + factor * teleportationPoint.x);
						teleportationPoint.set(x, 0);
					}
				} else {
					double factor = (-width/2.) / teleportationPoint.x; 
					float y = (float) (height/2. + factor * teleportationPoint.y);
					if(y >=0 && y<height) {
						teleportationPoint.set(0, y);
					} else if (teleportationPoint.y > 0) {
						factor = (height/2.) / teleportationPoint.y; 
						float x = (float) (width/2. + factor * teleportationPoint.x);
						teleportationPoint.set(x, height);
					} else {
						factor = (-height/2.) / teleportationPoint.y; 
						float x = (float) (width/2. + factor * teleportationPoint.x);
						teleportationPoint.set(x, 0);
					}
				}
			}
			PVector finalTeleP = teleportationPoint;
			return (flyOb) -> {
				if (flyOb.position.x >= width || flyOb.position.x < 0 || flyOb.position.y >= height
						|| flyOb.position.y < 0) {
					flyOb.prevPosition = finalTeleP.copy();
					if (flyOb.position.dist(flyOb.prevPosition) < 10) {
						flyOb.velocity.mult(-1);
					}
					flyOb.position = flyOb.prevPosition;
				}
			};
		case HIDDEN_FORCE:
			float activationBorder = 0.08f;
			float widthLeftBorder = width * activationBorder;
			float heightTopBorder = height * activationBorder;
			float widthRightBorder = width - widthLeftBorder;
			float heightBorderDown = height - heightTopBorder;

			return (flyOb) -> {
				PVector position = flyOb.position;
				PVector prevPosition = flyOb.prevPosition;
				PVector velocity = flyOb.velocity;
				int fac = 0;
				if (position.x >= widthRightBorder) { // --->|| right Border
					if (velocity.y > 0) {
						fac = 1;
					} else {
						fac = -1;
					}
				} else if (position.y >= heightBorderDown) {// v Down
					if (velocity.x < 0) {
						fac = 1;
					} else {
						fac = -1;
					}
				} else if (position.x < widthLeftBorder) {// ||<--- left Border
					if (velocity.y < 0) {
						fac = 1;
					} else {
						fac = -1;
					}
				} else if (position.y < heightTopBorder) {// A Top
					if (velocity.x > 0) {
						fac = 1;
					} else {
						fac = -1;
					}
				}
				if (fac != 0) {
//					System.out.println(position);
					// current position -> to middle
					PVector posToMiddle = new PVector(width / 2 - position.x, height / 2 - position.y);
					// coutn of iteration when flyOb would be reach the middle in the case the
					// velocity points on it
					float distanceToMiddle = posToMiddle.mag();
					float a = distanceToMiddle / velocity.mag();
					float distanceToMiddleAfterAIterations = new PVector(posToMiddle.x - (position.x + a * velocity.x),
							posToMiddle.y - (position.y + a * velocity.y)).mag();
//					float distanceToMiddleAfterAIterations = posToMiddle.set(width / 2 - (position.x + a * velocity.x), height / 2 - (position.y + a * velocity.y)).mag();
					if (distanceToMiddleAfterAIterations < height * 0.65) {
						flyOb.rotationAngle = 0;
						return;
					}

					if (flyOb.rotationAngle == 0) {
						float mag = velocity.mag();
						float angle = (float) (fac * Math.PI * mag / 300);
						flyOb.rotationAngle = angle;
					}
//					System.out.println("Add Angle: "+flyOb.rotationAngle);
					velocity.rotate(flyOb.rotationAngle);
					position.set(prevPosition.x + velocity.x, prevPosition.y + velocity.y);
				}
			};
		default:
			break;
		}
		return null;
	}

	public static interface PositionAdapter {
		void checkEdges(FlyingObject flyOb);
	}
}
