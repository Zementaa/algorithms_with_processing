package de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration;

import java.util.Random;

/**
 * @author bwinzen
 *
 */
public enum ColorMode {
	RANDOM, RANDOM_GRAY, STATIC, GAUSS, STEAL_COLOR;

	private int findGaussColor(int colorChannel, double randomGaussian) {
		return (int) Math.max(0, Math.min(255, colorChannel + 50 * randomGaussian));
	}

	public int getColor(Random r, int color) {
		switch (this) {
		case RANDOM_GRAY:
			int nextInt = r.nextInt(0x100);
			return (0xFF_000000) | nextInt << 16 | nextInt << 8 | nextInt;
		case RANDOM:
			return (0xFF_000000) | r.nextInt(0x1000000);
		case GAUSS:
			int red = color >> 16 & 0xFF;
			int green = color >> 8 & 0xFF;
			int blue = color & 0xFF;
			return 0xFF000000 | (findGaussColor(red, r.nextGaussian()) << 16)
					| (findGaussColor(green, r.nextGaussian()) << 8) | (findGaussColor(blue, r.nextGaussian()));
		case STEAL_COLOR:
		case STATIC:
		default:
			return color;

		}
	}
	
	public int getSecondColor(Random r, int color) {
		switch (this) {
		case RANDOM_GRAY:
			int nextInt = r.nextInt(0x100);
			return (0xFF_000000) | nextInt << 16 | nextInt << 8 | nextInt;
		case STEAL_COLOR:
		case RANDOM:
			return (0xFF_000000) | r.nextInt(0x1000000);
		case GAUSS:
			int red = color >> 16 & 0xFF;
			int green = color >> 8 & 0xFF;
			int blue = color & 0xFF;
			return 0xFF000000 | (findGaussColor(red, r.nextGaussian()) << 16)
					| (findGaussColor(green, r.nextGaussian()) << 8) | (findGaussColor(blue, r.nextGaussian()));
		case STATIC:
		default:
			return color;

		}
	}

	public int varColor(Random random, int old) {
		switch (this) {
		case RANDOM_GRAY:
			int nextInt = 0xFF & varChannel(random, old & 0xFF, 10);
			return (0xFF_000000) | nextInt << 16 | nextInt << 8 | nextInt;
		case STATIC:
			return old;
		case GAUSS:
		case RANDOM:
		case STEAL_COLOR:
		default:
			return 0xFF_000000 | var(random, old, 10);

		}
	}
	
	public int varColor(Random random, int old, int varianz) {
		switch (this) {
		case RANDOM_GRAY:
			int nextInt = 0xFF & varChannel(random, old & 0xFF, varianz);
			return (0xFF_000000) | nextInt << 16 | nextInt << 8 | nextInt;
		case STATIC:
			return old;
		case GAUSS:
		case RANDOM:
		case STEAL_COLOR:
		default:
			return 0xFF_000000 | var(random, old, varianz);

		}
	}

	public static int var(Random random, int pix, int varianz) {
		int r = pix >> 16 & 0xFF;
		int g = pix >> 8 & 0xFF;
		int b = pix & 0xFF;
		int nPix = 0;
//		switch (random.nextInt(4)) {
//		case 0:
//			nPix = varChannel(random, r, varianz) << 16 | g << 8 | b;
//			break;
//		case 1:
//			nPix = r << 16 | varChannel(random, g, varianz) << 8 | b;
//			break;
//		case 2:
//			nPix = r << 16 | g << 8 | varChannel(random, b, varianz);
//			break;
//		case 3:
//			nPix = varChannel(random, r, varianz) << 16  |  varChannel(random, g, varianz) << 8 | varChannel(random, b, varianz);
//			break;
//		}
		nPix = varChannel(random, r, varianz) << 16  |  varChannel(random, g, varianz) << 8 | varChannel(random, b, varianz);
		return nPix;
	}

	private static int varChannel(Random r, int v, int varianz) {
		return Math.max(0, Math.min(255, v + r.nextInt(varianz) - varianz / 2));
	}
}