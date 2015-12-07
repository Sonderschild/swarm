package goldenshiners;


import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class LeftPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSlider cohSlider,sepSlider,aliSlider;
	private JLabel cohLabel, sepLabel, aliLabel;
	private JLabel speedLabel;
	private JSlider speedSlider;
	public LeftPanel() {
		this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
		speedLabel=getLabel("Speed");
		speedSlider = new JSlider(5,200,20);
		//speedSlider.setMajorTickSpacing(25);
		//speedSlider.setMinorTickSpacing(1);
		//speedSlider.setPaintTicks(true);
		speedSlider.setInverted(true);
		//speedSlider.setPaintLabels(true);
		speedSlider.createStandardLabels(1);
		add(speedSlider);
		
		cohLabel = getLabel("Cohesion");
		cohSlider = getWeightSlider("coh");

		sepLabel = getLabel("Seperation");
		sepSlider = getWeightSlider("sep");
		
		aliLabel = getLabel("Aligation");
		aliSlider = getWeightSlider("ali");
		
		
		
	}
	
	private JSlider getWeightSlider(String name){
		JSlider slider;
		slider = new  JSlider(0,100,50);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.createStandardLabels(1);
		add(slider);
		return slider;
	}
	
	private JLabel getLabel(String name){
		JLabel label;
		label = new JLabel(name);
		add(label);
		return label;
	}
	
	public double getCoh(){
		return cohSlider.getValue()/100.0;
	}
	
	public double getSep(){
		return sepSlider.getValue()/100.0;
	}
	
	public double getAli(){
		return aliSlider.getValue()/100.0;
	}
	
	public int getSpeed(){
		return speedSlider.getValue();
	}
	
}
