package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.opengl.GLWindow;

import de.fernunihagen.mci.group2.coopalgoart.api.ui.UIInteractionProvider;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.ArtConfiguration;
import de.fernunihagen.mci.group2.coopalgoart.impl.recorder.DummyFx;
import de.fernunihagen.mci.group2.coopalgoart.impl.recorder.RecorderUIController;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * @author bwinzen
 *
 */
public class Exhibit extends PApplet {
	private int pixelWidth;
	private int pixelHeight;
	private Artist artist;
	private String renderer;
	@Getter
	private int background;
	@Setter
	private float fps;
	private int maxframeCount;
	private boolean showFps = false;
	private boolean run = true;
	private boolean next = false;
	private boolean showFrameCount;
	private boolean interactiveMode;
	
	private static final List<Exhibit> LIST_OF_INSTANCES = new LinkedList<>();

	private List<UIInteractionProvider> interactions = new LinkedList<>();
	@Getter
	private ArtConfiguration artConfiguration;

	public Exhibit(Artist artist, ArtConfiguration artConfiguration) {
		this.artConfiguration = artConfiguration;
		pixelWidth = artConfiguration.getWidth();
		pixelHeight = artConfiguration.getHeight();
		this.artist = artist;
		this.renderer = artConfiguration.getRenderer().getProcessingConstant();
		this.background = artConfiguration.getBackgroundColor();
		this.maxframeCount = artConfiguration.getNumberOfFrames();
		this.run = !artConfiguration.isRecordMode();
		if(!this.run) {
			noLoop();
		}
		LIST_OF_INSTANCES.add(this);
		registerUIInteraction(artist);
		Arrays.stream(artist.getGeneratorTriples()) //
		.filter(g->g.getGenerator() instanceof UIInteractionProvider) //
		.map(g->(UIInteractionProvider)g.getGenerator()) //
		.forEach(this::registerUIInteraction);
	}
	
	public String getTitle() {
		return artConfiguration.getName();
	}

	public void settings() {
//		fullScreen(renderer);
		size(pixelWidth, pixelHeight, renderer);
	}

	@Override
	public void setup() {
		if(artConfiguration.isRecordMode()) {
			try {
				RecorderUIController.createUI(this, artist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!Boolean.getBoolean("close_progressing")) {
			// HACK to not close the whole application on exit.
			Object native1 = getSurface().getNative();
			if (native1 instanceof GLWindow) {
				((GLWindow) native1).setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE);
			}
		}else {
			Object native1 = getSurface().getNative();
			if (native1 instanceof  Canvas) {
				((Canvas) native1).getScene().getWindow().setOnCloseRequest(e -> {
					this.exit();
					super.exitActual();
				});
			}
		}
		if (fps != 0) {
			this.frameRate(fps);
		}
	}

	public void setInvisible() {
		getSurface().setVisible(false);
	}
	
	
	public void stopAnimation() {
		run = false;
	}
	
	public void runAnimation() {
		run = true;
		loop();
	}
	

	public void draw() {
		if (!run && !next) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			frameCount--;
			return;
		}
//		if(width!=pixelWidth|| height!=pixelHeight) {
			artist.setOffsets((width-pixelWidth)/2, (height-pixelHeight)/2);
//		}
		artist.showArt(this);
		if (maxframeCount > 0 && frameCount >= maxframeCount) {
			noLoop();
			artist.exhibitClosed();
		}
		if (showFps) {
			textSize(20);
			fill(0);
			text(String.format("%.2ffps", frameRate), width - 100, height - 12);
		}
		if (showFrameCount) {
			textSize(20);
			fill(0);
			text(String.format("%d.f", frameCount), 2, height - 12);
		}
		next = false;
//		System.out.println(frameRate);
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKey() == 'f'||event.getKey() == 'F') {
			showFps = !showFps;
		} else if (event.getKey() == 's'||event.getKey() == 'S') {
			run = !run;
		} else if (event.getKey() == 'i'||event.getKey() == 'I') {
			interactiveMode = !interactiveMode;
			System.out.println((interactiveMode? "Activate Interactivmode":"Deactivate Interactivmode"));
		}else if (event.getKey() == 'n'||event.getKey() == 'N') {
			run = false;
			next = true;
		} else if (event.getKey() == 'c'||event.getKey() == 'C') {
			showFrameCount = !showFrameCount;
		} else if (event.getKey() == 'p'||event.getKey() == 'P') {
			AtomicBoolean bool = new AtomicBoolean(false);
			try {
			saveSnapshot(bool);
			}catch(IllegalStateException e) {
				//It might be that the javafx framework has to be initialized
				Thread thread = new Thread(() -> new DummyFx().startFX());
				thread.setDaemon(true);
				thread.start();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
				}
				saveSnapshot(bool);
			}
			while (!bool.get()) { // blocks until file chosen
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if(!interactiveMode) {
			return;
		}
		try {
			interactions.forEach(inter -> inter.keyReleased(event));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveSnapshot(AtomicBoolean bool) {
		Platform.runLater(() -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Snapshot Speichern");
			File file = fileChooser.showSaveDialog(null);
			fileChooser.getExtensionFilters().add(new ExtensionFilter("*.jpeg", "*.jpeg", "*.jpg"));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("*.png", "*.png"));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("*.tif", "*.tif"));
			if (file != null) {
				g.save(file.toString());
			}
			bool.set(true);
		});
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(!interactiveMode) {
			return;
		}
		try {
			interactions.forEach(inter -> inter.keyPressed(event));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent event) {
		if(!interactiveMode) {
			return;
		}
		try {
			interactions.forEach(inter -> inter.mouseMoved(event));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void mouseClicked(MouseEvent event) {
		if(!interactiveMode) {
			return;
		}
		try {
			interactions.forEach(inter -> inter.mouseClicked(event));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if(!interactiveMode) {
			return;
		}
		try {
			interactions.forEach(inter -> inter.mouseWheel(event));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void registerUIInteraction(UIInteractionProvider listener) {
		interactions.add(listener);
	}

	public void unregisterKeyListener(UIInteractionProvider listener) {
		interactions.remove(listener);
	}

	@Override
	public void exitActual() {
		artist.exhibitClosed();
		LIST_OF_INSTANCES.remove(this);
	}

	public PGraphics getCanvas() {
		return createGraphics(pixelWidth, pixelHeight);
	}

	public void clearBackground() {
		background(background);
	}

	public static void closeAllInstances() {
		LIST_OF_INSTANCES.forEach(ex -> ex.exit());
		try { // Give the gl thread time to shutdown without exception
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}
