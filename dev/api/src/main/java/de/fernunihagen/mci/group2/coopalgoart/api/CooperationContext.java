package de.fernunihagen.mci.group2.coopalgoart.api;

import java.util.LinkedList;
import java.util.List;
import java.util.function.IntPredicate;

import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import lombok.Data;
import lombok.NonNull;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * The context is used to transport data from
 * 
 * @author bwinzen
 *
 */
@Data
public class CooperationContext {
	private PApplet pApplet;
	private long seed;
	@NonNull
	private PixelCoordinate coordinate;
	private PGraphics prevCanvas;
	private PixelCoordinateCalculationStrategy tactic;
	private boolean screenWillBeCleaned;

	public CooperationContext(long seed) {
		this(null, seed, null, null, null);

	}

	public CooperationContext(PApplet pApplet, long seed, PixelCoordinate coordinate, PGraphics prevCanvas) {
		this.pApplet = pApplet;
		this.seed = seed;
		if (coordinate == null) {
			this.coordinate = new PixelCoordinate(0, 0, 0);
		} else {
			this.coordinate = coordinate;
		}
		this.prevCanvas = prevCanvas;
	}

	public CooperationContext(PApplet pApplet, long seed, PixelCoordinate coordinate, PGraphics prevCanvas,
			PixelCoordinateCalculationStrategy tactic) {
		this(pApplet, seed, coordinate, prevCanvas);
		this.tactic = tactic;
	}

	public void setImageOfLastIteration(PGraphics prevCanvas) {
		this.prevCanvas = prevCanvas;
	}

	public List<PixelCoordinate> filterPixel(IntPredicate filter) {
		List<PixelCoordinate> coordinates = new LinkedList<>();
		if (prevCanvas != null) {
			int[] pixels = prevCanvas.pixels;
			int width = prevCanvas.width;
			for (int i = 0; i < pixels.length; i++) {
				if (filter.test(pixels[i])) {
					int y = i / width;
					int x = i - y * width;
					coordinates.add(new PixelCoordinate(x, y, 0));
				}
			}
		}
		return coordinates;
	}

	public void calculateNextCoordinate() {
		if (tactic != null) {
			coordinate = tactic.calculate(this);
		}
	}
}
