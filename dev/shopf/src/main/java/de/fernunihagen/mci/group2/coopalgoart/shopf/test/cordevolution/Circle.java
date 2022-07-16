package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import java.awt.Color;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;

@Getter
@Setter
public class Circle {

    private CordsEvolutionGenerator evolution;
    private int red;
    private int green;
    private int blue;

    public Circle(CordsEvolutionGenerator evolution) {
        this.evolution = evolution;
        red = evolution.getRandomValue().nextInt(256);
        green = evolution.getRandomValue().nextInt(256);
        blue = evolution.getRandomValue().nextInt(256);
    }

    public void run(PGraphics pg) {
        pg.fill(red, green, blue);
    }

}
