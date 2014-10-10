package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import view.GUI;
import model.FrameExtraction;
import model.Segmentation;

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
				
				JFileChooser fileChooser = new JFileChooser();	
				fileChooser.setMultiSelectionEnabled(false);
				int option = fileChooser.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION)
				{
					File movieFileChosen = fileChooser.getSelectedFile();
					
					FrameExtraction frameExtractor = new FrameExtraction();
					frameExtractor.setMovieFile(movieFileChosen);
					frameExtractor.extractImages();
					
					System.out.println(frameExtractor.getParentResultPath());
					System.out.println(frameExtractor.getFramesPath());
					Segmentation movieSegmentation = new Segmentation(frameExtractor.getFramesPath(), frameExtractor.getParentResultPath());
					movieSegmentation.segmentMovie();
				}
				
						
			}
		});
	}
}
