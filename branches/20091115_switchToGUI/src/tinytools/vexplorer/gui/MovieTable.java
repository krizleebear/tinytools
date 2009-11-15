package tinytools.vexplorer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;

import info.clearthought.layout.TableLayout;

import javax.swing.Box;
import javax.swing.JButton;
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
		double[][] tableSizes = 
			{
				{border, 200, TableLayout.FILL, border},  // Columns
				{border, border}   // Rows
			};
		
		layout = new TableLayout(tableSizes);
		
		this.setBackground(Color.WHITE);
		this.setLayout(layout);
		
		//initCells();
		EventDispatcher.getInstance().addListener(this);
	}

	Font titleFont = new java.awt.Font("Arial Unicode MS", 0, 120);
	int rowHeight = 260;
	
	private void updateTable()
	{
		Arrays.sort(videos); // sort the videos by name
		
		for (int i = 0; i < videos.length; i++) 
		{
			FileInfo currentVideo = videos[i];

			int rowIndex = i;
			layout.insertRow(rowIndex, rowHeight);
			
			JPanel moviePanel = new JPanel();
			moviePanel.setBackground(Color.WHITE);
			moviePanel.setLayout(new javax.swing.BoxLayout(moviePanel, javax.swing.BoxLayout.X_AXIS));
			this.add(moviePanel, "2, "+rowIndex+", f, f");

			JLabel videoNameLabel = new JLabel(currentVideo.getDisplayedName());
			videoNameLabel.setFont(titleFont);
			moviePanel.add(videoNameLabel);
			
			//check next entry to see if it has the same tokenized filename
			//that's our indicator to recognize multipart videos
			int consecutivePartIndex = 1;
			String nextName = getNextName(i);
			while(nextName.equals(currentVideo.getDisplayedName()))
			{
				//System.out.print( " [" + ++consecutivePartIndex + "]");
				//sb.append(" <a href=\""+getNextFile(i).toURI().toString()+"\">["+consecutivePartIndex+"]</a>");
				consecutivePartIndex++;

				moviePanel.add(Box.createRigidArea(new Dimension(5,0))); //create space between buttons
				JLabel consecutiveLabel = new JLabel("["+consecutivePartIndex+"]");
				consecutiveLabel.setFont(titleFont);
				moviePanel.add(new JLabel("["+consecutivePartIndex+"]"));
				
				i++;
				nextName = getNextName(i);
			}
		}
		
		this.validate();
		this.getParent().validate();
	}
	
	private File getNextFile(int currentIndex)
	{
		if(currentIndex < videos.length-1)
		{
			return videos[currentIndex+1].getFile();
		}
		return null;
	}
	
	private String getNextName(int currentIndex)
	{
		if(currentIndex < videos.length-1)
		{
			return videos[currentIndex+1].getDisplayedName();
		}
		return "";
	}

	public void updateFile(FileInfo file)
	{
		// TODO Auto-generated method stub
	}
	
	public void updateFileList(FileInfo[] filelist)
	{
		this.videos = filelist;
		updateTable();
	}

	public void updateStatus(int itemsDone, int itemsTotal)
	{
		//nothing to do here
	}
}
