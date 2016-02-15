import java.util.LinkedList;
import java.lang.Math;

public class Scene {
	private LinkedList<Sphere> Spheres = new LinkedList<Sphere>();
	private Plane plane;
	private Light light;
	private boolean inter;
	
	public void add(Sphere s){
		Spheres.add(s);
	}
	
	public void add(Plane p){
		plane = p;
	}
	
	public void add(Light l){
		light = l;
	}
	
	public int[] coloredIntersect(Ray ray, double dist){
		inter = false;
		int[] RGB = {0,0,0};
		for(Sphere s: Spheres){
			RGB = SphereIntersect(s, ray, false);
			if(inter) return RGB;
		}
		RGB = PlaneIntersect(ray, dist);
		if(!inter){
			RGB[0] = 0;
			RGB[1] = 0;
			RGB[2] = 0;
		}

		return RGB;
	}
	
	private int[] SphereIntersect(Sphere s, Ray ray, boolean recursive){
		int[] RGB = new int[3];
		Vector l = ray.getVector().getUnit();
		Vector o = ray.getOrigin();
		Vector c = s.getCenter();
		double r = s.getRadius();
		Vector o_minus_c = Vector.subtract(o, c);
		double loc = Vector.dot(l, o_minus_c);
		double intersection;
		Vector illumination;
		double epsilon = 0.99;
		if((square(loc) - (square(o_minus_c.getLength())) + square(r)) < 0){
			RGB[0] = 0;
			RGB[1] = 0;
			RGB[2] = 0;
		}
		else if((square(loc) - (square(o_minus_c.getLength())) + square(r)) == 0){
			inter = true;
			intersection = -loc;
			illumination = Vector.scalarMult(intersection, ray.getVector().getUnit());
			if(!recursive){
				RGB = SphereShading(s, Vector.scalarMult(-loc, ray.getVector().getUnit()));
			}
			else{
				if(Vector.dot(ray.getVector().getUnit(), illumination.getUnit()) < epsilon){
					RGB[0] = 0;
					RGB[1] = 0;
					RGB[2] = 0;
				}
				else{
					RGB[0] = 1;
					RGB[1] = 1;
					RGB[2] = 1;
				}
			}
		}
		
		else{
			inter = true;
			intersection = Math.abs(-loc + Math.sqrt(square(loc) - square(o_minus_c.getLength()) + square(r)))
					< Math.abs(-loc - Math.sqrt(square(loc) - square(o_minus_c.getLength()) + square(r)))
					? -loc + Math.sqrt(square(loc) - square(o_minus_c.getLength()) + square(r))
					: -loc - Math.sqrt(square(loc) - square(o_minus_c.getLength()) + square(r));
			illumination = Vector.scalarMult(intersection, ray.getVector().getUnit());
			if(!recursive){
				RGB = SphereShading(s, illumination);
			}
			else{
				if(Vector.dot(ray.getVector().getUnit(), illumination.getUnit()) < epsilon){
					RGB[0] = 0;
					RGB[1] = 0;
					RGB[2] = 0;
				}
				else{
					RGB[0] = 1;
					RGB[1] = 1;
					RGB[2] = 1;
				}
			}
		}

		return RGB;
	}
	
	private int[] SphereShading(Sphere s, Vector v){
		int Lr = 0, Lg = 0, Lb = 0;
		
		Vector l = Vector.subtract(light.getPoint(), v);
		Vector n = Vector.subtract(v, s.getCenter());
		Ray shadow = new Ray(v, l, l.getLength());
		boolean shadowed = false;
		for(Sphere p: Spheres){
			if(!p.equals(s)){
				int[] check = SphereIntersect(p, shadow, true);
				if(check[0] == 1 || check[1] == 1 || check[2] == 1) shadowed = true;
			}
		}
		
		if(!shadowed){
			//Diffuse shading
			Lr += (int) (s.getKD()[0]*Math.max(0, Vector.dot(n.getUnit(), l.getUnit()))*255);
			Lg += (int) (s.getKD()[1]*Math.max(0, Vector.dot(n.getUnit(), l.getUnit()))*255);
			Lb += (int) (s.getKD()[2]*Math.max(0, Vector.dot(n.getUnit(), l.getUnit()))*255);
			
			//Specular shading
			Vector h = Vector.bisector(l, Vector.scalarMult(-1, v));
			Lr += (int) ((s.getKS()[0] * Math.pow(Math.max(0, Vector.dot(n.getUnit(), h.getUnit())), s.getSpecular()))*255);
			Lg += (int) ((s.getKS()[1] * Math.pow(Math.max(0, Vector.dot(n.getUnit(), h.getUnit())), s.getSpecular()))*255);
			Lb += (int) ((s.getKS()[2] * Math.pow(Math.max(0, Vector.dot(n.getUnit(), h.getUnit())), s.getSpecular()))*255);
		}

		
		//Ambient shading
		Lr += (int) (s.getKA()[0]*255);
		Lg += (int) (s.getKA()[1]*255);
		Lb += (int) (s.getKA()[2]*255);
		
		
		int[] RGB = {Math.min(Lr, 255), Math.min(Lg, 255), Math.min(Lb, 255)};
		RGB = gammaCorrect(RGB, 1/2.2);
		return RGB;
	}
	
	private int[] PlaneIntersect(Ray ray, double dist){
		int[] RGB = new int[3];
		double d = (Vector.dot(Vector.subtract(plane.getCenter(), ray.getVector().getUnit()), plane.getNormal()))
				/Vector.dot(ray.getVector().getUnit(), plane.getNormal());
		if(d < dist){
			RGB[0] = 0;
			RGB[1] = 0;
			RGB[2] = 0;
		}
		else{
			inter = true;
			RGB = PlaneShading(Vector.scalarMult(d, ray.getVector().getUnit()));
		}
		return RGB;
	}
	
	private int[] PlaneShading(Vector v){
		int Lr = 0, Lg = 0, Lb = 0;
		
		//Diffuse shading
		Vector l = Vector.subtract(light.getPoint(), v);
		Vector n = plane.getNormal();
		Ray shadow = new Ray(v, l, l.getLength());
		boolean shadowed = false;
		for(Sphere s: Spheres){
			int[] check = SphereIntersect(s, shadow, true);
			if(check[0] == 1 || check[1] == 1 || check[2] == 1) shadowed = true;
		}
		
		if(!shadowed){
			//Diffuse shading
			Lr += (int) (plane.getKD()[0]*Math.max(0, Vector.dot(n.getUnit(), l.getUnit()))*255);
			Lg += (int) (plane.getKD()[1]*Math.max(0, Vector.dot(n.getUnit(), l.getUnit()))*255);
			Lb += (int) (plane.getKD()[2]*Math.max(0, Vector.dot(n.getUnit(), l.getUnit()))*255);
			
			//Specular shading
			Vector h = Vector.bisector(l, v);
			Lr += (int) (plane.getKS()[0]*Math.max(0, Vector.dot(n.getUnit(), h.getUnit()))*255);
			Lg += (int) (plane.getKS()[1]*Math.max(0, Vector.dot(n.getUnit(), h.getUnit()))*255);
			Lb += (int) (plane.getKS()[2]*Math.max(0, Vector.dot(n.getUnit(), h.getUnit()))*255);
		}
		
		//Ambient shading
		Lr += (int) (plane.getKA()[0]*255);
		Lg += (int) (plane.getKA()[1]*255);
		Lb += (int) (plane.getKA()[2]*255);
		
		int[] RGB = {Math.min(Lr, 255), Math.min(Lg, 255), Math.min(Lb, 255)};
		RGB = gammaCorrect(RGB, 1/2.2);
		return RGB;
	}
	
	private int[] gammaCorrect(int[] RGB, double gamma){
		double r = (double)RGB[0]/255, g = (double)RGB[1]/255, b = (double)RGB[2]/255;
		r = Math.pow(r, gamma);
		g = Math.pow(g, gamma);
		b = Math.pow(b, gamma);
		int[] corrected = new int[3];
		corrected[0] = (int) (r*255);
		corrected[1] = (int) (g*255);
		corrected[2] = (int) (b*255);
		return corrected;
	}
	
	private double square(double a){
		return a*a;
	}
}
