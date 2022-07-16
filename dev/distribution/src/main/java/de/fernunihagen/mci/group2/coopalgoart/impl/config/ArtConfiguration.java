package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bwinzen
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtConfiguration {
	private String name;
	private RendererType renderer;
	private int backgroundColor;
	private int width;
	private int height;
	private float fps;
	private boolean recordMode = false;
	@Builder.Default
	private CoopMode coopMode = CoopMode.OVERLAYER;
//	private long countOfIterations;
	private Object seed;
	private long delayBetweenIterations;
	@Builder.Default
	private int numberOfFrames = -1;
	private List<GeneratorConfiguration> generatorConfigs;

	public long calculateSeed() {
		if (seed instanceof Integer || seed instanceof Long) {
			return ((Number) seed).longValue();
		}
		return seed.hashCode();
	}
}
