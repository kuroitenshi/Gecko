package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Segmentation 
{

	public Segmentation(String absolutePath) {
		File f = new File(absolutePath);
		int count = 0;
		
		System.out.println("File: "+f);
		System.out.println("Starting image count");
			for (File file : f.listFiles()) {
				if (file.isFile() && (file.getName().endsWith(".jpg")))
        		  count++; 
			}
		
		for(int i = 1; i < count; i++)
		{
			File image1 = new File(absolutePath + "//"+i+".jpg");
			File image2 = new File(absolutePath + "//"+(i+1)+".jpg");
			
	        BufferedImage img1 = null;
	        BufferedImage img2 = null;
	        
	        int pixc = 0;
	        
			try {
				img1 = ImageIO.read(image1);
				img2 = ImageIO.read(image2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int width1 = img1.getWidth();
			int height1 = img1.getHeight();
			
			int width2 = img2.getWidth();
			int height2 = img2.getHeight();
			
	    	for(int y = 0; y < height1; y++){
	    		for(int x = 0; x < width1; x++){
	    			
	    			int pix1 = img1.getRGB(x, y);
	    			
	    			pixc++;
	    			
	    			int red = (pix1 >> 16) & 0xff;
	    			int green = (pix1 >> 8) & 0xff;
	    			int blue = (pix1) & 0xff;
	    			
			    	System.out.println("Pixel "+pixc+" Frame "+i+" Results: [Red: "+red+"] || [Green: "+green+"] || [Blue: "+blue+"]");
	    		    			
	    		}
	    	}
	    	
	    	pixc = 0;
	    	
	    	for(int y = 0; y < height2; y++){
	    		for(int x = 0; x < width2; x++){
	    			
	    			int pix2 = img1.getRGB(x, y);
	    			
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
