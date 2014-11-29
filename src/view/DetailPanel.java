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

import model.Shot;


public class DetailPanel extends JPanel {
	
	int shotNumber = 1;
	double dist;
	double lumi;
	double flam;
	double enrg;
	double powr;
	double pace;

	public DetailPanel(Shot shot) {
		
		dist = shot.getVisualDisturbanceValue();
		lumi = shot.getLuminanceValue();
		flam = shot.getFlamePercentageValue();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(250, 480));
		
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
		
		JLabel distLabel = new JLabel("Disturbance: " + String.format( "%.5f", dist));
		distLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		distLabel.setMaximumSize(new Dimension(200, 30));
		distLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(distLabel);
		
		JLabel lumiLabel = new JLabel("Luminance: " + String.format( "%.5f", lumi));
		lumiLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		lumiLabel.setMaximumSize(new Dimension(200, 30));
		lumiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lumiLabel);
		
		JLabel flamLabel = new JLabel("Flame: " + String.format( "%.5f", flam));
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
		
		JLabel enrgLabel = new JLabel("Energy: " + String.format( "%.5f", enrg));
		enrgLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		enrgLabel.setMaximumSize(new Dimension(200, 30));
		enrgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(enrgLabel);
		
		JLabel powrLabel = new JLabel("Power: " + String.format( "%.5f", powr));
		powrLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));		
		powrLabel.setMaximumSize(new Dimension(200, 30));
		powrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(powrLabel);
		
		JLabel paceLabel = new JLabel("Pace: " + String.format( "%.5f", pace));
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
