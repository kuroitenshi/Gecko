package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Frame
{
	private BufferedImage image;
	private int key;
	private ArrayList<RGB> rgb;	
	private String directory;
	
	public Frame(int key, String directory)
	{
		this.directory = directory;
		File file = new File(this.directory);
		try 
		{
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setKey(key);
		rgb = new ArrayList<RGB>();
		this.computeRGB();
	}


	private void computeRGB() 
	{
		//Divide the bitmap into 9 blocks, return each RGBTotal.. horizontal increment
		//RASTERIZE first
		
//		BitmapData bmpData = a.LockBits(new Rectangle(0, 0, a.Width, a.Height), ImageLockMode.ReadOnly, PixelFormat.Format24bppRgb);
//      IntPtr ptr = bmpData.Scan0;
		
		RGB rgb1, rgb2, rgb3, rgb4, rgb5, rgb6, rgb7, rgb8, rgb9;
//		byte* p = (byte*)(void*)ptr;
//      int offset = bmpData.Stride - a.Width * 3;
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
				int currentPixel = image.getRGB(j, i);
				int red = (currentPixel >> 16) & 0xff;
				int green = (currentPixel >> 8) & 0xff;
    			int blue = (currentPixel) & 0xff;
				
				try
				{
					// first block
					if(row1hs <= i && i <= row1he && col1ws <= j && j <= col1we)
					{						
		    			rgb1.setR(red);
						rgb1.setG(green);
						rgb1.setB(blue);
					} 
					// second block
					else if(row1hs <= i && i <= row1he && col2ws <= j && j<= col2we)
					{
						rgb2.setR(red);
						rgb2.setG(green);
						rgb2.setB(blue);
					}
					// third block
					else if(row1hs <= i && i <= row1he && col3ws <= j && j<= col3we)
					{
						rgb3.setR(red);
						rgb3.setG(green);
						rgb3.setB(blue);
					}
					// fourth block
					else if(row2hs <= i && i <= row2he && col1ws <= j && j <= col1we)
					{
						rgb4.setR(red);
						rgb4.setG(green);
						rgb4.setB(blue);
					}
					//5th block
					else if (row2hs <= i && i <= row2he && col2ws <= j && j <= col2we)
					{
						rgb5.setR(red);
						rgb5.setG(green);
						rgb5.setB(blue);
					}
					//6th block
					else if (row2hs <= i && i <= row2he && col3ws <= j && j <= col3we)
					{
						rgb6.setR(red);
						rgb6.setG(green);
						rgb6.setB(blue);
					}
					//7th block
					else if (row3hs <= i && i <= row3he && col1ws <= j && j <= col1we)
					{
						rgb7.setR(red);
						rgb7.setG(green);
						rgb7.setB(blue);
	                }
					//8th block
					else if (row3hs <= i && i <= row3he && col2ws <= j && j <= col2we)
					{
						rgb8.setR(red);
						rgb8.setG(green);
						rgb8.setB(blue);
	                }
					//9th block
					else if (row3hs <= i && i <= row3he && col3ws <= j && j <= col3we)
					{
						rgb9.setR(red);
						rgb9.setG(green);
						rgb9.setB(blue);
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
	}
	
	public BufferedImage getImage() 
	{
		return image;
	}

	public void setImage(BufferedImage image) 
	{
		this.image = image;
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
}