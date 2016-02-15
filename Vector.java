import java.lang.Math;

public class Vector {
	private double u;
	private double v;
	private double w;
	private double length;
	public Vector(double u, double v, double w){
		this.u = u;
		this.v = v;
		this.w = w;
		length = Math.sqrt(u*u + v*v + w*w);
	}
	
	public static double dot(Vector a, Vector b){
		return ((a.getU()*b.getU()) + (a.getV()*b.getV()) + (a.getW()*b.getW()));
	}
	
	public static Vector add(Vector a, Vector b){
		return new Vector(a.getU()+b.getU(), a.getV()+b.getV(), a.getW()+b.getW());
	}
	
	public static Vector subtract(Vector a, Vector b){
		return new Vector(a.getU()-b.getU(), a.getV()-b.getV(), a.getW()-b.getW());
	}
	
	public static Vector divide(Vector a, double x){
		return new Vector(a.getU()/x, a.getV()/x, a.getW()/x);
	}
	
	public static Vector scalarMult(double x, Vector a){
		return new Vector(x*a.getU(), x*a.getV(), x*a.getW());
	}
	
	public static Vector bisector(Vector a, Vector b){
		double normalize = Vector.add(a, b).getLength();
		return Vector.divide(Vector.add(a, b), normalize);
	}
	
	public double getU(){
		return u;
	}
	
	public double getV(){
		return v;
	}
	
	public double getW(){
		return w;
	}
	
	public Vector getUnit(){
		return new Vector(u/length, v/length, w/length);
	}
	
	public double getLength(){
		return length;
	}
	
	public boolean equals(Vector b){
		return this.u == b.getU() && this.v == b.getV() && this.w == b.getW();
	}
}
