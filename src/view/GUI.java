package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GUI
{

	private JButton btnProcess;
	private FileFinderFrame fileFinder;
	private String filepath;
	
	
	public GUI() 
	{
		fileFinder = new FileFinderFrame();
		btnProcess = fileFinder.go_button;
	}
	
	
	
	/*Sample Action Listener for buttons*/
	public void putListener(ActionListener l)
	{
		btnProcess.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	filepath = fileFinder.getFilepath();
		    	fileFinder.dispose();
		    	
		    	btnProcess.doClick();
		    }
		});
	}



	public String getFilepath() {
		return filepath;
	}



	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
