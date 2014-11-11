package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AudialFeatures
{
	private String segmentPath;
	private String directoryMain;
	private String praatPath;
	
	public void setFile(String resultPath)
	{
		this.directoryMain = resultPath;
	}
	
	public AudialFeatures(String audialSegment, String praatResult) {
		this.segmentPath = audialSegment;
		this.praatPath = praatResult;
	}
	
	public void producePraatFile()
	{
		StringBuilder script = new StringBuilder();
		File audialSeg = new File(segmentPath);
		int fileCount = audialSeg.listFiles().length;
		System.out.println("FILES FOLDER: "+audialSeg.toString());
		System.out.println("FILE COUNT: "+fileCount);
		System.out.println("SEGMENT PATH: "+segmentPath);
		System.out.println("PRAAT PATH: "+praatPath);
		System.out.println("TEST ARGS: "+ segmentPath.concat("\\"+1+".wav"));
		for(int i = 1; i <= fileCount; i++)
		{
			File praatFile = new File(praatPath.concat("\\"+i+".praat"));
			script = script.append("Read from file: "+"\""+segmentPath.concat("\\"+i+".wav")+"\"" + "\r\n");
			script = script.append("energy$ = Get energy: 0, 0" + "\r\n");
			script = script.append("power$ = Get power: 0, 0" + "\r\n");
			script = script.append("writeInfoLine: "+ "\"" +"Audio Energy="+"\""+", energy$" + "\r\n");
			script = script.append("writeInfoLine: "+ "\""+"Audio Power="+"\""+",power$" + "\r\n");
			script = script.append("writeFileLine: "+ "\""+praatFile.getAbsolutePath().concat("\\"+i+".txt")+"\""+", energy$"+ "\r\n");
			script = script.append("appendFileLine: "+"\""+praatFile.getAbsolutePath().concat("\\"+i+".txt")+"\""+", power$"+ "\r\n");
			
			FileWriter praatWriter = null;
	    	try 
			{
				praatWriter = new FileWriter(praatFile.getAbsoluteFile());			
				
				BufferedWriter praatBuffWrite = new BufferedWriter(praatWriter);
		    	
		    	praatBuffWrite.write(script.toString());
				praatBuffWrite.close();
			} 
	    	catch (IOException e) 
			{
				e.printStackTrace();
			};
			System.out.println("CURRENT WAV: "+segmentPath.concat("\\"+i+".wav"));
			System.out.println("PRAAT ABSOLUTE: "+praatFile.getAbsolutePath());
		}

	}
	
	
}
