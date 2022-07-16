package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * @author bwinzen
 *
 */
public class JsonConfigurationIO implements ConfigurationIO {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private ObjectWriter objectWriter = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

	@Override
	public void save(String filePath, ArtConfiguration artConfiguration) throws IOException {
		if (artConfiguration == null) {
			throw new IllegalArgumentException("Could not save a null configuration");
		}
		Path path = Paths.get(filePath);
		objectWriter.writeValue(Files.newOutputStream(path), artConfiguration);
	}

	@Override
	public ArtConfiguration load(String filePath) throws IOException {
		InputStream inputStream = Files.newInputStream(Paths.get(filePath));
		return load(inputStream);
	}

	@Override
	public ArtConfiguration load(InputStream in) throws IOException {
		return OBJECT_MAPPER.readValue(in, ArtConfiguration.class);
	}

}
