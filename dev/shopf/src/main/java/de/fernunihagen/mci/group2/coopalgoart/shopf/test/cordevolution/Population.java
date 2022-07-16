package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import java.util.ArrayList;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PGraphics;

public class Population {

    private Cords[] cords;
    private ArrayList<DNA> parentspool;
    private CordsEvolutionGenerator evolution;
    private int size;
    private Cords start;

    public Population(CordsEvolutionGenerator evolution, CooperationContext context) {
        this.evolution = evolution;
        size = evolution.getParameter().getNumCords(); // wieviele Schnüre sollen erzeugt werden
        cords = new Cords[size];
        for (int i = 0; i < cords.length; i++) {
            cords[i] = new Cords(evolution, context);
        }
    }

    public void show(PGraphics pg) {
        for (int i = 0; i < cords.length; i++) {
            cords[i].move();
            cords[i].run(pg);
            if (evolution.isCordsConnect()) { // Variation Schnüre verbinden
                connectCords(i, pg);
            }
        }
    }

    public void evaluate(CooperationContext context, PGraphics pg) {
        float maxfit = 0;
        for (int i = 0; i < cords.length; i++) {
            cords[i].calcFitness(context, pg);
            // was ist die höchste Fitness von allen
            if (cords[i].getFitness() > maxfit) {
                maxfit = cords[i].getFitness();
            }
        }
        // normalisiere/
        for (int i = 0; i < cords.length; i++) {
            cords[i].setFitness(cords[i].getFitness() / maxfit);
        }
        parentspool = new ArrayList<DNA>();
        for (int i = 0; i < cords.length; i++) {
            // je höher die Fitness, desto mehr Schnüre dieser Richtung sind im Parentspool um die neue Generation zu
            // finden
            float p = cords[i].getFitness() * 100;
            for (int j = 0; j < p; j++) {
                parentspool.add(cords[i].getDna());
            }
        }
    }

    // Die Kind Generation wird ausgewählt, durch Kreuzung der Elternteile
    public void selection(CooperationContext context) {
        for (int i = 0; i < cords.length; i++) {
            int ran1 = evolution.getRandomValue().nextInt(parentspool.size());
            int ran2 = evolution.getRandomValue().nextInt(parentspool.size());
            int a = (int) Math.floor(ran1);
            int b = (int) Math.floor(ran2);
            DNA parentA = parentspool.get(a);
            DNA parentB = parentspool.get(b);
            DNA child = parentA.crossover(parentB, context);
            child.mutation(context);
            cords[i] = new Cords(evolution, child);
        }
    }

    public int getHitTarget() {
        evolution.setTargetCounter(0);
        for (int i = 0; i < cords.length; i++) {
            if (Math.abs(evolution.getTargetStartX() - cords[i].getPosition().x) < 60
                    && Math.abs(evolution.getTargetStartY() - cords[i].getPosition().y) < 60) {
                evolution.setTargetCounter(evolution.getTargetCounter() + 1);
            }
        }
        return evolution.getTargetCounter();
    }

    // Variation Schnüre verbinden
    private void connectCords(int i, PGraphics pg) {
        if (i > 0) {
            Cords ende = cords[i];
            start = cords[i - 1];
            pg.line(start.getPosition().x, start.getPosition().y, ende.getPosition().x, ende.getPosition().y);
        }
        start = cords[i];
    }
}
