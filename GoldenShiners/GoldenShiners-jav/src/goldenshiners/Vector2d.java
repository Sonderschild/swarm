package goldenshiners;

import java.util.Random;

public class Vector2d {

	private double x;
	private double y;
	
	
	public Vector2d() {
		super();
		this.x = 0;
		this.y = 0;
	}


	public Vector2d(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d location) {
		super();
		this.x = location.getX();
		this.y = location.getY();
	}


	public void multiply(double c){
		x*=c;
		y*=c;
		return ;
	}
	
	public void divide(double c){
		x/=c;
		y/=c;		
		return ;
	}
	
	public void add(Vector2d v2){
		x=x+v2.getX();
		y=y+v2.getY();
		return ;
	}

	public void sub(Vector2d v2){
		x=x-v2.getX();
		y=y-v2.getY(); 
		return  ;
	}
	
	public double norm(){
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y,2));
	} 
	
	public void normalize(){
		double norm = this.norm();
		if (norm != 0) this.divide(norm);			
	}
	
	public void setMag(double c){
		this.normalize();
		this.multiply(c);
	}
	
	public double dot(Vector2d v2){
		return (this.getX()*v2.getX() + this.getY() * v2.getY());
	}
	
	public double angle (Vector2d v2) {
		return Math.acos((this.dot(v2))/(this.norm()*v2.norm()));
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return '('+Double.toString(x)+','+Double.toString(y)+')';
	}

	/**
	 * Creates random vector between -(0.5,0.5) and (0.5,0.5) 
	 * @return new random vetcor
	 */
	public static Vector2d random() {
		double x;
		double y;
		
		x =0.5- Math.random();
		y =0.5- Math.random();
		return new Vector2d(x,y);
	}


	public void limit(double max) {
		if (this.norm() > max) {
			this.setMag(max);
		}
	}

	/**
	 * Subtrahiert v2 von v1
	 * @param v1 
	 * @param v2
	 * @return v1-v2
	 */
	public static Vector2d sub(Vector2d v1, Vector2d v2) {
		return new Vector2d((v1.getX()-v2.getX()), (v1.getY() -v2.getY()));
	}

	public double heading() {
		Vector2d e1;
		e1 = new Vector2d(1,0);
		if (this.getY() >= 0 ) return  this.angle(e1);
		else return 2*Math.PI-this.angle(e1);
	}


	public static double dist(Vector2d v1, Vector2d v2) {
		Vector2d distv;
		distv = Vector2d.sub(v1, v2);
		return distv.norm() ;
	}


	public static Vector2d multiply(Vector2d v, double c) {
		// TODO Auto-generated method stub
		return new Vector2d(v.getX()*c,v.getY()*c);
	}
	public static Vector2d add(Vector2d v, Vector2d w) {
		// TODO Auto-generated method stub
		return new Vector2d(v.getX()+w.getX(),v.getY()+w.getY());
	}
}
