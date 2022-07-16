package de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges;

import lombok.Value;

@Value
public class FloatRange {
	private double defaultValue;
	private double min;
	private double max;
	private Double increment;
}
