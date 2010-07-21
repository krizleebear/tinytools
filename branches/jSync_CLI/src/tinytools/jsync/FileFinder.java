package tinytools.jsync;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class FileFinder {

	private class FileInfo
	{
		File file = null;
		long lastModified = 0;
		long size = 0;
		
		public FileInfo(File f)
		{
			this.file = f;
			this.lastModified = f.lastModified();
			this.size = f.length();
		}
		
		public boolean equals(FileInfo otherFileInfo)
		{
			return this.lastModified==otherFileInfo.lastModified && this.size==otherFileInfo.size;
		}
	}
	
	private File rootDir;
	private String absoluteRootPath = "";
	private int foundFileCount = 0;
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();
	private ChangeSet changes = new ChangeSet();
	private Options options;
	
	public FileFinder(String path, Options options)
	{
		rootDir = new File(path);
		this.options = options;
	}
	
	public void indexFiles() throws IOException
	{
		
		if(!rootDir.isDirectory())
			throw new IOException(rootDir.getAbsolutePath() + " is not a directory.");
		
		absoluteRootPath = rootDir.getAbsolutePath();
		recursiveSearch(rootDir);

		printVerbose("Found "+foundFileCount+" files in "+absoluteRootPath);
	}
	
	private void recursiveSearch(File dir)
	{
		File[] entries = dir.listFiles();
		for(File entry : entries)
		{
			if(entry.isHidden() ) //ignore hidden files
				continue;
			
			foundFileCount++;
			fileIndex.put(getRelativePath(entry), new FileInfo(entry));

			if(entry.isDirectory())
			{
				recursiveSearch(entry);  //recursive search in directories
			}
			
			if(foundFileCount%100==0)
				System.out.print("."); //user feedback (still-alive)
		}
	}
	
	private void printVerbose(String msg)
	{
		if(options.isVerbose())
		{
			System.out.println(msg);
		}
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

	public File getPath()
	{
		return rootDir;
	}
	
	public void compareToSlave(FileFinder slave)
	{
		for(Entry<String, FileInfo> entry : fileIndex.entrySet()) //run over master index
		{
			HashMap<String, FileInfo> slaveIndex = slave.fileIndex;
			String masterKey = entry.getKey();
			FileInfo masterInfo = entry.getValue();
			FileInfo slaveInfo = slaveIndex.get(entry.getKey());
			
			if(slaveIndex.containsKey(entry.getKey())) //file is also existing on slave
			{
				//compare file information
				if(!masterInfo.equals(slaveInfo)) //file was changed on master
				{
					printVerbose("CHANGED: "+masterKey);
					changes.modifiedFiles.add(masterInfo.file);
				}
				else //file is equal on master and slave
				{
					printVerbose("EQUALS: "+masterKey);
					changes.equalFiles.add(masterInfo.file);
				}
			}
			else //file was added on master
			{
				printVerbose("ADDED: "+masterKey);
				changes.addedFiles.add(masterInfo.file);
			}
		}
		
		for(Entry<String, FileInfo> entry : slave.fileIndex.entrySet()) //run over slave index
		{
			String slaveKey = entry.getKey();
			
			if(!fileIndex.containsKey(slaveKey)) //file was deleted on master
			{
				printVerbose("DELETED: "+slaveKey);
				changes.deletedFiles.add(entry.getValue().file);
			}
		}
	}
	
	public ChangeSet getChanges()
	{
		return this.changes;
	}
}
