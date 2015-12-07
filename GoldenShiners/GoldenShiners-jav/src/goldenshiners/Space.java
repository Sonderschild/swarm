package goldenshiners;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public	class Space extends JPanel implements ActionListener, MouseListener {
	private final int INITDELAY = 10;
	private int delay = INITDELAY;
	private Timer timer;
	private LeftPanel leftSide;
	
	private Flock flocke;

	double a_weight;
	double s_weight;
	double c_weight;

	double seePhi;
	double seeRad; 

	int r; //ausdehnung

	public Space() {
		this.flocke = flocke;
		a_weight=1.2;
		s_weight=1.5;
		c_weight=1.4;
		
		r=2;

		seePhi=Math.PI/4;
		seeRad=20;
		initTimer();
		addMouseListener(this);
		
		flocke = new Flock();
		for (int i = 0; i < 150; i++) {
			//velo = new Vector2d(10*(rand.nextDouble()-0.5), 10*(rand.nextDouble()-0.5));
		    flocke.addBoid(new Boid1(this.getWidth()/2,this.getHeight()/2));
		}
	}

	private void initTimer() {

		timer = new Timer(INITDELAY, this);
		timer.start();
	}

	public Timer getTimer() {

		return timer;
	}

	private void doDrawing(Graphics g) {
		ArrayList<Boid> boids = flocke.getBoids();
		Polygon unitTriangle;
		Shape tri;
		AffineTransform  rotator;
		AffineTransform translator;
		Graphics2D g2d = (Graphics2D) g;

		g2d.setPaint(Color.black);
		unitTriangle = new Polygon(new int[]{0,-r,r}, new int[] {-r*2,r*2,r*2} , 3);
		double theta;
		for (Boid b : boids){
			// Draw a triangle rotated in the direction of velocity
			theta = b.getVelocity().heading() + Math.PI/2;
			rotator = AffineTransform.getRotateInstance(theta);
			tri= rotator.createTransformedShape(unitTriangle);

			translator = AffineTransform.getTranslateInstance(b.getLocation().getX(),b.getLocation().getY());
			tri =  translator.createTransformedShape(tri);
			g2d.setPaint((Paint) b.getColor());
			g2d.draw(tri);
			g2d.fill(tri);
			
		}


	}

	private void updateWeights(){
		a_weight=leftSide.getAli();
		s_weight=leftSide.getSep();
		c_weight=leftSide.getCoh();
	}


	public void setLeftSide(LeftPanel leftSide){
		this.leftSide = leftSide;
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	//System.out.println("Ich bin da");
		updateWeights();
		flocke.run(a_weight, s_weight, c_weight);
		flocke.setBorder(this.getWidth()-r,this.getHeight()-r);
		//revalidate();
		repaint();
		timer.setDelay(leftSide.getSpeed());
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Boid b = new Boid2(e.getX(),e.getY());
		b.setColor(Color.red);
		flocke.addSpecialBoid(b);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}

