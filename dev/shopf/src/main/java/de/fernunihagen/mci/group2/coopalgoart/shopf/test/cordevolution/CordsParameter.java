package de.fernunihagen.mci.group2.coopalgoart.shopf.test.cordevolution;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import lombok.Getter;
import processing.core.PGraphics;
import processing.core.PVector;

@Getter
public class CordsParameter {

    private PVector target;
    private int lifespan;
    private int startPositionX;
    private int startPositionY;
    private int numCords;

    public CordsParameter(int lifespan, int startPositionX, int startPositionY, int numCords) {
        this.lifespan = lifespan;
        this.startPositionX = startPositionX;
        this.startPositionY = startPositionY;
        this.numCords = numCords;
    }
}
