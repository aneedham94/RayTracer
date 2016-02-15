
public class Ray {
	private Vector origin;
	private Vector ray;
	private double length;
	
	public Ray(Vector origin, Vector ray, double length){
		this.origin = origin;
		this.ray = ray;
		this.length = length;
	}
	
	public Vector getOrigin(){
		return origin;
	}
	
	public Vector getVector(){
		return ray;
	}
	
	public double getLength(){
		return length;
	}
}
