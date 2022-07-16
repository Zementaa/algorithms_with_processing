package de.fernunihagen.mci.group2.coopalgoart.shopf.test.quadtree;

import lombok.Getter;

@Getter
public class Circle {

    private float x;
    private float y;
    private int r;
    private boolean c;

    
    public Circle(float x, float y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    
    public boolean contains(Point point) {
        if (point.getX() > Math.abs(this.x - this.r) && point.getX() < Math.abs(this.x + this.r)
                && point.getY() > Math.abs(this.y - this.r) && point.getY() < Math.abs(this.y + this.r)) {
            c = true;
        } else {
            c = false;
        }
        return c;
    }

}
