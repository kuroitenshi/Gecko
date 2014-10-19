package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import view.GUI;
import model.AudialSegmentation;
import model.AudioExtraction;
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
				
				
					File movieFileChosen = new File(geckoView.getFilepath());
					
					FrameExtraction frameExtractor = new FrameExtraction();
					frameExtractor.setMovieFile(movieFileChosen);
					frameExtractor.extractImages();
					
					System.out.println(frameExtractor.getParentResultPath());
					System.out.println(frameExtractor.getFramesPath());
					Segmentation movieSegmentation = new Segmentation(frameExtractor.getFramesPath(), frameExtractor.getParentResultPath());
					movieSegmentation.segmentMovie();
					
					AudioExtraction audioExtractor = new AudioExtraction(frameExtractor.getParentResultPath());
					audioExtractor.setFile(movieFileChosen);
					audioExtractor.extractAudio();
					
					AudialSegmentation audialSeg = new AudialSegmentation(frameExtractor.getAudialDataPath());
					audialSeg.setFile(frameExtractor.getParentResultPath());
					try 
					{
						audialSeg.segmentAudio();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}


				
				
						
			}
		});
	}
}
