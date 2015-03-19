package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenreClassifier 
{
	
	public int HORROR_count = 0;
	public int COMEDY_count = 0;
	public int ACTION_count = 0;
	public int DRAMA_count = 0;
	public int NEUTRAL_count = 0;
	
	
	private ArrayList<Shot> shotList;
	private String resultsDirectory;
	/*-----------------------------------------------*/
	/*-------------ACTION CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double ACTION_FLAME_PERCENTAGE = 0.694210387; //OLD Not updated 
	public final double ACTION_VISUAL_DISTURBANCE = 0.048394148; //LOWER
	public final double ACTION_AUDIO_ENERGY = 0.002525;// ADJUSTED VARIANCE
	public final double ACTION_AUDIO_PACE= 85; //UPPER 
	

	
	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double HORROR_VISUAL_DISTURBANCE =  0.005668848; //VARIANCE 
	public final double HORROR_AUDIO_ENERGY = 0.023435068; //VARIANCE
	public final double HORROR_LUMINANCE = 25; //ADJUSTED

	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double COMEDY_LUMINANCE = 42.33017011; //LOWER

	
	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double DRAMA_AUDIO_POWER = 0.001650383; // UPPER

	
	
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
			if(shotList.get(i).getFlamePercentageValue() >= ACTION_FLAME_PERCENTAGE  || shotList.get(i).getVisualDisturbanceValue() >= ACTION_VISUAL_DISTURBANCE && classified == false)
			{
				
				if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY  && shotList.get(i).getAudioPaceValue() >= ACTION_AUDIO_PACE)
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
			
			if(shotList.get(i).getVisualDisturbanceValue() >= HORROR_VISUAL_DISTURBANCE && classified == false ) 
			{
				
				if(shotList.get(i).getAudioEnergyValue() >= HORROR_AUDIO_ENERGY)
				{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
					HORROR_count++;
					classified = true;
				}
				else if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY )
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
			
			if(shotList.get(i).getLuminanceValue() >= COMEDY_LUMINANCE  && classified == false)
			{
				
				if(shotList.get(i).getAudioPowerValue() > DRAMA_AUDIO_POWER )
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
			
			if(shotList.get(i).getLuminanceValue() < HORROR_LUMINANCE && classified == false)
			{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
					HORROR_count++;
					classified = true;
					
			}	
			if(shotList.get(i).getAudioPowerValue() >= 0.000174287 &&  shotList.get(i).getAudioPowerValue() < DRAMA_AUDIO_POWER && classified == false)
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