package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Segmentation 
{
	private String filepath;
	
	public Segmentation(String absolutePath) 
	{
		this.filepath = absolutePath;							
	}
	public void segmentMovie()
	{
		File f = new File(filepath);							
		int imageCount = f.listFiles().length;		
		
		for(int i = 1; i < imageCount - 1; i++)
		{
			String imagePath1 = filepath + "\\" + i + ".jpeg";
			String imagePath2 = filepath + "\\" + (i+1) + ".jpeg";
			
			File image1 = new File(imagePath1);
			File image2 = new File(imagePath2);
	
			BufferedImage img1 = null;
	        BufferedImage img2 = null;	        	        
	        
	        try 
	        {
	        	img1 = ImageIO.read(image1);
				img2 = ImageIO.read(image2);
								
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
	        
	        
	        
	        int pixc = 0;
	        
		
			int width1 = img1.getWidth();
			int height1 = img2.getHeight();			
					
	    	for(int y = 0; y < height1; y++)
	    	{
	    		for(int x = 0; x < width1; x++)
	    		{
	    			
	    			int pix1 = img1.getRGB(x, y);
	    			
	    			pixc++;
	    			
	    			int red = (pix1 >> 16) & 0xff;
	    			int green = (pix1 >> 8) & 0xff;
	    			int blue = (pix1) & 0xff;
	    			
			    	System.out.println("Pixel "+pixc+" Frame "+i+" Results: [Red: "+red+"] || [Green: "+green+"] || [Blue: "+blue+"]");
	    		    			
	    		}
	    	}
	    	
	    	
			int width2 = img2.getWidth();
			int height2 = img2.getHeight();

	    	pixc = 0;
	    	
	    	for(int y = 0; y < height2; y++)
	    	{
	    		for(int x = 0; x < width2; x++)
	    		{
	    			
	    			int pix2 = img2.getRGB(x, y);
	    			
	    			pixc++;
	    			
	    			int red = (pix2 >> 16) & 0xff;
	    			int green = (pix2 >> 8) & 0xff;
	    			int blue = (pix2) & 0xff;
	    			
			    	System.out.println("Pixel "+pixc+" Frame "+(i+1)+" Results: [Red: "+red+"] || [Green: "+green+"] || [Blue: "+blue+"]");
	    		    			
	    		}
	    	}
	        
		}
	}
}
