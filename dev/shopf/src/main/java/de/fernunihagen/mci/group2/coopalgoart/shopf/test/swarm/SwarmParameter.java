package de.fernunihagen.mci.group2.coopalgoart.shopf.test.swarm;


public class SwarmParameter {

    private int separateView; // wie groﬂ soll der Abstand zum Nachbarn sein
    private ESwarmShape shape;
    private int obstacleDefense;

    
    public SwarmParameter(int separateView, String form, int obstacleDefense) {
        this.separateView = separateView;
        setShape(form);
        this.obstacleDefense = obstacleDefense;
    }

    
    private void setShape(String form) {
        if (form.equalsIgnoreCase("Punkte")) {
            shape = ESwarmShape.POINTS;
        }
        if (form.equalsIgnoreCase("Kurve")) {
            shape = ESwarmShape.CURVE;
        }
        if (form.equalsIgnoreCase("Dreieck")) {
            shape = ESwarmShape.TRIANGLE;
        }
    }

    
    public int getSeparateView() {
        return separateView;
    }

    
    public ESwarmShape getShape() {
        return shape;
    }

    
    public int getObstacleDefense() {
        return obstacleDefense;
    }

}
