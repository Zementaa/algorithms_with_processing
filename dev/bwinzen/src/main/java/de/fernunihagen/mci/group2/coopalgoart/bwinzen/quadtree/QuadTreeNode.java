package de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * @author bwinzen
 *
 * @param <T>
 */
@Getter
public class QuadTreeNode<T extends QuadTreeElement> {
	public static final int NORTH_WEST = 0;
	public static final int NORTH_EAST = 1;
	public static final int SOUTH_WEST = 2;
	public static final int SOUTH_EAST = 3;

	private double x;
	private double y;
	private double width;
	private double height;
	private int maxElements;

	private QuadTreeNode<T>[] children;

	private List<T> elements = new LinkedList<>();

	public QuadTreeNode(double x, double y, double width, double height, int maxElements) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxElements = maxElements;
	}

	public boolean isValid(T element) {
		double x = element.getX();
		double y = element.getY();
		return x < this.x + this.width && x >= this.x && y < this.y + this.height && y >= this.y;
	}

	public boolean add(T element) {
		if (!isValid(element)) {
			return false;
		}
		if (elements != null) {
			if (maxElements > elements.size()) {
				elements.add(element);
				return true;
			} else if (maxElements == elements.size()) {
				children = new QuadTreeNode[4];
				children[NORTH_WEST] = new QuadTreeNode<>(x, y, width / 2, height / 2, maxElements);
				children[NORTH_EAST] = new QuadTreeNode<>(x + width / 2, y, width / 2, height / 2, maxElements);
				children[SOUTH_WEST] = new QuadTreeNode<>(x, y + width / 2, width / 2, height / 2, maxElements);
				children[SOUTH_EAST] = new QuadTreeNode<>(x + width / 2, y + width / 2, width / 2, height / 2,
						maxElements);
				elements.forEach(this::addToChild);
				elements = null;
			}
		}
		return addToChild(element);
	}

	private boolean addToChild(T element) {
		int x = (int) ((element.getX() - this.x) / (width / 2));
		int y = (int) ((element.getY() - this.y) / (height / 2));
		return children[x + y * 2].add(element);
	}

	public void query(QuadTreeQuery<T> query, List<T> resultContainer) {
		if (elements != null) {
			elements.stream().filter(query::isValid).forEach(resultContainer::add);
		} else {
			for (int i = 0; i < children.length; i++) {
				if (query.isValid(children[i])) {
					children[i].query(query, resultContainer);
				}
			}
		}
	}

	public void queryDistances(double x, double y, QuadTreeQuery<T> query, NavigableMap<Double, List<T>> result) {
		if (elements != null) {
			elements.stream().filter(query::isValid).forEach(t -> {
				result.computeIfAbsent(QuadTreeQuery.dist(x, y, t.getX(), t.getY()), d -> new LinkedList<>()).add(t);
			});
		} else {
			for (int i = 0; i < children.length; i++) {
				if (query.isValid(children[i])) {
					children[i].queryDistances(x, y, query, result);
				}
			}
		}

	}

	private DistanceContainer<T> createDistanceContainer(T source, T target) {
		return new DistanceContainer<>(QuadTreeQuery.dist(source.getX(), source.getY(), target.getX(), target.getY()),
				target);
	}

	private DistanceContainer<T> createDistanceContainer(double x, double y, T target) {
		return new DistanceContainer<>(QuadTreeQuery.dist(x, y, target.getX(), target.getY()), target);
	}

	public DistanceContainer<T> findClosest(T element) {
		double requestedX = element.getX();
		double requestedY = element.getY();
		return findClosestInternal(requestedX, requestedY, element);
	}
	
	public DistanceContainer<T> findClosest(double requestedX, double requestedY) {
		return findClosestInternal(requestedX, requestedY, null);
	}

	private DistanceContainer<T> findClosestInternal(double requestedX, double requestedY, T element) {
		if (elements != null) {
			Stream<T> stream = elements.stream();
			if (element != null) {
				stream = stream.filter(e -> e != element);
			}
			Optional<DistanceContainer<T>> min = stream.map(e -> createDistanceContainer(requestedX, requestedY, e))
					.min(DistanceContainer.comparator());
			if (min.isPresent()) {
				return min.get();
			}
			return null;
		}
		int x = (int) ((requestedX - this.x) / (width / 2));
		int y = (int) ((requestedY - this.y) / (height / 2));
		if (x < 0) {
			x = 0;
		} else if (x > 1) {
			x = 1;
		}

		if (y < 0) {
			y = 0;
		} else if (y > 1) {
			y = 1;
		}

		int index = x + y * 2;
		DistanceContainer<T> closest = children[index].findClosestInternal(requestedX, requestedY, element);

		if (closest == null || requestedX - this.x > closest.getDistance() || this.x + this.width - requestedX > closest.getDistance()
				|| requestedY - this.y > closest.getDistance() || this.y + this.height - requestedY > closest.getDistance()) {
			for (int i = 0; i < children.length; i++) {
				if (index == i) {
					// skip already done
					continue;
				}
				DistanceContainer<T> maybeCloser = children[i].findClosestInternal(requestedX, requestedY, element);
				if (maybeCloser != null && (closest == null || maybeCloser.getDistance() < closest.getDistance())) {
					closest = maybeCloser;
				}
			}
		}
		return closest;
	}

	public void clear() {
		if (elements != null) {
			elements.clear();
		} else {
			for (int i = 0; i < children.length; i++) {
				children[i].clear();
			}
		}
	}

	public int size() {
		if (elements != null) {
			return elements.size();
		} else {
			int sum = 0;
			for (int i = 0; i < children.length; i++) {
				sum += children[i].size();
			}
			return sum;
		}
	}

}
