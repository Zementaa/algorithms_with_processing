package de.fernunihagen.mci.group2.coopalgoart.shopf.test.quadtree;

import java.util.ArrayList;

import de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm.SwarmGenerator;
import processing.core.PGraphics;


public class QuadTree {

    private SwarmGenerator quad;
    private Rectangle rectangle;
    private int toGuard;
    private ArrayList<Point> points;
    private boolean divided;
    private QuadTree leftup;
    private QuadTree rightup;
    private QuadTree leftdown;
    private QuadTree rightdown;

    
    public QuadTree(SwarmGenerator quad, Rectangle rectangle, int toGuard, PGraphics pg) {
        this.quad = quad;
        this.rectangle = rectangle;
        this.toGuard = toGuard;
        points = new ArrayList<Point>(); // Dinge, die man beobachten möchte
        divided = false;
    }

    
    // in QuadTree einfügen
    public void insert(Point point, PGraphics pg) {
        if (!rectangle.contains(point)) { return; }
        if (points.size() < toGuard) {
            points.add(point);
        } else {
            if (!this.divided) {
                subdivide(pg);
            }
            leftup.insert(point, pg);
            rightup.insert(point, pg);
            leftdown.insert(point, pg);
            rightdown.insert(point, pg);
        }

    }

    
    // QuadTree vierteln
    private void subdivide(PGraphics pg) {
        Rectangle lu = new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getW() / 2, rectangle.getH() / 2);
        Rectangle ru = new Rectangle(rectangle.getX() + rectangle.getW() / 2, rectangle.getY(), rectangle.getW() / 2,
                rectangle.getH() / 2);
        Rectangle ld = new Rectangle(rectangle.getX(), rectangle.getY() + rectangle.getH() / 2, rectangle.getW() / 2,
                rectangle.getH() / 2);
        Rectangle rd = new Rectangle(rectangle.getX() + rectangle.getW() / 2, rectangle.getY() + rectangle.getH() / 2,
                rectangle.getW() / 2, rectangle.getH() / 2);
        leftup = new QuadTree(quad, lu, this.toGuard, pg);
        rightup = new QuadTree(quad, ru, this.toGuard, pg);
        leftdown = new QuadTree(quad, ld, this.toGuard, pg);
        rightdown = new QuadTree(quad, rd, this.toGuard, pg);
        this.divided = true;

    }

    
    public ArrayList<Point> query(Rectangle rect, ArrayList<Point> found) {
        if (!this.rectangle.intersects(rect)) { // wenn sich die Bereiche nicht schneiden, wird eine leere Liste
                                                // übergeben
            return found;
        } else {
            for (int i = 0; i < points.size(); i++) { // ansonsten werden die Punkte in diesem Bereich in die Liste
                                                      // geschrieben
                Point point2 = points.get(i);
                if (rect.contains(point2)) {
                    found.add(point2);
                }
            }
        }
        if (this.divided) { // und praktisch die Kinder des QuadTree
            leftup.query(rect, found);
            rightup.query(rect, found);
            leftdown.query(rect, found);
            rightdown.query(rect, found);
        }
        return found;
    }

    
    public ArrayList<Point> query(Circle circle, ArrayList<Point> found) {
        if (!this.rectangle.intersects(circle)) { // wenn sich die Bereiche nicht schneiden, wird eine leere Liste
                                                  // übergeben
            return found;
        } else {
            for (int i = 0; i < points.size(); i++) { // ansonsten werden die Punkte in diesem Bereich in die Liste
                                                      // geschrieben
                // quad.count++;
                Point point2 = points.get(i);
                if (circle.contains(point2)) {
                    found.add(point2);
                }
            }
        }
        if (this.divided) { // und praktisch die Kinder des QuadTree
            leftup.query(circle, found);
            rightup.query(circle, found);
            leftdown.query(circle, found);
            rightdown.query(circle, found);
        }
        return found;
    }

    
    public void show(PGraphics pg) {
        pg.stroke(255);
        pg.strokeWeight(1);
        pg.noFill();
        pg.rect(rectangle.getX(), rectangle.getY(), rectangle.getW(), rectangle.getH());
        if (divided) {
            leftup.show(pg);
            rightup.show(pg);
            leftdown.show(pg);
            rightdown.show(pg);
        }
    }
}
