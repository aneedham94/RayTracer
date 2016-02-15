
public abstract class Shape {
	private double[] ka, kd, ks;
	private double specular;
	public Shape(double[] ka, double[] kd, double[] ks, double specular){
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.specular = specular;
	}
	
	public double[] getKA(){
		return ka;
	}
	
	public double[] getKD(){
		return kd;
	}
	
	public double[] getKS(){
		return ks;
	}
	
	public double getSpecular(){
		return specular;
	}
	
	public static double[] shading(double r, double g, double b){
		double[] shading = {r, g, b};
		return shading;
	}
	
}
