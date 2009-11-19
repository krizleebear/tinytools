package tinytools.vexplorer.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tinytools.vexplorer.FileInfo;

public class MoviePanel extends JPanel
{
	private static final long serialVersionUID = -6015364378179187302L;
	private static LayoutManager borderLayout = new BorderLayout();
	
	private FileInfo video;
	
	public MoviePanel(FileInfo video)
	{
		this.video = video;
		
		this.setLayout(borderLayout);
		this.add(videoIcon, BorderLayout.WEST);
	}
	
	JLabel videoIcon = new JLabel();
	
	public void updateVideo() throws IOException
	{
		File posterFile = null;
		
		// try to render medium sized poster
		if(video.getMediumPicFile()!=null)
		{
			posterFile = new File(video.getMediumPicFile());
			if(!posterFile.exists() && video.getSmallPicFile()!=null)
			{
				posterFile = new File(video.getSmallPicFile());
			}
		}
		
		if(posterFile!=null && posterFile.exists())
		{
			BufferedImage img = ImageIO.read(posterFile);
			ImageIcon icon = new ImageIcon(img);
			videoIcon.setIcon(icon);
			videoIcon.repaint();
		}
	}
	

}
