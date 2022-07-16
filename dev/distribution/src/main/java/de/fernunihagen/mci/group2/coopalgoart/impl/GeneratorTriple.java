package de.fernunihagen.mci.group2.coopalgoart.impl;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.impl.config.GeneratorConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import processing.core.PGraphics;

@RequiredArgsConstructor
@Getter
@Setter
public class GeneratorTriple {
	private final GeneratorConfiguration config;
	private final Generator generator;
	private final CooperationContext context;
	private PGraphics pg;
	private boolean hidden;
}
