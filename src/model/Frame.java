package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import model.Objects.Histogram;

public class Frame
{
	private BufferedImage image;
	private int key;
	private ArrayList<RGB> rgb;
	private String directory;
	
	public Frame(int key, String directory)
	{
		this.directory = directory;
		File file = new File(directory);
	
		try 
		{
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setKey(key);
		rgb = new ArrayList<RGB>();
		computeRGB();
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

	public RGB getRgb(int index) 
	{
		return rgb.get(index);
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
		int width = image.getWidth() * 3;
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
				try
				{
					// first block
					if(row1hs <= i && i <= row1he && col1ws <= j && j <= col1we)
					{
//						  data1.r += p[0];             //gets red values
//                          data1.g += p[1];             //gets green values
//                          data1.b += p[2];             //gets blue values
					} 
					// second block
					else if(row1hs <= i && i <= row1he && col2ws <= j && j<= col2we)
					{
						
					}
					// third block
					else if(row1hs <= i && i <= row1he && col3ws <= j && j<= col3we)
					{
						
					}
					// fourth block
					else if(row2hs <= i && i <= row2he && col1ws <= j && j <= col1we)
					{
						
					}
					//5th block
					else if (row2hs <= i && i <= row2he && col2ws <= j && j <= col2we)
					{
					}
					//6th block
					else if (row2hs <= i && i <= row2he && col3ws <= j && j <= col3we)
					{
					}
					//7th block
					else if (row3hs <= i && i <= row3he && col1ws <= j && j <= col1we)
					{
	                }
					//8th block
					else if (row3hs <= i && i <= row3he && col2ws <= j && j <= col2we)
					{
	                }
					//9th block
					else if (row3hs <= i && i <= row3he && col3ws <= j && j <= col3we)
					{
	                }
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
//				p++;
			}
//			p += offset;
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
}