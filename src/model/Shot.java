package model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Shot 
{
	private int key;	
	private String shotRangePath;
	private String framePath;
	private int startingFrame;
	private int endingFrame;
	private double visualDisturbanceValue;
	private double luminanceValue;
	private double flamePercentageValue;
	private double audioPowerValue;
	private double audioEnergyValue;
	private double audioPaceValue;
	private double VD_THRESHOLD;
	private double FLAME_THRESHOLD;
	private ArrayList<Frame> frameList = new ArrayList<Frame>();
	StringBuilder hsbString = new StringBuilder();
	public String classification;
	
	public Shot(int key)
	{
		this.key = key;
	}
	
	public Shot(int key, String shotRangePath, String framePath)
	{
		this.setShotRangePath(shotRangePath);
		this.setFramePath(framePath);
		this.setKey(key);
		
		setStartingFrame(0);
		setEndingFrame(0);
		visualDisturbanceValue = 0;
		luminanceValue = 0;				
		VD_THRESHOLD = 0.35;
		FLAME_THRESHOLD = 0.70; // CHANGE FLAME_THRESHOLD VALUE			
		
		setFlamePercentageValue(0);
		getFrameRange();
		getFramesAndComputeRGB();
		setVisualDisturbanceValue(0);
	}
	
	/*Extract RGB Values for each frame once only*/
	public void extractVisualFeatures()
	{
		//Visual Disturbance
		System.out.println("Extracting Visual Disturbance Values ");
		this.setVisualDisturbanceValue(this.computeVisualDisturbance());		
		//Luminance
		System.out.println("Extracting Luminance Values ");
		this.setLuminanceValue(this.computeLuminance());
		//Flame Percentage
		System.out.println("Extracting Flame Percentages Values ");
		this.setFlamePercentageValue(this.computeFlamePercentage());
	}
	
	/**
	 * Get the range of the frames
	 */
	public void getFrameRange() 
	{		
		try
		{
			FileReader inputFile = new FileReader(getShotRangePath());
		    BufferedReader bufferReader = new BufferedReader(inputFile);
		
		    String line;
		    while ((line = bufferReader.readLine()) != null)
		    {
		    	String[] returnValue = line.split(" ", 7);
		    	if(returnValue[2].equals(""+key))
		    	{
		    		setStartingFrame(Integer.parseInt(returnValue[4]));
		    		setEndingFrame(Integer.parseInt(returnValue[6]));
		    		break;
		    	}
		    }
		    bufferReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();                   
	    }	
	}	
	
	public void getFramesAndComputeRGB() 
	{
		for(int i = getStartingFrame(); i <= getEndingFrame(); i++)
		{
			String OS = System.getProperty("os.name").toLowerCase();

			Frame retrievedFrame = null;
			
			if (OS.indexOf("win") >= 0){
				retrievedFrame = new Frame(i, getFramePath() + "\\" + i + ".jpeg" );
			}
			else if (OS.indexOf("mac") >= 0) {
				retrievedFrame = new Frame(i, getFramePath() + "/" + i + ".jpeg" );
			}
			
			File file = new File(retrievedFrame.getDirectory());
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {					
				
			}			
			retrievedFrame.setRgb(retrievedFrame.computeFrameVisualComponents(image));
						
			getFrameList().add(retrievedFrame);

		}
	}
	
	public void retrieveFrames()
	{
		for(int i = getStartingFrame(); i <= getEndingFrame(); i++)
		{
			String OS = System.getProperty("os.name").toLowerCase();

			Frame retrievedFrame = null;
			
			if (OS.indexOf("win") >= 0){
				retrievedFrame = new Frame(i, getFramePath() + "\\" + i + ".jpeg" );
			}
			else if (OS.indexOf("mac") >= 0) {
				retrievedFrame = new Frame(i, getFramePath() + "/" + i + ".jpeg" );
			}
			
			getFrameList().add(retrievedFrame);

		}
	}
	
	/**
	 * Computes the average Visual Disturbance Value of the Shot
	 * @return AVGVisualDisturbance
	 */
	public double computeVisualDisturbance()
	{
		int counter = 0;
		int temp = 0;
		int divisor = 0;
		int frameInterval = this.getFrameList().get(getFrameList().size()-1).getKey() - this.getFrameList().get(0).getKey();
		
		for(int i = 1; i < this.getFrameList().size(); i+=2)
		{
			for (int j = 0; j < 9; j++)
			{				
				double r1 = this.getFrameList().get(i).getRgb(j).getR();
				double g1 = this.getFrameList().get(i).getRgb(j).getG();
				double b1 = this.getFrameList().get(i).getRgb(j).getB();
												
				double r2 = this.getFrameList().get(i-1).getRgb(j).getR();
				double g2 = this.getFrameList().get(i-1).getRgb(j).getG();
				double b2 = this.getFrameList().get(i-1).getRgb(j).getB();
				
				double distance = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);				
				double max = Math.max(r1, r2) + Math.max(g1, g2) + Math.max(b1, b2);
				double difference = distance/max;
							
				if(difference > VD_THRESHOLD)
				{
					temp++;
				}
			}
			
			//ignore images that don't have any "motion"
			if (temp == 0)
			{
				frameInterval--;
			}

			counter += temp;
			temp = 0;
		}
		
		divisor = 9 * (frameInterval) / 2;
		visualDisturbanceValue = (counter * 1.0 / divisor);
		if(divisor == 0)
		{
			visualDisturbanceValue = 0;
		}
		return visualDisturbanceValue;
	}	
	
	/**
	 * Computes the Average Luminance of the Shot
	 * @return AVGLuminance
	 */
	public double computeLuminance()
	{
		double luminanceAVG = 0.0;
		int counter = 0;
		
		for(int i = getStartingFrame(); i <= getEndingFrame(); i++)
		{			
			if(i == getStartingFrame() || i  == getEndingFrame() || i == Math.floor((getStartingFrame() + getEndingFrame()) /2 ))
			{				
				luminanceAVG += getFrameList().get(counter).getLuminance();
			}
			counter++;
		}
		
		luminanceAVG = luminanceAVG/3;
		
		return luminanceAVG;
	}
	
	/**
	 * Computes the Average Flame Percentage of a Shot
	 * @return AVGFlamePercentage
	 */
	public double computeFlamePercentage()
	{
		double flamePercentage = 0;
		double flamePercentageAVG = 0;
		int counter = 0;
		
		for(int i = getStartingFrame(); i <= getEndingFrame(); i++)
		{
			flamePercentage = getFrameList().get(counter).getFlamePercentage();
			if(flamePercentage >= FLAME_THRESHOLD)
			{
				flamePercentageAVG += flamePercentage;
			}
			counter++;
		}
		
		flamePercentageAVG /= (getEndingFrame() - getStartingFrame() + 1.0); 
		if(getEndingFrame()-getStartingFrame() == 0)
		{
			flamePercentageAVG = 0;
		}
		
		return flamePercentageAVG;
	}
	
	public int getKey() 
	{
		return key;
	}

	public void setKey(int key) 
	{
		this.key = key;
	}
	
	public double getVisualDisturbanceValue() 
	{
		return visualDisturbanceValue;
	}

	public void setVisualDisturbanceValue(double visualDisturbanceValue) 
	{
		this.visualDisturbanceValue = visualDisturbanceValue;
	}
	
	public double getLuminanceValue() 
	{
		return luminanceValue;
	}

	public void setLuminanceValue(double luminanceValue) 
	{
		this.luminanceValue = luminanceValue;
	}
	
	public double getFlamePercentageValue() 
	{
		return flamePercentageValue;
	}

	public void setFlamePercentageValue(double flamePercentageValue) 
	{
		this.flamePercentageValue = flamePercentageValue;
	}

	public double getAudioPowerValue() 
	{
		return audioPowerValue;
	}

	public void setAudioPowerValue(double audioPowerValue)
	{
		this.audioPowerValue = audioPowerValue;
	}

	public double getAudioEnergyValue() 
	{
		return audioEnergyValue;
	}

	public void setAudioEnergyValue(double audioEnergyValue) 
	{
		this.audioEnergyValue = audioEnergyValue;
	}

	public double getAudioPaceValue() 
	{
		return audioPaceValue;
	}

	public void setAudioPaceValue(double audioPaceValue) 
	{
		this.audioPaceValue = audioPaceValue;
	}

	public ArrayList<Frame> getFrameList() 
	{
		return frameList;
	}

	public void setFrameList(ArrayList<Frame> frameList) 
	{
		this.frameList = frameList;
	}

	public String getShotRangePath() {
		return shotRangePath;
	}

	public void setShotRangePath(String shotRangePath) {
		this.shotRangePath = shotRangePath;
	}

	public String getFramePath() {
		return framePath;
	}

	public void setFramePath(String framePath) {
		this.framePath = framePath;
	}

	public int getStartingFrame() {
		return startingFrame;
	}

	public void setStartingFrame(int startingFrame) {
		this.startingFrame = startingFrame;
	}

	public int getEndingFrame() {
		return endingFrame;
	}

	public void setEndingFrame(int endingFrame) {
		this.endingFrame = endingFrame;
	}
}