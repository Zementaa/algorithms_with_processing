package de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem;

import lombok.Getter;
import processing.core.*;

@Getter
public class LsysParameter {

    private LsystemGenerator generator;
    private int numRules;
    private float angle;
    private int generation;
    private String start;
    private int branchLength;
    private float shortBranch;
    private int startTreeX;
    private int startTreeY;
    private float rotateTreeStart;
    private ETrees eTree;
    private int treeColor;
    private float treeLocX;
    private boolean alternative;
    private float ran;
    private int colorCount = 0;
    private int numberOfTree;

    
                        // Generator; Startpunkt x, y; Anfangsrotation; Baumart; Farbe; Anzahl; Grafik
    public LsysParameter(LsystemGenerator generator, int startTreeX, int startTreeY, float rotateTreeStart, String form,
            int treeColor, int numberOfTree, PGraphics pg) {
        this.generator = generator;
        this.startTreeX = startTreeX;
        this.startTreeY = startTreeY;
        this.rotateTreeStart = rotateTreeStart;
        this.treeColor = treeColor;
        this.numberOfTree = numberOfTree;
        setTreeShape(form);
        setTreeParameter();
    }

    
    private void setTreeShape(String form) {
        if (form.equalsIgnoreCase("gerade, kleine Zweige")) {
            eTree = ETrees.BTREE;
        }
        if (form.equalsIgnoreCase("geneigt, grÃ¶ÃŸere Zweige")) {
            eTree = ETrees.CTREE;
        }
        if (form.equalsIgnoreCase("gerade, wenig Zweige")) {
            eTree = ETrees.DTREE;
        }
        if (form.equalsIgnoreCase("gerade, symmetrisch")) {
            eTree = ETrees.ETREE;
        }
        if (form.equalsIgnoreCase("geneigt, kleine Zweige")) {
            eTree = ETrees.FTREE;
        }
    }

    
    private void setTreeParameter() {
        if (eTree == ETrees.BTREE) {
            numRules = 1;
            ran = generator.getRandomValue().nextFloat();
            angle = (float) (Math.PI / 8 * (ran + 0.27)); // wird durch andere Parameter beeinflußt
            generation = 6;
            start = "F";
            branchLength = (int) (ran * 138.9); // Größe wird durch andere Parameter beeinflußt
            shortBranch = 0.6f;
        }
        if (eTree == ETrees.CTREE) {
            numRules = 1;
            ran = generator.getRandomValue().nextFloat();
            angle = (float) (Math.PI / 6 * (ran + 0.27)); // wird durch andere Parameter beeinflußt
            generation = 5;
            start = "F";
            branchLength = (int) (ran * 138.9); // Größe wird durch andere Parameter beeinflußt
            shortBranch = 0.5f;
        }
        if (eTree == ETrees.DTREE) {
            numRules = 2;
            ran = generator.getRandomValue().nextFloat();
            angle = (float) (Math.PI / 8 * (ran + 0.27)); // wird durch andere Parameter beeinflußt
            generation = 8;
            start = "X";
            branchLength = (int) (ran * 138.9); // Größe wird durch andere Parameter beeinflußt

            shortBranch = 0.58f;
        }
        if (eTree == ETrees.ETREE) {
            numRules = 2;
            ran = generator.getRandomValue().nextFloat();
            angle = 0.139f * (float) (Math.PI * (ran + 0.27)); // wird durch andere Parameter beeinflußt
            generation = 8;
            start = "X";
            branchLength = (int) (ran * 138.9); // Größe wird durch andere Parameter beeinflußt
            shortBranch = 0.58f;
        }
        if (eTree == ETrees.FTREE) {
            numRules = 2;
            ran = generator.getRandomValue().nextFloat();
            angle = 0.139f * (float) (Math.PI * (ran + 0.27)); // wird durch andere Parameter beeinflußt
            generation = 7;
            start = "X";
            branchLength = (int) (ran * 138.9); // Größe wird durch andere Parameter beeinflußt
            shortBranch = 0.58f;
        }
    }

    
    public int getTreeColorAlpha() {
        int alpha = (treeColor >> 24 & 0xFF) - generator.getRandomValue().nextInt() * 50;
        treeColor = (alpha << 24) | (treeColor & 0xFFFFFF);
        colorCount++;
        return treeColor;
    }

    
    public int setTreeColor(int treeColor) {
        int alpha = (treeColor >> 24 & 0xFF) - colorCount * 10;
        treeColor = (alpha << 24) | (treeColor & 0xFFFFFF);
        colorCount++;
        System.out.println(colorCount);
        return treeColor;
    }

    
    public void setAlternative(boolean alternative) {
        this.alternative = alternative;
    }

}
