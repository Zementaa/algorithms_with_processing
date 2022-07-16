package de.fernunihagen.mci.group2.coopalgoart.bwinzen.quadtree;

/**
 * @author bwinzen
 *
 */
public interface QuadTreeQuery<T extends QuadTreeElement> {
	
	boolean isValid(QuadTreeNode<T> node);
	
	boolean isValid(QuadTreeElement element);
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	static double dist(double x1, double y1,double x2, double y2) {
		double xDif=x1-x2;
		double yDif=y1-y2;
		return Math.sqrt(xDif*xDif+yDif*yDif);
	}
	static<T extends QuadTreeElement> QuadTreeQuery<T> queryAll(){
		return new QuadTreeQuery<T>() {

			@Override
			public boolean isValid(QuadTreeNode<T> node) {
				return true;
			}

			@Override
			public boolean isValid(QuadTreeElement element) {
				return true;
			}
		};
	}
	static<T extends QuadTreeElement> QuadTreeQuery<T> queryMaxDistance(double x, double y , double maxDistance) {
		return new QuadTreeQuery<T>() {

			@Override
			public boolean isValid(QuadTreeNode<T> node) {
				double nodeX = node.getX();
				double nodeY = node.getY();
				double nodeXMax = node.getX()+node.getWidth();
				double nodeYMax = node.getY()+node.getHeight();
				  double dx = Math.max(Math.max(nodeX - x, 0), x - nodeXMax);
				  double dy = Math.max(Math.max(nodeY - y, 0), y - nodeYMax);
				  return maxDistance >= Math.sqrt(dx*dx + dy*dy);
//				return  maxDistance>= dist(x, y, nodeX, nodeY) ||
//						maxDistance >= dist(x, y, nodeX, nodeYMax) ||
//						maxDistance >= dist(x, y, nodeXMax, nodeY) ||
//						maxDistance >= dist(x, y, nodeXMax, nodeYMax);
						

			}
			@Override
			public boolean isValid(QuadTreeElement element) {
				return maxDistance >= dist(x, y, element.getX(), element.getY());
			}
		};
	}
}
