package de.fernunihagen.mci.group2.coopalgoart.bwinzen.lsystem.nonrec.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
@AllArgsConstructor
@Getter
public class CalculationState {
	PVector position;
	float angle;
	boolean wentBack;

	public CalculationState forward(float len) {
		PVector newPos = new PVector(0, len);
		newPos.rotate(angle);
		newPos.add(position);
		return new CalculationState(newPos, angle, false);
	}

	public CalculationState rotate(float angle) {
		return new CalculationState(position, this.angle + angle, false);
	}

	public CalculationState goBack() {
		return new CalculationState(position, this.angle, true);
	}

	@Override
	public String toString() {
		return position.toString() + "  angle=" + angle;
	}
}
