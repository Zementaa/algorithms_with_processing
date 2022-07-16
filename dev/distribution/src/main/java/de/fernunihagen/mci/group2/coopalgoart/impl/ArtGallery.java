package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.impl.Artist.DebugArtist;
import de.fernunihagen.mci.group2.coopalgoart.impl.Artist.DelayedArtist;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.ArtConfiguration;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.ConfigurationIOHelper;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.GeneratorConfiguration;
import de.fernunihagen.mci.group2.coopalgoart.impl.recorder.Recorder;
import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class ArtGallery {
	/**
	 * @param artConfig
	 * @param debugMode
	 * @param recorder
	 * @return
	 */
	public Exhibit show(ArtConfiguration artConfig, boolean debugMode, Recorder... recorders) {
		String[] processingArgs = {
//				"--present"
				getLocationParameter(artConfig), artConfig.getName() };
		Artist artist = createArtist(artConfig, debugMode);
		if (recorders != null) {
			Arrays.stream(recorders).forEach(artist::register);
		}
		Exhibit exhibit = new Exhibit(artist, artConfig);
		Random r = new Random(artConfig.calculateSeed());
		exhibit.noiseSeed(r.nextLong());
		exhibit.randomSeed(r.nextLong());
		exhibit.setFps(artConfig.getFps());
		PApplet.runSketch(processingArgs, exhibit);
		return exhibit;
	}

	private Artist createArtist(ArtConfiguration artConfig, boolean stepperArtist) {
		CooperationContext defaultContext = createDefaultContext(artConfig);
		List<GeneratorTriple> generatorTriples = createGenerators(artConfig.getGeneratorConfigs(), defaultContext);
		if (stepperArtist) {
			return new DebugArtist(defaultContext, generatorTriples);
		}
		if (artConfig.getDelayBetweenIterations() > 0) {
			return new DelayedArtist(artConfig.getDelayBetweenIterations(), defaultContext, generatorTriples);
		}
		return new Artist(defaultContext, generatorTriples);
	}

	private CooperationContext createDefaultContext(ArtConfiguration artConfig) {
		return new CooperationContext(artConfig.calculateSeed());
	}

	/**
	 * @author grissland
	 *
	 */
	private List<GeneratorTriple> createGenerators(List<GeneratorConfiguration> generatorConfigs,
			CooperationContext defaultContext) {
		List<GeneratorTriple> resultTriple = new LinkedList<>();
		CooperationContext actualContext = defaultContext;
		for (GeneratorConfiguration config : generatorConfigs) {
			actualContext.setScreenWillBeCleaned(config.isClearScreen());
			resultTriple.add(new GeneratorTriple(config, this.createGenerator(config), actualContext));
			actualContext = new CooperationContext(config.parameterToSeed() + actualContext.getSeed());
		}

		return resultTriple;
	}

	private Generator createGenerator(GeneratorConfiguration gConfig) {
		ServiceFactory<Generator> generatorFactory = ServiceRegistry.getServiceFactory(Generator.class,
				gConfig.getGeneratorId());
		if (generatorFactory == null) {
			// TODO
			throw new IllegalArgumentException("Could not create generator. No valid service factory was found");
		}
		Generator generator = generatorFactory.createService(gConfig.getParameter());
//		generator.setup(gConfig.getGeneratorVersion(),gConfig.getXOffset(), gConfig.getYOffset(), gConfig.getWidth(), gConfig.getHeight());
		return generator;
	}

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException(
					"You have to specify an json file in which the configuration is located");
		}
		try {
			ArtConfiguration artConfiguration = ConfigurationIOHelper.load(args[0]);
			showArt(artConfiguration);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"You have to specify an json file in which the configuration is located. " + args[0]
							+ " is invalid");
		}
	}

	public static Exhibit showArt(ArtConfiguration artConfiguration, Recorder... recorders) {
		ArtGallery artGallery = new ArtGallery();
		return artGallery.show(artConfiguration, false, recorders);
	}

	private String getLocationParameter(ArtConfiguration artConfiguration) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = ge.getDefaultScreenDevice();
		// Set displayWidth and displayHeight for people still using those.
		DisplayMode displayMode = device.getDisplayMode();
		int width = displayMode.getWidth();
//	    int height = displayMode.getHeight();
		
//	    Because Window frame is not included
		int y = 25;
		int x = width - artConfiguration.getWidth();
		if (x > 0) {
			x /= 2;
		}
		return "--location=" + x + "," + y;

	}
}
