package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author bwinzen
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GeneratorOptions extends Options{
	private boolean clearScreen=true;
	public GeneratorOptions(boolean clearScreen, Map<String, Object> parameter) {
		super(parameter);
		this.clearScreen = clearScreen;
	}
}
