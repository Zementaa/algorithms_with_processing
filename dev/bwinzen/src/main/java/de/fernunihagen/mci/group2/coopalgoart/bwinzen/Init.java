package de.fernunihagen.mci.group2.coopalgoart.bwinzen;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.api.ServiceRegistry;
import de.fernunihagen.mci.group2.coopalgoart.api.services.ServiceDescription;
import de.fernunihagen.mci.group2.coopalgoart.api.services.SimpleServiceFactory;
import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.ColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopColorMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.configuration.CoopMode;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.ImageEvolution3dGenerator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.LSystemGenerator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.SwarmGenerator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.noalg.ColorBorderGenerator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.noalg.ColorReservoirsGenerator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.noalg.CooperationTestGenerator;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.generators.noalg.CooperationTestGeneratorMouse;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.LSystemBuilder;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.EdgeBehavior;
import de.fernunihagen.mci.group2.coopalgoart.bwinzen.objects.Form;
import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class Init {

	private static final String[][] COLOR_COOP_ENUM = new String[][] { { "NO", "Keine Übernahme" }, //
			{ "STRONG", "Hohe Farbkanal Werte (> 0xF8)" }, //
			{ "MIDDLE", "Mittel Starke Farbkanal Werte (> 0xC4)" }, //
			{ "WEAK", "Schwache Farbkanal Werte (> 0x80)" }, //
			{ "HIGH_RED", "Starker Rot Wert (> 0xF8)" }, //
			{ "HIGH_GREEN", "Starker Grün Wert (> 0xF8)" }, //
			{ "HIGH_BLUE", "Starker Blau Wert (> 0xF8)" }, //
			{ "BRIGHT", "Helle (Brightness > 0.7)" }, //
			{ "DARK", "Dunkle (Brightness < 0.3)" }, //
			{ "HIGH_SATURATION", "Hohe Sättigung (Saturation > 0.7)" } };
	private static final String[][] COLOR_MODE_ENUM = new String[][] { { "RANDOM", "Zufällige Farben" },
			{ "RANDOM_GRAY", "Zufälliges Grau" }, { "STATIC", "Einfarbig" },
			{ "GAUSS", "Gaußverteilte Zufallsfarben um Auswahl" } };
	private static final String[][] COLOR_MODE_WIDTH_STEAL_ENUM = new String[][] { { "RANDOM", "Zufällige Farben" },
			{ "RANDOM_GRAY", "Zufälliges Grau" }, { "STATIC", "Einfarbig" },
			{ "GAUSS", "Gaußverteilte Zufallsfarben um Auswahl" },
			{ "STEAL_COLOR", "Aktuelle Pixelfarbe übernehmen" } };
	private static final String RULE_REGEX_PATTERN = "[A-Z]=[A-Z\\\\+\\\\-\\\\[\\\\]].*";

	private static final String[][] COLOR_FITNESS_ENUM = new String[][] { { "grayscale", "Grauwerte" },
			{ "rgb", "RGB-Werte" }, { "r", "Rotwerte" }, { "g", "Grünwerte" }, { "b", "Blauwerte" }, { "h", "Hue" },
			{ "s", "Saturation" }, { "v", "Helligkeit" } };

	private static final String[][] COOPERATION_CONTEXT_USE_ENUM = new String[][] {
			{ "ONLY_BY_SEED", "Kooperation via Seed" }, { "COORDINATE_AVOIDANCE", "Kooperation via Abstoßung" },
			{ "COORDINATE_ATTRACTION", "Kooperation via Anziehung" } };
	private static final String[][] FORMS_ENUM = new String[][] { { "CIRCLE", "Kreise" }, { "LINE", "Linien" },
			{ "POLE", "\"Pole\"" }, { "RECT", "Rechtecke" }, { "QUADRAT", "Quadrate" }, { "TRIANGLE", "Dreiecke" },
			{ "RANDOM", "Zufällige Formen" } };

	/**
	 * The internal basepath for registered configurations menubar>>beispiele>>BWinzen
	 */
	private static final String CONFIGURATION_BASEPATH = "de/fernunihagen/mci/group2/coopalgoart/bwinzen/";

	public Map<String, Object> exampleConfigurations() {
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> subfolder = new LinkedHashMap<>();
		Map<String, Object> items = new LinkedHashMap<>();
		items.put("2 Schwärme", path("swarm_simple.json"));
		items.put("Batcave", path("swarm_batcave.json"));
		items.put("Colorexplosion", path("swarm_colorexplosion.json"));
		subfolder.put("Schwarm", items);

		items = new LinkedHashMap<>();
		items.put("Portrait", path("3dimageevolution_portrait.json"));
		items.put("Eule", path("3dimageevolution_owl.json"));
		items.put("Papagei", path("3dimageevolution_parrot.json"));
		subfolder.put("3DImageEvolution", items);

		items = new LinkedHashMap<>();
		items.put("Einfaches L-System", path("lsystem_simple.json"));
		items.put("Milkyway", path("lsystem_milkyway.json"));
		items.put("AutumnBlues", path("lsystem_autumn.json"));
		items.put("Sierpinski Dreieck", path("lsystem_sierpinski.json"));
		subfolder.put("LSystem", items);

		items = new LinkedHashMap<>();
		items.put("Jäger & Gejagte", path("coop1.json"));
		items.put("ImageEvoColorInduction", path("coop2.json"));
		items.put("Regenwald", path("coop3.json"));
		subfolder.put("Kooperation", items);

		map.put("BWinzen", subfolder);
		return map;
	}

	private static String path(String config) {
		return CONFIGURATION_BASEPATH + config;
	}

	public void registerAlgorithms() {
		System.out.println("Load algorithms from " + Init.class.getPackage().getName());
		registerSwarm();
		registerLSystem();
		registerLSystemBuilder();
//		registerImageEvolution();
//		registerTestParameter();
		registerCooperationContextTest();
		registerImageEvolution3D();
		registerCooperationContextTest2();
		registerColorTanks();
		registerColoredBorder();
	}

	private void registerTestParameter() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("countOfBoids", "Int", 1, 10000, 100));
		description.add(ParameterDescription.createFloat("radiusAlignment", "Float", 1, 400, 50));
		description.add(ParameterDescription.createColor("boidsColor", "Color", 0xFF0000));
		description.add(ParameterDescription.createEnumDescription("boidsForm", "Enum", "ellipses",
				new String[] { "ellipses", "poles", "lines", "randomColors" }));
		description.add(ParameterDescription.createString("string", "String", false, "hello world", "hello World"));
		description.add(ParameterDescription.createFile("file", "file", false, "*.jpeg,*.jpg,*.png", "*.jpeg", "*.jpg",
				"*.png"));
		description.add(ParameterDescription.createDirectory("dir", "directory", false));
		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "testParams",
				"TestParams", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println(p);
			return SwarmGenerator.builder() //
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerSwarm() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("countOfBoids", "Anzahl", 1, 20000, 100));
		description.add(ParameterDescription.createFloat("alignment", "Ausrichtung", 0, 4, 2));
		description.add(ParameterDescription.createFloat("cohesion", "Kohäsion", 0, 4, 0.5));
		description.add(ParameterDescription.createFloat("seperation", "Separation", 0, 4, 1));

		description.add(ParameterDescription.createFloat("radiusAlignment", "Radius Aus.", 0, 400, 50));
		description.add(ParameterDescription.createFloat("radiusSeperation", "Radius Sep.", 0, 400, 100));
		description.add(ParameterDescription.createFloat("radiusCohesion", "Radius Kohäsion", 0, 400, 50));

		description.add(ParameterDescription.createEnumDescription("boidsForm", "Form", "CIRCLE", FORMS_ENUM));
		description.add(ParameterDescription.createFloat("size", "Größe", 1, 100, 4));
		description.add(ParameterDescription.createCheckBox("solidSize", "Feste Größe", true));
		description.add(ParameterDescription.createColor("boidsColor", "Anfangsfarbe", 0xFF0000));
		description.add(ParameterDescription.createEnumDescription("colorMode", "Farbmodus", "STATIC", COLOR_MODE_ENUM));
		description.add(ParameterDescription.createCheckBox("colorTransition", "Farbverläufe", false));
		description.add(ParameterDescription.createEnumDescription("coopColorMode", "KoopModus Farbenübernahme", "NO",
				COLOR_COOP_ENUM));
		description.add(ParameterDescription.createEnumDescription("coopMode", "KoopModus Orientierung", "ONLY_BY_SEED",
				COOPERATION_CONTEXT_USE_ENUM));

		description.add(ParameterDescription.createFloat("maxSpeed", "Max Geschwindigkeit ", 2, 100, 4));
		description.add(ParameterDescription.createFloat("maxForce", "Max Beschleunigung ", 0.01, 10, 1));
		description.add(ParameterDescription.createCheckBox("staticSpeed", "Einheitliche Geschwindigkeit", true));
		description.add(ParameterDescription.createEnumDescription("edgeBehavior", "Kanten Verhalten", "ENDLESS_SCREEN",
				new String[][] { { "ENDLESS_SCREEN", "Kozentrisch" }, { "MIRROR", "Spiegeln" },
						{ "HIDDEN_FORCE", "Rotation" },{ "TELEPORTATION", "Teleportation" } }));
		description
				.add(ParameterDescription.createCheckBox("forceNoColorTanks", "Farbreservoirs nicht zeichnen", false));
		description.add(ParameterDescription.createInteger("fading", "Farben verplassen", 0, 50, 0));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "bwinzen_swarm",
				"BWinzen::Swarm", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println("Parameter: " + p);
			int countOfBoids = ((Number) p.get("countOfBoids")).intValue();
			float alignment = ((Number) p.get("alignment")).floatValue();
			float cohesion = ((Number) p.get("cohesion")).floatValue();
			float seperation = ((Number) p.get("seperation")).floatValue();
			float maxForce = ((Number) p.get("maxForce")).floatValue();
			float maxSpeed = ((Number) p.getOrDefault("maxSpeed", 4)).floatValue();
			double radiusAlignment = ((Number) p.get("radiusAlignment")).doubleValue();
			double radiusSeperation = ((Number) p.get("radiusSeperation")).doubleValue();
			double radiusCohesion = ((Number) p.get("radiusCohesion")).doubleValue();
			int colorOfBoids = 0xFF << 24 | ((Number) p.get("boidsColor")).intValue();
			String form = (String) p.get("boidsForm");
			String colorMode = (String) p.get("colorMode");
			String coopMode = (String) p.get("coopMode");
			float size = ((Number) p.getOrDefault("size", 4)).floatValue();
			Boolean solidSize = (Boolean) p.get("solidSize");
			Boolean colorTransition = (Boolean) p.getOrDefault("colorTransition", false);
			int fading = ((Number) p.getOrDefault("fading", 0)).intValue();
			Object coopColorMode = p.get("coopColorMode");
			if (coopColorMode instanceof Boolean) {
				colorTransition = (Boolean) coopColorMode;
				coopColorMode = (Boolean) coopColorMode ? "STRONG" : "NO";

			}
			Boolean forceNoColorTanks = (Boolean) p.getOrDefault("forceNoColorTanks", false);

			Boolean staticSpeed = (Boolean) p.getOrDefault("staticSpeed", false);

			String edgeBehavior = (String) p.getOrDefault("edgeBehavior", "ENDLESS_SCREEN");
			return SwarmGenerator.builder() //
					.alignment(alignment) //
					.cohesion(cohesion) //
					.seperation(seperation) //
					.countOfBoids(countOfBoids) //
					.radiusAlignment(radiusAlignment) //
					.radiusCohesion(radiusCohesion) //
					.radiusSeperation(radiusSeperation) //
					.colorOfBoids(colorOfBoids) //
					.form(Form.valueOf(form)) //
					.maxForce(maxForce) //
					.maxSpeed(maxSpeed) //
					.coopMode(CoopMode.valueOf(coopMode)) //
					.coopColorMode(CoopColorMode.valueOf((String) coopColorMode)) //
					.colorMode(ColorMode.valueOf(colorMode)) //
					.colorTransition(colorTransition) //
					.solidSize(solidSize) //
					.size(size) //
					.staticSpeed(staticSpeed) //
					.forceNoColorTanks(forceNoColorTanks) //
					.edgeBehavior(EdgeBehavior.valueOf(edgeBehavior)) //
					.fading(fading) //
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerLSystem() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createEnumDescription("lsystem", "LSystem", "Coding Train Beispiel",
				new String[] { "Coding Train Beispiel", "Geordnetes Chaos", "Spiralen", "Kochkurve", "Ein Baum", "Sierpinski Arrow Head", "Hilbert" }));
		description.add(ParameterDescription.createInteger("treeCount", "Anzahl", 1, 15, 1));
		description.add(ParameterDescription.createFloat("size", "Größe", 0.1, 10, 1));
		description.add(ParameterDescription.createCheckBox("solidSize", "gleiche Größe", true));
		description.add(ParameterDescription.createInteger("iteration", "Iterationen", 1, 10, 5));
		description.add(ParameterDescription.createInteger("iterationdelay", "Interationsverzögerung", 0, 200, 10));
		description.add(ParameterDescription.createColor("strokeColor", "Strich Farbe", 0xFF650060));
		description.add(ParameterDescription.createInteger("strokeWeight", "Strich Stärke", 1, 15, 2));
		description.add(
				ParameterDescription.createEnumDescription("colorMode", "Farbmodus", "GAUSS", COLOR_MODE_WIDTH_STEAL_ENUM));
		description.add(ParameterDescription.createInteger("alpha", "Alpha Wert", 0, 255, 255));
		description.add(ParameterDescription.createEnumDescription("coopMode", "KoopModus Orientierung", "ONLY_BY_SEED",
				COOPERATION_CONTEXT_USE_ENUM));
		description.add(ParameterDescription.createCheckBox("reverseMode", "Reihenfolge umkehren", false));
		description.add(ParameterDescription.createCheckBox("rotation", "Rotation", false));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "bwinzen_lystem",
				"BWinzen::LSystem", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			String form = (String) p.get("lsystem");
			float size = ((Number) p.get("size")).floatValue();
			int alpha = ((Number) p.getOrDefault("offsetY", 255)).intValue();
			int strokeColor = alpha<<24 | ((Number) p.getOrDefault("strokeColor", 0)).intValue();
			int strokeWeight = ((Number) p.getOrDefault("strokeWeight", 2)).intValue();
			int iterations = ((Number) p.getOrDefault("iteration", 5)).intValue();
			int iterationdelay = ((Number) p.get("iterationdelay")).intValue();
			String colorMode = (String) p.getOrDefault("colorMode", "STATIC");
			String coopMode = (String) p.getOrDefault("coopMode", "ONLY_BY_SEED");
			Boolean reverseMode = (Boolean) p.getOrDefault("reverseMode", false);
			Boolean solidSize = (Boolean) p.getOrDefault("solidSize", false);
			Boolean rotation = (Boolean) p.getOrDefault("rotation", false);
			int treeCount = ((Number) p.getOrDefault("treeCount", 10)).intValue();
			return LSystemGenerator.builder() //
					.lSystemId(form)//
					.size(size) //
					.iteration(iterations) //
					.strokeColor(strokeColor) //
					.strokeWeight(strokeWeight) //
					.coopMode(CoopMode.valueOf(coopMode)) //
					.colorMode(ColorMode.valueOf(colorMode)) //
					.alpha(alpha) //
					.solidSize(solidSize) //
					.reverseMode(reverseMode) //
					.treeCount(treeCount) //
					.rotation(rotation) //
					.iterationdelay(iterationdelay) //
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerLSystemBuilder() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createString("axiom", "Axiom", true,
				"[X][-X][+X][--X][++X][---X][+++X][++++X]", "[A-Z\\+\\-\\[\\]].*"));
		description.add(
				ParameterDescription.createString("rule0", "Regel0", true, "X=F[-X][+X]FFX", "RULE_REGEX_PATTERN"));
		description.add(ParameterDescription.createString("rule1", "Regel1", true, "F=FF", RULE_REGEX_PATTERN));
		description.add(ParameterDescription.createString("rule2", "Regel2", true, "", "RULE_REGEX_PATTERN"));
		description.add(ParameterDescription.createString("rule3", "Regel3", true, "", "RULE_REGEX_PATTERN"));
		description.add(ParameterDescription.createString("rule4", "Regel4", true, "", "RULE_REGEX_PATTERN"));
		description.add(ParameterDescription.createFloat("angle", "Winkel", 0f, 360f, 45));
		description.add(ParameterDescription.createInteger("treeCount", "Anzahl", 1, 15, 1));
		description.add(ParameterDescription.createFloat("size", "Größe", 0.1, 10, 1));
		description.add(ParameterDescription.createCheckBox("solidSize", "gleiche Größe", true));
		description.add(ParameterDescription.createInteger("iteration", "Iterationen", 1, 10, 5));
		description.add(ParameterDescription.createInteger("iterationdelay", "Interationsverzögerung", 0, 1000, 10));
		description.add(ParameterDescription.createColor("strokeColor", "Strich Farbe", 0xFF000000));
		description.add(ParameterDescription.createInteger("strokeWeight", "Strich Stärke", 1, 15, 2));
		description.add(
				ParameterDescription.createEnumDescription("colorMode", "Farbmodus", "GAUSS", COLOR_MODE_WIDTH_STEAL_ENUM));
		description.add(ParameterDescription.createInteger("alpha", "Alpha Wert", 0, 255, 255));
		description.add(ParameterDescription.createEnumDescription("coopMode", "KoopModus Orientierung", "ONLY_BY_SEED",
				COOPERATION_CONTEXT_USE_ENUM));
		description.add(ParameterDescription.createCheckBox("reverseMode", "Reihenfolge umkehren", false));
		description.add(ParameterDescription.createCheckBox("rotation", "Rotation", false));
		description.add(ParameterDescription.createInteger("offsetX", "Offset X", -500, 500, 0));
		description.add(ParameterDescription.createInteger("offsetY", "Offset Y", -500, 500, 0));

		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class,
				"bwinzen_lystembuilder", "BWinzen::LSystemBuilder", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			int offsetX = ((Number) p.getOrDefault("offsetX", 0)).intValue();
			int offsetY = ((Number) p.getOrDefault("offsetY", 0)).intValue();
			int alpha = ((Number) p.getOrDefault("alpha", 255)).intValue();
			LSystemBuilder builder = new LSystemBuilder().addRule((String) p.get("rule0")) //
					.addRule((String) p.get("rule1")) //
					.addRule((String) p.get("rule2")) //
					.addRule((String) p.get("rule3")) //
					.addRule((String) p.get("rule4")) //
					.addAxiome((String) p.get("axiom")) //
					.angle360(((Number) p.get("angle")).floatValue()).offsetX(offsetX) //
					.offsetY(offsetY); //
			int strokeColor = alpha<<24 | ((Number) p.getOrDefault("strokeColor", 0)).intValue();
			int strokeWeight = ((Number) p.getOrDefault("strokeWeight", 2)).intValue();
			int iterations = ((Number) p.getOrDefault("iteration", 10)).intValue();
			int iterationdelay = ((Number) p.get("iterationdelay")).intValue();
			String colorMode = (String) p.getOrDefault("colorMode", "STATIC");
			String coopMode = (String) p.getOrDefault("coopMode", "ONLY_BY_SEED");
			Boolean reverseMode = (Boolean) p.getOrDefault("reverseMode", false);
			int treeCount = ((Number) p.getOrDefault("treeCount", 10)).intValue();
			float size = ((Number) p.get("size")).floatValue();
			Boolean solidSize = (Boolean) p.getOrDefault("solidSize", false);
			Boolean rotation = (Boolean) p.getOrDefault("rotation", false);
			return LSystemGenerator.builder() //
					.lSystemBuilder(builder) //
					.alpha(alpha) //
					.strokeColor(strokeColor) //
					.strokeWeight(strokeWeight) //
					.coopMode(CoopMode.valueOf(coopMode)) //
					.colorMode(ColorMode.valueOf(colorMode)) //
					.reverseMode(reverseMode) //
					.treeCount(treeCount) //
					.iteration(iterations).iterationdelay(iterationdelay) //
					.size(size)//
					.solidSize(solidSize)//
					.rotation(rotation)//
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}


	private void registerImageEvolution3D() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createFile("imagePath", "Bild", true, "*.jpeg,*.jpg,*.png", "*.jpeg",
				"*.jpg", "*.png"));
		description.add(ParameterDescription.createInteger("count", "Max. Anzahl", 100, 100000, 50000));
		description.add(
				ParameterDescription.createEnumDescription("mode", "Fitness Funktion", "grayscale", COLOR_FITNESS_ENUM));
		description.add(ParameterDescription.createColor("color", "Farbe", 0xFF_FFFFFF));
		description.add(
				ParameterDescription.createEnumDescription("colorMode", "Farbmodus", "STATIC", COLOR_MODE_WIDTH_STEAL_ENUM));
		description.add(ParameterDescription.createEnumDescription("elementForm", "Form", "CIRCLE", FORMS_ENUM));
		description.add(ParameterDescription.createFloat("size", "Element Größe", 0.2, 20, 3));
		description.add(ParameterDescription.createCheckBox("solidSize", "Feste Größe", true));
		description.add(ParameterDescription.createEnumDescription("coopMode", "KoopModus Start", "ONLY_BY_SEED",
				COOPERATION_CONTEXT_USE_ENUM));
		description.add(ParameterDescription.createEnumDescription("coopModeTarget", "KoopModus Ziel", "ONLY_BY_SEED",
				COOPERATION_CONTEXT_USE_ENUM));
		description.add(ParameterDescription.createFloat("elementSpeed", "Elem. Geschwindigkeit", 0.2, 100, 3));
		description
				.add(ParameterDescription.createFloat("perlinoiseItStep", "Pinsel Geschwindigkeit", 0.005, 0.1, 0.01));
		description.add(ParameterDescription.createCheckBox("shoterEvolution", "Shooter Evolution", false));
		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class,
				"bwinzen_evolution3d", "BWinzen::3DImageEvolution", "", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println(p);
			float size = ((Number) p.get("size")).floatValue();
			float elementSpeed = ((Number) p.get("elementSpeed")).floatValue();
			float perlinoiseItStep = ((Number) p.get("perlinoiseItStep")).floatValue();
			int count = ((Number) p.get("count")).intValue();
			String imagePath = (String) p.get("imagePath");
			String mode = (String) p.get("mode");
			String elementForm = (String) p.get("elementForm");
			int color = 0xFF_000000 | ((Number) p.get("color")).intValue();
			String colorMode = (String) p.get("colorMode");
			String coopMode = (String) p.get("coopMode");
			String coopModeTarget = (String) p.getOrDefault("coopModeTarget", "ONLY_BY_SEED");
			Boolean solidSize = (Boolean) p.getOrDefault("solidSize", false);
			Boolean shoterEvolution = (Boolean) p.getOrDefault("shoterEvolution", false);
			Form form = Form.valueOf(elementForm);
			return ImageEvolution3dGenerator.builder() //
					.mode(mode) //
					.size(size) //
					.form(form) //
					.perlinoiseItStep(perlinoiseItStep) //
					.maxShots(count) //
					.imagePath(imagePath) //
					.solidSize(solidSize) //
					.colorMode(ColorMode.valueOf(colorMode)) //
					.coopModeShots(CoopMode.valueOf(coopModeTarget)) //
					.coopMode(CoopMode.valueOf(coopMode)) //
					.color(color) //
					.elementSpeed(elementSpeed) //
					.shooterEvolution(shoterEvolution) //
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerCooperationContextTest() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("contextX", "X", 1, 1000, 500));
		description.add(ParameterDescription.createInteger("contextY", "Y", 1, 1000, 500));
		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "contextTest",
				"Helper::CooperationTest", "Bereitstellung einer coordinate die andere Algorithmen nutzen können", description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			int x = ((Number) p.get("contextX")).intValue();
			int y = ((Number) p.get("contextY")).intValue();
			return CooperationTestGenerator.builder() //
					.x(x) //
					.y(y) //
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}

	private void registerCooperationContextTest2() {
		List<ParameterDescription> description = new LinkedList<>();
		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "contextTest2",
				"Helper::CooperationTest Maus (Press I for activation)", "Bereitstellung einer coordinate die andere Algorithmen nutzen können",
				description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			return new CooperationTestGeneratorMouse();
		});
		ServiceRegistry.register(serviceFactory);
	}
	
	private void registerColorTanks() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("count", "Max. Anzahl", 1, 36, 4));
//		description.add(ParameterDescription.createColor("color", "Farbe", 0xFF_FFFFFF));
//		description.add(
//				ParameterDescription.createEnumDescription("colorMode", "Farbmodus", "STATIC", COLOR_MODE_WIDTH_STEAL));
//		description.add(ParameterDescription.createEnumDescription("elementForm", "Form", "CIRCLE", FORMS));
		description.add(ParameterDescription.createInteger("size", "Größe", 5, 180, 50));
		description.add(ParameterDescription.createInteger("speed", "Geschwindigkeit", 0, 180, 5));
		description.add(ParameterDescription.createEnumDescription("coopColorMode", "KoopModus Farbenübernehmen", "NO",
				COLOR_COOP_ENUM));
		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "colorTanks",
				"Helper::Farbreservoirs", "Bereitstellung von Farbreservoirs",
				description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println(p);
			int size = ((Number) p.get("size")).intValue();
			float degrees = ((Number) p.get("speed")).intValue();
			float speed = degrees * PApplet.PI / 360;
//			String elementForm = (String) p.get("elementForm");
//			int color = 0xFF_000000 | ((Number) p.get("color")).intValue();
			int count = ((Number) p.get("count")).intValue();
			String coopColorMode = (String) p.get("coopColorMode");
			Form form = Form.CIRCLE;
			return ColorReservoirsGenerator.builder() //
					.countOfTanks(count) //
					.form(form)//
					.size(size)//
					.speed(speed)//
					.coopColorMode(CoopColorMode.valueOf(coopColorMode))//
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}
	
	private void registerColoredBorder() {
		List<ParameterDescription> description = new LinkedList<>();
		description.add(ParameterDescription.createInteger("count", "Anzahl Elemente pro Kante", 1, 36, 4));
//		description.add(ParameterDescription.createColor("color", "Farbe", 0xFF_FFFFFF));
//		description.add(
//				ParameterDescription.createEnumDescription("colorMode", "Farbmodus", "STATIC", COLOR_MODE_WIDTH_STEAL));
//		description.add(ParameterDescription.createEnumDescription("elementForm", "Form", "CIRCLE", FORMS));
		description.add(ParameterDescription.createInteger("weight", "Breite in Pixel", 1, 50, 4));
		description.add(ParameterDescription.createEnumDescription("coopColorMode", "KoopModus Farbenübernehmen", "NO",
				COLOR_COOP_ENUM));
		ServiceDescription<Generator> serviceDescription = new ServiceDescription<>(Generator.class, "colorTanks2",
				"Helper::Farbrahmen", "Bereitstellung von Farbreservoirs",
				description);
		SimpleServiceFactory<Generator> serviceFactory = new SimpleServiceFactory<>(serviceDescription, p -> {
			System.out.println(p);
			int count = ((Number) p.get("count")).intValue();
			int weight = ((Number) p.get("weight")).intValue();
			String coopColorMode = (String) p.get("coopColorMode");
			return ColorBorderGenerator.builder() //
//					.countOfTanks(count) //
//					.form(form)//
//					.size(size)//
					.countOfElements(count)
					.weight(weight)//
					.coopColorMode(CoopColorMode.valueOf(coopColorMode))//
					.build();
		});
		ServiceRegistry.register(serviceFactory);
	}
}
