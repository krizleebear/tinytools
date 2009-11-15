package tinytools.vexplorer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FileScanner {

	
	
	private File rootDir;
	private String absoluteRootPath = "";
	private int foundFileCount = 0;
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();
	
	public FileScanner(String path, HashMap<String, FileInfo> fileIndex)
	{
		this.rootDir = new File(path);
		this.fileIndex = fileIndex;
	}
	
	public void indexFiles() throws IOException
	{
		
		if(!rootDir.isDirectory())
			throw new IOException(rootDir.getAbsolutePath() + " is not a directory.");
		
		absoluteRootPath = rootDir.getAbsolutePath();
		recursiveSearch(rootDir);

		System.out.println();
		System.out.println("Found "+foundFileCount+" files in "+absoluteRootPath);
	}
	
	/**
	 * Returns the path relative to this FileFinder's root, e.g.:
	 * \Beatsteaks - Living Targets - 2002
	 * \Beatsteaks - Living Targets - 2002\Beatsteaks - Living Targets - 01 - Not Ready To Rock.mp3
	 * @return
	 */
	private String getRelativePath(File f)
	{
		return f.getAbsolutePath().substring(absoluteRootPath.length());
	}
	
	private void recursiveSearch(File dir)
	{
		File[] entries = dir.listFiles();
		for(File entry : entries)
		{
			if(entry.isHidden() || entry.getName().startsWith(".")) //ignore hidden files
				continue;
			
			if(entry.getName().endsWith(".db")) //ignore windows thumbs.db
				continue;
			
			if(!entry.isDirectory()) //don`t add directories to found files
			{
				foundFileCount++;
				fileIndex.put(getRelativePath(entry), new FileInfo(entry));
			}
			else
			{
				//recursiveSearch(entry);  //recursive search in directories
			}
			
			
			if(foundFileCount%100==0)
				System.out.print("."); //user feedback (still-alive)
		}
	}
	
}
