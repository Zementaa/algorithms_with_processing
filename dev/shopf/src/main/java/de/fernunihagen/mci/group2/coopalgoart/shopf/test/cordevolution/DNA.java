package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import java.awt.Color;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PVector;

public class DNA {

    private CordsEvolutionGenerator evolution;
    private PVector[] genes;
    private Color[] genesColor;

    public DNA(CordsEvolutionGenerator evolution, CooperationContext context) {
        this.evolution = evolution;
        genes = new PVector[evolution.getLifespan()];
        genesColor = new Color[evolution.getLifespan()];
        for (int i = 0; i < evolution.getLifespan(); i++) {
            // ein zufälliger Random Vector
            float ran1 = -1 + evolution.getRandomValue().nextFloat() * 2;
            float ran2 = -1 + evolution.getRandomValue().nextFloat() * 2;
            genes[i] = new PVector(ran1, ran2);
            genes[i].setMag(0.2f);
            // eine zufällige Farbe
            genesColor[i] = new Color(evolution.getRandomValue().nextInt(256), evolution.getRandomValue().nextInt(256),
                    evolution.getRandomValue().nextInt(256));
        }
    }

    private DNA(CordsEvolutionGenerator evolution, int num) {
        this.evolution = evolution;
        genes = new PVector[num];
        genesColor = new Color[num];
    }

    // hier wird nur zufällig ein Elternteil als neues Kind gewählt, bei
    // anderen Verläufen, könnte man aber auch Teile des einen Elternteil mit dem anderen kreuzen etc.
    public DNA crossover(DNA dna, CooperationContext context) {
        DNA child = new DNA(evolution, evolution.getLifespan());
        int middle = evolution.getRandomValue().nextInt(genes.length);
        for (int i = 0; i < genes.length; i++) {
            if (i > middle) {
                child.genes[i] = genes[i];
                child.genesColor[i] = genesColor[i];
            } else {
                child.genes[i] = dna.genes[i];
                child.genesColor[i] = dna.genesColor[i];
            }
        }
        return child;
    }

    // Mutation erzeugt in 1% der Fälle einen neuen PVector
    public void mutation(CooperationContext context) {
        for (int i = 0; i < genes.length; i++) {
            if (evolution.getRandomValue().nextFloat() < 0.01) {
                float ran1 = -1 + evolution.getRandomValue().nextFloat() * 2;
                float ran2 = -1 + evolution.getRandomValue().nextFloat() * 2;
                genes[i] = new PVector(ran1, ran2);
                genes[i].setMag(0.1f);
                genesColor[i] = new Color(evolution.getRandomValue().nextInt(256),
                        evolution.getRandomValue().nextInt(256), evolution.getRandomValue().nextInt(256));
            }
        }
    }

    public PVector[] getGenes() {
        return genes;
    }

    public Color[] getGenesColor() {
        return genesColor;
    }

}
