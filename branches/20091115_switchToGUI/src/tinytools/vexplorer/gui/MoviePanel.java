package tinytools.vexplorer.gui;

import java.awt.Color;

import info.clearthought.layout.TableLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import tinytools.vexplorer.EventDispatcher;
import tinytools.vexplorer.FileInfo;
import tinytools.vexplorer.IExplorerListener;

public class MoviePanel extends JPanel implements IExplorerListener
{
	private static final long serialVersionUID = 3140790568623668335L;
	TableLayout layout = null;
	FileInfo[] filelist = null;

	public MoviePanel()
	{
		double border = 10;
		double[][] tableSizes = 
			{
				{border, 200, TableLayout.FILL, border},  // Columns
				{border, border}   // Rows
			};
		
		layout = new TableLayout(tableSizes);
		
		this.setBackground(Color.RED);
		this.setLayout(layout);
		
		//initCells();
		EventDispatcher.getInstance().addListener(this);
	}

	private void updateTable()
	{
		for(int i=0; i<5; i++)
		{
			layout.insertRow(i+1, 40);
			JButton bt = new JButton("Knopf"+i);
			this.add(bt, "1, "+(i+1));
		}
	}

	public void updateFile(FileInfo file)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void updateFileList(FileInfo[] filelist)
	{
		this.filelist = filelist;
		updateTable();
	}

	public void updateStatus(int itemsDone, int itemsTotal)
	{
		//nothing to do here
	}
	
}
