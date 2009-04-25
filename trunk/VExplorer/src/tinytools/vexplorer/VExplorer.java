package tinytools.vexplorer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class VExplorer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 1)
		{
			System.out.println("Usage: VExplorer <path to video files> [<path to video files>...]");
			System.exit(1);
		}
		
		VExplorer ve = new VExplorer(args);
		ve.start();
	}

	private String[] pathes = null;
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();

	public VExplorer(String[] pathes)
	{
		this.pathes = pathes;
	}
	
	private void start() 
	{
		// scan directories
		for (int i = 0; i < pathes.length; i++)
		{
			FileScanner scanner = new FileScanner(pathes[i], fileIndex);
			try 
			{
				scanner.indexFiles();
			} 
			catch (IOException e) 
			{
				System.err.println("Error scanning path " + pathes[i]);
//				e.printStackTrace();
			}
		}
		
		// run over the collected files
		for(Entry<String, FileInfo> entry : fileIndex.entrySet())
		{
			System.out.println(entry.getValue().tokenizeFilename());
		}
		
	}




}
