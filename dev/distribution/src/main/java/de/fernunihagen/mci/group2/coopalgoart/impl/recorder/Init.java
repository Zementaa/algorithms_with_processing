package de.fernunihagen.mci.group2.coopalgoart.impl.recorder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.SimpleServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;

/**
 * @author bwinzen
 *
 */
public class Init {
	private static final String CONFIGURATION_BASEPATH = "de/fernunihagen/mci/group2/coopalgoart/impl/";

	public Map<String, Object> exampleConfigurations() {
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> items = new LinkedHashMap<>();
		Map<String, Object> submenus = new LinkedHashMap<>();
		
		items.put("Alles in einem", path("allinone.json"));
		items.put("Buntes Treiben", path("buntestreiben.json"));
		items.put("Rosa Welt", path("rosawelt.json"));
		map.put("Kooperation", submenus);
		submenus.put("Ausarbeitung", items);
		
		items = new LinkedHashMap<>();
		items.put("Buntes Chaos", path("beispiel.json"));
        items.put("Einfache Koop", path("beispiel2.json"));
        items.put("Farbenexplosion", path("beispiel4.json"));
        items.put("L-System-Spiel", path("beispiel5.json"));
        items.put("Blumen und Punkte", path("beispiel6.json"));
        items.put("Fliehendes Ziel", path("fliehendesZiel.json"));
        items.put("Blinking Etwas", path("blinkingetwas.json"));
        map.put("Kooperation", submenus);
        submenus.put("Andere Beispiele", items);
		
		return map;
	}

	private static String path(String config) {
		return CONFIGURATION_BASEPATH + config;
	}

	public void registerAlgorithms() {
		registerRecorder();
	}

	private void registerRecorder() {
		registerLastImageRecorder();
		registerImageDirectoryRecorder();
		registerMP4Recorder();
		registerGIFRecorder();
	}

	private void registerLastImageRecorder() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createSaveFile("file", "Speicherort", true, "*.jpeg,*.jpg,*.png", "*.png",
				"*.jpeg", "*.jpg", "*.tif"));

		ServiceDescription<Recorder> serviceDescription = new ServiceDescription<>(Recorder.class, "lastImage_recorder",
				"Letztes Bild", "", description);
		SimpleServiceFactory<Recorder> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println("Parameter: " + p);
			String imagePath = (String) p.get("file");
			return new Recorder.SaveLastRecorder(Paths.get(imagePath));
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerImageDirectoryRecorder() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createDirectory("dir", "Speicherort", true));
		description.add(ParameterDescription.createEnumDescription("type", "Bildtyp", "png",
				new String[] { "png", "jpg", "tif" }));

		ServiceDescription<Recorder> serviceDescription = new ServiceDescription<>(Recorder.class, "imageDir_recorder",
				"Bild Ordner", "", description);
		SimpleServiceFactory<Recorder> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println("Parameter: " + p);
			String imagePath = (String) p.get("dir");
			String type = (String) p.get("type");
			return new Recorder.ImageDirectory(Paths.get(imagePath), type);
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerMP4Recorder() {
		List<ParameterDescription> description = new LinkedList<>();
//		description.add(ParameterDescription.createEnumDescription("format", "Format", "H264", new String[] {"mp4", "H264"}));
		description.add(ParameterDescription.createSaveFile("file", "Speicherort", true, "*.mp4", "mp4"));
		description.add(ParameterDescription.createInteger("fps", "FPS", 1, 120, 24));

		ServiceDescription<Recorder> serviceDescription = new ServiceDescription<>(Recorder.class, "mp4_recorder",
				"MP4", "", description);
		SimpleServiceFactory<Recorder> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println("Parameter: " + p);
			String imagePath = (String) p.get("file");
			int fps = ((Number) p.get("fps")).intValue();
			try {
				return new Recorder.VideoRecorder(Paths.get(imagePath), fps);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerGIFRecorder() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createSaveFile("file", "Speicherort", true, "*.gif", "gif"));
		description.add(ParameterDescription.createInteger("delay", "Ms zwischen Frames", 1, 4000, 1000 / 24));
		description.add(ParameterDescription.createCheckBox("replay", "Wiederholen", true));
		description.add(ParameterDescription.createInteger("quality", "Performance über Qualität", 1, 20, 10));

		ServiceDescription<Recorder> serviceDescription = new ServiceDescription<>(Recorder.class, "gif_recorder",
				"GIF", "", description);
		SimpleServiceFactory<Recorder> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println("Parameter: " + p);
			String imagePath = (String) p.get("file");
			int delay = ((Number) p.get("delay")).intValue();
			boolean replay = ((Boolean) p.get("replay"));
			int quality = ((Number) p.get("quality")).intValue();
			try {
				return new Recorder.GIFRecorder(Paths.get(imagePath), delay, replay, quality);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		});
		ServiceRegistry.register(serviceFactory);
	}

}
