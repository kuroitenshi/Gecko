package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Shot;

public class ShotFrame extends JFrame {
	
	public Shot selectedShot;
	
	public ShotFrame (String genre) {
		
		super("Gecko Movie Classifier");
		getContentPane().setBackground(Color.white);
		getContentPane().setPreferredSize(new Dimension(900, 500));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		getContentPane().add(new ShotPanel());
		getContentPane().add(new DetailPanel());
		
		pack();
		setVisible(true);

		
	}
	
}
