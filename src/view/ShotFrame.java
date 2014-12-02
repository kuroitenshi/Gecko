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
	
	private int selectedShotIndex;
	public Shot selectedShot;
	public ArrayList<Shot> shotList;
	
	public ShotFrame (String genre, ArrayList<Shot> shotList) {
		
		super("Gecko Movie Classifier");
		this.shotList = shotList;
		refresh(0);
		setVisible(true);

	}
	
	public void refresh(int selected) {	
		getContentPane().removeAll();
		
		selectedShotIndex = selected;
		selectedShot = shotList.get(selectedShotIndex);
		
		getContentPane().setBackground(Color.black);
		getContentPane().setPreferredSize(new Dimension(890, 480));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
			
		getContentPane().add(new ShotPanel(selectedShotIndex, shotList, this));
		getContentPane().add(new DetailPanel(selectedShot, selected));
		
		pack();
	}

	public int getSelectedShotIndex() {
		return selectedShotIndex;
	}

	public void setSelectedShotIndex(int selectedShotIndex) {
		this.selectedShotIndex = selectedShotIndex;
	}
	
}
