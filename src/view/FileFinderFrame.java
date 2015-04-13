package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FileFinderFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private String filepath = "";
	JPanel cards = new JPanel(new CardLayout());
	public JButton go_button = new JButton("Classify");
	JLabel fileLabel;
	private String mode = "";
	
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
		
		JLabel hi3 = new JLabel("You can give it a movie file or give it the feature values.");
		hi3.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi3);	
		
		JLabel hi4 = new JLabel("After that, it gives you its genre.");
		hi4.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi4);	
		
		JLabel hi5 = new JLabel("Classification takes a while,");
		hi4.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi5);	
		
		JLabel hi6 = new JLabel("so sit back, and maybe watch your movie.");
		hi5.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi6);	
		
		JLabel hi7 = new JLabel(":)");
		hi6.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi7);	
		
		FindFileCard.add(Box.createRigidArea(new Dimension(0, 10)));
		JButton select_button = new JButton("Select Movie");
		JButton input_button = new JButton("Select Parent Directory");
		select_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		input_button.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(select_button);
		FindFileCard.add(input_button);
		FindFileCard.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel hi8 = new JLabel("Made with <3 by the Gecko Team");
		hi7.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi8);	
		
		JLabel hi9 = new JLabel("Josh C");
		hi8.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi9);
		
		JLabel hi10 = new JLabel("Josh G");
		hi9.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi10);
		
		JLabel hi11 = new JLabel("Aldrich G");
		hi10.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi11);
		
		JLabel hi12 = new JLabel("Tim R");
		hi11.setAlignmentX(Component.CENTER_ALIGNMENT);
		FindFileCard.add(hi12);
		
		select_button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent evt) 
		    {
		    	File selectedFile = new File(openVideoFile());
		    	filepath = selectedFile.getAbsolutePath();
		    	mode = "MOVIE";
		    	confirm(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.')));
		    }
		});
		
		input_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				File selectedFile = searchDirectory();
				filepath = selectedFile.getAbsolutePath();
				mode = "PATH";
		    	confirm(selectedFile.getAbsolutePath());
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
	
	public File searchDirectory()
	{
		File filepath = null;
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setFileSelectionMode(
		        JFileChooser.FILES_AND_DIRECTORIES);

		int option = fileChooser.showDialog(null,
		        "Select Directory");

		if (option == JFileChooser.APPROVE_OPTION) {
			filepath = fileChooser.getSelectedFile();
		    // if the user accidently click a file, then select the parent directory.
		    if (!filepath.isDirectory()) {
		    	filepath = filepath.getParentFile();
		    }
		    System.out.println("Selected directory for import " + filepath);
		}

	    return filepath;
	}
	
	/**
	 * Movie confirmation layout change
	 * @param filename
	 */
	public void confirm(String filename) 
	{
		System.out.println(filename);
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

	public String getMode()
	{
		return mode;
	}
	
	public String getFilepath() 
	{
		return filepath;
	}

	public void setFilepath(String filepath) 
	{
		this.filepath = filepath;
	}
}