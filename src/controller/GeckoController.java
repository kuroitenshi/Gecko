package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import view.FileFinderFrame;
import view.GUI;
import model.AudialSegmentation;
import model.AudioExtraction;
import model.FrameExtraction;
import model.Segmentation;
import model.Shot;

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
		geckoView.setClassifyButtonActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{	
				
				FileFinderFrame fileFinder = new FileFinderFrame();
				String filepath = fileFinder.getFilepath();
				fileFinder.dispose();
				File movieFileChosen = new File(filepath);
				ArrayList<Double> shotVisualDisturbance = new ArrayList<Double>();
				ArrayList<Double> shotLuminance = new ArrayList<Double>();
			
					
				extractionModel.setMovieFile(movieFileChosen);
				extractionModel.extractImages();
				
				Segmentation movieSegmentation = new Segmentation(extractionModel.getFramesPath(), extractionModel.getParentResultPath());
				movieSegmentation.segmentMovie();
					
				
				System.out.println("VISUAL DISTURBANCE VALUES");
				StringBuilder visualDisturbanceValues = new StringBuilder();
				StringBuilder visualLuminanceValues = new StringBuilder();
				for(int i = 1; i <= movieSegmentation.getShotRangeNumber(); i++)						
				{						
					
					Shot shot = new Shot(i, extractionModel.getVisualDataPath() + "\\ShotRange.txt", extractionModel.getFramesPath());
					shotVisualDisturbance.add(shot.computeVisualDisturbance());
					shotLuminance.add(shot.computeLuminance());
					visualDisturbanceValues = visualDisturbanceValues.append("SHOT " + i + " VALUE " + shotVisualDisturbance.get(i-1) + System.lineSeparator());
					visualLuminanceValues = visualLuminanceValues.append("SHOT " + i + " VALUE " + shotLuminance.get(i-1) + System.lineSeparator());						
				}
					
				File resultVisualDisturbanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Visual Disturbance Values.txt"));
				File resultLuminanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Luminance Values.txt"));
			    
				FileWriter resultVisualDisturbanceWriter = null;
			    FileWriter resultLuminanceWriter = null;
			    
			    try 
				{								
					resultVisualDisturbanceWriter = new FileWriter(resultVisualDisturbanceFile.getAbsoluteFile());
					resultLuminanceWriter = new FileWriter(resultLuminanceFile.getAbsoluteFile());
					
				   	BufferedWriter visualDisturbanceBufferedWriter = new BufferedWriter(resultVisualDisturbanceWriter);
					BufferedWriter luminanceBufferedWriter = new BufferedWriter(resultLuminanceWriter);				    	
						
					visualDisturbanceBufferedWriter.write(visualDisturbanceValues.toString());
			    	visualDisturbanceBufferedWriter.close();
			    	

				   	luminanceBufferedWriter.write(visualLuminanceValues.toString());
					luminanceBufferedWriter.close();
					
				} 
			    catch (IOException e) 
				{
					e.printStackTrace();
				};				
					
				AudioExtraction audioExtractor = new AudioExtraction(extractionModel.getParentResultPath());
				audioExtractor.setFile(movieFileChosen);
				audioExtractor.extractAudio();
					
				AudialSegmentation audialSeg = new AudialSegmentation(extractionModel.getAudialDataPath());
				audialSeg.setFile(extractionModel.getParentResultPath());
	
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
