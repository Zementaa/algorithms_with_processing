package flocking;
import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import processing.core.PApplet;
import processing.core.PGraphics;


public class Flocking extends PApplet{
	public Flocker[] flock;
	int size1;
	int size2;
	int countFlock;
	int closDistance;
	PGraphics pg;
	Random r = new Random();
	//create the flock--> constructor
	CooperationContext contextC;
	public Flocking(CooperationContext context, PGraphics applet, int countFlocks, int distance) {
		
		size1 = applet.width;
		size2 = applet.height;
		countFlock = countFlocks;
		closDistance = distance;
		flock = new Flocker[countFlocks];
		
		//get seed for coorperation
		r.setSeed(context.getSeed());
		contextC = context;
		pg = applet;
	}

	//loop for creating new flocker
	public void setup() {
	 
	  for (int i = 0; i < flock.length -3; i++) {
	    flock[i] = new Flocker();
	  }
	}

	//draw the single flocks
	public void draw() {

	  for (int i = 0; i < flock.length - 3; i++) {
	    flock[i].step(this.contextC);
	    pg.stroke((i + this.pg.width/ 2));
	    flock[i].draw(this.contextC, this.pg);
	  }
	}
	
	
	class Flocker {
	  // setting global parameter of the flocker
		float x = r.nextFloat();
		float y = r.nextFloat();
		float heading = r.nextFloat();
		float speed = r.nextFloat(); 
	  	float radius = r.nextFloat();

	  	void step(CooperationContext context) {

	  		//find the closest Flocker
	  		float closestDistance = closDistance;
	  		Flocker closestFlocker = null;
	  		for (int i = 0; i < flock.length - 3; i++) {
	      
	  			//make sure not to check against yourself
	        if (flock[i] != this) {

			float distance = context.getPApplet().dist(x, y, flock[i].x, flock[i].y);
	          if (distance < closestDistance) {
	            closestDistance = distance;
	            closestFlocker = flock[i];
	          }
	        }
	      }

	      float angleToClosest = pg.parent.atan2(closestFlocker.y-y, closestFlocker.x-x);

	      //prevent case where heading is 350 and angleToClosest is 10
	      if (heading-angleToClosest > PI) {
	        angleToClosest += TWO_PI;
	      } else if (angleToClosest-heading > PI) {
	        angleToClosest -= TWO_PI;
	      }

	      //turn towards closest
	      if (heading < angleToClosest) {
	        heading+=PI/40;
	      } else {
	        heading-=PI/40;
	      }

	      //move in direction
	      x += cos(heading)*speed;
	      y += sin(heading)*speed;

	      //wrap around edges
	      if (x < 0) {
	        x =context.getPApplet().width;
	      }
	      if (x > context.getPApplet().width) {
	        x = 0;
	      }

	      if (y < 0) {
	        y = context.getPApplet().height;
	      }
	      if (y > context.getPApplet().height) {
	        y = 0;
	      }
  	    }

   	    void draw(CooperationContext context, PGraphics pg) {
   	      pg.stroke(r.nextInt());
	      pg.ellipse(x, y, radius, radius);
	      
	    }
	  }


}
