package de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.*;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.Obstacle;
import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.SwarmParameter;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PGraphics;

@Builder
public class LsystemGenerator implements Generator, PixelCoordinateCalculationStrategy {

    private Drawing drawing;
    private LsysParameter parameter;
    private RulesModel ruleModel;
    private String treeShape;
    private String string;
    private int startPositionX;
    private int startPositionY;
    private float startRotation;
    private int treeColor;
    private float len;
    private boolean clearScreen;
    private char current;
    private boolean right;
    private int generation;
    private int start;
    private int stop;
    private float angle;
    private float shortBranch;
    private Random randomValue;
    private float treeLocX;
    private float treeLocY;
    private int numberOfTree;
    @Builder.Default
    private boolean init = false;

    
    @Override
    public void nextStep(CooperationContext context, PGraphics pg) {
        if (!this.init) {
            randomValue = new Random(context.getSeed());
            parameter = new LsysParameter(this, startPositionX, startPositionY, startRotation, treeShape, treeColor,
                    numberOfTree, pg);
            // Startbuchstabe
            string = parameter.getStart();
            // Wieviele Generationen
            generation = parameter.getGeneration();
            // Wieviele Regeln an welcher Stelle
            ruleModel = new RulesModel(parameter);
            start = ruleModel.rulesFinderStart();
            stop = ruleModel.rulesFinderStop();
            // Wie lang ist der Stamm
            len = parameter.getBranchLength();
            // Welchen Winkel haben die Zweige
            angle = parameter.getAngle();
            // Wie stark werden die Zweige verkÃ¼rzt
            shortBranch = parameter.getShortBranch();
            this.init = true;

            // für Kooperation:
            // die Pixel Koordinate bestimmt hier nur, ob der Winkel nach rechts oder links geneigt wird
            // wenn die Random Value kleiner 0,5 ist, wird ein entarteter Baum gezeichnet
            PixelCoordinate coordinate = context.getCoordinate();
            if (!(coordinate.x == startPositionX && coordinate.y == startPositionY)) {
                if (coordinate.x > pg.width / 2) {
                    parameter.setAlternative(true);
                } else {
                    parameter.setAlternative(false);
                }
                if (parameter.getRan() < 0.5) {
                    ruleModel.findAlternative();
                }

            }
        }
        drawing = new Drawing(pg, string, parameter, len);
        if (context.getPApplet().frameCount - (generation - 1) <= 0) { // Anzahl der Generationen
            clearScreen = true;
        }
        if (context.getPApplet().frameCount - generation <= 0) {
            substitute(pg);
            len = len * shortBranch;

        }
    }

    
    private void substitute(PGraphics pg) {
        StringBuffer nextString = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            current = string.charAt(i); // String wird in einzelne Buchstaben unterteilt
            for (int j = start; j < stop; j++) { // Regeln werden geprüft
                right = false;
                if (current == ruleModel.getRule(j).getLetter()) {
                    right = true;
                    nextString.append(ruleModel.getRule(j).getString());
                    break;
                }
            }
            if (!right) {
                nextString.append(current);
            }
        }
        string = nextString.toString();
        drawing.convert(angle);

    }

    
    @Override
    public boolean forceClearScreen() {
        boolean tempClearScreen = clearScreen;
        clearScreen = false;
        return tempClearScreen;
    }

    
    // Punkt, der übergeben wird
    @Override
    public PixelCoordinate calculate(CooperationContext cooperationContext) {
        PGraphics pg = cooperationContext.getPrevCanvas();
        Random ran = new Random(cooperationContext.getSeed());
        treeLocX = startPositionX + startRotation * 300;
        treeLocY = startPositionY - (generation * shortBranch * (int) (ran.nextFloat() * 138.9)) + startRotation * 450;
        if (treeLocY < 0) {
            treeLocY = 0;
        }
        return new PixelCoordinate(treeLocX, treeLocY, 0);
    }

    
    public Random getRandomValue() {
        return randomValue;
    }

}
