package de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm;

import java.util.ArrayList;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.quadtree.*;
import processing.core.*;

public class Boids {

    private SwarmGenerator swarm;
    private ESwarmShape shape;
    private PVector position;
    private PVector speed;
    private PVector acceleration;
    private PVector alignment;
    private PVector cohesion;
    private PVector separation;
    private float maxForce; // begrenzt die Kraft (praktisch die Anziehungskraft)
    private float maxSpeed; // mit maximaler Geschwindigkeit in Richtung des Nachbarn
    private int r = 2;
    private float tint; // Farbton
    private float originalTint;

    
    public Boids(SwarmGenerator swarm, CooperationContext context, PGraphics pg, float tint) {
        this.swarm = swarm;
        // Startposition
        this.position = new PVector(swarm.getRandomValue().nextFloat() * pg.width,
                swarm.getRandomValue().nextFloat() * pg.height);
        // Geschwindigkeit
        float ran1 = -1 + swarm.getRandomValue().nextFloat() * 2;
        float ran2 = -1 + swarm.getRandomValue().nextFloat() * 2;
        this.speed = new PVector(ran1, ran2);
        // Beschleunigung
        this.acceleration = new PVector(0, 0);
        this.maxForce = 0.03f;
        this.maxSpeed = 4;
        this.originalTint = tint;
        this.tint = tint;

    }

    
    // Verhalten am Rand des Bildschirms - Punkte kommen an der gegenüberliegenden Seite wieder heraus
    public void edges(PGraphics pg) {
        if (this.position.x > pg.width) {
            this.position.x = 0;
        } else if (this.position.x < 0) {
            this.position.x = pg.width;
        }
        if (this.position.y > pg.height) {
            this.position.y = 0;
        } else if (this.position.y < 0) {
            this.position.y = pg.height;
        }
    }

    
    // die durchschnittliche Ausrichtung der Nachbarn
    private PVector align() {
        PVector direction = new PVector();
        // wie groß soll der Wahrnehmungsumkreis sein
        int viewRadius = 50;
        int count = 0;
        Circle circle = new Circle(this.position.x, this.position.y, viewRadius * 2);
        ArrayList<Point> found = new ArrayList<Point>();
        found = swarm.getQuad().query(circle, found);
        for (Point point : found) {
            Boids boid = point.getBoid();
            // Distanz vom aktuellem Boid zum Nachbarboid
            float neighbours = PVector.dist(this.position, boid.position);

            if (neighbours > 0 && neighbours < viewRadius) {
                // Ausrichtungen der Nachbarn werden summiert
                direction.add(boid.speed);
                // wenn die Ausrichtung ähnlich ist, nimm die Farbe des Nachbarn an
                this.tint = boid.tint;
                count++;
                // ansonsten nimm wieder deine Originalfarbe an
            } else if (neighbours > (viewRadius * 2 + 20)) {
                this.tint = originalTint;
            }
        }
        if (count > 0) {
            // Durchschnitt errechnen
            direction.div(count);
            // Länge des Vektors skalieren, also wie schnell soll er zum nächsten Nachbarn fliegen
            direction.setMag(maxSpeed);
            // Die Richtung, in die man möchte (die Richtung, in die man sich bewegt minus die Richtung, in die man
            // möchte)
            direction.sub(this.speed);
            // Größe des Vektors wird begrenzt
            direction.limit(maxForce);
        }
        return direction;
    }

    
    // den durchschnittliche Standort der Nachbarn
    private PVector cohesion() {
        PVector direction = new PVector();
        // wie groß soll der Wahrnehmungsumkreis sein
        int viewRadius = 50;
        int count = 0;
        Circle circle = new Circle(this.position.x, this.position.y, 50 * 2);
        ArrayList<Point> found = new ArrayList<Point>();
        found = swarm.getQuad().query(circle, found);
        for (Point point : found) {
            Boids boid = point.getBoid();
            // Distanz vom aktuellem Boid zum Nachbarboid
            float neighbours = PVector.dist(this.position, boid.position);

            if (neighbours > 0 && neighbours < viewRadius) {
                // Standorte der Nachbarn werden summiert
                direction.add(boid.position);
                count++;
            }
        }
        if (count > 0) {
            // Durchschnitt errechnen
            direction.div(count);
            // Den Standort, zu dem man möchte (der Standort, zu dem man sich bewegt minus den Standort, zu dem man
            // möchte)
            direction.sub(this.position);
            // Länge des Vektors skalieren, also wie schnell soll er zum nächsten Nachbarn fliegen
            direction.setMag(maxSpeed);
            // Die Richtung, in die man möchte (die Richtung, in die man sich bewegt minus die Richtung, in die man
            // möchte)
            direction.sub(this.speed);
            // Größe des Vektors wird begrenzt
            direction.limit(maxForce);
        }
        return direction;
    }

    
    // Nachbarn sollen nicht zu dicht zusammen sein
    private PVector separate() {
        PVector direction = new PVector(0, 0, 0);
        // wie groß soll der Wahrnehmungsumkreis sein
        int viewRadius = swarm.getParameter().getSeparateView();
        if (shape == ESwarmShape.CURVE) {
            viewRadius = swarm.getParameter().getSeparateView() * 2; // Die Curves sind größer, müssen weiter
                                                                     // auseinander schwimmen
        }
        int count = 0;
        Circle circle = new Circle(this.position.x, this.position.y, swarm.getParameter().getSeparateView() * 2);
        ArrayList<Point> found = new ArrayList<Point>();
        found = swarm.getQuad().query(circle, found);
        for (Point point : found) {
            Boids boid = point.getBoid();
            // Distanz vom aktuellem Boid zum Nachbarboid
            float neighbours = PVector.dist(this.position, boid.position);

            if (neighbours > 0 && neighbours < viewRadius) {
                // Ein Vektor der vom Nachbarn auf meinen Standort zeigt und je nachdem, wie weit der Nachbar weg ist,
                // den Boid noch proportional zur Gesamtentfernung in die Gegenrichtung fliegen lässt
                PVector inverseDirection = PVector.sub(this.position, boid.position);
                inverseDirection.normalize();
                inverseDirection.div(neighbours);

                // Standorte der Gegenrichtungen werden summiert
                direction.add(inverseDirection);
                count++;
            }
        }
        if (count > 0) {
            // Durchschnitt errechnen
            direction.div(count);
            // Länge des Vektors skalieren, also wie schnell soll er zum nächsten Nachbarn fliegen
            direction.setMag(maxSpeed);
            // Die Richtung, in die man möchte (die Richtung, in die man sich bewegt minus die Richtung, in die man
            // möchte)
            direction.sub(speed);
            // Größe des Vektors wird begrenzt
            direction.limit(maxForce);
        }

        return direction;
    }

    
    public void applyObstacle(Obstacle obstacle) {
        // Kraft, die berechnet wird, um ein Hinderniss zu umgehen
        Circle circle = new Circle(this.position.x, this.position.y, 50 * 2);
        ArrayList<Point> found = new ArrayList<Point>();
        found = swarm.getQuad().query(circle, found);
        for (Point point : found) {
            Boids boid = point.getBoid();
            PVector force = obstacle.swimBy(boid);
            boid.acceleration.add(force);
        }
    }

    
    // Verhalten zufügen
    public void flock() {
        alignment = align();
        cohesion = cohesion();
        separation = separate();
        separation.mult(1.5f);
        alignment.mult(1.0f);
        cohesion.mult(1.0f);
        acceleration.add(alignment);
        acceleration.add(separation);
        acceleration.add(cohesion);
    }

    
    public void shapeFinder(PGraphics pg) {
        shape = swarm.getParameter().getShape();
        if (shape == ESwarmShape.POINTS) {
            shapesPoints(pg);
        }
        if (shape == ESwarmShape.CURVE) {
            shapesCurves(pg);
        }
        if (shape == ESwarmShape.TRIANGLE) {
            shapesTriangle(pg);
        }
    }

    
    // Gestalt der Boids = Punkte
    private void shapesPoints(PGraphics pg) {
        pg.colorMode(pg.HSB);
        pg.stroke(tint * 360, 255, 255);
        pg.strokeWeight(5);
        pg.fill(255);
        pg.ellipse(position.x, position.y, 5, 5);
    }

    
    // Gestalt: Dreieck, was in die Richtung von Speed rotiert
    private void shapesTriangle(PGraphics pg) {
        // heading berechnet den Winkel der Rotation des Vektors vom Ursprung aus.
        float theta = speed.heading() + PApplet.radians(90);
        pg.fill(200, 100);
        pg.colorMode(pg.HSB);
        pg.stroke(tint * 360, 255, 255);
        pg.pushMatrix();
        pg.translate(position.x, position.y); // Spitze des Vektors wird 0
        pg.rotate(theta); // wird um 90 Grad und dann nochmal um Winkel des Vektors gedreht
        pg.beginShape(pg.TRIANGLES); // Dreieck wird gemalt
        pg.vertex(0, -r * 2);
        pg.vertex(-r, r * 2);
        pg.vertex(r, r * 2);
        pg.endShape();
        pg.popMatrix();
    }

    
    // Gestalt: geschwungene Linien, die in die Richtung von Speed rotieren
    private void shapesCurves(PGraphics pg) {
        // heading berechnet den Winkel der Rotation des Vektors vom Ursprung aus.
        float theta = speed.heading();
        pg.colorMode(pg.HSB);
        pg.stroke(tint * 360, 255, 255);
        pg.strokeWeight(4);
        pg.pushMatrix();
        pg.translate(position.x, position.y); // Spitze des Vektors wird 0
        pg.rotate(theta); // wird um Winkel des Vektors gedreht
        pg.beginShape();
        pg.noFill();
        pg.vertex(0, 30);
        pg.bezierVertex(-20, 10, -40, 50, -60, 30);
        pg.endShape();
        pg.popMatrix();
    }

    
    // Bewegung
    public void move() {
        speed.add(acceleration);
        speed.limit(maxSpeed);
        position.add(speed);
        acceleration.mult(0);
    }

    
    public PVector getPosition() {
        return position;
    }

}
