package de.fernunihagen.mci.group2.coopalgoart.api.services.desc.ranges;

import java.util.Map;

import lombok.Value;

@Value
public class FileRange {
	Map<String, String[]> filter;
	Type type;
	
	public enum Type{
		OPEN, SAVE
	}
}
