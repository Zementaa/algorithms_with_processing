package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import lombok.Getter;

/**
 * @author bwinzen
 *
 */
@Getter
public enum CoopMode {
	OVERLAYER("Übereinander"), SPLIT_SCREEN_2X1("Geteilte Bereiche 2x1"), SPLIT_SCREEN_2X2("Geteilte Bereiche 2x2");

	private String label;

	CoopMode(String label) {
		this.label = label;
	}
}
