/**
* @author bwinzen
**/
package de.fernunihagen.mci.group2.coopalgoart.impl.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author bwinzen
 *
 */
public class JsonConfigurationIOTest {

	@Test
	public void test() throws IOException {
		ArtConfiguration artConfiguration = ArtConfiguration.builder()
				.name("Simple Test") //
				.width(1000) //
				.height(400) //
				.renderer(RendererType.FX2D) //
				.delayBetweenIterations(1000)
				.generatorConfigs(Arrays.asList(createSwarmGenerator())) //
				.build();
		
		JsonConfigurationIO io = new JsonConfigurationIO();
//		io.save(new File("art.json").getAbsolutePath(), artConfiguration);
	}

	private GeneratorConfiguration createSwarmGenerator() {
		return GeneratorConfiguration.builder()
				.generatorId("bwinzen_swarm") //
				.xOffset(0) //
				.yOffset(0) //
				.width(1000) //
				.height(400) //
				.parameter(null) //
				.build();
	}

}
