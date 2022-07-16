package de.fernunihagen.mci.group2.coopalgoart.shopf.test.quadtree;

import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.Boids;
import lombok.Getter;

@Getter
public class Point {

    private float x;
    private float y;
    private Boids boid;

    
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    
    public Point(float x, float y, Boids boid) {
        this.x = x;
        this.y = y;
        this.boid = boid;
    }

    
    public Boids getBoid() {
        return boid;
    }

}
