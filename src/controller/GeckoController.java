package controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import view.FileFinderFrame;
import view.ProgressFrame;
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

public class GeckoController implements ActionListener, PropertyChangeListener {
	
	
	FileFinderFrame fileFinderFrame;
	private JProgressBar progressBar;
	private Task task;
	private JFrame frame;
	
	public GeckoController() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	initialize();
	        }
	    });
	}

	public void initialize() {
    	fileFinderFrame = new FileFinderFrame();
    	fileFinderFrame.go_button.addActionListener(this);
    	makeProgressFrame();
	}
	
	public void makeProgressFrame() {
		frame = new JFrame("Gecko Movie Classifier");
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setPreferredSize(new Dimension(300, 75));		

		progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(25, 25, 250, 25);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		fileFinderFrame.setVisible(false);
		frame.setVisible(true);
		task = new Task(fileFinderFrame.getFilepath(), fileFinderFrame.getMode());
        task.addPropertyChangeListener(this);
        task.execute();
        System.out.println("hello");
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if ("progress" == evt.getPropertyName()) 
		{
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            if (progressBar.getValue() == 100)
            	frame.setVisible(false);
        } 		
	}
}

class Task extends SwingWorker
{
	FileFinderFrame fileFinder;
	
	FrameExtraction extractionModel = new FrameExtraction();
	String mode = "";
	
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
	
	String directory;
	String filepath;
	
	public Task(String filepath, String mode) {
		this.filepath = filepath;
		this.mode = mode;
	}

	public Void doInBackground() 
	{
		inTime = new StringBuilder();
		ArrayList<String> Clips = new ArrayList<String>();	
		System.out.println("Process thread started!");		
				
		File movieFileChosen = new File(filepath);
		ArrayList<Double> shotVisualDisturbance = new ArrayList<Double>();
		ArrayList<Double> shotLuminance = new ArrayList<Double>();
		ArrayList<Double> shotFlamePercentage = new ArrayList<Double>();
		ArrayList<Shot> shotList = new ArrayList<Shot>();
		String movieName = "";
		System.out.println("MODE: " +mode);
		
		if(mode.equals("MOVIE"))
		{
			startTimeFrameEx = System.currentTimeMillis();
			extractionModel.setMovieFile(movieFileChosen);
			extractionModel.extractImages();
	
			endTimeFrameEx = System.currentTimeMillis();
			totTimeFrameEx = ((endTimeFrameEx/1000) - (startTimeFrameEx/1000));
			setProgress(15);
			
			startTimeVisSeg = System.currentTimeMillis();
	
			System.out.println("SHOT SEGMENTATION - START");
			Segmentation movieSegmentation = new Segmentation(extractionModel.getFramesPath(), extractionModel.getParentResultPath());
			movieSegmentation.segmentMovie();
			System.out.println("SHOT SEGMENTATION - END");
					
			endTimeVisSeg = System.currentTimeMillis();
			totTimeVisSeg = ((endTimeVisSeg/1000) - (startTimeVisSeg/1000));
			setProgress(30);
	
			System.out.println("VISUAL FEATURE VALUE - EXTRACTION");
			StringBuilder visualDisturbanceValues = new StringBuilder();
			StringBuilder visualLuminanceValues = new StringBuilder();
			StringBuilder visualFlamePercentageValues = new StringBuilder();
	
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
				} 
				else if (OS.indexOf("mac") >= 0)
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
			else if (OS.indexOf("mac") >= 0) 
			{
				resultVisualDisturbanceFile = new File(extractionModel.getParentResultPath().concat("/Visual Data/Visual Disturbance Values.txt"));
				resultLuminanceFile = new File(extractionModel.getParentResultPath().concat("/Visual Data/Luminance Values.txt"));
				resultFlamePercentageFile = new File(extractionModel.getParentResultPath().concat("/Visual Data/Flame Percentage Values.txt"));
			}
			// END ADDED
	
			FileWriter resultVisualDisturbanceWriter = null;
			FileWriter resultLuminanceWriter = null;
			FileWriter resultFlamePercentageWriter = null;
	
			try 
			{
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
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			};
					
			endTimeVisEx = System.currentTimeMillis();
			totTimeVisEx = ((endTimeVisEx/1000) - (startTimeVisEx/1000));
					
			setProgress(45);
	
			//progressFrame.dispose();
					
			startTimeAudialEx = System.currentTimeMillis();
	
			AudioExtraction audioExtractor = new AudioExtraction(
							extractionModel.getParentResultPath());
			audioExtractor.setFile(movieFileChosen);
			audioExtractor.extractAudio();
					
			endTimeAudialEx = System.currentTimeMillis();
			totTimeAudialEx = ((endTimeAudialEx/1000) - (startTimeAudialEx/1000));
	
			setProgress(60);
	
			startTimeAudialSeg = System.currentTimeMillis();
					
			AudialSegmentation audialSeg = new AudialSegmentation(
							extractionModel.getAudialDataPath());
			audialSeg.setFile(extractionModel.getParentResultPath());
	
			try
			{
				audialSeg.segmentAudio();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
					
			endTimeAudialSeg = System.currentTimeMillis();
			totTimeAudialSeg = ((endTimeAudialSeg/1000) - (startTimeAudialSeg/1000));
					
			setProgress(75);
	
			startTimeAudialFeat = System.currentTimeMillis();
	
			AudialFeatures audialFeatures = new AudialFeatures(
							extractionModel.getAudialSegPath(), extractionModel
									.getAudialDataPath());
			audialFeatures.setFile(extractionModel.getParentResultPath());
			audialFeatures.producePraatScripts();
			
			try 
			{
				audialFeatures.setAudioFeatures(shotList);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
					
			endTimeAudialFeat = System.currentTimeMillis();
			totTimeAudialFeat =  ((endTimeAudialFeat/1000) - (startTimeAudialFeat/1000));
					
			setProgress(90);
			startTimeClass = System.currentTimeMillis();
					
			movieName = movieFileChosen.getName().substring(0, movieFileChosen.getName().lastIndexOf('.'));
		}
		else if(mode.equals("PATH"))
		{
			extractionModel.setMovieFile(movieFileChosen);
			System.out.println("CLASSIFICATION: PATH");
			shotList = new ArrayList<Shot>();
			ArrayList<Double> audialEnergy = new ArrayList<Double>();
			ArrayList<Double> audialPower = new ArrayList<Double>();
			ArrayList<Double> audialPace = new ArrayList<Double>();
			
			System.out.println("SETTING VALUES: " + mode);
			System.out.println("FILE: " + extractionModel.getMovieFile().toString());
			
			Scanner fileIn;
			int counter = 1;
			try {
				// VISUAL DISTURBANCE 
				fileIn = new Scanner(new File(filepath + "\\Visual Data\\Visual Disturbance Values.txt"));
				while(fileIn.hasNext())
				{
					String string = fileIn.next(); // Reads one word from the file
					if(counter % 4 == 0)
					{
						System.out.println("RETRIEVING VISUAL DISTURBANCE");
						shotVisualDisturbance.add(Double.parseDouble(string));
					}
					counter++;
				}		
				
				// LUMINANCE
				counter = 1;
				fileIn = new Scanner(new File(filepath + "\\Visual Data\\Luminance Values.txt"));
				while(fileIn.hasNext())
				{
					String string = fileIn.next(); // Reads one word from the file
					if(counter % 4 == 0)
					{
						System.out.println("RETRIEVING LUMINANCE");
						shotLuminance.add(Double.parseDouble(string));
					}
					counter++;
				}	
				
				// FLAME PERCENTAGE
				counter = 1;
				fileIn = new Scanner(new File(filepath + "\\Visual Data\\Flame Percentage Values.txt"));
				while(fileIn.hasNext())
				{
					String string = fileIn.next(); // Reads one word from the file
					if(counter % 4 == 0)
					{
						System.out.println("RETRIEVING FLAME PERCENTAGE");
						shotFlamePercentage.add(Double.parseDouble(string));
					}
					counter++;
				}	
								
				// AUDIO ENERGY 
				counter = 1;
				fileIn = new Scanner(new File(filepath + "\\Audial Data\\AudialEnergy.txt"));
				while(fileIn.hasNext())
				{
					String string = fileIn.next(); // Reads one word from the file
					if(counter % 3 == 0)
					{
						System.out.println("RETRIEVING Audial Energy");
						audialEnergy.add(Double.parseDouble(string));
						fileIn.next();
						fileIn.next();
					}
					counter++;
				}	
				
				// AUDIO POWER 
				counter = 1;
				fileIn = new Scanner(new File(filepath + "\\Audial Data\\AudialPower.txt"));
				while(fileIn.hasNext())
				{
					String string = fileIn.next(); // Reads one word from the file
					if(counter % 3 == 0)
					{
						System.out.println("RETRIEVING Audial Power");
						audialPower.add(Double.parseDouble(string));
						fileIn.next();
					}
					counter++;
				}	
				
				// AUDIO PACE
				fileIn = new Scanner(new File(filepath + "\\Audial Data\\AudialPace.txt"));
				while(fileIn.hasNextLine())
				{
					String string = fileIn.nextLine(); // Reads one word from the file
					System.out.println("RETRIEVING Audial Pace");
					audialPace.add(Double.parseDouble(string));
				}	
				
//				C:\FFOutput\Horror Clips\Training Data\1
				//Store all features to shotList
				int numberOfShots = shotLuminance.size();
				for(int i = 1; i<=numberOfShots;i++)
				{
					Shot tempShot = new Shot(i);
//					Shot tempShot = new Shot(i, filepath +"\\Visual Data\\ShotRange.txt", filepath+"\\Frames");
					tempShot.setVisualDisturbanceValue(shotVisualDisturbance.get(i-1));
					tempShot.setLuminanceValue(shotLuminance.get(i-1));
					tempShot.setFlamePercentageValue(shotFlamePercentage.get(i-1));
					tempShot.setAudioEnergyValue(audialEnergy.get(i-1));
					tempShot.setAudioPowerValue(audialPower.get(i-1));
					tempShot.setAudioPaceValue(audialPace.get(i-1));
					tempShot.setShotRangePath(filepath+"\\Visual Data\\ShotRange.txt");
					tempShot.setFramePath(filepath+"\\Frames");
					tempShot.getFrameRange();
					tempShot.retrieveFrames();
					shotList.add(tempShot);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}		
			
			movieName = movieFileChosen.getName().toString();
		}
	
		GenreClassifier movieGenreClassifier = null;
		GenreClassifierVisual movieGenreClassifierVisual = null;
		String directory = "";		
		String folderName = "";
		
		if(mode.equals("MOVIE"))
		{
			movieGenreClassifier = new GenreClassifier(shotList, extractionModel.getParentResultPath());
			movieGenreClassifierVisual = new GenreClassifierVisual(shotList, extractionModel.getParentResultPath());
			directory = movieFileChosen.getParent().replace("\\", "/");
			folderName = movieFileChosen.getName().substring(0, movieFileChosen.getName().lastIndexOf('.'));
			directory = directory+folderName;
		} 
		else if(mode.equals("PATH"))
		{
			System.out.println("CLASSIFYING");
			movieGenreClassifier = new GenreClassifier(shotList, filepath);
			movieGenreClassifierVisual = new GenreClassifierVisual(shotList, filepath);
			directory = filepath;
		}
		
		movieGenreClassifier.classifyMovieGenre();
		movieGenreClassifierVisual.classifyMovieGenre();
				
		System.out.println("CLASSIFICATION DONE");
	
		ResultPercentages results = new ResultPercentages();			
		results.action = movieGenreClassifier.ACTION_count;
		results.comedy = movieGenreClassifier.COMEDY_count;
		results.drama = movieGenreClassifier.DRAMA_count;
		results.horror = movieGenreClassifier.HORROR_count;
				
		results.actionframes = movieGenreClassifier.ACTION_frames;
		results.comedyframes = movieGenreClassifier.COMEDY_frames;
		results.dramaframes = movieGenreClassifier.DRAMA_frames;
		results.horrorframes = movieGenreClassifier.HORROR_frames;
				
		setProgress(100);

		new ResultsFrame(movieName, shotList, results, directory);
				
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
				
		try 
		{
			benchMarkFW = new FileWriter( benchMarkFile.getAbsoluteFile());
			BufferedWriter resultGenreFileBufferedWriter = new BufferedWriter(benchMarkFW);
			resultGenreFileBufferedWriter.write(inTime.toString());
			resultGenreFileBufferedWriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
				
		return null;		
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
