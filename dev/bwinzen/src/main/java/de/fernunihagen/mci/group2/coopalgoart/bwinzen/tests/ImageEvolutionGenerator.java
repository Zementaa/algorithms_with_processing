package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import java.util.Arrays;
import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.FitnessCheck;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

@Builder
public class ImageEvolutionGenerator implements Generator {
	private String imagePath;
	private String mode;
	private PImage targetImage;
	@Builder.Default
	private float pixelSize = 20;
	private int[] lastFitness;
	private int[] pixel;
	private int[] pixelTargets;
	private FitnessCheck fitnessCheck;

	private int xOffset;
	private int yOffset;
	private int pixelInX;
	private int pixelInY;
	@Builder.Default
	private int varianz = 20;
	private long fitness;
	private boolean reverse;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {
		if (targetImage == null) {
			init(context, pg);
		}
		
		pg.translate(0, 0,-500);
//		pg.rotateX(PApplet.PI / 2.2f);
//		pg.translate(-pixelInX*pixelSize/2, -pixelInY*pixelSize / 2, 500);
		
		drawImage(pg);

		long nFitness = checkFitness(context);
		if(Math.abs(nFitness - fitness)<=pixel.length/40000) {
			//reverse process
			reverse = nFitness<100000;
		}
		fitness = nFitness;
//		System.out.println(context.getPApplet().frameCount+". fitness "+fitness);
	}
	
	

	
	private void drawImage(PGraphics pg) {
		pg.noStroke();
		for (int y = 0; y < pixelInY; y++) {
			int yOff = y * pixelInX;
			for (int x = 0; x < pixelInX; x++) {
				int index = x + yOff;
				pg.fill(0xFF_000000 | pixel[index]);
				pg.rect(xOffset + x * pixelSize, yOffset + y * pixelSize, pixelSize, pixelSize);
			}
		}
	}

	private void init(CooperationContext context, PGraphics pg) {
		fitnessCheck = FitnessCheck.find(mode);
		targetImage = context.getPApplet().loadImage(imagePath);

		double scaleX = ((double) pg.width) / targetImage.width;
		double scaleY = ((double) pg.height) / targetImage.height;
		int width;
		int height;
		if (scaleX < scaleY) {
			width = (int) (targetImage.width * scaleX);
			height = (int) (targetImage.height * scaleX);
			xOffset = 0;
			yOffset = (pg.height - height) / 2;
		} else {
			width = (int) (targetImage.width * scaleY);
			height = (int) (targetImage.height * scaleY);
			xOffset = (pg.width - width) / 2;
			yOffset = 0;
		}
		pixelInX = (int) (width / pixelSize);
		pixelInY = (int) (height / pixelSize);
		//press image into pg
		targetImage.resize(pixelInX, pixelInY);
		
		pixel = new int[targetImage.width * targetImage.height];
		Random random = new Random(context.getSeed());
		for (int i = 0; i < pixel.length; i++) {
			pixel[i] = random.nextInt(256) << 16 | random.nextInt(256) << 8 | random.nextInt(256);
		}
		lastFitness = new int[targetImage.width * targetImage.height];
		Arrays.fill(lastFitness, Integer.MAX_VALUE);
		pixelTargets = targetImage.pixels;

		// Add frame//no pixel area around image
		PImage tmpTargetImage = context.getPApplet().createImage(pg.width, pg.width, PApplet.HSB);
		tmpTargetImage.copy(targetImage, 0, 0, targetImage.width, targetImage.height, xOffset, yOffset, width, height);
		targetImage = tmpTargetImage;
	}

	private long checkFitness(CooperationContext context) {
		Random random = new Random();
		long fitness = 0;
		for (int i = 0; i < pixel.length; i++) {
			int lFit = lastFitness[i];
			int pix = pixel[i];
			int tPix = pixelTargets[i];

			int r = pix >> 16 & 0xFF;
			int g = pix >> 8 & 0xFF;
			int b = pix & 0xFF;
			int nPix = 0;
			switch (random.nextInt(3)) {
			case 0:
				nPix = var(random, r) << 16 | g << 8 | b;
				break;
			case 1:
				nPix = r << 16 | var(random, g) << 8 | b;
				break;
			case 2:
				nPix = r << 16 |  g<<8  | var(random,b);
				break;
			}
//			nPix =  var(random,r) << 16 |   var(random,g)<<8  | var(random,b);
			int nFit = fitnessCheck.checkFitness(context, nPix, tPix);
			boolean decision = nFit < lFit;
			if(reverse) {
				decision = !decision;
			}
			if (decision) {
				pixel[i] = nPix;
				lastFitness[i] = nFit;
				fitness +=nFit;
			}else {
				fitness +=lFit;
			}
			
		}
		return fitness;
	}
	private int var(Random r, int v) {
		return Math.max(0, Math.min(255, v + r.nextInt(varianz) - varianz / 2));
	}
}
