package goldenshiners;

import java.awt.Color;
import java.util.ArrayList;

class Flock {
	private final int SMOOTH = 1;
	private final int HARD = 2;
	private final int IGNOR = 0;
	private ArrayList<Boid> boids; // An ArrayList for all the boids
	private ArrayList<Boid> spezialBoids;
	private Color c1;
	private Color c2;
	
	private int borderType;
	private double height;
	private double width;
	

	public Flock() {
		boids = new ArrayList<Boid>(); // Initialize the ArrayList
		spezialBoids = new ArrayList<Boid>(); //
		c1 = Color.black;
		c2 = Color.blue;

	}

	public void run(double a_weight, double s_weight, double c_weight) {
		double tempX, tempY;
		for (Boid b : boids) {
			if (!spezialBoids.contains(b)) b.setColor(c1);
		}
		for (Boid sb :spezialBoids){
			for (Boid ob : boids) {
				if (!spezialBoids.contains(ob)) {
					if(sb.canSee(ob)){
						ob.setColor(sb.getColor().darker().darker());
					}
				}
			}
		}
		for (Boid b : boids) {
			b.run(boids,a_weight,s_weight,c_weight);  // Passing the entire list of boids to each boid individually
			tempX = b.getLocation().getX() ;
			tempY = b.getLocation().getY() ;
			if (borderType == SMOOTH) {
				if (tempX < 0) b.setLocation(tempX+width,tempY);
				if (tempY < 0) b.setLocation(tempX,tempY+height);
				if (tempX > width) b.setLocation(tempX-width,tempY);
				if (tempY > height) b.setLocation(tempX,tempY-height);
			}else if (borderType == HARD){
				if (tempX < 0 || tempX > width)  b.setVelocity(-b.getVelocity().getX(), b.getVelocity().getY());
				if (tempY < 0 || tempY > height) b.setVelocity( b.getVelocity().getX(),-b.getVelocity().getY());
			}
		}
	}

	public void addBoid(Boid b) {
		boids.add(b);
	}
	
	public void addSpecialBoid(Boid b) {
		boids.add(b);
		spezialBoids.add(b);
	}
	

	public ArrayList<Boid> getBoids(){
		return boids;
	}

	public ArrayList<Boid> getSpzeialBoids(){
		return (ArrayList<Boid>) spezialBoids.clone();
	}

	public void setBorder(double width, double height) {
		this.height = height;
		this.width = width;
		this.borderType = HARD;
	}
}
