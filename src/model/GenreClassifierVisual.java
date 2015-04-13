package model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenreClassifierVisual
{
	public int HORROR_count = 0;
	public int COMEDY_count = 0;
	public int ACTION_count = 0;
	public int DRAMA_count = 0;
	public int HORROR_frames = 0;
	public int COMEDY_frames = 0;
	public int ACTION_frames = 0;
	public int DRAMA_frames = 0;
	
	private ArrayList<Shot> shotList;
	private String resultsDirectory;
	/*-----------------------------------------------*/
	/*-------------ACTION CONSTANTS------------------*/
	/*-----------------------------------------------*/
	public final double ACTION_FLAME_PERCENTAGE = 0.694210387; //OLD Not updated
	public final double ACTION_VISUAL_DISTURBANCE = 0.023431134; //LOWER
	
	/*-----------------------------------------------*/
	/*-------------HORROR CONSTANTS------------------*/
	/*-----------------------------------------------*/
	
	public final double HORROR_VISUAL_DISTURBANCE = 0.021292403; //
	public final double HORROR_LUMINANCE = 50; //ADJUSTED
	
	
	/*-----------------------------------------------*/
	/*-------------COMEDY CONSTANTS------------------*/
	/*-----------------------------------------------*/
	
	public final double COMEDY_LUMINANCE = 84; //LOWER
	
	
	public GenreClassifierVisual(ArrayList<Shot> shotList, String resultsDirectory)
	{
		this.shotList = shotList;
		this.resultsDirectory = resultsDirectory;
	}
	
	/**
	* Classifies the Movie based on the Gecko feature decision tree (Refer to documentation)
	*/
	public void classifyMovieGenre()
	{
		System.out.println("CLASSIFYING VISUAL ONLY");
		StringBuilder shotGenres = new StringBuilder();
		boolean classified = false;
		for(int i=0; i < this.shotList.size(); i++)
		{
			if(shotList.get(i).getFlamePercentageValue() >= ACTION_FLAME_PERCENTAGE || shotList.get(i).getVisualDisturbanceValue() >= ACTION_VISUAL_DISTURBANCE && classified == false)
			{
				shotGenres = shotGenres.append("SHOT " + (i+1)
							+ " GENRE: "
							+ "ACTION"
							+ System.lineSeparator());
				ACTION_count++;
				ACTION_frames+= shotList.get(i).getFrameList().size();
				//shotList.get(i).classification = "Action";
				classified = true;
			}
			if(shotList.get(i).getVisualDisturbanceValue() >= HORROR_VISUAL_DISTURBANCE && classified == false )
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
			if(shotList.get(i).getLuminanceValue() >= COMEDY_LUMINANCE && classified == false)
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
			if(classified == false)
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
			classified = false;
			}
			shotGenres = shotGenres.append("NUMBER OF SHOTS CLASSIFIED: " + System.lineSeparator() +
					"ACTION= " + ACTION_count + System.lineSeparator() +
					"DRAMA= " + DRAMA_count + System.lineSeparator() +
					"HORROR= " + HORROR_count + System.lineSeparator() +
					"COMEDY= " + COMEDY_count + System.lineSeparator() + System.lineSeparator());
			
			shotGenres = shotGenres.append("NUMBER OF FRAMES CLASSIFIED: "+ System.lineSeparator() + 
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
					"\\GENRE RESULTS VISUAL.txt"));
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("mac") >= 0)
			{
				resultGenreFile = new File(resultsDirectory.concat(
						"/GENRE RESULTS VISUAL.txt"));
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