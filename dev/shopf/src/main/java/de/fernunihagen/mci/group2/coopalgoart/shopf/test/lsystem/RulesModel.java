package de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem;

import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;

public class RulesModel {

    private Rules[] rules = new Rules[8];
    private ETrees eTree;
    private LsysParameter parameter;
    private int start;
    private int stop;

    
    public RulesModel(LsysParameter parameter) {
        this.parameter = parameter;
        rules[0] = new Rules('F', "F[-F]F[+F][F]");         // BTREE
        rules[1] = new Rules('F', "FF+[+F-F-F]-[-F+F+F]");  // CTREE
        rules[2] = new Rules('X', "F[-X]F[+X]-X");          // DTREE
        rules[3] = new Rules('F', "FF");                    // DTREE
        rules[4] = new Rules('X', "F[+X][-X]FX");           // ETREE
        rules[5] = new Rules('F', "FF");                    // ETREE
        rules[6] = new Rules('X', "F-[[X]+X]+F[+FX]-X");    // FTREE
        rules[7] = new Rules('F', "FF");                    // FTREE
    }

    
    public int rulesFinderStart() {
        eTree = parameter.getETree();
        if (eTree == ETrees.BTREE) {
            start = 0;
        }
        if (eTree == ETrees.CTREE) {
            start = 1;
        }
        if (eTree == ETrees.DTREE) {
            start = 2;
        }
        if (eTree == ETrees.ETREE) {
            start = 4;
        }
        if (eTree == ETrees.FTREE) {
            start = 6;
        }
        return start;
    }

    
    public int rulesFinderStop() {
        eTree = parameter.getETree();
        if (eTree == ETrees.BTREE) {
            stop = 1;
        }
        if (eTree == ETrees.CTREE) {
            stop = 2;
        }
        if (eTree == ETrees.DTREE) {
            stop = 4;
        }
        if (eTree == ETrees.ETREE) {
            stop = 6;
        }
        if (eTree == ETrees.FTREE) {
            stop = 8;
        }
        return stop;
    }

    
    public Rules getRule(int num) {
        return rules[num];
    }

    
    public void findAlternative() {
        eTree = parameter.getETree();
        if (eTree == ETrees.BTREE) {
            rules[0] = new Rules('F', "F[-F]F[+F][---F]");
        }
        if (eTree == ETrees.CTREE) {
            rules[1] = new Rules('F', "FF+[+F-F+F]-[-F+F-F]");
        }
        if (eTree == ETrees.DTREE) {
            rules[3] = new Rules('F', "FF[-FXF][+X]");
        }
        if (eTree == ETrees.ETREE) {
            rules[5] = new Rules('F', "-F+F");
        }
        if (eTree == ETrees.FTREE) {
            rules[7] = new Rules('F', "+F-F");
        }
    }
    
}
