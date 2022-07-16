package de.fernunihagen.mci.group2.coopalgoart.shopf.test.quadtree;

import lombok.Getter;

@Getter
public class Rectangle {

    private float x;
    private float y;
    private float w;
    private float h;
    private boolean c;

    
    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    
    public boolean contains(Point point) {
        if (point.getX() >= x && point.getX() <= (x + w) && point.getY() >= (y) && point.getY() <= (y + h)) {
            c = true;
        } else {
            c = false;
        }
        return c;
    }

    
    public boolean intersects(Rectangle rectangle) {
        return !(this.x > rectangle.x + rectangle.w || this.x + this.w < rectangle.x
                || this.y > rectangle.y + rectangle.h || this.y + this.h < rectangle.y);
    }

    
    public boolean intersects(Circle circle) {
        return !(this.x > this.x + this.w + circle.getR() || this.x + this.w + circle.getR() < circle.getX()
                || this.y > this.y + this.h + circle.getR() || this.y + this.h + circle.getR() < circle.getY());
    }

}
