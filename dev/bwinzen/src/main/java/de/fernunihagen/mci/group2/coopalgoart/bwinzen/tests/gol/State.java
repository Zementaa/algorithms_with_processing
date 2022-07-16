package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.gol;

public enum State {
	FOX, FOX_GREEN, GREEN, RABBIT, RABBIT_GREEN, NOTHING;

	State merge(State newState) {
		if(newState == NOTHING) {
			return this;
		}
		switch(newState) {
		case FOX:
			if(this == GREEN)
				return FOX_GREEN;
			if(this == RABBIT) {
				return FOX;
			}
			if(this == RABBIT_GREEN) {
				return FOX_GREEN;
			}
			break;
		case GREEN:
			if(this == FOX)
				return FOX_GREEN;
			break;
		case NOTHING:
			break;
		case RABBIT:
			if(this == FOX)
				return FOX;
			break;
		case RABBIT_GREEN:
			if(this == FOX || this == FOX_GREEN)
				return this;
			break;
		default:
			break;
		
		}
		return newState;
	}
}
