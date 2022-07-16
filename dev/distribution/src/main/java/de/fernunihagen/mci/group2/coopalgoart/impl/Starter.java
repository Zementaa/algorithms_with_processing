package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author bwinzen
 *
 */
public class Starter {

	public static Map<String, Object> examples;

	public static void main(String[] args) {
		initFramework();
		if (args == null || args.length == 0 || args[0].equals("configurator")) {
			Configurator.main(args);
		} else if (args[0].equals("help")) {
			// TODO print help
		} else if (args[0].equals("latest")) {
			System.setProperty("close_progressing", "true"); //This is needed becuase in  case of multiwindows the progressing window should not close the whole application 
			Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("mci_fachpraktikum_gruppe2");
			if (Files.exists(tempDir)) {
				try {
					Optional<Path> max = Files.list(tempDir).max((p1, p2) -> {
						try {
							FileTime p1LM = Files.getLastModifiedTime(p1);
							FileTime p2LM = Files.getLastModifiedTime(p2);
							return p1LM.compareTo(p2LM);
						} catch (IOException e) {
							e.printStackTrace();
							return -1;
						}
					});

					String path = max.get().toAbsolutePath().toString();
					System.out.println(path);
					args[0] = path;
					ArtGallery.main(args);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				Configurator.main(args);
			}
		} else {
			System.setProperty("close_progressing", "true"); 
			ArtGallery.main(args);
		}
	}

	public static void initFramework() {
		loadExamples();
		new de.fernunihagen.mci.group2.coopalgoart.bwinzen.Init().registerAlgorithms();
		new de.fernunihagen.mci.group2.coopalgoart.ccamier.Init().registerAlgorithms();
		new de.fernunihagen.mci.group2.coopalgoart.grissland.Init().registerAlgorithms();
		new de.fernunihagen.mci.group2.coopalgoart.shopf.Init().registerAlgorithms();
		new de.fernunihagen.mci.group2.coopalgoart.impl.recorder.Init().registerAlgorithms();
	}
	
	public static void loadExamples() {
		examples = new LinkedHashMap<>();
		examples.putAll(new de.fernunihagen.mci.group2.coopalgoart.impl.recorder.Init().exampleConfigurations());
		examples.putAll(new de.fernunihagen.mci.group2.coopalgoart.bwinzen.Init().exampleConfigurations());
		examples.putAll(new de.fernunihagen.mci.group2.coopalgoart.ccamier.Init().exampleConfigurations());
		examples.putAll(new de.fernunihagen.mci.group2.coopalgoart.grissland.Init().exampleConfigurations());
		examples.putAll(new de.fernunihagen.mci.group2.coopalgoart.shopf.Init().exampleConfigurations());
	}

	public static void startProcessingWindow(String... args) {
		ArtGallery.main(args);
	}
	
	public static void startSecondProcess(String... args) {
		try {
			String jarPath = Configurator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("windows") && jarPath.startsWith("/")) {
				jarPath = jarPath.substring(1);
			}
			Path path = Paths.get(jarPath);
			String javaHome = System.getProperty("java.home");
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			String classpath = System.getProperty("java.class.path");

			List<String> command = new ArrayList<>();
			command.add(javaBin);
//			    command.addAll(jvmArgs);

			if (!Files.exists(path)) {
				System.err.println("Could not start Test application " + path);
				return;
			}

			command.add("-Xmx1500m");
			
			//Startet by Java class files
			if (Files.isDirectory(path)) { 
				command.add("-cp");
				command.add(classpath);
				String className = Starter.class.getName();
				command.add(className);
			} else {
			//Startet by jar class files
				command.add("-cp");
				command.add(classpath);
				System.out.println(classpath);
				command.add("-jar");
				System.out.println(jarPath);
				command.add(jarPath);
			}
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					command.add(args[i]);
				}
			}
			System.out.println(jarPath);
			ProcessBuilder builder = new ProcessBuilder(command);
			Process process = builder.inheritIO().start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println();
	}

}