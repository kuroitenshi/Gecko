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
//		this.computeRGB();
	}


	private void computeRGB() 
	{
		//Divide the bitmap into 9 blocks, return each RGBTotal.. horizontal increment
		//RASTERIZE first
		
//		BitmapData bmpData = a.LockBits(new Rectangle(0, 0, a.Width, a.Height), ImageLockMode.ReadOnly, PixelFormat.Format24bppRgb);
//      IntPtr ptr = bmpData.Scan0;
		
		RGB rgb1, rgb2, rgb3, rgb4, rgb5, rgb6, rgb7, rgb8, rgb9;
//		byte* p = (byte*)(void*)ptr;
		byte[] p = null;
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
				int currentPixel = image.getRGB(i, j);
				int red = (currentPixel >> 16) & 0xff;
				int green = (currentPixel >> 8) & 0xff;
    			int blue = (currentPixel) & 0xff;
				
				try
				{
					// first block
					if(row1hs <= i && i <= row1he && col1ws <= j && j <= col1we)
					{						
		    			rgb1.setR(rgb1.getR()+p[0]);
						rgb1.setG(rgb1.getR()+p[1]);
						rgb1.setB(rgb1.getR()+p[2]);
					} 
					// second block
					else if(row1hs <= i && i <= row1he && col2ws <= j && j<= col2we)
					{
						rgb2.setR(rgb2.getR()+p[0]);
						rgb2.setG(rgb2.getR()+p[1]);
						rgb2.setB(rgb2.getR()+p[2]);
					}
					// third block
					else if(row1hs <= i && i <= row1he && col3ws <= j && j<= col3we)
					{
						rgb3.setR(rgb3.getR()+p[0]);
						rgb3.setG(rgb3.getR()+p[1]);
						rgb3.setB(rgb3.getR()+p[2]);
					}
					// fourth block
					else if(row2hs <= i && i <= row2he && col1ws <= j && j <= col1we)
					{
						rgb4.setR(rgb4.getR()+p[0]);
						rgb4.setG(rgb4.getR()+p[1]);
						rgb4.setB(rgb4.getR()+p[2]);
					}
					//5th block
					else if (row2hs <= i && i <= row2he && col2ws <= j && j <= col2we)
					{
						rgb5.setR(rgb5.getR()+p[0]);
						rgb5.setG(rgb5.getR()+p[1]);
						rgb5.setB(rgb5.getR()+p[2]);
					}
					//6th block
					else if (row2hs <= i && i <= row2he && col3ws <= j && j <= col3we)
					{
						rgb6.setR(rgb6.getR()+p[0]);
						rgb6.setG(rgb6.getR()+p[1]);
						rgb6.setB(rgb6.getR()+p[2]);
					}
					//7th block
					else if (row3hs <= i && i <= row3he && col1ws <= j && j <= col1we)
					{
						rgb7.setR(rgb7.getR()+p[0]);
						rgb7.setG(rgb7.getR()+p[1]);
						rgb7.setB(rgb7.getR()+p[2]);
	                }
					//8th block
					else if (row3hs <= i && i <= row3he && col2ws <= j && j <= col2we)
					{
						rgb8.setR(rgb8.getR()+p[0]);
						rgb8.setG(rgb8.getR()+p[1]);
						rgb8.setB(rgb8.getR()+p[2]);
	                }
					//9th block
					else if (row3hs <= i && i <= row3he && col3ws <= j && j <= col3we)
					{
						rgb9.setR(rgb9.getR()+p[0]);
						rgb9.setG(rgb9.getR()+p[1]);
						rgb9.setB(rgb9.getR()+p[2]);
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