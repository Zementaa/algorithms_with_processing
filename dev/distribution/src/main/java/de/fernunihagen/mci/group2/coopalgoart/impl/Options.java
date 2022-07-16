package de.fernunihagen.mci.group2.coopalgoart.impl;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Options {
	private Map<String, Object> parameter = new TreeMap<>();

	public Options(Map<String, Object> parameter) {
		if(parameter!=null) {
			this.parameter.putAll(parameter);
		}
	}
}
