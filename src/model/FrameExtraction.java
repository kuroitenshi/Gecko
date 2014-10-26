package model;

import java.io.File;
import model.Objects.StreamConsumer;

public class FrameExtraction 
{
	private File movieFile;
	private String framesPath;
	private String visualDataPath;
	private String audialDataPath;
	private String audialSegPath;
	

	private String parentResultPath;
	
	/**
	 * Extract JPEG images from movie input (16 fps)
	 */
	public void extractImages()
	{
		String movieFilePath = "\"" + this.movieFile.getAbsolutePath() + "\"";			
		setupResultsFolder();
		// Add -s film for testing
		
		String command = "";
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0){
			System.out.println("OS: Windows");
			command = "ffmpeg -i " + movieFilePath + " -r 7 -f image2 " + "\"" + framesPath + "\\%d.jpeg\"";
		}
		else if (OS.indexOf("mac") >= 0) {
			System.out.println("OS: Mac");
			command = "ffmpeg -i " + movieFilePath + " -r 7 -f image2 \"" + framesPath + "/%d.jpeg\"";
			System.out.println(command);

		}
		
		
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
		
		File audialSegFolder = new File(resultsFolder.getAbsolutePath() + "/Audial Data/Segments");
		setAudialSegPath(audialSegFolder.getAbsolutePath());
		audialSegFolder.mkdir();
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
	
	public String getAudialSegPath()
	{
		return audialSegPath;
	}

	public void setAudialSegPath(String audialSegPath)
	{
		this.audialSegPath = audialSegPath;
	}

	

}
