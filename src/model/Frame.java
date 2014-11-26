package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Frame
{
	private int key;
	private ArrayList<RGB> rgb;	
	

	private String directory;
	
	
	public Frame(int key, String directory)
	{
		this.directory = directory;				
		this.setKey(key);		
		
	}
	public ArrayList<RGB> computeRGB(BufferedImage image) 
	{
		ArrayList<RGB> rgb = new ArrayList<RGB>();
		RGB rgb1, rgb2, rgb3, rgb4, rgb5, rgb6, rgb7, rgb8, rgb9;
		int width = image.getWidth();
		int height = image.getHeight();	
		
		int row1hs = 0;
		int row1he = height / 3;
		int row2hs = height / 3;
		int row2he = height / 3 * 2;
		int row3hs = height / 3 * 2;
		int row3he = height;

		int col1ws = 0;
		int col1we = width / 3;
		int col2ws = width / 3;
		int col2we = width / 3 * 2;
		int col3ws = width / 3 * 2;
		int col3we = width;
		
		rgb1 = new RGB();
		rgb2 = new RGB();
		rgb3 = new RGB();
		rgb4 = new RGB();
		rgb5 = new RGB();
		rgb6 = new RGB();
		rgb7 = new RGB();
		rgb8 = new RGB();
		rgb9 = new RGB();

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				int[] currentPixel = image.getRaster().getPixel(j, i, new int[3]);
				int red = currentPixel[0];
				int green = currentPixel[1];
    			int blue = currentPixel[2];
				
				try
				{
					// first block
					if(row1hs <= i && i <= row1he && col1ws <= j && j <= col1we)
					{						
		    			rgb1.setR(rgb1.getR() + red);
		    			rgb1.setG(rgb1.getG() + green);
		    			rgb1.setB(rgb1.getB() + blue);
					} 
					// second block
					else if(row1hs <= i && i <= row1he && col2ws <= j && j<= col2we)
					{
						rgb2.setR(rgb2.getR() + red);
		    			rgb2.setG(rgb2.getG() + green);
		    			rgb2.setB(rgb2.getB() + blue);
					}
					// third block
					else if(row1hs <= i && i <= row1he && col3ws <= j && j<= col3we)
					{
						rgb3.setR(rgb3.getR() + red);
		    			rgb3.setG(rgb3.getG() + green);
		    			rgb3.setB(rgb3.getB() + blue);
					}
					// fourth block
					else if(row2hs <= i && i <= row2he && col1ws <= j && j <= col1we)
					{
						rgb4.setR(rgb4.getR() + red);
		    			rgb4.setG(rgb4.getG() + green);
		    			rgb4.setB(rgb4.getB() + blue);
					}
					//5th block
					else if (row2hs <= i && i <= row2he && col2ws <= j && j <= col2we)
					{
						rgb5.setR(rgb5.getR() + red);
		    			rgb5.setG(rgb5.getG() + green);
		    			rgb5.setB(rgb5.getB() + blue);
					}
					//6th block
					else if (row2hs <= i && i <= row2he && col3ws <= j && j <= col3we)
					{
						rgb6.setR(rgb6.getR() + red);
		    			rgb6.setG(rgb6.getG() + green);
		    			rgb6.setB(rgb6.getB() + blue);
					}
					//7th block
					else if (row3hs <= i && i <= row3he && col1ws <= j && j <= col1we)
					{
						rgb7.setR(rgb7.getR() + red);
		    			rgb7.setG(rgb7.getG() + green);
		    			rgb7.setB(rgb7.getB() + blue);
	                }
					//8th block
					else if (row3hs <= i && i <= row3he && col2ws <= j && j <= col2we)
					{
						rgb8.setR(rgb8.getR() + red);
		    			rgb8.setG(rgb8.getG() + green);
		    			rgb8.setB(rgb8.getB() + blue);
	                }
					//9th block
					else if (row3hs <= i && i <= row3he && col3ws <= j && j <= col3we)
					{
						rgb9.setR(rgb9.getR() + red);
		    			rgb9.setG(rgb9.getG() + green);
		    			rgb9.setB(rgb9.getB() + blue);
	                }
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
		rgb.add(rgb1);
		rgb.add(rgb2);
		rgb.add(rgb3);
		rgb.add(rgb4);
		rgb.add(rgb5);
		rgb.add(rgb6);
		rgb.add(rgb7);
		rgb.add(rgb8);
		rgb.add(rgb9);
		
		return rgb;
	}

	public int getKey() 
	{
		return key;
	}

	public void setKey(int key) 
	{
		this.key = key;
	}
	
	public String getDirectory() 
	{
		return directory;
	}

	public RGB getRgb(int index) 
	{
		return rgb.get(index);
	}
	
	public void setRgb(ArrayList<RGB> rgb)
	{
		this.rgb = rgb;
	}
}