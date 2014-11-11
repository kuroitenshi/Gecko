package model;

import model.Objects.StreamConsumer;

public class AudialFeatures
{
	
	public void openPraatConnection()
	{
		
		String command = "praatcon  C:\\Users\\Pheebz\\Desktop\\test.praat";
		
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
		
		
	}
}
