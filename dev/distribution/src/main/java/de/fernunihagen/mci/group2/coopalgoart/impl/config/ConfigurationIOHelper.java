package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author bwinzen
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final  class ConfigurationIOHelper {
	private static final ConfigurationIO DEFAULT_IO;
	private static final Map<String, ConfigurationIO> IOMAPPING = new HashMap<>();
	
	static {
		DEFAULT_IO = new JsonConfigurationIO();
		IOMAPPING.put("json", DEFAULT_IO);
	}
	
	public static String createTmpFile(ArtConfiguration artConfiguration) throws IOException {
		Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("mci_fachpraktikum_gruppe2");
		if (!Files.exists(tempDir)) {
			Files.createDirectory(tempDir);
		}
		Path tempFile = Files.createTempFile(tempDir, "configuration", ".json");
		System.out.println("Start with following config " + tempFile);
		String tempFilePath = tempFile.toString();
		DEFAULT_IO.save(tempFilePath, artConfiguration);
		return tempFilePath;
	}
	
	public static void save(String filePath, ArtConfiguration artConfiguration) throws IOException{
		DEFAULT_IO.save(filePath, artConfiguration);
	}
	
	public static ArtConfiguration load(String filePath) throws IOException{
		return DEFAULT_IO.load(filePath);
	}
	
	public static ArtConfiguration load(InputStream in) throws IOException{
		return DEFAULT_IO.load(in);
	}
}
