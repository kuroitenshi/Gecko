package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressFrame extends JFrame {
	
	public JProgressBar progressBar;
	
	public ProgressFrame() {
		
		super("Processing...");
		getContentPane().setLayout(null);
		getContentPane().setPreferredSize(new Dimension(300, 75));		
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(25, 25, 250, 25);
		progressBar.setValue(80);
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar);


		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		
		
	}

}
