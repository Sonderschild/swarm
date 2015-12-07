package goldenshiners;


import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;

public class Simulation extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Flock flocke;
	private int width, height;


    public Simulation() {
		width= 555;
		height = 444;

        initUI();
    }
    

    
    private void initUI() {
    	final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    	final Space space = new Space();
    //	space.setVisible(true);
        split.setBottomComponent(space);
		final LeftPanel sliderPanel = new LeftPanel();
		space.setLeftSide(sliderPanel);
    	split.setTopComponent(sliderPanel);
    	add(split);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Timer timer = space.getTimer();
                timer.stop();
            }
        });
        setTitle("GoldenShiners");
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	try{
	                Simulation ex = new Simulation();
	                //ex.pack();
	                ex.setVisible(true);
            	}catch(Exception e){
            		System.out.println(e.getMessage());
            		System.out.println(e.toString());
            		return;
            	}
            }
        });
    }
}

