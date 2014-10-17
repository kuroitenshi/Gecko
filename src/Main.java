import model.FrameExtraction;
import controller.GeckoController;
import view.GUI;


public class Main {

	
	public static void main(String[] args) 
	{
		GUI gui = new GUI();		
		FrameExtraction extractionModel = new FrameExtraction();
		GeckoController mainController = new GeckoController(extractionModel, gui);
		
	}

}
