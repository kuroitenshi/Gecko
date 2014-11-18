package model;

import java.awt.Color;
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
	private double flamePercentageValue;
	private double VD_THRESHOLD;
	private double FLAME_THRESHOLD;

	public Shot(int key, String shotRangePath, String framePath)
	{
		this.setKey(key);
		startingFrame = 0;
		endingFrame = 0;
		visualDisturbanceValue = 0;
		luminanceValue = 0;
		setFlamePercentageValue(0);
		VD_THRESHOLD = 0.35;
		FLAME_THRESHOLD = 0.10; // CHANGE FLAME_THRESHOLD VALUE
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
			shot = new Shot(i, "C:\\FFOutput\\22.Jump.Street.2014.1080p.WEB-DL.AAC2.0.H264-RARBG 01_26_10-01_28_10\\Visual Data\\ShotRange.txt",
					"C:\\FFOutput\\22.Jump.Street.2014.1080p.WEB-DL.AAC2.0.H264-RARBG 01_26_10-01_28_10\\Frames");
//			System.out.println(shot.computeVisualDisturbance());
//			System.out.println(shot.computeLuminance());	
			System.out.println(shot.computeFlamePercentage());
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
			
			String OS = System.getProperty("os.name").toLowerCase();

			Frame retrievedFrame = null;
			
			if (OS.indexOf("win") >= 0){
				retrievedFrame = new Frame(i, framePath + "\\" + i + ".jpeg" );
			}
			else if (OS.indexOf("mac") >= 0) {
				retrievedFrame = new Frame(i, framePath + "/" + i + ".jpeg" );
			}
			
			frames.add(retrievedFrame);			
		}
	}
	
	public double computeVisualDisturbance()
	{
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
	
	public double computeLuminance()
	{
		double luminanceAVG = 0.0;
		int counter = 0;
		
		for(int i = startingFrame; i <= endingFrame; i++)
		{			
			if(i == startingFrame || i  == endingFrame || i == Math.floor((startingFrame + endingFrame) /2 ))
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
	
	public double computeFlamePercentage()
	{
		double flamePercentage = 0;
		double flamePercentageAVG = 0;
		int counter = 0;
		
		for(int i = startingFrame; i <= endingFrame; i++)
		{
			flamePercentage = detectFlamePixels(frames.get(counter));
			if(flamePercentage >= FLAME_THRESHOLD)
			{
				flamePercentageAVG += flamePercentage;
			}
			counter++;
		}
		
		flamePercentageAVG /= endingFrame - startingFrame;
		if(endingFrame-startingFrame == 0)
		{
			flamePercentageAVG = 0;
		}
		return flamePercentageAVG;
	}
	
	public double detectFlamePixels(Frame frame) 
	{
		int flamePixels = 0;
		double flamePercentage = 0;
		float saturation = 0;
		RGB rgb = new RGB();
		RGB tempRGB = new RGB();
		int height = frame.getImage().getHeight();
		int width = frame.getImage().getWidth();
		int resolution = height * width;
		double redTreshold = 169; // VALUE NOT YET FINAL
		// 40 to 70 normal threshold, BEAM has a 0.10 threshold
		// play with the saturation threshold
		double saturationThreshold = 0.10; 
		
		for (int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				int[] currentPixel = frame.getImage().getRaster().getPixel(j, i, new int[3]);
				
				tempRGB.setR(tempRGB.getR()+ currentPixel[0]);
				tempRGB.setG(tempRGB.getG()+ currentPixel[1]);
				tempRGB.setB(tempRGB.getB()+ currentPixel[2]);
				
				// normalize RGB Values
				double RGBSum = tempRGB.getR() + tempRGB.getG() + tempRGB.getB();
				
				rgb.setR(tempRGB.getR()/RGBSum);
				rgb.setG(tempRGB.getG()/RGBSum);
				rgb.setB(tempRGB.getB()/RGBSum);
				
				if(tempRGB.getR() > redTreshold)
				{
					if(rgb.getR() > rgb.getG() && rgb.getG() > rgb.getB())
					{
						saturation = calculateSaturation(tempRGB);
						double value = ((255 - tempRGB.getR()*saturationThreshold)/redTreshold);
						if(saturation>value)
						{
							flamePixels++;
						}
					}
				}
			}
		}
		flamePercentage = (flamePixels)/resolution;
		return flamePercentage;
	}

	public float calculateSaturation(RGB rgb) {
		float saturation = 0;
		float af[] = Color.RGBtoHSB((int)rgb.getR(), (int)rgb.getG(), (int)rgb.getB(), null);
		saturation = af[1];
		return saturation;
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
}