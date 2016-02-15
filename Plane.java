
public class Plane extends Shape {
	private Vector normal;
	private Vector center;
	public Plane(Vector normal, Vector center, double[] ka, double[] kd, double[] ks, double specular){
		super(ka, kd, ks, specular);
		this.normal = normal;
		this.center = center;
	}
	
	public Vector getNormal(){
		return normal;
	}
	
	public Vector getCenter(){
		return center;
	}
}
