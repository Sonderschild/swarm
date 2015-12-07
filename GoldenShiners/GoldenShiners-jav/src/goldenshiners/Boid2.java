package goldenshiners;

import java.awt.Color;
import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.text.Position;

public class Boid2 implements Boid{

	private Vector2d location;
	private Vector2d velocity;
	private Vector2d acceleration;
	private Color c1;
	
	private double fric; //
	private double m; // mass
	private double maxforce;    // Maximum steering force
	private double maxspeed;    // Maximum speed

	private double seePhi; //  viewing angle
	private double seeRad; // viewing radius

	Boid2(double x, double y, Vector2d velocity) {
		acceleration = new Vector2d(0, 0);
		fric = 0.2;
		seePhi=Math.PI/3;
		seeRad=80;
		c1 = Color.black;
		m = 10;

		this.velocity = velocity;

		location = new Vector2d(x, y);
		maxspeed = 1.5;
		maxforce = 0.05;
	}
	
	Boid2(double x, double y) {
		this(x,y,Vector2d.random());
	}

	public void run(ArrayList<Boid> Boids, double s_weight, double a_weight, double c_weight ){ //double seePhi, double seeRad
		flock(Boids,s_weight,a_weight,c_weight);
		update();
	}

	public void applyForce(Vector2d force) {
		acceleration.add(Vector2d.multiply(force, 1/m));
	}

	// We accumulate a new acceleration each time based on three 
	public void flock(ArrayList<Boid> allBoids, double s_weight, double a_weight, double c_weight) {
		Vector2d force = new Vector2d();
		ArrayList<Boid> Boids = new ArrayList<Boid>();
		for (Boid b : allBoids){
			if (this.canSee(b)) Boids.add(b);
		}
		//System.out.println(Boids.size());
		Vector2d sep = separate(Boids);   // Separation
		Vector2d ali = align(Boids);      // Alignment
		Vector2d coh = cohesion(Boids);   // Cohesion
		//System.out.println("Sep: "+sep);
		//System.out.println("Ali: "+ali);
		//System.out.println("Coh: "+coh);
		// Arbitrarily weight these forces
		double totalMag = sep.norm()+ali.norm()+coh.norm();
		//System.out.println(totalMag);
		
		sep.limit(maxforce);
		ali.limit(maxforce);
		coh.limit(maxforce);
		sep.multiply(s_weight);
		ali.multiply(a_weight);
		coh.multiply(c_weight);
		// Add the force vectors to acceleration
		//System.out.println(force);
		force.add(coh);
		force.add(sep);
		force.add(ali);
		//System.out.println(force);
		//force.limit(maxforce);
		applyForce(force);
		if (acceleration.norm() == 0) acceleration = Vector2d.random();
	}

	// Method to update location
	public void update() {
		velocity.sub(Vector2d.multiply(velocity,fric));
		// Update velocity
		velocity.add(Vector2d.multiply(acceleration,m));
		// Limit speed
		velocity.limit(maxspeed);
		location.add(velocity);
		// Reset accelertion to 0 each cycle
		acceleration.multiply(0);
	}

//	// A method that calculates and applies a steering force towards a target
//	// STEER = DESIRED MINUS VELOCITY
//	private Vector2d seek(Vector2d target) {
//		Vector2d desired = Vector2d.sub(target, location);  // A vector pointing from the location to the target
//		// Scale to maximum speed
//		// desired.normalize();
//		// desired.mult(maxspeed);
//
//		// Above two lines of code below could be condensed with new Vector2d setMag() method
//		// Not using this method until Processing.js catches up
//		desired.setMag(maxspeed);
//
//		// Steering = Desired minus Velocity
//		Vector2d steer = Vector2d.sub(desired, velocity);
//		//steer.limit(maxforce);  // Limit to maximum steering force
//		return steer;
//	}

	// Separation
	// Method checks for nearby Boids and steers away
	private Vector2d separate (ArrayList<Boid> Boids) {
		Vector2d diff ;
		Vector2d steer = new Vector2d(0, 0);
		int count = 0;
		// For every Boid in the system, check if it's too close
		for (Boid other : Boids) {
			diff = Vector2d.sub(location, other.getLocation());
			double d = diff.norm();
			// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
			if ((d > 0)) {
				// Calculate vector pointing away from neighbor
				diff.setMag(Math.pow(d, -1.5));       // Weight by distance
				steer.add(diff);
				count++;            // Keep track of how many
			}
		}

		// As long as the vector is greater than 0
		if (steer.norm() > 0) {

			// steer.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			steer.sub(velocity);
			steer.limit(maxforce);
		}
		return steer;
	}

	// Alignment
	// For every nearby Boid in the system, calculate the average velocity
	private Vector2d align (ArrayList<Boid> Boids) {
		Vector2d sum = new Vector2d(0, 0);
		int count = 0;
		for (Boid other : Boids) {
			double d = Vector2d.dist(location, other.getLocation());
			if ((d > 0) ) {
				sum.add(other.getVelocity());
				count++;
			}
		}
	
		if (count > 0) {
			sum.divide(count);
			return Vector2d.sub(sum, location);
			// Implement Reynolds: Steering = Desired - Velocity

//			sum.setMag(maxspeed);
//			Vector2d steer = Vector2d.sub(sum, velocity);
//			steer.limit(maxforce);
//			return steer;
//			return sum;
		} 
		else {
			return new Vector2d(0, 0);
		}
	}

	// Cohesion
	// For the average location (i.e. center) of all nearby Boids, calculate steering vector towards that location
	private Vector2d cohesion (ArrayList<Boid> Boids) {
		Vector2d sum = new Vector2d(0, 0);   // Start with empty vector to accumulate all locations
		int count = 0;
		for (Boid other : Boids) {
			double d = Vector2d.dist(location, other.getLocation());
			if ((d > 0)) {
				sum.add(other.getLocation()); // Add location
				count++;
			}
		}
		if (count > 0) {
			sum.divide(count);
			return Vector2d.sub(sum, location);  // Steer towards the location
		} 
		else {
			return new Vector2d(0, 0);
		}
	}

	public boolean canSee(Boid other){
		//System.out.println(other.getLocation());
		double d = Vector2d.dist(location, other.getLocation());
		if (d>0 && Math.abs(velocity.heading()-Vector2d.sub(other.getLocation(), location).heading())<seePhi && d <= seeRad){
			return true;
		}
		return false;
	}

	public Vector2d getLocation() {
		return new Vector2d(location);
	}

	public Vector2d getVelocity() {
		return new Vector2d(velocity);
	}

	public Vector2d getAccerleation() {
		return new Vector2d(acceleration);
	}

	public Color getColor(){
		return c1;
	}
	public void setColor(Color c){
		c1 = c;
	}

	public void setLocation(double x, double y) {
		this.location = new Vector2d(x,y);
	}
	public void setVelocity(double x, double y) {
		this.velocity = new Vector2d(x,y);
	}
}