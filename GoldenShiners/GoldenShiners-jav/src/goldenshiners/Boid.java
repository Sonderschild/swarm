package goldenshiners;

import java.awt.Color;
import java.util.ArrayList;
/**
 * Interface for Boids
 * @author Sonderschild
 *
 */
public interface Boid {
	
	/**
	 * Expects ArrayList of other Boids and performs the swarm thingy. 
	 * Moves in the end this Boid how the swarm algorithm says
	 * @param Boids List of other Boids in Swarm
	 * @param s_weight how much weight for seperation
	 * @param a_weight how much weight for ali
	 * @param c_weight how much weight for cohesion
	 */
	public void run(ArrayList<Boid> Boids, double s_weight, double a_weight, double c_weight );
	
	/**
	 * Can this Boid see another Boid?
	 * @param other other Boid
	 * @return true if it can see it, false else
	 */
	public boolean canSee(Boid other);
	
	/**
	 * Get the Location of the Boid
	 * @return location of Boid
	 */
	public Vector2d getLocation() ;
	
	/**
	 * Gets the Velocity of the Boid
	 * @return Velocity of Boid
	 */
	public Vector2d getVelocity() ;
	
	/**
	 * gets the acceleration of the Boid
	 * @return Accerleration of Boid
	 */
	public Vector2d getAccerleation() ;
	
	/**
	 * Gets Color of Boid
	 * @return Color of Boid
	 */
	public Color getColor();
	
	/**
	 * Sets Color of Boid
	 * @param Color of Boid
	 */
	public void setColor(Color c);
	
	/**
	 * Sets the Location of a Boid (Why so ever)
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setLocation(double x, double y) ;
	
	/**
	 * Sets the Velocity of the Boid (Think this was importent-some how)
	 * @param x x coordinate (of v Vector)
	 * @param y y coordinate (of v vector)
	 */
	public void setVelocity(double x, double y) ;
}

