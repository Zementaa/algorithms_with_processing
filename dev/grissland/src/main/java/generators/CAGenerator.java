package generators;

import java.util.Random;

import cellularAutomate.CellularAutomate;
import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import processing.core.PGraphics;
import lombok.Builder;

@Builder
public class CAGenerator implements Generator {

	private CellularAutomate ca;
	private int dimBlocks;
	private int yPoint;
	@Override
	public void nextStep(CooperationContext context, PGraphics applet) {
		//create CellularAutomate, if not exist already
		if (ca == null) {
			ca = new CellularAutomate(applet.width, dimBlocks, yPoint,context );
			ca.setup();
			ca.showCA(applet);
		}
		//call next generation and shape
		ca.nextgen();
		ca.showCA(applet);
	}
}
