package generators;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import flocking.Flocking;
import processing.core.PApplet;
import processing.core.PGraphics;
import lombok.Builder;

@Builder
public class FlockingGenerator implements Generator {
	private Flocking flocking;

	private int countFlocks;
	private int closestDistance;
    long s = 29;
	
	@Override
	public void nextStep(CooperationContext context, PGraphics applet) {
		//create Flocking, if not exist already
		if(flocking == null) {
			flocking = new Flocking(context, applet, countFlocks, closestDistance);
			flocking.setup();
		}
		//shape
		flocking.draw();

		
	}
	
		  	

}
