package tinytools.vexplorer;

import java.io.File;

public class FileInfo implements Comparable<FileInfo>
{
	private File file = null;
	private long lastModified = 0;
	private long filesize = 0;
	private String displayedName;
	
	public FileInfo(File f)
	{
		this.file = f;
		this.lastModified = f.lastModified();
		this.filesize = f.length();
		displayedName = tokenizeFilename();
		displayedName = normalizeArticle();
	}
	
	public boolean equals(FileInfo otherFileInfo)
	{
		return this.lastModified==otherFileInfo.lastModified && this.filesize==otherFileInfo.filesize;
	}
	
	/**
	 * Replaces common tokenizers (e.g. ._-) to more spaces that are better legible to humans.
	 * @return
	 */
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
	
	private static final String[] articles = {"der", "die", "das", "the", "eine"};
	
	/**
	 * Moves suffixed articles (e.g. ,The) back to the front of the name 
	 * @return
	 */
	private String normalizeArticle()
	{
		int commaIndex = displayedName.lastIndexOf(',');
		if(commaIndex<0)
			return displayedName;
		
		String end = displayedName.substring(commaIndex+1).trim(); //+1 (might be painful - indexbounds!)
		
		for (int i = 0; i < articles.length; i++)
		{
			if(articles[i].equalsIgnoreCase(end))
			{
				displayedName = end + " " + displayedName.substring(0, commaIndex);
			}
		}
		
		return displayedName;
	}
	
	public String getDisplayedName()
	{
		return displayedName;
	}
	
	public File getFile()
	{
		return file;
	}
	
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
