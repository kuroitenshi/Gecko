package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import view.GUI;
import model.AudialSegmentation;
import model.AudioExtraction;
import model.FrameExtraction;
import model.Segmentation;

public class GeckoController 
{
	FrameExtraction extractionModel;
	GUI geckoView;
	
	public GeckoController(FrameExtraction extractionModel, GUI geckoView)
	{
		this.extractionModel = extractionModel;
		this.geckoView = geckoView;		
		setActionListeners();
	}
	public void setActionListeners()
	{
		geckoView.setNewButtonActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 7a533b123d2164c5ad3e577bf1a2d76069b3b49b
				
				
					File movieFileChosen = new File(geckoView.getFilepath());
					
					FrameExtraction frameExtractor = new FrameExtraction();
					frameExtractor.setMovieFile(movieFileChosen);
					frameExtractor.extractImages();
					
					System.out.println(frameExtractor.getParentResultPath());
					System.out.println(frameExtractor.getFramesPath());
					Segmentation movieSegmentation = new Segmentation(frameExtractor.getFramesPath(), frameExtractor.getParentResultPath());
					movieSegmentation.segmentMovie();
					
					AudioExtraction audioExtractor = new AudioExtraction(frameExtractor.getParentResultPath());
					audioExtractor.setFile(movieFileChosen);
					audioExtractor.extractAudio();
					
					AudialSegmentation audialSeg = new AudialSegmentation(frameExtractor.getAudialDataPath());
					audialSeg.setFile(frameExtractor.getParentResultPath());
=======
<<<<<<< HEAD
=======
>>>>>>> parent of d96f079... Added skeleton for Luminance Computation
=======
>>>>>>> 7a533b123d2164c5ad3e577bf1a2d76069b3b49b
								
					FileFinderFrame fileFinder = new FileFinderFrame();
					String filepath = fileFinder.getFilepath();
					fileFinder.dispose();
					File movieFileChosen = new File(filepath);
					ArrayList<Shot> shots = new ArrayList<Shot>();
										
					extractionModel.setMovieFile(movieFileChosen);
					extractionModel.extractImages();
					
					System.out.println(extractionModel.getParentResultPath());
					System.out.println(extractionModel.getFramesPath());
					Segmentation movieSegmentation = new Segmentation(extractionModel.getFramesPath(), extractionModel.getParentResultPath());
					movieSegmentation.segmentMovie();
					
					for(int i = 1; i <= movieSegmentation.getShotNumber(); i++)
					{
						Shot shot = new Shot(i, extractionModel.getVisualDataPath() + "\\ShotRange.txt", extractionModel.getFramesPath());
						shots.add(shot);
					}
					
					AudioExtraction audioExtractor = new AudioExtraction(extractionModel.getParentResultPath());
					audioExtractor.setFile(movieFileChosen);
					audioExtractor.extractAudio();
					
					AudialSegmentation audialSeg = new AudialSegmentation(extractionModel.getAudialDataPath());
					audialSeg.setFile(extractionModel.getParentResultPath());
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> parent of d96f079... Added skeleton for Luminance Computation
=======
=======
>>>>>>> 7a533b123d2164c5ad3e577bf1a2d76069b3b49b
>>>>>>> parent of d96f079... Added skeleton for Luminance Computation
					try 
					{
						audialSeg.segmentAudio();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 7a533b123d2164c5ad3e577bf1a2d76069b3b49b


				
				
						
=======
>>>>>>> parent of d96f079... Added skeleton for Luminance Computation
<<<<<<< HEAD
=======
>>>>>>> parent of d96f079... Added skeleton for Luminance Computation
=======
>>>>>>> 7a533b123d2164c5ad3e577bf1a2d76069b3b49b
			}
		});
	}
}
