package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import view.FileFinderFrame;
import view.ResultsFrame;
import model.AudialFeatures;
import model.AudialSegmentation;
import model.AudioExtraction;
import model.FrameExtraction;
import model.GenreClassifier;
import model.GenreClassifierVisual;
import model.Segmentation;
import model.Shot;
import model.Objects.ResultPercentages;

public class GeckoController 
{
	FrameExtraction extractionModel = new FrameExtraction();
	FileFinderFrame fileFinder;
	
	
	/* Benchmark Purpose */	
	long startTimeFrameEx;
	long endTimeFrameEx;
	long totTimeFrameEx;
	long startTimeVisSeg;
	long endTimeVisSeg;
	long totTimeVisSeg;
	long startTimeVisEx;
	long endTimeVisEx;
	long totTimeVisEx;
	long startTimeAudialEx;
	long endTimeAudialEx;
	long totTimeAudialEx;
	long startTimeAudialSeg;
	long endTimeAudialSeg;
	long totTimeAudialSeg;
	long startTimeAudialFeat;
	long endTimeAudialFeat;
	long totTimeAudialFeat;
	long startTimeClass;
	long endTimeClass;
	long totTimeClass;
	
	StringBuilder inTime;

	public GeckoController() {
		inTime = new StringBuilder();
		fileFinder = new FileFinderFrame();

		fileFinder.go_button.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent arg0) 
			{		
				String filepath = fileFinder.getFilepath();
				fileFinder.dispose();
					
				File movieFileChosen = new File(filepath);
				ArrayList<Double> shotVisualDisturbance = new ArrayList<Double>();
				ArrayList<Double> shotLuminance = new ArrayList<Double>();
				ArrayList<Double> shotFlamePercentage = new ArrayList<Double>();
				
				startTimeFrameEx = System.currentTimeMillis();

				extractionModel.setMovieFile(movieFileChosen);
				extractionModel.extractImages();
				
				endTimeFrameEx = System.currentTimeMillis();
				totTimeFrameEx = ((endTimeFrameEx/1000) - (startTimeFrameEx/1000));
				
				startTimeVisSeg = System.currentTimeMillis();

				System.out.println("SHOT SEGMENTATION - START");
				Segmentation movieSegmentation = new Segmentation(
						extractionModel.getFramesPath(), extractionModel
								.getParentResultPath());
				movieSegmentation.segmentMovie();
				System.out.println("SHOT SEGMENTATION - END");
				
				endTimeVisSeg = System.currentTimeMillis();
				totTimeVisSeg = ((endTimeVisSeg/1000) - (startTimeVisSeg/1000));

				System.out.println("VISUAL FEATURE VALUE - EXTRACTION");
				StringBuilder visualDisturbanceValues = new StringBuilder();
				StringBuilder visualLuminanceValues = new StringBuilder();
				StringBuilder visualFlamePercentageValues = new StringBuilder();
				ArrayList<Shot> shotList = new ArrayList<Shot>();

				startTimeVisEx = System.currentTimeMillis();
				
				for (int i = 1; i <= movieSegmentation.getShotRangeNumber()+1; i++) 
				{
					String OS = System.getProperty("os.name").toLowerCase();

					Shot shot = null;
					if (OS.indexOf("win") >= 0) 
					{
						shot = new Shot(i, extractionModel.getVisualDataPath()
								+ "\\ShotRange.txt", extractionModel
								.getFramesPath());
					} else if (OS.indexOf("mac") >= 0)
					{
						shot = new Shot(i, extractionModel.getVisualDataPath()
								+ "/ShotRange.txt", extractionModel
								.getFramesPath());
					}

					//Extract Visual Feature Values
					System.out.println("Extracting Visual Features for shot " + i);
					shot.extractVisualFeatures();
					// SET VISUAL DISTURBANCE VALUE
					shotVisualDisturbance.add(shot.getVisualDisturbanceValue());

					// SET LUMINANCE VALUE			
					shotLuminance.add(shot.getLuminanceValue());

					// SET FLAME PERCENTAGE VALUE
					shotFlamePercentage.add(shot.getFlamePercentageValue());

					visualDisturbanceValues = visualDisturbanceValues
							.append("SHOT " + i + " VALUE "
									+ shotVisualDisturbance.get(i - 1)
									+ System.lineSeparator());
					visualLuminanceValues = visualLuminanceValues
							.append("SHOT " + i + " VALUE "
									+ shotLuminance.get(i - 1)
									+ System.lineSeparator());
					visualFlamePercentageValues = visualFlamePercentageValues
							.append("SHOT " + i + " VALUE "
									+ shotFlamePercentage.get(i - 1)
									+ System.lineSeparator());

					shotList.add(shot);
				}

				// ADDED
				String OS = System.getProperty("os.name").toLowerCase();

				File resultVisualDisturbanceFile = null;
				File resultLuminanceFile = null;
				File resultFlamePercentageFile = null;
				
				if (OS.indexOf("win") >= 0){
					resultVisualDisturbanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Visual Disturbance Values.txt"));
					resultLuminanceFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Luminance Values.txt"));
					resultFlamePercentageFile = new File(extractionModel.getParentResultPath().concat("\\Visual Data\\Flame Percentage Values.txt"));
				}
				else if (OS.indexOf("mac") >= 0) {
					resultVisualDisturbanceFile = new File(extractionModel.getParentResultPath().concat("/Visual Data/Visual Disturbance Values.txt"));
					resultLuminanceFile = new File(extractionModel.getParentResultPath().concat("/Visual Data/Luminance Values.txt"));
					resultFlamePercentageFile = new File(extractionModel.getParentResultPath().concat("/Visual Data/Flame Percentage Values.txt"));
				}
				// END ADDED

				FileWriter resultVisualDisturbanceWriter = null;
				FileWriter resultLuminanceWriter = null;
				FileWriter resultFlamePercentageWriter = null;

				try {
					resultVisualDisturbanceWriter = new FileWriter(
							resultVisualDisturbanceFile.getAbsoluteFile());
					resultLuminanceWriter = new FileWriter(resultLuminanceFile
							.getAbsoluteFile());
					resultFlamePercentageWriter = new FileWriter(
							resultFlamePercentageFile.getAbsoluteFile());

					BufferedWriter visualDisturbanceBufferedWriter = new BufferedWriter(
							resultVisualDisturbanceWriter);
					BufferedWriter luminanceBufferedWriter = new BufferedWriter(
							resultLuminanceWriter);
					BufferedWriter flamePercentageBufferedWriter = new BufferedWriter(
							resultFlamePercentageWriter);

					visualDisturbanceBufferedWriter
							.write(visualDisturbanceValues.toString());
					visualDisturbanceBufferedWriter.close();

					luminanceBufferedWriter.write(visualLuminanceValues
							.toString());
					luminanceBufferedWriter.close();

					flamePercentageBufferedWriter
							.write(visualFlamePercentageValues.toString());
					flamePercentageBufferedWriter.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				};
				
				endTimeVisEx = System.currentTimeMillis();
				totTimeVisEx = ((endTimeVisEx/1000) - (startTimeVisEx/1000));
				
				//progressFrame.dispose();
				
				startTimeAudialEx = System.currentTimeMillis();

				AudioExtraction audioExtractor = new AudioExtraction(
						extractionModel.getParentResultPath());
				audioExtractor.setFile(movieFileChosen);
				audioExtractor.extractAudio();
				
				endTimeAudialEx = System.currentTimeMillis();
				totTimeAudialEx = ((endTimeAudialEx/1000) - (startTimeAudialEx/1000));

				startTimeAudialSeg = System.currentTimeMillis();
				
				AudialSegmentation audialSeg = new AudialSegmentation(
						extractionModel.getAudialDataPath());
				audialSeg.setFile(extractionModel.getParentResultPath());

				try
				{
					audialSeg.segmentAudio();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				endTimeAudialSeg = System.currentTimeMillis();
				totTimeAudialSeg = ((endTimeAudialSeg/1000) - (startTimeAudialSeg/1000));
				
				startTimeAudialFeat = System.currentTimeMillis();

				AudialFeatures audialFeatures = new AudialFeatures(
						extractionModel.getAudialSegPath(), extractionModel
								.getAudialDataPath());
				audialFeatures.setFile(extractionModel.getParentResultPath());
				audialFeatures.producePraatScripts();
				try 
				{
					audialFeatures.setAudioFeatures(shotList);
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				endTimeAudialFeat = System.currentTimeMillis();
				totTimeAudialFeat =  ((endTimeAudialFeat/1000) - (startTimeAudialFeat/1000));
				
				startTimeClass = System.currentTimeMillis();
				
				String movieName = movieFileChosen.getName().substring(0, movieFileChosen.getName().lastIndexOf('.'));
				
				GenreClassifier movieGenreClassifier = new GenreClassifier(shotList, extractionModel.getParentResultPath());
				GenreClassifierVisual movieGenreClassifierVisual = new GenreClassifierVisual(shotList, extractionModel.getParentResultPath());
				movieGenreClassifier.classifyMovieGenre();
				movieGenreClassifierVisual.classifyMovieGenre();
				
				ResultPercentages results = new ResultPercentages();			
				results.action = movieGenreClassifier.ACTION_count;
				results.comedy = movieGenreClassifier.COMEDY_count;
				results.drama = movieGenreClassifier.DRAMA_count;
				results.horror = movieGenreClassifier.HORROR_count;
				
				results.actionframes = movieGenreClassifier.ACTION_frames;
				results.comedyframes = movieGenreClassifier.COMEDY_frames;
				results.dramaframes = movieGenreClassifier.DRAMA_frames;
				results.horrorframes = movieGenreClassifier.HORROR_frames;
				
				new ResultsFrame(movieName, shotList, results);
				
				endTimeClass = System.currentTimeMillis();
				totTimeClass =  ((endTimeClass/1000) - (startTimeClass/1000));
							
				inTime = inTime.append("Frame Extraction: "+ System.lineSeparator() + timeDivider(totTimeFrameEx) + System.lineSeparator() + System.lineSeparator()
						+ "Frame Segmentation: "+ System.lineSeparator()+ timeDivider(totTimeVisSeg) + System.lineSeparator()+ System.lineSeparator()
						+ "Visual Feature Extraction: "+ System.lineSeparator()+ timeDivider(totTimeVisEx) + System.lineSeparator()+ System.lineSeparator()
						+"Audial Extraction: "+ System.lineSeparator()+ timeDivider(totTimeAudialEx) + System.lineSeparator()+ System.lineSeparator()
						+"Audial Segmentation: "+ System.lineSeparator()+ timeDivider(totTimeAudialSeg) + System.lineSeparator()+ System.lineSeparator()
						+"Audial Feature Extraction: "+ System.lineSeparator()+ timeDivider(totTimeAudialFeat) + System.lineSeparator()+ System.lineSeparator()
						+"Genre Classification: "+ System.lineSeparator()+ timeDivider(totTimeClass) + System.lineSeparator());
				
				File benchMarkFile = new File (extractionModel
						.getParentResultPath().concat("\\Runtime Values.txt"));
		
				FileWriter benchMarkFW = null;
				
				try {
					benchMarkFW = new FileWriter( benchMarkFile.getAbsoluteFile());
					BufferedWriter resultGenreFileBufferedWriter = new BufferedWriter(benchMarkFW);
					resultGenreFileBufferedWriter.write(inTime.toString());
					resultGenreFileBufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				
			}			
		});
	}
	
	String timeDivider(long dur)
	{
		String lines = "";
		int hours; int minutes; int excess; int seconds;
		hours = (int)dur / 3600;
		excess = (int)dur - (hours*3600);
		minutes = excess / 60;
		excess = excess - (minutes*60);
		seconds = excess;
		
		lines = hours+" hours; "+minutes+" minutes; "+seconds+" seconds";
		return lines;
	}
}
