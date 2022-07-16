package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinate;
import de.fernunihagen.mci.group2.coopalgoart.api.cooperation.PixelCoordinateCalculationStrategy;
import de.fernunihagen.mci.group2.coopalgoart.api.ui.UIInteractionProvider;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.GeneratorConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.opengl.PGraphics3D;

/**
 * @author bwinzen
 *
 */
@RequiredArgsConstructor
@Getter
public class Artist implements UIInteractionProvider{
	private final CooperationContext context;
	private final GeneratorTriple[] generatorTriples;
	// Alpha value is 0 everywhere when no generator used the pixel
	private PGraphics canvas;
	private PGraphics prevCanvas;
	private List<ImageObserver> imageObservers = new LinkedList<>();
	private boolean parallel;
	private int offsetX;
	private int offsetY;

	public Artist(CooperationContext defaultContext, List<GeneratorTriple> generatorTriples) {
		this(defaultContext, generatorTriples.toArray(new GeneratorTriple[0]));
//		Opengl is not threadsafe
		Optional<Boolean> optional3dIsUsed = generatorTriples.stream().map(gT -> gT.getGenerator().use3D())
				.reduce((a, b) -> a || b);
		if (optional3dIsUsed.isPresent()) {
			parallel = !optional3dIsUsed.get();
		}
	}

	public void showArt(Exhibit exhibit) {
		calculateCoopContextCoordinates();
		canvas = exhibit.getCanvas();
		Stream<GeneratorTriple> stream = Arrays.stream(generatorTriples);
		// Asynchron calculation of images only possible if jogl not needed otherwise there are problems
		if (parallel) {
			stream = stream.parallel();
		}
		stream.forEach(g -> {
			Generator generator = g.getGenerator();
			GeneratorConfiguration config = g.getConfig();

			CooperationContext localContext = g.getContext();
			localContext.setImageOfLastIteration(prevCanvas);
			localContext.setPApplet(exhibit);

			PGraphics pg = g.getPg();
			if (pg == null || config.isClearScreen() || generator.forceClearScreen()) {
				if (generator.use3D()) {
//					OpenGL is not threadsafe
					pg = exhibit.createGraphics(config.getWidth(), config.getHeight(), PConstants.P3D);
				} else {
					pg = exhibit.createGraphics(config.getWidth(), config.getHeight());
				}
				g.setPg(pg);
			}
			pg.beginDraw();
			generator.nextStep(localContext, pg);
			pg.endDraw();

		});
		canvas.beginDraw();
		// Synchron merge
		Arrays.stream(generatorTriples).forEach(g -> {
			if(g.isHidden()) {
				return;
			}
			GeneratorConfiguration config = g.getConfig();
			PGraphics pg = g.getPg();
			//3d pgraphics have to be converted into in image otherwise the graphics will not be displayed
			canvas.image((pg instanceof PGraphics3D ? pg.get() : pg), config.getXOffset(), config.getYOffset());
		});
		exhibit.clearBackground();
		canvas.endDraw();
		if(offsetX+offsetY!=0) {
			exhibit.stroke(0xFF000000);
			exhibit.strokeWeight(4);
			exhibit.noFill();
			exhibit.rect(offsetX-4, offsetY-4, canvas.width+8, canvas.height+8);
		}
		exhibit.image(canvas, offsetX, offsetY);

		if (imageObservers != null) {
			exhibit.g.loadPixels();
			imageObservers.forEach(o -> o.onImageChange(exhibit.frameCount, exhibit.g));
		}
		prevCanvas = canvas;
	}

	/**
	 * Calculate cooperation coordinate
	 */
	private void calculateCoopContextCoordinates() {
		for (int i = 0; i < generatorTriples.length; i++) {
			int nI = (i + 1) % generatorTriples.length;
			GeneratorTriple g = generatorTriples[i];
			Generator generator = g.getGenerator();
			if (generator instanceof PixelCoordinateCalculationStrategy) {
				CooperationContext context = g.getContext();
				PixelCoordinate coordinate = ((PixelCoordinateCalculationStrategy) generator).calculate(context);
				GeneratorConfiguration config = g.getConfig();
				GeneratorTriple generatorTriple = generatorTriples[nI];
				GeneratorConfiguration nextConfiguration = generatorTriple.getConfig();
				CooperationContext nextContext = generatorTriple.getContext();
				coordinate.x += config.getXOffset()-nextConfiguration.getXOffset();
				coordinate.y += config.getYOffset()-nextConfiguration.getYOffset();
				
				nextContext.setCoordinate(coordinate);
			}
		}
	}

	public void exhibitClosed() {
		this.imageObservers.forEach(o -> o.exhibitClosed());
	}

	public void register(ImageObserver imageObserver) {
		this.imageObservers.add(imageObserver);
	}

	public void unregister(ImageObserver imageObserver) {
		this.imageObservers.remove(imageObserver);
	}

	@Override
	public void keyReleased(KeyEvent ev) {
		int keyCode = ev.getKeyCode();
		int index = keyCode-48;
		if(keyCode>=48 && index < generatorTriples.length) {
			GeneratorTriple g = generatorTriples[index];
			g.setHidden(!g.isHidden());
		}
	}
	
	public static class DelayedArtist extends Artist {

		private long delay;

		public DelayedArtist(long delay, CooperationContext defaultContext, List<GeneratorTriple> generatorTriples) {
			super(defaultContext, generatorTriples);
			this.delay = delay;
		}

		public void showArt(Exhibit exhipit) {
			super.showArt(exhipit);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public static class DebugArtist extends Artist {
		enum State {
			RUN, STOP, ONE_STEP
		}

		private volatile State state = State.RUN;

		public DebugArtist(CooperationContext defaultContext, List<GeneratorTriple> generatorTriples) {
			super(defaultContext, generatorTriples);
			Thread thread = new Thread(this::listenToSystemIn);
			thread.setName("Debug Artist");
			thread.start();
		}

		public void showArt(Exhibit exhipit) {
			switch (state) {
			case ONE_STEP:
				state = State.STOP;
			case RUN:
				super.showArt(exhipit);
				break;
			case STOP:
			default:
				break;
			}
			return;
		}

		private void listenToSystemIn() {
			try (Scanner inScanner = new Scanner(System.in)) {
				while (true) {
					byte nextByte = inScanner.nextByte();
					parseByteToState(nextByte);
				}
			}
		}

		private void parseByteToState(byte nextByte) {
			switch (nextByte) {

			}
		}
	}

	public void setOffsets(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
}
