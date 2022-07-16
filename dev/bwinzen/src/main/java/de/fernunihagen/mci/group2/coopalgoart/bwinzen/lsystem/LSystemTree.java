package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorFactory;
import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;

public abstract class LSystemTree {
	@Getter
	protected LSystem lSystem;
	protected ColorFactory colorFactory;
	
	@Setter
	@Getter
	protected int offsetX;
	@Setter
	@Getter
	protected int offsetY;
	
	public LSystemTree(LSystem lSystem) {
		this.lSystem = lSystem;
	}
	
	
	public abstract void draw(CooperationContext context, PGraphics pg);
	public LSystemTree drawAndNext(CooperationContext context, PGraphics pg) {
		draw(context, pg);
		return next();
	}
	
	public abstract LSystemTree next();
	
	public boolean hasPrevious() {
		return lSystem.getParent() != null;
	}
	
	public abstract LSystemTree previous();
	
	public void setColorFactory(ColorFactory colorFactory) {
		this.colorFactory = colorFactory;
	}
	
	public boolean isMultiColorAble() {
		return false;
	}
}
