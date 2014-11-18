package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Objects.StreamConsumer;
import model.Objects.WavFile;
import model.Objects.WavFileException;

public class AudialFeatures
{
	private String segmentPath;
	private String directoryMain;
	private String praatPath;
	
	public void setFile(String resultPath)
	{
		this.directoryMain = resultPath;
	}
	
	public AudialFeatures(String audialSegment, String praatResult)
	{
		this.segmentPath = audialSegment;
		this.praatPath = praatResult;
	}
	
	/**
	 * Create Praat Scripts for querying Audial energy and Power for all generate .wav files
	 */
	public void producePraatScripts()
	{
		StringBuilder script = new StringBuilder();
		File audialSeg = new File(segmentPath);
		int fileCount = audialSeg.listFiles().length;
		System.out.println("FILES FOLDER: "+audialSeg.toString());
		System.out.println("FILE COUNT: "+fileCount);
		System.out.println("SEGMENT PATH: "+segmentPath);
		System.out.println("PRAAT PATH: "+praatPath);
		System.out.println("TEST ARGS: "+ segmentPath.concat("\\"+1+".wav"));
		
		File praatFile = new File(praatPath.concat("\\Features.praat"));
		File energyFile = new File (praatPath.concat("\\AudialEnergy.txt"));
		File powerFile = new File (praatPath.concat("\\AudialPower.txt"));
		File paceFile = new File(praatPath.concat("\\AudialPace.txt"));
		for(int i = 1; i <= fileCount; i++)
		{			
			script = script.append("Read from file: "+"\""+segmentPath.concat("\\"+i+".wav")+"\"" + "\r\n");			
			script = script.append("energy$ = Get energy: 0, 0" + "\r\n");
			script = script.append("power$ = Get power: 0, 0" + "\r\n");			
			script = script.append("writeInfoLine: "+ "\"" +"Audio Energy= "+"\""+", energy$" + "\r\n");
			script = script.append("writeInfoLine: "+ "\""+"Audio Power= "+"\""+",power$" + "\r\n");			
			script = script.append("appendFileLine: "+"\""+ energyFile.getAbsolutePath() +"\""+", " + "\"Shot " + i + " \"" + "+ energy$"+ "\r\n");			
			script = script.append("appendFileLine: "+"\""+ powerFile.getAbsolutePath() +"\""+", " + "\"Shot " + i + " \"" + "+ power$"+ "\r\n");
			findPeak(i, paceFile, segmentPath);
		}
		
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
							
		executePraatScript(praatFile.getAbsolutePath());
	}
	/**
	 * Finds the peak of an audio file
	 * @param shotNumber
	 * @param paceFile
	 * @param segmentPath2 
	 */
	private void findPeak(int shotNumber, File paceFile, String segmentPath2) {
		// TODO Auto-generated method stub
		try 
		{
			WavFile wavFile = WavFile.openWavFile(new File(segmentPath2.concat("\\"+shotNumber+".wav")));
			double samplesPerPixel = wavFile.getSampleRate()/576; // 576 is from visbounds.width
			ArrayList<Double> distance = new ArrayList<Double>();
			int maxSampleRate = (int)(samplesPerPixel*576);
			int index = 0;
			int previousX = 0;
			int previousY = 0;
			int i = 0;
			
			while(index < maxSampleRate)
			{
				short maxVal = -32767;
				short minVal = 32767;
				
				for(int x = 0; x < samplesPerPixel; x++)
				{
//					maxVal = Math.Max(maxVal, m_Wavefile.Data[x + index]);
//                    minVal = Math.Min(minVal, m_Wavefile.Data[x + index]);
				}
				
				int scaledMinVal = (int)(((minVal + 32768) * 376) / 65536);
				int scaledMaxVal = (int)(((maxVal + 32768) * 376) / 65536);
				
				if(samplesPerPixel > 0.0000000001)
				{
					if(scaledMinVal == scaledMaxVal)
					{
						if(previousY != 0)
						{
							double d = Math.abs(previousX - i) + Math.abs(previousY - maxVal);
							distance.add(d);
						}
					}
					else
					{
						double d = Math.abs(i - i) + Math.abs(minVal - maxVal);
						distance.add(d);
					}
				} else return;
				
				previousX = i;
				previousY = scaledMaxVal;
				i++;
				index = (int)(i * samplesPerPixel);
			}
			
			int numPeaks = 0;
//			string srB;//sr buffer
//            List<int> rawList = new List<int>();
//            int s1 = 0, s2 = 0, s3 = 0;
//
//            while ((srB = sr.ReadLine()) != null)
//            {
//                rawList.Add(Convert.ToInt32(srB));
//            }
//            sr.Close();
//
//            tw.WriteLine("0");
//            int total = 0;
//            for (int k = 0; k < rawList.Count; k++)
//            {
//                s1 = rawList[k];
//                if (k + 1 < rawList.Count)
//                    s2 = rawList[k + 1];
//                if (k + 2 < rawList.Count)
//                    s3 = rawList[k + 2];
//                if (k + 2 > rawList.Count) break;
//
//                if (s1 < s2 && s2 > s3)
//                {
//                    tw.WriteLine(s2);
//                    total++;
//                }
//                else
//                    tw.WriteLine("0");
//            }
//            tw.WriteLine("PEAK: " + total + " TOTAL: " + rawList.Count);
//            tw.Close();
			
			
		} 
		catch (IOException | WavFileException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Execute a command line script for praatcon
	 * @param filepath
	 */
	public void executePraatScript(String filepath)
	{			
		String[] praatScript = new String[] {"praatcon", filepath.replace("\\", "\\\\")};
		  
		try 
		{	
			Runtime runTime = Runtime.getRuntime();
			Process extractionProcess = runTime.exec(praatScript);									
			
			StreamConsumer errorGobbler = new StreamConsumer(extractionProcess.getErrorStream(), "ERROR");            
			StreamConsumer outputGobbler = new StreamConsumer(extractionProcess.getInputStream(), "OUTPUT");
			
	        errorGobbler.start();
	        outputGobbler.start();
	        int exitVal = extractionProcess.waitFor();
            System.out.println("ExitValue: " + exitVal);   
            if(exitVal == 0)
            {
            	System.out.println("Praat Execution Successful");
            }
      
		} catch (Throwable e) 
		{			
			e.printStackTrace();
		}
	}
}
