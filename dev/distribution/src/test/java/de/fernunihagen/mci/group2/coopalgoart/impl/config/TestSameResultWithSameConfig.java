package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.BeforeClass;

import de.fernunihagen.mci.group2.coopalgoart.impl.ArtGallery;
import de.fernunihagen.mci.group2.coopalgoart.impl.Exhibit;
import de.fernunihagen.mci.group2.coopalgoart.impl.Starter;
import de.fernunihagen.mci.group2.coopalgoart.impl.recorder.Recorder;
import processing.core.PGraphics;

public class TestSameResultWithSameConfig {

	@BeforeClass
	public static void setup() {
		Starter.initFramework();
	}

	// @Test
	public void testConfiguration1() throws IOException {
		testConfiguration("beispiel.json", 5, -1531340357);
	}

	private void testConfiguration(String pathToArtConfig, int iteration, int hashAfter) throws IOException {
		testConfiguration(pathToArtConfig, new TreeMap<>(Collections.singletonMap(iteration, hashAfter)));
	}

	private void testConfiguration(String pathToArtConfig, NavigableMap<Integer, Integer> expectedHashes)
			throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(pathToArtConfig);
		ArtConfiguration artConfiguration = ConfigurationIOHelper.load(inputStream);
		int iterations = expectedHashes.lastKey();
		artConfiguration.setNumberOfFrames(iterations + 1);
		AtomicBoolean finished = new AtomicBoolean(false);
		HashRecorder hashRecorder = new HashRecorder(expectedHashes) {
			@Override
			protected void finished() {
				finished.set(true);
			}
		};
		Exhibit exExhibit = ArtGallery.showArt(artConfiguration, hashRecorder);
		exExhibit.setInvisible();
		while (!finished.get()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!hashRecorder.fails.isEmpty()) {
			fail("fails: " + hashRecorder.fails + " all the interations have unexpected images");
		}
		if (!hashRecorder.expectedHashes.isEmpty()) {
			fail("iterations not reached: " + hashRecorder.expectedHashes + " last frame " + hashRecorder.lastFrame);
		}
	}

	/**
	 * @author bwinzen
	 *
	 */
	public static abstract class HashRecorder extends Recorder {
		private Map<Integer, Integer> expectedHashes;

		private Map<Integer, Integer> fails = new LinkedHashMap<>();

		private int lastFrame;

		public HashRecorder(NavigableMap<Integer, Integer> expectedHashes) {
			this.expectedHashes = expectedHashes;
			startRecording();
		}

		@Override
		protected void addImage(int frame, PGraphics pg) {
			lastFrame = frame;
			Integer hash = expectedHashes.get(frame);
			if (hash != null) {
				int calculatedHash = calculateHash(pg);
				if (hash.intValue() != calculatedHash) {
					fails.put(frame, calculatedHash);
				} else {
					expectedHashes.remove(frame);
				}
			}
		}

		private int calculateHash(PGraphics pg) {
			int[] pixel = pg.pixels;
			return Arrays.hashCode(pixel);
		}
	}
}
