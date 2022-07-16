package de.fernunihagen.mci.group2.coopalgoart.impl.recorder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;
import org.jcodec.scale.AWTUtil;

import de.fernunihagen.mci.group2.coopalgoart.impl.ImageObserver;
import processing.core.PGraphics;

/**
 * @author bwinzen
 *
 */
public abstract class Recorder implements ImageObserver {
	private boolean record;
	private boolean finished;
	private final List<Runnable> doAfterFinish = new LinkedList<>();

	public void startRecording() {
		record = true;
	}

	public void pauseRecording() {
		record = false;
	}

	public void stopRecording() {
		record = false;
		finished = true;
		finished();
		doAfterFinish.forEach(Runnable::run);
	}

	public void addOnFinishTask(Runnable run) {
		if (run != null) {
			doAfterFinish.add(run);
		}
	}

	protected abstract void finished();

	@Override
	public void onImageChange(int frame, PGraphics pg) {
		if (record && !finished) {
			addImage(frame, pg);
		}
	}

	@Override
	public void exhibitClosed() {
		stopRecording();
	}

	protected abstract void addImage(int frame, PGraphics pg);

	/**
	 * @author bwinzen
	 *
	 */
	public static class SaveLastRecorder extends Recorder {
		private Path targetPath;
		private PGraphics pg;

		public SaveLastRecorder(Path targetPath) {
			this.targetPath = targetPath;
		}

		@Override
		protected void finished() {
			if (pg != null) {
				pg.save(targetPath.toString());
			}
		}

		@Override
		protected void addImage(int frame, PGraphics pg) {
			this.pg = pg;
		}
	}

	/**
	 * @author bwinzen
	 *
	 */
	public static class ImageDirectory extends Recorder {
		private Path targetPath;
		private String type;

		public ImageDirectory(Path targetPath, String type) {
			this.targetPath = targetPath;
			this.type = type;
		}

		@Override
		protected void finished() {
		}

		@Override
		protected void addImage(int frame, PGraphics pg) {
			String name = frame +"."+ type;
			String target = targetPath.resolve(name).toAbsolutePath().toString();
			pg.save(target);
		}

	}

	/**
	 * @author bwinzen
	 *
	 */
	public static class GIFRecorder extends Recorder {

		private GifEncoder encoder;

		/**
		 * @param targetPath
		 * @param delay
		 * @param repeat
		 * @param quality 10 default 20 bad 1 is good
		 * @throws IOException
		 */
		public GIFRecorder(Path targetPath, int delay, boolean repeat, int quality) throws IOException {
			encoder = new GifEncoder();
			encoder.start(Files.newOutputStream(targetPath));
			encoder.setDelay(delay);
			if(repeat) {
				encoder.setRepeat( 0);
			}
			encoder.setQuality(quality);
		}

		@Override
		protected void addImage(int frame, PGraphics pg) {
			addFrame(pg.pixels, pg.width, pg.height);
		}

		public void addFrame(int[] pixels, int width, int height) {
			BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			frame.setRGB(0, 0, width, height, pixels, 0, width);
			encoder.addFrame(frame);
		}

		@Override
		protected void finished() {
			encoder.finish();

		}

	}

	/**
	 * @author bwinzen
	 *
	 */
	public static class VideoRecorder extends Recorder {

		private SequenceEncoder encoder;

		public VideoRecorder(Path targetPath, int fps) throws IOException {
			encoder = SequenceEncoder.createWithFps(NIOUtils.writableChannel(targetPath.toFile()),
					new Rational(fps, 1));
		}

		@Override
		protected void addImage(int frame, PGraphics pg) {
			addFrame(pg.pixels, pg.width, pg.height);
		}

		public void addFrame(int[] pixels, int width, int height) {
			BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			frame.setRGB(0, 0, width, height, pixels, 0, width);
			try {
				encoder.encodeNativeFrame(AWTUtil.fromBufferedImageRGB(frame));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void finished() {
			try {
				encoder.finish();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
