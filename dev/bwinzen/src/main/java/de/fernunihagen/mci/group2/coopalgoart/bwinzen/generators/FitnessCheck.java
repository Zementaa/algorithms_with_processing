package de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import javafx.scene.paint.Color;

@FunctionalInterface
public interface FitnessCheck {
	int checkFitness(CooperationContext context, int currentValue, int targetValue);

	public static int checkFitnessRGB(CooperationContext context, int currentValue, int targetValue) {
		int r = currentValue >> 16 & 0xFF;
		int g = currentValue >> 8 & 0xFF;
		int b = currentValue & 0xFF;

		int rT = targetValue >> 16 & 0xFF;
		int gT = targetValue >> 8 & 0xFF;
		int bT = targetValue & 0xFF;
		return Math.abs(r - rT) + Math.abs(g - gT) + Math.abs(b - bT);
	}

	public static int checkFitnessRed(CooperationContext context, int currentValue, int targetValue) {
		int r = currentValue >> 16 & 0xFF;
//		int g = currentValue >> 8 & 0xFF;
//		int b = currentValue & 0xFF;

		int rT = targetValue >> 16 & 0xFF;
//		int gT = targetValue >> 8 & 0xFF;
//		int bT = targetValue & 0xFF;
		return Math.abs(r - rT) * 3; //+ Math.abs(g - gT) + Math.abs(b - bT);
	}

	public static int checkFitnessGreen(CooperationContext context, int currentValue, int targetValue) {
//		int r = currentValue >> 16 & 0xFF;
		int g = currentValue >> 8 & 0xFF;
//		int b = currentValue & 0xFF;

//		int rT = targetValue >> 16 & 0xFF;
		int gT = targetValue >> 8 & 0xFF;
//		int bT = targetValue & 0xFF;
		return Math.abs(g - gT) * 3; // + Math.abs(r - rT) +  Math.abs(b - bT);
	}

	public static int checkFitnessBlue(CooperationContext context, int currentValue, int targetValue) {
//		int r = currentValue >> 16 & 0xFF;
//		int g = currentValue >> 8 & 0xFF;
		int b = currentValue & 0xFF;

//		int rT = targetValue >> 16 & 0xFF;
//		int gT = targetValue >> 8 & 0xFF;
		int bT = targetValue & 0xFF;
		return Math.abs(b - bT) * 100; //+Math.abs(r - rT) + Math.abs(g - gT) ;
	}

	public static int checkFitnessGrayScale(CooperationContext context, int currentValue, int targetValue) {
		int r = currentValue >> 16 & 0xFF;
		int g = currentValue >> 8 & 0xFF;
		int b = currentValue & 0xFF;

		int rT = targetValue >> 16 & 0xFF;
		int gT = targetValue >> 8 & 0xFF;
		int bT = targetValue & 0xFF;

		double brightness = 0.299 * r + 0.587 * g + 0.114 * b;
		double targetBrightness = 0.299 * rT + 0.587 * gT + 0.114 * bT;
		return (int) (Math.abs(brightness - targetBrightness)*3);
	}

	public static int checkFitnessSaturation(CooperationContext context, int currentValue, int targetValue) {
		int r = currentValue >> 16 & 0xFF;
		int g = currentValue >> 8 & 0xFF;
		int b = currentValue & 0xFF;
		
		int rT = targetValue >> 16 & 0xFF;
		int gT = targetValue >> 8 & 0xFF;
		int bT = targetValue & 0xFF;
		return (int) (Math.abs((Color.rgb(r, g, b).getSaturation()- Color.rgb(rT, gT, bT).getSaturation())* 255 *3));
	}

	public static int checkFitnessHue(CooperationContext context, int currentValue, int targetValue) {
		int r = currentValue >> 16 & 0xFF;
		int g = currentValue >> 8 & 0xFF;
		int b = currentValue & 0xFF;
		
		int rT = targetValue >> 16 & 0xFF;
		int gT = targetValue >> 8 & 0xFF;
		int bT = targetValue & 0xFF;
		return (int) (Math.abs((Color.rgb(r, g, b).getHue()- Color.rgb(rT, gT, bT).getHue())* 255 /120)); //3/360
	}

	public static int checkFitnessValue(CooperationContext context, int currentValue, int targetValue) {
		int r = currentValue >> 16 & 0xFF;
		int g = currentValue >> 8 & 0xFF;
		int b = currentValue & 0xFF;
		
		int rT = targetValue >> 16 & 0xFF;
		int gT = targetValue >> 8 & 0xFF;
		int bT = targetValue & 0xFF;
		return (int) (Math.abs((Color.rgb(r, g, b).getBrightness()- Color.rgb(rT, gT, bT).getBrightness())* 255*3));
	}

	/**
	 * Find a fitness-function for id. The fitness-function deliver values between 0 and 3*255 were 0 is the best. 
	 * @param mode
	 * @return
	 */
	public static FitnessCheck find(String mode) {
		FitnessCheck fitnessCheck = null;
		if (mode.equals("grayscale")) {
			fitnessCheck = FitnessCheck::checkFitnessGrayScale;
		} else if (mode.equals("rgb")) {
			fitnessCheck = FitnessCheck::checkFitnessRGB;
		} else if (mode.equals("r")) {
			fitnessCheck = FitnessCheck::checkFitnessRed;
		} else if (mode.equals("g")) {
			fitnessCheck = FitnessCheck::checkFitnessGreen;
		} else if (mode.equals("b")) {
			fitnessCheck = FitnessCheck::checkFitnessBlue;
		} else if (mode.equals("h")) {
			fitnessCheck = FitnessCheck::checkFitnessHue;
		} else if (mode.equals("s")) {
			fitnessCheck = FitnessCheck::checkFitnessSaturation;
		} else if (mode.equals("v")) {
			fitnessCheck = FitnessCheck::checkFitnessValue;
		}
		return fitnessCheck;
	}
}