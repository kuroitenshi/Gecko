package model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

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
	public double ACTION_FLAME_PERCENTAGE ;
	public double ACTION_VISUAL_DISTURBANCE ;
	public double ACTION_AUDIO_ENERGY ;
	public double ACTION_AUDIO_PACE;
	
	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	
	public double HORROR_VISUAL_DISTURBANCE; 
	public double HORROR_AUDIO_ENERGY ;
	public double HORROR_LUMINANCE ;
	
	
	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/
	
	public double COMEDY_LUMINANCE;
	
	/*-----------------------------------------------*/
	/*-------------DRAMA CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public double DRAMA_AUDIO_POWER;
	public double DRAMA_AUDIO_POWER_END;
	
	public GenreClassifier(ArrayList<Shot> shotList, String resultsDirectory)
	{
		this.shotList = shotList;
		this.resultsDirectory = resultsDirectory;
		this.getThresholdFromResource();
	}
	
	
	/**
	 * Initializes Threshold values from a properties file
	 * @throws IOException
	 */
	public void getThresholdFromResource()
	{
		
		Properties prop = new Properties();
		String propertiesFileName = "config.properties";
 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
 
		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				throw new FileNotFoundException("property file '" + propertiesFileName + "' not found in the classpath");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
		this.ACTION_FLAME_PERCENTAGE = Double.parseDouble(prop.getProperty("ACTION_FLAME_PERCENTAGE"));
		this.ACTION_VISUAL_DISTURBANCE = Double.parseDouble(prop.getProperty("ACTION_VISUAL_DISTURBANCE"));
		this.ACTION_AUDIO_ENERGY = Double.parseDouble(prop.getProperty("ACTION_AUDIO_ENERGY"));
		this.ACTION_AUDIO_PACE = Double.parseDouble(prop.getProperty("ACTION_AUDIO_PACE"));
		this.HORROR_VISUAL_DISTURBANCE = Double.parseDouble(prop.getProperty("HORROR_VISUAL_DISTURBANCE"));
		this.HORROR_AUDIO_ENERGY = Double.parseDouble(prop.getProperty("HORROR_AUDIO_ENERGY"));
		this.HORROR_LUMINANCE = Double.parseDouble(prop.getProperty("HORROR_LUMINANCE"));
		this.COMEDY_LUMINANCE = Double.parseDouble(prop.getProperty("COMEDY_LUMINANCE"));
		this.DRAMA_AUDIO_POWER = Double.parseDouble(prop.getProperty("DRAMA_AUDIO_POWER"));
		this.DRAMA_AUDIO_POWER_END = Double.parseDouble(prop.getProperty("DRAMA_AUDIO_POWER_END"));
	}
	
	/**
	* Classifies the Movie based on the Gecko feature decision tree (Refer to documentation)
	*/
	public void classifyMovieGenre()
	{
		System.out.println("CLASSIFIYING AUDIAL + VISUAL");
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
					shotList.get(i).classification = "Action";
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
					shotList.get(i).classification = "Horror";
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
					shotList.get(i).classification = "Action";
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
					shotList.get(i).classification = "Comedy";
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
				shotList.get(i).classification = "Horror";
				classified = true;
			}
			if(shotList.get(i).getAudioPowerValue() < DRAMA_AUDIO_POWER_END && classified == false)
			{
				/*SHOT IS DRAMA*/
				shotGenres = shotGenres.append("SHOT " + (i+1)
						+ " GENRE: "
						+ "DRAMA"
						+ System.lineSeparator());
				DRAMA_count++;
				DRAMA_frames+= shotList.get(i).getFrameList().size();
				shotList.get(i).classification = "Drama";
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
				shotList.get(i).classification = "Neutral";
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
					"\\GENRE RESULTS-NEW.txt"));
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("mac") >= 0)
			{
				resultGenreFile = new File(resultsDirectory.concat(
						"/GENRE RESULTS-NEW.txt"));
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