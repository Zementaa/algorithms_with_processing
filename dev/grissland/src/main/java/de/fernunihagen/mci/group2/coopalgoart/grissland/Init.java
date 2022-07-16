package de.fernunihagen.mci.group2.coopalgoart.grissland;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.SimpleServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import generators.CAGenerator;
import generators.FlockingGenerator;
import generators.PerlinNoiseGenerator;

public class Init {
	private static final String CONFIGURATION_BASEPATH = "grissland/";

	//hinzufuegen der einzelnen Beispiele mit jeweilligen Untermenue
	public Map<String, Object> exampleConfigurations() {
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> items = new LinkedHashMap<>();
		Map<String, Object> subfolder = new LinkedHashMap<>();
		items.put("CA_Ergebnis 1", path("CA_default.json"));
		items.put("CA_Ergebnis 2", path("CA_size50.json"));
		items.put("CA_Ergebnis 3", path("CA_y-point100.json"));
		subfolder.put("CAutomate", items);
		items = new LinkedHashMap<>();
		items.put("Flocking_Ergebnis 1", path("Flocking_default.json"));
		items.put("Flocking_Ergebnis 2", path("Flocking_anzahl500.json"));
		items.put("Flocking_Ergebnis 3", path("Flocking_entfernung1000000.json"));
		subfolder.put("Flocking", items);
		items = new LinkedHashMap<>();
		items.put("PerlinNoise_Ergebnis 1", path("Perlin_default_iteration10.json"));
		items.put("PerlinNoise_Ergebnis 2", path("Perlin_Skalierung200.json"));
		items.put("PerlinNoise_Ergebnis 3", path("Perlin_Skalierung100_xuy1.json"));
		subfolder.put("PerlinNoise", items);
		items = new LinkedHashMap<>();
		items.put("Koorperation_Ergebnis 1", path("Flocking_cautomate.json"));
		items.put("Koorperation_Ergebnis 2", path("PerlinNoise_Cautomate.json"));
		items.put("Koorperation_Ergebnis 3", path("perlinnoise_Flocking_cautomate.json"));
		subfolder.put("Koorperation", items);
		map.put("GRissland", subfolder);
		return map;
	}

	private static String path(String config) {
		return CONFIGURATION_BASEPATH + config;
	}

	public void registerAlgorithms() {
		System.out.println("Load algorithms from " + Init.class.getPackage().getName());
		registerFlocking();
		registerPerlinNoise();
		registerCA();
	}

	//register the flocking generator with the needed parameter
	private void registerFlocking() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("countFlocks", "Anzahl", 1, 500, 200));
		description.add(ParameterDescription.createInteger("closestDistance", "Entfernung", 5000, 1000000, 100000));
		description.add(ParameterDescription.createClearScreen(false));
		
		
		ServiceDescription<Generator> servicedescription = new ServiceDescription<>(Generator.class, "grissland_Flocking",
				"GRissland::flocking", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(servicedescription,
				p -> {
					int countFlocks = ((Number)p.get("countFlocks")).intValue();
					int closestDistance =((Number)p.get("closestDistance")).intValue();
					return FlockingGenerator.builder() //
						.countFlocks(countFlocks) //
						.closestDistance(closestDistance)//
						.build();
				});
		ServiceRegistry.register(serviceFactory);
	}
	
	//register the perlin noise generator with the needed parameter
	private void registerPerlinNoise() {
		
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("scl", "Skalierung", 1, 200, 20));
		description.add(ParameterDescription.createFloat("fly", "Bewegung", 0, 30, 0));
		description.add(ParameterDescription.createFloat("increaseFly", "Beschleunigung", -1, 0,(-0.05)));
		description.add(ParameterDescription.createFloat("xOff", "x-Wert", 0, 1,(0.15)));		
		description.add(ParameterDescription.createFloat("yOff", "y-Wert", 0, 1,(0.15)));		
		description.add(ParameterDescription.createInteger("strokeValue", "Referenzwert", 1, 255, 255));
		description.add(ParameterDescription.createClearScreen(false));
		ServiceDescription<Generator> servicedescription = new ServiceDescription<>(Generator.class, "grissland_PerlineNoise",
				"GRissland::perlinNoise", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(servicedescription,
				p -> {
					int scl = ((Number)p.get("scl")).intValue();
					float fly =((Number)p.get("fly")).floatValue();
					float increaseFly =((Number)p.get("increaseFly")).floatValue();
					float xOff =((Number)p.get("xOff")).floatValue();
					float yOff =((Number)p.get("yOff")).floatValue();
					int strokeValue =((Number)p.get("strokeValue")).intValue();
					return PerlinNoiseGenerator.builder() //
							.scl(scl) //
							.fly(fly) //
							.increaseFly(increaseFly) //
							.xOff(xOff) //
							.yOff(yOff) //
							.stroke(strokeValue) //
							.build();
				});
		ServiceRegistry.register(serviceFactory);
	}
	//register the Cellular Automate generator with the needed parameter
	private void registerCA() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("dimBlocks", "größe der Blocks", 1, 50, 15));
		description.add(ParameterDescription.createInteger("yStartpoint", "y-Startpunkt", 0, 100, 0));
		description.add(ParameterDescription.createClearScreen(false));
		
		ServiceDescription<Generator> servicedescription = new ServiceDescription<>(Generator.class, "grissland_Cautomate",
				"GRissland::Cautomate", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(servicedescription,
				p -> {
					int dimBlocks = ((Number)p.get("dimBlocks")).intValue();
					int yStartpoint =((Number)p.get("yStartpoint")).intValue();
					return CAGenerator.builder() //
						.dimBlocks(dimBlocks) //
						.yPoint(yStartpoint)//
						.build();
				});
		
		ServiceRegistry.register(serviceFactory);
	}
}
