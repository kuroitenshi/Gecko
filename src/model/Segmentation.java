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
	private int shotNumber;
	private int totalPix = 0;
	public Segmentation(String framesPath, String resultsPath) 
	{
		this.framesPath = framesPath;	
		this.resultsPath = resultsPath;
		setShotRangeNumber(1);
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
		totalPix = width * height;				
    	for(int y = 0; y < height; y++)
    	{
    		for(int x = 0; x < width; x++)
    		{
    			int[] currentPixel = buffImage.getRaster().getPixel(x, y, new int[3]);
    	    			    		
                tempRed[currentPixel[0] / BINS]++;             //gets red values
                tempGreen[currentPixel[1] / BINS]++;             //gets green values
                tempBlue[currentPixel[2] / BINS]++;             //gets blue values
    			
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
        
        return eucDist/totalPix;
    }
	
	/**
	 * Segments the whole movie into shots
	 * Results will be outputted to the Visual Data folder 
	 */
	public void segmentMovie()
	{
		
		File f = new File(this.framesPath);				
		int imageCount = f.listFiles().length;		
		int fileEnd = imageCount;
		int counterImage = 0;
		int shotRangeCounter = 1;
		 
		/*ATOMICA's Threshold for Action Movies*/
		double distance_threshold = 0.491387; 
		
		Histogram histA;
		Histogram histB;
		Histogram histCheck;	
		
		StringBuilder shotNumbersString = new StringBuilder();
		StringBuilder shotRangeString = new StringBuilder();
		
		for(int i = 1; i < imageCount-1; i++)
		{
			double imageDiff = 0;
			
			// ADDED
			
			String OS = System.getProperty("os.name").toLowerCase();

			String imagePath1 = "";		
			String imagePath2 = "";
						
			if (OS.indexOf("win") >= 0){
				imagePath1 = framesPath + "\\" + i + ".jpeg";			
				imagePath2 = framesPath + "\\" + (i+1) + ".jpeg";
			}
			else if (OS.indexOf("mac") >= 0) {
				imagePath1 = framesPath + "/" + i + ".jpeg";			
				imagePath2 = framesPath + "/" + (i+1) + ".jpeg";
			}
			// END ADDED
			
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
                {
                	break;
                }
                //Image A and B are not the same; Compare Image A and Image Check
                try
                {
                	// ADDED
                	
        			String imagePath3 = "";		
        			
        			if (OS.indexOf("win") >= 0){
                    	imagePath3 = framesPath + "\\" + (i+2) + ".jpeg";
        			}
        			else if (OS.indexOf("mac") >= 0) {
                    	imagePath3 = framesPath + "/" + (i+2) + ".jpeg";
        			}
                	
                	// END ADDED
        			
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
                	if(counterImage > 4)
                	{
                      	shotNumbersString = shotNumbersString.append("Shot No: " + getShotRangeNumber() + " Frame " + i + " to " + (i+1)  + " Difference " + imageDiff + "\r\n");
                    	shotRangeString = shotRangeString.append("Shot No: " + getShotRangeNumber() + " Frames " + shotRangeCounter + " to " + i + "\r\n");
                        shotRangeCounter = (i+1);
           
                    	System.out.println("Shot " + getShotRangeNumber());
                    	setShotRangeNumber(getShotRangeNumber() + 1);
                        counterImage = 0;  
                	}
                }
            }
		}		
		
		shotNumbersString = shotNumbersString.append("Shot No: " + getShotRangeNumber() + " Frame " + shotRangeCounter + " to " + imageCount  + " Difference " + 0.0 + "\r\n");
		shotRangeString = shotRangeString.append("Shot No: " + getShotRangeNumber() + " Frames " + shotRangeCounter + " to " + imageCount + "\r\n");
		this.setShotRangeNumber(getShotRangeNumber()- 1);

		// ADDED
		
			String OS = System.getProperty("os.name").toLowerCase();
			
			File resultShotFile = null;
			File resultShotRangeFile = null;
			
			if (OS.indexOf("win") >= 0){
				resultShotFile = new File(resultsPath.concat("\\Visual Data\\Shots.txt"));
				resultShotRangeFile = new File(resultsPath.concat("\\Visual Data\\ShotRange.txt"));
			}
			else if (OS.indexOf("mac") >= 0) {
				resultShotFile = new File(resultsPath.concat("/Visual Data/Shots.txt"));
				resultShotRangeFile = new File(resultsPath.concat("/Visual Data/ShotRange.txt"));
			}
	    	
	    // END ADDED
		
		FileWriter resultShotRangeWriter = null;
    	FileWriter resultWriter = null;
    	
    	try 
		{
			resultWriter = new FileWriter(resultShotFile.getAbsoluteFile());			
			resultShotRangeWriter = new FileWriter(resultShotRangeFile.getAbsoluteFile());
			
			BufferedWriter shotWriter = new BufferedWriter(resultWriter);
	    	BufferedWriter shotRangeWriter = new BufferedWriter(resultShotRangeWriter);
	    	
	    	shotWriter.write(shotNumbersString.toString());
			shotWriter.close();
			
			shotRangeWriter.write(shotRangeString.toString());
    		shotRangeWriter.close();
		} 
    	catch (IOException e) 
		{
			e.printStackTrace();
		};
	}

	public int getShotRangeNumber() 
	{
		return shotNumber;
	}

	public void setShotRangeNumber(int shotNumber) 
	{
		this.shotNumber = shotNumber;
	}
}