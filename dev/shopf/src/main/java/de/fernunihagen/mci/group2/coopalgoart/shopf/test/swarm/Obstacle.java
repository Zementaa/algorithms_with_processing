package de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm;

import processing.core.PGraphics;
import processing.core.PVector;

public class Obstacle {

    private PVector location;
    private int defense;
    private int heightCylinder = 100;

    
    public Obstacle(SwarmGenerator swarm, PGraphics pg, float x, float y) {
        location = new PVector(x, y);
        defense = swarm.getParameter().getObstacleDefense();
    }

    
    public PVector swimBy(Boids boid) {
        PVector dir = PVector.sub(location, boid.getPosition());
        float drift = dir.mag();
        if (drift < 5)
            drift = 5;
        if (drift > 500)
            drift = 500;
        float force = -1 * defense / (drift * drift);
        dir.setMag(force);
        return dir;
    }

    
    public void display(PGraphics pg) {
        pg.noStroke();
        pg.fill(205, 133, 63);
        pg.rect(location.x - 60, location.y, 120, heightCylinder + 2);
        pg.stroke(0);
        pg.ellipse(location.x, location.y, 120, 100);
        pg.arc(location.x, (location.y + heightCylinder), 120, 100, 0, pg.PI);
        pg.line(location.x - 60, location.y, location.x - 60, location.y + heightCylinder);
        pg.line(location.x + 60, location.y, location.x + 60, location.y + heightCylinder);
        pg.line(location.x - 57, location.y + 17, location.x - 57, location.y + 17 + heightCylinder);
        pg.line(location.x - 50, location.y + 27, location.x - 50, location.y + 27 + heightCylinder);
        pg.line(location.x - 40, location.y + 38, location.x - 40, location.y + 38 + heightCylinder);
        pg.line(location.x - 30, location.y + 44, location.x - 30, location.y + 44 + heightCylinder);
        pg.line(location.x - 20, location.y + 48, location.x - 20, location.y + 48 + heightCylinder);
        pg.line(location.x - 10, location.y + 49, location.x - 10, location.y + 49 + heightCylinder);
        pg.line(location.x, location.y + 50, location.x, location.y + 50 + heightCylinder);
        pg.line(location.x + 10, location.y + 49, location.x + 10, location.y + 49 + heightCylinder);
        pg.line(location.x + 20, location.y + 48, location.x + 20, location.y + 48 + heightCylinder);
        pg.line(location.x + 30, location.y + 44, location.x + 30, location.y + 44 + heightCylinder);
        pg.line(location.x + 40, location.y + 38, location.x + 40, location.y + 38 + heightCylinder);
        pg.line(location.x + 50, location.y + 27, location.x + 50, location.y + 27 + heightCylinder);
        pg.line(location.x + 57, location.y + 17, location.x + 57, location.y + 17 + heightCylinder);
    }

    
    public PVector getLocation() {
        return location;
    }

}
