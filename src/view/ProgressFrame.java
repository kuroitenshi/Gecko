package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressFrame extends JFrame {
	
	private JProgressBar progressBar;
	
	public ProgressFrame() {
		super("Processing...");
		setSize(300, 75);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
	}

}
