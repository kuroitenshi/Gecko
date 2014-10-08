package model.Objects;

public class Histogram 
{
	public int[] getRED() {
		return RED;
	}

	public void setRED(int[] r) {
		RED = r;
	}

	public int[] getGREEN() {
		return GREEN;
	}

	public void setGREEN(int[] g) {
		GREEN = g;
	}

	public int[] getBLUE() {
		return BLUE;
	}

	public void setBLUE(int[] b) {
		BLUE = b;
	}

	private int[] RED = new int[16];
	private int[] GREEN = new int[16];
	private int[] BLUE = new int[16];
	
	public Histogram()
	{
		for (int i = 0; i < 16; i++)
        {
            RED[i] = 0;
            GREEN[i] = 0;
            BLUE[i] = 0;
         }
	}
	
}
