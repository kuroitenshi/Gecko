package view;


import java.awt.event.ActionListener;

import javax.swing.JButton;

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
	public void setClassifyButtonActionListener(ActionListener l)
	{
		btnProcess.addActionListener(l);
	}

	public String getFilepath() 
	{
		return filepath;
	}

	public void setFilepath(String filepath) 
	{
		this.filepath = filepath;
	}
}
