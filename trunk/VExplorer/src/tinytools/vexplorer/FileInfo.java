package tinytools.vexplorer;

import java.io.File;

public class FileInfo implements Comparable<FileInfo>
{
	private File file = null;
	private long lastModified = 0;
	private long filesize = 0;
	private String tokenizedFilename;
	
	public FileInfo(File f)
	{
		this.file = f;
		this.lastModified = f.lastModified();
		this.filesize = f.length();
		this.tokenizedFilename = tokenizeFilename();
	}
	
	public boolean equals(FileInfo otherFileInfo)
	{
		return this.lastModified==otherFileInfo.lastModified && this.filesize==otherFileInfo.filesize;
	}
	
	private String tokenizeFilename()
	{
		String name = file.getName();
		
		//remove file extension
		name = name.substring(0, name.lastIndexOf('.'));
		
		int additionalInformationIndex = name.indexOf(" - ");
		if(additionalInformationIndex>0)
			name = name.substring(0, additionalInformationIndex);
		
		name = name.replace('_', ' ');
		name = name.replace('.', ' ');
		name = name.replaceAll("(?<=[a-z])(?=[A-Z])", " "); //undo camel casing
		
		return name;
	}
	
	public String getTokenizedFilename()
	{
		return tokenizedFilename;
	}
	
	public File getFile()
	{
		return file;
	}

	@Override
	public int compareTo(FileInfo other) 
	{
		return file.getName().compareToIgnoreCase(other.file.getName());
	}
	
	/**
	 * Returns the path relative to the defined root, e.g.:
	 * \Beatsteaks - Living Targets - 2002
	 * \Beatsteaks - Living Targets - 2002\Beatsteaks - Living Targets - 01 - Not Ready To Rock.mp3
	 * @return
	 */
	private String getRelativePath(String absoluteRootPath)
	{
		return file.getAbsolutePath().substring(absoluteRootPath.length());
	}
}
