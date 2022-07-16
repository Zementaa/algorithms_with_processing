package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.Obstacle;
import lombok.*;
import processing.core.*;

@Builder
@Getter
public class CordsEvolutionGenerator implements Generator, PixelCoordinateCalculationStrategy {

    private Population population;
    private CordsParameter parameter;
    private int lifespan;
    private int numCords;
    private boolean clearScreen;
    private int targetStartX;
    private int targetStartY;
    private int cordsStartX;
    private int cordsStartY;
    private int hitPara;
    private Random randomValue;
    private PApplet pa;
    private Target target;
    private boolean cordsConnect;
    @Builder.Default
    @Setter
    private int count = 0;
    @Builder.Default
    @Setter
    private int targetCounter = 0;

    @Override
    public void nextStep(CooperationContext context, PGraphics pg) {
        if (context.getPApplet().frameCount < 2) { return; }
        if (population == null) {
            randomValue = new Random(context.getSeed());
            // Lebensspanne Frames pro Generation, StartpositionX, StartpositionY, Anzahl Cords
            parameter = new CordsParameter(lifespan, cordsStartX, cordsStartY, numCords); 
            lifespan = parameter.getLifespan();
            population = new Population(this, context);
            target = new Target(this, pg, pa);
        }
        PixelCoordinate coordinate = context.getCoordinate();
        if (!((coordinate.x == cordsStartX && coordinate.y == cordsStartY))) { 
            targetStartX = (int) coordinate.x;
            targetStartY = (int) coordinate.y;
        } else {
            targetStartX = pg.width / 2 + pg.width / 10;
            targetStartY = pg.height / 2;
        }
        count++;
        if (count == lifespan - 1) {
            clearScreen = true;
            population.show(pg);
            target.display(pg, pa);

        } else if (count == lifespan) {
            targetCounter = population.getHitTarget();
            population.evaluate(context, pg);
            population.selection(context);
            target.hitChange();
        } else {
            population.show(pg);
            target.display(pg, pa);
        }
    }

    @Override
    public boolean forceClearScreen() {
        boolean tempClearScreen = clearScreen;
        clearScreen = false;
        return tempClearScreen;
    }

    @Override
    public PixelCoordinate calculate(CooperationContext cooperationContext) {
        return new PixelCoordinate(cordsStartX, cordsStartY, 0);
    }

}
