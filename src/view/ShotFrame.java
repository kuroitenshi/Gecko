package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class ShotFrame extends JFrame {
	
	public ShotFrame () {
		
		super("Gecko Movie Classifier");

		getContentPane().setBackground(Color.white);
		getContentPane().setPreferredSize(new Dimension(600, 400));
		pack();
		setVisible(true);

		
	}
	
}
