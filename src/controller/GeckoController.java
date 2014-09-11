package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.GUI;
import model.FrameExtraction;

public class GeckoController 
{
	FrameExtraction extractionModel;
	GUI geckoView;
	
	public GeckoController(FrameExtraction extractionModel, GUI geckoView)
	{
		this.extractionModel = extractionModel;
		this.geckoView = geckoView;		
		setActionListeners();
	}
	public void setActionListeners()
	{
		geckoView.setNewButtonActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				geckoView.setLblSomething(extractionModel.helloworld());
				System.out.println(geckoView.getLblSomething().getText());				
			}
		});
	}
}
