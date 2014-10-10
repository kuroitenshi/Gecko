package model;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

import model.Objects.Histogram;

public class Segmentation 
{
	
	private int BINS = 16;
	private String framesPath;
	private String resultsPath;
	
	public Segmentation(String framesPath, String resultsPath) 
	{
		this.framesPath = framesPath;	
		this.resultsPath = resultsPath;
	}
	
	/**
	 * Returns computed Histogram of an Image
	 * @param image
	 * @return Histogram Object
	 */
	public Histogram findHistogram(File image)
	{
		Histogram currentHist = new Histogram();
		BufferedImage buffImage = null;
		
		int[] tempRed = new int[16];
		int[] tempGreen = new int[16];
		int[] tempBlue = new int[16];
       
		try 
		{
        	buffImage = ImageIO.read(image);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		
		int width = buffImage.getWidth();
		int height = buffImage.getHeight();			
				
    	for(int y = 0; y < height; y++)
    	{
    		for(int x = 0; x < width; x++)
    		{
    			
    			int currentPixel = buffImage.getRGB(x, y);
    			
    			
    			int red = (currentPixel >> 16) & 0xff;
    			int green = (currentPixel >> 8) & 0xff;
    			int blue = (currentPixel) & 0xff;
    			
                tempRed[red / BINS]++;             //gets red values
                tempGreen[green / BINS]++;             //gets green values
                tempBlue[blue / BINS]++;             //gets blue values
    			
                currentHist.setRED(tempRed);
                currentHist.setGREEN(tempGreen);
                currentHist.setBLUE(tempBlue);
                		    	
    		    			
    		}
    	}
		
		return currentHist;
		
	}
	
	/**
	 * Computes the EuclideanDistance Between to Histograms
	 * @param Ha HistogramA 
	 * @param Hb HistogramB
	 * @return Distance between to histograms
	 */
	public double euclideanDist(Histogram Ha, Histogram Hb)
	{
		double eucDist = 0;
		
        for (int i = 0; i < BINS; i++)
        {
           eucDist += Math.sqrt(Math.pow(Ha.getRED()[i] - Hb.getRED()[i], 2) + Math.pow(Ha.getGREEN()[i] - Hb.getGREEN()[i], 2) + Math.pow(Ha.getBLUE()[i] - Hb.getBLUE()[i], 2));
        }
        
        return eucDist;
    }
	
	
	/**
	 * Segments the whole movie into shots
	 * Results will be outputted to the Visual Data folder 
	 */
	public void segmentMovie()
	{
		
		System.out.println(this.framesPath);
		File f = new File(this.framesPath);							
		int imageCount = f.listFiles().length;
		System.out.println(imageCount);
		int counterImage = 0; 
		double distance_threshold = 89908.84; //experimental threshold
		int fileEnd = imageCount;
		
		Histogram histA;
		Histogram histB;
		Histogram histCheck;		
		int shotNumber = 1;
		
		StringBuilder shotNumbersString = new StringBuilder();
		for(int i = 1; i < imageCount-1; i++)
		{
			
			double imageDiff = 0;
			
			String imagePath1 = framesPath + "\\" + i + ".jpeg";			
			String imagePath2 = framesPath + "\\" + (i+1) + ".jpeg";
			
			File image1 = new File(imagePath1);
			File image2 = new File(imagePath2);
			
			histA = findHistogram(image1);
			histB = findHistogram(image2);
		
			imageDiff = euclideanDist(histA, histB);
			
			
            if (imageDiff < distance_threshold)
            {
               counterImage++;
            }            
            else //not similar, second change
            {
                if (i + 2 == fileEnd) 
                	break;

                //Image A and B are not the same; Compare Image A and Image Check
                try
                {
                	String imagePath3 = framesPath + "\\" + (i+2) + ".jpeg";
                	File image3 = new File(imagePath3);
                	histCheck = findHistogram(image3);                
                    imageDiff = euclideanDist(histA, histCheck);                    
                }
                catch (Exception e2) 
                {
                	e2.printStackTrace();
                }
                // Image A and Image Check are the same.
                if (imageDiff < distance_threshold)
                {
                    counterImage++;
                }
                else
                {
                    if (counterImage > 4)
                    {
                    	shotNumbersString = shotNumbersString.append("Shot No: " + shotNumber + " Frame " + i + " to " + (i+1)  + " Difference " + imageDiff + "\r\n");   
                    	System.out.println(shotNumber);
                    	shotNumber++;
                        counterImage = 0;
                    }
                }
            }
            
    		
		}
		File resultFile = new File(resultsPath.concat("\\Visual Data\\Shots.txt"));
    	FileWriter resultWriter = null;
		try {
			resultWriter = new FileWriter(resultFile.getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		};
    	BufferedWriter writer = new BufferedWriter(resultWriter);
    	try {
			writer.write(shotNumbersString.toString());
			writer.close();
		} catch (IOException e) {							
			e.printStackTrace();
		}
    	
		
	}

}
