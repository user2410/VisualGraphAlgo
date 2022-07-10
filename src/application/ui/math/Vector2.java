package application.ui.math;

public class Vector2 {

	public double x, y;

	public Vector2(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 c) {
		this.x = c.x;
		this.y = c.y;
	}
	
	public static Vector2 Zero() {return new Vector2(0.0, 0.0);}
	
	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y); 
	}
	
	public static Vector2 sub(Vector2 a, Vector2 b) {
		return new Vector2(a.x - b.x, a.y - b.y); 
	}
	
	public void scale(double scale) {
		x *= scale;
		y *= scale;
	}
	
	public static Vector2 scalar(Vector2 a, float scale) {
		return new Vector2(a.x * scale, a.y * scale); 
	}
	
	public double sqLength() {
		return x*x + y*y;
	}
	
	public double length() {
		return Math.sqrt(sqLength());
	}
	
	public void normalize() {
		double length = length();
		x /= length;
		y /= length;
	}
	
	public double getAlpha() {
		return 180.0 / Math.PI * Math.atan(y/x);
	}
	
	public Vector2 getPerpendicular() {
		if(x == 0.0) {
			if(y == 0.0) return null;
			return new Vector2(1.0, 0.0);
		}else {
			if(y == 0.0) return new Vector2(0.0, 1.0);
			return new Vector2(1.0, -x/y);			
		}
	}
}
