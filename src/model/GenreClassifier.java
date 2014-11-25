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
	public final double ACTION_FLAME_PERCENTAGE = 0.0;
	public final double ACTION_VISUAL_DISTURBANCE = 0.0;
	public final double ACTION_AUDIO_ENERGY = 0.0;
	public final double ACTION_AUDIO_PACE= 0.0;
	

	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double HORROR_VISUAL_DISTURBANCE = 0.0;
	public final double HORROR_AUDIO_ENERGY = 0.0;

	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double COMEDY_LUMINANCE = 0.0;
	public final double COMEDY_AUDIO_POWER = 0.0;
	
	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double DRAMA_AUDIO_POWER = 0.0;
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
		for(int i=0; i < this.shotList.size(); i++)
		{
			if(shotList.get(i).getFlamePercentageValue() >= ACTION_FLAME_PERCENTAGE)
			{
				if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY && shotList.get(i).getAudioPaceValue() >= ACTION_AUDIO_PACE)
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
									+ " GENRE: "
									+ "ACTION"
									+ System.lineSeparator());
				}
				
			}else if(shotList.get(i).getVisualDisturbanceValue() >= HORROR_VISUAL_DISTURBANCE || shotList.get(i).getVisualDisturbanceValue() >= ACTION_VISUAL_DISTURBANCE) 
			{
				if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY)
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "ACTION"
							+ System.lineSeparator());
					
				}else if(shotList.get(i).getAudioEnergyValue() >= HORROR_AUDIO_ENERGY)
				{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
				}
				
			}else if(shotList.get(i).getLuminanceValue() >= COMEDY_LUMINANCE && shotList.get(i).getAudioPowerValue() >= COMEDY_AUDIO_POWER)
			{
					/*SHOT IS COMEDY*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "COMEDY"
							+ System.lineSeparator());
								
			}else if(shotList.get(i).getAudioEnergyValue() >= HORROR_AUDIO_ENERGY)
			{
					/*SHOT IS HORROR*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "HORROR"
							+ System.lineSeparator());
			}else if(shotList.get(i).getAudioPowerValue() >= DRAMA_AUDIO_POWER && shotList.get(i).getAudioPaceValue() >= DRAMA_AUDIO_PACE)
			{
					/*SHOT IS DRAMA*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ "DRAMA"
							+ System.lineSeparator());
			}else
			{
					/*SHOT IS NEUTRAL - Gecko team Note: Leave blank first*/
					shotGenres = shotGenres.append("SHOT " + (i+1) 
							+ " GENRE: "
							+ " "
							+ System.lineSeparator());
			}
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
