//package goldenshiners;
//
//import java.awt.Color;
//import java.awt.EventQueue;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.util.Random;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.Timer;
//
//class Surface extends JPanel implements ActionListener {
//
//    private final int DELAY = 150;
//    private Timer timer;
//
//    public Surface() {
//
//        initTimer();
//    }
//
//    private void initTimer() {
//
//        timer = new Timer(DELAY, this);
//        timer.start();
//    }
//    
//    public Timer getTimer() {
//        
//        return timer;
//    }
//
//    private void doDrawing(Graphics g) {
//
//        Graphics2D g2d = (Graphics2D) g;
//
//        g2d.setPaint(Color.blue);
//
//        int w = getWidth();
//        int h = getHeight();
//
//        Random r = new Random();
//
//        for (int i = 0; i < 2000; i++) {
//
//            int x = Math.abs(r.nextInt()) % w;
//            int y = Math.abs(r.nextInt()) % h;
//            g2d.drawLine(x, y, x, y);
//        }
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//
//        super.paintComponent(g);
//        doDrawing(g);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        repaint();
//    }
//}
//
//public class Rumpelkammer {
//
//}
//
//
//class Simulation{
//	Flock flock;
//	
//	void setup() {
//	  size(640, 360);
//	  flock = new Flock();
//	  // Add an initial set of boids into the system
//	  for (int i = 0; i < 150; i++) {
//	    flock.addBoid(new Boid(width/2,height/2));
//	  }
//	}
//	
//	void draw() {
//	  a_weight=1.5;
//	  s_weight=1.0;
//	  c_weight=1.0;
//	  
//	  seePhi=Math.PI/4;
//	  seeRad=20;
//	  background(50);
//	  flock.run(a_weight,s_weight,c_weight);
//	}
//	
//	// Add a new boid into the System
//	void mousePressed() {
//	  Boid b = new Boid(mouseX,mouseY);
//	  b.c1 =#FC0303;
//	  flock.spezialBoids.add(b);
//	  flock.addBoid(b);
//	}
//	
//	// Wraparound
//	void borders() {
//	  if (location.getX() < -r) this.location.add(new Vector2d(width+r,location.getY());
//	  if (location.getY() < -r) location.y = height+r;
//	  if (location.getX() > width+r) location.x = -r;
//	  if (location.getY() > height+r) location.y = -r;
//	}
//	
//	void render() {
//	    // Draw a triangle rotated in the direction of velocity
//	    double theta = velocity.heading() + Math.PI/2;
//	    // heading2D() above is now heading() but leaving old syntax until Processing.js catches up
//	    
//	    //fill(900, 900);
//	    stroke(255);
//	    pushMatrix();
//	    translate(location.x, location.y);
//	    rotate(theta);
//	    beginShape(TRIANGLES);
//	    vertex(0, -r*2);
//	    vertex(-r, r*2);
//	    vertex(r, r*2);
//	    fill(c1);
//	    endShape();
//	    popMatrix();
//	  }
//	
//	
//	// The Flock (a list of Boid objects)
//
