package de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree;

import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("rawtypes")
public class DistanceContainer<T extends QuadTreeElement> implements Comparable<DistanceContainer> {
	private double distance;
	private T element;
	@Override
	public int compareTo(DistanceContainer o) {
		return Double.compare(this.distance, o.distance);
	}
	
	public static Comparator<DistanceContainer> comparator(){
		return new DistanceContainerComparator();
	}
	
	private static class DistanceContainerComparator implements Comparator<DistanceContainer>{

		@Override
		public int compare(DistanceContainer o1, DistanceContainer o2) {
			return o1.compareTo(o2);
		}
		
	}
}
