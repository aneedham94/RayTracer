
public class Sphere extends Shape {
	private Vector center;
	private double radius;

	public Sphere(Vector center, double radius, double[] ka, double[] kd, double[] ks, double specular){
		super(ka, kd, ks, specular);
		this.center = center;
		this.radius = radius;
		

	}
	
	public Vector getCenter(){
		return center;
	}
	
	public double getRadius(){
		return radius;
	}
}
