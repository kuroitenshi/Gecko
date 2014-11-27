package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Shot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;


public class ResultsFrame extends JFrame {

	String movieName;
	int actionPercent;
	int comedyPercent;
	int dramaPercent;
	int horrorPercent;
	ArrayList<Shot> shotList;
	
	public ResultsFrame(String movieName, ArrayList<Shot> shotList) {
		
		super("Gecko Movie Classifier");
		this.movieName = movieName;
		this.shotList = shotList;
		
		actionPercent = 2;
		comedyPercent = 3;
		dramaPercent = 5;
		horrorPercent = 4;
		
		getContentPane().setBackground(Color.white);
		getContentPane().setPreferredSize(new Dimension(400, 600));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		PieDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset, "Classification");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));
		
        
        
		setup(chartPanel);		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void setup(ChartPanel chartPanel) {
		
		JLabel movieLabel = new JLabel(movieName);
		movieLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
		movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel classificationLabel = new JLabel("Classified as " + getGenre() + ".");
		classificationLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
		classificationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
		getContentPane().add(movieLabel);
		getContentPane().add(classificationLabel);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));

		getContentPane().add(chartPanel);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));

		
		JButton viewAllShotsBtn = new JButton("View All Shots");
		JButton viewActShotsBtn = new JButton("View Action Shots");
		JButton viewComShotsBtn = new JButton("View Comedy Shots");
		JButton viewDrmShotsBtn = new JButton("View Drama Shots");
		JButton viewHorShotsBtn = new JButton("View Horror Shots");
		
		viewAllShotsBtn.setMaximumSize(new Dimension(200, 30));
		viewActShotsBtn.setMaximumSize(new Dimension(200, 30));
		viewComShotsBtn.setMaximumSize(new Dimension(200, 30));
		viewDrmShotsBtn.setMaximumSize(new Dimension(200, 30));
		viewHorShotsBtn.setMaximumSize(new Dimension(200, 30));
		
		viewAllShotsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		viewActShotsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		viewComShotsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		viewDrmShotsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		viewHorShotsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		getContentPane().add(viewAllShotsBtn);
		getContentPane().add(viewActShotsBtn);
		getContentPane().add(viewComShotsBtn);
		getContentPane().add(viewDrmShotsBtn);
		getContentPane().add(viewHorShotsBtn);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));

		
		viewAllShotsBtn.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	new ShotFrame("All", shotList);
            }
        });
		
		viewActShotsBtn.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
                new ShotFrame("Action", shotList);
            }
        });
		
		viewComShotsBtn.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	new ShotFrame("Comedy", shotList);
            }
        });
		
		viewDrmShotsBtn.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	new ShotFrame("Drama", shotList);
            }
        });
		
		viewHorShotsBtn.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	new ShotFrame("Horror", shotList);
            }
        });
		
	}
	
	private String getGenre() {
		if (actionPercent > comedyPercent && actionPercent > dramaPercent && actionPercent > horrorPercent)
			return "action";
		if (comedyPercent > actionPercent && comedyPercent > dramaPercent && comedyPercent > horrorPercent)
			return "comedy";
		if (dramaPercent > actionPercent && dramaPercent > comedyPercent && dramaPercent > horrorPercent)
			return "drama";
		if (horrorPercent > actionPercent && horrorPercent > comedyPercent && horrorPercent > dramaPercent)
			return "horror";
		else return "neutral";
	}

	private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Action", actionPercent);
        result.setValue("Comedy", comedyPercent);
        result.setValue("Drama", dramaPercent);
        result.setValue("Horror", horrorPercent);
        return result;
        
    }
	
	private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
        
    }
	
}
