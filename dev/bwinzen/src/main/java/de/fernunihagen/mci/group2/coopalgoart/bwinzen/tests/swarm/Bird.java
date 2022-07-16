package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.swarm;

/**
 * @author bwinzen
 *
 */
public class Bird {

	public double xSpeed;
	public double ySpeed;
	
	public double rotation;
	
	public double x;
	public double y;
	
	private NextState nextState;
	
	boolean debug;
	String name="";
	
	public Bird(double x, double y, double xSpeed, double ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.x = x;
		this.y = y;
	}
	
	public double speed() {
		return (double) Math.sqrt(xSpeed*xSpeed+ySpeed*ySpeed);
	}
	
	public double angle() {
		double angle = (double) Math.acos(ySpeed / speed());
		if (xSpeed > 0) {
			angle = (double) (2 * Math.PI - angle);
		}
		return angle;
	}
	
	public void setNext(double xNext, double yNext, double xSpeedNext, double ySpeedNext) {
		nextState = ()->{
			Bird.this.x = xNext;
			Bird.this.y = yNext;
			Bird.this.xSpeed = xSpeedNext;
			Bird.this.ySpeed = ySpeedNext;
			if(debug) {
				System.out.println(Bird.this);
			}
		};
	}
	
	public void nextState() {
		if(nextState != null) {
			nextState.establish();
		}
	}
	
	private interface NextState {
		void establish();
	}
	
	@Override
		public String toString() {
			return name+" "+x+", "+y+" -> "+xSpeed+", "+ySpeed;
		}
}
