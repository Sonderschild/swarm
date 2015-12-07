package goldenshiners;

import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;

public class Boid1 implements Boid{
	private Vector2d[] lastForces;
	private Vector2d location;
	private Vector2d velocity;
	private Vector2d acceleration;
	private Color c1;

	private double maxforce;    // Maximum steering force
	private double maxspeed;    // Maximum speed

	private double seePhi; //  viewing angle
	private double seeRad; //ve´wing radius

	Boid1(double x, double y, Vector2d velocity) {
		lastForces = new Vector2d[3];
		acceleration = new Vector2d(0, 0);

		seePhi=Math.PI/2;
		seeRad=80;
		c1 = Color.black;


		this.velocity = velocity;

		// Leaving the code temporarily this way so that this example runs in JS
		// double angle = random(TWO_PI);
		// velocity = new Vector2d(cos(angle), sin(angle));

		location = new Vector2d(x, y);
		maxspeed = 1.5;
		maxforce = 0.03;
	}
	
	Boid1(double x, double y) {
		this(x,y,Vector2d.random());
	}

	public void run(ArrayList<Boid> boids, double a_weight, double s_weight, double c_weight ){ //double seePhi, double seeRad
		flock( boids,s_weight,a_weight,c_weight);
		update();
	}

	public void applyForce(Vector2d force) {
		// We could add mass here if we want A = F / M
		acceleration.add(force);
	}

	// We accumulate a new acceleration each time based on three 
	public void flock(ArrayList<Boid> allBoids, double s_weight, double a_weight, double c_weight) {
		ArrayList<Boid> boids = new ArrayList<Boid>();
		for (Boid b : allBoids){
			if (this.canSee(b)) boids.add(b);
		}
		Vector2d sep = separate(boids);   // Separation
		Vector2d ali = align(boids);      // Alignment
		Vector2d coh = cohesion(boids);   // Cohesion
		// Arbitrarily weight these forces
		sep.multiply(s_weight);
		ali.multiply(a_weight);
		coh.multiply(c_weight);
		// Add the force vectors to acceleration
		applyForce(sep);
		applyForce(ali);
		applyForce(coh);
		lastForces[0] = ali;
		lastForces[1] = sep;
		lastForces[2] = coh;
	}

	// Method to update location
	public void update() {
		// Update velocity
		velocity.add(acceleration);
		// Limit speed
		velocity.limit(maxspeed);
		location.add(velocity);
		// Reset accelertion to 0 each cycle
		acceleration.multiply(0);
	}

	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	private Vector2d seek(Vector2d target) {
		Vector2d desired = Vector2d.sub(target, location);  // A vector pointing from the location to the target
		// Scale to maximum speed
		// desired.normalize();
		// desired.mult(maxspeed);

		// Above two lines of code below could be condensed with new Vector2d setMag() method
		// Not using this method until Processing.js catches up
		desired.setMag(maxspeed);

		// Steering = Desired minus Velocity
		Vector2d steer = Vector2d.sub(desired, velocity);
		steer.limit(maxforce);  // Limit to maximum steering force
		return steer;
	}

	// Separation
	// Method checks for nearby boids and steers away
	private Vector2d separate (ArrayList<Boid> boids) {
		Vector2d diff ;
		double desiredseparation = 25.0;
		Vector2d steer = new Vector2d(0, 0);
		int count = 0;
		// For every boid in the system, check if it's too close
		for (Boid other : boids) {
			double d = Vector2d.dist(this.location, other.getLocation());
			// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
			if ((d > 0) && (d < desiredseparation)) {
				// Calculate vector pointing away from neighbor
				diff = Vector2d.sub(location, other.getLocation());
				diff.normalize();
				diff.divide(d);        // Weight by distance
				steer.add(diff);
				count++;            // Keep track of how many
			}
		}
		// Average -- divide by how many
		if (count > 0) {
			steer.divide((double)count);
		}

		// As long as the vector is greater than 0
		if (steer.norm() > 0) {
			// First two lines of code below could be condensed with new Vector2d setMag() method
			// Not using this method until Processing.js catches up
			// steer.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			steer.normalize();
			steer.multiply(maxspeed);
			steer.sub(velocity);
			steer.limit(maxforce);
		}
		return steer;
	}

	// Alignment
	// For every nearby boid in the system, calculate the average velocity
	private Vector2d align (ArrayList<Boid> boids) {
		double neighbordist = 50;
		Vector2d sum = new Vector2d(0, 0);
		int count = 0;
		for (Boid other : boids) {
			double d = Vector2d.dist(location, other.getLocation());
			if ((d > 0) && (d < neighbordist)) {
				sum.add(other.getVelocity());
				count++;
			}
		}
		if (count > 0) {
			sum.divide((double)count);
			// First two lines of code below could be condensed with new Vector2d setMag() method
			// Not using this method until Processing.js catches up
			// sum.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			sum.normalize();
			sum.multiply(maxspeed);
			Vector2d steer = Vector2d.sub(sum, velocity);
			steer.limit(maxforce);
			return steer;
		} 
		else {
			return new Vector2d(0, 0);
		}
	}

	// Cohesion
	// For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
	private Vector2d cohesion (ArrayList<Boid> boids) {
		double neighbordist = 50;
		Vector2d sum = new Vector2d(0, 0);   // Start with empty vector to accumulate all locations
		int count = 0;
		for (Boid other : boids) {
			double d = Vector2d.dist(location, other.getLocation());
			if ((d > 0) && (d < neighbordist)) {
				sum.add(other.getLocation()); // Add location
				count++;
			}
		}
		if (count > 0) {
			sum.divide(count);
			return seek(sum);  // Steer towards the location
		} 
		else {
			return new Vector2d(0, 0);
		}
	}

	public boolean canSee(Boid other){
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