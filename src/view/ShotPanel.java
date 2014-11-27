package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Shot;


public class ShotPanel extends JPanel {

	int howManyShots;
	
	public ShotPanel(ArrayList<Shot> shotList) {
		
		howManyShots = shotList.size();
		
		setPreferredSize(new Dimension(650, 500));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JScrollPane scroller = new JScrollPane();
	    scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scroller.setBorder(BorderFactory.createEmptyBorder());
	    scroller.setBackground(new Color(1, 0, 0, 0));
	    scroller.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
	    scroller.getViewport().setBorder(null);
	    scroller.setViewportBorder(null);
	    scroller.setBorder(null);


	    // Shots 
	    // 4 columns
	    
		int rows = (int) Math.ceil((double)howManyShots/4.0);
	    
		JPanel inner = new JPanel();
		inner.setBackground(Color.black);
		inner.setPreferredSize(new Dimension(650, rows*120));
		inner.setLayout(new GridLayout(0, 4));
		
		for (int i = 0; i < howManyShots; i++) {
			BufferedImage img = null;
			model.Frame frame = shotList.get(i).getFrameList().get(0);
			try {
				img = ImageIO.read(new File(frame.getDirectory()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Image dimg = img.getScaledInstance(160, 120, Image.SCALE_SMOOTH);		
			JLabel imgicon = new JLabel(new ImageIcon(toBufferedImage(dimg)));
			imgicon.setPreferredSize(new Dimension(160, 120));
			inner.add(imgicon);
		}
		
	    
	    scroller.getViewport().add(inner);
	    add(scroller);
	    
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
}
