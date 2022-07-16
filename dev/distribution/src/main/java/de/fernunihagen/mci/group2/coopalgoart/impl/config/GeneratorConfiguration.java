package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import java.util.Map;

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
public class GeneratorConfiguration {
	private String generatorId;
	private int generatorVersion;
	private int xOffset;
	private int yOffset;
	private int width;
	private int height;
	private boolean clearScreen;
	private Map<String, Object> parameter;
	
	/**
	 * @author grissland
	 *
	 */
	public long parameterToSeed() {
		long resultSeed = 0;
		for(Object parameterValue: parameter.values()) {
			resultSeed = resultSeed + parameterValue.hashCode();
		}
		 return resultSeed;
	}
}
