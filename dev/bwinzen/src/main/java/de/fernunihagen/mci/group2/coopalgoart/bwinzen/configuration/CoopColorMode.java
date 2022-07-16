package de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration;

import java.util.Random;
import java.util.function.IntPredicate;

import javafx.scene.paint.Color;

/**
 * @author bwinzen
 *
 */
public enum CoopColorMode {
	NO, STRONG, MIDDLE, WEAK, HIGH_RED, HIGH_GREEN, HIGH_BLUE, BRIGHT, DARK, HIGH_SATURATION;

	private static final int STRONG_VALUE = 0xF8;
	private static final int MIDDLE_STRONG_VALUE = 0xC4;
	private static final int WEAK_VALUE = 0x80;
	public int getRandomColorInRange(Random randomGenerator) {
		int minR=STRONG_VALUE;
		int minG=STRONG_VALUE;
		int minB=STRONG_VALUE;
		int maxR=0xFF;
		int maxG=0xFF;
		int maxB=0xFF;
		
		switch(this) {
		case BRIGHT:
			break;
		case DARK:
			break;
		case HIGH_BLUE:
			minR = 0;
			minG = 0;
			break;
		case HIGH_GREEN:
			minR = 0;
			minB = 0;
			break;
		case HIGH_RED:
			minG = 0;
			minB = 0;
			break;
		case HIGH_SATURATION:
			minR = 0;
			minG = 0;
			minB = 0;
			break;
		case MIDDLE:
			minR = 0;
			minG = 0;
			minB = 0;
			break;
		case WEAK:
			minR = WEAK_VALUE;
			minG = WEAK_VALUE;
			minB = WEAK_VALUE;
			break;
		case NO:
			minR = 0;
			minG = 0;
			minB = 0;
			break;
		case STRONG:
		default:
			break;
		}
		int r = (int) (minR+randomGenerator.nextFloat()*(maxR-minR));
		int g = (int) (minG+randomGenerator.nextFloat()*(maxG-minG));
		int b = (int) (minB+randomGenerator.nextFloat()*(maxB-minB));
		
		int r2 = (int) (minR+randomGenerator.nextFloat()*(maxR-minR));
		int g2 = (int) (minG+randomGenerator.nextFloat()*(maxG-minG));
		int b2 = (int) (minB+randomGenerator.nextFloat()*(maxB-minB));
		
		int[] rgb = new int[] { r<<16, g<<8, b, 0, r2<<16, g2<<8, b2 };
		return 0xFFFFFF & (rgb[randomGenerator.nextInt(rgb.length)]
				| rgb[randomGenerator.nextInt(rgb.length)] | rgb[randomGenerator.nextInt(rgb.length)]);
	}
	
	public IntPredicate getPredicate() {
		switch (this) {
		case STRONG:
			return getPredicateOr(STRONG_VALUE,STRONG_VALUE,STRONG_VALUE);
		case MIDDLE:
			return getPredicateOr(MIDDLE_STRONG_VALUE,MIDDLE_STRONG_VALUE,MIDDLE_STRONG_VALUE);
		case WEAK:
			return getPredicateOr(WEAK_VALUE,WEAK_VALUE,WEAK_VALUE);
		case BRIGHT:
			return getPredicateBrightnessHigher(0.7);
		case DARK:
			return getPredicateBrightnessHigher(0.3).negate();
		case HIGH_BLUE:
			return getPredicateShift(0xF8, 0);
		case HIGH_GREEN:
			return getPredicateShift(0xF8, 8);
		case HIGH_RED:
			return getPredicateShift(0xF8, 16);
		case HIGH_SATURATION:
			return (v) -> {//
				int r = v >> 16 & 0xFF;
				int g = v >> 8 & 0xFF;
				int b = v & 0xFF;
				double min = Math.min(r, Math.min(g, b));
				double max = Math.max(r, Math.max(g, b));
				return Math.min((max-min)/(max+min), 255)>0.7;
			};
		case NO:
		default:
			return null;
		}

	}
	
	public static IntPredicate getPredicateBrightnessHigher(double minBrightness) {
		return v->{
			int r = v >> 16 & 0xFF;
			int g = v >> 8 & 0xFF;
			int b = v & 0xFF;
			return Color.rgb(r, g, b).getBrightness()>minBrightness;
		};
	}
	
	public static IntPredicate getPredicateShift(int minColorChannel, int shift) {
		return (value) -> {//
			int colorChannel = value >> shift & 0xFF;
			return colorChannel >= minColorChannel;
		};
	}
	public static IntPredicate getPredicateOr(int minRed, int minGreen, int minBlue) {
		return (value) -> {//
			int r = value >> 16 & 0xFF;
			int g = value >> 8 & 0xFF;
			int b = value & 0xFF;
			return r >= minRed || g >= minGreen || b >= minBlue;
		};
	}

}
