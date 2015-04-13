package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FileFinderFrame extends JFrame
{

	private static final long serialVersionUID = 1L;
	private static String filepath = "";
	JPanel cards = new JPanel(new CardLayout());
	public JButton go_button = new JButton("Classify");
	JLabel fileLabel;

	
	public FileFinderFrame() 
	{
		
		super("Gecko Movie Classifier");	
		cards.setPreferredSize(new Dimension(300, 300));
		add(cards);		
		findfile();
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	/**
	 * Opens up a filechooser for the selection of the Movie to analyze
	 */
	public void findfile() 
	{
		
		JPanel FindFileCard = new JPanel();
		
		FindFileCard.setPreferredSize(new Dimension(300, 300));		
		FindFileCard.setLayout(new BoxLayout(FindFileCard, BoxLayout.Y_AXIS));
		FindFileCard.add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel hi = new JLabel("Hello!");
		hi.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi);	
		
		JLabel hi1 = new JLabel("Welcome to Gecko.");
		hi1.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi1);	
		
		JLabel hi2 = new JLabel("Gecko is a movie classification system.");
		hi2.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi2);	
		
		JLabel hi3 = new JLabel("You give it a movie file, and it gives you its genre.");
		hi3.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi3);	
		
		JLabel hi4 = new JLabel("Classification takes a while,");
		hi4.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi4);	
		
		JLabel hi5 = new JLabel("so sit back, and maybe watch your movie.");
		hi5.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi5);	
		
		JLabel hi6 = new JLabel(":)");
		hi6.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi6);	
		
		
		FindFileCard.add(Box.createRigidArea(new Dimension(0, 10)));
		JButton select_button = new JButton("Select Movie");
		select_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(select_button);
		FindFileCard.add(Box.createRigidArea(new Dimension(0, 10)));

		
		JLabel hi7 = new JLabel("Made with <3 by the Gecko Team");
		hi7.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi7);	
		
		JLabel hi8 = new JLabel("Josh C");
		hi8.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi8);
		
		JLabel hi9 = new JLabel("Josh G");
		hi9.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi9);
		
		JLabel hi10 = new JLabel("Aldrich G");
		hi10.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi10);
		
		JLabel hi11 = new JLabel("Tim R");
		hi11.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi11);
		
		select_button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent evt) {
		    	
		    	    File selectedFile = new File(openVideoFile());
		    	    FileFinderFrame.setFilepath(selectedFile.getAbsolutePath());
		    	    confirm(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.')));
		    	
		    }
		});
		
		cards.add(FindFileCard, "ff");
		CardLayout cl = (CardLayout)(cards.getLayout());
	    cl.show(cards, "ff");
	    


	}
	
	/**
	 * Returns the filepath from the Filedialog chooser
	 * @return filepath
	 */
	public String openVideoFile()
	{
		FileDialog fileDialog = new FileDialog(this, "Choose a video", FileDialog.LOAD);
		fileDialog.setDirectory("C:\\");
		fileDialog.setFile("*.mkv;*.avi;*.mp4");
		fileDialog.setVisible(true);
		String filePath = fileDialog.getDirectory() + 
		           System.getProperty("file.separator") + fileDialog.getFile();
		if (fileDialog.getFile() == null)
		  return "Please choose a file"; //Add proper error checking later
		else
		  return filePath;
	}

	/**
	 * Movie confirmation layout change
	 * @param filename
	 */
	public void confirm(String filename) 
	{
		
		JPanel ConfirmCard = new JPanel();
		
		ConfirmCard.setPreferredSize(new Dimension(300, 300));
		ConfirmCard.setLayout(null);
			
		JLabel picLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/movie_file.png")));
		picLabel.setBounds(60, 25, 180, 180);
		ConfirmCard.add(picLabel);
		
		fileLabel = new JLabel(filename);
		fileLabel.setBounds(25, 210, 250, 30);
		fileLabel.setFont(new Font("Roboto", Font.BOLD, 20));
		fileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ConfirmCard.add(fileLabel);
		
		go_button.setBounds(150, 250, 125, 25);
		ConfirmCard.add(go_button);
		
		JButton cancel_button = new JButton("Cancel");
		cancel_button.setBounds(25, 250, 125, 25);
		cancel_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
			    findfile();
		    }
		});
		ConfirmCard.add(cancel_button);

		
		cards.add(ConfirmCard, "cfm");
		CardLayout cl = (CardLayout)(cards.getLayout());
	    cl.show(cards, "cfm");

		
	}

	public String getFilepath() 
	{
		return filepath;
	}

	public static void setFilepath(String filepath) 
	{
		FileFinderFrame.filepath = filepath;
	}
	
	
	
}