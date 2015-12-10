package goldenshiners;

import java.awt.Color;
import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.text.Position;

public class Boid2 implements Boid{
	private ArrayList<Boid> icu;
	private Vector2d[] lastForces;
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
		icu = new ArrayList<Boid>();
		lastForces = new Vector2d[3];
		acceleration = new Vector2d(0, 0);
		fric = 0.1;
		seePhi=Math.PI/3;
		seeRad=50;
		c1 = Color.black;
		m = 10;

		this.velocity = velocity;

		location = new Vector2d(x, y);
		maxspeed = 1.5;
		maxforce = 0.1;
	}

	Boid2(double x, double y) {
		this(x,y,Vector2d.random());
	}

	public void run(ArrayList<Boid> Boids, double a_weight, double s_weight, double c_weight ){ //double seePhi, double seeRad
		flock(Boids,a_weight,s_weight,c_weight);
		update();
	}

	private void applyForce(Vector2d force) {
		acceleration.add(Vector2d.multiply(force, 1/m));
	}

	// We accumulate a new acceleration each time based on three 
	private void flock(ArrayList<Boid> allBoids, double a_weight, double s_weight, double c_weight) {
		Vector2d force = new Vector2d();
		for (Boid b : allBoids){
			if (this.canSee(b)) icu.add(b);
		}
		//System.out.println(Boids.size());
		Vector2d sep = separate(icu);   // Separation
		Vector2d ali = align(icu);      // Alignment
		Vector2d coh = cohesion(icu);   // Cohesion

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
		//System.out.println("Sep: "+sep);
		//System.out.println("Ali: "+ali);
		force.add(ali);
		force.add(sep);
		force.add(coh);

		lastForces[0] = ali;
		lastForces[1] = sep;
		lastForces[2] = coh;
		//System.out.println(force);
		//force.limit(maxforce);
		applyForce(force);
		if (acceleration.norm() == 0) acceleration = Vector2d.random();
	}

	// Method to update location
	private void update() {
		velocity.sub(Vector2d.multiply(velocity,fric*velocity.norm()));
		// Update velocity
		velocity.add(Vector2d.multiply(acceleration,m));
		// Limit speed
		velocity.limit(maxspeed);
		location.add(velocity);
		// Reset accelertion to 0 each cycle
		acceleration.multiply(0.3);
	}


	// Separation
	// Method checks for nearby Boids and steers away
	private Vector2d separate (ArrayList<Boid> Boids) {
		double desiredseparation = seeRad;
		Vector2d diff ;
		Vector2d steer = new Vector2d(0, 0);
		int count = 0;
		// For every Boid in the system, check if it's too close
		for (Boid other : Boids) {
			diff = Vector2d.sub(location, other.getLocation());
			double d = diff.norm();
			// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
			if ((d > 0) && desiredseparation > d) {
				// Calculate vector pointing away from neighbor
				diff.setMag(Math.pow(d, -2.0));       // Weight by distance
				steer.add(diff);
				count++;            // Keep track of how many
			}
		}

		// As long as the vector is greater than 0
		if (steer.norm() > 0) {

			steer.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			steer.sub(velocity);
			//steer.limit(maxforce);
		}
		return steer;
	}

	// Alignment
	// For every nearby Boid in the system, calculate the average velocity
	private Vector2d align (ArrayList<Boid> Boids) {
		double neighbordist = seeRad;
		Vector2d sum = new Vector2d(0, 0);
		int count = 0;
		for (Boid other : Boids) {
			double d = Vector2d.dist(location, other.getLocation());
			if ((d > 0) && 	neighbordist > d ) {
				sum.add(other.getVelocity());
				count++;
			}
		}

		if (count > 0) {
			sum.divide(count);
			//return Vector2d.sub(sum, location);
			// Implement Reynolds: Steering = Desired - Velocity

			sum.setMag(maxspeed);
			Vector2d steer = Vector2d.sub(sum, velocity);
			//steer.limit(maxforce);
			return steer;
			//return sum;
		} 
		else {
			return new Vector2d(0, 0);
		}
	}

	// Cohesion
	// For the average location (i.e. center) of all nearby Boids, calculate steering vector towards that location
	private Vector2d cohesion (ArrayList<Boid> Boids) {
		double neighbordist = seeRad;
		Vector2d middle = new Vector2d(0, 0);   // Start with empty vector to accumulate all locations
		Vector2d aim = new Vector2d();
		int count = 0;
		for (Boid other : Boids) {
			double d = Vector2d.dist(location, other.getLocation());
			if ((d > 0) && neighbordist > d) {
				middle.add(other.getLocation()); // Add location
				count++;
			}
		}
		if (count > 0) {
			middle.divide(count);
			aim=Vector2d.sub(middle, location);
			aim.setMag(maxspeed);
			return Vector2d.sub(aim, velocity);  // Steer towards the location
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

	@Override
	public Vector2d[] getLastForces() {
		return lastForces;
	}
}