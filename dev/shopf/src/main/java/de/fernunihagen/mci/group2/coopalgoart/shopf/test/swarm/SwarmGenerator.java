package de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm;

import java.util.ArrayList;
import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem.LsysParameter;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.quadtree.*;
import lombok.*;
import processing.core.PGraphics;

@Builder
@Getter
public class SwarmGenerator implements Generator, PixelCoordinateCalculationStrategy {

    // Liste mit Boids
    private ArrayList<Boids> boids;
    private int numBoids;
    private int separateView; // wie groﬂ soll der Abstand zum Nachbarn sein
    private String shape;
    private int obstacleDefense; // wie groﬂ ist die Abwehr des Hindernisses
    private Random randomValue;
    private Point point;
    private QuadTree quad;
    private ArrayList<Point> found;
    private Obstacle obstacle;
    private boolean showQuad;
    private boolean showObstacle;
    private SwarmParameter parameter;
    @Builder.Default
    private boolean init = false;

    
    @Override
    public void nextStep(CooperationContext context, PGraphics pg) {
        if (!this.init) { // Anzahl Boids, SeparateView, Gestalt, Abwehrkraft Hindernis
            parameter = new SwarmParameter(separateView, shape, obstacleDefense);
            randomValue = new Random(context.getSeed());
            float t = 0;
            boids = new ArrayList<Boids>();
            for (int i = 0; i < numBoids; i++) {
                if (t <= 1) {
                    t = t + 0.01f;
                } else
                    t = 0.0f;
                boids.add(new Boids(this, context, pg, t));
                this.init = true;
            }
        }
        // wo wird das Hindernis platziert
        PixelCoordinate coordinate = context.getCoordinate();
        if (!(coordinate.x == 0 && coordinate.y == 0 && coordinate.z == 0)) {
            obstacle = new Obstacle(this, pg, coordinate.x, coordinate.y);
        } else {
            obstacle = new Obstacle(this, pg, pg.width / 2, pg.height / 2);
        }
        // Auswahl, ob Hindernis gezeigt wird
        if (showObstacle) {
            obstacle.display(pg);
        }
        // QuadTree
        Rectangle rectangle = new Rectangle(0, 0, pg.width, pg.height);
        quad = new QuadTree(this, rectangle, 4, pg);
        for (int j = 0; j < numBoids; j++) {
            Boids boid = boids.get(j);
            point = new Point(boid.getPosition().x, boid.getPosition().y, boid);
            quad.insert(point, pg);
        }
        for (int j = 0; j < numBoids; j++) {
            Boids boid = boids.get(j);
            boid.edges(pg);
            boid.flock();
            boid.applyObstacle(obstacle);
            boid.shapeFinder(pg);
            boid.move();
        }
        // Auswahl, ob QuadTree gezeigt wird
        if (showQuad)
            quad.show(pg);
    }

    
    // Punkt, der ¸bergeben wird
    @Override
    public PixelCoordinate calculate(CooperationContext cooperationContext) {
        if (obstacle == null) { return cooperationContext.getCoordinate(); }
        return new PixelCoordinate((float) obstacle.getLocation().x, (float) obstacle.getLocation().y, 0);
    }

}
