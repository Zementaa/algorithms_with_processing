package de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author bwinzen
 *
 */
public class QuadTree<T extends QuadTreeElement> {
	@Getter(value = AccessLevel.PACKAGE)
	QuadTreeNode<T> root;
	public QuadTree(double x, double y, double width, double height, int maxElements) {
		root = new QuadTreeNode<>(x, y, width, height, maxElements);
	}
	
	public boolean add(T element) {
		return root.add(element);
	}
	
	public boolean addAll(Collection<T> element) {
		if(element == null) {
			return false;
		}
		element.forEach(this::add);
		return true;
	}
	
	public NavigableMap<Double, List<T>> queryDistances(double x, double y, QuadTreeQuery<T> query){
		NavigableMap<Double, List<T>> result = new TreeMap<>();
		root.queryDistances(x, y, query, result);
		return result;
	}
	
	public T findClosest(T element) {
		return root.findClosest(element).getElement();
	}
	
	public DistanceContainer<T> findClosestWithDistance(T element) {
		return root.findClosest(element);
	}
	
	public T findClosest(double x, double y) {
		return root.findClosest(x, y).getElement();
	}
	
	public DistanceContainer<T> findClosestWithDistance(double x, double y) {
		return root.findClosest(x, y);
	}
	
	public List<T> query(QuadTreeQuery<T> query){
		List<T> result = new LinkedList<>();
		root.query(query, result);
		return result;
	}
	
	public List<T> getAll(){
		List<T> result = new LinkedList<>();
		root.query(QuadTreeQuery.queryAll(), result);
		return result;
	}
	
	public void clear() {
		root.clear();
	}
	
	public int size() {
		return root.size();
	}
}
