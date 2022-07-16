package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigurationIO {
	void save(String filePath, ArtConfiguration artConfiguration) throws IOException;
	ArtConfiguration load(String filePath) throws IOException;
	ArtConfiguration load(InputStream in) throws IOException;
}
