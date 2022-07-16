package de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges;

import lombok.Value;

@Value
public class IntRange {
	private long defaultValue;
	private long min;
	private long max;
	private Long increment;
}
