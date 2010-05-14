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
	
	public FileFinder(String path)
	{
		rootDir = new File(path);
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
	
	private void recursiveSearch(File dir)
	{
		File[] entries = dir.listFiles();
		for(File entry : entries)
		{
			if(entry.getName().startsWith(".")) //ignore hidden files
				continue;
			
			if(!entry.isDirectory()) //don`t add directories to found files
			{
				foundFileCount++;
				fileIndex.put(getRelativePath(entry), new FileInfo(entry));
			}
			else
			{
				recursiveSearch(entry);  //recursive search in directories
			}
			
			
			if(foundFileCount%100==0)
				System.out.print("."); //user feedback (still-alive)
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
		for(Entry<String, FileInfo> entry : fileIndex.entrySet()) //Master-Index durchlaufen
		{
			HashMap<String, FileInfo> slaveIndex = slave.fileIndex;
			String masterKey = entry.getKey();
			FileInfo masterInfo = entry.getValue();
			FileInfo slaveInfo = slaveIndex.get(entry.getKey());
			
			if(slaveIndex.containsKey(entry.getKey())) //Datei ist auch beim Slave vorhanden
			{
				//Datei-Infos vergleichen
				if(!masterInfo.equals(slaveInfo)) //Datei wurde auf dem Master geändert
				{
					System.out.println("CHANGED: "+masterKey);
					changes.modifiedFiles.add(masterInfo.file);
				}
				else //Datei ist synchron
				{
					System.out.println("EQUALS: "+masterKey);
					changes.equalFiles.add(masterInfo.file);
				}
			}
			else //Datei ist auf dem Master neu dazugekommen
			{
				System.out.println("ADDED: "+masterKey);
				changes.addedFiles.add(masterInfo.file);
			}
		}
		
		for(Entry<String, FileInfo> entry : slave.fileIndex.entrySet()) //Slave-Index durchlaufen
		{
			String slaveKey = entry.getKey();
			
			if(!fileIndex.containsKey(slaveKey)) //Datei wurde auf dem Master gelöscht
			{
				System.out.println("DELETED: "+slaveKey);
				changes.deletedFiles.add(entry.getValue().file);
			}
		}
	}
	
	public ChangeSet getChanges()
	{
		return this.changes;
	}
}
