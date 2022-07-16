package de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges;

import java.util.List;
import java.util.Map;

import de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ParameterDescription;
import lombok.Value;

@Value
public class OptionRange {
	private Map<String, List<ParameterDescription>> options;
	private String defaultValue;
}
