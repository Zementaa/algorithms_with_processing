package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Target {

    private CordsEvolutionGenerator evolution;
    private Circle[] circles;
    private int circleSize = 120;
    private int ringSize = circleSize / 8;
    private int cordsHit;
    private int hitPara;
    private int hitRest;
    private ArrayList<Circle> circleHit;
    private int tempCounter = 0;

    public Target(CordsEvolutionGenerator evolution, PGraphics pg, PApplet pa) {
        this.evolution = evolution;
        hitPara = evolution.getHitPara();
        circles = new Circle[32];
        circleHit = new ArrayList<Circle>();
        for (int i = 0; i < circles.length; i++) {
            circles[i] = new Circle(evolution);
            circleHit.add(circles[i]);
        }
        cordsHit = hitPara / 32;
        hitRest = hitPara % 32;
    }

    public void display(PGraphics pg, PApplet pa) {
        pg.noStroke();
        for (int i = 0; i < 8; i++) {
            circles[i].run(pg);
            pg.arc(evolution.getTargetStartX(), evolution.getTargetStartY(), circleSize, circleSize, 0, pa.HALF_PI);
            circles[i + 8].run(pg);
            pg.arc(evolution.getTargetStartX(), evolution.getTargetStartY(), circleSize, circleSize, pa.HALF_PI, pa.PI);
            circles[i + 16].run(pg);
            pg.arc(evolution.getTargetStartX(), evolution.getTargetStartY(), circleSize, circleSize, pa.PI,
                    pa.PI + pa.HALF_PI);
            circles[i + 24].run(pg);
            pg.arc(evolution.getTargetStartX(), evolution.getTargetStartY(), circleSize, circleSize, pa.PI + pa.HALF_PI,
                    2 * pa.PI);
            circleSize = circleSize - ringSize;
        }
        circleSize = 120;
    }

    public void hitChange() {
        int target = evolution.getTargetCounter(); // temporärer Zwischenspeicher
        evolution.setTargetCounter(evolution.getTargetCounter() - tempCounter);
        while (evolution.getTargetCounter() != 0) {
            if (!circleHit.isEmpty()) {
                int lastIndex = circleHit.size() - 1;
                if (circleHit.size() == 1) {
                    hitRest = hitRest - evolution.getTargetCounter();
                    if (hitRest <= 0) {
                        circleHit.get(lastIndex).setRed(circleHit.get(0).getRed()); // Farbe wird auf letzte Farbe im
                                                                                    // Innenkreis umgestellt
                        circleHit.get(lastIndex).setGreen(circleHit.get(0).getGreen());
                        circleHit.get(lastIndex).setBlue(circleHit.get(0).getBlue());
                    }

                } else if (evolution.getTargetCounter() >= cordsHit) {
                    circleHit.get(lastIndex).setRed(circleHit.get(0).getRed()); // Farbe wird auf letzte Farbe im
                                                                                // Innenkreis umgestellt
                    circleHit.get(lastIndex).setGreen(circleHit.get(0).getGreen());
                    circleHit.get(lastIndex).setBlue(circleHit.get(0).getBlue());
                    circleHit.remove(lastIndex);
                }

                evolution.setTargetCounter(evolution.getTargetCounter() - cordsHit);
                if (evolution.getTargetCounter() < 0) {
                    evolution.setTargetCounter(0);
                }
            }
        }
        if (tempCounter < target) {
            tempCounter = target;
        }
    }

}
