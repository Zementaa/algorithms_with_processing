package de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem;

import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import processing.core.PGraphics;

public class Drawing {

    private String string;
    private LsysParameter lsys;
    private PGraphics pg;
    private char current;
    private float len;
    

    public Drawing(PGraphics pg, String string, LsysParameter lsys, float len) {
        this.pg = pg;
        this.string = string;
        this.lsys = lsys;
        this.len = len;
    }

    
    public void convert(float angle) {
        // Startpunkt des Baumes festlegen
        int startX = lsys.getStartTreeX();
        int startY = lsys.getStartTreeY();
        // Anzahl der Bäume
        int count = lsys.getNumberOfTree();
        for (int j = 0; j < count; j++) {
            pg.pushMatrix();
            pg.translate(startX, startY);
            pg.rotate(lsys.getRotateTreeStart());
            if (lsys.isAlternative()) { // Baum wird zur anderen Seite gezeichnet
                angle = -angle;
                pg.stroke(lsys.getTreeColorAlpha());
            } else {
                pg.stroke(lsys.getTreeColor());
            }
            for (int i = 0; i < string.length(); i++) {
                current = string.charAt(i);
                if (current == 'F') {
                    pg.line(0, 0, 0, -len);
                    pg.translate(0, -len); // springt zum Ende der Linie
                } else if (current == '+') {
                    pg.rotate(angle);
                } else if (current == '-') {
                    pg.rotate(-angle);
                } else if (current == '[') {
                    pg.pushMatrix();
                } else if (current == ']') {
                    pg.popMatrix();
                }
            }
            pg.popMatrix();
            startX = (int) (lsys.getGenerator().getRandomValue().nextInt(pg.width));
            startY = (int) (lsys.getGenerator().getRandomValue().nextInt(pg.height / 2) + pg.height / 2);
        }
    }
}
