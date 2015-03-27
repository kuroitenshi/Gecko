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
	public int HORROR_frames = 0;
	public int COMEDY_frames = 0;
	public int ACTION_frames = 0;
	public int DRAMA_frames = 0;
	public int NEUTRAL_frames = 0;
	private ArrayList<Shot> shotList;
	private String resultsDirectory;
	/*-----------------------------------------------*/
	/*-------------ACTION CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double ACTION_FLAME_PERCENTAGE = 0.694210387; //OLD Not updated
	public final double ACTION_VISUAL_DISTURBANCE = 0.023431134; //LOWER
	public final double ACTION_AUDIO_ENERGY = 0.00121;// ADJUSTED VARIANCE
	public final double ACTION_AUDIO_PACE= 89.24806197; //UPPER
	
	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	
	public final double HORROR_VISUAL_DISTURBANCE = 0.1150001995; //
	public final double HORROR_AUDIO_ENERGY = 0.0138572405; //
	public final double HORROR_LUMINANCE = 50; //ADJUSTED
	
	
	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/
	
	public final double COMEDY_LUMINANCE = 84; //LOWER
	
	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double DRAMA_AUDIO_POWER = 0.000587166; // UPPER
	
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
			if(shotList.get(i).getFlamePercentageValue() >= ACTION_FLAME_PERCENTAGE || shotList.get(i).getVisualDisturbanceValue() >= ACTION_VISUAL_DISTURBANCE && classified == false)
			{
				if(shotList.get(i).getAudioEnergyValue() >= ACTION_AUDIO_ENERGY || shotList.get(i).getAudioPaceValue() >= ACTION_AUDIO_PACE)
				{
					/*SHOT IS ACTION*/
					shotGenres = shotGenres.append("SHOT " + (i+1)
							+ " GENRE: "
							+ "ACTION"
							+ System.lineSeparator());
					ACTION_count++;
					ACTION_frames+= shotList.get(i).getFrameList().size();
					//shotList.get(i).classification = "Action";
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
					HORROR_frames+= shotList.get(i).getFrameList().size();
					//shotList.get(i).classification = "Horror";
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
					ACTION_frames+= shotList.get(i).getFrameList().size();
					//shotList.get(i).classification = "Action";
					classified = true;
				}
			}
			if(shotList.get(i).getLuminanceValue() >= COMEDY_LUMINANCE && classified == false)
			{
				if(shotList.get(i).getAudioPowerValue() > DRAMA_AUDIO_POWER )
				{
					/*SHOT IS COMEDY*/
					shotGenres = shotGenres.append("SHOT " + (i+1)
							+ " GENRE: "
							+ "COMEDY"
							+ System.lineSeparator());
					COMEDY_count++;
					COMEDY_frames+= shotList.get(i).getFrameList().size();
					//shotList.get(i).classification = "Comedy";
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
				HORROR_frames+= shotList.get(i).getFrameList().size();
				//shotList.get(i).classification = "Horror";
				classified = true;
			}
			if(shotList.get(i).getAudioPowerValue() < 0.00221 && classified == false)
			{
				/*SHOT IS DRAMA*/
				shotGenres = shotGenres.append("SHOT " + (i+1)
						+ " GENRE: "
						+ "DRAMA"
						+ System.lineSeparator());
				DRAMA_count++;
				DRAMA_frames+= shotList.get(i).getFrameList().size();
				//shotList.get(i).classification = "Drama";
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
				NEUTRAL_frames+= shotList.get(i).getFrameList().size();
				//shotList.get(i).classification = "Neutral";
				classified = true;
			}
			classified = false;
			}
		
			shotGenres = shotGenres.append("NUMBER OF SHOTS CLASSIFIED: " + System.lineSeparator() +
					"NEUTRAL= " + NEUTRAL_count + System.lineSeparator() +
					"ACTION= " + ACTION_count + System.lineSeparator() +
					"DRAMA= " + DRAMA_count + System.lineSeparator() +
					"HORROR= " + HORROR_count + System.lineSeparator() +
					"COMEDY= " + COMEDY_count + System.lineSeparator() + System.lineSeparator());
			
			shotGenres = shotGenres.append("NUMBER OF FRAMES CLASSIFIED: "+ System.lineSeparator() + 
					"NEUTRAL= " + NEUTRAL_frames + System.lineSeparator() +
					"ACTION= " + ACTION_frames + System.lineSeparator() +
					"DRAMA= " + DRAMA_frames + System.lineSeparator() +
					"HORROR= " + HORROR_frames + System.lineSeparator() +
					"COMEDY= " + COMEDY_frames + System.lineSeparator() );
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