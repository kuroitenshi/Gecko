package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DetailPanel extends JPanel {
	
	int shotNumber = 1;
	double dist;
	double lumi;
	double flam;
	double enrg;
	double powr;
	double pace;

	public DetailPanel() {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(250, 500));
		setMaximumSize(new Dimension(250, 500));
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		JLabel shotLabel = new JLabel("Shot " + shotNumber);
		shotLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
		shotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(shotLabel);
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		JLabel videoLabel = new JLabel("Video Features");
		videoLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));		
		videoLabel.setMaximumSize(new Dimension(200, 30));
		videoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(videoLabel);
		
		JLabel distLabel = new JLabel("Disturbance: " + dist);
		distLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		distLabel.setMaximumSize(new Dimension(200, 30));
		distLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(distLabel);
		
		JLabel lumiLabel = new JLabel("Luminance: 100" + lumi);
		lumiLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		lumiLabel.setMaximumSize(new Dimension(200, 30));
		lumiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lumiLabel);
		
		JLabel flamLabel = new JLabel("Flame: 100" + flam);
		flamLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		flamLabel.setMaximumSize(new Dimension(200, 30));
		flamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(flamLabel);
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		JLabel audioLabel = new JLabel("Audio Features");
		audioLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));		
		audioLabel.setMaximumSize(new Dimension(200, 30));
		audioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(audioLabel);
		
		JLabel enrgLabel = new JLabel("Energy: 100" + enrg);
		enrgLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		enrgLabel.setMaximumSize(new Dimension(200, 30));
		enrgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(enrgLabel);
		
		JLabel powrLabel = new JLabel("Power: 100" + powr);
		powrLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		powrLabel.setMaximumSize(new Dimension(200, 30));
		powrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(powrLabel);
		
		JLabel paceLabel = new JLabel("Pace: 100" + pace);
		paceLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		paceLabel.setMaximumSize(new Dimension(200, 30));
		paceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(paceLabel);
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		JLabel classLabel = new JLabel("Shot classified as action.");
		classLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));		
		classLabel.setMaximumSize(new Dimension(200, 30));
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(classLabel);
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		JButton viewFramesBtn = new JButton("View Frames");
		viewFramesBtn.setMaximumSize(new Dimension(200, 30));
		viewFramesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(viewFramesBtn);
		
		JButton hearAudioBtn = new JButton("Listen to Audio");
		hearAudioBtn.setMaximumSize(new Dimension(200, 30));
		hearAudioBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(hearAudioBtn);
		
	}

	
}
