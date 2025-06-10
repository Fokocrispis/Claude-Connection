package vacuumCleanerRobot;

public class Vector2D {
	private double x;
	private double y;
	
	public Vector2D (double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public void setVector2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public void setVectorX(double x) {
		this.x=x;
	}
	
	public void setVectorY(double y) {
		this.y=y;
	}
	
	public double VectorX() {
		return x;
	}
	
	public double VectorY() {
		return y;
	}
}
