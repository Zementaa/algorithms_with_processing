package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import java.awt.Color;
import java.util.ArrayList;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PGraphics;
import processing.core.PVector;

public class Cords {

    private CordsEvolutionGenerator evolution;
    private PVector position;
    private PVector speed;
    private PVector acceleration;
    private DNA dna;
    private float fitness;
    private Color col;
    private float lastPositionX;
    private float lastPositionY;

    public Cords(CordsEvolutionGenerator evolution, CooperationContext context) {
        this.evolution = evolution;
        // Startposition
        this.position = new PVector(evolution.getParameter().getStartPositionX(),
                evolution.getParameter().getStartPositionY());
        // Geschwindigkeit
        this.speed = new PVector();
        // Beschleunigung
        this.acceleration = new PVector(0, 0);
        dna = new DNA(evolution, context);
        col = dna.getGenesColor()[evolution.getCount()];
        fitness = 0;
        lastPositionX = this.position.x;
        lastPositionY = this.position.y;
    }

    public Cords(CordsEvolutionGenerator evolution, DNA dna) {
        this.evolution = evolution;
        // Startposition
        this.position = new PVector(evolution.getParameter().getStartPositionX(),
                evolution.getParameter().getStartPositionY());
        // Geschwindigkeit
        this.speed = new PVector();
        // Beschleunigung
        this.acceleration = new PVector(0, 0);
        this.dna = dna;
        if (evolution.getCount() == evolution.getLifespan()) {
            evolution.setCount(0);
        }
        col = dna.getGenesColor()[evolution.getCount()];
        fitness = 0;
        lastPositionX = this.position.x;
        lastPositionY = this.position.y;
    }

    public void move() {
        acceleration.add(dna.getGenes()[evolution.getCount()]); 
     // wenn Ziel erreicht, wird nicht weiter gemalt, Zahl (50) hängt von der Größe (Radius) des Ziels ab
        if (!(Math.abs(evolution.getTargetStartX() - position.x) < 50
                && Math.abs(evolution.getTargetStartY() - position.y) < 50)) {
            speed.add(acceleration);
            position.add(speed);
            acceleration.mult(0);
        }
    }

    public void run(PGraphics pg) {
        pg.strokeWeight(4);
        pg.stroke(col.getRed(), col.getGreen(), col.getBlue());
        pg.point(position.x, position.y);
        pg.line(lastPositionX, lastPositionY, position.x, position.y);
        lastPositionX = this.position.x;
        lastPositionY = this.position.y;
    }

    public void calcFitness(CooperationContext context, PGraphics pg) {
        float d = context.getPApplet().dist(position.x, position.y, evolution.getTargetStartX(),
                evolution.getTargetStartY());
        fitness = context.getPApplet().map(d, 0, pg.width, pg.width, 0);
        if (d < 15) {       // hiermit kann man die Fitness noch ein bisschen boostern (Zahl (Pixelnähe) kann variieren)
            fitness = fitness * 10;
        }
    }

    public PVector getPosition() {
        return position;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public DNA getDna() {
        return dna;
    }

}
