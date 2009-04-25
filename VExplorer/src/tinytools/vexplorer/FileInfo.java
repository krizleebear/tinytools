package tinytools.vexplorer;

import java.io.File;

public class FileInfo
{
	private File file = null;
	private long lastModified = 0;
	private long filesize = 0;
	
	public FileInfo(File f)
	{
		this.file = f;
		this.lastModified = f.lastModified();
		this.filesize = f.length();
	}
	
	public boolean equals(FileInfo otherFileInfo)
	{
		return this.lastModified==otherFileInfo.lastModified && this.filesize==otherFileInfo.filesize;
	}
	
	public String tokenizeFilename()
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
}
