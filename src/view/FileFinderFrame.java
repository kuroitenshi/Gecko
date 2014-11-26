package view;

import java.awt.CardLayout;
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
		
		BufferedImage buttonIcon;
		try {
			buttonIcon = ImageIO.read(new File("select_movie.png"));
			JButton select_button = new JButton(new ImageIcon(buttonIcon));
			select_button.setBorder(BorderFactory.createEmptyBorder());
			select_button.setContentAreaFilled(false);
			select_button.setBounds(0, 0, 300, 300);
			FindFileCard.add(select_button);
			
			
			
			select_button.addActionListener(new ActionListener()
			{
			    public void actionPerformed(ActionEvent evt) {
			    	
			    	    File selectedFile = new File(openVideoFile());
			    	    FileFinderFrame.setFilepath(selectedFile.getAbsolutePath());
			    	    confirm(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.')));
			    	
			    }
			});
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("movie_file.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
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