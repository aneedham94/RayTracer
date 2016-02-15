
public class Light {
	private Vector point;
	private double intensity;
	public Light(Vector point, double intensity){
		this.point = point;
		this.intensity = intensity;
	}
	
	public Vector getPoint(){
		return point;
	}
	
	public double getIntensity(){
		return intensity;
	}
}
