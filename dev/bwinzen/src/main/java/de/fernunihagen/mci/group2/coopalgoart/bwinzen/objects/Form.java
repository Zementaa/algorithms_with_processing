package de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects;

import processing.core.PGraphics;

public enum Form {
	CIRCLE, LINE, POLE, RECT, QUADRAT, TRIANGLE, RANDOM;

	public FlyingObjectFormPrinter formPrinter() {
		switch (this) {
		case RANDOM:
			Form[] forms = values();
			return (pg, flyObs)->{
				forms[(flyObs.id%(forms.length-1))].formPrinter().print(pg, flyObs);
			};
		case CIRCLE:
			return (pg, flyOb) -> {
				pg.fill(flyOb.color);
				pg.noStroke();
				pg.ellipse(flyOb.position.x, flyOb.position.y, flyOb.size, flyOb.size);
			};
		case LINE:
			return (pg, flyOb) -> {
				pg.fill(flyOb.color);
				pg.stroke(flyOb.color);
				pg.strokeWeight(flyOb.size);
				pg.line(flyOb.position.x, flyOb.position.y, flyOb.prevPosition.x, flyOb.prevPosition.y);
			};
		case POLE:
			return (pg, flyOb) -> {
				pg.pushMatrix();
				pg.translate(flyOb.position.x, flyOb.position.y);

//				System.out.println(b.angle());
				float poleWidth = flyOb.size / 4;
				float poleHeight = flyOb.size;
				float poleStartX = -poleWidth / 2;
				float poleStartY = 0;
				pg.strokeWeight(1);
				pg.stroke(0xFF000000);
				pg.rotate((float) flyOb.angle());
				pg.fill(flyOb.color);
				pg.rect(poleStartX, poleStartY, poleWidth, poleHeight);
				pg.fill(0xFF000000);
				pg.rect(poleStartX, poleStartY, poleWidth, -poleHeight);
				pg.popMatrix();
			};
		case QUADRAT:
			return (pg, flyOb) -> {
				pg.fill(flyOb.color);
				pg.noStroke();
				pg.rect(flyOb.position.x, flyOb.position.y, flyOb.size, flyOb.size);
			};
		case RECT:
			return (pg, flyOb) -> {
				pg.fill(flyOb.color);
				pg.stroke(flyOb.color);
				pg.strokeWeight(flyOb.size);
				pg.rect(flyOb.position.x, flyOb.position.y, flyOb.velocity.x, flyOb.velocity.y);
			};
		case TRIANGLE:
			return (pg, flyOb) -> {
				pg.pushMatrix();
				pg.translate(flyOb.position.x, flyOb.position.y);
				pg.fill(flyOb.color);
				pg.noStroke();
				float size = flyOb.size;
				pg.rotate((float) flyOb.angle());
				pg.triangle(-size, 0, 0, size, size, 0);
				pg.popMatrix();
			};
		default:
			break;

		}
		return null;
	}

	@FunctionalInterface
	public interface FlyingObjectFormPrinter {

		void print(PGraphics pg, FlyingObject flyOb);

	}
}
