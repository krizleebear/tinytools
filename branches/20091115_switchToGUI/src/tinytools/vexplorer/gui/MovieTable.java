package tinytools.vexplorer.gui;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tinytools.vexplorer.EventDispatcher;
import tinytools.vexplorer.FileInfo;
import tinytools.vexplorer.IExplorerListener;

public class MovieTable extends JPanel implements IExplorerListener
{
	private static final long serialVersionUID = 3140790568623668335L;
	TableLayout layout = null;
	FileInfo[] videos = null;

	public MovieTable()
	{
		double border = 10;
		double[][] tableSizes = { { 200, 60, TableLayout.FILL, border }, // Columns
				{ border, border } // Rows
		};

		layout = new TableLayout(tableSizes);

		this.setBackground(Color.WHITE);
		this.setLayout(layout);

		EventDispatcher.getInstance().addListener(this);
	}

	Font titleFont = new java.awt.Font("Arial Unicode MS", 0, 72);
	int rowHeight = 280;

	HashMap<FileInfo, Integer> testMap = new HashMap<FileInfo, Integer>();

	private void updateTable()
	{
		Arrays.sort(videos); // sort the videos by name

		for (int i = 0; i < videos.length; i++)
		{
			FileInfo currentVideo = videos[i];
			testMap.put(currentVideo, new Integer(i));

			int rowIndex = i;
			layout.insertRow(rowIndex, rowHeight);



			JPanel moviePanel = new JPanel();
			moviePanel.setBackground(Color.WHITE);
			moviePanel.setLayout(new javax.swing.BoxLayout(moviePanel,
					javax.swing.BoxLayout.X_AXIS));
			this.add(moviePanel, "2, " + rowIndex + ", f, f");

			JLabel videoNameLabel = new JLabel(currentVideo.getDisplayedName());
			videoNameLabel.setFont(titleFont);
			moviePanel.add(videoNameLabel);

			// check next entry to see if it has the same tokenized filename
			// that's our indicator to recognize multipart videos
			int consecutivePartIndex = 1;
			String nextName = getNextName(i);
			while (nextName.equals(currentVideo.getDisplayedName()))
			{
				consecutivePartIndex++;

				moviePanel.add(Box.createRigidArea(new Dimension(5, 0))); // create
				// space
				// between
				// buttons
				JLabel consecutiveLabel = new JLabel("[" + consecutivePartIndex
						+ "]");
				consecutiveLabel.setFont(titleFont);
				moviePanel.add(new JLabel("[" + consecutivePartIndex + "]"));

				i++;
				nextName = getNextName(i);
			}
		}

		this.validate();
		this.getParent().validate();
	}

	private File getNextFile(int currentIndex)
	{
		if (currentIndex < videos.length - 1)
		{
			return videos[currentIndex + 1].getFile();
		}
		return null;
	}

	private String getNextName(int currentIndex)
	{
		if (currentIndex < videos.length - 1)
		{
			return videos[currentIndex + 1].getDisplayedName();
		}
		return "";
	}

	public void updateFile(FileInfo videofile)
	{
		System.out.println("new images for " + videofile.getDisplayedName());

		Integer rowIndex = testMap.get(videofile);
		System.out.println("rowindex for " + videofile.getDisplayedName() + " is "
				+ rowIndex);
		
		if(rowIndex == null)
			return;

		BufferedImage img = null;
		try
		{
			File iconFile = null;
			if (videofile.getMediumPicFile() != null)
			{
				iconFile = new File("./images", videofile.getMediumPicFile());
			}
			if (!iconFile.exists() && videofile.getSmallPicFile()!=null)
			{
				iconFile = new File("./images", videofile.getSmallPicFile());
			}

			if(iconFile.exists())
			{
				img = ImageIO.read(iconFile);
				JLabel icon = new JLabel();
				if (img != null)
					icon.setIcon(new ImageIcon(img));
				this.add(icon, "0, " + rowIndex + ", c, c");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void updateFileList(FileInfo[] filelist)
	{
		this.videos = filelist;
		updateTable();
	}

	public void updateStatus(int itemsDone, int itemsTotal)
	{
		// nothing to do here
	}
}
