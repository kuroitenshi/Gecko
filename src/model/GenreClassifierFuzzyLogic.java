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
	
	public final double ACTION_AUDIO_ENERGY_UP = 0.02020;//upper
	public final double ACTION_AUDIO_ENERGY_LOW = 0.00121;//lower
	public final double ACTION_AUDIO_ENERGY_MID = 0.0107;// ADJUSTED VARIANCE
	
	public final double ACTION_AUDIO_PACE_UP= 138; //upper
	public final double ACTION_AUDIO_PACE_LOW= 89; //lower
	public final double ACTION_AUDIO_PACE_MID= 113; //ave
	

	
	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/

	public final double HORROR_VISUAL_DISTURBANCE_UP =  0.448801645; //upper 
	public final double HORROR_VISUAL_DISTURBANCE_LOW =  0.021292403 ; //lower
	public final double HORROR_VISUAL_DISTURBANCE_MID =  0.235047024; //VARIANCE
	
	public final double HORROR_AUDIO_ENERGY_UP = 0.037900; //upper
	public final double HORROR_AUDIO_ENERGY_LOW = 0.004744383; //lower
	public final double HORROR_AUDIO_ENERGY_MID = 0.0213221915; //VARIANCE
	
	public final double HORROR_LUMINANCE_UP = 50; //upper
	public final double HORROR_LUMINANCE_LOW = 14; //lower
	public final double HORROR_LUMINANCE_MID = 32; //ADJUSTED

	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/

	public final double COMEDY_LUMINANCE_UP = 84; //upper
	public final double COMEDY_LUMINANCE_MID = 57; //mid
	public final double COMEDY_LUMINANCE_LOW = 30; //lower

	
	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/

	public final double DRAMA_AUDIO_POWER_UP = 0.00221; //upper
	public final double DRAMA_AUDIO_POWER_LOW = 0.000444905; //lower
	public final double DRAMA_AUDIO_POWER_MID = 0.0013274525; //mid


	
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
		int HORROR_count = 0;
		int COMEDY_count = 0;
		int ACTION_count = 0;
		int DRAMA_count = 0;
		int NEUTRAL_count = 0;
		for(int i=0; i < this.shotList.size(); i++)
		{
			
			if(thresholdJudge(ACTION_FLAME_PERCENTAGE_UP,ACTION_FLAME_PERCENTAGE_LOW, ACTION_FLAME_PERCENTAGE_MID, shotList.get(i).getFlamePercentageValue()) == true || thresholdJudge(ACTION_VISUAL_DISTURBANCE_UP, ACTION_VISUAL_DISTURBANCE_LOW, ACTION_VISUAL_DISTURBANCE_MID, shotList.get(i).getVisualDisturbanceValue()) == true && classified == false)
			{
				
				if(thresholdJudge(ACTION_AUDIO_ENERGY_UP, ACTION_AUDIO_ENERGY_LOW, ACTION_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue()) == true && thresholdJudge(ACTION_AUDIO_PACE_UP, ACTION_AUDIO_PACE_LOW, ACTION_AUDIO_PACE_MID, shotList.get(i).getAudioPaceValue()) == true)
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
									+ " GENRE: "
									+ "ACTION"
									+ System.lineSeparator());
					ACTION_count++;
					classified = true;
				}
			}
			
			if(thresholdJudge( HORROR_VISUAL_DISTURBANCE_UP,  HORROR_VISUAL_DISTURBANCE_LOW,  HORROR_VISUAL_DISTURBANCE_MID, shotList.get(i).getVisualDisturbanceValue()) == true&& classified == false ) 
			{
				
				if(thresholdJudge(HORROR_AUDIO_ENERGY_UP, HORROR_AUDIO_ENERGY_LOW, HORROR_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue()) == true)
				{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
					HORROR_count++;
					classified = true;
				}
				else if(thresholdJudge(ACTION_AUDIO_ENERGY_UP, ACTION_AUDIO_ENERGY_LOW, ACTION_AUDIO_ENERGY_MID, shotList.get(i).getAudioEnergyValue()) == true )
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "ACTION"
							+ System.lineSeparator());
					ACTION_count++;
					classified = true;
					
				}
				
			}
			
			if(thresholdJudge(COMEDY_LUMINANCE_UP, COMEDY_LUMINANCE_LOW, COMEDY_LUMINANCE_MID, shotList.get(i).getLuminanceValue()) == true  && classified == false)
			{
				
				if(thresholdJudge(DRAMA_AUDIO_POWER_UP, DRAMA_AUDIO_POWER_LOW, DRAMA_AUDIO_POWER_MID, shotList.get(i).getAudioPowerValue()) == true )
				{
					/*SHOT IS COMEDY*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "COMEDY"
							+ System.lineSeparator());
					COMEDY_count++;
					classified = true;		
				
				}
										
			}
			
			if(thresholdJudge(HORROR_LUMINANCE_UP, HORROR_LUMINANCE_LOW, HORROR_LUMINANCE_MID, shotList.get(i).getLuminanceValue()) == true && classified == false)
			{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
					HORROR_count++;
					classified = true;
					
			}	
			if(thresholdJudge(DRAMA_AUDIO_POWER_UP, DRAMA_AUDIO_POWER_LOW, DRAMA_AUDIO_POWER_MID, shotList.get(i).getAudioPowerValue())==true  && classified == false)
			{
					/*SHOT IS DRAMA*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "DRAMA"
							+ System.lineSeparator());
					DRAMA_count++;
					classified = true;
			}
			
			
			
			if(classified == false)
			{
					/*SHOT IS NEUTRAL */
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "NEUTRAL"
							+ System.lineSeparator());
					NEUTRAL_count++;
					classified = true;
			}
			
			classified = false;
			
		}
		shotGenres = shotGenres.append("NEUTRAL=  " + NEUTRAL_count + System.lineSeparator() +
									   "ACTION=  " + ACTION_count + System.lineSeparator() +
									   "DRAMA=  " + DRAMA_count + System.lineSeparator() +
									   "HORROR=  " + HORROR_count + System.lineSeparator() +
									   "COMEDY=  " + COMEDY_count + System.lineSeparator() );
								
		this.classificationResultsWriter(shotGenres.toString());
	}
	
	public boolean thresholdJudge(double upThresh, double lowThresh, double midThresh, double feat)
	{
		//Differences of questioned value with its respective upper, lower and mid thresholds.
		double upRes =   Math.abs(upThresh - feat);
		double lowRes = Math.abs(lowThresh - feat);
		double midRes = Math.abs(midThresh - feat);
		
		//If the  mid threshold differences are lower than upper and lower thresholds, the value passes.
		if(upRes > midRes && lowRes > midRes)
			return true;
		else
			return false;
	}
	
	/**
	 * Writes the genre results to a text file
	 * @param genreResults
	 */
	public void classificationResultsWriter(String genreResults)
	{
		File resultGenreFile = new File(resultsDirectory.concat(
						"\\GENRE RESULTS-FUZZY LOGIC.txt"));
		
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("mac") >= 0)
		{
			resultGenreFile = new File(resultsDirectory.concat(
					"/GENRE RESULTS-FUZZY LOGIC.txt"));
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
