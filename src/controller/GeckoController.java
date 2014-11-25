package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import view.FileFinderFrame;
import view.ProgressFrame;
import model.AudialFeatures;
import model.AudialSegmentation;
import model.AudioExtraction;
import model.FrameExtraction;
import model.Segmentation;
import model.Shot;

public class GeckoController 
{
	FrameExtraction extractionModel = new FrameExtraction();
	FileFinderFrame fileFinder;
	
	public GeckoController()
	{
		
		fileFinder = new FileFinderFrame();
		
		
		fileFinder.go_button.addActionListener(new ActionListener() 
		{
			
			public void actionPerformed(ActionEvent arg0) 
			{	
				
				String filepath = fileFinder.getFilepath();
				fileFinder.dispose();
				
				ProgressFrame progressFrame = new ProgressFrame();
				
				//FOR THRESH COMPUTATIONS ONLY
//				System.out.println("VISUAL FEATURE VALUES");
//				StringBuilder visualDisturbanceValues = new StringBuilder();
//				StringBuilder visualLuminanceValues = new StringBuilder();
//				StringBuilder visualFlamePercentageValues = new StringBuilder();
//				for(int i = 1; i <= movieSegmentation.getShotRangeNumber(); i++)						
//				{						
//					String OS = System.getProperty("os.name").toLowerCase();
//
//					Shot shot = null;					
//					if (OS.indexOf("win") >= 0){
//						shot = new Shot(i, extractionModel.getVisualDataPath() + "\\ShotRange.txt", extractionModel.getFramesPath());
//					}
//					else if (OS.indexOf("mac") >= 0) {
//						shot = new Shot(i, extractionModel.getVisualDataPath() + "/ShotRange.txt", extractionModel.getFramesPath());
//					}
//					shotVisualDisturbance.add(shot.computeVisualDisturbance());
//					shotLuminance.add(shot.computeLuminance());
//					shotFlamePercentage.add(shot.computeFlamePercentage());
//					visualDisturbanceValues = visualDisturbanceValues.append("SHOT " + i + " VALUE " + shotVisualDisturbance.get(i-1) + System.lineSeparator());
//					visualLuminanceValues = visualLuminanceValues.append("SHOT " + i + " VALUE " + shotLuminance.get(i-1) + System.lineSeparator());						
//					visualFlamePercentageValues = visualFlamePercentageValues.append("SHOT " + i + " VALUE " + shotFlamePercentage.get(i-1) + System.lineSeparator());
//				}
//					
//				File resultVisualDisturbanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Visual Disturbance Values.txt"));
//				File resultLuminanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Luminance Values.txt"));
//				File resultFlamePercentageFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Flame Percentage Valuess.txt"));
//			    
//				FileWriter resultVisualDisturbanceWriter = null;
//			    FileWriter resultLuminanceWriter = null;
//			    FileWriter resultFlamePercentageWriter = null;
//			    
//			    try 
//				{								
//					resultVisualDisturbanceWriter = new FileWriter(resultVisualDisturbanceFile.getAbsoluteFile());
//					resultLuminanceWriter = new FileWriter(resultLuminanceFile.getAbsoluteFile());
//					resultFlamePercentageWriter = new FileWriter(resultFlamePercentageFile.getAbsoluteFile());
//					
//				   	BufferedWriter visualDisturbanceBufferedWriter = new BufferedWriter(resultVisualDisturbanceWriter);
//					BufferedWriter luminanceBufferedWriter = new BufferedWriter(resultLuminanceWriter);				
//					BufferedWriter flamePercentageBufferedWriter = new BufferedWriter(resultFlamePercentageWriter);		
//						
//					visualDisturbanceBufferedWriter.write(visualDisturbanceValues.toString());
//			    	visualDisturbanceBufferedWriter.close();
//			    	
//				   	luminanceBufferedWriter.write(visualLuminanceValues.toString());
//					luminanceBufferedWriter.close();
//					
//					flamePercentageBufferedWriter.write(visualFlamePercentageValues.toString());
//					flamePercentageBufferedWriter.close();
//				} 
//			    catch (IOException e) 
//				{
//					e.printStackTrace();
//				};				
//					
				/*
				ArrayList<String> Clips = new ArrayList<String>();
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 1 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 2 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 3 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 4 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 5 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 6 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 7 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 8 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 9 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 10 (Hercules).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 11 (Edge of Tomorrow).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 12 (Edge of Tomorrow).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 13 (Edge of Tomorrow).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 14 (Edge of Tomorrow).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 15 (Edge of Tomorrow).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 16 (GI Joe Retaliation).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 17 (GI Joe Retaliation).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 19 (Ender's Game).avi");
				Clips.add("C:\\Users\\Pheebz\\Desktop\\ACTION Clips\\ACTION 20 (Ender's Game).avi");
				
				
				
				
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 1 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 2 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 3 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 4 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 5 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 6 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 7 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 8 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 9 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 10 (Accepted).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 11 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 12 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 13 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 14 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 15 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 16 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 17 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 18 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 19 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 20 (Superbad).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 21 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 22 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 23 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 24 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 25 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 26 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 27 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 28 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 29 (22 Jump Street).avi");
				comedyClips.add("C:\\Users\\Pheebz\\Desktop\\COMEDY Clips\\Comedy 30 (22 Jump Street).avi");
				
				for(int x=0; x < Clips.size(); x++)
				{*/

					File movieFileChosen = new File(filepath);
					ArrayList<Double> shotVisualDisturbance = new ArrayList<Double>();
					ArrayList<Double> shotLuminance = new ArrayList<Double>();
					ArrayList<Double> shotFlamePercentage = new ArrayList<Double>();
				
						
					extractionModel.setMovieFile(movieFileChosen);
					extractionModel.extractImages();
					
					Segmentation movieSegmentation = new Segmentation(extractionModel.getFramesPath(), extractionModel.getParentResultPath());
					movieSegmentation.segmentMovie();
						
					
					System.out.println("VISUAL FEATURE VALUES");
					StringBuilder visualDisturbanceValues = new StringBuilder();
					StringBuilder visualLuminanceValues = new StringBuilder();
					StringBuilder visualFlamePercentageValues = new StringBuilder();
					ArrayList<Shot> shotList = new ArrayList<Shot>();
					
					for(int i = 1; i <= movieSegmentation.getShotRangeNumber(); i++)						
					{						
						String OS = System.getProperty("os.name").toLowerCase();

						Shot shot = null;					
						if (OS.indexOf("win") >= 0){
							shot = new Shot(i, extractionModel.getVisualDataPath() + "\\ShotRange.txt", extractionModel.getFramesPath());
						}
						else if (OS.indexOf("mac") >= 0) {
							shot = new Shot(i, extractionModel.getVisualDataPath() + "/ShotRange.txt", extractionModel.getFramesPath());
						}
						
						//SET VISUAL DISTURBANCE VALUE
						shot.setVisualDisturbanceValue(shot.computeVisualDisturbance());
						shotVisualDisturbance.add(shot.getVisualDisturbanceValue());
						
						//SET LUMINANCE VALUE3
						shot.setLuminanceValue(shot.computeLuminance());
						shotLuminance.add(shot.getLuminanceValue());
						
						//SET FLAME PERCENTAGE VALUE
						shot.setFlamePercentageValue(shot.computeFlamePercentage());
						shotFlamePercentage.add(shot.getFlamePercentageValue());
						
						visualDisturbanceValues = visualDisturbanceValues.append("SHOT " + i + " VALUE " + shotVisualDisturbance.get(i-1) + System.lineSeparator());
						visualLuminanceValues = visualLuminanceValues.append("SHOT " + i + " VALUE " + shotLuminance.get(i-1) + System.lineSeparator());						
						visualFlamePercentageValues = visualFlamePercentageValues.append("SHOT " + i + " VALUE " + shotFlamePercentage.get(i-1) + System.lineSeparator());
						
						shotList.add(shot);
					}
						
					File resultVisualDisturbanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Visual Disturbance Values.txt"));
					File resultLuminanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Luminance Values.txt"));
					File resultFlamePercentageFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Flame Percentage Valuess.txt"));
				    
					FileWriter resultVisualDisturbanceWriter = null;
				    FileWriter resultLuminanceWriter = null;
				    FileWriter resultFlamePercentageWriter = null;
				    
				    try 
					{								
						resultVisualDisturbanceWriter = new FileWriter(resultVisualDisturbanceFile.getAbsoluteFile());
						resultLuminanceWriter = new FileWriter(resultLuminanceFile.getAbsoluteFile());
						resultFlamePercentageWriter = new FileWriter(resultFlamePercentageFile.getAbsoluteFile());
						
					   	BufferedWriter visualDisturbanceBufferedWriter = new BufferedWriter(resultVisualDisturbanceWriter);
						BufferedWriter luminanceBufferedWriter = new BufferedWriter(resultLuminanceWriter);				
						BufferedWriter flamePercentageBufferedWriter = new BufferedWriter(resultFlamePercentageWriter);		
							
						visualDisturbanceBufferedWriter.write(visualDisturbanceValues.toString());
				    	visualDisturbanceBufferedWriter.close();
				    	
					   	luminanceBufferedWriter.write(visualLuminanceValues.toString());
						luminanceBufferedWriter.close();
						
						flamePercentageBufferedWriter.write(visualFlamePercentageValues.toString());
						flamePercentageBufferedWriter.close();
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
						
					AudialFeatures audialFeatures = new AudialFeatures(extractionModel.getAudialSegPath(), extractionModel.getAudialDataPath());
					audialFeatures.setFile(extractionModel.getParentResultPath());
					audialFeatures.producePraatScripts();
					try {
						audialFeatures.setAudioFeatures(shotList);
					} catch (IOException e) {						
						e.printStackTrace();
					}
					
				}
			});
		}
}
