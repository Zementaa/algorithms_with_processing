package de.fernunihagen.mci.group2.coopalgoart.bwinzen.imageevolution;

import java.util.LinkedList;

import de.fernunihagen.mci.group2.coopalgoart.bwinzen.imageevolution.PrioQueue.EvolutionContainer;
import processing.core.PVector;

/**
 * @author bwinzen
 *
 */
public class PrioQueue extends LinkedList<EvolutionContainer>{
	private static final long serialVersionUID = 1L;
	private int maxCapacity;

	public PrioQueue(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	public boolean offer(EvolutionContainer e) {
		//if queue is full only add values with really low/good fitness values 
		if(size()>=maxCapacity) {
			if(peek().fitness<e.fitness) {
				return true;
			}
		}
		return super.offer(e);
	};
	
	public static class EvolutionContainer implements Comparable<EvolutionContainer> {
		public PVector target;
		public int color;
		public int fitness;
		public long inputTime;

		public EvolutionContainer(PVector target, int color, int fitness) {
			this.target = target;
			this.fitness = fitness;
			this.color = color;
			this.inputTime = System.currentTimeMillis();
		}

		@Override
		public int compareTo(EvolutionContainer o) {
			return (int) Math.signum(fitness - o.fitness + inputTime - o.inputTime);
		}
	}
}
