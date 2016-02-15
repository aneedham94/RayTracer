import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.Math;

public class Driver {
	private static final int nx = 512;
	private static final int ny = 512;
	private static final double left = -0.1;
	private static final double right = 0.1;
	private static final double bottom = -0.1;
	private static final double top = 0.1;
	private static final double dist = 0.1;
	private static final Vector u_ = new Vector(1, 0, 0);
	private static final Vector v_ = new Vector(0, 1, 0);
	private static final Vector w_ = new Vector(0, 0, 1);
	private static final Vector e = new Vector(0, 0, 0);
	
	private static final int anti_aliasing_num = 64;
	
	
	public static void main(String[] args){
		double start;
		
		Sphere sa = new Sphere(new Vector(-4, 0, -7), 1, Shape.shading(0.2, 0, 0), Shape.shading(1, 0, 0), Shape.shading(0, 0, 0), 0);
		Sphere sb = new Sphere(new Vector(0, 0, -7), 2, Shape.shading(0, 0.2, 0), Shape.shading(0, 0.5, 0), Shape.shading(0.5, 0.5, 0.5), 32);
		Sphere sc = new Sphere(new Vector(4, 0, -7), 1, Shape.shading(0, 0, 0.2), Shape.shading(0, 0, 1), Shape.shading(0, 0, 0), 0);
		Plane plane = new Plane(new Vector(0, 1, 0), new Vector(0, -2, 0), Shape.shading(0.2, 0.2, 0.2), Shape.shading(1, 1, 1), Shape.shading(0, 0, 0), 0);
		Light light = new Light(new Vector(-4, 4, -3), 1);
		Scene scene = new Scene();
		scene.add(sa);
		scene.add(sb);
		scene.add(sc);
		scene.add(plane);
		scene.add(light);
		BufferedImage img1 = new BufferedImage(nx, ny, BufferedImage.TYPE_INT_RGB);
		BufferedImage img2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		BufferedImage img3 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		
		String directory = System.getProperty("user.dir");
		
		File f1 = new File(directory + "/Image1.png");
		File f2 = new File(directory + "/Image2.png");
		File f3 = new File(directory + "/Image3.png");
		
		Vector s;
		double u, v;
		Vector p, d;
		Ray rt;
		
		
		start = System.currentTimeMillis();
		for(int i = 0; i < nx; i++){
			for(int j = 0; j < ny; j++){
				u = left + (right-left)*(i+0.5)/nx;
				v = bottom + (top-bottom)*(j+0.5)/ny;
				
				//This should be equivalent to the equation  (s = e + uU + vV - dW)  from the slides
				s = Vector.add(e, Vector.add(Vector.scalarMult(u, u_), Vector.add(Vector.scalarMult(v, v_), Vector.scalarMult(-dist, w_))));
				
				p = e;
				d = Vector.subtract(s,  e);
				rt = new Ray(p, d, dist);
				int[] RGB = scene.coloredIntersect(rt, dist);
				if(RGB[0] != 0 || RGB[1] != 0 || RGB[2] != 0){
					img1.setRGB(i, 511-j, 0xFFFFFF);
				}
				else{
					img1.setRGB(i, 511-j, 0x000000);
				}
				
				//Doing colors
				int color = (RGB[0]*256*256) + RGB[1]*256 + RGB[2];
				img2.setRGB(i, 511-j, color);
				
				//Doing anti-aliasing
				int[][] samples = new int[anti_aliasing_num][3];
				samples[0] = RGB;
				for(int k = 1; k < anti_aliasing_num; k++){
					double rx = Math.random();
					double ry = Math.random();
					u = left + (right-left)*(i+rx)/nx;
					v = bottom + (top-bottom)*(j+ry)/ny;
					s = Vector.add(e, Vector.add(Vector.scalarMult(u, u_), Vector.add(Vector.scalarMult(v, v_), Vector.scalarMult(-dist, w_))));
					p = e;
					d = Vector.add(s, Vector.scalarMult(-1, e));
					rt = new Ray(p, d, dist);
					samples[k] = scene.coloredIntersect(rt, dist);
				}
				
				int Rsum = 0;
				int Gsum = 0;
				int Bsum = 0;
				
				for(int k = 0; k < anti_aliasing_num; k++){
					Rsum += samples[k][0];
					Gsum += samples[k][1];
					Bsum += samples[k][2];
				}
				
				int R_average = (int) Rsum/anti_aliasing_num;
				int G_average = (int) Gsum/anti_aliasing_num;
				int B_average = (int) Bsum/anti_aliasing_num;
				int color_average = (R_average*256*256) + G_average*256 + B_average;
				img3.setRGB(i, 511-j, color_average);
			}
		}
		
		try{
			ImageIO.write(img1, "PNG", f1);
			ImageIO.write(img2, "PNG", f2);
			ImageIO.write(img3, "PNG", f3);
		} catch(IOException e){
			System.out.println("Failed to save one of the images to file correctly");
			e.printStackTrace();
		}
		System.out.println("Total time(ms): " + (System.currentTimeMillis()-start));
	}
}
