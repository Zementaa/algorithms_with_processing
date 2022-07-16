package generators;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import processing.core.PGraphics;
import lombok.Builder;
import perlinNoise.PerlinNoise;


@Builder
public class PerlinNoiseGenerator implements Generator {

	private PerlinNoise perlinNoise;
	public static int size_1 = 1000;
	public static int size_2 = 1000;
	private int scl;
	public static int w = 900;
	public static int h = 600;
	private float fly;
	private double increaseFly;
	private double xOff;
	private double yOff;
	private int stroke;
	long s = 29;
	int cols,rows;
	float[][] scape;
	Random r = new Random();
	
	@Override
	public void nextStep(CooperationContext context, PGraphics applet) {
		//create PerlinNoise, if not exist already
		if (perlinNoise == null) {
			applet.setParent(context.getPApplet());
			perlinNoise = new PerlinNoise(context, applet, scl, w, h, fly, increaseFly, xOff, yOff, stroke);
			perlinNoise.setup(context, applet);
		}
		
		//shape
		for (int i = 0; i < scl; i++) {
					perlinNoise.draw(context, applet);

		}		
	}
}
