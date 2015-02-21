package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenreClassifier 
{
	private ArrayList<Shot> shotList;
	private String resultsDirectory;
	/*-----------------------------------------------*/
	/*-------------ACTION CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double ACTION_FLAME_PERCENTAGE = 0.694210387;
	public final double ACTION_VISUAL_DISTURBANCE = 0.096804224;
	public final double ACTION_AUDIO_ENERGY = 0.106805809;
	public final double ACTION_AUDIO_PACE= 0.0;
	

	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double HORROR_VISUAL_DISTURBANCE =  0.096804224; //SUBJECT TO CHANGE (Not yet Computed)
	public final double HORROR_AUDIO_ENERGY = 0.097566871;

	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double COMEDY_LUMINANCE = 75.75177648;
	//FOR NEW SEG = 78.59996361
	public final double COMEDY_AUDIO_POWER = 0.004889318;
	//MP3 = 0.004016932
	
	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double DRAMA_AUDIO_POWER = 0.002071157;
	public final double DRAMA_AUDIO_PACE= 0.0;
	
	
	public GenreClassifier(ArrayList<Shot> shotList, String resultsDirectory)	
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
		for(int i=0; i < this.shotList.size(); i++)
		{
			if(shotList.get(i).getFlamePercentageValue() >= ACTION_FLAME_PERCENTAGE && classified == false)
			{
				
				//shotList.get(i).getAudioPaceValue() >= ACTION_AUDIO_PACE
				if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY )
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
									+ " GENRE: "
									+ "ACTION"
									+ System.lineSeparator());
					classified = true;
				}
			}
			
			if(shotList.get(i).getVisualDisturbanceValue() >= HORROR_VISUAL_DISTURBANCE || shotList.get(i).getVisualDisturbanceValue() >= ACTION_VISUAL_DISTURBANCE && classified == false ) 
			{
				
				if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY)
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "ACTION"
							+ System.lineSeparator());
					classified = true;
					
				}else if(shotList.get(i).getAudioEnergyValue() >= HORROR_AUDIO_ENERGY)
				{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
					classified = true;
				}
				
			}
			
			if(shotList.get(i).getLuminanceValue() >= COMEDY_LUMINANCE && shotList.get(i).getAudioPowerValue() >= COMEDY_AUDIO_POWER  && classified == false)
			{
					/*SHOT IS COMEDY*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "COMEDY"
							+ System.lineSeparator());
					classified = true;
								
			}
			
			if(shotList.get(i).getAudioEnergyValue() >= HORROR_AUDIO_ENERGY && classified == false)
			{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
					classified = true;
					//shotList.get(i).getAudioPaceValue() >= DRAMA_AUDIO_PACE
			}
			
			if(shotList.get(i).getAudioPowerValue() >= DRAMA_AUDIO_POWER && classified == false)
			{
					/*SHOT IS DRAMA*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "DRAMA"
							+ System.lineSeparator());
					classified = true;
			}
			if(classified == false)
			{
					/*SHOT IS NEUTRAL - Gecko team Note: Leave blank first*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ " NEUTRAL"
							+ System.lineSeparator());
					classified = true;
			}
			
			classified = false;
		}
		
		this.classificationResultsWriter(shotGenres.toString());
	}
	
	/**
	 * Writes the genre results to a text file
	 * @param genreResults
	 */
	public void classificationResultsWriter(String genreResults)
	{
		File resultGenreFile = new File(resultsDirectory.concat(
						"\\GENRE RESULTS.txt"));
		
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("mac") >= 0)
		{
			resultGenreFile = new File(resultsDirectory.concat(
					"/GENRE RESULTS.txt"));
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
