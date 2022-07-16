package de.fernunihagen.mci.group2.coopalgoart.ccamier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.SimpleServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals.Startpunkt;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.generators.GOLGenerator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.generators.LSystemGenerator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.generators.ParticleSystemGenerator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.generators.RecursionGenerator;

public class Init {
	private static final String CONFIGURATION_BASEPATH = "de/fernunihagen/mci/group2/coopalgoart/ccamier/";

	private static final String[][] RULESETS = new String[][] { { "Veraestelung", "Verästelung" }, { "Koch", "Koch" },
			{ "Fractal", "Fractal" }, { "Sierpinksi", "Sierpinksi Gasket Triangle" }, { "default", "Überraschung" } };

	public void registerAlgorithms() {
		System.out.println("Load algorithms from " + Init.class.getPackage().getName());
		registerLSystem();
		// registerRecursion();
		registerPSystem();
		registerGOL();

	}

	public Map<String, Object> exampleConfigurations() {
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> items = new LinkedHashMap<>();
		Map<String, Object> subfolder = new LinkedHashMap<>();

		items.put("Schwarze Materie", path("psystem.json"));
		items.put("Wasserfarben", path("psystem2.json"));
		items.put("All Black", path("all_black.json"));
		subfolder.put("Partikelsystem", items);

		items = new LinkedHashMap<>();
		items.put("Sudden Death", path("sudden_death.json"));
		items.put("Federball", path("federball.json"));
		items.put("Blinker", path("blinker.json"));
		subfolder.put("Game of Life", items);

		items = new LinkedHashMap<>();
		items.put("Veraestelung", path("veraestelung.json"));
		items.put("Sierpinski", path("sierpinski.json"));
		items.put("Gespiegelter Baum", path("spiegelbaum.json"));
		items.put("Yellow Fractals", path("yellow_fractals.json"));
		items.put("Schneeflocken", path("snowflakes.json"));
		subfolder.put("L-System", items);

		items = new LinkedHashMap<>();
		items.put("Fractal Explosion", path("fractal_explosion.json"));
		items.put("Lsystem Kombi", path("lsystem_kombi.json"));
		subfolder.put("Koop", items);

		map.put("CCamier", subfolder);
		return map;
	}

	private static String path(String config) {
		return CONFIGURATION_BASEPATH + config;
	}

	private void registerLSystem() {
		List<ParameterDescription> description = new LinkedList<>();

		description.add(
				ParameterDescription.createEnumDescription("ruleset", "Auswahl der Regel", "Veraestelung", RULESETS));

		description.add(ParameterDescription.createColor("farbeZweige", "Farbe", 0xFF00FF));
		description.add(ParameterDescription.createInteger("x", "Start X", 200, 1000, 300));
		description.add(ParameterDescription.createInteger("y", "Start Y", 200, 1000, 300));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "ccamier_lsystem",
				"CCamier::LSystem", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			int farbeZweige = 0xFF << 24 | ((Number) p.get("farbeZweige")).intValue();
			Startpunkt startpunkt = new Startpunkt(((Number) p.get("x")).intValue(), ((Number) p.get("y")).intValue());

			String ruleset = (String) p.get("ruleset");

			return LSystemGenerator.builder() //
					.farbeZweige(farbeZweige).startpunkt(startpunkt).ruleset(ruleset).build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerRecursion() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createColor("farbeKreise", "Farbe", 0xFF0000));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class,
				"ccamier_recursion", "CCamier::Recursion", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			int farbeKreise = 0xFF << 24 | ((Number) p.get("farbeKreise")).intValue();

			return RecursionGenerator.builder() //
					.farbeKreise(farbeKreise) // )
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerPSystem() {

		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createFile("filePath1", "Bild 1", false, "*.jpeg,*.jpg,*.png", "*.jpeg",
				"*.jpg", "*.png"));
		description.add(ParameterDescription.createFile("filePath2", "Bild 2", false, "*.jpeg,*.jpg,*.png", "*.jpeg",
				"*.jpg", "*.png"));

		description.add(ParameterDescription.createFile("filePath3", "Bild 3", false, "*.jpeg,*.jpg,*.png", "*.jpeg",
				"*.jpg", "*.png"));
		description.add(ParameterDescription.createFile("filePath4", "Bild 4", false, "*.jpeg,*.jpg,*.png", "*.jpeg",
				"*.jpg", "*.png"));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "ccamier_psystem",
				"CCamier::PSystem", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			String filePath1 = (String) p.get("filePath1");
			String filePath2 = (String) p.get("filePath2");
			String filePath3 = (String) p.get("filePath3");
			String filePath4 = (String) p.get("filePath4");

			return ParticleSystemGenerator.builder() //
					.filePath1(filePath1) //
					.filePath2(filePath2) //
					.filePath3(filePath3) //
					.filePath4(filePath4) //
					.build();
		});
		ServiceRegistry.register(serviceFactory);

	}

	private void registerGOL() {

		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("verhaeltnis", "Spaltenanzahl - Teile Bildschirm durch", 10,
				120, 80));
		description.add(ParameterDescription.createColor("farbe1", "Reproduktion", 0x00FF00));
		description.add(ParameterDescription.createColor("farbe2", "Sterben", 0x0000FF));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "ccamier_gol",
				"CCamier::GOL", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			int verhaeltnis = ((Number) p.get("verhaeltnis")).intValue();
			int farbe1 = 0xFF << 24 | ((Number) p.get("farbe1")).intValue();
			int farbe2 = 0xFF << 24 | ((Number) p.get("farbe2")).intValue();

			return GOLGenerator.builder()//
					.verhaeltnis(verhaeltnis)//
					.farbe1(farbe1)//
					.farbe2(farbe2)//
					.build();
		});
		ServiceRegistry.register(serviceFactory);

	}
}
