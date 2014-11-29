package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Shot;

public class ShotFrame extends JFrame {
	
	public Shot selectedShot;
	
	public ShotFrame (String genre, ArrayList<Shot> shotList) {
		
		super("Gecko Movie Classifier");
		getContentPane().setBackground(Color.black);
		getContentPane().setPreferredSize(new Dimension(890, 480));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		getContentPane().add(new ShotPanel(shotList));
		getContentPane().add(new DetailPanel(shotList.get(0)));
		
		pack();
		setVisible(true);

		
	}
	
}
