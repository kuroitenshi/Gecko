package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Shot 
{
	private int key;
	private ArrayList<Frame> frames;
	private String shotRangePath;
	private String framePath;
	private int startingFrame;
	private int endingFrame;
	private double visualDisturbanceValue;
	private double luminanceValue;

	public Shot(int key, String shotRangePath, String framePath)
	{
		this.setKey(key);
		startingFrame = 0;
		endingFrame = 0;
		this.shotRangePath = shotRangePath;
		this.framePath = framePath;
		frames = new ArrayList<Frame>();
		getFrameRange();
		getFrames();
		setVisualDisturbanceValue(0);
	}

	//Keep this Main for testing
	public static void main(String[] args)
	{
		Shot shot = null;
		for(int i=1; i < 72; i++)
		{
			shot = new Shot(i, "C:\\FFOutput\\Divergent.2014.720p.BluRay.x264.YIFY 01_51_30-01_53_30\\Visual Data\\ShotRange.txt",
					"C:\\FFOutput\\Divergent.2014.720p.BluRay.x264.YIFY 01_51_30-01_53_30\\Frames");
			//System.out.println(shot.computeVisualDisturbance());
			System.out.println(shot.computeLuminance());	
		}
		
	}
	
	private void getFrameRange() 
	{		
		try
		{
			FileReader inputFile = new FileReader(shotRangePath);
		    BufferedReader bufferReader = new BufferedReader(inputFile);
		
		    String line;
		    while ((line = bufferReader.readLine()) != null)
		    {
		    	String[] returnValue = line.split(" ", 7);
		    	if(returnValue[2].equals(""+key))
		    	{
		    		startingFrame = Integer.parseInt(returnValue[4]);
		    		endingFrame = Integer.parseInt(returnValue[6]);
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
	
	private void getFrames() 
	{
		for(int i = startingFrame; i <= endingFrame; i++)
		{
			Frame retrievedFrame = new Frame(i, framePath + "\\" + i + ".jpeg" );
			frames.add(retrievedFrame);			
		}
	}
	
	public double computeVisualDisturbance()
	{
		double THRESHOLD = 0.35;
		int counter = 0;
		int temp = 0;
		int divisor = 0;
		int frameInterval = this.frames.get(frames.size()-1).getKey() - this.frames.get(0).getKey();
		
		for(int i = 1; i < this.frames.size(); i+=2)
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
		
		return visualDisturbanceValue;
		
	}	
	
	public double computeLuminance()
	{
		int lastFrame = this.frames.size();
		double luminanceAVG = 0.0;
		int counter = 0;
		
		for(int i = 0; i < lastFrame; i++)
		{			
			if(i == 0 || i  == lastFrame || i == Math.floor((0 + lastFrame) /2 ))
			{				
				luminanceAVG += getFrameLuminance(frames.get(counter));
			}
			counter++;
		}
		
		luminanceAVG = luminanceAVG/3;
		
		return luminanceAVG;
	}
	
	public double getFrameLuminance(Frame frame)
	{
		double pixelSum  = 0;
		double luminanceValue = 0;
		RGB rgb = new RGB();
		
		
		int width = frame.getImage().getWidth();
		int height = frame.getImage().getHeight();
		
		for (int i =0; i < height; i++)
		{
			for(int j =0; j < width; j++)
			{
				int[] currentPixel = frame.getImage().getRaster().getPixel(j, i, new int[3]);
				
				rgb.setR(rgb.getR()+ currentPixel[0]);
				rgb.setG(rgb.getG()+ currentPixel[1]);
				rgb.setB(rgb.getB()+ currentPixel[2]);
			}
		}
		
		luminanceValue = 0.2126 * rgb.getR() + 0.7152 * rgb.getG() + 0.0722 * rgb.getB();
		pixelSum = luminanceValue / (height * width);	
		
		return pixelSum;
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
}