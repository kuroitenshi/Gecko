package model;

import java.io.File;
import java.util.ArrayList;

public class Shot 
{
	private int key;
	private ArrayList<Frame> frames;
	private String framesPath;
	private double visualDisturbanceValue;
	
	public Shot(String framesPath)
	{
		this.framesPath = framesPath;
	} 	

	public Shot(int key)
	{
		this.setKey(key);
		frames = new ArrayList<Frame>();
		getFrames(key);
		setVisualDisturbanceValue(0);
	}
	
	private void getFrames(int key) 
	{		
		File framesFile = new File(this.framesPath);
		int framesFileSize = framesFile.listFiles().length;
		for(int i = 1; i < framesFileSize-1; i++)
		{
			Frame retrievedFrame = new Frame(i, this.framesPath + "\\" + i + ".jpeg" );
			this.frames.add(retrievedFrame);			
		}
				
	}	
	
	
	public void computeVisualDisturbance()
	{
		double THRESHOLD = 0.35;
		int counter = 0;
		int temp = 0;
		int divisor = 0;
		int frameInterval = this.frames.get(frames.size()-1).getKey() - this.frames.get(0).getKey();
		
		for(int i = 1; i < this.frames.size(); i++)
		{
			for (int j = 0; j < 9; j++)
			{
				double r1 = this.frames.get(i).getRgb(j).getR();
				double g1 = this.frames.get(i).getRgb(j).getG();
				double b1 = this.frames.get(i).getRgb(j).getB();
				
				double r2 = this.frames.get(i-1).getRgb(j).getR();
				double g2 = this.frames.get(i-1).getRgb(j).getG();
				double b2 = this.frames.get(i-1).getRgb(j).getB();
				
				double distance = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
				double max = Math.max(r1, r2) + Math.max(g1, g2) + Math.max(b1, b2);
				double difference = distance/max;
				
				if(difference > THRESHOLD)
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
	
	

}
