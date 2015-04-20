package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenreClassifierFuzzyLogic 
{
	private ArrayList<Shot> shotList;
	private String resultsDirectory;
	
	/*-----------------------------------------------*/
	/*-------------ACTION CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double ACTION_FLAME_PERCENTAGE_UP = 1.097195132; //upper
	public final double ACTION_FLAME_PERCENTAGE_LOW = 0.291225640; //lower
	public final double ACTION_FLAME_PERCENTAGE_MID = 0.694210387; //OLD Not updated
	
	public final double ACTION_VISUAL_DISTURBANCE_UP = 0.268948514; //upper
	public final double ACTION_VISUAL_DISTURBANCE_LOW = 0.023431134; //lowe
	public final double ACTION_VISUAL_DISTURBANCE_MID = 0.146189824; //mid

	public final double ACTION_LUMINANCE_UP = 96.42427803; //upper
	public final double ACTION_LUMINANCE_LOW = 34.03590127; //lower
	public final double ACTION_LUMINANCE_MID = 48.21213902; //LOWER
	
	public final double ACTION_AUDIO_POWER_UP = 0.0202;//upper
	public final double ACTION_AUDIO_POWER_LOW = 0.00112;//lower
	public final double ACTION_AUDIO_POWER_MID = 0.0101;// ave
	
	public final double ACTION_AUDIO_PACE_UP= 138; //upper
	public final double ACTION_AUDIO_PACE_LOW= 89; //lower
	public final double ACTION_AUDIO_PACE_MID= 113; //ave

	public final double ACTION_AUDIO_ENERGY_UP = 0.02020;//upper
	public final double ACTION_AUDIO_ENERGY_LOW = 0.00121;//lower
	public final double ACTION_AUDIO_ENERGY_MID = 0.0107;// ADJUSTED VARIANCE
		
	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double HORROR_VISUAL_DISTURBANCE_UP =  0.448801645; //upper 
	public final double HORROR_VISUAL_DISTURBANCE_LOW =  0.021292403 ; //lower
	public final double HORROR_VISUAL_DISTURBANCE_MID =  0.235047024; //VARIANCE

	public final double HORROR_LUMINANCE_UP = 50; //upper
	public final double HORROR_LUMINANCE_LOW = 14; //lower
	public final double HORROR_LUMINANCE_MID = 32; //ADJUSTED
	
	public final double HORROR_AUDIO_ENERGY_UP = 0.037900; //upper
	public final double HORROR_AUDIO_ENERGY_LOW = 0.004744383; //lower
	public final double HORROR_AUDIO_ENERGY_MID = 0.0213221915; //VARIANCE
	
	public final double HORROR_AUDIO_POWER_UP = 0.007489539; //upper
	public final double HORROR_AUDIO_POWER_LOW = 0.002331827; //lower
	public final double HORROR_AUDIO_POWER_MID = 0.004910683; //ave

	public final double HORROR_AUDIO_PACE_UP = 133.5158277; //upper
	public final double HORROR_AUDIO_PACE_LOW = 51.79845803; //lower
	public final double HORROR_AUDIO_PACE_MID = 66.75791384; //ave

	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/

	public final double COMEDY_LUMINANCE_UP = 84; //upper
	public final double COMEDY_LUMINANCE_MID = 57; //mid
	public final double COMEDY_LUMINANCE_LOW = 30; //lower

	public final double COMEDY_VISUAL_DISTURBANCE_UP = 0.136644163; //upper
	public final double COMEDY_VISUAL_DISTURBANCE_LOW = 0.036067944; //lower
	public final double COMEDY_VISUAL_DISTURBANCE_MID = 0.086356053; //mid

	public final double COMEDY_AUDIO_ENERGY_UP = 0.0294; //upper
	public final double COMEDY_AUDIO_ENERGY_LOW = 0.0107; //lower
	public final double COMEDY_AUDIO_ENERGY_MID = 0.0200; //mid

	public final double COMEDY_AUDIO_POWER_UP = 0.281938284; //upper
	public final double COMEDY_AUDIO_POWER_LOW = 0.132679755; //lower
	public final double COMEDY_AUDIO_POWER_MID = 0.207309019; //mid

	public final double COMEDY_AUDIO_PACE_UP = 146.7447615; //upper
	public final double COMEDY_AUDIO_PACE_LOW = 79.3653302; //lower
	public final double COMEDY_AUDIO_PACE_MID = 113.0550459; //mid

	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/

	public final double DRAMA_VISUAL_DISTURBANCE_UP =  0.099372396; //upper 
	public final double DRAMA_VISUAL_DISTURBANCE_LOW =  0.031450171; //lower
	public final double DRAMA_VISUAL_DISTURBANCE_MID =  0.065411283; //ave

	public final double DRAMA_LUMINANCE_UP = 84.83507559; //upper
	public final double DRAMA_LUMINANCE_LOW = 45.06967157; //lower
	public final double DRAMA_LUMINANCE_MID = 64.95237358; //ave
	
	public final double DRAMA_AUDIO_ENERGY_UP = 0.009167027; //upper
	public final double DRAMA_AUDIO_ENERGY_LOW = 0.002614836; //lower
	public final double DRAMA_AUDIO_ENERGY_MID = 0.005890931; //mid

	public final double DRAMA_AUDIO_POWER_UP = 0.00221; //upper
	public final double DRAMA_AUDIO_POWER_LOW = 0.000444905; //lower
	public final double DRAMA_AUDIO_POWER_MID = 0.0013274525; //mid

	public final double DRAMA_AUDIO_PACE_UP = 115.0143986; //upper
	public final double DRAMA_AUDIO_PACE_LOW = 61.85352597; //lower
	public final double DRAMA_AUDIO_PACE_MID = 88.43396226; //mid

	public int HORROR_COUNT = 0;
	public int ACTION_COUNT = 0;
	public int COMEDY_COUNT = 0;
	public int DRAMA_COUNT = 0;


	
	public GenreClassifierFuzzyLogic(ArrayList<Shot> shotList, String resultsDirectory)	
	{
		this.shotList = shotList;		
		this.resultsDirectory = resultsDirectory;
	}
	
	/**
	 * Classifies the Movie based on the Gecko feature decision tree (Refer to documentation)
	 */
	public void classifyMovieGenre()
	{
		StringBuilder shotGenres = new StringBuilder();
		boolean classified = false;
		
		double HORROR_AVERAGE = 0;
		double ACTION_AVERAGE = 0;
		double COMEDY_AVERAGE = 0;
		double DRAMA_AVERAGE = 0;
		
		double HORROR_VD = 0;
		double HORROR_FL = 0;
		double HORROR_L = 0;
		double HORROR_AE = 0;
		double HORROR_AP = 0;
		double HORROR_T = 0;
		
		double ACTION_VD = 0;
		double ACTION_FL = 0;
		double ACTION_L = 0;
		double ACTION_AE = 0;
		double ACTION_AP = 0;
		double ACTION_T = 0;
		
		double COMEDY_VD = 0;
		double COMEDY_FL = 0;
		double COMEDY_L = 0;
		double COMEDY_AE = 0;
		double COMEDY_AP = 0;
		double COMEDY_T = 0;
		
		double DRAMA_VD = 0;
		double DRAMA_FL = 0;
		double DRAMA_L = 0;
		double DRAMA_AE = 0;
		double DRAMA_AP = 0;
		double DRAMA_T = 0;
		
		for(int i=0; i < this.shotList.size(); i++)
		{
			//FOR FLAME PERCENTAGE
			if(ACTION_FLAME_PERCENTAGE_UP < shotList.get(i).getFlamePercentageValue())
				ACTION_FL = 100;
			else if(isThresholdRange(ACTION_FLAME_PERCENTAGE_UP,ACTION_FLAME_PERCENTAGE_LOW, shotList.get(i).getFlamePercentageValue()))
			{
				ACTION_FL = getDistance(ACTION_FLAME_PERCENTAGE_UP, ACTION_FLAME_PERCENTAGE_MID, shotList.get(i).getFlamePercentageValue());
			} else ACTION_FL = 0;
			
			//FOR VISUAL DISTURBANCE
			if(isThresholdRange(ACTION_VISUAL_DISTURBANCE_UP,ACTION_VISUAL_DISTURBANCE_LOW, shotList.get(i).getVisualDisturbanceValue()))
			{
				ACTION_VD = getDistance(ACTION_VISUAL_DISTURBANCE_UP, ACTION_VISUAL_DISTURBANCE_MID, shotList.get(i).getVisualDisturbanceValue());
			} else ACTION_VD = 0;

			if(HORROR_VISUAL_DISTURBANCE_UP < shotList.get(i).getVisualDisturbanceValue())
				HORROR_VD = 100;
			else if(isThresholdRange(HORROR_VISUAL_DISTURBANCE_UP,HORROR_VISUAL_DISTURBANCE_LOW, shotList.get(i).getVisualDisturbanceValue()))
			{
				HORROR_VD = getDistance(HORROR_VISUAL_DISTURBANCE_UP, HORROR_VISUAL_DISTURBANCE_MID, shotList.get(i).getVisualDisturbanceValue());
			} else HORROR_VD = 0;
			
			if(isThresholdRange(COMEDY_VISUAL_DISTURBANCE_UP,COMEDY_VISUAL_DISTURBANCE_LOW, shotList.get(i).getVisualDisturbanceValue()))
			{
				COMEDY_VD = getDistance(COMEDY_VISUAL_DISTURBANCE_UP, COMEDY_VISUAL_DISTURBANCE_MID, shotList.get(i).getVisualDisturbanceValue());
			} else COMEDY_VD = 0;
			
			if(DRAMA_VISUAL_DISTURBANCE_LOW > shotList.get(i).getVisualDisturbanceValue())
				DRAMA_VD = 100;
			else if(isThresholdRange(DRAMA_VISUAL_DISTURBANCE_UP,DRAMA_VISUAL_DISTURBANCE_LOW, shotList.get(i).getVisualDisturbanceValue()))
			{
				DRAMA_VD = getDistance(DRAMA_VISUAL_DISTURBANCE_UP, DRAMA_VISUAL_DISTURBANCE_MID, shotList.get(i).getVisualDisturbanceValue());
			} else DRAMA_VD = 0;
			
			// FOR LUMINANCE
			if(ACTION_LUMINANCE_UP < shotList.get(i).getLuminanceValue())
				ACTION_L = 100;
			else if(isThresholdRange(ACTION_LUMINANCE_UP,ACTION_LUMINANCE_LOW, shotList.get(i).getLuminanceValue()))
			{
				ACTION_L = getDistance(ACTION_LUMINANCE_UP, ACTION_LUMINANCE_MID, shotList.get(i).getLuminanceValue());
			} else ACTION_L = 0;

			if(HORROR_LUMINANCE_LOW > shotList.get(i).getLuminanceValue())
				HORROR_L = 100;
			else if(isThresholdRange(HORROR_LUMINANCE_UP,HORROR_LUMINANCE_LOW, shotList.get(i).getLuminanceValue()))
			{
				HORROR_L = getDistance(HORROR_LUMINANCE_UP, HORROR_LUMINANCE_MID, shotList.get(i).getLuminanceValue());
			} else HORROR_L = 0;
			
			if(isThresholdRange(COMEDY_LUMINANCE_UP,COMEDY_LUMINANCE_LOW, shotList.get(i).getLuminanceValue()))
			{
				COMEDY_L = getDistance(COMEDY_LUMINANCE_UP, COMEDY_LUMINANCE_MID, shotList.get(i).getLuminanceValue());
			} else COMEDY_L = 0;
			
			if(isThresholdRange(DRAMA_LUMINANCE_UP,DRAMA_LUMINANCE_LOW, shotList.get(i).getLuminanceValue()))
			{
				DRAMA_L = getDistance(DRAMA_LUMINANCE_UP, DRAMA_LUMINANCE_MID, shotList.get(i).getLuminanceValue());
			} else DRAMA_L = 0;
			
			//FOR AUDIO ENERGY

			if(ACTION_AUDIO_ENERGY_LOW > shotList.get(i).getAudioEnergyValue())
				ACTION_AE = 100;
			else if(isThresholdRange(ACTION_AUDIO_ENERGY_UP,ACTION_AUDIO_ENERGY_LOW, shotList.get(i).getAudioEnergyValue()))
			{
				ACTION_AE = getDistance(ACTION_AUDIO_ENERGY_UP, ACTION_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue());
			} else ACTION_AE = 0;

			if(HORROR_AUDIO_ENERGY_UP < shotList.get(i).getAudioEnergyValue())
				HORROR_AE = 100;
			else if(isThresholdRange(HORROR_AUDIO_ENERGY_UP,HORROR_AUDIO_ENERGY_LOW, shotList.get(i).getAudioEnergyValue()))
			{
				HORROR_AE = getDistance(HORROR_AUDIO_ENERGY_UP, HORROR_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue());
			} else HORROR_AE = 0;
			
			if(isThresholdRange(COMEDY_AUDIO_ENERGY_UP,COMEDY_AUDIO_ENERGY_LOW, shotList.get(i).getAudioEnergyValue()))
			{
				COMEDY_AE = getDistance(COMEDY_AUDIO_ENERGY_UP, COMEDY_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue());
			} else COMEDY_AE = 0;
			
			if(isThresholdRange(DRAMA_AUDIO_ENERGY_UP,DRAMA_AUDIO_ENERGY_LOW, shotList.get(i).getAudioEnergyValue()))
			{
				DRAMA_AE = getDistance(DRAMA_AUDIO_ENERGY_UP, DRAMA_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue());
			} else DRAMA_AE = 0;
			
			//FOR AUDIO POWER
			if(isThresholdRange(ACTION_AUDIO_POWER_UP,ACTION_AUDIO_POWER_LOW, shotList.get(i).getAudioPowerValue()))
			{
				ACTION_AP = getDistance(ACTION_AUDIO_POWER_UP, ACTION_AUDIO_POWER_MID, shotList.get(i).getAudioPowerValue());
			} else ACTION_AP = 0;

			if(isThresholdRange(HORROR_AUDIO_POWER_UP,HORROR_AUDIO_POWER_LOW, shotList.get(i).getAudioPowerValue()))
			{
				HORROR_AP = getDistance(HORROR_AUDIO_POWER_UP, HORROR_AUDIO_POWER_MID, shotList.get(i).getAudioPowerValue());
			} else HORROR_AP = 0;
			
			if(COMEDY_AUDIO_POWER_UP < shotList.get(i).getAudioPowerValue())
				COMEDY_AP = 100;
			else if(isThresholdRange(COMEDY_AUDIO_POWER_UP,COMEDY_AUDIO_POWER_LOW, shotList.get(i).getAudioPowerValue()))
			{
				COMEDY_AP = getDistance(COMEDY_AUDIO_POWER_UP, COMEDY_AUDIO_POWER_MID, shotList.get(i).getAudioPowerValue());
			} else COMEDY_AP = 0;
			
			if(DRAMA_AUDIO_POWER_LOW > shotList.get(i).getAudioPowerValue())
				DRAMA_AP = 100;
			else if(isThresholdRange(DRAMA_AUDIO_POWER_UP,DRAMA_AUDIO_POWER_LOW, shotList.get(i).getAudioPowerValue()))
			{
				DRAMA_AP = getDistance(DRAMA_AUDIO_POWER_UP, DRAMA_AUDIO_POWER_MID, shotList.get(i).getAudioPowerValue());
			} else DRAMA_AP = 0;
			
			//FOR AUDIO PACE
			if(isThresholdRange(ACTION_AUDIO_PACE_UP,ACTION_AUDIO_PACE_LOW, shotList.get(i).getAudioPaceValue()))
			{
				ACTION_T = getDistance(ACTION_AUDIO_PACE_UP, ACTION_AUDIO_PACE_MID, shotList.get(i).getAudioPaceValue());
			} else ACTION_T = 0;

			if(HORROR_AUDIO_PACE_LOW > shotList.get(i).getAudioPaceValue())
				HORROR_T = 100;
			else if(isThresholdRange(HORROR_AUDIO_PACE_UP,HORROR_AUDIO_PACE_LOW, shotList.get(i).getAudioPaceValue()))
			{
				HORROR_T = getDistance(HORROR_AUDIO_PACE_UP, HORROR_AUDIO_PACE_MID, shotList.get(i).getAudioPaceValue());
			} else HORROR_T = 0;
			
			if(COMEDY_AUDIO_PACE_UP < shotList.get(i).getAudioPaceValue())
				COMEDY_T = 100;
			else if(isThresholdRange(COMEDY_AUDIO_PACE_UP,COMEDY_AUDIO_PACE_LOW, shotList.get(i).getAudioPaceValue()))
			{
				COMEDY_T = getDistance(COMEDY_AUDIO_PACE_UP, COMEDY_AUDIO_PACE_MID, shotList.get(i).getAudioPaceValue());
			} else COMEDY_T = 0;
			
			if(isThresholdRange(DRAMA_AUDIO_PACE_UP,DRAMA_AUDIO_PACE_LOW, shotList.get(i).getAudioPaceValue()))
			{
				DRAMA_T = getDistance(DRAMA_AUDIO_PACE_UP, DRAMA_AUDIO_PACE_MID, shotList.get(i).getAudioPaceValue());
			} else DRAMA_T = 0;
			
			HORROR_AVERAGE = (HORROR_VD + HORROR_L + HORROR_AE + HORROR_AP + HORROR_T)/5;
			ACTION_AVERAGE = (ACTION_VD + ACTION_FL + ACTION_L + ACTION_AE + ACTION_AP + ACTION_T)/6;
			COMEDY_AVERAGE = (COMEDY_VD + COMEDY_L + COMEDY_AE + COMEDY_AP + COMEDY_T)/5;
			DRAMA_AVERAGE = (DRAMA_VD + DRAMA_L + DRAMA_AE + DRAMA_AP + DRAMA_T)/5;
			
			if(HORROR_AVERAGE > ACTION_AVERAGE && HORROR_AVERAGE > COMEDY_AVERAGE && HORROR_AVERAGE > DRAMA_AVERAGE)
			{
				/*SHOT IS HORROR*/
				shotGenres = shotGenres.append("SHOT " + (i+1) 
						+ " GENRE: "
						+ "HORROR"
						+ System.lineSeparator());
				HORROR_COUNT++;
				classified = true;
			}
			else if(ACTION_AVERAGE > HORROR_AVERAGE && ACTION_AVERAGE > COMEDY_AVERAGE && ACTION_AVERAGE > DRAMA_AVERAGE)
			{
				/*SHOT IS ACTION*/
				shotGenres = shotGenres.append("SHOT " + (i+1) 
								+ " GENRE: "
								+ "ACTION"
								+ System.lineSeparator());
				ACTION_COUNT++;
				classified = true;
			}
			else if(COMEDY_AVERAGE > HORROR_AVERAGE && COMEDY_AVERAGE > ACTION_AVERAGE && COMEDY_AVERAGE > DRAMA_AVERAGE)
			{
				/*SHOT IS COMEDY*/
				shotGenres = shotGenres.append("SHOT " + (i+1) 
						+ " GENRE: "
						+ "COMEDY"
						+ System.lineSeparator());
				COMEDY_COUNT++;
				classified = true;		
			}
			else 
			{
				/*SHOT IS DRAMA*/
				shotGenres = shotGenres.append("SHOT " + (i+1) 
						+ " GENRE: "
						+ "DRAMA"
						+ System.lineSeparator());
				DRAMA_COUNT++;
				classified = true;
			}
		}
		
		shotGenres = shotGenres.append("ACTION=  " + ACTION_COUNT + System.lineSeparator() +
									   "DRAMA=  " + DRAMA_COUNT + System.lineSeparator() +
									   "HORROR=  " + HORROR_COUNT + System.lineSeparator() +
									   "COMEDY=  " + COMEDY_COUNT + System.lineSeparator() );
								
		this.classificationResultsWriter(shotGenres.toString());
	}
	
	public boolean isThresholdRange(double upThresh, double lowThresh, double thresholdValue)
	{
		if(thresholdValue >= lowThresh && thresholdValue <= upThresh)
		{
			return true;
		}
		else return false;
	}
	
	public double getDistance(double upThresh, double midThresh, double value)
	{
		double distance = 0;
		distance = 1 - Math.abs(midThresh - value)/Math.abs(midThresh - upThresh);
		return distance;
	}
	
	/**
	 * Writes the genre results to a text file
	 * @param genreResults
	 */
	public void classificationResultsWriter(String genreResults)
	{
		File resultGenreFile = new File(resultsDirectory.concat(
						"\\GENRE RESULTS FUZZY.txt"));
		
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("mac") >= 0)
		{
			resultGenreFile = new File(resultsDirectory.concat(
					"/GENRE RESULTS FUZZY.txt"));
		}
		
		FileWriter resultGenreFileWriter = null;
		try {
			resultGenreFileWriter = new FileWriter(resultGenreFile.getAbsoluteFile());
			BufferedWriter resultGenreFileBufferedWriter = new BufferedWriter(resultGenreFileWriter);
			resultGenreFileBufferedWriter.write(genreResults);
			resultGenreFileBufferedWriter.close();
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
	}
}
