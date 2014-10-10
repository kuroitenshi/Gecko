package model;

import java.io.File;
import model.Objects.StreamConsumer;

public class FrameExtraction 
{
	private File movieFile;
	private String framesPath;
	private String visualDataPath;
	private String audialDataPath;
	

	private String parentResultPath;
	
	/**
	 * Extract 720p JPEG images from movie input
	 */
	public void extractImages()
	{
		String movieFilePath = "\"" + this.movieFile.getAbsolutePath() + "\"";			
		
		//Put up File does not exist err capture
		setupResultsFolder();
		String command = "ffmpeg -i " + movieFilePath + " -r 16 -s film -f image2 " + "\"" + framesPath + "\\%d.jpeg\"";
		
		try 
		{	
			Runtime runTime = Runtime.getRuntime();
			Process extractionProcess = runTime.exec(command);									
			
			StreamConsumer errorGobbler = new StreamConsumer(extractionProcess.getErrorStream(), "ERROR");            
			StreamConsumer outputGobbler = new StreamConsumer(extractionProcess.getInputStream(), "OUTPUT");
			
	        errorGobbler.start();
	        outputGobbler.start();
	        int exitVal = extractionProcess.waitFor();
            System.out.println("ExitValue: " + exitVal);   
		} catch (Throwable e) 
		{			
			e.printStackTrace();
		}
		
		
		System.out.println("Extraction Completed");
		
	}
	
	
	/**
	 * Gets the path of the movie file to create the results folders
	 */
	public void setupResultsFolder()
	{
		String movieFileParentDirectory = this.movieFile.getParent().replace("\\", "/");
		String folderName = this.movieFile.getName().substring(0,this.movieFile.getName().lastIndexOf('.'));
		
		File resultsFolder = new File(movieFileParentDirectory + "/" + folderName);
		setParentResultPath(resultsFolder.getAbsolutePath());
		resultsFolder.mkdir();
		
		
		File framesFolder = new File(resultsFolder.getAbsolutePath() + "/Frames");
		setFramesPath(framesFolder.getAbsolutePath());
		framesFolder.mkdir();
		
		
		File visualDataFolder = new File(resultsFolder.getAbsolutePath() + "/Visual Data");
		setVisualDataPath(visualDataFolder.getAbsolutePath());
		visualDataFolder.mkdir();
		
		File audialDataFolder = new File(resultsFolder.getAbsolutePath() + "/Audial Data");		
		setAudialDataPath(audialDataFolder.getAbsolutePath());
		audialDataFolder.mkdir();
	}
	

	public File getMovieFile() 
	{
		return movieFile;
	}

	public void setMovieFile(File movieFile) 
	{
		this.movieFile = movieFile;
	}
	
	public String getFramesPath() 
	{
		return framesPath;
	}

	public void setFramesPath(String framesPath) 
	{
		this.framesPath = framesPath;
	}

	public String getVisualDataPath() 
	{
		return visualDataPath;
	}

	public void setVisualDataPath(String visualDataPath) 
	{
		this.visualDataPath = visualDataPath;
	}

	public String getAudialDataPath() 
	{
		return audialDataPath;
	}

	public void setAudialDataPath(String audialDataPath) 
	{
		this.audialDataPath = audialDataPath;
	}

	public String getParentResultPath()
	{
		return parentResultPath;
	}

	public void setParentResultPath(String parentResultPath) 
	{
		this.parentResultPath = parentResultPath;
	}

	

}
