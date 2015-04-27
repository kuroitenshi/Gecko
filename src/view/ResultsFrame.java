package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Shot;
import model.Objects.ResultPercentages;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;


public class ResultsFrame extends JFrame {

	private String movieName;
	private String directory;
	private ArrayList<Shot> shotList;
	
	public ResultsFrame(String movieName, ArrayList<Shot> shotList, ResultPercentages results, String directory) {
		
		super("Gecko Movie Classifier");
		this.movieName = movieName;
		this.shotList = shotList;
		this.directory = directory;
		
		
		getContentPane().setBackground(Color.white);
		getContentPane().setPreferredSize(new Dimension(400, 600));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		PieDataset ShotDataset = createShotDataset(results);
		JFreeChart perShotChart = createChart(ShotDataset, getGenre("shots", results));
        ChartPanel ShotChartPanel = new ChartPanel(perShotChart);
        ShotChartPanel.setPreferredSize(new java.awt.Dimension(400, 400));
		
        PieDataset FrameDataset = createFrameDataset(results);
		JFreeChart perFrameChart = createChart(FrameDataset, getGenre("frames", results));
        ChartPanel FrameChartPanel = new ChartPanel(perFrameChart);
        FrameChartPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        
        
        setup(ShotChartPanel, FrameChartPanel);		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void setup(ChartPanel shotChartPanel, ChartPanel frameChartPanel) {
		
		JLabel movieLabel = new JLabel(movieName, SwingConstants.CENTER);
		movieLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
		movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		movieLabel.setMaximumSize(new Dimension(400, 30));
		
		final JPanel cards = new JPanel(new CardLayout());
		cards.setBackground(Color.WHITE);
		JPanel cardshot = new JPanel();
		cardshot.setBackground(Color.WHITE);
		cardshot.add(shotChartPanel);
		cards.add(cardshot, "Shots");
		JPanel cardfram = new JPanel();
		cardfram.setBackground(Color.WHITE);
		cardfram.add(frameChartPanel);
		cards.add(cardfram, "Frames");
		
		ItemListener itemListener = new ItemListener() {
		      public void itemStateChanged(ItemEvent evt) {
		    	    CardLayout cl = (CardLayout)(cards.getLayout());
		    	    String event = (String)evt.getItem();
		    	    if (event.equals("Shots"))
		    	    	cl.show(cards, "Shots");
		    	    else cl.show(cards, "Frames");
		      }
		    };
		
		String comboBoxItems[] = { "Shots", "Frames" };
		JComboBox cb = new JComboBox(comboBoxItems);
		cb.addItemListener(itemListener);
		cb.setMaximumSize(new Dimension(200, 30));

		JButton viewShotsButton = new JButton("View Shots");	
		viewShotsButton.setMaximumSize(new Dimension(200, 30));
		viewShotsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
		getContentPane().add(movieLabel);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
		getContentPane().add(cb);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
		// 120
		getContentPane().add(cards);
		// 400
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
		getContentPane().add(viewShotsButton);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
		// 70

		
		viewShotsButton.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	new ShotFrame("All", shotList, directory);
            }
        });
		
	}
	
	private String getGenre(String type, ResultPercentages results) {
		int maxvalue = 0;
		String genre = "";
		
		int actionPercent;
		int comedyPercent;
		int dramaPercent;
		int horrorPercent;
		int neutralPercent;
		
		if (type.equals("shots")) {
			actionPercent = results.action;
			comedyPercent = results.comedy;
			dramaPercent = results.drama;
			horrorPercent = results.horror;
			neutralPercent = results.neutral;
		} else {
			actionPercent = results.actionframes;
			comedyPercent = results.comedyframes;
			dramaPercent = results.dramaframes;
			horrorPercent = results.horrorframes;
			neutralPercent = 0;
		}
		
		if (actionPercent > maxvalue)
			maxvalue = actionPercent;
		if (horrorPercent > maxvalue)
			maxvalue = horrorPercent;
		if (dramaPercent > maxvalue)
			maxvalue = dramaPercent;
		if (comedyPercent > maxvalue)
			maxvalue = comedyPercent;
		if (neutralPercent > maxvalue)
			maxvalue = neutralPercent;
		
		if (actionPercent == maxvalue)
			genre += "action";
		if (horrorPercent == maxvalue)
			if (genre == "")
				genre += "horror";
			else genre += " & horror";
		if (dramaPercent == maxvalue)
			if (genre == "")
				genre += "drama";
			else genre += " & drama";
		if (comedyPercent == maxvalue)
			if (genre == "")
				genre += "comedy";
			else genre += " & comedy";
		if (neutralPercent == maxvalue)
			if (genre == "")
				genre += "neutral";
			else genre += " & neutral";
		
		return genre;
		
	}

	private  PieDataset createShotDataset(ResultPercentages results) {
        DefaultPieDataset result = new DefaultPieDataset();
        if (results.action != 0)
        	result.setValue("Action", results.action);
        if (results.comedy != 0)
        	result.setValue("Comedy", results.comedy);
        if (results.drama != 0)
        	result.setValue("Drama", results.drama);
        if (results.horror != 0)
        	result.setValue("Horror", results.horror);
        if (results.neutral != 0)
        	result.setValue("Neutral", results.neutral);
        return result;
        
    }
	
	private  PieDataset createFrameDataset(ResultPercentages results) {
        DefaultPieDataset result = new DefaultPieDataset();
        if (results.actionframes != 0)
        	result.setValue("Action", results.actionframes);
        if (results.comedyframes != 0)
        	result.setValue("Comedy", results.comedyframes);
        if (results.dramaframes != 0)
        	result.setValue("Drama", results.dramaframes);
        if (results.horrorframes != 0)
        	result.setValue("Horror", results.horrorframes);
        return result;
        
    }
	
	private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart("classified as " +title,          // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false);

        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setNoDataMessage("No data available");
        plot.setLabelGap(0.02);
        return chart;
      
        
    }
	

}
